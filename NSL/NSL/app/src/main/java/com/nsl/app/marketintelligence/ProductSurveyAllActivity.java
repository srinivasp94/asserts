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
import com.nsl.app.MainProductSurveyActivity;
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


    public class ProductSurveyAllActivity extends AppCompatActivity {

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
        String jsonData,userId,id,user,company_name,product_name,name_of_the_check_segment,launch_year,no_units_sold,area_crop_sown_new_product,remarks_unique_feature,created_by,created_on,ffmid,ffmidsqlite;
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
            setContentView(R.layout.activity_product_survey_all);
            sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            userId            = sharedpreferences.getString("userId", "");

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            listView = (ListView) findViewById(R.id.listView);
            listView.setEmptyView(findViewById(R.id.empty));
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

                    Intent newfedback = new Intent(getApplicationContext(),MainProductSurveyActivity.class);
                    startActivity(newfedback);
                    finish();

                }
            });
            refresh = (Button)findViewById(R.id.refresh);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ProductSurveyAllActivity.this,ProductSurveyAllActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            db = new DatabaseHandler(ProductSurveyAllActivity.this);

            if (Common.haveInternet(this)){
                new Async_getallProductsurvey().execute();
            }else {
                getofflineProductsurvey();
            }


            adapter = new ItemfavitemAdapter(ProductSurveyAllActivity.this, favouriteItem);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                }
            });




        }

        private void getofflineProductsurvey() {
            Log.d("Reading: ", "Reading all getofflineProductsurvey from server..");
            List<Product_Survey> commodity_price = db.getAllProduct_Survey(userId);
            if(commodity_price.size()==0){
                empty.setVisibility(View.VISIBLE);
            }
            else {
                empty.setVisibility(View.GONE);
            }
            for (Product_Survey cm : commodity_price) {
                String log = "Id: " + cm.getID() + ",userid: " + userId + " ,company_name: " + cm.get_product_survey_company_name() + " ,product_name: " + cm.get_product_survey_product_name() + "," + cm.get_product_survey_name_of_the_check_segment() + "," + cm.get_product_survey_launch_year() + "," +
                        cm.get_product_survey_no_units_sold() + ","+ cm.get_product_survey_area_crop_sown_new_product() +cm.get_product_survey_remarks_unique_feature() + ",created_by" + cm.get_product_survey_created_by() + ",created_on:" + cm.get_product_survey_created_on()+ ",ffmid:" + cm.get_product_survey_ffmid();

                ffmidsqlite = cm.get_product_survey_ffmid();
                Log.e("commoditypricesqlite: ", log);
            }
            for (Product_Survey fb : commodity_price) {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("product_survey_id", String.valueOf(fb.getID()));
                map.put("company_name", fb.get_product_survey_company_name());
                map.put("product_name", fb.get_product_survey_product_name());
                map.put("name_of_the_check_segment", fb.get_product_survey_name_of_the_check_segment());
                map.put("launch_year",fb.get_product_survey_launch_year());
                map.put("no_units_sold", fb.get_product_survey_no_units_sold());
                map.put("area_crop_sown_new_product", fb.get_product_survey_area_crop_sown_new_product());
                map.put("remarks_unique_feature", fb.get_product_survey_remarks_unique_feature());
                map.put("created_by", fb.get_product_survey_created_by());
                map.put("created_on", fb.get_product_survey_created_on());
                favouriteItem.add(map);

            }
            adapter = new ItemfavitemAdapter(ProductSurveyAllActivity.this, favouriteItem);
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


            public ItemfavitemAdapter(ProductSurveyAllActivity pd, ArrayList<HashMap<String, String>> results) {
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
                    convertView                 = inflater.inflate(R.layout.row_product_survey, parent, false);
                    holder.tv_companyname              = (TextView)convertView.findViewById(R.id.tv_companyname);
                    holder.tv_productname          = (TextView)convertView.findViewById(R.id.tv_productname);
                    holder.tv_segmentname              = (TextView)convertView.findViewById(R.id.tv_segmentname);
                    holder.tv_launch_year          = (TextView)convertView.findViewById(R.id.tv_launch_year);
                    holder.tv_units_sold              = (TextView)convertView.findViewById(R.id.tv_units_sold);
                    holder.tv_area_sown          = (TextView)convertView.findViewById(R.id.tv_area_sown);
                    holder.tv_remarks          = (TextView)convertView.findViewById(R.id.tv_remarks);


                    //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHolder)convertView.getTag();
                }
                //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
                holder.tv_companyname.setText("COMPANY:"+" "+results.get(position).get("company_name"));
                //  holder.tv_place.setVisibility(View.GONE);
                holder.tv_productname.setText("PRODUCT:"+" "+results.get(position).get("product_name"));
                holder.tv_segmentname.setText("SEGMENT:"+" "+results.get(position).get("name_of_the_check_segment"));
                holder.tv_launch_year.setText("LAUNCH YEAR:"+" "+results.get(position).get("launch_year"));
                holder.tv_units_sold.setText("SOLD UNITS:"+" "+results.get(position).get("no_units_sold"));
                holder.tv_area_sown.setText("SOWN AREA:"+" "+results.get(position).get("area_crop_sown_new_product"));
                holder.tv_remarks.setText("REMARKS:"+" "+results.get(position).get("remarks_unique_feature"));
                // fdbk = results.get(position).get("feedback");
                // int len= fdbk.length();
                //  if(len > 60){
                //      holder.tv_feedback.setText(fdbk.substring(0,60)+"...");
                //  }
                //   else
                //      holder.tv_feedback.setText(fdbk);
                return convertView;
            }


            public class ViewHolder
            {
                public TextView  tv_companyname,tv_productname,tv_segmentname,tv_launch_year,tv_units_sold,tv_area_sown,tv_remarks;
                public ImageView img;

            }
            public void updateResults(ArrayList<HashMap<String, String>> results) {

                this.results = results;
                notifyDataSetChanged();
            }
        }


        public class Async_getallProductsurvey extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            progressDialog = new ProgressDialog(ProductSurveyAllActivity.this);
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
                            .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_PRODUCT_SURVEY)
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
                            db.deleteDataByTableName(db.TABLE_MI_PRODUCT_SURVEY);

                            JSONArray companyarray = new JSONArray(jsonData);
                            System.out.println(companyarray.length());
                            Log.e("Length product_survey", String.valueOf(companyarray.length()));

                            for (int n = 0; n < companyarray.length(); n++) {
                                JSONObject objinfo = companyarray.getJSONObject(n);

                                Product_Survey ps = new Product_Survey();

                                ps._product_survey_master_id = objinfo.getString("product_survey_id");
                                //   user = objinfo.getString("user_id");
                                ps._product_survey_company_name = objinfo.getString("company_name");
                                ps._product_survey_product_name = objinfo.getString("product_name");
                                ps._product_survey_name_of_the_check_segment = objinfo.getString("name_of_the_check_segment");
                                ps._product_survey_launch_year = objinfo.getString("launch_year");
                                ps._product_survey_no_units_sold = objinfo.getString("no_units_sold");
                                ps._product_survey_area_crop_sown_new_product = objinfo.getString("area_crop_sown_new_product");
                                ps._product_survey_remarks_unique_feature = objinfo.getString("remarks_unique_feature");
                                ps._product_survey_created_by = objinfo.getString("created_by");
                                ps._product_survey_created_on = objinfo.getString("created_on");
                                ps._product_survey_ffmid = objinfo.getString("product_survey_id");


                                Log.d("Insert: ", "Inserting commodity price ..");
                                db.addProductServey(new Product_Survey(ps._product_survey_master_id,ps._product_survey_company_name, ps._product_survey_product_name, ps._product_survey_name_of_the_check_segment,ps._product_survey_launch_year,ps._product_survey_no_units_sold,ps._product_survey_area_crop_sown_new_product,ps._product_survey_remarks_unique_feature,ps._product_survey_created_by,ps._product_survey_created_on,ps._product_survey_ffmid));


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
                getofflineProductsurvey();
                Common.dismissProgressDialog(progressDialog);
            }

        }

    }



