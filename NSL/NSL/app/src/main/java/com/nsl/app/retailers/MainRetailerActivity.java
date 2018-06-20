package com.nsl.app.retailers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nsl.app.MainActivity;
import com.nsl.app.R;

public class MainRetailerActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    String jsonData;
    MainRetailerActivity instance;
    private String company_id;
    private String division_id;
    private String crop_id;
    private String customer_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_retailer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Retailers");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainRetailerActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        fm = getSupportFragmentManager();
        company_id = getIntent().getStringExtra("company_id");
        division_id = getIntent().getStringExtra("division_id");
        crop_id = getIntent().getStringExtra("crop_id");
        customer_id = getIntent().getStringExtra("customer_id");

        FragmentRetailer fragmentRetailer = new FragmentRetailer();
        Bundle bundle = new Bundle();
        bundle.putString("id", crop_id);
        bundle.putString("company_id", company_id);
        bundle.putString("division_id", division_id);
        bundle.putString("customer_id", customer_id);
        fragmentRetailer.setArguments(bundle);

        ft = fm.beginTransaction().add(R.id.content_frame, fragmentRetailer);
        ft.commit();
    }


}
