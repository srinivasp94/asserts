package com.example.pegasys.rapmedixuser.activity.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by pegasys on 12/7/2017.
 */

public class Common {
//    public static void customToast(Context context, String message) {
//        customToast(context, message, Toast.LENGTH_LONG);
//    }
public static void dismissProgressDialog(ProgressDialog progressDialog) {
    if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.dismiss();
    }

}

    public static ProgressDialog showProgressDialog(Context context) {

        final ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Please Wait.....");

        progressDialog.setCancelable(false);

        if (!((Activity) context).isFinishing()) {

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progressDialog.show();

                }
            });
        }


        return progressDialog;
    }

    public static boolean haveInternet(Context ctx) {
        try {
            NetworkInfo info = ((ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();

            if (info == null || !info.isConnected()) {
                return false;
            }
        } catch (Exception e) {
            android.util.Log.d("err", e.toString());
        }
        return true;
    }

    public static <T> T getSpecificDataObject(Object object, Class<T> classOfT) {

        String jsonString = new Gson().toJson(object);

        return new Gson().fromJson(jsonString, classOfT);

    }

    public static void showContentView(Activity activity, boolean showStatus) {

        int visibleStatus = showStatus ? View.VISIBLE : View.GONE;

        activity.findViewById(android.R.id.content).setVisibility(visibleStatus);


    }
}
