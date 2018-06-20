package com.nsl.app.scheduler;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.nsl.app.Companies;
import com.nsl.app.Constants;
import com.nsl.app.Crops;
import com.nsl.app.Customer_Details;
import com.nsl.app.Customers;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.Divisions;
import com.nsl.app.LoginActivity;
import com.nsl.app.Products_Pojo;
import com.nsl.app.Regions;
import com.nsl.app.Scheme_Products;
import com.nsl.app.Schemes;
import com.nsl.app.Users;
import com.nsl.app.commonutils.Common;
import com.nsl.app.network.RetrofitRequester;
import com.nsl.app.network.RetrofitResponseListener;
import com.nsl.app.pojo.GradePojo;
import com.nsl.app.pojo.VersionControlVo;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code DailySchedulerReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SchedulingService extends IntentService implements RetrofitResponseListener {
    public SchedulingService() {
        super("SchedulingService");
    }

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String mypreference = "mypref";
    private static final String TAG = SchedulingService.class.getName();
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
        Log.i("Scheduler", "No doodle found.onCreate :-(");
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        this.mContext = getApplicationContext();

        userId = sharedpreferences.getString("userId", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        team = sharedpreferences.getString("team", "");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i("Scheduler", "No doodle found. :-(");
        if (Common.haveInternet(this) && !userId.equals("")) {
            if (sharedpreferences.contains(Constants.SharedPrefrancesKey.CURRENT_DATE) && sharedpreferences.getString(Constants.SharedPrefrancesKey.CURRENT_DATE, "").equals(Common.getCurrentDate())) {
                Log.i("Scheduler", "haveInternet1..");

            } else {
                Log.i("Scheduler", "else 2..");
                if (Common.isTime3AM()) {
                    Log.i("Scheduler", "isTime3AM...");
                    sharedpreferences.edit().putString(Constants.SharedPrefrancesKey.CURRENT_DATE, Common.getCurrentDate()).commit();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", "update_table_names");

                    new RetrofitRequester(this).callGetApi(map);
                }

            }


        }


        // Release the wake lock provided by the BroadcastReceiver.
        DailySchedulerReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
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

                        Log.d("tableMustInsertList",versionControlVo.tableName+"dbVersionCode.status: "+dbVersionCode.status+"versionControlVo.status: "+versionControlVo.status);
                        tableMustInsertList.add(versionControlVo.tableName);
                        db.updateVersionControlData(versionControlVo);


                    }

                }
            }
        }

//if (tableMustInsertList.contains("customers") || tableMustInsertList.contains("customer_details")){





        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_DIVISION)) {

            new Async_getalldivisions().execute();

        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_REGION)) {

            new Async_getallreions().execute();

        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_COMPANIES)) {

            new Async_getallcompanies().execute();

        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_CROPS)) {


            new Async_getallcrops().execute();
        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_PRODUCTS)) {

            new Async_getallproducts().execute();

        }

        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_PRODUCT_PRICE)) {

            new Async_getallproducts_price().execute();

        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_SCHEMES)) {
            new Async_getallschemes().execute();


        }

        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_USERS)) {

            new Async_getallusers().execute();

        }

        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_GRADE)) {

            new Async_getAllGrade().execute();

        }

        new Async_getallcustomers().execute();
