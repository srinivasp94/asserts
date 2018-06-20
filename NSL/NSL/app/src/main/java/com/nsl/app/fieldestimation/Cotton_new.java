package com.nsl.app.fieldestimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Cotton_new extends AppCompatActivity {

    EditText ed1,ed2,ed3,ed4;
    Button btn;
    TextView result;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cotton);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ed1=(EditText)findViewById(R.id.ed1);
        ed2=(EditText)findViewById(R.id.ed2);
        ed3=(EditText)findViewById(R.id.ed3);
        ed4=(EditText)findViewById(R.id.ed4);
        btn=(Button)findViewById(R.id.button_cotton);
        result=(TextView)findViewById(R.id.result);








        btn.setOnClickListener(new View.OnClickListener() {
            public double Yield_per_hector_in_grams;

            @Override
            public void onClick(View v) {
                String bolls_plant = ed1.getText().toString();
                String weight_plant = ed2.getText().toString();
                String row_distnce = ed3.getText().toString();
                String plant_distance = ed4.getText().toString();

                if (ed1.getText().toString().length()>0 && ed2.getText().toString().length()>0 && ed3.getText().toString().length()>0&& ed4.getText().toString().length()>0) {
                    Yield_per_hector_in_grams = (Double.parseDouble(bolls_plant) * Double.parseDouble(weight_plant)) * (10000 / (Double.parseDouble(row_distnce) * Double.parseDouble(plant_distance)));
                    result.setText("" + String.valueOf(Yield_per_hector_in_grams));
                }else {
                    Toast.makeText(getApplicationContext(),"Please enter field",Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

}
