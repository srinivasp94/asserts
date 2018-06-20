package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.Home_page;
import com.example.pegasys.rapmedixuser.activity.LoginActivity;
import com.example.pegasys.rapmedixuser.activity.fragments.Doctorsfrag;
import com.example.pegasys.rapmedixuser.activity.fragments.overView;
import com.example.pegasys.rapmedixuser.activity.pojo.DocIdReq;
import com.example.pegasys.rapmedixuser.activity.pojo.DocdetailsResponse;
import com.example.pegasys.rapmedixuser.activity.pojo.Doctordatail;
import com.example.pegasys.rapmedixuser.activity.pojo.Doctorworkingdatail;
import com.example.pegasys.rapmedixuser.activity.pojo.User;
import com.example.pegasys.rapmedixuser.activity.pojo.loginRequsest;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DoctorDescription extends AppCompatActivity implements RetrofitResponseListener {

    private TextView Name, Desig, Qualification, Rating, Experience;
    private LinearLayout Overview, Service, Review;
    private ImageView Propic;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Object obj, docidobj;

    ArrayList<Doctordatail> listdetail = new ArrayList<>();
    public static ArrayList<Doctorworkingdatail> listWork;
    String aboutUs;
    Doctorworkingdatail workingdatail;
    private String doc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_description);

        viewPager = (ViewPager) findViewById(R.id.viewpager2);
        tabLayout = (TabLayout) findViewById(R.id.tabs_cat);

        Intent intent = getIntent();
        doc_id = intent.getStringExtra("doctorId");

        Name = (TextView) findViewById(R.id.doctor_name);
        Desig = (TextView) findViewById(R.id.doctor_desig);
        Qualification = (TextView) findViewById(R.id.doctor_quli);
        Rating = (TextView) findViewById(R.id.doctor_rating);
        Experience = (TextView) findViewById(R.id.doctor_exp);
        Propic = (ImageView) findViewById(R.id.doctorimg);
        DocIdReq req = new DocIdReq();
        req.doctorId = doc_id;
        try {
            obj = Class.forName(DocIdReq.class.getName()).cast(req);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 1, "/index/doctordescription_service", true);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("DOCTORWORKINGDATAIL", listWork);
        bundle.putString("ABOUTUS", aboutUs);
        bundle.putString("DOC_ID",doc_id);
        bundle.putString("Doc_Name",listdetail.get(0).name);

        overView overView = new overView();
        overView.setArguments(bundle);
        adapter.addFragment(overView, "OverView");
        adapter.addFragment(new Doctorsfrag(), "Services");
        adapter.addFragment(new Doctorsfrag(), "Profile");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        listWork = new ArrayList<>();
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(DoctorDescription.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            DocdetailsResponse response = Common.getSpecificDataObject(objectResponse, DocdetailsResponse.class);
            Gson gson = new Gson();
            if (response.status.equals("success")) {
                listWork = (ArrayList<Doctorworkingdatail>) response.doctorworkingdatails;
                listdetail = (ArrayList<Doctordatail>) response.doctordatails;
                Name.setText(listdetail.get(0).name);
                Desig.setText(listdetail.get(0).specialisationName);
                Name.setText(listdetail.get(0).name);
                Qualification.setText(listdetail.get(0).degreeName);
                Experience.setText(listdetail.get(0).experience + "Years Experiance");

                Picasso.with(DoctorDescription.this).load("https://www.rapmedix.com/uploads/doctor_image/" +listdetail.get(0).profilePic).into(Propic);
                aboutUs = listdetail.get(0).aboutus;
                Log.i("ABOUTUS", aboutUs);

                setupViewPager(viewPager);

                tabLayout.setupWithViewPager(viewPager);


                workingdatail = new Doctorworkingdatail(DoctorDescription.this);
            } else {
                Toast.makeText(DoctorDescription.this, "" + response.status, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
