package com.nsl.app.scheduler;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.GPSTracker;
import com.nsl.app.MyApplication;
import com.nsl.app.Utility;
import com.nsl.app.commonutils.Common;
import com.nsl.app.network.RetrofitAPI;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_GEO_TRACKING;

/**
 * Created by sys on 5/30/2017.
 */

public class LocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long INTERVAL = 1000 * 20;
    private static final long FASTEST_INTERVAL = 1000 * 10;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private RetrofitAPI retrofitAPI;
    public String checkinlatlong;
    public SharedPreferences sharedpreferences;
    private String jsonData;
    volatile boolean isPushToServer = false;
    public String trackingId, tid, existedroutepath;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    public static final String mypreference = "mypref";
    private float ACCURACY = 60;
    private String ffmID;
    private float DISTANCE=30;
    private String accuracy;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("LocationService", "LocationService...");

        createLocationRequest();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
        db = new DatabaseHandler(this);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);


    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationService", "LocationService "+location );
        if (location != null) {
            accuracy=accuracy+String.valueOf(location.getAccuracy())+",";
            sharedpreferences.edit().putString("accuracy",accuracy ).commit();
            Log.d("LocationService", String.valueOf(location.getLatitude())+" location "+location.getAccuracy());
            if (location.getAccuracy() < ACCURACY)
                afficher(location);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        Log.d("onDestroy", "onDestroy .......................");
    }


    //call this in onPause method
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d("startLocationUpdates", "Location update stopped .......................");
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d("startLocationUpdates", "Location update started ..............: ");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        sharedpreferences.edit().putString("accuracy", String.valueOf("")).commit();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void afficher(Location mLocation) {

        double latitude = mLocation.getLatitude();
        double longitude = mLocation.getLongitude();

        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);


        Log.d("save_lat_long.1", "latitude " + latitude);
        Location locationFromSp = Common.getCurrentLocationFromSP(this);
        if (locationFromSp.getLatitude() == 0 && locationFromSp.getLongitude() == 0) {
            Log.d("save_lat_long.2", "latitude " + latitude);
            Common.saveCurrentLocationInSP(this, latitude, longitude);
        } else {
            Log.d("save_lat_long.3", "latitude " + latitude);
            float distance = location.distanceTo(locationFromSp);
            if (distance < DISTANCE) {
                Log.d("save_lat_long.5", "latitude " + latitude);
                return;
            } else {
                Log.d("save_lat_long.6", "latitude " + latitude);
                Common.saveCurrentLocationInSP(this, latitude, longitude);

                checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude);
                //Toast.makeText(getApplicationContext(),checkinlatlong,Toast.LENGTH_LONG).show();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String strDate = sdf.format(c.getTime());
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
                String datefromcalander = df.format(c.getTime());

                /**
                 * CRUD Operations
                 * */
                // Inserting Contacts
                Log.d("Insert: ", "Inserting ..");
                String customerId = Common.getDataFromSP(this, Constants.CUSTOMER_ID_GEO);

                String selectQueryss = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + "," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + "," + KEY_TABLE_GEO_TRACKING_FFMID + " FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + customerId + " and  visit_date like '" + datefromcalander + "%'"+ " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
                sdbw = db.getWritableDatabase();

                Cursor ccc = sdbw.rawQuery(selectQueryss, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (ccc != null && ccc.moveToFirst()) {
                    tid = String.valueOf(ccc.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0
                    ffmID = ccc.getString(2); //The 0 is the column index, we only have 1 column, so the index is 0
                    existedroutepath = String.valueOf(ccc.getString(1));
                    if (existedroutepath.equalsIgnoreCase("0")) {
                        existedroutepath = checkinlatlong;
                    } else {
                        existedroutepath = existedroutepath + ":" + checkinlatlong;

                    }
                /*if(latitude!=0.0 && longitude!=0.0)
				*/

                    Log.e("++++ lastId ++++", tid + ": existed route path " + existedroutepath);
                }


                String updatequerys = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + " = '" + existedroutepath + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + tid;
                sdbw.execSQL(updatequerys);
                // db.addGeotracking(new Geo_Tracking_POJO("",sel_visittype,userId,checkinlatlong,"",checkinlatlong,str_distance,visit_date,strDate,"",visit_date,visit_date,"","1"));

                //	Toast.makeText(getApplicationContext(), String.valueOf(latitude) + String.valueOf(longitude), Toast.LENGTH_SHORT).show();


                if (Utility.isNetworkAvailable(this, false)) {
                    trackingId = sharedpreferences.getString("tracking_id", null);
                   // checkinlatlong = sharedpreferences.getString("checkinlatlong", null);
                    if (ffmID == null || ffmID == "0" || trackingId==null) {
                        return;
                    } else {
                        new Async_Routepath().execute();
                    }

                }else {

                }
            }
        }


    }


    public class Async_Routepath extends AsyncTask<Void, Void, String> {

        private String jsonData;

        protected String doInBackground(Void... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("latlon", existedroutepath)
                        .add("tracking_id", trackingId)
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



    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
