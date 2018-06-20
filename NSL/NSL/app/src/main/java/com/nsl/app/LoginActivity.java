package com.nsl.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.nsl.app.commonutils.Common;
import com.nsl.app.complaints.Complaints;
import com.nsl.app.feedback.Feedback;
import com.nsl.app.network.RetrofitRequester;
import com.nsl.app.network.RetrofitResponseListener;
import com.nsl.app.pojo.DistributorsRetailerPojo;
import com.nsl.app.pojo.GradePojo;
import com.nsl.app.pojo.StockMovementDetailsPojo;
import com.nsl.app.pojo.StockMovementPoJo;
import com.nsl.app.pojo.StockMovementRetailerDetails;
import com.nsl.app.pojo.StockReturnDetailsPoJo;
import com.nsl.app.pojo.StockReturnPoJo;
import com.nsl.app.pojo.VersionControlVo;
import com.nsl.app.retailers.Retailer;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nsl.app.DatabaseHandler.KEY_EMP_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;

public class LoginActivity extends AppCompatActivity implements RetrofitResponseListener {

    EditText et_username, et_password;
    Button btn_submit, btn_Toggle;
    TextView tv_changepassword;
    public static boolean isSignedIn = false;
    ProgressDialog progressDialog;
    AlertDialog alert;
    String team;
    private String str_username;
    private String str_password;
    private String userId;
    private String jsonData;
    private String status;
    private int role;
    private String customerscount;
    private String checkdivisions;
    private String fcm_id;
    DatabaseHandler db;
    private ProgressBar bar;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    final Context context = this;
    Dialog dialog;
    private String strusername, stroldpswd, strpaswd, strcnfmpswd;
    private Dialog dataSyncingDialog;
    private ArrayList<String> tableMustInsertList;
    private String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String checkuid = sharedpreferences.getString("userId", "");
        fcm_id = sharedpreferences.getString("fcm_id", "");
        if (fcm_id.equalsIgnoreCase("") || fcm_id.equalsIgnoreCase("")) {
            fcm_id = FirebaseInstanceId.getInstance().getToken();
        }
        checkdivisions = sharedpreferences.getString("Divisions", "");
        //  new RetrofitRequester(this).callPostLoginApi();

        if (Common.haveInternet(this)) {


            Map<String, String> map = new HashMap<String, String>();
            map.put("name", "update_table_names");

            //  new RetrofitRequester(this).callGetApi(map);


        }


        if (checkuid != null && checkuid.length() > 0) {
            Intent login1 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(login1);
            finish();
        }
        bar = (ProgressBar) this.findViewById(R.id.progressBar);
        btn_Toggle = (Button) findViewById(R.id.btn_visibility);
        tv_changepassword = (TextView) findViewById(R.id.tv_changepassword);

