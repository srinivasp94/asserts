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
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.PrescriptAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.HealthRecordsRespo;
import com.example.pegasys.rapmedixuser.activity.pojo.PrescriptList;
import com.example.pegasys.rapmedixuser.activity.pojo.Typos;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pegasys on 12/26/2017.
 */

public class PrescriptionFrag extends Fragment implements RetrofitResponseListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    DataBase_Helper db;
    private Object obj;
    ArrayList<PrescriptList> prescriptLists = new ArrayList<>();
    PrescriptAdapter prescriptAdapter;

    public PrescriptionFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.rv_add);
        db = new DataBase_Helper(getActivity());
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);


        userIdreq req = new userIdreq();
        req.userId = db.getUserId("1");
        try {
            obj = Class.forName(userIdreq.class.getName()).cast(req);
            Log.i("obj", obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "/user/viewhealthrecords_service", true);
        return view;

    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            HealthRecordsRespo healthRecordsRespo = Common.getSpecificDataObject(objectResponse, HealthRecordsRespo.class);
            Gson gson = new Gson();
            if (healthRecordsRespo.status.equals("success")) {
                Typos typos = healthRecordsRespo.typos;
                /*for (int i = 0; i < 10; i++) {

                    PrescriptList list = new PrescriptList();
                    list.setTitle(typos.title);
                    list.setCreatedat(typos.createdDate);
                    HashMap<String, String> map = new HashMap<>();
                    if (typos.docType.equals("1")) {

                        map.put("DP", "Docotor Prescription");
                        list.setDoc_type("Docotor Prescription");
                    }
                    if (typos.docType.equals("2")) {
                        map.put("LR", "Lab Reports");
                        list.setDoc_type("Lab Reports");
                    }
                    if (typos.docType.equals("3")) {
                        map.put("DS", "Discharge Summary");
                        list.setDoc_type("Discharge Summary");
                    }
                    if (typos.docType.equals("4")) {
                        map.put("MB", "Medical Bills");
                        list.setDoc_type("Medical Bills");
                    }
                    prescriptLists.add(list);
                }*/

                prescriptAdapter = new PrescriptAdapter(getActivity(), prescriptLists);
                recyclerView.setAdapter(prescriptAdapter);
            }
        }


    }
}
