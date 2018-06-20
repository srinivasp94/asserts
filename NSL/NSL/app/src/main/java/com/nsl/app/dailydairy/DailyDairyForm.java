package com.nsl.app.dailydairy;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;
import com.nsl.app.Utility;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_DD_COMMENTS;
import static com.nsl.app.DatabaseHandler.KEY_DD_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_DD_ID;
import static com.nsl.app.DatabaseHandler.KEY_DD_TENTATIVE_TIME;
import static com.nsl.app.DatabaseHandler.KEY_DD_TIME_SLOT;
import static com.nsl.app.DatabaseHandler.KEY_DD_TITLE;
import static com.nsl.app.DatabaseHandler.TABLE_DAILYDAIRY;

public class DailyDairyForm extends AppCompatActivity {
    EditText title, date, comments;
    private String[] time_slots = {"Select time_slot", " 9AM - 11AM", " 11AM - 1PM", "2PM - 4PM", "4PM - 6PM"};
    Button submit;
    Spinner time;
    int flag=0;
    TextView tv_time;
    String existinguserid,existingffmid,timeslot;
    String sqliteid, ffmid;
    String checkuid, jsonData;
    private static SQLiteDatabase sql, sdbr, sdbw;
    SharedPreferences sharedpreferences;
    ProgressDialog progressDialog;
    ImageView edt;
    public static final String mypreference = "mypref";
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    DatabaseHandler db;
    String usertitle = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_dairy_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = new DatabaseHandler(DailyDairyForm.this);
        //db.deleteDailyDiary();

        //title = (EditText) findViewById(R.id.title);
        date = (EditText) findViewById(R.id.date);
        comments = (EditText) findViewById(R.id.comments);
        time = (Spinner) findViewById(R.id.spintime);
        submit = (Button) findViewById(R.id.submit);
        tv_time = (TextView) findViewById(R.id.time);
        edt = (ImageView) findViewById(R.id.edit);
        sharedpreferences = DailyDairyForm.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        timeslot = getIntent().getStringExtra("time");
        Log.d("Reading: ", "Reading all daily..");

