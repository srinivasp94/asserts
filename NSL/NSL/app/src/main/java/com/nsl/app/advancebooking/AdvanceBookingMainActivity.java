package com.nsl.app.advancebooking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;
import com.nsl.app.ServiceOrderDetailMaster;
import com.nsl.app.ServiceOrderMaster;
import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;

public class AdvanceBookingMainActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    String jsonData,checkdivisions;
    TextView toolbarTitle;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw;
    private String orderidfromserviceorder;
    String userId;
    private int role;
    private String team;
    private StringBuilder sb=new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId=sharedpreferences.getString("userId", "");
        role      = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        team      = sharedpreferences.getString("team", "");

        checkdivisions    = sharedpreferences.getString("SOSYNC", "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //getSupportActionBar().setTitle("Advance Booking");
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        /*Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/SEGOEWP-SEMIBOLD.TTF");
        toolbarTitle.setTypeface(myTypeface);*/
        toolbarTitle.setText("Advance Booking");

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction().add(R.id.content_frame, new FragmentAdvanceboking());
        ft.commit();

        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.Theme_Dark_primary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }


       /* if(Common.haveInternet(this)){
            progressDialog = new ProgressDialog(AdvanceBookingMainActivity.this);
            progressDialog.setMessage("Please Wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Async_getNewOrUpdateserviceorders().execute();
        }*/

    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {



                try {
                    ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                    if (netInfo != null) {  // connected to the internet
                        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            // connected to wifi
                            // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                            new Async_getNewOrUpdateserviceorders().execute();

                        } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            // connected to the mobile provider's data plan
                            new Async_getNewOrUpdateserviceorders().execute();
                        }
                    } else {

                    }

                } catch (Exception e) {
                    Log.d("Tag", e.toString());
                }




            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/










}
