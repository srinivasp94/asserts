package com.example.pegasys.rapmedixuser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.SlidingImage_Adapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends AppCompatActivity {
//    Button Registration, login;

    Button Registration, login;


    private static ViewPager mPager;
    private static int currentPage = 1;
    TextView text1;
    LinearLayout linearLayout;
    private static int NUM_PAGES = 0;

    private static final Integer[] IMAGES = {
            R.drawable.img1_3x,
            R.drawable.img2_3x,
            R.drawable.img3_3x};

    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        Registration = findViewById(R.id.Registration);
        login = findViewById(R.id.Login);

        init();
    }

    private void init() {

        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(FirstActivity.this, ImagesArray));
        CirclePageIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
        indicator.setRadius(3 * density);
        NUM_PAGES = IMAGES.length;
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2000);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
    }
}
