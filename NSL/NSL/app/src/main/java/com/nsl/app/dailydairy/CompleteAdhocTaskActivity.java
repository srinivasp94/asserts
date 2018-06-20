package com.nsl.app.dailydairy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;
import com.nsl.app.Utility;
import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_DD_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_DD_ID;
import static com.nsl.app.DatabaseHandler.KEY_DD_STATUS;
import static com.nsl.app.DatabaseHandler.TABLE_DAILYDAIRY;

public class CompleteAdhocTaskActivity extends AppCompatActivity {
    EditText title, time, comments;
    private String[] time_slots = {"Select time_slot", " 9AM - 11AM", " 11AM - 1PM", "2PM - 4PM", "4PM - 6PM"};
    Button submit;
    int flag = 0;
    TextView tv_time;
    String existinguserid, existingffmid, timeslot;
    String sqliteid, ffmid;
    String checkuid, jsonData;
    private static SQLiteDatabase sql, sdbr, sdbw;
    SharedPreferences sharedpreferences;
    ProgressDialog progressDialog;
    public static final String mypreference = "mypref";
    DatabaseHandler db;
    private HashMap<String, String> data;
    private String team;
    private int role;
    String task_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhoc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        db = new DatabaseHandler(CompleteAdhocTaskActivity.this);


        title = (EditText) findViewById(R.id.et_title);
        time = (EditText) findViewById(R.id.et_time);
        comments = (EditText) findViewById(R.id.comments);
        submit = (Button) findViewById(R.id.submit);
        tv_time = (TextView) findViewById(R.id.time);

        sharedpreferences = CompleteAdhocTaskActivity.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

        if (role==Constants.Roles.ROLE_5 || role==Constants.Roles.ROLE_6){
            submit.setVisibility(View.GONE);

        }else {
            submit.setVisibility(View.VISIBLE);
        }

        data = (HashMap<String, String>) getIntent().getSerializableExtra("data");
        task_status = getIntent().getStringExtra("taskstatus");

        Log.d("Reading: ", "Reading all daily..");
        title.setEnabled(false);
        comments.setEnabled(false);
        time.setEnabled(false);

        sqliteid = data.get("id");
        data.get("userid");
        ffmid = data.get("ffmid");
        task_status=data.get("status");
        title.setText(data.get("title"));
        comments.setText(data.get("comments"));
        time.setText(data.get("time"));
        Log.d("Reading: ", task_status);


        if(task_status.equals("1")){
            submit.setText("Complete Task");
        }
        else if(task_status.equals("2")){
            submit.setText("Task Completed");
            submit.setEnabled(false);
        }
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sdbw = db.getWritableDatabase();
                // updateFeedback(Feedback feedback);
                String updatequery = "UPDATE " + TABLE_DAILYDAIRY + " SET " + KEY_DD_STATUS + " = 2" + " WHERE " + KEY_DD_ID + " = '" + sqliteid + "'";
                sdbw.execSQL(updatequery);
                System.out.println(updatequery);

                if (Common.haveInternet(CompleteAdhocTaskActivity.this)) {
                    new Async_UpdateStatus().execute();
                } else {
                    setResult(Activity.RESULT_OK);
                    finish();
                }


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(getApplicationContext(), DailyDairyActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class Async_updateDailyDairy extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CompleteAdhocTaskActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            // progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormEncodingBuilder()
                        .add("user_id", params[0])
                        .add("title", params[1])
                        .add("note", params[2])
                        .add("dairy_date", params[3])
                        .add("time_slot", params[4])
                        .add("id", params[5])
                        .add("dailydairy_id", params[6])
//                        .add("created_date",params[6])

                        .build();

                Response responses = null;
                System.out.println("---- dairy data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_UPDATING_DAILYDAIRY)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();
                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 updatediary" + jsonData);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        //  Toast.makeText(getActivity(), "Feed back inserted sucessfully", Toast.LENGTH_SHORT).show();


                        List<DailyDairy> dailydairy = db.getAllnullDailyDairy(checkuid);

                        for (DailyDairy fb : dailydairy) {
                            String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_comments() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate();

                            Log.e("Dairy before : ", log);
                        }

                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private class Async_UpdateStatus extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CompleteAdhocTaskActivity.this);
            progressDialog.setMessage("Updating Approval Status");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                RequestBody formBody = new FormEncodingBuilder()
                        .add("table", "dailydairy")
                        .add("primary_key", "dailydairy_id")
                        .add("primary_value", ffmid)
                        .add("sql_id", sqliteid)
                        .add("status", "2")
                        .build();

                Request request = new Request.Builder()
                        .url(Constants.URL_TABLE_UPDATE)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 approve" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        setResult(Activity.RESULT_OK);
                        finish();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            /*Intent checkinintent = new Intent(getApplicationContext(),PlanerOneActivity.class);
            startActivity(checkinintent);*/


        }
    }
}

