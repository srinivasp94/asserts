package com.example.pegasys.rapmedixuser.activity.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.healthAdapter;
import com.example.pegasys.rapmedixuser.activity.newactivities.DoctorlistPage;
import com.example.pegasys.rapmedixuser.activity.pojo.checkupList;
import com.example.pegasys.rapmedixuser.activity.pojo.checkupResponse;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/14/2017.
 */

public class HealthCheckups extends Fragment implements RetrofitResponseListener{
    private Object obj;
    ArrayList<checkupList> checkupListArrayList = new ArrayList<>();
    healthAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout layout;


    public HealthCheckups() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_doctorlist_page, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.doctors_list);
        layout = (RelativeLayout)view.findViewById(R.id.rl);
        layout.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        obj = new Object();
        Log.d("obj", obj.toString());

        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/healthCheckup_service", true);

        return view;
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            checkupResponse checkresponse = Common.getSpecificDataObject(objectResponse, checkupResponse.class);
            Gson gson = new Gson();
            if (checkresponse.status.equals("success")) {
                checkupListArrayList = (ArrayList<checkupList>) checkresponse.checkupLists;

                adapter = new healthAdapter(getActivity(),checkupListArrayList);
                recyclerView.setAdapter(adapter);

            }
            else {
                Toast.makeText(getActivity(), "" + checkresponse.status, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
