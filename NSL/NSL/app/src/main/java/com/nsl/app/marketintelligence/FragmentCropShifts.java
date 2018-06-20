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

import static com.nsl.app.DatabaseHandler.KEY_CROP_SHIFTS_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_CROP_SHIFTS_ID;
import static com.nsl.app.DatabaseHandler.TABLE_MI_CROP_SHIFTS;
import static java.lang.String.valueOf;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCropShifts extends Fragment{
    private Button btnSave,cancel;
    String sqliteid,ffmid,jsonData;
    private static final String HOME_URL = "http://www.mehndiqueens.com/indianbeauty/api/user/images";
    ViewPager mViewPager;
    FrameLayout fm;
    DatabaseHandler db;
    private static SQLiteDatabase sql, sdbr,sdbw;
    String checkuid;
    EditText crop_name, cropvariety, previousarea, currentarea,enterpercent,reason,saved,previous,current,next;
    ProgressDialog progressDialog;
    TextView tvInvisibleError;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    public FragmentCropShifts() {

    }
    @SuppressLint("ValidFragment")
    public FragmentCropShifts(DatabaseHandler db) {
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
        View view = inflater.inflate(R.layout.activity_crop_shift, container, false);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        cancel = (Button) view.findViewById(R.id.btn_cancel);

        crop_name = (EditText) view.findViewById(R.id.edt_selectcrop);
        cropvariety = (EditText) view.findViewById(R.id.edt_selectcropvariety);
        previousarea = (EditText) view.findViewById(R.id.edt_enterarea);
        currentarea = (EditText) view.findViewById(R.id.edt_area);
        enterpercent = (EditText) view.findViewById(R.id.edt_enterpercent);
        reason = (EditText) view.findViewById(R.id.edt_reason);
        saved = (EditText) view.findViewById(R.id.edt_savedseeds);
        previous = (EditText) view.findViewById(R.id.edt_previous_year);
        current = (EditText) view.findViewById(R.id.edt_current_year);
        next = (EditText) view.findViewById(R.id.edt_next_year);
      //  tvInvisibleError = (TextView) view.findViewById(R.id.tvInvisibleError);
        fm = (FrameLayout) view.findViewById(R.id.frame);
        crop_name.requestFocus();
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        Log.e("userid is : ", checkuid);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewall = new Intent(getActivity(),CropShiftAllActivity.class);
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
        Handler handler=Common.disableClickEvent(btnSave,true);
        String cpid = "1";
        String cropname = crop_name.getText().toString();
        String varietyortype = cropvariety.getText().toString();
        String previous_area = previousarea.getText().toString();
        String current_area = currentarea.getText().toString();
        String percentage = enterpercent.getText().toString();
        String reasoncrop=reason.getText().toString();
        String saved_seed =saved.getText().toString();
        String previous_seed=previous.getText().toString();
        String current_seed=current.getText().toString();
        String next_seed=next.getText().toString();

        if (TextUtils.isEmpty(cropname) || cropname.length() > 0 && cropname.startsWith(" ")) {
            crop_name.setError("Please enter Crop name");
            crop_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(varietyortype) || varietyortype.length() > 0 && varietyortype.startsWith(" ")) {
            cropvariety.setError("Please enter Crop variety");
            cropvariety.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(previous_area) || previous_area.length() > 0 && previous_area.startsWith(" ")) {
            previousarea.setError("Please enter Previous year area");
            previousarea.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(current_area) || current_area.length() > 0 && current_area.startsWith(" ")) {
            currentarea.setError("Please enter current year area");
            currentarea.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(percentage) || percentage.length() > 0 && percentage.startsWith(" ")) {
            enterpercent.setError("Please enter percentage");
            enterpercent.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(reasoncrop) || reasoncrop.length() > 0 && reasoncrop.startsWith(" ")) {
            reason.setError("Please enter reson for crop shift");
            reason.requestFocus();
            return;
        }
       if (TextUtils.isEmpty(saved_seed) || saved_seed.length() > 0 && saved_seed.startsWith(" ")) {
            reason.setError("Please enter reason saved seed");
            reason.requestFocus();
            return;
        }
       if (TextUtils.isEmpty(previous_seed) || previous_seed.length() > 0 && previous_seed.startsWith(" ")) {
            previous.setError("Please enter previous year seed");
            previous.requestFocus();
            return;
        }
       if (TextUtils.isEmpty(current_seed) || current_seed.length() > 0 && current_seed.startsWith(" ")) {
            current.setError("Please enter current year seed");
            current.requestFocus();
            return;
        }
      if (TextUtils.isEmpty(next_seed) || next_seed.length() > 0 && next_seed.startsWith(" ")) {
                next.setError("Please enter next year seed");
                next.requestFocus();
                return;
        }




        else {
          Common.disableClickEvent(btnSave,handler);
            //  Toast.makeText(getActivity(), "Inserting...", Toast.LENGTH_SHORT).show();
            int fdid = Integer.parseInt(cpid);
            String ffmid = "0";
            //String img="xyz.jpg";
            db.addCropShifts(new Crop_Shifts(valueOf(fdid),cropname, varietyortype, previous_area,current_area, percentage,reasoncrop,checkuid,null,saved_seed,previous_seed,current_seed,next_seed,null));
            Log.e("crop name ", cropname + ":" + varietyortype + ":" + reasoncrop);
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put("Id", valueOf(fdid));
            map.put("user_id",checkuid);
            map.put("cropname", cropname);
            map.put("varietyortype", varietyortype);
            map.put("previousarea", previous_area);
            map.put("currentarea", current_area);
            map.put("percentage", percentage);
            map.put("reasoncrop", reasoncrop);
          //add remaining
            map.put("ffmid", null);

            favouriteItem.add(map);
            // do some stuff....

        }
        try {
           if(Utility.isNetworkAvailable(getActivity(), Constants.isShowNetworkToast)){
               insertToService();
            } else {
               Intent complaints = new Intent(getActivity(),CropShiftAllActivity.class);
               startActivity(complaints);

            }
        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }
    }

    private void insertToService() {
        List<Crop_Shifts> cropshifts = db.getAllnullCrop_Shifts(checkuid);
        Log.e("list size", String.valueOf(cropshifts.size()));
        for(Crop_Shifts cp: cropshifts)
            new Async_InsertCropShifts().execute(String.valueOf(cp.getID()),cp.get_crop_shifts_crop_name(),cp.get_crop_shifts_variety_type(),cp.get_crop_shifts_previous_year_area(),cp.get_crop_shifts_current_year_expected_area(),cp.get_crop_shifts_percentage_increase_decrease(),cp.get_crop_shifts_reason_crop_shift(),cp.get_crop_shifts_created_by(),cp.get_crop_shifs_crop_in_saved_seed(),cp.get_crop_shifs_crop_in_previous_year(),cp.get_crop_shifs_crop_in_current_year(),cp.get_crop_shifs_crop_in_next_year());
        //   Log.e(":",String.valueOf(cp.getID())+" : "+cp.get_crop_shifts_crop_name()+" : "+cp.get_crop_shifts_variety_type()+" : "+cp.get_crop_shifts_previous_year_area()+" : "+cp.get_crop_shifts_current_year_expected_area()+" : "+cp.get_crop_shifts_percentage_increase_decrease()+" : "+cp.get_crop_shifts_reason_crop_shift()+" : "+cp.get_crop_shifts_created_by()+" : "+cp.get_crop_shifs_crop_in_saved_seed()+" : "+cp.get_crop_shifs_crop_in_previous_year()+" : "+cp.get_crop_shifs_crop_in_current_year()+" : "+cp.get_crop_shifs_crop_in_next_year());

    }

    private class Async_InsertCropShifts extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
              progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
                        .add("table",TABLE_MI_CROP_SHIFTS)
                        .add("mobile_id", params[0])
                        // .add("user_id", String.valueOf(favouriteItem.get(1)))
                        .add("crop_name", params[1])
                        .add("variety_type", params[2])
                        .add("previous_year_area", params[3])
                        .add("current_year_expected_area", params[4])
                        .add("percentage_increase_decrease",params[5])
                        .add("reason_crop_shift",params[6])
                        .add("created_by",params[7])
                        .add("crop_in_saved_seed",params[8])
                        .add("previous_year_srr",params[9])
                        .add("current_year_srr",params[10])
                        .add("next_year_srr",params[11])


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
                        String updatequery = "UPDATE " + TABLE_MI_CROP_SHIFTS + " SET " + KEY_CROP_SHIFTS_FFMID + " = " + ffmid + " WHERE " + KEY_CROP_SHIFTS_ID + " = " + sqliteid;
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);
                        Intent complaints = new Intent(getActivity(),CropShiftAllActivity.class);
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
