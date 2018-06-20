package com.nsl.app.feedback;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsl.app.CapitalizeFirstLetter;
import com.nsl.app.Constants;
import com.nsl.app.Crops;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;
import com.nsl.app.SelectedCities;
import com.nsl.app.Utility;
import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_FEEDBACK_FEEDBACK_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_FEEDBACK_FFMID;
import static com.nsl.app.DatabaseHandler.TABLE_CROPS;
import static com.nsl.app.DatabaseHandler.TABLE_FEEDBACK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFeedback extends Fragment{
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect,btnremove,cancel;
    private ImageView ivImage, ivImage1, ivImage2, ivImage4, ivImage5, ivImage6;
    private String userChoosenTask,result="NO IMAGE";
    String sqliteid,ffmid;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String HOME_URL = "http://www.mehndiqueens.com/indianbeauty/api/user/images";
    ViewPager mViewPager;
    FrameLayout fm;
    Spinner spin_crop;
    String sel_comp_id, sel_division_id, sel_crop_id, sel_product_id,jsonData;
    DatabaseHandler db;
    private static SQLiteDatabase sql, sdbr,sdbw;
    private ArrayList<SelectedCities> arlist_companies, arlist_divisions, arlist_crops, arlist_products;
    String checkuid;
    EditText mobile, farmer_name, place, feedback, hybrid, sowing_date;
    ArrayList<String> adapter_companies, adapter_divisions, adapter_crops, adapter_products;
    ProgressDialog progressDialog;
    TextView tvInvisibleError,tvListItem;
    //private SliderLayout imageSlider;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    CapitalizeFirstLetter capital = new CapitalizeFirstLetter();

    DateFormat dateFormat;
    Date myDate;
    Button submit;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    private int imageViewId;
    private String base64String1;
    private String base64String2;
    private String base64String3;
    private String base64String4;
    private String base64String5;
    private String base64String6;
    private String sow_date;

    public FragmentFeedback() {

    }
    public FragmentFeedback(DatabaseHandler db) {
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
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
      //  btnSelect = (Button)view.findViewById(R.id.btnupload);
        cancel = (Button)view.findViewById(R.id.btncancel);
        btnremove=(Button)view.findViewById(R.id.btndelete);
       /* btnSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });*/
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        ivImage1 = (ImageView) view.findViewById(R.id.ivImage1);
        ivImage2 = (ImageView) view.findViewById(R.id.ivImage2);
        ivImage4 = (ImageView) view.findViewById(R.id.ivImage4);
        ivImage5 = (ImageView) view.findViewById(R.id.ivImage5);
        ivImage6 = (ImageView) view.findViewById(R.id.ivImage6);

        ivImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage.getId();
            }
        });

        ivImage1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage1.getId();
            }
        });
        ivImage2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imageViewId = ivImage2.getId();
                selectImage();
            }
        });
        ivImage4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imageViewId = ivImage4.getId();
                selectImage();
            }
        });
        ivImage5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imageViewId = ivImage5.getId();
                selectImage();
            }
        });

        ivImage6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imageViewId = ivImage6.getId();
                selectImage();
            }
        });
        btnremove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage.setImageDrawable(null);
                btnremove.setVisibility(View.GONE);
                fm.setVisibility(View.GONE);
                result="NO IMAGE";
            }
        });

        farmer_name = (EditText) view.findViewById(R.id.etFarmerName);
        place = (EditText) view.findViewById(R.id.etPlace);
        mobile = (EditText) view.findViewById(R.id.etMobile);
        //  landarea=(EditText)view.findViewById(R.id.etLandArea);
        //   spin_division = (Spinner) view.findViewById(R.id.spinDivision);
        // spin_company = (Spinner) view.findViewById(R.id.spinCompany);

        tvInvisibleError= (TextView)view.findViewById(R.id.tvInvisibleError);
        spin_crop = (Spinner) view.findViewById(R.id.spinCrop);
        // spin_product = (Spinner) view.findViewById(R.id.spinProduct);
        hybrid = (EditText) view.findViewById(R.id.etHybrid);
        sowing_date = (EditText) view.findViewById(R.id.etSowingDate);
        feedback = (EditText) view.findViewById(R.id.etFeedback);
        submit = (Button) view.findViewById(R.id.btnSubmit);
        fm = (FrameLayout)view.findViewById(R.id.frame);
        farmer_name.requestFocus();
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        Log.e("userid is : ",checkuid);
        new AsyncCropsoffline().execute();

        farmer_name.addTextChangedListener(capital.capitalise(farmer_name));
        place.addTextChangedListener(capital.capitalise(place));
        mobile.addTextChangedListener(capital.capitalise(mobile));
        hybrid.addTextChangedListener(capital.capitalise(hybrid));
        feedback.addTextChangedListener(capital.capitalise(feedback));




        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();

            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewall = new Intent(getActivity(),FeedbackallActivity.class);
                startActivity(viewall);
            }
        });
        sowing_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int selmon = selectedmonth +1;
                         sow_date = selectedyear + "-" + selmon + "-" + selectedday + " 00:00:00";
                        sowing_date.setText(Common.setDateFormateOnTxt(selectedyear + "-" + selmon + "-" + selectedday ));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });



        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        byte[] byteArray = bytes .toByteArray();
        Log.d("byteArray", String.valueOf(byteArray.length));
        result=  Base64.encodeToString(byteArray, Base64.DEFAULT);

        //Toast.makeText(getActivity(),"base64 successfull "+ result,Toast.LENGTH_LONG).show();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        //  String result = Base64.encodeToString(destination, Base64.DEFAULT);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (imageViewId) {
            case R.id.ivImage:
                ivImage.setImageBitmap(thumbnail);
                base64String1 = result;
                break;
            case R.id.ivImage1:
                ivImage1.setImageBitmap(thumbnail);
                base64String2 = result;

                break;
            case R.id.ivImage2:
                ivImage2.setImageBitmap(thumbnail);
                base64String3 = result;

                break;

            case R.id.ivImage4:
                ivImage4.setImageBitmap(thumbnail);
                base64String4 = result;

                break;
            case R.id.ivImage5:
                ivImage5.setImageBitmap(thumbnail);
                base64String5 = result;
                break;
            case R.id.ivImage6:
                ivImage6.setImageBitmap(thumbnail);
                base64String6 = result;
                break;


        }
