package com.nsl.app.fieldestimation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OtherActivity extends AppCompatActivity {
    @BindView(R.id.ed1)
    EditText ed1;
    @BindView(R.id.button_cotton)
    Button btn;
    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.et_area_in_meters)
    EditText etAreaInMeters;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_crops);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @OnClick(R.id.button_cotton) void get(){
        String plot_grain_yield = ed1.getText().toString();

        double Yield_in_quintals;
        String plot_area = etAreaInMeters.getText().toString();
        if (ed1.getText().toString().length()>0 && plot_area.length()>0) {

            Yield_in_quintals = ((Double.parseDouble(plot_grain_yield) * 10000) / Double.parseDouble(plot_area))/ 100;
            result.setText("" + String.valueOf(Yield_in_quintals));
        }else {
            Toast.makeText(getApplicationContext(),"Please enter field",Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.tv_go_to_map) void tvGoToMap(){
        Intent intent=new Intent(this,FieldEstimationActivity.class);
        intent.putExtra("yield",true);
        startActivityForResult(intent, 10);
    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        Log.d("onActivityResult", "OnresultAxtivity1");
        switch (requestCode) {
            case 10:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        double area = data.getDoubleExtra("area", 0);
                        etAreaInMeters.setText(""+area);
                        Log.d("onActivityResult", "OnresultAxtivity4");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // finish();

                        break;

                    default:
                        break;
                }
                break;


        }
    }

}
