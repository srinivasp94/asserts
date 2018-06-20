package com.example.pegasys.rapmedixuser.activity.utils;

/**
 * Created by pegasys on 12/5/2017.
 */

public class Constants {

//    public static final String BASE_URL = "https://www.rapmedix.com";
    public static final String BASE_URL = "http://dev.rapmedix.com";
    public static final String INTERNET_UNABLEABLE="Not connected to the internet. Please check your connection and try again.";
    //https://www.rapmedix.com/user/userregister_service
    //public static final String URL_NSL_MAIN = "http://nslbeejtantra.com/Api/";

    public static final long CONNECTION_TIME_OUT = 60;
    public static final long READ_TIME_OUT = 60;
    public static final long WRITE_TIME_OUT = 60;

    public interface  RequestCodes {
        int ONCREATE_REQUEST_CODE = 5000;
    }
}
