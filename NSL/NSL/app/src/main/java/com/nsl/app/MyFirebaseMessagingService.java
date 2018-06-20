package com.nsl.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nsl.app.advancebooking.AdvanceBookingMainActivity;
import com.nsl.app.commonutils.Common;
import com.nsl.app.orderindent.OrderIndentMainActivity;
import com.nsl.app.pojo.FfmIdStatusVo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Akshay Raj on 5/31/2016.
 * Snow Corporation Inc.
 * www.snowcorp.org
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    NotificationCompat.Builder notificationBuilder;
    Intent intent ;
    Bitmap image;
    private DatabaseHandler databaseHandler;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
       // Log.d(TAG, "From: " + remoteMessage.getFrom());
       // Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //Log.d(TAG, "FCM Data: " + remoteMessage.getData());
Log.d("notificationdata",remoteMessage.getData().toString());
        databaseHandler =new DatabaseHandler(getApplicationContext());

        String tag    = remoteMessage.getData().get("title");
        String msg    = remoteMessage.getData().get("message");
        String img    = remoteMessage.getData().get("image");
        String screen = remoteMessage.getData().get("screen");
        if(screen.equalsIgnoreCase("evm_update")) {
            String info = remoteMessage.getData().get("info");

            FfmIdStatusVo ffmIdStatusVo=Common.getSpecificDataObject(info, FfmIdStatusVo.class);
//            String ffm_id = remoteMessage.getData().get("ffm_id");
//            String status = remoteMessage.getData().get("status");
            databaseHandler.updateAprrovalStatus(ffmIdStatusVo.ffmId,ffmIdStatusVo.ffmId,ffmIdStatusVo.status);

        }else if (screen.equalsIgnoreCase("evm")){


        }

        image = getBitmapFromURL(img);


        sendNotification(tag, msg, image, screen);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param screen
     */
    private void sendNotification(String tag, String messageBody, Bitmap img, String screen) {

       /* if(screen.equalsIgnoreCase("adv")){
            intent= new Intent(this, AdvanceBookingMainActivity.class);

        }
        else if(screen.equalsIgnoreCase("sod")){
            intent= new Intent(this, OrderIndentMainActivity.class);

        }
        else if(screen.equalsIgnoreCase("evm_update")){
            intent= new Intent(this, PlanerMainActivity.class);

        }else if(screen.equalsIgnoreCase("evm")){
            intent= new Intent(this, PlanerMainActivity.class);

        }else if(screen.equalsIgnoreCase("pc")){
            intent= new Intent(this, PaymentCollectionPaymentTypeActivity.class);
        }else if(screen.equalsIgnoreCase("cp")){
            intent= new Intent(this, PaymentCollectionPaymentTypeActivity.class);
        }else if(screen.equalsIgnoreCase("fb")){
            intent= new Intent(this, PaymentCollectionPaymentTypeActivity.class);
        }else {
            intent=new Intent();
        }*/

        intent= new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
/*
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
        contentView.setImageViewResource(R.id.image, R.mipmap.nsl_notification_logo);
        contentView.setTextViewText(R.id.title, "Nuziveedu Seeds");
        contentView.setTextViewText(R.id.time, time);
      //  contentView.setTextViewText(R.id.text, "Testing is awecome gdgfdgdfgdfgdfgdfhdfdfdfd bdfhgdfghdfghdfdfgdf dfsgdfgfdgdfgdfgfdgdfgdf dfgdfgdfgdfgdfgfdgfdgdfgdfg dfgdfgfdg  dgdfgdfgdfgdfgdfg d");
      */
NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(messageBody);
        if (tag.equalsIgnoreCase("image")) {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.nsl_notification_logo)
                    .setContentTitle("Nuziveedu Seeds")
                //    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                          .bigPicture(img))
                    .setStyle(bigTextStyle)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                   .setSmallIcon(R.mipmap.nsl_notification_logo)
                    .setTicker("Nuziveedu Seeds")
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                  //  .setColor(getResources().getColor(R.color.black))
                    .setStyle(bigTextStyle)
                    .setContentTitle("Nuziveedu Seeds")
                //    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
