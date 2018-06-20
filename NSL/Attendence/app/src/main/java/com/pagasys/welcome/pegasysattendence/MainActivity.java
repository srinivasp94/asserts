package com.pagasys.welcome.pegasysattendence;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pagasys.welcome.pegasysattendence.models.UsersModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_CHECK_SETTINGS = 11;
    Button checkin, checkOut;
    TextView date, txtName, txtRole;
    LocationManager locationManager;
    String provider;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Double lat, lng;
    double lat_ofc = 17.449123;
    double lon_ofc = 78.363443;
    Geocoder gc = null;
    RelativeLayout layout;
    AlertDialog.Builder builder;
    Date d;

    private FirebaseDatabase databaseuser;
    private DatabaseReference dbRef;

    private String userdate;
    private double distance;
    String biometricid, name, role;
    private ArrayList<UsersModel> modelList;
    private CharSequence previousdate;

    SharedPreferences mPreferences, sharedPreferences;
    private SharedPreferences.Editor editor;
    boolean isCheckedIn = false;
    private boolean hasCheckedIn;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.snackbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkin = (Button) findViewById(R.id.checkin);
        checkOut = (Button) findViewById(R.id.check_out);
        date = (TextView) findViewById(R.id.date_time);
        txtName = (TextView) findViewById(R.id.tv_name);
        txtRole = (TextView) findViewById(R.id.tv_role);

        Intent intent = getIntent();
        mPreferences = getSharedPreferences("LoginStatusPref", MODE_PRIVATE);
        name = mPreferences.getString("mNAME", "");
        role = mPreferences.getString("mROLE", "");
        biometricid = mPreferences.getString("mBIOMETRIC_ID", "");

        txtName.setText(name);
        txtRole.setText(role);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        d = new Date();
        userdate = df.format(d.getTime());


        if (!checkWifiOnAndConnected()) {
            Log.i("wifi off", "" + ServerValue.TIMESTAMP);
            return;
        } else {

            Log.i("ELSE CHECK WIFI ON", "" + ServerValue.TIMESTAMP);
            checkin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    turnGPSOnRuntimePermissions();
                    displayLocationSettingsRequest(MainActivity.this);

                    Log.i("  CHECKIN", "" + ServerValue.TIMESTAMP);
                    if (distance <= 300) {
                        count++;
                        Log.i("COOUNT ++", " is " + count);

                        databaseuser = FirebaseDatabase.getInstance();
                        dbRef = databaseuser.getReference("employee logData");
                        if (TextUtils.isEmpty(biometricid)) {
                            biometricid = dbRef.push().getKey();
                            Log.i("  CHECKIN if", "" + ServerValue.TIMESTAMP);
                        }
                        Date mDate = new Date();
                        CharSequence dateandtime = DateFormat.format(" hh:mm:ss", mDate.getTime());
                        UsersModel usermodel = new UsersModel(biometricid, dateandtime, "");
                        Log.i("SERVER DATE AND TIME", "" + ServerValue.TIMESTAMP);

                        if (TextUtils.isEmpty(userdate)) {
                            userdate = dbRef.push().getKey();
                        }
                        hasCheckedIn = false;
                        if (hasCheckedIn) {
                            Log.i("hasCheckedIn", "" + hasCheckedIn);
                            sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putBoolean("IsCheckin", true);
                            editor.commit();
                            dbRef.child(biometricid).child("date : " + userdate).setValue(usermodel);
                            isCheckedIn = true;

                        }
                        if (count > 1) {
                            Log.i("COOUNT >>", " is " + count);
                            alertDialog("", "You are already checked In", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            count--;
                            Log.i("COOUNT ++", " is " + count);

                        } else {
                            Log.i("else case count", "!@#");
                        }
                    } else {
                        alertDialog("", "You are already checked In", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });

//            boolean hasCheckin=sharedPreferences.getBoolean("IsCheckin",true);
            if (isCheckedIn)

            {
                Log.i("  IsCheckin()", "" + ServerValue.TIMESTAMP);
                dbRef.child("employee logData").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        modelList = new ArrayList<UsersModel>();
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            UsersModel model = noteDataSnapshot.getValue(UsersModel.class);
                            modelList.add(model);
                        }
                        for (int j = 0; j < modelList.size(); j++) {

                            if (biometricid == modelList.get(j).getBioid()) {
                                previousdate = modelList.get(j).getLogin_time();
//                                    UsersModel usermodel = new UsersModel(biometricid, modelList.get(j).getLogin_time(), dateandtime);
//                                    Log.i("SERVER DATE AND TIME", "" + ServerValue.TIMESTAMP);
//                                    dbRef.child(userdate).child("date : " + d).setValue(usermodel);
                                    /*HashMap<String,Object> mpa= new HashMap<>();

                                    mpa.put("LOGIN",modelList.get(j).getLogin_time().toString() );
                                    mpa.put("LOGIN",dateandtime.toString() );

//                                    dbRef.child(userdate).child("date : " + d).updateChildren(mpa);
                                    dbRef.child((String) userdate).child("date : " + d).updateChildren(mpa);*/
                                checkin.setEnabled(true);
                                Log.i("  PERFORMCLICK()", "" + ServerValue.TIMESTAMP);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "ERRORs( wifi connection" + databaseError, LENGTH_SHORT).show();
                    }
                });
            }
            checkOut.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Log.i("  PERFORMCLICK()", "" + ServerValue.TIMESTAMP);
                    if (atOfficLocation()) {
                        Date mDate = new Date();
                        CharSequence dateandtime = DateFormat.format(" hh:mm:ss", mDate.getTime());

//                            UsersModel usermodel = new UsersModel(biometricid, previousdate, dateandtime);
                        Log.i("SERVER DATE AND TIME", "" + ServerValue.TIMESTAMP);
                        // dbRef.child(userdate).child("date : " + d).setValue(usermodel);

                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put("bioid", biometricid);
                        //taskMap.put("login_time", previousdate);
                        taskMap.put("logout_time", DateFormat.format(" hh:mm:ss", mDate.getTime()));
//                        userdate= String.valueOf(d.getDate());

                        if (TextUtils.isEmpty(userdate)) {
                            userdate = dbRef.push().getKey();
                        }
                        dbRef.child(biometricid).child("date : " + userdate).updateChildren(taskMap);
//                        checkin.setClickable(true);
                        count--;
                        editor.clear();
                    }
                }
            });
        }
    }


