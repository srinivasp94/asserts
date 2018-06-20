package com.example.pegasys.rapmedixuser.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.loginRequsest;
import com.example.pegasys.rapmedixuser.activity.pojo.userlog;
import com.example.pegasys.rapmedixuser.activity.receivers.MyFirebaseInstanceIDService;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.example.pegasys.rapmedixuser.activity.utils.ConnectionDetector;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements RetrofitResponseListener {

    EditText mobile_number, pwd_login;
    LinearLayout mobile;
    Button submit;
    TextView forgot_pwd, sign_up;
    ProgressDialog pDialog;
    LinearLayout linearLayout;
    boolean isNet;
    ConnectionDetector detector;
    private String Notification;
    Object obj;
    SharedPreferences global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobile_number = (EditText) findViewById(R.id.mobile_number_input);
        pwd_login = (EditText) findViewById(R.id.pwd_login);
        forgot_pwd = (TextView) findViewById(R.id.forgot);
        sign_up = (TextView) findViewById(R.id.signup);
        submit = (Button) findViewById(R.id.submit);
        linearLayout = (LinearLayout) findViewById(R.id.snackbarlogin);
        detector = new ConnectionDetector(this);
        isNet = detector.isConnectingToInternet();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Logging in...");

        final SharedPreferences sp = getSharedPreferences(MyFirebaseInstanceIDService.pref, this.MODE_PRIVATE);
        Notification = sp.getString("Notification", "Error");

        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_intent = new Intent(LoginActivity.this, Forgotpassword.class);
                startActivity(signup_intent);
                finish();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signup_intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNet) {
                    if (mobile_number.getText().toString().trim().equals("")) {
                        Snackbar snackbar = Snackbar.make(linearLayout, "Enter valid Mobile number", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                        snackbar.show();
                    } else {
                        verifyAuth();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyAuth() {
        loginRequsest logRequest = new loginRequsest();
        logRequest.mobile = mobile_number.getText().toString();
        logRequest.password = pwd_login.getText().toString();
        logRequest.deviceToken = Notification;
        logRequest.deviceType = "1";

        try {
            obj = Class.forName(loginRequsest.class.getName()).cast(logRequest);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 1, "/user/login_service", true);
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(LoginActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            userlog user = Common.getSpecificDataObject(objectResponse, userlog.class);
            Gson gson = new Gson();
            if (user.status.equals("success")) {
                String uid = user.userId;
                String mobile = user.mobile;
                String otp = user.otp;

                global = getSharedPreferences("loggers",MODE_PRIVATE);
                SharedPreferences.Editor editor = global.edit();
                editor.putString("UID",uid);
                editor.putString("NAME",user.name);
                editor.putString("MOBILE",mobile);
                editor.commit();

                if (mobile.equals(mobile_number.getText().toString())) {

                    Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("otp", otp);
                    intent.putExtra("name", user.name);
                    startActivity(intent);
                    finish();
//

                } else {
                    Toast.makeText(LoginActivity.this, "Not a registered User", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Cresdentials Or " + user.status, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {
        //super.onBackPressed();
        //finish();
        Intent back_intent = new Intent(LoginActivity.this, FirstActivity.class);
        startActivity(back_intent);

    }
}
