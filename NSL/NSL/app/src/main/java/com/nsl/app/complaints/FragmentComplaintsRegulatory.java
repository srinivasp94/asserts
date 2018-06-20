package com.nsl.app.complaints;


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
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.TextUtils;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsl.app.CapitalizeFirstLetter;
import com.nsl.app.Companies;
import com.nsl.app.Constants;
import com.nsl.app.Crops;
import com.nsl.app.Customers;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.Divisions;
import com.nsl.app.Products;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okio.Buffer;

import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_CROP_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANY_DIVISION_CROPS;
import static com.nsl.app.DatabaseHandler.TABLE_COMPLAINT;
import static com.nsl.app.DatabaseHandler.TABLE_CROPS;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.app.DatabaseHandler.TABLE_PRODUCTS;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentComplaintsRegulatory extends Fragment {
    public static final String mypreference = "mypref";
    private static final String HOME_URL = "http://www.mehndiqueens.com/indianbeauty/api/user/images";
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    //  private SQLiteDatabase sqldb;
    private static SQLiteDatabase sql, sdbr;
    Spinner spin_company, spin_division, spin_crop, spin_product, spin_regulatory, spin_distributor;
    String sel_comp_id, sel_division_id, sel_crop_id, sel_product_id, sel_distributor_id;
    ProgressDialog progressDialog;
    ViewPager mViewPager;
    String sqliteid, ffmid;
    DatabaseHandler db;
    String jsonData;
    ArrayList<String> adapter_companies, adapter_divisions, adapter_crops, adapter_products, adapter_distributors;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    LinearLayout ll_regulatory, ll_products;
    Spinner spin_complaint_type;
    String checkuid;
    EditText phone, mkt_lot_num, place, date, retailer, distributor, ofcer_name, comments;
    TextView tvInvisibleError1, tvInvisibleError2, tvInvisibleError3, tvInvisibleError4, tvInvisibleError5, tvInvisibleError6;
    Button submit;
    FrameLayout fm;
    //private SliderLayout imageSlider;
    SharedPreferences sharedpreferences;
    CapitalizeFirstLetter capital = new CapitalizeFirstLetter();
    String regexStr;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnremove, cancel;
    private ImageView ivImage, ivImage1, ivImage2, ivImage4, ivImage5, ivImage6;
    private String userChoosenTask, result = "NO IMAGE";
    private ArrayList<SelectedCities> arlist_companies, arlist_divisions, arlist_crops, arlist_products, arlist_distributor;
    private String[] stringqone = {"Select Regulatory Type", "Sample Drawn", "Legal Metorology"};
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private int imageViewId;
    private String base64String1;
    private String base64String2;
    private String base64String3;
    private String base64String4;
    private String base64String5;
    private String base64String6;
    private JSONObject jsonObject1=null;
    private String samp_date;

    public FragmentComplaintsRegulatory() {
    }

    public FragmentComplaintsRegulatory(DatabaseHandler db) {
        this.db = db;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(getActivity());
        View view = inflater.inflate(R.layout.fragment_complaintsregulatory, container, false);
        // db.deleteComplaints();
        // btnSelect = (Button)view.findViewById(R.id.btnuploadRegulatory);
        cancel = (Button) view.findViewById(R.id.btncancel);
        btnremove = (Button) view.findViewById(R.id.btndelete);

        fm = (FrameLayout) view.findViewById(R.id.frame);
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
                fm.setVisibility(View.GONE);
                btnremove.setVisibility(View.GONE);
                result = "NO IMAGE";
            }
        });


        sel_comp_id = "0";
        spin_division = (Spinner) view.findViewById(R.id.spinDivision);
        spin_division.setVisibility(View.INVISIBLE);
        spin_company = (Spinner) view.findViewById(R.id.spinCompany);
        spin_company.setFocusable(true);
        spin_company.setFocusableInTouchMode(true);
        spin_crop = (Spinner) view.findViewById(R.id.spinCrop);
        spin_product = (Spinner) view.findViewById(R.id.spinProduct);
        spin_regulatory = (Spinner) view.findViewById(R.id.spinRegulatoryComplaint);
        phone = (EditText) view.findViewById(R.id.etSamplingOfficerContact);
        mkt_lot_num = (EditText) view.findViewById(R.id.etMarketingLotNumber);
        place = (EditText) view.findViewById(R.id.etPlaceOfSampling);
        date = (EditText) view.findViewById(R.id.etDateOfSampling);
        retailer = (EditText) view.findViewById(R.id.etRetailerName);
        spin_distributor = (Spinner) view.findViewById(R.id.spinDistributor);
        ofcer_name = (EditText) view.findViewById(R.id.etSamplingOfficerName);
        comments = (EditText) view.findViewById(R.id.etComments);
        submit = (Button) view.findViewById(R.id.btnSubmit);
        tvInvisibleError1 = (TextView) view.findViewById(R.id.tvInvisibleError1);
        tvInvisibleError2 = (TextView) view.findViewById(R.id.tvInvisibleError2);
        tvInvisibleError3 = (TextView) view.findViewById(R.id.tvInvisibleError3);
        tvInvisibleError4 = (TextView) view.findViewById(R.id.tvInvisibleError4);
        tvInvisibleError5 = (TextView) view.findViewById(R.id.tvInvisibleError5);
        tvInvisibleError6 = (TextView) view.findViewById(R.id.tvInvisibleError6);


        date = (EditText) view.findViewById(R.id.etDateOfSampling);
        date.setInputType(InputType.TYPE_NULL);

        ArrayAdapter NoCoreAdapter = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, R.id.customSpinnerItemTextView, stringqone);
        spin_regulatory.setAdapter(NoCoreAdapter);
        date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int selmon = selectedmonth + 1;
                         samp_date = selectedyear + "-" + selmon + "-" + selectedday + " 00:00:00";
                         date.setText(Common.setDateFormateOnTxt(selectedyear + "-" + selmon + "-" + selectedday ));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");

        new AsyncCompaniesoffline().execute();
        mkt_lot_num.addTextChangedListener(capital.capitalise(mkt_lot_num));
        place.addTextChangedListener(capital.capitalise(place));
        retailer.addTextChangedListener(capital.capitalise(retailer));
        ofcer_name.addTextChangedListener(capital.capitalise(ofcer_name));
        comments.addTextChangedListener(capital.capitalise(comments));
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewall = new Intent(getActivity(), ComplaintsregallActivity.class);
                startActivity(viewall);
            }
        });
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                    validate();

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
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
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

        byte[] byteArray = bytes.toByteArray();
        result = Base64.encodeToString(byteArray, Base64.DEFAULT);

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
        // fm.setVisibility(View.VISIBLE);

        btnremove.setVisibility(View.GONE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                //
                //   bm = (Bitmap) data.getExtras().get("data");
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                result = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // Toast.makeText(getActivity(),"base64 successfull "+ result,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
        String fid = "1";
        if (spin_company.getSelectedItem().toString().trim().equalsIgnoreCase("Select Company")) {
            tvInvisibleError1.requestFocus();
            tvInvisibleError1.setError("Please select company");
            return;

        }
        if (spin_division.getSelectedItem().toString().trim().equalsIgnoreCase("Select Division")) {
            tvInvisibleError2.requestFocus();
            tvInvisibleError2.setError("Please select division");
            return;
        }
        if (spin_crop.getSelectedItem().toString().trim().equalsIgnoreCase("Select Crop")) {
            tvInvisibleError3.requestFocus();
            tvInvisibleError3.setError("Please select crop");
            return;
        }
        if (spin_product.getSelectedItem().toString().trim().equalsIgnoreCase("Select Product Name") || spin_product.getSelectedItem() == " ") {
            tvInvisibleError4.requestFocus();
            tvInvisibleError4.setError("Please select product");
            return;
        }

        String mkt_lot = mkt_lot_num.getText().toString();
        if (TextUtils.isEmpty(mkt_lot) || mkt_lot.length() > 0 && mkt_lot.startsWith(" ")) {
            mkt_lot_num.requestFocus();
            mkt_lot_num.setError("Please enter marketing lot number");
            return;
        }

        if (spin_regulatory.getSelectedItem().toString().trim().equalsIgnoreCase("Select Regulatory")) {
            tvInvisibleError5.requestFocus();
            tvInvisibleError5.setError("Please select regulatory type");
            return;
        }

        if (TextUtils.isEmpty(date.getText().toString()) || date.getText().toString().length() > 0 && date.getText().toString().startsWith(" ")) {
            date.requestFocus();
            date.setError("Please enter date");

            return;
        }

        String plc = place.getText().toString();
        if (TextUtils.isEmpty(plc) || plc.length() > 0 && plc.startsWith(" ")) {
            place.requestFocus();
            place.setError("Please enter place");

            return;
        }
        String rtler_name = retailer.getText().toString();
        if (TextUtils.isEmpty(rtler_name) || rtler_name.length() > 0 && rtler_name.startsWith(" ")) {
            retailer.requestFocus();
            retailer.setError("Please enter retailer name");
            return;
        }

        if (spin_distributor.getSelectedItem().toString().trim().equalsIgnoreCase("Select Distributor Name") || spin_distributor.getSelectedItem() == (" ")) {
            tvInvisibleError6.requestFocus();
            tvInvisibleError6.setError("Please select distributor");
            return;
        }
        String ofcer = ofcer_name.getText().toString();
        if (TextUtils.isEmpty(ofcer) || ofcer.length() > 0 && ofcer.startsWith(" ")) {
            ofcer_name.requestFocus();
            ofcer_name.setError("Please enter officer name");
            return;
        }

        String contact = phone.getText().toString();
        if (TextUtils.isEmpty(contact) || contact.length() > 0 && contact.startsWith(" ")) {
            phone.requestFocus();
            phone.setError("Please enter mobile number");
            return;
        }
        if (contact.length() > 0 && contact.length() < 10 || contact.startsWith(" ")) {
            phone.requestFocus();
            phone.setError("Please enter valid mobile number");
            return;
        }

        String comment = comments.getText().toString();
        if (TextUtils.isEmpty(comment) || comment.length() > 0 && comment.startsWith(" ")) {
            comments.requestFocus();
            comments.setError("Please enter comments");
            return;
        }

        //  int cnt = Integer.parseInt(contact);


        else {
            Common.disableClickEvent(submit,handler);
            progressDialog=Common.showProgressDialog(getActivity());
            int comp = Integer.parseInt(sel_comp_id);
            int div = Integer.parseInt(sel_division_id);
            int crop = Integer.parseInt(sel_crop_id);
            int product = Integer.parseInt(sel_product_id);
            String dist = spin_distributor.getSelectedItem().toString();
            String regtype = spin_regulatory.getSelectedItem().toString();

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



            }

          //  String allImagesBase64 = stringBuilder.toString();
            Log.e("imges",jsonArray.toString());

            int fdid = Integer.parseInt(fid);
            String nothing = null;
            int nothingint = 0;
            double nothingdouble = 0;

            String samp_date1 = date.getText().toString();
            //   db.addComplaint(new Complaints(comp,div,crop,1,mkt_lot,regtype,nothing,nothing,nothing,nothing,nothing,nothing,nothing,dt,nothing,rtler_name,nothingint,nothing,nothing,result,nothing,dt,plc,ofcer,contact,cmts,nothingint,nothing,nothing));
            // db.addComplaint(new Complaints(comp,div,crop,1,mkt_lot,nothing,nothing,nothing,nothing,nothing,nothing,nothing,nothing,nothing,nothing,rtler_name,nothingint,nothing,nothing,result,regtype,samp_date,plc,ofcer,contact,comment,nothingint,nothing,nothing));
            db.addComplaint(new Complaints(fdid, Integer.parseInt(checkuid), comp, div, Integer.parseInt(sel_crop_id), product, mkt_lot, "Regulatory", nothing, nothing, nothing, nothing, nothing, nothing, nothing, nothing, nothing, rtler_name, Integer.parseInt(sel_distributor_id), nothing, nothing, jsonArray.toString(), regtype, samp_date, plc, ofcer, contact, comment, nothingint, nothing, nothing, nothing, null));
            // Log.e("farmer name ", farmer + ":" + plc + ":" + mobileno);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("sr", String.valueOf(fdid));
            map.put("user_id", checkuid);
            //  map.put("name", "");
            //  map.put("area", fb.get_complaint_area_acres());
            map.put("type", "Regulatory");
            // map.put("billno", fb.get_bill_number());
            map.put("retailer", rtler_name);
            map.put("comments", comment);
            map.put("image", result);
            //  map.put("status", String.valueOf(fb.get_status()));
            //    map.put("complaint_type",fb.get_complaint_type());
            // String comp_type = fb.get_complaint_type();
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
        Log.e("Reading: ", "Reading all Complaints..");

        List<Complaints> complaints = db.getAllComplaintsregulatory(checkuid);
        Log.e("list size", String.valueOf(complaints.size()));

        if (complaints.size() == 0) {
            Log.e("no data found", "no data");
        } else {
            for (Complaints cm : complaints) {
                String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                        cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                        + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                        + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                        + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() + ",remarks" + cm.get_remarks() +
                        ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                Log.e("complaints: ", log);

                String imageStr1="";
                String imageStr2="";
                String imageStr3="";
                String imageStr4="";
                String imageStr5="";
                String imageStr6="";
                try {
                    JSONArray jsonArray=new JSONArray(cm.get_image());

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


                new Async_InsertComplaintsreg().execute(String.valueOf(cm.getID()),String.valueOf(checkuid),String.valueOf(cm.getCompanyId()),String.valueOf(cm.get_division_id()),sel_crop_id,String.valueOf(cm.getProductid()),cm.get_marketing_lot_number(),cm.get_regulatory_type(),cm.get_sampling_date(),
                        cm.get_place_sampling(),cm.get_retailer_name(),String.valueOf(cm.get_distributor()),cm.get_sampling_officer_name(),cm.get_sampling_officer_contact(),
                        cm.get_comments(),imageStr1,imageStr2,imageStr3,imageStr4,imageStr5,imageStr6);

            }

        }


    }


    public class AsyncCompaniesoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_companies = new ArrayList<SelectedCities>();
            adapter_companies = new ArrayList<String>();
            arlist_companies.clear();

            SelectedCities companies = new SelectedCities();
            companies.setCityId("1");
            companies.setCityName("Select Company");
            //    System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_companies.add(companies);
            adapter_companies.add("Select Company");


            try {

                List<Companies> compList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + "," + KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR." + KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")";

                System.out.println(selectQuery);
                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Companies company = new Companies();

                        companies.setCityId(cursor.getString(0));

                        companies.setCityName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_companies.add(cities2);
                        adapter_companies.add(cursor.getString(1));
                        System.out.println(cursor.getString(1));


                    } while (cursor.moveToNext());
                }

                  /*for (int l=0; l<=cropids.size();l++){
                      sdbr =db.getReadableDatabase();

                      Cursor cursors = sdbr.query(TABLE_CROPS, new String[] { KEY_TABLE_CROPS_CROP_ID,
                                      KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME }, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                              new String[] { String.valueOf(cropids.get(l).get("crop_id")) }, null, null, null, null);
                      if (cursors != null)
                          cursors.moveToFirst();

                      Crops crops = new Crops(Integer.parseInt(cursors.getString(0)),cursors.getString(1), cursors.getString(2), cursors.getString(3), cursors.getString(4), cursors.getString(5), cursors.getString(6),cursors.getString(7));
                      // return contact
                      Log.v("Cropidis xyz",cursors.getString(1)+cursors.getString(2));
                  }*/


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            spin_company.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_companies));
            // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.activity_list_item,android.R.id.text1,adapter_companies);
            //   dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //  spin_company.setAdapter(dataAdapter);
            spin_company.setSelection(adapter_companies.indexOf("Nuziveedu Seeds Ltd"));
            spin_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_comp_id = arlist_companies.get(position).getCityId();
                    // sel_comp_id = arlist_companies.get(position).toString();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_comp_id.equalsIgnoreCase("0")) {
                        sel_comp_id = "0";

                    } else {
                        spin_division.setVisibility(View.VISIBLE);
                        new AsyncDivisionsoffline().execute();
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    sel_comp_id = "0";
                }
            });
        }
    }

    public class AsyncDivisionsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_divisions = new ArrayList<SelectedCities>();
            adapter_divisions = new ArrayList<String>();
            arlist_divisions.clear();

            SelectedCities divisions = new SelectedCities();
            divisions.setCityId("0");
            divisions.setCityName("Select Division");
            //    System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_divisions.add(divisions);
            adapter_divisions.add("Select Division");


            try {

                List<Divisions> divList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                //String selectQuery = "SELECT  " +"CR."+ KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid+" group by(CR."+ KEY_TABLE_COMPANIES_MASTER_ID+")";
                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + checkuid + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                System.out.println(selectQuery);

                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Divisions Division = new Divisions();

                        divisions.setCityId(cursor.getString(0));

                        divisions.setCityName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_divisions.add(cities2);
                        adapter_divisions.add(cursor.getString(1));
                        System.out.println(cursor.getString(1));


                    } while (cursor.moveToNext());
                }

                  /*for (int l=0; l<=cropids.size();l++){
                      sdbr =db.getReadableDatabase();

                      Cursor cursors = sdbr.query(TABLE_CROPS, new String[] { KEY_TABLE_CROPS_CROP_ID,
                                      KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME }, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                              new String[] { String.valueOf(cropids.get(l).get("crop_id")) }, null, null, null, null);
                      if (cursors != null)
                          cursors.moveToFirst();

                      Crops crops = new Crops(Integer.parseInt(cursors.getString(0)),cursors.getString(1), cursors.getString(2), cursors.getString(3), cursors.getString(4), cursors.getString(5), cursors.getString(6),cursors.getString(7));
                      // return contact
                      Log.v("Cropidis xyz",cursors.getString(1)+cursors.getString(2));
                  }*/


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // adapter.updateResults(arrayList);
            spin_division.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_divisions));
            // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.activity_list_item,android.R.id.text1,adapter_companies);
            //   dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //  spin_company.setAdapter(dataAdapter);
            spin_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_division_id = arlist_divisions.get(position).getCityId();
                    // sel_comp_id = arlist_companies.get(position).toString();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //  Toast.makeText(getActivity(), "Companyid" + sel_comp_id + "divisionid" + sel_division_id, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_division_id.equalsIgnoreCase("0")) {

                    } else {
                        new AsyncCropsoffline().execute();
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                //String selectQuery = "SELECT  " +"CR."+ KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid+" group by(CR."+ KEY_TABLE_COMPANIES_MASTER_ID+")";
                String selectQuery = "SELECT  CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + "," + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC." + KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id + " group by(CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + ")";                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
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
                    //     Toast.makeText(getActivity(), "Divisionid" + sel_division_id + "cropid" + sel_crop_id, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_crop_id.equalsIgnoreCase("0")) {

                    } else {
                        new AsyncProductsoffline().execute();
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    public class AsyncProductsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_products = new ArrayList<SelectedCities>();
            adapter_products = new ArrayList<String>();
            arlist_products.clear();

            SelectedCities products = new SelectedCities();
            products.setCityId("0");
            products.setCityName("Select Product Name");
            //    System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_products.add(products);
            adapter_products.add("Select Product Name");


            try {

                List<Products> prodList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                //String selectQuery = "SELECT  " +"CR."+ KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid+" group by(CR."+ KEY_TABLE_COMPANIES_MASTER_ID+")";
                String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_NAME + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + sel_division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_crop_id;
                System.out.println(selectQuery);

                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Products Product = new Products();

                        products.setCityId(cursor.getString(0));

                        products.setCityName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_products.add(cities2);
                        adapter_products.add(cursor.getString(1));
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

            spin_product.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_products));

            spin_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_product_id = arlist_products.get(position).getCityId();

                    if (sel_product_id.equalsIgnoreCase("0")) {

                    } else {
                        new AsyncDistributorsoffline().execute();
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    public class AsyncDistributorsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_distributor = new ArrayList<SelectedCities>();
            adapter_distributors = new ArrayList<String>();
            arlist_distributor.clear();

            SelectedCities distributor = new SelectedCities();
            distributor.setCityId("0");
            distributor.setCityName("Select Distributor");
            //    System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_distributor.add(distributor);
            adapter_distributors.add("Select Distributor");


            try {

                List<Customers> customersList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                //String selectQuery = "SELECT  " +"CR."+ KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid+" group by(CR."+ KEY_TABLE_COMPANIES_MASTER_ID+")";
                // String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_NAME + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + sel_division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_crop_id;
                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid + " group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
                System.out.println(selectQuery);

                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {

                        SelectedCities cities2 = new SelectedCities();

                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        String cityId = cities2.getCityId();
                        String cityName = cities2.getCityName();
                        System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_distributor.add(cities2);
                        adapter_distributors.add(cursor.getString(1));
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

            spin_distributor.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_distributors));

            spin_distributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_distributor_id = arlist_distributor.get(position).getCityId();

                    if (sel_distributor_id.equalsIgnoreCase("0")) {

                    } else {

                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    public class Async_InsertComplaintsreg extends AsyncTask<String, String, String> {
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
                        .add("id", params[0])
                        .add("user_id", params[1])
                        .add("company_id", params[2])
                        .add("division_id", params[3])
                        .add("complaint_type", "regulatory")
                        .add("crop_id", params[4])
                        .add("product_id", params[5])
                        .add("marketing_lot_number", params[6])
                        .add("regulatory_type", params[7])
                        .add("sampling_date", params[8])
                        .add("place_sampling", params[9])
                        .add("retailer_name", params[10])
                        .add("distributor", params[11])
                        .add("sampling_officer_name", params[12])
                        .add("sampling_officer_contact", params[13])
                        .add("comments", params[14])
                        .add("image_1", params[15])
                        .add("image_2", params[16])
                        .add("image_3", params[17])
                        .add("image_4", params[18])
                        .add("image_5", params[19])
                        .add("image_6", params[20])

                        .build();
/*

                MediaType mediaType = MediaType.parse("application/octet-stream");
if (jsonObject1==null){

    return null;
}
                RequestBody formBody = RequestBody.create(mediaType, jsonObject1.toString());
*/

                Response responses = null;
            //    System.out.println(bodyToString(formBody));
             //   System.out.println(new Gson().toJson(jsonObject1));
               /* System.out.println("---- Complaints Regulatory data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7] + params[8]
                        + params[9] + params[10] + params[11] + params[12] + params[13] + params[14] + params[15]);
*/
                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_COMPLAINTS)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertComplaintsregulatory" + jsonData);

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

            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        sqliteid = jsonobject.getString("sqlite");
                        ffmid = jsonobject.getString("ffm_id");
                        String image_url = jsonobject.getString("image_url");

                        //          Toast.makeText(getActivity(), "sqlite id and ffmid " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", sqliteid);
                        Log.e("ffmid", ffmid);
                        sql = db.getWritableDatabase();
                        // updatecomplaints
                        String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
                        sql.execSQL(updatequery);
                        System.out.println(updatequery);
                        List<Complaints> complaints = db.getAllComplaints(checkuid);

                        for (Complaints cm : complaints) {
                            String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                                    cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                                    + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                                    + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                                    + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                                    ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                            Log.e("complaintsafterupdate: ", log);


                        }

                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        alert.setTitle("Success");
                        alert.setMessage("Complaints Regulatory inserted sucessfully");
                        alert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        getActivity().setResult(Activity.RESULT_OK);
                                        getActivity().finish();

                                        dialog.dismiss();
                                    }
                                });
                        alert.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

        }
    }

    private static String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }
}