//}
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
                           // String credit_balance = jsonObjectData.getString("credit_balance");
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
                // Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }


    private class Async_getAllGrade extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_GRADE)
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
            if (jsonData != null) {
                try {
                    db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_GRADE});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String grade_id = objinfo.getString("grade_id");
                        String grade_name = objinfo.getString("grade_name");
                        String price_per_km = objinfo.getString("price_per_km");
                        GradePojo gradePojo = new GradePojo();
                        gradePojo.gradeId = grade_id;
                        gradePojo.gradeName = grade_name;
                        gradePojo.pricePerKm = price_per_km;

                        Log.d("Insert: ", "Inserting Users ..");
                        db.insertGrade(db.getReadableDatabase(), gradePojo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    private class Async_getallusers extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_USERS+userId)
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
            if (jsonData != null) {
                try {
                    db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_USERS});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String user_id = objinfo.getString("user_id");
                        String first_name = objinfo.getString("first_name");
                        String last_name = objinfo.getString("last_name");
                        String mobile_no = objinfo.getString("mobile_no");
                        String email = objinfo.getString("email");
                        String sap_id = objinfo.getString("sap_id");
                        String password = objinfo.getString("password");
                        String role_id = objinfo.getString("role_id");
                        String reporting_manager_id = objinfo.getString("reporting_manager_id");

                        String status = objinfo.getString("status");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");
                        String designation = objinfo.getString("designation");
                        String headquarter = objinfo.getString("headquarter");
                        String location = objinfo.getString("location");
                        String region_id = objinfo.getString("region_id");
                        String grade = objinfo.getString("grade");
                        String cost_center = objinfo.getString("cost_center");
                        String profile_base64 = objinfo.getString("profile_base64");


                        Log.d("Insert: ", "Inserting Users ..");
                        db.addusers(new Users(user_id, first_name, last_name, mobile_no, email, sap_id, password, role_id, reporting_manager_id, status, created_datetime, updated_datetime, designation, headquarter, location, region_id, grade, cost_center,profile_base64));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //   Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }


    private class Async_getallschemes extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_SCHEMES+userId)
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
            /// bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
                    db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_SCHEME_PRODUCTS,Constants.MasterTableNames.TABLE_SCHEMES});
                    JSONObject jsonObject=new JSONObject(jsonData);

                    JSONArray companyarray = jsonObject.getJSONArray("scheme_information");

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);
                        JSONObject schemeDataObj = objinfo.getJSONObject("scheme_data");

                        String scheme_id = schemeDataObj.getString("scheme_id");
                        String scheme_title = schemeDataObj.getString("scheme_title");
                        String product_sap_code = schemeDataObj.getString("scheme_sap_code");
                        String file_location = schemeDataObj.getString("file_location");
                        String status = schemeDataObj.getString("status");

                        db.addSchemes(new Schemes(scheme_id, scheme_title, product_sap_code, file_location, status));

                        JSONArray schemeProductDetailsArray = objinfo.getJSONArray("scheme_product_details");

                        for (int j=0; j< schemeProductDetailsArray.length(); j++){
                            JSONObject schemeProductDetailsObjinfo = companyarray.getJSONObject(j);

                            String scheme_id1 = schemeProductDetailsObjinfo.getString("scheme_id");
                            String product_id = schemeProductDetailsObjinfo.getString("product_id");
                            String scheme_price = schemeProductDetailsObjinfo.getString("price_per_packet");
                            String region_id = schemeProductDetailsObjinfo.getString("region_id");
                            String company_id = schemeProductDetailsObjinfo.getString("company_id");
                            String crop_id = schemeProductDetailsObjinfo.getString("crop_id");
                            String slab_id = schemeProductDetailsObjinfo.getString("slab_id");
                            String booking_incentive = schemeProductDetailsObjinfo.getString("booking_incentive");
                            String valid_from = schemeProductDetailsObjinfo.getString("valid_from");
                            String valid_to = schemeProductDetailsObjinfo.getString("valid_to");
                            String booking_year = schemeProductDetailsObjinfo.getString("booking_year");
                            String season_code = schemeProductDetailsObjinfo.getString("season_code");
                            String extenstion_date = schemeProductDetailsObjinfo.getString("extenstion_date");


                            // Log.d("Insert: ", "Inserting Divisions ..");
                            db.addScheme_Products(new Scheme_Products(scheme_id1, product_id, scheme_price, region_id, company_id, crop_id, slab_id, booking_incentive, valid_from, valid_to, booking_year, season_code, extenstion_date));

                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }



        }


    }


    private class Async_getallproducts_price extends AsyncTask<Void, Void, String> {

        private String jsonData;

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_PRODUCT_PRICE)
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
            //  bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
                    db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_PRODUCT_PRICE});

                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        db.addProductPrice(new Products_Pojo(objinfo.getString("product_id"), objinfo.getString("price"), objinfo.getString("discount"), objinfo.getString("from_date"), objinfo.getString("to_date"), objinfo.getString("status"), objinfo.getString("region_id")));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }

    private class Async_getallproducts extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_PRODUCTS)
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
            //  bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
                    db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_PRODUCTS});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        // Log.d("Insert: ", "Inserting Products ..");
                        db.addProducts(new Products_Pojo(objinfo.getString("product_id"),
                                objinfo.getString("product_name"),
                                objinfo.getString("product_description"),
                                objinfo.getString("product_sap_code"),
                                objinfo.getString("crop_id"),
                                objinfo.getString("company_id"),
                                objinfo.getString("division_id"),
                                "", "", "", "", "",
                                objinfo.getString("status"),
                                objinfo.getString("products_images"),
                                objinfo.getString("product_videos"),
                                objinfo.getString("created_datetime"),
                                objinfo.getString("updated_datetime"),
                                objinfo.getString("no_packets"),
                                objinfo.getString("catlog_url"),
                                objinfo.getString("brand_name")));
                        //Log.d("Reading: ", "Reading all products..");

                        /*List<Products_Pojo> products_pojo = db.getAllProducts();

                        for (Products_Pojo div : products_pojo) {
                            String log = "Id: "+div.getID()+ " \n Product master ID "+div.getProductMasterId()+" \n ,Product: " + div.getProductName()+ " \n ,Product division_id: " + div.getProductdivisionid()+ "\n  ,Product_region: " + div.getProductregeion() + " ,Product cropid: " + div.getProductcropid();
                            // Writing Contacts to log
                            Log.e("Products : ", log);

                        }*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }


    private class Async_getallcrops extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_CROPS)
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
            //bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
                    db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_CROPS});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String crop_id = objinfo.getString("crop_id");
                        String crop_name = objinfo.getString("crop_name");
                        String crop_code = objinfo.getString("crop_code");
                        String divsion_id = objinfo.getString("divsion_id");
                        String crop_sap_id = objinfo.getString("crop_sap_id");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");


                        //Log.d("Insert: ", "Inserting Crops ..");
                        db.addCrops(new Crops(crop_id, crop_name, crop_code, crop_sap_id, divsion_id, created_datetime, updated_datetime));


                        // do some stuff....

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

        }

    }


    private class Async_getallreions extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_REGIONS)
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
                    db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_REGION});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String region_id = objinfo.getString("region_id");
                        String region_name = objinfo.getString("region_name");
                        String region_code = objinfo.getString("region_code");
                        String status = objinfo.getString("status");

                        // Log.d("Insert: ", "Inserting Regions ..");
                        db.addRegions(new Regions(region_id, region_name, region_code, status));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }


    private class Async_getallcompanies extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {
            //odb.delete(db.TABLE_COMPANIES,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_COMPANIES)
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                //System.out.println("######"+Constants.URL_NSL_MAIN + Constants.URL_CUSTOMERS + userId);


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
                    db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_COMPANIES});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String company_id = objinfo.getString("company_id");
                        String name = objinfo.getString("name");
                        String company_code = objinfo.getString("company_code");
                        String company_sap_id = objinfo.getString("company_sap_id");
                        String status = objinfo.getString("status");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");


                        // Log.d("Insert: ", "Inserting Divisions ..");
                        db.addCompanies(new Companies(company_id, name, company_sap_id, company_code, status, created_datetime, updated_datetime));

                        // do some stuff....

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }





    private class Async_getalldivisions extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage(LoginActivity.this.getString(R.string.loading_masters));
            progressDialog.setCancelable(false);
            progressDialog.show();*/

            //  bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_DIVISIONS)
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
            //  bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
                    db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_DIVISION});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String division_id = objinfo.getString("division_id");
                        String division_name = objinfo.getString("division_name");
                        String division_code = objinfo.getString("division_code");
                        String division_sap_id = objinfo.getString("division_sap_id");
                        String status = objinfo.getString("status");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");


                        // Log.d("Insert: ", "Inserting Divisions ..");
                        db.addDivisions(new Divisions(division_id, division_name, division_code, division_sap_id, status, created_datetime, updated_datetime));


                       /* HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("division_id",        division_id   );
                        map.put("division_name",      division_name);
                        map.put("division_code",      division_code);
                        map.put("division_sap_id",    division_sap_id);
                        map.put("status",             status);
                        map.put("created_datetime",   created_datetime);
                        map.put("updated_datetime",   updated_datetime);


                        favouriteItem.add(map);*/
                        // do some stuff....

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }


}
