package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.ProfileResponse;
import com.example.pegasys.rapmedixuser.activity.pojo.forgotpwdresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements RetrofitResponseListener {

    ImageView ProfileImage;
    TextView Name, Mobile, mEmail, Membership, Address, free_count;//, State;
    LinearLayout Edit;
    String Addr, city, state,Str_img;
    DataBase_Helper db;
    private Object obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ProfileImage = (ImageView) findViewById(R.id.prflImage);
        Name = (TextView) findViewById(R.id.prflName);
        Mobile = (TextView) findViewById(R.id.prflNo);
        mEmail = (TextView) findViewById(R.id.prflEmail);
        free_count = (TextView) findViewById(R.id.free_count);
        Membership = (TextView) findViewById(R.id.membership_type);
        Address = (TextView) findViewById(R.id.membership_address);
        Edit = (LinearLayout) findViewById(R.id.prflEdit);
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
            ProfileResponse profileResponse = Common.getSpecificDataObject(objectResponse, ProfileResponse.class);
            Gson gson = new Gson();
            if (profileResponse.status.equals("success")) {
                Addr = profileResponse.address;
                city = profileResponse.city;
                state = profileResponse.state;

                Name.setText(profileResponse.name);
                Mobile.setText(profileResponse.mobile);
                mEmail.setText(profileResponse.email);
                int type = Integer.parseInt(profileResponse.membershipType);
                if (type == 1) {
                    Membership.setText("Free");
                } else {
                    Membership.setText("Premium");
                }
                Address.setText(profileResponse.address + "\n" + profileResponse.city + "\n" + profileResponse.state);
                Picasso.with(ProfileActivity.this).load(profileResponse.profilePic).into(ProfileImage);
                free_count.setText(profileResponse.coupons);
            } else {
                Toast.makeText(ProfileActivity.this, "" + profileResponse.state, Toast.LENGTH_SHORT).show();
            }
        }


    }
}