        btn_Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSignedIn) {
                    btn_Toggle.setBackgroundResource(R.drawable.invisible);
                    isSignedIn = true;
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_password.setSelection(et_password.length());

                } else {
                    btn_Toggle.setBackgroundResource(R.drawable.vsible);
                    isSignedIn = false;
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_password.setSelection(et_password.length());

                }

            }
        });
        // Set up the login form.
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);


        et_username.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    et_password.requestFocus();
                    return true;
                }
                return false;
            }
        });
        et_password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    enterlogin();
                    return true;
                }
                return false;
            }
        });
        tv_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.alert_changepassword);

                final EditText et_username = (EditText) dialog.findViewById(R.id.et_username);
                final EditText et_old_password = (EditText) dialog.findViewById(R.id.et_cpassword);
                final EditText et_new_password = (EditText) dialog.findViewById(R.id.et_newpassword);
                final EditText et_confirm_password = (EditText) dialog.findViewById(R.id.et_confirmpassword);


                et_username.requestFocus();

                Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);


                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                // if button is clicked, close the custom dialog
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        strusername = et_username.getText().toString();
                        stroldpswd = et_old_password.getText().toString();
                        strpaswd = et_new_password.getText().toString();
                        strcnfmpswd = et_confirm_password.getText().toString();


                        if (TextUtils.isEmpty(strusername) || strusername.length() > 0 && strusername.startsWith(" ")) {
                            et_username.setError("Please Enter Username");
                            et_username.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(stroldpswd) || stroldpswd.length() > 0 && stroldpswd.startsWith(" ")) {
                            et_old_password.setError("Please enter old password");
                            et_old_password.requestFocus();
                            return;
                        }


                        if (TextUtils.isEmpty(strpaswd) || strpaswd.length() > 0 && strpaswd.startsWith(" ")) {
                            et_new_password.setError("Please enter new  password");
                            et_new_password.requestFocus();
                            return;
                        }


                        if (TextUtils.isEmpty(strcnfmpswd) || strcnfmpswd.length() > 0 && strcnfmpswd.startsWith(" ")) {
                            et_confirm_password.setError("Please confirm password");
                            et_confirm_password.requestFocus();
                            return;
                        }
                        if (strpaswd.equals(strcnfmpswd)) {

                            try {
                                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                                if (netInfo != null) {  // connected to the internet
                                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                        // connected to wifi
                                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                                        new Async_ChangePassword().execute();
                                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                        // connected to the mobile provider's data plan

                                        new Async_ChangePassword().execute();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Log.d("Tag", e.toString());
                            }
                        } else {
                            et_confirm_password.setError(" Password and Confirm password should be same");
                            return;
                        }


                        dialog.dismiss();
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }


        });


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterlogin();
            }
        });
        // showpopup();

    }

    private Dialog showpopup() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_progress);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return dialog;

       /* progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/

    }

    private void enterlogin() {

        Common.hideSoftKeyboard(this);

        str_username = et_username.getText().toString();
        str_password = et_password.getText().toString();

        /*et_username.setFocusable(false);
        et_username.setFocusableInTouchMode(true);
        et_password.setFocusable(false);
        et_password.setFocusableInTouchMode(true);*/
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        if (TextUtils.isEmpty(str_username) || str_username.length() > 0 && str_username.startsWith(" ")) {
            et_username.setError("Please enter username");
            return;
        }
        if (TextUtils.isEmpty(str_password) || str_password.length() > 0 && str_password.startsWith(" ")) {
            et_password.setError("Please enter password");
            return;
        } else {
            if (Common.haveInternet(getApplicationContext())) {
                sharedpreferences.edit().putLong(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME, 0).commit();
                new Async_Login().execute();

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Exit alertdFragment = new Exit();
        alertdFragment.show(fragmentManager, "Exit");
    }


    private class Async_Login extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                Response responses = null;

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "username=" + str_username + "&password=" + str_password + "&fcm_id=" + fcm_id + "&imei_number=" + android_id + "&device_info=" + Common.getDeviceInfo());
                Request request = new Request.Builder()
                        .url(Constants.URL_LOGIN)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1login" + jsonData);
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
            bar.setVisibility(View.GONE);

            if (s != null) {
                try {
                    JSONObject jsonobject = new JSONObject(s);
                    JSONArray teamarray;
                    status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        userId = jsonobject.getString("user_id");
                        role = jsonobject.getInt("role");
                        customerscount = jsonobject.getString("customers");
                        team = jsonobject.getString("team");
                        Log.d("role: ", String.valueOf(role));
                        if (role != Constants.Roles.ROLE_5 || role != Constants.Roles.ROLE_6 || role != Constants.Roles.ROLE_7) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("userId", userId);
                            editor.putInt(Constants.SharedPrefrancesKey.ROLE, role);
                            editor.putString("customerscount", customerscount);
                            editor.putString("team", team);
                            editor.putString("img", "1");
                            editor.commit();
                            boolean isDefault = str_password.equalsIgnoreCase("password") ? true : false;
                            Common.saveDataInSP(LoginActivity.this, Constants.SharedPrefrancesKey.IS_DEFAULT_PASSWORD, isDefault);

                            Log.d("img: ", "in login 1");
                            if (checkdivisions != null && checkdivisions.equalsIgnoreCase("true")) {
                                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(main);
                                finish();
                            } else {
                                dataSyncingDialog = showpopup();

                                if (Common.haveInternet(getApplicationContext())) {  // connected to the internet
                                    new Async_getalldivisions().execute();

                                } else {
                                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }


                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Please contact to administrator", Toast.LENGTH_LONG).show();

                            return;
                        }


                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(getApplicationContext(), jsonobject.getString("msg"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Credentials \n Please try again", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }


        }
    }

    private class Async_ChangePassword extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                Response responses = null;

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "username=" + strusername + "&password=" + stroldpswd + "&newpassword=" + strcnfmpswd);
                Request request = new Request.Builder()
                        .url(Constants.URL_CHANGE_PASSWORD)
                        .post(body)
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
            bar.setVisibility(View.GONE);

            if (s != null) {
                try {
                    JSONObject jsonobject = new JSONObject(s);

                    status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        Toast.makeText(getApplicationContext(), "Password Updated Sucessfully!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Password Doesn't match \n Please try again", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }


        }
    }

    private class Async_getalldivisions extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


            callcompanies();


        }
    }


    private void callcompanies() {

        if (Common.haveInternet(LoginActivity.this)) {  // connected to the internet

            new Async_getallcompanies().execute();

        }

    }

    private void callcrops() {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo != null) {  // connected to the internet
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    new Async_getallcrops().execute();

                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    new Async_getallcrops().execute();
                }
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }
    }

    private class Async_getallcompanies extends AsyncTask<Void, Void, String> {

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
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

            /*Log.d("Reading: ", "Reading all companies..");

            List<Companies> companies = db.getAllCompanies();

            for (Companies com : companies) {
                String log = "Id: "+com.getID()+" ,Name: " + com.getCompanyName()+ " ,company code: " + com.getCompanycode()+ " ,company sapid: " + com.getCompanysapid()+ " ,div status: " + com.getCompanystatus()+ " ,company created date: " + com.getCompanycdatetime()+ " ,company updated date : " + com.getCompanyudatetime();
                // Writing Contacts to log
                Log.e("Companies: ", log);

            }*/

            callcrops();

        }
    }

    private class Async_getallreions extends AsyncTask<Void, Void, String> {

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
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

           /* Log.d("Reading: ", "Reading all Regions..");

            List<Regions> regions = db.getAllRegions();

            for (Regions regions1 : regions) {
                String log = "Id: "+regions1.getID()+ "Region master ID"+regions1.getRegionMasterId()+" ,Name: " + regions1.getRegionName()+ " ,region code: " + regions1.getRegioncode() + regions1.getRegionstatus();
                // Writing Contacts to log
                Log.e("Regions: ", log);

            }*/

            try {
                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo != null) {  // connected to the internet
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                        new Async_getallproducts().execute();

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        new Async_getallproducts().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }
    }

    private class Async_getallcrops extends AsyncTask<Void, Void, String> {

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
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

          /*  Log.d("Reading: ", "Reading all crops..");

            List<Crops> crops = db.getAllCrops();

            for (Crops crop : crops) {
                String log = "Id: "+crop.getID()+ "Div master ID"+crop.getCropMasterId()+" ,Name: " + crop.getCropName()+ " ,crop code: " + crop.getCropcode()+ "crop sapid: " + crop.getCropsapid() + "crop divid: " + crop.getCropdivisionId()+ " ,crop created date: " + crop.getCropcdatetime()+ " ,crop updated date : " + crop.getCropudatetime();
                // Writing Contacts to log
                Log.e("Crops: ", log);

            }
            Log.e("Crops: ", "testing crops");*/
            //new Async_getallcdc().execute();
            try {
                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo != null) {  // connected to the internet
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                        new Async_getallreions().execute();

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        new Async_getallreions().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }
        }
    }

    private class Async_getallcdc extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_COMPANY_DIVISION_CROP)
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

                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String company_id = objinfo.getString("company_id");
                        String division_id = objinfo.getString("division_id");
                        String crop_id = objinfo.getString("crop_id");


                        //Log.d("Insert: ", "Inserting Divisions ..");
                        db.addCompany_division_crops(new Company_division_crops(company_id, division_id, crop_id));


                        // do some stuff....

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

            // Log.d("Reading: ", "Reading all divisions..");

           /* List<Company_division_crops> company_division_crops = db.getAllCompany_division_crops();

            for (Company_division_crops cdc : company_division_crops) {
                String log = "Id: "+cdc.getID()+ "CompanyId"+cdc.getCdcCompanyId()+" ,DivisionId: " + cdc.getCdcDivisionId()+ " ,CropI: " + cdc.getCdcCropId();
                // Writing Contacts to log
                Log.e("company division crops", log);

            }*/
            // new Async_getallcrops().execute();

            try {
                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo != null) {  // connected to the internet
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                        new Async_getallucd().execute();

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        new Async_getallucd().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }
        }
    }

    private class Async_getallucd extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_USER_COMPANY_DIVISION + team)
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


                if (jsonData != null) {
                    try {

                        JSONArray companyarray = new JSONArray(jsonData);

                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);

                            String user_id = objinfo.getString("user_id");
                            String company_id = objinfo.getString("company_id");
                            String division_id = objinfo.getString("division_id");


                            // Log.d("Insert: ", "Inserting user company division ..");
                            db.addUser_Company_Division(new User_Company_Division(user_id, company_id, division_id));
                            // do some stuff....

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            getAllServiceOrders();
        }
    }

    private class Async_getallucc extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.URL_NSL_MAIN + Constants.URL_MASTER_USER_COMPANY_CUSTOMER, userId, team))
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

                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);
                        if (!objinfo.has("error")) {

                            String user_id = objinfo.getString("user_id");
                            String company_id = objinfo.getString("company_id");
                            String customer_id = objinfo.getString("customer_id");
                            Log.d("Insert: ", "Inserting user company customer ..");
                            db.addUser_Company_Customer(new User_Company_Customer(user_id, company_id, customer_id));
                        }

                        // do some stuff....

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // Log.e("ServiceHandler", "Couldn't get any data from the url");
                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // bar.setVisibility(View.GONE);

            // dialog.dismiss();

           /* Map<String, String> map = new HashMap<String, String>();
            map.put("name", "distributor_retailer");
            new RetrofitRequester(new RetrofitResponseListener() {
                @Override
                public void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId) {
                    // DistributorsRetailerPojo distributorsRetailerPojo=Common.getSpecificDataObject(object.get(i),DistributorsRetailerPojo.class);
                    *//*SQLiteDatabase dbWritable = db.getWritableDatabase();
                    for (int i = 0; i < object.size(); i++) {
                        DistributorsRetailerPojo distributorsRetailerPojo = Common.getSpecificDataObject(object.get(i), DistributorsRetailerPojo.class);
                        Common.Log.i("distributorsRetailerPojo" + distributorsRetailerPojo);
                        db.insertDistributorRetailers(dbWritable, distributorsRetailerPojo);


                    }
                    dbWritable.close();*//*


                }


            }).callGetApi(map);
*/

            new Async_getallevm().execute();

        }
    }

    private class Async_getallproducts extends AsyncTask<Void, Void, String> {

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


            try {
                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo != null) {  // connected to the internet
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                        new Async_getallproducts_price().execute();

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        new Async_getallproducts_price().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }
    }

    private class Async_getallproducts_price extends AsyncTask<Void, Void, String> {

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
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


            try {
                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo != null) {  // connected to the internet
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                        new Async_getallschemes().execute();

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        new Async_getallschemes().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }
    }

    private class Async_getallschemes extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_SCHEMES + userId)
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

                    JSONObject jsonObject = new JSONObject(jsonData);

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

                        for (int j = 0; j < schemeProductDetailsArray.length(); j++) {
                            JSONObject schemeProductDetailsObjinfo = schemeProductDetailsArray.getJSONObject(j);

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


            try {


                if (Common.haveInternet(getApplicationContext())) {  // connected to the internet

                    new Async_getallusers().execute();

                } else {
                    //  Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }


    }

    private class Async_getallcustomers extends AsyncTask<Void, Void, String> {

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
                    JSONObject jsonObject = new JSONObject(jsonData);
                    if (!jsonObject.has("error")) {


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
                             /*= jsonObjectData.getString("credit_balance");*/
                                String credit_balance = "0";


                                Log.d("Insert: ", "Inserting Customer details ..");
                                db.addCustomer_details(new Customer_Details(customer_id1, divsion_id, credit_limit, inside_bucket, outside_bucket, status1, created_datetime1, updated_datetime1, credit_balance));

                            }

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

            sharedpreferences.edit().putString(Constants.SharedPrefrancesKey.CURRENT_DATE, Common.getCurrentDate()).commit();
            try {
                if (Common.haveInternet(getApplicationContext())) {
                    new Async_getallcdc().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }
    }


    private class Async_getallusers extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_USERS + userId)
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
                        db.addusers(new Users(user_id, first_name, last_name, mobile_no, email, sap_id, password, role_id, reporting_manager_id, status, created_datetime, updated_datetime, designation, headquarter, location, region_id, grade, cost_center, profile_base64));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // bar.setVisibility(View.GONE);


          /*  Log.d("Reading: ", "Reading all users..");

            List<Users> users = db.getAllUsers();

            for (Users div : users) {
                String log = "Id: "+div.getID()+ "users master ID"+div.getUserMasterId()+" first name : lastname " + div.getUser_first_name();
                // Writing Contacts to log
                Log.e("Users: ", log);

            }*/

            try {
                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo != null) {  // connected to the internet
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                        new Async_getallcustomers().execute();

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        new Async_getallcustomers().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }
            ;

        }
    }

    private class Async_getPaymentCollection extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.GET_PAYMENT_COLLECTION_DETAILS + team)
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

                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        if (!objinfo.has("error")) {

                            String payment_id = objinfo.getString("payment_id");
                            String sqlite_id = objinfo.getString("sqlite_id");
                            String payment_type = objinfo.getString("payment_type");
                            String user_id = objinfo.getString("user_id");
                            String company_id = objinfo.getString("company_id");
                            String division_id = objinfo.getString("division_id");
                            String customer_id = objinfo.getString("customer_id");
                            String total_amount = objinfo.getString("total_amount");
                            String payment_mode = objinfo.getString("payment_mode");
                            String bank_name = objinfo.getString("bank_name");
                            String rtgs_or_neft_no = objinfo.getString("rtgs_or_neft_no");
                            String payment_datetime = objinfo.getString("payment_datetime");
                            String date_on_cheque_no = objinfo.getString("date_on_cheque_no");
                            String cheque_no_dd_no = objinfo.getString("cheque_no_dd_no");
                            String status = objinfo.getString("status");
                            String created_by = objinfo.getString("created_by");
                            String created_datetime = objinfo.getString("created_datetime");
                            String updated_datetime = objinfo.getString("updated_datetime");
                            if (status.equalsIgnoreCase(null) || status.equalsIgnoreCase("null") || status.equalsIgnoreCase("")) {
                                status = "0";
                            }

                            Log.d("Insert: ", "Inserting Customer details ..");
                            db.addPaymentCollection(new Payment_collection(payment_id, payment_type, user_id, company_id, division_id, customer_id, total_amount, payment_mode, bank_name, rtgs_or_neft_no, payment_datetime, date_on_cheque_no, cheque_no_dd_no, Integer.parseInt(status), created_datetime, updated_datetime, payment_id));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

            new Async_getAllGrade().execute();
        }
    }


    public void getAllServiceOrders() {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo != null) {  // connected to the internet
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    new Async_getallserviceorders().execute();

                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    new Async_getallserviceorders().execute();
                }
            } else {

            }

        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }

    }


    public class Async_getallserviceorders extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        private SQLiteDatabase sdbw;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(LoginActivity.this);
