package com.nsl.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.nsl.app.commonutils.Common;
import com.nsl.app.complaints.Complaints;
import com.nsl.app.dailydairy.DailyDairy;
import com.nsl.app.feedback.Feedback;
import com.nsl.app.marketintelligence.Commodity_Price;
import com.nsl.app.marketintelligence.Crop_Shifts;
import com.nsl.app.marketintelligence.Price_Survey;
import com.nsl.app.marketintelligence.Product_Survey;
import com.nsl.app.network.RetrofitRequester;
import com.nsl.app.network.RetrofitResponseListener;
import com.nsl.app.pojo.StockMovementRetailerDetails;
import com.nsl.app.pojo.StockMovementUnSynedPojo;
import com.nsl.app.pojo.StockReturnUnSynedPojo;
import com.nsl.app.pojo.VersionControlVo;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nsl.app.DatabaseHandler.KEY_COMMODITY_PRICE_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_COMMODITY_PRICE_ID;
import static com.nsl.app.DatabaseHandler.KEY_CROP_SHIFTS_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_CROP_SHIFTS_ID;
import static com.nsl.app.DatabaseHandler.KEY_DD_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_DD_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_APPROVAL_STATUS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_CHECK_IN_TIME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_COMMENTS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_CONCERN_PERSON_NAME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_CREATED_BY;
import static com.nsl.app.DatabaseHandler.KEY_EMP_CREATED_DATETIME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_END_DATE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_NAME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_PARTICIPANTS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_PURPOSE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_EVENT_VENUE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_FEILD_AREA;
import static com.nsl.app.DatabaseHandler.KEY_EMP_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_GEO_TRACKING_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_LOCATION_ADDRESS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_MOBILE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_PLAN_DATE_TIME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_PURPOSE_VISIT_IDS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_STATUS;
import static com.nsl.app.DatabaseHandler.KEY_EMP_TYPE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_UPDATED_BY;
import static com.nsl.app.DatabaseHandler.KEY_EMP_UPDATE_DATETIME;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VILLAGE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_PLAN_TYPE;
import static com.nsl.app.DatabaseHandler.KEY_EMP_VISIT_USER_ID;
import static com.nsl.app.DatabaseHandler.KEY_PAYMENT_COLLECTION_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_PAYMENT_COLLECTION_PAYMENT_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRICE_SURVEY_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_PRICE_SURVEY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_FEEDBACK_FEEDBACK_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_FEEDBACK_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SMD_DETAIL_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPLAINT;
import static com.nsl.app.DatabaseHandler.TABLE_DAILYDAIRY;
import static com.nsl.app.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.app.DatabaseHandler.TABLE_FEEDBACK;
import static com.nsl.app.DatabaseHandler.TABLE_MI_COMMODITY_PRICE;
import static com.nsl.app.DatabaseHandler.TABLE_MI_CROP_SHIFTS;
import static com.nsl.app.DatabaseHandler.TABLE_MI_PRICE_SURVEY;
import static com.nsl.app.DatabaseHandler.TABLE_MI_PRODUCT_SURVEY;
import static com.nsl.app.DatabaseHandler.TABLE_PAYMENT_COLLECTION;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDERDETAILS;
import static com.nsl.app.DatabaseHandler.TABLE_SMD;
import static com.nsl.app.DatabaseHandler.TABLE_STOCK_MOVEMENT_RETAILER_DETAILS;
import static com.nsl.app.DatabaseHandler.TABLE_STOCK_RETURNS_DETAILS;
import static com.nsl.app.advancebooking.CropsFragmentAdvancebookingActivity.toJson;

/**
 * Created by Apresh on 1/26/2017.
 */

public class BackgroundPushService extends Service implements RetrofitResponseListener {

