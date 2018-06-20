package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.Forgotpassword;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.ProfileResponse;
import com.example.pegasys.rapmedixuser.activity.pojo.forgotpwdresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.fpwreq;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

public class InviteFriendsActivity extends AppCompatActivity implements RetrofitResponseListener {
    TextView ref_code, p1, p2, p3;
    Button share_referral;
    SharedPreferences ref_code_sp,preferences,sp;
    ImageView share_via;
    public static final String ref_codee = "referral";
    DataBase_Helper db;
    private String userid;
    private Object obj;
    String invite_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        p1 = (TextView) findViewById(R.id.point1);
        p2 = (TextView) findViewById(R.id.point2);
        p3 = (TextView) findViewById(R.id.point3);
        ref_code = (TextView) findViewById(R.id.ref_code);
        share_via = (ImageView) findViewById(R.id.share_via);
        share_referral = (Button) findViewById(R.id.share_referral);

        db = new DataBase_Helper(this);
        userid = db.getUserId("1");
        sp = getSharedPreferences("refcode", MODE_PRIVATE);
        preferences = getSharedPreferences("REF",MODE_PRIVATE);
        invite_code = preferences.getString("REFERRAL","");
        Log.i("ref",invite_code);
        if (invite_code.equals("")) {
            userIdreq req = new userIdreq();
            req.userId = userid;
            try {
                obj = Class.forName(userIdreq.class.getName()).cast(req);
                Log.d("obj", obj.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            new RetrofitRequester(this).callPostServices(obj, 1, "/user/check_user_membership_type", true);

        } else {
            ref_code.setText(invite_code);
        }

        p1.setText("\u2022  Invite your friends to Get up to 3 Free Consultations. 1 for every successful signup through the app.");
        p2.setText("\u2022 Add your family members to get 1 Free Consultation.");
        p3.setText("\u2022 Sign Up to Rapmedix and Get 1 Free Consultation.");

        share_via.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share_intent = new Intent(Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra("android.intent.extra.TEXT", "Hey, \nI care about you, so i thought this app would be extremely useful for you & your family for your future Doctor Consultations and Health Checkups. Signup today to avail free consultations by using referral code"
                        + ": " + ref_code_sp.getString("referral_code", "empty") + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName().toString());
                startActivity(share_intent);
            }
        });

        share_referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PackageManager pm = getPackageManager();

                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");

                    String text = "Hey, \nI care about you, so i thought this app would be extremely useful for you & your family for your future Doctor Consultations and Health Checkups. Signup today to avail free consultations by using referral code"
                            + ": " + ref_code_sp.getString("referral_code", "empty") + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName().toString();

                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(InviteFriendsActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });



    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {

        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            ProfileResponse mProfileResponse = Common.getSpecificDataObject(objectResponse, ProfileResponse.class);
            Gson gson = new Gson();
            if (mProfileResponse.state.equals("success")) {
                String referal = mProfileResponse.referral;
                ref_code.setText(referal);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("REF", referal);
                editor.commit();

            } else {
                Toast.makeText(this, mProfileResponse.status, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
