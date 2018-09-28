package com.example.pegasys.rapmedixuser.activity.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.newactivities.FamilymemberActivity;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.addMember;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by pegasys on 12/21/2017.
 */

public class Addfamilymemberfragment extends Fragment implements RetrofitResponseListener {
    private Button Above18, AddMember, Below18;
    private EditText Mobile, Name, aEmail;
    private TextView Relation, Birthday;
    private ArrayList<String> strRelation = new ArrayList<>();
    private String dattte;
    private LinearLayout layoutPhn, layoutMail;
    private int count = 2;
    private DataBase_Helper db;
    private Object obj;
    private String mname, mbirth, mreltion;
    private int page;

    public Addfamilymemberfragment() {
    }

    public static Addfamilymemberfragment newInstance(int page, String title) {
        Addfamilymemberfragment fragmentsecond = new Addfamilymemberfragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentsecond.setArguments(args);
        return fragmentsecond;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addfamilymember_layout, container, false);
        db = new DataBase_Helper(getActivity());
        Name = view.findViewById(R.id.member_name);
        Mobile = view.findViewById(R.id.member_mobile);
        aEmail = view.findViewById(R.id.member_email);
        Relation = view.findViewById(R.id.member_relation);
        Birthday = view.findViewById(R.id.member_birthday);
        Above18 = view.findViewById(R.id.above);
        Below18 = view.findViewById(R.id.below);
        AddMember = view.findViewById(R.id.member_add);
        layoutPhn = view.findViewById(R.id.ll_mobile);
        layoutMail = view.findViewById(R.id.ll_email);

        Above18.setBackgroundColor(Color.parseColor("#ed3237"));
        Above18.setTextColor(Color.parseColor("#ffffff"));

        strRelation.add("Wife");
        strRelation.add("Husband");
        strRelation.add("Son");
        strRelation.add("Daughter");
        strRelation.add("Father");
        strRelation.add("Mother");
        strRelation.add("Grand Father");
        strRelation.add("Grand Mother");
        strRelation.add("Sister");
        strRelation.add("Brother");
        strRelation.add("Father In Law");
        strRelation.add("Mother In Law");
        strRelation.add("Sister In Law");
        strRelation.add("Brother In Law");
        strRelation.add("Cousin");
        strRelation.add("Other");

        Birthday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Birthday.setText(new StringBuilder().append(dayOfMonth).append("-").append(monthOfYear + 1).append("-").append(year).append(" "));
                        dattte = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        Above18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strRelation.clear();
                strRelation.add("Wife");
                strRelation.add("Husband");
                strRelation.add("Son");
                strRelation.add("Daughter");
                strRelation.add("Father");
                strRelation.add("Mother");
                strRelation.add("Grand Father");
                strRelation.add("Grand Mother");
                strRelation.add("Sister");
                strRelation.add("Brother");
                strRelation.add("Father In Law");
                strRelation.add("Mother In Law");
                strRelation.add("Sister In Law");
                strRelation.add("Brother In Law");
                strRelation.add("Cousin");
                strRelation.add("Other");

                count = 2;
                Mobile.setText("");
                Name.setText("");
                aEmail.setText("");
                Birthday.setText("");
                Relation.setText("");
                layoutPhn.setVisibility(View.VISIBLE);
                layoutMail.setVisibility(View.VISIBLE);
                Below18.setTextColor(Color.parseColor("#707070"));
                Below18.setBackgroundColor(0);
                Above18.setBackgroundColor(Color.parseColor("#ed3237"));
                Above18.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        Below18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 1;
                strRelation.clear();

                strRelation.add("Son");
                strRelation.add("Daughter");
                strRelation.add("Sister");
                strRelation.add("Brother");
                strRelation.add("Cousin");
                strRelation.add("Other");
                Name.setText("");
                Birthday.setText("");
                Relation.setText("");
                layoutPhn.setVisibility(View.GONE);
                layoutMail.setVisibility(View.GONE);
                Below18.setBackgroundColor(Color.parseColor("#ed3237"));
                Below18.setTextColor(Color.parseColor("#ffffff"));
                Above18.setTextColor(Color.parseColor("#707070"));
                Above18.setBackgroundColor(0);
            }
        });
        this.Relation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] searchitems = strRelation.toArray(new String[strRelation.size()]);
                final Dialog alertDialog = new Dialog(getActivity());
                alertDialog.getWindow();
                alertDialog.setCancelable(true);
                //  alertDialog.getWindow().setBackgroundDrawableResource(17170445);
                alertDialog.requestWindowFeature(1);
                alertDialog.getWindow().setGravity(17);
                alertDialog.setContentView(R.layout.person_details);
                ListView lv = alertDialog.findViewById(R.id.person_details_list);
                ArrayAdapter ad = new ArrayAdapter(getActivity(), R.layout.spinner_item, searchitems);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Relation.setText(searchitems[position]);
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
                lv.setAdapter(ad);
                alertDialog.show();
            }
        });

        AddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyData();
            }
        });
        return view;
    }

    private void verifyData() {
        Log.i("Birthday", Birthday.getText().toString());
        Log.i("Birthday", Relation.getText().toString());
        Log.i("Birthday", Name.getText().toString());

        mname = Name.getText().toString();
        mreltion = Relation.getText().toString();
        mbirth = Birthday.getText().toString().trim();
        if (count == 2) {

            addMember member = new addMember();
            member.userId = new DataBase_Helper(getActivity()).getUserId("1");
            member.membername = mname;
            member.relationship = mreltion;
            member.birthdate = mbirth;
            member.ageType = "2";
            member.emailid = "";
            member.mobilenumber = Mobile.getText().toString().trim();

            try {
                obj = Class.forName(addMember.class.getName()).cast(member);
                Log.d("obj", new Gson().toJson(obj));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            new RetrofitRequester(this).callPostServices(obj, 1, "/user/addfamily_member_service", true);
        }
        if (count == 1) {

            addMember member = new addMember();
            member.userId = new DataBase_Helper(getActivity()).getUserId("1");
            member.membername = mname;
            member.relationship = mreltion;
            member.birthdate = mbirth;
            member.ageType = "1";
            member.emailid = "";
            member.mobilenumber = Mobile.getText().toString().trim();

            try {
                obj = Class.forName(addMember.class.getName()).cast(member);
                Log.d("obj", new Gson().toJson(obj));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            new RetrofitRequester(this).callPostServices(obj, 1, "/user/addfamily_member_service", true);
        }

    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            Simpleresponse response1 = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
            Gson gson = new Gson();
            if (response1.status.equals("success")) {
                Toast.makeText(getActivity(), "" + response1.status, Toast.LENGTH_SHORT).show();
                Name.setText("");
                Mobile.setText("");
                aEmail.setText("");
                Birthday.setText("");
                Relation.setText("");
                startActivity(new Intent(getActivity(), FamilymemberActivity.class));

            } else {
                Toast.makeText(getActivity(), "" + response1.status, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}
