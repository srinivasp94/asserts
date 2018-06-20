package com.pagasys.welcome.pegasysattendence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FlashScreenActivity extends AppCompatActivity {

    SharedPreferences mPreferences;
   /* SharedPreferences.Editor mEditor;
    private boolean isLogin;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Thread t = new Thread() {
            public void run() {

                try {

                    sleep(3000);
                    finish();
                    mPreferences = getSharedPreferences("LoginStatusPref", MODE_PRIVATE);
                    boolean hasLoggedIn = mPreferences.getBoolean("Status", false);



                    if (hasLoggedIn) {
                        Intent cv = new Intent(FlashScreenActivity.this, MainActivity.class/*otherclass*/);
                        String name=mPreferences.getString("mNAME","");
                        String role=mPreferences.getString("mROLE","");
                        String bioid=mPreferences.getString("mBIOMETRIC_ID","");
                        startActivity(cv);
                        finish();
                    } else {
                        Intent intent = new Intent(FlashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }
}
