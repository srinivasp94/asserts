package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.fragments.ActiveFrag;
import com.example.pegasys.rapmedixuser.activity.fragments.Addfamilymemberfragment;
import com.example.pegasys.rapmedixuser.activity.fragments.CancelledFrag;
import com.example.pegasys.rapmedixuser.activity.fragments.CompletedFrag;
import com.example.pegasys.rapmedixuser.activity.fragments.Familymemberfragment;
import com.example.pegasys.rapmedixuser.activity.fragments.PendingFragment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        adapter.addFragment(new ActiveFrag(), "Active");
        adapter.addFragment(new PendingFragment(), "Pending");
        adapter.addFragment(new CompletedFrag(), "Completed");
        adapter.addFragment(new CancelledFrag(), "Cancelled");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
