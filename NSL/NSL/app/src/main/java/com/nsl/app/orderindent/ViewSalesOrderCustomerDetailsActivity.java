
package com.nsl.app.orderindent;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;
import com.nsl.app.advancebooking.NewAdvancebokingChooseActivity;
import com.nsl.app.commonutils.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_BRAND_NAME;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_NAME;
import static com.nsl.app.DatabaseHandler.KEY_SCHEMES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ORDERDATE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ORDERTYPE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMER_DETAILS;
import static com.nsl.app.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.app.DatabaseHandler.TABLE_PRODUCTS;
import static com.nsl.app.DatabaseHandler.TABLE_SCHEMES;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDERDETAILS;
import static com.nsl.app.orderindent.FragmentOrderIndent.REQUEST_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSalesOrderCustomerDetailsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    String  aging1 = null, aging2 = null;
    String  jsonData, userId, customer_id,divison_id;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name, tv_code, tv_amount,tv_credit,tv_osa,tv_division,tv_company_name;
    ImageView iv_ageing;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    private int requestType,j=0,i = 0;

    private float so, sum_so,sum_osa;
    private String str_division_name,so_company_code;
    double str_climit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorder_customer_details);
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");
        so_company_code   = sharedpreferences.getString("so_company_code", "");
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotohome = new Intent(getApplicationContext(), NewAdvancebokingChooseActivity.class);

                gotohome.putExtra("customer_id", customer_id);
                startActivity(gotohome);
            }
        });

        tv_name           = (TextView) findViewById(R.id.tv_customer_name);
        tv_company_name   = (TextView) findViewById(R.id.tv_company_name);

        tv_division       = (TextView) findViewById(R.id.tv_division_name);
        tv_code           = (TextView) findViewById(R.id.tv_code);
        tv_amount         = (TextView) findViewById(R.id.tv_amount);
        tv_credit         = (TextView) findViewById(R.id.tv_credit);
        tv_osa            = (TextView) findViewById(R.id.tv_osa);
        iv_ageing         = (ImageView) findViewById(R.id.iv_ageing);
        customer_id       = getIntent().getStringExtra("customer_id");
        tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_company_name.setText(so_company_code);
       // tv_code.setText("(" + getIntent().getStringExtra("customer_code") + ")");
        divison_id = getIntent().getStringExtra("divison_id");

        if(getIntent().getStringExtra("TYPE").equalsIgnoreCase("rm")){
            userId  =  getIntent().getStringExtra("userId");
        }

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
               /* Intent customers = new Intent(getApplicationContext(),ViewAdvancebokingCustomerDetailsActivity.class);
                customers.putExtra("customer_name",favouriteItem.get(i).get("customer_name"));
                customers.putExtra("customer_code",favouriteItem.get(i).get("customer_code"));
                customers.putExtra("ABS",favouriteItem.get(i).get("ABS"));
                startActivity(customers);*/
            }
        });
        adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
        listView.setAdapter(adapter);
        new Async_getallcustomersoffline().execute();
        new Async_getalloffline().execute();
        requestType = getIntent().getIntExtra(REQUEST_TYPE, 0);




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
           /* progressDialog = new ProgressDialog(ViewAdvancebokingCustomerDetailsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();*/

        }

        protected String doInBackground(Void... params) {

            favouriteItem.clear();
            j=0;

            // String selectQuery  = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME +  " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
            //working query    // String selectQuery    = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + ","+ KEY_TABLE_CUSTOMER_CODE+            " FROM " + TABLE_SERVICEORDER + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C."+KEY_TABLE_CUSTOMER_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_CUSTOMER_ID +"  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId  ;
            String selectQuery = " SELECT " + KEY_PRODUCT_BRAND_NAME
                    + ",SD." +    KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE
                    + ",C."  + KEY_TABLE_CUSTOMER_MASTER_ID
                    + ",CD." + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT
                    + ",CD." + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET
                    + ",CD." + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET
                    + ",dv." + KEY_TABLE_DIVISION_NAME
                    + ",SO." + KEY_TABLE_SERVICEORDER_ORDERDATE
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID
                    + " FROM " + TABLE_SERVICEORDER
                    + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID
                    + " LEFT JOIN " + TABLE_SERVICEORDERDETAILS + " AS SD ON SD."
                    + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = SO." + KEY_TABLE_SERVICEORDER_ID
                    + " LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID
                    + " LEFT JOIN " + TABLE_SCHEMES + " AS SCH ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " = SCH." + KEY_SCHEMES_MASTER_ID
                    + " LEFT JOIN " + TABLE_PRODUCTS + " AS PRO ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " = PRO." + KEY_PRODUCT_MASTER_ID
                    + " LEFT JOIN division AS dv ON dv.division_id = SO.division_id"
                    + " LEFT JOIN scheme_products AS sp ON sp.scheme_id = SCH.scheme_id"+
                    "  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId
                    + " AND  SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + " = " + customer_id + " AND SO."
                    + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=1 AND SO.division_id="+divison_id +" group by " + KEY_TABLE_SERVICEORDER_ORDERDATE+","+KEY_PRODUCT_NAME ;
            Log.e("selectQuery", selectQuery);
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    if (cursor.getString(2) == null || cursor.getString(2).equalsIgnoreCase("") || cursor.getString(2).equalsIgnoreCase("null")) {
                        int str_count = cursor.getCount();
                    } else {
                        j++;
                        so = Float.parseFloat(cursor.getString(2));
                        Log.e("Values", cursor.getString(0) + " : " + cursor.getString(1) + " : OSA" + String.valueOf(so) + " : customer id" + cursor.getString(3)+ " : qty" + cursor.getString(9));
                        sum_so              = sum_so + so;
                        String inside_value = cursor.getString(5);
                        String outside_value = cursor.getString(6);
                        float inside        = (inside_value!=null && !inside_value.equals("null")) ? Float.parseFloat(cursor.getString(5)):0;
                        float outside       = (outside_value!=null && !outside_value.equals("null")) ? Float.parseFloat(cursor.getString(6)):0;
                        float total_osa     = inside + outside;
                        sum_osa             = sum_osa + total_osa;

                        str_division_name   = cursor.getString(7);

                        Double creditLimt = Double.parseDouble(cursor.getString(4));
                        str_climit = creditLimt-(sum_osa);

                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("customer_name", cursor.getString(0));
                        map.put("customer_code", cursor.getString(1));
                        map.put("customer_id", cursor.getString(3));
                        map.put("Division", "");
                        map.put("ABS", String.valueOf(so));
                        map.put("Crop name", "");
                        map.put("Product Name", "");
                        map.put("Order price", "");
                        map.put("Quantity", cursor.getString(9));
                        map.put("OrderDate", cursor.getString(8));
                        map.put("count", String.valueOf(j));
                        map.put("price_per_unit", db.getPricePerUnitByProductId(cursor.getString(10), Common.getDefaultSP(getApplicationContext()).getString("region_id","0")));
                        favouriteItem.add(map);
                        if (outside > 0) {
                            aging1 = ">";

                            aging2 = "90";
                            //color= "#ff0000";

                        } else {
                            aging1 = "<";
                            aging2 = "90";
                            // color="#008000";
                        }
                    }


                    String orders = String.valueOf(favouriteItem.size());
                } while (cursor.moveToNext());

            } else Log.d("LOG", "returned null!");




            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv_amount.setText("" + String.valueOf(sum_so));
            tv_credit.setText(String.valueOf(str_climit));
            if (sum_osa>0) {
                tv_osa.setText(String.valueOf(sum_osa));
            }else {
                tv_osa.setText("0");
            }
            tv_division.setText(""+str_division_name);
            if (aging1 != null && aging1.equalsIgnoreCase("<")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.less));
            } else if (aging1 != null && aging1.equalsIgnoreCase(">")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.greater));
            }
            adapter.updateResults(favouriteItem);
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
            ItemfavitemAdapter.ViewHolder holder = new ItemfavitemAdapter.ViewHolder();
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView             = inflater.inflate(R.layout.listheader_salesorder, parent, false);
                holder.itemname         = (TextView) convertView.findViewById(R.id.tv_product);
                holder.company_code     = (TextView) convertView.findViewById(R.id.tv_schemedetails);
                holder.scheme           = (TextView) convertView.findViewById(R.id.tv_scheme);
                holder.tv_rbs_amount    = (TextView) convertView.findViewById(R.id.tv_amount);
                holder.tv_itemcount     = (TextView) convertView.findViewById(R.id.tv_sr_no);
                holder.tv_orderdate     = (TextView) convertView.findViewById(R.id.textViewTitle);
                holder.rl_date          = (RelativeLayout) convertView.findViewById(R.id.header);
                holder.tv_qunatity          = (TextView) convertView.findViewById(R.id.tv_qunatity);
                holder.tv_rate          = (TextView) convertView.findViewById(R.id.tv_rate);
                convertView.setTag(holder);
            } else {
                holder = (ItemfavitemAdapter.ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.itemname.setText(results.get(position).get("customer_name"));
            holder.company_code.setText(results.get(position).get("customer_code"));
            holder.tv_rbs_amount.setText(results.get(position).get("ABS"));
            holder.tv_qunatity.setText(results.get(position).get("Quantity"));
            holder.tv_rate.setText(results.get(position).get("price_per_unit"));

            String dateString = results.get(position).get("OrderDate");

            if(dateString!=null && dateString.indexOf(" ")>-1){
                dateString = dateString.split(" ")[0];
            }

            if (position==0) {
                holder.rl_date.setVisibility(View.VISIBLE);
                holder.tv_orderdate.setText("S# DETAILS (" + dateString + ")");
            }
            else if(position<results.size()-1  && dateString!=null) {
                String prevDateString = results.get(position-1).get("OrderDate");
                if(prevDateString!=null && prevDateString.indexOf(" ")>-1){
                    prevDateString = prevDateString.split(" ")[0];
                }
                if(!dateString.equals(prevDateString)){
                    holder.rl_date.setVisibility(View.VISIBLE);
                    holder.tv_orderdate.setText("S# DETAILS ("+dateString+")");
                }else{
                    holder.rl_date.setVisibility(View.GONE);
                }
            }else if(position==results.size()-1 && dateString!=null ){
                String prevDateString = results.get(position-1).get("OrderDate");
                if(prevDateString!=null && prevDateString.indexOf(" ")>-1){
                    prevDateString = prevDateString.split(" ")[0];
                }
                if(!dateString.equals(prevDateString)){
                    holder.rl_date.setVisibility(View.VISIBLE);
                    holder.tv_orderdate.setText("S# DETAILS ("+dateString+")");
                }else{
                    holder.rl_date.setVisibility(View.GONE);
                }
            }

            holder.tv_itemcount.setText(results.get(position).get("count")+".");
            holder.company_code.setVisibility(View.GONE);
            holder.scheme.setVisibility(View.GONE);
            return convertView;
        }


        public class ViewHolder {
            public TextView itemname, company_code, tv_rbs_amount,scheme,tv_itemcount,tv_orderdate,tv_qunatity,tv_rate;
            public RelativeLayout rl_date;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }
    class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewSalesOrderCustomerDetailsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {


            try {

                String selectQuery = "SELECT D." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET + "," + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET + "," + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT + " FROM " + TABLE_CUSTOMERS + " AS C LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID + " LEFT JOIN " + TABLE_DIVISION + " AS D ON  D." + KEY_TABLE_DIVISION_MASTER_ID + " = CD.Division_id WHERE C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + sharedpreferences.getString("customer_id", null) + " and " + "D." + KEY_TABLE_DIVISION_MASTER_ID + " = " + divison_id+ " group by C." + KEY_TABLE_CUSTOMER_MASTER_ID+",D."+KEY_TABLE_DIVISION_MASTER_ID;
                System.out.println(selectQuery);


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
                        Log.d("div id", cursor.getString(0));
                        Log.d("div name ", cursor.getString(1));
                        Log.d("inside bucket", cursor.getString(2));
                        Log.d("outside bucket", cursor.getString(3));

                        float inside        = Float.parseFloat(cursor.getString(2));
                        float outside       = Float.parseFloat(cursor.getString(3));
                        sum_osa             = inside + outside;
                        if (outside > 0) {
                            aging1 = ">";

                            aging2 = "90";
                            //color= "#ff0000";

                        } else if (outside == 0) {
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
          //  tv_osa.setText(String.valueOf(sum_osa));

           /* if (aging1 != null && aging1.equalsIgnoreCase("<")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.less));
            } else if (aging1 != null && aging1.equalsIgnoreCase(">")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.greater));
            }*/
        }
    }

}
