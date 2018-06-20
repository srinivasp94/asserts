package com.nsl.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.nsl.app.commonutils.Common;
import com.nsl.app.scheduler.LocationService;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.app.DatabaseHandler.TABLE_GEO_TRACKING;

public class GPSTracker extends Service implements LocationListener {

	public static  final String TAG = GPSTracker.class.getName();
	private static final int REQUEST_CHECK_SETTINGS = 11;
	private Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000*30*1; // 2 minutes

	// Declaring a Location Manager
	protected LocationManager locationManager;
	DatabaseHandler db;
	SQLiteDatabase sdbw, sdbr;
	public static final String mypreference = "mypref";

	// Binder given to clients
	private final IBinder mBinder = new GpsBinder();
	public String checkinlatlong;
	public SharedPreferences sharedpreferences;
	private String jsonData;
	volatile boolean isPushToServer = false;
	public String trackingId,tid, existedroutepath;

	public GPSTracker() {
	}

	public GPSTracker(Context context) {
		this.mContext = context;

	}

	/**
	 * Class used for the client Binder.  Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class GpsBinder extends Binder {
		GPSTracker getService() {
			// Return this instance of LocalService so clients can call public methods
			return GPSTracker.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		db = new DatabaseHandler(this);
		sharedpreferences     = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
		this.mContext = getApplicationContext();
		getLocation();
		Log.v(TAG,"onCreate");
	}



	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG,"onDestroy");
//		isPushToServer = false;
//		stopUsingGPS();

	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	/**
	 * Function to get latitude
	 */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 *
	 * @return boolean
	 */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 */
	public void showSettingsAlert(Context context) {
		/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();*/




	}


	public void displayGoogleLocationSettingPage(final Activity activity) {

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
						//Common.myCurrentLocationCamera(currentLocation, gMap);
						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						// Location settings are not satisfied. But could be fixed by showing the user
						// a dialog.
						Toast.makeText(activity, "RESOLUTION_REQUIRED", Toast.LENGTH_SHORT).show();
						try {
							// Show the dialog by calling startResolutionForResult(),
							// and check the result in onActivityResult().
							status.startResolutionForResult(
									activity,
									REQUEST_CHECK_SETTINGS);
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
	public void onLocationChanged(Location location) {
		this.location = location;

	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}


	public void startPushThread(){
		Intent intent =new Intent(this, LocationService.class);
		startService(intent);
		Log.v(TAG,"startPushThread");
		trackingId = sharedpreferences.getString("tracking_id", null);
		checkinlatlong = sharedpreferences.getString("checkinlatlong",null);
		isPushToServer = true;
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				while(isPushToServer) {
					try {
						Log.v(TAG,"sleep(20000)");
						Thread.sleep(2000);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					afficher();
				}
			}
		}).start();*/

	}
	private void afficher() {

			double latitude = getLatitude();
			double longitude = getLongitude();

		Location location=new Location("");
		location.setLatitude(latitude);
		location.setLongitude(longitude);


		Log.d("save_lat_long.1","latitude "+latitude);
		Location locationFromSp = Common.getCurrentLocationFromSP(this);
		if (locationFromSp.getLatitude()==0 && locationFromSp.getLongitude()==0){
			Log.d("save_lat_long.2","latitude "+latitude);
			Common.saveCurrentLocationInSP(this,latitude,longitude);
		}else {
			Log.d("save_lat_long.3","latitude "+latitude);
			float distance = location.distanceTo(locationFromSp);
			if (distance<1){
				Log.d("save_lat_long.5","latitude "+latitude);
				return;
			}else {
				Log.d("save_lat_long.6","latitude "+latitude);
				Common.saveCurrentLocationInSP(this,latitude,longitude);

				checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude);
				//Toast.makeText(getApplicationContext(),checkinlatlong,Toast.LENGTH_LONG).show();
				Calendar c = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				String strDate = sdf.format(c.getTime());
				/**
				 * CRUD Operations
				 * */
				// Inserting Contacts
				Log.d("Insert: ", "Inserting ..");
				String selectQueryss = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID +","+KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG  +" FROM " + TABLE_GEO_TRACKING + " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
				sdbw = db.getWritableDatabase();

				Cursor ccc = sdbw.rawQuery(selectQueryss, null);
				//System.out.println("cursor count "+cursor.getCount());
				if (ccc != null && ccc.moveToFirst()) {
					tid = String.valueOf(ccc.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0
					existedroutepath   = String.valueOf(ccc.getString(1));
					if (existedroutepath.equalsIgnoreCase("0")){
						existedroutepath=checkinlatlong;
					}
					else{
						existedroutepath =existedroutepath+":"+ checkinlatlong;

					}
				/*if(latitude!=0.0 && longitude!=0.0)
				*/

					Log.e("++++ lastId ++++" , tid +": existed route path "+ existedroutepath);
				}


				String updatequerys = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + " = '" + existedroutepath + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + tid;
				sdbw.execSQL(updatequerys);
				// db.addGeotracking(new Geo_Tracking_POJO("",sel_visittype,userId,checkinlatlong,"",checkinlatlong,str_distance,visit_date,strDate,"",visit_date,visit_date,"","1"));

				//	Toast.makeText(getApplicationContext(), String.valueOf(latitude) + String.valueOf(longitude), Toast.LENGTH_SHORT).show();


				if(Utility.isNetworkAvailable(GPSTracker.this,false)) {

					new Async_Routepath().execute();

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
						.add("latlon", checkinlatlong)
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
}