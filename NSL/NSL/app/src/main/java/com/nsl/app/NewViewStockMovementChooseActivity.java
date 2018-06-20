package com.nsl.app;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.nsl.app.commonutils.Common;
import com.nsl.app.stockexchange.StockInformation;

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
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewViewStockMovementChooseActivity extends AppCompatActivity {
    // JSON parser class

    ProgressDialog progressDialog;


    TextView tv_name, tv_code;
    Spinner spin_company, spin_customer, spin_division, spin_scheme;
    Button btn_savebooing;

    String sel_company_id, sel_customer_id, sel_product_id, sel_division_id, finalJson, company_id, customer_id, order_date, status, orderidfromserviceorder;

    private ArrayList<SelectedCities> organisations;
    ArrayList<String> adaptercity;

    private ArrayList<SelectedCities> arlist_products;
    ArrayList<String> adapter_products;

    private ArrayList<SelectedCities> arlist_crops;
    ArrayList<String> adapter_crops;

    private ArrayList<SelectedCities> arlist_schemes;
    ArrayList<String> adapter_schemes;

    String jsonData, str_qty, str_amount, userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> cropids = new ArrayList<HashMap<String, String>>();

    DateFormat dateFormat, orderdateFormat;
    Date myDate;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    public static String companyname, customername, companycode, divisionname;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    private TextView tv_company, tv_division, tv_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stockmovementchoose);
        db = new DatabaseHandler(this);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
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

        tv_company = (TextView) findViewById(R.id.text_company);
        tv_customer = (TextView) findViewById(R.id.text_customer);
        tv_division = (TextView) findViewById(R.id.text_division);

        spin_company = (Spinner) findViewById(R.id.spin_company);
        spin_customer = (Spinner) findViewById(R.id.spin_customer);
        spin_division = (Spinner) findViewById(R.id.spin_division);

        if (getIntent().getExtras().getString("selection", "defaultKey").equalsIgnoreCase("distributor")) {
            tv_customer.setVisibility(View.GONE);
            spin_customer.setVisibility(View.GONE);
        }

        btn_savebooing = (Button) findViewById(R.id.btn_savebooking);
        btn_savebooing.setText("View Stock Supply");
        btn_savebooing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getIntent().getExtras().getString("selection", "defaultKey").equalsIgnoreCase("distributor")) {
                    if (sel_company_id == null || sel_company_id == "0" || sel_division_id == null || sel_division_id == "0") {
                        Toast.makeText(NewViewStockMovementChooseActivity.this, "Please select Company, Division and go to booking ", Toast.LENGTH_SHORT).show();
                    }
                /*else if (sel_customer_id == null || sel_customer_id == "0") {
                    Toast.makeText(NewAdvancebokingChooseActivity.this, "Please select Customer", Toast.LENGTH_SHORT).show();
                } else if (sel_division_id == null || sel_division_id == "0") {
                    Toast.makeText(NewAdvancebokingChooseActivity.this, "Please select Division", Toast.LENGTH_SHORT).show();
                }*/
                    else {
                        //Intent cropsproducts = new Intent(getApplicationContext(), CropsFragmentViewStockMovementActivity.class);
                        Intent cropsproducts = new Intent(getApplicationContext(), StockInformation.class);
                        startActivity(cropsproducts);
                    }
                } else {
                    if (sel_company_id == null || sel_company_id == "0" || sel_customer_id == null || sel_customer_id == "0" || sel_division_id == null || sel_division_id == "0") {
                        Toast.makeText(NewViewStockMovementChooseActivity.this, "Please select Company,Customer& Division and go to booking ", Toast.LENGTH_SHORT).show();
                    }
                /*else if (sel_customer_id == null || sel_customer_id == "0") {
                    Toast.makeText(NewAdvancebokingChooseActivity.this, "Please select Customer", Toast.LENGTH_SHORT).show();
                } else if (sel_division_id == null || sel_division_id == "0") {
                    Toast.makeText(NewAdvancebokingChooseActivity.this, "Please select Division", Toast.LENGTH_SHORT).show();
                }*/
                    else {
                        //Intent cropsproducts = new Intent(getApplicationContext(), CropsFragmentViewStockMovementActivity.class);
                        Intent cropsproducts = new Intent(getApplicationContext(), StockInformation.class);
                        startActivity(cropsproducts);
                    }
                }

                /*Intent cropsproducts = new Intent(getApplicationContext(), CropsFragmentAdvancebookingActivity.class);
                startActivity(cropsproducts);*/
            }
        });


        new AsyncGetofflineCompanies().execute();

    }


    public class AsyncGetofflineCompanies extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewViewStockMovementChooseActivity.this);
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
            citiez.setCityName("Select Company");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            organisations.add(citiez);
            adaptercity.add("Select Company");
            try {
                List<Crops> cdcList = new ArrayList<>();


                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + "," + KEY_TABLE_COMPANIES_NAME + "," + KEY_TABLE_COMPANIES_COMPANY_CODE + "," + KEY_TABLE_COMPANIES_COMPANY_SAP_ID + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = CR." + KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")";
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
            spin_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_company_id = organisations.get(position).getCityId();
                    tv_company.setTextColor(getResources().getColor(R.color.colorPrimary));
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_company_id.equalsIgnoreCase("0")) {

                    } else {
                        companycode = organisations.get(position).getCityName();
                        companyname = organisations.get(position).getCityName();
                        String selectQuerys = "SELECT  " + KEY_TABLE_COMPANIES_COMPANY_CODE + " FROM " + TABLE_COMPANIES + " WHERE " + KEY_TABLE_COMPANIES_MASTER_ID + " = " + sel_company_id;
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

    public class AsyncCustomersoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewViewStockMovementChooseActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_crops = new ArrayList<SelectedCities>();
            adapter_crops = new ArrayList<String>();
            arlist_crops.clear();

            SelectedCities citiesz = new SelectedCities();
            citiesz.setCityId("0");
            citiesz.setCityName(Common.getStringResourceText(R.string.select_distributor));
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_crops.add(citiesz);
            adapter_crops.add(Common.getStringResourceText(R.string.select_distributor));
            try {
                List<Crops> cdcList = new ArrayList<>();

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + "CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_company_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
                // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Customers customers = new Customers();

                        customers.setCusMasterID(cursor.getString(0));
                        customers.setCusName(cursor.getString(1));
                        customers.setCuscode(cursor.getString(2));


                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_crops.add(cities2);
                        adapter_crops.add(cursor.getString(1));
                        System.out.println("+++++++++++" + cursor.getString(0) + cursor.getString(1) + cursor.getString(2));


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
            spin_customer.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_crops));
            spin_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_customer_id = arlist_crops.get(position).getCityId();
                    tv_customer.setTextColor(getResources().getColor(R.color.colorPrimary));
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_customer_id.equalsIgnoreCase("0")) {

                    } else {
                        customername = arlist_crops.get(position).getCityName();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("customer_id", sel_customer_id);
                        editor.putString("customer_name", arlist_crops.get(position).getCityName());
                        editor.commit();
                        // new AsyncDivisionsoffline().execute();
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
            progressDialog = new ProgressDialog(NewViewStockMovementChooseActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_schemes = new ArrayList<SelectedCities>();
            adapter_schemes = new ArrayList<String>();
            arlist_schemes.clear();

            SelectedCities citieszzy = new SelectedCities();
            citieszzy.setCityId("0");
            citieszzy.setCityName("Select Division");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_schemes.add(citieszzy);
            adapter_schemes.add("Select Division");

            try {

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_DIVISION_CODE + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " and " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + sel_company_id + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
                Log.e("DIVISIONS QUERY", selectQuery);
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {

                        Divisions divisions = new Divisions();
                        divisions.setDivMasterID(cursor.getString(0));
                        divisions.setDivName(cursor.getString(1));

                        SelectedCities cities3 = new SelectedCities();
                        cities3.setCityId(cursor.getString(0));
                        cities3.setCityName(cursor.getString(1));

                        arlist_schemes.add(cities3);
                        adapter_schemes.add(cursor.getString(1));

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

            spin_division.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_schemes));
            spin_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tv_division.setTextColor(getResources().getColor(R.color.colorPrimary));
                    sel_division_id = arlist_schemes.get(position).getCityId();
                    if (sel_division_id.equalsIgnoreCase("0")) {

                    } else {
                        divisionname = arlist_schemes.get(position).getCityName();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("division_id", sel_division_id);
                        editor.commit();
                        //new AsyncCustomersoffline().execute();
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
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
