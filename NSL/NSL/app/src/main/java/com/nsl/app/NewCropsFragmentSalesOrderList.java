package com.nsl.app;


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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_CATALOG_URL;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_PACKETS_COUNT;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_BRAND_NAME;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_CROP_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_DISCOUNT;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_PRODUCTS;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCropsFragmentSalesOrderList extends Fragment {

    private String customer_id;

    public NewCropsFragmentSalesOrderList() {
        // Required empty public constructor
    }

    ArrayList<CropsFragmentSalesorderActivity.Child> cList = new ArrayList<>();
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

    public static final String TAG = NewCropsFragmentSalesOrderList.class.getSimpleName();
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
        View v = inflater.inflate(R.layout.fragment_newrequestlist, container, false);

        db = new DatabaseHandler(getActivity());

        recyclerView  = (RecyclerView) v.findViewById(R.id.listTypes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sel_cropid    = getArguments().getString("id");
        company_id    = getArguments().getString("company_id");
        division_id   = getArguments().getString("division_id");
        groupPosition = getArguments().getInt("position");
        customer_id    = getArguments().getString("customer_id");

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

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_BRAND_NAME + "," + KEY_PRODUCTS_PACKETS_COUNT+ "," + KEY_PRODUCT_DISCOUNT+ "," + KEY_PRODUCTS_CATALOG_URL + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + company_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_cropid;
                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                Log.e("Products query ", selectQuery);
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        /*Products products = new Products();
                        products.setProductMasterID(cursor.getString(0));
                        products.setProductName(cursor.getString(1));*/

                        HashMap<String, String> first = new HashMap<String, String>();
                        first.put("productId", cursor.getString(0));
                        first.put("productName", cursor.getString(1));
                        first.put("no_packets", cursor.getString(2));
                        first.put("discount", cursor.getString(3));
                        first.put("quantity_url", cursor.getString(4));

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
        ArrayList<HashMap<String, String>> results;
        private ArrayList filmArray;
        Products products = new Products();
        Context context;

        public ProductsRecyclerAdapter(ArrayList<HashMap<String, String>> results) {
            this.results = results;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_salesorder_products, parent, false);

            return new ViewHolder(v,new QunatityTextWatcher(),new AmountTextWatcher());
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

           int mPosition = position;
            final HashMap<String, String> result = results.get(position);



            //holder.time.setText(result.get("productId")+"pos:"+position+"Id:"+holder.et_schemeprice.getId());

            holder.checkBox.setText(result.get("productName"));

            holder.txt_qty.setText("   Qty("+result.get("no_packets")+")");

            Log.v("POSITION",""+mPosition);
            holder.qunatityTextWatcher.updatePosition(holder.getAdapterPosition(),holder.et_schemeprice,holder.et_qty,holder.et_amount);
            holder.amountTextWatcher.updatePosition(holder.getAdapterPosition(),holder.et_amount);

            List<CropsFragmentSalesorderActivity.Child> mchildList = CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList;

            if(mchildList!=null && mchildList.size()>0){
                int  childListPosition = mchildList.indexOf(new CropsFragmentSalesorderActivity.Child(results.get(mPosition).get("productId")));
                if(childListPosition>-1) {
                    CropsFragmentSalesorderActivity.Child child = CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition);
                    holder.et_schemeprice.setText(child.scheme_amount);
                    holder.et_qty.setText(child.qty);
                    holder.et_amount.setText(child.amount);
                    Products_Pojo products_pojo =  db.getAmountByProducId(child.product_id);
                    if(products_pojo!=null) {
                        holder.et_schemeprice.setText("" + products_pojo._product_price);
                    }
                    holder.checkBox.setOnCheckedChangeListener(null);
                    holder.checkBox.setChecked(true);
                    holder.et_amount.setFocusable(false);
                    holder.et_qty.setEnabled(true);

                    holder.et_schemeprice.setEnabled(true);
                    Log.v("POSITION - childPos",""+childListPosition);

                }
                else {
                    holder.checkBox.setOnCheckedChangeListener(null);
                    holder.checkBox.setChecked(false);
                    holder.et_amount.setFocusable(false);
                    holder.et_qty.setEnabled(false);

                    holder.et_schemeprice.setEnabled(false);
//
                    holder.et_schemeprice.setText("");
                    holder.et_qty.setText("");
                    holder.et_amount.setText("");

                    Log.v("POSITION - childPos",""+childListPosition);

                }
            }

            holder.checkBox.setTag(mPosition);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public float Totaldiscount;

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    int item_position = (int) compoundButton.getTag();


                    // checkedStatesType.set(item_position, checked);
                    Log.i("   PName ----", "" + result.get("productName"));

                    for (int i=0;i<results.size();i++) {
                        Log.d("onCheckedChanged..1",holder.et_schemeprice.getText().toString() );
                    }
                    if (checked) {
                        holder.et_schemeprice.setEnabled(true);
                        holder.et_amount.setFocusable(false);
                        holder.et_qty.setEnabled(true);
                        String product_Id = results.get(item_position).get("productId");

                        String selectQuery1 ="SELECT region_id FROM "+db.TABLE_CUSTOMERS+" WHERE customer_id = "+ customer_id;

                        Cursor cursor = sdbw.rawQuery(selectQuery1, null);
                        //System.out.println("cursor count "+cursor.getCount());
                        String region_id = null;
                        if (cursor.moveToFirst()) {
                            do {

                                region_id =cursor.getString(0);

                                System.out.println("*****@@@@@" + cursor.getString(0));
                                // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                            } while (cursor.moveToNext());
                        }

                        String selectQuery4 ="SELECT * FROM "+db.TABLE_PRODUCT_PRICE+" WHERE "+db.KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID +" = "+ product_Id  +" AND "+db.KEY_TABLE_SCHEME_PRODUCTS_REGION_ID+" = "+region_id;
                        Cursor cursor4 = sdbw.rawQuery(selectQuery4, null);
                        //System.out.println("cursor count "+cursor.getCount());
                        float productPrice = 0;
                        float discount = 0;
                        if (cursor4.moveToFirst()) {


                            productPrice= Float.parseFloat(cursor4.getString(2));
                            discount= Float.parseFloat(cursor4.getString(3));


                        }else{



                            String selectQuery5 ="SELECT * FROM "+db.TABLE_PRODUCT_PRICE+" WHERE "+db.KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID +" = "+ product_Id ;
                            Cursor cursor5 = sdbw.rawQuery(selectQuery5, null);
                            if (cursor5.moveToFirst()) {
                                productPrice= Float.parseFloat(cursor5.getString(2));
                                discount= Float.parseFloat(cursor5.getString(3));
                            }


                        }

                        Totaldiscount = productPrice*discount/100;
                        float discountedprice = productPrice - Totaldiscount;

                        holder.et_schemeprice.setText(String.valueOf(discountedprice));

                        holder.et_amount.setText("");
                        holder.et_qty.setText("");


                        CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).isChecked = holder.checkBox.isChecked();
                        List<CropsFragmentSalesorderActivity.Child> childList = CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList;
                        int childListPosition = childList.indexOf(new CropsFragmentSalesorderActivity.Child(results.get(item_position).get("productId")));

                        if (childListPosition == -1) {
                            childListPosition = childList.size();
                            CropsFragmentSalesorderActivity.Child child = new CropsFragmentSalesorderActivity.Child();
                            CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.add(child);
                        }


                        CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition).name = results.get(item_position).get("productName");
                        CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition).isChecked = holder.checkBox.isChecked();
                        CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition).product_id = results.get(item_position).get("productId");
                        CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition).id = results.get(item_position).get("productId");
                        CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition).scheme_amount =  holder.et_schemeprice.getText().toString();

                    } else {

                        holder.et_amount.setFocusable(false);
                        holder.et_qty.setEnabled(false);
                        holder.et_amount.setText("");
                        holder.et_qty.setText("");
                        holder.et_schemeprice.setEnabled(false);

                        //  selection.remove(results.get(item_position).get("productId"));
                        List<CropsFragmentSalesorderActivity.Child> childList = CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList;
                        if (childList != null && childList.size() > 0) {
                            int index = childList.indexOf(new CropsFragmentSalesorderActivity.Child(results.get(item_position).get("productId")));
                            if (index > -1) {
                                childList.remove(index);

                            }
                        }
                    }


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onAmountChangeListener.onAmountChanged();
                        }
                    }, 10);

                    Log.i("   Size ----", "" + CropsFragmentSalesorderActivity.globalGroup.size());
                    for (int i=0; i< CropsFragmentSalesorderActivity.globalGroup.size();i++) {
                        Log.i("   GName ----", "" + CropsFragmentSalesorderActivity.globalGroup.get(i).name +"Size : "+CropsFragmentSalesorderActivity.globalGroup.get(i).childList.size());

                    }
                    responseText1 = new StringBuffer();

                    for (int i=0;i<results.size();i++) {

                    }
               }
            });

            holder.et_amount.setTag(position);

            holder.et_qty.setTag(position);

        }

        @Override
        public int getItemCount() {
            return results.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView time,txt_qty;
            public EditText et_schemeprice;
            public EditText et_amount, et_qty;
            public CheckBox checkBox;
            QunatityTextWatcher qunatityTextWatcher;
            AmountTextWatcher amountTextWatcher;

            public ViewHolder(View itemView,QunatityTextWatcher qunatityTextWatcher,AmountTextWatcher amountTextWatcher) {

                super(itemView);

                time           = (TextView) itemView.findViewById(R.id.code);
                txt_qty        = (TextView) itemView.findViewById(R.id.txt_qty);
                et_schemeprice = (EditText) itemView.findViewById(R.id.et_schemeprice);
                et_amount      = (EditText) itemView.findViewById(R.id.et_amount);
                et_qty         = (EditText) itemView.findViewById(R.id.et_qty);
                checkBox       = (CheckBox) itemView.findViewById(R.id.chk_product);

                this.qunatityTextWatcher = qunatityTextWatcher;
                this.et_qty.addTextChangedListener(qunatityTextWatcher);

                this.amountTextWatcher   = amountTextWatcher;
                this.et_amount.addTextChangedListener(amountTextWatcher);

            }
        }

        private class QunatityTextWatcher implements TextWatcher {
            int position;
            EditText schemeEditText;
            EditText quantityEditText;
            EditText amountEditText;

            public void updatePosition(int position, EditText schemeEditText, EditText quantityEditText, EditText amountEditText) {
                this.position         = position;
                this.schemeEditText   = schemeEditText;
                this.quantityEditText = quantityEditText;
                this.amountEditText   = amountEditText;
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                HashMap<String, String> productMap = results.get(position);
                Log.d("onTextChanged",productMap.get("discount")+" position: "+position);

                if (charSequence.length() > 0) {

                    if (schemeEditText.getText().toString().length() > 0) {
                        float amount     = Float.parseFloat(schemeEditText.getText().toString());
                        if (amount > 0) {
                            int quantity = Integer.parseInt(charSequence.toString());
                            amountEditText.setText("" + quantity * amount);

                            if (productMap != null) {

                                String no_packets   = productMap.get("no_packets");
                                String quantity_url = productMap.get("quantity_url");
                                String discount     = productMap.get("discount");
                                if (no_packets != null) {
                                    if(discount==null || discount.length()==0){
                                        discount="5";
                                    }
                                    int packetsValue  = Integer.parseInt(no_packets);
                                    int discountValue = Integer.parseInt(discount);
                                    float finalAmount = packetsValue * quantity * amount;
                                    finalAmount = finalAmount - (finalAmount*(discountValue/100));
                                    amountEditText.setText("" +finalAmount);

                                }
                            }
                        }

                    }

                } else {
                    amountEditText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int childListPosition = CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.indexOf(new CropsFragmentSalesorderActivity.Child(results.get(position).get("productId")));

                try {
                    if (childListPosition > -1) {
                        CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition).qty = quantityEditText.getText().toString();
                        Log.e(" ---- quantity ---- ", "" + CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition).qty);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // }

            }
        }

        private class AmountTextWatcher implements TextWatcher {

            int position;
            EditText amountEditText;

            public void updatePosition(int position, EditText amountEditText) {
                this.position = position;
                this.amountEditText = amountEditText;
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {



                int childListPosition = CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.indexOf(new CropsFragmentSalesorderActivity.Child(results.get(position).get("productId")));

                //  if(item_position<=CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.size()-1) {
                try {

                    Log.d("POsition: ","childListPosition :"+childListPosition+"groupPosition: "+groupPosition);
                    if(childListPosition>-1) {
                        CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition).amount = amountEditText.getText().toString();
                        Log.e("   pos amount ----", "" + CropsFragmentSalesorderActivity.globalGroup.get(groupPosition).childList.get(childListPosition).amount);
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










