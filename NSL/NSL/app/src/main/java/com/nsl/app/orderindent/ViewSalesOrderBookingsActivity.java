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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.orderindent.FragmentOrderIndent.REQUEST_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSalesOrderBookingsActivity extends AppCompatActivity {
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    DatabaseHandler db;
    int osa;
    SQLiteDatabase sdbw, sdbr;
    String jsonData;
    String userId;
    String company_id;
    int role;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    int requestType;
    private StringBuilder sb=new StringBuilder();
    private String team;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private TextView tv_list_all;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salesorderbookings);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team      = sharedpreferences.getString("team", "");
        role      = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        db = new DatabaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
               /* Intent orderindent = new Intent(getApplicationContext(),OrderIndentMainActivity.class);
                startActivity(orderindent);*/
                finish();
            }
        });
        tv_list_all         = (TextView) findViewById(R.id.tv_list_all);
        toolbarTitle        = (TextView) toolbar.findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Order Indent");
        tv_list_all.setText("List of all Order Indent");
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
                Intent customers = new Intent(getApplicationContext(), ViewSalesOrderCustomerDetailsActivity.class);
                customers.putExtra("customer_name", favouriteItem.get(i).get("customer_name"));
                customers.putExtra("customer_code", favouriteItem.get(i).get("customer_code"));
                customers.putExtra("ABS",           favouriteItem.get(i).get("ABS"));
                customers.putExtra("customer_id",   favouriteItem.get(i).get("customer_id"));
                customers.putExtra("divison_id",    favouriteItem.get(i).get("DivisionId"));
                customers.putExtra("TYPE", "mo");

                customers.putExtra(REQUEST_TYPE,    requestType);
                startActivity(customers);
            }
        });
        adapter = new ItemfavitemAdapter(getApplicationContext(),favouriteItem);
        listView.setAdapter(adapter);
        requestType = getIntent().getIntExtra(REQUEST_TYPE, 0);
        company_id  = getIntent().getStringExtra("company_id");



        // new Async_getall().execute();


    }

    @Override
    protected void onResume() {
        super.onResume();

        progressDialog = new ProgressDialog(ViewSalesOrderBookingsActivity.this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(Common.haveInternet(this)){

            new Async_getNewOrUpdateserviceorders().execute();
        }else {
            new Async_getallcustomersoffline().execute();
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



    private class Async_getallcustomersoffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(ViewSalesOrderBookingsActivity.this);
//            progressDialog.setMessage("Please wait");
//            progressDialog.show();
        }

        protected String doInBackground(Void... params) {
            favouriteItem.clear();

            // String selectQuery  = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME +  " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
            //working query    // String selectQuery    = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + ","+ KEY_TABLE_CUSTOMER_CODE+            " FROM " + TABLE_SERVICEORDER + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C."+KEY_TABLE_CUSTOMER_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_CUSTOMER_ID +"  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId  ;
            /*String selectQuery  = "SELECT DISTINCT " + KEY_TABLE_CUSTOMER_NAME + ","+ KEY_TABLE_CUSTOMER_CODE+","+ KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET+","+ KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET+ ",C."+ KEY_TABLE_CUSTOMER_MASTER_ID + ",SO."+ KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT + ",CM."+ KEY_TABLE_COMPANIES_COMPANY_CODE + ",SD.advance_amount, SUM(SD.advance_amount) AS sumprice,division_name,dv.division_id FROM " + TABLE_SERVICEORDER
                    + " AS SO  JOIN " + TABLE_CUSTOMERS + " AS C ON C."+KEY_TABLE_CUSTOMER_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_CUSTOMER_ID
                    + " LEFT JOIN "+TABLE_SERVICEORDERDETAILS + " AS SD ON SD."+ KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_ID
                    + "  LEFT JOIN "+TABLE_COMPANIES + " AS CM ON CM."+ KEY_TABLE_COMPANIES_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_COMPANY_ID
                    + "  LEFT JOIN "+ TABLE_CUSTOMER_DETAILS + " AS CD ON CD."+ KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C."+ KEY_TABLE_CUSTOMER_MASTER_ID
                    +"   LEFT JOIN division AS dv ON dv.division_id = SO.division_id"
                    +"  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId +" AND SO."+KEY_TABLE_SERVICEORDER_ORDERTYPE+"="+requestType;
                   // +" GROUP BY SO.customer_id,SO.division_id";*/


            /*if("skjd"){
                String conQury=" and SO.user_id =" + userId;
            }
            if("sdfsdf") {
                String conQury=" and SO.user_id in (" +userId+")";
            }*/
            String selectQuery = "SELECT "+
                    KEY_TABLE_CUSTOMER_NAME+", "+KEY_TABLE_CUSTOMER_CODE+"," +
                    " C.customer_id," +
                    " SUM(SO.advance_amount), SUM(SOD.advance_amount) AS sumprice,dv.division_id,division_name,CM."+ KEY_TABLE_COMPANIES_COMPANY_CODE+",SO.approval_status"+
                    " FROM service_order_details SOD" +
                    " LEFT JOIN service_order SO ON SO."+ KEY_TABLE_SERVICEORDER_ID +" = SOD."+KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID+
                    " LEFT JOIN customers AS C ON C.customer_id = SO.customer_id" +
                    " LEFT JOIN division AS dv ON dv.division_id = SO.division_id" +
                    " LEFT LEFT JOIN "+TABLE_COMPANIES  + " AS CM ON CM."+ KEY_TABLE_COMPANIES_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_COMPANY_ID +
                    " WHERE SO.user_id =" + userId +
                    " AND SO.order_type =1" +
                    " GROUP BY SO.customer_id,SO.division_id";

            Log.e("selectQuery",selectQuery);
            sdbw = db.getWritableDatabase();
            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    /*if(cursor.getString(2)!= null && cursor.getString(3)!= null) {
                        int insidebucket = Integer.parseInt(cursor.getString(2));
                        int outsidebucket = Integer.parseInt(cursor.getString(3));
                        osa = insidebucket + outsidebucket;
                        Log.e("Values", cursor.getString(2) + ":" + cursor.getString(3) + ":OSA" + String.valueOf(osa) + ":customer id" + cursor.getString(4));
                    }*/
                    HashMap<String, String> map = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
                    map.put("customer_name",     cursor.getString(0));
                    map.put("customer_code",     cursor.getString(1));
                    map.put("customer_id",       cursor.getString(2));
                    map.put("Division",          cursor.getString(6));
                    map.put("DivisionId",          cursor.getString(5));
                    map.put("ABS",               "");
                    map.put("Token_Amount",      db.getSumOfOrderInAmount(userId,cursor.getString(2),cursor.getString(5)));
                    map.put("Crop name",         "");
                    map.put("Product Name",      "");
                    map.put("Order_price",       cursor.getString(4));
                    map.put("Quantity",          "");
                    map.put("Order Date",        "");
                    map.put("company_name",      cursor.getString(6));
                    map.put("approval_status",   cursor.getString(8));
                    favouriteItem.add(map);
                    String orders = String.valueOf(favouriteItem.size());
                } while (cursor.moveToNext());
            }
            else Log.d("LOG", "returned null!");

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (favouriteItem.size()==0){
                listView.setEmptyView(findViewById(R.id.tv_alert));
            }
            else{
            adapter.updateResults(favouriteItem);}
        }
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> results) {
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
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_viewbooking, parent, false);
                holder.itemname = (TextView) convertView.findViewById(R.id.tv_company);
                holder.company_code = (TextView) convertView.findViewById(R.id.tv_company_code);
                holder.tv_rbs_amount = (TextView) convertView.findViewById(R.id.tv_company_coded);
                holder.tv_bookeamount = (TextView) convertView.findViewById(R.id.tv_bookeamount);
                holder.tv_approvalstatus    = (TextView) convertView.findViewById(R.id.tv_approvalstatus);
                holder.tv_accept            = (TextView) convertView.findViewById(R.id.tv_accept);
                holder.tv_reject            = (TextView) convertView.findViewById(R.id.tv_reject);
                holder.btn_view            = (Button) convertView.findViewById(R.id.view_comments);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.btn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // showDialog(CUSTOM_DIALOG_ID);
                }
            });
            if(role==Constants.Roles.ROLE_7 || role==Constants.Roles.ROLE_12){
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
            }
            else{
                holder.tv_accept.setVisibility(View.VISIBLE);
                holder.tv_reject.setVisibility(View.VISIBLE);
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.itemname.setText(results.get(position).get("customer_name"));
            holder.company_code.setText(results.get(position).get("Division"));
            holder.tv_rbs_amount.setText("BA :"+Math.round(Float.parseFloat(results.get(position).get("Token_Amount"))));
            holder.tv_bookeamount.setText("BA :"+Math.round(Float.parseFloat(results.get(position).get("Order_price"))));


            if (results.get(position).get("approval_status").equalsIgnoreCase("1")) {
                holder.tv_approvalstatus.setText("Approved");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.green));
                holder.btn_view.setVisibility(View.GONE);
            } else if (results.get(position).get("approval_status").equalsIgnoreCase("2")) {
                holder.tv_approvalstatus.setText("Rejected");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                holder.btn_view.setVisibility(View.VISIBLE);
            } else {
                holder.tv_approvalstatus.setText("Pending");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.tabselectedcolor));
                holder.btn_view.setVisibility(View.GONE);
            }

            return convertView;
        }


        public class ViewHolder {
            public TextView itemname, company_code, tv_rbs_amount,tv_bookeamount,tv_approvalstatus,tv_accept,tv_reject;

            public Button btn_view;
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
                      //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
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
                        .add("user_id",Common.getUserIdFromSP(getApplicationContext()))
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

            new Async_getallcustomersoffline().execute();
            Common.dismissProgressDialog(progressDialog);



        }
    }


}
