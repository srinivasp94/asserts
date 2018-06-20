package com.nsl.app.complaints;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ComplaintsallActivity extends AppCompatActivity {

    private JSONObject JSONObj;
    private JSONArray JSONArr=null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;
    int i=0,sum_osa=0;
    private static final String URL = "http://www.mehndiqueens.com/indianbeauty/api/mycookingtalent/get";
    private ListView listView;
    private ItemfavitemAdapter adapter;
    String customer_id,comp_id;
    String jsonData,userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color;
    String aging1=null,aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name,tv_code,tv_total_osa_amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_all);
        db=new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(home);
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newfedback = new Intent(getApplicationContext(),MainComplaintsActivity.class);
                startActivity(newfedback);

            }
        });


        listView = (ListView) findViewById(R.id.listView);
        final ArrayList<HashMap<String, String>> feedList= new ArrayList<HashMap<String, String>>();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*TextView divtext=(TextView)findViewById(R.id.tv_div);
                String div_value;
                div_value= divtext.getText().toString();*/

            }
        });
        adapter = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem);
        listView.setAdapter(adapter);
        new Async_getalloffline().execute();



    }

    private class Async_getalloffline extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ComplaintsallActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {



                /*List<Feedback> feedback = db.getAllFeedback();

                for (Feedback fb : feedback) {
                    String log = "Id: " + fb.getID() + " ,Name: " + fb.getFarmerName() + " ,fb place " + fb.getplace() + " ,div sapid: " + fb.getfeedbackmessage();
                    // Writing Contacts to log
                    Log.e("DIVISIONS: ", log);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("sr",       String.valueOf(fb.getID()));
                    map.put("name",     fb.getFarmerName());
                    map.put("place",    fb.getplace());
                    map.put("feedback", fb.getfeedbackmessage());

                    favouriteItem.add(map);*/


            try {
                List<Complaints> complaints = db.getAllComplaint(userId);

                for (Complaints fb : complaints) {



                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("sr", String.valueOf(fb.getID()));
                    map.put("name", fb.get_farmer_name());
                    map.put("area", fb.get_complaint_area_acres());
                    map.put("type", fb.get_complaint_type());
                    map.put("billno", fb.get_bill_number());
                    map.put("retailer", fb.get_retailer_name());

                    favouriteItem.add(map);

                    //  }

                }


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
           // tv_total_osa_amt.setText(String.valueOf(sum_osa));
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

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String,String>>();


        public ItemfavitemAdapter(ComplaintsallActivity pd, ArrayList<HashMap<String, String>> results) {
            this.context = pd;
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
                convertView                 = inflater.inflate(R.layout.row_complaints, parent, false);
                holder.tv_name              = (TextView)convertView.findViewById(R.id.tv_name);
                holder.tv_place             = (TextView)convertView.findViewById(R.id.tv_place);
                holder.tv_comptype          = (TextView)convertView.findViewById(R.id.tv_complainttype);
                holder.tv_billno            = (TextView)convertView.findViewById(R.id.tv_billno);
                holder.tv_retailer          = (TextView)convertView.findViewById(R.id.tv_retailername);
                // holder.tv_aging1          = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.tv_name.setText("Name : "+results.get(position).get("name"));
            holder.tv_place.setText("Complaint Area : "+results.get(position).get("area"));
            holder.tv_comptype.setText("Compalaint Type : "+results.get(position).get("type"));
            holder.tv_billno.setText("Bill No : "+results.get(position).get("billno"));
            holder.tv_retailer.setText("Retailer Name : "+results.get(position).get("retailer"));


            return convertView;
        }


        public class ViewHolder
        {
            public TextView  tv_name,tv_place,tv_comptype,tv_billno,tv_retailer;
            public ImageView img;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }




}



