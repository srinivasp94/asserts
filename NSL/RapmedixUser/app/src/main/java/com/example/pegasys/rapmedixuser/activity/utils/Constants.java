package com.example.pegasys.rapmedixuser.activity.utils;

import android.support.annotation.NonNull;

import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.DiagnosticsSubcatList;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.Diagnosticscenter;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.TestReviewsResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by pegasys on 12/5/2017.
 */

public class Constants {

    public static final String BASE_URL = "https://www.rapmedix.com";

    public static final String JSON_URL = "https://secure.ccavenue.com/transaction/transaction.do";

    public static final String PARAMETER_SEP = "&";
    public static final String PARAMETER_EQUALS = "=";
    public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";

    public static final String responseHandler  = "https://www.rapmedix.com/index/ccavResponseHandler";
    public static final String RSAKey = "https://www.rapmedix.com/index/get_RSA";

    //    public static final String BASE_URL = "http://dev.rapmedix.com";
    public static final String INTERNET_UNABLEABLE = "Not connected to the internet. Please check your connection and try again.";
    //https://www.rapmedix.com/user/userregister_service
    //public static final String URL_NSL_MAIN = "http://nslbeejtantra.com/Api/";

    public static String imageUpload = "userprofile_update_service";
    public static final long CONNECTION_TIME_OUT = 60;
    public static final long READ_TIME_OUT = 60;
    public static final long WRITE_TIME_OUT = 60;
    public static String upload_healthrecords_service = "upload_healthrecords_service";
    public static ArrayList<DiagnosticsSubcatList> cartDiagnosticscenters = new ArrayList<>();
//    public static ArrayList<TestReviewsResponse> cartPageitems = new ArrayList<>();

    public interface RequestCodes {
        int ONCREATE_REQUEST_CODE = 5000;
    }
}
