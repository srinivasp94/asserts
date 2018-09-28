package com.example.pegasys.rapmedixuser.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.RegisterDetails;
import com.example.pegasys.rapmedixuser.activity.pojo.User;
import com.example.pegasys.rapmedixuser.activity.pojo.userlog;
import com.example.pegasys.rapmedixuser.activity.receivers.MyFirebaseInstanceIDService;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.APIInterface;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.example.pegasys.rapmedixuser.activity.utils.ConnectionDetector;
import com.example.pegasys.rapmedixuser.activity.utils.Validation;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity implements RetrofitResponseListener {

    EditText name_regi, mobile_num, email_reg, pwd_reg, confirm_pwd, referral_code;
    Button reg_button;
    TextView login_text;
    boolean isinternet;
    View v;
    LinearLayout layout;
    ConnectionDetector detector;
    private String Notification;
    private ProgressDialog pDialog;
    private Object obj;
    private APIInterface apiInterface;
    TextInputLayout inputLayoutName, inputLayoutphone, inputLayoutMail, inputLayoutpwd, inputLayoutcnf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        detector = new ConnectionDetector(this);
        isinternet = detector.isConnectingToInternet();

        name_regi = findViewById(R.id.name_reg);
        mobile_num = findViewById(R.id.mob_number);
        email_reg = findViewById(R.id.email_reg);
        pwd_reg = findViewById(R.id.pwd_reg);
        confirm_pwd = findViewById(R.id.confirm_pwd);
        referral_code = findViewById(R.id.referral_code);
        reg_button = findViewById(R.id.submit_reg);
        login_text = findViewById(R.id.login_text);
        layout = findViewById(R.id.loginsnackbar);

        inputLayoutName = findViewById(R.id.tl_name);
        inputLayoutphone = findViewById(R.id.tl_phone);
        inputLayoutMail = findViewById(R.id.tl_email);
        inputLayoutpwd = findViewById(R.id.tl_pwd);
        inputLayoutcnf = findViewById(R.id.tl_cnfpwd);

        final SharedPreferences sp = getSharedPreferences(MyFirebaseInstanceIDService.pref, MODE_PRIVATE);
        Notification = sp.getString("Notification", "Error");
        Log.e("TAG", Notification);
        pDialog = new ProgressDialog(RegisterActivity.this);

        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login_intent);
                finish();
            }
        });

        alldataValidations();


    }

    private void alldataValidations() {
        name_regi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Validation.hasText(name_regi);
            }
        });
        mobile_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Validation.isPhoneNumber(mobile_num, true);
            }
        });

        pwd_reg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Validation.hasText(pwd_reg);
            }
        });
/*
        confirm_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Validation.hasText(confirm_pwd);
            }
        });
*/
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isinternet) {
                    Snackbar snackbar = Snackbar.make(layout, "No Internet Connection", Snackbar.LENGTH_SHORT).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    snackbar.show();
                } else {
                    if (checkValidation()) {
                        sendRequest();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Fill all required fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void sendRequest() {
        RegisterDetails registerDetails = new RegisterDetails();

        registerDetails.setName(name_regi.getText().toString());
        registerDetails.setMobile(mobile_num.getText().toString());
        registerDetails.setEmailId(email_reg.getText().toString());
        registerDetails.setPassword(pwd_reg.getText().toString());
//        registerDetails.setCpassword(confirm_pwd.getText().toString());
        registerDetails.setReferee(referral_code.getText().toString().trim());
        registerDetails.setDeviceToken(Notification);
        registerDetails.setDeviceType("1");

        Log.d("TAG", registerDetails.toString());

        try {
            obj = Class.forName(RegisterDetails.class.getName()).cast(registerDetails);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 1, "/user/userregister_service", true);

/*
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .build();
        RetrofitApi retrofitApi = restAdapter.create(RetrofitApi.class);
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                pDialog.dismiss();
                Log.d("TAg", o.toString());
                Log.d("TAg", response.getBody().toString());
                User user = (User) o;
                if (user.getStatus().equalsIgnoreCase("success")) {
                    String str_id = user.getId();
                    String str_uid = user.getUid();
                    String str_OTP = user.getOtp();
                    String str_mobile = user.getMobile();
                    String str_name = user.getName();
                    Log.d("TAg", str_uid + str_name + " OTPpojo " + str_OTP);

                    Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                    intent.putExtra("uid", str_uid);
                    intent.putExtra("mobile", str_mobile);
                    intent.putExtra("otp", str_OTP);
                    intent.putExtra("name", str_name);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "" + user.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("TAg", error.toString());
            }
        };
        retrofitApi.callforregister(registerDetails, callback);*/
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(name_regi)) ret = false;
        if (!Validation.isPhoneNumber(mobile_num, true)) ret = false;
        if (!Validation.isEmailAddress(email_reg, true)) ret = false;
        if (!Validation.hasText(pwd_reg)) ret = false;
//        if (!Validation.hasText(confirm_pwd)) ret = false;
//        if (!Validation.isequal(pwd_reg, confirm_pwd)) ret = false;
        return ret;

       /* if (name_regi.getText().toString().equalsIgnoreCase("")) {
            inputLayoutName.requestFocus();
            inputLayoutName.setError("Please enter name");
            return false;
        }*/
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
//        User user = (User) objectResponse;
        userlog user = Common.getSpecificDataObject(objectResponse, userlog.class);
        Gson gson = new Gson();

        if (user.status.equals("success")) {

            String str_uid = user.userId;
//            String str_OTP = user.otp;
            String str_mobile = user.mobile;
            String str_name = user.name;
            Log.d("TAg", str_uid + str_name + " OTPpojo ");
            Toast.makeText(getApplicationContext(), "" + user.status, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("uid", str_uid);
            intent.putExtra("mobile", str_mobile);
//            intent.putExtra("otp", str_OTP);
            intent.putExtra("name", str_name);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "" + user.status, Toast.LENGTH_SHORT).show();
        }

    }
}
