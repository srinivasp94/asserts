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
import com.example.pegasys.rapmedixuser.activity.adapters.familyAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.Familydatum;
import com.example.pegasys.rapmedixuser.activity.pojo.familydata;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/21/2017.
 */

public class Familymemberfragment extends Fragment implements RetrofitResponseListener {
    RecyclerView mRecycle;
    RecyclerView.LayoutManager layoutManager;
    DataBase_Helper dataBase_helper;
    ArrayList<Familydatum> list = new ArrayList<>();
    private Object obj;
    familyAdapter adapter;

    public Familymemberfragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_recyclerview, container, false);
        dataBase_helper = new DataBase_Helper(getActivity());
        mRecycle = (RecyclerView) view.findViewById(R.id.rv_add);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecycle.setLayoutManager(layoutManager);

        userIdreq idreq = new userIdreq();
        idreq.userId = dataBase_helper.getUserId("1");
        try {
            obj = Class.forName(userIdreq.class.getName()).cast(idreq);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "user/get_family_members", true);

        return view;
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            familydata mFamilydata = Common.getSpecificDataObject(objectResponse, familydata.class);
            Gson gson = new Gson();
            if (mFamilydata.status.equals("success")) {
                list = (ArrayList<Familydatum>) mFamilydata.familydata;
                adapter = new familyAdapter(getActivity(),list);
                mRecycle.setAdapter(adapter);

            } else {
                Toast.makeText(getActivity(), " " + mFamilydata.status, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
