package com.nsl.app.retailers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nsl.app.CapitalizeFirstLetter;
import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;
import com.nsl.app.commonutils.Common;
import com.nsl.app.pojo.DistributorsRetailerPojo;
import com.nsl.app.pojo.RetailersNamePojo;
import com.squareup.okhttp.FormEncodingBuilder;
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

import static com.nsl.app.DatabaseHandler.KEY_TABLE_RETAILER_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_RETAILER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_RETAILER;


/**
 * Created by admin on 2/22/2017.
 */
public class FragmentRetailer extends Fragment {
    DatabaseHandler db;
    Button submit, cancel;
    ProgressDialog progressDialog;
    private static SQLiteDatabase sql, sdbr, sdbw;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    EditText mobile, retailer_name, tinno, address, phone, email;
    String jsonData, sqliteid, ffmid, checkuid;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private EditText ettinno1;
    private Button btnGo;
    DistributorsRetailerPojo distributorsRetailerPojo = new DistributorsRetailerPojo();
    private ProgressDialog progressDialog1;
    private String company_id;
    private String division_id;
    private String crop_id;
    private String customer_id;
    private Activity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());

       /* FragmentFeedback fb = new FragmentFeedback();
        fb.checkConnection();*/
        View view = inflater.inflate(R.layout.fragment_retailer, container, false);
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        retailer_name = (EditText) view.findViewById(R.id.etRetailerName);
        tinno = (EditText) view.findViewById(R.id.ettinno);
        mobile = (EditText) view.findViewById(R.id.etMobile);
        email = (EditText) view.findViewById(R.id.etemail);
        phone = (EditText) view.findViewById(R.id.etphone);
        address = (EditText) view.findViewById(R.id.etaddress);
        submit = (Button) view.findViewById(R.id.btnSubmit);
        cancel = (Button) view.findViewById(R.id.btncancel);
        ettinno1 = (EditText) view.findViewById(R.id.ettinno1);
        btnGo = (Button) view.findViewById(R.id.btnGo);

        company_id = getArguments().getString("company_id");
        division_id = getArguments().getString("division_id");
        crop_id = getArguments().getString("crop_id");
        customer_id = getArguments().getString("customer_id");
        CapitalizeFirstLetter capital = new CapitalizeFirstLetter();
        retailer_name.addTextChangedListener(capital.capitalise(retailer_name));
        address.addTextChangedListener(capital.capitalise(address));

        btnGo.setOnClickListener(new View.OnClickListener() {
            public List<RetailersNamePojo> retailersNamePojos;

            @Override
            public void onClick(View v) {
                if (ettinno1.getText().toString().trim().length() > 0) {
                    retailersNamePojos = db.searchTinNoIsAvailable(ettinno1.getText().toString().trim());
                    if (retailersNamePojos.size() != 0) {
                        if (Common.haveInternet(getContext())) {

                            tinno.setText(retailersNamePojos.get(0).retailerTinNo);
                            retailer_name.setText(retailersNamePojos.get(0).retailerName);
                            mobile.setText(retailersNamePojos.get(0).mobileNo);
                            email.setText(retailersNamePojos.get(0).email);
                            phone.setText(retailersNamePojos.get(0).phoneNo);
                            address.setText(retailersNamePojos.get(0).address);

                            distributorsRetailerPojo.distributorId = customer_id;
                            distributorsRetailerPojo.retailerId = retailersNamePojos.get(0).sqliteId;
                            if (db.isDistributorRetailerMapped(retailersNamePojos.get(0).sqliteId,customer_id)){
                                Toast.makeText(getActivity(), "Retailer already mapped with this distributor", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            progressDialog1 = Common.showProgressDialog(getActivity());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    preparePlannerOfflineDataAndPush(retailersNamePojos.get(0).sqliteId, customer_id, retailersNamePojos.get(0).sqliteId);
                                }

                            }).start();
                        }else {
                            Toast.makeText(getActivity(), Common.INTERNET_UNABLEABLE, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Tin No is not available please enter following details", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void validate() {
        Handler handler=Common.disableClickEvent(submit,true);
        String fid = "1";
        String retailer = retailer_name.getText().toString();
        String mobileno = mobile.getText().toString();
        String phn = phone.getText().toString();
        String addr = address.getText().toString();
        String eml = email.getText().toString().trim();
        String tin = tinno.getText().toString();

        if (TextUtils.isEmpty(retailer) || retailer.length() > 0 && retailer.startsWith(" ")) {
            retailer_name.setError("Please enter Retailer name");
            retailer_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(tin) || tin.length() > 0 && tin.startsWith(" ")) {
            tinno.setError("Please enter tin number");
            tinno.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mobileno) || (mobileno.length() > 0 && mobileno.startsWith(" "))) {
            mobile.setError("Please enter mobile number");
            mobile.requestFocus();
            return;
        }
        if (mobileno.length() > 0 && mobileno.length() < 10 || mobileno.startsWith(" ") || !Common.isValidMobileNumber(mobileno)) {
            mobile.setError("Please enter valid mobile number");
            mobile.requestFocus();
            return;
        }


        if (phn.length() < 6 || phn.length() > 13) {
            phone.setError("Please enter phone number");
            phone.requestFocus();
            return;
        }


        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

// onClick of button perform this simplest code.
        if (!eml.matches(emailPattern)) {
            email.setError("Please enter valid email");
            email.requestFocus();
            return;
        }
/*
        if (TextUtils.isEmpty(eml) || eml.length() > 0 && eml.startsWith(" ")) {
            email.setError("Please enter email");
            email.requestFocus();
            return;
        }*/
        if (TextUtils.isEmpty(addr) || addr.length() > 0 && addr.startsWith(" ")) {
            address.setError("Please enter address");
            address.requestFocus();
            return;
        } else {
            //  Toast.makeText(getActivity(), "Inserting...", Toast.LENGTH_SHORT).show();
            int fdid = Integer.parseInt(fid);

            List<RetailersNamePojo> retailersNamePojos = db.searchTinNoIsAvailable(tinno.getText().toString().trim());

            if (retailersNamePojos.size()!=0){
                Toast.makeText(getActivity(), "Tin No is already exist please map this TIN No.", Toast.LENGTH_SHORT).show();
                return;

            }
            Common.disableClickEvent(submit,handler);
            db.addRetailers(new Retailer(String.valueOf(fdid), retailer, tin, addr, phn, mobileno, eml, checkuid, null, null, null, null, null));
            Log.e("retailer name ", retailer + ":" + tin + ":" + mobileno);
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put("Id", fid);
            map.put("retailer_name", retailer);
            map.put("tin", tin);
            map.put("mobile_no", mobileno);
            map.put("phn", phn);
            map.put("addr", addr);
            map.put("eml", eml);
            map.put("distid", checkuid);
            map.put("sap", null);
            map.put("status", null);

            favouriteItem.add(map);
            // do some stuff....

        }
        insertToService();
    }

    private void insertToService() {
        db = new DatabaseHandler(getActivity());
        Log.d("Reading: ", "Reading all retailers..");


        List<Retailer> feedback = db.getAllnullRetailer(checkuid);

        Log.e("list size", String.valueOf(feedback.size()));

        if (feedback.size() == 0) {
            Log.e("no data found", "no data");
        } else {
            for (Retailer cus : feedback) {
                String log = "Id: " + cus.getID() + " \n master ID" + cus.get_ret_masterid() + " , \n name: " + cus.get_ret_name() + "\n  code " + cus.get_ret_tin_no() + " \n Address : " + cus.get_ret_address() + " \n phn : " + cus.get_ret_phone() + "\n mobile " + cus.get_ret_mobile() + ":" + cus.get_email() + " \n C dist id" + cus.get_ret_dist_id() + " \n sap" + cus.get_ret_dist_sap_code() + " \n status" + cus.get_ret_status() + " \n cdate" + cus.get_ret_cdatetime() + " \n udate" + cus.get_ret_udatetime() + " \n ffmid" + cus.get_ffmid();
                // Writing Contacts to log
                Log.e("retailer: ", log);

                if (Common.haveInternet(getContext())) {
                    new Async_InsertRetailer().execute((String.valueOf(cus.getID())), cus.get_ret_name(), cus.get_ret_tin_no(), cus.get_ret_address(), cus.get_ret_phone(), cus.get_ret_mobile(), cus.get_email(), cus.get_ret_dist_id(), cus.get_ret_dist_sap_code(), cus.get_ret_status());
                } else {
                    getActivity().finish();
                }
                //  }

            }

        }

    }


    public class Async_InsertRetailer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
                        .add("mobile_id", params[0])
                        .add("retailer_name", params[1])
                        .add("retailer_tin_no", params[2])
                        .add("address", params[3])
                        .add("phone", params[4])
                        .add("mobile", params[5])
                        .add("email_id", params[6])
                        .add("distributor_id", params[7])
                        .add("customer_id", checkuid)
                        .build();

                Response responses = null;

                System.out.println("---- retailer data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_RETAILER)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertRetailer" + jsonData);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing())
                progressDialog.dismiss();


            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    System.out.println("!!!!!!!1 postexecuteInsertRetailer" + jsonData);

                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        //  Toast.makeText(getActivity(), "Feed back inserted sucessfully", Toast.LENGTH_SHORT).show();
                        sqliteid = jsonobject.getString("sqlite");
                        ffmid = jsonobject.getString("ffm_id");

                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", sqliteid);
                        Log.e("ffmid", ffmid);
                        sdbw = db.getWritableDatabase();
                        // updateFeedback(Feedback feedback);
                        String updatequery = "UPDATE " + TABLE_RETAILER + " SET " + KEY_TABLE_RETAILER_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_RETAILER_ID + " = " + sqliteid;
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

            activity.finish();

        }
    }


    private void preparePlannerOfflineDataAndPush(String retailerId, String distributorId, String sqliteId) {


        Log.d("Reading: ", "Reading all payment collection..");


        String jsonData;
        OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
        RequestBody formBody = new FormEncodingBuilder()
                .add("table", "distributor_retailer")
                .add("mobile_id", sqliteId)
                .add("distributor_id", distributorId)
                .add("retailer_id", retailerId)


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
                        SQLiteDatabase dbWritableDatabase = db.getWritableDatabase();
                        db.insertDistributorRetailers(dbWritableDatabase, distributorsRetailerPojo);
                        dbWritableDatabase.close();

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
        Common.dismissProgressDialog(progressDialog1);

    }

}

