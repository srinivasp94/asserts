package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.Home_page;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.ProfileResponse;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ProfileActivity extends AppCompatActivity implements RetrofitResponseListener {

    private ImageView ProfileImage;
    private TextView Name, Mobile, mEmail, Membership, Address, free_count;//, State;
    private LinearLayout Edit;
    private String Addr, city, state, Str_img;
    private DataBase_Helper db;
    private Object obj;
    private ProfileResponse profileResponse;
//            =new ProfileResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView backButton = toolbar.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, Home_page.class);
                startActivity(intent);
                finish();
            }
        });

        ProfileImage = findViewById(R.id.prflImage);
        Name = findViewById(R.id.prflName);
        Mobile = findViewById(R.id.prflNo);
        mEmail = findViewById(R.id.prflEmail);
        free_count = findViewById(R.id.free_count);
        Membership = findViewById(R.id.membership_type);
        Address = findViewById(R.id.membership_address);
        Edit = findViewById(R.id.prflEdit);
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileActivity.this, EditprofileActivity.class);
                intent.putExtra("Name", Name.getText().toString());
                intent.putExtra("Email", mEmail.getText().toString());
                intent.putExtra("Address", Addr);
                intent.putExtra("City", city);
                intent.putExtra("State", state);
                intent.putExtra("Image", Str_img);
                startActivity(intent);
                finish();
            }
        });

        db = new DataBase_Helper(this);

        userIdreq idreq = new userIdreq();
        idreq.userId = db.getUserId("1");
        try {
            obj = Class.forName(userIdreq.class.getName()).cast(idreq);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new RetrofitRequester(ProfileActivity.this).callPostServices(obj, 1, "/user/check_user_membership_type", true);

    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(ProfileActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {

            profileResponse = Common.getSpecificDataObject(objectResponse, ProfileResponse.class);
            Gson gson = new Gson();
            if (profileResponse.status.equals("success")) {
                Addr = profileResponse.address;
                city = profileResponse.city;
                state = profileResponse.state;
                Str_img = profileResponse.profilePic.replace("\\", "");
                Name.setText(profileResponse.name);
                Mobile.setText(profileResponse.mobile);
                mEmail.setText(profileResponse.email);
                int type = Integer.parseInt(profileResponse.membershipType);
                if (type == 1) {
                    Membership.setText("Free");
                } else {
                    Membership.setText("Premium");
                }
//                Str_img = profileResponse.profilePic.replace("\\","");
                Address.setText(profileResponse.address + "\n" + profileResponse.city + "\n" + profileResponse.state);
                try {
//                    Picasso.with(ProfileActivity.this).load(profileResponse.profilePic).error(R.drawable.doctor_icon).into(ProfileImage);
//                    PicassoTools.clearCache(Picasso.with(context));
//                    Picasso.with(getApplicationContext()).invalidate(profileResponse.profilePic);
                    Picasso.with(ProfileActivity.this).load(profileResponse.profilePic).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).into(ProfileImage);

                    /*Glide.with(ProfileActivity.this)
                            .load(profileResponse.profilePic)
                            .bitmapTransform(new CropCircleTransformation(this))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(ProfileImage);

                    Glide.get(ProfileActivity.this).clearDiskCache();
*/

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("printStack", e.toString());
                }

                free_count.setText(profileResponse.coupons);
            } else {
                Toast.makeText(ProfileActivity.this, "" + profileResponse.state, Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void Clear_GlideCaches() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.get(ProfileActivity.this).clearMemory();
            }
        }, 0);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Glide.get(ProfileActivity.this).clearDiskCache();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, Home_page.class);
        startActivity(intent);
        finish();
    }
}



