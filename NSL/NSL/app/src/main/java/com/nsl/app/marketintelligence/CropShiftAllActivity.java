package com.nsl.app.marketintelligence;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;
import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CropShiftAllActivity extends AppCompatActivity {

    private JSONObject JSONObj;
    private JSONArray JSONArr=null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    TextView empty;
    SQLiteDatabase sdbw,sdbr;
    int i=0,sum_osa=0,flag=0;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    String customer_id,comp_id,cropname;
    String jsonData,userId,id,user,crop_name,variety_type,previous_year_area,current_year_expected_area,percentage_increase_decrease,reason_crop_shift,crop_in_saved_seed,created_by,created_on,crop_in_previous_year,crop_in_current_year,crop_in_next_year,ffmid,ffmidsqlite;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color,fdbk;
    Button refresh;
    String aging1=null,aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name,tv_code,tv_total_osa_amt;
    private Crop_Shifts[] crop_shifts;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_shifts_all);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId","");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newfedback = new Intent(getApplicationContext(),MarketIntelligenceAcivity.class);
                startActivity(newfedback);
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newfedback = new Intent(getApplicationContext(),MainCropShiftActivity.class);
                startActivity(newfedback);
                finish();

            }
        });
        refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CropShiftAllActivity.this,CropShiftAllActivity.class);
                startActivity(i);
                finish();
            }
        });

        db = new DatabaseHandler(CropShiftAllActivity.this);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ItemfavitemAdapter(CropShiftAllActivity.this, favouriteItem);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty));
        empty = (TextView)findViewById(R.id.empty);

        if (Common.haveInternet(this)){

            new Async_getallCropshifts().execute();
        }else
        {
            getofflineCropshifts();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });





    }

    private void getofflineCropshifts() {
        Log.d("Reading: ", "Reading all Cropshifts from server..");
        List<Crop_Shifts> crop_shiftsList = db.getAllCrop_Shifts(userId);
        if(crop_shiftsList.size()==0){
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.GONE);
        }
       /* for (Crop_Shifts cm : crop_shiftsList) {
            String log = "Id: " + cm.getID() + ",userid: " + userId + " ,cropname: " + cm.get_commodity_price_crop_name() + " ,variety: " + cm.get_commodity_price_variety_type() + ",apmc_mandi_price:" + cm.get_commodity_price_apmc_mandi_price() + " ,commodity_dealer_agent_price: " + cm.get_commodity_price_commodity_dealer_agent_price() + ",purchage_price_by_industry:" +
                    cm.get_commodity_price_purchage_price_by_industry() + ",created_by" + cm.get_commodity_price_created_by() + ",created_on:" + cm.get_commodity_price_created_on();
            ffmidsqlite = cm.get_commodity_price_ffmid();
            Log.e("commoditypricesqlite: ", log);
        } */
        for (Crop_Shifts fb : crop_shiftsList) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("crop_shifts_id", String.valueOf(fb.getID()));
            map.put("crop_name", fb.get_crop_shifts_crop_name());
            map.put("variety_type", fb.get_crop_shifts_variety_type());
            map.put("previous_year_area", fb.get_crop_shifts_previous_year_area());
            map.put("current_year_expected_area",fb.get_crop_shifts_current_year_expected_area());
            map.put("percentage_increase_decrease", fb.get_crop_shifts_percentage_increase_decrease());
            map.put("reason_crop_shift", fb.get_crop_shifts_reason_crop_shift());
            map.put("created_by", fb.get_crop_shifts_created_by());
            map.put("created_on", fb.get_crop_shifts_created_on());
            map.put("crop_in_saved_seed", fb.get_crop_shifs_crop_in_saved_seed());

            favouriteItem.add(map);

        }
        adapter = new ItemfavitemAdapter(CropShiftAllActivity.this, favouriteItem);
        //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
        listView.setAdapter(adapter);

    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
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


        public ItemfavitemAdapter(CropShiftAllActivity pd, ArrayList<HashMap<String, String>> results) {
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
                convertView                     = inflater.inflate(R.layout.row_crop_shift, parent, false);
                holder.tv_cropname              = (TextView)convertView.findViewById(R.id.tv_cropname);
                holder.tv_variety               = (TextView)convertView.findViewById(R.id.tv_variety);
                holder.tv_previous_year_area    = (TextView)convertView.findViewById(R.id.tv_previous_year_area);
                holder.tv_current_year_area     = (TextView)convertView.findViewById(R.id.tv_current_year_area);
                holder.tv_percentage_inc_dec    = (TextView)convertView.findViewById(R.id.tv_percentage_inc_dec);
                holder.tv_reason_crop_shift     = (TextView)convertView.findViewById(R.id.tv_reason_crop_shift);
                holder.tv_crop_saved_seed       = (TextView)convertView.findViewById(R.id.tv_crop_saved_seed);


                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.tv_cropname.setText("CROP:"+" "+results.get(position).get("crop_name"));
                       //  holder.tv_place.setVisibility(View.GONE);
            holder.tv_variety.setText("Variety:"+" "+results.get(position).get("variety_type"));
            holder.tv_previous_year_area.setText("PREV. YEAR AREA:"+" "+results.get(position).get("previous_year_area"));
            holder.tv_current_year_area.setText("CURR. YEAR AREA:"+" "+results.get(position).get("current_year_expected_area"));
            holder.tv_percentage_inc_dec.setText("%INC/DEC:"+" "+results.get(position).get("percentage_increase_decrease"));
            holder.tv_reason_crop_shift.setText("REASON:"+" "+results.get(position).get("reason_crop_shift"));
            holder.tv_crop_saved_seed.setText("SEED USED:"+" "+results.get(position).get("crop_in_saved_seed"));


            return convertView;
        }


        public class ViewHolder
        {
            public TextView  tv_cropname,tv_variety,tv_previous_year_area,tv_current_year_area,tv_percentage_inc_dec,tv_reason_crop_shift,tv_crop_saved_seed;
            public ImageView img;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }

    public class Async_getallCropshifts extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CropShiftAllActivity.this);
            progressDialog.setMessage("Please Wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();


        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_CROP_SHIFTS)
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


                if (jsonData != null) {
                    try {
                        db.deleteDataByTableName(db.TABLE_MI_CROP_SHIFTS);
                        JSONArray companyarray = new JSONArray(jsonData);
                        System.out.println(companyarray.length());
                        Log.e("Length commodity price", String.valueOf(companyarray.length()));

                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);

                            Crop_Shifts cs = new Crop_Shifts();

                            cs._crop_shifts_master_id = objinfo.getString("crop_shifts_id");
                            //   user = objinfo.getString("user_id");
                            cs._crop_shifts_crop_name = objinfo.getString("crop_name");
                            cs._crop_shifts_variety_type = objinfo.getString("variety_type");
                            cs._crop_shifts_previous_year_area = objinfo.getString("previous_year_area");
                            cs._crop_shifts_current_year_expected_area = objinfo.getString("current_year_expected_area");
                            cs._crop_shifts_percentage_increase_decrease = objinfo.getString("percentage_increase_decrease");
                            cs._crop_shifts_reason_crop_shift = objinfo.getString("reason_crop_shift");
                            cs._crop_shifs_crop_in_saved_seed = objinfo.getString("crop_in_saved_seed");
                            cs._crop_shifts_created_by = objinfo.getString("created_by");
                            cs._crop_shifts_created_on = objinfo.getString("created_on");
                            cs._crop_shifs_crop_in_previous_year = objinfo.getString("previous_year_srr");
                            cs._crop_shifs_crop_in_current_year = objinfo.getString("current_year_srr");
                            cs._crop_shifs_crop_in_next_year = objinfo.getString("next_year_srr");
                            cs._crop_shifts_ffmid = objinfo.getString("crop_shifts_id");


                            db.addCropShifts(new Crop_Shifts( cs._crop_shifts_master_id, cs._crop_shifts_crop_name,  cs._crop_shifts_variety_type,  cs._crop_shifts_previous_year_area,  cs._crop_shifts_current_year_expected_area,  cs._crop_shifts_percentage_increase_decrease, cs._crop_shifts_reason_crop_shift, cs._crop_shifs_crop_in_saved_seed,  cs._crop_shifts_created_by,  cs._crop_shifts_created_on,  cs._crop_shifs_crop_in_saved_seed,  cs._crop_shifs_crop_in_previous_year, cs._crop_shifs_crop_in_current_year, cs._crop_shifs_crop_in_next_year, cs._crop_shifts_ffmid));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    //  db.deleteFeedback();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getofflineCropshifts();
            Common.dismissProgressDialog(progressDialog);

        }

    }


}



