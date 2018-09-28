package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.CentersAdapter;
import com.example.pegasys.rapmedixuser.activity.adapters.DiagnosticssubcatAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.CentersResponse;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.Diagnosticscenter;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.SubservicesName;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.subCategoriesCentersRequest;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;

public class DiagnosticsCentersActivity extends AppCompatActivity implements RetrofitResponseListener, View.OnClickListener {
    private RecyclerView recyclerView_centers;
    private TextView txt_hint, txt_sort;
    private String iAddress, iServiceIds, iCity;
    private double iLat, iLong;
    private Object obj;
    private CentersAdapter mcentersAdapter;

    private RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
    private ArrayList<Diagnosticscenter> diagnosticscentersList = new ArrayList<>();
    private ArrayList<SubservicesName> subservicesNamesList = new ArrayList<>();
    ArrayList<String> namesList = new ArrayList<>();
    private String hospitalId;
    private String serviceids;
    private ImageView backButton;
    private DataBase_Helper dataBase_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics_centers);

        initComponents();
    }

    private void initComponents() {
        setreferecnces();
        setonClicklistners();
        getCustomIntent();
    }

    private void setreferecnces() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backButton = toolbar.findViewById(R.id.backbutton);
        recyclerView_centers = findViewById(R.id.rv_centers);
        txt_hint = findViewById(R.id.tv_hinttitle);
        txt_sort = findViewById(R.id.tv_sort);

    }

    private void setonClicklistners() {
        backButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbutton:
                finish();
                break;
        }
    }

    private void getCustomIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            iServiceIds = intent.getStringExtra("Subservice_id");
            iLat = intent.getDoubleExtra("Latitude", 0.0);
            iLong = intent.getDoubleExtra("Longitude", 0.0);
            iAddress = intent.getStringExtra("Location");
        }
        txt_hint.setText("Selected Tests Are available at following Centers" + iAddress);
        recyclerView_centers.setLayoutManager(manager);
        subCategoriesCentersRequest request = new subCategoriesCentersRequest();
        request.userid = new DataBase_Helper(this).getUserId("1");
        request.subserviceId = iServiceIds;
        request.latitude = iLat;
        request.longitude = iLong;
        request.location = iAddress;
        request.city = iAddress;

        try {
            obj = Class.forName(subCategoriesCentersRequest.class.getName()).cast(request);
            Log.i("obj", obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "/index/get_diagnostics_centers_service", true);

    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null && objectResponse.equals("")) {
            Toast.makeText(DiagnosticsCentersActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    CentersResponse response = Common.getSpecificDataObject(objectResponse, CentersResponse.class);
                    Gson gson = new Gson();
                    diagnosticscentersList = (ArrayList<Diagnosticscenter>) response.diagnosticscenters;
                    subservicesNamesList = (ArrayList<SubservicesName>) response.subservicesNames;

                    SubservicesName name = new SubservicesName();
                    namesList.clear();
                    for (SubservicesName serviceIds : subservicesNamesList) {

                        String stringName = serviceIds.id;
                        namesList.add(stringName);
                        Log.i("namesList", namesList.toString());

                    }
                    if (diagnosticscentersList != null && diagnosticscentersList.size() > 0) {
                        setDataAdapter();
                    } else {

                        Toast.makeText(this, "No hospitals were Found", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    }

    private void setDataAdapter() {
/*
        if (Constants.cartPageitems != null && Constants.cartPageitems.size() > 0) {
            ArrayList<Diagnosticscenter> list = new ArrayList<>();

            if (diagnosticscentersList.contains(Constants.cartPageitems)) {
                Diagnosticscenter name;
                for (int i = 0; i < diagnosticscentersList.size(); i++) {
                    name = new Diagnosticscenter();
                    name.name = diagnosticscentersList.get(i).name;
                    name.hospitalId = diagnosticscentersList.get(i).hospitalId;
                    name.profilePic = diagnosticscentersList.get(i).profilePic;
                    name.geolocation = diagnosticscentersList.get(i).geolocation;
                    name.distance = diagnosticscentersList.get(i).distance;
                    name.bprice = diagnosticscentersList.get(i).bprice;
                    name.diagnosticsId = diagnosticscentersList.get(i).diagnosticsId;
                    list.add(name);
                }
                mcentersAdapter = new CentersAdapter(getApplicationContext(), list);
                recyclerView_centers.setAdapter(mcentersAdapter);
                mcentersAdapter.setOnItemClickListener(new CentersAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (view.getId()) {

                            case R.id.ll_item:
                                hospitalId = diagnosticscentersList.get(position).hospitalId;
                                serviceids = namesList.toString().replace("[", "").replace("]", "");
                                Intent intent = new Intent(DiagnosticsCentersActivity.this, TestReviewsActivity.class);
                                intent.putExtra("NAME", diagnosticscentersList.get(position).name);
                                intent.putExtra("HospitalId", hospitalId);
                                intent.putExtra("ServiceIDS", serviceids);
                                startActivity(intent);
                                break;
                        }
                    }
                });
            }


                else {
                    Toast.makeText(this, "No Tests Are available with same hospital", Toast.LENGTH_SHORT).show();
                }
            }
*/


//        else {
            mcentersAdapter = new CentersAdapter(getApplicationContext(), diagnosticscentersList);
            recyclerView_centers.setAdapter(mcentersAdapter);
            mcentersAdapter.setOnItemClickListener(new CentersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    switch (view.getId()) {

                        case R.id.ll_item:
                            hospitalId = diagnosticscentersList.get(position).hospitalId;
                            serviceids = namesList.toString().replace("[", "").replace("]", "");
                            Intent intent = new Intent(DiagnosticsCentersActivity.this, TestReviewsActivity.class);
                            intent.putExtra("NAME", diagnosticscentersList.get(position).name);
                            intent.putExtra("HospitalId", hospitalId);
                            intent.putExtra("ServiceIDS", serviceids);
                            startActivity(intent);
                            break;
                    }
                }
            });

//        }
    }

}
