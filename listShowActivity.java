package com.example.list;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class listShowActivity extends AppCompatActivity {
    List<String> activity=new ArrayList<>();
    List<String> include=new ArrayList<>();
    String title ="";
    activityAdapter adapter;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_show);
        ImageView list_photo = findViewById(R.id.list_photo);
sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        title = sharedPreferences.getString("title", "");
        baseList baseList = new baseList(getBaseContext());
        SQLiteDatabase db = baseList.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from list", null);
while (cursor.moveToNext()){
    System.out.println(cursor.getString(2));
Log.d("tiitle", title);
    if(cursor.getString(1).trim().equals(title.trim())){
    break;
}
}
TextView title1 = findViewById(R.id.title);
title1.setText(title);
byte [] bytes = cursor.getBlob(4);

 Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
 int height = bit.getHeight();
 int weight = bit.getWidth();
 height=height/6;
 weight = weight/6;
 list_photo.setImageBitmap(Bitmap.createScaledBitmap(bit, weight , height, false));
String [] tab = cursor.getString(2).split(" X/X ");
for(String tab1  : tab){
    String [] tab2 = tab1.split(" / ");
    if(tab2.length==2){
        activity.add(tab2[0]);
        include.add(tab2[1]);
    }
    Log.d("X/X", tab1);
}
adapter = new activityAdapter(activity);
RecyclerView recyclerView = findViewById(R.id.list_show_list);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
recyclerView.setAdapter(adapter);
    }
    public class activityAdapter extends RecyclerView.Adapter<activityAdapter.activityAdapterViewHolder>{
        List<String> activity;
        public activityAdapter(List<String> activity){
            this.activity=activity;
        }
        public class activityAdapterViewHolder extends RecyclerView.ViewHolder {
            CheckBox include;
            TextView activity;
            public  activityAdapterViewHolder(View view){
                super(view);
            }
        }
        @NonNull
        @Override
        public activityAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_widget, parent, false);
            activityAdapterViewHolder holder = new activityAdapterViewHolder(view);
            holder.activity=view.findViewById(R.id.activity);
            holder.include=view.findViewById(R.id.include);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final activityAdapterViewHolder holder, final int position) {
holder.include.setChecked(Boolean.valueOf(include.get(position)));
           holder.include.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
include.set(position, String.valueOf(holder.include.isChecked()));
                   baseList baseList1 = new baseList(getBaseContext());
                   SQLiteDatabase db = baseList1.getWritableDatabase();
                   Cursor cursor1 = db.rawQuery("select * from list", null);
                   int i2=0;
                   cursor1.moveToNext();
                   while(!cursor1.getString(1).equals(sharedPreferences.getString("title", ""))){
                       i2++;
                       cursor1.moveToNext();
                   }
                   ContentValues contentValues = new ContentValues();
                   contentValues.put("title",cursor1.getString(1));
                   String this1="";
                   int i1=-1;
                   for (String t :activity){
                       i1++;
                       if(i1==0){
                           this1=this1+activity.get(i1)+" / "+include.get(i1);
                       }else{
                           this1=this1+" X/X "+activity.get(i1)+" / "+include.get(i1);

                       }
                   }
                   contentValues.put("description", this1);
                   contentValues.put("hour", cursor1.getString(3));
                   contentValues.put("photo", cursor1.getBlob(4));
                   db.update("list", contentValues, "id = ?", new String[]{String.valueOf(i2)});
                   db.close();
               }
           });
           holder.activity.setText(activity.get(position).trim());
        }

        @Override
        public int getItemCount() {
            return include.size();
        }


    }
}
