package com.example.pegasys.rapmedixuser.activity.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.healthAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.Callbackreq;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
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

public class HealthCheckups extends Fragment implements RetrofitResponseListener {
    private Object obj;
    ArrayList<checkupList> checkupListArrayList = new ArrayList<>();
    healthAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
//    RelativeLayout layout;
    DataBase_Helper dataBase_helper;
    private String type, id, mobile;
    private Dialog dialog;

    public HealthCheckups() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.healthcheckupfragmentlayout, container, false);
        recyclerView = view.findViewById(R.id.doctors_list);
//        layout = (RelativeLayout) view.findViewById(R.id.rl);
//        layout.setVisibility(View.GONE);
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
            switch (requestId) {
                case 1:

                    checkupResponse checkresponse = Common.getSpecificDataObject(objectResponse, checkupResponse.class);
                    Gson gson = new Gson();
                    if (checkresponse.status.equals("success")) {
                        checkupListArrayList = (ArrayList<checkupList>) checkresponse.checkupLists;

                        setAdaptermethod();

                    } else {
                        Toast.makeText(getActivity(), "" + checkresponse.status, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Simpleresponse simpleresponse = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
                    gson = new Gson();
                    if (simpleresponse.status.equals("success")) {
                        Toast.makeText(getActivity(), "" + simpleresponse.status, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "" + simpleresponse.status, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    break;
            }
        }

    }

    private void setAdaptermethod() {
        adapter = new healthAdapter(getActivity(), checkupListArrayList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new healthAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                type = "7";
                id = checkupListArrayList.get(position).id;
                mobile = new DataBase_Helper(getActivity()).getUserMobileNumber("1");
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.requst_callback);

                Button callBack = dialog.findViewById(R.id.callback_btn);
                callBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callbackmethod();

                    }
                });
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                dialog.show();

            }
        });
    }

    private void callbackmethod() {
        Callbackreq callbackreq = new Callbackreq();
        callbackreq.type = type;
        callbackreq.serviceId = id;
        callbackreq.callbackMobile = mobile;

        try {
            obj = Class.forName(Callbackreq.class.getName()).cast(callbackreq);
            Log.i("obj", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 2, "/user/requestCallback", true);
    }

}
