package com.nsl.app.marketintelligence;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;

import java.util.ArrayList;
import java.util.HashMap;
public class MainCropShiftActivity extends AppCompatActivity {

        FragmentManager fm;
        FragmentTransaction ft;
        String jsonData;

        ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
        SharedPreferences sharedpreferences;
        public static final String mypreference = "mypref";
        ProgressDialog progressDialog;
        private SQLiteDatabase odb;
        DatabaseHandler db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_crop_shifts);
            db = new DatabaseHandler(this);
            sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Crop Shifts");
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newfedback = new Intent(getApplicationContext(),MarketIntelligenceAcivity.class);
                    startActivity(newfedback);
                    finish();
                }
            });
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction().add(R.id.content_frame, new FragmentCropShifts(db));
            ft.commit();

        }
    }
