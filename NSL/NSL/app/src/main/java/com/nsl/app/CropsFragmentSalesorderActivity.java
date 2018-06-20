package com.nsl.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsl.app.commonutils.Common;
import com.nsl.app.orderindent.NewSalesOrderChooseActivity;
import com.nsl.app.orderindent.ViewSalesOrderCompanyActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_CREDIT_BALANCE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMER_DETAILS;
import static com.nsl.app.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDERDETAILS;
import static com.nsl.app.orderindent.FragmentOrderIndent.REQUEST_TYPE;
import static com.nsl.app.advancebooking.CropsFragmentAdvancebookingActivity.toJson;

public class CropsFragmentSalesorderActivity extends AppCompatActivity implements OnAmountChangeListener {


    AlertDialog.Builder alert;
    private boolean isDialogVisible;
    private String sqliteid, ffmid;
    int i = 0;
    int sum_osa = 0;
    double creditlimit;
    String aging1 = null, aging2 = null;
    private Toolbar toolbar;
    private TextView txt_validation;
    private String advqty;
    private String company_id,customer_id,division_id;
    private String tokenAount;
    private String advanceBookingAount;
    private String mobile_serivce_details_id;

    @Override
    public void onAmountChanged() {
        float total_amount = 0;
        for (int i = 0; i < CropsFragmentSalesorderActivity.globalGroup.size(); i++) {
            List<Child> childList = CropsFragmentSalesorderActivity.globalGroup.get(i).childList;
            for (int j = 0; j < childList.size(); j++) {
                if (childList.get(j).amount.length() > 0) {
                    total_amount = total_amount + Float.parseFloat(childList.get(j).amount);

                }
            }
        }
        String outstandingamount = et_outstandingamount.getText().toString();
        String creditlimit = et_creditlimit.getText().toString();

        if(tokenAount!=null)
        total_amount = Float.parseFloat(tokenAount)+ total_amount;

        if (outstandingamount.length() == 0) {
            showAlert("Please provide outstanding amount.");
            return;
        } else if (creditlimit.length() == 0) {
            showAlert("Please provide credit limit amount.");
            return;
        }
        double available_amount = Float.parseFloat(creditlimit) - Float.parseFloat(outstandingamount);
        if (available_amount < total_amount) {
            txt_validation.setVisibility(View.VISIBLE);
            et_booked_amount.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        } else {
            txt_validation.setVisibility(View.GONE);
            et_booked_amount.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        }

        et_booked_amount.setText("" +Math.round(total_amount*100)/100);
    }

    @Override
    public void beforeAmountChanged(EditText amountText, String quantity) {

    }

    public static class Group {
        public String name = "";
        public String id = "";
        public String company_id = "";
        public String devison_id = "";
        public String costumer_id = "";
        public boolean isChecked = false;
        public ArrayList<Child> childList = new ArrayList<Child>();
    }

    static class Child {

        public String name = "";
        public String id = "";
        public String product_id = "";
        public String amount = "";
        public String scheme_amount = "";
        public String scheme_id = "";
        public String qty = "";
        public boolean isChecked = false;

        public Child() {
        }

