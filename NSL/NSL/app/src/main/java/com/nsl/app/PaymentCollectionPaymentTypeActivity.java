package com.nsl.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_PAYMENT_COLLECTION_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_PAYMENT_COLLECTION_PAYMENT_ID;
import static com.nsl.app.DatabaseHandler.TABLE_PAYMENT_COLLECTION;

public class PaymentCollectionPaymentTypeActivity extends AppCompatActivity {

    TextView tv_name, tv_code, tv_company, tv_division, tv_total_osa,tvInvisibleError,tvInvisibleError1,tvInvisibleError2;
    EditText pay_date, et_rtgs, et_cheqnum, et_date_oncheq, et_amt ;
    Spinner spin_paymode;
    Spinner spin_paytype;
    EditText et_bn;
    Button submit,refresh;
    int i=0,flag=0;
    String sqliteid,ffmid,ffmidsqlite;
    int payment,inside_bucket,outside_bucket;
    ProgressDialog  progressDialog;
    DatabaseHandler db ;
    String userId, comp_id, div_id, cust_id,jsonData,comp_code,sel_bank_id,customer_bank_details;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    private static SQLiteDatabase sql, sdbr,sdbw;
    private String[] payment_mode = {"Select Payment Mode", "Online", "Cheque/DD"};
    private String[] payment_type = {"Select Payment Type", "ABS", "On Account"};

