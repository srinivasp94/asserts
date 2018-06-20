package com.nsl.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_VISIT_DATE;
import static com.nsl.app.DatabaseHandler.TABLE_GEO_TRACKING;

public class PlannerMapActivityCopy extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener  {
    GoogleMap                map;
    DatabaseHandler          db;
    ArrayList<MarkerData>        markerPoints;
    LatLng                   point;
    String                   userId,jsonData,datefromcalander,datefromrecords,str_address,str_name;
    SQLiteDatabase           sdbw, sdbr;
    SimpleDateFormat         sdf;
    SharedPreferences        sharedpreferences;
    ProgressDialog           progressDialog;
    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient  mGoogleApiClient;
    private LocationRequest  mLocationRequest;

    String                   latitude,longitude;
    public static final String mypreference = "mypref";
    private TextView tv_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_routemap);

        sharedpreferences     = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId                = sharedpreferences.getString("userId", "");
        sdf                   = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        tv_distance = (TextView)findViewById(R.id.tv_distance);

        Toolbar toolbar       = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initializing
        markerPoints         = new ArrayList<MarkerData>();
        db                   = new DatabaseHandler(this);
        datefromcalander     = PlanerMainActivity.curDate;
       // map                = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        if (map == null) {
            ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    map=googleMap;
                    // check if map is created successfully or not
                    if (map == null) {
                        Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
                    }
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setZoomControlsEnabled(true);
                    map.setPadding(0,30,30,100);
                    //googleMap.getUiSettings().setRotateGesturesEnabled(true);
                    //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    // googleMap.getUiSettings().setCompassEnabled(true);
                }
            });



        }



        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // Already 10 locations with 8 waypoints and 1 start location and 1 end location.
                // Upto 8 waypoints are allowed in a query for non-business users
                /*if (markerPoints.size() >= 10) {
                    return;
                }
                // Adding new item to the ArrayList
                markerPoints.add(point);
                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
                // Setting the position of the marker
                options.position(point);

               *//* *
                *For the start location, the color of marker is GREEN and
                *for the end location, the color of marker is RED and
                *for the rest of markers, the color is AZURE*//*

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }

                // Add new marker to the Google Map Android API V2
                map.addMarker(options);
                if (markerPoints.size() >= 2) {
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();
                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }*/
            }
        });


        // The map will be cleared on long click
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                // Removes all the points from Google Map
               /* map.clear();
                // Removes all the points in the ArrayList
                markerPoints.clear();*/

            }
        });

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
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        try {
            ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo != null){  // connected to the internet
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    new Async_getalloffline().execute();
                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan

                    new Async_getalloffline().execute();
                }
            }else{
                Log.e("No network","need to call service to do in background");
                Toast.makeText(getApplicationContext(), " Please Connect to internet to view route map ", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }



    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=2;i<markerPoints.size();i++){
            LatLng point  = markerPoints.get(i).latLng;
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }


        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
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

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception downloading ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
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
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            String distance="";
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                       String duration = (String)point.get("duration");
                        continue;
                    }
                    if(point!=null && point.get("lat")!=null && point.get("lng")!=null){

                    double lat      = Double.parseDouble(point.get("lat"));
                    double lng      = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position); }
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
            if(distance!=null)
            tv_distance.setText(" Total Distance : "+distance);
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } else {
            handleNewLocation(location);
        }
    }
    private void handleNewLocation(Location location) {
        latitude                  = String.valueOf(location.getLatitude());
        longitude                 = String.valueOf(location.getLongitude());
        LatLng newLatLng          = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(newLatLng, 12);
        map.animateCamera(yourLocation);
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
    private class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlannerMapActivityCopy.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            markerPoints.clear();
        }

        protected String doInBackground(Void... params) {


            try {

                String selectQuery = "SELECT DISTINCT "
                        + KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_VISIT_DATE
                        + " FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + userId;
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

                                Log.e(" ++Dates++ ", datefromcalander + " & " + datefromrecords);


                                ArrayList<String> latlng = new ArrayList(Arrays.asList(routepath.split(":")));

                                markerPoints.clear();

                                /*if(count($route_path)>0){

                                    $dcount=count($route_path)/9;
                                    $round=round($dcount);
                                    $j=0;
                                    for($i=0;$i<count($route_path);$i++){

                                        if($j==$i){
                                            $j=$j+$round;
                                            if(isset($route_path[$i])){
                                                list($lat,$lon)=explode(",",$route_path[$i]);
                                                $pathArr[]=array('lat'=>$lat,'lng'=>$lon);
                                            }
                                        }
                                    }

                                }*/
                                if (latlng.size()>0) {

                                    int index = -1;
                                    index = latlng.size() / 9;
                                    int j= 0;
                                    for (int i = 0; i < latlng.size(); i++)
                                    // Adding new item to the ArrayList
                                    {

                                        if (j == i ) {
                                            j = j+index;
                                            String latlngvalues = latlng.get(i);
                                            String latlngList[] = latlngvalues.split(",");
                                            double lat          = Double.parseDouble(latlngList[0]);
                                            double lng          = Double.parseDouble(latlngList[1]);
                                            point               = new LatLng(lat, lng);
                                            Log.e("Points after trim", String.valueOf(point));
                                            if (markerPoints.size() < 11)
                                                markerPoints.add(new MarkerData(point, "", ""));
                                        }
                                    }
                                }
                                Log.v("latlng", "" + latlng.size());
                                Log.v("markerpoints", "" + markerPoints.size());
                                //  markerPoints.add(new MarkerData(point,"",""));





                                /* Log.e("---inserted values --","event_name :"+cursor.getString(0)
                                        + "\n visit_plaanetype"+cursor.getString(1)
                                        + "\n event_status"+cursor.getString(2)
                                        + "\n event_datetime"+cursor.getString(3)
                                        + "\n event_purpose"+cursor.getString(4)
                                        + "\n type :"     +cursor.getString(5)
                                        + "\n geoid :"    +cursor.getString(6)
                                        + "\n userid :"+cursor.getString(7)
                                        + "\n event_customer_id"+cursor.getString(8)
                                        + "\n mobile :"+cursor.getString(9)
                                        + "\n village :"+cursor.getString(10)
                                        + "\n location address :"+cursor.getString(11)
                                        + "\n field area :"+cursor.getString(12)
                                        + "\n checkin :"+cursor.getString(13)
                                        + "\n comments"+cursor.getString(14)
                                        + "\n approval status"+cursor.getString(15)
                                        + "\n Event date"+cursor.getString(16)
                                        + "\n Event purpose"+cursor.getString(17)
                                        + "\n masterid :"+cursor.getString(18)
                                        + "\n event venue :"+cursor.getString(19)
                                        + "\n event participents :"+cursor.getString(20)
                                        + "\n ffmid :"    +cursor.getString(21)
                                        + "\n createdby :"+cursor.getString(22)
                                        + "\n updatedby :"+cursor.getString(23)
                                        + "\n cdatetime :"+cursor.getString(24)
                                        + "\n udatetime :"+cursor.getString(25)
                                        + "\n eventname :"+cursor.getString(26));*/

                            } else {

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
            }
            for (int i = 0; i < markerPoints.size(); i++) {
                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
                // Setting the position of the marker
                //options.position(markerPoints.get(i).latLng).title(markerPoints.get(i).str_name).snippet(markerPoints.get(i).str_address);
                options.position(markerPoints.get(i).latLng);

                if (i == 0) {
                    Log.e("first location", "");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (i == markerPoints.size() - 1) {
                    Log.e("last location", "");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else {
                    Log.e("middle location", "");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }

                // Add new marker to the Google Map Android API V2
                map.addMarker(options);
            }

            if (markerPoints.size() >= 2) {
                LatLng origin = markerPoints.get(0).latLng;
                LatLng dest = markerPoints.get(1).latLng;

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }


        }
    }
    @Override
    public void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
    }


}
