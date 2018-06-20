package com.nsl.app.stockreturns;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.nsl.app.Constants;
import com.nsl.app.Crops;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDERDETAILS;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabCropsStockReturnsFragment extends Fragment {

    Fragment fragment = null;
    private String userId;
    private String mo_id;

    public TabCropsStockReturnsFragment() {
        // Required empty public constructor
    }

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String url, response;
    ProgressDialog pDialog;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    Toolbar mToolbar;
    ArrayList<HashMap<String, String>> Crops = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map = new HashMap<String, String>();
    // adding each child node to HashMap key => value
    int size;
    String company_id, customer_id, division_id, division_name;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    Pagerclass pagerclass;
    int   role ;
    public static HashMap<String, HashSet<String>> selections = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        db = new DatabaseHandler(getActivity());
        company_id = sharedpreferences.getString("company_id", "");
        customer_id = sharedpreferences.getString("customer_id", "");
        division_id = sharedpreferences.getString("division_id", "");
        division_name = sharedpreferences.getString("division", "");
        userId=sharedpreferences.getString(Constants.SharedPrefrancesKey.USER_ID,null);
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        mo_id=userId;
        if (role != Constants.Roles.ROLE_7) {
            mo_id = sharedpreferences.getString("mo_id", "");


        }
        new GetCrops().execute();

        return view;
    }

    public void notifyTokenAmountChanged() {
        if (pagerclass != null && Crops != null) {
            refreshCropsAdapter();
        }
    }

    private class GetCrops extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... arg0) {

            Crops.clear();
            //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + selected_division_id;
            //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
           // String selectQuery = "SELECT DISTINCT CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + "," + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC." + KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + division_id;
            String selectQuery ="SELECT crops.crop_id,crop_name FROM "+TABLE_SERVICEORDERDETAILS+ " left join "+ TABLE_SERVICEORDER +" on service_order_details.service_relation_id=service_order.service_id left join crops on crops.crop_id=service_order_details.crop_id where user_id="+mo_id+" and service_order.division_id="+division_id+" and service_order.customer_id="+customer_id+" group by (service_order_details.crop_id)";

            Log.e("Crops Query", selectQuery);
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    com.nsl.app.Crops crops = new Crops();

                    crops.setCropMasterID(cursor.getString(0));

                    crops.setCropName(cursor.getString(1));

                    HashMap<String, String> map = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
                    map.put("cropId", cursor.getString(0));
                    map.put("cropName", cursor.getString(1));
                    //map.put("serviceImage",        obj.getString("serviceImage"));

                    Crops.add(map);
                    System.out.println("*****" + Crops.size() + ":" + cursor.getString(1));


                } while (cursor.moveToNext());
            }
            System.out.println("*****" + Crops.size() + ":");
            return response;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            refreshCropsAdapter();
        }
    }

    private void refreshCropsAdapter() {
        try {
            pagerclass = new Pagerclass(getChildFragmentManager(), Crops);
            //pagerclass.notifyDataSetChanged();
            mViewPager.setAdapter(pagerclass);

            mTabLayout.post(new Runnable() {
                @Override
                public void run() {
                    mTabLayout.setupWithViewPager(mViewPager);
                }
            });
            pagerclass.updateResults(Crops);
            pagerclass.notifyDataSetChanged();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Pagerclass extends FragmentPagerAdapter {

        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();

        public Pagerclass(FragmentManager fm, ArrayList<HashMap<String, String>> results) {
            super(fm);
            this.results = results;
        }

        @Override
        public Fragment getItem(int position) {
            String cropId = results.get(position).get("cropId");
            fragment = new NewCropsFragmentStockReturnsList();
            selections.put(cropId, new HashSet<String>());
            Bundle bundle = new Bundle();
            bundle.putString("id", cropId);
            bundle.putString("company_id", company_id);
            bundle.putString("division_id", division_id);
            bundle.putString("customer_id", customer_id);
            bundle.putInt("position", position);
            bundle.putString("crop_id", cropId);
            bundle.putString("mo_id", mo_id);
            fragment.setArguments(bundle);
            // Toast.makeText(getActivity(), "results.get(position).get(\"cropName\")", Toast.LENGTH_SHORT).show();
            return fragment;
        }

        @Override
        public int getCount() {
            return results.size();

        }

        @Override
        public CharSequence getPageTitle(int position) {
            CropsFragmentStockReturnsActivity.Group g = new CropsFragmentStockReturnsActivity.Group();
            g.name = results.get(position).get("cropName");
            g.id = results.get(position).get("cropId");
            g.company_id = company_id;
            g.devison_id = division_id;
            g.costumer_id = customer_id;
            CropsFragmentStockReturnsActivity.globalGroup.add(g);
            return results.get(position).get("cropName");
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {
            this.results = results;
            notifyDataSetChanged();
        }

    }
}
