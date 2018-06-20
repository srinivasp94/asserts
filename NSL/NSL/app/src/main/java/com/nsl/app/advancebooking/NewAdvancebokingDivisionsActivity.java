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

import com.nsl.app.Companies;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewAdvancebokingDivisionsActivity extends AppCompatActivity {
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr=null;



    ProgressDialog progressDialog;

    private ListView listView;
    private ItemfavitemAdapter adapter;

    String jsonData,companyinfo,company_id,customer_id;
    public static  String userId="",sel_userId,role,team;

    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    SharedPreferences sharedpreferences;
    public static final  String mypreference = "mypref";
    public static String divisionname;

    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newadvancebookingdivisions);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");

        companyinfo       = sharedpreferences.getString("companyinfo", "");


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
        company_id        =  getIntent().getStringExtra("company_id");
        customer_id       =  getIntent().getStringExtra("customer_id");

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
                divisionname =  favouriteItem.get(i).get("name");
                //Intent customers = new Intent(getApplicationContext(),AdvancebokingFormActivity.class);
                Intent customers = new Intent(getApplicationContext(),CropsFragmentAdvancebookingActivity.class);

                customers.putExtra("division_id",favouriteItem.get(i).get("company_id"));
                customers.putExtra("customer_id",customer_id);
                customers.putExtra("company_id", company_id);
                customers.putExtra("division",   favouriteItem.get(i).get("name"));

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("company_id",  company_id);
                editor.putString("customer_id", customer_id);
                editor.putString("division_id", favouriteItem.get(i).get("company_id"));
                editor.putString("division",    favouriteItem.get(i).get("name"));
                editor.commit();

                startActivity(customers);
            }
        });
        adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
        listView.setAdapter(adapter);
        /*try {
            ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo != null){  // connected to the internet
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    new Async_getall().execute();
                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan

                    new Async_getall().execute();
                }
            }else{
                new Async_getalloffline().execute();
            }

        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }*/

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


    private class Async_getalloffline extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewAdvancebokingDivisionsActivity.this);
            progressDialog.setMessage("Please wait \n data loading from offline");
            progressDialog.show();
        }

        protected String doInBackground(Void... params)
        {


            try {

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + ","+ KEY_TABLE_DIVISION_NAME+ ","+ KEY_TABLE_DIVISION_CODE + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR."+ KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " and " +KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + company_id + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")" ;
                Log.e("DIVISIONS QUERY",selectQuery);
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Companies companies = new Companies();

                        companies.setCompanyMasterId(cursor.getString(0));
                        companies.setCompanyName(cursor.getString(1));
                        companies.setCompanycode(cursor.getString(2));

                        System.out.println("+++++++++++" + cursor.getString(0)+ cursor.getString(1)+ cursor.getString(2));
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value


                        map.put("company_id",     cursor.getString(0));
                        map.put("name",           cursor.getString(1));
                        map.put("company_code",   cursor.getString(2));
                        map.put("company_sap_id", "");
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
            adapter.updateResults(favouriteItem);
            /*if (jsonData != null && jsonData.startsWith("{"))
            {
                try {
                    JSONObject jsonmainobject = new JSONObject(jsonData);
                    JSONArray  companyarray   = jsonmainobject.getJSONArray("company");

                    for(int n = 0; n < companyarray.length(); n++)
                    {
                        JSONObject object = companyarray.getJSONObject(n);

                        String info = object.getString("info");

                        JSONObject objinfo    = new JSONObject(info);
                        String company_code   = objinfo.getString("company_code");
                        String name           = objinfo.getString("name");
                        String company_sap_id = objinfo.getString("company_sap_id");
                        String company_id     = objinfo.getString("company_id");

                        JSONArray jArraycustomers = object.getJSONArray("customers");
                        String    strcustomers    = jArraycustomers.toString();
                        JSONArray jArraydivisions = object.getJSONArray("divisions");
                        String    strdivisions    = jArraydivisions.toString();
                        db.addCompanies(new Companies(company_id,name,company_code,company_sap_id,"","",""));

                        Log.e("code name sap_id",company_code+":"+name +":"+company_sap_id);
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("company_code",   company_code   );
                        map.put("name",           name);
                        map.put("company_sap_id", company_sap_id);
                        map.put("company_id",     company_id);
                        map.put("strcustomers",   strcustomers);
                        map.put("strdivisions",   strdivisions);


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
            Log.d("Reading: ", "Reading all companies..");

            List<Companies> companies = db.getAllCompanies();

            for (Companies com : companies) {
                String log = "Id: "+com.getID()+ "Company master ID" + com.getCompanyMasterId()+" ,Name: " + com.getCompanyName()+ " ,company code: " + com.getCompanycode()+ " ,company sapid: " + com.getCompanysapid()+ " ,div status: " + com.getCompanystatus()+ " ,company created date: " + com.getCompanycdatetime()+ " ,company updated date : " + com.getCompanyudatetime();
                // Writing Contacts to log
                Log.e("Companies: ", log);

            }
            adapter.updateResults(favouriteItem);*/
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
}
