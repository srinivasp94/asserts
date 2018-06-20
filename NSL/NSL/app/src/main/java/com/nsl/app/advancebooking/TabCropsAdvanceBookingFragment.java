package com.nsl.app.advancebooking;


import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.nsl.app.Crops;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.app.DatabaseHandler.TABLE_COMPANY_DIVISION_CROPS;
import static com.nsl.app.DatabaseHandler.TABLE_CROPS;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabCropsAdvanceBookingFragment extends Fragment {

    Fragment fragment = null;

    public TabCropsAdvanceBookingFragment() {
        // Required empty public constructor
    }

    SharedPreferences   sharedpreferences;
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

       /* HashMap<String, String> map = new HashMap<String, String>();
        for (int i=0 ; i<9 ;i++){

            // adding each child node to HashMap key => value
            map.put("cropId",           "cropId");
            map.put("cropName",         "cropName");
            map.put("serviceImage",        "serviceImage");

            Titles.add(map);
        }*/

        new GetCrops().execute();

        return view;
    }

    public void notifyTokenAmountChanged(){
        if(pagerclass!=null && Crops!=null){
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
            //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
            //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
            String selectQuery = "SELECT DISTINCT CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + "," + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC." + KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + division_id;
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
            if (Crops.size()==0){


                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_alert_salesorder);


                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.tv_message);
                text.setText("" + "No Crops & Products Available !");

                ImageView sucessimage = (ImageView) dialog.findViewById(R.id.iv_sucess);
                sucessimage.setVisibility(View.VISIBLE);
                //
                // ImageView failureimage = (ImageView) dialog.findViewById(R.id.iv_failure);

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
                dialog.show();
            }

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
            fragment      = new NewCropsFragmentAdvanceBookingList();
            selections.put(cropId, new HashSet<String>());
            Bundle bundle = new Bundle();
            bundle.putString("id", cropId);
            bundle.putString("company_id",  company_id);
            bundle.putString("division_id", division_id);
            bundle.putString("customer_id", customer_id);
            bundle.putInt("position", position);
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
            CropsFragmentAdvancebookingActivity.Group g = new CropsFragmentAdvancebookingActivity.Group();
            g.name = results.get(position).get("cropName");
            g.id = results.get(position).get("cropId");
            g.company_id = company_id;
            g.devison_id = division_id;
            g.costumer_id = customer_id;
            CropsFragmentAdvancebookingActivity.globalGroup.add(g);
            return results.get(position).get("cropName");
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {
            this.results = results;
            notifyDataSetChanged();
        }

    }
}
