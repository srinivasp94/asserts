package com.nsl.app;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.Companies;
import com.nsl.app.Crops;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;
import com.nsl.app.SelectedCities;
import com.nsl.app.commonutils.Common;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_SAP_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMER_DETAILS;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;


public class PaymentActivity extends AppCompatActivity {
    // JSON parser class

    ProgressDialog progressDialog;


    TextView  tv_name,tv_code;
    Spinner   spin_company,spin_customer,spin_distributor,spin_scheme;
    Button    btn_ok;

    String    sel_company_id,sel_customer_id,sel_product_id,sel_division_id,finalJson,company_id,customer_id,order_date,status,orderidfromserviceorder;
    String team,cust_dtls_master_id;
    private ArrayList<SelectedCities> organisations;
    ArrayList<String> adaptercity;

    private ArrayList<SelectedCities> arlist_products;
    ArrayList<String> adapter_products;

    private ArrayList<SelectedCities> arlist_crops;
    ArrayList<String> adapter_crops;

    private ArrayList<SelectedCities> arlist_schemes;
    ArrayList<String> adapter_schemes;

    String jsonData,str_qty,str_amount,userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> cropids = new ArrayList<HashMap<String, String>>();

    DateFormat dateFormat,orderdateFormat;
    Date       myDate;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    public static String companyname,customername,companycode,customercode,customerid,companyid,OSA;
    DatabaseHandler  db;
    SQLiteDatabase   sdbw,sdbr;
    private TextView tv_company,tv_division,tv_customer;
    private int positionDistributor;
    private ArrayList<Companies> companies;
    private ArrayList<Companies> companiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpaymentcollection);
        db                = new DatabaseHandler(this);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        Log.d("team: ",team);

        Toolbar toolbar   = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
       /* toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//* Intent dist = new Intent(PaymentActivity.this, DistributorsActivity.class);
                startActivity(dist);*//*
                finish();
            }
        });*/

        tv_company     = (TextView)findViewById(R.id.text_company);
        tv_customer    = (TextView)findViewById(R.id.text_customer);
        tv_division    = (TextView)findViewById(R.id.text_division);

        spin_company   = (Spinner)findViewById(R.id.spin_company);
        spin_customer  = (Spinner)findViewById(R.id.spin_customer);
        spin_distributor  = (Spinner)findViewById(R.id.spin_distributor);



        btn_ok    =(Button)findViewById(R.id.btn_savebooking);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  Log.e("ok",favouriteItem.get(0)+" "+favouriteItem.get(1));

