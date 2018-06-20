package com.nsl.app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.nsl.app.commonutils.Common;
import com.nsl.app.scheduler.LocationService;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_ADDRESS;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_UPDATED_STATUS;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.app.DatabaseHandler.TABLE_GEO_TRACKING;
import static com.nsl.app.DatabaseHandler.TABLE_USERS;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanerOneActivity extends AppCompatActivity {


    public static final String mypreference = "mypref";
    private static final int REQUEST_CHECK_SETTINGS = 111;
    private static final int REQUEST_MAP_CLICKED = 222;
    private static final int PLANNER_ONE_ACTIVITY = 333;
    public static boolean isSignedIn = false;
    LocationManager locationManager;
    String checkinlatlong, visit_date, trackingid, check_in_time, check_out_time, status, ffmidsqlite, ffmid, str_distance;
    GPSTracker gpsService;
    boolean mBound = false;
    String i;
    ProgressDialog progressDialog;
    String jsonData, userId, companyinfo, chkevm, sel_company_id, userName;
    int role;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    TextView tv_date, tv_noresults;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    FloatingActionButton fab;
    DateFormat dateFormat;
    SimpleDateFormat sdf;
    Date myDate;
    RelativeLayout checkinlayout;
    DateFormat timeFormat, currenttimeFormat;
    Date mytime;
    Date checkout, currenttime;
    String datefromcalander, dateString, eventdate, datefromrecords, cname, caddress;
    boolean iScheckedInClicked = false;
    ArrayList<String> adaptercity;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    private String tid, team;
    private int approval_status;
    private LinearLayout ll_select;
    private Spinner selectDistributor;
    private ArrayList<SelectedCities> organisations;
    private String emp_visit_id, sql_id, event_approval_status;
    private ImageButton btn_add;
    private String strDate;
    private boolean isNoUsers = false;
    private boolean isDirectCustomers=false;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GPSTracker.GpsBinder binder = (GPSTracker.GpsBinder) service;
            gpsService = binder.getService();
            mBound = true;

            if (iScheckedInClicked && gpsService.canGetLocation) {
                iScheckedInClicked = false;
                double latitude = gpsService.getLatitude();
                double longitude = gpsService.getLongitude();
                checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude);

                //checkin();
            }

            if (!gpsService.canGetLocation) {
                gpsService.getLocation();
                if (!gpsService.canGetLocation)
                    gpsService.showSettingsAlert(PlanerOneActivity.this);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannerone);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        chkevm = sharedpreferences.getString("evm", "");
        companyinfo = sharedpreferences.getString("companyinfo", "");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        i = getIntent().getStringExtra("i");
        selectDistributor = (Spinner) findViewById(R.id.spin_user);
        btn_add = (ImageButton) findViewById(R.id.btn_add);
        if (i.equalsIgnoreCase("1")) {
            datefromcalander = getIntent().getStringExtra("date");
        } else {
            datefromcalander = PlanerMainActivity.curDate;
        }

        if (isSignedIn) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PlanerOneActivity.this, GPSTracker.class);
                    startService(intent);
                }
            }).start();

        }

        db = new DatabaseHandler(this);
        long date = System.currentTimeMillis();
        sdf = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        dateString = sdf.format(date);
        SimpleDateFormat sdfs = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        eventdate = sdfs.format(date);
        dateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        myDate = new Date();
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        currenttimeFormat = new SimpleDateFormat("h:mm:ss aa");
        mytime = new Date();
        checkout = new Date();
        listView = (ListView) findViewById(R.id.listView);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_noresults = (TextView) findViewById(R.id.tv_noresults);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_date.setText(datefromcalander);
