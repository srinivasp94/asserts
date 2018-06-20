package com.nsl.app.stockmovement;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.NewViewStockMovementChooseActivity;
import com.nsl.app.OnAmountChangeListener;
import com.nsl.app.R;
import com.nsl.app.ServiceOrderDetailMaster;
import com.nsl.app.ServiceOrderMaster;
import com.nsl.app.advancebooking.TabCropsAdvanceBookingFragment;
import com.nsl.app.advancebooking.ViewBookings1Activity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.orderindent.FragmentOrderIndent.REQUEST_TYPE;

public class CropsFragmentViewStockMovementActivity extends AppCompatActivity implements OnAmountChangeListener {


    AlertDialog.Builder alert;
    private boolean isDialogVisible;
    private String sqliteid,ffmid;
    private String advqty;
    int sum_osa = 0,osa;
    private String advanceBookingAount;

    public static class Group{
        public String name="";
        public String id="";
        public String company_id="";
        public String devison_id="";
        public String costumer_id="";
        public boolean isChecked=false;
        public ArrayList<Child> childList=new ArrayList<Child>();
    }

   static class Child{

        public String name="";
        public String id="";
        public String product_id="";
        public String amount="";
        public String scheme_amount="";
        public String scheme_id="";
        public String qty="";
        public boolean isChecked=false;

       public Child() {
       }

       public Child(String productId) {
           this.product_id = productId;
       }

       @Override
       public boolean equals(Object o) {
           if(o instanceof Child){
               Child c = (Child)o;
               if(this.product_id!=null && this.product_id.equals(c.product_id)){
                   return true;
               }
           }
           return false;
       }
   }
    FragmentManager fm;
    FragmentTransaction ft;
    String jsonData,sel_crop_id,sel_scheme_id,productname,sel_product_id,str_amount,order_date,orderidfromserviceorder,userId,sel_division_id,company_id,division_id,customer_id;

    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ProgressDialog progressDialog;
    private SQLiteDatabase odb;
    public static ArrayList<Group> globalGroup=new ArrayList<>();
    public String previousQuantity;
    public EditText previousamountText;

