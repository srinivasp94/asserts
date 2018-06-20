package com.nsl.app.stockreturns;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsl.app.Crops;
import com.nsl.app.CustomGridtAdapterTenImg;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.DistributorsViewStockMovementActivity;
import com.nsl.app.OnAmountChangeListener;
import com.nsl.app.Products;
import com.nsl.app.R;
import com.nsl.app.SelectedCities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCTS_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_BRAND_NAME;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_CROP_ID;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_PRODUCTS;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCropsViewFragmentStockReturnsList extends Fragment {

    public NewCropsViewFragmentStockReturnsList() {
        // Required empty public constructor
    }

    ArrayList<CropsFragmentViewStockReturnsActivity.Child> cList = new ArrayList<>();
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
    public static final String TAG = NewCropsViewFragmentStockReturnsList.class.getSimpleName();
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
    int groupPosition, mPosition;
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

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + selected_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_BRAND_NAME + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + company_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_cropid;
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

        HashMap<String, String> result;
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row__view_stockmovement_products, parent, false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent viewdistributors = new Intent(getActivity(),DistributorsViewStockMovementActivity.class);
                    startActivity(viewdistributors);
                }
            });
            return new ViewHolder(v);

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {



            mPosition = position;
            result    = results.get(position);
            selctedSchemeId = 0;

            holder.time.setText(result.get("productId"));
            //  holder.time.setText(result.get("productId"));
            //  holder.spin_schemes.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item,R.id.customSpinnerItemTextView, adapter_schemes));

            holder.checkBox.setText(result.get("productName"));
            //  holder.checkBox.setChecked(checkedStatesType.get(position));

            /* if(selection.contains(result.get("productId")))
                holder.checkBox.setChecked(true);
            else
                holder.checkBox.setChecked(false);
            */


/*
              List<CropsFragmentViewStockReturnsActivity.Child> mchildList = CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList;

            if(mchildList!=null && mchildList.size()>0){
                int  childListPosition = mchildList.indexOf(new CropsFragmentViewStockReturnsActivity.Child(results.get(mPosition).get("productId")));
                if(childListPosition>-1) {
                    CropsFragmentViewStockReturnsActivity.Child child = CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition);
                    holder.et_schemeprice.setText(child.scheme_amount);
                    holder.et_qty.setText(child.qty);
                    holder.et_amount.setText(child.amount);
                    final ArrayList<Schemes>  adapter_schemes = (ArrayList<Schemes>) db.getSchemesByProducId(child.product_id);
                    Schemes schemes = new Schemes();
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
                                CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition).scheme_id = "" + selctedSchemeId;

                                    holder.et_schemeprice.setFocusable(false);
                                holder.et_schemeprice.setText(adapter_schemes.get(position).getscheme_value());
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
                  //  holder.checkBox.setChecked(true);
                    holder.et_amount.setFocusable(false);
                    holder.et_qty.setEnabled(true);
                    holder.spin_schemes.setEnabled(true);
                    holder.et_schemeprice.setEnabled(true);


                }
            else {
                   // holder.checkBox.setChecked(false);
                    holder.et_amount.setFocusable(false);
                    holder.et_qty.setEnabled(false);
                    holder.spin_schemes.setEnabled(false);
                    holder.et_schemeprice.setEnabled(false);
                }
            }*/

            /*holder.checkBox.setTag(mPosition);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    int item_position = (int) compoundButton.getTag();


//                        checkedStatesType.set(item_position, checked);
                    Log.i("   PName ----", "" + result.get("productName"));

                    if (checked) {
                        holder.et_schemeprice.setEnabled(true);
                        holder.et_amount.setFocusable(false);
                        holder.et_qty.setEnabled(true);
                        holder.spin_schemes.setEnabled(true);

                        holder.et_cs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                                LayoutInflater inflater           = getActivity().getLayoutInflater();
                                final View dialogView             = inflater.inflate(R.layout.alert_addnew_cs, null);
                                dialogBuilder.setView(dialogView);
                                final AlertDialog b               = dialogBuilder.create();
                                final EditText et_status          = (EditText) dialogView.findViewById(R.id.et_csqty);
                                final Button status_submit        = (Button)   dialogView.findViewById(R.id.btn_status_submit);
                                final Button status_cancel        = (Button)   dialogView.findViewById(R.id.btn_status_cancel);

                                et_status.setSelection(et_status.length());


                                status_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        b.dismiss();
                                    }
                                });
                                status_submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogInterface dialog = null;



                                        b.dismiss();
                                    }
                                });

                                b.show();


                            }
                        });

                        holder.et_sp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                                LayoutInflater inflater           = getActivity().getLayoutInflater();
                                final View dialogView             = inflater.inflate(R.layout.alert_addnew_stockmovement, null);
                                dialogBuilder.setView(dialogView);
                                final AlertDialog b = dialogBuilder.create();
                                final EditText et_status          = (EditText) dialogView.findViewById(R.id.et_qty);
                                final EditText et_date            = (EditText) dialogView.findViewById(R.id.et_date);

                                final Button status_submit        = (Button) dialogView.findViewById(R.id.btn_status_submit);
                                final Button status_cancel        = (Button) dialogView.findViewById(R.id.btn_status_cancel);

                                et_status.setSelection(et_status.length());

                                et_date.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Calendar mcurrentDate   = Calendar.getInstance();
                                        final int mYear         = mcurrentDate.get(Calendar.YEAR);
                                        final int mMonth        = mcurrentDate.get(Calendar.MONTH);
                                        final int mDay          = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                                        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                                // TODO Auto-generated method stub
                                                int sel_month = selectedmonth + 1;
                                                String sday = String.valueOf(selectedday);
                                                String smonth = null;
                                                if (sel_month < 10)
                                                    smonth = "0" + sel_month;
                                                else
                                                    smonth = String.valueOf(sel_month);

                                                if (selectedday < 10)
                                                    sday = "0" + selectedday;
                                                else
                                                    sday = String.valueOf(selectedday);

                                                et_date.setText(selectedyear + "-" + smonth + "-" + sday);
                                            }
                                        }, mYear, mMonth, mDay);
                                        //mDatePicker.setTitle("Select date");
                                        mDatePicker.show();
                                    }
                                });
                                status_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        b.dismiss();
                                    }
                                });
                                status_submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogInterface dialog = null;



                                        b.dismiss();
                                    }
                                });

                                b.show();
                            }
                        });



                        CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).isChecked = holder.checkBox.isChecked();
                        List<CropsFragmentViewStockReturnsActivity.Child> childList = CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList;
                        int childListPosition = childList.indexOf(new CropsFragmentViewStockReturnsActivity.Child(results.get(item_position).get("productId")));

                        if (childListPosition == -1) {
                            childListPosition = childList.size();
                            CropsFragmentViewStockReturnsActivity.Child child = new CropsFragmentViewStockReturnsActivity.Child();
                            CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.add(child);
                        }


                        CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition).name = results.get(item_position).get("productName");
                        CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition).isChecked = holder.checkBox.isChecked();
                        CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition).product_id = results.get(item_position).get("productId");
                        CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition).id = results.get(item_position).get("productId");





                    } else {
                        holder.et_amount.setFocusable(false);
                        holder.et_qty.setEnabled(false);
                        holder.spin_schemes.setEnabled(false);
                        holder.spin_schemes.setSelection(0);
                        holder.et_schemeprice.setEnabled(false);
                        holder.et_amount.setText("");
                        holder.et_qty.setText("");
                        //  selection.remove(results.get(item_position).get("productId"));
                        List<CropsFragmentViewStockReturnsActivity.Child> childList = CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList;
                        if (childList != null && childList.size() > 0) {
                            int index = childList.indexOf(new CropsFragmentViewStockReturnsActivity.Child(results.get(item_position).get("productId")));
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



                    Log.i("   Size ----", "" + CropsFragmentViewStockReturnsActivity.globalGroup.size());
                    Log.i("   GName ----", "" + CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition));
                    //((CropsFragmentViewStockReturnsActivity)context).globalGroup.get(result).isChecked=checkBox.isChecked();
                    responseText1 = new StringBuffer();
//
                }
            });*/

            holder.et_amount.setTag(position);
                holder.et_amount.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //String product_id = (String) holder.et_amount.getTag();
                        int position = (int) holder.et_amount.getTag();
                        int childListPosition = CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.indexOf(new CropsFragmentViewStockReturnsActivity.Child(results.get(position).get("productId")));

                        //  if(item_position<=CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.size()-1) {
                        try {
                            if(childListPosition>-1) {
                                CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition).amount = holder.et_amount.getText().toString();
                                Log.e("   pos amount ----", "" + CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition).amount);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String value = holder.et_amount.getText().toString();
                        onAmountChangeListener.onAmountChanged();
                        // }
                    }
                });


            holder.et_qty.setTag(position);
                holder.et_qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (charSequence.length() > 0) {

                            if (holder.et_schemeprice.getText().toString().length() > 0) {
                                int amount = Integer.parseInt(holder.et_schemeprice.getText().toString());
                                if (amount > 0) {
                                    int quantity = Integer.parseInt(charSequence.toString());
                                    holder.et_amount.setText("" + quantity * amount);
                                }
                            }

                        } else {
                            holder.et_amount.setText("");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //String product_id = (String) holder.et_amount.getTag();
                        int position = (int) holder.et_amount.getTag();
                        int childListPosition = CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.indexOf(new CropsFragmentViewStockReturnsActivity.Child(results.get(position).get("productId")));

                        try {
                            if(childListPosition>-1) {
                                CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition).qty = holder.et_qty.getText().toString();
                                Log.e(" ---- quantity ---- ", "" + CropsFragmentViewStockReturnsActivity.globalGroup.get(groupPosition).childList.get(childListPosition).qty);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // }
                        onAmountChangeListener.beforeAmountChanged(holder.et_qty,""+holder.et_qty.getText().toString());

                    }
                });

        }

        @Override
        public int getItemCount() {
            return results.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView time;
            public EditText et_schemeprice;
            public EditText et_amount, et_qty,et_cs,et_sp;
            public TextView checkBox;
            public Spinner  spin_schemes;
            public Button   btn_addsp;


            public ViewHolder(View itemView) {

                super(itemView);

                time           = (TextView) itemView.findViewById(R.id.code);
                et_schemeprice = (EditText) itemView.findViewById(R.id.et_schemeprice);
                et_amount      = (EditText) itemView.findViewById(R.id.et_amount);
                et_cs          = (EditText) itemView.findViewById(R.id.et_cs);
                et_sp          = (EditText) itemView.findViewById(R.id.et_sp);
                et_qty         = (EditText) itemView.findViewById(R.id.et_qty);
                checkBox       = (TextView) itemView.findViewById(R.id.chk_product);
                spin_schemes   = (Spinner) itemView.findViewById(R.id.spin_scheme);
                btn_addsp      = (Button) itemView.findViewById(R.id.btn_addsp);


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


    }
}










