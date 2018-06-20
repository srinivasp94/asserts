package com.nsl.app.distributors;


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
import android.widget.Spinner;
import android.widget.TextView;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class DistributorsActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    private String jsonData, team, company_id, company,checkuid;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    private int user_pos;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private Spinner spin_distribute;
    ArrayList<String> optionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributors);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        team = sharedpreferences.getString("team", "");
        checkuid = sharedpreferences.getString("userId", "");
        Log.e("userid is : ",checkuid);
        Log.d("team",team);
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
      //  System.out.println("&&&&&" + db.getAllUser_Company_Customer().get(0).getuccCustomerId());

        company_id = getIntent().getStringExtra("company_id");
        company    = getIntent().getStringExtra("company_name");
        listView   = (ListView) findViewById(R.id.listView);
        spin_distribute = (Spinner) findViewById(R.id.spin_distribute);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
                Intent booking = new Intent(getApplicationContext(), Distributor_details.class);

                //   booking.putExtra("company_name",  company);
                booking.putExtra("customer_code", favouriteItem.get(i).get("customer_code"));
                booking.putExtra("customer_id",   favouriteItem.get(i).get("customer_id"));
                booking.putExtra("customer_name", favouriteItem.get(i).get("customer_name"));
                booking.putExtra("company_id",    favouriteItem.get(i).get("company_id"));
                booking.putExtra("OSA",           favouriteItem.get(i).get("osa"));
                booking.putExtra("company",       favouriteItem.get(i).get("company_code"));
                startActivity(booking);
                finish();
            }
        });
        adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
        listView.setAdapter(adapter);
        // jsonData = getIntent().getStringExtra("strcustomers");
        System.out.println("!!!!!" + jsonData);
        new Async_getalloffline().execute();


        optionsList = new ArrayList<>();
        optionsList.add("Select User");
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
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DistributorsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {

            favouriteItem.clear();
            try {

                /*String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","
                        + KEY_TABLE_CUSTOMER_NAME
                        + "," + KEY_TABLE_CUSTOMER_CODE
                        + "," + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET
                        + "," + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET
                        + "," + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT
                        + " FROM " + TABLE_USER_COMPANY_CUSTOMER
                        + " AS CDC JOIN " + TABLE_CUSTOMERS
                        + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID
                        + " LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + team + " group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
                */

              /*  String selectQuery  = "SELECT DISTINCT (CR.customer_id)," +
                        "CR.customer_name, " +
                        "CR.customer_code, " +
                        "C.company_id, " +
                        "C.company_code" +
                        " FROM user_company_customer AS CDC JOIN customers AS CR ON CDC.customer_id = CR.customer_id " +
                        "JOIN companies AS C ON C.company_id = CDC.company_id WHERE user_id IN ("+ team +")";*/

                String[] splitedArr= team.split(",");
                int[] arr = new int[splitedArr.length];
                for(int i = 0; i < splitedArr.length; i++) {
                    arr[i] = Integer.parseInt(splitedArr[i]);
                }
                System.out.println(Arrays.toString(arr));
               /* String selectQuery1 = "SELECT DISTINCT (CR.customer_id)," +
                        "CR.customer_name, " +
                        "CR.customer_code, " +
                        "C.company_id, " +
                        "C.company_code" +
                        " FROM user_company_customer AS CDC JOIN customers AS CR ON CDC.customer_id = CR.customer_id " +
                        "JOIN companies AS C ON C.company_id = CDC.company_id WHERE user_id =" + checkuid;*/
               for(int i=0;i<arr.length;i++) {


                    String selectQuery1 = "SELECT DISTINCT (CR.customer_id)," +
                            "CR.customer_name, " +
                            "CR.customer_code, " +
                            "C.company_id, " +
                            "C.company_code" +
                            " FROM user_company_customer AS CDC JOIN customers AS CR ON CDC.customer_id = CR.customer_id " +
                            "JOIN companies AS C ON C.company_id = CDC.company_id WHERE user_id =" + arr[i];

                    String selectQuery = "SELECT "+ db.KEY_TABLE_USERS_FIRST_NAME + ","+ db.KEY_TABLE_USERS_SAP_ID  + " FROM " + db.TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_MASTER_ID + " = " + arr[i];

                    sdbw = db.getWritableDatabase();

                    Log.e("Distributor selectQuery", selectQuery1);
                    Cursor cursor = sdbw.rawQuery(selectQuery1, null);
                    //System.out.println("cursor count "+cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {

                       /* int insidebucket = Integer.parseInt(cursor.getString(3));
                        int outsidebucket = Integer.parseInt(cursor.getString(4));
                        int osa = insidebucket + outsidebucket;*/

                            {
                                System.out.println("+++++++++++" + cursor.getString(0) + ":::" + cursor.getString(3) + ":::" + cursor.getString(4));
                                // Log.e("OSA : ", String.valueOf(osa) + " : osa > 0");

                                HashMap<String, String> map = new HashMap<String, String>();
                                // adding each child node to HashMap key => value
                                map.put("customer_name", cursor.getString(1));
                                map.put("customer_id", cursor.getString(0));
                                map.put("company_id", cursor.getString(3));
                                map.put("company_code", cursor.getString(4));
                                map.put("customer_code", cursor.getString(2));
                                map.put("region_name", "");
                                map.put("division_name", "");
                                map.put("credit_limit", "");
                                map.put("inside_bucket", "");
                                map.put("outside_bucket", "");
                                map.put("osa", "");
                                favouriteItem.add(map);

                            }


                        } while (cursor.moveToNext());
                    }
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

            if (favouriteItem.size()==0){
               /*tv_alert.setVisibility(View.VISIBLE);
               lv_content.setVisibility(View.GONE);*/

                listView.setEmptyView(findViewById(R.id.tv_alert));
            }
            else{
               /* tv_alert.setVisibility(View.GONE);
                lv_content.setVisibility(View.VISIBLE);*/
                adapter.updateResults(favouriteItem);

            }

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
            holder.osa.setText(results.get(position).get("company_code"));

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
