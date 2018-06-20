package com.nsl.app.advancebooking;


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

import com.nsl.app.Crops;
import com.nsl.app.Customers;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewAdvancebokingCustomersActivity extends AppCompatActivity {
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr=null;
    ProgressDialog progressDialog;

    private ListView listView;
    private ItemfavitemAdapter adapter;
    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;
    String jsonData,userId,company_id,company;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    public static String customername;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newadvancebookingcustomers);
        sharedpreferences =getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId      = sharedpreferences.getString("userId", "");
        db = new DatabaseHandler(this);
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

        System.out.println("&&&&&"+getIntent().getStringExtra("company_id"));
        company_id =  getIntent().getStringExtra("company_id");
        company    =  getIntent().getStringExtra("company");
        listView   = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
               // Intent booking = new Intent(getApplicationContext(),AdvancebokingFormActivity.class);
                customername   = favouriteItem.get(i).get("customer_name");
                Intent booking = new Intent(getApplicationContext(),NewAdvancebokingDivisionsActivity.class);
                booking.putExtra("company",       favouriteItem.get(i).get("company"));
                booking.putExtra("customer_code", favouriteItem.get(i).get("customer_code"));
                booking.putExtra("customer_id",   favouriteItem.get(i).get("customer_id"));
                booking.putExtra("customer_name", favouriteItem.get(i).get("customer_name"));
                booking.putExtra("company_id",    company_id);
                booking.putExtra("company",       company);
                startActivity(booking);
            }
        });
        adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
        listView.setAdapter(adapter);
        jsonData = getIntent().getStringExtra("strcustomers");
        System.out.println("!!!!!"+jsonData);
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
            Intent home = new Intent(getApplicationContext(),MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Async_getall extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewAdvancebokingCustomersActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params)
        {


            try {



            }catch (Exception e)
            {
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

            if (jsonData != null && jsonData.startsWith("["))
            {
                try {
                    JSONArray  customersarray = new JSONArray(jsonData);


                    for(int n = 0; n < customersarray.length(); n++)
                    {
                        JSONObject object = customersarray.getJSONObject(n);

                        //String info = object.getString("info");

                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("customer_name",    object.getString("customer_name"));
                        map.put("customer_id",      object.getString("customer_id"));
                        map.put("company",          object.getString("company"));
                        map.put("customer_code",    object.getString("customer_code"));
                        map.put("region_name",      object.getString("region_name"));
                        map.put("division_name",    object.getString("division_name"));
                        map.put("credit_limit",     object.getString("credit_limit"));
                        map.put("inside_bucket",    object.getString("inside_bucket"));
                        map.put("outside_bucket",   object.getString("outside_bucket"));



                        favouriteItem.add(map);
                        // do some stuff....

                    }

                }  catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

            adapter.updateResults(favouriteItem);
        }
    }
    private class Async_getalloffline extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewAdvancebokingCustomersActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params)
        {


            try {
                List<Crops> cdcList = new ArrayList<>();

                 String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE+  " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + "CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + userId+ " group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")" ;
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

                        System.out.println("+++++++++++" + cursor.getString(0)+ cursor.getString(1)+ cursor.getString(2));


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
            adapter.updateResults(favouriteItem);

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
                convertView                 = inflater.inflate(R.layout.row_newadvancebookingcustomers, parent, false);
                holder.itemname             = (TextView)convertView.findViewById(R.id.tv_customer);
                holder.company_code         = (TextView)convertView.findViewById(R.id.tv_customer_code);
                holder.osa                  = (TextView)convertView.findViewById(R.id.tv_osa);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
           //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.itemname.setText(results.get(position).get("customer_name"));
            holder.company_code.setText(results.get(position).get("customer_code"));
            holder.osa.setText(results.get(position).get("inside_bucket"));

            return convertView;
        }


        public class ViewHolder
        {
            public TextView  itemname,company_code,osa;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }




}
