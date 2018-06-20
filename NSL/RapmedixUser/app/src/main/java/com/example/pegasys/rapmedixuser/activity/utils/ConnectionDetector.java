package com.example.pegasys.rapmedixuser.activity.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    
   private Context _context;
    boolean connected = false;
   public ConnectionDetector(Context context) {
       this._context = context;
   }

   public boolean isConnectingToInternet(){
      /* ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
         if (connectivity != null) 
         {
             NetworkInfo[] info = connectivity.getAllNetworkInfo();
             if (info != null) 
                 for (int i = 0; i < info.length; i++) 
                     if (info[i].getState() == NetworkInfo.State.CONNECTED)
                     {
                         return true;
                     }

         }
         return false;*/
       ConnectivityManager connectivityManager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
       if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
               connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
           //we are connected to a network
           connected = true;
       }
       else
           connected = false;
       return connected;
       // return false;
   }
}
