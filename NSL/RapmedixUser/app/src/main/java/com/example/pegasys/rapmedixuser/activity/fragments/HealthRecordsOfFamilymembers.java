package com.example.pegasys.rapmedixuser.activity.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.AllHealthrecordsGridviewAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.newactivities.HealthrecordsIndividualActivity;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.healthrecords.AllHealthRecords;
import com.example.pegasys.rapmedixuser.activity.pojo.healthrecords.BelowfamilyDetail;
import com.example.pegasys.rapmedixuser.activity.pojo.healthrecords.CreatepinSuccess;
import com.example.pegasys.rapmedixuser.activity.pojo.healthrecords.FamilyDetail;
import com.example.pegasys.rapmedixuser.activity.pojo.healthrecords.HealthrecordsService;
import com.example.pegasys.rapmedixuser.activity.pojo.healthrecords.ProfilepinCheck;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by pegasys on 2/7/2018.
 */

public class HealthRecordsOfFamilymembers extends Fragment implements View.OnClickListener, RetrofitResponseListener {
    private View view;
    private TextView txt_createpin, txt_title, txt_pin;
    private LinearLayout linearLayout;
    private GridView gv_familyHealthrecords;
    private EditText editText_pin;
    private Button btn_submit;
    private Object obj;

    private ArrayList<AllHealthRecords> mAllHealthRecordses = new ArrayList<>();
    private ArrayList<FamilyDetail> mfamilyDetails = new ArrayList<>();
    private ArrayList<BelowfamilyDetail> mbelowfamilyDetails = new ArrayList<>();
    private String s_profile;
    private AllHealthrecordsGridviewAdapter gridviewAdapter;
    AllHealthRecords records = new AllHealthRecords();
    private DataBase_Helper helper;
    private View Dialogview;
    private Dialog dialog;


    public HealthRecordsOfFamilymembers() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.healthrecords_members, container, false);
        initComoponents();
        return view;

    }

    private void initComoponents() {
        setReferences();
        setonclicklistners();
        seeHealthrecordsWS();
    }

    private void setReferences() {
        txt_createpin = view.findViewById(R.id.tv_create_pin);
        txt_pin = view.findViewById(R.id.tv_profilepin);
        gv_familyHealthrecords = view.findViewById(R.id.grid_familyhealthrecords);
        linearLayout = view.findViewById(R.id.ll);
    }

    private void setonclicklistners() {
        txt_createpin.setOnClickListener(this);

    }

    private void seeHealthrecordsWS() {
        helper = new DataBase_Helper(getActivity());
        userIdreq mIdRequset = new userIdreq();
        mIdRequset.userId = new DataBase_Helper(getActivity()).getUserId("1");
        try {
            obj = Class.forName(userIdreq.class.getName()).cast(mIdRequset);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "/user/healthrecords_service", true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_create_pin:
                createpinAlertDialogShow();
                break;
        }
    }

    private void createpinAlertDialogShow() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.checkingpin_dialog, null);
        builder.setView(v);
