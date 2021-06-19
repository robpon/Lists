package com.example.list;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class hourActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);
        final SeekBar minutes = findViewById(R.id.minute_seekBar);
        final TextView minute_value = findViewById(R.id.minute_value);
final SeekBar hours = findViewById(R.id.hour_seekBar);
final TextView hour_value = findViewById(R.id.hour_value);
        baseList baseList = new baseList(getBaseContext());
        SQLiteDatabase db = baseList.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from list", null);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        while(cursor.moveToNext()){
            if(cursor.getString(1).trim()
                    .equals(sharedPreferences.getString("title","").trim())){
                break;
            }
        }
        String [] tab = cursor.getString(3).split(" ");
        if(tab.length==2){
            minutes.setProgress(Integer.valueOf(tab[1]));
            hours.setProgress(Integer.valueOf(tab[0]));
        }
        minutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minute_value.setText(String.valueOf(minutes.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        hours.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hour_value.setText(String.valueOf(hours.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button save  = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseList baseList = new baseList(getBaseContext());
                SQLiteDatabase db = baseList.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from list", null);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                while(cursor.moveToNext()){
                    if(cursor.getString(1).trim()
                            .equals(sharedPreferences.getString("title","").trim())){
                        break;
                    }
                }
                ContentValues values = new ContentValues();
                values.put("title", cursor.getString(1));
                values.put("description", cursor.getString(2));
                values.put("hour", hour_value+" "+minute_value);
                values.put("photo", cursor.getBlob(4));
                db.update("list", values, "id=?", new  String[]{String.valueOf(cursor.getInt(0))});
                db.close();
                mainPage();
            }
        });
    }
    public void mainPage(){
        Intent intent = new Intent(this, mainPageActivity.class);
        startActivity(intent);
        finish();
    }
}
