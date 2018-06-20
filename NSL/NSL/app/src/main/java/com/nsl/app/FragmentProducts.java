package com.nsl.app;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProducts extends Fragment {

    ViewPager mViewPager;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
   //private SliderLayout imageSlider;
   SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_products, container, false);


// get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return view;
    }

    /*
      * Preparing the list data
      */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Chillies");
        listDataHeader.add("Jowar");
        listDataHeader.add("Bitter guard");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Test product");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("test");
        nowShowing.add("YS-JOW-L-369-3KG");
        nowShowing.add("test");


        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("YS-BAJ-L-234-1.5KG");


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

}
