package com.example.pegasys.rapmedixuser.activity.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.Homevisitadapter;
import com.example.pegasys.rapmedixuser.activity.pojo.docSpec;
import com.example.pegasys.rapmedixuser.activity.pojo.homevisitresponse;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/18/2017.
 */

public class HomeVisitsFragment extends Fragment implements RetrofitResponseListener {
    private GridView gridhomevisit;
    Homevisitadapter homevisitadapter;
    ArrayList<homevisitresponse.Visitlist> visitlists = new ArrayList<>();
    Object obj;

    public HomeVisitsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctors_layout, container, false);
        gridhomevisit = (GridView) view.findViewById(R.id.grid_doctors);

        obj = new Object();
        Log.d("obj", obj.toString());

        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/getHomeVisit_service", true);
        return view;
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            homevisitresponse hvrespo = Common.getSpecificDataObject(objectResponse, homevisitresponse.class);
            Gson gson = new Gson();
            if (hvrespo.status.equals("success")) {
                visitlists = (ArrayList<homevisitresponse.Visitlist>) hvrespo.visitlistList;
                homevisitadapter = new Homevisitadapter(getActivity(), visitlists);
                gridhomevisit.setAdapter(homevisitadapter);
                gridhomevisit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getActivity(), "You Clicked at " + visitlists.get(i).homeserviceName, Toast.LENGTH_SHORT).show();
                    }
                });

            } else Toast.makeText(getActivity(), "" + hvrespo.status, Toast.LENGTH_SHORT).show();
        }

    }
}