//        Log.e("date",datefromcalander);
        iScheckedInClicked = false;

        fab = (FloatingActionButton) findViewById(R.id.fab);


        febButtonStatus();
        if (role == Constants.Roles.ROLE_7) {
            ll_select.setVisibility(View.GONE);

            new Async_getalloffline().execute();

        } else if (role != Constants.Roles.ROLE_7) {


            new Async_getRMDirectDistributor().execute();
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNoUsers) {
                    Intent one = new Intent(getApplicationContext(), PlanerTypeActivity.class);
                    one.putExtra("date", datefromcalander);
                    one.putExtra("selecteduser", userName);
                    one.putExtra("selecteduserid", userId);
                    one.putExtra("isDirectCustomers", isDirectCustomers);
                    startActivityForResult(one, PLANNER_ONE_ACTIVITY);
                }else {
                    Toast.makeText(getApplicationContext(),"You can't create plan. Please Map distributor/Mo",Toast.LENGTH_SHORT).show();
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* fab.setEnabled(false);
                fab.setClickable(false);
*/
                Common.disableClickEvent(fab, true);
                displayGoogleLocationSettingPage(PlanerOneActivity.this, REQUEST_CHECK_SETTINGS);


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
                Intent customers = new Intent(getApplicationContext(), PlanerDetailActivity.class);
                customers.putExtra("date", datefromcalander);
                customers.putExtra("name", favouriteItem.get(i).get("event_name"));
                customers.putExtra("event_purpose", favouriteItem.get(i).get("event_purpose"));
                customers.putExtra("address", favouriteItem.get(i).get("address"));
                customers.putExtra("event_datetime", favouriteItem.get(i).get("event_datetime"));
                customers.putExtra("event_id", favouriteItem.get(i).get("event_id"));
                customers.putExtra("event_status", favouriteItem.get(i).get("event_status"));
                startActivity(customers);
                // Toast.makeText(getApplicationContext(),favouriteItem.get(i).get("master_id"),Toast.LENGTH_LONG).show();
            }
        });


    }

    private void febButtonStatus() {
        String[] checkinStatus = db.getCheckinStatus(dateString);
        Log.d("dateString", dateString + "\n datefromcalander :" + datefromcalander + "checkinStatus:" + checkinStatus);

        if (datefromcalander.equalsIgnoreCase(dateString)) {

            if (checkinStatus == null) {
                fab.setVisibility(View.VISIBLE);
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_checkin));

            } else if (checkinStatus != null && (checkinStatus[0] != ("") || checkinStatus[0] != ("null") || checkinStatus[0] != null) && (checkinStatus[1] == ("") || checkinStatus[1] == ("null") || checkinStatus[1] == (null))) {
                isSignedIn = true;
                fab.setVisibility(View.VISIBLE);
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_checkout));

            } else if (checkinStatus != null && (!checkinStatus[0].equalsIgnoreCase("") || !checkinStatus[0].equalsIgnoreCase("null") || !checkinStatus[0].equalsIgnoreCase(null)) && (!checkinStatus[1].equalsIgnoreCase("") || !checkinStatus[1].equalsIgnoreCase("null") || !checkinStatus[1].equalsIgnoreCase(null))) {
                fab.setVisibility(View.GONE);


            }


        } else {
            fab.setVisibility(View.GONE);
        }

    }


    private void checkAndProcess() {
        Log.d("getLongitude", "" + gpsService);
        if (gpsService == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkAndProcess();
                }
            }, 2000);
        } else if (gpsService != null) {
            double latitude = gpsService.getLatitude();
            double longitude = gpsService.getLongitude();
            Log.d("getLongitude", "" + latitude);
            if (latitude == 0.0 && longitude == 0.0) {
                return;
            }
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_checkout));
            isSignedIn = true;
            check_in_time = timeFormat.format(mytime);
            //  Log.e("Check_in_time",check_in_time);

            visit_date = dateFormat.format(myDate);
