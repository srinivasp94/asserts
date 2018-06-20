package com.nsl.app.stockreturns;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.Constants;
import com.nsl.app.Crops;
import com.nsl.app.CustomGridtAdapterTenImg;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.OnAmountChangeListener;
import com.nsl.app.Products;
import com.nsl.app.R;
import com.nsl.app.SelectedCities;
import com.nsl.app.adapters.StockPlacementPopupListAdapter;
import com.nsl.app.commonutils.Common;
import com.nsl.app.commonutils.DatabaseUtil;
import com.nsl.app.pojo.StockMovementFirstListPojo;
import com.nsl.app.pojo.StockPlacementPopupPojo;
import com.nsl.app.pojo.StockReturnDetailsPoJo;
import com.nsl.app.pojo.StockReturnPoJo;
import com.nsl.app.pojo.StockReturnUnSynedPojo;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDERDETAILS;
import static com.nsl.app.DatabaseHandler.TABLE_STOCK_RETURNS_DETAILS;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCropsFragmentStockReturnsList extends Fragment {

    private String userId;
    private String crop_id;
    int dd, mm, yy;
    String date;
    private int role;
    private String customer_id;
    private JSONArray mainArray;
    private ProgressDialog progressDialog;
    private String mo_id;
    private String team;

    public NewCropsFragmentStockReturnsList() {
        // Required empty public constructor
    }

    ArrayList<CropsFragmentStockReturnsActivity.Child> cList = new ArrayList<>();
    AlertDialog dialog;
    // Array of strings storing country names
    LocationManager locationManager;
    //private CustomListAdapter mWorkerListAdapter;
    private CustomGridtAdapterTenImg mGridadapter;
    String latitude, longitude, sel_cropid;
    AlertDialog alert;
    ProgressDialog pDialog;
    StringBuffer responseText, responseText1, responseText2, responseText3;
    Fragment fragment = null;
    String division_id, company_id, response, workerId, wname, subcategoryId;
    ProductsRecyclerAdapter adapter;
//    ArrayList<Boolean> checkedStatesType = new ArrayList<>();

    //ArrayList<States> stateList                          = new ArrayList<States>();
    ArrayList<String> selectedrtype = new ArrayList<String>();
    ArrayList<HashMap<String, String>> type = new ArrayList<HashMap<String, String>>();

    private static final String URL = "http://m3infotech.com/muhurtham/api/search";
    public static final String TAG = NewCropsFragmentStockReturnsList.class.getSimpleName();
    int sucess, i = 0;
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    Button getquote, btn_submitproducts;
    FragmentManager fm;
    FragmentTransaction ft;

    ViewPager mViewPager;
    RecyclerView recyclerView;
    int groupPosition;
    int mPosition = 0;
    OnAmountChangeListener onAmountChangeListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onAmountChangeListener = (OnAmountChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
            onAmountChangeListener = (OnAmountChangeListener) a;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_d, container, false);
        View v = inflater.inflate(R.layout.fragment_newrequestlist, container, false);
        //  mCustomPagerAdapter = new CustomPagerAdapter(getActivity(), ImagesArray);

        // mViewPager = (ViewPager) v.findViewById(R.id.pager);
        //  mViewPager.setAdapter(mCustomPagerAdapter);
        db = new DatabaseHandler(getActivity());

        recyclerView = (RecyclerView) v.findViewById(R.id.listTypes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sel_cropid = getArguments().getString("id");
        company_id = getArguments().getString("company_id");
        division_id = getArguments().getString("division_id");
        crop_id = getArguments().getString("crop_id");
        groupPosition = getArguments().getInt("position");
        customer_id = getArguments().getString("customer_id");
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString(Constants.SharedPrefrancesKey.USER_ID, null);
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        team      = sharedpreferences.getString("team", "");
        mo_id=userId;
        if (role != Constants.Roles.ROLE_7) {
            mo_id =  getArguments().getString("mo_id", "");
        }
        progressDialog = Common.showProgressDialog(getActivity());
        if (Common.haveInternet(getActivity())){
            new  Async_getAllStockReturns().execute();
        }else{
            new GetProducts().execute();
        }
        return v;
    }

    public class GetProducts extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... arg0) {

            try {

                type.clear();
                List<Crops> cdcList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + selected_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_BRAND_NAME + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + company_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_cropid;

                String selectQuery = "SELECT products.product_id,brand_name FROM " + TABLE_SERVICEORDERDETAILS + " left join " + TABLE_SERVICEORDER + " on service_order_details.service_relation_id=service_order.service_id left join products on products.product_id=service_order_details.product_id where user_id=" + mo_id + " and products.product_crop_id=" + crop_id + " group by (products.product_id)";

                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                Log.e("Products query ", selectQuery);
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Products products = new Products();
                        products.setProductMasterID(cursor.getString(0));
                        products.setProductName(cursor.getString(1));

                        HashMap<String, String> first = new HashMap<String, String>();
                        first.put("productId", cursor.getString(0));
                        first.put("productName", cursor.getString(1));
                        int relationId = db.getCombinationOf3FromStockReturn(Integer.parseInt(mo_id), Integer.parseInt(company_id), Integer.parseInt(division_id));
                        String quantity = "0";
                        if (relationId != 0) {
                            ArrayList<StockReturnUnSynedPojo> offlineStockReturnsListUnSyncData = db.getStockReturnsData(relationId, cursor.getString(0));
                            if (offlineStockReturnsListUnSyncData.size() != 0) {
                                quantity = offlineStockReturnsListUnSyncData.get(0).quantity;
                            }
                        }

                        first.put("quantity", quantity);
                        type.add(first);
                        quantity = "0";
                        System.out.println("*****@@@@@" + type.size() + ":" + cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ArrayList<StockMovementFirstListPojo> offlineStockPlacementList = db.getOfflineStockPlacementList(mo_id, company_id, division_id, customer_id);
           Common.dismissProgressDialog(progressDialog);
            adapter = new ProductsRecyclerAdapter(offlineStockPlacementList, type);
            recyclerView.setAdapter(adapter);
        }
    }

    public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {
        private ArrayList<SelectedCities> arlist_schemes;
        //ArrayList<String> adapter_schemes;

        ArrayList<StockMovementFirstListPojo> offlineStockPlacementList;
        ArrayList<HashMap<String, String>> type;

        private ArrayList filmArray;
        Products products = new Products();
        Context context;
        private int selctedSchemeId;

        // HashSet<String> selection;

        public ProductsRecyclerAdapter(ArrayList<StockMovementFirstListPojo> offlineStockPlacementList, ArrayList<HashMap<String, String>> type) {
            this.offlineStockPlacementList = offlineStockPlacementList;
            this.type = type;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_stock_return, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {


            mPosition = position;

            // Log.d("result", stockMovementFirstListPojo.toString());
            selctedSchemeId = 0;
            final String productId = type.get(position).get("productId");

            holder.tvDealerNameCotton.setText(type.get(position).get("productName"));
            holder.tvStockReturn.setText(type.get(position).get("quantity"));

            if (role != Constants.Roles.ROLE_7) {
                mo_id =  getArguments().getString("mo_id", "");

                if (!userId.equalsIgnoreCase(mo_id)){
                    holder.tvStockReturn.setEnabled(false);
                    holder.tvStockReturn.setClickable(false);
                    holder.btnSaveStock.setEnabled(false);

                }


            }

            holder.btnSaveStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if (holder.tvStockReturn.getText().toString().equalsIgnoreCase("0"))
                  {
                      holder.tvStockReturn.setError("Please enter quantity");
                      holder.tvStockReturn.requestFocus();
                  }
                  else {
                      saveStockMovementInDb(holder.tvStockReturn.getText().toString(), Integer.parseInt(productId));
                  }
                }
            });


        }

        @Override
        public int getItemCount() {
            return type.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvDealerNameCotton;
            public EditText tvStockReturn;
            public ImageButton btnSaveStock;


            public ViewHolder(View itemView) {

                super(itemView);

                tvDealerNameCotton = (TextView) itemView.findViewById(R.id.tv_dealer_name_cotton);
                tvStockReturn = (EditText) itemView.findViewById(R.id.tv_stock_return);
                btnSaveStock = (ImageButton) itemView.findViewById(R.id.btn_save_stock);


            }
        }


    }


    private boolean saveStockMovementInDb(String currentStockQuantity, int productId) {
        if (!currentStockQuantity.equalsIgnoreCase("")) {

            progressDialog = Common.showProgressDialog(getActivity());


            StockReturnPoJo stockMovementPoJo = new StockReturnPoJo();
            stockMovementPoJo.ffmId = 0;
            stockMovementPoJo.divisionId = Integer.parseInt(division_id);
            stockMovementPoJo.companyId = Integer.parseInt(company_id);
            stockMovementPoJo.updatedDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementPoJo.createdDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementPoJo.createdBy = mo_id;
            stockMovementPoJo.userId = Integer.parseInt(mo_id);
            stockMovementPoJo.customerId = Integer.parseInt(customer_id);


            StockReturnDetailsPoJo stockMovementDetailsPojo = new StockReturnDetailsPoJo();
            stockMovementDetailsPojo.ffmId = 0;
            stockMovementDetailsPojo.cropId = Integer.parseInt(crop_id);
            stockMovementDetailsPojo.quantity = currentStockQuantity;
            stockMovementDetailsPojo.productId = productId;


            db.insertStackReturn(stockMovementPoJo, stockMovementDetailsPojo);
            Toast.makeText(getActivity(), "Record successfully inserted..", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (Common.haveInternet(getContext())) {
                prepareStockReturnsDataAndPush();
            } else {
                Common.dismissProgressDialog(progressDialog);
            }
            return true;

            // DatabaseUtil.copyDatabaseToExtStg(getContext());
        } else {
            Toast.makeText(getActivity(), "Please date and quntity", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    private void prepareStockReturnsDataAndPush() {
        mainArray = new JSONArray();
        JSONArray adbBookArray = new JSONArray();
        JSONObject naimObject = null;

        ArrayList<StockReturnUnSynedPojo> stockMovementUnSynedPojoList = db.getOfflineStockReturnListUnSyncData1();
        for (StockReturnUnSynedPojo stockMovementUnSynedPojo : stockMovementUnSynedPojoList) {

            ArrayList<StockReturnUnSynedPojo> stockMovementUnSynedPojos = db.getOfflineStockReturnsListUnSyncData(stockMovementUnSynedPojo.stockReturnId);
            for (StockReturnUnSynedPojo stockMovementUnSynedPojo1 : stockMovementUnSynedPojos) {

                JSONObject advBookObj = new JSONObject();

                try {
                    advBookObj.put("CompanyID", stockMovementUnSynedPojo1.companyId);
                    advBookObj.put("customer_id", stockMovementUnSynedPojo1.customerId);
                    // advBookObj.put("role", stockMovementUnSynedPojo1.userType);
                    advBookObj.put("DivisionID", stockMovementUnSynedPojo1.divisionId);
                    advBookObj.put("id", stockMovementUnSynedPojo1.stockReturnId);
                    advBookObj.put("user_id", mo_id);


                    JSONArray cropArray = new JSONArray();

                    JSONArray productArray = new JSONArray();


                    JSONObject object_one = new JSONObject();
                    object_one.put("mobile_stock_return_details_id", stockMovementUnSynedPojo1.stockReturnsDetailsId);
                    object_one.put("ProductID", stockMovementUnSynedPojo1.productId);
                    object_one.put("CropId", stockMovementUnSynedPojo1.cropId);
                    object_one.put("Qunatity", stockMovementUnSynedPojo1.quantity);
                    productArray.put(object_one);


                    JSONObject cropObj = new JSONObject();
                    cropObj.put("CropId", stockMovementUnSynedPojo1.cropId);

                    cropArray.put(cropObj);
                    advBookObj.put("Crops", cropArray);
                    cropObj.put("Products", productArray);
                    //}
                    naimObject = new JSONObject();
                    adbBookArray.put(advBookObj);
                    naimObject.put("stockreturns", adbBookArray);


                } catch (JSONException e) {
                }

            }
        }
        mainArray.put(naimObject);
        Log.i("  -json array -", "" + mainArray.toString());
        if (mainArray.length() > 0) {
            new callAPIToPushStockReturnsData().execute();
        }
    }
/*
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, mainArray.toString());
        Request request = new Request.Builder()
                .url(Constants.URL_INSERT_STOCKMOVEMENT)
                .post(body)
                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();


            String jsonData = response.body().string();
            System.out.println("!!!!!!!1Sales Oreder" + jsonData);

            if (jsonData != null) {
                JSONArray jsonArray;

                jsonArray = new JSONArray(jsonData);

                for (int i = 0; i < jsonArray.length(); i++)

                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String status = jsonObject.getString("status");*/
/*
                    if (status.equalsIgnoreCase("success")) {
                        //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                        String service_id = jsonObject.getString("service_id");
                        String ffmid = jsonObject.getString("ffm_id");

                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", service_id);
                        Log.e("ffmid", ffmid);
                        sdbw = db.getWritableDatabase();
                        // updateFeedback(Feedback feedback);
                        String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + service_id;
                        sdbw.execSQL(updatequery);

                        JSONArray detailsArray = jsonObject.getJSONArray("details");
                        for (int k = 0; k < detailsArray.length(); k++) {
                            String ffm_id = detailsArray.getJSONObject(k).getString("ffm_id");
                            String order_detail_id = detailsArray.getJSONObject(k).getString("order_detail_id");

                            String updatequery1 = "UPDATE " + TABLE_SERVICEORDERDETAILS + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffm_id + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " = " + order_detail_id;
                            sdbw.execSQL(updatequery1);
                        }


                    }
*/


//                }
//
//
//            }

/*

        try {
            ArrayList<StockMovementPoJo> serviceOrderMasterList = db.getOfflineStockPlacementUnSynced(0);

            for (int i = 0; i < serviceOrderMasterList.size(); i++) {
                ServiceOrderMaster serviceOrderMaster = serviceOrderMasterList.get(i);
                JSONObject advBookObj = new JSONObject();

                advBookObj.put("CompanyID", serviceOrderMasterList.get(i)._serviceorder_company_id);
                advBookObj.put("customer_id", serviceOrderMasterList.get(i)._serviceorder_customer_id);
                advBookObj.put("Tokenamount", serviceOrderMasterList.get(i)._token_amount);

                advBookObj.put("DivisionID", serviceOrderMasterList.get(i)._serviceorder_division_id);


                advBookObj.put("id", serviceOrderMasterList.get(i)._serviceorder_masterid);
                advBookObj.put("user_id", sharedpreferences.getString("userId", null));

                JSONArray cropArray = new JSONArray();

                JSONArray productArray = new JSONArray();
                for (int l = 0; l < serviceOrderMaster.getServiceOrderDetailMasterList().size(); l++) {
                    ServiceOrderDetailMaster serviceOrderDetailMaster = serviceOrderMaster.getServiceOrderDetailMasterList().get(l);
                    JSONObject object_one = new JSONObject();
                    object_one.put("advance_amount", serviceOrderDetailMaster._serviceorderdetail_advance_amount);
                    object_one.put("ProductID", serviceOrderDetailMaster._serviceorderdetail_product_id);
                    object_one.put("Qunatity", serviceOrderDetailMaster._serviceorderdetail_quantity);
                    object_one.put("scheme_id", serviceOrderDetailMaster._serviceorderdetail_scheme_id);
                    object_one.put("mobile_serivce_details_id", serviceOrderDetailMaster._serviceorderdetail_masterid);
                    productArray.put(object_one);

                }
                JSONObject cropObj = new JSONObject();
                cropObj.put("CropId", serviceOrderMaster.getServiceOrderDetailMasterList().get(0)._serviceorderdetail_crop_id);
                cropObj.put("scheme_id", "");

                cropArray.put(cropObj);
                advBookObj.put("Crops", cropArray);
                cropObj.put("Products", productArray);
                //}
                naimObject = new JSONObject();
                adbBookArray.put(advBookObj);
                naimObject.put("AdvanceBookings", adbBookArray);

            }
            mainArray.put(naimObject);
            Log.i("  -json array -", "" + mainArray.toString());

*/

    // if (serviceOrderMasterList.size() > 0) {

/*
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, mainArray.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_INSERT_ADVANCEBOOKING)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);

                if (jsonData != null) {
                    JSONArray jsonArray;

                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                            String service_id = jsonObject.getString("service_id");
                            String ffmid = jsonObject.getString("ffm_id");

                            //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                            Log.e("sqlite id", service_id);
                            Log.e("ffmid", ffmid);
                            sdbw = db.getWritableDatabase();
                            // updateFeedback(Feedback feedback);
                            String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + service_id;
                            sdbw.execSQL(updatequery);

                            JSONArray detailsArray = jsonObject.getJSONArray("details");
                            for (int k = 0; k < detailsArray.length(); k++) {
                                String ffm_id = detailsArray.getJSONObject(k).getString("ffm_id");
                                String order_detail_id = detailsArray.getJSONObject(k).getString("order_detail_id");

                                String updatequery1 = "UPDATE " + TABLE_SERVICEORDERDETAILS + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffm_id + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " = " + order_detail_id;
                                sdbw.execSQL(updatequery1);
                            }


                        }


                    }


                }*/

      /*      }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }*/


       /* } catch (Exception e) {
            e.printStackTrace();
        }*/
    // }

    public StockPlacementPopupListAdapter setListOnAdapter(final List<StockPlacementPopupPojo> stockPlacementPopupPojos, RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        StockPlacementPopupListAdapter stockPlacementPopupListAdapter = null;

        if (stockPlacementPopupPojos.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //recordnotfnd.setVisibility(View.GONE);
            stockPlacementPopupListAdapter = new StockPlacementPopupListAdapter(getContext(), stockPlacementPopupPojos);
            recyclerView.setAdapter(stockPlacementPopupListAdapter);
            stockPlacementPopupListAdapter.setOnItemClickListener(new StockPlacementPopupListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {

                    //   busDetailsFragment();


                }
            });
            stockPlacementPopupListAdapter.notifyDataSetChanged();

        } else if (stockPlacementPopupPojos == null || stockPlacementPopupPojos.size() == 0) {

            // recordnotfnd.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        return stockPlacementPopupListAdapter;
    }

    private class callAPIToPushStockReturnsData extends AsyncTask<Void, Void, String> {
        private String jsonData;

        @Override
        protected String doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, mainArray.toString());
            Request request = new Request.Builder()
                    .url(Constants.URL_INSERTING_STOCKRETURNS)
                    .post(body)
                    .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();


                jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (jsonData != null) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject jsonObject = null;

                        jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArrayDetails = jsonObject.getJSONArray("details");
                            for (int j = 0; j < jsonArrayDetails.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayDetails.getJSONObject(j);


                                String sqlite_id = jsonObject1.getString("return_detail_id");
                                String ffmid = jsonObject1.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", sqlite_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_STOCK_RETURNS_DETAILS + " SET " + db.KEY_FFMID + " = " + ffmid + " WHERE " + db.KEY_STOCK_RETURNS_DETAILS_ID + " = " + sqlite_id;
                                sdbw.execSQL(updatequery);
                            }

                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            Common.dismissProgressDialog(progressDialog);

        }
    }



    public class Async_getAllStockReturns extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.GET_STOCKRETURNS_LIST + team)
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (jsonData != null) {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonData);
                        if (!jsonObject.has("error")) {


                            JSONArray retailer_detailsArray = jsonObject.getJSONArray("stock_return_data");

                            for (int j = 0; j < retailer_detailsArray.length(); j++) {
                                JSONObject retailer_detailsObject = retailer_detailsArray.getJSONObject(j);
                                JSONObject stock_movement_object = retailer_detailsObject.getJSONObject("stock_returns");


                                StockReturnPoJo stockMovementPoJo = new StockReturnPoJo();
                                stockMovementPoJo.ffmId = stock_movement_object.isNull("stock_returns_id") ? 0 : stock_movement_object.getInt("stock_returns_id");
                                stockMovementPoJo.divisionId = stock_movement_object.isNull("division_id") ? 0 : stock_movement_object.getInt("division_id");
                                stockMovementPoJo.companyId = stock_movement_object.isNull("company_id") ? 0 : stock_movement_object.getInt("company_id");
                                stockMovementPoJo.customerId = stock_movement_object.isNull("customer_id") ? 0 : stock_movement_object.getInt("customer_id");
                                stockMovementPoJo.updatedDatetime = stock_movement_object.getString("updated_datetime");
                                stockMovementPoJo.createdDatetime = stock_movement_object.getString("created_datetime");
                                stockMovementPoJo.createdBy = stock_movement_object.getString("created_by");
                                stockMovementPoJo.updatedBy = stock_movement_object.getString("updated_by");
                                stockMovementPoJo.userId = stock_movement_object.isNull("user_id") ? 0 : stock_movement_object.getInt("user_id");


                                JSONArray stock_movementdetails_array = retailer_detailsObject.getJSONArray("stock_returns_details");
                                for (int k = 0; k < stock_movementdetails_array.length(); k++) {
                                    JSONObject jsonObjectDeatils = stock_movementdetails_array.getJSONObject(k);


                                    StockReturnDetailsPoJo stockMovementDetailsPojo = new StockReturnDetailsPoJo();
                                    stockMovementDetailsPojo.ffmId = jsonObjectDeatils.getInt("stock_returns_details_id");
                                    stockMovementDetailsPojo.stockReturnId = jsonObjectDeatils.getInt("stock_returns_id");
                                    stockMovementDetailsPojo.cropId = jsonObjectDeatils.getInt("crop_id");
                                    stockMovementDetailsPojo.productId = jsonObjectDeatils.getInt("product_id");
                                    stockMovementDetailsPojo.quantity = jsonObjectDeatils.getString("quantity");


                                    db.insertStackReturn(stockMovementPoJo, stockMovementDetailsPojo);


                                }


                            }

                        }
                        // dbWritable.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    // db.deleteComplaints();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            new GetProducts().execute();
        }

    }
}










