package com.nsl.app.complaints;



import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsl.app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainTab_Complaints extends Fragment {

    ViewPager mainPager;
    TabLayout tabLayout;
    // private int[] tabIcons = { R.drawable.my_icon, R.drawable.my_icon_flower,};

    public MainTab_Complaints() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_complaints1, container, false);

        mainPager = (ViewPager) view.findViewById(R.id.mehndi_mainPager);
        tabLayout = (TabLayout) view.findViewById(R.id.mehndi_tabLayout);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(mainPager);

                //  setupTabIcons();
            }
        });

        MainPagerClass mainPagerClass = new MainPagerClass(getChildFragmentManager());
        mainPager.setAdapter(mainPagerClass);
        mainPager.setCurrentItem(0);
        return view;
    }
   /* private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
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
                fragment = new FragmentComplaintsRegulatory() ;
            }
            if (position == 1) {
                fragment = new FragmentComplaintsProducts();
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

