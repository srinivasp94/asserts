package com.nsl.app.marketintelligence;


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


public class CommodityPriceAllActivity extends AppCompatActivity {

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
    String jsonData,userId,id,user,crop_name,variety_type,apmc_mandi_price,commodity_dealer_agent_price,purchage_price_by_industry,created_by,created_on,ffmid,ffmidsqlite;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color,fdbk;
    Button refresh;
    String aging1=null,aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name,tv_code,tv_total_osa_amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_price_all);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listView);
        empty = (TextView)findViewById(R.id.empty);
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

                Intent newfedback = new Intent(getApplicationContext(),MainCommodityPriceActivity.class);
                startActivity(newfedback);
                finish();

            }
        });
       /* refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommodityPriceAllActivity.this,CommodityPriceAllActivity.class);
                startActivity(i);
                finish();
            }
        });*/

        db = new DatabaseHandler(CommodityPriceAllActivity.this);
        if (Common.haveInternet(this)){

            new Async_getallCommodityPrice().execute();
        }else
        {
            getofflineCommodityprice();
        }


        adapter = new ItemfavitemAdapter(CommodityPriceAllActivity.this, favouriteItem);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });




    }

    private void getofflineCommodityprice() {
        Log.d("Reading: ", "Reading all Commodityprice from server..");
        List<Commodity_Price> commodity_price = db.getAllCommodity_price(userId);
        if(commodity_price.size()==0){
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.GONE);
        }
        for (Commodity_Price cm : commodity_price) {
            String log = "Id: " + cm.getID() + ",userid: " + userId + " ,cropname: " + cm.get_commodity_price_crop_name() + " ,variety: " + cm.get_commodity_price_variety_type() + ",apmc_mandi_price:" + cm.get_commodity_price_apmc_mandi_price() + " ,commodity_dealer_agent_price: " + cm.get_commodity_price_commodity_dealer_agent_price() + ",purchage_price_by_industry:" +
                    cm.get_commodity_price_purchage_price_by_industry() + ",created_by" + cm.get_commodity_price_created_by() + ",created_on:" + cm.get_commodity_price_created_on()+ ",ffmid:" + cm.get_commodity_price_ffmid();
            ffmidsqlite = cm.get_commodity_price_ffmid();
            Log.e("commoditypricesqlite: ", log);
        }
        for (Commodity_Price fb : commodity_price) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("commodity_price_id", String.valueOf(fb.getID()));
            map.put("crop_name", fb.get_commodity_price_crop_name());
            map.put("variety_type", fb.get_commodity_price_variety_type());
            map.put("apmc_mandi_price", fb.get_commodity_price_apmc_mandi_price());
            map.put("commodity_dealer_agent_price",fb.get_commodity_price_commodity_dealer_agent_price());
            map.put("purchage_price_by_industry", fb.get_commodity_price_purchage_price_by_industry());
            map.put("created_by", fb.get_commodity_price_created_by());
            map.put("created_on", fb.get_commodity_price_created_on());
            favouriteItem.add(map);

        }
        adapter = new ItemfavitemAdapter(CommodityPriceAllActivity.this, favouriteItem);
        listView.setAdapter(adapter);

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


        public ItemfavitemAdapter(CommodityPriceAllActivity pd, ArrayList<HashMap<String, String>> results) {
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
                convertView                 = inflater.inflate(R.layout.row_commodityprice, parent, false);
                holder.tv_cropname              = (TextView)convertView.findViewById(R.id.tv_cropname);
                holder.tv_crop_variety          = (TextView)convertView.findViewById(R.id.tv_crop_variety);
                holder.tv_apmc_mandi_price              = (TextView)convertView.findViewById(R.id.tv_apmc_mandi_price);
                holder.tv_commodity_dealer          = (TextView)convertView.findViewById(R.id.tv_commodity_dealer);
                holder.tv_purchase_price          = (TextView)convertView.findViewById(R.id.tv_purchase_price);

                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.tv_cropname.setText("CROP:" + results.get(position).get("crop_name"));
                       //  holder.tv_place.setVisibility(View.GONE);
            holder.tv_crop_variety.setText("VARIETY: " + results.get(position).get("variety_type"));
            holder.tv_apmc_mandi_price.setText("MANDI PRICE: " +results.get(position).get("apmc_mandi_price"));
            holder.tv_commodity_dealer.setText("AGENT PRICE: " + results.get(position).get("commodity_dealer_agent_price"));
            holder.tv_purchase_price.setText("PURCHASE PRICE: "+results.get(position).get("purchage_price_by_industry"));

            return convertView;
        }


        public class ViewHolder
        {
            public TextView  tv_cropname,tv_crop_variety,tv_apmc_mandi_price,tv_commodity_dealer,tv_purchase_price;
            public ImageView img;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }


    public class Async_getallCommodityPrice extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommodityPriceAllActivity.this);
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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_COMMODITY_PRICE)
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
                        db.deleteDataByTableName(db.TABLE_MI_COMMODITY_PRICE);
                        JSONArray companyarray = new JSONArray(jsonData);
                        System.out.println(companyarray.length());
                        Log.e("Length commodity price", String.valueOf(companyarray.length()));

                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);

                            Commodity_Price cp = new Commodity_Price();

                            cp._commodity_price_id = Integer.parseInt(objinfo.getString("commodity_price_id"));
//                            user = objinfo.getString("user_id");
                            cp._commodity_price_crop_name = objinfo.getString("crop_name");
                            cp._commodity_price_variety_type = objinfo.getString("variety_type");
                            cp._commodity_price_apmc_mandi_price = objinfo.getString("apmc_mandi_price");
                            cp._commodity_price_commodity_dealer_agent_price = objinfo.getString("commodity_dealer_agent_price");
                            cp._commodity_price_purchage_price_by_industry = objinfo.getString("purchage_price_by_industry");
                            cp._commodity_price_created_by = objinfo.getString("created_by");
                            cp._commodity_price_created_on = objinfo.getString("created_on");
                            cp._commodity_price_ffmid = objinfo.getString("commodity_price_id");



                            db.addCommodityPrice(new Commodity_Price(String.valueOf(cp._commodity_price_id),cp._commodity_price_crop_name,cp._commodity_price_variety_type,cp. _commodity_price_apmc_mandi_price,cp. _commodity_price_commodity_dealer_agent_price, cp._commodity_price_purchage_price_by_industry, cp._commodity_price_created_by, cp._commodity_price_created_on, cp._commodity_price_ffmid));


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

            getofflineCommodityprice();
            Common.dismissProgressDialog(progressDialog);

        }

    }
}