//        fm.setVisibility(View.VISIBLE);
//        ivImage.setImageBitmap(thumbnail);
        btnremove.setVisibility(View.GONE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
            //
                //   bm = (Bitmap) data.getExtras().get("data");
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes .toByteArray();
                result=  Base64.encodeToString(byteArray, Base64.DEFAULT);
                // Toast.makeText(getActivity(),"base64 successfull "+ result,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        fm.setVisibility(View.VISIBLE);
//        ivImage.setImageBitmap(bm);

        switch (imageViewId) {
            case R.id.ivImage:
                ivImage.setImageBitmap(bm);
                base64String1 = result;
                break;
            case R.id.ivImage1:
                ivImage1.setImageBitmap(bm);
                base64String2 = result;

                break;
            case R.id.ivImage2:
                ivImage2.setImageBitmap(bm);
                base64String3 = result;

                break;

            case R.id.ivImage4:
                ivImage4.setImageBitmap(bm);
                base64String4 = result;

                break;
            case R.id.ivImage5:
                ivImage5.setImageBitmap(bm);
                base64String5 = result;
                break;
            case R.id.ivImage6:
                ivImage6.setImageBitmap(bm);
                base64String6 = result;
                break;


        }
        btnremove.setVisibility(View.GONE);
    }

    private void validate() {
        Handler handler=Common.disableClickEvent(submit,true);
        Log.d("ButtonClicked","ButtonClicked");
        String fid = "1";
        String farmer = farmer_name.getText().toString();
        String mobileno = mobile.getText().toString();
        String plc = place.getText().toString();
        String feedbk = feedback.getText().toString();
        String hyb = hybrid.getText().toString();
        String crop = spin_crop.getSelectedItem().toString();


        if (TextUtils.isEmpty(farmer) || farmer.length() > 0 && farmer.startsWith(" ")) {
            farmer_name.setError("Please enter Farmer name");
            farmer_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(plc) || plc.length() > 0 && plc.startsWith(" ")) {
            place.setError("Please enter place");
            place.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mobileno) || mobileno.length() > 0 && mobileno.startsWith(" ")) {
            mobile.setError("Please enter mobile number");
            mobile.requestFocus();
            return;
        }
        if (mobileno.length() > 0 && mobileno.length() < 10 || mobileno.startsWith(" ")) {
            mobile.setError("Please enter valid mobile number");
            mobile.requestFocus();
            return;
        }
        if (spin_crop.getSelectedItem().toString().trim().equalsIgnoreCase("Select Crop")) {

                // Toast.makeText(getActivity(), "Please select crop", Toast.LENGTH_SHORT).show();
               tvInvisibleError.requestFocus();
                tvInvisibleError.setError("Please select crop");
                return;

        }
        if (TextUtils.isEmpty(sowing_date.getText().toString()) || sowing_date.getText().toString().length() > 0 && sowing_date.getText().toString().startsWith(" ")) {
            sowing_date.setError("Please enter Sowing date");
            sowing_date.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(feedbk) || feedbk.length() > 0 && feedbk.startsWith(" ")) {
            feedback.setError("Please enter Feedback");
            feedback.requestFocus();
            return;
        }

       else {
            Common.disableClickEvent(submit,handler);
            //  Toast.makeText(getActivity(), "Inserting...", Toast.LENGTH_SHORT).show();
            int fdid = Integer.parseInt(fid);
            //String ivImage="xyz.jpg";
progressDialog=Common.showProgressDialog(getActivity());

            ArrayList<String> objects = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            if (base64String1 != null) {
                objects.add(base64String1);
            }
            if (base64String2 != null) {
                objects.add(base64String2);
            }
            if (base64String3 != null) {
                objects.add(base64String3);
            }
            if (base64String4 != null) {
                objects.add(base64String4);
            }
            if (base64String5 != null) {
                objects.add(base64String5);
            }
            if (base64String6 != null) {
                objects.add(base64String6);
            }

            JSONArray jsonArray=new JSONArray();
            for (int i = 0; i < objects.size(); i++) {
                try {
                    jsonArray.put(new JSONObject().put("image_64",objects.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               /* if (i != 0) {
                    stringBuilder.append(",nsl,");
                }
                stringBuilder.append(objects.get(i));*/

            }

            String allImagesBase64 = stringBuilder.toString();
            Log.e("imges",jsonArray.toString());

            db.addFeedback(new Feedback(fdid,Integer.parseInt(checkuid), farmer, plc, mobileno, sel_crop_id, hyb, sow_date, feedbk, jsonArray.toString(), null));
            Log.e("farmer name ", farmer + ":" + plc + ":" + mobileno);
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put("Id", fid);
            map.put("user_id",checkuid);
            map.put("farmer_name", farmer);
            map.put("place", plc);
            map.put("mobile_no", mobileno);
            map.put("crop", crop);
            map.put("hybrid", hyb);
            map.put("sowing_date", sow_date);
            map.put("Feedback", feedbk);
            map.put("image", result);
            map.put("ffmid", null);

            favouriteItem.add(map);
            // do some stuff....

        }
        if (Common.haveInternet(getActivity())) {
            insertToService();
        }else {
            Common.dismissProgressDialog(progressDialog);
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
         //   Toast.makeText(getContext(),Common.INTERNET_UNABLEABLE,Toast.LENGTH_SHORT).show();
        }

    }

    private void insertToService() {
        db = new DatabaseHandler(getActivity());
        Log.d("Reading: ", "Reading all Feedback..");



        List<Feedback> feedback =db.getAllFeedback(checkuid);

        Log.e("list size", String.valueOf(feedback.size()));

        if(feedback.size()==0)
        {
            Log.e("no data found","no data");
        }
        else
        {
            for (Feedback fb : feedback) {
                String log = "Id: "+fb.getID()+",Userid: "+checkuid+" ,Name: " + fb.getFarmerName()+ " ,place: " + fb.getplace()+ " ,mobile: " + fb.getContactNo()+ " ,crop: " + fb.getCrop()+ " ,hybrid: " + fb.getHybrid()+ " ,sowing date : " + fb.getSowingDate()+" ,feedback message:"+fb.getfeedbackmessage()+",image:"+fb.getImage()+",ffmid:"+fb.get_ffmid();

                Log.e("feedback before : ", log);
                // String ffmid = fb.get_ffmid();
                // if(!ffmid.equals(null)){
                String imageStr1="";
                String imageStr2="";
                String imageStr3="";
                String imageStr4="";
                String imageStr5="";
                String imageStr6="";

                try {
                    JSONArray jsonArray=new JSONArray(fb.getImage());

              //  String[] imagesDb = fb.getImage().split(",nsl,");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String image = jsonObject.getString("image_64");

                    if (i==0){
                        imageStr1=image;
                    }
                    if (i==1){
                        imageStr2=image;
                    }
                    if (i==2){
                        imageStr3=image;
                    }
                    if (i==3){
                        imageStr4=image;
                    }
                    if (i==4){
                        imageStr5=image;
                    }
                    if (i==5){
                        imageStr6=image;
                    }

                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Async_InsertFeedback().execute(String.valueOf(fb.getID()),checkuid,fb.getFarmerName(),fb.getplace(),fb.getContactNo(), fb.getCrop(),fb.getHybrid(),fb.getSowingDate(),fb.getfeedbackmessage(),imageStr1,imageStr2,imageStr3,imageStr4,imageStr5,imageStr6);

                //  }

            }

        }



    }




    public class AsyncCropsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_crops = new ArrayList<SelectedCities>();
            adapter_crops = new ArrayList<String>();
            arlist_crops.clear();

            SelectedCities crops = new SelectedCities();
            crops.setCityId("0");
            crops.setCityName("Select Crop");
            //    System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_crops.add(crops);
            adapter_crops.add("Select Crop");


            try {

                List<Crops> cropList = new ArrayList<>();


                String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + "," + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_CROPS + " group by(" + KEY_TABLE_CROPS_CROP_MASTER_ID + ")";                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;

                System.out.println(selectQuery);

                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();

                sql = db.getWritableDatabase();
                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Crops Crop = new Crops();

                        crops.setCityId(cursor.getString(0));

                        crops.setCityName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_crops.add(cities2);
                        adapter_crops.add(cursor.getString(1));
                        System.out.println(cursor.getString(1));


                    } while (cursor.moveToNext());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // adapter.updateResults(arrayList);
            spin_crop.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_crops));
            // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.activity_list_item,android.R.id.text1,adapter_companies);
            //   dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //  spin_company.setAdapter(dataAdapter);
            spin_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_crop_id = arlist_crops.get(position).getCityId();
                    // sel_comp_id = arlist_companies.get(position).toString();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    // Toast.makeText(getActivity(), "Divisionid" + sel_division_id + "cropid" + sel_crop_id, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_crop_id.equalsIgnoreCase("0")) {

                    } else {
                        // new AsyncProductsoffline().execute();
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public class Async_InsertFeedback extends AsyncTask<String, String, String> {
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
                        .add("id",params[0])
                        .add("user_id",params[1])
                        .add("farmer_name", params[2])
                        .add("place", params[3])
                        .add("contact_no", params[4])
                        .add("crop_id", params[5])
                        .add("hybrid",params[6])
                        .add("sowing_date", params[7])
                        .add("feedback_message", params[8])
                        .add("image_1", params[9])
                        .add("image_2", params[10])
                        .add("image_3", params[11])
                        .add("image_4", params[12])
                        .add("image_5", params[13])
                        .add("image_6", params[14])
                        .build();

                Response responses = null;

                System.out.println("---- Feedback data -----" +params[0]+ params[1]+params[2]+params[3]+params[4]+params[5]+params[6]+params[7]+params[8]+params[9]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_FEEDBACK)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertFeedback" + jsonData);

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
           Common.dismissProgressDialog(progressDialog);


            if (jsonData != null)
            {   JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        //  Toast.makeText(getActivity(), "Feed back inserted sucessfully", Toast.LENGTH_SHORT).show();
                        sqliteid = jsonobject.getString("sqlite");
                        ffmid = jsonobject.getString("ffm_id");
                        String image_url = jsonobject.getString("image_url");

                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", sqliteid);
                        Log.e("ffmid", ffmid);
                        sdbw = db.getWritableDatabase();
                        // updateFeedback(Feedback feedback);
                        String updatequery = "UPDATE " + TABLE_FEEDBACK + " SET " + KEY_TABLE_FEEDBACK_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_FEEDBACK_FEEDBACK_ID + " = " + sqliteid;
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);
                        List<Feedback> feedback = db.getAllFeedback(checkuid);

                        for (Feedback fb : feedback) {
                            String log = "Id: " + fb.getID()+",Userid: "+fb.get_user_id() + " ,Name: " + fb.getFarmerName() + " ,place: " + fb.getplace() + " ,mobile: " + fb.getContactNo() + " ,crop: " + fb.getCrop() + " ,hybrid: " + fb.getHybrid() + " ,sowing date : " + fb.getSowingDate() + " ,feedback message:" + fb.getfeedbackmessage() + ",image:" + fb.getImage() + ",ffmidafter:" + fb.get_ffmid();

                            Log.e("feedback: ", log);
                        }
                        getActivity().setResult(Activity.RESULT_OK);
                       getActivity().finish();

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