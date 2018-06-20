package com.nsl.app.advancebooking;


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
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsl.app.Constants;
import com.nsl.app.Crops;
import com.nsl.app.CustomGridtAdapterTenImg;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.OnAmountChangeListener;
import com.nsl.app.Products;
import com.nsl.app.R;
import com.nsl.app.Schemes;
import com.nsl.app.SelectedCities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCropsFragmentAdvanceBookingList extends Fragment {

    private String customer_id;

    public NewCropsFragmentAdvanceBookingList() {
        // Required empty public constructor
    }

    ArrayList<CropsFragmentAdvancebookingActivity.Child> cList = new ArrayList<>();
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
    public static final String TAG = NewCropsFragmentAdvanceBookingList.class.getSimpleName();
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

        if (context instanceof Activity){
            a=(Activity) context;
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
       /* btn_submitproducts = (Button) v.findViewById(R.id.btn_submitproducts);
        btn_submitproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseText1 = new StringBuffer();


                for (int i = 0; i < type.size(); i++) {
                    if (checkedStatesType.get(i)) {
                        //checked at Ith position
                        //add it to array
                        // selected.add(results.get(i).get("subCatid"));
                        if (responseText1.length() > 0)
                            responseText1.append(",");

                        responseText1.append(type.get(i).get("productId"));
                        Toast.makeText(getActivity(),responseText1.toString(),Toast.LENGTH_LONG).show();
                        // editor.putString("TestName",responseText1.toString());
                        //editor.commit();
                    }
                }
            }
        });*/
        recyclerView = (RecyclerView) v.findViewById(R.id.listTypes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sel_cropid    = getArguments().getString("id");
        company_id    = getArguments().getString("company_id");
        customer_id    = getArguments().getString("customer_id");

        division_id   = getArguments().getString("division_id");
        groupPosition = getArguments().getInt("position");
        // Toast.makeText(getActivity(), sel_cropid, Toast.LENGTH_SHORT).show();
        // gridViewCustom     = (GridView) v.findViewById(R.id.gridViewCustom);

        // mWorkerListAdapter = new CustomListAdapter(getActivity(), workersList, images_arraylist);

        //mGridadapter = new CustomGridtAdapterTenImg(getActivity(), categoriesList);

        //
        // listView.setAdapter(mWorkerListAdapter);
        // gridViewCustom.setAdapter(mGridadapter);


        new GetProducts().execute();
        return v;
    }

    public class GetProducts extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading  ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... arg0) {

            try {

                type.clear();
                List<Crops> cdcList = new ArrayList<>();
                Calendar c =Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
                String CURDATE = df.format(c.getTime());
              //
                //
                // String selectQuery = "SELECT  products.product_id, brand_name, price_per_packet FROM  scheme_products  LEFT JOIN  products  ON  products.product_id = scheme_products.product_id  WHERE  scheme_products.product_id  <>0 and  valid_from  <= '"+CURDATE+"'  and  extenstion_date   >= '"+ CURDATE +"' and  products.product_crop_id = "+ sel_cropid + " and  products.company_id  = "+ company_id +" group by products.product_id";

                //String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_BRAND_NAME+","+KEY_PRODUCTS_PRICE + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + company_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_cropid;
                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();


                String selectQuery ="SELECT * FROM "+db.TABLE_PRODUCTS+" WHERE "+db.KEY_PRODUCT_CROP_ID +" = "+ sel_cropid +" AND "+db.KEY_PRODUCTS_COMPANY_ID+" = "+company_id+" AND "+db.KEY_PRODUCTS_DIVISION_ID +" = "+division_id;
                Log.e("Products query ", selectQuery);

                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Products products = new Products();
                        products.setProductMasterID(cursor.getString(0));
                        products.setProductName(cursor.getString(20));

                        HashMap<String, String> first = new HashMap<String, String>();
                        first.put("productId", cursor.getString(1));
                        first.put("productName", cursor.getString(20));
                        first.put("productPrice", cursor.getString(2));
                        type.add(first);
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
            pDialog.dismiss();
            adapter = new ProductsRecyclerAdapter(type);
            recyclerView.setAdapter(adapter);
        }
    }

    public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {
        private ArrayList<SelectedCities> arlist_schemes;
        //ArrayList<String> adapter_schemes;

        ArrayList<HashMap<String, String>> results;


        private ArrayList filmArray;
        Products products = new Products();
        Context context;
        private int selctedSchemeId;

        // HashSet<String> selection;

        public ProductsRecyclerAdapter(ArrayList<HashMap<String, String>> results) {
            this.results = results;
           // selection = TabCropsAdvanceBookingFragment.selections.get(sel_cropid);
//            checkedStatesType = new ArrayList<>();
//            for (int i = 0; i < results.size(); i++) {
//                checkedStatesType.add(false);
//            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_advancebooking_products, parent, false);

            return new ViewHolder(v,new QunatityTextWatcher(),new AmountTextWatcher());
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            int mPosition = position;
            final HashMap<String, String> result = results.get(position);
            selctedSchemeId = 0;

            holder.time.setText(result.get("productId"));
            holder.checkBox.setText(result.get("productName"));
            Log.v("POSITION ",""+mPosition);

            holder.qunatityTextWatcher.updatePosition(holder.getAdapterPosition(),holder.et_schemeprice,holder.et_qty,holder.et_amount);
            holder.amountTextWatcher.updatePosition(holder.getAdapterPosition(),holder.et_amount);


            List<CropsFragmentAdvancebookingActivity.Child> mchildList = CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList;

            if(mchildList!=null && mchildList.size()>0){
                int  childListPosition = mchildList.indexOf(new CropsFragmentAdvancebookingActivity.Child(results.get(mPosition).get("productId")));
                if(childListPosition>-1) {
                    CropsFragmentAdvancebookingActivity.Child child = CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition);
                    Log.d("holder.et_schemeprice","holder.et_schemeprice...2 : "+ child.scheme_amount);
                    holder.et_schemeprice.setText(child.scheme_amount);
                    holder.et_qty.setText(child.qty);
                    holder.et_amount.setText(child.amount);
                    final ArrayList<Schemes>  adapter_schemes = (ArrayList<Schemes>) db.getSchemesByProducId(child.product_id);
                    Schemes schemes = new Schemes();
                    if(adapter_schemes==null|| adapter_schemes.size()==0)
                        schemes._scheme_title = "No scheme";
                    else
                        schemes._scheme_title = "Select";

                    adapter_schemes.add(schemes);
                    holder.spin_schemes.setAdapter(new ArrayAdapter<Schemes>(getActivity(), R.layout.schemes_spinner_item,R.id.customSpinnerItemTextView, adapter_schemes));
                    selctedSchemeId=adapter_schemes.get(0)._scheme_id;
                    holder.spin_schemes.setTag(childListPosition);
                    holder.spin_schemes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int childListPosition = (int)parent.getTag();
                            if(adapter_schemes.get(position)._scheme_masterid!=null) {

                                    selctedSchemeId = Integer.parseInt(adapter_schemes.get(position)._scheme_masterid);
                                    CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).scheme_id = "" + selctedSchemeId;

                                    holder.et_schemeprice.setFocusable(false);
                                holder.et_schemeprice.setText(adapter_schemes.get(position).getScheme_value());
                                Log.d("holder.et_schemeprice","holder.et_schemeprice...3");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if(adapter_schemes!=null && adapter_schemes.size()>0){
                        int index = adapter_schemes.indexOf(child.name);
                        if(index>-1)
                        holder.spin_schemes.setSelection(index);
                    }
                    holder.checkBox.setOnCheckedChangeListener(null);
                    holder.checkBox.setChecked(true);
                    holder.et_amount.setFocusable(false);
                    holder.et_qty.setEnabled(true);
                    holder.spin_schemes.setEnabled(true);
                    holder.et_schemeprice.setEnabled(true);
                    Log.v("POSITION-CHECKED ",""+mPosition);

                }
            else {
                    holder.checkBox.setOnCheckedChangeListener(null);
                    holder.checkBox.setChecked(false);
                    holder.et_amount.setFocusable(false);
                    holder.et_qty.setEnabled(false);
                    holder.spin_schemes.setEnabled(false);
                    holder.et_schemeprice.setEnabled(false);

                    holder.et_amount.setText("");
                    holder.et_qty.setText("");
                    holder.et_schemeprice.setText("");


                    Log.v("POSITION-UNCHECKED ",""+mPosition);
                }
            }

            holder.checkBox.setTag(mPosition);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public float Totaldiscount;

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    int item_position = (int) compoundButton.getTag();

                    String region_id = null;
//                        checkedStatesType.set(item_position, checked);
                    Log.i("   PName ----", "" + result.get("productName"));

                    if (checked) {
                        holder.et_schemeprice.setEnabled(true);
                        holder.et_amount.setFocusable(false);
                        holder.et_qty.setEnabled(true);
                        holder.spin_schemes.setEnabled(true);
                        sdbw = db.getWritableDatabase();
                        final String product_Id = results.get(item_position).get("productId");
                        Log.d("product_Id",product_Id);
                        String selectQuery1 ="SELECT region_id FROM "+db.TABLE_CUSTOMERS+" WHERE customer_id = "+ customer_id;

                        Cursor cursor = sdbw.rawQuery(selectQuery1, null);
                        //System.out.println("cursor count "+cursor.getCount());
                        if (cursor.moveToFirst()) {
                            do {

                                 region_id =cursor.getString(0);

                                System.out.println("*****@@@@@" + cursor.getString(0));
                                // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                            } while (cursor.moveToNext());
                        }







                       // if(sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE,0)==Constants.Roles.ROLE_7){
                        String selectQuery3 ="SELECT * FROM "+db.TABLE_SCHEME_PRODUCTS+" WHERE "+db.KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID +" = "+ product_Id  +" AND "+db.KEY_TABLE_SCHEME_PRODUCTS_REGION_ID+" = "+region_id;


                        final ArrayList<Schemes> adapter_schemes = new ArrayList<Schemes>();

                        Cursor cursor1 = sdbw.rawQuery(selectQuery3, null);
                        //System.out.println("cursor count "+cursor.getCount());
                        if (cursor1.moveToFirst()) {
                            do {


                                Schemes schemes = new Schemes();
                                schemes._scheme_title = "Select";
                                schemes.setschemeName(cursor1.getString(6));
                                schemes.setScheme_value(cursor1.getString(13));
                                schemes.setschemeMasterID(cursor1.getString(1));
                                adapter_schemes.add(schemes);
                              //  System.out.println("*****@@@@@" + type.size() + ":" + cursor1.getString(1));
                                // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                            } while (cursor1.moveToNext());
                        }else {
                            Schemes schemes = new Schemes();
                            schemes._scheme_title = "No scheme";

                            String selectQuery4 ="SELECT * FROM "+db.TABLE_PRODUCT_PRICE+" WHERE "+db.KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID +" = "+ product_Id  +" AND "+db.KEY_TABLE_SCHEME_PRODUCTS_REGION_ID+" = "+region_id;
                            Cursor cursor4 = sdbw.rawQuery(selectQuery4, null);
                            //System.out.println("cursor count "+cursor.getCount());
                            float productPrice = 0;
                            float discount = 0;
                            if (cursor4.moveToFirst()) {

                                    schemes.setScheme_value(cursor4.getString(2));
                                    adapter_schemes.add(schemes);
                                productPrice= Float.parseFloat(cursor4.getString(2));
                                discount= Float.parseFloat(cursor4.getString(3));


                            }else{



                                String selectQuery5 ="SELECT * FROM "+db.TABLE_PRODUCT_PRICE+" WHERE "+db.KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID +" = "+ product_Id ;
                                Cursor cursor5 = sdbw.rawQuery(selectQuery5, null);
                                if (cursor5.moveToFirst()) {
                                    schemes.setScheme_value(cursor5.getString(2));
                                    adapter_schemes.add(schemes);
                                    productPrice= Float.parseFloat(cursor5.getString(2));
                                    discount= Float.parseFloat(cursor5.getString(3));
                                }


                            }

                            Totaldiscount = productPrice*discount/100;
                            float discountedprice = productPrice - Totaldiscount;

                            holder.et_schemeprice.setText(String.valueOf(discountedprice));
                             Log.d("holder.et_schemeprice","holder.et_schemeprice...5"+String.valueOf(discountedprice));
                            holder.et_amount.setText("");
                            holder.et_qty.setText("");

                            /* $actual_price=$price->price;
                                $price=$actual_price*$price->discount/100;
                                echo $discountedprice=$product_info->price-$price;*/



                        }





                        //selection.add(product_Id);

                        /*Schemes schemes = new Schemes();
                        schemes._scheme_title = "Select";
                        adapter_schemes.add(schemes);
                        adapter_schemes.addAll((ArrayList<Schemes>) db.getSchemesByProducId(product_Id));
                       */
                        //selctedSchemeId=adapter_schemes.get(0)._scheme_id;
                        holder.spin_schemes.setAdapter(new ArrayAdapter<Schemes>(getActivity(), R.layout.schemes_spinner_item, R.id.customSpinnerItemTextView, adapter_schemes));


                        CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).isChecked = holder.checkBox.isChecked();
                        List<CropsFragmentAdvancebookingActivity.Child> childList = CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList;
                        int childListPosition = childList.indexOf(new CropsFragmentAdvancebookingActivity.Child(results.get(item_position).get("productId")));

                        if (childListPosition == -1) {
                            childListPosition = childList.size();
                            CropsFragmentAdvancebookingActivity.Child child = new CropsFragmentAdvancebookingActivity.Child();
                            CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.add(child);
                        }

                       /* if(adapter_schemes.size()==1){
                            selctedSchemeId = 0;
                            CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).scheme_id = "" + selctedSchemeId;
                            holder.et_schemeprice.setFocusable(false);
                            holder.et_schemeprice.setText(results.get(item_position).get("productPrice"));
                            holder.et_amount.setText("");
                            holder.et_qty.setText("");
                        }*/


                        CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).name = results.get(item_position).get("productName");
                        CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).isChecked = holder.checkBox.isChecked();
                        CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).product_id = results.get(item_position).get("productId");
                        CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).id = results.get(item_position).get("productId");
                        CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).scheme_amount=holder.et_schemeprice.getText().toString();



                        holder.spin_schemes.setTag(childListPosition);
                        final String finalRegion_id = region_id;
                        holder.spin_schemes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public float Totaldiscount;

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                                int childListPosition = (int)parent.getTag();
                                if(adapter_schemes.get(position)._scheme_masterid!=null) {
                                    selctedSchemeId = Integer.parseInt(adapter_schemes.get(position)._scheme_masterid);
                                    CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).scheme_id = "" + selctedSchemeId;
                                    CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).slab_id = "" + adapter_schemes.get(position).getschemeName();
                                    holder.et_schemeprice.setFocusable(false);
                                    holder.et_schemeprice.setText(adapter_schemes.get(position).getScheme_value());
                                    Log.d("holder.et_schemeprice","holder.et_schemeprice...7");
                                    holder.et_amount.setText("");
                                    holder.et_qty.setText("");
                                }else {



                                    String selectQuery4 ="SELECT * FROM "+db.TABLE_PRODUCT_PRICE+" WHERE "+db.KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID +" = "+ product_Id  +" AND "+db.KEY_TABLE_SCHEME_PRODUCTS_REGION_ID+" = "+ finalRegion_id;
                                    Cursor cursor4 = sdbw.rawQuery(selectQuery4, null);
                                    //System.out.println("cursor count "+cursor.getCount());
                                    float productPrice = 0;
                                    float discount = 0;
                                    if (cursor4.moveToFirst()) {
                                        productPrice= Float.parseFloat(cursor4.getString(2));
                                        discount= Float.parseFloat(cursor4.getString(3));


                                    }else {


                                        String selectQuery5 = "SELECT * FROM " + db.TABLE_PRODUCT_PRICE + " WHERE " + db.KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + " = " + product_Id;
                                        Cursor cursor5 = sdbw.rawQuery(selectQuery5, null);
                                        if (cursor5.moveToFirst()) {
                                            productPrice= Float.parseFloat(cursor5.getString(2));
                                            discount= Float.parseFloat(cursor5.getString(3));
                                        }
                                    }

                                    Totaldiscount = productPrice*discount/100;
                                    float discountedprice = productPrice - Totaldiscount;
Log.d("holder.et_schemeprice","holder.et_schemeprice...1");
                                    holder.et_schemeprice.setText(String.valueOf(discountedprice));
                                    holder.et_amount.setText("");
                                    holder.et_qty.setText("");
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else {
                        holder.et_amount.setFocusable(false);
                        holder.et_qty.setEnabled(false);
                        holder.spin_schemes.setEnabled(false);
                        holder.spin_schemes.setSelection(0);
                        holder.et_schemeprice.setEnabled(false);
                        holder.et_amount.setText("");
                        holder.et_qty.setText("");
                        //  selection.remove(results.get(item_position).get("productId"));
                        List<CropsFragmentAdvancebookingActivity.Child> childList = CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList;
                        if (childList != null && childList.size() > 0) {
                            int index = childList.indexOf(new CropsFragmentAdvancebookingActivity.Child(results.get(item_position).get("productId")));
                            if (index > -1) {
                                childList.remove(index);
                                notifyDataSetChanged();

                            }
                        }
                    }


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            onAmountChangeListener.onAmountChanged();
                        }
                    }, 500);



                    Log.i("   Size ----", "" + CropsFragmentAdvancebookingActivity.globalGroup.size());
                    Log.i("   GName ----", "" + CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition));
                    responseText1 = new StringBuffer();

                }
            });

            holder.et_amount.setTag(position);


            holder.et_qty.setTag(position);

            //}
           /* List<Schemes> schemes = db.getAllSchemes();

            for (Schemes fb : schemes) {
                String log = " sch Id: " + fb.getID()
                        + "  ,sch Name: " + fb.getschemeName()
                        + "  ,sch sap_code " + fb.getscheme_sap_code()
                        + "  ,sch sapid: " + fb.getscheme_value()
                        + "  ,sch companyid: " + fb.getscheme_company_id()
                        + "  ,sch cropid: " + fb.getscheme_crop_id()
                        + "  ,sch division: " + fb.getscheme_division_id()
                        + "  ,sch file location: " + fb.getscheme_file_location()
                        + "  ,sch status: " + fb.getscheme_status()
                        + "  ,sch unit: " + fb.getscheme_unit();
                // Writing Contacts to log
                Log.e("Schemes: ", log);
            }*/
        }

        @Override
        public int getItemCount() {
            return results.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView time;
            public EditText et_schemeprice;
            public EditText et_amount, et_qty;
            public CheckBox checkBox;
            public Spinner spin_schemes;
            QunatityTextWatcher qunatityTextWatcher;
            AmountTextWatcher amountTextWatcher;

            public ViewHolder(View itemView,QunatityTextWatcher qunatityTextWatcher,AmountTextWatcher amountTextWatcher) {
                super(itemView);

                time = (TextView) itemView.findViewById(R.id.code);
                et_schemeprice = (EditText) itemView.findViewById(R.id.et_schemeprice);
                et_amount = (EditText) itemView.findViewById(R.id.et_amount);
                et_qty = (EditText) itemView.findViewById(R.id.et_qty);
                checkBox = (CheckBox) itemView.findViewById(R.id.chk_product);
                spin_schemes = (Spinner) itemView.findViewById(R.id.spin_scheme);

                this.qunatityTextWatcher = qunatityTextWatcher;
                this.et_qty.addTextChangedListener(qunatityTextWatcher);

                this.amountTextWatcher = amountTextWatcher;
                this.et_amount.addTextChangedListener(amountTextWatcher);


            }
        }
        ////////////////////////////////////////////////////
        ///////////////////////////////////////////////////

        public ArrayList<String> getSelectedTests() {
            ArrayList<String> selected = new ArrayList<>();

            for (int i = 0; i < results.size(); i++) {
//                if (checkedStatesType.get(i)) {
//                    //checked at Ith position
//                    //add it to array
//                    selected.add(results.get(i).get("productId"));
//                }
            }
            return selected;
        }

        private class QunatityTextWatcher implements TextWatcher {
            int position;
            EditText schemeEditText;
            EditText quantityEditText;
            EditText amountEditText;

            public void updatePosition(int position, EditText schemeEditText, EditText quantityEditText, EditText amountEditText) {
                this.position = position;
                this.schemeEditText = schemeEditText;
                this.quantityEditText = quantityEditText;
                this.amountEditText = amountEditText;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {

                    if (schemeEditText.getText().toString().length() > 0) {
                        float amount = Float.parseFloat(schemeEditText.getText().toString());
                        if (amount > 0) {
                            int quantity = Integer.parseInt(charSequence.toString());
                            amountEditText.setText("" + quantity * amount);
                        }
                    }

                } else {
                    amountEditText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().startsWith("0")){
                    String value = "";
                    if(editable.toString().length()==1) {
                        value = "";
                    }
                    else if(editable.toString().length()>0){
                        value = editable.toString().substring(1);
                    }
                    quantityEditText.setText(value);
                }
                //String product_id = (String) holder.et_amount.getTag();
                int childListPosition = CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.indexOf(new CropsFragmentAdvancebookingActivity.Child(results.get(position).get("productId")));

                try {
                    if (childListPosition > -1) {
                        CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).qty = quantityEditText.getText().toString();
                        Log.e(" ---- quantity ---- ", "" + CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).qty);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // }
                onAmountChangeListener.beforeAmountChanged(quantityEditText, "" + quantityEditText.getText().toString());

            }
        }
        private class AmountTextWatcher implements TextWatcher{

            int position;
            EditText amountEditText;
            public void updatePosition(int position,EditText amountEditText){
                this.position = position;
                this.amountEditText = amountEditText;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String product_id = (String) holder.et_amount.getTag();

                int childListPosition = CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.indexOf(new CropsFragmentAdvancebookingActivity.Child(results.get(position).get("productId")));

                //  if(item_position<=CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.size()-1) {
                try {
                    if(childListPosition>-1) {
                        CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).amount = amountEditText.getText().toString();
                        Log.e("   pos amount ----", "" + CropsFragmentAdvancebookingActivity.globalGroup.get(groupPosition).childList.get(childListPosition).amount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                onAmountChangeListener.onAmountChanged();
                // }
            }


        }

    }
}










