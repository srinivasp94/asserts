package com.nsl.app.fieldestimation;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.nsl.app.Constants;
import com.nsl.app.MyApplication;
import com.nsl.app.R;
import com.nsl.app.commonutils.Common;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sys on 6/8/2017.
 */

public class FieldEstimationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback {

    @BindView(R.id.btn_calculate_area)
    Button btn_btnCalculateArea;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private static final int REQUEST_CHECK_SETTINGS = 111;
    ArrayList<LatLng> polyline = new ArrayList<>();
    private GoogleMap mGoogleMap;
    boolean isEstimationStarted = false;
    private float ACCURACY = 60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_estimation);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocationUpdates();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startLocationUpdates();
    }

    @OnClick(R.id.btn_calculate_area)
    void setBtn_btnCalculateArea() {
        displayGoogleLocationSettingPage(this, REQUEST_CHECK_SETTINGS);


    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            if (mGoogleMap != null) {
                myCurrentLocationCamera(mGoogleMap, location);
            }
            if (isEstimationStarted && location.getAccuracy() < ACCURACY) {

                polyline.add(new LatLng(location.getLatitude(), location.getLongitude()));
                addMarkerOnMap(new LatLng(location.getLatitude(), location.getLongitude()), getResources().getDrawable(R.drawable.dot_red));
                if (polyline.size() > 1) {
                    ArrayList<LatLng> latLngs = new ArrayList<>();
                    latLngs.add(polyline.get(polyline.size() - 2));
                    latLngs.add(polyline.get(polyline.size() - 1));
                    addPolyline(latLngs);
                }
            }
        }

    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                MyApplication.getInstance().getGoogleApiClient(), this);
        Log.d("startLocationUpdates", "Location update stopped .......................");
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                MyApplication.getInstance().getGoogleApiClient(), MyApplication.getInstance().getLocationRequest(), this);
        Log.d("startLocationUpdates", "Location update started ..............: ");
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

                                calculateArea();
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
                        calculateArea();
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


    public void calculateArea() {

        Log.d("isEstimationStarted", String.valueOf(isEstimationStarted));
        if (!isEstimationStarted) {
            if (MyApplication.getInstance().getGoogleApiClient() != null && Common.haveInternet(this)) {
                mGoogleMap.clear();
                polyline.clear();
                tvArea.setText("Area:" + 0 + " m");
                Common.getDefaultSP(this).edit().putString("latlng_field_estimation", null).commit();
                Common.getDefaultSP(this).edit().putString("area_in_hectare", null).commit();
                startLocationUpdates();
                isEstimationStarted = true;
                btn_btnCalculateArea.setText(Common.getStringResourceText(R.string.stop));
            } else {
                Toast.makeText(this, Common.INTERNET_UNABLEABLE, Toast.LENGTH_SHORT).show();

            }
        } else {
            isEstimationStarted = false;
            stopLocationUpdates();
            btn_btnCalculateArea.setText(Common.getStringResourceText(R.string.start));
            double area =0;
            if (polyline!=null && polyline.size()>0) {
                polyline.add(polyline.get(0));
                 area = SphericalUtil.computeArea(polyline);
                // double areaInHectare = area / 10000;
                Gson gson = new Gson();
                String json = gson.toJson(polyline);
                Common.getDefaultSP(this).edit().putString("latlng_field_estimation", json).commit();
                Common.getDefaultSP(this).edit().putString("area_in_hectare", String.valueOf(area)).commit();
                // Log.d("isEstimationStarted", "Area:" +area+"\n"+ Math.round(areaInHectare*10000)/10000 +" H");
                tvArea.setText("Area:" + area + " m");
            }
            if (getIntent().hasExtra("yield")) {
                Intent intent = new Intent();
                intent.putExtra("area", area);
                this.setResult(Activity.RESULT_OK, intent);

                finish();

            }


        }
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.setPadding(0, 30, 30, 100);

        if (Common.getDefaultSP(this).contains("latlng_field_estimation")) {
            String latlngFieldEstimation = Common.getDefaultSP(this).getString("latlng_field_estimation", null);
            if (latlngFieldEstimation != null && latlngFieldEstimation.length() > 20) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<LatLng>>() {
                }.getType();
                ArrayList<LatLng> latLngs = gson.fromJson(latlngFieldEstimation, type);
                Log.d("latLngs", latLngs.toString());
                if (latLngs.size() > 2) {
                    addPolyline(latLngs);
                }
                tvArea.setText("" + Common.getDefaultSP(this).getString("area_in_hectare", null));


            }
        }

    }

    public void myCurrentLocationCamera(GoogleMap googleMap, Location location) {
        Log.d("lastknownLocation", location.toString());
        if (location != null && googleMap != null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            Log.d("lastknownLocation", location.toString() + " " + googleMap.getCameraPosition());

        }
    }

    public void addMarkerOnMap(LatLng latLng, Drawable drawableId) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(getMarkerIconFromDrawable(drawableId));
        mGoogleMap.addMarker(options);
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    void addPolyline(ArrayList<LatLng> latLngs) {
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(latLngs);
        lineOptions.width(10);
        lineOptions.color(Color.RED);
        mGoogleMap.addPolyline(lineOptions);
    }
}
