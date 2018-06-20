package com.nsl.app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPlanner extends Fragment {

    ViewPager mViewPager;
    public static  String curDate;
    CalendarView calendarView;
    //private SliderLayout imageSlider;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                String d = String.valueOf(dayOfMonth);
                if (d.length()==1){
                    d = "0"+d;
                }
                else{

                }
                int m = month+1;
                String strmonth = String.valueOf(m);
                if (strmonth.length()==1){
                    strmonth = "0"+strmonth;
                }
                else{

                }
                curDate  = String.valueOf(year)+"-"+strmonth+"-"+d;
                //Toast.makeText(getActivity(), curDate, Toast.LENGTH_SHORT).show();

                Intent plannerone = new Intent(getActivity(),PlanerOneActivity.class);
                startActivity(plannerone);
            }
        });


        return view;
    }


}
