package com.nsl.app;


import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nsl.app.commonutils.Common;
import com.nsl.app.enums.PlannerEnum;
import com.nsl.app.view.MultiSelectionSpinner;
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
import static com.nsl.app.DatabaseHandler.KEY_EMP_PURPOSE_VISIT_IDS;
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
public class PlanScheduleFormActivity extends AppCompatActivity {
    // JSON parser class

    ProgressDialog progressDialog;


    TextView tv_name, tv_code;
    Spinner spin_visittypes, spin_user, spin_customers;
    EditText et_time, et_name;
    AutoCompleteTextView et_address;
    EditText et_fieldarea;
    Button btn_savebooing;

    String sel_crop_id, str_name, str_address, str_fieldarea, sel_product_id, sel_scheme_id, str_time, company_id, order_date, str_date;
    String village = "", location_address, field_area, check_in_time, comments = "", str_sel_visittypeid, str_sel_purposeid;
    int sel_customer_id, type, geo_tracking_id, mobile, status = 1, customer_id;
    MultiSelectionSpinner spin_lanuagesknown;
    private ArrayList<SelectedCities> arlist_users;
    private ArrayList<Filminstitutes> languagesList;
    ArrayList<String> adapter_users;
    final Context context = this;
    private LinearLayout ll_select;
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

    private ArrayList<Users> userses;
    ArrayList<String> adapterusers;

