package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;

public class PrescriptionActivity extends AppCompatActivity {
    RecyclerView rv;
    RecyclerView.LayoutManager manager;
    TextView noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        rv= (RecyclerView)findViewById(R.id.rv);
        noData = (TextView)findViewById(R.id.tv_nodata);
        noData.setText("No Data Found");
        manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);

    }
}
