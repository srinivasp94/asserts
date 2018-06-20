package com.nsl.app;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private GoogleMap googleMap;
    DatabaseHandler   db;

    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    ArrayList<LatLng> points = new ArrayList<LatLng>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
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


        db                 = new DatabaseHandler(this);
        try {
            // Loading map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initilizeMap() {
        if (googleMap == null) {

            ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    // check if map is created successfully or not
                    if (googleMap == null) {
                        Toast.makeText(getApplicationContext(),
                                "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                                .show();
                    }
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.setPadding(0,0,0,100);



                    Location location = googleMap.getMyLocation();

                    if (location != null) {

                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                        CameraUpdate zoom   = CameraUpdateFactory.zoomTo(11);
                        googleMap.moveCamera(center);
                        googleMap.animateCamera(zoom);
                    }

                    // Reading all contacts
                    Log.d("Reading: ", "Reading all locations..");
                    List<Contact> contacts = db.getAllContacts();

                    for (Contact cn : contacts) {


                        String log = "Id: " + cn.getID() + " ,Latitude: " + cn.getName() + " ,Longitude: " + cn.getPhoneNumber();
                        // Writing Contacts to log
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(cn.getName()), Double.parseDouble(cn.getPhoneNumber()))).title(String.valueOf(cn.getID())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        //googleMap.addMarker(new MarkerOptions().position(latLng).title("My Spot").snippet("This is my spot!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
                        Log.d("Location: ", log);

                /*HashMap<String, String> map = new HashMap<String, String>();
                // adding each child node to HashMap key => value
                map.put("lat",   cn.getName());
                map.put("long",  cn.getPhoneNumber());
                favouriteItem.add(map);*/

                        MarkerOptions markerOptions = new MarkerOptions();

                        // Setting latitude and longitude of the marker position
                        LatLng point = new LatLng(Double.parseDouble(cn.getName()), Double.parseDouble(cn.getPhoneNumber()));
                        markerOptions.position(point);

                        // Setting titile of the infowindow of the marker
                        markerOptions.title("Position");

                        // Setting the content of the infowindow of the marker
                        markerOptions.snippet("Latitude:" + point.latitude + "," + "Longitude:" + point.longitude);

                        // Instantiating the class PolylineOptions to plot polyline in the map
                        PolylineOptions polylineOptions = new PolylineOptions();

                        // Setting the color of the polyline
                        polylineOptions.color(Color.RED);

                        // Setting the width of the polyline
                        polylineOptions.width(10);

                        // Adding the taped point to the ArrayList
                        points.add(point);

                        // Setting points of polyline
                        polylineOptions.addAll(points);

                        // Adding the polyline to the map
                        googleMap.addPolyline(polylineOptions);

                        // Adding the marker to the map
                        //googleMap.addMarker(markerOptions);

                    }
                    }
            });


        }


        //googleMap.getUiSettings().setRotateGesturesEnabled(true);
        //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        // googleMap.getUiSettings().setCompassEnabled(true);

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
            Intent home = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
