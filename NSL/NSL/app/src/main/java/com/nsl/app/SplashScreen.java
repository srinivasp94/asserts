package com.nsl.app;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.nsl.app.commonutils.Common;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    String userrId,jsonData;
    DatabaseHandler db;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ProgressDialog progressDialog;
    String fcm_id;
    private String currentVersion;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userrId           = sharedpreferences.getString("userId", "");
        SharedPreferences.Editor editor = sharedpreferences.edit();

        fcm_id = FirebaseInstanceId.getInstance().getToken();
        editor.putString("fcm_id", fcm_id);
        editor.commit();

        ImageView splashText = (ImageView)findViewById(R.id.splashtext);

        if (Build.VERSION.SDK_INT >= 21) {
           //getWindow().setNavigationBarColor(getResources().getColor(R.color.Theme_Dark_primary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        db = new DatabaseHandler(this);
        db.getWritableDatabase();


        getCurrentVersion();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        }, SPLASH_TIME_OUT);

    }

    private void getCurrentVersion()
    {


        PackageManager pm = this.getPackageManager();

        PackageInfo pInfo = null;

        try
        {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        }
        catch (PackageManager.NameNotFoundException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        currentVersion = pInfo.versionName;

        System.out.println("? - My local app version====>>>>>" + currentVersion);

//        Common.customToast(this,"currentVersion : "+currentVersion);






        if (!Common.haveInternet(getApplicationContext()))
        {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                }
            }, SPLASH_TIME_OUT);




        }
        else
        {

//            setContentView(R.layout.splash);


//           ==========================================================
//            new GetLatestVersion().execute();



        }





    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject>
    {


        private String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                org.jsoup.nodes.Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.nsl.beejtantra").timeout(30*1000).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();
                System.out.println("? - Latest version of my app from playstore====>>>>"+latestVersion);
            }catch (Exception e){
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {
            if (latestVersion != null)
            {

                if (!currentVersion.equalsIgnoreCase(latestVersion))
                {
                Common.Log.i("Common.versionCompare(currentVersion,latestVersion) : "+Common.versionCompare(currentVersion,latestVersion));

               // if (Common.versionCompare(currentVersion,latestVersion) < 0)

                    showUpdateDialog();
                }
                else
                {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            // Start your app main activity
                            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(i);
                            finish();

                        }
                    }, SPLASH_TIME_OUT);


                }

            }
            super.onPostExecute(jsonObject);


        }
    }




    private void showUpdateDialog()
    {

        //Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS,false);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Common.getStringResourceText(R.string.a_new_update_is_available));
        builder.setPositiveButton(Common.getStringResourceText(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nsl.beejtantra"));


                startActivity(intent);




            }
        });



        builder.setCancelable(false);
        dialog = builder.show();
    }

   private void Popup() {
       Dialog dialog1 = new Dialog(SplashScreen.this);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.popup);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.CENTER | Gravity.CENTER;
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }


}