//
            // check if GPS enabled

            checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude);
            Toast.makeText(getApplicationContext(), String.valueOf(latitude) + String.valueOf(longitude), Toast.LENGTH_SHORT).show();

            db.addGeotracking(new Geo_Tracking_POJO("", "", userId, checkinlatlong, "", checkinlatlong, str_distance, visit_date, check_in_time, null, "", "0", visit_date, visit_date));


            if (Utility.isNetworkAvailable(PlanerOneActivity.this, Constants.isShowNetworkToast)) {
                if (gpsService.canGetLocation()) {
                    new Async_Checkin().execute();
                } else {
                    iScheckedInClicked = true;
                    //  gpsService.showSettingsAlert(PlanerOneActivity.this);
                    //  gpsService.displayGoogleLocationSettingPage(PlanerOneActivity.this);
                }
            } else {
                // Log.e("Inserted into sqlite","after");

                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_checkout));
                isSignedIn = true;

                if (gpsService != null) {
                    gpsService.startPushThread();
                }
            }
        }
        //  febButtonStatus();
    }

    private void callapi(int i) {
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
            String selectQuery;
            if (i == 0) {
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
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            System.out.println("cursor count " + cursor.getCount() + "\n selectQuery" + selectQuery);
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
            } else {
                isNoUsers = true;
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        }

        selectDistributor.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));
        selectDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userId = organisations.get(position).getCityId();
                userName = organisations.get(position).getCityName();
                if (userId.equalsIgnoreCase("0")) {
                    favouriteItem = new ArrayList<HashMap<String, String>>();
                    adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
                    Log.e("adapter ", String.valueOf(adapter.getCount()));
                    listView.setAdapter(adapter);
                } else {
                    ll_select.setVisibility(View.VISIBLE);
                    Log.e("selected user ====", userName + " Id " + userId);
                    new Async_getalloffline().execute();

                }


                if (Common.getUserIdFromSP(getApplicationContext()).equalsIgnoreCase(userId)) {
                    febButtonStatus();

                } else {

                    fab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*private void checkin() {
        try {
            if (Utility.isNetworkAvailable(PlanerOneActivity.this, Constants.isShowNetworkToast)) {  // connected to the internet

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to GpsService
        if (isSignedIn) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PlanerOneActivity.this, GPSTracker.class);
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                }
            }).start();


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
       /* if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBound) {
//            unbindService(mConnection);
//            mBound = false;
        }
    }

    private void showAproveAlertToUser(String id) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to approve this plan ?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                                    if (netInfo != null) {  // connected to the internet
                                        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                            // connected to wifi
                                            // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                                            new Async_UpdateAprovalStatus().execute();
                                        } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                            // connected to the mobile provider's data plan

                                            new Async_UpdateAprovalStatus().execute();
                                        }
                                    } else {
                                        Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    Log.d("Tag", e.toString());
                                }
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showRejectAlertToUser(String id) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to reject this plan ?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                try {
                                    ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                                    if (netInfo != null) {  // connected to the internet
                                        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                            // connected to wifi
                                            // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                                            /*sdbw = db.getWritableDatabase();
                                            String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_APPROVAL_STATUS + " = '" + "2" + "' WHERE " + KEY_EMP_VISIT_ID + " = " + sql_id;
                                            sdbw.execSQL(updatequerys);*/
                                            new Async_UpdateAprovalStatus().execute();
                                        } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                            // connected to the mobile provider's data plan
                                           /* sdbw = db.getWritableDatabase();
                                            String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_APPROVAL_STATUS + " = '" + "2" + "' WHERE " + KEY_EMP_VISIT_ID + " = " + sql_id;
                                            sdbw.execSQL(updatequerys);*/
                                            new Async_UpdateAprovalStatus().execute();
                                        }
                                    } else {
                                        Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    Log.d("Tag", e.toString());
                                }
                                //dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_planner, menu);
        MenuItem item_checkin = menu.findItem(R.id.action_checkin);
        MenuItem item_checkout = menu.findItem(R.id.action_checkout);
        item_checkin.setVisible(false);
        item_checkout.setVisible(false);
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

        if (id == R.id.action_map) {
            displayGoogleLocationSettingPage(PlanerOneActivity.this, REQUEST_MAP_CLICKED);

            return true;
        }

        if (id == R.id.action_checkin) {


        }
        if (id == R.id.action_checkout) {

        }

        return super.onOptionsItemSelected(menu);
    }

    public void displayGoogleLocationSettingPage(final Activity activity, final int requestCode) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(MyApplication.getInstance().getLocationRequest())
                .setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(MyApplication.getInstance().getGoogleApiClient(), builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                // final LocationSettingsStates s= result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //  Common.myCurrentLocationCamera(currentLocation, gMap);

                        switch (requestCode) {
                            case REQUEST_CHECK_SETTINGS:
                                fabButtonFunctionality();
                               /* fab.setEnabled(true);
                                fab.setClickable(true);*/
                                break;
                            case REQUEST_MAP_CLICKED:
                                Intent map = new Intent(getApplicationContext(), PostRouteMapActivityCopy.class);
                                startActivity(map);
                                break;
                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        //   Toast.makeText(activity, "RESOLUTION_REQUIRED", Toast.LENGTH_SHORT).show();
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, requestCode);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                       /* fab.setEnabled(true);
                        fab.setClickable(true);*/

                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        Log.d("onActivityResult", "OnresultAxtivity1");
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        fabButtonFunctionality();
                        /*fab.setEnabled(true);
                        fab.setClickable(true);*/
                        Log.d("onActivityResult", "OnresultAxtivity4");
                        break;
                    case Activity.RESULT_CANCELED:
                       /* fab.setEnabled(true);
                        fab.setClickable(true);*/
                        // The user was asked to change settings, but chose not to
                        // finish();

                        break;

                    default:
                        break;
                }
                break;

            case REQUEST_MAP_CLICKED:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Intent map = new Intent(getApplicationContext(), PostRouteMapActivityCopy.class);
                        startActivity(map);
                        Log.d("onActivityResult", "OnresultAxtivity4");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // finish();

                        break;

                    default:
                        break;
                }
                break;

            case PLANNER_ONE_ACTIVITY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        new Async_getalloffline().execute();
                        Log.d("onActivityResult", "PLANNER_ONE_ACTIVITY");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // finish();

                        break;

                    default:
                        break;
                }
                break;


        }
    }

    public void fabButtonFunctionality() {

        if (favouriteItem == null || favouriteItem.size() == 0) {
            Toast.makeText(PlanerOneActivity.this, "Please add plan", Toast.LENGTH_SHORT).show();
            return;

        }

        if (!isSignedIn) {


            Intent intent = new Intent(PlanerOneActivity.this, GPSTracker.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            startService(intent);
            /*Intent intent1 =new Intent(this, LocationService.class);
            startService(intent1);
*/
            checkAndProcess();


        } else {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
            strDate = sdftime.format(c.getTime());
            Log.e("Check_out_time", strDate);
            visit_date = dateFormat.format(myDate);
            double latitude = gpsService.getLatitude();
            double longitude = gpsService.getLongitude();
            checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude);
            Toast.makeText(getApplicationContext(), checkinlatlong, Toast.LENGTH_LONG).show();
            String selectQuerys = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + "," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + " FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + userId + " and  visit_date like '" + datefromcalander + "%'" + " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
            sdbw = db.getWritableDatabase();

            Cursor cc = sdbw.rawQuery(selectQuerys, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cc != null && cc.moveToFirst()) {
                tid = String.valueOf(cc.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0


                Log.e("++ checkout lastId ++", tid);
            }

            String updatequery = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + " = '" + checkinlatlong + "'," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + " = '" + strDate + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + tid;

            sdbw.execSQL(updatequery);
            System.out.println(updatequery);
            try {
                if (Utility.isNetworkAvailable(PlanerOneActivity.this, Constants.isShowNetworkToast)) {  // connected to the internet

                    new Async_Checkout().execute();

                } else {

                    String updatequery1 = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " = 0 ," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + " = '" + checkinlatlong + "'," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + " = '" + strDate + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + tid;

                    sdbw.execSQL(updatequery1);

                    fab.setVisibility(View.GONE);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_checkin));
                    isSignedIn = false;

                    Intent intent = new Intent(PlanerOneActivity.this, GPSTracker.class);
                    if (mBound) {
                        unbindService(mConnection);
                        mBound = false;
                    }
                    stopService(intent);

                    Intent intent1 = new Intent(PlanerOneActivity.this, LocationService.class);
                    stopService(intent1);
                    Log.d("onDestroy", "onDestroy 1.......................");
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }
    }

    private class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Please wait \n data loading from offline");
            progressDialog.show();*/
            favouriteItem.clear();
        }

        protected String doInBackground(Void... params) {


            try {

                String selectQuery = "SELECT DISTINCT "
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
                        + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " = " + userId;

                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                System.out.println("selectQuery  " + selectQuery);

                if (cursor.moveToFirst()) {
                    do {
                        datefromrecords = cursor.getString(3).substring(0, 10);

                        //Toast.makeText(getApplicationContext(), datefromrecords, Toast.LENGTH_SHORT).show();

                        HashMap<String, String> map = new HashMap<String, String>();
                        try {
                            if (sdf.parse(datefromcalander).equals(sdf.parse(datefromrecords))) {
                                // If two dates are equal.

                                Log.e("++Dates++", datefromcalander + "&" + datefromrecords);
                                // System.out.println("+++++++++++" + cursor.getString(0)+ cursor.getString(1)+ cursor.getString(2)+ cursor.getString(3));
                                String planvisittype = cursor.getString(1);
                                String emptype = cursor.getString(5);
                                if (planvisittype.equalsIgnoreCase("1")) {
                                    String cid = cursor.getString(8);


                                    String selectcQuery = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_ADDRESS + " FROM " + TABLE_CUSTOMERS + "  where " + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + cid;
                                    // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                                    sdbw = db.getWritableDatabase();

                                    Cursor ccursor = sdbw.rawQuery(selectcQuery, null);
                                    System.out.println("ccursor count " + ccursor.getCount());

                                    if (ccursor.moveToFirst()) {
                                        do {
                                            cname = ccursor.getString(0);
                                            caddress = ccursor.getString(1);

                                        } while (ccursor.moveToNext());
                                    } else {
                                        Log.e("no plans", "no plans");
                                    }

                                    map.put("event_name", cname);
                                    map.put("address", caddress);
                                    map.put("event_status", cursor.getString(2));
                                    map.put("event_approval_status", cursor.getString(15));
                                    map.put("event_customer_id", cursor.getString(8));
                                    map.put("event_datetime", cursor.getString(3));
                                    map.put("event_purpose", cursor.getString(4));
                                    map.put("event_id", cursor.getString(27));
                                    map.put("emp_visit_id", cursor.getString(21));

                                } else if (emptype.equalsIgnoreCase("2")) {
                                    Log.e("Type : ", "Event");

                                    map.put("event_name", cursor.getString(26));
                                    map.put("address", cursor.getString(19));
                                    map.put("event_status", cursor.getString(2));
                                    map.put("event_approval_status", cursor.getString(15));
                                    map.put("event_customer_id", cursor.getString(8));
                                    map.put("event_datetime", cursor.getString(3));
                                    map.put("event_purpose", cursor.getString(17));
                                    map.put("event_id", cursor.getString(27));
                                    map.put("emp_visit_id", cursor.getString(21));
                                } else {
                                    // adding each child node to HashMap key => value
                                    map.put("event_name", cursor.getString(0));
                                    map.put("address", cursor.getString(11));
                                    map.put("event_status", cursor.getString(2));
                                    map.put("event_customer_id", cursor.getString(8));
                                    map.put("event_datetime", cursor.getString(3));
                                    map.put("event_purpose", cursor.getString(4));
                                    map.put("event_id", cursor.getString(27));
                                    map.put("event_approval_status", cursor.getString(15));
                                    map.put("emp_visit_id", cursor.getString(21));
                                }

                                favouriteItem.add(map);


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

                            } else {

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } while (cursor.moveToNext());


                } else {
                    Log.e("no plans", "no plans");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
            Log.e("adapter ", String.valueOf(adapter.getCount()));
            listView.setAdapter(adapter);


        }
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> results) {
            this.context = context;
            this.results = results;

        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {

                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_planner, parent, false);
                holder.event_name = (TextView) convertView.findViewById(R.id.tv_customer);
                //holder.tv_status       = (TextView) convertView.findViewById(R.id.status);
                holder.event_purpose = (TextView) convertView.findViewById(R.id.tv_purpose);
                holder.event_status = (TextView) convertView.findViewById(R.id.tv_status);
                holder.tv_approvalstatus = (TextView) convertView.findViewById(R.id.tv_approvalstatus);
                holder.tv_accept = (TextView) convertView.findViewById(R.id.tv_accept);
                holder.tv_reject = (TextView) convertView.findViewById(R.id.tv_reject);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (role == Constants.Roles.ROLE_7 || role == Constants.Roles.ROLE_12) {
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
            } else if (results.get(position).get("event_approval_status").equalsIgnoreCase("1")) {
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.VISIBLE);
            } else if (results.get(position).get("event_approval_status").equalsIgnoreCase("2")) {
                holder.tv_accept.setVisibility(View.VISIBLE);
                holder.tv_reject.setVisibility(View.GONE);
            } else {
                holder.tv_accept.setVisibility(View.VISIBLE);
                holder.tv_reject.setVisibility(View.VISIBLE);
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.event_name.setText(results.get(position).get("event_name"));
            if (results.get(position).get("event_purpose").equalsIgnoreCase("1")) {
                holder.event_purpose.setText(Html.fromHtml("Payment Collection "));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("6")) {
                holder.event_purpose.setText(Html.fromHtml("ABS"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("5")) {
                holder.event_purpose.setText(Html.fromHtml("Sales order"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("4")) {
                holder.event_purpose.setText(Html.fromHtml("Stock Information"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("7")) {
                holder.event_purpose.setText(Html.fromHtml("POG"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("3")) {
                holder.event_purpose.setText(Html.fromHtml("Feedback"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("2")) {
                holder.event_purpose.setText(Html.fromHtml("Complaint"));
            } else {
                holder.event_purpose.setText(Html.fromHtml(results.get(position).get("event_purpose")));
            }
            holder.event_status.setText(results.get(position).get("event_status") + " U " + userId);

            if (results.get(position).get("event_status").equalsIgnoreCase("1")) {
                holder.event_status.setText("Open");
                holder.event_status.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (results.get(position).get("event_status").equalsIgnoreCase("2")) {
                holder.event_status.setText("Deviation");
                holder.event_status.setTextColor(getResources().getColor(R.color.tabselectedcolor));
            } else if (results.get(position).get("event_status").equalsIgnoreCase("3")) {
                holder.event_status.setText("In Progress");
                holder.event_status.setTextColor(getResources().getColor(R.color.tabselectedcolor));
            } else if (results.get(position).get("event_status").equalsIgnoreCase("4")) {
                holder.event_status.setText("Completed");
                holder.event_status.setTextColor(getResources().getColor(R.color.green));
            } else {

            }
            if (results.get(position).get("event_approval_status").equalsIgnoreCase("1")) {
                holder.tv_approvalstatus.setText("Approved");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.green));
            } else if (results.get(position).get("event_approval_status").equalsIgnoreCase("2")) {
                holder.tv_approvalstatus.setText("Rejected");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                holder.tv_approvalstatus.setText("Pending");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.tabselectedcolor));
            }


            holder.tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    emp_visit_id = results.get(position).get("emp_visit_id");
                    sql_id = results.get(position).get("event_id");
                    event_approval_status = "1";

                    try {
                        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                        if (netInfo != null) {  // connected to the internet
                            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                // connected to wifi
                                // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                                showAproveAlertToUser(results.get(position).get("event_id"));
                            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                // connected to the mobile provider's data plan
                                showAproveAlertToUser(results.get(position).get("event_id"));

                            }
                        } else {
                            Toast.makeText(context, "No Internet Conection ! \n Please try again   ", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("Tag", e.toString());
                    }


                }
            });
            holder.tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    emp_visit_id = results.get(position).get("emp_visit_id");
                    sql_id = results.get(position).get("event_id");
                    event_approval_status = "2";

                    try {
                        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                        if (netInfo != null) {  // connected to the internet
                            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                // connected to wifi
                                // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                                showRejectAlertToUser(results.get(position).get("event_id"));
                            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                // connected to the mobile provider's data plan
                                showRejectAlertToUser(results.get(position).get("event_id"));

                            }
                        } else {
                            Toast.makeText(context, "No Internet Conection ! \n Please try again   ", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("Tag", e.toString());
                    }

                }
            });


            Log.e("++ event status ++", results.get(position).get("event_status"));


            return convertView;
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
            Log.e("sssssssss", String.valueOf(results.size()));
            if (results.size() == 0) {
                tv_noresults.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                tv_noresults.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }

        }

        public class ViewHolder {
            public TextView event_name, event_purpose, event_status, tv_approvalstatus, tv_accept, tv_reject;

        }
    }


    private class Async_Checkin extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Submitting CheckIn");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {
            check_in_time = timeFormat.format(mytime);
            //  Log.e("Check_in_time",check_in_time);

            visit_date = dateFormat.format(myDate);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdtime = new SimpleDateFormat("HH:mm:ss");
            String strDate = sdtime.format(c.getTime());
            Log.e("Check_in_time", strDate);

            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("type", "check_in_lat_lon")
                        .add("visit_type", "1")
                        .add("user_id", userId)
                        .add("latlon", checkinlatlong)
                        .add("visit_date", visit_date)
                        .add("check_in_time", check_in_time)
                        .add("installed_apps", Common.getPackageName(PlanerOneActivity.this))
                        .build();

                Response responses = null;


              /*  MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=" + "check_in_lat_lon" + "&visit_type=" + "1" + "&user_id=" + userId + "&latlon=" + checkinlatlong + "&visit_date=" + visit_date + "&check_in_time=" + check_in_time);
           */
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_CHECKINOUT)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
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
                    status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        trackingid = jsonobject.getString("tracking_id");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("tracking_id", trackingid);
                        editor.putString("checkinlatlong", checkinlatlong);
                        editor.putString("checkin", "true");
                        editor.commit();
                        String selectQuerys = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + " FROM " + TABLE_GEO_TRACKING + " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
                        sdbw = db.getWritableDatabase();

                        Cursor cc = sdbw.rawQuery(selectQuerys, null);
                        //System.out.println("cursor count "+cursor.getCount());
                        if (cc != null && cc.moveToFirst()) {
                            tid = String.valueOf(cc.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                        }


                        String updatequery = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_FFMID + " = '" + trackingid + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + tid;
                        sdbw.execSQL(updatequery);

                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_checkout));
                        isSignedIn = true;

                        if (gpsService != null) {
                            gpsService.startPushThread();
                        }
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

    private class Async_UpdateAprovalStatus extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Updating Approval Status");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {
            check_in_time = timeFormat.format(mytime);
            //  Log.e("Check_in_time",check_in_time);

            visit_date = dateFormat.format(myDate);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdtime = new SimpleDateFormat("HH:mm:ss");
            String strDate = sdtime.format(c.getTime());
            Log.e("Check_in_time", strDate);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                RequestBody formBody = new FormEncodingBuilder()
                        .add("table", "employee_visit_management")
                        .add("primary_key", "emp_visit_id")
                        .add("primary_value", emp_visit_id)
                        .add("sql_id", sql_id)
                        .add("approval_status", event_approval_status)
                        .add("updated_by", sharedpreferences.getString("userId", ""))
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
                    status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        sdbw = db.getWritableDatabase();
                        String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_APPROVAL_STATUS + " = '" + event_approval_status + "' WHERE " + KEY_EMP_VISIT_ID + " = " + sql_id;
                        sdbw.execSQL(updatequerys);

                        new Async_getalloffline().execute();
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

    private class Async_Checkout extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Submitting Check out");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {


            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
            String strDate = sdftime.format(c.getTime());
            Log.e("Check_out_time", strDate);
            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("type", "check_out_lat_lon")
                        .add("latlon", checkinlatlong)
                        .add("check_out_time", strDate)
                        .add("tracking_id", sharedpreferences.getString("tracking_id", ""))
                        .add("user_id", userId)
                        .add("installed_apps", Common.getPackageName(PlanerOneActivity.this))
                        .build();

                Response responses = null;



                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_CHECKINOUT)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 checkout" + jsonData);
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
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("checkin", "false");
            editor.commit();
            fab.setVisibility(View.GONE);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_checkin));
            isSignedIn = false;

            sdbw = db.getWritableDatabase();
            String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_STATUS + " = '" + "4" + "' WHERE " + KEY_EMP_VISIT_USER_ID + " = " + userId + " and " + KEY_EMP_PLAN_DATE_TIME + " like '" + datefromcalander + "%'";
            sdbw.execSQL(updatequerys);

            String updatequery1 = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " = 1 ," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + " = '" + checkinlatlong + "'," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + " = '" + strDate + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + tid;

            sdbw.execSQL(updatequery1);


            Intent intent = new Intent(PlanerOneActivity.this, GPSTracker.class);
            if (mBound) {
                if (mConnection != null)
                    unbindService(mConnection);
                mBound = false;
            }
            stopService(intent);

            Intent intent1 = new Intent(PlanerOneActivity.this, LocationService.class);
            stopService(intent1);
            Log.d("onDestroy", "onDestroy 2.......................");

            /*if (jsonData != null && jsonData.startsWith("["))
            {   JSONArray jsonarray;
                try {
                    jsonarray = new JSONArray(jsonData);
                    for (int j = 0; j < jsonarray.length(); j++)
                    {
                        JSONObject jsonobject = jsonarray.getJSONObject(j);
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("name",      jsonobject.getString("name"));
                        map.put("icon",      jsonobject.getString("icon"));
                        map.put("latitude",  jsonobject.getString("latitude"));
                        map.put("longitude", jsonobject.getString("longitude"));
                        map.put("address",   jsonobject.getString("address"));
                        map.put("distance",  jsonobject.getString("distance"));
                        map.put("placeId",   jsonobject.getString("placeId"));
                        favouriteItem.add(map);

                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getActivity(),"No data found",Toast.LENGTH_LONG).show();
            }
            adapter.updateResults(favouriteItem);*/

            /*Intent checkoutintent = new Intent(getApplicationContext(),PlanerOneActivity.class);
            startActivity(checkoutintent);*/

        }
    }

    private class Async_Routepath extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("latlon", checkinlatlong)
                        .add("tracking_id", sharedpreferences.getString("tracking_id", ""))
                        .build();

                Response responses = null;

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_ROUTEPATH_UPDATE_INTERVAL)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
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


    private class Async_getRMDirectDistributor extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();*/
        }

        protected String doInBackground(Void... params) {


            sdbw = db.getWritableDatabase();

            String selectQuery1 = "SELECT DISTINCT (CR.customer_id)," +
                    "CR.customer_name, " +
                    "CR.customer_code, " +
                    "C.company_id, " +
                    "C.company_code" +
                    " FROM user_company_customer AS CDC JOIN customers AS CR ON CDC.customer_id = CR.customer_id " +
                    "JOIN companies AS C ON C.company_id = CDC.company_id WHERE user_id =" + Common.getUserIdFromSP(PlanerOneActivity.this);


            Log.e("Distributor selectQuery", selectQuery1);
            Cursor cursor1 = sdbw.rawQuery(selectQuery1, null);

            if (cursor1 != null && cursor1.getCount() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // fab.setVisibility(View.VISIBLE);
                        callapi(1);
                        isDirectCustomers=true;

                        febButtonStatus();
                    }
                });

            } else {
                callapi(0);
            }


            // do some stuff....

            Common.Log.i(favouriteItem.toString());

            return jsonData;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }
}
