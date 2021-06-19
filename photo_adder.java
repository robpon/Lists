package com.example.list;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.prefs.PreferenceChangeEvent;

public class photo_adder extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);
        Button add_photo1 = findViewById(R.id.add_photo);
        add_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri image = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
            ByteArrayOutputStream bitmapFactory = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bitmapFactory);
byte [] byt = bitmapFactory.toByteArray();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            final baseList baseList = new baseList(getBaseContext());
            SQLiteDatabase db = baseList.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from list", null);
            while (cursor.moveToNext()) {
                if (cursor.getString(1).trim()
                        .equals(sharedPreferences.getString("title", "").trim())) {
                    break;
                }
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", cursor.getString(1));
            contentValues.put("description", cursor.getString(2));
            contentValues.put("hour", cursor.getString(3));
            contentValues.put("photo", byt);
            db.update("list", contentValues, "id = ?",
                    new String[]{String.valueOf(cursor.getInt(0))});
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

listEditorActivity();
    }
public void listEditorActivity(){
        Intent intent = new Intent(this, listEditorActivity.class);
        startActivity(intent);
        finish();
}
}
