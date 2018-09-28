package com.example.pegasys.rapmedixuser.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.activity.fragments.Pharmacy;
import com.example.pegasys.rapmedixuser.activity.newactivities.DiagnosticsPageSubCatActivity;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.PlaceArrayAdapter;
import com.example.pegasys.rapmedixuser.activity.adapters.SearchAdapter;
import com.example.pegasys.rapmedixuser.activity.customviews.RoundedImageView;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.fragments.Bestquotesfragment;
import com.example.pegasys.rapmedixuser.activity.fragments.Diagnostics;
import com.example.pegasys.rapmedixuser.activity.fragments.Doctorsfrag;
import com.example.pegasys.rapmedixuser.activity.fragments.HealthCheckups;
import com.example.pegasys.rapmedixuser.activity.fragments.HomeVisitsFragment;
import com.example.pegasys.rapmedixuser.activity.newactivities.AboutusActivity;
import com.example.pegasys.rapmedixuser.activity.newactivities.AppointmentActivity;
import com.example.pegasys.rapmedixuser.activity.newactivities.Changepassword;
import com.example.pegasys.rapmedixuser.activity.newactivities.DoctorlistPage;
import com.example.pegasys.rapmedixuser.activity.newactivities.FamilymemberActivity;
import com.example.pegasys.rapmedixuser.activity.newactivities.HealthRecordsActivity;
import com.example.pegasys.rapmedixuser.activity.newactivities.InviteFriendsActivity;
import com.example.pegasys.rapmedixuser.activity.newactivities.Map_Loc;
import com.example.pegasys.rapmedixuser.activity.newactivities.PrescriptionActivity;
import com.example.pegasys.rapmedixuser.activity.newactivities.ProfileActivity;
import com.example.pegasys.rapmedixuser.activity.newactivities.TermsActivity;
import com.example.pegasys.rapmedixuser.activity.pojo.AppointmentsList;
import com.example.pegasys.rapmedixuser.activity.pojo.AppointmentsResp;
import com.example.pegasys.rapmedixuser.activity.pojo.DocspecList;
import com.example.pegasys.rapmedixuser.activity.pojo.ProfileResponse;
import com.example.pegasys.rapmedixuser.activity.pojo.Searchmodel;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class Home_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RetrofitResponseListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.tab_icon_chang,
            R.drawable.tab_icon_chang_diagnostics,
            R.drawable.tab_icon_chang_pharmacy,
            R.drawable.tab_icon_chang_diagnostics,
            R.drawable.tab_icon_chang_pharmacy,
            R.drawable.tab_icon_chang_home_visit
    };
    public static Home_page instance;

    private Doctorsfrag doctorsfrag;
    private Diagnostics diagnosticsFrag;
    private Pharmacy pharmacy;
    private HealthCheckups healthCheckupsFrag;
    private Bestquotesfragment bestquotesfragment;
    private HomeVisitsFragment homeVisitsFragment;

    private SharedPreferences ref_code_sp, sp;
    public static final String pref = "Location";
    public static final String ref_code = "referral";
    double lat, longg;
    GoogleApiClient mGoogleApiClient1;
    PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(17.3660, 78.4760), new LatLng(17.3660, 78.4760));
    double currentLatitude, currentLongitude;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 125;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private Bitmap smallMarker;
    private String selected_address;

    TextView hName, hMail;
    TextView aPending, aActive, aComplete;
    int pending = 0, active = 0, completed = 0;
    ArrayList<AppointmentsList> lists = new ArrayList<>();
    RoundedImageView hProfile;
    private SharedPreferences global;
    private String sMobile, sName;
    private Object obj;
    private TextView tab11;
    private ImageView icon;
    ArrayList<String> tabs = new ArrayList<>();
    private AutoCompleteTextView actv_searchDos;
    ArrayList<String> actv_ArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = new Bundle();
        bundle.putDouble("LATITUDE", lat);
        bundle.putDouble("LONGITUDE", longg);

        global = getSharedPreferences("loggers", MODE_PRIVATE);
        sName = global.getString("NAME", "");
        sMobile = global.getString("MOBILE", "");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabs.clear();
        tabs.add("Doctors");
        tabs.add("Diagnostics");
        tabs.add("Pharmacy");
        tabs.add("Health Checkups");
        tabs.add("Best Quotes");
        tabs.add("Home Visits");

        DataBase_Helper db = new DataBase_Helper(this);
        String UserId = db.getUserId("1");

        ref_code_sp = getSharedPreferences(ref_code, MODE_PRIVATE);

        sp = getSharedPreferences(pref, MODE_PRIVATE);
        lat = Double.longBitsToDouble(sp.getLong("lattitude", Double.doubleToLongBits(0.0)));
        longg = Double.longBitsToDouble(sp.getLong("longitude", Double.doubleToLongBits(0.0)));

