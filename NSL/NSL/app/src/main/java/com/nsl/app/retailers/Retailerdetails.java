package com.nsl.app.retailers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Retailerdetails extends AppCompatActivity {
    ProgressDialog progressDialog;
    TextView empty;
    String jsonData, checkuid;
    private ListView listView;
    TextView tv_name, tv_tin, tv_email, tv_phone, tv_mobile, tv_address;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    Button refresh;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailerdetails);
        tv_name = (TextView) findViewById(R.id.name);
        tv_tin = (TextView) findViewById(R.id.tinno);
        tv_email = (TextView) findViewById(R.id.email);
        tv_phone = (TextView) findViewById(R.id.phone);
        tv_mobile = (TextView) findViewById(R.id.mobile);
        tv_address = (TextView) findViewById(R.id.address);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        Log.e("user id ", checkuid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), AllRetailersActivity.class);
                startActivity(home);
                finish();
            }
        });

        tv_name.setText(getIntent().getStringExtra("retailer_name"));
        tv_tin.setText(getIntent().getStringExtra("retailer_tin_no"));
        tv_email.setText(getIntent().getStringExtra("email_id"));
        tv_phone.setText(getIntent().getStringExtra("phone"));
        tv_mobile.setText(getIntent().getStringExtra("mobile"));
        tv_address.setText(getIntent().getStringExtra("address"));


    }
}