//            progressDialog.setMessage("Please Wait ...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.URL_MASTER_SERVICE_ORDER, userId, team))
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
                    JSONObject resultobject = new JSONObject(jsonData);

                    if (!resultobject.has("error")) {

                        JSONArray adavancebooking = resultobject.getJSONArray("adavancebooking");

                        for (int n = 0; n < adavancebooking.length(); n++) {


                            JSONObject result_service_order = adavancebooking.getJSONObject(n);
                            JSONObject service_order = result_service_order.getJSONObject("service_order");
                            if (!result_service_order.has("service_order_details")) {
                                continue;
                            }

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
                                String orderidfromserviceorder = null;
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

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    }
                });

            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }


            try {
                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo != null) {  // connected to the internet
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                        new Async_getallucc().execute();

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        new Async_getallucc().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }


        }
    }


    @Override
    public void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId) {
        //VersionControlListVo versionControlListVo=Common.getSpecificDataObject(object, VersionControlListVo.class);
        List<VersionControlVo> versionControlVoList = new ArrayList<>();


        for (Object objectt : object) {

            VersionControlVo versionControlListVo = Common.getSpecificDataObject(objectt, VersionControlVo.class);
            versionControlVoList.add(versionControlListVo);


        }


        if (db.getVersionControlList().size() == 0) {

            db.insertVersionControlData(versionControlVoList);

        } else {
            tableMustInsertList = new ArrayList<>();
            List<VersionControlVo> dbVersionControl = db.getVersionControlList();
            for (VersionControlVo versionControlVo : versionControlVoList) {

                for (VersionControlVo dbVersionCode : dbVersionControl) {

                    if (versionControlVo.tableName.equalsIgnoreCase(dbVersionCode.tableName) && !versionControlVo.status.equalsIgnoreCase(dbVersionCode.status)) {
                        /*if (versionControlVo.tableName.contains("service_order") || versionControlVo.tableName.contains("service_order_details")) {
                            db.deleteDataByTableName("service_order");
                            db.deleteDataByTableName("service_order_details");
                        } else {
                            db.deleteDataByTableName(versionControlVo.tableName);
                        }

*/
                        tableMustInsertList.add(versionControlVo.tableName);


                    }

                }
            }
        }

//if (tableMustInsertList.contains("customers") || tableMustInsertList.contains("customer_details")){


        new Async_getallcustomers().execute();
//}
    }


    private class Async_getallevm extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Please wait ");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.URL_NSL_MAIN + Constants.URL_EMPLOYEE_VISIT_MANAGEMENT, userId, team))
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

                    JSONArray companyarray = new JSONArray(jsonData);
                    int mobile, geo_tracking_id;
                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);
                        if (!objinfo.has("error")) {


                            String emp_visit_id = objinfo.getString("emp_visit_id");
                            int type = objinfo.getInt("type");

                            int user_id = objinfo.getInt("user_id");
                            int customer_id = objinfo.getInt("customer_id");
                            String visit_plan_type = objinfo.getString("visit_plan_type");
                            String purpose_visit = objinfo.getString("purpose_visit");
                            String plan_date_time = objinfo.getString("plan_date_time");
                            String concern_person_name = objinfo.getString("concern_person_name");
                            if (objinfo.getString("geo_tracking_id").equalsIgnoreCase("null") || objinfo.getString("geo_tracking_id").equalsIgnoreCase("")) {
                                geo_tracking_id = 0;
                            } else {
                                geo_tracking_id = objinfo.getInt("geo_tracking_id");

                            }
                            if (objinfo.getString("mobile").equalsIgnoreCase("null") || objinfo.getString("mobile").equalsIgnoreCase("")) {
                                mobile = 0;
                            } else {
                                mobile = objinfo.getInt("mobile");
                            }
                            String village = objinfo.getString("village");
                            String location_address = objinfo.getString("location_address");
                            String field_area = objinfo.getString("field_area");
                            String check_in_time = objinfo.getString("check_in_time");
                            String comments = objinfo.getString("comments");

                            int status = objinfo.getInt("status");


                            String event_name = objinfo.getString("event_name");
                            String event_end_date = objinfo.getString("event_end_date");
                            String event_purpose = objinfo.getString("event_purpose");
                            String event_venue = objinfo.getString("event_venue");
                            String event_participants = objinfo.getString("event_participants");
                            String created_by = objinfo.getString("created_by");
                            String updated_by = objinfo.getString("updated_by");


                            String created_datetime = objinfo.getString("created_datetime");
                            String update_datetime = objinfo.getString("update_datetime");
                            int approval_status = objinfo.getInt("approval_status");
                            purpose_visit = purpose_visit == "" ? "1" : purpose_visit;


                            String selectQuery = "SELECT * FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " WHERE " + KEY_EMP_FFM_ID + " = '" + emp_visit_id + "'";
                            String purpose_visit_titles = Common.getPlannerTitleById(purpose_visit);
                            SQLiteDatabase sdbw = db.getWritableDatabase();
                            Cursor cc = sdbw.rawQuery(selectQuery, null);
                            cc.getCount();
                            // looping through all rows and adding to list
                            if (cc.getCount() == 0) {
                                //doesn't exists therefore insert record.
                                db.addEVM(new Employe_visit_management_pojo(emp_visit_id, type, geo_tracking_id, user_id, customer_id,
                                        visit_plan_type, purpose_visit_titles, String.valueOf(purpose_visit), plan_date_time, concern_person_name, mobile, village, location_address, field_area, check_in_time,
                                        comments, status, approval_status, event_name, event_end_date, event_purpose, event_venue, event_participants, created_by, updated_by, created_datetime, update_datetime
                                        , Integer.parseInt(emp_visit_id)));
                            }

                            Log.d("Insert: ", "Inserting Employee visit management");
                       /* db.addEVM(new Employe_visit_management_pojo(emp_visit_id, type, geo_tracking_id, user_id, customer_id,
                                visit_plan_type, purpose_visit, plan_date_time, concern_person_name, mobile, village, location_address, field_area, check_in_time,
                                comments, status, approval_status, event_name, event_end_date, event_purpose, event_venue, event_participants, created_by, updated_by, created_datetime, update_datetime
                                , 0));*/

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
//                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            new Async_getPaymentCollection().execute();


        }
    }


    private class Async_getAllGrade extends AsyncTask<Void, Void, String> {

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

                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);
                        if (!objinfo.has("error")) {


                            String grade_id = objinfo.getString("grade_id");
                            String grade_name = objinfo.getString("grade_name");
                            String price_per_km = objinfo.getString("price_per_km");
                            GradePojo gradePojo = new GradePojo();
                            gradePojo.gradeId = grade_id;
                            gradePojo.gradeName = grade_name;
                            gradePojo.pricePerKm = price_per_km;

                            Log.d("Insert: ", "Inserting Grades ..");
                            db.insertGrade(db.getReadableDatabase(), gradePojo);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (Common.haveInternet(LoginActivity.this)) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Divisions", "true");
                editor.commit();
            }

            new Async_getallFeedback().execute();
            //  Intent main = new Intent(LoginActivity.this, MainActivity.class);
            //  startActivity(main);
            // finish();
        }
    }

    public class Async_getallFeedback extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_FEEDBACK + "&user_id=" + userId)
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


                if (jsonData != null) {
                    try {

                        JSONArray companyarray = new JSONArray(jsonData);
                        System.out.println(companyarray.length());
                        Log.e("Length feedback", String.valueOf(companyarray.length()));

                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);
                            if (!objinfo.has("error")) {

                                Feedback fb = new Feedback();
                                fb._feedback_id = Integer.parseInt(objinfo.getString("feedback_id"));
                                fb._user_id = Integer.parseInt(objinfo.getString("user_id"));
                                fb._farmer_name = objinfo.getString("farmer_name");
                                fb._place = objinfo.getString("place");
                                fb._contact_no = objinfo.getString("contact_no");
                                fb._crop = objinfo.getString("crop_id");
                                fb._hybrid = objinfo.getString("hybrid");
                                fb._sowing_date = objinfo.getString("sowing_date");
                                fb._feedback_message = objinfo.getString("feedback_message");
                                fb._image = objinfo.getString("image");
                                fb._ffmid = objinfo.getString("feedback_id");
                                fb.image_64 = objinfo.getString("image_64");


                                Log.d("Insert: ", "Inserting Feedback ..");
                                db.addFeedback(new Feedback(fb._feedback_id, fb._user_id, fb._farmer_name, fb._place, fb._contact_no, fb._crop, fb._hybrid, fb._sowing_date, fb._feedback_message,
                                        fb.image_64, fb._ffmid));


                                Log.e("Inserted!!!!", "Inserted to sqlite");
                                // do some stuff....

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    //  db.deleteFeedback();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Common.dismissDialog(dataSyncingDialog);

            new Async_getallComplaints().execute();

        }

    }

    public class Async_getallComplaints extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_COMPLAINTS + "&user_id=" + userId)
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

                if (jsonData != null) {
                    try {

                        JSONArray companyarray = new JSONArray(jsonData);
                        System.out.println(companyarray.length());
                        Log.e("Length complaint", String.valueOf(companyarray.length()));

                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);
                            if (!objinfo.has("error")) {


                                Complaints cmpreg = new Complaints();
                                cmpreg._complaint_id = Integer.parseInt(objinfo.getString("complaint_id"));
                                cmpreg._user_id = Integer.parseInt(objinfo.getString("user_id"));
                                cmpreg._company_id = Integer.parseInt(objinfo.getString("company_id"));
                                cmpreg._division_id = Integer.parseInt(objinfo.getString("division_id"));
                                cmpreg._crop_id = Integer.parseInt(objinfo.getString("crop_id"));
                                cmpreg._product_id = Integer.parseInt(objinfo.getString("product_id"));
                                cmpreg._marketing_lot_number = objinfo.getString("marketing_lot_number");
                                cmpreg._complaint_type = objinfo.getString("complaint_type");
                                cmpreg._farmer_name = objinfo.getString("farmer_name");
                                cmpreg._contact_no = objinfo.getString("contact_no");
                                cmpreg._complaint_area_acres = objinfo.getString("complaint_area_acres");
                                cmpreg._soil_type = objinfo.getString("soil_type");
                                cmpreg._others = objinfo.getString("others");
                                cmpreg._purchased_quantity = objinfo.getString("purchased_quantity");
                                cmpreg._complaint_quantity = objinfo.getString("complaint_quantity");
                                cmpreg._purchase_date = objinfo.getString("purchase_date");
                                cmpreg._bill_number = objinfo.getString("bill_number");
                                cmpreg._retailer_name = objinfo.getString("retailer_name");
                                cmpreg._distributor = Integer.parseInt(objinfo.getString("distributor"));
                                cmpreg._mandal = objinfo.getString("mandal");
                                cmpreg._village = objinfo.getString("village");
                                cmpreg._image = objinfo.getString("image_64");
                                cmpreg._regulatory_type = objinfo.getString("regulatory_type");
                                cmpreg._sampling_date = objinfo.getString("sampling_date");
                                cmpreg._place_sampling = objinfo.getString("place_sampling");
                                cmpreg._sampling_officer_name = objinfo.getString("sampling_officer_name");
                                cmpreg._sampling_officer_contact = objinfo.getString("sampling_officer_contact");
                                cmpreg._comments = objinfo.getString("comments");
                                cmpreg._status = Integer.parseInt(objinfo.getString("status"));
                                cmpreg._remarks = objinfo.getString("remarks");
                                cmpreg._created_datetime = objinfo.getString("created_datetime");
                                cmpreg._updated_datetime = objinfo.getString("updated_datetime");
                                cmpreg._ffmid = objinfo.getString("complaint_id");


                                Log.d("Insert: ", "Inserting complaints ..");
                                db.addComplaint(new Complaints(cmpreg._user_id, cmpreg._company_id, cmpreg._division_id, cmpreg._crop_id, cmpreg._product_id, cmpreg._marketing_lot_number, cmpreg._complaint_type, cmpreg._farmer_name,
                                        cmpreg._contact_no, cmpreg._complaint_area_acres, cmpreg._soil_type, cmpreg._others, cmpreg._purchased_quantity, cmpreg._complaint_quantity, cmpreg._purchase_date, cmpreg._bill_number,
                                        cmpreg._retailer_name, cmpreg._distributor, cmpreg._mandal, cmpreg._village, cmpreg._image, cmpreg._regulatory_type, cmpreg._sampling_date, cmpreg._place_sampling, cmpreg._sampling_officer_name,
                                        cmpreg._sampling_officer_contact, cmpreg._comments, cmpreg._status, cmpreg._remarks, cmpreg._created_datetime, cmpreg._updated_datetime, cmpreg._ffmid));


                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    // db.deleteComplaints();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new Async_getAlldistributorRetailers().execute();
           /* Common.dismissDialog(dataSyncingDialog);
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main);
            finish();*/


        }

    }


    public class Async_getAlldistributorRetailers extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.GET_DISTRIBUTOR_RETAILERS + team)
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

                if (jsonData != null) {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonData);
                        if (!jsonObject.has("error")) {


                            JSONObject companyarray = jsonObject.getJSONObject("distributor_information");
                            System.out.println(companyarray.length());
                            Log.e("Length complaint", String.valueOf(companyarray.length()));
                            //   SQLiteDatabase dbWritable = db.getWritableDatabase();


                            JSONArray retailer_detailsArray = companyarray.getJSONArray("retailer_details");

                            for (int j = 0; j < retailer_detailsArray.length(); j++) {
                                JSONObject retailer_detailsObject = retailer_detailsArray.getJSONObject(j);

                                String ffm_id = retailer_detailsObject.getString("ffm_id");
                                String retailer_name = retailer_detailsObject.getString("retailer_name");
                                String retailer_tin_no = retailer_detailsObject.getString("retailer_tin_no");
                                String address = retailer_detailsObject.getString("address");
                                String mobile = retailer_detailsObject.getString("mobile");
                                String phone = retailer_detailsObject.getString("phone");
                                String email_id = retailer_detailsObject.getString("email_id");
                                db.addRetailers(new Retailer(ffm_id, retailer_name, retailer_tin_no, address, phone, mobile, email_id, null, null,
                                        null, null, null, ffm_id));
                            }

                            JSONArray distributor_retailersArray = companyarray.getJSONArray("distributor_retailers");
                            for (int i = 0; i < distributor_retailersArray.length(); i++) {
                                JSONObject distributor_retailersObject = distributor_retailersArray.getJSONObject(i);
                                DistributorsRetailerPojo distributorsRetailerPojo = new DistributorsRetailerPojo();
                                distributorsRetailerPojo.distributorId = distributor_retailersObject.getString("distributor_id");
                                distributorsRetailerPojo.retailerId = String.valueOf(db.getSqlPrimaryKeyByFFMID(distributor_retailersObject.getString("retailer_id")));
                                Common.Log.i("distributorsRetailerPojo" + distributorsRetailerPojo);
                                db.insertDistributorRetailers(distributorsRetailerPojo);
                            }

                        }


                        // dbWritable.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    // db.deleteComplaints();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new Async_getAllStockMovement().execute();


        }

    }


    public class Async_getAllStockMovement extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.GET_STOCKMOVEMENT_LIST + team)
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

                if (jsonData != null) {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonData);
                        if (!jsonObject.has("error")) {
                            db.deleteDataByTableName(new String[]{"stock_movement", "stock_movement_detail", "stock_movement_retailer_details"});

                            JSONArray retailer_detailsArray = jsonObject.getJSONArray("stock_movement_data");

                            for (int j = 0; j < retailer_detailsArray.length(); j++) {
                                JSONObject retailer_detailsObject = retailer_detailsArray.getJSONObject(j);
                                JSONObject stock_movement_object = retailer_detailsObject.getJSONObject("stock_movement");

                                StockMovementPoJo stockMovementPoJo = new StockMovementPoJo();
                                stockMovementPoJo.ffmId = stock_movement_object.isNull("stock_movement_id") ? 0 : stock_movement_object.getInt("stock_movement_id");
                                stockMovementPoJo.movementType = stock_movement_object.isNull("movement_type") ? 0 : stock_movement_object.getInt("movement_type");
                                stockMovementPoJo.status = stock_movement_object.isNull("status") ? 0 : stock_movement_object.getInt("status");
                                stockMovementPoJo.divisionId = stock_movement_object.isNull("division_id") ? 0 : stock_movement_object.getInt("division_id");
                                stockMovementPoJo.companyId = stock_movement_object.isNull("company_id") ? 0 : stock_movement_object.getInt("company_id");
                                stockMovementPoJo.customerId = stock_movement_object.isNull("customer_id") ? 0 : stock_movement_object.getInt("customer_id");
                                stockMovementPoJo.updatedDatetime = stock_movement_object.getString("updated_datetime");
                                stockMovementPoJo.createdDatetime = stock_movement_object.getString("created_datetime");
                                stockMovementPoJo.createdBy = stock_movement_object.getString("created_by");
                                stockMovementPoJo.updatedBy = stock_movement_object.getString("updated_by");
                                stockMovementPoJo.userId = stock_movement_object.isNull("user_id") ? 0 : stock_movement_object.getInt("user_id");

                                if (retailer_detailsObject.has("stock_movement_details")) {
                                    JSONArray stock_movementdetails_array = retailer_detailsObject.getJSONArray("stock_movement_details");

                                    for (int k = 0; k < stock_movementdetails_array.length(); k++) {
                                        JSONObject jsonObjectDeatils = stock_movementdetails_array.getJSONObject(k);


                                        StockMovementDetailsPojo stockMovementDetailsPojo = new StockMovementDetailsPojo();
                                        stockMovementDetailsPojo.ffmId = jsonObjectDeatils.getInt("stock_movement_detail_id");
                                        stockMovementDetailsPojo.cropId = jsonObjectDeatils.getInt("crop_id");
                                        stockMovementDetailsPojo.customerId = jsonObjectDeatils.getInt("customer_id");
                                        stockMovementDetailsPojo.stockPlaced = jsonObjectDeatils.getString("stock_placed");
                                        stockMovementDetailsPojo.currentStock = jsonObjectDeatils.getString("current_stock");
                                        stockMovementDetailsPojo.updatedDatetime = jsonObjectDeatils.getString("updated_datetime");
                                        stockMovementDetailsPojo.createdDatetime = jsonObjectDeatils.getString("created_datetime");
                                        stockMovementDetailsPojo.createdBy = jsonObjectDeatils.getString("created_by");
                                        stockMovementDetailsPojo.updatedBy = jsonObjectDeatils.getString("updated_by");
                                        stockMovementDetailsPojo.userType = jsonObjectDeatils.getString("user_type");
                                        stockMovementDetailsPojo.placedDate = jsonObjectDeatils.getString("placed_date");
                                        stockMovementDetailsPojo.productId = jsonObjectDeatils.getInt("product_id");
                                        stockMovementDetailsPojo.stockMovementId = jsonObjectDeatils.getInt("stock_movement_id");


                                        db.insertStackMovement(stockMovementPoJo, stockMovementDetailsPojo);

                                    }
                                }
                                if (retailer_detailsObject.has("stock_movement_retailer_details")) {
                                    JSONArray stock_movementRetailerdetails_array = retailer_detailsObject.getJSONArray("stock_movement_retailer_details");
                                    for (int l = 0; l < stock_movementRetailerdetails_array.length(); l++) {
                                        JSONObject jsonObjectDeatils = stock_movementRetailerdetails_array.getJSONObject(l);


                                        StockMovementRetailerDetails stockMovementDetailsPojo = new StockMovementRetailerDetails();
                                        stockMovementDetailsPojo.cropId = jsonObjectDeatils.getString("crop_id");
                                        stockMovementDetailsPojo.ffmId = jsonObjectDeatils.getInt("stock_movement_retailer_id");
                                        stockMovementDetailsPojo.stockPlaced = jsonObjectDeatils.getString("stock_placed");
                                        stockMovementDetailsPojo.currentStock = jsonObjectDeatils.getString("current_stock");
                                        stockMovementDetailsPojo.updatedDatetime = jsonObjectDeatils.getString("updated_datetime");
                                        stockMovementDetailsPojo.createdDatetime = jsonObjectDeatils.getString("created_datetime");
                                        stockMovementDetailsPojo.createdBy = jsonObjectDeatils.getString("created_by");
                                        stockMovementDetailsPojo.userType = jsonObjectDeatils.getString("user_type");
                                        stockMovementDetailsPojo.placedDate = jsonObjectDeatils.getString("placed_date");
                                        stockMovementDetailsPojo.productId = jsonObjectDeatils.getString("product_id");
                                        stockMovementDetailsPojo.stockMovementId = jsonObjectDeatils.getString("stock_movement_id");
                                        stockMovementDetailsPojo.retailerId = jsonObjectDeatils.getString("retailer_id");
                                        stockMovementDetailsPojo.verified = jsonObjectDeatils.getString("verified");
                                        stockMovementDetailsPojo.verifiedBy = jsonObjectDeatils.getString("verified_by");

                                        db.insertStackMovementForRetailerDetails(stockMovementPoJo, stockMovementDetailsPojo);


                                    }

                                }

                            }


                        }


                        // dbWritable.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    // db.deleteComplaints();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new Async_getAllStockReturns().execute();


        }

    }


    public class Async_getAllStockReturns extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.GET_STOCKRETURNS_LIST + team)
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

                if (jsonData != null) {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonData);
                        if (!jsonObject.has("error")) {


                            JSONArray retailer_detailsArray = jsonObject.getJSONArray("stock_return_data");

                            for (int j = 0; j < retailer_detailsArray.length(); j++) {
                                JSONObject retailer_detailsObject = retailer_detailsArray.getJSONObject(j);
                                JSONObject stock_movement_object = retailer_detailsObject.getJSONObject("stock_returns");


                                StockReturnPoJo stockMovementPoJo = new StockReturnPoJo();
                                stockMovementPoJo.ffmId = stock_movement_object.isNull("stock_returns_id") ? 0 : stock_movement_object.getInt("stock_returns_id");
                                stockMovementPoJo.divisionId = stock_movement_object.isNull("division_id") ? 0 : stock_movement_object.getInt("division_id");
                                stockMovementPoJo.companyId = stock_movement_object.isNull("company_id") ? 0 : stock_movement_object.getInt("company_id");
                                stockMovementPoJo.customerId = stock_movement_object.isNull("customer_id") ? 0 : stock_movement_object.getInt("customer_id");
                                stockMovementPoJo.updatedDatetime = stock_movement_object.getString("updated_datetime");
                                stockMovementPoJo.createdDatetime = stock_movement_object.getString("created_datetime");
                                stockMovementPoJo.createdBy = stock_movement_object.getString("created_by");
                                stockMovementPoJo.updatedBy = stock_movement_object.getString("updated_by");
                                stockMovementPoJo.userId = stock_movement_object.isNull("user_id") ? 0 : stock_movement_object.getInt("user_id");


                                JSONArray stock_movementdetails_array = retailer_detailsObject.getJSONArray("stock_returns_details");
                                for (int k = 0; k < stock_movementdetails_array.length(); k++) {
                                    JSONObject jsonObjectDeatils = stock_movementdetails_array.getJSONObject(k);


                                    StockReturnDetailsPoJo stockMovementDetailsPojo = new StockReturnDetailsPoJo();
                                    stockMovementDetailsPojo.ffmId = jsonObjectDeatils.getInt("stock_returns_details_id");
                                    stockMovementDetailsPojo.stockReturnId = jsonObjectDeatils.getInt("stock_returns_id");
                                    stockMovementDetailsPojo.cropId = jsonObjectDeatils.getInt("crop_id");
                                    stockMovementDetailsPojo.productId = jsonObjectDeatils.getInt("product_id");
                                    stockMovementDetailsPojo.quantity = jsonObjectDeatils.getString("quantity");


                                    db.insertStackReturn(stockMovementPoJo, stockMovementDetailsPojo);


                                }


                            }

                        }
                        // dbWritable.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    // db.deleteComplaints();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Common.dismissDialog(dataSyncingDialog);
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main);
            finish();


        }

    }

}
