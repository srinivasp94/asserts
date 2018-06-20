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


public class PricingSurveyAllActivity extends AppCompatActivity {
    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    TextView empty;
    SQLiteDatabase sdbw, sdbr;
    int i = 0, sum_osa = 0, flag = 0;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    String customer_id, comp_id, cropname;
    String jsonData, userId, id, user, company_name, product_name, sku_pack_size, retail_price, invoice_price, net_distributor_landing_price, created_by, created_on, ffmid, ffmidsqlite;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color, fdbk;
    Button refresh;
    String aging1 = null, aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name, tv_code, tv_total_osa_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing_survey_all);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");

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

                Intent newfedback = new Intent(getApplicationContext(), MainPricingSurveyActivity.class);
                startActivity(newfedback);
                finish();

            }
        });
        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PricingSurveyAllActivity.this, PricingSurveyAllActivity.class);
                startActivity(i);
                finish();
            }
        });

        db = new DatabaseHandler(PricingSurveyAllActivity.this);

        listView = (ListView) findViewById(R.id.listView);
        empty = (TextView) findViewById(R.id.empty);
        adapter = new ItemfavitemAdapter(PricingSurveyAllActivity.this, favouriteItem);
        listView.setAdapter(adapter);

        listView.setEmptyView(findViewById(R.id.empty));


              if (Common.haveInternet(this)){
                  new Async_getallPriceSurvey().execute();
              }else {
                  getofflinePriceSurvey();
              }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });




    }

    private void getofflinePriceSurvey() {
        Log.d("Reading: ", "Reading all getofflinePriceSurvey from server..");
        List<Price_Survey> commodity_price = db.getAllPrice_Survey(userId);
        if (commodity_price.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }
        for (Price_Survey cm : commodity_price) {
            String log = "Id: " + cm.getID() + ",userid: " + userId + " ,company_name: " + cm.get_price_survey_company_name() + " ,product_name: " + cm.get_price_survey_product_name() + ",sku_pack_size:" + cm.get_price_survey_sku_pack_size() + " ,retail_price: " + cm.get_price_survey_retail_price() + ",invoice_price:" +
                    cm.get_price_survey_invoice_price() + ",net_distributor_landing_price" + cm.get_price_survey_net_distributor_landing_price() + ",created_by" + cm.get_price_survey_created_by() + ",created_on:" + cm.get_price_survey_created_on() + ",ffmid:" + cm.get_price_survey_ffmid();

            ffmidsqlite = cm.get_price_survey_ffmid();
            Log.e("commoditypricesqlite: ", log);
        }
        for (Price_Survey fb : commodity_price) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("price_survey_id", String.valueOf(fb.getID()));
            map.put("company_name", fb.get_price_survey_company_name());
            map.put("product_name", fb.get_price_survey_product_name());
            map.put("sku_pack_size", fb.get_price_survey_sku_pack_size());
            map.put("retail_price", fb.get_price_survey_retail_price());
            map.put("invoice_price", fb.get_price_survey_invoice_price());
            map.put("net_distributor_landing_price", fb.get_price_survey_net_distributor_landing_price());
            map.put("created_by", fb.get_price_survey_created_by());
            map.put("created_on", fb.get_price_survey_created_on());
            favouriteItem.add(map);

        }
        adapter = new ItemfavitemAdapter(PricingSurveyAllActivity.this, favouriteItem);
        //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
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
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(PricingSurveyAllActivity pd, ArrayList<HashMap<String, String>> results) {
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
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_pricing_survey, parent, false);
                holder.t_name = (TextView) convertView.findViewById(R.id.tv_company);
                holder.t_product = (TextView) convertView.findViewById(R.id.tv_product);
                holder.t_sku = (TextView) convertView.findViewById(R.id.tv_sku_pack_size);
                holder.t_retailPrice = (TextView) convertView.findViewById(R.id.tv_retailer_price);
                holder.t_invoice = (TextView) convertView.findViewById(R.id.tv_invoice);
                holder.t_distributorLandingPrice = (TextView) convertView.findViewById(R.id.tv_dist_landing_price);

                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.t_name.setText("COMPANY:"+" "+results.get(position).get("company_name"));
            //  holder.tv_place.setVisibility(View.GONE);
            holder.t_product.setText("PRODUCT:"+" "+results.get(position).get("product_name"));
            holder.t_sku.setText("PACKET SIZE:"+" "+results.get(position).get("sku_pack_size"));
            holder.t_retailPrice.setText("RETAIL PRICE:"+" "+results.get(position).get("retail_price"));
            holder.t_invoice.setText("INVOICE PRICE:"+" "+results.get(position).get("invoice_price"));
            holder.t_distributorLandingPrice.setText("LANDING PRICE:"+" "+results.get(position).get("net_distributor_landing_price"));
            // fdbk = results.get(position).get("feedback");
            // int len= fdbk.length();
            //  if(len > 60){
            //      holder.tv_feedback.setText(fdbk.substring(0,60)+"...");
            //  }
            //   else
            //      holder.tv_feedback.setText(fdbk);
            return convertView;
        }


        public class ViewHolder {
            public TextView t_name, t_product, t_sku, t_retailPrice, t_invoice, t_distributorLandingPrice;
            public ImageView img;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }




    public class Async_getallPriceSurvey extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PricingSurveyAllActivity.this);
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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_PRICE_SURVEY)
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
                        db.deleteDataByTableName(db.TABLE_MI_PRICE_SURVEY);
                        JSONArray companyarray = new JSONArray(jsonData);
                        System.out.println(companyarray.length());
                        Log.e("Length PRICE_SURVEY", String.valueOf(companyarray.length()));

                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);

                            Price_Survey prs = new Price_Survey();

                            prs._price_survey_master_id = objinfo.getString("price_survey_id");
                            //   user = objinfo.getString("user_id");
                            prs._price_survey_company_name = objinfo.getString("company_name");
                            prs._price_survey_product_name = objinfo.getString("product_name");
                            prs._price_survey_sku_pack_size = objinfo.getString("sku_pack_size");
                            prs._price_survey_retail_price = objinfo.getString("retail_price");
                            prs._price_survey_invoice_price = objinfo.getString("invoice_price");
                            prs._price_survey_net_distributor_landing_price = objinfo.getString("net_distributor_landing_price");
                            prs._price_survey_created_by = objinfo.getString("created_by");
                            prs._price_survey_created_on = objinfo.getString("created_on");
                            prs._price_survey_ffmid = objinfo.getString("price_survey_id");




                            Log.d("Insert: ", "Inserting commodity price ..");
                            db.addPricingServey(new Price_Survey(prs._price_survey_master_id, prs._price_survey_company_name, prs._price_survey_product_name, prs._price_survey_sku_pack_size, prs._price_survey_retail_price, prs._price_survey_invoice_price, prs._price_survey_net_distributor_landing_price, prs._price_survey_created_by, prs._price_survey_created_on, prs._price_survey_ffmid));


                            Log.e("Inserted!!!!", "Inserted to sqlite");
                            // do some stuff....



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
            getofflinePriceSurvey();

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


        }

    }
}



