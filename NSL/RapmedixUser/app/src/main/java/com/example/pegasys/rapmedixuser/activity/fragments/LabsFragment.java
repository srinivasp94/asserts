package com.example.pegasys.rapmedixuser.activity.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.LabsAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.Callbackreq;
import com.example.pegasys.rapmedixuser.activity.pojo.LabList;
import com.example.pegasys.rapmedixuser.activity.pojo.LabModel;
import com.example.pegasys.rapmedixuser.activity.pojo.LabReq;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pegasys on 2/5/2018.
 */

public class LabsFragment extends Fragment implements RetrofitResponseListener {
    private double lat, longg;
    private View view;
    private RecyclerView rv_labs;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    private Object obj;
    private ArrayList<LabList> mlabList = new ArrayList<>();
    private LabsAdapter mlabsAdapter;
    private SharedPreferences sp;

    private SharedPreferences ref_code_sp;
    public static final String pref = "Location";
    private long latit;
    private long longitude;
    private String type;
    private String id;
    private String mobile;
    private Dialog dialog;


    public LabsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            lat = getArguments().getDouble("LATITUDE");
            longg = getArguments().getDouble("LONGITUDE");

            Log.i("Location ", lat + " and " + longg);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.labslayout, container, false);

        initComponents();
        return view;
    }

    private void initComponents() {
        setReferences_ids();
        setonClickListners();
        requestToLabsWS();
    }

    private void requestToLabsWS() {
        sp = getActivity().getSharedPreferences(pref, MODE_PRIVATE);
        ref_code_sp = getActivity().getSharedPreferences("category", MODE_PRIVATE);
        latit = sp.getLong("lattitude", 0L);
        longitude = sp.getLong("longitude", 0L);

        LabReq req = new LabReq();
        req.latitude = String.valueOf(latit);
        req.longitude = String.valueOf(longitude);

        try {
            obj = Class.forName(LabReq.class.getName()).cast(req);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/getDiagnosticsLabs_service", true);

    }

    private void setonClickListners() {

    }

    private void setReferences_ids() {
        rv_labs = view.findViewById(R.id.rv_diag_labs);
        rv_labs.setLayoutManager(layoutManager);

    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    LabModel model = Common.getSpecificDataObject(objectResponse, LabModel.class);
                    Gson gson = new Gson();
                    if (model.status.equals("success")) {
                        mlabList = (ArrayList<LabList>) model.mLabs;
                        setLabsAdapterTorecyclerview();

                    }
                    break;
                case 3:
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

    private void setLabsAdapterTorecyclerview() {

        mlabsAdapter = new LabsAdapter(getActivity(), mlabList);
        rv_labs.setAdapter(mlabsAdapter);
        mlabsAdapter.setOnItemClickListener(new LabsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                type = "6";
                id = mlabList.get(position).id;
                mobile = new DataBase_Helper(getActivity()).getUserMobileNumber("1");
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.requst_callback);

                Button callBack = dialog.findViewById(R.id.callback_btn);
                callBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requesatcallbackmethod();

                    }
                });
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                dialog.show();
            }
        });
        Animation slide_down = AnimationUtils.loadAnimation(getActivity(),
                R.anim.right_left);
        rv_labs.setAnimation(slide_down);
    }

    private void requesatcallbackmethod() {
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
        new RetrofitRequester(this).callPostServices(obj, 3, "/user/requestCallback", true);
    }
}
