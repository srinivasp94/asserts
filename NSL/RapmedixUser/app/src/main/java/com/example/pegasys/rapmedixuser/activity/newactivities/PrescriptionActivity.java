package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.LoginActivity;
import com.example.pegasys.rapmedixuser.activity.adapters.PrescriptionAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.PrescriptionModel;
import com.example.pegasys.rapmedixuser.activity.pojo.PrescriptionModelList;
import com.example.pegasys.rapmedixuser.activity.pojo.loginRequsest;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.pojo.userlog;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PrescriptionActivity extends AppCompatActivity implements RetrofitResponseListener {
    RecyclerView rv;
    RecyclerView.LayoutManager manager;
    TextView noData;
    DataBase_Helper db;
    ArrayList<PrescriptionModelList> pList = new ArrayList<>();
    private Object obj;
    PrescriptionAdapter prescriptionAdapter;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView backButton = toolbar.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        db = new DataBase_Helper(PrescriptionActivity.this);

        rv = findViewById(R.id.rv);
        noData = findViewById(R.id.tv_nodata);
        noData.setText("No Prescriptoins Added Yet");
        manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);

        userIdreq uidreq = new userIdreq();
        uidreq.userId = new DataBase_Helper(PrescriptionActivity.this).getUserId("1");
        try {
            obj = Class.forName(userIdreq.class.getName()).cast(uidreq);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 1, "/user/userPrescriptions_service", true);

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

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(PrescriptionActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            PrescriptionModel prescriptionModel = Common.getSpecificDataObject(objectResponse, PrescriptionModel.class);
            Gson gson = new Gson();
            if (prescriptionModel.status.equals("success")) {

                pList = (ArrayList<PrescriptionModelList>) prescriptionModel.mPrescriptionModelList;
                if (pList.size() == 0) {
                    noData.setVisibility(View.VISIBLE);
                    noData.setText("No Prescriptoins Added Yet");
                }
                url = prescriptionModel.imgUrl;
                setDataAdapter();


            } else {
                pList = new ArrayList<>();
                if (pList.size() == 0) {
                    noData.setVisibility(View.VISIBLE);
                    noData.setText("No Prescriptoins Added Yet");
                }
            }
        }


    }

    private void setDataAdapter() {

        prescriptionAdapter = new PrescriptionAdapter(PrescriptionActivity.this, pList);
        rv.setAdapter(prescriptionAdapter);
        prescriptionAdapter.setOnItemClickListener(new PrescriptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getApplicationContext(),ImageListActivity.class);
                intent.putExtra("Imageurls",pList.get(position).doctorPrescription);
                startActivity(intent);

                /*LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.prescription_imagepreview, null);
//                SubsamplingScaleImageView previewImage = (SubsamplingScaleImageView)view1.findViewById(R.id.iv_preview_prescription);
                ImageView previewImage  = view1.findViewById(R.id.iv_preview_prescription);
                AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionActivity.this);

                builder.setView(view1);
                Dialog dialog = builder.create();

                if (pList.get(position).doctorPrescription !=null) {
                    Picasso.with(PrescriptionActivity.this).load(url + pList.get(position).doctorPrescription)
                            .error(R.drawable.doctor_icon).into(previewImage);
                } else {

                }
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
                dialog.show();*/
//                builder.show();

            }
        });
    }
}
