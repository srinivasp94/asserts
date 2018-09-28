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
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.AppointmentAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.AppointmentsList;
import com.example.pegasys.rapmedixuser.activity.pojo.AppointmentsResp;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/26/2017.
 */

public class CompletedFrag extends Fragment implements RetrofitResponseListener {

    DataBase_Helper db;
    String uid;
    ArrayList<AppointmentsList> appointmentsList = new ArrayList<>();
    ArrayList<AppointmentsList> mList = new ArrayList<>();
    AppointmentAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private Object obj;
    TextView textView_nodata;


    public CompletedFrag() {
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
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
        recycler = view.findViewById(R.id.rv_add);
        textView_nodata = view.findViewById(R.id.text_nodata);

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
            switch (requestId) {

                case 1:
                    AppointmentsResp res = Common.getSpecificDataObject(objectResponse, AppointmentsResp.class);
                    Gson gson = new Gson();
                    if (res.status.equals("success")) {
                        appointmentsList.clear();
                        appointmentsList = res.appointmentsLists;
                        mList.clear();
                        for (int i = 0; i < appointmentsList.size(); i++) {
                            AppointmentsList model = new AppointmentsList();
                            model.name = appointmentsList.get(i).name;
                            model.id = appointmentsList.get(i).id;
                            model.categoryName = appointmentsList.get(i).categoryName;
                            model.hospitalName = appointmentsList.get(i).hospitalName;
                            model.location = appointmentsList.get(i).location;
                            model.appointmentDate = appointmentsList.get(i).appointmentDate;
                            model.appointmentTime = appointmentsList.get(i).appointmentTime;
                            model.status = appointmentsList.get(i).status;


                            if (appointmentsList.get(i).status.equals("3")) {
//                        mList.clear();
                                mList.add(model);
                            }
                        }
                        if (mList.size() == 0) {
                            textView_nodata.setVisibility(View.VISIBLE);
                            textView_nodata.setText("No Completed Appointments yet");
                        }
                        adapter = new AppointmentAdapter(getActivity(), mList);
                        recycler.setAdapter(adapter);


                    } else {
                        mList = new ArrayList<>();
                        if (mList.size() == 0) {
                            textView_nodata.setVisibility(View.VISIBLE);
                            textView_nodata.setText("No Completed Appointments yet");
                        }
                        Toast.makeText(getActivity(), res.status, Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        }
    }

}