//        dateandtime = DateFormat.format(" hh:mm", d.getTime());

       /* if (isNetworkEnabled()) {
            checkin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnGPSOn();
                }
            });
        } else {
            Toast.makeText(this, "no wifi connection", LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar
                    .make(layout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);

            snackbar.show();
//            finish();
        }*/


    private void turnGPSOnRuntimePermissions() {
        // run time permissions for gps
        int permissioncheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissioncheck != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            Log.i("  RUN TIME PERMISSIONS", "" + ServerValue.TIMESTAMP);
        } else {
            Log.i("  RUN TIME PERMISSIONS", "" + ServerValue.TIMESTAMP);
            displayLocationSettingsRequest(this);
        }

    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Toast.makeText(this, R.string.permision_available_location,
                        LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.permissions_not_granted,
                        LENGTH_SHORT).show();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void displayLocationSettingsRequest(final Context context) {
        Log.i("  LOCATION SETTINGS", "" + ServerValue.TIMESTAMP);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("MainActivity", "All location settings are satisfied.");

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions

                            return;
                        }
                        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MainActivity.this);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("MainActivity", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("MainActivity", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("MainActivity", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("  ONLOCATIONCHANGED", "" + ServerValue.TIMESTAMP);
        lat = location.getLatitude();
        lng = location.getLongitude();

//        date.setText("location  " + lat + " " + lng);
        Log.d("MAinActivity", "" + lat + " " + lng);
        if (location != null) {
            String s = Common.getAddressString(getApplicationContext(), location.getLatitude(), location.getLongitude());

            /*double earthRadius = 3958.75;

            double dLat = Math.toRadians(lat-lat_ofc);
            double dLng = Math.toRadians(lng-lon_ofc);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(lat_ofc)) *
                            Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double dist = earthRadius * c;*/

            Location startPoint = new Location("locationA");
            startPoint.setLatitude(lat_ofc);
            startPoint.setLongitude(lon_ofc);

            Location endPoint = new Location("locationA");
            endPoint.setLatitude(location.getLatitude());
            endPoint.setLongitude(location.getLongitude());

            distance = startPoint.distanceTo(endPoint);
            date.setText("" + s + "" + distance + " in Meters");
            Log.d("Distance ", "" + distance);


        }
    }

    public void getDistance(double distance) {

        Log.i("", "" + distance);

        return;
    }

    public boolean atOfficLocation() {
        if (distance >= 250) {
            //database not updated
            /*builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setMessage("You are not at your office location");
            builder.setPositiveButton("ahh..", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.show();*/
            alertDialog("Goto Office ..!", "You are not at your office location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

        } else {

            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        Log.d("onActivityResult", "OnresultAxtivity1");
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions

                            return;
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MainActivity.this);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

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


        }
    }

    private boolean isNetworkEnabled() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
        NetworkInfo networkInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
//            checkin.setVisibility(View.VISIBLE);
            checkWifiOnAndConnected();
        }

        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean checkWifiOnAndConnected() {
//        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if (wifiInfo.getNetworkId() == -1) {
                return false; // Not connected to an access point
            }
//            checkin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {

//                }
//            });
            else {
                Log.i("  WifiManager", "" + ServerValue.TIMESTAMP);
                return true; // Connected to an access point
            }

        } else {

            /*builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.alert_message);
            builder.create();
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            builder.show();*/
            alertDialog("ALert", "You are not connected to WI-FI Network. Please check WI-FI Connections", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            return false; // Wi-Fi adapter is OFF
        }
    }

    public void alertDialog(String Title, String message, DialogInterface.OnClickListener okListener) {

        builder = new AlertDialog.Builder(getApplicationContext());
        setTitle(Title);
        builder.setMessage(message);
        builder.create();
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", okListener);
        builder.show();
    }

    @Override
    protected void onResume() {
        checkWifiOnAndConnected();
        super.onResume();
    }
}
