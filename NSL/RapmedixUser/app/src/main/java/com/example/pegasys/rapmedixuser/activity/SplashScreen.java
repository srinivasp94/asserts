package com.example.pegasys.rapmedixuser.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.utils.Common;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private String currentVersion;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getCurrentVersion();

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new DbCheck().execute();
                finish();
            }
        }, SPLASH_TIME_OUT);
*/
    }

    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = packageInfo.versionName;
        Log.i("packagename", "" + currentVersion);
        if (!Common.haveInternet(getApplicationContext())) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    new DbCheck().execute();
                    finish();

                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    new DbCheck().execute();
                    finish();

                }
            }, SPLASH_TIME_OUT);
//            new GetLatestVersion().execute();
        }

    }

    private class DbCheck extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //super.onPostExecute(result);
            if (result != null) {
                if (result.equals("0")) {
                    finish();
                    startActivity(new Intent(SplashScreen.this, FirstActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(SplashScreen.this, Home_page.class));
                }
            } else {

            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            DataBase_Helper dh = new DataBase_Helper(SplashScreen.this);
            int c = dh.getUserCount();
            if (c == 0) {
                return "0";
                /*finish();
                startActivity(new Intent(SplashScreen.this,Registration.class));*/
            } else {
                return "1";
            }

        }

    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {
        private String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {


            try {
                org.jsoup.nodes.Document doc =
                        Jsoup.connect("https://play.google.com/store/apps/details?id=com.tiqs.rapmedix&hl=en")
                                .timeout(8 * 1000).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop", "softwareVersion").first().text();
                System.out.println("? - Latest version of my app from playstore====>>>>" + latestVersion);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Exception1", e.toString());
            }
            return new JSONObject();

            /*try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.tiqs.rapmedix&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

//            return latestVersion;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    showUpdateDialog();

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new DbCheck().execute();
                            finish();
                        }
                    }, SPLASH_TIME_OUT);

                }
            }
            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New update available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.tiqs.rapmedix&hl=en"));


                startActivity(intent);


            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new DbCheck().execute();
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            }
        });


        builder.setCancelable(false);
        dialog = builder.show();
    }
}
