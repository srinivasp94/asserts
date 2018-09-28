package com.example.pegasys.rapmedixuser.activity.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.DiagnosticsCategoriesAdapter;
import com.example.pegasys.rapmedixuser.activity.adapters.LabsAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.newactivities.AppointmentActivity;
import com.example.pegasys.rapmedixuser.activity.pojo.Callbackreq;
import com.example.pegasys.rapmedixuser.activity.pojo.Categories;
import com.example.pegasys.rapmedixuser.activity.pojo.CategoriesList;
import com.example.pegasys.rapmedixuser.activity.pojo.LabList;
import com.example.pegasys.rapmedixuser.activity.pojo.LabModel;
import com.example.pegasys.rapmedixuser.activity.pojo.LabReq;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by BhargavaSunkara on 27-Dec-17.
 */

public class Diagnostics extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;

    /*LinearLayout cate, labs;
    FrameLayout frame;
    LinearLayout l_cate, l_labs;

    RecyclerView rv_labs;
    RecyclerView.LayoutManager layoutManager;
    private Object obj;
    ArrayList<CategoriesList> mCategoriesList = new ArrayList<>();
    ArrayList<LabList> mlabList = new ArrayList<>();
    DiagnosticsCategoriesAdapter categoriesAdapter;*/
    private double lat, longg;
    SharedPreferences sp;
    SharedPreferences ref_code_sp;
    public static final String pref = "Location";
    private long latit;
    private long longitude;

    private String type, id, mobile;
    private Dialog dialog;
    private LabsAdapter mlabsAdapter;
    private int[] tabicons = {
            R.drawable.subcat_selector,
            R.drawable.subcat_selector2
    };


