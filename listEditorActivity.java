package com.example.list;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class listEditorActivity extends AppCompatActivity {
    List<String> list1=new ArrayList<>();
    List<String> list2=new ArrayList<>();
    listOfActivities adapter; RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_editor);
//Reading activities from base
TextView title = findViewById(R.id.title);
        final baseList baseList = new baseList(getBaseContext());
        SQLiteDatabase db = baseList.getWritableDatabase();
        final Cursor cursor = db.rawQuery("select * from list", null);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //Set textview on title of list
        title.setText(sharedPreferences.getString("title", ""));
        cursor.moveToNext();
        while(!cursor.getString(1).trim()
                .equals(sharedPreferences.getString("title","").trim())){

            cursor.moveToNext();
        }

        String [] tab = cursor.getString(2).split(" X/X ");
        for (String tab1 :tab){
            String [] tab2 = tab1.split(" / ");
            if(tab2.length==2) {
                list1.add(tab2[0]);
                list2.add(tab2[1]);
            }
        }
      recyclerView = findViewById(R.id.list_of_activities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new listOfActivities(list1);
        recyclerView.setAdapter(adapter);
ImageView back = findViewById(R.id.back);
Button save_action = findViewById(R.id.save_action);
Button save_list = findViewById(R.id.save_list);
//Save new action
save_action.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        EditText action = findViewById(R.id.action);
        list1.add(action.getText().toString());
        list2.add("false");
        listOfActivities adapter1;
        adapter1 = new listOfActivities(list1);
        RecyclerView recyclerView = findViewById(R.id.list_of_activities);
        recyclerView.setAdapter(adapter1);
        action.setText("");

    }
});
//Back to last page
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
//Writing new information in database
save_list.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
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
for (String t :list1){
    i1++;
    if(i1==0){
      this1=this1+list1.get(i1)+" / "+list2.get(i1);
    }else{
        this1=this1+" X/X "+list1.get(i1)+" / "+list2.get(i1);

    }

}
contentValues.put("description", this1);
contentValues.put("hour", cursor1.getString(3));
contentValues.put("photo", cursor1.getBlob(4));
        db.update("list", contentValues, "id = ?", new String[]{String.valueOf(i2)});
        db.close();
        hourActivity();
    }
});
title.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      titleActivity();
    }
});
final ImageView photo = findViewById(R.id.photo);
photo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      photoAdder();
    }
});
    }
    public class listOfActivities extends RecyclerView.Adapter<listOfActivities.listOfCourseViewHolder>{
List<String> list ;
public listOfActivities(List<String> list1){
    this.list=list1;
}
        public class listOfCourseViewHolder extends RecyclerView.ViewHolder{
            ImageView delete;
            TextView activity;
            public listOfCourseViewHolder(View view){
                super(view);
            }
        }
        @NonNull
        @Override
        public listOfActivities.listOfCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_activity_widget, parent, false);
            listOfCourseViewHolder listOfCourseViewHolder = new listOfCourseViewHolder(view);
            listOfCourseViewHolder.activity=view.findViewById(R.id.activity);
            listOfCourseViewHolder.delete=view.findViewById(R.id.remove);
            return listOfCourseViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull listOfCourseViewHolder holder, final int position) {
holder.delete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
list.remove(position);
        System.out.println(list);
list1.clear();
list1=list;
adapter = new listOfActivities(list1);
recyclerView.setAdapter(adapter);
        //adapter.notifyItemRemoved(position);

    }
});
holder.activity.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }
public void hourActivity(){
        Intent intent = new Intent(this, mainPageActivity.class);
        startActivity(intent);
        finish();
}
public void titleActivity(){
        Intent intent = new Intent(this, newList.class);
        startActivity(intent);
}
public void photoAdder(){
        Intent intent = new Intent(this, photo_adder.class);
        startActivity(intent);
        finish();
}
}
