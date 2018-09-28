package com.example.pegasys.rapmedixuser.activity.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.DoctorservicesAdapter;
import com.example.pegasys.rapmedixuser.activity.newactivities.DoctorDescription;
import com.example.pegasys.rapmedixuser.activity.pojo.DoctorSelectedService;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/28/2017.
 */

public class ServicesFrag extends Fragment {
    ArrayList<DoctorSelectedService> serviceArrayList = DoctorDescription.selectedServiceList;
    GridView servicegrid;
    private String doctor_Id;
    private Object obj;
    DoctorservicesAdapter adapter;

    public ServicesFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceArrayList = getArguments().getParcelableArrayList("KeyServices");
            doctor_Id = getArguments().getString("DOC_ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctors_layout, container, false);
        servicegrid = view.findViewById(R.id.grid_doctors);
        setview();
        /*DocIdReq doc_id_request = new DocIdReq();
        doc_id_request.doctorId = doctor_Id;

        try {
            obj = Class.forName(DocIdReq.class.getName()).cast(doc_id_request);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        return view;
    }

    private void setview() {

//        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.timeslot_list, R.id.timeslot, serviceArrayList);
        adapter = new DoctorservicesAdapter(getActivity(),serviceArrayList);
        servicegrid.setAdapter(adapter);

    }
}
