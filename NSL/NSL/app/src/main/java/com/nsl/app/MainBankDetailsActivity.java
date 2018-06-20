package com.nsl.app;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainBankDetailsActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    String jsonData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bankdetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Retailers");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainBankDetailsActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction().add(R.id.content_frame, new FragmentBankDetails());
        ft.commit();
    }
}