    DatabaseHandler db;
    SQLiteDatabase  sdbw,sdbr;
    Button          btn_submitproducts;
    DateFormat      dateFormat,orderdateFormat;
    Date            myDate;
    private         TextView tv_customername,tv_divisionname,tv_companyname;
    private         EditText et_token_amount,et_booked_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crops_fragment_stockmovementactivity);

        globalGroup.clear();
        btn_submitproducts =(Button)findViewById(R.id.btn_submitproducts);
        db                 = new DatabaseHandler(this);
        dateFormat         = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        orderdateFormat    = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        myDate             = new Date();
        sharedpreferences  = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        btn_submitproducts.setVisibility(View.GONE);
        tv_companyname     = (TextView)findViewById(R.id.tv_company_name);
        tv_customername    = (TextView)findViewById(R.id.tv_customer_name);
        tv_customername.setVisibility(View.GONE);
        tv_divisionname    = (TextView)findViewById(R.id.tv_division_name);

        et_token_amount    = (EditText)findViewById(R.id.et_token_amount);
        et_booked_amount   = (EditText)findViewById(R.id.et_booked_amount);

        et_token_amount.setSelection(et_token_amount.getText().length());
        et_booked_amount.setSelection(et_booked_amount.getText().length());

       // tv_customername.setText(sharedpreferences.getString("customer_name",""));
        tv_divisionname.setText(NewViewStockMovementChooseActivity.divisionname);
        tv_companyname.setText(NewViewStockMovementChooseActivity.companycode);

        userId      = sharedpreferences.getString("userId", "");

        company_id  = sharedpreferences.getString("company_id", "");
        customer_id = sharedpreferences.getString("customer_id", "");
        division_id = sharedpreferences.getString("division_id", "");

        Toolbar toolbar    = (Toolbar) findViewById(R.id.toolbar);
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
        ft = fm.beginTransaction().add(R.id.content_frame, new TabCropsViewStockMovemetFragment());
        ft.commit();


        new Async_getTokenAmount().execute();
        et_token_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean atleastOnChecked = false;
                for(int i = 0; i< CropsFragmentViewStockMovementActivity.globalGroup.size(); i++) {

                    Group  group = CropsFragmentViewStockMovementActivity.globalGroup.get(i);
                    if(group.isChecked){
                        atleastOnChecked = true;
                        break;
                    }


                }
                if(atleastOnChecked) {
                    globalGroup.clear();
                    fm = getSupportFragmentManager();
                    ft = fm.beginTransaction().replace(R.id.content_frame, new TabCropsAdvanceBookingFragment());
                    ft.commit();
                    et_booked_amount.setText("");
                }

            }
        });
        btn_submitproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String token_amount = et_token_amount.getText().toString();
                order_date = dateFormat.format(myDate);
                if(token_amount.length()==0 ){
                    showAlert("Please provide token amount.");
                }
                else{
                    double total_amount = 0.00;

                    /*if(total_amount>Float.parseFloat(token_amount)){
                        showAlert("Booked Amount should not exceed Token amount.");
                    }else{*/
                        try {
                            boolean isValidated = true;
                            boolean isCheckedAtleastOne = false;
                            for(int i = 0; i< CropsFragmentViewStockMovementActivity.globalGroup.size(); i++) {
                                List<Child> childList = CropsFragmentViewStockMovementActivity.globalGroup.get(i).childList;
                                for(int j=0;j<childList.size();j++){
                                    Child child = childList.get(j);
                                    if(child.isChecked) {
                                        isCheckedAtleastOne = true;
                                        if(child.scheme_id.length()==0 || child.scheme_id.equals("0")){
                                            isValidated = false;
                                            showAlert("Please select scheme.");
                                            break;
                                        }
                                       else if(child.qty.length()==0){
                                            isValidated = false;
                                            showAlert("Please provide quantity for "+child.name);
                                            break;
                                        }
                                        if(child.amount.length()>0){
                                            total_amount = total_amount + Float.parseFloat(child.amount);
                                        }
                                    }


                                }
                            }
                            if(!isCheckedAtleastOne){
                                isValidated = false;
                                showAlert("Please select atleast one product.");
                            }

                            else if(total_amount>Float.parseFloat(token_amount)){
                                isValidated = false;
                                showAlert("Booked Amount should not exceed Token amount.");
                            }

                            if(!isValidated){
                                return;
                            }

                           /* DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);
                            total_amount = Double.parseDouble(df.format(total_amount));*/

                            JSONArray mainArray = new JSONArray();
                            JSONArray adbBookArray = new JSONArray();
                            JSONObject naimObject = new JSONObject();
                            for (int i = 0; i < globalGroup.size(); i++) {
                                if (globalGroup.get(i).isChecked) {

                                    JSONObject advBookObj = new JSONObject();
                                    db.addServiceorder(new ServiceOrderMaster("", "2", order_date, sharedpreferences.getString("userId",null), globalGroup.get(i).costumer_id,
                                            globalGroup.get(i).devison_id, globalGroup.get(i).company_id, "1",
                                            order_date, order_date,"",token_amount,sharedpreferences.getString("userId",null),"0",""));
                                    advBookObj.put("CompanyID", globalGroup.get(i).company_id);
                                    advBookObj.put("customer_id", globalGroup.get(i).costumer_id);
                                    advBookObj.put("Tokenamount", total_amount);
                                    customer_id = globalGroup.get(i).costumer_id;
                                    company_id  = globalGroup.get(i).company_id;
                                    sel_division_id = globalGroup.get(i).devison_id;
                                    advBookObj.put("DivisionID", globalGroup.get(i).devison_id);

                                    String selectQuerys = "SELECT  " + KEY_TABLE_SERVICEORDER_ID  +" FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";
                                    sdbw = db.getWritableDatabase();

                                    Cursor c = sdbw.rawQuery(selectQuerys, null);
                                    //System.out.println("cursor count "+cursor.getCount());
                                    if (c != null && c.moveToFirst()) {
                                        orderidfromserviceorder = String.valueOf(c.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                        Log.e("++++ lastId 1 ++++" , orderidfromserviceorder);
                                    }

                                    advBookObj.put("id", orderidfromserviceorder);
                                    advBookObj.put("user_id", sharedpreferences.getString("userId",null));

                                    order_date = dateFormat.format(myDate);

                                    Log.d("Insert: ", "Inserting Advanced orders ..");



                                    JSONArray cropArray = new JSONArray();
                                    // for (int j = 0; j < globalGroup.size(); j++) {
                                    JSONObject cropObj = new JSONObject();
                                    cropObj.put("CropId", globalGroup.get(i).id);

                                    sel_crop_id = globalGroup.get(i).id;

                                    cropObj.put("scheme_id", "");

                                    cropArray.put(cropObj);
                                    advBookObj.put("Crops", cropArray);
                                    //JSONArray productArray = new JSONArray();
                                    // for (int k = 0; k < globalGroup.size(); k++) {


                                    JSONArray productArray = new JSONArray();

                                    for (int l = 0; l < globalGroup.get(i).childList.size(); l++) {
                                        if (globalGroup.get(i).childList.get(l).isChecked) {
                                            JSONObject object_one = new JSONObject();
                                            object_one.put("advance_amount", globalGroup.get(i).childList.get(l).amount);
                                            object_one.put("ProductID",      globalGroup.get(i).childList.get(l).product_id);
                                            object_one.put("Qunatity",       globalGroup.get(i).childList.get(l).qty);
                                            object_one.put("scheme_id",       globalGroup.get(i).childList.get(l).scheme_id);
                                            productArray.put(object_one);

                                            sel_product_id = globalGroup.get(i).childList.get(l).product_id;
                                            str_amount     = globalGroup.get(i).childList.get(l).amount;
                                            productname    = globalGroup.get(i).childList.get(l).name;

                                            sel_scheme_id = globalGroup.get(i).childList.get(l).scheme_id;
                                            advqty        = globalGroup.get(i).childList.get(l).qty;
                                   /*For fetcheing last inserted id from service order table*/
                                            String selectQueryss = "SELECT  " + KEY_TABLE_SERVICEORDER_ID  +" FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";
                                            sdbw = db.getWritableDatabase();

                                            Cursor cs = sdbw.rawQuery(selectQueryss, null);
                                            //System.out.println("cursor count "+cursor.getCount());
                                            if (cs != null && cs.moveToFirst()) {
                                                orderidfromserviceorder = String.valueOf(cs.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                                Log.e("++++ lastId 2 ++++" , orderidfromserviceorder);
                                            }

                      /*For Inserting orderdetails into service order details table*/
                                            Log.d("Insert: ", "Inserting sevice order details ..");
                                            int serviceorderdetailorderid = 0;
                                            db.addServiceorderdetails(new ServiceOrderDetailMaster("",
                                                    orderidfromserviceorder,
                                                    sel_crop_id,sel_scheme_id,
                                                    sel_product_id,
                                                    advqty,
                                                    "",
                                                    str_amount,
                                                    "1",
                                                    order_date,
                                                    order_date,"", ""));

                                            // db.addOrders(new Orders(productname, str_amount));
                                        }
                                        //}
                                    }
                                    cropObj.put("Products", productArray);
                                    //}

                                    adbBookArray.put(advBookObj);
                                    naimObject.put("AdvanceBookings", adbBookArray);

                                }
                            }
                            mainArray.put(naimObject);
                            Log.i("  -json array -",""+mainArray.toString());
                            // Toast.makeText(CropsFragmentAdvancebookingActivity.this, globalGroup.get(0).name, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(CropsFragmentAdvancebookingActivity.this, globalGroup.get(0).childList.get(0).name, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(CropsFragmentAdvancebookingActivity.this, globalGroup.get(0).childList.get(0).id, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(CropsFragmentAdvancebookingActivity.this, globalGroup.get(0).childList.get(0).amount, Toast.LENGTH_SHORT).show();
                            try {
                                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                                if (netInfo != null){  // connected to the internet
                                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                        // connected to wifi
                                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                                        new Async_UpdateAdvancebooking().execute(mainArray.toString());
                                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                        // connected to the mobile provider's data plan

                                        new Async_UpdateAdvancebooking().execute(mainArray.toString());
                                    }
                                }else{
                                    Log.e("No network","need to call service to do in background");
                                   // Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    Intent adv = new Intent(getApplicationContext(),ViewBookings1Activity.class);
                                    adv.putExtra(REQUEST_TYPE,2);
                                    startActivity(adv);
                                    finish();
                                }

                            } catch (Exception e) {
                                Log.d("Tag", e.toString());
                            }

                        }catch (Exception e){e.printStackTrace();}

                }

            }
        });
    }


    public class Async_UpdateAdvancebooking extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CropsFragmentViewStockMovementActivity.this);
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
                        .url(Constants.URL_NSL_MAIN +Constants.URL_INSERT_ADVANCEBOOKING)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                       // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                        Response response = client.newCall(request).execute();


                        try {
                            response = client.newCall(request).execute();
                            jsonData = response.body().string();
                            System.out.println("!!!!!!!1 Advance Booking" + jsonData);
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


            if (jsonData != null)
            {   JSONArray jsonarray;
                try {

                    jsonarray = new JSONArray(jsonData);

                    for(int n = 0; n < jsonarray.length(); n++)
                    {
                        JSONObject object = jsonarray.getJSONObject(n);
                        String status = object.getString("status");
                        if (status.equalsIgnoreCase("success")){
                            //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                            sqliteid = object.getString("sqlite");
                            ffmid = object.getString("ffm_id");

                            //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                            Log.e("sqlite id", sqliteid);
                            Log.e("ffmid", ffmid);
                            sdbw = db.getWritableDatabase();
                            // updateFeedback(Feedback feedback);
                            String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + sqliteid;
                            sdbw.execSQL(updatequery);

                            AlertDialog.Builder alert = new AlertDialog.Builder(CropsFragmentViewStockMovementActivity.this);
                            alert.setTitle("Success");
                            alert.setMessage("Advance booking record updated sucessfully");
                            alert.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent adv = new Intent(getApplicationContext(),ViewBookings1Activity.class);
                                            adv.putExtra(REQUEST_TYPE,2);
                                            startActivity(adv);
                                            finish();
                                            dialog.dismiss();
                                        }
                                    });
                            alert.show();
                        }
                    }

                }  catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void showAlert(String message) {

        if (!isDialogVisible) {
            alert = new AlertDialog.Builder(CropsFragmentViewStockMovementActivity.this);
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
    private void showExceedAlert(String message) {

        if (!isDialogVisible) {
            alert = new AlertDialog.Builder(CropsFragmentViewStockMovementActivity.this);
            alert.setTitle("Info");
            alert.setMessage(message);
            alert.setCancelable(false);
            alert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            isDialogVisible = false;
                            dialog.dismiss();
                            if(previousamountText!=null) {
                                previousamountText.setText("");
                               /* if(previousQuantity!=null && previousQuantity.length()>0)
                                 previousamountText.setText("" + previousQuantity);
                                else
                                previousamountText.setText("");*/
                            }

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


    @Override
    public void onAmountChanged() {

        et_booked_amount.setTextColor(ContextCompat.getColor(this, R.color.black));
        double total_amount = 0.00;
        for(int i = 0; i< CropsFragmentViewStockMovementActivity.globalGroup.size(); i++) {
            List<Child> childList = CropsFragmentViewStockMovementActivity.globalGroup.get(i).childList;
            for(int j=0;j<childList.size();j++){
                Child child = childList.get(j);
                if(child.isChecked) {
                    if(child.amount.length()>0 ){

                        total_amount = total_amount +Double.parseDouble(child.amount) ;

                    }
                }
            }

            }

        String token_amount = et_token_amount.getText().toString();
        if(advanceBookingAount!=null)
        total_amount = Double.parseDouble(advanceBookingAount) + total_amount;

        if(token_amount.length()==0 ){
            showAlert("Please provide token amount.");
            return;
        }else{
            if(total_amount>Float.parseFloat(token_amount)){
                et_booked_amount.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                showExceedAlert("Booked Amount should not exceed Token amount.");
                return;
            }
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            et_booked_amount.setText(""+total_amount);

        }

    }

    @Override
    public void beforeAmountChanged(final EditText amountText, final String quantity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                previousamountText = amountText;
                if(!isExceeded() ){
                    previousQuantity = quantity;
                }
            }
        },300);

    }

    private boolean isExceeded(){
        double total_amount = 0.00;
        for(int i = 0; i< CropsFragmentViewStockMovementActivity.globalGroup.size(); i++) {
            List<Child> childList = CropsFragmentViewStockMovementActivity.globalGroup.get(i).childList;
            for (int j = 0; j < childList.size(); j++) {
                Child child = childList.get(j);
                if (child.isChecked) {
                    if (child.amount.length() > 0) {

                        total_amount = total_amount + Double.parseDouble(child.amount);
                    }
                }
            }
            String token_amount = et_token_amount.getText().toString();
            if (token_amount.length() > 0) {
                if (total_amount > Float.parseFloat(token_amount)) {
                     return true;
                }

            }
        }
        return  false;
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
            if(tokenAount!=null)
            et_token_amount.setText(""+tokenAount);
            if(advanceBookingAount!=null)
            et_booked_amount.setText(""+advanceBookingAount);


        }
    }

}
