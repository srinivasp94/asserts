package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.DiagnosticssubcatAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.CentersResponse;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.DiagnosticIdRequest;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.DiagnosticsSubcatList;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.Diagnosticscenter;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.subCategoriesCentersRequest;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class DiagnosticsPageSubCatActivity extends AppCompatActivity implements RetrofitResponseListener, View.OnClickListener {

    private TextView txt_location, txt_itemsCount, txt_count;
    private ImageView img_iconlocation,backButton;
    private Button btn_proceed;
    private RelativeLayout layout_click, Root_relativeLayout;
    private RecyclerView rvSubcats;
    private String mId;
    private Object obj;
    private ArrayList<DiagnosticsSubcatList> mDiagnosticsSubcatLists = new ArrayList<>();
    private ArrayList<String> subservice_idList = new ArrayList<>();
    private RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
    private DiagnosticssubcatAdapter adapter;
    private DiagnosticsSubcatList mSubcat;

    private SharedPreferences sp;
    public static final String pref = "Location";
    private double lat, longg;
    private String mAddress;
    private StringBuilder stringBuilder;
    private ArrayList<String> ids = new ArrayList<>();

    private ArrayList<Diagnosticscenter> diagnosticscentersList = new ArrayList<>();
//    private ArrayList<Diagnosticscenter> cartDiagnosticscenters = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREFERENCE_NAME = "cartPrefer";

    private DataBase_Helper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics_page_sub_cat);

        sp = getSharedPreferences(pref, MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
//        editor = sharedPreferences.edit();

        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("stringObject", "");
//            DiagnosticsSubcatList obj = gson.fromJson(json, DiagnosticsSubcatList.class);

            /*Type type = new TypeToken<List< Diagnosticscenter >>() {}.getType();
            cartDiagnosticscenters = new Gson().fromJson(json, type);*/

//            Log.i("Listshared","" + cartDiagnosticscenters.size());
          /*  for (int i=0;i<cartDiagnosticscenters.size();i++) {
                String s = cartDiagnosticscenters.get(i).id;
                Log.i("sharID",s);
            }*/

        }



        if (sp != null) {
            sp = getSharedPreferences(pref, MODE_PRIVATE);
            lat = Double.longBitsToDouble(sp.getLong("lattitude", Double.doubleToLongBits(0.0)));
            longg = Double.longBitsToDouble(sp.getLong("longitude", Double.doubleToLongBits(0.0)));
            mAddress = sp.getString("address", null);
        }

        initComponents();
    }

    private void initComponents() {
        setReferences();
        setonclicklistners();
        getdatafromIntent();
        txt_location.setText(mAddress.replace("null", ""));

    }

    private void getdatafromIntent() {

        rvSubcats.setLayoutManager(manager);
        if (Constants.cartDiagnosticscenters != null && Constants.cartDiagnosticscenters.size()>0) {
            txt_count.setText("" + Constants.cartDiagnosticscenters.size());
        } else {
            txt_count.setText("0");
        }

        Intent intent = getIntent();
        if (intent != null && !intent.equals("")) {
            mId = intent.getStringExtra("ID");
        }

        DiagnosticIdRequest diagnosticIdRequest = new DiagnosticIdRequest();
        diagnosticIdRequest.diagnostic_id = mId;
        try {
            obj = Class.forName(DiagnosticIdRequest.class.getName()).cast(diagnosticIdRequest);
            Log.i("obj", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "/index/searchdiagnostics_based_category_service", true);

    }

    private void setReferences() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backButton = toolbar.findViewById(R.id.backbutton);
        txt_count = findViewById(R.id.txt_count);
        layout_click = findViewById(R.id.relative_layout);
        Root_relativeLayout = (RelativeLayout)findViewById(R.id.rootlayout);
        txt_location = findViewById(R.id.tv_subcat_locationname);
        txt_itemsCount = findViewById(R.id.tv_subcat_testcount);
        img_iconlocation = findViewById(R.id.iv_subcat_location);
        btn_proceed = findViewById(R.id.btn_add_to_cart);
        rvSubcats = findViewById(R.id.rv_subcatdiagnostics);
    }

    private void setonclicklistners() {
        btn_proceed.setOnClickListener(this);
        backButton.setOnClickListener(this);
        layout_click.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_to_cart:
                requestSubCategoriesCentersWS();
                break;
            case R.id.backbutton:
                finish();
                break;
            case R.id.relative_layout:
                cartpageSetup();
                break;
        }
    }

    private void requestSubCategoriesCentersWS() {
        if (Constants.cartDiagnosticscenters != null && Constants.cartDiagnosticscenters.size() > 0) {
            btn_proceed.setBackgroundColor(Color.RED);
//            Toast.makeText(DiagnosticsPageSubCatActivity.this, "Selected List Size " + Constants.cartDiagnosticscenters.size(), Toast.LENGTH_LONG).show();
            subCategoriesCentersRequest request = new subCategoriesCentersRequest();
            ids.clear();
            for (int i = 0; i < Constants.cartDiagnosticscenters.size(); i++) {
                Log.i("String", Constants.cartDiagnosticscenters.get(i).id);
                String s = Constants.cartDiagnosticscenters.get(i).id;
                ids.add(s);

                Log.i("StringArray", ids.toString());
            }
            request.userid= new DataBase_Helper(this).getUserId("1");
            request.subserviceId = ids.toString().replace("[", "").replace("]", "");
            request.latitude = lat;
            request.longitude = longg;
            request.location = mAddress;
            request.city = mAddress;

            try {
                obj = Class.forName(subCategoriesCentersRequest.class.getName()).cast(request);
                Log.i("obj", obj.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            new RetrofitRequester(this).callPostServices(obj, 2, "/index/get_diagnostics_centers_service", true);
        } else {
            btn_proceed.setBackgroundColor(Color.BLACK);
        }

    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null && objectResponse.equals("")) {
            Toast.makeText(DiagnosticsPageSubCatActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    Gson gson = new Gson();
                    try {
                        JSONArray array = new JSONArray(new Gson().toJson(objectResponse));

                        Log.i("array", array.toString());

//                        Iterator<String> keys = array.

                        for (int i = array.length() - 1; i >= 0; i--) {
                            mSubcat = new DiagnosticsSubcatList();
                            mSubcat.id = array.getJSONObject(i).getString("id");
                            mSubcat.diagnosticsId = array.getJSONObject(i).getString("diagnostics_id");
                            mSubcat.subserviceName = array.getJSONObject(i).getString("subservice_name");
                            mSubcat.homevisitAvaliable = array.getJSONObject(i).getString("homevisit_avaliable");
                            mDiagnosticsSubcatLists.add(mSubcat);
                            Log.i("mSubcat", mSubcat.toString());
                            Log.i("mDiagnosticsSubcatLists", mDiagnosticsSubcatLists.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("Exception", e.toString());

                    }
                    if (mDiagnosticsSubcatLists != null && mDiagnosticsSubcatLists.size() > 0) {

                        setRecyclerviewAdapter();
                    } else
                        btn_proceed.setVisibility(View.GONE);
//                    Toast.makeText(this, "No Diagnostics Available For now ", Toast.LENGTH_SHORT).show();
//                    finish();
                    break;
                case 2:
                    CentersResponse response = Common.getSpecificDataObject(objectResponse, CentersResponse.class);
                    gson = new Gson();
                    try {
                        diagnosticscentersList = (ArrayList<Diagnosticscenter>) response.diagnosticscenters;
                        if (diagnosticscentersList == null) {
                            Toast.makeText(this, "No Items Found Commonly", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(DiagnosticsPageSubCatActivity.this, DiagnosticsCentersActivity.class);

                            intent.putExtra("Subservice_id", ids.toString().replace("[", "").replace("]", ""));
                            intent.putExtra("Location", mAddress);
                            intent.putExtra("Latitude", lat);
                            intent.putExtra("Longitude", longg);
                            startActivity(intent);
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                    break;
            }
        }

    }

    private void setRecyclerviewAdapter() {
        adapter = new DiagnosticssubcatAdapter(getApplicationContext(), 1,mDiagnosticsSubcatLists, subservice_idList, DiagnosticsPageSubCatActivity.this, btn_proceed, txt_itemsCount,txt_count);
        rvSubcats.setAdapter(adapter);
        /*Gson gson = new Gson();
        String lists = gson.toJson(adapter.getSelectedList());
        editor.putString("stringObject", lists);
        editor.putInt("count", adapter.getSelectedList().size());
        txt_count.setText(""+ adapter.getSelectedList().size());
        editor.commit();
        Log.i("shared", sharedPreferences.toString());*/
        adapter.setOnItemClickListener(new DiagnosticssubcatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                Toast.makeText(DiagnosticsPageSubCatActivity.this, "Selected List Size " + adapter.getSelectedList().size(), Toast.LENGTH_LONG).show();
                txt_itemsCount.setText("" + adapter.getSelectedList().size());

            }
        });


    }

    private void cartpageSetup() {
        if (Constants.cartDiagnosticscenters != null && Constants.cartDiagnosticscenters.size() > 0) {
            adapter = new DiagnosticssubcatAdapter(getApplicationContext(),2, Constants.cartDiagnosticscenters, subservice_idList, DiagnosticsPageSubCatActivity.this, btn_proceed, txt_itemsCount,txt_count);
            rvSubcats.setAdapter(adapter);
        } else {
            Snackbar snackbar = Snackbar.make(Root_relativeLayout,"Cart Page is empty",3000);
            snackbar.show();
        }
    }
}