//        viewPager = (ViewPager) findViewById(R.id.viewpager1);
//        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tab);
        actv_searchDos = findViewById(R.id.search_for_doctor);
//        tabLayout.setupWithViewPager(viewPager);
//                setupTabIcons();

        doctorsfrag = new Doctorsfrag();
        diagnosticsFrag = new Diagnostics();
        pharmacy = new Pharmacy();
        healthCheckupsFrag = new HealthCheckups();
        bestquotesfragment = new Bestquotesfragment();
        homeVisitsFragment = new HomeVisitsFragment();

        for (int i = 0; i < tabs.size(); i++) {
            Drawable img1 = getResources().getDrawable(tabIcons[i]);

            LinearLayout relativeLayout = (LinearLayout)
                    LayoutInflater.from(this).inflate(R.layout.custom_tab, tabLayout, false);

            tab11 = relativeLayout.findViewById(R.id.tab_title);
            icon = relativeLayout.findViewById(R.id.tab_icon);


            icon.setImageDrawable(img1);
            tab11.setText(tabs.get(i));

            tabLayout.addTab(tabLayout.newTab().setCustomView(relativeLayout));

        }

        Fragment existing = getSupportFragmentManager().findFragmentById(R.id.fl_container);

        if (existing == null) {
            Fragment newFragment = new Doctorsfrag();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, newFragment)
                    .commit();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                setCurrentTabFragment(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        userIdreq req = new userIdreq();
        req.userId = new DataBase_Helper(this).getUserId("1");

        try {
            obj = Class.forName(userIdreq.class.getName()).cast(req);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new RetrofitRequester(Home_page.this).callPostServices(obj, 1, "/user/userAppointments_service", true);
        new RetrofitRequester(Home_page.this).callPostServices(obj, 2, "/user/check_user_membership_type", true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        hName = header.findViewById(R.id.header_name);
     /*   hMail = (TextView) header.findViewById(R.id.header_mail);*/
        hProfile = header.findViewById(R.id.header_imageView);
        aPending = header.findViewById(R.id.header_notification_pending);
        aPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pendingIntent = new Intent(Home_page.this, AppointmentActivity.class);
                pendingIntent.putExtra("Input", 2);
                startActivity(pendingIntent);

            }
        });
        aActive = header.findViewById(R.id.header_notification_active);
        aActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pendingIntent = new Intent(Home_page.this, AppointmentActivity.class);
                pendingIntent.putExtra("Input", 1);
                startActivity(pendingIntent);
            }
        });
        aComplete = header.findViewById(R.id.header_notification_completer);
        aComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pendingIntent = new Intent(Home_page.this, AppointmentActivity.class);
                pendingIntent.putExtra("Input", 3);
                startActivity(pendingIntent);
            }
        });

//        hName.setText(sName);
//        hMail.setText(sMobile);
        navigationView.setNavigationItemSelectedListener(this);


        if (lat == 0.0 && longg == 0.0) {
            int perBool1 = ContextCompat.checkSelfPermission(Home_page.this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (perBool1 == PackageManager.PERMISSION_GRANTED)

            {
                Intent intent = new Intent(this, Map_Loc.class);
                startActivity(intent);


            } else {
                int currentapiVersion = Build.VERSION.SDK_INT;

                if (currentapiVersion >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access Location ",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(Home_page.this,
                                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                    REQUEST_CODE_ASK_PERMISSIONS);
                                        }
                                    });
                            return;
                        } else {
                            ActivityCompat.requestPermissions(Home_page.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    }


                } else {

                }
            }

        }

    }

    public void logSentFriendRequestEvent () {
//        logger.logEvent("sentFriendRequest");
        Logger.getLogger("sentFriendRequest");
    }

    public void setSearchAdaper(final ArrayList<String> actv_ArrayList,final ArrayList<String> IdList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Home_page.this, R.layout.auto_list, actv_ArrayList);
