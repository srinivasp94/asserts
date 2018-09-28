package com.example.pegasys.rapmedixuser.activity.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.Home_page;
import com.example.pegasys.rapmedixuser.activity.LoginActivity;
import com.example.pegasys.rapmedixuser.activity.adapters.docspecgrid;
import com.example.pegasys.rapmedixuser.activity.newactivities.DoctorlistPage;
import com.example.pegasys.rapmedixuser.activity.pojo.DocspecList;
import com.example.pegasys.rapmedixuser.activity.pojo.User;
import com.example.pegasys.rapmedixuser.activity.pojo.docSpec;
import com.example.pegasys.rapmedixuser.activity.pojo.loginRequsest;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pegasys on 12/11/2017.
 */

public class Doctorsfrag extends Fragment implements RetrofitResponseListener {
    GridView gridView;
    docSpec spec;
    docspecgrid specadapter;
    ArrayList<DocspecList> docspecLists;
    Object obj;
    Double lat;
    Double longg;
    private String selected_address;
    SharedPreferences sp;
    SharedPreferences ref_code_sp;
    public static final String pref = "Location";
    private long longitude, latit;
    private String id, title, icon_Url;
    ArrayList<String> searchname = new ArrayList<>();
    private ArrayList<String> idlist=new ArrayList<>();

    public Doctorsfrag() {
    }

    public static Doctorsfrag newInstance(Double latitude, Double longitide) {

        Bundle args = new Bundle();
        args.putDouble("LATITUDE", latitude);
        args.putDouble("LONGITUDE", longitide);
        Doctorsfrag fragment = new Doctorsfrag();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble("LATITUDE");
            longg = getArguments().getDouble("LONGITUDE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctors_layout, container, false);
        gridView = view.findViewById(R.id.grid_doctors);

        sp = getActivity().getSharedPreferences(pref, MODE_PRIVATE);
        ref_code_sp = getActivity().getSharedPreferences("category",MODE_PRIVATE);
        lat = Double.valueOf(sp.getLong("lattitude", 0L));
        longg= Double.valueOf(sp.getLong("longitude", 0L));


        //            obj = Class.forName(loginRequsest.class.getName()).cast(logRequest);
        obj = new Object();
        Log.d("obj", obj.toString());

        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/getallspecialisations", true);

        return view;
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {

        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            spec = Common.getSpecificDataObject(objectResponse, docSpec.class);
            Gson gson = new Gson();
            if (spec.status.equals("success")) {
                docspecLists = (ArrayList<DocspecList>) spec.docspecLists;
                for (int i=0;i<docspecLists.size();i++) {
                    searchname.add(docspecLists.get(i).specialisationName);
                    idlist.add(docspecLists.get(i).id);
                }
                Home_page activity = (Home_page) getActivity();
                activity.setSearchAdaper(searchname,idlist);
                specadapter = new docspecgrid(getActivity(), docspecLists);
                gridView.setAdapter(specadapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        id = docspecLists.get(position).id;
                        title = docspecLists.get(position).specialisationName;
                        icon_Url = docspecLists.get(position).specialisationImage;

                        SharedPreferences.Editor editor = ref_code_sp.edit();
                        editor.putString("spec_name", title);
                        editor.commit();
                        Log.e("adddssa", "key" + ref_code_sp.getString("spec_name", "em"));

                        Intent intent = new Intent(getActivity(), DoctorlistPage.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("longg", longg);
                        intent.putExtra("id", id);
                        intent.putExtra("TITLE", title);
                        intent.putExtra("location", selected_address);
                        Log.e("aa", latit + id + "ccc" + longitude);
                        Log.e("aa", lat + id + "ccc" + longg);
//                        Toast.makeText(getActivity(), "clicked at" + position + id, Toast.LENGTH_SHORT).show();
                        startActivity(intent);

//                            actv.setText("");
                    }
                });
                Animation slide_down = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.right_left);
                gridView.setAnimation(slide_down);

            } else {
                Toast.makeText(getActivity(), "" + spec.status, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
