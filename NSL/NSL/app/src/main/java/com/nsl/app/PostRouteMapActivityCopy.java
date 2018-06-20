package com.nsl.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_VISIT_DATE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_DESIGNATION;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_EMAIL;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_HEADQUARTER;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_IMAGE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MOBILE_NO;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_REGION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_SAP_ID;
import static com.nsl.app.DatabaseHandler.TABLE_GEO_TRACKING;
import static com.nsl.app.DatabaseHandler.TABLE_USERS;

public class PostRouteMapActivityCopy extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, RoutingListener {

    private static final int REQUEST_CHECK_SETTINGS = 111;
    GoogleMap map;
    DatabaseHandler db;
    ArrayList<MarkerData> markerPoints;
    LatLng point;
    String userId, jsonData, datefromcalander, datefromrecords, str_address, str_name, str_userName;
    SQLiteDatabase sdbw, sdbr;
    SimpleDateFormat sdf;
    SharedPreferences sharedpreferences;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    int hours;
    String latitude, longitude, str_distance, timeTaken;
    double duration;
    public static final String mypreference = "mypref";
    private TextView tv_distance, tv_name;
    private EditText et_date;
    private Spinner spin_company;
    private ArrayList<SelectedCities> organisations;
    ArrayList<String> adaptercity;
    private String team;
    private int role;
    private String userName;
    private String customerId;
    private LinearLayout ll_select;
    private Location myCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routemap);
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

        sdf = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_name = (TextView) findViewById(R.id.tv_name);
        et_date = (EditText) findViewById(R.id.et_date);
        spin_company = (Spinner) findViewById(R.id.spin_user);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        et_date.setVisibility(View.VISIBLE);
        if (role == Constants.Roles.ROLE_7) {
            ll_select.setVisibility(View.GONE);
            customerId = userId;
            Common.saveDataInSP(getApplicationContext(), Constants.CUSTOMER_ID_GEO, customerId);
        }

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
        callapi();

        datefromcalander = PlanerMainActivity.curDate;

        if (datefromcalander == null) {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
            datefromcalander = df.format(c.getTime());
        }

        // Initializing
        markerPoints = new ArrayList<MarkerData>();

        // map                = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        if (map == null) {
            ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    // check if map is created successfully or not
                    if (map == null) {
                        //Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
                    }
                    map = googleMap;
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setZoomControlsEnabled(true);
                    map.setPadding(0, 30, 30, 100);
                    //googleMap.getUiSettings().setRotateGesturesEnabled(true);
                    //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    // googleMap.getUiSettings().setCompassEnabled(true);
                    map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            if (myCurrentLocation==null){
                                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, PostRouteMapActivityCopy.this);
                            }

                            displayGoogleLocationSettingPage(PostRouteMapActivityCopy.this,REQUEST_CHECK_SETTINGS);
                            return true;
                        }
                    });
                }
            });

            try {

                new Async_getalloffline().execute(datefromcalander);
            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }


        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 3000)
                // 10 seconds, in milliseconds
                .setFastestInterval(1 * 2000); // 1 second, in milliseconds
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(PostRouteMapActivityCopy.this, new DatePickerDialog.OnDateSetListener() {
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
                        map.clear();
                        markerPoints.clear();
                        et_date.setText(selectedyear + "-" + smonth + "-" + sday);
                        tv_distance.setVisibility(View.VISIBLE);
                        datefromcalander = et_date.getText().toString();
                        try {


                            new Async_getalloffline().execute(datefromcalander);
                        } catch (Exception e) {
                            Log.d("Tag", e.toString());
                        }
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


        String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_SAP_ID + "," + KEY_TABLE_USERS_MOBILE_NO + "," + KEY_TABLE_USERS_EMAIL + "," + KEY_TABLE_USERS_DESIGNATION + "," + KEY_TABLE_USERS_HEADQUARTER + "," + KEY_TABLE_USERS_REGION_ID + "," + KEY_TABLE_USERS_IMAGE + " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + userId;
        //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

        sdbw = db.getWritableDatabase();

        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Users users = new Users();

                Log.e("-----", "fname : " + cursor.getString(0) + "lname : " + cursor.getString(1) + "mobile : " + cursor.getString(2) + "email : " + cursor.getString(3));
                str_userName = cursor.getString(0);


            } while (cursor.moveToNext());
        }


        tv_name.setText(str_userName + " > " + datefromcalander);
    }

    /*@Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        String selectQuery = "SELECT "+ KEY_TABLE_USERS_FIRST_NAME + ","+ KEY_TABLE_USERS_SAP_ID +","+ KEY_TABLE_USERS_MOBILE_NO + ","+KEY_TABLE_USERS_EMAIL +","+KEY_TABLE_USERS_DESIGNATION +","+KEY_TABLE_USERS_HEADQUARTER+","+KEY_TABLE_USERS_REGION_ID+","+KEY_TABLE_USERS_IMAGE + " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + userId;
        //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

        sdbw = db.getWritableDatabase();

        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Users users = new Users();

                Log.e("-----","fname : "+cursor.getString(0)+"lname : "+cursor.getString(1)+"mobile : "+cursor.getString(2)+"email : "+cursor.getString(3));
                str_userName = cursor.getString(0);


            } while (cursor.moveToNext());
        }

    }*/

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for (int i = 2; i < markerPoints.size(); i++) {
            LatLng point = markerPoints.get(i).latLng;
            if (i == 2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }


        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
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
            Log.d("Exception downloading ", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    //**//** A class to parse the Google Places in JSON format *//**//*
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            double distance = 0.0;
            // Traversing through all the routes
            for (int i = 0; i < 1; i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                if (result != null && result.size() > 0) {
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {

                        HashMap<String, String> point = path.get(j);

                        if (point != null && point.get("distance") != null) {
                            String[] distanceValue = point.get("distance").split(" ");
                            distance = distance + Double.parseDouble(distanceValue[0]);
                        }

                        if (point != null && point.get("duration") != null) {
                            String[] durationValue = point.get("duration").split(" ");
                            duration = duration + Double.parseDouble(durationValue[0]);

                        }


                        if (point != null && point.get("lat") != null && point.get("lng") != null && j != path.size() - 1) {

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }
                    }
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route

            if (lineOptions != null)
                map.addPolyline(lineOptions);
            if (distance > 0) {
                distance = round(distance, 2);
                if (timeTaken == null) {
                    timeTaken = "0";
                }
                double travelAllowance = distance * Double.parseDouble(db.getTravelAllowance(customerId));
                tv_distance.setText("TRAVELED : " + distance + "Km  in " + timeTaken + "\n Travel Allowance: " + Math.round(travelAllowance));
            }

            tv_distance.setVisibility(View.VISIBLE);
        }
    }


    public float round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    @Override
    public void onLocationChanged(Location location) {
        myCurrentLocation=location;
        //handleNewLocation(location);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        if (location == null) {

        } else {
            handleNewLocation(location);
        }
    }

    private void handleNewLocation(Location location) {
        if (location!=null && map!=null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(newLatLng, 12);
            map.animateCamera(yourLocation);
        }
        // Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //  Toast.makeText(getActivity(),latitude + longitude,Toast.LENGTH_LONG).show();
        // System.out.println("Location  : " + latitude + "==" + longitude + "==");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
 /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
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

    private class Async_getalloffline extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PostRouteMapActivityCopy.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            markerPoints.clear();
        }

        protected String doInBackground(String... params) {


            try {

                String selectQuery = "SELECT DISTINCT "
                        + KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_VISIT_DATE + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME
                        + " FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + customerId + " and  visit_date like '" + params[0] + "%'";
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);

                System.out.println("selectQuery  " + selectQuery + "cursor count" + cursor.getCount());

                if (cursor.moveToFirst()) {
                    do {
                        datefromrecords = cursor.getString(3).substring(0, 10);
                        String routepath = cursor.getString(2);

                        //Toast.makeText(getApplicationContext(), datefromrecords, Toast.LENGTH_SHORT).show();


                        try {
                            if (sdf.parse(datefromcalander).equals(sdf.parse(datefromrecords))) {
                                // If two dates are equal.
                                String str_checkIn = cursor.getString(4);
                                String str_checkOut = cursor.getString(5);
                                Log.e("XXXXXXX", "str_checkIn " + str_checkIn + " str_checkOut " + str_checkOut);
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                                Date checkIn = null;
                                Date checkOut = null;

                                checkIn = format.parse(str_checkIn);
                                DateTime checkInDate = new DateTime(checkIn);
                                if (str_checkOut != null && !str_checkOut.equalsIgnoreCase("null") && !str_checkOut.equalsIgnoreCase("")) {
                                    checkOut = format.parse(str_checkOut);


                                    DateTime checkOutDate = new DateTime(checkOut);

                                    timeTaken = Hours.hoursBetween(checkInDate, checkOutDate).getHours() % 24 + " Hr " + Minutes.minutesBetween(checkInDate, checkOutDate).getMinutes() % 60 + " min ";

                                } else {
                                    DateTime checkOutDatTime = new DateTime(new Date(System.currentTimeMillis()));
                                    timeTaken = Hours.hoursBetween(checkInDate, checkOutDatTime).getHours() % 24 + " Hr " + Minutes.minutesBetween(checkInDate, checkOutDatTime).getMinutes() % 60 + " min ";
                                }
                                Log.e(" ++Dates++ ", datefromcalander + " & " + datefromrecords + "timeTaken;" + timeTaken);


                                ArrayList<String> latlng = new ArrayList(Arrays.asList(routepath.split(":")));

                                markerPoints.clear();

                                String[] checkInLatLong = cursor.getString(0).split(",");
                                point = new LatLng(Double.parseDouble(checkInLatLong[0]), Double.parseDouble(checkInLatLong[1]));
                                markerPoints.add(new MarkerData(point, "", ""));

                              /*  if (cursor.getString(2) != null) {
                                    String[] checkOutLatLong = cursor.getString(1).split(",");
                                    point = new LatLng(Double.parseDouble(checkOutLatLong[0]), Double.parseDouble(checkOutLatLong[1]));
                                    markerPoints.add(new MarkerData(point, "", ""));
                                }*/


                                if (latlng.size() > 0) {
                                    for (int i = 0; i < latlng.size(); i++)
                                    // Adding new item to the ArrayList
                                    {
                                        String latlngvalues = latlng.get(i);
                                        String latlngList[] = latlngvalues.split(",");
                                        double lat = Double.parseDouble(latlngList[0]);
                                        double lng = Double.parseDouble(latlngList[1]);
                                        point = new LatLng(lat, lng);

                                        markerPoints.add(new MarkerData(point, "", ""));
                                    }
                                }
                                Log.v("latlng", "" + latlng.size());
                                Log.v("markerpoints", "" + markerPoints.size());
                                //  markerPoints.add(new MarkerData(point,"",""));

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } while (cursor.moveToNext());

                    //  adapter.updateResults(favouriteItem);
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
                progressDialog.hide();
            }
            if (map==null) {
                return;
            }
            map.clear();
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = new PolylineOptions();
            float distanceValue = 0;

            if (markerPoints != null && markerPoints.size() > 0) {

                MarkerOptions options = new MarkerOptions();
                options.position(markerPoints.get(0).latLng);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                map.addMarker(options);

            }
            if (markerPoints != null && markerPoints.size() > 1) {

                MarkerOptions options = new MarkerOptions();
                options.position(markerPoints.get(markerPoints.size() - 1).latLng);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                map.addMarker(options);
            }


            for (int i = 0; i < markerPoints.size(); i++) {

                if (i > 1)
                    points.add(markerPoints.get(i).latLng);


                if (i > 1 && i != markerPoints.size() - 1) {
                    Location location1 = new Location("");
                    location1.setLatitude(markerPoints.get(i).latLng.latitude);
                    location1.setLongitude(markerPoints.get(i).latLng.longitude);

                    Location location2 = new Location("");
                    location2.setLatitude(markerPoints.get(i + 1).latLng.latitude);
                    location2.setLongitude(markerPoints.get(i + 1).latLng.longitude);
                    distanceValue = distanceValue + location1.distanceTo(location2);
                }
                Log.d("distanceValue", String.valueOf(distanceValue));

            }
            if (lineOptions != null) {
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
                map.addPolyline(lineOptions);
                //callDirectionAPI(points);
                distanceValue = round(distanceValue / 1000, 2);
                if (timeTaken == null) {
                    timeTaken = "0 Hr";
                }
                double travelAllowance = distanceValue * Double.parseDouble(db.getTravelAllowance(customerId));
                Common.Log.i("Travel Allowance: " + db.getTravelAllowance(customerId));
                tv_distance.setText(" " + distanceValue + "  KM   " + timeTaken + " INR " + Math.round(travelAllowance));

            }


        }
    }

    private class Async_getall_geotracking extends AsyncTask<Void, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PostRouteMapActivityCopy.this);
            pDialog.setMessage("Please wait ");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.URL_MASTER_GEO_TRACKING, userId, team))

                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 GEO_TRACKING" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonData != null) {
                try {

                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String tracking_id = objinfo.getString("tracking_id");
                        String visit_type = objinfo.getString("visit_type");
                        String user_id = objinfo.getString("user_id");
                        String check_in_lat_lon = objinfo.getString("check_in_lat_lon");
                        String check_out_lat_lon = objinfo.getString("check_out_lat_lon");
                        String route_path_lat_lon = objinfo.getString("route_path_lat_lon");
                        String distance = objinfo.getString("distance");
                        String visit_date = objinfo.getString("visit_date");
                        String check_in_time = objinfo.getString("check_in_time");
                        String check_out_time = objinfo.getString("check_out_time");
                        String status = objinfo.getString("status");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");
                        String selectQuery = "SELECT * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_FFMID + " = '" + tracking_id + "'";

                        sdbw = db.getWritableDatabase();
                        Cursor cc = sdbw.rawQuery(selectQuery, null);
                        cc.getCount();
                        // looping through all rows and adding to list
                        if (cc.getCount() == 0) {
                            //doesn't exists therefore insert record.
                            db.addGeotracking(new Geo_Tracking_POJO(tracking_id, visit_type, user_id, check_in_lat_lon, check_out_lat_lon, route_path_lat_lon, distance, visit_date, check_in_time, check_out_time, status, tracking_id, created_datetime, updated_datetime));
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
//                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

           /* SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Geo_tracing", "true");
            editor.commit();*/


        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
        if (sharedpreferences.getString("Geo_tracing", "") != null && sharedpreferences.getString("Geo_tracing", "").equalsIgnoreCase("true")) {

        } else {
            try {
                if (Common.haveInternet(this)) {

                    new Async_getall_geotracking().execute();

                } else {

                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
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

            String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  user_id in (" + team + ")";
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();
            System.out.println("selectQuery " + selectQuery);
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

        spin_company.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));
        spin_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customerId = organisations.get(position).getCityId();
                userName = organisations.get(position).getCityName();
                Common.saveDataInSP(getApplicationContext(), Constants.CUSTOMER_ID_GEO, customerId);

               /* if (customerId.equalsIgnoreCase("0")) {

                } else {
                   // ll_select.setVisibility(View.VISIBLE);
                   // new PlanerOneActivity.Async_getalloffline().execute();

                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void animateMarker(GoogleMap googleMap, final Marker marker, final LatLng toPosition,
                               final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 5000;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
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

if (myCurrentLocation!=null){
    handleNewLocation(myCurrentLocation);
}
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
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (myCurrentLocation!=null){
                            handleNewLocation(myCurrentLocation);
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


    public  void callDirectionAPI(List<LatLng> latLngs){
        Log.d("latLngs:",latLngs.toString());
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(latLngs)
                .build();
        routing.execute();
    }



    @Override
    public void onRoutingFailure(RouteException e) {
        Log.d("Route", e.toString());
    }

    @Override
    public void onRoutingStart() {
        Log.d("Route", "onRoutingStart..");
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        Log.d("Route", arrayList.toString());

    }

    @Override
    public void onRoutingCancelled() {

    }



}