//        SearchAdapter adapter = new SearchAdapter(Home_page.this,actv_ArrayList);
//        actv_searchDos.setThreshold(2);
        actv_searchDos.setAdapter(arrayAdapter);
        actv_searchDos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                int pos = actv_ArrayList.indexOf(selected);

                Intent intent = new Intent(Home_page.this, DoctorlistPage.class);
                intent.putExtra("lat", lat);
                intent.putExtra("longg", longg);
                intent.putExtra("id", IdList.get(pos));
                intent.putExtra("location", selected_address);
                Log.e("aa", pos + "   posi  " + lat + "ccc" + IdList.get(i));

                startActivity(intent);
                actv_searchDos.setText("");
            }
        });
    }

    public void setSearchAdaperDiagnostics(final ArrayList<String> actv_ArrayList,final ArrayList<String> IdList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Home_page.this, R.layout.auto_list, actv_ArrayList);
        actv_searchDos.setAdapter(arrayAdapter);
        actv_searchDos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                int pos = actv_ArrayList.indexOf(selected);

                Intent intent = new Intent(getApplicationContext(), DiagnosticsPageSubCatActivity.class);
                intent.putExtra("ID", IdList.get(pos));
                Log.e("aa", pos + "   posi  "+ IdList.get(i));
                startActivity(intent);
                actv_searchDos.setText("");
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(doctorsfrag);
                break;
            case 1:
                replaceFragment(diagnosticsFrag);
                break;
            case 2:
                replaceFragment(pharmacy);
                break;
            case 3:
                replaceFragment(healthCheckupsFrag);
                break;
            case 4:
                replaceFragment(bestquotesfragment);
                break;

            case 5:
                replaceFragment(homeVisitsFragment);
                break;
            default:
                replaceFragment(doctorsfrag);

        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Home_page.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home_page.this);
            builder.setTitle("Alert");
            builder.setMessage("Are you sure to logout..?");
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DataBase_Helper dataBase_helper = new DataBase_Helper(Home_page.this);
                    SQLiteDatabase db = dataBase_helper.getWritableDatabase();
                    db.execSQL("delete  from User ");
                    db.close();
                    dialog.dismiss();
                    Intent intent = new Intent(Home_page.this, FirstActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            });
            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(Home_page.this, ProfileActivity.class);
            startActivity(intent);
            finish();


        } else if (id == R.id.nav_familymember) {
            Intent intent = new Intent(Home_page.this, FamilymemberActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_appointments) {
            Intent intent = new Intent(Home_page.this, AppointmentActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_prescription) {
            Intent intent = new Intent(Home_page.this, PrescriptionActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_health_records) {
            Intent intent = new Intent(Home_page.this, HealthRecordsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_invite) {
            Intent intent = new Intent(Home_page.this, InviteFriendsActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_changepwd) {
            Intent intent = new Intent(Home_page.this, Changepassword.class);
            startActivity(intent);


        }
        /*else if () {
        }*/
        else {
            if (id == R.id.nav_logout) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home_page.this);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure to logout..?");
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataBase_Helper dataBase_helper = new DataBase_Helper(Home_page.this);
                        SQLiteDatabase db = dataBase_helper.getWritableDatabase();
                        db.execSQL("delete  from User ");
                        db.close();
                        dialog.dismiss();
                        Intent intent = new Intent(Home_page.this, FirstActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                builder.show();


            } else if (id == R.id.nav_about) {
                Intent intent = new Intent(Home_page.this, AboutusActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_terms) {
                Intent intent = new Intent(Home_page.this, TermsActivity.class);
                startActivity(intent);

            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(Home_page.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    AppointmentsResp res = Common.getSpecificDataObject(objectResponse, AppointmentsResp.class);
                    Gson gson = new Gson();
                    if (res.status.equals("success")) {
                        lists = res.appointmentsLists;
                        for (int i = 0; i < lists.size(); i++) {
                            if (lists.get(i).status.equals("0")) {
                                pending++;
                                aPending.setText("" + pending);
                            } else if (lists.get(i).status.equals("1")) {
                                active++;
                                aActive.setText("" + active);
                            } else if (lists.get(i).status.equals("3")) {
                                completed++;
                                aComplete.setText("" + completed);
                            }
                        }
                    } else {

                    }
                    break;
                case 2:
                    ProfileResponse instan = Common.getSpecificDataObject(objectResponse, ProfileResponse.class);
                    gson = new Gson();
                    if (instan.status.equals("success")) {
                        hName.setText(instan.name);
                        String ImgUrl = instan.profilePic.replace("\\", "");
                        Picasso.with(Home_page.this).load(ImgUrl).error(R.drawable.doctor_icon).into(hProfile);
                    }
                    break;
            }

        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {

            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    public void location_popup() {

        final Dialog dialog = new Dialog(Home_page.this);
        // Include dialog.xml file
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_map__loc);

        MapFragment supportMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(Home_page.this);

        mGoogleApiClient1 = new GoogleApiClient.Builder(Home_page.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(Home_page.this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();


        final AutoCompleteTextView mAutocompleteTextView =
                dialog.findViewById(R.id.input_address);
        Button submit = dialog.findViewById(R.id.submit);
        Button cancel = dialog.findViewById(R.id.cancel);


        Window window = dialog.getWindow();
        //   window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));


        dialog.show();


        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_app);
        Bitmap b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


        mAutocompleteTextView.setThreshold(2);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        // mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor edit = sp.edit();
                edit.putLong("lattitude", Double.doubleToRawLongBits(currentLatitude));
                edit.putLong("longitude", Double.doubleToRawLongBits(currentLongitude));
                edit.putString("address", selected_address);
                edit.commit();
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(this, Map_Loc.class);
                    startActivity(intent);
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
