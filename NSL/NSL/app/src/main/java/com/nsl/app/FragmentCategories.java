package com.nsl.app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nsl.app.advancebooking.AdvanceBookingMainActivity;
import com.nsl.app.commonutils.Common;
import com.nsl.app.commonutils.DatabaseUtil;
import com.nsl.app.complaints.Complaintsselectactivity;
import com.nsl.app.distributors.DIstributorsListActivity;
import com.nsl.app.feedback.FeedbackallActivity;
import com.nsl.app.fieldestimation.FieldEstimationMainAcivity;
import com.nsl.app.marketintelligence.MarketIntelligenceAcivity;
import com.nsl.app.orderindent.OrderIndentMainActivity;
import com.nsl.app.products.Activity_Webview;
import com.nsl.app.stockmovement.NewStockMovementChooseActivity;
import com.nsl.app.stockreturns.NewStockReturnsChooseActivity;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_DESIGNATION;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_EMAIL;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_HEADQUARTER;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_IMAGE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MOBILE_NO;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_REGION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_SAP_ID;
import static com.nsl.app.DatabaseHandler.TABLE_USERS;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCategories extends Fragment implements View.OnClickListener {
    // JSON parser class

    private GridView gridView;

    String jsonData, userId;
    private static final int PICK_IMAGE_ID = 234;
    String regionid, headquarter, image_string,updated_img_string;
    TextView tv_firstname, tv_designation, tv_email, tv_mobile, tv_emp_id, tv_headquarter, tv_workingfor;
    //private SliderLayout imageSlider;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    MarshMallowPermission marshMallowPermission;


    StringBuilder builder = new StringBuilder();
    private RoundedImageView profile_image;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String[] Name = {"Planner", "Distributors", "Products", "Schemes", "Advance Booking", "Order Indent", "Payment Collections",
            "Market Intelligence", "Stock Supply", "Stock Returns", "Route Map", "Feedback", "Complaints", "Daily Diary", "Yield Estimation"};
    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flags = new int[]{
            R.mipmap.ic_planner,
            R.mipmap.ic_distributors,
            R.mipmap.ic_products,
            R.mipmap.ic_schemes,
            R.mipmap.ic_advancebooking,
            R.mipmap.ic_orderindent,
            R.mipmap.ic_paymentcollection,
            R.mipmap.ic_marketintelligence,
            R.mipmap.ic_stockmovements,
            R.mipmap.ic_stockmovements,
            R.mipmap.ic_routemap,
            R.mipmap.ic_feedback,
            R.mipmap.ic_complaints,
            R.mipmap.ic_dailydairy,
            R.mipmap.ic_yieldestimation,
    };
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private String team;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marshMallowPermission = new MarshMallowPermission(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.categories, container, false);
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        db = new DatabaseHandler(getActivity());

        profile_image = (RoundedImageView) view.findViewById(R.id.profile_image);
        tv_firstname = (TextView) view.findViewById(R.id.tv_firstname);
        tv_designation = (TextView) view.findViewById(R.id.tv_designation);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        profile_image.setOnClickListener(this);

        if (Common.haveInternet(getActivity())){
            new Async_getallucd().execute();
        }

        String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_SAP_ID + "," + KEY_TABLE_USERS_MOBILE_NO + "," + KEY_TABLE_USERS_EMAIL + "," + KEY_TABLE_USERS_DESIGNATION + "," + KEY_TABLE_USERS_HEADQUARTER + "," + KEY_TABLE_USERS_REGION_ID + "," + KEY_TABLE_USERS_IMAGE + " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + userId;
        //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

        sdbw = db.getWritableDatabase();

        DatabaseUtil.copyDatabaseToExtStg(getActivity());

        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Users users = new Users();

                Log.e("-----", "fname : " + cursor.getString(0) + "lname : " + cursor.getString(1) + "mobile : " + cursor.getString(2) + "email : " + cursor.getString(3));
                tv_firstname.setText(cursor.getString(0));
                tv_designation.setText(cursor.getString(4));
                image_string = cursor.getString(7);

            } while (cursor.moveToNext());
        }
        if (image_string != null) {
            Bitmap bitmap = stringToBitMap(image_string);
            if (bitmap != null) {
                profile_image.setImageBitmap(bitmap);
            }
        }
        gridView = (GridView) view.findViewById(R.id.gridView);

        // Each row in the list stores country name, currency and flag
        final List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < Name.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", Name[i]);
            hm.put("flag", Integer.toString(flags[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"txt", "flag"};

        // Ids of views in listview_layout
        int[] to = {R.id.tv_gridview, R.id.iv_gridView};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.gridview_row, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                if (i == 0) {

                    Intent planner = new Intent(getActivity(), PlanerMainActivity.class);
                    startActivity(planner);
//                    MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                } else if (i == 1) {

                    Intent distributors = new Intent(getActivity(), DIstributorsListActivity.class);
                    startActivity(distributors);
                } else if (i == 2) {

                   /* Intent products = new Intent(getActivity(), NewPoductsChooseActivity.class);
                    startActivity(products);*/
                    Intent customers = new Intent(getActivity(), Activity_Webview.class);
                    /*customers.putExtra("TITLE", favouriteItem.get(i).get("productName"));
                    customers.putExtra("URL",   favouriteItem.get(i).get("productUrl"));*/
                    startActivity(customers);

                } else if (i == 3) {

                    Intent schemes = new Intent(getActivity(), SchemesActivity.class);
                    startActivity(schemes);
                } else if (i == 4) {

                    Intent advancebooking = new Intent(getActivity(), AdvanceBookingMainActivity.class);
                    startActivity(advancebooking);
                } else if (i == 5) {

                    Intent orderindent = new Intent(getActivity(), OrderIndentMainActivity.class);
                    startActivity(orderindent);
                } else if (i == 6) {

                    Intent payment = new Intent(getActivity(), PaymentActivity.class);
                    startActivity(payment);
                    // MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));

                } else if (i == 7) {

                    Intent payment = new Intent(getActivity(), MarketIntelligenceAcivity.class);
                    startActivity(payment);
                    // MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                } else if (i == 8) {

                    // Intent stockmovement = new Intent(getActivity(), StockInformation.class);
//                    Intent stockmovement = new Intent(getActivity(), StockReturnsMainActivity.class);
//                    startActivity(stockmovement);
                    //stockmovement.putExtra("selection", "adv");
                    //startActivity(stockmovement);
                    // MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                    Intent newbooking = new Intent(getActivity(), NewStockMovementChooseActivity.class);
                    newbooking.putExtra("selection", "adv");
                    startActivity(newbooking);

                } else if (i == 9) {

                    Intent newbooking = new Intent(getActivity(), NewStockReturnsChooseActivity.class);
                    newbooking.putExtra("selection", "adv");
                    startActivity(newbooking);
//                    MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                } else if (i == 10) {

                    Intent route = new Intent(getActivity(), PostRouteMapActivityCopy.class);
                    startActivity(route);
//                    MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                } else if (i == 11) {

                    Intent feedback = new Intent(getActivity(), FeedbackallActivity.class);
                    startActivity(feedback);
                } else if (i == 12) {

                    Intent complaints = new Intent(getActivity(), Complaintsselectactivity.class);
                    startActivity(complaints);
                } else if (i == 13) {
                    Intent route = new Intent(getActivity(), MainDailyDiaryActivity.class);
                    startActivity(route);
//                    MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                }
                else if (i == 14) {
                    Intent route = new Intent(getActivity(), FieldEstimationMainAcivity.class);
                    startActivity(route);
//                    MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                }



            }
        });


        return view;
    }


    public void handleActivityResult(Bitmap profile_photo) {
        if (profile_photo != null) {
            profile_image.setImageBitmap(profile_photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (profile_photo != null) {
                profile_photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                String photo = Base64.encodeToString(data, Base64.DEFAULT);
                db.updateusers(userId, photo);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("img", "0");
                editor.commit();
                Log.e("updated on sqlite",sharedpreferences.getString("img", ""));

                String selectQuery = "SELECT  " + db.KEY_TABLE_USERS_IMAGE + " FROM " + db.TABLE_USERS + " WHERE " +db.KEY_TABLE_USERS_MASTER_ID + " = "+userId ;                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;

                System.out.println(selectQuery);
                sdbw = db.getWritableDatabase();
                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        Users users = new Users();

                        Log.e("image ",  cursor.getString(0));

                        updated_img_string = cursor.getString(0);

                    } while (cursor.moveToNext());
                }
                if (Common.haveInternet(getActivity())) {
                    new Async_UpdateProfilePicture().execute(updated_img_string);
                }else {
                    editor = sharedpreferences.edit();
                    editor.putString("img", "0");
                    editor.commit();
                    Log.e("updated  offline",sharedpreferences.getString("img", ""));
                    getActivity().setResult(Activity.RESULT_OK);
                    //getActivity().finish();
                    //   Toast.makeText(getContext(),Common.INTERNET_UNABLEABLE,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profile_image) {
            selectImage();
        }
    }

    private void selectImage() {
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        } else if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
        } else {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
            startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
        }
    }


    private class Async_getallucd extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = Common.showProgressDialog(getActivity());
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_USER_COMPANY_DIVISION + team)
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
                        db.deleteDataByTableName(db.TABLE_USER_COMPANY_DIVISION);

                        JSONArray companyarray = new JSONArray(jsonData);


                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);

                            String user_id = objinfo.getString("user_id");
                            String company_id = objinfo.getString("company_id");
                            String division_id = objinfo.getString("division_id");


                            // Log.d("Insert: ", "Inserting user company division ..");
                            db.addUser_Company_Division(new User_Company_Division(user_id, company_id, division_id));
                            // do some stuff....

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Common.dismissProgressDialog(progressDialog);

        }
    }

    private class Async_UpdateProfilePicture extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
                        .add("profile_pic",params[0])
                        .add("user_id",userId)
                        .build();

                Response responses = null;
                Request request = new Request.Builder()
                        .url(Constants.URL_UPDATING_PROFILEPIC)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();
                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 updateedprofilepic" + jsonData);
                    JSONObject jsonobj = new JSONObject(jsonData);
                    String status = jsonobj.getString("status");
                    if (status.equalsIgnoreCase("SUCCESS")){
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("img", "1");
                        editor.commit();
                        Log.e("updated on server",sharedpreferences.getString("img", ""));
                    }

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


        }

    }

}

