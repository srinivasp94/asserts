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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_DD_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_DD_ID;
import static com.nsl.app.DatabaseHandler.TABLE_DAILYDAIRY;

public class AdhocActivity extends AppCompatActivity {
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
        db = new DatabaseHandler(AdhocActivity.this);
        // db.deleteDailyDiary();

        title = (EditText) findViewById(R.id.et_title);
        time = (EditText) findViewById(R.id.et_time);
        comments = (EditText) findViewById(R.id.comments);
        submit = (Button) findViewById(R.id.submit);
        tv_time = (TextView) findViewById(R.id.time);

        sharedpreferences = AdhocActivity.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");

        Log.d("Reading: ", "Reading all daily..");


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AdhocActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String formattedTime = "";
                        String sHour = "00";
                        String sMinute = "00";
                        // converting hour to tow digit if its between 0 to 9. (e.g. 7 to 07)
                        if (selectedHour < 10)
                            sHour = "0" + selectedHour;
                        else
                            sHour = String.valueOf(selectedHour);

                        if (selectedMinute < 10)
                            sMinute = "0" + selectedMinute;
                        else
                            sMinute = String.valueOf(selectedMinute);

                        time.setText(sHour + ":" + sMinute + ":00");
                    }
                }, hour, minute, true);
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        /*time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(AdhocActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TO DO: Auto-generated method stub
                        int selmon = selectedmonth + 1;
                        time.setText(selectedyear + "-" + selmon + "-" + selectedday);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select time");
                mDatePicker.show();
            }
        });*/

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String noval = "null";
                String usertitle = title.getText().toString();
                String time1 = time.getText().toString();
                String usercomments = comments.getText().toString();
                String userdate = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT).format(new Date());

                if (TextUtils.isEmpty(usertitle) || usertitle.length() > 0 && usertitle.startsWith(" ")) {
                    title.requestFocus();
                    title.setError("Please enter title");
                    return;
                }

                if (TextUtils.isEmpty(usercomments) || usercomments.length() > 0 && usercomments.startsWith(" ")) {
                    comments.requestFocus();
                    comments.setError("Please enter comment");
                    return;
                }
                if (TextUtils.isEmpty(time1) || time1.length() > 0 && time1.startsWith(" ")) {
                    time.requestFocus();
                    time.setError("Please enter tentative time");
                    return;
                }


                db.addDailyDairy(new DailyDairy(usertitle, Integer.parseInt(checkuid), usercomments, null, userdate, null, null, 2, time.getText().toString().trim(),1 ));

                Log.e("id ", ":" + usertitle + ":" + checkuid + ":" + usercomments + ":" + userdate + ":" + null);

                insertToService();


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkdata();
    }

    private void checkdata() {
        String userdate = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT).format(new Date());
        List<DailyDairy> dailydairy = db.getAdhocDailyDairy(checkuid,userdate);

        Log.e("list size", String.valueOf(dailydairy.size()));
        for (DailyDairy fb : dailydairy) {
            String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate() + " : " + fb.get_ffmid();
            Log.e("DailyDairy", log);
            // Log.e("found",timeslot);
/*
            if ((timeslot.equalsIgnoreCase(fb.get_time())) && (checkuid.equalsIgnoreCase(String.valueOf(fb.get_userid())) && (userdate.equalsIgnoreCase(fb.get_date())))) {
                // if(timeslot.){
                Log.e("Dairy time found : ", log);


                existinguserid = String.valueOf(fb.getID());
                existingffmid = String.valueOf(fb.get_ffmid());
                title.setText(fb.get_title());
                comments.setText(fb.get_comments());
                flag = 1;
                break;

            }*/
        }
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

    private void insertToService() {

        db = new DatabaseHandler(AdhocActivity.this);

        List<DailyDairy> dailydairy = db.getAllnullAdhocDailyDairy(checkuid);

        Log.e("list size", String.valueOf(dailydairy.size()));

        if (dailydairy.size() == 0) {
            Log.e("no data found", "no data");
        } else {
            for (DailyDairy fb : dailydairy) {
                String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate() + " : " + fb.get_ffmid() + " : " + fb.get_tentative_time();
                Log.d("list size", log);
                try {
                    if (Utility.isNetworkAvailable(AdhocActivity.this, Constants.isShowNetworkToast)) {
                        new Async_InsertDailyDairy().execute(String.valueOf(fb.getID()), fb.get_title(), checkuid, fb.get_comments(), fb.get_time(), fb.get_date(), fb.get_createddate(), fb.get_tentative_time());
                    } else {


                    }

                } catch (Exception e) {
                    Log.d("Tag", e.toString());
                }

                //  }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdhocActivity.this);

                // set title
                alertDialogBuilder.setTitle("Added");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Your Diary is created successfully!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                setResult(Activity.RESULT_OK);
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
            progressDialog = new ProgressDialog(AdhocActivity.this);
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
                        .add("time_slot", "")
                        .add("dairy_date", params[5])
                        .add("tentative_time", params[7])
                        .add("type", "2")
//                        .add("created_date",params[6])

                        .build();

                Response responses = null;
                System.out.println("---- dairy data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[7]);

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
            Common.dismissProgressDialog(progressDialog);

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
                        String updatequery = "UPDATE " + TABLE_DAILYDAIRY + " SET " + KEY_DD_FFMID + " = " + ffmid + " WHERE " + KEY_DD_ID + " = '" + sqliteid + "'";
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);

                        List<DailyDairy> dailydairy = db.getAllDailyDairy(checkuid);

                        for (DailyDairy fb : dailydairy) {
                            String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_comments() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate();

                            Log.e("Dairy after : ", log);
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


    private class Async_updateDailyDairy extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdhocActivity.this);
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
}

