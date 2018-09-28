package com.example.pegasys.rapmedixuser.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.OTPpojo;
import com.example.pegasys.rapmedixuser.activity.pojo.Otpresend;
import com.example.pegasys.rapmedixuser.activity.pojo.Otpresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.User;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.example.pegasys.rapmedixuser.activity.utils.ConnectionDetector;
import com.google.gson.Gson;

import java.util.List;


public class OtpActivity extends AppCompatActivity implements RetrofitResponseListener {

    TextView resend_otp;
    EditText editText_otp;
    Button Submit;
    BroadcastReceiver broadcastReceiver;
    static boolean isInternentAvailable;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 125;
    View rootview;
    ConnectionDetector detector;
    private String uid;
    private String Mobile;
    private String otp;
    private String Name;
    private ProgressDialog pDialog;
    LinearLayout linearLayout;
    private Object obj;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        rootview = findViewById(R.id.Otp_rootview);
        linearLayout = findViewById(R.id.llsnack);
        editText_otp = findViewById(R.id.logmobileotp);
        Submit = findViewById(R.id.otp_submit);
        resend_otp = findViewById(R.id.resend_otp);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        Mobile = intent.getStringExtra("mobile");
        String motp = intent.getStringExtra("otp");
        otp = motp.substring(0, 4);
        Name = intent.getStringExtra("name");
        Log.e("otp_", Name + "otp  " + otp + "  mob  " + Mobile + "  uid  " + uid);
        preferences = getSharedPreferences("REF", MODE_PRIVATE);

//        Otpresend otpobject = new Otpresend();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText_otp.getText().toString();
                if (!s.equals(otp)) {
                    Snackbar snackbar = Snackbar.make(linearLayout, "Please Enter valid OTP ", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.show();
                } else {
                    verifyOtp();
                }
            }
        });
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servercallresendotp();
            }
        });
        /*if (isReadStorageAllowed()) {
            //If permission is already having then showing the toast
            //Toast.makeText(Otp_page.this,"You already have the permission",Toast.LENGTH_LONG).show();
            //Existing the method with return
            return;
        } else requestStoragePermission();*/

    }

    private void servercallresendotp() {
        OTPpojo insta = new OTPpojo();
        insta.mobile = Mobile;


        try {
            obj = Class.forName(OTPpojo.class.getName()).cast(insta);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 1, "/user/resendotp_service", true);
    }

    private void verifyOtp() {

        OTPpojo insta = new OTPpojo();
        insta.mobile = Mobile;

        try {
            obj = Class.forName(OTPpojo.class.getName()).cast(insta);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 2, "/user/userdata_service", true);
    }

    private boolean isReadStorageAllowed() {

        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 0);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Bundle bundle = intent.getExtras();

                //  public void recivedSms(String message) {
                try {
                    //Intent inte = new Intent();
                    String message = bundle.getString("message");

                    editText_otp.setText(message);
                    if (editText_otp.getText().toString().equals(message)) {
                        try {
                            String otpdat = editText_otp.getText().toString().trim();
                            verifyOtp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {

                }
                registerReceiver(broadcastReceiver, new IntentFilter("SMS_SENT"));
            }


        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            Log.e("exp", e + "exp");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == 0) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                // Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                // Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(OtpActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {

                case 1:

                    Otpresend otpobject = Common.getSpecificDataObject(objectResponse, Otpresend.class);
                    Gson gson = new Gson();

                    String statusmsg = otpobject.status;
                    if (statusmsg.equalsIgnoreCase("success")) {
                        String otpsmsg = otpobject.status;
                        String rOtp = otpobject.otp;
                        otp = rOtp.substring(0, 4);
                        Log.i("otp_", rOtp + "" + otp);
//                        otp = String.valueOf(otpobject.otp);
                    } else {
                        Toast.makeText(getApplicationContext(), "" + statusmsg, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Otpresponse otpresponse = Common.getSpecificDataObject(objectResponse, Otpresponse.class);
                    gson = new Gson();

                    String status = otpresponse.status;
                    if (status.equalsIgnoreCase("success")) {
                        DataBase_Helper dh = new DataBase_Helper(OtpActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("REFERRAL", otpresponse.referral);
                        editor.commit();
                        int c = dh.getUserCount();
                        if (c == 0) {
                            dh.insertUserId(new User("1", uid, Mobile, Name));
                        } else {
                            dh.updatetUserId(new User("1", uid, Mobile, Name));
                        }
                        List<User> li = dh.getUserData();
                        for (User u : li) {
                            String log = "username" + " " + u.getName() + " mobile " + u.getMobile() + " user id " + u.getUid();
                            Log.e("db data", log);
                        }
                        //  Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(OtpActivity.this, Home_page.class);
                        startActivity(intent);
                        /*if (broadcastReceiver != null) {
                            unregisterReceiver(broadcastReceiver);
                            broadcastReceiver = null;
                        }*/
                        finish();


                    } else {
                        Toast.makeText(getApplicationContext(), "Please try again" + status, Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }
}
