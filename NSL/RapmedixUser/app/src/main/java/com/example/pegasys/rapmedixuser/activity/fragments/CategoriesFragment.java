package com.example.pegasys.rapmedixuser.activity.fragments;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.pegasys.rapmedixuser.activity.Home_page;
import com.example.pegasys.rapmedixuser.activity.adapters.DiagnosticsCategoriesAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.newactivities.DiagnosticsPageSubCatActivity;
import com.example.pegasys.rapmedixuser.activity.pojo.Callbackreq;
import com.example.pegasys.rapmedixuser.activity.pojo.Categories;
import com.example.pegasys.rapmedixuser.activity.pojo.CategoriesList;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.DiagnosticIdRequest;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.DiagnosticsSubcatList;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by pegasys on 2/5/2018.
 */

public class CategoriesFragment extends Fragment implements RetrofitResponseListener {

    private GridView gv_cate;
    private Object obj;
    private ArrayList<CategoriesList> mCategoriesList = new ArrayList<>();
    private View view;
    Home_page homePage;

    private double lat, longg;
    private String type;
    private String id;
    private String mobile;
    private Dialog dialog;

    ArrayList<String> searchname = new ArrayList<>();
    private ArrayList<String> idlist=new ArrayList<>();

    public CategoriesFragment() {
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
        view = inflater.inflate(R.layout.categorieslayout, container, false);

        initComponents();
        return view;
    }

    private void initComponents() {
        setReferences_ids();
        setonClickListners();
        requestTocategoriesWS();
    }

    private void requestTocategoriesWS() {
        obj = new Object();

        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/getDiagnosticsCategory_service", true);
    }

    private void setonClickListners() {

    }

    private void setReferences_ids() {
        gv_cate = view.findViewById(R.id.list_diag_cate);
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    Categories mCategories = Common.getSpecificDataObject(objectResponse, Categories.class);
                    Gson gson = new Gson();
                    if (mCategories.status.equals("success")) {
                        mCategoriesList = (ArrayList<CategoriesList>) mCategories.mCategoriesList;
                        for (int i=0;i<mCategoriesList.size();i++) {
                            searchname.add(mCategoriesList.get(i).diagnosticsName);
                            idlist.add(mCategoriesList.get(i).id);
                        }
                        homePage = (Home_page)getActivity();
                        homePage.setSearchAdaperDiagnostics(searchname,idlist);
                        setgridviewOfcategories();

                    } else {
                        Toast.makeText(getActivity(), mCategories.status, Toast.LENGTH_SHORT).show();
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
                case 4:
                    DiagnosticsSubcatList subcatList = Common.getSpecificDataObject(objectRequest,DiagnosticsSubcatList.class);

                    break;
            }
        }
    }

    private void setgridviewOfcategories() {
        gv_cate.setAdapter(new DiagnosticsCategoriesAdapter(getActivity(), mCategoriesList));
        gv_cate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               /* type = "6";
                id = mCategoriesList.get(i).id;
                mobile = new DataBase_Helper(getActivity()).getUserMobileNumber("1");
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.requst_callback);

                Button callBack = dialog.findViewById(R.id.callback_btn);
                callBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestcallbackWS();

                    }
                });
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                dialog.show();*/
                //---------------------------------------
                Intent intent = new Intent(getActivity(), DiagnosticsPageSubCatActivity.class);
                intent.putExtra("ID",mCategoriesList.get(i).id);
                startActivity(intent);
            }
        });
        Animation slide_down = AnimationUtils.loadAnimation(getActivity(),
                R.anim.right_left);
        gv_cate.setAnimation(slide_down);
    }

    private void requsetSubCategoriesDiagnosticsWS(int i) {
        DiagnosticIdRequest diagnosticIdRequest = new DiagnosticIdRequest();
        diagnosticIdRequest.diagnostic_id = mCategoriesList.get(i).id;
        try {
            obj = Class.forName(DiagnosticIdRequest.class.getName()).cast(diagnosticIdRequest);
            Log.i("obj", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 4, "/index/searchdiagnostics_based_category_service", true);
    }

    private void requestcallbackWS() {
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
