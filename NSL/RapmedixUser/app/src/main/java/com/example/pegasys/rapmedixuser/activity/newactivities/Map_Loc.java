package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.PlaceArrayAdapter;
import com.example.pegasys.rapmedixuser.activity.utils.ConnectionDetector;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Map_Loc extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    GoogleApiClient mGoogleApiClient1;
    GoogleMap googlemap1;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(17.3660, 78.4760), new LatLng(17.3660, 78.4760));
    double currentLatitude = 0.0, currentLongitude = 0.0;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    SharedPreferences sp;
    public static final String pref = "Location";
    double lat, longg;
    String selected_address;
    View view;
    public static final int mLocationRequestHighAccuracy = 100;
    public static final int mLocationRequestBalancedPowerAccuracy = 104;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 125;
    LocationManager manager;
    public static final int REQUEST_CHECK_SETTINGS = 10;
    AutoCompleteTextView mAutocompleteTextView;
    Button submit;
    PlaceArrayAdapter mPlaceArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map__loc);

        sp = getSharedPreferences(pref, MODE_PRIVATE);

        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MapFragment supportMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
                    supportMapFragment.getMapAsync(Map_Loc.this);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }


        submit = findViewById(R.id.submit);
        mAutocompleteTextView = findViewById(R.id.input_address);


        mGoogleApiClient1 = new GoogleApiClient.Builder(Map_Loc.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mAutocompleteTextView.setThreshold(2);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(Map_Loc.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
                    String cityName = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getAddressLine(1);
                    String countryName = addresses.get(0).getAddressLine(2);
                    selected_address = cityName + "," + stateName + "," + countryName;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("address", "no address");

                }

                SharedPreferences.Editor edit = sp.edit();
                edit.putLong("lattitude", Double.doubleToRawLongBits(currentLatitude));
                edit.putLong("longitude", Double.doubleToRawLongBits(currentLongitude));
                edit.putString("address", selected_address);
                edit.commit();
/*
                Intent intent=new Intent(Map_Loc.this,Home_Page.class);
                startActivity(intent);*/
                finish();


            }
        });

        Location_verification();

    }


    ResultCallback<LocationSettingsResult> mResultCallbackFromSettings = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(LocationSettingsResult result) {
            final Status status = result.getStatus();
            //final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                                Map_Loc.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.e("tag", "Settings change unavailable. We have no way to fix the settings so we won't show the dialog.");
                    break;
            }
        }
    };


    public void Location_verification() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            if (noLocation()) {
                //Log.e("verify if", "if");

            } else {

                if (mGoogleApiClient == null) {

                    mGoogleApiClient = new GoogleApiClient.Builder(Map_Loc.this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(Map_Loc.this)
                            .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                                @Override
                                public void onConnectionFailed(ConnectionResult connectionResult) {

                                    //   Log.e("Location error ", "" + connectionResult.getErrorCode());
                                }
                            }).build();
                    mGoogleApiClient.connect();


                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(1 * 1000);
                    locationRequest.setFastestInterval(5 * 100);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);

                    builder.setAlwaysShow(true);

                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        status.startResolutionForResult(
                                                Map_Loc.this, REQUEST_CHECK_SETTINGS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SUCCESS:
                                    Log.e("locccccc", "LasKnown location");
                                    getLastKnownLocation();

                                    break;
                            }
                        }
                    });


                }
            }
        } else {
            Toast.makeText(Map_Loc.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean noLocation() {
        final LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableLocation();
            return true;
        }
        return false;

    }

    private void enableLocation() {


        if (mGoogleApiClient == null) {
            // Log.e("loooo", "LasKnown location");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            //  Log.e("Location error ", "" + connectionResult.getErrorCode());
                        }
                    }).build();
            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1 * 1000);
            locationRequest.setFastestInterval(5 * 100);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        Map_Loc.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SUCCESS:
                            //calLoc();
                            //   Log.e("loc", "LasKnown location");
                            getLastKnownLocation();
                            break;
                    }
                }
            });
        }


    }

    private void getLastKnownLocation() {
        //Log.e(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                LocationRequest locationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(1000)
                        .setFastestInterval(900);

                if (checkPermission())
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);


            } else {
                LocationRequest locationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(1000)
                        .setFastestInterval(900);

                if (checkPermission())
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);


            }
        }
        // else askPermission();
        else {

        }

    }

    private boolean checkPermission() {
        //Log.e(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            selected_address = String.valueOf(item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient1, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            //    Log.i("log", "Fetching details for ID: " + item.placeId);

        }
    };
    ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
              /*  Log.e("log", "Place query did not complete. Error: " +
                        places.getStatus().toString());*/
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            LatLng latLng = place.getLatLng();
            currentLatitude = latLng.latitude;
            currentLongitude = latLng.longitude;
            //Log.i("log", currentLatitude+"Fetching details for ID: " + currentLongitude);


            googlemap1.clear();

            LatLng latLng0 = new LatLng(currentLatitude, currentLongitude);
            googlemap1.moveCamera(CameraUpdateFactory.newLatLng(latLng0));
            googlemap1.animateCamera(CameraUpdateFactory.zoomTo(11));
            googlemap1.addMarker(new MarkerOptions()
                    .position(latLng0)
                    .title(selected_address));
            CharSequence attributions = places.getAttributions();

           /* mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");*/
            if (attributions != null) {
                // mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        if (mGoogleApiClient.isConnected()) {
                            getLastKnownLocation();

                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        //  Log.e("location",location+"locationnnn");
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng centerOfMap = googlemap1.getCameraPosition().target;
        googlemap1.clear();
        googlemap1.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googlemap1.animateCamera(CameraUpdateFactory.zoomTo(13));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googlemap1.addMarker(markerOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap1 = googleMap;
        googlemap1.getUiSettings().setMyLocationButtonEnabled(true);
        //  Log.e("","city");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.create());

        googlemap1.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                //  googlemap1.addMarker(new MarkerOptions().position(latLng));


                Geocoder gcd = new Geocoder(Map_Loc.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    //  Log.e("cityyyy",addresses+"city"+addresses.get(0).getLocality());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void getCurrentLocatoin() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            //  Log.e("crashingggg","crashing"+mLastLocation);

            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();

            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            googlemap1.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googlemap1.animateCamera(CameraUpdateFactory.zoomTo(13));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            googlemap1.addMarker(markerOptions);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                selected_address = cityName + "," + stateName + "," + countryName;
            } catch (IOException e) {
                e.printStackTrace();
                //Log.e("address","no address");

            }

        } else {
            //Log.e("crashing","crashing");
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Geocoder geocoder = new Geocoder(Map_Loc.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
            selected_address = cityName + "," + stateName + "," + countryName;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("address", "no address");
        }

        SharedPreferences.Editor edit = sp.edit();
        edit.putLong("lattitude", Double.doubleToRawLongBits(currentLatitude));
        edit.putLong("longitude", Double.doubleToRawLongBits(currentLongitude));
        edit.putString("address", selected_address);
        edit.commit();
      /*  Intent intent=new Intent(Map_Loc.this,Home_Page.class);
        startActivity(intent);*/
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
