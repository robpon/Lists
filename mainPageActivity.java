package com.example.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

public class mainPageActivity extends AppCompatActivity {
listOfActivities adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("edit", "false");
                editor.commit();
                newList();
            }
        });
        baseList baseList = new baseList(getBaseContext());
        SQLiteDatabase db =baseList.getWritableDatabase();
        List <String> list1 = new ArrayList<>();
        System.out.println("d");
        Cursor cursor = db.rawQuery("select * from list", null);
        while (cursor.moveToNext()){
            list1.add(cursor.getString(1));
            System.out.println(cursor.getString(1));
        }
        System.out.println(list1.size());
adapter = new listOfActivities(list1);
        RecyclerView recyclerView = findViewById(R.id.activities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    public class listOfActivities extends RecyclerView.Adapter<listOfActivities.listOfCourseViewHolder>{
        List<String> list ;
        public listOfActivities(List<String> list1){
            this.list=list1;
        }
        public class listOfCourseViewHolder extends RecyclerView.ViewHolder{

            TextView activity;
            ImageView edit;
            public listOfCourseViewHolder(View view){
                super(view);
            }
        }
        @NonNull
        @Override
        public listOfActivities.listOfCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_widget, parent, false);
            listOfCourseViewHolder listOfCourseViewHolder = new listOfCourseViewHolder(view);
            listOfCourseViewHolder.activity=view.findViewById(R.id.textView);
            listOfCourseViewHolder.edit = view.findViewById(R.id.edit);
            return listOfCourseViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull listOfCourseViewHolder holder, final int position) {
            holder.activity.setText(list.get(position));
            holder.activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("title", list.get(position));
                    editor.commit();
                    listShowActivity();
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("edit", "true");
                    editor.putString("title", list.get(position));

                    editor.commit();
                    editing();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }
    public void newList(){
        Intent intent = new Intent(this, newList.class);
        startActivity(intent);
    }

    public void listShowActivity(){
        Intent intent = new Intent(this, listShowActivity.class);
        startActivity(intent);
    }
    public void editing(){
        Intent intent = new Intent(this, listEditorActivity.class);
        startActivity(intent);
    }
}
