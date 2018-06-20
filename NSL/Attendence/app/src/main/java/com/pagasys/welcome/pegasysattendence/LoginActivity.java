package com.pagasys.welcome.pegasysattendence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pagasys.welcome.pegasysattendence.models.EmployeeInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText edtuniqueNumber, edtPsaaword;
    Button btnLogin;
    String userid;
    SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private EmployeeInfo employeeInfo;
    private ArrayList<EmployeeInfo> employeeList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtuniqueNumber = (EditText) findViewById(R.id.et_uniquenumber);
        edtPsaaword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.bt_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPreferences = getSharedPreferences("LoginStatusPref", MODE_PRIVATE);
        mEditor = mPreferences.edit();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
//                mEditor.putBoolean("Status",true);
//                mEditor.putString("NAME",bio)
//                mEditor.commit();

            }
        });
    }

    private void submitForm() {
        if (!validId()) {
            return;
        }
        if (!validPassword()) {
            return;
        }

        callfireDb();

        Toast.makeText(LoginActivity.this, "thank You", Toast.LENGTH_SHORT).show();

    }

    public void callfireDb() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Employee Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                employeeList = new ArrayList<EmployeeInfo>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Log.i("EMPLOYE", "" + noteDataSnapshot);
                    employeeInfo = noteDataSnapshot.getValue(EmployeeInfo.class);
                    employeeList.add(employeeInfo);

                }

                int size = employeeList.size();
                for (int i = 0; i < size; i++) {
                    Log.i("EMPLOYEEINFO", "" + employeeList.get(i) + "&& " + employeeList.get(i).getBiometric());
                    if (edtuniqueNumber.getText().toString().matches(employeeList.get(i).getBiometric()) && edtPsaaword.getText().toString().matches(employeeList.get(i).getPassword())) {

//                        intent.putExtra("BIOMETRIC_ID", edtuniqueNumber.getText().toString());
//                        intent.putExtra("NAME", employeeList.get(i).getName());
//                        intent.putExtra("ROLE", employeeList.get(i).getRole());

                        mEditor.putBoolean("Status",true);
                        mEditor.putString("mNAME",employeeList.get(i).getName());
                        mEditor.putString("mROLE",employeeList.get(i).getRole());
                        mEditor.putString("mBIOMETRIC_ID",employeeList.get(i).getBiometric());
                        mEditor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean validPassword() {
        if (edtPsaaword.getText().toString().length() <= 0 && edtPsaaword.getText().toString().length() > 5) {
            edtPsaaword.setError("Enter Password");
            return false;
        } else {

        }
        return true;
    }

    private boolean validId() {
        if (edtuniqueNumber.getText().toString().length() <= 0 && edtuniqueNumber.getText().toString().length() > 5) {
            edtuniqueNumber.setError("Enter Biometric ID");
            return false;
        } else {

        }
        return true;
    }
}