    public static final String mypreference = "mypref";
    private static final String TAG = BackgroundPushService.class.getName();
    public static final int UPDATE_PROGRESS = 1000;
    public SharedPreferences sharedpreferences;
    DatabaseHandler db;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String userId, team;
    int role;
    private Context mContext;
    private String checkuid;
    private SQLiteDatabase sdbw;
    private ArrayList<String> tableMustInsertList;
    private JSONArray mainArray;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        db = new DatabaseHandler(BackgroundPushService.this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        this.mContext = getApplicationContext();

        userId = sharedpreferences.getString("userId", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        team = sharedpreferences.getString("team", "");

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if (userId.equals("")) {
            return;
        }

        pushAdvanceBookingThread();
        pushOrderIndentThread();
        pushFeedBackThread();
        pushComplaintprodThread();
        pushComplaintThread();
        pushPlanerScheduleThread();
        getNewOrUpdatedServiceOrder();
        pushDailyDairyThread();
        pushMICommodityPriceThread();
        pushMICropShiftsThread();
        pushMIPriceSurveyThread();
        pushMIProductSurveyThread();
        pushGeoThread();
        pushPaymentThread();
        pushProfileImageThread();
        prepareStockMovementDataAndPush();
        prepareStockMovementRetailerDataAndPush();
        prepareStockReturnsDataAndPush(intent);

        if (Common.haveInternet(this)) {
            if (sharedpreferences.contains(Constants.SharedPrefrancesKey.CURRENT_DATE) && sharedpreferences.getString(Constants.SharedPrefrancesKey.CURRENT_DATE, "").equals(Common.getCurrentDate())) {


            } else {
                if (Common.isTime3AM()) {
                    sharedpreferences.edit().putString(Constants.SharedPrefrancesKey.CURRENT_DATE, Common.getCurrentDate()).commit();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", "update_table_names");

                    new RetrofitRequester(this).callGetApi(map);
                }

            }


        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable


    //Pushes the CheckIn,Check out,Path Data
    public void pushGeoThread() {
        Log.v(TAG, "pushPlannerThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareGEOOfflineDataAndPush();
            }
        }).start();
    }

    public void getNewOrUpdatedServiceOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Async_getNewOrUpdateserviceorders().execute();
            }
        }).start();
    }

    public void pushPaymentThread() {
        Log.v(TAG, "pushPaymentThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                preparePaymentOfflineDataAndPush();
            }
        }).start();
    }

    public void pushProfileImageThread() {
        Log.v(TAG, "pushPaymentThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareProfileImageOfflineDataAndPush();
            }
        }).start();
    }

    //Pushes the Complaints regulatory saved in offline
    public void pushComplaintThread() {
        Log.v(TAG, "pushComplaintregThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareComplainregOfflineDataAndPush();

            }
        }).start();
    }

    public void pushComplaintprodThread() {
        Log.v(TAG, "pushComplaintprodThread");
        new Thread(new Runnable() {
            @Override
            public void run() {

                prepareComplainprodOfflineDataAndPush();
            }
        }).start();
    }

    //Pushes the Feedback saved in offline
    public void pushFeedBackThread() {
        Log.v(TAG, "pushFeedBackThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareFeedBackOfflineDataAndPush();
            }
        }).start();
    }

    public void pushDailyDairyThread() {
        Log.v(TAG, "pushDailyDairyThread");
        new Thread(new Runnable() {
            @Override
            public void run() {

                prepareDailyDairyOfflineDataAndPush();
            }
        }).start();
    }

    //Pushes the Check out,Path Data where checkin not null
    public void pushPlannerCheckinThread() {
        Log.v(TAG, "pushPlannerCheckinThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareGEOOfflinecheckinDataAndPush();
            }
        }).start();
    }

    //Pushes the Advance booking data
    public void pushPlanerEventThread() {
        Log.v(TAG, "pushPlanerEventThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                preparePlanerEventAndPush();
            }
        }).start();
    }


    public void pushPlanerScheduleThread() {
        Log.v(TAG, "pushPlanerScheduleThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                preparePlanerScheduleAndPush();
            }
        }).start();
    }

    //Pushes the Service order data
    public void pushOrderIndentThread() {
        Log.v(TAG, "pushOrderIndentThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareOfflineOrderIndentDataAndPush();
            }
        }).start();
    }

    //Pushes the Advance booking data
    public void pushAdvanceBookingThread() {
        Log.v(TAG, "pushAdvancebookingThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareBookingOfflinecheckinDataAndPush();
            }
        }).start();
    }


    //Pushes the Approve/Reject data
    public void pushApproveRejectThread() {
        Log.v(TAG, "pushApproveRejectThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareApproveRejectOfflineDataAndPush();
            }
        }).start();
    }

    public void pushMICommodityPriceThread() {
        Log.v(TAG, "pushFeedBackThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareMICommodityPriceOfflineDataAndPush();
            }
        }).start();
    }

    public void pushMICropShiftsThread() {
        Log.v(TAG, "pushFeedBackThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareMICropShiftsOfflineDataAndPush();
            }
        }).start();
    }

    public void pushMIPriceSurveyThread() {
        Log.v(TAG, "pushFeedBackThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareMIPriceSurveyOfflineDataAndPush();
            }
        }).start();
    }

    public void pushMIProductSurveyThread() {
        Log.v(TAG, "pushFeedBackThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareMIProductSurveyOfflineDataAndPush();
            }
        }).start();
    }

    private void prepareOfflineOrderIndentDataAndPush() {


        try {
            List<ServiceOrderMaster> serviceOrderMasterList = db.getOfflineServiceorders();
            Log.i("  -json array -", "returned Anil" + serviceOrderMasterList.size());
            JSONArray mainArray = new JSONArray();
            JSONArray adbBookArray = new JSONArray();
            JSONObject naimObject = new JSONObject();
            if (serviceOrderMasterList == null || serviceOrderMasterList.size() == 0) {
                Log.i("  -json array -", "returned.." + mainArray.toString());
                return;

            }
            for (int i = 0; i < serviceOrderMasterList.size(); i++) {
                ServiceOrderMaster serviceOrderMaster = serviceOrderMasterList.get(i);

                JSONObject advBookObj = new JSONObject();

                advBookObj.put("CompanyID", serviceOrderMaster._serviceorder_company_id);
                advBookObj.put("customer_id", serviceOrderMaster._serviceorder_customer_id);
                advBookObj.put("DivisionID", serviceOrderMaster._serviceorder_division_id);

                advBookObj.put("id", serviceOrderMaster._serviceorder_masterid);
                advBookObj.put("user_id", sharedpreferences.getString("userId", null));

                advBookObj.put("OrderDate", serviceOrderMaster._serviceorder_date);
                advBookObj.put("status", serviceOrderMaster._serviceorder_status);


                //JSONArray productArray = new JSONArray();
                // for (int k = 0; k < globalGroup.size(); k++) {


                String sel_crop_id = "";
                JSONArray productArray = new JSONArray();

                for (int l = 0; l < serviceOrderMaster.getServiceOrderDetailMasterList().size(); l++) {
                    ServiceOrderDetailMaster serviceOrderDetailMaster = serviceOrderMaster.getServiceOrderDetailMasterList().get(l);
                    JSONObject object_one = new JSONObject();
                    object_one.put("OrderPrice", serviceOrderDetailMaster._serviceorderdetail_order_price);
                    object_one.put("ProductID", serviceOrderDetailMaster._serviceorderdetail_product_id);
                    object_one.put("Qunatity", serviceOrderDetailMaster._serviceorderdetail_quantity);
                    object_one.put("mobile_serivce_details_id", serviceOrderDetailMaster._serviceorderdetail_masterid);

                    productArray.put(object_one);

                }

                JSONArray cropArray = new JSONArray();
                // for (int j = 0; j < globalGroup.size(); j++) {
                JSONObject cropObj = new JSONObject();
                sel_crop_id = serviceOrderMaster.getServiceOrderDetailMasterList().get(0).get_serviceorderdetail_crop_id();
                cropObj.put("CropId", sel_crop_id);

                cropArray.put(cropObj);
                advBookObj.put("Crops", cropArray);

                cropObj.put("Products", productArray);
                //}

                adbBookArray.put(advBookObj);
                naimObject.put("salesorder", adbBookArray);

            }
            mainArray.put(naimObject);
            Log.i("  -json array -", "" + mainArray.toString());

            if (serviceOrderMasterList.size() > 0) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, mainArray.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_ORDERINDENT)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                System.out.println("!!!!!!!1Oreder Indent" + jsonData);


                if (jsonData != null) {
                    try {
                        System.out.println("!!!!!!!1 Service order post execute" + jsonData + "\n");
                        // JSONArray jsonArray=new JSONArray(jsonData);
                        //  String sdfds = new Gson().toJson(jsonData);
                        //  System.out.println("!!!!!!!1 Advance Booking" + sdfds+"\n"+s);
                        JSONArray jsonArray = toJson(jsonData);

                        for (int i = 0; i < jsonArray.length(); i++)

                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                                String service_id = jsonObject.getString("service_id");
                                String ffmid = jsonObject.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", service_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + service_id;
                                sdbw.execSQL(updatequery);

                                JSONArray detailsArray = jsonObject.getJSONArray("details");
                                for (int k = 0; k < detailsArray.length(); k++) {
                                    String ffm_id = detailsArray.getJSONObject(k).getString("ffm_id");
                                    String order_detail_id = detailsArray.getJSONObject(k).getString("order_detail_id");

                                    String updatequery1 = "UPDATE " + TABLE_SERVICEORDERDETAILS + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffm_id + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " = " + order_detail_id;
                                    sdbw.execSQL(updatequery1);
                                }


                            }


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // pushAdvanceBookingThread();
    }


    private void preparePlannerOfflineDataAndPush() {


        Log.d("Reading: ", "Reading all payment collection..");

        String selectQuery = "SELECT DISTINCT "
                + KEY_TABLE_GEO_TRACKING_ID + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_DISTANCE + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_VISIT_DATE + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME
                + " FROM " + DatabaseHandler.TABLE_GEO_TRACKING + " where " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID + " = " + sharedpreferences.getString("userId", null);
        //+ " and " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID + " IS NULL" ;


        Log.e("selectQuery", selectQuery);
        sdbw = db.getWritableDatabase();
        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                String jsonData;
                OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("user_id", cursor.getString(1))
                        .add("table", "geo_tracking")
                        .add("mobile_id", cursor.getString(0))
                        .add("check_in_lat_lon", cursor.getString(2))
                        .add("check_out_lat_lon", cursor.getString(3))
                        .add("route_path_lat_lon", cursor.getString(4))
                        .add("visit_date", cursor.getString(6))
                        .add("check_in_time", cursor.getString(7))
                        .add("check_out_time", cursor.getString(8))
                        .build();

                Response responses = null;

                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_PUSHTABLE)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 GEO inserting" + jsonData);

                    if (jsonData != null) {
                        JSONArray jsonarray;
                        try {
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {


                                String sqliteid = jsonobject.getString("sqlite");
                                String ffmid = jsonobject.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                SQLiteDatabase sql = db.getWritableDatabase();
                                // updatecomplaints
                                String updatequery = "UPDATE " + DatabaseHandler.TABLE_GEO_TRACKING + " SET " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID + " = " + ffmid + " WHERE " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID + " = " + sqliteid;
                                sql.execSQL(updatequery);
                                Log.v("UPDATE", "UPDATE Success Geo tracking");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        } else Log.d("LOG", "returned null!");


    }

    private void prepareGEOOfflineDataAndPush() {


        Log.d("Reading: ", "Reading all geo tracking..");

        List<Geo_Tracking_POJO> geo_tracking_pojo = db.getAllGeotracking();

        Log.e("list size", String.valueOf(geo_tracking_pojo.size()));

        if (geo_tracking_pojo.size() > 0) {


            for (Geo_Tracking_POJO geo : geo_tracking_pojo) {
                String checkOutTime = geo.getGeo_check_out_time();
                if (checkOutTime == null) {
                    checkOutTime = " ";
                }

                if (geo.getGeoffmid() == null || geo.getGeoffmid() == "" || geo.getGeoffmid() == "0") {

                    String jsonData;
                    OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("user_id", geo.get_Geo_user_id())
                            .add("table", "geo_tracking")
                            .add("mobile_id", String.valueOf(geo.getID()))
                            .add("check_in_lat_lon", geo.get_Geo_check_in_lat_lon())
                            .add("check_out_lat_lon", geo.getGeo_check_out_lat_lon())
                            .add("route_path_lat_lon", geo.getGeo_route_path_lat_lon())
                            .add("visit_date", geo.getGeo_visit_date())
                            .add("check_in_time", geo.getGeo_check_in_time())
                            .add("check_out_time", checkOutTime)
                            .build();

                    Response responses = null;

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PUSHTABLE)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 GEO inserting" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {


                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("tracking_id", ffmid);
                                    editor.commit();

                                    //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                    SQLiteDatabase sql = db.getWritableDatabase();
                                    // updatecomplaints
                                    String updatequery = "UPDATE " + DatabaseHandler.TABLE_GEO_TRACKING + " SET " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID + " = " + ffmid + " WHERE " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID + " = " + sqliteid;
                                    sql.execSQL(updatequery);
                                    Log.v("UPDATE", "UPDATE Success Geo tracking");

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                            // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    new Async_Routepath().execute(geo.getGeo_route_path_lat_lon(), geo.getGeoffmid());
                }


            }


        }
    }

    private void prepareGEOOfflinecheckinDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");

        Log.d("Reading: ", "Reading all geo tracking checkin..");

        List<Geo_Tracking_POJO> geo_tracking_pojo = db.getAllGeotrackingwithcheckin(checkuid);

        Log.e("list size", String.valueOf(geo_tracking_pojo.size()));

        if (geo_tracking_pojo.size() > 0) {


            for (Geo_Tracking_POJO geo : geo_tracking_pojo) {


                String jsonData;
                OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("tracking_id", geo.getGeoffmid())
                        .add("check_out_lat_lon", geo.getGeo_check_out_lat_lon())
                        .add("route_path_lat_lon", geo.getGeo_route_path_lat_lon())
                        .add("check_out_time", geo.getGeo_check_out_time())
                        .build();

                Response responses = null;

                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_GEO_PUSH_FULL_PATH)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    // System.out.println("!!!!!!!1 GEO inserting" + jsonData);

                    /*if (jsonData != null) {
                        JSONArray jsonarray;
                        try {
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {


                                String sqliteid = jsonobject.getString("sqlite");
                                String ffmid = jsonobject.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                SQLiteDatabase sql = db.getWritableDatabase();
                                // updatecomplaints
                                String updatequery = "UPDATE " + DatabaseHandler.TABLE_GEO_TRACKING + " SET " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID + " = " + ffmid + " WHERE " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID + " = " + sqliteid;
                                sql.execSQL(updatequery);
                                Log.v("UPDATE", "UPDATE Success Geo tracking");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }*/


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }

        pushPlanerEventThread();
        Log.e(" ##### ", " Push planer event");
    }

    private void preparePaymentOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");

        Log.d("Reading: ", "Reading all Payment collection..");

        List<Payment_collection> paymentCollections = null;
        try {
            paymentCollections = db.getAllPaymentCollection(checkuid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        Log.e("list size", String.valueOf(paymentCollections.size()));

        if (paymentCollections.size() > 0) {


            for (Payment_collection pc : paymentCollections) {
                String log = "Id: " + pc.getID() + " ,pay_type: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() +
                        " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() +
                        " ,amt : " + pc.get_total_amount() + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() +
                        "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() +
                        ",cheque number:" + pc.get_cheque_no_dd_no() + ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() +
                        ",updated datetime:" + pc.get_updated_datetime() + ",ffmid:" + pc.get_ffmid();

                Log.e("payment collection: ", log);
                String payment_type = pc.get_payment_type();

                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                     /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("id", String.valueOf(pc.getID()))
                            .add("sqlite_id", String.valueOf(pc.getID()))
                            .add("payment_type", String.valueOf(payment_type))
                            .add("user_id", String.valueOf(pc.get_user_id()))
                            .add("bank_name", pc.get_bank_name())
                            .add("company_id", String.valueOf(pc.get_company_id()))
                            .add("division_id", String.valueOf(pc.get_division_id()))
                            .add("payment_datetime", pc.get_payment_datetime())
                            .add("customer_id", String.valueOf(pc.get_customer_id()))
                            .add("total_amount", String.valueOf(pc.get_total_amount()))
                            .add("payment_mode", String.valueOf(pc.get_payment_mode()))
                            .add("rtgs_or_neft_no", String.valueOf(pc.get_rtgs_or_neft_no()))
                            .add("created_by", String.valueOf(pc.get_user_id()))
                            .add("cheque_no_dd_no", String.valueOf(pc.get_cheque_no_dd_no()))
                            .add("date_on_cheque_no", String.valueOf(pc.get_date_on_cheque_no()))
                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PAYMENT_COLLECTION)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertPayment" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");

                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_PAYMENT_COLLECTION + " SET " + KEY_PAYMENT_COLLECTION_FFMID + " = " + ffmid + " WHERE " + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);

                                    List<Payment_collection> paymentListData = db.getAllPaymentCollection(checkuid);

                                    for (Payment_collection p : paymentListData) {
                                        log = "Id: " + pc.getID() + " ,pay_type: " + p.get_payment_type() + " ,userId: " + pc.get_user_id() + " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() + " ,amt : " + pc.get_total_amount()
                                                + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() + "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() + ",cheque number:" + pc.get_cheque_no_dd_no() +
                                                ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() + ",updated datetime:" + pc.get_updated_datetime() + ",ffmid:" + pc.get_ffmid();

                                        Log.e("paymentcollectionaftr: ", log);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }


    }


    private void prepareComplainregOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all complaintsreg..");
        List<Complaints> complaints = db.getAllComplaintsregulatory(checkuid);

        Log.e("list size", String.valueOf(complaints.size()));

        if (complaints.size() > 0) {


            for (Complaints cm : complaints) {
                String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                        cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                        + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                        + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                        + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                        ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                Log.e("complaintsbefore: ", log);

                try {


                    String imageStr1 = "";
                    String imageStr2 = "";
                    String imageStr3 = "";
                    String imageStr4 = "";
                    String imageStr5 = "";
                    String imageStr6 = "";

                    try {
                        JSONArray jsonArray = new JSONArray(cm.get_image());

                        //  String[] imagesDb = fb.getImage().split(",nsl,");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String image = jsonObject.getString("image_64");

                            if (i == 0) {
                                imageStr1 = image;
                            }
                            if (i == 1) {
                                imageStr2 = image;
                            }
                            if (i == 2) {
                                imageStr3 = image;
                            }
                            if (i == 3) {
                                imageStr4 = image;
                            }
                            if (i == 4) {
                                imageStr5 = image;
                            }
                            if (i == 5) {
                                imageStr6 = image;
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                     /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("id", String.valueOf(cm.getID()))
                            .add("user_id", String.valueOf(cm.get_user_id()))
                            .add("company_id", String.valueOf(cm.getCompanyId()))
                            .add("division_id", String.valueOf(cm.get_division_id()))
                            .add("complaint_type", "regulatory")
                            .add("crop_id", String.valueOf(cm.getCropid()))
                            .add("product_id", String.valueOf(cm.getProductid()))
                            .add("marketing_lot_number", cm.get_marketing_lot_number())
                            .add("regulatory_type", cm.get_regulatory_type())
                            .add("sampling_date", cm.get_sampling_date())
                            .add("place_sampling", cm.get_place_sampling())
                            .add("retailer_name", cm.get_retailer_name())
                            .add("distributor", String.valueOf(cm.get_distributor()))
                            .add("sampling_officer_name", cm.get_sampling_officer_name())
                            .add("sampling_officer_contact", cm.get_sampling_officer_contact())
                            .add("comments", cm.get_comments())
                            .add("image_1", imageStr1)
                            .add("image_2", imageStr2)
                            .add("image_3", imageStr3)
                            .add("image_4", imageStr4)
                            .add("image_5", imageStr5)
                            .add("image_6", imageStr6)

                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_COMPLAINTS)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertComplaintsregulatory" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Complaints> complaintsdata = db.getAllComplaintsregulatory(checkuid);

                                    for (Complaints cmp : complaintsdata) {
                                        log = "Id: " + cm.getID() + " ,company: " + cmp.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                                                cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                                                + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                                                + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                                                + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                                                ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                                        Log.e("complaintsafterupdate: ", log);


                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }

        //pushComplaintprodThread();
    }


    private void prepareComplainprodOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all complaintsprod..");
        List<Complaints> complaints = db.getAllComplaintsproducts(checkuid);

        Log.e("list size", String.valueOf(complaints.size()));

        if (complaints.size() > 0) {


            for (Complaints cm : complaints) {
                String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                        cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                        + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                        + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                        + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                        ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                Log.e("complaintsbefore: ", log);

                try {


                    String imageStr1 = "";
                    String imageStr2 = "";
                    String imageStr3 = "";
                    String imageStr4 = "";
                    String imageStr5 = "";
                    String imageStr6 = "";

                    try {
                        JSONArray jsonArray = new JSONArray(cm.get_image());

                        //  String[] imagesDb = fb.getImage().split(",nsl,");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String image = jsonObject.getString("image_64");

                            if (i == 0) {
                                imageStr1 = image;
                            }
                            if (i == 1) {
                                imageStr2 = image;
                            }
                            if (i == 2) {
                                imageStr3 = image;
                            }
                            if (i == 3) {
                                imageStr4 = image;
                            }
                            if (i == 4) {
                                imageStr5 = image;
                            }
                            if (i == 5) {
                                imageStr6 = image;
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                     /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("id", String.valueOf(cm.getID()))
                            .add("user_id", String.valueOf(cm.get_user_id()))
                            .add("company_id", String.valueOf(cm.getCompanyId()))
                            .add("division_id", String.valueOf(cm.get_division_id()))
                            .add("complaint_type", "product")
                            .add("crop_id", String.valueOf(cm.getCropid()))
                            .add("product_id", String.valueOf(cm.getProductid()))
                            .add("marketing_lot_number", cm.get_marketing_lot_number())
                            .add("others", cm.get_others())
                            .add("farmer_name", cm.get_farmer_name())
                            .add("contact_no", cm.get_contact_no())
                            .add("complaint_area_acres", cm.get_complaint_area_acres())
                            .add("soil_type", String.valueOf(cm.get_soil_type()))
                            .add("purchased_quantity", cm.get_purchased_quantity())
                            .add("complaint_quantity", cm.get_complaint_quantity())
                            .add("purchase_date", cm.get_purchase_date())
                            .add("bill_number", cm.get_bill_number())
                            .add("retailer_name", cm.get_retailer_name())
                            .add("distributor", String.valueOf(cm.get_distributor()))
                            .add("mandal", cm.get_mandal())
                            .add("village", cm.get_village())
                            .add("comments", cm.get_comments())
                            .add("image_1", imageStr1)
                            .add("image_2", imageStr2)
                            .add("image_3", imageStr3)
                            .add("image_4", imageStr4)
                            .add("image_5", imageStr5)
                            .add("image_6", imageStr6)

                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_COMPLAINTS)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertComplaintsproduct" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Complaints> complaintsdata = db.getAllComplaintsregulatory(checkuid);

                                    for (Complaints cmp : complaintsdata) {
                                        log = "Id: " + cm.getID() + " ,company: " + cmp.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                                                cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                                                + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                                                + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                                                + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                                                ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                                        Log.e("complaintsafterupdate: ", log);


                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
        //pushFeedBackThread();

    }


    private void prepareFeedBackOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all Feedback..");
        List<Feedback> feedback = db.getAllFeedback(checkuid);
        Log.e("list size", String.valueOf(feedback.size()));

        if (feedback.size() > 0) {


            for (Feedback fb : feedback) {
                String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " ,Name: " + fb.getFarmerName() + " ,place: " + fb.getplace() + " ,mobile: " + fb.getContactNo() + " ,crop: " + fb.getCrop() + " ,hybrid: " + fb.getHybrid() + " ,sowing date : " + fb.getSowingDate() + " ,feedback message:" + fb.getfeedbackmessage() + ",image:" + fb.getImage() + ",ffmid:" + fb.get_ffmid();

                Log.e("feedback before : ", log);
                try {


                    String imageStr1 = "";
                    String imageStr2 = "";
                    String imageStr3 = "";
                    String imageStr4 = "";
                    String imageStr5 = "";
                    String imageStr6 = "";

                    try {
                        JSONArray jsonArray = new JSONArray(fb.getImage());

                        //  String[] imagesDb = fb.getImage().split(",nsl,");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String image = jsonObject.getString("image_64");

                            if (i == 0) {
                                imageStr1 = image;
                            }
                            if (i == 1) {
                                imageStr2 = image;
                            }
                            if (i == 2) {
                                imageStr3 = image;
                            }
                            if (i == 3) {
                                imageStr4 = image;
                            }
                            if (i == 4) {
                                imageStr5 = image;
                            }
                            if (i == 5) {
                                imageStr6 = image;
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                     /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("id", String.valueOf(fb.getID()))
                            .add("user_id", checkuid)
                            .add("farmer_name", fb.getFarmerName())
                            .add("place", fb.getplace())
                            .add("contact_no", fb.getContactNo())
                            .add("crop_id", fb.getCrop())
                            .add("hybrid", fb.getHybrid())
                            .add("sowing_date", fb.getSowingDate())
                            .add("feedback_message", fb.getfeedbackmessage())
                            .add("image_1", imageStr1)
                            .add("image_2", imageStr2)
                            .add("image_3", imageStr3)
                            .add("image_4", imageStr4)
                            .add("image_5", imageStr5)
                            .add("image_6", imageStr6)
                            .build();


                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_FEEDBACK)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertFeedback" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_FEEDBACK + " SET " + KEY_TABLE_FEEDBACK_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_FEEDBACK_FEEDBACK_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Feedback> feedbacksListData = db.getAllFeedback(checkuid);

                                    for (Feedback fbdata : feedbacksListData) {
                                        String logtext = "Id: " + fbdata.getID() + ",Userid: " + fbdata.get_user_id() + " ,Name: " + fbdata.getFarmerName() + " ,place: " + fbdata.getplace() + " ,mobile: " + fbdata.getContactNo() + " ,crop: " + fbdata.getCrop() + " ,hybrid: " + fbdata.getHybrid() + " ,sowing date : " + fbdata.getSowingDate() + " ,feedback message:" + fbdata.getfeedbackmessage() + ",image:" + fbdata.getImage() + ",ffmidafter:" + fbdata.get_ffmid();

                                        Log.e("feedback: ", logtext);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

        pushDailyDairyThread();
    }


    private void preparePlanerScheduleAndPush() {
        String jsonData = null;
        String selectQuery = "SELECT DISTINCT "
                + KEY_EMP_CONCERN_PERSON_NAME + ","
                + KEY_EMP_VISIT_PLAN_TYPE + ","
                + KEY_EMP_STATUS + ","
                + KEY_EMP_PLAN_DATE_TIME + ","
                + KEY_EMP_PURPOSE_VISIT_IDS + ","
                + KEY_EMP_TYPE + ","
                + KEY_EMP_GEO_TRACKING_ID + ","
                + KEY_EMP_VISIT_USER_ID + ","
                + KEY_EMP_VISIT_CUSTOMER_ID + ","
                + KEY_EMP_MOBILE + ","
                + KEY_EMP_VILLAGE + ","
                + KEY_EMP_LOCATION_ADDRESS + ","
                + KEY_EMP_FEILD_AREA + ","
                + KEY_EMP_CHECK_IN_TIME + ","
                + KEY_EMP_COMMENTS + ","
                + KEY_EMP_APPROVAL_STATUS + ","
                + KEY_EMP_EVENT_END_DATE + ","
                + KEY_EMP_EVENT_PURPOSE + ","
                + KEY_EMP_VISIT_MASTER_ID + ","
                + KEY_EMP_EVENT_VENUE + ","
                + KEY_EMP_EVENT_PARTICIPANTS + ","
                + KEY_EMP_FFM_ID + ","
                + KEY_EMP_CREATED_BY + ","
                + KEY_EMP_UPDATED_BY + ","
                + KEY_EMP_CREATED_DATETIME + ","
                + KEY_EMP_UPDATE_DATETIME + ","
                + KEY_EMP_EVENT_NAME + ","
                + KEY_EMP_VISIT_ID
                + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " in (" + team + ") and " + KEY_EMP_FFM_ID + " = 0" + " and " + KEY_EMP_TYPE + "= 1";


        Log.e("selectQueryPlaner", selectQuery);
        sdbw = db.getWritableDatabase();


        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                Log.e(" +++ Values +++ ", "type :" + cursor.getString(5) + " : " + cursor.getString(6) + " : " + cursor.getString(21) + ": checkintime" + cursor.getString(13) + ": sqlite id" + cursor.getString(27) + "approvalstatus" + cursor.getString(16));

                try {

                    OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("user_id", cursor.getString(7))
                            .add("type", cursor.getString(5))
                            .add("geo_tracking_id", cursor.getString(6))
                            .add("customer_id", cursor.getString(8))
                            .add("visit_plan_type", cursor.getString(1))
                            .add("purpose_visit", cursor.getString(4))
                            .add("plan_date_time", cursor.getString(3))
                            .add("concern_person_name", cursor.getString(0))
                            .add("mobile", cursor.getString(9))
                            .add("village", cursor.getString(10))
                            .add("location_address", cursor.getString(11))
                            .add("field_area", cursor.getString(12))
                            .add("id", cursor.getString(27))
                            .add("comments", cursor.getString(14))
                            .add("status", cursor.getString(15))
                            .add("event_name", cursor.getString(26))
                            .add("event_end_date", cursor.getString(16))
                            .add("event_purpose", cursor.getString(17))
                            .add("event_venue", cursor.getString(19))
                            .add("event_participants", cursor.getString(20))
                            .add("created_by", cursor.getString(22))
                            .add("updated_by", cursor.getString(23))
                            .build();

                    Response responses = null;


                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_EMP_VISIT_MANAGEMENT)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 Planner inserting" + jsonData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (jsonData != null) {
                    JSONArray jsonarray;
                    try {
                        JSONObject jsonobject = new JSONObject(jsonData);
                        String status = jsonobject.getString("status");
                        if (status.equalsIgnoreCase("success")) {


                            String sqliteid = jsonobject.getString("sqlite");
                            String ffmid = jsonobject.getString("ffm_id");

                            //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                            SQLiteDatabase sql = db.getWritableDatabase();
                            // updatecomplaints
                            String updatequery = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_FFM_ID + " = " + ffmid + " WHERE " + KEY_EMP_VISIT_ID + " = " + sqliteid;
                            sql.execSQL(updatequery);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                }


            } while (cursor.moveToNext());
        } else Log.d("LOG", "returned null!");

        //   pushAdvanceBookingThread();
        //pushOrderIndentThread();
    }

    private void preparePlanerEventAndPush() {
        String jsonData = null;
        String selectQuery = "SELECT DISTINCT "
                + KEY_EMP_CONCERN_PERSON_NAME + ","
                + KEY_EMP_VISIT_PLAN_TYPE + ","
                + KEY_EMP_STATUS + ","
                + KEY_EMP_PLAN_DATE_TIME + ","
                + KEY_EMP_PURPOSE_VISIT_IDS + ","
                + KEY_EMP_TYPE + ","
                + KEY_EMP_GEO_TRACKING_ID + ","
                + KEY_EMP_VISIT_USER_ID + ","
                + KEY_EMP_VISIT_CUSTOMER_ID + ","
                + KEY_EMP_MOBILE + ","
                + KEY_EMP_VILLAGE + ","
                + KEY_EMP_LOCATION_ADDRESS + ","
                + KEY_EMP_FEILD_AREA + ","
                + KEY_EMP_CHECK_IN_TIME + ","
                + KEY_EMP_COMMENTS + ","
                + KEY_EMP_APPROVAL_STATUS + ","
                + KEY_EMP_EVENT_END_DATE + ","
                + KEY_EMP_EVENT_PURPOSE + ","
                + KEY_EMP_VISIT_MASTER_ID + ","
                + KEY_EMP_EVENT_VENUE + ","
                + KEY_EMP_EVENT_PARTICIPANTS + ","
                + KEY_EMP_FFM_ID + ","
                + KEY_EMP_CREATED_BY + ","
                + KEY_EMP_UPDATED_BY + ","
                + KEY_EMP_CREATED_DATETIME + ","
                + KEY_EMP_UPDATE_DATETIME + ","
                + KEY_EMP_EVENT_NAME + ","
                + KEY_EMP_VISIT_ID
                + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + "=" + sharedpreferences.getString("userId", null) + " and " + KEY_EMP_FFM_ID + "=0" + " and " + KEY_EMP_TYPE + "=2";


        Log.e("selectQuery", selectQuery);
        sdbw = db.getWritableDatabase();


        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //Log.e(" +++ Values +++ ", "type :" + cursor.getString(5)+"concern person name :" + cursor.getString(0) + " : " + cursor.getString(6) + " : " + cursor.getString(21) + ": checkintime" + cursor.getString(13) + ": sqlite id" + cursor.getString(27));
                Log.e(" +++ Values +++ ", "concern person name :" + cursor.getString(0) + "type :" + cursor.getString(5));

                try {


                    OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("user_id", cursor.getString(7))
                            .add("type", cursor.getString(5))
                            .add("geo_tracking_id", cursor.getString(6))
                            .add("customer_id", cursor.getString(8))
                            .add("visit_plan_type", cursor.getString(1))
                            .add("purpose_visit", cursor.getString(4))
                            .add("plan_date_time", cursor.getString(3))
                            .add("concern_person_name", "" + cursor.getString(0))
                            .add("mobile", cursor.getString(9))
                            .add("village", cursor.getString(10))
                            .add("location_address", cursor.getString(11))
                            .add("field_area", cursor.getString(12))
                            .add("id", cursor.getString(27))
                            .add("comments", cursor.getString(14))
                            .add("status", cursor.getString(2))
                            .add("approval_status", cursor.getString(15))
                            .add("event_name", cursor.getString(26))
                            .add("event_end_date", cursor.getString(16))
                            .add("event_purpose", cursor.getString(17))
                            .add("event_venue", cursor.getString(19))
                            .add("event_participants", cursor.getString(20))
                            .add("created_by", cursor.getString(21))
                            .add("updated_by", cursor.getString(22))
                            .build();

                    Response responses = null;


                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_EMP_VISIT_MANAGEMENT)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 Planner inserting" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {


                                    int sqliteid = jsonobject.getInt("sqlite");
                                    int ffmid = jsonobject.getInt("ffm_id");

                                    //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                    SQLiteDatabase sql = db.getWritableDatabase();
                                    // updatecomplaints
                                    String updatequery = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_FFM_ID + " = " + ffmid + " WHERE " + KEY_EMP_VISIT_ID + " = " + sqliteid;
                                    sql.execSQL(updatequery);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                            // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        } else Log.d("LOG", "returned null!");

        //  pushPlanerScheduleThread();
    }

    private void prepareDailyDairyOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all dailydairy..");

        List<DailyDairy> dailydairy = db.getAllnullDailyDairy(checkuid);

        Log.e("list size", String.valueOf(dailydairy.size()));


        if (dailydairy.size() > 0) {


            for (DailyDairy fb : dailydairy) {
                String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_createddate() + " : " + fb.get_date() + " : " + fb.get_time();

                Log.e("Dairy before : ", log);

                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();

                    RequestBody formBody = new FormEncodingBuilder()
                            .add("id", String.valueOf(fb.getID()))
                            .add("title", fb.get_title())
                            .add("user_id", String.valueOf(fb.get_userid()))
                            .add("note", fb.get_comments())
                            .add("time_slot", fb.get_time())
                            .add("dairy_date", fb.get_date())
                            .add("tentative_time", fb.get_tentative_time())
                            .add("type", String.valueOf(fb.get_type()))
                            .add("status", String.valueOf(fb.get_status()))

                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_DAILYDAIRY)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 Insertdairy" + jsonData);
                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_DAILYDAIRY + " SET " + KEY_DD_FFMID + " = " + ffmid + " WHERE " + KEY_DD_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);

                                    List<DailyDairy> dailydairy1 = db.getAllnullDailyDairy(checkuid);

                                    for (DailyDairy fb1 : dailydairy1) {
                                        String log1 = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_createddate() + " : " + fb.get_date() + " : " + fb.get_time();

                                        Log.e("Dairy before : ", log1);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            pushMICommodityPriceThread();
        }
        pushPlannerCheckinThread();
    }

    private void prepareProfileImageOfflineDataAndPush() {

        String img_status = sharedpreferences.getString("img", "");
        String updated_img_string = "";
        Log.d("Reading: ", "Reading all dailydairy..");

        String selectQuery = "SELECT  " + db.KEY_TABLE_USERS_IMAGE + " FROM " + db.TABLE_USERS + " WHERE " + db.KEY_TABLE_USERS_MASTER_ID + " = " + userId;                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;

        System.out.println(selectQuery);
        sdbw = db.getWritableDatabase();
        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Users users = new Users();

                Log.e("image ", cursor.getString(0));

                updated_img_string = cursor.getString(0);

            } while (cursor.moveToNext());
        }
        if (updated_img_string != null && img_status.equalsIgnoreCase("0")) {

            try {
                String jsonData = null;
                Response responses = null;

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormEncodingBuilder()
                        .add("profile_pic", updated_img_string)
                        .add("user_id", userId)
                        .build();

                Request request = new Request.Builder()
                        .url(Constants.URL_UPDATING_PROFILEPIC)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 updateedprofilepic" + jsonData);
                    if (jsonData != null) {
                        try {
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("img", "1");
                                editor.commit();
                                Log.e("updated on server", sharedpreferences.getString("img", ""));

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private void prepareBookingOfflinecheckinDataAndPush() {

        try {
            List<ServiceOrderMaster> serviceOrderMasterList = db.getOfflineAdvanceBookingData();
            JSONArray mainArray = new JSONArray();
            JSONArray adbBookArray = new JSONArray();
            JSONObject naimObject = null;
            for (int i = 0; i < serviceOrderMasterList.size(); i++) {
                ServiceOrderMaster serviceOrderMaster = serviceOrderMasterList.get(i);
                JSONObject advBookObj = new JSONObject();

                advBookObj.put("CompanyID", serviceOrderMasterList.get(i)._serviceorder_company_id);
                advBookObj.put("customer_id", serviceOrderMasterList.get(i)._serviceorder_customer_id);
                advBookObj.put("Tokenamount", serviceOrderMasterList.get(i)._token_amount);

                advBookObj.put("DivisionID", serviceOrderMasterList.get(i)._serviceorder_division_id);


                advBookObj.put("id", serviceOrderMasterList.get(i)._serviceorder_masterid);
                advBookObj.put("user_id", sharedpreferences.getString("userId", null));

                JSONArray cropArray = new JSONArray();

                JSONArray productArray = new JSONArray();
                for (int l = 0; l < serviceOrderMaster.getServiceOrderDetailMasterList().size(); l++) {
                    ServiceOrderDetailMaster serviceOrderDetailMaster = serviceOrderMaster.getServiceOrderDetailMasterList().get(l);
                    JSONObject object_one = new JSONObject();
                    object_one.put("advance_amount", serviceOrderDetailMaster._serviceorderdetail_advance_amount);
                    object_one.put("ProductID", serviceOrderDetailMaster._serviceorderdetail_product_id);
                    object_one.put("Qunatity", serviceOrderDetailMaster._serviceorderdetail_quantity);
                    object_one.put("scheme_id", serviceOrderDetailMaster._serviceorderdetail_scheme_id);
                    object_one.put("slab_id", serviceOrderDetailMaster.slabId);
                    object_one.put("mobile_serivce_details_id", serviceOrderDetailMaster._serviceorderdetail_masterid);
                    productArray.put(object_one);

                }
                JSONObject cropObj = new JSONObject();
                cropObj.put("CropId", serviceOrderMaster.getServiceOrderDetailMasterList().get(0)._serviceorderdetail_crop_id);
                cropObj.put("scheme_id", "");

                cropArray.put(cropObj);
                advBookObj.put("Crops", cropArray);
                cropObj.put("Products", productArray);
                //}
                naimObject = new JSONObject();
                adbBookArray.put(advBookObj);
                naimObject.put("AdvanceBookings", adbBookArray);

            }
            mainArray.put(naimObject);
            Log.i("  -json array -", "" + mainArray.toString());


            if (serviceOrderMasterList.size() > 0) {


                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, mainArray.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_INSERT_ADVANCEBOOKING)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                System.out.println("!!!!!!!1 Advance booking" + jsonData);

                if (jsonData != null) {
                    JSONArray jsonArray;

                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                            String service_id = jsonObject.getString("service_id");
                            String ffmid = jsonObject.getString("ffm_id");

                            //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                            Log.e("sqlite id", service_id);
                            Log.e("ffmid", ffmid);
                            sdbw = db.getWritableDatabase();
                            // updateFeedback(Feedback feedback);
                            String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + service_id;
                            sdbw.execSQL(updatequery);

                            JSONArray detailsArray = jsonObject.getJSONArray("details");
                            for (int k = 0; k < detailsArray.length(); k++) {
                                String ffm_id = detailsArray.getJSONObject(k).getString("ffm_id");
                                String order_detail_id = detailsArray.getJSONObject(k).getString("order_detail_id");

                                String updatequery1 = "UPDATE " + TABLE_SERVICEORDERDETAILS + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffm_id + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " = " + order_detail_id;
                                sdbw.execSQL(updatequery1);
                            }


                        }


                    }


                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pushApproveRejectThread();
        }


    }

    private void prepareMICommodityPriceOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all CommodityPrice..");
        List<Commodity_Price> commodity_prices = db.getAllnullCommodity_price(userId);
        Log.e("list size", String.valueOf(commodity_prices.size()));

        if (commodity_prices.size() > 0) {


            for (Commodity_Price cm : commodity_prices) {
                String log = "Id: " + cm.getID() + ",userid: " + userId + " ,cropname: " + cm.get_commodity_price_crop_name() + " ,variety: " + cm.get_commodity_price_variety_type() + ",apmc_mandi_price:" + cm.get_commodity_price_apmc_mandi_price() + " ,commodity_dealer_agent_price: " + cm.get_commodity_price_commodity_dealer_agent_price() + ",purchage_price_by_industry:" +
                        cm.get_commodity_price_purchage_price_by_industry() + ",created_by" + cm.get_commodity_price_created_by() + ",created_on:" + cm.get_commodity_price_created_on();
                Log.e("Commodity before : ", log);
                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                     /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("table", TABLE_MI_COMMODITY_PRICE)
                            .add("mobile_id", String.valueOf(cm.getID()))
                            // .add("user_id", String.valueOf(favouriteItem.get(1)))
                            .add("crop_name", cm.get_commodity_price_crop_name())
                            .add("variety_type", cm.get_commodity_price_variety_type())
                            .add("apmc_mandi_price", cm.get_commodity_price_apmc_mandi_price())
                            .add("commodity_dealer_agent_price", cm.get_commodity_price_commodity_dealer_agent_price())
                            .add("purchage_price_by_industry", cm.get_commodity_price_purchage_price_by_industry())
                            .add("created_by", cm.get_commodity_price_created_by())
                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PUSHTABLE)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertFeedback" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_MI_COMMODITY_PRICE + " SET " + KEY_COMMODITY_PRICE_FFMID + " = " + ffmid + " WHERE " + KEY_COMMODITY_PRICE_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Commodity_Price> commodity = db.getAllCommodity_price(checkuid);

                                    for (Commodity_Price cp : commodity) {
                                        log = "Id: " + cp.getID() + ",Name: " + cp.get_commodity_price_crop_name() + " variety: " + cp.get_commodity_price_variety_type() + cp.get_commodity_price_apmc_mandi_price() + cp.get_commodity_price_commodity_dealer_agent_price() + cp.get_commodity_price_purchage_price_by_industry() + cp.get_commodity_price_ffmid();

                                        Log.e("commodity_price: ", log);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            pushMICropShiftsThread();
        }


    }


    private void prepareMICropShiftsOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all CommodityPrice..");
        List<Crop_Shifts> commodity_prices = db.getAllnullCrop_Shifts(userId);
        Log.e("list size", String.valueOf(commodity_prices.size()));

        if (commodity_prices.size() > 0) {


            for (Crop_Shifts cm : commodity_prices) {
                String log = "Id: " + cm.getID() + ",userid: " + userId + " ,cropname: " + cm.get_crop_shifts_crop_name() + " ,variety: " + cm.get_crop_shifts_variety_type() + ",previous_year_area:" + cm.get_crop_shifts_previous_year_area() + " ,current_year_expected_area: " + cm.get_crop_shifts_current_year_expected_area() + ",percentage_increase_decrease:" +
                        cm.get_crop_shifts_percentage_increase_decrease() + ",created_by" + cm.get_crop_shifts_created_by() + ",created_on:" + cm.get_crop_shifts_created_on() + ",ffmid:" + cm.get_crop_shifts_ffmid();
                Log.e("Crop_Shifts before : ", log);
                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                     /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("table", TABLE_MI_CROP_SHIFTS)
                            .add("mobile_id", cm.getID())
                            // .add("user_id", String.valueOf(favouriteItem.get(1)))
                            .add("crop_name", cm.get_crop_shifts_crop_name())
                            .add("variety_type", cm.get_crop_shifts_variety_type())
                            .add("previous_year_area", cm.get_crop_shifts_previous_year_area())
                            .add("current_year_expected_area", cm.get_crop_shifts_current_year_expected_area())
                            .add("percentage_increase_decrease", cm.get_crop_shifts_percentage_increase_decrease())
                            .add("reason_crop_shift", cm.get_crop_shifts_reason_crop_shift())
                            .add("created_by", cm.get_crop_shifts_created_by())
                            .add("crop_in_saved_seed", cm.get_crop_shifs_crop_in_saved_seed())
                            .add("previous_year_srr", cm.get_crop_shifs_crop_in_previous_year())
                            .add("current_year_srr", cm.get_crop_shifs_crop_in_current_year())
                            .add("next_year_srr", cm.get_crop_shifs_crop_in_next_year())
                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PUSHTABLE)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertFeedback" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_MI_CROP_SHIFTS + " SET " + KEY_CROP_SHIFTS_FFMID + " = " + ffmid + " WHERE " + KEY_CROP_SHIFTS_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Crop_Shifts> cropshift = db.getAllCrop_Shifts(userId);

                                    for (Crop_Shifts cs : cropshift) {
                                        log = "Id: " + cs.getID() + ",userid: " + userId + " ,cropname: " + cs.get_crop_shifts_crop_name() + " ,variety: " + cs.get_crop_shifts_variety_type() + ",previous_year_area:" + cs.get_crop_shifts_previous_year_area() + " ,current_year_expected_area: " + cs.get_crop_shifts_current_year_expected_area() + ",percentage_increase_decrease:" +
                                                cs.get_crop_shifts_percentage_increase_decrease() + ",created_by" + cs.get_crop_shifts_created_by() + ",created_on:" + cs.get_crop_shifts_created_on() + ",ffmid:" + cs.get_crop_shifts_ffmid();


                                        Log.e("feedbacksqlite: ", log);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                prepareMIPriceSurveyOfflineDataAndPush();
            }
        }


    }
    //Last one to be pushed


    private void prepareMIPriceSurveyOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all CommodityPrice..");
        List<Price_Survey> commodity_prices = db.getAllnullPrice_Survey(userId);
        Log.e("list size", String.valueOf(commodity_prices.size()));

        if (commodity_prices.size() > 0) {


            for (Price_Survey cm : commodity_prices) {
                String log = "Id: " + cm.getID() + ",userid: " + userId + " ,company_name: " + cm.get_price_survey_company_name() + " ,product_name: " + cm.get_price_survey_product_name() + ",sku_pack_size:" + cm.get_price_survey_sku_pack_size() + " ,retail_price: " + cm.get_price_survey_retail_price() + ",invoice_price:" +
                        cm.get_price_survey_invoice_price() + ",net_distributor_landing_price" + cm.get_price_survey_net_distributor_landing_price() + ",created_by" + cm.get_price_survey_created_by() + ",created_on:" + cm.get_price_survey_created_on() + ",ffmid:" + cm.get_price_survey_ffmid();
                Log.e("commoditypricesqlite: ", log);
                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                     /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("table", TABLE_MI_PRICE_SURVEY)
                            .add("mobile_id", String.valueOf(cm.getID()))
                            // .add("user_id", String.valueOf(favouriteItem.get(1)))
                            .add("company_name", cm.get_price_survey_company_name())
                            .add("product_name", cm.get_price_survey_product_name())
                            .add("sku_pack_size", cm.get_price_survey_sku_pack_size())
                            .add("retail_price", cm.get_price_survey_retail_price())
                            .add("invoice_price", cm.get_price_survey_invoice_price())
                            .add("net_distributor_landing_price", cm.get_price_survey_net_distributor_landing_price())
                            .add("created_by", cm.get_price_survey_created_by())
                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PUSHTABLE)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertFeedback" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_MI_PRICE_SURVEY + " SET " + KEY_PRICE_SURVEY_FFMID + " = " + ffmid + " WHERE " + KEY_PRICE_SURVEY_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Price_Survey> price = db.getAllPrice_Survey(checkuid);

                                    for (Price_Survey cp : price) {
                                        log = "Id: " + cp.getID() + ",Name: " + cp.get_price_survey_company_name() + " product_name: " + cp.get_price_survey_product_name() + cp.get_price_survey_sku_pack_size() + cp.get_price_survey_retail_price() + cp.get_price_survey_invoice_price() + cp.get_price_survey_net_distributor_landing_price() + cp.get_price_survey_ffmid();

                                        Log.e("Price_Survey: ", log);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                prepareMIProductSurveyOfflineDataAndPush();
            }
        }


    }


    private void prepareMIProductSurveyOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all CommodityPrice..");
        List<Product_Survey> commodity_prices = db.getAllnullProduct_Survey(userId);
        Log.e("list size", String.valueOf(commodity_prices.size()));

        if (commodity_prices.size() > 0) {


            for (Product_Survey cm : commodity_prices) {
                String log = "Id: " + cm.getID() + ",userid: " + userId + " ,company_name: " + cm.get_product_survey_company_name() + " ,product_name: " + cm.get_product_survey_product_name() + "," + cm.get_product_survey_name_of_the_check_segment() + "," + cm.get_product_survey_launch_year() + "," +
                        cm.get_product_survey_no_units_sold() + "," + cm.get_product_survey_area_crop_sown_new_product() + cm.get_product_survey_remarks_unique_feature() + ",created_by" + cm.get_product_survey_created_by() + ",created_on:" + cm.get_product_survey_created_on() + ",ffmid:" + cm.get_product_survey_ffmid();

                Log.e("commoditypricesqlite: ", log);
                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                     /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("table", TABLE_MI_PRODUCT_SURVEY)
                            .add("mobile_id", String.valueOf(cm.getID()))
                            // .add("user_id", String.valueOf(favouriteItem.get(1)))
                            .add("company_name", cm.get_product_survey_company_name())
                            .add("product_name", cm.get_product_survey_product_name())
                            .add("name_of_the_check_segment", cm.get_product_survey_name_of_the_check_segment())
                            .add("launch_year", cm.get_product_survey_launch_year())
                            .add("no_units_sold", cm.get_product_survey_no_units_sold())
                            .add("area_crop_sown_new_product", cm.get_product_survey_area_crop_sown_new_product())
                            .add("remarks_unique_feature", cm.get_product_survey_remarks_unique_feature())
                            .add("created_by", checkuid)
                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PUSHTABLE)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertFeedback" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_MI_PRICE_SURVEY + " SET " + KEY_PRICE_SURVEY_FFMID + " = " + ffmid + " WHERE " + KEY_PRICE_SURVEY_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Price_Survey> price = db.getAllPrice_Survey(checkuid);

                                    for (Price_Survey cp : price) {
                                        log = "Id: " + cp.getID() + ",Name: " + cp.get_price_survey_company_name() + " product_name: " + cp.get_price_survey_product_name() + cp.get_price_survey_sku_pack_size() + cp.get_price_survey_retail_price() + cp.get_price_survey_invoice_price() + cp.get_price_survey_net_distributor_landing_price() + cp.get_price_survey_ffmid();

                                        Log.e("Price_Survey: ", log);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }


    }

    private void prepareApproveRejectOfflineDataAndPush() {

        try {
            List<ServiceOrderMaster> approvalRejectOrderList = db.getOfflineApproveRejectData();

            for (ServiceOrderMaster serviceOrderMaster : approvalRejectOrderList) {
                String requestBody = "approval_status=" + serviceOrderMaster.get_approval_status() + "&updated_by=" + serviceOrderMaster.get_updated_by() + "&order_id=" + serviceOrderMaster.getID();
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, requestBody);
                Request request = new Request.Builder()
                        .url(Constants.URL_ORDER_APPROVAL)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                responses = client.newCall(request).execute();
                String jsonData = responses.body().string();
                System.out.println("!!!!!!!1" + jsonData);
                JSONObject responseObject = new JSONObject(jsonData);
                if (responseObject.get("status").equals("success")) {

                    db.updateApprovalOrRejectStatus(serviceOrderMaster.get_updated_by(), serviceOrderMaster.get_approval_status(), serviceOrderMaster.get_approval_comments(), (String) responseObject.get("ffm_id"), false);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId) {
        //VersionControlListVo versionControlListVo=Common.getSpecificDataObject(object, VersionControlListVo.class);
        List<VersionControlVo> versionControlVoList = new ArrayList<>();
        tableMustInsertList = new ArrayList<>();
        tableMustInsertList.clear();


        for (Object objectt : object) {

            VersionControlVo versionControlListVo = Common.getSpecificDataObject(objectt, VersionControlVo.class);
            versionControlVoList.add(versionControlListVo);


        }


        if (db.getVersionControlList().size() == 0) {

            db.insertVersionControlData(versionControlVoList);

        } else {

            List<VersionControlVo> dbVersionControl = db.getVersionControlList();
            for (VersionControlVo versionControlVo : versionControlVoList) {

                for (VersionControlVo dbVersionCode : dbVersionControl) {

                    if (versionControlVo.tableName.equalsIgnoreCase(dbVersionCode.tableName) && !versionControlVo.status.equalsIgnoreCase(dbVersionCode.status)) {
                        db.updateVersionControlData(versionControlVo);
                        tableMustInsertList.add(versionControlVo.tableName);


                    }

                }
            }
        }

//if (tableMustInsertList.contains("customers") || tableMustInsertList.contains("customer_details")){


        new Async_getallcustomers().execute();
//}
    }


    public class Async_getNewOrUpdateserviceorders extends AsyncTask<Void, Void, String> {

        private String jsonData;
        private StringBuilder sb = new StringBuilder();
        private String orderidfromserviceorder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURLEVM(Constants.NEW_OR_UPDATED_RECORDS_SERVICE_ORDER, userId, team))
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 NewOrUpdate Ser" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonData != null) {
                try {
                    JSONObject resultobject = new JSONObject(jsonData);
                    JSONArray adavancebooking = resultobject.getJSONArray("newrecord");

                    for (int n = 0; n < adavancebooking.length(); n++) {


                        JSONObject result_service_order = adavancebooking.getJSONObject(n);
                        JSONObject service_order = result_service_order.getJSONObject("service_order");

                        JSONArray service_order_details = result_service_order.getJSONArray("service_order_details");

                        String ffm_id = service_order.getString("order_id");//ffm_id =order_id PRIMARY KEY
                        String order_type = service_order.getString("order_type");
                        String order_date = service_order.getString("order_date");
                        String user_id = service_order.getString("user_id");
                        String customer_id = service_order.getString("customer_id");


                        String division_id = service_order.getString("division_id");
                        String company_id = service_order.getString("company_id");
                        String advance_amount = service_order.getString("advance_amount");
                        String created_by = service_order.getString("created_by");
                        String status = service_order.getString("status");
                        String approval_status = service_order.getString("approval_status");
                        String approval_comments = service_order.getString("approval_comments");
                        String created_datetime = service_order.getString("created_datetime");
                        String updated_datetime = service_order.getString("updated_datetime");
                        if (n > 0) {
                            sb.append(",");
                        }
                        sb.append(ffm_id);


                        String selectQuery = "SELECT * FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_FFM_ID + " = '" + ffm_id + "'";


                        sdbw = db.getWritableDatabase();
                        Cursor cc = sdbw.rawQuery(selectQuery, null);
                        cc.getCount();
                        // looping through all rows and adding to list
                        if (cc.getCount() == 0) {
                            //doesn't exists therefore insert record.
                            db.addServiceorder(new ServiceOrderMaster("", order_type, order_date, user_id, customer_id, division_id, company_id, status, created_datetime, updated_datetime, ffm_id, advance_amount, created_by, approval_status, approval_comments));
                            String selectQuerys = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + " FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";
                            sdbw = db.getWritableDatabase();

                            Cursor c = sdbw.rawQuery(selectQuerys, null);
                            //System.out.println("cursor count "+cursor.getCount());
                            if (c != null && c.moveToFirst()) {
                                orderidfromserviceorder = String.valueOf(c.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                Log.e("++++ lastId ++++", orderidfromserviceorder);
                            }

                            try {

                                for (int m = 0; m < service_order_details.length(); m++) {
                                    JSONObject objinfo = service_order_details.getJSONObject(m);
                                    String service_order_details_order_id = objinfo.getString("order_id");
                                    String service_order_details_crop_id = objinfo.getString("crop_id");
                                    String scheme_id = objinfo.getString("scheme_id");
                                    String product_id = objinfo.getString("product_id");
                                    String quantity = objinfo.getString("quantity");


                                    String order_price = objinfo.getString("order_price");
                                    String service_order_details_advance_amount = objinfo.getString("advance_amount");
                                    String service_order_details_status = objinfo.getString("status");
                                    // String created_by            = objinfo.getString("created_by");
                                    String service_order_details_created_datetime = objinfo.getString("created_datetime");
                                    String service_order_details_updated_datetime = objinfo.getString("updated_datetime");
                                    String ffmID = objinfo.getString("service_order_detail_id");
                                    String slab_id = objinfo.getString("slab_id");
                                    slab_id = slab_id != null || !slab_id.equalsIgnoreCase("null") || !slab_id.equalsIgnoreCase("") ? slab_id : "";


                                    db.addServiceorderdetails(new ServiceOrderDetailMaster(ffmID, orderidfromserviceorder, service_order_details_crop_id, scheme_id,
                                            product_id, quantity, order_price,
                                            service_order_details_advance_amount, service_order_details_status, service_order_details_created_datetime, service_order_details_updated_datetime, ffmID, slab_id));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    JSONArray updated = resultobject.getJSONArray("updated");
                    for (int n = 0; n < updated.length(); n++) {


                        JSONObject result_service_order = updated.getJSONObject(n);
                        JSONObject service_order = result_service_order.getJSONObject("service_order");

                        JSONArray service_order_details = result_service_order.getJSONArray("service_order_details");

                        String ffm_id = service_order.getString("order_id");//ffm_id =order_id PRIMARY KEY
                        String order_type = service_order.getString("order_type");
                        String order_date = service_order.getString("order_date");
                        String user_id = service_order.getString("user_id");
                        String customer_id = service_order.getString("customer_id");


                        String division_id = service_order.getString("division_id");
                        String company_id = service_order.getString("company_id");
                        String advance_amount = service_order.getString("advance_amount");
                        String created_by = service_order.getString("created_by");
                        String status = service_order.getString("status");

                        String created_datetime = service_order.getString("created_datetime");
                        String updated_datetime = service_order.getString("updated_datetime");
                        String approval_status = service_order.getString("approval_status");
                        String approval_comments = service_order.getString("approval_comments");
                        if (sb.toString().length() > 0) {
                            sb.append(",");
                        }
                        sb.append(ffm_id);

                        db.updateApprovalOrRejectStatus(userId, approval_status, approval_comments, ffm_id, false);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");


            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           /* if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/

            new Async_UpdateAprovalStatus().execute("service_order", "order_id", sb.toString());


        }
    }

    private class Async_UpdateAprovalStatus extends AsyncTask<String, String, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Updating Aproval Status");
            progressDialog.show();*/
        }

        protected String doInBackground(String... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                RequestBody formBody = new FormEncodingBuilder()
                        .add("table", params[0])
                        .add("field", params[1])
                        .add("updated_ids", params[2])
                        .add("user_id", userId)
                        .build();

                Common.Log.i("Acknowledge" + params[0] + "\n " + params[1] + "\n " + params[2]);

                Request request = new Request.Builder()
                        .url(Constants.ACKNOWLEDGE_TO_SERVER)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1ACKNOWLEDGE_TO_SERVER" + formBody.toString() + "\n" + jsonData + "\n" + params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }


    private void prepareStockMovementDataAndPush() {
        mainArray = new JSONArray();
        JSONArray adbBookArray = new JSONArray();
        JSONObject naimObject = null;

        ArrayList<StockMovementUnSynedPojo> stockMovementUnSynedPojoList = db.getOfflineStockPlacementListUnSyncData1();
        for (StockMovementUnSynedPojo stockMovementUnSynedPojo : stockMovementUnSynedPojoList) {

            ArrayList<StockMovementUnSynedPojo> stockMovementUnSynedPojos = db.getOfflineStockPlacementListUnSyncData(stockMovementUnSynedPojo.stockMovementId);
            for (StockMovementUnSynedPojo stockMovementUnSynedPojo1 : stockMovementUnSynedPojos) {

                JSONObject advBookObj = new JSONObject();

                try {
                    advBookObj.put("CompanyID", stockMovementUnSynedPojo1.companyId);
                    advBookObj.put("customer_id", stockMovementUnSynedPojo1.customerId);
                    advBookObj.put("role", stockMovementUnSynedPojo1.userType);
                    advBookObj.put("DivisionID", stockMovementUnSynedPojo1.divisionId);
                    advBookObj.put("id", stockMovementUnSynedPojo1.stockMovementId);
                    advBookObj.put("user_id", userId);
                    advBookObj.put("placed_date", stockMovementUnSynedPojo1.placedDate);
                    advBookObj.put("movement_type", "0");


                    JSONArray cropArray = new JSONArray();

                    JSONArray productArray = new JSONArray();


                    JSONObject object_one = new JSONObject();
                    object_one.put("stock_placed", stockMovementUnSynedPojo1.stockPlaced);
                    object_one.put("ProductID", stockMovementUnSynedPojo1.productId);
                    object_one.put("current_stock", stockMovementUnSynedPojo1.currentStock);
                    object_one.put("pog", stockMovementUnSynedPojo1.pog);
                    object_one.put("sqlite_id_detail", stockMovementUnSynedPojo1.stockMovementDetailId);
                    productArray.put(object_one);


                    JSONObject cropObj = new JSONObject();
                    cropObj.put("CropId", stockMovementUnSynedPojo1.cropId);

                    cropArray.put(cropObj);
                    advBookObj.put("Crops", cropArray);
                    cropObj.put("Products", productArray);
                    //}
                    naimObject = new JSONObject();
                    adbBookArray.put(advBookObj);
                    naimObject.put("stockmovement", adbBookArray);


                } catch (JSONException e) {
                }

            }
        }
        if (naimObject != null) {
            mainArray.put(naimObject);
        }
        Log.i("  -json array -", "" + mainArray.toString());
        if (mainArray.length() > 0) {
            new callAPIToPushStockmovementData().execute();
        }
    }

    private class callAPIToPushStockmovementData extends AsyncTask<Void, Void, String> {
        private String jsonData;

        @Override
        protected String doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, mainArray.toString());
            Request request = new Request.Builder()
                    .url(Constants.URL_INSERT_STOCKMOVEMENT)
                    .post(body)
                    .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();


                jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (jsonData != null) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject jsonObject = null;

                        jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArrayDetails = jsonObject.getJSONArray("details");
                            for (int j = 0; j < jsonArrayDetails.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayDetails.getJSONObject(j);


                                String sqlite_id = jsonObject1.getString("sqlite_id");
                                String ffmid = jsonObject1.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", sqlite_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_SMD + " SET " + db.FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SMD_DETAIL_ID + " = " + sqlite_id;
                                sdbw.execSQL(updatequery);
                            }

                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            // Common.dismissProgressDialog(progressDialog);

        }
    }


    public class Async_getallcustomers extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.GET_CUSTOMERS_AND_CUSTOMER_DETAILS, userId, team))
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
                    db.deleteDataByTableName(new String[]{"customers", "customer_details"});

                    JSONObject jsonObject = new JSONObject(jsonData);


                    JSONArray companyarray = jsonObject.getJSONArray("customer_information");

                    for (int n = 0; n < companyarray.length(); n++) {


                        JSONObject jsonObjectCstomerData = companyarray.getJSONObject(n);

                        JSONObject objinfo = jsonObjectCstomerData.getJSONObject("customer_data");
                        JSONArray jsonArrayCustomerDetails = jsonObjectCstomerData.getJSONArray("customer_data_details");

                        String customer_id = objinfo.getString("customer_id");
                        String customer_name = objinfo.getString("customer_name");
                        String customer_code = objinfo.getString("customer_code");
                        String address = objinfo.getString("address");
                        String street = objinfo.getString("street");
                        String company_id = objinfo.getString("company_id");
                        String city = objinfo.getString("city");
                        String region_id = objinfo.getString("region_id");
                        String country = objinfo.getString("country");
                        String telephone = objinfo.getString("telephone");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");
                        String status = objinfo.getString("status");
                        String password = objinfo.getString("password");
                        String email = objinfo.getString("email");
                        String state = objinfo.getString("state");
                        String district = objinfo.getString("district");
                        String lat_lon = objinfo.getString("lat_lon");


                        Log.d("Insert: ", "Inserting Customers ..");
                        db.addCustomers(new Customers(customer_id,
                                customer_name,
                                customer_code,
                                address,
                                street,
                                city,
                                country,
                                region_id,
                                telephone,
                                company_id,
                                status,
                                created_datetime,
                                updated_datetime, password, email, state, district, lat_lon));

                        Log.d("Inserted: ", "Inserted Customers ..");

                        for (int i = 0; i < jsonArrayCustomerDetails.length(); i++) {
                            JSONObject jsonObjectData = jsonArrayCustomerDetails.getJSONObject(i);

                            String customer_id1 = jsonObjectData.getString("customer_id");
                            String divsion_id = jsonObjectData.getString("divsion_id");
                            String credit_limit = jsonObjectData.getString("credit_limit");
                            String inside_bucket = jsonObjectData.getString("inside_bucket");
                            String outside_bucket = jsonObjectData.getString("outside_bucket");
                            String status1 = jsonObjectData.getString("status");
                            String created_datetime1 = jsonObjectData.getString("created_datetime");
                            String updated_datetime1 = jsonObjectData.getString("updated_datetime");
                            //String credit_balance = jsonObjectData.getString("credit_balance");
                            String credit_balance = "0";

                            Log.d("Insert: ", "Inserting Customer details ..");
                            db.addCustomer_details(new Customer_Details(customer_id1, divsion_id, credit_limit, inside_bucket, outside_bucket, status1, created_datetime1, updated_datetime1, credit_balance));

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            /*Log.d("Reading: ", "Reading all customers..");

            List<Customers> customers = db.getAllCustomers();

            for (Customers cus : customers) {
                String log = "Id: "+cus.getID() + " \n customers master ID"+ cus.getCusMasterId()+" , \n customersname: " + cus.getCusName() + "\n cus code "+cus.getCuscode()+" \n Address : "+cus.getCusaddress()+" \n Street : "+cus.getCusstreet()+  "\n cus city & country "+cus.getCus_city() +":"+ cus.getCuscountry()+ " \n C region id"+ cus.getCusregion_Id()+ " \n C telephone"+ cus.getCustelephone()+" \n C C date"+ cus.getCuscdatetime()+" \n C company Id"+ cus.getCuscompany_Id()+" \n C status"+ cus.getCusstatus()+" \n Cus cdate"+cus.getCuscdatetime()+" \n Cus udate"+cus.getCusudatetime();
                // Writing Contacts to log
                Log.e("Customers: ", log);

            }*/


        }
    }


    public class Async_Routepath extends AsyncTask<String, Void, String> {

        private String jsonData;

        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("latlon", params[0])
                        .add("tracking_id", params[1])
                        .build();

                Response responses = null;

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_ROUTEPATH_UPDATE_INTERVAL)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }


    }


    private void prepareStockMovementRetailerDataAndPush() {
        mainArray = new JSONArray();

        JSONArray adbBookArray = new JSONArray();
        JSONObject naimObject = null;
        ArrayList<StockMovementRetailerDetails> stockMovementUnSynedPojos = new ArrayList<>();

        ArrayList<StockMovementRetailerDetails> stockMovementUnSynedPojoList = db.getOfflineStockPlacementSMRDListUnSyncData1();
        for (StockMovementRetailerDetails stockMovementUnSynedPojo : stockMovementUnSynedPojoList) {

            stockMovementUnSynedPojos = db.getOfflineStockPlacementSMRDListUnSyncData(Integer.parseInt(stockMovementUnSynedPojo.stockMovementId));
            for (StockMovementRetailerDetails stockMovementUnSynedPojo1 : stockMovementUnSynedPojos) {

                JSONObject advBookObj = new JSONObject();

                try {
                    advBookObj.put("CompanyID", stockMovementUnSynedPojo1.companyId);
                    advBookObj.put("role", stockMovementUnSynedPojo1.userType);
                    advBookObj.put("DivisionID", stockMovementUnSynedPojo1.divisionId);
                    advBookObj.put("id", stockMovementUnSynedPojo1.stockMovementId);
                    advBookObj.put("user_id", userId);
                    advBookObj.put("placed_date", stockMovementUnSynedPojo1.placedDate);
                    advBookObj.put("movement_type", "0");
                    advBookObj.put("retailer_id", stockMovementUnSynedPojo1.retailerId);


                    JSONArray cropArray = new JSONArray();

                    JSONArray productArray = new JSONArray();


                    JSONObject object_one = new JSONObject();
                    object_one.put("stock_placed", stockMovementUnSynedPojo1.stockPlaced);
                    object_one.put("ProductID", stockMovementUnSynedPojo1.productId);
                    object_one.put("current_stock", stockMovementUnSynedPojo1.currentStock);
                    object_one.put("pog", stockMovementUnSynedPojo1.pog);
                    object_one.put("sqlite_id_detail", stockMovementUnSynedPojo1.stockMovementRetailerId);
                    productArray.put(object_one);


                    JSONObject cropObj = new JSONObject();
                    cropObj.put("CropId", stockMovementUnSynedPojo1.cropId);

                    cropArray.put(cropObj);
                    advBookObj.put("Crops", cropArray);
                    cropObj.put("Products", productArray);
                    //}
                    naimObject = new JSONObject();
                    adbBookArray.put(advBookObj);
                    naimObject.put("stockmovement", adbBookArray);


                } catch (JSONException e) {
                }

            }
        }
        if (naimObject != null) {
            mainArray.put(naimObject);
        }
        Log.i("  -json array -", "" + mainArray.toString());
        if (mainArray.length() > 0 || stockMovementUnSynedPojos.size() > 0) {
            new callAPIToPushStockmovementRetailerData().execute();
        }
    }


    private class callAPIToPushStockmovementRetailerData extends AsyncTask<String, Void, String> {
        private String jsonData;

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            try {
                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, mainArray.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERT_STOCKMOVEMENT_RETAILER)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = null;


                response = client.newCall(request).execute();


                jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (jsonData != null) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject jsonObject = null;

                        jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArrayDetails = jsonObject.getJSONArray("details");
                            for (int j = 0; j < jsonArrayDetails.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayDetails.getJSONObject(j);


                                String sqlite_id = jsonObject1.getString("sqlite_id");
                                String ffmid = jsonObject1.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", sqlite_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS + " SET " + db.FFM_ID + " = " + ffmid + " WHERE stock_movement_retailer_id" + " = " + sqlite_id;
                                sdbw.execSQL(updatequery);
                            }

                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }


        }
    }


    private void prepareStockReturnsDataAndPush(Intent intent) {
        mainArray = new JSONArray();
        JSONArray adbBookArray = new JSONArray();
        JSONObject naimObject = null;

        ArrayList<StockReturnUnSynedPojo> stockMovementUnSynedPojoList = db.getOfflineStockReturnListUnSyncData1();
        for (StockReturnUnSynedPojo stockMovementUnSynedPojo : stockMovementUnSynedPojoList) {

            ArrayList<StockReturnUnSynedPojo> stockMovementUnSynedPojos = db.getOfflineStockReturnsListUnSyncData(stockMovementUnSynedPojo.stockReturnId);
            for (StockReturnUnSynedPojo stockMovementUnSynedPojo1 : stockMovementUnSynedPojos) {

                JSONObject advBookObj = new JSONObject();

                try {
                    advBookObj.put("CompanyID", stockMovementUnSynedPojo1.companyId);
                    advBookObj.put("customer_id", stockMovementUnSynedPojo1.customerId);
                    // advBookObj.put("role", stockMovementUnSynedPojo1.userType);
                    advBookObj.put("DivisionID", stockMovementUnSynedPojo1.divisionId);
                    advBookObj.put("id", stockMovementUnSynedPojo1.stockReturnId);
                    advBookObj.put("user_id", userId);


                    JSONArray cropArray = new JSONArray();

                    JSONArray productArray = new JSONArray();


                    JSONObject object_one = new JSONObject();
                    object_one.put("mobile_stock_return_details_id", stockMovementUnSynedPojo1.stockReturnsDetailsId);
                    object_one.put("ProductID", stockMovementUnSynedPojo1.productId);
                    object_one.put("CropId", stockMovementUnSynedPojo1.cropId);
                    object_one.put("Qunatity", stockMovementUnSynedPojo1.quantity);
                    productArray.put(object_one);


                    JSONObject cropObj = new JSONObject();
                    cropObj.put("CropId", stockMovementUnSynedPojo1.cropId);

                    cropArray.put(cropObj);
                    advBookObj.put("Crops", cropArray);
                    cropObj.put("Products", productArray);
                    //}
                    naimObject = new JSONObject();
                    adbBookArray.put(advBookObj);
                    naimObject.put("stockreturns", adbBookArray);


                } catch (JSONException e) {
                }

            }
        }
        if (naimObject != null)
            mainArray.put(naimObject);
        Log.i("  -json array -", "" + mainArray.toString());
        if (mainArray.length() > 0) {
            new callAPIToPushStockReturnsData(intent).execute();
        } else {
            logoutBroadcast(intent);
        }


    }


    private void logoutBroadcast(Intent intent) {
        if (intent != null && intent.hasExtra("logout")) {
            if (Common.haveInternet(mContext)) {
                ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("logout_receiver");
                Bundle resultData = new Bundle();
                resultData.putBoolean("logout", true);
                receiver.send(UPDATE_PROGRESS, resultData);
                Log.d("logout", "BackPushService.....");
            }
        }
    }


    private class callAPIToPushStockReturnsData extends AsyncTask<Void, Void, String> {
        private String jsonData;
        Intent intent;

        public callAPIToPushStockReturnsData(Intent intent) {
            this.intent = intent;
        }

        @Override
        protected String doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, mainArray.toString());
            Request request = new Request.Builder()
                    .url(Constants.URL_INSERTING_STOCKRETURNS)
                    .post(body)
                    .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();


                jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (jsonData != null) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject jsonObject = null;

                        jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArrayDetails = jsonObject.getJSONArray("details");
                            for (int j = 0; j < jsonArrayDetails.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayDetails.getJSONObject(j);


                                String sqlite_id = jsonObject1.getString("return_detail_id");
                                String ffmid = jsonObject1.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", sqlite_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_STOCK_RETURNS_DETAILS + " SET " + db.KEY_FFMID + " = " + ffmid + " WHERE " + db.KEY_STOCK_RETURNS_DETAILS_ID + " = " + sqlite_id;
                                sdbw.execSQL(updatequery);
                            }

                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            logoutBroadcast(intent);
        }
    }

}


