package com.nsl.app.stockreturns;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nsl.app.R;
import com.nsl.app.SchemesActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStockReturns extends Fragment {
    public static final String REQUEST_TYPE = "request_type";
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr=null;
    ProgressDialog progressDialog;


    private ListView listView;
    private ItemfavitemAdapter adapter;

    String jsonData,userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String[] Name={"View Stock Supply"};
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view         = inflater.inflate(R.layout.fragment_orderindent, container, false);
        sharedpreferences =  getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        listView = (ListView) view.findViewById(R.id.listView);
        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.rl_schemes);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent schemes = new Intent(getActivity(),SchemesActivity.class);
                startActivity(schemes);
            }
        });
        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<1;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt",  Name[i]);

            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "txt" };

        // Ids of views in listview_layout
        int[] to = { R.id.tv_type};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.row_advancebooking, from, to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                /*if (i==0){
//                    Intent newbooking  = new Intent(getActivity(),NewViewStockMovementChooseActivity.class);
                    Intent newbooking  = new Intent(getActivity(),NewViewStockMovementChooseActivity.class);
                    newbooking.putExtra("selection","distributor");
                    startActivity(newbooking);
                }
                else{
*/
                    Intent newbooking  = new Intent(getActivity(),NewStockReturnsChooseActivity.class);
                    newbooking.putExtra("selection","adv");
                    startActivity(newbooking);
                //}
            }
        });


        return view;
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String,String>>();


        public ItemfavitemAdapter(Context context,ArrayList<HashMap<String, String>> results) {
            this.context = context;
            this.results = results;

        }
        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if(convertView == null)
            {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater     = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView                 = inflater.inflate(R.layout.row_advancebooking, parent, false);
                holder.itemname             = (TextView)convertView.findViewById(R.id.tv_company);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
           //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.itemname.setText(results.get(position).get("name"+"-"+"company_code"));

            return convertView;
        }


        public class ViewHolder
        {
            public TextView  itemname;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }




}
