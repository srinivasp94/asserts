package com.example.pegasys.rapmedixuser.activity.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSms extends BroadcastReceiver
{


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e("IncomingSms", "intent");

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj .length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();
                    String finalNumber = senderNum.substring(3,9);
                    Log.e("IncomingSms", finalNumber);

                    try
                    {
                        if (finalNumber.equals("RAPMED"))
                        {
                            final StringBuilder sb = new StringBuilder(message.length());

                            for (int m = 0; m < message.length(); m++) {

                                final char c = message.charAt(m);
                                if(c > 47 && c < 58){
                                    Log.e("IncomingSms", sb.toString());
                                    sb.append(c);
                                }
                            }

                            if (!sb.toString().equals("") || sb.toString() != null){
                            context.sendBroadcast(new Intent("SMS_SENT").putExtra("message",sb.toString()));
                                Log.e("IncomingSms", "Exception"+sb);
                            }
                            else Log.e("IncomingSms", "Exception");


                        }
                        else {

                        }
                    }
                    catch(Exception e){}

                }
            }
            else {
            }

        } catch (Exception e)
        {

        }
    }

}