if ( spin_distributor.getSelectedItemPosition()>0) {
    Intent booking = new Intent(getApplicationContext(), Payment_collection_detailsActivity.class);


    booking.putExtra("company_name", companyname);
    booking.putExtra("customer_code", arlist_schemes.get(spin_distributor.getSelectedItemPosition()).getCustomerCode());
    booking.putExtra("customer_id", arlist_schemes.get(spin_distributor.getSelectedItemPosition()).getCityId());
    booking.putExtra("customer_name", arlist_schemes.get(spin_distributor.getSelectedItemPosition()).getCityName());
    booking.putExtra("company_id",    companiesList.get(spin_company.getSelectedItemPosition()-1).getCompanyMasterId());
    Common.Log.i("company_id "+companiesList.get(spin_company.getSelectedItemPosition()-1).getCompanyMasterId());
           /*             booking.putExtra("company_code",  favouriteItem.get(spin_distributor.getSelectedItemPosition()-1).get("company_code"));
                        booking.putExtra("OSA",           favouriteItem.get(spin_distributor.getSelectedItemPosition()-1).get("OSA"));
*/
    startActivity(booking);
}else
{
    Toast.makeText(PaymentActivity.this,"Please select distributor",Toast.LENGTH_SHORT).show();
}
                    }




                /*Intent cropsproducts = new Intent(getApplicationContext(), CropsFragmentAdvancebookingActivity.class);
                startActivity(cropsproducts);*/

        });


        new AsyncGetofflineCompanies().execute();

    }


    public class AsyncGetofflineCompanies extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            organisations = new ArrayList<SelectedCities>();
            companiesList = new ArrayList<Companies>();
            adaptercity = new ArrayList<String>();
            organisations.clear();
            SelectedCities citiez = new SelectedCities();
            citiez.setCityId("0");
            citiez.setCityName("Select Company");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            organisations.add(citiez);
            adaptercity.add("Select Company");
            try {
                List<Crops> cdcList = new ArrayList<>();


                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME+ ","+ KEY_TABLE_COMPANIES_COMPANY_CODE+ ","+ KEY_TABLE_COMPANIES_COMPANY_SAP_ID + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Companies companies = new Companies();

                        companies.setCompanyMasterId(cursor.getString(0));
                        companies.setCompanyName(cursor.getString(1));
                        companies.setCompanycode(cursor.getString(3));
                        companies.setCompanysapid(cursor.getString(2));
                        companiesList.add(companies);
                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        organisations.add(cities2);
                        adaptercity.add(cursor.getString(1));

                    } while (cursor.moveToNext());
                }

                // do some stuff....
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
            spin_company.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));
            spin_company.setSelection(adaptercity.indexOf("Nuziveedu Seeds Ltd"));
            spin_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String dd = adaptercity.get(position);

                    sel_company_id = organisations.get(position).getCityId();
                //    tv_company.setTextColor(getResources().getColor(R.color.colorPrimary));
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_company_id.equalsIgnoreCase("0")) {

                    } else {
                        companycode = organisations.get(position).getCityId();
                        companyname = organisations.get(position).getCityName();
                        String selectQuerys = "SELECT  " + KEY_TABLE_COMPANIES_COMPANY_CODE  +" FROM " + TABLE_COMPANIES + " WHERE " + KEY_TABLE_COMPANIES_MASTER_ID + " = " + sel_company_id;
                        sdbw = db.getWritableDatabase();

                        Cursor cc = sdbw.rawQuery(selectQuerys, null);
                        //System.out.println("cursor count "+cursor.getCount());
                        if (cc != null && cc.moveToFirst()) {
                            Companies companies = new Companies();
                            companycode = cc.getString(0);
                            companies.setCompanycode(companycode);
                            //The 0 is the column index, we only have 1 column, so the index is 0

                        }



                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("company_id", sel_company_id);
                        editor.commit();
                        new AsyncDivisionsoffline().execute();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }



    public class AsyncDivisionsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_schemes = new ArrayList<SelectedCities>();
            adapter_schemes = new ArrayList<String>();
            arlist_schemes.clear();
            adapter_schemes.clear();

            SelectedCities citieszzy = new SelectedCities();
            citieszzy.setCityId("0");
            citieszzy.setCityName("Select Distributor");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_schemes.add(0,citieszzy);
            adapter_schemes.add(0,"Select Distributor");

            try {



                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID +","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE +","+ KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET+","+ KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET+  " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + " LEFT JOIN "+ TABLE_CUSTOMER_DETAILS + " AS CD ON CD."+ KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID+ "  where " + "CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_company_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID +" IN ("+ team +")"+  " group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")" ;

               // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_DIVISION_CODE + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " and " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + sel_company_id + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
                Log.e("Distributor QUERY", selectQuery);
                sdbw = db.getWritableDatabase();
                System.out.println(selectQuery);
                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        int insidebucket,outsidebucket;
                        cust_dtls_master_id = cursor.getString(0);

                        if(cursor.getString(3)!=null){
                            insidebucket = Integer.parseInt(cursor.getString(3));
                        }
                        else {
                            insidebucket = 0;
                        }

                        if(cursor.getString(4)!=null){
                            outsidebucket = Integer.parseInt(cursor.getString(4));
                        }
                        else{
                            outsidebucket = 0;
                        }

                        int osa = insidebucket+outsidebucket;

                        if (osa>0){
                            System.out.println("+++++++++++" + cursor.getString(0)+" , "+ cursor.getString(1)+" , "+ cursor.getString(2)+" , "+cursor.getString(3)+" , "+cursor.getString(4));
                            Log.e("OSA : ", String.valueOf(osa)+" : osa>0");

                            customername =  cursor.getString(1);
                            customerid   =  cursor.getString(0);
                            customercode = cursor.getString(2);
                            OSA          = String.valueOf(osa);

                            HashMap<String, String> map = new HashMap<String, String>();
                            // adding each child node to HashMap key => value
                            map.put("customer_name",    cursor.getString(1));
                            map.put("customer_id",      cursor.getString(0));

                            map.put("company",          "");
                            map.put("customer_code",    cursor.getString(2));
                            map.put("region_name",      "");
                            map.put("division_name",    "");
                            map.put("credit_limit",     "");
                            map.put("inside_bucket",    "");
                            map.put("outside_bucket",   "");
                            map.put("osa", String.valueOf(osa));


                            favouriteItem.add(map);
                        }
                        else{
                            Log.e("OSA : ", String.valueOf(osa)+"osa==0");
                        }
                        SelectedCities cities3 = new SelectedCities();
                        cities3.setCityId(cursor.getString(0));
                        cities3.setCityName(cursor.getString(1));
                        cities3.setCustomerCode(cursor.getString(2));

                        arlist_schemes.add(cities3);
                        adapter_schemes.add(cursor.getString(1));



                    } while (cursor.moveToNext());
                }

                // do some stuff....
            }  catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            spin_distributor.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_schemes));
            spin_distributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Common.Log.i(adapter_schemes.get(position)+" "+position +"\n "+arlist_schemes.get(position).getCityId());
                    positionDistributor=position;
         //           tv_division.setTextColor(getResources().getColor(R.color.colorPrimary));
                    sel_division_id = arlist_schemes.get(position).getCityId();
                    if (sel_division_id.equalsIgnoreCase("0")) {

                    } else {
                        customername = arlist_schemes.get(position).getCityName();
                        customercode = arlist_schemes.get(position).getCityId();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("division_id", sel_division_id);
                        editor.commit();

                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






}
