package com.example.pegasys.rapmedixuser.activity.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.RegisterActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pegasys on 12/5/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String Mess;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String str0 = remoteMessage.getData().get("message");
        Log.e("mess","mess"+str0);

        String str = remoteMessage.getData().get("message");
        try {
            JSONObject j = new JSONObject(str);

            Mess = j.getString("message");
            Log.e("JsonStr", Mess);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ShowNotification(Mess);

    }

    private void ShowNotification(String message)
    {

        Intent i = new Intent(this, RegisterActivity.class)
                .putExtra("Pending_Intent",1);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon((BitmapFactory.decodeResource(getResources(), R.mipmap.ic_app)))
                .setSmallIcon(R.mipmap.ic_app)
                .setContentTitle("Rapmedix")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }
}
