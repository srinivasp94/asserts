package com.nsl.app.complaints;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.MainRegulatoryActivity;
import com.nsl.app.R;
import com.nsl.app.commonutils.Common;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AllregulatoryActivity extends AppCompatActivity {

    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    DatabaseHandler db ;
    SQLiteDatabase sdbw, sdbr;
    int i = 0, sum_osa = 0;
    private static final String URL = "http://www.mehndiqueens.com/indianbeauty/api/mycookingtalent/get";
    private ListView listView, listViewproducts;
    /// private ItemfavitemAdapter adapter,adapter1;
    String customer_id, comp_id;
    String jsonData, userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> favouriteItem1 = new ArrayList<HashMap<String, String>>();
    String color, comments, place, billno, comp_type, retailer, remarks, comp_name, div_name, crop_name, prod_name, dist_name;
    String mkt_lot, reg_type, samp_date, samp_place, samp_officer, samp_officer_contact;
    int status;
    Button all_regulatory, all_products;
    String aging1 = null, aging2 = null, dtls, imgfile;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name, tv_place, tv_comptype, tv_billno, tv_retailer, statustext, tv_remarkstitle, tv_remarks, tv_details;
    TextView tv_compname, tv_divname, tv_cropname, tv_prodname, tv_mktlot, tv_distname, tv_regtype, tv_sampdate, tv_sampplace;
    TextView tv_officer, tv_officercontact, tv_comments;
    ImageView status_icon, ivImage,ivImage1,ivImage2,ivImage4,ivImage5,ivImage6;
    FrameLayout frame,frame1,frame2,frame4,frame5,frame6;
    ImageView [] imageViewsAll;
    private FrameLayout[] frameViewsAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_details);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        db = new DatabaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        remarks = "";

        comp_name = getIntent().getStringExtra("comp_name");
        div_name = getIntent().getStringExtra("div_name");
        crop_name = getIntent().getStringExtra("crop_name");
        prod_name = getIntent().getStringExtra("prod_name");
        dist_name = getIntent().getStringExtra("dist_name");
        mkt_lot = getIntent().getStringExtra("mkt_lot");
        reg_type = getIntent().getStringExtra("reg_type");
        samp_date = getIntent().getStringExtra("samp_date");
        samp_place = getIntent().getStringExtra("samp_place");
        retailer = getIntent().getStringExtra("retailer");
        samp_officer = getIntent().getStringExtra("samp_officer");
        samp_officer_contact = getIntent().getStringExtra("samp_officer_contact");
        comments = getIntent().getStringExtra("comments");
        status = Integer.parseInt(getIntent().getStringExtra("status"));
        remarks = getIntent().getStringExtra("remarks");
        dtls = getIntent().getStringExtra("details");
        imgfile = (getIntent().getStringExtra("image"));

        tv_compname = (TextView) findViewById(R.id.tv_companyname);
        tv_divname = (TextView) findViewById(R.id.tv_divname);
        tv_cropname = (TextView) findViewById(R.id.tv_cropname);
        tv_prodname = (TextView) findViewById(R.id.tv_productname);
        tv_mktlot = (TextView) findViewById(R.id.tv_mkt_lot);
        tv_distname = (TextView) findViewById(R.id.tv_dist_name);
        tv_regtype = (TextView) findViewById(R.id.tv_reg_type);
        tv_sampdate = (TextView) findViewById(R.id.tv_samp_date);
        tv_sampplace = (TextView) findViewById(R.id.tv_samp_place);
        tv_officer = (TextView) findViewById(R.id.tv_officername);
        tv_officercontact = (TextView) findViewById(R.id.tv_contact);
        tv_retailer = (TextView) findViewById(R.id.tv_retailername);
        tv_comments = (TextView) findViewById(R.id.tv_comment);
        status_icon = (ImageView) findViewById(R.id.status);
        statustext = (TextView) findViewById(R.id.statustext);
        tv_remarkstitle = (TextView) findViewById(R.id.tv_remarks);
        tv_remarks = (TextView) findViewById(R.id.remarks);
        tv_details = (TextView) findViewById(R.id.tv_details);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage1 = (ImageView)findViewById(R.id.ivImage1);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        ivImage4 = (ImageView) findViewById(R.id.ivImage4);
        ivImage5 = (ImageView)findViewById(R.id.ivImage5);
        ivImage6 = (ImageView) findViewById(R.id.ivImage6);

        frame = (FrameLayout) findViewById(R.id.frame);
        frame1 = (FrameLayout) findViewById(R.id.frame1);
        frame2 = (FrameLayout) findViewById(R.id.frame2);
        frame4 = (FrameLayout) findViewById(R.id.frame4);
        frame5 = (FrameLayout) findViewById(R.id.frame5);
        frame6 = (FrameLayout) findViewById(R.id.frame6);


        imageViewsAll=new ImageView[]{ivImage,ivImage1,ivImage2,ivImage4,ivImage5,ivImage6};
       frameViewsAll=new FrameLayout []{frame,frame1,frame2,frame4,frame5,frame6};
        // Log.e("image ",imgfile);
        if (samp_date.contains("00:00:00")) {
            samp_date = samp_date.substring(0, samp_date.length() - 8);
        }

        tv_compname.setText(comp_name);
        tv_divname.setText(div_name);
        tv_cropname.setText(crop_name);
        tv_prodname.setText(prod_name);
        tv_mktlot.setText(mkt_lot);
        tv_distname.setText(dist_name);
        tv_regtype.setText(reg_type);
        tv_sampdate.setText(Common.setDateFormateOnTxt(samp_date));
        tv_sampplace.setText(samp_place);
        tv_officer.setText(samp_officer);
        tv_officercontact.setText(samp_officer_contact);
       /* String[] allImages = imgfile.split("::");
        Log.e("no", String.valueOf(frameViewsAll.length));
        for (int j=5;j>allImages.length-1;j--){
            frameViewsAll[j].setVisibility(View.GONE);
        }

        Log.e("size",imgfile+" : " + String.valueOf(allImages.length)+" : "+allImages.toString());
        if(allImages.length == 0 || imgfile.equals("")){
            Log.e("no","imges");
            frameViewsAll[0].setVisibility(View.GONE);
        }
        else  {
            for (int i = 0; i < allImages.length; i++) {

                Picasso.with(this).load(allImages[i]).into(imageViewsAll[i]);


            }
        }*/
