package com.example.list;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class baseList extends SQLiteOpenHelper {
    public baseList(@Nullable Context context) {
        super(context, "list", null, 1);
    }


    public baseList(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "list", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL("DROP TABLE IF EXISTS list" );
db.execSQL("CREATE TABLE list (id integer primary key autoincrement not null, title TEXT, description TEXT, hour TEXT, photo BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
