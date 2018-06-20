package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.Home_page;
import com.example.pegasys.rapmedixuser.activity.adapters.DoctorlistAdapter;
import com.example.pegasys.rapmedixuser.activity.adapters.PlaceArrayAdapter;
import com.example.pegasys.rapmedixuser.activity.pojo.SearchDoc;
import com.example.pegasys.rapmedixuser.activity.pojo.docSpecReq;
import com.example.pegasys.rapmedixuser.activity.pojo.specDoclist;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoctorlistPage extends AppCompatActivity implements RetrofitResponseListener, LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    RecyclerView mDoctorsrecyclerview;
    TextView header, address;
    ImageView menu, add_address;
    private AutoCompleteTextView actv;
    DoctorlistAdapter mAdapter;
    ArrayList<specDoclist> mDoclists = new ArrayList<>();
    private Object obj;
    private double currentLatitude;
    private double currentLongitude;
    private String id;
    private String selected_address;
    private String sTitle;
    private String sAddress;

    GoogleApiClient mGoogleApiClient;
    GoogleApiClient mGoogleApiClient1;
    GoogleMap googlemap1;
    PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(17.3660, 78.4760), new LatLng(17.3660, 78.4760));
    private String location;
    double lat, longg;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    MapFragment supportMapFragment;
    SharedPreferences sp;
    public static final String pref = "Location";

    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorlist_page);

        mDoctorsrecyclerview = (RecyclerView) findViewById(R.id.doctors_list);
        add_address = (ImageView) findViewById(R.id.add_address);
        address = (TextView) findViewById(R.id.location);
        if (getIntent() != null) {
            currentLatitude = getIntent().getDoubleExtra("lat", 0.0);
            currentLongitude = getIntent().getDoubleExtra("longg", 0.0);
            id = getIntent().getStringExtra("id");
            sTitle = getIntent().getStringExtra("TITLE");
            sAddress = getIntent().getStringExtra("location");
            Log.e("aa", currentLatitude + id + "ccc" + currentLongitude);
        }

        sp = getSharedPreferences(pref, MODE_PRIVATE);
        currentLatitude = Double.longBitsToDouble(sp.getLong("lattitude", Double.doubleToLongBits(0.0)));
        currentLongitude = Double.longBitsToDouble(sp.getLong("longitude", Double.doubleToLongBits(0.0)));
        location = sp.getString("address", "City");

        Log.e("aa", location + "ccc" + longg);
        if (location != null) address.setText(location);
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location_popup();
            }
        });
        actv = (AutoCompleteTextView) findViewById(R.id.search_input);
        actv.setHint(Html.fromHtml("<small>" +
                "Enter Doctor Name" + "</small>"));

        setrecyclerviewAdapter();
    }

    private void setrecyclerviewAdapter() {

        layoutManager = new LinearLayoutManager(DoctorlistPage.this);
        mDoctorsrecyclerview.setLayoutManager(layoutManager);


        //---------------SearchDoc && specDoclist ----//
        docSpecReq req = new docSpecReq();
        req.specialisationId = id;
        req.latitude = String.valueOf(currentLatitude);
        req.longitude = String.valueOf(currentLongitude);
        Log.e("aaparam", currentLatitude + id + "ccc" + currentLongitude);


        try {
            obj = Class.forName(docSpecReq.class.getName()).cast(req);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/searchdoctors_service", true);

    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(DoctorlistPage.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            SearchDoc doc = Common.getSpecificDataObject(objectResponse, SearchDoc.class);
            Gson gson = new Gson();
            if (doc.status.equals("success")) {
                mDoclists = (ArrayList<specDoclist>) doc.specialDoclist;
                for (int i = 0; i < mDoclists.size(); i++) {
                    String mName = mDoclists.get(i).name;
                    String mId = mDoclists.get(i).id;
                    String mSpecName = mDoclists.get(i).specialisationName;
                    String mDegreeName = mDoclists.get(i).degreeName;
                    String mHospitalName = mDoclists.get(i).hospitalName;
                    String mExpe = mDoclists.get(i).experience;
                }
                mAdapter = new DoctorlistAdapter(getApplicationContext(), mDoclists);
                mDoctorsrecyclerview.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new DoctorlistAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String mId = mDoclists.get(position).id;
                        Intent intent = new Intent(DoctorlistPage.this,DoctorDescription.class);
                        intent.putExtra("doctorId", mId);
                        startActivity(intent);

                    }
                });
            } else {
                Toast.makeText(DoctorlistPage.this, "" + doc.status, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void location_popup() {

        final Dialog dialog = new Dialog(DoctorlistPage.this);
        // Include dialog.xml file
        dialog.setCancelable(false);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_map__loc);

        mGoogleApiClient1 = new GoogleApiClient.Builder(DoctorlistPage.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(DoctorlistPage.this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(DoctorlistPage.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(DoctorlistPage.this)
                .addOnConnectionFailedListener(this)
                .build();

        AutoCompleteTextView mAutocompleteTextView =
                (AutoCompleteTextView) dialog.findViewById(R.id.input_address);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setVisibility(View.VISIBLE);


//        Window window = dialog.getWindow();
        //   window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

//        window.setGravity(Gravity.CENTER);
//        window.setBackgroundDrawable(new ColorDrawable(
//                Color.TRANSPARENT));


        dialog.setCancelable(false);


        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        supportMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);

        if (googlemap1 != null)

        {
            Log.e("mapmap", "map");
            googlemap1.clear();

            LatLng latLng0 = new LatLng(currentLatitude, currentLongitude);
            googlemap1.moveCamera(CameraUpdateFactory.newLatLng(latLng0));
            googlemap1.animateCamera(CameraUpdateFactory.zoomTo(15f));
            googlemap1.addMarker(new MarkerOptions()
                    .position(latLng0)
                    .title(selected_address)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }


        mAutocompleteTextView.setThreshold(2);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        //mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (actv.getText().toString().equals("") || actv.getText().toString().isEmpty()) {
                    sp = getSharedPreferences(pref, MODE_PRIVATE);


                    lat = Double.longBitsToDouble(sp.getLong("lattitude", Double.doubleToLongBits(0.0)));
                    longg = Double.longBitsToDouble(sp.getLong("longitude", Double.doubleToLongBits(0.0)));
                    location = sp.getString("address", "City");

                    Log.e("loc", location + "ccc" + longg);


                    SharedPreferences.Editor edit = sp.edit();
                    edit.putLong("lattitude", Double.doubleToRawLongBits(currentLatitude));
                    edit.putLong("longitude", Double.doubleToRawLongBits(currentLongitude));
                    if (!selected_address.equals("")) {
                        Log.e("aa", selected_address + "ccc");
                        edit.putString("address", selected_address);

                    } else {
                        Log.e("aap", location + "ccc");
                        edit.putString("address", location);
                    }
                    edit.commit();
                    Log.e("selected_address", currentLatitude + "" + currentLongitude + "location" + location + "selected_address" + selected_address);
                    setrecyclerviewAdapter();
                   /* JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("specialisation_id", id);
                        jsonObject.put("latitude", currentLatitude);
                        jsonObject.put("longitude", currentLongitude);
                        Log.e("log_loc", currentLatitude + "Fetching details for ID: 22" + currentLongitude);

                        mobile_verification(jsonObject, Constants.getDoctors);

                        if (doctor_list_adapter != null) {
                            doctor_list_adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                    if (supportMapFragment != null) {
                        getFragmentManager().beginTransaction().remove(supportMapFragment).commitAllowingStateLoss();
                        supportMapFragment = null;

                    }


                    dialog.dismiss();
                    googlemap1 = null;

                    mGoogleApiClient1.stopAutoManage(DoctorlistPage.this);
                    mGoogleApiClient1.disconnect();


                } else {
                    Toast.makeText(DoctorlistPage.this, "Select Location ", Toast.LENGTH_LONG).show();

                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (supportMapFragment != null) {
                    getFragmentManager().beginTransaction().remove(supportMapFragment).commitAllowingStateLoss();
                    supportMapFragment = null;
                    googlemap1 = null;

                }

                dialog.dismiss();

                mGoogleApiClient1.stopAutoManage(DoctorlistPage.this);
                mGoogleApiClient1.disconnect();

            }
        });


        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog0 = new AlertDialog.Builder(this);
            dialog0.setMessage("gps network not enabled");
            dialog0.setPositiveButton("open location settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog0.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });

            dialog0.show();
        }
        //show error dialog if GoolglePlayServices not available

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {

                // Snackbar.make(linearLayout, "Permission already granted.", Snackbar.LENGTH_LONG).show();

            } else {

                //Snackbar.make(linearLayout, "Please request permission.", Snackbar.LENGTH_LONG).show();
                requestPermission();
                //  Snackbar.make(linearLayout, "Permission already granted.", Snackbar.LENGTH_LONG).show();
            }
        }
        dialog.show();

    }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Toast.makeText(this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient1);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mPlaceArrayAdapter.setGoogleApiClient(null);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleApiClient.connect();

        if (googlemap1 == null) {
            googlemap1 = googleMap;
            googlemap1.getUiSettings().setMyLocationButtonEnabled(true);
            LatLng latLng = new LatLng(currentLatitude, currentLongitude);
            googlemap1.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googlemap1.animateCamera(CameraUpdateFactory.zoomTo(10));

            googlemap1.clear();
            LatLng latLng0 = new LatLng(currentLatitude, currentLongitude);
            googlemap1.moveCamera(CameraUpdateFactory.newLatLng(latLng0));
            googlemap1.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
            googlemap1.addMarker(new MarkerOptions()
                    .position(latLng0)
                    .title(selected_address)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        }

        googlemap1.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                //  googlemap1.addMarker(new MarkerOptions().position(latLng));


                Geocoder gcd = new Geocoder(DoctorlistPage.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    Log.e("cityyyy", addresses + "city" + addresses.get(0).getLocality());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //   System.out.println(addresses.get(0).getLocality());

                // Log.e("city",addresses.get(0).getSubLocality()+"city"+addresses.get(0).getLocality());

               /* Intent intent=new Intent(Home_Page.this,Home_Page.class);
                intent.putExtra("area",addresses.get(0).getSubLocality());
                intent.putExtra("city",addresses.get(0).getLocality());
                startActivity(intent);*/


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

}
