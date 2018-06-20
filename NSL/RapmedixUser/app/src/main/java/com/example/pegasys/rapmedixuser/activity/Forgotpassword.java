package com.example.pegasys.rapmedixuser.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.forgotpwdresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.fpwreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.example.pegasys.rapmedixuser.activity.utils.Validation;
import com.google.gson.Gson;

public class Forgotpassword extends AppCompatActivity implements RetrofitResponseListener {
    EditText mobile;
    Button submit;
    LinearLayout layout;
    Validation validation;
    private Object obj;
//    private String statusMsg;
//    private String alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        mobile = (EditText) findViewById(R.id.mobile_number_input);
        submit = (Button) findViewById(R.id.forgot_btn);
        layout = (LinearLayout) findViewById(R.id.root);

        validation = new Validation();

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Validation.isPhoneNumber(mobile, true);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.getText().toString().equals("") || mobile.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(layout, "Enter Mobile Number", Snackbar.LENGTH_SHORT).setAction("Ok", null);
                    snackbar.show();
                } else {
                    String phone = mobile.getText().toString();
                    fpwreq fpreq = new fpwreq();
                    fpreq.usermobile = phone;

                    try {
                        obj = Class.forName(fpwreq.class.getName()).cast(fpreq);
                        Log.d("obj", obj.toString());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    new RetrofitRequester(Forgotpassword.this).callPostServices(obj, 1, "/user/user_forgotpassword_service", true);
                }
            }
        });
    }


    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(Forgotpassword.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            forgotpwdresponse fpr = Common.getSpecificDataObject(objectResponse, forgotpwdresponse.class);
            Gson gson = new Gson();
            final AlertDialog.Builder builder = new AlertDialog.Builder(Forgotpassword.this);
            builder.setTitle("Alert..!");
            builder.setCancelable(false);

            if (fpr.status.equals("1")) {
                Toast.makeText(Forgotpassword.this, "Your password send your register mobile number.", Toast.LENGTH_SHORT).show();
                final AlertDialog dialog = builder.create();
                final String statusMsg = fpr.message;
                builder.setMessage(fpr.message);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        Intent intent = new Intent(Forgotpassword.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
                builder.show();
            } else {
                String alert = fpr.message;
                final AlertDialog dialog = builder.create();
                final String statusMsg = fpr.message;
                builder.setMessage(fpr.message);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        Intent intent = new Intent(Forgotpassword.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
                builder.show();
            }
        }
    }

    private String getAlert(final String statusMsg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Forgotpassword.this);
        builder.setTitle("Alert..!");
        builder.setCancelable(false);
//        builder.setMessage(statusMsg);

        final AlertDialog dialog = builder.create();

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();

            }
        });
        builder.show();

        return statusMsg;
    }
}
