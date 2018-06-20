package com.pagasys.welcome.pegasysattendence;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pagasys.welcome.pegasysattendence.models.EmployeeInfo;
import com.pagasys.welcome.pegasysattendence.utils.Validations;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by pegasys on 10/5/2017.
 */

public class OnetimeLoginActivity extends AppCompatActivity {
    EditText mName,mbiometric, mRole, mPassword;
//    AutoCompleteTextView mName;
    Button signin;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mdbRef;
    private Validations validations;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onetime_login);
//        mName = (AutoCompleteTextView) findViewById(R.id.name);
        mName = (EditText) findViewById(R.id.name);
        mbiometric = (EditText) findViewById(R.id.biometricid);
        mRole = (EditText) findViewById(R.id.role);
        mPassword = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.email_sign_in_button);



        validations = new Validations(OnetimeLoginActivity.this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validations.Is_Valid_Person_Name(mName) && validations.Is_valid_password(mbiometric) && validations.Is_Valid_Person_Name(mRole) && validations.Is_valid_password(mPassword)) {

//                    validations.Is_Valid_Person_Name(mName);
//                    validations.Is_valid_password(mbiometric);
//                    validations.Is_Valid_Person_Name(mRole);
//                    validations.Is_valid_password(mPassword);

                    String name = mName.getText().toString();
                    String bioAccess = mbiometric.getText().toString();
                    String role = mRole.getText().toString();
                    String password = mPassword.getText().toString();

                    mDatabase = FirebaseDatabase.getInstance();
                    mdbRef = mDatabase.getReference("Employee Information");
                    if (TextUtils.isEmpty(userId)) {
                        userId = mdbRef.push().getKey();
                    }
//                    EmployeeInfo info = new EmployeeInfo(name,bioAccess,role,password);
                    mdbRef.child(userId).setValue(new EmployeeInfo(name,bioAccess,role,password));
                    startActivity(new Intent(OnetimeLoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(OnetimeLoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
