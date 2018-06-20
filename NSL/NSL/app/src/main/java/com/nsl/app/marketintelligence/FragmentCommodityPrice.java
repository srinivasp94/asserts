package com.nsl.app.marketintelligence;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;
import com.nsl.app.Utility;
import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_COMMODITY_PRICE_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_COMMODITY_PRICE_ID;
import static com.nsl.app.DatabaseHandler.TABLE_MI_COMMODITY_PRICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCommodityPrice extends Fragment{
    private Button btnSubmit,cancel;
    String sqliteid,ffmid;
    private static final String HOME_URL = "http://www.mehndiqueens.com/indianbeauty/api/user/images";
    ViewPager mViewPager;
    FrameLayout fm;
    DatabaseHandler db;
    private static SQLiteDatabase sql, sdbr,sdbw;
    String checkuid,jsonData;
    EditText crop_name, variety, mandi_price, agent_price,industry_price;
    ProgressDialog progressDialog;
    TextView tvInvisibleError;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    public FragmentCommodityPrice() {

    }
    @SuppressLint("ValidFragment")
    public FragmentCommodityPrice(DatabaseHandler db) {
        this.db = db;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());

       /* FragmentFeedback fb = new FragmentFeedback();
        fb.checkConnection();*/
        View view = inflater.inflate(R.layout.activity_commodity_price, container, false);
        btnSubmit = (Button) view.findViewById(R.id.btn_save);
        cancel = (Button) view.findViewById(R.id.btn_cancel);

        crop_name = (EditText) view.findViewById(R.id.et_crop_name);
        variety = (EditText) view.findViewById(R.id.et_variety);
        mandi_price = (EditText) view.findViewById(R.id.et_apmc);
        industry_price = (EditText) view.findViewById(R.id.et_purchase_price);
        agent_price = (EditText) view.findViewById(R.id.et_commodity_dealer);
        tvInvisibleError = (TextView) view.findViewById(R.id.tvInvisibleError);
        fm = (FrameLayout) view.findViewById(R.id.frame);
        crop_name.requestFocus();
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        Log.e("userid is : ", checkuid);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewall = new Intent(getActivity(),CommodityPriceAllActivity.class);
                startActivity(viewall);
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    private void validate() {
        Handler handler=Common.disableClickEvent(btnSubmit,true);
        String cpid = "1";
        String cropname = crop_name.getText().toString();
        String varietyortype = variety.getText().toString();
        String agentprice = agent_price.getText().toString();
        String industryprice = industry_price.getText().toString();
        String mandiprice = mandi_price.getText().toString();


        if (TextUtils.isEmpty(cropname) || cropname.length() > 0 && cropname.startsWith(" ")) {
            crop_name.setError("Please enter Crop name");
            crop_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(varietyortype) || varietyortype.length() > 0 && varietyortype.startsWith(" ")) {
            variety.setError("Please enter Variety");
            variety.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(agentprice) || agentprice.length() > 0 && agentprice.startsWith(" ")) {
            agent_price.setError("Please enter agent price");
            agent_price.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(industryprice) || industryprice.length() > 0 && industryprice.startsWith(" ")) {
            industry_price.setError("Please enter valid industry price");
            industry_price.requestFocus();
            return;
        }
       if (TextUtils.isEmpty(mandiprice) || mandiprice.length() > 0 && mandiprice.startsWith(" ")) {
            industry_price.setError("Please enter valid mandi price");
            industry_price.requestFocus();
            return;
        }




        else {
           Common.disableClickEvent(btnSubmit,handler);
            //  Toast.makeText(getActivity(), "Inserting...", Toast.LENGTH_SHORT).show();
            int fdid = Integer.parseInt(cpid);

            //String ivImage="xyz.jpg";

            db.addCommodityPrice(new Commodity_Price(String.valueOf(fdid),cropname, varietyortype, mandiprice, agentprice, industryprice,checkuid,null,null));
            Log.e("crop name ", cropname + ":" + varietyortype + ":" + mandiprice);
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put("Id", String.valueOf(fdid));
            map.put("user_id",checkuid);
            map.put("cropname", cropname);
            map.put("varietyortype", varietyortype);
            map.put("mandiprice", mandiprice);
            map.put("agentprice", agentprice);
            map.put("industryprice", industryprice);
            map.put("ffmid", ffmid);

            favouriteItem.add(map);
            // do some stuff....

        }
        if(Utility.isNetworkAvailable(getActivity(), Constants.isShowNetworkToast)){
            List<Commodity_Price> commodity = db.getAllCommodity_price(checkuid);

            for (Commodity_Price cp : commodity) {
                String log = "Id: " + cp.getID()+",Name: " + cp.get_commodity_price_crop_name() + " variety: " + cp.get_commodity_price_variety_type() + cp.get_commodity_price_apmc_mandi_price()+cp.get_commodity_price_commodity_dealer_agent_price()+cp.get_commodity_price_purchage_price_by_industry()+cp.get_commodity_price_ffmid();

                Log.e("commodity_price: ", log);
            }
            insertToService();
        }
        else {
            Intent complaints = new Intent(getActivity(),CommodityPriceAllActivity.class);
            startActivity(complaints);

        }
        }

    private void insertToService() {
               List<Commodity_Price> commodityPrice = db.getAllnullCommodity_price(checkuid);
        Log.e("list size", String.valueOf(commodityPrice.size()));
        for(Commodity_Price cp: commodityPrice)
            new Async_InsertCommodity_Price().execute(String.valueOf(cp.getID()),cp.get_commodity_price_crop_name(),cp.get_commodity_price_variety_type(),cp.get_commodity_price_apmc_mandi_price(),cp.get_commodity_price_commodity_dealer_agent_price(),cp.get_commodity_price_purchage_price_by_industry(),cp.get_commodity_price_created_by());

    }

    private class Async_InsertCommodity_Price extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            //  progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
                        .add("table",TABLE_MI_COMMODITY_PRICE)
                        .add("mobile_id", params[0])
                       // .add("user_id", String.valueOf(favouriteItem.get(1)))
                        .add("crop_name", params[1])
                        .add("variety_type", params[2])
                        .add("apmc_mandi_price", params[3])
                        .add("commodity_dealer_agent_price", params[4])
                        .add("purchage_price_by_industry",params[5])
                        .add("created_by",params[6])

                        .build();

                Response responses = null;

                System.out.println("---- cmp data -----" +params[0]+ params[1]+params[2]+params[3]+params[4]+params[5]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_PUSHTABLE)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 Insertcmp" + jsonData);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }



            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing())
                progressDialog.dismiss();


            if (jsonData != null)
            {   JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        //  Toast.makeText(getActivity(), "Feed back inserted sucessfully", Toast.LENGTH_SHORT).show();
                        sqliteid = jsonobject.getString("sqlite");
                        ffmid = jsonobject.getString("ffm_id");

                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", sqliteid);
                        Log.e("ffmid", ffmid);
                        sdbw = db.getWritableDatabase();
                        // updateFeedback(Feedback feedback);
                        String updatequery = "UPDATE " + TABLE_MI_COMMODITY_PRICE + " SET " + KEY_COMMODITY_PRICE_FFMID + " = " + ffmid + " WHERE " + KEY_COMMODITY_PRICE_ID + " = " + sqliteid;
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);
                        List<Commodity_Price> commodity = db.getAllCommodity_price(checkuid);

                        for (Commodity_Price cp : commodity) {
                            String log = "Id: " + cp.getID()+",Name: " + cp.get_commodity_price_crop_name() + " variety: " + cp.get_commodity_price_variety_type() + cp.get_commodity_price_apmc_mandi_price()+cp.get_commodity_price_commodity_dealer_agent_price()+cp.get_commodity_price_purchage_price_by_industry()+cp.get_commodity_price_ffmid();

                            Log.e("commodity_price: ", log);
                        }



                                        Intent complaints = new Intent(getActivity(),CommodityPriceAllActivity.class);
                                                        startActivity(complaints);



                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

        }

    }
}

