package com.nsl.app;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.marcohc.robotocalendar.RobotoCalendarView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nsl.app.DatabaseHandler.KEY_EMP_APPROVAL_STATUS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_CHECK_IN_TIME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_COMMENTS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_CONCERN_PERSON_NAME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_CREATED_BY;
import static com.nsl.app.DatabaseHandler.KEY_EMP_CREATED_DATETIME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_END_DATE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_NAME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_PARTICIPANTS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_PURPOSE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_VENUE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_FEILD_AREA;
import static com.nsl.app.DatabaseHandler.KEY_EMP_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_GEO_TRACKING_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_LOCATION_ADDRESS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_MOBILE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_PLAN_DATE_TIME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_PURPOSE_VISIT;
import static com.nsl.app.DatabaseHandler.KEY_EMP_STATUS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_TYPE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_UPDATED_BY;
import static com.nsl.app.DatabaseHandler.KEY_EMP_UPDATE_DATETIME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VILLAGE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_PLAN_TYPE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanerMainActivity extends AppCompatActivity implements RobotoCalendarView.RobotoCalendarListener {
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr = null;

    SQLiteDatabase sdbw;


    public static String curDate;
    RobotoCalendarView calendarView;
    //private SliderLayout imageSlider;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    String datefromcalander, dateString, eventdate;
    private DatabaseHandler db;
    private SimpleDateFormat sdfs;
    private Calendar globalCalendar;
    private String jsonData;
    private String userId;
    private int role;
    private String team;
    StringBuilder sb=new StringBuilder();
    private ProgressDialog progressDialog;
    List<CalendarData> calendarDataList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannermain);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked

                finish();
            }
        });
        sharedpreferences   = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        calendarView        = (RobotoCalendarView) findViewById(R.id.calendarView);
        sdfs                = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        userId              = sharedpreferences.getString("userId", "");
        team                   = sharedpreferences.getString("team", "");
        role                   = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        db                  = new DatabaseHandler(this);
        globalCalendar      = Calendar.getInstance();


        calendarView.setRobotoCalendarListener(this);


        calendarView.setShortWeekDays(false);

        calendarView.showDateTitle(true);

        calendarView.updateView();

        fetchByMonth();


    }

    private void fetchByMonth() {
        Date date = globalCalendar.getTime();
        String dateString = sdfs.format(date);
        String dateMM = dateString.substring(0, 7);
        Log.d("date",dateString+" dateMM "+dateMM);
        new Async_getalloffline().execute(dateMM);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plannermain, menu);


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
            finish();
            return true;
        }

        if (id == R.id.action_add) {
            Intent one = new Intent(getApplicationContext(), PlanerTypeActivity.class);
            one.putExtra("i", "0");
            one.putExtra("date", datefromcalander);
            one.putExtra("selecteduser","");
            one.putExtra("selecteduserid",userId);
           // startActivity(one);
            return true;
        }

        return super.onOptionsItemSelected(menu);
    }


    /*@Override
    public void onBackPressed() {
        Intent plannermain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(plannermain);
        finish();
    }*/


    @Override
    public void onDayClick(Calendar calendar) {
        String d = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (d.length() == 1) {
            d = "0" + d;
        } else {

        }
        int m = calendar.get(Calendar.MONTH) + 1;
        String strmonth = String.valueOf(m);
        if (strmonth.length() == 1) {
            strmonth = "0" + strmonth;
        } else {

        }
        curDate =String.valueOf(calendar.get(Calendar.YEAR)) + "-" + strmonth + "-" +d ;
        //Toast.makeText(getActivity(), curDate, Toast.LENGTH_SHORT).show();
//get a list of installed apps.
        String pkg = getPackageName();
        Log.e("packages ",pkg);

        Intent plannerone = new Intent(getApplicationContext(), PlanerOneActivity.class);
        plannerone.putExtra("i", "0");
        startActivity(plannerone);
    }

    @Override
    public void onDayLongClick(Calendar calendar) {

    }

    @Override
    public void onRightButtonClick() {
        globalCalendar.add(Calendar.MONTH, 1);
        fetchByMonth();
    }

    @Override
    public void onLeftButtonClick() {
        globalCalendar.add(Calendar.MONTH, -1);
        fetchByMonth();
    }

    private class CalendarData {
        public String requestType;
        public String date;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Common.haveInternet(this.getApplicationContext())) {
            new Async_getallevm().execute();
        }else {
           fetchByMonth();
        }

    }

    private class Async_getalloffline extends AsyncTask<String, Void, String> {

        private Calendar calendar;

       // List<CalendarData> calendarDataList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            calendarView.clearCalendar();

        }

        protected String doInBackground(String... params) {

            String selectQuery = null;
            try {
                if(role==Constants.Roles.ROLE_7) {
                   selectQuery = "SELECT DISTINCT "
                            + KEY_EMP_CONCERN_PERSON_NAME + ","
                            + KEY_EMP_VISIT_PLAN_TYPE + ","
                            + KEY_EMP_STATUS + ","
                            + KEY_EMP_PLAN_DATE_TIME + ","
                            + KEY_EMP_PURPOSE_VISIT + ","
                            + KEY_EMP_TYPE + ","
                            + KEY_EMP_GEO_TRACKING_ID + ","
                            + KEY_EMP_VISIT_USER_ID + ","
                            + KEY_EMP_VISIT_CUSTOMER_ID + ","
                            + KEY_EMP_MOBILE + ","
                            + KEY_EMP_VILLAGE + ","
                            + KEY_EMP_LOCATION_ADDRESS + ","
                            + KEY_EMP_FEILD_AREA + ","
                            + KEY_EMP_CHECK_IN_TIME + ","
                            + KEY_EMP_COMMENTS + ","
                            + KEY_EMP_APPROVAL_STATUS + ","
                            + KEY_EMP_EVENT_END_DATE + ","
                            + KEY_EMP_EVENT_PURPOSE + ","
                            + KEY_EMP_VISIT_MASTER_ID + ","
                            + KEY_EMP_EVENT_VENUE + ","
                            + KEY_EMP_EVENT_PARTICIPANTS + ","
                            + KEY_EMP_FFM_ID + ","
                            + KEY_EMP_CREATED_BY + ","
                            + KEY_EMP_UPDATED_BY + ","
                            + KEY_EMP_CREATED_DATETIME + ","
                            + KEY_EMP_UPDATE_DATETIME + ","
                            + KEY_EMP_EVENT_NAME + ","
                            + KEY_EMP_VISIT_ID


                            + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " = " + userId  + " AND " + KEY_EMP_PLAN_DATE_TIME + " like '" + params[0] + "%'" ;
                }

                if(role!=Constants.Roles.ROLE_7) {
                    selectQuery = "SELECT DISTINCT "
                            + KEY_EMP_CONCERN_PERSON_NAME + ","
                            + KEY_EMP_VISIT_PLAN_TYPE + ","
                            + KEY_EMP_STATUS + ","
                            + KEY_EMP_PLAN_DATE_TIME + ","
                            + KEY_EMP_PURPOSE_VISIT + ","
                            + KEY_EMP_TYPE + ","
                            + KEY_EMP_GEO_TRACKING_ID + ","
                            + KEY_EMP_VISIT_USER_ID + ","
                            + KEY_EMP_VISIT_CUSTOMER_ID + ","
                            + KEY_EMP_MOBILE + ","
                            + KEY_EMP_VILLAGE + ","
                            + KEY_EMP_LOCATION_ADDRESS + ","
                            + KEY_EMP_FEILD_AREA + ","
                            + KEY_EMP_CHECK_IN_TIME + ","
                            + KEY_EMP_COMMENTS + ","
                            + KEY_EMP_APPROVAL_STATUS + ","
                            + KEY_EMP_EVENT_END_DATE + ","
                            + KEY_EMP_EVENT_PURPOSE + ","
                            + KEY_EMP_VISIT_MASTER_ID + ","
                            + KEY_EMP_EVENT_VENUE + ","
                            + KEY_EMP_EVENT_PARTICIPANTS + ","
                            + KEY_EMP_FFM_ID + ","
                            + KEY_EMP_CREATED_BY + ","
                            + KEY_EMP_UPDATED_BY + ","
                            + KEY_EMP_CREATED_DATETIME + ","
                            + KEY_EMP_UPDATE_DATETIME + ","
                            + KEY_EMP_EVENT_NAME + ","
                            + KEY_EMP_VISIT_ID


                            + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " in (" + team+ ") AND " + KEY_EMP_PLAN_DATE_TIME + " like '" + params[0] + "%'";
                }
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                System.out.println("selectQuery PlanerM " + selectQuery);
                calendarDataList.clear();
                if (cursor.moveToFirst()) {


                    do {

                        try {
                            String emptype = cursor.getString(5);
                            String plan_date = cursor.getString(3);
                            if (plan_date!=null && plan_date.length()>7) {
                                String date = plan_date.split(" ")[0];
                                CalendarData calendarData = new CalendarData();
                                calendarData.requestType = emptype;
                                calendarData.date = date;


                                PlanerMainActivity.this.calendarDataList.add(calendarData);
                            }


                           // calendarDataList.add(calendarData);

                            Log.e("---inserted values --", "event_name :" + cursor.getString(0)
                                    + "\n visit_plaanetype" + cursor.getString(1)
                                    + "\n event_status" + cursor.getString(2)
                                    + "\n event_datetime" + cursor.getString(3)
                                    + "\n event_purpose" + cursor.getString(4)
                                    + "\n type :" + cursor.getString(5)
                                    + "\n geoid :" + cursor.getString(6)
                                    + "\n userid :" + cursor.getString(7)
                                    + "\n event_customer_id" + cursor.getString(8)
                                    + "\n mobile :" + cursor.getString(9)
                                    + "\n village :" + cursor.getString(10)
                                    + "\n location address :" + cursor.getString(11)
                                    + "\n field area :" + cursor.getString(12)
                                    + "\n checkin :" + cursor.getString(13)
                                    + "\n comments" + cursor.getString(14)
                                    + "\n approval status" + cursor.getString(15)
                                    + "\n Event date" + cursor.getString(16)
                                    + "\n Event purpose" + cursor.getString(17)
                                    + "\n masterid :" + cursor.getString(18)
                                    + "\n event venue :" + cursor.getString(19)
                                    + "\n event participents :" + cursor.getString(20)
                                    + "\n ffmid :" + cursor.getString(21)
                                    + "\n createdby :" + cursor.getString(22)
                                    + "\n updatedby :" + cursor.getString(23)

                                    + "\n cdatetime :" + cursor.getString(24)
                                    + "\n udatetime :" + cursor.getString(25)
                                    + "\n eventname :" + cursor.getString(26)
                                    + "\n event id :" + cursor.getString(27));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } while (cursor.moveToNext());

                }

                Set<CalendarData> set = new HashSet<CalendarData>();
                set.addAll(PlanerMainActivity.this.calendarDataList);
                PlanerMainActivity.this.calendarDataList.clear();

                PlanerMainActivity.this.calendarDataList.addAll(set);





            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /*if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/

            for (int i = 0; i < calendarDataList.size(); i++) {
                Common.Log.i(""+calendarDataList.size());
                CalendarData calendarData = calendarDataList.get(i);
                Calendar calendar = Calendar.getInstance();
                Date date = null;
                try {
                    date = sdfs.parse(calendarData.date.split(" ")[0]);
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (calendarData.requestType.equalsIgnoreCase("1")) {

                    calendarView.markCircleImage1(calendar);
                } else if (calendarData.requestType.equalsIgnoreCase("2")) {

                    calendarView.markCircleImage2(calendar);

                }
            }

        }
    }

    private class Async_getallevm extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(PlanerMainActivity.this);
            progressDialog.setMessage("Please wait ");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURLEVM(Constants.NEW_OR_UPDATED_RECORDS_EMPLOYEE_VISIT_MANAGEMENT,userId,team))
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 Planner" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonData != null) {
                try {
                    JSONObject jsonObject=new JSONObject(jsonData);
                    JSONArray companyarray=jsonObject.getJSONArray("newrecord");

                   // JSONArray companyarray = new JSONArray(jsonData);
                    int mobile, geo_tracking_id ;
                    String visit_plan_type;
                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String emp_visit_id = objinfo.getString("emp_visit_id");
                        if (n>0){
                            sb.append(",");
                        }
                        sb.append(emp_visit_id);

                        int type = objinfo.getInt("type");

                        int user_id = objinfo.getInt("user_id");
                        int customer_id = objinfo.getInt("customer_id");
                        if (objinfo.getString("visit_plan_type").equalsIgnoreCase("null") || objinfo.getString("visit_plan_type").equalsIgnoreCase("")) {
                             visit_plan_type = "0";
                        } else {
                             visit_plan_type = objinfo.getString("visit_plan_type");

                        }


                        String purpose_visit = objinfo.getString("purpose_visit");
                        String plan_date_time = objinfo.getString("plan_date_time");
                        String concern_person_name = objinfo.getString("concern_person_name");
                        if (objinfo.getString("geo_tracking_id").equalsIgnoreCase("null") || objinfo.getString("geo_tracking_id").equalsIgnoreCase("")) {
                            geo_tracking_id = 0;
                        } else {
                            geo_tracking_id = objinfo.getInt("geo_tracking_id");

                        }
                        if (objinfo.getString("mobile").equalsIgnoreCase("null") || objinfo.getString("mobile").equalsIgnoreCase("")) {
                            mobile = 0;
                        } else {
                            mobile = objinfo.getInt("mobile");
                        }
                        String village = objinfo.getString("village");
                        String location_address = objinfo.getString("location_address");
                        String field_area = objinfo.getString("field_area");
                        String check_in_time = objinfo.getString("check_in_time");
                        String comments = objinfo.getString("comments");

                        int status = objinfo.getInt("status");


                        String event_name = objinfo.getString("event_name");
                        String event_end_date = objinfo.getString("event_end_date");
                        String event_purpose = objinfo.getString("event_purpose");
                        String event_venue = objinfo.getString("event_venue");
                        String event_participants = objinfo.getString("event_participants");
                        String created_by = objinfo.getString("created_by");
                        String updated_by = objinfo.getString("updated_by");
                        int approval_status = objinfo.getInt("approval_status");


                        String created_datetime = objinfo.getString("created_datetime");
                        String update_datetime = objinfo.getString("update_datetime");
                        String selectQuery = "SELECT * FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " WHERE " + KEY_EMP_FFM_ID + " = '" + emp_visit_id + "'";
                        String purpose_visit_titles = Common.getPlannerTitleById(purpose_visit);
                        sdbw = db.getWritableDatabase();
                        Cursor cc = sdbw.rawQuery(selectQuery, null);
                        cc.getCount();
                        // looping through all rows and adding to list
                        if (cc.getCount() == 0) {
                            //doesn't exists therefore insert record.
                            db.addEVM(new Employe_visit_management_pojo(emp_visit_id, type, geo_tracking_id, user_id, customer_id,
                                    visit_plan_type,purpose_visit_titles, String.valueOf(purpose_visit), plan_date_time, concern_person_name, mobile, village, location_address, field_area, check_in_time,
                                    comments, status, approval_status, event_name, event_end_date, event_purpose, event_venue, event_participants, created_by, updated_by, created_datetime, update_datetime
                                    , Integer.parseInt(emp_visit_id)));
                        }

                        Log.d("Insert: ", "Inserting Employee visit management");
                       /* db.addEVM(new Employe_visit_management_pojo(emp_visit_id, type, geo_tracking_id, user_id, customer_id,
                                visit_plan_type, purpose_visit, plan_date_time, concern_person_name, mobile, village, location_address, field_area, check_in_time,
                                comments, status, approval_status, event_name, event_end_date, event_purpose, event_venue, event_participants, created_by, updated_by, created_datetime, update_datetime
                                , 0));*/

                    }


                    JSONArray updatedJSONArray=jsonObject.getJSONArray("updated");


                    for (int n = 0; n < updatedJSONArray.length(); n++) {
                        JSONObject objinfo = updatedJSONArray.getJSONObject(n);

                        String emp_visit_id = objinfo.getString("emp_visit_id");

                        if (sb.toString().length()>0){
                            sb.append(",");
                        }
                        sb.append(emp_visit_id);

                        int type = objinfo.getInt("type");

                        int user_id = objinfo.getInt("user_id");
                        int customer_id = objinfo.getInt("customer_id");
                        if (objinfo.getString("visit_plan_type").equalsIgnoreCase("null") || objinfo.getString("visit_plan_type").equalsIgnoreCase("")) {
                            visit_plan_type = "0";
                        } else {
                            visit_plan_type = objinfo.getString("visit_plan_type");

                        }


                        String purpose_visit = objinfo.getString("purpose_visit");
                        String plan_date_time = objinfo.getString("plan_date_time");
                        String concern_person_name = objinfo.getString("concern_person_name");
                        if (objinfo.getString("geo_tracking_id").equalsIgnoreCase("null") || objinfo.getString("geo_tracking_id").equalsIgnoreCase("")) {
                            geo_tracking_id = 0;
                        } else {
                            geo_tracking_id = objinfo.getInt("geo_tracking_id");

                        }
                        if (objinfo.getString("mobile").equalsIgnoreCase("null") || objinfo.getString("mobile").equalsIgnoreCase("")) {
                            mobile = 0;
                        } else {
                            mobile = objinfo.getInt("mobile");
                        }
                        String village = objinfo.getString("village");
                        String location_address = objinfo.getString("location_address");
                        String field_area = objinfo.getString("field_area");
                        String check_in_time = objinfo.getString("check_in_time");
                        String comments = objinfo.getString("comments");

                        int status = objinfo.getInt("status");
                        int approval_status = objinfo.getInt("approval_status");


                        String event_name = objinfo.getString("event_name");
                        String event_end_date = objinfo.getString("event_end_date");
                        String event_purpose = objinfo.getString("event_purpose");
                        String event_venue = objinfo.getString("event_venue");
                        String event_participants = objinfo.getString("event_participants");
                        String created_by = objinfo.getString("created_by");
                        String updated_by = objinfo.getString("updated_by");


                        String created_datetime = objinfo.getString("created_datetime");
                        String update_datetime = objinfo.getString("update_datetime");
                      //  String selectQuery = "SELECT * FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " WHERE " + KEY_EMP_FFM_ID + " = '" + emp_visit_id + "'";
                        String purpose_visit_titles = Common.getPlannerTitleById(purpose_visit);

                        sdbw = db.getWritableDatabase();
                        String updatequerys=null;
                        if (comments.equalsIgnoreCase(null) || comments.equalsIgnoreCase("null")|| comments.equalsIgnoreCase("")){
                            updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_APPROVAL_STATUS + " = '" + approval_status  + "' WHERE " + KEY_EMP_FFM_ID + " = " + emp_visit_id;
                        }else {
                             updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_APPROVAL_STATUS + " = '" + approval_status + "'," + KEY_EMP_COMMENTS + " = '" + comments + "' WHERE " + KEY_EMP_FFM_ID + " = " + emp_visit_id;
                        }
                            sdbw.execSQL(updatequerys);

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
             //   Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

                new Async_UpdateAprovalStatus().execute();
            fetchByMonth();
        }
    }


    private class Async_UpdateAprovalStatus extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Updating Aproval Status");
            progressDialog.show();*/
        }

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;



                RequestBody formBody = new FormEncodingBuilder()
                        .add("table","employee_visit_management")
                        .add("field", "emp_visit_id")
                        .add("updated_ids",sb.toString())
                        .add("user_id",userId)
                        .build();



         Request request = new Request.Builder()
                        .url(Constants.ACKNOWLEDGE_TO_SERVER)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1ACKNOWLEDGE_TO_SERVER" + request.body().toString()+"\n"+jsonData);
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



        }
    }
}
