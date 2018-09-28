package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.changepwdReq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

public class Changepassword extends AppCompatActivity implements RetrofitResponseListener {
    EditText old_pwd, new_pwd, confirm_pwd;
    Button btn_change;
    DataBase_Helper dataBase_helper;
    private String UserId;
    private Object obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView backButton = (ImageView) toolbar.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataBase_helper = new DataBase_Helper(this);

        old_pwd = (EditText) findViewById(R.id.old_pwd);
        new_pwd = (EditText) findViewById(R.id.new_pwd);
        confirm_pwd = (EditText) findViewById(R.id.confirm_pwd);
        btn_change = (Button) findViewById(R.id.change_pwd);

        DataBase_Helper db = new DataBase_Helper(this);
        UserId = db.getUserId("1");

        btn_change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (old_pwd.getText().toString().length() == 0 || new_pwd.getText().toString().length() == 0 || (confirm_pwd.getText().toString().length() == 0)) {
                    Toast.makeText(getApplicationContext(), "Fields should not be empty", Toast.LENGTH_SHORT).show();

                } else if (old_pwd.getText().toString().equals(new_pwd.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "old password and new password should not be same", Toast.LENGTH_SHORT).show();

                } else if (!new_pwd.getText().toString().equals(confirm_pwd.getText().toString())) {
                    Toast.makeText(getApplicationContext(), " New and confirm  password should be same", Toast.LENGTH_SHORT).show();
                } else {
                    changepwdReq req = new changepwdReq();
                    req.userId = UserId;
                    req.oldpassword = old_pwd.getText().toString();
                    req.newpassword = new_pwd.getText().toString();
                    req.confirmpassword = confirm_pwd.getText().toString();
                    try {
                        obj = Class.forName(changepwdReq.class.getName()).cast(req);
                        Log.d("obj", obj.toString());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    new RetrofitRequester(Changepassword.this).callPostServices(obj, 1, "/user/changePassword_service", true);
                }
            }
        });
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(Changepassword.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            Simpleresponse res = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
            Gson gson = new Gson();
            if (res.status.equals("success")) {
                Toast.makeText(Changepassword.this, res.status, Toast.LENGTH_SHORT).show();
                old_pwd.setText("");
                new_pwd.setText("");
                confirm_pwd.setText("");
                finish();

            } else
                Toast.makeText(Changepassword.this, res.status, Toast.LENGTH_SHORT).show();
        }
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

}

