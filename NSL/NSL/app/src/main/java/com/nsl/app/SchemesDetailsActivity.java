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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.app.adapters.BankDetailsListAdapter;
import com.nsl.app.adapters.SchemeDetailsListAdapter;
import com.nsl.app.pojo.BankDetailsListPojo;
import com.nsl.app.pojo.SchemeDetailsListPojo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class SchemesDetailsActivity extends AppCompatActivity {

    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    int i = 0, sum_osa = 0;
    private RecyclerView listView;

    String customer_id, comp_id;
    String jsonData, userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color;
    String aging1 = null, aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_schemename, tv_code, tv_total_osa_amt,tv_slab_id,tvcompany,tvcrop,tvstatus,tvempty;
    private String scheme_id,CURDATE,slab_id,status;
    List<SchemeDetailsListPojo>  bankDetailsListPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_detail);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
         db = new DatabaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvcompany = (TextView) findViewById(R.id.tv_company);
        tvcrop = (TextView) findViewById(R.id.tv_crop);
        tvstatus = (TextView) findViewById(R.id.tv_status);
        tv_schemename = (TextView) findViewById(R.id.tv_schemename);
        tv_slab_id    = (TextView) findViewById(R.id.tv_slab_id);
        tvempty    = (TextView) findViewById(R.id.empty);
        tv_schemename.setText(getIntent().getStringExtra("name"));
        tv_slab_id.setText("");
        scheme_id     = getIntent().getStringExtra("scheme_id");
        status     = getIntent().getStringExtra("status");


        listView      = (RecyclerView) findViewById(R.id.listView);
        final ArrayList<HashMap<String, String>> feedList = new ArrayList<HashMap<String, String>>();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        CURDATE = df.format(c.getTime());


        String selectQuery = "SELECT `scheme_products`.`price_per_packet`, `scheme_products`.`slab_id`, `companies`.`company_code`, `crops`.`crop_name` FROM `scheme_products` LEFT JOIN `companies` ON `companies`.`company_id`=`scheme_products`.`company_id` LEFT JOIN `crops` ON `crops`.`crop_id`=`scheme_products`.`crop_id` WHERE `scheme_id` = "+scheme_id+ " limit 1";
        sdbw = db.getWritableDatabase();
        Log.e("Schemes Details",selectQuery);
        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {

              Log.e(":",cursor.getString(2)+":"+cursor.getString(3)+":"+status);
                if(status.equals("1")){
                    status = "Active";
                }
                else if(status.equals("0")){
                    status = "InActive";
                }
              tvcompany.setText(cursor.getString(2));
                tvcrop.setText(cursor.getString(3));
                tvstatus.setText(status);

            } while (cursor.moveToNext());
        }



        new Async_getalloffline().execute();

    }


    private class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SchemesDetailsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {

            bankDetailsListPojo= new ArrayList<>();
            //String selectQuery = "SELECT  " + KEY_PRODUCTS_PRICE + "," + KEY_PRODUCT_NAME + " FROM " + TABLE_SCHEME_PRODUCTS + " sp JOIN " + TABLE_PRODUCTS + " s ON s." + KEY_PRODUCT_MASTER_ID + " = sp." + KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + " WHERE sp." + KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID + "=" + scheme_id;
             /* String selectQuery = "SELECT  pp.price,product_name FROM scheme_products sp " +
                      "JOIN products s ON s.product_id = sp.product_id " +
                      "JOIN product_price pp ON s.product_id = pp.product_id WHERE sp.scheme_id="+scheme_id+"  and (pp.from_date<='"+CURDATE+"' and pp.extenstion_date>='"+CURDATE+"')";*/

            String selectQuery = "SELECT scheme_products.price_per_packet,scheme_products.valid_from,scheme_products.valid_to,scheme_products.slab_id,region.region_name,scheme_products.scheme_id,products.product_id, products.brand_name "+
            "FROM scheme_products left JOIN products ON scheme_products.product_id = products.product_id left  JOIN region ON scheme_products.region_id = region.region_id WHERE scheme_id ="+scheme_id+ " and scheme_products.product_id<>0 order by  scheme_products.product_id  asc";

           /* String selectQuery = "SELECT pp.price, sp.price_per_packet, sp.slab_id, brand_name FROM " +
                    "scheme_products sp JOIN products s ON s.product_id = sp.product_id JOIN product_price pp ON s.product_id = pp.product_id WHERE sp.scheme_id="+scheme_id+" and (pp.from_date<='"+CURDATE+"' and pp.to_date>='"+CURDATE+"')";
         */
            sdbw = db.getWritableDatabase();
            Log.e("Schemes Details",selectQuery);

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    SchemeDetailsListPojo obj = new SchemeDetailsListPojo();
                    obj.productName = cursor.getString(7);
                    obj.price   = cursor.getString(0);
                    obj.region  = cursor.getString(4);
                    obj.validfrom = cursor.getString(1);
                    obj.validto = cursor.getString(2);
                    obj.slabid = cursor.getString(3);
                    slab_id = cursor.getString(3);
                    bankDetailsListPojo.add(obj);
                 /*   HashMap<String, String> map = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
                    map.put("scheme_price", cursor.getString(0));
                    map.put("valid_from", cursor.getString(1));
                    map.put("valid_to", cursor.getString(2));
                    slab_id = cursor.getString(3);
                    map.put("region",cursor.getString(4));
                    map.put("product_name",cursor.getString(6));
                    favouriteItem.add(map);
*/
                } while (cursor.moveToNext());

            }
            else Log.d("LOG", "returned null!"+ bankDetailsListPojo.toString());




            return jsonData;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
           // adapter.updateResults(favouriteItem);
            tv_slab_id.setText(slab_id);
            setListOnAdapter(bankDetailsListPojo,listView);
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


    public void setListOnAdapter(final List<SchemeDetailsListPojo> bankDetailsListPojo, RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        SchemeDetailsListAdapter schemeDetailsListAdapter = null;
        Log.e("size ", String.valueOf(bankDetailsListPojo.size()));
        if (bankDetailsListPojo.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
             tvempty.setVisibility(View.GONE);
            schemeDetailsListAdapter = new SchemeDetailsListAdapter(getApplicationContext(), bankDetailsListPojo);
            recyclerView.setAdapter(schemeDetailsListAdapter);
            schemeDetailsListAdapter.setOnItemClickListener(new SchemeDetailsListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {


                }
            });
            // stockPlacementPopupListAdapter.notifyDataSetChanged();

        } else if (bankDetailsListPojo == null || bankDetailsListPojo.size() == 0) {

             tvempty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }


}



