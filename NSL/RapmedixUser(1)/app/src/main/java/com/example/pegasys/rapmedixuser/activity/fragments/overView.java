package com.example.pegasys.rapmedixuser.activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.ClinicsAdapter;
import com.example.pegasys.rapmedixuser.activity.newactivities.DoctorDescription;
import com.example.pegasys.rapmedixuser.activity.newactivities.Slot_Selectionpage;
import com.example.pegasys.rapmedixuser.activity.pojo.Doctorworkingdatail;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/13/2017.
 */

public class overView extends Fragment {
    TextView tDocdesc;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager layoutManager;
    ClinicsAdapter clinicsAdapter;
    ArrayList<Doctorworkingdatail> mList = DoctorDescription.listWork;
    private String sBio;
    private String doctor_Id,mdoctorName;

    public overView() {
    }

    public static overView newInstance(ArrayList<Doctorworkingdatail> mWorkingList, String bio,String docid) {


        Bundle args = new Bundle();
        args.putParcelableArrayList("DOCTORWORKINGDATAIL", mWorkingList);
        args.putString("ABOUTUS", bio);
        args.putString("DOC_ID", docid);
        overView frag = new overView();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mList = getArguments().getParcelableArrayList("DOCTORWORKINGDATAIL");
            sBio = getArguments().getString("ABOUTUS");
            doctor_Id = getArguments().getString("DOC_ID");
            mdoctorName = getArguments().getString("Doc_Name");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview, container, false);
        tDocdesc = (TextView) view.findViewById(R.id.doctor_desc);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.rv_overview);
        tDocdesc.setText(sBio);
        setclientsView();
        return view;
    }


    private void setclientsView() {
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(layoutManager);
        clinicsAdapter = new ClinicsAdapter(getActivity(), mList);
        mRecyclerview.setAdapter(clinicsAdapter);
        clinicsAdapter.setOnItemClickListener(new ClinicsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), Slot_Selectionpage.class);
                intent.putExtra("HOSP_ID", mList.get(position).hospitalId);
                intent.putExtra("HOSP_NAME", mList.get(position).hospitalName);
                intent.putExtra("ID", mList.get(position).id);
                intent.putExtra("doctorId", doctor_Id);
                intent.putExtra("doctorName", mdoctorName);
                intent.putExtra("Fee", mList.get(position).fee);
                startActivity(intent);
            }
        });


    }
}
