package com.nsl.app.advancebooking;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.OnAmountChangeListener;
import com.nsl.app.PaymentActivity;
import com.nsl.app.Payment_collection;
import com.nsl.app.R;
import com.nsl.app.ServiceOrderDetailMaster;
import com.nsl.app.ServiceOrderMaster;
import com.nsl.app.commonutils.Common;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDERDETAILS;
import static com.nsl.app.orderindent.FragmentOrderIndent.REQUEST_TYPE;

public class CropsFragmentAdvancebookingActivity extends AppCompatActivity implements OnAmountChangeListener {


    AlertDialog.Builder alert;
    private boolean isDialogVisible;
    private String service_id, ffmid;
    private String advqty;
    int sum_osa = 0, osa;
    private String advanceBookingAount;
    private String mobile_serivce_details_id;
    private AlertDialog dialog;
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;
    private String scheme_amount="";
    private String slab_id="";


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
        public String slab_id = "";
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
    String jsonData, sel_crop_id, productname, sel_product_id, str_amount, order_date, orderidfromserviceorder, userId, sel_division_id, company_id, division_id, customer_id;
    String sel_scheme_id = "0";
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ProgressDialog progressDialog;
    private SQLiteDatabase odb;
    public static ArrayList<Group> globalGroup = new ArrayList<>();
    public String previousQuantity;
    public EditText previousamountText;

    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    Button btn_submitproducts;
    DateFormat dateFormat, orderdateFormat;
    Date myDate;
    private TextView tv_customername, tv_divisionname, tv_companyname;
    private EditText et_token_amount, et_booked_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crops_fragment_advancebookingactivity);


        // Get the activity
        mContext = getApplicationContext();

        // Get the activity
        mActivity = CropsFragmentAdvancebookingActivity.this;

        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.drawer_layout);
        globalGroup.clear();
        btn_submitproducts = (Button) findViewById(R.id.btn_submitproducts);
        db = new DatabaseHandler(this);
        dateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        orderdateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        myDate = new Date();
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        tv_companyname = (TextView) findViewById(R.id.tv_company_name);
        tv_customername = (TextView) findViewById(R.id.tv_customer_name);
        tv_divisionname = (TextView) findViewById(R.id.tv_division_name);

        et_token_amount = (EditText) findViewById(R.id.et_token_amount);
        et_booked_amount = (EditText) findViewById(R.id.et_booked_amount);


        tv_customername.setText(sharedpreferences.getString("customer_name", ""));
        tv_divisionname.setText(NewAdvancebokingChooseActivity.divisionname);
        tv_companyname.setText(NewAdvancebokingChooseActivity.companycode);

        userId = sharedpreferences.getString("userId", "");

        company_id = sharedpreferences.getString("company_id", "");
        customer_id = sharedpreferences.getString("customer_id", "");
        division_id = sharedpreferences.getString("division_id", "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        ft = fm.beginTransaction().add(R.id.content_frame, new TabCropsAdvanceBookingFragment());
        ft.commit();


        new Async_getTokenAmount().execute();
        System.out.print("getTokenAmount " + db.getTokenAmount(customer_id, division_id, Calendar.getInstance().get(Calendar.YEAR), "1"));

        et_token_amount.setText(db.getTokenAmount(customer_id, division_id, Calendar.getInstance().get(Calendar.YEAR), "1"));


        et_token_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /* LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                    // Inflate the custom layout/view
                    View customView = inflater.inflate(R.layout.row_payment_history,null);

                    mPopupWindow = new PopupWindow(
                            customView,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    if(Build.VERSION.SDK_INT>=21){
                        mPopupWindow.setElevation(5.0f);
                    }

                    mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);*/
                ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
                favouriteItem.clear();

                final Dialog dialog = new Dialog(CropsFragmentAdvancebookingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_payment_history_in_abs);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setCancelable(true);
                ListView listView = (ListView) dialog.findViewById(R.id.listView);
                TextView empty = (TextView) dialog.findViewById(R.id.empty);
                Button close = (Button) dialog.findViewById(R.id.button_stock_placed_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ItemfavitemAdapter adapter = new ItemfavitemAdapter(CropsFragmentAdvancebookingActivity.this, favouriteItem);
                listView.setAdapter(adapter);


                Log.e("Reading: ", "calling history..");

                List<Payment_collection> paymentCollections = db.getAllPaymentCollectionhistoryForABS(userId, customer_id,division_id);
                // List<Payment_collection> paymentCollections = db.getAllPaymentCollections(userId);
                if (paymentCollections.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    empty.setVisibility(View.GONE);
                }
                for (Payment_collection pc : paymentCollections) {
                    String log = "Id: " + pc.getID() + " ,pay_type: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() +
                            " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() +
                            " ,amt : " + pc.get_total_amount() + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() +
                            "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() +
                            ",cheque number:" + pc.get_cheque_no_dd_no() + ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() +
                            ",updated datetime:" + pc.get_updated_datetime() + ",ffmid:" + pc.get_ffmid();

                    Log.e("payment collection: ", log);
                }
                int srno = 1;
                for (Payment_collection fb : paymentCollections) {

                    String bankid = fb.get_bank_name();

                    Log.e("bank id", bankid);

                    Log.e("serial number", String.valueOf(srno));
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("srno", String.valueOf(srno));
                    map.put("sr", String.valueOf(fb.getID()));
                    map.put("name", fb.get_bank_name());
                    map.put("amount", fb.get_total_amount());
                    map.put("mode", fb.get_payment_mode());
                    map.put("type", fb.get_payment_type());
                    map.put("company_id", String.valueOf(fb.get_company_id()));
                    map.put("division_id", String.valueOf(fb.get_division_id()));
                    map.put("customer_id", String.valueOf(fb.get_customer_id()));
                    map.put("total_amount", fb.get_total_amount());
                    map.put("payment_mode", fb.get_payment_mode());
                    map.put("rtgs", fb.get_rtgs_or_neft_no());
                    map.put("pay_date", fb.get_payment_datetime());
                    map.put("dateoncheque", fb.get_date_on_cheque_no());
                    map.put("chequeno", fb.get_cheque_no_dd_no());
                    map.put("status", String.valueOf(fb.get_status()));
                    map.put("createddate", fb.get_created_datetime());
                    map.put("updateddate", fb.get_updated_datetime());
                    map.put("ffmid", fb.get_ffmid());

                    favouriteItem.add(map);
                    srno = srno + 1;

                }

                adapter = new ItemfavitemAdapter(CropsFragmentAdvancebookingActivity.this, favouriteItem);
                //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
                listView.setAdapter(adapter);

                dialog.show();

            }
        });






    /*
        et_token_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().startsWith("0")){
                    String value = "";
                    if(s.toString().length()==1) {
                         value = "";
                    }
                    else if(s.toString().length()>0){
                        value = s.toString().substring(1);
                    }
                    et_token_amount.setText(value);
                }
                boolean atleastOnChecked = false;

                for(int i = 0; i< CropsFragmentAdvancebookingActivity.globalGroup.size(); i++) {

                    Group  group = CropsFragmentAdvancebookingActivity.globalGroup.get(i);
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
*/
        btn_submitproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler=Common.disableClickEvent(btn_submitproducts,true);
                String token_amount = et_token_amount.getText().toString();
                order_date = dateFormat.format(myDate);
                if (token_amount.length() == 0) {
                    showAlert("Please submit token amount.");
                } else {
                    double total_amount = 0.00;

                    /*if(total_amount>Float.parseFloat(token_amount)){
                        showAlert("Booked Amount should not exceed Token amount.");
                    }else{*/
                    try {
                        boolean isValidated = true;
                        boolean isCheckedAtleastOne = false;
                        for (int i = 0; i < CropsFragmentAdvancebookingActivity.globalGroup.size(); i++) {
                            List<Child> childList = CropsFragmentAdvancebookingActivity.globalGroup.get(i).childList;
                            for (int j = 0; j < childList.size(); j++) {
                                Child child = childList.get(j);
                                if (child.isChecked) {
                                    isCheckedAtleastOne = true;
                                       /* if(child.scheme_id.length()==0 || child.scheme_id.equals("0")){
                                            isValidated = false;
                                            showAlert("Please select scheme.");
                                            break;
                                        }
                                       else*/
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
                        } else if (total_amount > Float.parseFloat(token_amount)) {
                            isValidated = false;
                            showAlert("Booked Amount should not exceed Token amount.");
                        }

                        if (!isValidated) {
                            return;
                        }

                           /* DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);
                            total_amount = Double.parseDouble(df.format(total_amount));*/
                        Common.disableClickEvent(btn_submitproducts,handler);
                        JSONArray mainArray = new JSONArray();
                        JSONArray adbBookArray = new JSONArray();
                        JSONObject naimObject = new JSONObject();
                        for (int i = 0; i < globalGroup.size(); i++) {
                            if (globalGroup.get(i).isChecked) {

                                JSONObject advBookObj = new JSONObject();
                                db.addServiceorder(new ServiceOrderMaster("", "2", order_date, sharedpreferences.getString("userId", null), globalGroup.get(i).costumer_id,
                                        globalGroup.get(i).devison_id, globalGroup.get(i).company_id, "1",
                                        order_date, order_date, "0", token_amount, sharedpreferences.getString("userId", null), "0", ""));
                                advBookObj.put("CompanyID", globalGroup.get(i).company_id);
                                advBookObj.put("customer_id", globalGroup.get(i).costumer_id);
                                advBookObj.put("Tokenamount", total_amount);
                                customer_id = globalGroup.get(i).costumer_id;
                                company_id = globalGroup.get(i).company_id;
                                sel_division_id = globalGroup.get(i).devison_id;
                                advBookObj.put("DivisionID", globalGroup.get(i).devison_id);

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

                                        sel_product_id = globalGroup.get(i).childList.get(l).product_id;
                                        str_amount = globalGroup.get(i).childList.get(l).amount;
                                        productname = globalGroup.get(i).childList.get(l).name;

                                        sel_scheme_id = globalGroup.get(i).childList.get(l).scheme_id;
                                        scheme_amount=globalGroup.get(i).childList.get(l).scheme_amount;
                                        slab_id=globalGroup.get(i).childList.get(l).slab_id;

                                        if (sel_scheme_id.equalsIgnoreCase(null) || sel_scheme_id.equalsIgnoreCase("null") || sel_scheme_id.equalsIgnoreCase("")) {
                                            sel_scheme_id = "0";
                                        }
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
                                        db.addServiceorderdetails(new ServiceOrderDetailMaster(String.valueOf(l + 1),
                                                orderidfromserviceorder,
                                                sel_crop_id,
                                                sel_scheme_id,
                                                sel_product_id,
                                                advqty,
                                                "",
                                                str_amount,
                                                "1",
                                                order_date,
                                                order_date, "0",slab_id));

                                        // db.addOrders(new Orders(productname, str_amount));

                                        String selectQueryss1 = "SELECT  " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " FROM " + TABLE_SERVICEORDERDETAILS + " ORDER BY " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " DESC LIMIT 1 ";
                                        sdbw = db.getWritableDatabase();

                                        Cursor cs1 = sdbw.rawQuery(selectQueryss1, null);
                                        //System.out.println("cursor count "+cursor.getCount());
                                        if (cs1 != null && cs1.moveToFirst()) {
                                            mobile_serivce_details_id = String.valueOf(cs1.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                            Log.e("++++ lastId  ++++", mobile_serivce_details_id);
                                        }

                                        JSONObject object_one = new JSONObject();
                                        object_one.put("advance_amount", globalGroup.get(i).childList.get(l).amount);
                                        object_one.put("ProductID", globalGroup.get(i).childList.get(l).product_id);
                                        object_one.put("Qunatity", globalGroup.get(i).childList.get(l).qty);
                                        if (globalGroup.get(i).childList.get(l).scheme_id.equalsIgnoreCase("")) {
                                            object_one.put("scheme_id", 0);

                                        } else {
                                            object_one.put("scheme_id", globalGroup.get(i).childList.get(l).scheme_id);
                                        }
                                        object_one.put("mobile_serivce_details_id", mobile_serivce_details_id);
                                        object_one.put("slab_id", slab_id);
                                        productArray.put(object_one);


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
                        Log.i("  -json array -", "" + mainArray.toString());
                        // Toast.makeText(CropsFragmentAdvancebookingActivity.this, globalGroup.get(0).name, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CropsFragmentAdvancebookingActivity.this, globalGroup.get(0).childList.get(0).name, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CropsFragmentAdvancebookingActivity.this, globalGroup.get(0).childList.get(0).id, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CropsFragmentAdvancebookingActivity.this, globalGroup.get(0).childList.get(0).amount, Toast.LENGTH_SHORT).show();
                        try {

                                if (Common.haveInternet(getApplicationContext())) {

                                    new Async_UpdateAdvancebooking().execute(mainArray.toString());

                                }  else {
                                Log.e("No network", "need to call service to do in background");
                                // Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                Intent adv = new Intent(getApplicationContext(), ViewBookings1Activity.class);
                                adv.putExtra(REQUEST_TYPE, 2);
                                startActivity(adv);
                                finish();
                            }

                        } catch (Exception e) {
                            Log.d("Tag", e.toString());
                        }

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
            progressDialog = new ProgressDialog(CropsFragmentAdvancebookingActivity.this);
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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_INSERT_ADVANCEBOOKING)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                //     Response response = client.newCall(request).execute();
                Response response = null;

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


            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing())
                progressDialog.dismiss();


            if (jsonData != null) {
                try {
                    System.out.println("!!!!!!!1 Advance Booking" + jsonData + "\n" + s);
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

                            service_id = jsonObject.getString("service_id");
                            ffmid = jsonObject.getString("ffm_id");

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

                    AlertDialog.Builder alert = new AlertDialog.Builder(CropsFragmentAdvancebookingActivity.this);
                    alert.setTitle("Success");
                    alert.setMessage("Advance booking record updated sucessfully");
                    alert.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent adv = new Intent(getApplicationContext(), ViewBookings1Activity.class);
                                    adv.putExtra(REQUEST_TYPE, 2);
                                    startActivity(adv);
                                    finish();
                                    dialog.dismiss();
                                }
                            });
                    alert.show();
                    //JSONObject jsonObject = new JSONObject(jsonData);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

        }
    }


    public static JSONArray toJson(String jString) {
        System.out.println("I've got" + jString + "*");

        try {
            return new JSONArray(jString);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Error while converting to JSONObject");
        }

        return null;
    }

    private void showAlert(String message) {

        if (!isDialogVisible) {
            alert = new AlertDialog.Builder(CropsFragmentAdvancebookingActivity.this);
            alert.setTitle("Info");
            alert.setMessage(message);
            alert.setPositiveButton("Continue",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isDialogVisible = false;
                                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }, 1000);

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
            alert = new AlertDialog.Builder(CropsFragmentAdvancebookingActivity.this);
            alert.setTitle("Info");
            alert.setMessage(message);
            alert.setCancelable(false);
            alert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isDialogVisible = false;
                                }
                            }, 1000);

                            dialog.dismiss();
                            if (previousamountText != null) {
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
        for (int i = 0; i < CropsFragmentAdvancebookingActivity.globalGroup.size(); i++) {
            List<Child> childList = CropsFragmentAdvancebookingActivity.globalGroup.get(i).childList;
            for (int j = 0; j < childList.size(); j++) {
                Child child = childList.get(j);
                if (child.isChecked) {
                    if (child.amount.length() > 0) {

                        total_amount = total_amount + Double.parseDouble(child.amount);

                    }
                }
            }

        }

        String token_amount = et_token_amount.getText().toString();
        if (advanceBookingAount != null)
            total_amount = Double.parseDouble(advanceBookingAount) + total_amount;

        if (token_amount.length() == 0) {
            showAlert("Please provide token amount.");
            return;
        } else {
            if (total_amount > Float.parseFloat(token_amount)) {
                et_booked_amount.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                showExceedAlert("Booked Amount should not exceed Token amount.");
                return;
            }
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            et_booked_amount.setText("" + total_amount);

        }

    }

    @Override
    public void beforeAmountChanged(final EditText amountText, final String quantity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                previousamountText = amountText;
                if (!isExceeded()) {
                    previousQuantity = quantity;
                }
            }
        }, 300);

    }

    private boolean isExceeded() {
        double total_amount = 0.00;
        for (int i = 0; i < CropsFragmentAdvancebookingActivity.globalGroup.size(); i++) {
            List<Child> childList = CropsFragmentAdvancebookingActivity.globalGroup.get(i).childList;
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
        return false;
    }

    private class Async_getTokenAmount extends AsyncTask<Void, Void, Void> {

        private String tokenAount;


        protected Void doInBackground(Void... params) {
            String selectQuery = "SELECT " +
                    " SO.advance_amount,SUM(SOD.advance_amount) AS sumprice" +
                    " FROM service_order_details SOD" +
                    " JOIN service_order SO ON SO." + KEY_TABLE_SERVICEORDER_ID + " = SOD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID +
                    " JOIN customers AS C ON C.customer_id = SO.customer_id" +
                    " JOIN division AS dv ON dv.division_id = SO.division_id" +
                    " LEFT JOIN " + TABLE_COMPANIES + " AS CM ON CM." + KEY_TABLE_COMPANIES_MASTER_ID + " = SO." + KEY_TABLE_SERVICEORDER_COMPANY_ID +
                    " WHERE SO.user_id =" + userId +
                    " AND SO.order_type =2 AND SO.division_id=" + division_id +
                    " AND SO.customer_id =" + customer_id + " AND SO." + KEY_TABLE_SERVICEORDER_COMPANY_ID + "=" + company_id;

            Log.e("selectQuery", selectQuery);
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    tokenAount = cursor.getString(0);
                    advanceBookingAount = cursor.getString(1);
                } while (cursor.moveToNext());

            } else Log.d("LOG", "returned null!");

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            // if(tokenAount!=null)
            // et_token_amount.setText(db.getTokenAmount(customer_id,division_id,Calendar.getInstance().get(Calendar.YEAR),"Advance Collection"));
            if (advanceBookingAount != null)
                et_booked_amount.setText("" + advanceBookingAount);

//            et_token_amount.setSelection(et_token_amount.getText().length());
//            et_booked_amount.setSelection(et_booked_amount.getText().length());


        }
    }


    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();

        String date;

        public ItemfavitemAdapter(Context pd, ArrayList<HashMap<String, String>> results) {
            this.context = pd;
            this.results = results;

        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_payment_history, parent, false);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
                holder.tv_comptype = (TextView) convertView.findViewById(R.id.tv_complainttype);
                holder.tv_billno = (TextView) convertView.findViewById(R.id.tv_billno);
                holder.tv_retailer = (TextView) convertView.findViewById(R.id.tv_retailername);
                holder.tvsr = (TextView) convertView.findViewById(R.id.tv_sr);
                holder.tvrtgschq = (TextView) convertView.findViewById(R.id.tv_rtgs_chq);
                holder.tvpaydate = (TextView) convertView.findViewById(R.id.tv_paydate);
                holder.tvamt = (TextView) convertView.findViewById(R.id.tv_amount);
                holder.tvrtgschqno = (TextView) convertView.findViewById(R.id.tv_rtgsno);
                holder.tvbankname = (TextView) convertView.findViewById(R.id.tv_bankname);
                holder.tv_paymemt_type = (TextView) convertView.findViewById(R.id.tv_paymemt_type);
                // holder.tv_aging1          = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
          /*  holder.tv_name.setText( results.get(position).get("name"));

            holder.tv_comptype.setText("Payment Type : " + results.get(position).get("type"));
            holder.tv_billno.setText("Payment Mode : " + results.get(position).get("mode"));*/
            holder.tvsr.setText("  " + results.get(position).get("srno") + ".");
            holder.tvbankname.setText(results.get(position).get("name"));
            holder.tvamt.setText(results.get(position).get("amount"));
            if (results.get(position).get("mode").equalsIgnoreCase("Online")) {
                holder.tvrtgschq.setText("RTGS/NEFT");
                holder.tvrtgschqno.setText(results.get(position).get("rtgs"));
            } else if (results.get(position).get("mode").equalsIgnoreCase("Cheque/dd")) {
                holder.tvrtgschq.setText("CHEQUE/DD");
                holder.tvrtgschqno.setText(results.get(position).get("chequeno"));
            }
            if (results.get(position).get("type").equalsIgnoreCase("1")) {
                holder.tv_paymemt_type.setText("ABS");

            } else {
                holder.tv_paymemt_type.setText("Normal");
            }

            date = results.get(position).get("pay_date");

            if (date.contains("00:00:00")) {
                date = date.substring(0, date.length() - 8);
                holder.tvpaydate.setText(date);
            } else {
                holder.tvpaydate.setText(date);
            }

            return convertView;
        }


        public class ViewHolder {
            public TextView tv_name, tv_place, tv_comptype, tv_billno, tv_retailer, tvsr, tvrtgschq, tvpaydate, tvrtgschqno, tvbankname, tvamt;
            public ImageView img;
            public TextView tv_paymemt_type;
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }

}
