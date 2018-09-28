package com.example.pegasys.rapmedixuser.activity.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.pegasys.rapmedixuser.activity.adapters.familyAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.newactivities.FamilymemberActivity;
import com.example.pegasys.rapmedixuser.activity.pojo.DeleteFamily;
import com.example.pegasys.rapmedixuser.activity.pojo.Familydatum;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
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
    TextView textView_nodata;
    RecyclerView.LayoutManager layoutManager;
    DataBase_Helper dataBase_helper;
    ArrayList<Familydatum> list = new ArrayList<>();
    Object obj;
    familyAdapter adapter;
    private int page;

    public Familymemberfragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    public static Familymemberfragment newInstance(int page, String title) {
        Familymemberfragment fragmentFirst = new Familymemberfragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_recyclerview, container, false);
        dataBase_helper = new DataBase_Helper(getActivity());
        mRecycle = view.findViewById(R.id.rv_add);
        textView_nodata = view.findViewById(R.id.text_nodata);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecycle.setLayoutManager(layoutManager);

        serviceCall();
        return view;
    }

    private void serviceCall() {
        userIdreq idreq = new userIdreq();
        idreq.userId = dataBase_helper.getUserId("1");

        try {
            obj = Class.forName(userIdreq.class.getName()).cast(idreq);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "/user/get_family_members", true);
    }
//another way

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    familydata mFamilydata = Common.getSpecificDataObject(objectResponse, familydata.class);
                    Gson gson = new Gson();
                    if (mFamilydata.status.equals("success")) {
                        list = (ArrayList<Familydatum>) mFamilydata.familydata;
                        if (list.size() == 0) {
                            textView_nodata.setVisibility(View.VISIBLE);
                            textView_nodata.setText(R.string.no_family_member_added_yet);
                        }
                       /* if (list.size() < 0) {
                            Toast.makeText(getActivity(), "Data Count is " + list.size(), Toast.LENGTH_SHORT).show();
                        }*/
                        setdataAdapter();

                    } else {
                        list = new ArrayList<>();
                        if (list.size() == 0) {
                            textView_nodata.setVisibility(View.VISIBLE);
                            textView_nodata.setText(R.string.no_family_member_added_yet);
                        }
                        setdataAdapter();
                        Toast.makeText(getActivity(), " " + mFamilydata.status, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:

                    Simpleresponse simpleresponse = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
                    gson = new Gson();
                    //   if (simpleresponse.status.equalsIgnoreCase("1")) { // Problem here check later

                    Toast.makeText(getActivity(), "Family member successfully deleted.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), FamilymemberActivity.class);
                    // startActivity(intent);
                    serviceCall();

                  /*  } else {
                        Toast.makeText(getActivity(), "Family member not deleted.", Toast.LENGTH_SHORT).show();
                    }*/
                    break;
                default:

            }
        }

    }
// Ok Bye
    //tq sir

    private void setdataAdapter() {

        adapter = new familyAdapter(getActivity(), list);
        mRecycle.setAdapter(adapter);

        adapter.setOnItemClickListener(new familyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Do you want to cancel this family member?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DeleteFamily df = new DeleteFamily();
                        df.id = list.get(position).id;
                        df.userId = list.get(position).userId;

                        try {
                            obj = Class.forName(DeleteFamily.class.getName()).cast(df);
                            Log.d("obj", obj.toString());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        new RetrofitRequester(Familymemberfragment.this).callPostServices(obj, 2, "/user/deletefamilymember_service", true);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialogBuilder.create();
                alertDialogBuilder.show();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //serviceCall();
    }


}
