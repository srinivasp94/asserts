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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_CROP_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANY_DIVISION_CROPS;
import static com.nsl.app.DatabaseHandler.TABLE_CROPS;
import static com.nsl.app.DatabaseHandler.TABLE_PRODUCTS;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlannerTaskActivity extends AppCompatActivity {
    // JSON parser class

    ProgressDialog progressDialog;


    TextView  tv_name,tv_code;
    Spinner   spin_division,spin_crop,spin_product,spin_scheme;
    EditText  et_amount,et_quantity;
    Button    btn_savebooing;

    String    sel_division_id,sel_crop_id,sel_product_id,sel_scheme_id,finalJson,company_id,customer_id,order_date,status,orderidfromserviceorder;

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

    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_task);
        db = new DatabaseHandler(this);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
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


        userId            = sharedpreferences.getString("userId", "");
        /*company_id        = getIntent().getStringExtra("company_id");
        customer_id       = getIntent().getStringExtra("customer_id");
        tv_name           = (TextView)findViewById(R.id.tv_companyname);
        tv_code           = (TextView)findViewById(R.id.tv_code);
        spin_division     = (Spinner)findViewById(R.id.spin_division);
        spin_crop         = (Spinner)findViewById(R.id.spin_crop);
        spin_product      = (Spinner)findViewById(R.id.spin_product);
        spin_scheme       = (Spinner)findViewById(R.id.spin_scheme);
        et_amount         = (EditText)findViewById(R.id.et_amount);
        et_quantity       = (EditText)findViewById(R.id.et_quantity);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderdateFormat = new SimpleDateFormat("yyyy-MM-dd");
        myDate = new Date();*/
        /*et_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus && et_amount.isEnabled() && et_amount.isFocusable()) {
                    et_amount.post(new Runnable() {
                        @Override
                        public void run() {
                            final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(et_amount,InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
                }
            }
        });*/
        btn_savebooing    =(Button)findViewById(R.id.btn_savebooking);




    }


    public class AsyncGetofflineDivisions extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlannerTaskActivity.this);
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
            citiez.setCityName("Select Division");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            organisations.add(citiez);
            adaptercity.add("Select Division");

            try {


                List<Divisions> divisions = db.getAllDivisions();

                for (Divisions div : divisions) {
                    String log = "Id: "+div.getID()+ "Div master ID"+div.getDivMasterId()+" ,Name: " + div.getDivName()+ " ,div code: " + div.getDivcode()+ " ,div sapid: " + div.getDivsapid()+ " ,div status: " + div.getDivstatus()+ " ,div created date: " + div.getDivcdatetime()+ " ,div updated date : " + div.getDivudatetime();
                    // Writing Contacts to log
                    Log.e("DIVISIONS: ", log);

                    // Log.e("code name sap_id",company_code+":"+name +":"+company_sap_id);
                    SelectedCities cities2 = new SelectedCities();
                    cities2.setCityId(div.getDivMasterId());
                    cities2.setCityName(div.getDivName());
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    organisations.add(cities2);
                    adaptercity.add(div.getDivName());


                }
                /*db = new DatabaseHandler(getApplicationContext());
                String selectQuery = "SELECT  * FROM " + db.TABLE_DIVISION + " where " + db.KEY_TABLE_DIVISION_MASTER_ID + " = 2 " ;
                //	String selectQuery = "SELECT  * FROM " + TABLE_DIVISION + " where " + KEY_TABLE_DIVISION_MASTER_ID + " = " + example + "";

                db.getWritableDatabase();*/






            }  catch (Exception e)
            {
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
            spin_division.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,R.id.customSpinnerItemTextView, adaptercity));
            spin_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_division_id = organisations.get(position).getCityId();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_division_id.equalsIgnoreCase("0")){

                    }else {
                        new AsyncCropsoffline().execute();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public class AsyncCropsoffline extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlannerTaskActivity.this);
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
            citiesz.setCityName("Select Crop");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_crops.add(citiesz);
            adapter_crops.add("Select Crop");


            try {

                List<Crops> cdcList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                String selectQuery  = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME +  " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;

                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Crops crops = new Crops();

                        crops.setCropMasterID(cursor.getString(0));

                        crops.setCropName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_crops.add(cities2);
                        adapter_crops.add(cursor.getString(1));



                    } while (cursor.moveToNext());
                }

                  /*for (int l=0; l<=cropids.size();l++){
                      sdbr =db.getReadableDatabase();

                      Cursor cursors = sdbr.query(TABLE_CROPS, new String[] { KEY_TABLE_CROPS_CROP_ID,
                                      KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME }, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                              new String[] { String.valueOf(cropids.get(l).get("crop_id")) }, null, null, null, null);
                      if (cursors != null)
                          cursors.moveToFirst();

                      Crops crops = new Crops(Integer.parseInt(cursors.getString(0)),cursors.getString(1), cursors.getString(2), cursors.getString(3), cursors.getString(4), cursors.getString(5), cursors.getString(6),cursors.getString(7));
                      // return contact
                      Log.v("Cropidis xyz",cursors.getString(1)+cursors.getString(2));
                  }*/


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
            spin_crop.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,R.id.customSpinnerItemTextView, adapter_crops));
            spin_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_crop_id = arlist_crops.get(position).getCityId();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_crop_id.equalsIgnoreCase("0")){

                    }else {
                        new AsyncProductsoffline().execute();
                    }
                }



                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    public class AsyncProductsoffline extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlannerTaskActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_products = new ArrayList<SelectedCities>();
            adapter_products = new ArrayList<String>();
            arlist_products.clear();

            SelectedCities citieszzy = new SelectedCities();
            citieszzy.setCityId("0");
            citieszzy.setCityName("Select Product");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_products.add(citieszzy);
            adapter_products.add("Select Product");


            try {
                List<Crops> cdcList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + ","+ KEY_PRODUCT_NAME + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + company_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + sel_division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_crop_id ;
                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Products products = new Products();

                        products.setProductMasterID(cursor.getString(0));

                        products.setProductName(cursor.getString(1));



                        SelectedCities cities3 = new SelectedCities();
                        cities3.setCityId(cursor.getString(0));
                        cities3.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_products.add(cities3);
                        adapter_products.add(cursor.getString(1));



                    } while (cursor.moveToNext());
                }





                // do some stuff....


            }  catch (Exception e) {
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
            spin_product.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,R.id.customSpinnerItemTextView, adapter_products));
            spin_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_product_id = arlist_products.get(position).getCityId();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_product_id.equalsIgnoreCase("0")){

                    }else {
                        //new AsyncSchemesoffline().execute();
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
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    



}
