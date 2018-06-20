package com.nsl.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.nsl.app.scheduler.DailySchedulerReceiver;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String userId;
    DailySchedulerReceiver alarm = new DailySchedulerReceiver();
    private Context context;
    private String android_id;
    boolean isConnected;
    private static final String DATABASE_NAME = "NSL.db";

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        this.context=context;
        Log.d("ConnectivityManager", String.valueOf("ConnectivityManager..."));
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
         isConnected = activeNetwork != null && activeNetwork.isConnected();
        sharedpreferences     = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
        if(isConnected )
        {


            userId                = sharedpreferences.getString("userId", "");
            if (userId != "") {

                new Async_IsAppLoggedIn().execute();
            }

          //  Toast.makeText(context, "Network Available Do operations",Toast.LENGTH_LONG).show();
        }else{
            Log.d("ConnectivityManager", String.valueOf(isConnected));
            sharedpreferences.edit().putLong(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME,0).commit();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("sync_offline_coordinates", "true");
            editor.commit();
        }


    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }


    private class Async_IsAppLoggedIn extends AsyncTask<Void, Void, String> {
        String jsonData;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                Response responses = null;

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "user_id=" + userId+"&imei_number="+android_id );
                Request request = new Request.Builder()
                        .url(Constants.URL_APP_LOGGED_IN)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1login" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    JSONObject jsonobject = new JSONObject(s);
                    status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        if (sharedpreferences.contains(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME) && (System.currentTimeMillis() - sharedpreferences.getLong(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME, 0)) > 60000) {

                                Log.d("ConnectivityManager", String.valueOf(isConnected));
                                Intent backgroundIntent = new Intent(context, BackgroundPushService.class);
                                context.startService(backgroundIntent);

                                alarm.setAlarm(context);


                        }else if (!sharedpreferences.contains(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME)){

                                Log.d("ConnectivityManager", String.valueOf(isConnected));
                                Intent backgroundIntent = new Intent(context, BackgroundPushService.class);
                                context.startService(backgroundIntent);

                                alarm.setAlarm(context);

                        }

                        sharedpreferences.edit().putLong(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME,System.currentTimeMillis()).commit();


                    } else {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Divisions", "false");
                        editor.putString("userId", "");
                        editor.putInt(Constants.SharedPrefrancesKey.ROLE, 0);
                        editor.putString("fcm_id", "");
                        editor.commit();
                        context.deleteDatabase(DATABASE_NAME);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }


        }
    }
}