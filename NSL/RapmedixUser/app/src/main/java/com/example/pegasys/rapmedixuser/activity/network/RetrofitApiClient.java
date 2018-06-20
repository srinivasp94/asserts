//package com.example.pegasys.rapmedixuser.activity.network;
//
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import retrofit.RestAdapter;
//import retrofit.converter.GsonConverter;
//
///**
// * Created by pegasys on 12/5/2017.
// */
//
//public class RetrofitApiClient {
//
//    RetrofitApi retrofitApi;
//    private String BASE_URL = "https://api.github.com";
//
//    public RetrofitApiClient() {
//
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
//                .create();
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .setEndpoint(BASE_URL)
//                .setConverter(new GsonConverter(gson))
//                .build();
//
//        retrofitApi = restAdapter.create(RetrofitApi.class);
//        Log.d("TAG", "" + retrofitApi.toString());
//    }
//
//    public RetrofitApi getRestInterface() {
//        Log.d("TAG", "" + getRestInterface().toString());
//        return retrofitApi;
//    }
//
//}
