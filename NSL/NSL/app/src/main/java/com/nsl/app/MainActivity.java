package com.nsl.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.advancebooking.AdvanceBookingMainActivity;
import com.nsl.app.commonutils.Common;
import com.nsl.app.complaints.Complaintsselectactivity;
import com.nsl.app.distributors.DistributorsActivity;
import com.nsl.app.feedback.FeedbackallActivity;
import com.nsl.app.marketintelligence.MarketIntelligenceAcivity;
import com.nsl.app.orderindent.OrderIndentMainActivity;
import com.nsl.app.products.Activity_Webview;
import com.nsl.app.scheduler.LocationService;
import com.nsl.app.stockmovement.NewStockMovementChooseActivity;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.nsl.app.DatabaseHandler.KEY_EMP_PLAN_DATE_TIME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_STATUS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_USER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_UPDATED_STATUS;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_DESIGNATION;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_EMAIL;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_HEADQUARTER;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_IMAGE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MOBILE_NO;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_REGION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_SAP_ID;
import static com.nsl.app.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.app.DatabaseHandler.TABLE_GEO_TRACKING;
import static com.nsl.app.DatabaseHandler.TABLE_USERS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String mypreference = "mypref";
    public static String region_id = "";
    FragmentManager fm;
    FragmentTransaction ft;
    String jsonData;
    int role;
    String team;
    String user_id;
    TextView toolbarTitle;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    NavigationView navigationView;
    SQLiteDatabase sdbw, sdbr;
    Context context;
    private static final String DATABASE_NAME = "NSL.db";
    private String geoTrackingId;
    private Dialog syncDialog;
    private DialogInterface dialogInterface;

    public static void showExceedingAlert(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_coming_soon);


        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.tv_message);
        text.setText(message);

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
                // finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        db = new DatabaseHandler(this);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("NSL");
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
       /* Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/SEGOEWP-SEMIBOLD.TTF");
        toolbarTitle.setTypeface(myTypeface);*/
        toolbarTitle.setText("NSL");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        team = sharedpreferences.getString("team", "");
        user_id = sharedpreferences.getString("userId", "");
        fm = getSupportFragmentManager();

        ft = fm.beginTransaction().add(R.id.content_frame, new FragmentCategories());
        ft.commit();

        if (Common.getBooleanDataFromSP(MainActivity.this,Constants.SharedPrefrancesKey.IS_DEFAULT_PASSWORD)){
            Intent intent=new Intent(this,ChangePassword.class);
            intent.putExtra("is_default",true);
            startActivity(intent);
        }


        callregionid();
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.Theme_Dark_primary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (role == Constants.Roles.ROLE_7 || role == Constants.Roles.ROLE_12) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_payment_approval).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        Log.d("isCheckedIn()", String.valueOf(isCheckedIn()));

        if (Common.haveInternet(this)){
            new Async_IsAppLoggedIn().execute();
        }

    }

    private void callregionid() {
        String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_SAP_ID + "," + KEY_TABLE_USERS_MOBILE_NO + "," + KEY_TABLE_USERS_EMAIL + "," + KEY_TABLE_USERS_DESIGNATION + "," + KEY_TABLE_USERS_HEADQUARTER + "," + KEY_TABLE_USERS_REGION_ID + "," + KEY_TABLE_USERS_IMAGE + " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + user_id;
        //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

        sdbw = db.getWritableDatabase();

        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {


                region_id = cursor.getString(6);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("region_id", region_id);
                editor.commit();


            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Exit alertdFragment = new Exit();
            alertdFragment.show(fragmentManager, "Exit");
        }
    }

   /* @Override
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
    }*/

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/SEGOEWP-LIGHT.TTF");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {


        // Handle navigation view item clicks here.
        if (menuItem.getItemId() == R.id.nav_home) {
            getSupportActionBar().setTitle("NSL");

            fm = getSupportFragmentManager();
            ft = fm.beginTransaction().replace(R.id.content_frame, new FragmentCategories());
            ft.commit();
        }
        if (menuItem.getItemId() == R.id.nav_planner) {
            /*getSupportActionBar().setTitle("Planer");

            fm = getSupportFragmentManager();
            ft = fm.beginTransaction().replace(R.id.content_frame, new FragmentPlanner());
            ft.commit();*/


            Intent planner = new Intent(getApplicationContext(), PlanerMainActivity.class);
            startActivity(planner);
//            showExceedingAlert(this, menuItem.getTitle() + "\n" + Common.getStringResourceText(R.string.will_be_live_soon));


        }
        if (menuItem.getItemId() == R.id.nav_feedback) {
           /*getSupportActionBar().setTitle("Feedback");

            fm = getSupportFragmentManager();
            ft = fm.beginTransaction().replace(R.id.content_frame, new FragmentFeedback());
            ft.commit();*/
            Intent feedback = new Intent(getApplicationContext(), FeedbackallActivity.class);
            startActivity(feedback);
        }
        if (menuItem.getItemId() == R.id.nav_complaints) {

            //getSupportActionBar().setTitle("Complaints");

            /*fm = getSupportFragmentManager();
            ft = fm.beginTransaction().replace(R.id.content_frame, new Complaints_tabs_new());
            ft.commit();
*/
            Intent complaints = new Intent(getApplicationContext(), Complaintsselectactivity.class);
            startActivity(complaints);

        }
        if (menuItem.getItemId() == R.id.nav_distributors) {

            Intent distributors = new Intent(getApplicationContext(), DistributorsActivity.class);
            startActivity(distributors);
        }
        if (menuItem.getItemId() == R.id.nav_products) {

            Intent customers = new Intent(this, Activity_Webview.class);
            startActivity(customers);
        }
        if (menuItem.getItemId() == R.id.nav_schemes) {

            Intent schemes = new Intent(getApplicationContext(), SchemesActivity.class);
            startActivity(schemes);
        }
        if (menuItem.getItemId() == R.id.nav_advancebooking) {

            Intent advancebooking = new Intent(getApplicationContext(), AdvanceBookingMainActivity.class);
            startActivity(advancebooking);
        }
        if (menuItem.getItemId() == R.id.nav_orderindent) {

            Intent orderindent = new Intent(getApplicationContext(), OrderIndentMainActivity.class);
            startActivity(orderindent);
        }
        if (menuItem.getItemId() == R.id.nav_paymentcollection) {
            // getSupportActionBar().setTitle("Payment Collection");

            Intent payment = new Intent(getApplicationContext(), PaymentActivity.class);
            startActivity(payment);
//            showExceedingAlert(this, menuItem.getTitle() + "\n" + Common.getStringResourceText(R.string.will_be_live_soon));
        }
        if (menuItem.getItemId() == R.id.nav_marketintelligence) {
            // getSupportActionBar().setTitle("Payment Collection");

            Intent payment = new Intent(getApplicationContext(), MarketIntelligenceAcivity.class);
            startActivity(payment);
//            showExceedingAlert(this, menuItem.getTitle() + "\n" + Common.getStringResourceText(R.string.will_be_live_soon));
        }
        if (menuItem.getItemId() == R.id.nav_stockmovements) {

            Intent newbooking = new Intent(getApplicationContext(), NewStockMovementChooseActivity.class);
            newbooking.putExtra("selection", "adv");
            startActivity(newbooking);
        }
        if (menuItem.getItemId() == R.id.nav_profile) {
            /*getSupportActionBar().setTitle("Profile");
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction().replace(R.id.content_frame, new FragmentProfile());
            ft.commit();*/
            Intent profile = new Intent(getApplicationContext(), ProfileMainActivity.class);
            startActivity(profile);
        }

        if (menuItem.getItemId() == R.id.nav_daily_dairy) {
           /* getSupportActionBar().setTitle("Daily Dairy");

            fm = getSupportFragmentManager();
            ft = fm.beginTransaction().replace(R.id.content_frame, new FragmentDailyDairy());
            ft.commit();*/
            Intent route = new Intent(getApplicationContext(), MainDailyDiaryActivity.class);
            startActivity(route);
//            showExceedingAlert(this, menuItem.getTitle() + "\n" + Common.getStringResourceText(R.string.will_be_live_soon));
        }
        if (menuItem.getItemId() == R.id.nav_routemap) {
            // getSupportActionBar().setTitle("Advance Booking");

            Intent route = new Intent(getApplicationContext(), PostRouteMapActivityCopy.class);
            startActivity(route);
//            showExceedingAlert(this, menuItem.getTitle() + "\n" + Common.getStringResourceText(R.string.will_be_live_soon));
        }
        if (menuItem.getItemId() == R.id.nav_change_password) {
            // getSupportActionBar().setTitle("Advance Booking");

            Intent route = new Intent(getApplicationContext(), ChangePassword.class);
            startActivity(route);
        }
        if (menuItem.getItemId() == R.id.nav_logout) {

            /*FragmentManager fragmentManager = getSupportFragmentManager();
            Logout alertdFragment = new Logout();
            alertdFragment.show(fragmentManager, "Logout");*/
            String message;
            if (isCheckedIn()){
                message="You have been checked in for today. If you log-out now, your attendance for today will be understood as day end. The current time will be updated as end time.\n\n Are you sure, you want to log-out? ";
            }else {
                message ="Are you sure, you want to log-out? ";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Alert !");
            builder.setMessage(message);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(final DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    if (Common.haveInternet(getApplicationContext())) {
                       syncDialog = Common.showpopup(MainActivity.this);
                        dialogInterface=dialog;


                        Intent backgroundIntent = new Intent(context, BackgroundPushService.class);
                        backgroundIntent.putExtra("logout",true);
                        backgroundIntent.putExtra("logout_receiver",new LogoutReceiver(new Handler()));

                        context.startService(backgroundIntent);

                        Log.d("logout","Yes..");


                    } else {
                        Toast.makeText(getApplicationContext(), Common.INTERNET_UNABLEABLE, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutFunction(DialogInterface dialog, Dialog syncDialog) {


        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Divisions", "false");
        editor.putString("userId", "");
        editor.putInt(Constants.SharedPrefrancesKey.ROLE, 0);
        editor.putString("fcm_id", "");
        editor.commit();
        dialog.dismiss();
        context.deleteDatabase(DATABASE_NAME);
        Common.dismissDialog(syncDialog);

        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(login);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap profile_photo = ImagePicker.getImageFromResult(this, resultCode, data);
        FragmentCategories fragment = (FragmentCategories) fm.findFragmentById(R.id.content_frame);
        fragment.handleActivityResult(profile_photo);

    }


    public boolean isCheckedIn() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        String datefromcalander = df.format(c.getTime());
        String selectQueryss = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + "," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + "," + KEY_TABLE_GEO_TRACKING_FFMID + "," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + "," + KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + " FROM " + TABLE_GEO_TRACKING + " where " + " visit_date like '" + datefromcalander + "%'" + " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
        sdbw = db.getWritableDatabase();

        Cursor ccc = sdbw.rawQuery(selectQueryss, null);
        System.out.println("cursor count " + ccc.getCount() + "\n" + selectQueryss);
        if (ccc != null && ccc.moveToFirst()) {
            do {
                if ((ccc.getString(3) == null || ccc.getString(3) == "") && (ccc.getString(4) != null && ccc.getString(4).length() > 5)) {
                    geoTrackingId = ccc.getString(0);
                    return true;
                }
            } while (ccc.moveToNext());
        }
        return false;
    }


    private class Async_Checkout extends AsyncTask<Void, Void, String> {

        private String checkinlatlong;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        String datefromcalander = df.format(c.getTime());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {

            Location lastKnownLocation = Common.getCurrentLocationFromSP(getApplicationContext());
            checkinlatlong = String.valueOf(lastKnownLocation.getLatitude()) + "," + String.valueOf(lastKnownLocation.getLongitude());
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
                        .add("user_id", user_id)
                        .add("installed_apps",Common.getPackageName(MainActivity.this) )
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

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("checkin", "false");
            editor.commit();


            sdbw = db.getWritableDatabase();

            String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_STATUS + " = '" + "4" + "' WHERE " + KEY_EMP_VISIT_USER_ID + " = " + user_id + " and " + KEY_EMP_PLAN_DATE_TIME + " like '" + datefromcalander + "%'";
            sdbw.execSQL(updatequerys);

            String updatequery1 = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " = 1 ," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + " = '" + checkinlatlong + "'," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + " = '" + strDate + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + geoTrackingId;

            sdbw.execSQL(updatequery1);


            Intent intent = new Intent(MainActivity.this, GPSTracker.class);
           /* if (mBound) {
                if (mConnection!=null)
                    unbindService(mConnection);
                mBound = false;
            }*/
            stopService(intent);

            Intent intent1 = new Intent(MainActivity.this, LocationService.class);
            stopService(intent1);
            Log.d("onDestroy", "onDestroy 2.......................");


        }
    }



    private class Async_Logout extends AsyncTask<Void, Void, String> {
        String jsonData;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                Response responses = null;

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "user_id=" + user_id );
                Request request = new Request.Builder()
                        .url(Constants.URL_LOGOUT)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1login" + jsonData);
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

            if (s != null) {
                try {
                    JSONObject jsonobject = new JSONObject(s);
                    status = jsonobject.getString("status");
                    Log.d("jsonobject",jsonobject.toString());
                    if (status.equalsIgnoreCase("success")) {
                        Log.d("jsonobject1.",jsonobject.toString());
                        if (isCheckedIn()) {
                            new Async_Checkout().execute();
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("jsonobject","2..");
                                if (sharedpreferences.contains(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME) && (System.currentTimeMillis() - sharedpreferences.getLong(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME, 0)) > 120000) {
                                    logoutFunction(dialogInterface, syncDialog);
                                } else {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            logoutFunction(dialogInterface, syncDialog);
                                        }
                                    }, 10000);
                                }
                            }
                        }, 1000 * 5);



                    } else {
                        Toast.makeText(getApplicationContext(), jsonobject.getString("msg"), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }


        }
    }
    private class Async_IsAppLoggedIn extends AsyncTask<Void, Void, String> {
        String jsonData;
        String status;
        String android_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                Response responses = null;

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "user_id=" + user_id);
                Request request = new Request.Builder()
                        .url(Constants.URL_UPDATE_LAST_LOGIN)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1login" + jsonData);
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

            if (s != null) {
                try {
                    JSONObject jsonobject = new JSONObject(s);
                    status = jsonobject.getString("status");


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }


        }
    }



    private class LogoutReceiver extends ResultReceiver {
        public LogoutReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == BackgroundPushService.UPDATE_PROGRESS) {
                boolean logout = resultData.getBoolean("logout");

                if (logout) {
                     new Async_Logout().execute();

                    Log.d("logout","LogoutReceiver..");
                }
            }
        }
    }
}
