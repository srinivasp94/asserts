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

        import static com.nsl.app.DatabaseHandler.KEY_PRICE_SURVEY_FFMID;
        import static com.nsl.app.DatabaseHandler.KEY_PRICE_SURVEY_ID;
        import static com.nsl.app.DatabaseHandler.TABLE_MI_PRICE_SURVEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPricingServey extends Fragment{
    private Button btnSubmit,cancel;
    String sqliteid,ffmid;
    private static final String HOME_URL = "http://www.mehndiqueens.com/indianbeauty/api/user/images";
    ViewPager mViewPager;
    FrameLayout fm;
    DatabaseHandler db;
    private static SQLiteDatabase sql, sdbr,sdbw;
    String checkuid,jsonData;
    EditText company_name, product_name, pack_size, retail_price,invoice_price,landing_price;
    ProgressDialog progressDialog;
    TextView tvInvisibleError;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    public FragmentPricingServey() {

    }
    @SuppressLint("ValidFragment")
    public FragmentPricingServey(DatabaseHandler db) {
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
        View view = inflater.inflate(R.layout.activity_pricing_survey, container, false);
        btnSubmit = (Button) view.findViewById(R.id.btn_save);
        cancel = (Button) view.findViewById(R.id.btn_cancel);

        company_name = (EditText) view.findViewById(R.id.edt_selectcompany);
        product_name = (EditText) view.findViewById(R.id.edt_selectproduct);
        pack_size = (EditText) view.findViewById(R.id.edt_selectpacksize);
        retail_price = (EditText) view.findViewById(R.id.edt_retailprice);
        invoice_price = (EditText) view.findViewById(R.id.edt_invoiceprice);
        landing_price = (EditText) view.findViewById(R.id.edt_landingprice);
       // tvInvisibleError = (TextView) view.findViewById(R.id.tvInvisibleError);
        fm = (FrameLayout) view.findViewById(R.id.frame);
        company_name.requestFocus();
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
                Intent viewall = new Intent(getActivity(),PricingSurveyAllActivity.class);
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
        String companyname = company_name.getText().toString();
        String productname = product_name.getText().toString();
        String packsize = pack_size.getText().toString();
        String retailprice = retail_price.getText().toString();
        String invoiceprice = invoice_price.getText().toString();
        String landingprice = landing_price.getText().toString();


        if (TextUtils.isEmpty(companyname) || companyname.length() > 0 && companyname.startsWith(" ")) {
            company_name.setError("Please enter Company name");
            company_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(productname) || productname.length() > 0 && productname.startsWith(" ")) {
            product_name.setError("Please enter Product name");
            product_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(packsize) || packsize.length() > 0 && packsize.startsWith(" ")) {
            pack_size.setError("Please enter pack size");
            pack_size.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(retailprice) || retailprice.length() > 0 && retailprice.startsWith(" ")) {
            retail_price.setError("Please enter retail price");
            retail_price.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(invoiceprice) || invoiceprice.length() > 0 && invoiceprice.startsWith(" ")) {
            invoice_price.setError("Please enter invoice price");
            invoice_price.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(landingprice) || landingprice.length() > 0 && landingprice.startsWith(" ")) {
            landing_price.setError("Please enter landing price");
            landing_price.requestFocus();
            return;
        }




        else {
            Common.disableClickEvent(btnSubmit,handler);
            //  Toast.makeText(getActivity(), "Inserting...", Toast.LENGTH_SHORT).show();
            int fdid = Integer.parseInt(cpid);
            String ffmid = "0";
            //String ivImage="xyz.jpg";
            db.addPricingServey(new Price_Survey(String.valueOf(fdid),companyname, productname, packsize, retailprice, invoiceprice,landingprice,checkuid,null,null));
            Log.e("company name ", companyname + ":" + productname + ":" + packsize);
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put("Id", String.valueOf(fdid));
            map.put("user_id",checkuid);
            map.put("companyname", companyname);
            map.put("productname", productname);
            map.put("packsize", packsize);
            map.put("retailprice", retailprice);
            map.put("invoiceprice", invoiceprice);
            map.put("landingprice", landingprice);
            map.put("ffmid", ffmid);

            favouriteItem.add(map);
            // do some stuff....

        }
        if(Utility.isNetworkAvailable(getActivity(), Constants.isShowNetworkToast)){
            List<Price_Survey> priceservey = db.getAllPrice_Survey(checkuid);

            for (Price_Survey cp : priceservey) {
                String log = "Id: " + cp.getID()+",Name: " + cp.get_price_survey_company_name() + " product_name: " + cp.get_price_survey_product_name() + cp.get_price_survey_sku_pack_size()+cp.get_price_survey_retail_price()+cp.get_price_survey_invoice_price()+cp.get_price_survey_net_distributor_landing_price()+cp.get_price_survey_created_by()+cp.get_price_survey_ffmid();

                Log.e("Price_Survey: ", log);
            }
            insertToService();
        }
        else {
            Intent complaints = new Intent(getActivity(),PricingSurveyAllActivity.class);
            startActivity(complaints);

        }
    }

    private void insertToService() {
        List<Price_Survey> pricing = db.getAllnullPrice_Survey(checkuid);
        Log.e("list size", String.valueOf(pricing.size()));
        for(Price_Survey cp: pricing)
            new Async_InsertPricingServey().execute(String.valueOf(cp.getID()),cp.get_price_survey_company_name(),cp.get_price_survey_product_name(),cp.get_price_survey_sku_pack_size(),cp.get_price_survey_retail_price(),cp.get_price_survey_invoice_price(),cp.get_price_survey_net_distributor_landing_price(),cp.get_price_survey_created_by());

    }

    private class Async_InsertPricingServey extends AsyncTask<String, String, String> {
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
                        .add("table",TABLE_MI_PRICE_SURVEY)
                        .add("mobile_id", params[0])
                        // .add("user_id", String.valueOf(favouriteItem.get(1)))
                        .add("company_name", params[1])
                        .add("product_name", params[2])
                        .add("sku_pack_size", params[3])
                        .add("retail_price", params[4])
                        .add("invoice_price",params[5])
                        .add("net_distributor_landing_price",params[6])
                        .add("created_by",params[7])


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
                        String updatequery = "UPDATE " + TABLE_MI_PRICE_SURVEY + " SET " + KEY_PRICE_SURVEY_FFMID + " = " + ffmid + " WHERE " + KEY_PRICE_SURVEY_ID + " = " + sqliteid;
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);
                        List<Price_Survey> price = db.getAllPrice_Survey(checkuid);

                        for (Price_Survey cp : price) {
                            String log = "Id: " + cp.getID()+",Name: " + cp.get_price_survey_company_name() + " product_name: " + cp.get_price_survey_product_name() + cp.get_price_survey_sku_pack_size()+cp.get_price_survey_retail_price()+cp.get_price_survey_invoice_price()+cp.get_price_survey_net_distributor_landing_price()+cp.get_price_survey_ffmid();

                            Log.e("Price_Survey: ", log);
                        }



                        Intent complaints = new Intent(getActivity(),PricingSurveyAllActivity.class);
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

