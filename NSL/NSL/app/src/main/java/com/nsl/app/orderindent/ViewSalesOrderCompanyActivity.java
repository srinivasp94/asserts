package com.nsl.app.orderindent;


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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;
import com.nsl.app.ServiceOrderDetailMaster;
import com.nsl.app.ServiceOrderMaster;
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
import java.util.HashMap;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_SAP_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSalesOrderCompanyActivity extends AppCompatActivity {
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr=null;



    ProgressDialog progressDialog;

    private ListView listView;
    private ItemfavitemAdapter adapter;

    String jsonData,userId,companyinfo;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;
    public static String Companyname;
    private String str_ccount,str_company_id,str_company_code,str_company_name;
    private StringBuilder sb=new StringBuilder();
    private String team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newadvancebookingcompanies);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");
        team      = sharedpreferences.getString("team", "");
        companyinfo       = sharedpreferences.getString("companyinfo", "");
        db                = new DatabaseHandler(this);
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
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
                Companyname      = favouriteItem.get(i).get("company_code");

                Intent customers = new Intent(getApplicationContext(),ViewSalesOrderBookingsActivity.class);
                customers.putExtra("strcustomers", favouriteItem.get(i).get("strcustomers"));
                customers.putExtra("company_id",   favouriteItem.get(i).get("company_id"));
                customers.putExtra("company",      favouriteItem.get(i).get("name"));
                customers.putExtra("company_code", favouriteItem.get(i).get("company_code"));

                SharedPreferences.Editor editor =  sharedpreferences.edit();
                editor.putString("so_company_id",  favouriteItem.get(i).get("company_id"));
                editor.putString("so_company_code",favouriteItem.get(i).get("company_code"));
                editor.commit();

                startActivity(customers);
                //finish();
            }
        });
        adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
        listView.setAdapter(adapter);


        new Async_getalloffline().execute();

    }


    @Override
    protected void onResume() {
        super.onResume();

       /* progressDialog = new ProgressDialog(ViewSalesOrderCompanyActivity.this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(Common.haveInternet(this)){

            new Async_getNewOrUpdateserviceorders().execute();
        }else {
            new Async_getalloffline().execute();
        }*/
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


    private class Async_getalloffline extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewSalesOrderCompanyActivity.this);
            progressDialog.setMessage("Please wait \n data loading from offline");
            progressDialog.show();
        }

        protected String doInBackground(Void... params)
        {

            try {

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME+ ","+ KEY_TABLE_COMPANIES_COMPANY_CODE+ ","+ KEY_TABLE_COMPANIES_COMPANY_SAP_ID  + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);


                Log.e(" ++++ cursor count ++++", String.valueOf(cursor.getCount()));
                str_ccount = String.valueOf(cursor.getCount());

                /*if(str_ccount.equalsIgnoreCase("1")){

                    str_company_id   =  cursor.getString(0);
                    str_company_name =  cursor.getString(1);
                    str_company_code =  cursor.getString(2);

                }

                else {

                }*/
                if (cursor.moveToFirst()) {
                    do {
                            /*Companies companies = new Companies();

                            companies.setCompanyMasterId(cursor.getString(0));
                            companies.setCompanyName(cursor.getString(1));
                            companies.setCompanycode(cursor.getString(2));
                            companies.setCompanysapid(cursor.getString(3));*/

                        System.out.println("+++++++++++" + cursor.getString(0)+ cursor.getString(1)+ cursor.getString(2)+ cursor.getString(3));
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        str_company_id   =  cursor.getString(0);
                        str_company_name =  cursor.getString(1);
                        str_company_code =  cursor.getString(2);

                        map.put("company_id",     cursor.getString(0));
                        map.put("name",           cursor.getString(1));
                        map.put("company_code",   cursor.getString(2));
                        map.put("company_sap_id", cursor.getString(3));
                        map.put("strcustomers",   "");
                        map.put("strdivisions",   "");

                        favouriteItem.add(map);

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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if(str_ccount.equalsIgnoreCase("1")){


                Intent customers = new Intent(getApplicationContext(),ViewSalesOrderBookingsActivity.class);
                customers.putExtra("strcustomers", "");
                customers.putExtra("company_id",   str_company_id);
                customers.putExtra("company",      str_company_name);
                customers.putExtra("company_code", str_company_code);

                SharedPreferences.Editor editor =  sharedpreferences.edit();
                editor.putString("so_company_id",  str_company_id);
                editor.putString("so_company_code",str_company_code);
                editor.commit();

                startActivity(customers);
                finish();

            }
            else{
                adapter.updateResults(favouriteItem);
            }


        }
    }
    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String,String>>();


        public ItemfavitemAdapter(Context context,ArrayList<HashMap<String, String>> results) {
            this.context = context;
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
            if(convertView == null)
            {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater     = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView                 = inflater.inflate(R.layout.row_newadvancebooking, parent, false);
                holder.itemname             = (TextView)convertView.findViewById(R.id.tv_company);
                holder.company_code         = (TextView)convertView.findViewById(R.id.tv_company_code);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
           //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.itemname.setText(results.get(position).get("name"));
            holder.company_code.setText(results.get(position).get("company_code"));

            return convertView;
        }


        public class ViewHolder
        {
            public TextView  itemname,company_code;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }




    public class Async_getNewOrUpdateserviceorders extends AsyncTask<Void, Void, String> {

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
                        .url(Common.getCompleteURLEVM(Constants.NEW_OR_UPDATED_RECORDS_SERVICE_ORDER,userId,team))
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
                    JSONObject resultobject   = new JSONObject(jsonData);
                    JSONArray adavancebooking = resultobject.getJSONArray("newrecord");

                    for (int n = 0; n < adavancebooking.length(); n++) {


                        JSONObject  result_service_order           = adavancebooking.getJSONObject(n);
                        JSONObject  service_order                  = result_service_order.getJSONObject("service_order");

                        JSONArray  service_order_details           = result_service_order.getJSONArray("service_order_details");

                        String ffm_id              = service_order.getString("order_id");//ffm_id =order_id PRIMARY KEY
                        String order_type            = service_order.getString("order_type");
                        String order_date            = service_order.getString("order_date");
                        String user_id               = service_order.getString("user_id");
                        String customer_id           = service_order.getString("customer_id");


                        String division_id           = service_order.getString("division_id");
                        String company_id            = service_order.getString("company_id");
                        String advance_amount        = service_order.getString("advance_amount");
                        String created_by            = service_order.getString("created_by");
                        String status                = service_order.getString("status");
                        String approval_status       = service_order.getString("approval_status");
                        String created_datetime      = service_order.getString("created_datetime");
                        String updated_datetime      = service_order.getString("updated_datetime");
                        String approval_comments = service_order.getString("approval_comments");
                        if (n>0){
                            sb.append(",");
                        }
                        sb.append(ffm_id);


                        String selectQuery = "SELECT * FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_FFM_ID + " = '" + ffm_id + "'";


                        sdbw               = db.getWritableDatabase();
                        Cursor cc          = sdbw.rawQuery(selectQuery, null);
                        cc.getCount();
                        // looping through all rows and adding to list
                        if (cc.getCount()  == 0) {
                            //doesn't exists therefore insert record.
                            db.addServiceorder(new ServiceOrderMaster("", order_type, order_date, user_id, customer_id,division_id,company_id,status,created_datetime,updated_datetime,ffm_id,advance_amount,created_by,approval_status,approval_comments));
                            String selectQuerys = "SELECT  " + KEY_TABLE_SERVICEORDER_ID  +" FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";
                            sdbw = db.getWritableDatabase();

                            Cursor c = sdbw.rawQuery(selectQuerys, null);
                            //System.out.println("cursor count "+cursor.getCount());
                            String orderidfromserviceorder = null;
                            if (c != null && c.moveToFirst()) {
                                orderidfromserviceorder = String.valueOf(c.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                Log.e("++++ lastId ++++" , orderidfromserviceorder);
                            }

                            try {

                                for (int m = 0; m < service_order_details.length(); m++) {
                                    JSONObject objinfo = service_order_details.getJSONObject(m);
                                    String service_order_details_order_id = objinfo.getString("order_id");
                                    String service_order_details_crop_id = objinfo.getString("crop_id");
                                    String scheme_id = objinfo.getString("scheme_id");
                                    String product_id                             = objinfo.getString("product_id");
                                    String quantity                               = objinfo.getString("quantity");


                                    String order_price                            = objinfo.getString("order_price");
                                    String service_order_details_advance_amount   = objinfo.getString("advance_amount");
                                    String service_order_details_status           = objinfo.getString("status");
                                    // String created_by            = objinfo.getString("created_by");
                                    String service_order_details_created_datetime = objinfo.getString("created_datetime");
                                    String service_order_details_updated_datetime = objinfo.getString("updated_datetime");
                                    String ffmID = objinfo.getString("service_order_detail_id");
                                    String slab_id = objinfo.getString("slab_id");
                                    slab_id = slab_id != null || !slab_id.equalsIgnoreCase("null") || !slab_id.equalsIgnoreCase("")? slab_id: "";



                                    db.addServiceorderdetails(new ServiceOrderDetailMaster(ffmID, orderidfromserviceorder, service_order_details_crop_id, scheme_id,
                                            product_id, quantity, order_price,
                                            service_order_details_advance_amount, service_order_details_status, service_order_details_created_datetime, service_order_details_updated_datetime,ffmID, slab_id));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }



                    }

                    JSONArray updated = resultobject.getJSONArray("updated");
                    for (int n = 0; n < updated.length(); n++) {


                        JSONObject  result_service_order           = updated.getJSONObject(n);
                        JSONObject  service_order                  = result_service_order.getJSONObject("service_order");

                        JSONArray  service_order_details           = result_service_order.getJSONArray("service_order_details");

                        String ffm_id              = service_order.getString("order_id");//ffm_id =order_id PRIMARY KEY
                        String order_type            = service_order.getString("order_type");
                        String order_date            = service_order.getString("order_date");
                        String user_id               = service_order.getString("user_id");
                        String customer_id           = service_order.getString("customer_id");


                        String division_id           = service_order.getString("division_id");
                        String company_id            = service_order.getString("company_id");
                        String advance_amount        = service_order.getString("advance_amount");
                        String created_by            = service_order.getString("created_by");
                        String status                = service_order.getString("status");

                        String created_datetime      = service_order.getString("created_datetime");
                        String updated_datetime      = service_order.getString("updated_datetime");
                        String approval_status      = service_order.getString("approval_status");
                        String approval_comments = service_order.getString("approval_comments");
                        if (sb.toString().length()>0){
                            sb.append(",");
                        }
                        sb.append(ffm_id);

                        db.updateApprovalOrRejectStatus(userId,approval_status,approval_comments,ffm_id,false);

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
           /* if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/

            new Async_UpdateAprovalStatus().execute();


        }
    }



    private class Async_UpdateAprovalStatus extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Updating Aproval Status");
            progressDialog.show();*/
        }

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;



                RequestBody formBody = new FormEncodingBuilder()
                        .add("table","service_order")
                        .add("field", "order_id")
                        .add("updated_ids",sb.toString())
                        .add("user_id",userId)
                        .build();



                Request request = new Request.Builder()
                        .url(Constants.ACKNOWLEDGE_TO_SERVER)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1ACKNOWLEDGE_TO_SERVER" + formBody.toString()+"\n"+jsonData);
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
            Common.dismissProgressDialog(progressDialog);



        }
    }
}