    private ArrayList<SelectedCities> organisations;
    ArrayList<String> adaptercity;
    public  ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>();
    CustomAdapter adapter;
    PaymentCollectionPaymentTypeActivity activity = null;
    private String paymentDate;
    private String chequeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment_collection_payment_type);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adv = new Intent(getApplicationContext(), Payment_collection_detailsActivity.class);
                adv.putExtra("company_name",tv_company.getText().toString());
                adv.putExtra("customer_name",tv_name.getText().toString());
                adv.putExtra("customer_code",tv_code.getText().toString());
                adv.putExtra("customer_id",cust_id);
                adv.putExtra("company_id",comp_id);
                adv.putExtra("company_code",comp_code);
                startActivity(adv);
                finish();
            }
        });
        db = new DatabaseHandler(PaymentCollectionPaymentTypeActivity.this);
       // db.deleteCustomerbankdetails();

        userId                  = sharedpreferences.getString("userId", "");
        customer_bank_details   = sharedpreferences.getString("customer_bank_details", "");
        tv_name                 = (TextView) findViewById(R.id.tv_name);
        tv_code                 = (TextView) findViewById(R.id.tv_code);
        tv_company              = (TextView) findViewById(R.id.textViewcompany);
        tv_division             = (TextView) findViewById(R.id.textViewdivision);
        tv_total_osa            = (TextView) findViewById(R.id.textViewAmount);
        tvInvisibleError        = (TextView)findViewById(R.id.tvInvisibleError);
        tvInvisibleError1       = (TextView)findViewById(R.id.tvInvisibleError1);
        tvInvisibleError2       = (TextView)findViewById(R.id.tvInvisibleError2);
        submit                  = (Button) findViewById(R.id.btn_submit);
        et_amt                  = (EditText) findViewById(R.id.etp_amt);
        et_bn                   = (EditText) findViewById(R.id.etBank);
        cust_id                 = getIntent().getStringExtra("customer_id");
        comp_id                 = getIntent().getStringExtra("company_id");
        comp_code               = getIntent().getStringExtra("company_code");
        div_id                  = getIntent().getStringExtra("division_id");
        tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_code.setText(getIntent().getStringExtra("customer_code"));
        tv_total_osa.setText(getIntent().getStringExtra("total_osa"));
        tv_company.setText(getIntent().getStringExtra("company_name"));
        tv_division.setText(getIntent().getStringExtra("division"));
        String s_inside_bkt     = getIntent().getStringExtra("inside_bucket");

        Common.Log.i(comp_id+"PaymentCollection");
        if(s_inside_bkt==null){
            s_inside_bkt  = String.valueOf(0);
            inside_bucket =0;
        }
        else{
            inside_bucket = Integer.parseInt(s_inside_bkt);
        }

        String s_outside_bkt = getIntent().getStringExtra("outside_bucket");
        if (s_outside_bkt == null) {
            s_outside_bkt =  String.valueOf(0);
            outside_bucket=0;
        }
        else{
            outside_bucket = Integer.parseInt(s_outside_bkt);
        }
        Log.e("bucket : ",inside_bucket +" : " + outside_bucket);
        et_rtgs = (EditText) findViewById(R.id.etrtgs);
        et_cheqnum = (EditText) findViewById(R.id.etchq);
        et_date_oncheq = (EditText) findViewById(R.id.etdatechq);
        spin_paymode = (Spinner) findViewById(R.id.spin_payment_mode);
        spin_paytype = (Spinner) findViewById(R.id.spin_payment_type);
        pay_date = (EditText) findViewById(R.id.etdate);
        pay_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(PaymentCollectionPaymentTypeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int sel_month = selectedmonth + 1;
                        String sday = String.valueOf(selectedday);
                        String smonth = null;
                        if (sel_month < 10)
                            smonth = "0" + sel_month;
                        else
                            smonth = String.valueOf(sel_month);

                        if (selectedday < 10)
                            sday = "0" + selectedday;
                        else
                            sday = String.valueOf(selectedday);
                        paymentDate = selectedyear + "-" + smonth + "-" + sday + " 00:00:00";
                        pay_date.setText(Common.setDateFormateOnTxt(selectedyear + "-" + smonth + "-" + selectedday ));
                        //pay_date.setText(selectedyear + "-" + selmon + "-" + selectedday);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });
        et_date_oncheq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(PaymentCollectionPaymentTypeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub

                        int sel_month = selectedmonth + 1;
                        String sday = String.valueOf(selectedday);
                        String smonth = null;
                        if (sel_month < 10)
                            smonth = "0" + sel_month;
                        else
                            smonth = String.valueOf(sel_month);

                        if (selectedday < 10)
                            sday = "0" + selectedday;
                        else
                            sday = String.valueOf(selectedday);

                        chequeDate = selectedyear + "-" + smonth + "-" + sday + " 00:00:00";
                        et_date_oncheq.setText(Common.setDateFormateOnTxt(selectedyear + "-" + smonth + "-" + selectedday ));

                        //et_date_oncheq.setText(selectedyear + "-" + selectedmonth + "-" + selectedday);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });
        refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(Utility.isNetworkAvailable(PaymentCollectionPaymentTypeActivity.this,Constants.isShowNetworkToast)){
                        new Async_getallcustomerbankdetails().execute();
                    } else {

                        new AsyncGetofflineCustomerbank().execute();

                        }


                } catch (Exception e) {
                    Log.d("Tag", e.toString());
                }



            }
        });
        ArrayAdapter paytype = new ArrayAdapter(PaymentCollectionPaymentTypeActivity.this,
                R.layout.spinner_item, R.id.customSpinnerItemTextView, payment_type);
        spin_paytype.setAdapter(paytype);

        final ArrayAdapter paymode = new ArrayAdapter(PaymentCollectionPaymentTypeActivity.this,
                R.layout.spinner_item, R.id.customSpinnerItemTextView, payment_mode);
        spin_paymode.setAdapter(paymode);
        spin_paymode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payment = spin_paymode.getSelectedItemPosition();
                if (payment == 0) {

                }
                if (payment == 1) {
                    et_rtgs.setVisibility(View.VISIBLE);
                    et_cheqnum.setVisibility(View.GONE);
                    et_date_oncheq.setVisibility(View.GONE);
                } else if (payment == 2) {
                    et_cheqnum.setVisibility(View.VISIBLE);
                    et_date_oncheq.setVisibility(View.VISIBLE);
                    et_rtgs.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            public int paymentType;

            @Override
            public void onClick(View v) {
                validate();

            }



    private void validate() {
        Handler handler=Common.disableClickEvent(submit,true);

        String pay_amt = et_amt.getText().toString();
        sel_bank_id=et_bn.getText().toString().trim();
        if (TextUtils.isEmpty(pay_amt) || pay_amt.length() > 0 && pay_amt.startsWith(" ")) {
            et_amt.setError("Please enter Payment amount");
            et_amt.requestFocus();
            return;
        }
        String payment_type = spin_paytype.getSelectedItem().toString();
        if (spin_paytype.getSelectedItem().toString().trim().equalsIgnoreCase("Select payment type")) {

            // Toast.makeText(getActivity(), "Please select crop", Toast.LENGTH_SHORT).show();
            tvInvisibleError1.requestFocus();
            tvInvisibleError1.setError("Please select Payment Type");
            return;

        }
       /* if (et_bn.getText().toString().trim().length()==0) {

            // Toast.makeText(getActivity(), "Please select crop", Toast.LENGTH_SHORT).show();
            tvInvisibleError2.requestFocus();
            tvInvisibleError2.setError("Please enter Bank Name");
            return;

        }*/

      /*  String bank_name = et_bn.getText().toString();
        if (TextUtils.isEmpty(bank_name) || bank_name.length() > 0 && bank_name.startsWith(" ")) {
            et_bn.setError("Please enter Bank name");
            et_bn.requestFocus();
            return;
        }*/

        if (TextUtils.isEmpty( pay_date.getText().toString()) ||  pay_date.getText().toString().length() > 0 &&  pay_date.getText().toString().startsWith(" ")) {
            pay_date.requestFocus();
            pay_date.setError("Please enter Payment Date");

            return;
        }
        String paymode = spin_paymode.getSelectedItem().toString();
       /* if (spin_paymode.getSelectedItem().toString().trim().equalsIgnoreCase("Select payment mode")) {

            // Toast.makeText(getActivity(), "Please select crop", Toast.LENGTH_SHORT).show();
            tvInvisibleError.requestFocus();
            tvInvisibleError.setError("Please select Payment Mode");
            return;

        }*/
        if (paymode.equalsIgnoreCase("Online")) {
            {
                et_cheqnum.setVisibility(View.GONE);
                et_date_oncheq.setVisibility(View.GONE);
                et_rtgs.setVisibility(View.VISIBLE);
                String rtgs = et_rtgs.getText().toString();
                /*if (TextUtils.isEmpty(rtgs) || rtgs.length() > 0 && rtgs.startsWith(" ")) {
                    et_rtgs.setError("Please enter RTGS");
                    et_rtgs.requestFocus();
                    return;
                }*/

            }
        } else if (paymode.equalsIgnoreCase("Cheque/DD")) {
            et_cheqnum.setVisibility(View.VISIBLE);
            et_date_oncheq.setVisibility(View.VISIBLE);
            et_rtgs.setVisibility(View.GONE);
            String cheqno = et_cheqnum.getText().toString();
           /* if (TextUtils.isEmpty(cheqno) || cheqno.length() > 0 && cheqno.startsWith(" ")) {
                et_cheqnum.setError("Please enter Cheque/DD number");
                et_cheqnum.requestFocus();
                return;
            }*/
            //String cheqdate = et_date_oncheq.getText().toString() + " 00:00:00";
           /* if (TextUtils.isEmpty(et_date_oncheq.getText().toString()) || et_date_oncheq.getText().toString().length() > 0 && et_date_oncheq.getText().toString().startsWith(" ")) {
                et_date_oncheq.setError("Please enter Cheque date");
                et_date_oncheq.requestFocus();
                return;
            }*/
        }
        Common.disableClickEvent(submit,handler);
        paymentupdate();
    }

    private void paymentupdate() {

        String paymode = spin_paymode.getSelectedItem().toString();
        payment = spin_paytype.getSelectedItemPosition();
        if (payment==1){
            paymentType = Constants.PaymentTypes.PAYMENT_TYPES_ABS;
        }else if(payment==2){
            paymentType = Constants.PaymentTypes.PAYMENT_TYPES_NORMAL;
        }
        if(paymode.equalsIgnoreCase("Online")) {
            int pid = 1;
            int nothing = 0;
            String nothingstr = null;
            String pay_type = spin_paytype.getSelectedItem().toString();
            String amt = et_amt.getText().toString();

            String bank_name = et_bn.getText().toString();
            String paydate = paymentDate;

             String rtgs = et_rtgs.getText().toString();
           // String cheqno = et_cheqnum.getText().toString();
           // String cheqdate = et_date_oncheq.getText().toString() + " 00:00:00";
            if (comp_id==null){
                comp_id="1";
            }

            Log.e("__________", pid + "  " + paymentType + userId + comp_id + div_id + cust_id + amt + paymode + sel_bank_id +rtgs +paydate);
            db.addPaymentCollection(new Payment_collection(String.valueOf(pid), String.valueOf(paymentType), userId, comp_id, div_id, cust_id, amt, paymode, sel_bank_id, rtgs, paydate, nothingstr, nothingstr, nothing, nothingstr, nothingstr, null));
           Log.e("__________", pid + "  " + paymentType + userId + comp_id + div_id + cust_id + amt + paymode + sel_bank_id +rtgs +paydate);

            try {
                ConnectivityManager conMgr = (ConnectivityManager) PaymentCollectionPaymentTypeActivity.this
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if(Utility.isNetworkAvailable(PaymentCollectionPaymentTypeActivity.this,Constants.isShowNetworkToast)){
                    new Async_InsertPaymentCollection().execute();
                }
                else {
                    Toast.makeText(PaymentCollectionPaymentTypeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentCollectionPaymentTypeActivity.this);

                    // set title
                    alertDialogBuilder.setTitle("Payment Success");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Your Payment is successfull!")
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    Intent adv = new Intent(getApplicationContext(), Payment_collection_detailsActivity.class);
                                    adv.putExtra("customer_name",tv_name.getText().toString());
                                    adv.putExtra("customer_code",tv_code.getText().toString());
                                    adv.putExtra("customer_id",cust_id);
                                    adv.putExtra("company_code",comp_code);
                                    startActivity(adv);
                                    finish();

                                }
                            });


                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }
        else if (paymode.equalsIgnoreCase("Cheque/DD")) {
        int pid = 2;
        int nothing = 0;
        String nothingstr = null;
        String pay_type = spin_paytype.getSelectedItem().toString();
        String amt = et_amt.getText().toString();

            String bank_name = et_bn.getText().toString();
            String paydate = paymentDate;

            // String rtgs = et_rtgs.getText().toString();
            String cheqno = et_cheqnum.getText().toString();
            String cheqdate = chequeDate;


            db.addPaymentCollection(new Payment_collection(String.valueOf(pid), String.valueOf(paymentType), userId, comp_id, div_id, cust_id, amt, paymode, sel_bank_id, nothingstr, paydate, cheqdate, cheqno, nothing, nothingstr, nothingstr, null));
            Log.e("__________", pid + "  " + paymentType);
            Log.d("Reading: ", "Reading all Payment collection..");

            List<Payment_collection> paymentCollections = db.getAllPaymentCollection(userId);

            for (Payment_collection pc : paymentCollections) {
                String log = "Id: " + pc.getID() + " ,paymentType: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() +
                        " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() +
                        " ,amt : " + pc.get_total_amount() + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() +
                        "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() +
                        ",cheque number:" + pc.get_cheque_no_dd_no() + ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() +
                        ",updated datetime:" + pc.get_updated_datetime() + ",ffmid:" + pc.get_ffmid();

                Log.e("payment collection: ", log);

                try {
                   if (Common.haveInternet(getApplicationContext())){
                            new Async_InsertPaymentCollectioncheq().execute();
                        }
                     else {
                        Toast.makeText(PaymentCollectionPaymentTypeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentCollectionPaymentTypeActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("Payment Success");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Your Payment is successfull!")
                                .setCancelable(false)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        Intent adv = new Intent(getApplicationContext(), Payment_collection_detailsActivity.class);
                                        adv.putExtra("customer_name",tv_name.getText().toString());
                                        adv.putExtra("customer_code",tv_code.getText().toString());
                                        adv.putExtra("customer_id",cust_id);
                                        adv.putExtra("company_code",comp_code);
                                        adv.putExtra("company_id",comp_id);
                                        startActivity(adv);

                                    }
                                });


                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }

                } catch (Exception e) {
                    Log.d("Tag", e.toString());
                }
            }
        }else {

                int pid = 1;
                int nothing = 0;
                String nothingstr = null;
                String pay_type = spin_paytype.getSelectedItem().toString();
                String amt = et_amt.getText().toString();

                String bank_name = et_bn.getText().toString();
                String paydate = paymentDate;

                String rtgs = et_rtgs.getText().toString();
                // String cheqno = et_cheqnum.getText().toString();
                // String cheqdate = et_date_oncheq.getText().toString() + " 00:00:00";
                if (comp_id==null){
                    comp_id="1";
                }

               // Log.e("__________", pid + "  " + paymentType + userId + comp_id + div_id + cust_id + amt + paymode + sel_bank_id +rtgs +paymentDate);
                db.addPaymentCollection(new Payment_collection(String.valueOf(pid), String.valueOf(paymentType), userId, comp_id, div_id, cust_id, amt, paymode, sel_bank_id, rtgs, paydate, nothingstr, nothingstr, nothing, nothingstr, nothingstr, null));
               // Log.e("__________", pid + "  " + paymentType + userId + comp_id + div_id + cust_id + amt + paymode + sel_bank_id +rtgs +paymentDate);

                try {
                    ConnectivityManager conMgr = (ConnectivityManager) PaymentCollectionPaymentTypeActivity.this
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                    if(Utility.isNetworkAvailable(PaymentCollectionPaymentTypeActivity.this,Constants.isShowNetworkToast)){
                        new Async_InsertPaymentCollection().execute();
                    }
                    else {
                        Toast.makeText(PaymentCollectionPaymentTypeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentCollectionPaymentTypeActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("Payment Success");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Your Payment is successfull!")
                                .setCancelable(false)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        Intent adv = new Intent(getApplicationContext(), Payment_collection_detailsActivity.class);
                                        adv.putExtra("customer_name",tv_name.getText().toString());
                                        adv.putExtra("customer_code",tv_code.getText().toString());
                                        adv.putExtra("customer_id",cust_id);
                                        adv.putExtra("company_code",comp_code);
                                        startActivity(adv);
                                        finish();

                                    }
                                });


                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                    }

                } catch (Exception e) {
                    Log.d("Tag", e.toString());
                }



        }

    }
        });

        if (customer_bank_details != null && customer_bank_details.equalsIgnoreCase("true")) {
            new AsyncGetofflineCustomerbank().execute();
        }
        else {
            new AsyncGetofflineCustomerbank().execute();
        }


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void insertToService() {

        Log.d("Reading: ", "Reading all Payment collection..");

        List<Payment_collection> paymentCollections = db.getAllPaymentCollection(userId);

        for (Payment_collection pc : paymentCollections) {
            String log = "Id: " + pc.getID() + " ,paymentType: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() + " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() + " ,amt : " + pc.get_total_amount()
                    + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() + "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() + ",cheque number:" + pc.get_cheque_no_dd_no() +
                    ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() + ",updated datetime:" + pc.get_updated_datetime();

            Log.e("**payment collection: ", log);
            // String ffmid = fb.get_ffmid();
            // if(!ffmid.equals(null)){
            new Async_InsertPaymentCollection().execute();
          //  new Async_InsertPaymentCollectioncheq().execute();

            //  }

        }
    }

    public class Async_InsertPaymentCollection extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentCollectionPaymentTypeActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {


                Log.d("Reading: ", "Reading all Payment collection..");

                List<Payment_collection> paymentCollections = db.getAllPaymentCollection(userId);

                for (Payment_collection pc : paymentCollections) {
                    String log = "Id: " + pc.getID() + " ,paymentType: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() + " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() + " ,amt : " + pc.get_total_amount()
                            + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() + "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() + ",cheque number:" + pc.get_cheque_no_dd_no() +
                            ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() + ",updated datetime:" + pc.get_updated_datetime();

                    Log.e("**payment collection: ", log);
                    String payment_type = pc.get_payment_type();

                    OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("id",String.valueOf(pc.getID()))
                            .add("sqlite_id",String.valueOf(pc.getID()))
                            .add("payment_type",String.valueOf(payment_type))
                            .add("user_id", String.valueOf(pc.get_user_id()))
                            .add("bank_name",sel_bank_id)
                            .add("company_id",String.valueOf(pc.get_company_id()))
                            .add("division_id",String.valueOf(pc.get_division_id()))
                            .add("payment_datetime",pc.get_payment_datetime())
                            .add("customer_id",String.valueOf(pc.get_customer_id()))
                            .add("total_amount",String.valueOf(pc.get_total_amount()))
                            .add("payment_mode",pc.get_payment_mode())
                            .add("rtgs_or_neft_no",pc.get_rtgs_or_neft_no())
                            .add("created_by", String.valueOf(pc.get_user_id()))
                            .build();

                    Response responses = null;

                    System.out.println("---- Payment Collection data -----" + pc.getID() + payment_type + pc.get_user_id() + pc.get_company_id()+pc.get_bank_name() + pc.get_division_id() + pc.get_customer_id() + pc.get_total_amount()+pc.get_payment_mode()+ pc.get_rtgs_or_neft_no());

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
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
                        System.out.println("!!!!!!!1 InsertPaymentCollection" + jsonData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")){
                        // Toast.makeText(PaymentCollectionPaymentTypeActivity.this,"Payment Collection inserted sucessfully",Toast.LENGTH_SHORT).show();
                        sqliteid = jsonobject.getString("sqlite");
                        ffmid = jsonobject.getString("ffm_id");
                        // Toast.makeText(PaymentCollectionPaymentTypeActivity.this, "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", sqliteid);
                        Log.e("ffmid", ffmid);
                        sdbw = db.getWritableDatabase();
                        // updateFeedback(Feedback feedback);
                        String updatequery = "UPDATE " + TABLE_PAYMENT_COLLECTION + " SET " + KEY_PAYMENT_COLLECTION_FFMID + " = " + ffmid + " WHERE " + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " = " + sqliteid;
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);
                        List<Payment_collection> paymentCollections = db.getAllPaymentCollection(userId);

                        for (Payment_collection pc : paymentCollections) {
                            String log = "Id: " + pc.getID() + " ,paymentType: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() + " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() + " ,amt : " + pc.get_total_amount()
                                    + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() + "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() + ",cheque number:" + pc.get_cheque_no_dd_no() +
                                    ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() + ",updated datetime:" + pc.get_updated_datetime()+",ffmid:"+pc.get_ffmid();

                            Log.e("paymentcollectionaftr: ", log);
                        }

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentCollectionPaymentTypeActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("Payment Success");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Your Payment is successfull!")
                                .setCancelable(false)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        Intent adv = new Intent(getApplicationContext(), Payment_collection_detailsActivity.class);
                                        adv.putExtra("customer_name",tv_name.getText().toString());
                                        adv.putExtra("customer_code",tv_code.getText().toString());
                                        adv.putExtra("customer_id",cust_id);
                                        adv.putExtra("company_code",comp_code);
                                        adv.putExtra("company_id",comp_id);
                                        startActivity(adv);
                                        finish();

                                    }
                                });


                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
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

    public class Async_InsertPaymentCollectioncheq extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentCollectionPaymentTypeActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {


                Log.d("Reading: ", "Reading all Payment collection..");

                List<Payment_collection> paymentCollections = db.getAllPaymentCollection(userId);

                for (Payment_collection pc : paymentCollections) {
                    String log = "Id: " + pc.getID() + " ,paymentType: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() + " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() + " ,amt : " + pc.get_total_amount()
                            + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() + "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() + ",cheque number:" + pc.get_cheque_no_dd_no() +
                            ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() + ",updated datetime:" + pc.get_updated_datetime();

                    Log.e("payment collection c : ", log);
                    String payment_type = pc.get_payment_type();

                    OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("id",String.valueOf(pc.getID()))
                            .add("sqlite_id",String.valueOf(pc.getID()))
                            .add("payment_type",payment_type)
                            .add("user_id", String.valueOf(pc.get_user_id()))
                            .add("bank_name",sel_bank_id)
                            .add("company_id",String.valueOf(pc.get_company_id()))
                            .add("division_id",String.valueOf(pc.get_division_id()))
                            .add("payment_datetime",pc.get_payment_datetime())
                            .add("customer_id",String.valueOf(pc.get_customer_id()))
                            .add("total_amount",String.valueOf(pc.get_total_amount()))
                            .add("payment_mode",String.valueOf(pc.get_payment_mode()))
                            .add("cheque_no_dd_no",String.valueOf(pc.get_cheque_no_dd_no()))
                            .add("date_on_cheque_no",String.valueOf(pc.get_date_on_cheque_no()))
                            .build();

                    Response responses = null;

                    System.out.println("---- Payment Collection cheque data -----" + pc.getID() + payment_type + pc.get_user_id() + pc.get_company_id()+pc.get_bank_name() + pc.get_division_id() + pc.get_customer_id() + pc.get_total_amount()+pc.get_payment_mode()+ pc.get_cheque_no_dd_no()+ pc.get_date_on_cheque_no());

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
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
                        System.out.println("!!!!!!!1 InsertPaymentCollectioncheque" + jsonData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")){
                        //  Toast.makeText(PaymentCollectionPaymentTypeActivity.this,"Payment Collection inserted sucessfully",Toast.LENGTH_SHORT).show();
                        sqliteid = jsonobject.getString("sqlite");
                        ffmid = jsonobject.getString("ffm_id");

                        ///   Toast.makeText(PaymentCollectionPaymentTypeActivity.this, "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", sqliteid);
                        Log.e("ffmid", ffmid);
                        sdbw = db.getWritableDatabase();
                        // updateFeedback(Feedback feedback);
                        String updatequery = "UPDATE " + TABLE_PAYMENT_COLLECTION + " SET " + KEY_PAYMENT_COLLECTION_FFMID + " = " + ffmid + " WHERE " + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " = " + sqliteid;
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);
                        List<Payment_collection> paymentCollections = db.getAllPaymentCollection(userId);

                        for (Payment_collection pc : paymentCollections) {
                            String log = "Id: " + pc.getID() + " ,paymentType: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() + " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() + " ,amt : " + pc.get_total_amount()
                                    + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() + "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() + ",cheque number:" + pc.get_cheque_no_dd_no() +
                                    ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() + ",updated datetime:" + pc.get_updated_datetime()+",ffmid:"+pc.get_ffmid();

                            Log.e("paymentcollectionaftr: ", log);
                        }
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentCollectionPaymentTypeActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("Payment Success");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Your Payment is successfull!")
                                .setCancelable(false)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        Intent adv = new Intent(getApplicationContext(), Payment_collection_detailsActivity.class);
                                        adv.putExtra("customer_name",tv_name.getText().toString());
                                        adv.putExtra("customer_code",tv_code.getText().toString());
                                        adv.putExtra("customer_id",cust_id);
                                        adv.putExtra("company_code",comp_code);
                                        adv.putExtra("company_id",comp_id);
                                        startActivity(adv);

                                    }
                                });


                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
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


    private class Async_getallcustomerbankdetails extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected String doInBackground(Void... params) {
                db.deleteCustomerbankdetails();
                //odb.delete(TABLE_DIVISION,null,null);
                int cnt=0 ;
                try {
                    OkHttpClient client = new OkHttpClient();


                    Response responses = null;


                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                    Request request = new Request.Builder()
                            .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_CUSTOMER_BANKDETAILS)
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
                            Log.e("Length payment", String.valueOf(companyarray.length()));

                            for (int n = 0; n < companyarray.length(); n++) {
                                JSONObject objinfo = companyarray.getJSONObject(n);

                                String bank_detail_id      = objinfo.getString("bank_detail_id");
                                String customer_id    = objinfo.getString("customer_id");
                                String account_no    = objinfo.getString("account_no");
                                String account_name  = objinfo.getString("account_name");
                                String bank_name           = objinfo.getString("bank_name");
                                String branch_name = objinfo.getString("branch_name");
                                String ifsc_code = objinfo.getString("ifsc_code");
                                String status  = objinfo.getString("status");
                                String created_by           = objinfo.getString("created_by");
                                String updated_by = objinfo.getString("updated_by");
                                String created_date     = objinfo.getString("created_date");


                                db.addCustomersBankDetails(new Customer_Bank_Details(bank_detail_id, customer_id, account_no, account_name, bank_name, branch_name, ifsc_code,status,created_by,updated_by,created_date,ffmid));




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
                //  bar.setVisibility(View.GONE);

                Log.d("after insert: ", "");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("customer_bank_details", "true");
                editor.commit();

                 new AsyncGetofflineCustomerbank().execute();



               // adapter = new FeedbackallActivity.ItemfavitemAdapter(PaymentCollectionPaymentTypeActivity.this, favouriteItem);
                //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
               // listView.setAdapter(adapter);


            }
        }

    public class AsyncGetofflineCustomerbank extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentCollectionPaymentTypeActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            organisations = new ArrayList<SelectedCities>();
            adaptercity = new ArrayList<String>();
            organisations.clear();
            SelectedCities citiez = new SelectedCities();
            citiez.setCityId("0");
            citiez.setCityName("Select Bank Name");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            organisations.add(citiez);
            adaptercity.add("Select Bank Name");

            try {


                List<Customer_Bank_Details> fdbk = db.getAllCustomerbankdetails(cust_id);

                for (Customer_Bank_Details cm : fdbk) {
                    String log = "Id: " + cm.getID() + "master: " + cm.get_cus_bak_dtls_masterid() + " cust: " + cm.get_customer_id() + " ,acct: " + cm.get_account_no() + ",acct name:" + cm.get_account_name() + " ,bank: " + cm.get_bank_name() + ",branch:" +
                            cm.get_branch_name() + ",ifsc" + cm.get_ifsc_code() + ",status:" + cm.get_status() + ",createdby" + cm.get_created_by() + ",updatedby " + cm.get_updated_by() + ",createddate " + cm.get_created_date();
                    Log.e("details: ", log);
                    // Log.e("code name sap_id",company_code+":"+name +":"+company_sap_id);
                    SelectedCities cities2 = new SelectedCities();
                    cities2.setCityId(cm.get_cus_bak_dtls_masterid());
                    cities2.setCityName(cm.get_bank_name() + "\n" + "\n" + "\n" + cm.get_ifsc_code() + "\n\n" + cm.get_account_no());
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    organisations.add(cities2);
                    adaptercity.add("" + cm.get_bank_name() + "\n              " + "\nAccount No. : " + cm.get_account_no() + "\nIFSC : " + cm.get_ifsc_code() + "\n");
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
            // adapter.updateResults(arrayList);


           /* et_bn.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.customer_bank_spinner_item,R.id.customSpinnerItemTextView, adaptercity));
            et_bn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_bank_id = organisations.get(position).getCityId();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                //    Toast.makeText(PaymentCollectionPaymentTypeActivity.this,"selected id is "+sel_bank_id, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_bank_id.equalsIgnoreCase("0")){

                    }else {


                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }
    }


    class CustomAdapter extends ArrayAdapter<String>{

        private Activity activity;
        private ArrayList data;
        public Resources res;
        SpinnerModel tempValues=null;
        LayoutInflater inflater;

        /*************  CustomAdapter Constructor *****************/
        public CustomAdapter(
                PaymentCollectionPaymentTypeActivity activitySpinner,
                int textViewResourceId,
                ArrayList objects,
                Resources resLocal
        )
        {
            super(activitySpinner, textViewResourceId, objects);

            /********** Take passed values **********/
            activity = activitySpinner;
            data     = objects;
            res      = resLocal;

            /***********  Layout inflator to call external xml layout () **********************/
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.spinner_rows, parent, false);

            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (SpinnerModel) data.get(position);

            TextView bankname        = (TextView)row.findViewById(R.id.tv_bankname);
            TextView tv_accountno          = (TextView)row.findViewById(R.id.tv_accountno);
            TextView tv_ifsc        = (TextView)row.findViewById(R.id.tv_ifsc);


            if(position==0){

                // Default selected Spinner item
                bankname.setText("Please Select Bank");
                tv_accountno.setText("");
            }
            else
            {
                // Set values for spinner each row



            }

            return row;
        }
    }
    }

