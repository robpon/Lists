package com.example.list;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.text.Format;

public class newList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
setContentView(R.layout.list_add);
        Button save = findViewById(R.id.save);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (sharedPreferences.getString("edit", "false").equals("false")) {

                    EditText editText1 = findViewById(R.id.title);

                        if (!editText1.getText().toString().trim().equals("")) {
                            baseList baseList = new baseList(getBaseContext());

                            SQLiteDatabase db = baseList.getWritableDatabase();
                            Cursor cursor = db.rawQuery("select * from list", null);
                            int i = -1;
                            while (cursor.moveToNext()) {
                                i++;
                            }

                            ContentValues contentValues = new ContentValues();
                            EditText editText = findViewById(R.id.title);
                            contentValues.put("id", i + 1);
                            contentValues.put("title", editText.getText().toString());
                            contentValues.put("description", "");
                            contentValues.put("hour", "");
                            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources()
                                    , R.drawable.icon1);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            contentValues.put("photo", bytes);
                            db.insert("list", null, contentValues);
                            db.close();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("title", editText.getText().toString());
                            editor.commit();
                            listEditorActivity();
                        }

                } else {
                    final baseList baseList1 = new baseList(getBaseContext());
                    SQLiteDatabase db1 = baseList1.getWritableDatabase();
                    Cursor cursor1 = db1.rawQuery("select * from list", null);
                    EditText editText1 = findViewById(R.id.title);
                    boolean bol1=false;
                    while (cursor1.moveToNext()) {
                        if (cursor1.getString(1).trim()
                                .equals(editText1.getText().toString().trim())) {
                            bol1=true;
                        }
                    }
                    if (bol1==false){
                        final baseList baseList = new baseList(getBaseContext());
                        SQLiteDatabase db = baseList.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from list", null);
                        while (cursor.moveToNext()) {
                            if (cursor.getString(1).trim()
                                    .equals(sharedPreferences.getString("title", "").trim())) {
                                break;
                            }
                        }
                        EditText editText = findViewById(R.id.title);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("title", editText.getText().toString());
                        contentValues.put("description", cursor.getString(2));
                        contentValues.put("hour", cursor.getString(3));
                        contentValues.put("photo", cursor.getBlob(4));
                        db.update("list", contentValues, "id = ?",
                                new String[]{String.valueOf(cursor.getInt(0))});
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("title", editText.getText().toString());
                        editor.commit();
                    }
                }
            }
        });
    }
    public void listEditorActivity(){
        Intent intent = new Intent(this, listEditorActivity.class);
        startActivity(intent);
    }
}
