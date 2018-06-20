package com.nsl.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nsl.app.dailydairy.FragmentDailyDairy;

import java.util.ArrayList;
import java.util.HashMap;

public class MainDailyDiaryActivity extends AppCompatActivity
{
    FragmentManager fm;
    FragmentTransaction ft;
    String jsonData;

    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ProgressDialog progressDialog;
    private SQLiteDatabase odb;
    DatabaseHandler db;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_product);
        db = new DatabaseHandler(this);
        //db.deleteDailyDiary();
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Daily Diary");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
                finish();
            }
        });

        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/SEGOEWP.TTF");
        toolbarTitle.setTypeface(myTypeface);
        toolbarTitle.setText("Daily Diary");

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction().add(R.id.content_frame, new FragmentDailyDairy(db));
        ft.commit();

    }

}
