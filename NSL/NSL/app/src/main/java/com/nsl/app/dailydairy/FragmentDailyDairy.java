package com.nsl.app.dailydairy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class FragmentDailyDairy extends Fragment {
    DatabaseHandler db;

    private ListView listView;
    String[] Name={" History "," My Daily Diary "};


    public FragmentDailyDairy() {

    }
    public FragmentDailyDairy(DatabaseHandler db) {
        this.db = db;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_dairy, container, false);

        listView = (ListView) view.findViewById(R.id.listView);

        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<2;i++){
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
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                view.setSelected(true);

                if (position==0){
                    Intent newbooking = new Intent(getActivity(),DailyDiaryHistoryActivity.class);
                    startActivity(newbooking);
                }
                else if(position==1){
                    Intent viewbooking = new Intent(getActivity(),DailyDairyActivity.class);
                    startActivity(viewbooking);
                }
            }
        });


        return view;


    }


}
