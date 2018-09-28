package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.Home_page;
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
    private ImageView backButton;
    private int input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager1);
        tabLayout = findViewById(R.id.tab);

        Intent intent = getIntent();
        if (intent != null) {
            input = intent.getIntExtra("Input",1);
        }

        backButton = findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentActivity.this, Home_page.class);
                startActivity(intent);
            }
        });

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        if (input==1)
        viewPager.setCurrentItem(0);
        else if (input==2)
            viewPager.setCurrentItem(1);
        else if (input==3)
            viewPager.setCurrentItem(2);
        else
            viewPager.setCurrentItem(0);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
