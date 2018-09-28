package com.example.pegasys.rapmedixuser.activity.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.Homevisitadapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.Callbackreq;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
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
    private String type, id, mobile;
    private Dialog dialog;

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
        gridhomevisit = view.findViewById(R.id.grid_doctors);

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
            switch (requestId) {
                case 1:


                    homevisitresponse hvrespo = Common.getSpecificDataObject(objectResponse, homevisitresponse.class);
                    Gson gson = new Gson();
                    if (hvrespo.status.equals("success")) {
                        visitlists = (ArrayList<homevisitresponse.Visitlist>) hvrespo.visitlistList;
                        homevisitadapter = new Homevisitadapter(getActivity(), visitlists);
                        gridhomevisit.setAdapter(homevisitadapter);
                        gridhomevisit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                type = "4";
                                id = visitlists.get(i).id;
                                mobile = new DataBase_Helper(getActivity()).getUserMobileNumber("1");

                                dialog = new Dialog(getActivity());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.requst_callback);

                                Button callBack = dialog.findViewById(R.id.callback_btn);
                                callBack.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        callbackmethod();

                                    }
                                });
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                Window window = dialog.getWindow();
                                lp.copyFrom(window.getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                window.setAttributes(lp);
                                dialog.show();
//                        callbackmethod();

                                Toast.makeText(getActivity(), visitlists.get(i).homeserviceName, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Animation slide_down = AnimationUtils.loadAnimation(getActivity(),
                                R.anim.right_left);
                        gridhomevisit.setAnimation(slide_down);

                    } else
                        Toast.makeText(getActivity(), "" + hvrespo.status, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Simpleresponse simpleresponse = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
                    gson = new Gson();
                    if (simpleresponse.status.equals("success")) {
                        Toast.makeText(getActivity(), "" + simpleresponse.status, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "" + simpleresponse.status, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    break;

            }
        }

    }

    private void callbackmethod() {
        Callbackreq callbackreq = new Callbackreq();
        callbackreq.type = type;
        callbackreq.serviceId = id;
        callbackreq.callbackMobile = mobile;

        try {
            obj = Class.forName(Callbackreq.class.getName()).cast(callbackreq);
            Log.i("obj", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 2, "/user/requestCallback", true);
    }
}
