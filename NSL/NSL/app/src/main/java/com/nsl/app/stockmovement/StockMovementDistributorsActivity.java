package com.nsl.app.stockmovement;


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

import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;
import com.nsl.app.distributors.Distributor_details;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMER_DETAILS;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockMovementDistributorsActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    String jsonData, userId, company_id, company;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributors);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");
        db                = new DatabaseHandler(this);
        Toolbar toolbar   = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        System.out.println("&&&&&" + getIntent().getStringExtra("company_id"));
        company_id = getIntent().getStringExtra("company_id");
        company    = getIntent().getStringExtra("company_name");
        listView   = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
                Intent booking = new Intent(getApplicationContext(), Distributor_details.class);

                //   booking.putExtra("company_name",  company);
                booking.putExtra("customer_code", favouriteItem.get(i).get("customer_code"));
                booking.putExtra("customer_id",   favouriteItem.get(i).get("customer_id"));
                booking.putExtra("customer_name", favouriteItem.get(i).get("customer_name"));
                booking.putExtra("company_id",    company_id);
                booking.putExtra("OSA",           favouriteItem.get(i).get("osa"));
                // booking.putExtra("company",       company);
                startActivity(booking);
                finish();
            }
        });
        adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
        listView.setAdapter(adapter);
        // jsonData = getIntent().getStringExtra("strcustomers");
        System.out.println("!!!!!" + jsonData);
        new Async_getalloffline().execute();


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



    private class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StockMovementDistributorsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {


            try {

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_CODE + "," + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET + "," + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET + "," + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + " LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
                // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {

                        int insidebucket = Integer.parseInt(cursor.getString(3));
                        int outsidebucket = Integer.parseInt(cursor.getString(4));
                        int osa = insidebucket + outsidebucket;

                        if (osa > 0) {
                            System.out.println("+++++++++++" + cursor.getString(0) + ":::" + cursor.getString(3) + ":::" + cursor.getString(4));
                            Log.e("OSA : ", String.valueOf(osa) + " : osa > 0");

                            HashMap<String, String> map = new HashMap<String, String>();
                            // adding each child node to HashMap key => value
                            map.put("customer_name",  cursor.getString(1));
                            map.put("customer_id",    cursor.getString(0));
                            map.put("company",        "");
                            map.put("customer_code",  cursor.getString(2));
                            map.put("region_name",    "");
                            map.put("division_name",  "");
                            map.put("credit_limit",   cursor.getString(5));
                            map.put("inside_bucket",  String.valueOf(insidebucket));
                            map.put("outside_bucket", String.valueOf(outsidebucket));
                            map.put("osa",            String.valueOf(osa));
                            favouriteItem.add(map);

                        } else {
                            Log.e("OSA : ", String.valueOf(osa) + "Credit limit" + cursor.getString(5));
                        }


                    } while (cursor.moveToNext());
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
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_customers, parent, false);
                holder.itemname = (TextView) convertView.findViewById(R.id.tv_customer);
                holder.company_code = (TextView) convertView.findViewById(R.id.tv_customer_code);
                holder.osa = (TextView) convertView.findViewById(R.id.tv_osa);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.itemname.setText(results.get(position).get("customer_name"));
            holder.company_code.setText(results.get(position).get("customer_code"));
            holder.osa.setText(results.get(position).get("osa"));

            return convertView;
        }


        public class ViewHolder {
            public TextView itemname, company_code, osa;
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }


}