        public Child(String productId) {
            this.product_id = productId;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Child) {
                Child c = (Child) o;
                if (this.product_id != null && this.product_id.equals(c.product_id)) {
                    return true;
                }
            }
            return false;
        }
    }

    FragmentManager fm;
    FragmentTransaction ft;
    String jsonData, sel_crop_id, sel_scheme_id, productname, sel_product_id, str_amount, order_date, orderidfromserviceorder, userId;

    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ProgressDialog progressDialog;
    private SQLiteDatabase odb;
    public static ArrayList<Group> globalGroup = new ArrayList<>();

    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    Button btn_submitproducts;
    DateFormat dateFormat, orderdateFormat;
    Date myDate;
    private TextView tv_customername, tv_divisionname, tv_companyname;
    private EditText et_outstandingamount, et_booked_amount, et_creditlimit;
    //et_abs;
    ImageView iv_ageing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crops_fragment_salesorderactivity);

        globalGroup.clear();
        btn_submitproducts   = (Button) findViewById(R.id.btn_submitproducts);
        db                   = new DatabaseHandler(this);
        dateFormat           = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        orderdateFormat      = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        myDate = new Date();
        sharedpreferences    = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        toolbar              = (Toolbar) findViewById(R.id.toolbar);
        tv_companyname       = (TextView) findViewById(R.id.tv_company_name);
        tv_customername      = (TextView) findViewById(R.id.tv_customer_name);
        tv_divisionname      = (TextView) findViewById(R.id.tv_division_name);

        et_outstandingamount = (EditText) findViewById(R.id.et_osa);
        et_creditlimit       = (EditText) findViewById(R.id.et_credit_limit);
        et_booked_amount     = (EditText) findViewById(R.id.et_sales_order);
       // et_abs               = (EditText) findViewById(R.id.et_abs);

        txt_validation       = (TextView) findViewById(R.id.txt_validation);
        iv_ageing            = (ImageView) findViewById(R.id.iv_ageing);

        tv_customername.setText(sharedpreferences.getString("customer_name", ""));
        tv_divisionname.setText(NewSalesOrderChooseActivity.divisionname);
        tv_companyname.setText(NewSalesOrderChooseActivity.companycode);


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction().add(R.id.content_frame, new TabCropsSalesOrderFragment());
        ft.commit();


        userId      = sharedpreferences.getString("userId", "");

        company_id  = sharedpreferences.getString("company_id", "");
        customer_id = sharedpreferences.getString("customer_id", "");
        division_id = sharedpreferences.getString("division_id", "");

        new Async_getalloffline().execute();
        new Async_getSalesOrderAmount().execute();


        btn_submitproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler=Common.disableClickEvent(btn_submitproducts,true);
                String outstandingamount = et_outstandingamount.getText().toString();
                String creditlimit = et_creditlimit.getText().toString();
                order_date = dateFormat.format(myDate);
                if (outstandingamount.length() == 0) {
                    showAlert("Please provide outstanding amount.");
                } else if (creditlimit.length() == 0) {
                    showAlert("Please provide credit limit amount.");
                } else {
                    float total_amount = 0;

                    /*if(total_amount>Float.parseFloat(token_amount)){
                        showAlert("Booked Amount should not exceed Token amount.");
                    }else{*/
                    try {
                        boolean isValidated = true;
                        boolean isCheckedAtleastOne = false;
                        for (int i = 0; i < CropsFragmentSalesorderActivity.globalGroup.size(); i++) {
                            List<Child> childList = CropsFragmentSalesorderActivity.globalGroup.get(i).childList;
                            for (int j = 0; j < childList.size(); j++) {
                                Child child = childList.get(j);
                                if (child.isChecked) {
                                    isCheckedAtleastOne = true;
                                    if (child.qty.length() == 0) {
                                        isValidated = false;
                                        showAlert("Please provide quantity for " + child.name);
                                        break;
                                    }
                                    if (child.amount.length() > 0) {
                                        total_amount = total_amount + Float.parseFloat(child.amount);
                                    }
                                }


                            }
                        }
                        if (!isCheckedAtleastOne) {
                            isValidated = false;
                            showAlert("Please select atleast one product.");
                        }


                        if (!isValidated) {
                            return;
                        }
                        Common.disableClickEvent(btn_submitproducts,handler);
                        JSONArray mainArray = new JSONArray();
                        JSONArray adbBookArray = new JSONArray();
                        JSONObject naimObject = new JSONObject();
                        for (int i = 0; i < globalGroup.size(); i++) {
                            if (globalGroup.get(i).isChecked) {

                                int statusId = 0;
                                float available_amount = Float.parseFloat(creditlimit) - Float.parseFloat(outstandingamount);
                                if (available_amount < total_amount) {
                                    statusId = 2;
                                }
                                JSONObject advBookObj = new JSONObject();
                                db.addServiceorder(new ServiceOrderMaster("", "1", order_date, sharedpreferences.getString("userId", null), globalGroup.get(i).costumer_id,
                                        globalGroup.get(i).devison_id, globalGroup.get(i).company_id, "1",
                                        order_date, order_date, "0", "",sharedpreferences.getString("userId", null),"0",""));
                                advBookObj.put("CompanyID",   globalGroup.get(i).company_id);
                                advBookObj.put("customer_id", globalGroup.get(i).costumer_id);
                                advBookObj.put("DivisionID",  globalGroup.get(i).devison_id);

                                String selectQuerys = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + " FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";
                                sdbw = db.getWritableDatabase();

                                Cursor c = sdbw.rawQuery(selectQuerys, null);
                                //System.out.println("cursor count "+cursor.getCount());
                                if (c != null && c.moveToFirst()) {
                                    orderidfromserviceorder = String.valueOf(c.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                    Log.e("++++ lastId 1 ++++", orderidfromserviceorder);
                                }

                                advBookObj.put("id", orderidfromserviceorder);
                                advBookObj.put("user_id", sharedpreferences.getString("userId", null));


                                order_date = dateFormat.format(myDate);

                                advBookObj.put("OrderDate", order_date);
                                advBookObj.put("status", statusId);

                                Log.d("Insert: ", "Inserting Sales orders ..");


                                JSONArray cropArray = new JSONArray();
                                // for (int j = 0; j < globalGroup.size(); j++) {
                                JSONObject cropObj = new JSONObject();
                                cropObj.put("CropId", globalGroup.get(i).id);
                                sel_crop_id = globalGroup.get(i).id;


                                cropArray.put(cropObj);
                                advBookObj.put("Crops", cropArray);
                                //JSONArray productArray = new JSONArray();
                                // for (int k = 0; k < globalGroup.size(); k++) {


                                JSONArray productArray = new JSONArray();

                                for (int l = 0; l < globalGroup.get(i).childList.size(); l++) {
                                    if (globalGroup.get(i).childList.get(l).isChecked) {


                                        sel_product_id = globalGroup.get(i).childList.get(l).product_id;
                                        str_amount = globalGroup.get(i).childList.get(l).amount;
                                        productname = globalGroup.get(i).childList.get(l).name;

                                        sel_scheme_id = globalGroup.get(i).childList.get(l).scheme_id;
                                        advqty = globalGroup.get(i).childList.get(l).qty;
                                   /*For fetcheing last inserted id from service order table*/
                                        String selectQueryss = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + " FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";
                                        sdbw = db.getWritableDatabase();

                                        Cursor cs = sdbw.rawQuery(selectQueryss, null);
                                        //System.out.println("cursor count "+cursor.getCount());
                                        if (cs != null && cs.moveToFirst()) {
                                            orderidfromserviceorder = String.valueOf(cs.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                            Log.e("++++ lastId 2 ++++", orderidfromserviceorder);
                                        }

                      /*For Inserting orderdetails into service order details table*/
                                        Log.d("Insert: ", "Inserting sevice order details ..");
                                        int serviceorderdetailorderid = 0;
                                        db.addServiceorderdetails(new ServiceOrderDetailMaster("",
                                                orderidfromserviceorder,
                                                sel_crop_id,
                                                sel_scheme_id,
                                                sel_product_id,
                                                advqty,
                                                str_amount, "", "" + statusId,
                                                order_date, order_date,"0", ""));

                                        // db.addOrders(new Orders(productname, str_amount));

                                        String selectQueryss1 = "SELECT  " + KEY_TABLE_SERVICEORDER_DETAIL_ID  +" FROM " + TABLE_SERVICEORDERDETAILS + " ORDER BY " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " DESC LIMIT 1 ";
                                        sdbw = db.getWritableDatabase();

                                        Cursor cs1 = sdbw.rawQuery(selectQueryss1, null);
                                        //System.out.println("cursor count "+cursor.getCount());
                                        if (cs1 != null && cs1.moveToFirst()) {
                                            mobile_serivce_details_id = String.valueOf(cs1.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                            Log.e("++++ lastId  ++++" , mobile_serivce_details_id);
                                        }

                                        JSONObject object_one = new JSONObject();
                                        object_one.put("OrderPrice", globalGroup.get(i).childList.get(l).amount);
                                        object_one.put("ProductID", globalGroup.get(i).childList.get(l).product_id);
                                        object_one.put("Qunatity", globalGroup.get(i).childList.get(l).qty);
                                        object_one.put("mobile_serivce_details_id",       mobile_serivce_details_id);
                                        productArray.put(object_one);
                                    }
                                    //}
                                }
                                cropObj.put("Products", productArray);
                                //}

                                adbBookArray.put(advBookObj);
                                naimObject.put("salesorder", adbBookArray);

                            }
                        }
                        mainArray.put(naimObject);
                        Log.i("  -json array -", "" + mainArray.toString());
                        // Toast.makeText(CropsFragmentSalesorderActivity.this, globalGroup.get(0).name, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CropsFragmentSalesorderActivity.this, globalGroup.get(0).childList.get(0).name, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CropsFragmentSalesorderActivity.this, globalGroup.get(0).childList.get(0).id, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CropsFragmentSalesorderActivity.this, globalGroup.get(0).childList.get(0).amount, Toast.LENGTH_SHORT).show();
                        try {
                            ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                            if (netInfo != null) {  // connected to the internet
                                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                    // connected to wifi
                                    // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                                    new Async_UpdateAdvancebooking().execute(mainArray.toString());
                                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                    // connected to the mobile provider's data plan

                                    new Async_UpdateAdvancebooking().execute(mainArray.toString());
                                }
                            } else {
                                Log.e("No network", "need to call service to do in background");
                                //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                Intent adv = new Intent(getApplicationContext(), ViewSalesOrderCompanyActivity.class);
                                adv.putExtra(REQUEST_TYPE, 1);
                                startActivity(adv);
                                finish();
                            }

                        } catch (Exception e) {
                            Log.d("Tag", e.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public class Async_UpdateAdvancebooking extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CropsFragmentSalesorderActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //Toast.makeText(getApplicationContext(),"Updateing Advance Bookings",Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, params[0]);
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_ORDERINDENT)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = null;
                //client.newCall(request).execute();

                try {
                    response = client.newCall(request).execute();
                    jsonData = response.body().string();
                    System.out.println("!!!!!!!1Sales Oreder" + jsonData);
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
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (jsonData != null)
            {
                try {
                    System.out.println("!!!!!!!1 Service order post execute" + jsonData+"\n"+s);
                    // JSONArray jsonArray=new JSONArray(jsonData);
                    //  String sdfds = new Gson().toJson(jsonData);
                    //  System.out.println("!!!!!!!1 Advance Booking" + sdfds+"\n"+s);
                    JSONArray jsonArray=toJson(jsonData);

                    for (int i=0;i<jsonArray.length();i++)

                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")){
                            //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                            String service_id = jsonObject.getString("service_id");
                            ffmid = jsonObject.getString("ffm_id");

                            //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                            Log.e("sqlite id", service_id);
                            Log.e("ffmid", ffmid);
                            sdbw = db.getWritableDatabase();
                            // updateFeedback(Feedback feedback);
                            String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + service_id;
                            sdbw.execSQL(updatequery);

                            JSONArray detailsArray = jsonObject.getJSONArray("details");
                            for (int k=0; k < detailsArray.length();k++){
                                String ffm_id = detailsArray.getJSONObject(k).getString("ffm_id");
                                String order_detail_id = detailsArray.getJSONObject(k).getString("order_detail_id");

                                String updatequery1 = "UPDATE " + TABLE_SERVICEORDERDETAILS + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffm_id +" WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " = " + order_detail_id;
                                sdbw.execSQL(updatequery1);
                            }



                        }




                    }


                    AlertDialog.Builder alert = new AlertDialog.Builder(CropsFragmentSalesorderActivity.this);
                    alert.setTitle("Success");
                    alert.setMessage("Sales Order record updated sucessfully");
                    alert.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent adv = new Intent(getApplicationContext(), ViewSalesOrderCompanyActivity.class);
                                    adv.putExtra(REQUEST_TYPE, 1);
                                    startActivity(adv);
                                    finish();
                                    dialog.dismiss();
                                }
                            });
                    alert.show();
                    //JSONObject jsonObject = new JSONObject(jsonData);




                }  catch (Exception e){
                    e.printStackTrace();
                }

            }else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void showAlert(String message) {

        if (!isDialogVisible) {
            alert = new AlertDialog.Builder(CropsFragmentSalesorderActivity.this);
            alert.setTitle("Info");
            alert.setMessage(message);
            alert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            isDialogVisible = false;
                            dialog.dismiss();
                        }
                    });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    isDialogVisible = false;
                }
            });
            alert.show();
            isDialogVisible = true;

        }
    }


    class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CropsFragmentSalesorderActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {


            try {
                List<Crops> cdcList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                //String selectQuery = "SELECT  " + KEY_SCHEMES_MASTER_ID + ","+ KEY_SCHEMES_SAP_CODE + " FROM " + TABLE_SCHEMES + "  where " + KEY_SCHEMES_COMPANY_ID + " = " + company_id + " and " + KEY_SCHEMES_DIVISION_ID + " = " + sel_division_id + " and " + KEY_SCHEMES_CROP_ID + " = " + sel_crop_id ;
                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();


                String selectQuery = "SELECT CD." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET + "," + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET + "," + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT +"," + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_BALANCE+ " FROM " + TABLE_CUSTOMERS
                        + " AS C LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID
                        + " LEFT JOIN " + TABLE_DIVISION + " AS D " + " WHERE C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + sharedpreferences.getString("customer_id", null) + " and " + "CD." + KEY_TABLE_DIVISION_MASTER_ID + " = " + division_id + " group by C." + KEY_TABLE_CUSTOMER_MASTER_ID + "";
                System.out.println("@@@@@@@@@@"+selectQuery);


                // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        i = i + 1;
                        /*Customers customers = new Customers();

                        customers.setCusMasterID(cursor.getString(0));
                        customers.setCusName(cursor.getString(1));
                        customers.setCuscode(cursor.getString(2));*/

                        System.out.println("******----" + cursor.getString(0) + cursor.getString(1) + cursor.getString(2) + cursor.getString(3));
                       // Log.d("div id", cursor.getString(0));
                        //Log.d("div name ", cursor.getString(1));
                        Log.d("inside bucket", cursor.getString(2));
                        Log.d("outside bucket", cursor.getString(3));

                        String inside_value = cursor.getString(2);
                        String outside_value = cursor.getString(3);

                        int inside = (inside_value!=null && !inside_value.equals("null")) ? Integer.parseInt(inside_value):0;
                        int outside = (outside_value!=null && !outside_value.equals("null")) ? Integer.parseInt(outside_value):0;
                       // Double creditBalance = Double.parseDouble(cursor.getString(5));

                        int total_osa = inside + outside;
                        sum_osa = sum_osa + total_osa;

                        Double creditLimt = Double.parseDouble(cursor.getString(4));
                        creditlimit = creditLimt-(sum_osa);

                        if (outside > 1) {
                            aging1 = ">";

                            aging2 = "90";
                            //color= "#ff0000";

                        } else  {
                            aging1 = "<";
                            aging2 = "90";
                            // color="#008000";
                        }

                    } while (cursor.moveToNext());
                    //
                }

                // do some stuff....
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //adapter.updateResults(favouriteItem);
            if (sum_osa>0) {
                et_outstandingamount.setText(String.valueOf(sum_osa));
            }else {
                et_outstandingamount.setText("0");
            }
            et_creditlimit.setText(String.valueOf(creditlimit));
            if(creditlimit==0 || creditlimit==1) {
                showExceedingAlert("" + "You can't raise sales order ! ");
            }else if (aging1 != null && aging1.equalsIgnoreCase("<")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.less));
            } else if (aging1 != null && aging1.equalsIgnoreCase(">")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.greater));
                showExceedingAlert("" + "Ageing Exceeded, You can't raise sales order ! ");
            }
        }
    }

    private void showExceedingAlert(String message){
        final Dialog dialog = new Dialog(CropsFragmentSalesorderActivity.this);
        dialog.setContentView(R.layout.custom_alert_salesorder);


        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.tv_message);
        text.setText(message);

        ImageView sucessimage = (ImageView) dialog.findViewById(R.id.iv_sucess);
        sucessimage.setVisibility(View.VISIBLE);
        //
        // ImageView failureimage = (ImageView) dialog.findViewById(R.id.iv_failure);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private class Async_getSalesOrderAmount extends AsyncTask<Void, Void, Void>
    {

        protected Void doInBackground(Void... params)
        {

            String selectQuery = "SELECT  SUM(SOD.order_price) AS sumprice" +
                    " FROM service_order_details SOD" +
                    "  JOIN service_order SO ON SO."+ KEY_TABLE_SERVICEORDER_ID+" = SOD."+KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID+
                    "  JOIN customers AS C ON C.customer_id = SO.customer_id" +
                    "  JOIN division AS dv ON dv.division_id = SO.division_id" +
                    " WHERE SO.user_id =" + userId +
                    " AND SO.order_type = 1 AND SO.division_id=" +division_id+
                    " AND SO.customer_id ="+customer_id+" AND SO."+KEY_TABLE_SERVICEORDER_COMPANY_ID+"="+company_id;

            Log.e("selectQuery",selectQuery);
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    tokenAount = cursor.getString(0);
                } while (cursor.moveToNext());

            }

            else Log.d("LOG", "returned null!");

            return null;
        }
        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            if(tokenAount!=null)
                et_booked_amount.setText(""+tokenAount);
               new Async_getTokenAmount().execute();

        }
    }

    private class Async_getTokenAmount extends AsyncTask<Void, Void, Void>
    {

        private String tokenAount;


        protected Void doInBackground(Void... params)
        {
            String selectQuery = "SELECT "+
                    " SO.advance_amount,SUM(SOD.advance_amount) AS sumprice" +
                    " FROM service_order_details SOD" +
                    " JOIN service_order SO ON SO."+ KEY_TABLE_SERVICEORDER_ID+" = SOD."+KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID+
                    " JOIN customers AS C ON C.customer_id = SO.customer_id" +
                    " JOIN division AS dv ON dv.division_id = SO.division_id" +
                    " LEFT JOIN "+TABLE_COMPANIES + " AS CM ON CM."+ KEY_TABLE_COMPANIES_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_COMPANY_ID+
                    " WHERE SO.user_id =" + userId +
                    " AND SO.order_type =2 AND SO.division_id=" +division_id+
                    " AND SO.customer_id ="+customer_id+" AND SO."+KEY_TABLE_SERVICEORDER_COMPANY_ID+"="+company_id;

            Log.e("selectQuery",selectQuery);
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    tokenAount = cursor.getString(0);
                    advanceBookingAount =  cursor.getString(1);
                } while (cursor.moveToNext());

            }

            else Log.d("LOG", "returned null!");

            return null;
        }
        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

           /* if(advanceBookingAount!=null)
                et_abs.setText(""+advanceBookingAount);

          //  et_token_amount.setSelection(et_token_amount.getText().length());
            et_abs.setSelection(et_booked_amount.getText().length());*/



        }
    }


}
