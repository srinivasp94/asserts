package com.nsl.app.fieldestimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nsl.app.R;


public class FieldAreaActivity extends AppCompatActivity {
    final String[] str={"Sq m","Sq km","Hectare","Acre","Sq Mile"};
    Spinner sp_units;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_area);

        sp_units= (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adp1=new ArrayAdapter<String>(this,R.layout.spinner_item, R.id.customSpinnerItemTextView,str);

        sp_units.setAdapter(adp1);


    }
}
