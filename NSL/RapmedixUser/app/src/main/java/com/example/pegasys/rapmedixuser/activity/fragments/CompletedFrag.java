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
import com.example.pegasys.rapmedixuser.activity.adapters.AppointmentAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.AppointmentsList;
import com.example.pegasys.rapmedixuser.activity.pojo.AppointmentsResp;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/26/2017.
 */

public class CompletedFrag extends Fragment implements RetrofitResponseListener{

    DataBase_Helper db;
    String uid;
    ArrayList<AppointmentsList> appointmentsList = new ArrayList<>();
    AppointmentAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private Object obj;

    public CompletedFrag() {
    }

    RecyclerView recycler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_recyclerview, container, false);
        recycler = (RecyclerView)view.findViewById(R.id.rv_add);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);

        db = new DataBase_Helper(getActivity());
        uid = db.getUserId("1");

        userIdreq idreq = new userIdreq();
        idreq.userId = uid;

        try {
            obj = Class.forName(userIdreq.class.getName()).cast(idreq);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "user/userAppointments_service", true);
        return view;
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {

        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            AppointmentsResp res = Common.getSpecificDataObject(objectResponse, AppointmentsResp.class);
            Gson gson = new Gson();
            if (res.status.equals("success")) {
                appointmentsList = res.appointmentsLists;
                adapter = new AppointmentAdapter(getActivity(), appointmentsList);
                recycler.setAdapter(adapter);

            } else {
                Toast.makeText(getActivity(), res.status, Toast.LENGTH_SHORT).show();

            }
        }
    }
}
