package com.nsl.app;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.commonutils.Common;
import com.nsl.app.dailydairy.DailyDairy;
import com.nsl.app.dailydairy.DailyDiaryHistoryActivity;
import com.nsl.app.dailydairy.HistoryActivity;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_DD_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_DD_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_STATUS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_ID;
import static com.nsl.app.DatabaseHandler.TABLE_DAILYDAIRY;
import static com.nsl.app.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanerDetailActivity extends AppCompatActivity {

    String jsonData, userId, ffmid, sqliteid;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_date, tv_address, tv_name, tv_purpose, tv_time;
    Toolbar toolbar;
    String event_id, dateString, eventdate, currentdate;
    String  usertime = " 9AM - 11AM";;
    Button btn_taskcompleted;
    final Context context = this;
    DatabaseHandler db;
    SimpleDateFormat sdf;
    private static SQLiteDatabase sql, sdbr, sdbw;
    private double latitude;
    private double longitude;
    private ProgressDialog progressDialog;
    int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_detail);


        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        db = new DatabaseHandler(getApplicationContext());
        sdf = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        userId = sharedpreferences.getString("userId", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_appointmenttime);
        tv_purpose = (TextView) findViewById(R.id.tv_purpose);
        tv_address = (TextView) findViewById(R.id.tv_address);
        btn_taskcompleted = (Button) findViewById(R.id.btn_taskcompleted);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        currentdate = df.format(c.getTime());


        tv_date.setText(getIntent().getStringExtra("date"));
        tv_name.setText(getIntent().getStringExtra("name"));
        String evtime = getIntent().getStringExtra("event_datetime");
        //loginToken.substring(1, loginToken.length() - 1)
        event_id = getIntent().getStringExtra("event_id");
        final String timein24Format = evtime.substring(11, evtime.length() - 3);
        tv_time.setText("Appointment time : " + evtime.substring(11, evtime.length() - 3));
        final String selecteddate = getIntent().getStringExtra("date");
        System.out.println("Currrent Date Time : " + currentdate + "Selected date :" + selecteddate);
        try {
            if (sdf.parse(selecteddate).after(sdf.parse(currentdate))) {
                System.out.println("Currrent Date Time : YES");
                btn_taskcompleted.setClickable(false);
                btn_taskcompleted.setEnabled(false);
            } else {

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (getIntent().getStringExtra("event_status").equalsIgnoreCase("4")) {
            btn_taskcompleted.setBackgroundResource(R.drawable.btn_green);
            btn_taskcompleted.setClickable(false);
            btn_taskcompleted.setEnabled(false);
            btn_taskcompleted.setText(" Completed ");
        }
        if (role == Constants.Roles.ROLE_7) {

            btn_taskcompleted.setVisibility(View.VISIBLE);
        } else if (role != Constants.Roles.ROLE_7) {
            btn_taskcompleted.setVisibility(View.GONE);

        }
        btn_taskcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Currrent Date Time : Clicked..");

                try {
                    String[] checkinStatus = db.getCheckinStatus(currentdate);
                    if (sdf.parse(selecteddate).equals(sdf.parse(currentdate))) {
                        if (checkinStatus == null) {
                            Toast.makeText(PlanerDetailActivity.this, "Please checkin...", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (gpsService.canGetLocation) {
                    latitude = gpsService.getLatitude();
                    longitude = gpsService.getLongitude();
                    String checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude);
                    Log.d("checkinlatlong11  ", checkinlatlong);

                }

               /* if(!gpsService.canGetLocation) {
                        gpsService.showSettingsAlert(PlanerDetailActivity.this);

                    return;
                }*/


                String userdate = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT).format(new Date());
                sdbw = db.getWritableDatabase();
                // updateFeedback(Feedback feedback);
                String updatequery = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_STATUS + " = " + 4 + " WHERE " + KEY_EMP_VISIT_ID + " = " + event_id;
                sdbw.execSQL(updatequery);
                btn_taskcompleted.setBackgroundResource(R.drawable.btn_green);
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_alert_insert_dailydairy);


                // set the custom dialog components - text, image and button
                final EditText et_title = (EditText) dialog.findViewById(R.id.et_title);
                final EditText et_remark = (EditText) dialog.findViewById(R.id.et_remark);
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String str_title = et_title.getText().toString();
                        String str_remark = et_remark.getText().toString();

                        if (TextUtils.isEmpty(str_title) || str_title.length() > 0 && str_title.startsWith(" ")) {
                            et_title.setError("Please enter title");
                            return;
                        }
                        if (TextUtils.isEmpty(str_remark) || str_remark.length() > 0 && str_remark.startsWith(" ")) {
                            et_remark.setError("Please enter remark");
                            return;
                        } else {
                            Calendar cal = Calendar.getInstance(); //Create Calendar-Object
                            cal.setTime(new Date());               //Set the Calendar to now
                            int hour = cal.get(Calendar.HOUR_OF_DAY); //Get the hour from the calendar
                            if (hour < 11 && hour >= 9)              // Check if hour is between 9 am and 11pm
                            {
                                // 9AM - 11AM", " 11AM - 1PM", "2PM - 4PM", "4PM - 6PM
                                usertime = " 9AM - 11AM";
                            } else if (hour < 13 && hour >= 11)              // Check if hour is between 11 am and 1pm
                            {
                                // 9AM - 11AM", " 11AM - 1PM", "2PM - 4PM", "4PM - 6PM
                                usertime = " 11AM - 1PM";
                            } else if (hour < 16 && hour >= 14)              // Check if hour is between 2 pm and 4pm
                            {
                                // 9AM - 11AM", " 11AM - 1PM", "2PM - 4PM", "4PM - 6PM
                                usertime = " 2PM - 4PM";
                            } else if (hour < 18 && hour >= 16)              // Check if hour is between 4 pm and 6pm
                            {
                                // 9AM - 11AM", " 11AM - 1PM", "2PM - 4PM", "4PM - 6PM
                                usertime = " 4PM - 6PM";
                            }

                            db.addDailyDairy(new DailyDairy(str_title, Integer.parseInt(userId), str_remark, usertime, currentdate, null, null, 1, null, 1));

                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Common.dismissDialog(dialog);


                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.custom_alert);


                            // set the custom dialog components - text, image and button
                            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
                            text.setText("" + " Status updated sucessfully");
                            ImageView sucessimage = (ImageView) dialog.findViewById(R.id.iv_sucess);
                            sucessimage.setVisibility(View.VISIBLE);
                            //
                            // ImageView failureimage = (ImageView) dialog.findViewById(R.id.iv_failure);
                            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                            // if button is clicked, close the custom dialog
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Common.dismissDialog(dialog);

                                    if (Common.haveInternet(PlanerDetailActivity.this)) {
                                        insertToService();
                                    } else {
                                        Intent planerone = new Intent(getApplicationContext(), PlanerOneActivity.class);
                                        planerone.putExtra("i", "0");
                                        startActivity(planerone);
                                        finish();
                                    }


                                }
                            });


                            dialog.show();


                        }


                    }
                });


                dialog.show();
            }
        });

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });


        if (getIntent().getStringExtra("event_purpose").equalsIgnoreCase("7")) {
            tv_purpose.setText("Purpose of Visit : POG");
        } else if (getIntent().getStringExtra("event_purpose").equalsIgnoreCase("3")) {
            tv_purpose.setText("Purpose of Visit : Feedback ");
        } else if (getIntent().getStringExtra("event_purpose").equalsIgnoreCase("2")) {
            tv_purpose.setText("Purpose of Visit : Complaint ");
        } else if (getIntent().getStringExtra("event_purpose").equalsIgnoreCase("1")) {
            tv_purpose.setText("Purpose of Visit : Payment Collection ");
        } else if (getIntent().getStringExtra("event_purpose").equalsIgnoreCase("6")) {
            tv_purpose.setText("Purpose of Visit : ABS ");
        } else if (getIntent().getStringExtra("event_purpose").equalsIgnoreCase("5")) {
            tv_purpose.setText("Purpose of Visit : Sales order ");
        } else if (getIntent().getStringExtra("event_purpose").equalsIgnoreCase("4")) {
            tv_purpose.setText("Purpose of Visit : Stock Information ");
        } else {
            tv_purpose.setText("Purpose of Visit :" + getIntent().getStringExtra("event_purpose"));
        }
        if (getIntent().getStringExtra("address").equalsIgnoreCase("") || getIntent().getStringExtra("address").equalsIgnoreCase("null")) {
            tv_address.setText("Address Not Available");
        } else {
            tv_address.setText(getIntent().getStringExtra("address"));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menu.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(menu);
    }

    private void insertToService() {

        db = new DatabaseHandler(PlanerDetailActivity.this);
        Log.d("Reading: ", "Reading all daily..");


        List<DailyDairy> dailydairy = db.getAllnullDailyDairy(userId);

        Log.e("list size", String.valueOf(dailydairy.size()));

        if (dailydairy.size() == 0) {
            Log.e("no data found", "no data");
        } else {
            for (DailyDairy fb : dailydairy) {
                String log = "Id: " + fb.getID() + ",Userid: " + userId + " : " + fb.get_title() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate() + " : " + fb.get_ffmid();

                Log.e("Dairy before : ", log);
                try {
                    if (Utility.isNetworkAvailable(PlanerDetailActivity.this, Constants.isShowNetworkToast)) {
                        new Async_InsertDailyDairy().execute(String.valueOf(fb.getID()), fb.get_title(), userId, fb.get_comments(), fb.get_time(), fb.get_date(), fb.get_createddate());
                    } else {


                        Log.e("No internet", "no internet");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlanerDetailActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("Added");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Status added to Daily Dairy")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        Intent adv = new Intent(getApplicationContext(), DailyDiaryHistoryActivity.class);

                                        startActivity(adv);
                                        finish();

                                    }
                                });


                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                    }

                } catch (Exception e) {
                    Log.d("Tag", e.toString());
                }

                //  }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlanerDetailActivity.this);

                // set title
                alertDialogBuilder.setTitle("Added");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Planner Updated to Daily Dairy!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                Intent adv = new Intent(getApplicationContext(), DailyDiaryHistoryActivity.class);
                                startActivity(adv);
                                finish();

                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

        }


    }


    private class Async_InsertDailyDairy extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanerDetailActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormEncodingBuilder()
                        .add("id", params[0])
                        .add("title", params[1])
                        .add("user_id", params[2])
                        .add("note", params[3])
                        .add("time_slot", params[4])
                        .add("dairy_date", params[5])
                        .add("tentative_time", "")
                        .add("type", "1")

