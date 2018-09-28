package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.HealthRecordAdapter;
import com.example.pegasys.rapmedixuser.activity.customviews.TouchImageView;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.imageloadingutils.ImageLoader;
import com.example.pegasys.rapmedixuser.activity.pojo.Typos;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pegasys on 12/26/2017.
 */

public class HealthrecordsIndividualActivity extends AppCompatActivity implements RetrofitResponseListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView textView_nodata;
    private RecyclerView.LayoutManager manager;
    private DataBase_Helper db;
    private Object obj;
    private ArrayList<Typos> HealthRecordList = new ArrayList<>();
    private HealthRecordAdapter healthRecordAdapter;
    private View view;
    private Toolbar toolbar;
    private ImageView backButton;
    private String uid, adviserid;

    public HealthrecordsIndividualActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthrecordindividual);


        initComoponents();

    }
    /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }*/

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_recyclerview, container, false);

        initComoponents();

        db = new DataBase_Helper(HealthrecordsIndividualActivity.this);
        manager = new LinearLayoutManager(HealthrecordsIndividualActivity.this);
        recyclerView.setLayoutManager(manager);

        return view;

    }*/

    private void initComoponents() {
        setReferences();
        setonclicklistners();
        seeHealthrecordsWS();

        setSupportActionBar(toolbar);

    }

    private void seeHealthrecordsWS() {

        Intent intent = getIntent();
        if (intent != null) {
            uid = intent.getStringExtra("UserId");
            adviserid = intent.getStringExtra("AdviserId");
        }
        db = new DataBase_Helper(this);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        userIdreq req = new userIdreq();
//        req.userId = db.getUserId("1");
        req.userId = adviserid;
        try {
            obj = Class.forName(userIdreq.class.getName()).cast(req);
            Log.i("obj", obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "/user/viewhealthrecords_service", true);
    }

    private void setonclicklistners() {
        backButton.setOnClickListener(this);

    }

    private void setReferences() {
        toolbar = findViewById(R.id.toolbar);
        backButton = toolbar.findViewById(R.id.backbutton);
        recyclerView = findViewById(R.id.rv_add);
        textView_nodata = findViewById(R.id.text_nodata);
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(HealthrecordsIndividualActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {


            Typos healthRecordsRespo = Common.getSpecificDataObject(objectResponse, Typos.class);
            Gson gson = new Gson();
            try {
                JSONObject jsonObject = new JSONObject(new Gson().toJson(objectResponse));
                Log.d("response helath records", new Gson().toJson(jsonObject));
//                Toast.makeText(HealthrecordsIndividualActivity.this, "" + new Gson().toJson(jsonObject), Toast.LENGTH_SHORT).show();

                String status = jsonObject.getString("status");
                if (status.equals("success")) {

                    HealthRecordList.clear();
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject js = jsonObject.getJSONObject("" + i);
                        Log.d("response helath records", new Gson().toJson(js));
//                        Toast.makeText(HealthrecordsIndividualActivity.this, "" + new Gson().toJson(js), Toast.LENGTH_LONG).show();

                        Typos typos = new Typos();
                        typos.title = js.getString("title");

                        if (js.getString("doc_type").equals("1"))
                            typos.docName = "Docotor Prescription";
                        if (js.getString("doc_type").equals("2"))
                            typos.docName = "Lab Reports";
                        if (js.getString("doc_type").equals("3"))
                            typos.docName = "Discharge Summary";
                        if (js.getString("doc_type").equals("4"))
                            typos.docName = "Medical Bills";
                        typos.createdDate = js.getString("created_date").split(" ")[0];
                        typos.time = js.getString("created_date").split(" ")[1];
                        typos.healthRecords = js.getString("health_records");
                        HealthRecordList.add(typos);

                    }
                    if (HealthRecordList.size() == 0) {
                        textView_nodata.setVisibility(View.VISIBLE);
                        textView_nodata.setText("Toggle to Add Your Health Records");
                    }

                } else {
                    HealthRecordList = new ArrayList<>();
                    if (HealthRecordList.size() == 0) {
                        textView_nodata.setVisibility(View.VISIBLE);
                        textView_nodata.setText("Toggle to Add Your Health Records");
                    }
                    Toast.makeText(HealthrecordsIndividualActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Log.d("response helath records",new Gson().toJson(healthRecordsRespo));
//            Toast.makeText(HealthrecordsIndividualActivity.this,""+new Gson().toJson(healthRecordsRespo),Toast.LENGTH_SHORT).show();


            healthRecordAdapter = new HealthRecordAdapter(HealthrecordsIndividualActivity.this, HealthRecordList);
            recyclerView.setAdapter(healthRecordAdapter);
            healthRecordAdapter.setOnItemClickListener(new HealthRecordAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    Intent intent = new Intent(getApplicationContext(),ImageListActivity.class);
                    intent.putExtra("Imageurls",HealthRecordList.get(position).healthRecords);
                    startActivity(intent);
                    /*LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view1 = inflater.inflate(R.layout.prescription_imagepreview, null);
//                    ImageView previewImage = view1.findViewById(R.id.iv_preview_prescription);
                    ListView imgslist = (ListView) view1.findViewById(R.id.image_list);
                    AlertDialog.Builder builder = new AlertDialog.Builder(HealthrecordsIndividualActivity.this);
                    builder.create();
                    builder.setView(view1);
                    if (HealthRecordList.get(position).healthRecords != null) {
                        List<String> imgurls = Arrays.asList(HealthRecordList.get(position).healthRecords.split(","));
//                        ArrayAdapter<String> adapterImage = new ArrayAdapter<String>(HealthrecordsIndividualActivity.this, R.layout.slidingimages_layout, R.id.image, imgurls);
                        imageAdapter imageAdapter = new imageAdapter(HealthrecordsIndividualActivity.this, imgurls);
                        imgslist.setAdapter(imageAdapter);
//                        Picasso.with(HealthrecordsIndividualActivity.this).load(Constants.BASE_URL
//                                + "/uploads/health_records/"
//                                + HealthRecordList.get(position).healthRecords).error(R.drawable.doctor_icon).into(previewImage);
                    } else {

                    }
                    builder.show();*/
                }
            });


//            HealthRecordsRespo healthRecordsRespo = Common.getSpecificDataObject(objectResponse, HealthRecordsRespo.class);
//            Gson gson = new Gson();
//            if (healthRecordsRespo.status.equals("success")) {
//                Typos typos = healthRecordsRespo.typos;
//                for (int i = 0; i < ; i++) {
//
//                    PrescriptList list = new PrescriptList();
//                    list.setTitle(typos.title);
//                    list.setCreatedat(typos.createdDate);
//                    HashMap<String, String> map = new HashMap<>();
//                    if (typos.docType.equals("1")) {
//
//                        map.put("DP", "Docotor Prescription");
//                        list.setDoc_type("Docotor Prescription");
//                    }
//                    if (typos.docType.equals("2")) {
//                        map.put("LR", "Lab Reports");
//                        list.setDoc_type("Lab Reports");
//                    }
//                    if (typos.docType.equals("3")) {
//                        map.put("DS", "Discharge Summary");
//                        list.setDoc_type("Discharge Summary");
//                    }
//                    if (typos.docType.equals("4")) {
//                        map.put("MB", "Medical Bills");
//                        list.setDoc_type("Medical Bills");
//                    }
//                    prescriptLists.add(list);
//                }
//
//                healthRecordAdapter = new HealthRecordAdapter(HealthrecordsIndividualActivity.this, prescriptLists);
//                recyclerView.setAdapter(healthRecordAdapter);
//            }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbutton:
                finish();
                break;
        }
    }

    public class imageAdapter extends BaseAdapter {
        private Context context;
        private List<String> imgList = new ArrayList<>();
        LayoutInflater layoutInflater;


        public imageAdapter(Context context, List<String> imgList) {
            this.context = context;
            this.imgList = imgList;
            layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
//            if (view == null) {
            view = layoutInflater.inflate(R.layout.activity_switch_image_example, viewGroup, false);
            TouchImageView img = view.findViewById(R.id.img);
//                ImageView imageView = (ImageView)view.findViewById(R.id.image);

            int loader = R.drawable.rapmdix_logo;
            String image_url = Constants.BASE_URL
                    + "/uploads/health_records/" +
                    imgList.get(i);

            ImageLoader imgLoader = new ImageLoader(getApplicationContext());

            // whenever you want to load an image from url
            // call DisplayImage function
            // url - image url to load
            // loader - loader image, will be displayed before getting image
            // image - ImageView
            imgLoader.DisplayImage(image_url, loader, img);



               /*Picasso.with(context).load(Constants.BASE_URL
                       + "/uploads/health_records/"+
               imgList.get(i)).into(imageView);*/

//            }
            return view;
        }
    }
}
