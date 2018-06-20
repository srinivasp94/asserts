package com.nsl.app;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.nsl.app.view.MultiSelectionSpinner;

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
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.app.DatabaseHandler.TABLE_USERS;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanEventFormActivity extends AppCompatActivity {
    // JSON parser class

    ProgressDialog progressDialog;

    private ArrayList<SelectedCities> organisations;
    ArrayList<String> adaptercity;
    private ArrayList<Users> userses;
    TextView tv_name, tv_code;
    Spinner spin_visittypes, spin_user, spin_customers;
    //et_event_startdate,
    EditText  et_start_time, et_event_enddate, et_end_time, et_event_name, et_address, et_fieldarea, et_purpose,et_participants;
    Button btn_savebooing;
    AutoCompleteTextView  et_venue;
    private    LinearLayout ll_select;
    private String userName;
    String sel_crop_id, str_name, str_event_name, str_address, str_fieldarea, str_purpose, str_venue, str_participants, sel_scheme_id, str_event_start_time, str_event_end_date, str_event_end_time, company_id, order_date,str_event_start_date;
    String village = "", location_address, field_area, check_in_time, comments = "", str_sel_visittypeid, str_sel_purposeid;
    int sel_customer_id, type, geo_tracking_id, mobile, status = 1, customer_id;

    private ArrayList<SelectedCities> arlist_users;
    ArrayList<String> adapter_users;
    final Context context = this;

    int sqliteid, ffmid;

    String jsonData;
    String str_qty;
    String str_amount;
    String userId;
    String sel_userId;
    int role;
    String team;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> cropids = new ArrayList<HashMap<String, String>>();

    DateFormat dateFormat, orderdateFormat;
    Date myDate;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    DatabaseHandler db;
    SQLiteDatabase sql, sdbw, sdbr;
    MultiSelectionSpinner spin_purpose;
    private String str_sel_purposetypes;
    private ProgressDialog progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_event);
        db = new DatabaseHandler(this);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
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

        type = Integer.parseInt(getIntent().getExtras().getString("type"));
        str_event_start_date               = getIntent().getExtras().getString("startDate");

        userId = sharedpreferences.getString("userId", "");
        team                   = sharedpreferences.getString("team", "");
        role      = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        spin_visittypes = (Spinner) findViewById(R.id.spin_visittype);
        spin_purpose = (MultiSelectionSpinner) findViewById(R.id.spin_purpose);
        spin_user = (Spinner) findViewById(R.id.spin_user);
        spin_customers = (Spinner) findViewById(R.id.spin_customers);
        if (role == Constants.Roles.ROLE_5) {
            sel_userId = getIntent().getExtras().getString("sel_userId");
            userName = getIntent().getExtras().getString("sel_userName");
            Log.e("received username", userName + sel_userId);

        }
        //et_event_startdate = (EditText) findViewById(R.id.et_event_startdate);
        et_start_time = (EditText) findViewById(R.id.et_start_time);
        et_event_enddate = (EditText) findViewById(R.id.et_event_enddate);
        et_end_time = (EditText) findViewById(R.id.et_end_time);
        et_event_name = (EditText) findViewById(R.id.et_event_name);
        et_address = (EditText) findViewById(R.id.et_address);
        et_fieldarea = (EditText) findViewById(R.id.et_fieldarea);
        et_purpose = (EditText) findViewById(R.id.et_purpose);
        et_venue = (AutoCompleteTextView)findViewById(R.id.et_venue);
        et_participants = (EditText) findViewById(R.id.et_participants);
        ll_select              = (LinearLayout)findViewById(R.id.ll_select);
        dateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        orderdateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        myDate = new Date();

        new AsyncUsers().execute();
        setplanvisittypeData();
        et_venue.setThreshold(1);

        et_venue.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });


     /*   et_event_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(PlanEventFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int sel_month = selectedmonth + 1;
                        String sday = String.valueOf(selectedday);
                        String smonth = null;
                        if (sel_month < 10)
                            smonth = "0" + sel_month;
                        else
                            smonth = String.valueOf(sel_month);

                        if (selectedday < 10)
                            sday = "0" + selectedday;
                        else
                            sday = String.valueOf(selectedday);

                        et_event_startdate.setText(selectedyear + "-" + smonth + "-" + sday);
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });*/

        et_event_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(PlanEventFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {


                        // TODO Auto-generated method stub
                        int sel_month = selectedmonth + 1;
                        String sday = String.valueOf(selectedday);
                        String smonth = null;
                        if (sel_month < 10)
                            smonth = "0" + sel_month;
                        else
                            smonth = String.valueOf(sel_month);

                        if (selectedday < 10)
                            sday = "0" + selectedday;
                        else
                            sday = String.valueOf(selectedday);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(selectedyear, selectedmonth, selectedday);


                        String dateString = orderdateFormat.format(new Date(calendar.getTimeInMillis()));
                        et_event_enddate.setText(dateString);

                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        et_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PlanEventFormActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String formattedTime = "";
                        String sHour         = "00";
                        String sMinute       = "00";
                        // converting hour to tow digit if its between 0 to 9. (e.g. 7 to 07)
                        if (selectedHour < 10)
                            sHour = "0" + selectedHour;
                        else
                            sHour = String.valueOf(selectedHour);

                        if (selectedMinute < 10)
                            sMinute = "0" + selectedMinute;
                        else
                            sMinute = String.valueOf(selectedMinute);


                        et_start_time.setText(sHour + ":" + sMinute + ":00");
                    }
                }, hour, minute, true);
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        et_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PlanEventFormActivity.this, new TimePickerDialog.OnTimeSetListener() {
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



                        et_end_time.setText(sHour + ":" + sMinute + ":00");
                    }
                }, hour, minute, true);
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        btn_savebooing = (Button) findViewById(R.id.btn_saveplan);
        btn_savebooing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Handler handler=Common.disableClickEvent(btn_savebooing,true);
                str_event_start_time = et_start_time.getText().toString();
                str_event_end_date   = et_event_enddate.getText().toString();
                str_event_end_time   = et_end_time.getText().toString();
                str_event_name       = et_event_name.getText().toString();
                str_address          = et_address.getText().toString();
                str_fieldarea        = et_fieldarea.getText().toString();
                str_purpose          = et_purpose.getText().toString();
                str_venue            = et_venue.getText().toString();
                str_participants     = et_participants.getText().toString();

                if (TextUtils.isEmpty(str_event_name) || str_event_name.length() > 0 && str_event_name.startsWith(" ")) {
                    et_event_name.setError("Please enter event name");
                    et_event_name.requestFocus();
                    return;
                }
              /*  if (TextUtils.isEmpty(str_event_start_date) || str_event_start_date.length() > 0 && str_event_start_date.startsWith(" ")) {
                    et_event_startdate.setError("Please select  event start date");
                    et_event_startdate.requestFocus();
                    return;
                }*/

                if (TextUtils.isEmpty(str_event_start_time) || str_event_start_time.length() > 0 && str_event_start_time.startsWith(" ")) {
                    et_start_time.setError("Please select start time");
                    et_start_time.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(str_event_end_date) || str_event_end_date.length() > 0 && str_event_end_date.startsWith(" ")) {
                    et_event_enddate.setError("Please select  event end date");
                    et_event_enddate.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(str_event_end_time) || str_event_end_time.length() > 0 && str_event_end_time.startsWith(" ")) {
                    et_end_time.setError("Please select end time");
                    et_end_time.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(str_purpose) || str_purpose.length() > 0 && str_purpose.startsWith(" ")) {
                    et_purpose.setError("Please enter purpose");
                    et_purpose.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(str_venue) || str_venue.length() > 0 && str_venue.startsWith(" ")) {
                    et_venue.setError("Please enter venue");
                    et_venue.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(str_participants) || str_participants.length() > 0 && str_participants.startsWith(" ")) {
                    et_participants.setError("Please enter participants");
                    et_participants.requestFocus();
                    return;
                }



                else {
                    Common.disableClickEvent(btn_savebooing,handler);
                    progressDialog1=Common.showProgressDialog(PlanEventFormActivity.this);
                    //new Async_submitadvancepayment().execute();
                    order_date = dateFormat.format(myDate);

                    // For Inserting order into service order table
                    Log.d("Insert: ", "Inserting event ..");
                    // db.addEVM(new Employe_visit_management_pojo("",userId,sel_customer_id, str_sel_visittypeid, str_sel_purposeid, str_date, str_name, mobile, village, str_address, str_fieldarea, check_in_time, comments, "1","",type,userId,userId,order_date,order_date,geo_tracking_id));
                    // custom dialog

                    if(spin_purpose.getSelectedStrings()!=null && spin_purpose.getSelectedStrings().size()>0)
                    str_sel_purposetypes = convertlistToString(spin_purpose.getSelectedStrings());

                    if(role==Constants.Roles.ROLE_7){
                        db.addEVM(new Employe_visit_management_pojo("", type, geo_tracking_id, Integer.parseInt(userId), sel_customer_id,
                                "0", "","", str_event_start_date + " " + str_event_start_time, str_name, mobile, village, str_address, str_fieldarea, check_in_time,
                                comments, 1, 0, str_event_name, str_event_end_date + " " + str_event_end_date, str_purpose, str_venue, str_participants,userId, userId, order_date, order_date, 0));
                    }
                    if(role!=Constants.Roles.ROLE_7 ){
                        db.addEVM(new Employe_visit_management_pojo("", type, geo_tracking_id, Integer.parseInt(sel_userId), sel_customer_id,
                                "0", "","", str_event_start_date + " " + str_event_start_time, str_name, mobile, village, str_address, str_fieldarea, check_in_time,
                                comments, 1, 0, str_event_name, str_event_end_date + " " + str_event_end_date, str_purpose, str_venue, str_participants,userId, userId, order_date, order_date, 0));
                    }

                    if(Common.haveInternet(getApplicationContext())){
                        new Async_UpdatePlaner().execute();
                    }else {
                        Common.dismissProgressDialog(progressDialog1);
                        AlertDialog.Builder alert = new AlertDialog.Builder(PlanEventFormActivity.this);
                        alert.setTitle("Success");
                        alert.setMessage("Planner updated sucessfully");
                        alert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent planer = new Intent();
                                        planer.putExtra("date", str_event_start_date);
                                        planer.putExtra("i", "1");
                                        setResult(RESULT_OK,planer);
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        alert.show();
                    }

                   /* final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom_alert);


                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.tv_message);
                    if (type == 2) {
                        String planner = "Event";
                        text.setText("" + planner + " plan created sucessfully");
                    } else {

                        String planner = "Schedule";
                        text.setText("" + planner + " plan created sucessfully");
                    }
                    ImageView sucessimage = (ImageView) dialog.findViewById(R.id.iv_sucess);
                    sucessimage.setVisibility(View.VISIBLE);
                    //
                    // ImageView failureimage = (ImageView) dialog.findViewById(R.id.iv_failure);
                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();


                            try {
                                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                                if (netInfo != null) {  // connected to the internet
                                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                        // connected to wifi
                                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                                        new Async_UpdatePlaner().execute();

                                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                        // connected to the mobile provider's data plan
                                        new Async_UpdatePlaner().execute();
                                    }
                                } else {
                                    Intent planer = new Intent(getApplicationContext(), PlanerOneActivity.class);
                                    planer.putExtra("date", str_event_start_date);
                                    planer.putExtra("i", "1");
                                    startActivity(planer);
                                    finish();
                                }

                            } catch (Exception e) {
                                Log.d("Tag", e.toString());
                            }

                            *//*Intent planer = new Intent(getApplicationContext(), PlanerOneActivity.class);
                            planer.putExtra("date", str_event_start_date);
                            planer.putExtra("i", "1");
                            startActivity(planer);*//*


                        }
                    });


                    dialog.show();

*/
                }


            }
        });

       /* tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_code.setText(getIntent().getStringExtra("customer_code"));*/

        if(role==Constants.Roles.ROLE_7 ){
            ll_select.setVisibility(View.GONE);
        }
        if(role!=Constants.Roles.ROLE_7 ){
            callapi();
        }
        Log.e("user spinner id","id:="+sel_userId);
        if (sel_userId != null && sel_userId.length() > 0 && organisations != null) {

            for (SelectedCities citiez : organisations) {
                if (Integer.parseInt(citiez.getCityId()) == Integer.parseInt(sel_userId)) {
                    int index = organisations.indexOf(citiez);
                    spin_user.setSelection(index);
                    break;
                }
            }
        }
    }
    private void callapi() {
        organisations = new ArrayList<SelectedCities>();
        adaptercity = new ArrayList<String>();
        organisations.clear();
        SelectedCities citiez = new SelectedCities();
        citiez.setCityId("0");
        citiez.setCityName("Select User");
        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
        organisations.add(citiez);
        adaptercity.add("Select User");
        try {

            String selectQuery = "SELECT "+ KEY_TABLE_USERS_FIRST_NAME + ","+ KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  user_id in ("+userId+","+team+")";
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    Users users = new Users();

                    users.setUserMasterID(cursor.getString(1));
                    users.setUser_first_name(cursor.getString(0));

                    SelectedCities cities2 = new SelectedCities();
                    cities2.setCityId(cursor.getString(1));
                    cities2.setCityName(cursor.getString(0));
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    organisations.add(cities2);
                    adaptercity.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        }

        spin_user.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));
        spin_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_userId = organisations.get(position).getCityId();
                Log.e("user spinner id",sel_userId);
                if (userId.equalsIgnoreCase("0")) {

                } else {
                    new AsyncUsers().execute();
                    ll_select.setVisibility(View.VISIBLE);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



       /* tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_code.setText(getIntent().getStringExtra("customer_code"));*/



    }



    private void setplanvisittypeData() {

        final ArrayList<Visittypes> visittypeList = new ArrayList<>();

        visittypeList.add(new Visittypes(" -- Select -- ", "Please Select Visit Type"));
        visittypeList.add(new Visittypes("1", "Distributor"));
        visittypeList.add(new Visittypes("2", "Retailer"));
        visittypeList.add(new Visittypes("3", "Farmer"));


        final ArrayList<String> visittypecustomerpurposeList = new ArrayList<>();
        //Add countries
        visittypecustomerpurposeList.add(" Select Purpose ");
        visittypecustomerpurposeList.add("Payment Collection");
        visittypecustomerpurposeList.add("ABS");
        visittypecustomerpurposeList.add("Sales order");
        visittypecustomerpurposeList.add("Stock Information");

        final ArrayList<Visittypes> visittyperetailerpurposeList = new ArrayList<>();
        //Add countries
        visittyperetailerpurposeList.add(new Visittypes(" -- Select -- ", " Select Purpose "));
        visittyperetailerpurposeList.add(new Visittypes("7", "POG"));

        final ArrayList<Visittypes> visittypefarmerpurposeList = new ArrayList<>();
        //Add countries
        visittypefarmerpurposeList.add(new Visittypes(" -- Select -- ", " Select Purpose "));
        visittypefarmerpurposeList.add(new Visittypes("2", "Feedback"));
        visittypefarmerpurposeList.add(new Visittypes("3", "Complaint"));


        //fill data in spinner
       /* ArrayAdapter<Filminstitutes> adapter = new ArrayAdapter<Filminstitutes>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countryList);
        spin_filminstitute.setAdapter(adapter);*/

        final ArrayAdapter adapter = new ArrayAdapter<Visittypes>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, visittypeList);
        spin_visittypes.setAdapter(adapter);
        spin_visittypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Visittypes country = (Visittypes) parent.getSelectedItem();

                ///Toast.makeText(getApplicationContext(), "Institute id: "+country.getId()+",  Institute Name : "+country.getName(), Toast.LENGTH_SHORT).show();
                str_sel_visittypeid = country.getId();
                if (str_sel_visittypeid.equalsIgnoreCase("1")) {

                   // ArrayAdapter adaptercustomerpurpose = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, visittypecustomerpurposeList.toArray(new String[0]));
                    spin_purpose.setItems(visittypecustomerpurposeList);
                    spin_customers.setVisibility(View.VISIBLE);
                    et_event_name.setVisibility(View.GONE);
                    et_address.setVisibility(View.GONE);
                    et_fieldarea.setVisibility(View.GONE);
                } else if (str_sel_visittypeid.equalsIgnoreCase("2")) {
                    ArrayAdapter adapterretailerpurpose = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, visittyperetailerpurposeList.toArray(new String[0]));
                    spin_purpose.setItems(visittyperetailerpurposeList.toArray(new String[0]));
                    spin_customers.setVisibility(View.GONE);
                    et_event_name.setVisibility(View.VISIBLE);
                    et_address.setVisibility(View.VISIBLE);
                    et_fieldarea.setVisibility(View.VISIBLE);
                } else if (str_sel_visittypeid.equalsIgnoreCase("3")) {
                    ArrayAdapter adapterfarmerpurpose = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, visittypefarmerpurposeList.toArray(new String[0]));
                    spin_purpose.setItems(visittypefarmerpurposeList.toArray(new String[0]));
                    spin_customers.setVisibility(View.GONE);
                    et_event_name.setVisibility(View.VISIBLE);
                    et_address.setVisibility(View.VISIBLE);
                    et_fieldarea.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spin_purpose.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {

                StringBuffer buffer = new StringBuffer();


               /* for(Integer index : indices){
                    Filminstitutes country = (Filminstitutes) languagesList.get(index);
                    str_sel_languagesknown = country.getId();

                    if(!str_sel_languagesknown.equals(" -- Select -- "))
                        buffer.append(str_sel_languagesknown);

                    edtv_langvalidate.setError(null);

                    //if the value is not the last element of the list
                    //then append the comma(,) as well
                    if (!index.equals(indices.size()-1)){
                        buffer.append(",");
                    }
                }
                str_sel_languagesknown = buffer.toString();*/
            }

            @Override
            public void selectedStrings(List<String> strings) {

            }
        });



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
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class AsyncUsers extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanEventFormActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_users = new ArrayList<SelectedCities>();
            adapter_users = new ArrayList<String>();
            arlist_users.clear();

            SelectedCities citiesz = new SelectedCities();
            citiesz.setCityId("0");
            citiesz.setCityName(Common.getStringResourceText(R.string.select_distributor));
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_users.add(citiesz);
            adapter_users.add(Common.getStringResourceText(R.string.select_distributor));


            try {


                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";

                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Customers customers = new Customers();

                        customers.setCusMasterID(cursor.getString(0));

                        customers.setCusName(cursor.getString(1));
                        // customers.setUser_last_name(cursor.getString(2));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1) + " " + cursor.getString(2));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_users.add(cities2);
                        adapter_users.add(cursor.getString(1) + " " + cursor.getString(2));


                    } while (cursor.moveToNext());
                }

                  /*for (int l=0; l<=cropids.size();l++){
                      sdbr =db.getReadableDatabase();

                      Cursor cursors = sdbr.query(TABLE_CROPS, new String[] { KEY_TABLE_CROPS_CROP_ID,
                                      KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME }, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                              new String[] { String.valueOf(cropids.get(l).get("crop_id")) }, null, null, null, null);
                      if (cursors != null)
                          cursors.moveToFirst();

                      Crops crops = new Crops(Integer.parseInt(cursors.getString(0)),cursors.getString(1), cursors.getString(2), cursors.getString(3), cursors.getString(4), cursors.getString(5), cursors.getString(6),cursors.getString(7));
                      // return contact
                      Log.v("Cropidis xyz",cursors.getString(1)+cursors.getString(2));
                  }*/


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
            // adapter.updateResults(arrayList);
            spin_customers.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_users));
            spin_customers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_customer_id = Integer.parseInt(arlist_users.get(position).getCityId());
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    public class Async_UpdatePlaner extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanEventFormActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
            //Toast.makeText(getApplicationContext(),"Updateing Advance Bookings",Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {


            String selectQuery = null;
            if(role==Constants.Roles.ROLE_7 ){
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
                        + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + "=" + userId + " and " + KEY_EMP_FFM_ID + "=0" + " and " + KEY_EMP_TYPE + "=2";

            }

            else if(role!=Constants.Roles.ROLE_7 ){
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
                        + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_CREATED_BY+ " = " + userId +  " and " + KEY_EMP_FFM_ID + "=0" + " and " + KEY_EMP_TYPE + "=2";

            }

            Log.e("selectQuery", selectQuery);
            sdbw = db.getWritableDatabase();


            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {


                    //Log.e(" +++ Values +++ ", "type :" + cursor.getString(5)+"concern person name :" + cursor.getString(0) + " : " + cursor.getString(6) + " : " + cursor.getString(21) + ": checkintime" + cursor.getString(13) + ": sqlite id" + cursor.getString(27));
                    Log.e(" +++ Values +++ ", "concern person name :" + cursor.getString(0) + "type :" + cursor.getString(5));

                    try {


                        OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                        RequestBody formBody = new FormEncodingBuilder()
                                .add("user_id", cursor.getString(7))
                                .add("type", cursor.getString(5))
                                .add("geo_tracking_id", cursor.getString(6))
                                .add("customer_id", cursor.getString(8))
                                .add("visit_plan_type", cursor.getString(1))
                                .add("purpose_visit", cursor.getString(4))
                                .add("plan_date_time", cursor.getString(3))
                                .add("concern_person_name", "" + cursor.getString(0))
                                .add("mobile", cursor.getString(9))
                                .add("village", cursor.getString(10))
                                .add("location_address", cursor.getString(11))
                                .add("field_area", cursor.getString(12))
                                .add("id", cursor.getString(27))
                                .add("comments", cursor.getString(14))
                                .add("status", cursor.getString(2))
                                .add("approval_status", cursor.getString(15))
                                .add("event_name", cursor.getString(26))
                                .add("event_end_date", cursor.getString(16))
                                .add("event_purpose", cursor.getString(17))
                                .add("event_venue", cursor.getString(19))
                                .add("event_participants", cursor.getString(20))
                                .add("created_by", cursor.getString(21))
                                .add("updated_by", cursor.getString(22))
                                .build();

                        Response responses = null;


                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                        Request request = new Request.Builder()
                                .url(Constants.URL_INSERTING_EMP_VISIT_MANAGEMENT)
                                .post(formBody)
                                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .build();


                        try {
                            responses = client.newCall(request).execute();
                            jsonData  = responses.body().string();
                            System.out.println("!!!!!!!1 Planner inserting" + jsonData);

                            if (jsonData != null ) {
                                JSONArray jsonarray;
                                try {
                                    JSONObject jsonobject = new JSONObject(jsonData);
                                    String status = jsonobject.getString("status");
                                    if (status.equalsIgnoreCase("success")) {


                                        sqliteid = jsonobject.getInt("sqlite");
                                        ffmid = jsonobject.getInt("ffm_id");

                                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                        sql = db.getWritableDatabase();
                                        // updatecomplaints
                                        String updatequery = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_FFM_ID + " = " + ffmid + " WHERE " + KEY_EMP_VISIT_ID + " = " + sqliteid;
                                        sql.execSQL(updatequery);

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.e("ServiceHandler","Couldn't get any data from the url");
                                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                } while (cursor.moveToNext());
            } else Log.d("LOG", "returned null!");


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Common.dismissProgressDialog(progressDialog1);
            AlertDialog.Builder alert = new AlertDialog.Builder(PlanEventFormActivity.this);
            alert.setTitle("Success");
            alert.setMessage("Planner updated sucessfully");
            alert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Intent planer = new Intent();
                            planer.putExtra("date", str_event_start_date);
                            planer.putExtra("i", "1");
                            setResult(RESULT_OK,planer);
                            dialog.dismiss();
                            finish();
                        }
                    });
            alert.show();


        }
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBxCip_717NsFx2dnMQsY5zm6t5ywD4wek";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            ParserTask parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            et_venue.setAdapter(adapter);

            et_venue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

        }
    }
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exceptionadinl", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public String convertlistToString(List<String> experienceList){
        //The string builder used to construct the string
        StringBuilder commaSepValueBuilder = new StringBuilder();
        String experience_key = "";
        //Looping through the list
        for ( int i = 0; i< experienceList.size(); i++){
            //append the value into the builder
            commaSepValueBuilder.append(experienceList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if ( i != experienceList.size()-1){
                commaSepValueBuilder.append(",");
            }
        }
        return commaSepValueBuilder.toString();
    }
}
