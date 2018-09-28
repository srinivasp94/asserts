package com.example.pegasys.rapmedixuser.activity.retrofitnetwork;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.activity.MyApplication;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RetrofitRequester {
    private final RetrofitResponseListener retrofitResponseListener;
    private Activity activity;
    private Context context;
    private ProgressDialog progressDialog;

    public RetrofitRequester(RetrofitResponseListener retrofitResponseListener) {

        this.retrofitResponseListener = retrofitResponseListener;

        if (retrofitResponseListener instanceof Activity) {
            this.context = (Context) retrofitResponseListener;
            this.activity = (Activity) retrofitResponseListener;
        } else if (retrofitResponseListener instanceof Fragment) {
            this.context = ((Fragment) retrofitResponseListener).getActivity();
            this.activity = ((Fragment) retrofitResponseListener).getActivity();

        } else if (retrofitResponseListener instanceof android.support.v4.app.Fragment) {
            this.context = ((android.support.v4.app.Fragment) retrofitResponseListener).getActivity();
            this.activity = ((android.support.v4.app.Fragment) retrofitResponseListener).getActivity();

        } else if (retrofitResponseListener instanceof WakefulBroadcastReceiver) {
            this.context = (Context) retrofitResponseListener;
            this.activity = (Activity) retrofitResponseListener;

        }

    }

    public RetrofitRequester(Context context, RetrofitResponseListener retrofitResponseListener1, ProgressDialog progressDialog) {

        this.retrofitResponseListener = retrofitResponseListener1;
        this.context = context;
        this.progressDialog = progressDialog;
    }


    public void callPostServices(final Object obj, final int requesterId, String url, final boolean showProgressDialog) {

        if (requesterId == Constants.RequestCodes.ONCREATE_REQUEST_CODE) {
            Common.showContentView((Activity) context, false);
        }

        if (!Common.haveInternet(context)) {

//            Common.customToast(context, Constants.INTERNET_UNABLEABLE);
            Toast.makeText(context, Constants.INTERNET_UNABLEABLE, Toast.LENGTH_SHORT).show();

            return;

        }


        if (showProgressDialog) {

            progressDialog = Common.showProgressDialog(context);
//            progressDialog.dismiss();

        }


        Call<Object> dfdf = MyApplication.getInstance().getAPIInterface().callPost(url, obj);
        dfdf.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Common.dismissProgressDialog(progressDialog);
//            progressDialog.dismiss();
                Log.d("retrofit", response.toString());
                retrofitResponseListener.onResponseSuccess(response.body(), obj, requesterId);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Common.dismissProgressDialog(progressDialog);
                Log.d("retrofiterror", t.toString());
//            progressDialog.dismiss();
            }
        });
    }

}
