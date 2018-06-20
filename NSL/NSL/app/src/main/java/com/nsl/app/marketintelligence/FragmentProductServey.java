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
        import static com.nsl.app.DatabaseHandler.TABLE_MI_PRODUCT_SURVEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProductServey extends Fragment{
    private Button btnSubmit,cancel;
    String sqliteid,ffmid;
    private static final String HOME_URL = "http://www.mehndiqueens.com/indianbeauty/api/user/images";
    ViewPager mViewPager;
    FrameLayout fm;
    DatabaseHandler db;
    private static SQLiteDatabase sql, sdbr,sdbw;
    String checkuid,jsonData;
    EditText company_name, product_name, segment_name, launch_year,units_sold,crop_area,remarks;
    ProgressDialog progressDialog;
    TextView tvInvisibleError;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    public FragmentProductServey() {

    }
    @SuppressLint("ValidFragment")
    public FragmentProductServey(DatabaseHandler db) {
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
        View view = inflater.inflate(R.layout.activity_product_survey, container, false);
        btnSubmit = (Button) view.findViewById(R.id.btn_save);
        cancel = (Button) view.findViewById(R.id.btn_cancel);

        company_name = (EditText) view.findViewById(R.id.edt_selectcompany);
        product_name = (EditText) view.findViewById(R.id.edt_selectproduct);
        segment_name = (EditText) view.findViewById(R.id.edt_segmentname);
        launch_year = (EditText) view.findViewById(R.id.edt_launchyear);
        units_sold = (EditText) view.findViewById(R.id.edt_unitssold);
        crop_area = (EditText) view.findViewById(R.id.edt_croparea);
        remarks = (EditText) view.findViewById(R.id.edt_remarks);
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
                Intent viewall = new Intent(getActivity(),ProductSurveyAllActivity.class);
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
        String segment = segment_name.getText().toString();
        String launchyear = launch_year.getText().toString();
        String unitssold = units_sold.getText().toString();
        String croparea = crop_area.getText().toString();
        String remark = remarks.getText().toString();


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
        if (TextUtils.isEmpty(segment) || segment.length() > 0 && segment.startsWith(" ")) {
            segment_name.setError("Please enter segment");
            segment_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(launchyear) || launchyear.length() > 0 && launchyear.startsWith(" ")) {
            launch_year.setError("Please enter launch year");
            launch_year.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(unitssold) || unitssold.length() > 0 && unitssold.startsWith(" ")) {
            units_sold.setError("Please enter units sold");
            units_sold.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(croparea) || croparea.length() > 0 && croparea.startsWith(" ")) {
            crop_area.setError("Please enter crop area");
            crop_area.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(remark) || remark.length() > 0 && remark.startsWith(" ")) {
            remarks.setError("Please enter remarks");
            remarks.requestFocus();
            return;
        }


        else {
            Common.disableClickEvent(btnSubmit,handler);
            //  Toast.makeText(getActivity(), "Inserting...", Toast.LENGTH_SHORT).show();
            int fdid = Integer.parseInt(cpid);
            String ffmid = "0";
            //String ivImage="xyz.jpg";
            db.addProductServey(new Product_Survey(String.valueOf(fdid),companyname, productname, segment, launchyear, unitssold,croparea,remark,checkuid,null,null));
            Log.e("company name ", companyname + ":" + productname + ":" + segment+":"+launchyear+":"+unitssold+":"+croparea+":"+remark+":"+checkuid+":"+null+":"+null);
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put("Id", String.valueOf(fdid));
            map.put("user_id",checkuid);
            map.put("companyname", companyname);
            map.put("productname", productname);
            map.put("segment", segment);
            map.put("launchyear", launchyear);
            map.put("unitssold", unitssold);
            map.put("croparea", croparea);
            map.put("remark", remark);
            map.put("ffmid", null);

            favouriteItem.add(map);
            // do some stuff....

        }
        if(Utility.isNetworkAvailable(getActivity(), Constants.isShowNetworkToast)){

            insertToService();
        }
        else {
            Intent complaints = new Intent(getActivity(),PricingSurveyAllActivity.class);
            startActivity(complaints);

        }
    }

    private void insertToService() {
        List<Product_Survey> pricing = db.getAllnullProduct_Survey(checkuid);
        Log.e("list size", String.valueOf(pricing.size()));
        int i=0;
        for(Product_Survey cp: pricing) {
            i++;
            Log.e(":::", String.valueOf(cp.getID()) + "cmpny : " + cp.get_product_survey_company_name() + " prod: " + cp.get_product_survey_product_name() + "seg:" + cp.get_product_survey_name_of_the_check_segment() + "yr:" + cp.get_product_survey_launch_year() + "units:" + cp.get_product_survey_no_units_sold() + "crop:" + cp.get_product_survey_area_crop_sown_new_product() + "remks: " + cp.get_product_survey_remarks_unique_feature() + "createdby: " + cp.get_product_survey_created_by());
            Log.e("remarks", cp.get_product_survey_remarks_unique_feature() + "I:" + i);
            Log.e("createdby", cp.get_product_survey_created_by() + "I:" + i);

            new Async_InsertProductServey().execute(String.valueOf(cp.getID()), cp.get_product_survey_company_name(), cp.get_product_survey_product_name(), cp.get_product_survey_name_of_the_check_segment(), cp.get_product_survey_launch_year(), cp.get_product_survey_no_units_sold(), cp.get_product_survey_area_crop_sown_new_product(), cp.get_product_survey_remarks_unique_feature(),checkuid);
        }
    }

    private class Async_InsertProductServey extends AsyncTask<String, String, String> {
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
                        .add("table",TABLE_MI_PRODUCT_SURVEY)
                        .add("mobile_id", params[0])
                        // .add("user_id", String.valueOf(favouriteItem.get(1)))
                        .add("company_name", params[1])
                        .add("product_name", params[2])
                        .add("name_of_the_check_segment", params[3])
                        .add("launch_year", params[4])
                        .add("no_units_sold",params[5])
                        .add("area_crop_sown_new_product",params[6])
                        .add("remarks_unique_feature",params[7])
                        .add("created_by",checkuid)

                        .build();

                Response responses = null;

                System.out.println("---- cmp data -----" +params[0]+ params[1]+params[2]+params[3]+params[4]+params[5]+params[6]);

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
                        String updatequery = "UPDATE " + TABLE_MI_PRODUCT_SURVEY + " SET " + KEY_PRICE_SURVEY_FFMID + " = " + ffmid + " WHERE " + KEY_PRICE_SURVEY_ID + " = " + sqliteid;
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);
                        List<Product_Survey> price = db.getAllProduct_Survey(checkuid);

                        for (Product_Survey cp : price) {
                            String log = "Id: " + cp.getID()+",Name: " + cp.get_product_survey_company_name() + " product_name: " + cp.get_product_survey_product_name() + cp.get_product_survey_name_of_the_check_segment()+cp.get_product_survey_launch_year()+cp.get_product_survey_no_units_sold()+cp.get_product_survey_area_crop_sown_new_product()+cp.get_product_survey_remarks_unique_feature()+cp.get_product_survey_ffmid();

                            Log.e("Product_Survey: ", log);
                        }



                        Intent complaints = new Intent(getActivity(),ProductSurveyAllActivity.class);
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