/*
        if (imgfile.contains("uploads")) {
            Log.e("path", imgfile);
            Glide.with(getApplicationContext())
                    .load(imgfile)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(ivImage);

        } else {
            Log.e("base 64", imgfile);
           *//* byte[] bytarray = Base64.decode(imgfile, Base64.DEFAULT);

            Glide.with(getApplicationContext())
                    .load(bytarray)
                    .asBitmap()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(ivImage);*//*
            // Glide.with(CaptchaFragment.this).load(decodedBytes).crossFade().fitCenter().into(mCatpchaImageView);


        }*/

                    new DisplayImages().execute();

        if (remarks == null || remarks.isEmpty() || remarks.equalsIgnoreCase("null")) {
            tv_remarkstitle.setVisibility(View.GONE);
            tv_remarks.setVisibility(View.GONE);
        } else {
            tv_remarks.setText(remarks);
        }

        tv_comments.setText(comments);

        tv_retailer.setText(retailer);
        tv_details.setText(dtls);

        if (status == 1) {
            status_icon.setVisibility(View.VISIBLE);
            statustext.setVisibility(View.VISIBLE);
            status_icon.setImageResource(R.drawable.icon4);
            statustext.setText("Closed");
            statustext.setTextColor(Color.parseColor("#32cd32"));
        } else if (status == 2) {
            status_icon.setVisibility(View.VISIBLE);
            statustext.setVisibility(View.VISIBLE);
            status_icon.setImageResource(R.drawable.icon5);
            statustext.setText("Cancel");
            statustext.setTextColor(Color.parseColor("#ff4500"));
        } else if (status == 3) {
            status_icon.setVisibility(View.VISIBLE);
            statustext.setVisibility(View.VISIBLE);
            status_icon.setImageResource(R.drawable.icon6);
            statustext.setText("In Progress");
            statustext.setTextColor(Color.parseColor("#ffff00"));

        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newfedback = new Intent(getApplicationContext(), MainRegulatoryActivity
                        .class);
                startActivity(newfedback);

            }
        });


    }


    public void onBackPressed() {
        super.onBackPressed();
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

    public class DisplayImages extends AsyncTask<String, String, String> {


        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            imgfile = (getIntent().getStringExtra("image"));
            Log.d("imgfile",imgfile);
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (imgfile==null || imgfile.equalsIgnoreCase("") || imgfile.equalsIgnoreCase("null")){

                            for (int j = 0; j <6 ; j++) {

                                frameViewsAll[j].setVisibility(View.GONE);

                            }
                            Common.dismissProgressDialog(progressDialog);
                            return;
                        }


                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray = new JSONArray(imgfile);
                            Log.d("imgfile","lenth "+jsonArray.length()+"\n"+jsonArray.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        for (int j = jsonArray.length(); j <6 ; j++) {


                            frameViewsAll[j].setVisibility(View.GONE);

                        }
                        // Log.e("size",imgfile+" : " + String.valueOf(allImages.length)+" : "+allImages.toString());
                        if (jsonArray.length() == 0 || imgfile.equals("")) {
                            Log.e("no", "imges");
                            frameViewsAll[0].setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String image_64 = jsonObject.getString("image_64");
                                    Log.d("imgfile",image_64);
                                    byte[] bytarray = Base64.decode(image_64, Base64.DEFAULT);

                                    Glide.with(getApplicationContext())
                                            .load(bytarray)
                                            .asBitmap()
                                            .placeholder(R.mipmap.ic_launcher)
                                            .into(imageViewsAll[i]);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }




                            }
                        }

                    }
                });

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* progressDialog = new ProgressDialog(Feedbackall.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
*/

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Common.dismissProgressDialog(progressDialog);
        }
    }
}



