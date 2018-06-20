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
import com.example.pegasys.rapmedixuser.activity.adapters.BestquoteAdapter;
import com.example.pegasys.rapmedixuser.activity.adapters.Homevisitadapter;
import com.example.pegasys.rapmedixuser.activity.pojo.bestQuote;
import com.example.pegasys.rapmedixuser.activity.pojo.bestquoteList;
import com.example.pegasys.rapmedixuser.activity.pojo.homevisitresponse;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/18/2017.
 */

public class Bestquotesfragment extends Fragment implements RetrofitResponseListener{
    GridView gridbqf;
    BestquoteAdapter bestquoteAdapter;
    ArrayList<bestquoteList> lists = new ArrayList<>();
    private Object obj;

    public Bestquotesfragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctors_layout, container, false);
        gridbqf= (GridView) view.findViewById(R.id.grid_doctors);
        obj = new Object();
        Log.d("obj", obj.toString());

        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/getBestQuote_service", true);
        return view;
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            bestQuote quote= Common.getSpecificDataObject(objectResponse, bestQuote.class);
            Gson gson = new Gson();
            if (quote.status.equals("success")) {
                lists = (ArrayList<bestquoteList>) quote.quoteList;
                bestquoteAdapter = new BestquoteAdapter(getActivity(), lists);
                gridbqf.setAdapter(bestquoteAdapter);
                gridbqf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getActivity(), "You Clicked at " + lists.get(i).bestquoteName, Toast.LENGTH_SHORT).show();
                    }
                });

            } else Toast.makeText(getActivity(), "" + quote.status, Toast.LENGTH_SHORT).show();
        }

    }
}
