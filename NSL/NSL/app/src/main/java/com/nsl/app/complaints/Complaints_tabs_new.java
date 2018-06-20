package com.nsl.app.complaints;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;

/**
 * Created by admin on 1/5/2017.
 */


public class Complaints_tabs_new extends Fragment {

    ViewPager mainPager;
    TabLayout tabLayout;
    int current = 0;
    DatabaseHandler db;

    // private int[] tabIcons = { R.drawable.my_icon, R.drawable.my_icon_flower,};

    public Complaints_tabs_new() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tab_complaints, container, false);

        //  String strtext = getArguments().getString("value");
       /* if(strtext.equals("1")){
            current=1;
        }
        else if(strtext.equals("0")){
            current=0;
        }*/
        mainPager = (ViewPager) view.findViewById(R.id.mehndi_mainPager);
        tabLayout = (TabLayout) view.findViewById(R.id.mehndi_tabLayout);
       /* Bundle bundle=getArguments();
        String strtext = bundle.getString("value");
        if(strtext.equals("1")){
            current=1;
        }
        else if(strtext.equals("0")){
            current=0;
        }
*/
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm;
                FragmentTransaction ft;
                fm = getActivity().getSupportFragmentManager();
                ft = fm.beginTransaction().replace(R.id.content_frame, new MainTab_Complaints());
                ft.commit();

            }
        });

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(mainPager);

                //  setupTabIcons();
            }
        });

        MainPagerClass mainPagerClass = new MainPagerClass(getChildFragmentManager());
        mainPager.setAdapter(mainPagerClass);
        mainPager.setCurrentItem(current);
        return view;
    }
   /* private void setupTabIcons() {
     //   tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }*/

    class MainPagerClass extends FragmentPagerAdapter {

        public MainPagerClass(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            if (position == 0) {
                fragment = new FragmentComplaintsRegulatorylist();
            }
            if (position == 1) {
                fragment = new FragmentComplaintsProductslis();
            }


            return fragment;
        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //String title = new String();
            if (position == 0) {
                return "Regulatory";
            }
            if (position == 1) {
                return "Product";
            }


            return null;
        }

    }
}

