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
import android.view.animation.OvershootInterpolator;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.ClinicsAdapter;
import com.example.pegasys.rapmedixuser.activity.newactivities.DoctorDescription;
import com.example.pegasys.rapmedixuser.activity.newactivities.MapsActivity;
import com.example.pegasys.rapmedixuser.activity.newactivities.Slot_Selectionpage;
import com.example.pegasys.rapmedixuser.activity.pojo.Doctorworkingdatail;

import java.util.ArrayList;

import at.blogc.android.views.ExpandableTextView;

/**
 * Created by pegasys on 12/13/2017.
 */

public class overView extends Fragment {
    ExpandableTextView tDocdesc;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager layoutManager;
    ClinicsAdapter clinicsAdapter;
    ArrayList<Doctorworkingdatail> mList = DoctorDescription.listWork;
    private String sBio;
    private String doctor_Id, mdoctorName;

    public overView() {
    }

    public static overView newInstance(ArrayList<Doctorworkingdatail> mWorkingList, String bio, String docid) {


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
        tDocdesc = view.findViewById(R.id.doctor_desc);
        mRecyclerview = view.findViewById(R.id.rv_overview);
        tDocdesc.setText(sBio);
        tDocdesc.setAnimationDuration(1000);
        tDocdesc.setExpandInterpolator(new OvershootInterpolator());
        tDocdesc.setCollapseInterpolator(new OvershootInterpolator());
        tDocdesc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tDocdesc.isExpanded()) {
                    tDocdesc.collapse();
                } else {
                    tDocdesc.expand();
                }
            }
        });
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
                switch (view.getId()) {

                    case R.id.iv_navifation:
                        String mLati, mLongi;
                        mLati = mList.get(position).lat;
                        mLongi= mList.get(position)._long;
                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                        intent.putExtra("Latitude", mLati);
                        intent.putExtra("Langitude", mLongi);
                        startActivity(intent);
                        break;
                    case R.id.doctor_row_book:

                        Intent intr = new Intent(getActivity(), Slot_Selectionpage.class);
                        intr.putExtra("HOSP_ID", mList.get(position).hospitalId);
                        intr.putExtra("HOSP_NAME", mList.get(position).hospitalName);
                        intr.putExtra("ID", mList.get(position).id);
                        intr.putExtra("doctorId", doctor_Id);
                        intr.putExtra("doctorName", mdoctorName);
                        intr.putExtra("Fee", mList.get(position).fee);
                        startActivity(intr);

                        break;
                }
            }
        });


    }
}
