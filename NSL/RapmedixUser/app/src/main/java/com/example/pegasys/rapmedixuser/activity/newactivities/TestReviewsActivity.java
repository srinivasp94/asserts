package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.example.pegasys.rapmedixuser.activity.adapters.ReviewsAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.payment.AvenuesParams;
import com.example.pegasys.rapmedixuser.activity.payment.ServiceUtility;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.TestReviewsResponse;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.testreviewsRequest;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class TestReviewsActivity extends AppCompatActivity implements RetrofitResponseListener, View.OnClickListener {
    private CardView cardView;
    private RecyclerView recyclerView_centers;
    private TextView txt_hint, txt_sort, txt_totalPrice;
            //txt_cart_count;
    private ImageView backButton;
    private Button btn_Checkout;
    private RelativeLayout layout;
    private RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
    private String iName, ihospitalId, iServiceIds;
    private Object obj;
    private DataBase_Helper db;
    ArrayList<TestReviewsResponse> testReviewsList = new ArrayList<>();

    private String order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics_centers);
        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //generating new order number for every transaction
        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        order = randomNum.toString();
    }

    private void initComponents() {
        setreferecnces();
        setonClicklistners();
        getCustomIntent();
    }

    private void setreferecnces() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backButton = toolbar.findViewById(R.id.backbutton);

        recyclerView_centers = findViewById(R.id.rv_centers);
        txt_hint = findViewById(R.id.tv_hinttitle);
        txt_sort = findViewById(R.id.tv_sort);
        cardView = findViewById(R.id.card_review);
        cardView.setVisibility(View.VISIBLE);
        btn_Checkout = findViewById(R.id.btn_proceedtocheckout);
        btn_Checkout.setVisibility(View.VISIBLE);
        txt_totalPrice = findViewById(R.id.tv_totalprice);
        txt_sort.setVisibility(View.GONE);
        layout = (RelativeLayout) findViewById(R.id.rl_click);
        layout.setVisibility(View.GONE);
//        txt_cart_count = (TextView) findViewById(R.id.txt_count);

    }

    private void setonClicklistners() {
        backButton.setOnClickListener(this);
        btn_Checkout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbutton:
                Intent intent1 = new Intent(TestReviewsActivity.this,DiagnosticsPageSubCatActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.btn_proceedtocheckout:
                double sum = 0;
                for (int k=0;k<testReviewsList.size();k++) {
                    sum += Double.parseDouble(testReviewsList.get(k).basicprice);
                    Log.i("TAH","sum"+ sum);
                }


                Intent intent = new Intent(TestReviewsActivity.this, Payments_Initiate.class);
                intent.putExtra(AvenuesParams.ACCESS_CODE, "AVCG67DJ28AK15GCKA");
                intent.putExtra(AvenuesParams.MERCHANT_ID, "111543");
                intent.putExtra(AvenuesParams.ORDER_ID, order);
                Log.i("TAG", "orderid" + order);
                intent.putExtra(AvenuesParams.CURRENCY, "INR");
                intent.putExtra(AvenuesParams.AMOUNT, new Double(sum).toString());
//                intent.putExtra(AvenuesParams.AMOUNT, "1");
                intent.putExtra(AvenuesParams.REDIRECT_URL, Constants.responseHandler);
                intent.putExtra(AvenuesParams.CANCEL_URL, Constants.responseHandler);
                intent.putExtra(AvenuesParams.RSA_KEY_URL, Constants.RSAKey);
                startActivity(intent);
                finish();
//                break;
        }

    }

    private void getCustomIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            iName = intent.getStringExtra("NAME");
            ihospitalId = intent.getStringExtra("HospitalId");
            iServiceIds = intent.getStringExtra("ServiceIDS");
        }
        txt_hint.setText("Review your selected tests at " + iName);
        recyclerView_centers.setLayoutManager(manager);

        testreviewsRequest request = new testreviewsRequest();
        request.userid = new DataBase_Helper(this).getUserId("1");
        request.hospitalId = ihospitalId;
        request.subserviceId = iServiceIds;

        try {
            obj = Class.forName(testreviewsRequest.class.getName()).cast(request);
            Log.i("obj", obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "/index/get_price_subservice_by_category_service", true);

    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null && objectResponse.equals("")) {
            Toast.makeText(TestReviewsActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    Gson gson = new Gson();
                    try {
                        JSONArray array = new JSONArray(new Gson().toJson(objectResponse));
                        TestReviewsResponse reviewsResponse;
                        for (int i = 0; i < array.length(); i++) {
                            reviewsResponse = new TestReviewsResponse();
                            reviewsResponse.id = array.getJSONObject(i).getString("id");

                            reviewsResponse.diagnosticsId = array.getJSONObject(i).getString("diagnostics_id");
                            reviewsResponse.diagnosticssubserviceId = array.getJSONObject(i).getString("diagnosticssubservice_id");
                            reviewsResponse.hospitalId = array.getJSONObject(i).getString("hospital_id");
                            reviewsResponse.basicprice = array.getJSONObject(i).getString("basicprice");
                            reviewsResponse.offerprice = array.getJSONObject(i).getString("offerprice");
                            reviewsResponse.status = array.getJSONObject(i).getString("status");
                            reviewsResponse.subserviceName = array.getJSONObject(i).getString("subservice_name");
                            reviewsResponse.homevisitAvaliable = array.getJSONObject(i).getString("homevisit_avaliable");

//                            reviewsResponse.freeuser = array.getJSONObject(i).getString("freeuser");
//                            reviewsResponse.premiunmuser = array.getJSONObject(i).getString("premiunmuser");
                            testReviewsList.add(reviewsResponse);
                            Log.i("testReviewsList", testReviewsList.toString());
                        }
                        setRecyclerviewAdapter();

                        //add to cart page
//                        Constants.cartPageitems = testReviewsList;
//                        txt_cart_count.setText("" + testReviewsList.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void setRecyclerviewAdapter() {
        final ReviewsAdapter adapter = new ReviewsAdapter(TestReviewsActivity.this, testReviewsList);
        recyclerView_centers.setAdapter(adapter);
        adapter.setOnItemClickListener(new ReviewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_delete:
                        testReviewsList.remove(testReviewsList.get(position));
                        Log.i("TAG","Rview List" + testReviewsList.toString());
//                        if (Constants.cartPageitems != null && Constants.cartPageitems.size() > 0) {
//                            Constants.cartPageitems.remove(testReviewsList.get(position));
//                        }

                        adapter.notifyDataSetChanged();
//                        txt_cart_count.setText("" + Constants.cartPageitems.size());
                        break;
                }
            }
        });
    }
}
