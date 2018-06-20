package com.nsl.app;


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
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.nsl.app.DatabaseHandler.KEY_REGION_NAME;
import static com.nsl.app.DatabaseHandler.KEY_REGION__MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_SAP_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_DESIGNATION;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_EMAIL;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_HEADQUARTER;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_IMAGE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MOBILE_NO;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_REGION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_SAP_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.app.DatabaseHandler.TABLE_REGION;
import static com.nsl.app.DatabaseHandler.TABLE_USERS;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment implements View.OnClickListener{
    private static final int PICK_IMAGE_ID = 234;
    String User_id,regionid,headquarter,image_string;
    TextView tv_firstname,tv_designation,tv_email,tv_mobile,tv_emp_id,tv_headquarter,tv_workingfor;
   //private SliderLayout imageSlider;
    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private ProgressDialog progressDialog;
    MarshMallowPermission marshMallowPermission;

    private ArrayList<SelectedCities> organisations;
    ArrayList<String> adaptercity;

    StringBuilder builder = new StringBuilder();
    private RoundedImageView profile_image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marshMallowPermission =  new MarshMallowPermission(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        User_id           = sharedpreferences.getString("userId", "");
        db                = new DatabaseHandler(getActivity());

        profile_image     =  (RoundedImageView) view.findViewById(R.id.profile_image);
        tv_firstname      = (TextView)view.findViewById(R.id.tv_firstname);
        tv_designation    = (TextView)view.findViewById(R.id.tv_designation);
        tv_emp_id         = (TextView) view.findViewById(R.id.tv_emp_id);
        tv_mobile         = (TextView) view.findViewById(R.id.tv_mobileno);
        tv_email          = (TextView)view.findViewById(R.id.tv_email);
        tv_headquarter    = (TextView)view.findViewById(R.id.tv_headquarter);
        tv_workingfor     = (TextView)view.findViewById(R.id.tv_workingfor);

        profile_image.setOnClickListener(this);
        String selectQuery = "SELECT "+ KEY_TABLE_USERS_FIRST_NAME + ","+ KEY_TABLE_USERS_SAP_ID +","+ KEY_TABLE_USERS_MOBILE_NO + ","+KEY_TABLE_USERS_EMAIL +","+KEY_TABLE_USERS_DESIGNATION +","+KEY_TABLE_USERS_HEADQUARTER+","+KEY_TABLE_USERS_REGION_ID+","+KEY_TABLE_USERS_IMAGE + " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + User_id;
        //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

        sdbw = db.getWritableDatabase();

        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Users users = new Users();

                Log.e("-----","fname : "+cursor.getString(0)+"lname : "+cursor.getString(1)+"mobile : "+cursor.getString(2)+"email : "+cursor.getString(3));
                tv_firstname.setText(cursor.getString(0));
                tv_emp_id.setText(cursor.getString(1));
                tv_email.setText(cursor.getString(3));
                tv_mobile.setText(cursor.getString(2));
                tv_designation.setText(cursor.getString(4));
                tv_headquarter.setText(cursor.getString(5));
                headquarter =cursor.getString(5);
                regionid = cursor.getString(6);
                image_string = cursor.getString(7);

            } while (cursor.moveToNext());
        }

        String selectQuerys = "SELECT  " + KEY_REGION_NAME  +" FROM " + TABLE_REGION + " WHERE " + KEY_REGION__MASTER_ID + " = " + regionid;


        Cursor cc = sdbw.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cc != null && cc.moveToFirst()) {

            tv_headquarter.setText(" "+ headquarter+","+cc.getString(0));
            //The 0 is the column index, we only have 1 column, so the index is 0

        }
        if(image_string!=null){
            Bitmap bitmap = stringToBitMap(image_string);
            if(bitmap!=null){
                profile_image.setImageBitmap(bitmap);
            }
        }
        new AsyncGetofflineCompanies().execute();
        return view;

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.profile_image){
            selectImage();
        }
    }

    private void selectImage() {
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        } else if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
        } else{
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
            startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
        }
    }


    public class AsyncGetofflineCompanies extends AsyncTask<String, String, String> {
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

            organisations = new ArrayList<SelectedCities>();
            adaptercity = new ArrayList<String>();
            organisations.clear();

            try {

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + "," + KEY_TABLE_COMPANIES_NAME + "," + KEY_TABLE_COMPANIES_COMPANY_CODE + "," + KEY_TABLE_COMPANIES_COMPANY_SAP_ID + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = CR." + KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + User_id + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")";
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Companies companies = new Companies();

                        companies.setCompanyMasterId(cursor.getString(0));
                        companies.setCompanyName(cursor.getString(1));
                        companies.setCompanycode(cursor.getString(3));
                        companies.setCompanysapid(cursor.getString(2));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));

                        builder.append(cursor.getString(2));
                        if (!cursor.isLast())
                            builder.append(", ");
                        String result = builder.toString();

                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);


                    } while (cursor.moveToNext());
                }

                // do some stuff....
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
            // adapter.updateResults(arrayList);
            tv_workingfor.setText(""+ builder.toString());

        }
    }

    public void handleActivityResult(Bitmap profile_photo){
        if(profile_photo != null) {
            profile_image.setImageBitmap(profile_photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (profile_photo != null) {
                profile_photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                String photo = Base64.encodeToString(data, Base64.DEFAULT);
                db.updateusers(User_id,photo);
            }
        }
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}
