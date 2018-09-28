package com.example.pegasys.rapmedixuser.activity.receivers;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by ADMIN on 9/16/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    SharedPreferences sp;
    public static final String pref="Notif";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        Log.e("TOKEN","ccc TOKEN");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("PushNotification", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

        sp = getSharedPreferences(pref, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("Notification", refreshedToken);
        Log.e("aa","ccc"+refreshedToken);

        edit.commit();



    }
}