    DateFormat dateFormat, orderdateFormat;
    Date myDate;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tvInvisibleError;
    DatabaseHandler db;
    SQLiteDatabase sql, sdbw, sdbr;
    private TextView textView_start_date;
    MultiSelectionSpinner spin_purpose;
    private String str_sel_purposetypes;
    private String userName;
    private List<PlannerEnum> visitersEnumList;
    private String visiterIds;
    private ProgressDialog progressDialog1;
    private boolean isDirectCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_schedule);
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
        str_date = getIntent().getExtras().getString("startDate");


        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        spin_visittypes = (Spinner) findViewById(R.id.spin_visittype);
        spin_purpose = (MultiSelectionSpinner) findViewById(R.id.spin_purpose);
        spin_user = (Spinner) findViewById(R.id.spin_user);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        spin_customers = (Spinner) findViewById(R.id.spin_customers);
        tvInvisibleError= (TextView)findViewById(R.id.tvInvisibleError);
        et_time = (EditText) findViewById(R.id.et_time);
        et_name = (EditText) findViewById(R.id.et_name);
        et_address = (AutoCompleteTextView) findViewById(R.id.et_address);
        et_fieldarea = (EditText) findViewById(R.id.et_fieldarea);
        textView_start_date = (TextView) findViewById(R.id.start_date);


        textView_start_date.setText(str_date + "   >  SCHEDULE");
        if (role != Constants.Roles.ROLE_7) {
            sel_userId = getIntent().getExtras().getString("sel_userId");
            userName = getIntent().getExtras().getString("sel_userName");
            isDirectCustomers=getIntent().getBooleanExtra("isDirectCustomers",false);

            //textView_start_date.setText(userName+" > "+str_date+" > SCHEDULE");
            textView_start_date.setText(userName + ">" + str_date + " > SCHEDULE");
        }
        dateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        orderdateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        myDate = new Date();


        setplanvisittypeData();


        et_address.setThreshold(1);

        et_address.addTextChangedListener(new TextWatcher() {

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


        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PlanScheduleFormActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

                        et_time.setText(sHour + ":" + sMinute + ":00");
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

                str_time = et_time.getText().toString();
                str_name = et_name.getText().toString();
                str_address = et_address.getText().toString();
                str_fieldarea = et_fieldarea.getText().toString();

                if (str_sel_visittypeid.equalsIgnoreCase("2") || str_sel_visittypeid.equalsIgnoreCase("3")) {


                    if (TextUtils.isEmpty(str_name) || str_name.length() > 0 && str_name.startsWith(" ")) {
                        et_name.requestFocus();
                        et_name.setError("Please enter name");
                        return;
                    }

                    if (TextUtils.isEmpty(str_address) || str_address.length() > 0 && str_address.startsWith(" ")) {
                        et_address.requestFocus();
                        et_address.setError("Please enter address");

                        return;
                    }
                    if (TextUtils.isEmpty(str_fieldarea) || str_fieldarea.length() > 0 && str_fieldarea.startsWith(" ")) {
                        et_fieldarea.requestFocus();
                        et_fieldarea.setError("Please enter fieldarea");

                        return;
                    }
                }

               /* if (TextUtils.isEmpty(str_date) || str_date.length() > 0 && str_date.startsWith(" ")) {
                    et_date.setError("Please select date");
                    et_date.requestFocus();
                    return;
                }*/
                if (spin_visittypes.getSelectedItem().toString().trim().equalsIgnoreCase("Please select visit type")) {

                    // Toast.makeText(getActivity(), "Please select crop", Toast.LENGTH_SHORT).show();
                    tvInvisibleError.requestFocus();
                    tvInvisibleError.setError("Please select visit type");
                    return;

                }if ((str_sel_visittypeid.equalsIgnoreCase("1"))&&(spin_customers.getSelectedItemPosition()==0)) {

                    // Toast.makeText(getActivity(), "Please select crop", Toast.LENGTH_SHORT).show();
                    tvInvisibleError.requestFocus();
                    tvInvisibleError.setError("Please select distributor");
                    return;

                }
                if (TextUtils.isEmpty(str_time) || str_time.length() > 0 && str_time.startsWith(" ")) {
                    et_time.setError("Please select time");
                    et_time.requestFocus();
                    return;
                } if(spin_purpose.getSelectedStrings() == null || spin_purpose.getSelectedStrings().size() < 1){
                    tvInvisibleError.requestFocus();
                    tvInvisibleError.setError("Please select purpose");
                }
             else {
                    Common.disableClickEvent(btn_savebooing,handler);
                    //new Async_submitadvancepayment().execute();
                    order_date = dateFormat.format(myDate);
                    Log.d("Insert: ", "Inserting event .."+PlanScheduleFormActivity.this.getApplicationContext().toString());
                    progressDialog1=Common.showProgressDialog(PlanScheduleFormActivity.this);

                    // For Inserting order into service order table
                    Log.d("Insert: ", "Inserting event ..");
                    // db.addEVM(new Employe_visit_management_pojo("",userId,sel_customer_id, str_sel_visittypeid, str_sel_purposeid, str_date, str_name, mobile, village, str_address, str_fieldarea, check_in_time, comments, "1","",type,userId,userId,order_date,order_date,geo_tracking_id));
                    // custom dialog

                    if (spin_purpose.getSelectedStrings() != null && spin_purpose.getSelectedStrings().size() > 0) {

                        str_sel_purposetypes = convertlistToString(spin_purpose.getSelectedStrings());

                        Log.d("str_sel_purposetypes", str_sel_purposetypes.toString());
                    }
                    visiterIds = getVisiterIdByName(spin_purpose.getSelectedStrings());
                    Log.d("str_sel_purposetypes", visiterIds);
                    if (role == Constants.Roles.ROLE_7) {
                        db.addEVM(new Employe_visit_management_pojo("", type, geo_tracking_id, Integer.parseInt(userId), sel_customer_id,
                                str_sel_visittypeid, str_sel_purposetypes, visiterIds, str_date + " " + str_time, str_name, mobile, village, str_address, str_fieldarea, check_in_time,
                                comments, 1, 0, "", "", "", "", "", userId, userId, order_date, order_date, 0));
                    } else if (role != Constants.Roles.ROLE_7) {
                        db.addEVM(new Employe_visit_management_pojo("", type, geo_tracking_id, Integer.parseInt(sel_userId), sel_customer_id,
                                str_sel_visittypeid, str_sel_purposetypes, visiterIds, str_date + " " + str_time, str_name, mobile, village, str_address, str_fieldarea, check_in_time,
                                comments, 1, 1, "", "", "", "", "", userId, userId, order_date, order_date, 0));
                    }

                    if (Common.haveInternet(getApplicationContext())) {
                        updateplanner();

                    } else {
                        Common.dismissProgressDialog(progressDialog1);

                        AlertDialog.Builder alert = new AlertDialog.Builder(PlanScheduleFormActivity.this);
                        alert.setTitle("Success");
                        alert.setMessage("Planner updated sucessfully");
                        alert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent planer = new Intent();
                                        planer.putExtra("date", str_date);
                                        planer.putExtra("i", "1");
                                        setResult(Activity.RESULT_OK,planer);
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        alert.show();
                    }

/*
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom_alert);


                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.tv_message);
                    if (type == 2) {
                        String planner = "Event";
                        text.setText("" + planner + " plan created sucessfully");
                    } else if (type == 1) {

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





                        }
                    });


                    dialog.show();


                */
             }


            }
        });
        if (role == Constants.Roles.ROLE_7) {
            ll_select.setVisibility(View.GONE);

        } else if (role != Constants.Roles.ROLE_7) {

            callapi();
        }

        if (sel_userId != null && sel_userId.length() > 0 && userses != null) {

            for (Users users : userses) {
                if (users.getID() == Integer.parseInt(sel_userId)) {
                    int index = userses.indexOf(users);
                    spin_user.setSelection(index);
                    break;
                }
            }
        }
        // new Async_getalloffline().execute();
    }

    private void callapi() {
        userses = new ArrayList<Users>();
        adapterusers = new ArrayList<String>();
        userses.clear();
        Users user = new Users();
        /*user.setID(0);
        user.setUser_first_name("Selef");
        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
        userses.add(user);
        adapterusers.add("Self");*/
        try {

            String selectQuery;
            if (!isDirectCustomers) {
                String[] newTeam = team.split(",");
                StringBuilder sb = new StringBuilder();
                for (String a : newTeam) {
                    if (!Common.getUserIdFromSP(getApplicationContext()).equalsIgnoreCase(a)) {
                        if (sb.toString().length() > 0) {
                            sb.append(",");
                        }

                        sb.append(a);
                    }
                }
                String teamnew = sb.toString();
                selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  user_id in (" + teamnew + ")";
            } else {

                selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  user_id in (" + team + ")";

            }



          //  String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  user_id in (" + userId + "," + team + ")";
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                   /* Users users = new Users();

                    users.setUserMasterID(cursor.getString(1));
                    users.setUser_first_name(cursor.getString(0));*/

                    Users userDEtail = new Users();
                    userDEtail.setID(Integer.parseInt(cursor.getString(1)));
                    userDEtail.setUser_first_name(cursor.getString(0));
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    userses.add(userDEtail);
                    adapterusers.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        }

        spin_user.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapterusers));
        spin_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_userId = String.valueOf(userses.get(position).getID());
                Log.e("user spinner id",sel_userId);
                tvInvisibleError.setError(null);
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

    private void updateplanner() {
        String selectQuery = null;
        if (role == Constants.Roles.ROLE_7) {
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
                    + KEY_EMP_VISIT_ID + ","
                    + KEY_EMP_PURPOSE_VISIT_IDS
                    + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " = " + userId + " and " + KEY_EMP_FFM_ID + " = 0" + " and " + KEY_EMP_TYPE + "= 1";
        } else if (role != Constants.Roles.ROLE_7) {
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
                    + KEY_EMP_VISIT_ID + ","
                    + KEY_EMP_PURPOSE_VISIT_IDS
                    + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_CREATED_BY + " = " + userId + " and " + KEY_EMP_FFM_ID + " = 0" + " and " + KEY_EMP_TYPE + "= 1";
        }


        Log.e("selectQuery", selectQuery);
        sdbw = db.getWritableDatabase();


        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {

            Log.e(" +++ Values +++ ", "type :" + cursor.getString(5) + " : " + cursor.getString(6) + " : " + cursor.getString(21) + ": checkintime" + cursor.getString(13) + ": sqlite id" + cursor.getString(27) + " 28: " + cursor.getString(28));

           if (Common.haveInternet(getApplicationContext())){
                new Async_UpdatePlaner().execute(String.valueOf(cursor.getString(5)), String.valueOf(Integer.parseInt(cursor.getString(6))), cursor.getString(7), cursor.getString(8), cursor.getString(1), cursor.getString(28), cursor.getString(3), cursor.getString(0), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(27), cursor.getString(14), cursor.getString(2), cursor.getString(15), cursor.getString(26), cursor.getString(16), cursor.getString(17), cursor.getString(19), cursor.getString(20), cursor.getString(22), cursor.getString(23));
            }

            //while (cursor.moveToNext());
        } else Log.d("LOG", "returned null!");


    }


    private void setplanvisittypeData() {

        final ArrayList<Visittypes> visittypeList = new ArrayList<>();

        visittypeList.add(new Visittypes(" -- Select -- ", "Please Select Visit Type"));
        visittypeList.add(new Visittypes("1", "Distributor"));
        visittypeList.add(new Visittypes("2", "Retailer"));
        visittypeList.add(new Visittypes("3", "Farmer"));


        final ArrayList<Visittypes> visittypecustomerpurposeList = new ArrayList<>();
        //Add countries
        visittypecustomerpurposeList.add(new Visittypes("0", " Select Purpose "));
       /* visittypecustomerpurposeList.add(new Visittypes("1", "Payment Collection"));
        visittypecustomerpurposeList.add(new Visittypes("6", "ABS"));
        visittypecustomerpurposeList.add(new Visittypes("5", "Sales order"));
        visittypecustomerpurposeList.add(new Visittypes("4", "Stock Information"));*/

        visittypecustomerpurposeList.add(new Visittypes("1", "Payment Collection"));
        visittypecustomerpurposeList.add(new Visittypes("6", "Marketing"));
        visittypecustomerpurposeList.add(new Visittypes("5", "Sales order"));
        visittypecustomerpurposeList.add(new Visittypes("4", "Stock Supply"));
        visittypecustomerpurposeList.add(new Visittypes("4", "Market Intelligence"));


        languagesList = new ArrayList<>();
        //Add countries
        //languagesList.add(new Filminstitutes(" -- Select -- ", " -- Select -- "));
        languagesList.add(new Filminstitutes(" -- Select -- ", " Select Purpose "));
        languagesList.add(new Filminstitutes("1", "Payment Collection"));
        languagesList.add(new Filminstitutes("6", "ABS"));
        languagesList.add(new Filminstitutes("5", "Sales order"));
        languagesList.add(new Filminstitutes("4", "Stock Information"));


        final ArrayList<Visittypes> visittyperetailerpurposeList = new ArrayList<>();
        //Add countries
        visittyperetailerpurposeList.add(new Visittypes("0", " Select Purpose "));
        visittyperetailerpurposeList.add(new Visittypes("7", "POG"));

        final ArrayList<Visittypes> visittypefarmerpurposeList = new ArrayList<>();
        //Add countries
        visittypefarmerpurposeList.add(new Visittypes(" -- Select -- ", " Select Purpose "));
        visittypefarmerpurposeList.add(new Visittypes("3", "Feedback"));
        visittypefarmerpurposeList.add(new Visittypes("2", "Complaint"));


        //fill data in spinner
       /* ArrayAdapter<Filminstitutes> adapter = new ArrayAdapter<Filminstitutes>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countryList);
        spin_filminstitute.setAdapter(adapter);*/

        final ArrayAdapter adapter = new ArrayAdapter<Visittypes>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, visittypeList);
        spin_visittypes.setAdapter(adapter);
        spin_visittypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvInvisibleError.setError(null);
                Visittypes country = (Visittypes) parent.getSelectedItem();

                ///Toast.makeText(getApplicationContext(), "Institute id: "+country.getId()+",  Institute Name : "+country.getName(), Toast.LENGTH_SHORT).show();
                str_sel_visittypeid = country.getId();
                if (str_sel_visittypeid.equalsIgnoreCase("1") || str_sel_visittypeid.equalsIgnoreCase("2") || str_sel_visittypeid.equalsIgnoreCase("3")) {
                    visitersEnumList = PlannerEnum.getListByVisitTypeID(Integer.parseInt(str_sel_visittypeid));
                    Log.d("visiterList", visitersEnumList.toString());
                    ArrayList<String> visittypecustomerpurposeList = new ArrayList<>();
                    visittypecustomerpurposeList.clear();
                    for (PlannerEnum visitersEnum : visitersEnumList) {
                        visittypecustomerpurposeList.add(visitersEnum.getTitle());

                    }

                    new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, visittypecustomerpurposeList.toArray(new String[0]));

                    spin_purpose.setItems(visittypecustomerpurposeList);

                }


                if (str_sel_visittypeid.equalsIgnoreCase("1")) {

                    new AsyncUsers().execute();


                    spin_customers.setVisibility(View.VISIBLE);
                    et_name.setVisibility(View.GONE);
                    et_address.setVisibility(View.GONE);
                    et_fieldarea.setVisibility(View.GONE);
                } else if (str_sel_visittypeid.equalsIgnoreCase("2")) {

              spin_customers.setVisibility(View.GONE);
                    et_name.setVisibility(View.VISIBLE);
                    et_address.setVisibility(View.VISIBLE);
                    et_fieldarea.setVisibility(View.VISIBLE);
                } else if (str_sel_visittypeid.equalsIgnoreCase("3")) {


                    spin_customers.setVisibility(View.GONE);
                    et_name.setVisibility(View.VISIBLE);
                    et_address.setVisibility(View.VISIBLE);
                    et_fieldarea.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

       /* spin_purpose.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {
                Log.d("selectedStrings", indices.toString() );
            }

            @Override
            public void selectedStrings(List<String> strings) {
                Log.d("selectedStrings", strings.toString() );
            }
        });
        spin_purpose.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {

                StringBuffer buffer = new StringBuffer();
                Log.d("selectedStrings", indices.toString() );

               *//* for(Integer index : indices){
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
                str_sel_languagesknown = buffer.toString();*//*
            }

            @Override
            public void selectedStrings(List<String> strings) {
                visiterIds = getVisiterIdByName(strings);
                Log.d("selectedStrings", strings.toString() + "\n" + getVisiterIdByName(strings));

            }
        });
*/

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
            progressDialog = new ProgressDialog(PlanScheduleFormActivity.this);
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
                String selectQuery = null;
                if (role == Constants.Roles.ROLE_7) {
                    selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";

                }
                if (role != Constants.Roles.ROLE_7) {

                    selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + sel_userId + " group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
                }


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
            /*progressDialog = new ProgressDialog(PlanScheduleFormActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
            //Toast.makeText(getApplicationContext(),"Updateing Advance Bookings",Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()

                        .add("type", params[0])
                        .add("geo_tracking_id", params[1])
                        .add("user_id", params[2])
                        .add("customer_id", params[3])
                        .add("visit_plan_type", params[4])
                        .add("purpose_visit", params[5])
                        .add("plan_date_time", params[6])
                        .add("concern_person_name", params[7])
                        .add("mobile", params[8])
                        .add("village", params[9])
                        .add("location_address", params[10])
                        .add("field_area", params[11])
                        .add("id", params[12])
                        .add("comments", params[13])
                        .add("status", params[14])
                        .add("approval_status", params[15])
                        .add("event_name", params[16])
                        .add("event_end_date", params[17])
                        .add("event_purpose", params[18])
                        .add("event_venue", params[19])
                        .add("event_participants", params[20])
                        .add("created_by", params[21])
                        .add("updated_by", params[22])
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
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 Planner inserting" + jsonData);
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
            Common.dismissProgressDialog(progressDialog1);

            if (jsonData != null) {
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(PlanScheduleFormActivity.this);
                        alert.setTitle("Success");
                        alert.setMessage("Planner updated sucessfully");
                        alert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent planer = new Intent();
                                        planer.putExtra("date", str_date);
                                        planer.putExtra("i", "1");
                                        dialog.dismiss();
                                        setResult(Activity.RESULT_OK,planer);
                                        finish();
                                    }
                                });
                        alert.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Intent planer = new Intent();
                    planer.putExtra("date", str_date);
                    planer.putExtra("i", "1");
                    setResult(Activity.RESULT_OK,planer);
                    finish();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
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
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exceptionadinl", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBxCip_717NsFx2dnMQsY5zm6t5ywD4wek";

            String input = "";

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
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
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

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            et_address.setAdapter(adapter);

            et_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

        }
    }

    public String convertlistToString(List<String> experienceList) {
        //The string builder used to construct the string
        StringBuilder commaSepValueBuilder = new StringBuilder();
        String experience_key = "";
        //Looping through the list
        for (int i = 0; i < experienceList.size(); i++) {
            //append the value into the builder
            commaSepValueBuilder.append(experienceList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if (i != experienceList.size() - 1) {
                commaSepValueBuilder.append(",");
            }
        }
        return commaSepValueBuilder.toString();
    }


    public static String getVisiterIdByName(List<String> names) {
        ArrayList<String> list = new ArrayList();
        list.clear();
        for (int i = 0; i < names.size(); i++) {
            for (PlannerEnum plannerEnum : PlannerEnum.values()) {
                String visitTitle = plannerEnum.getTitle();

                if (names.get(i).equalsIgnoreCase(visitTitle)) {
                    list.add(String.valueOf(plannerEnum.getRoleId()));

                }


            }


        }

        String joined = TextUtils.join(",", list);
        return joined;
    }


}