//        setDialogReferences();
        txt_title = v.findViewById(R.id.tv_title_dialog);
        txt_title.setText("Creating Pin");
        editText_pin = v.findViewById(R.id.edt_pin);
        btn_submit = v.findViewById(R.id.btn_dialog_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCreateProfilePinWS();
            }
        });
        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }


    private void callCreateProfilePinWS() {

        ProfilepinCheck createPin = new ProfilepinCheck();
        createPin.userId = new DataBase_Helper(getActivity()).getUserId("1");
        createPin.profilePin = editText_pin.getText().toString().trim();

        try {
            obj = Class.forName(ProfilepinCheck.class.getName()).cast(createPin);
            Log.i("obj", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 3, "/user/createprofilepin_service", true);
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    mAllHealthRecordses.clear();

                    HealthrecordsService mhealthrecordsService = Common.getSpecificDataObject(objectResponse, HealthrecordsService.class);
                    Gson gson = new Gson();
                    if (mhealthrecordsService.userdetails.profilePin != null && !mhealthrecordsService.userdetails.profilePin.equals("")) {
                        linearLayout.setVisibility(View.VISIBLE);
                        txt_pin.setText("" + mhealthrecordsService.userdetails.profilePin.toString());
                        txt_createpin.setText("" + "Edit Pin");
                    } else {
                        linearLayout.setVisibility(View.INVISIBLE);
                        txt_createpin.setText("" + "Create Pin");
                    }
                    records.setUser_id(mhealthrecordsService.userdetails.userId);
                    records.setName(mhealthrecordsService.userdetails.name);
                    records.setAdviser_id(mhealthrecordsService.userdetails.userId);
                    mAllHealthRecordses.add(records);
//                    Log.d("mList1", mAllHealthRecordses.toString());

                    mbelowfamilyDetails = (ArrayList<BelowfamilyDetail>) mhealthrecordsService.belowfamilyDetails;
                    mfamilyDetails = (ArrayList<FamilyDetail>) mhealthrecordsService.familyDetails;

                    for (int i = 0; i < mbelowfamilyDetails.size(); i++) {
                        records = new AllHealthRecords();
                        if (mbelowfamilyDetails.get(i).userId != null || mbelowfamilyDetails.get(i).userId.equals("")) {
                            records.setUser_id(mbelowfamilyDetails.get(i).userId);
                            records.setAdviser_id(mbelowfamilyDetails.get(i).userId);
                        }
                        if (mbelowfamilyDetails.get(i).name != null || mbelowfamilyDetails.get(i).name.equals("")) {
                            records.setName(mbelowfamilyDetails.get(i).name);
                        }
                        if (mbelowfamilyDetails.get(i).mobile != null || mbelowfamilyDetails.get(i).mobile.equals("")) {
                            records.setMobile(mbelowfamilyDetails.get(i).mobile);
                        }
                        if (mbelowfamilyDetails.get(i).status != null || mbelowfamilyDetails.get(i).status.equals("")) {
                            records.setStatus(mbelowfamilyDetails.get(i).status);
                        }
                        mAllHealthRecordses.add(records);
//                        Log.d("mList below", mAllHealthRecordses.toString());
                    }
                    for (int i = 0; i < mfamilyDetails.size(); i++) {
                        records = new AllHealthRecords();
                        if (mfamilyDetails.get(i).userId != null || mfamilyDetails.get(i).userId.equals("")) {
                            records.setUser_id(mfamilyDetails.get(i).userId);
                            records.setAdviser_id(mfamilyDetails.get(i).userId);
                        }
                        if (mfamilyDetails.get(i).name != null || mfamilyDetails.get(i).name.equals("")) {
                            records.setName(mfamilyDetails.get(i).name);
                        }
                        if (mfamilyDetails.get(i).mobile != null || mfamilyDetails.get(i).mobile.equals("")) {
                            records.setMobile(mfamilyDetails.get(i).mobile);
                        }
                        if (mfamilyDetails.get(i).status != null || mfamilyDetails.get(i).status.equals("")) {
                            records.setStatus(mfamilyDetails.get(i).status);
                        }
                        if (mfamilyDetails.get(i).profilePic != null || mfamilyDetails.get(i).profilePic.equals("")) {
                            s_profile = mfamilyDetails.get(i).profilePic;
                            records.setProfile_pic(s_profile);
                        }
                        mAllHealthRecordses.add(records);
//                        Log.d("mList above", mAllHealthRecordses.toString());
                    }
                    setAllrecordsAdapter();
                    break;
                case 2:
                    Simpleresponse mSimpleresponse = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
                    if (mSimpleresponse.status.equals("success")) {
                        dialog.dismiss();
                        ProfilepinCheck check = Common.getSpecificDataObject(objectRequest, ProfilepinCheck.class);
                        Intent intent = new Intent(getActivity(), HealthrecordsIndividualActivity.class);
                        intent.putExtra("UserId", check.userId);
                        intent.putExtra("AdviserId", check.adviserId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), " " + mSimpleresponse.status, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    CreatepinSuccess success = Common.getSpecificDataObject(objectResponse, CreatepinSuccess.class);
                    if (success.status.equals("success")) {
                        linearLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Your profile pin has been updated  " + success.status + "fully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        txt_pin.setText(""+ success.profile_pin);
                    } else {
                        dialog.setCancelable(true);
                        txt_title.setText(success.status);
                        txt_title.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    break;

            }
        }
    }

    private void setAllrecordsAdapter() {
        gridviewAdapter = new AllHealthrecordsGridviewAdapter(getActivity(), mAllHealthRecordses);
        gv_familyHealthrecords.setAdapter(gridviewAdapter);
        gv_familyHealthrecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (mAllHealthRecordses.get(i).getUser_id().equals(helper.getUserId("1"))) {
                    Intent intent = new Intent(getActivity(), HealthrecordsIndividualActivity.class);
                    intent.putExtra("UserId", mAllHealthRecordses.get(i).getUser_id());
                    intent.putExtra("AdviserId", mAllHealthRecordses.get(i).getAdviser_id());
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    Dialogview = inflater.inflate(R.layout.checkingpin_dialog, null);
                    builder.setView(Dialogview);
                    setDialogReferences();
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            submitPinDialog(i);
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
//                    builder.show();
                    Log.i("user and advisor", mAllHealthRecordses.get(i).getUser_id() + "  " + mAllHealthRecordses.get(i).getAdviser_id());
                }
            }
        });
    }

    private void submitPinDialog(int i) {

        String mPin = editText_pin.getText().toString();
        if (mPin.length() == 4) {
            callCheckProfilePinWS(i);
        } else {
            Toast.makeText(getActivity(), "Pin Lenght must be 4 Numbers", Toast.LENGTH_SHORT).show();
        }

    }

    private void callCheckProfilePinWS(int position) {
        ProfilepinCheck check = new ProfilepinCheck();
        check.userId = helper.getUserId("1");
        check.adviserId = mAllHealthRecordses.get(position).getAdviser_id();
        check.profilePin = editText_pin.getText().toString();

        try {
            obj = Class.forName(ProfilepinCheck.class.getName()).cast(check);
            Log.i("obj", obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 2, "/user/checkprofilepin_service", true);
    }

    private void setDialogReferences() {
        editText_pin = Dialogview.findViewById(R.id.edt_pin);
        btn_submit = Dialogview.findViewById(R.id.btn_dialog_submit);
    }
}

