package com.nsl.app.feedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nsl.app.R;
import com.nsl.app.commonutils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Feedbackall extends AppCompatActivity {
    TextView tv_fbk, tv_name, tv_place, tv_contact, tv_crop, tv_hybrid, tv_sowing_date, tv_image;
    ImageView img;
    String imgfile;
    ImageView status_icon, ivImage, ivImage1, ivImage2, ivImage4, ivImage5, ivImage6;
    FrameLayout frame, frame1, frame2, frame4, frame5, frame6;
    ImageView[] imageViewsAll;
    private FrameLayout[] frameViewsAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbackdetails);
        tv_fbk = (TextView) findViewById(R.id.textViewfbkfull);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_place = (TextView) findViewById(R.id.tv_place);
        tv_contact = (TextView) findViewById(R.id.tv_contact);
        tv_crop = (TextView) findViewById(R.id.tv_crop);
        tv_hybrid = (TextView) findViewById(R.id.tv_hybrid);
        tv_sowing_date = (TextView) findViewById(R.id.tv_sowing_date);
        tv_image = (TextView) findViewById(R.id.tv_image);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage1 = (ImageView) findViewById(R.id.ivImage1);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        ivImage4 = (ImageView) findViewById(R.id.ivImage4);
        ivImage5 = (ImageView) findViewById(R.id.ivImage5);
        ivImage6 = (ImageView) findViewById(R.id.ivImage6);

        frame = (FrameLayout) findViewById(R.id.frame);
        frame1 = (FrameLayout) findViewById(R.id.frame1);
        frame2 = (FrameLayout) findViewById(R.id.frame2);
        frame4 = (FrameLayout) findViewById(R.id.frame4);
        frame5 = (FrameLayout) findViewById(R.id.frame5);
        frame6 = (FrameLayout) findViewById(R.id.frame6);


        imageViewsAll = new ImageView[]{ivImage, ivImage1, ivImage2, ivImage4, ivImage5, ivImage6};
        frameViewsAll = new FrameLayout[]{frame, frame1, frame2, frame4, frame5, frame6};


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), FeedbackallActivity.class);
                startActivity(home);
                finish();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newfedback = new Intent(getApplicationContext(), MainFeedbackActivity.class);
                startActivity(newfedback);
                finish();

            }
        });
        tv_fbk.setText(getIntent().getStringExtra("feedback"));
        tv_name.setText(getIntent().getStringExtra("name"));
        tv_place.setText(getIntent().getStringExtra("place"));
        tv_contact.setText(getIntent().getStringExtra("contact_no"));
        tv_crop.setText(getIntent().getStringExtra("crop"));
        tv_hybrid.setText(getIntent().getStringExtra("hybrid"));
        String date = getIntent().getStringExtra("sowing_date");
        date = date.substring(0, date.length() - 8);
        tv_sowing_date.setText(Common.setDateFormateOnTxt(date));
        //tv_image.setText(getIntent().getStringExtra("image"));
       // tv_image.setVisibility(View.GONE);
       // imgfile = (getIntent().getStringExtra("image"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                new DisplayImages().execute();
            }
        }).start();





/*
       // String[] allImages = imgfile.split("::");

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
       /* if(imgfile.contains("uploads"))
        {
            Log.e("path",imgfile);
            Glide.with(getApplicationContext())
                    .load(imgfile)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(img);

        }
        else
        {
            Log.e("base 64",imgfile);
            byte[] bytarray = Base64.decode(imgfile, Base64.DEFAULT);

            Glide.with(getApplicationContext())
                    .load(bytarray)
                    .asBitmap()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(img);
            // Glide.with(CaptchaFragment.this).load(decodedBytes).crossFade().fitCenter().into(mCatpchaImageView);


        }*/


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
                        JSONArray jsonArray =  new JSONArray();
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