//    BottomNavigationItemView navigationItemView;

    public Diagnostics() {
    }


    public static Diagnostics newInstance(Double latitude, Double longitide) {

        Bundle args = new Bundle();
        args.putDouble("LATITUDE", latitude);
        args.putDouble("LONGITUDE", longitide);
        Diagnostics fragment1 = new Diagnostics();
        fragment1.setArguments(args);
        return fragment1;
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
        View view = inflater.inflate(R.layout.diagnosticsfragmentlayout, container, false);

        viewPager = view.findViewById(R.id.viewpager1);
        tabLayout = view.findViewById(R.id.tl_diagnosetablayout);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setuptabicons();

/*

        cate = (LinearLayout) view.findViewById(R.id.cate);
        labs = (LinearLayout) view.findViewById(R.id.labs);
        cate.setBackgroundColor(Color.parseColor("#ed3237"));
//        frame = (FrameLayout) view.findViewById(R.id.fm);
        l_cate = (LinearLayout) view.findViewById(R.id.ll_cate);
        l_labs = (LinearLayout) view.findViewById(R.id.ll_Labs);
//        gv_cate = (GridView) view.findViewById(R.id.list_diag_cate);
        rv_labs = (RecyclerView) view.findViewById(R.id.rv_diag_labs);

        layoutManager = new LinearLayoutManager(getActivity());
        rv_labs.setLayoutManager(layoutManager);

        obj = new Object();

        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/getDiagnosticsCategory_service", true);
*/

        sp = getActivity().getSharedPreferences(pref, MODE_PRIVATE);
        ref_code_sp = getActivity().getSharedPreferences("category", MODE_PRIVATE);
        latit = sp.getLong("lattitude", 0L);
        longitude = sp.getLong("longitude", 0L);
        //            obj = Class.forName(loginRequsest.class.getName()).cast(logRequest);
      /*  LabReq req = new LabReq();
        req.latitude = String.valueOf(latit);
        req.longitude = String.valueOf(longitude);

        try {
            obj = Class.forName(LabReq.class.getName()).cast(req);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 2, "/webservices/getDiagnosticsLabs_service", true);


        cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l_cate.setVisibility(View.VISIBLE);
                l_labs.setVisibility(View.GONE);

                labs.setBackgroundColor(0);
                cate.setBackgroundColor(Color.parseColor("#ed3237"));


                gv_cate.setAdapter(new DiagnosticsCategoriesAdapter(getActivity(), mCategoriesList));
                gv_cate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        type = "6";
                        id = mCategoriesList.get(i).id;
                        mobile = new DataBase_Helper(getActivity()).getUserMobileNumber("1");
                        dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.requst_callback);

                        Button callBack = (Button) dialog.findViewById(R.id.callback_btn);
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
                    }
                });
            }
        });
        labs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l_cate.setVisibility(View.GONE);
                l_labs.setVisibility(View.VISIBLE);
                labs.setBackgroundColor(Color.parseColor("#ed3237"));
                cate.setBackgroundColor(0);
                setrAdapter();
            }
        });*/
        return view;
    }

    private void setuptabicons() {
        tabLayout.getTabAt(0).setIcon(tabicons[0]);
        tabLayout.getTabAt(1).setIcon(tabicons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putDouble("LATITUDE", lat);
        bundle.putDouble("LONGITUDE", longg);

        CategoriesFragment categoriesFragment = new CategoriesFragment();
        categoriesFragment.setArguments(bundle);
        adapter.addFragment(categoriesFragment, "Categories");

        LabsFragment labsFragment = new LabsFragment();
        labsFragment.setArguments(bundle);
        adapter.addFragment(labsFragment, "Labs");

        viewPager.setAdapter(adapter);
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



//    @Override
//    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
//        if (objectResponse == null || objectResponse.equals("")) {
//            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
//        } else {
//            switch (requestId) {
//                case 1:
//
//                    Categories mCategories = Common.getSpecificDataObject(objectResponse, Categories.class);
//                    Gson gson = new Gson();
//                    if (mCategories.status.equals("success")) {
//                        mCategoriesList = (ArrayList<CategoriesList>) mCategories.mCategoriesList;
//                        gv_cate.setAdapter(new DiagnosticsCategoriesAdapter(getActivity(), mCategoriesList));
//                        gv_cate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                            }
//                        });
//                        Animation slide_down = AnimationUtils.loadAnimation(getActivity(),
//                                R.anim.right_left);
//                        gv_cate.setAnimation(slide_down);
//
//                    } else {
//                        Toast.makeText(getActivity(), mCategories.status, Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case 2:
//                    LabModel model = Common.getSpecificDataObject(objectResponse, LabModel.class);
//                    gson = new Gson();
//                    if (model.status.equals("success")) {
//                        mlabList = (ArrayList<LabList>) model.mLabs;
//                        setrAdapter();
//
//                    }
//
//                    break;
//                case 3:
//                    Simpleresponse simpleresponse = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
//                    gson = new Gson();
//                    if (simpleresponse.status.equals("success")) {
//                        Toast.makeText(getActivity(), "" + simpleresponse.status, Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getActivity(), "" + simpleresponse.status, Toast.LENGTH_SHORT).show();
//                    }
//                    dialog.dismiss();
//                    break;
//            }
//        }
//    }

//    private void setrAdapter() {
//        mlabsAdapter = new LabsAdapter(getActivity(), mlabList);
//        rv_labs.setAdapter(new LabsAdapter(getActivity(), mlabList));
//        mlabsAdapter.setOnItemClickListener(new LabsAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(getActivity(), "6", Toast.LENGTH_SHORT).show();
//                type = "6";
//                id = mlabList.get(position).id;
//                mobile = new DataBase_Helper(getActivity()).getUserMobileNumber("1");
//                dialog = new Dialog(getActivity());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.requst_callback);
//
//                Button callBack = (Button) dialog.findViewById(R.id.callback_btn);
//                callBack.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callbackmethod();
//
//                    }
//                });
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                Window window = dialog.getWindow();
//                lp.copyFrom(window.getAttributes());
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                window.setAttributes(lp);
//                dialog.show();
//            }
//        });
//        Animation slide_down = AnimationUtils.loadAnimation(getActivity(),
//                R.anim.right_left);
//        rv_labs.setAnimation(slide_down);
//    }

//    private void callbackmethod() {
//        Callbackreq callbackreq = new Callbackreq();
//        callbackreq.type = type;
//        callbackreq.serviceId = id;
//        callbackreq.callbackMobile = mobile;
//
//        try {
//            obj = Class.forName(Callbackreq.class.getName()).cast(callbackreq);
//            Log.i("obj", obj.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        new RetrofitRequester(this).callPostServices(obj, 3, "/user/requestCallback", true);
//    }

}
