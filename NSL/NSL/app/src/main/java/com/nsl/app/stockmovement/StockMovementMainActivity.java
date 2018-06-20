package com.nsl.app.stockmovement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StockMovementMainActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    String jsonData;
    TextView toolbarTitle;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ProgressDialog progressDialog;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
       // getSupportActionBar().setTitle("Order Indent");

        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/SEGOEWP.TTF");
        toolbarTitle.setTypeface(myTypeface);
        toolbarTitle.setText("Stock Supply");


        fm = getSupportFragmentManager();
        ft = fm.beginTransaction().add(R.id.content_frame, new FragmentStockMovement());
        ft.commit();

        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.Theme_Dark_primary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }


    }


}