        tv_time.setText(timeslot);

        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // title.setEnabled(true);
                comments.setEnabled(true);
                edt.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(DailyDairyForm.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TO DO: Auto-generated method stub
                        int selmon = selectedmonth + 1;
                        date.setText(selectedyear + "-" + selmon + "-" + selectedday);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select time");
                mDatePicker.show();
            }
        });
        ArrayAdapter slots = new ArrayAdapter(DailyDairyForm.this,
                R.layout.spinner_item, R.id.customSpinnerItemTextView, time_slots);
        time.setAdapter(slots);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String noval = "null";
                //String usertitle = title.getText().toString();
                //String userdate = time.getText().toString();
                String usercomments = comments.getText().toString();
                String usertime = timeslot;
                String userdate = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT).format(new Date());
              //  String tent_time = title.getText().toString();



                if (TextUtils.isEmpty(usercomments) || usercomments.length() > 0 && usercomments.startsWith(" ")) {
                    comments.requestFocus();
                    comments.setError("Please enter comment");
                    return;
                }

                if(flag==1){
                    int id = Integer.parseInt(existinguserid);
                    sdbw = db.getWritableDatabase();
                    String updatequerys = "UPDATE " + TABLE_DAILYDAIRY + " SET " + KEY_DD_COMMENTS + " = '"+ usercomments+"' WHERE " + KEY_DD_ID  + " = " + existinguserid +" AND "+KEY_DD_TIME_SLOT+" = '"+timeslot+"'";
                    Log.e("update dd",updatequerys);
                    sdbw.execSQL(updatequerys);

                    Log.e("id ", id + ":" + usertitle + ":" + checkuid + ":" + usercomments + ":" + userdate + ":" + usertime);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
                    map.put("Id", String.valueOf(id));
                    map.put("usertitle", usertime);
                    map.put("checkuid", checkuid);
                    map.put("usercomments", usercomments);
                    map.put("userdate", userdate);
                    map.put("usertime", usertime);
                    map.put("ffmid", String.valueOf(0));
                    favouriteItem.add(map);
                    // do some stuff....
                    updateToService(id,usertime,checkuid,usercomments,userdate,usertime,"00:00:00");

                }
                else if(flag==0) {
                    int id = 1;
                    db.addDailyDairy(new DailyDairy(usertime, Integer.parseInt(checkuid), usercomments, usertime, userdate, null, null,1,null, 1));

                    Log.e("id ", id + ":" + usertitle + ":" + checkuid + ":" + usercomments + ":" + userdate + ":" + usertime);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
                    map.put("Id", String.valueOf(id));
                    map.put("usertitle", usertitle);
                    map.put("checkuid", checkuid);
                    map.put("usercomments", usercomments);
                    map.put("userdate", userdate);
                    map.put("usertime", usertime);
                    map.put("ffmid", String.valueOf(0));
                    favouriteItem.add(map);
                    // do some stuff....
                    insertToService();

                }



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
        List<DailyDairy> dailydairy = db.getAlldateDailyDairy(checkuid,userdate);

        Log.e("list size", String.valueOf(dailydairy.size()));
        for (DailyDairy fb : dailydairy) {
            String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() +" : " + fb.get_comments() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate() + " : " + fb.get_ffmid();
            Log.e("DailyDairy",log);
            Log.e("found",timeslot);

            if ((timeslot.equalsIgnoreCase(fb.get_time()))) {
               // if(timeslot.){
                Log.e("Dairy time found : ", log);


                existinguserid = String.valueOf(fb.getID());
                existingffmid = String.valueOf(fb.get_ffmid());
                //title.setText(fb.get_tentative_time());
                //title.setEnabled(false);
                comments.setText(fb.get_comments());
                comments.setEnabled(false);
                edt.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                flag = 1;
                break;

            }
        }
    }


    private void updateToService(int id, String usertitle, String checkuid, String usercomments, String userdate, String usertime,String tent_time) {
        db = new DatabaseHandler(DailyDairyForm.this);
        Log.d("Reading: ", "updated diary in sqlite sending to server");

                String log = "Id: " + id + ",Userid: " + checkuid + " : " + usertitle + " : " + usertime + " : " + userdate + " : " + tent_time + " : " + usercomments;

                Log.e("updateing to servive : ", log);
                try {
                    if (Utility.isNetworkAvailable(DailyDairyForm.this, Constants.isShowNetworkToast)) {
                        new Async_updateDailyDairy().execute(this.checkuid, tent_time ,usercomments,userdate,usertime,String.valueOf(id),String.valueOf(id));
                    } else {


                        Log.e("No internet", "no internet");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DailyDairyForm.this);

                        // set title
                        alertDialogBuilder.setTitle("Updated");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Your Diary is updated successfully!")
                                .setCancelable(false)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        Intent adv = new Intent();
                                        setResult(Activity.RESULT_OK);

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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DailyDairyForm.this);

        // set title
        alertDialogBuilder.setTitle("Updated");

        // set dialog message
        alertDialogBuilder
                .setMessage("Your Diary is updated successfully!")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
//                        Intent adv = new Intent(getApplicationContext(), DailyDairyActivity.class);
//
//                        startActivity(adv);
                        setResult(Activity.RESULT_OK);

                        finish();

                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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
//            Intent home = new Intent(getApplicationContext(),DailyDairyActivity.class);
//            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void insertToService() {

        db = new DatabaseHandler(DailyDairyForm.this);
        Log.d("Reading: ", "Reading all daily..");


        List<DailyDairy> dailydairy = db.getAllnullDailyDairy(checkuid);

        Log.e("list size", String.valueOf(dailydairy.size()));

        if (dailydairy.size() == 0) {
            Log.e("no data found", "no data");
        } else {
            for (DailyDairy fb : dailydairy) {
                String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate() + " : " + fb.get_ffmid();

                Log.e("Dairy before : ", log);
                    try {
                    if (Utility.isNetworkAvailable(DailyDairyForm.this, Constants.isShowNetworkToast)) {
                        new Async_InsertDailyDairy().execute(String.valueOf(fb.getID()),fb.get_time(), checkuid, fb.get_comments(), fb.get_time(), fb.get_date(),fb.get_createddate(),fb.get_tentative_time());
                    } else {


                        Log.e("No internet", "no internet");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DailyDairyForm.this);

                        // set title
                        alertDialogBuilder.setTitle("Added");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Your Diary is created successfully!")
                                .setCancelable(false)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
//                                        Intent adv = new Intent(getApplicationContext(), DailyDairyActivity.class);
//
//                                        startActivity(adv);
                                        setResult(Activity.RESULT_OK);

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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DailyDairyForm.this);

                // set title
                alertDialogBuilder.setTitle("Added");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Your Diary is created successfully!")
                        .setCancelable(false)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
//                                Intent adv = new Intent(getApplicationContext(), DailyDairyActivity.class);
//
//                                startActivity(adv);
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
            progressDialog = new ProgressDialog(DailyDairyForm.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
//             progressDialog.show();
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
                        .add("tentative_time", params[7])
                        .add("type", "1")

//                        .add("created_date",params[6])

                        .build();

                Response responses = null;
                System.out.println("---- dairy data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5]+ params[6]);

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
                        String updatequery = "UPDATE " + TABLE_DAILYDAIRY + " SET " + KEY_DD_FFMID + " = " + ffmid + " WHERE " + KEY_DD_ID + " = '" + sqliteid+"'";
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);

                        List<DailyDairy> dailydairy = db.getAllDailyDairy(checkuid);

                        for (DailyDairy fb : dailydairy) {
                            String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title()  + " : " + fb.get_comments() + " : " + fb.get_time()+  " : " + fb.get_date() + " : " + fb.get_createddate()+ " : " + fb.get_tentative_time();

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
                progressDialog = new ProgressDialog(DailyDairyForm.this);
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
                            .add("tentative_time", params[1])
                            .add("note", params[2])
                            .add("dairy_date", params[3])
                            .add("time_slot", params[4])
                            .add("id", params[5])
                            .add("dailydairy_id",params[6])
//                        .add("created_date",params[6])

                            .build();

                    Response responses = null;
                    System.out.println("---- dairy data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5]+ params[6]);

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
                                String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title()  + " : " + fb.get_comments() + " : " + fb.get_time()+  " : " + fb.get_date() + " : " + fb.get_createddate();

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

