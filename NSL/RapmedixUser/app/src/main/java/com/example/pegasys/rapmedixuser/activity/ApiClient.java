package com.example.pegasys.rapmedixuser.activity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by New android on 15-07-2017.
 */

public class ApiClient {
    public static final String Base_URL =  "https://www.rapmedix.com";
    //public static final String Base_URL =  "";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit            =           new Retrofit.Builder()
                                            .baseUrl(Base_URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
        }

        return retrofit;
    }
}