//                        .add("created_date",params[6])

                        .build();

                Response responses = null;
                System.out.println("---- dairy data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[5]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_DAILYDAIRY)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();
                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 Insertdairy" + jsonData);

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
                        sqliteid = jsonobject.getString("sqlite");
                        ffmid = jsonobject.getString("ffm_id");

                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", sqliteid);
                        Log.e("ffmid", ffmid);
                        sdbw = db.getWritableDatabase();
                        // updateFeedback(Feedback feedback);
                        String updatequery = "UPDATE " + TABLE_DAILYDAIRY + " SET " + KEY_DD_FFMID + " = " + ffmid + " WHERE " + KEY_DD_ID + " = '" + 0 + "'";
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);

                        List<DailyDairy> dailydairy = db.getAllDailyDairy(userId);

                        for (DailyDairy fb : dailydairy) {
                            String log = "Id: " + fb.getID() + ",Userid: " + userId + " : " + fb.get_title() + " : " + fb.get_comments() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate();

                            Log.e("Dairy after : ", log);
                        }

                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }
                    new Async_UpdateAprovalStatus().execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(PlanerDetailActivity.this, GPSTracker.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    private GPSTracker gpsService;
    private boolean mBound;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GPSTracker.GpsBinder binder = (GPSTracker.GpsBinder) service;
            gpsService = binder.getService();

            if (gpsService.canGetLocation) {
                double latitude = gpsService.getLatitude();
                double longitude = gpsService.getLongitude();
                String checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude);
                Log.d("checkinlatlong11  ", checkinlatlong);

            }

            if (!gpsService.canGetLocation) {
                gpsService.getLocation();
                if (!gpsService.canGetLocation)
                    gpsService.showSettingsAlert(PlanerDetailActivity.this);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private class Async_UpdateAprovalStatus extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Updating Aproval Status");
            progressDialog.show();*/
        }

        protected String doInBackground(Void... params) {
          /*  check_in_time = timeFormat.format(mytime);
            //  Log.e("Check_in_time",check_in_time);

            visit_date = dateFormat.format(myDate);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdtime = new SimpleDateFormat("HH:mm:ss");
            String strDate = sdtime.format(c.getTime());
            Log.e("Check_in_time", strDate);*/

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                RequestBody formBody = new FormEncodingBuilder()
                        .add("table", "employee_visit_management")
                        .add("primary_key", "emp_visit_id")
                        .add("primary_value", event_id)
                        .add("sql_id", event_id)
                        //.add("approval_status",event_approval_status)
                        .add("status", "4")
                        .add("completed_lat_lon", String.valueOf(latitude) + "," + String.valueOf(longitude))
                        .add("updated_by", userId)
                        .build();




                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
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
                    System.out.println("!!!!!!!1" + formBody + "\n" + jsonData);

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

                        sdbw = db.getWritableDatabase();
                        String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_STATUS + " = 4 WHERE " + KEY_EMP_VISIT_ID + " = " + event_id;
                        sdbw.execSQL(updatequerys);

                        // new PlanerOneActivity.Async_getalloffline().execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            /*Intent checkinintent = new Intent(getApplicationContext(),PlanerOneActivity.class);
            startActivity(checkinintent);*/


        }
    }
}
