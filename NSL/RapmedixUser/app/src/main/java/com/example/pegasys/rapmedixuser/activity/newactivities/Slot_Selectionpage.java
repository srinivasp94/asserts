package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.SlotReq;
import com.example.pegasys.rapmedixuser.activity.pojo.SlotResponse;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Slot_Selectionpage extends AppCompatActivity implements RetrofitResponseListener {

    LinearLayout layout;
    private TextView doctor_row_price, doctor_name, hosp_name, datte, timeW;
    private Button doctor_row_book;
    private GridView slotGrid;
    String d_Id, main_Id, hosp_Id, Hosp_Name, Doc_name, sFee;

    private TabLayout tabLayout;
    ArrayList<String> slotslist = new ArrayList<>();
    ArrayList<String> datess = new ArrayList();
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleDateFormat1;
    private SimpleDateFormat sdf;

    String[] converted = null;
    final ArrayList<String> modifiedDates = new ArrayList<String>();
    String converteDates = "";
    private Object obj;
    ArrayList<String> DatesOfSlots = new ArrayList();
    ArrayList<String> BookedSlots = new ArrayList();
    private String NoOfSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slots_popup);

        final Intent intent = getIntent();
        d_Id = intent.getStringExtra("doctorId");
        Hosp_Name = intent.getStringExtra("HOSP_NAME");
        Doc_name = intent.getStringExtra("doctorName");
        hosp_Id = intent.getStringExtra("HOSP_ID");
        main_Id = intent.getStringExtra("ID");
        sFee = intent.getStringExtra("Fee");

        layout = (LinearLayout) findViewById(R.id.snaklayout);
        tabLayout = (TabLayout) findViewById(R.id.dates_tab);
        doctor_row_price = (TextView) findViewById(R.id.doctor_row_price);
        doctor_name = (TextView) findViewById(R.id.doctor_name);
        hosp_name = (TextView) findViewById(R.id.hspital_name);
        datte = (TextView) findViewById(R.id.date);
        timeW = (TextView) findViewById(R.id.time);
        timeW.setText("00:00");
        doctor_row_book = (Button) findViewById(R.id.doctor_row_book);
        slotGrid = (GridView) findViewById(R.id.slots_list);
        hosp_name.setText(Hosp_Name);
        doctor_name.setText(Doc_name);
        doctor_row_price.setText(sFee);
        get_dates();

        Drawable img1 = getResources().getDrawable(R.drawable.slot_date_bg_selector);
        Drawable img2 = getResources().getDrawable(R.drawable.slot_date_bg_selector2);

        for (int i = 0; i < datess.size(); i++) {
            converted = datess.get(i).split("-");
            converteDates = converted[2] + "-" + converted[1] + "-" + converted[0];
            modifiedDates.add(converteDates);
            Log.e("TheDataofString", "938   " + converteDates);
            View relativeLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.slot_date_item, tabLayout, false);
            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.month);
            TextView date = (TextView) relativeLayout.findViewById(R.id.date);
            tabTextView.setBackgroundDrawable(img1);
            date.setBackgroundDrawable(img2);

            Date dateee = null;
            try {
                dateee = new SimpleDateFormat("dd-MM-yyyy").parse(converteDates);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date d = new Date();
            simpleDateFormat = new SimpleDateFormat("dd");
            simpleDateFormat1 = new SimpleDateFormat("MMM");
            sdf = new SimpleDateFormat("EEE");
            String dayOfTheWeek = sdf.format(d);
            String goal = sdf.format(dateee);
            String day = simpleDateFormat.format(dateee);
            tabTextView.setText(goal);
            date.setText(day);
            tabLayout.addTab(tabLayout.newTab().setCustomView(relativeLayout));
            setmethod();
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                timeW.setText("00:00");
                datte.setText(modifiedDates.get(tab.getPosition()).toString());
                SlotReq req = new SlotReq();
                req.date = modifiedDates.get(tab.getPosition()).toString();
                req.doctorId = d_Id;
                req.doctorworkingdetailsId = main_Id;

                try {
                    obj = Class.forName(SlotReq.class.getName()).cast(req);
                    Log.d("obj", obj.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                new RetrofitRequester(Slot_Selectionpage.this).callPostServices(obj, 1, "/webservices/doctorschedule", true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                DatesOfSlots.clear();
//                slotGrid.deferNotifyDataSetChanged();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                DatesOfSlots.clear();
//                slotGrid.deferNotifyDataSetChanged();
//                new RetrofitRequester(Slot_Selectionpage.this).callPostServices(obj, 1, "/webservices/doctorschedule", true);
            }
        });
        doctor_row_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeW.getText().toString().isEmpty() || timeW.getText().toString().equals("00:00")) {
                    Toast.makeText(Slot_Selectionpage.this, "Select slot", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent1 = new Intent(Slot_Selectionpage.this, Selected_candidate_page.class);
                    intent1.putExtra("DocId", d_Id);
                    intent1.putExtra("WorkingId", main_Id);
                    intent1.putExtra("HospitalId", hosp_Id);
                    intent1.putExtra("SelectedDate", modifiedDates.get(tabLayout.getSelectedTabPosition()));
                    intent1.putExtra("Price", sFee);
                    intent1.putExtra("TimeSlot", timeW.getText().toString());
                    Log.i("data", d_Id + " workid" + main_Id + " hospid" + hosp_Id + " date " + modifiedDates.get(tabLayout.getSelectedTabPosition()) + " fee " + sFee + " time" + timeW.getText().toString());
                    startActivity(intent1);
                }


            }
        });
    }

    public void setmethod() {
        datte.setText(modifiedDates.get(0).toString());
        SlotReq req = new SlotReq();
        req.date = modifiedDates.get(0).toString();
        req.doctorId = d_Id;
        req.doctorworkingdetailsId = main_Id;

        try {
            obj = Class.forName(SlotReq.class.getName()).cast(req);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(Slot_Selectionpage.this).callPostServices(obj, 1, "/webservices/doctorschedule", true);
    }

    public void get_dates() {
        for (int i = 0; i < 7; i++) {
            //this.datess.add(getCalculatedDate("dd-MM-yyyy", i));
            this.datess.add(getCalculatedDate("yyyy-MM-dd", i));
            Log.i("Dates", datess.get(i).toString());
            Collections.sort(this.datess);
        }
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat s = new java.text.SimpleDateFormat(dateFormat);
        cal.add(java.util.Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        DatesOfSlots.clear();
        BookedSlots.clear();
        slotslist.clear();

        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(Slot_Selectionpage.this, "Please Retry", Toast.LENGTH_SHORT).show();

        } else {
            SlotResponse respo = Common.getSpecificDataObject(objectResponse, SlotResponse.class);
            Gson gson = new Gson();
            if (respo.status.equals("success")) {
                DatesOfSlots.clear();
                BookedSlots.clear();
                slotslist.clear();

                slotslist = (ArrayList<String>) respo.schedule;
                for (int i = 0; i < slotslist.size(); i++) {
                    NoOfSlots = modifiedDates.get(0).toString() + " " + slotslist.get(i).toString();
                }
                java.util.Calendar c = java.util.Calendar.getInstance();
                java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm a");

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        R.layout.timeslot_list, R.id.timeslot, slotslist);

                slotGrid.setAdapter(adapter);
                slotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int posi, long l) {
                        String mSlot = slotslist.get(posi).toString();
                        CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.timeslot);
                        slotGrid.getChildAt(posi).setBackgroundResource(R.drawable.edittext_border_red);
//                        if (checkedTextView.isChecked()) {
//                            checkedTextView.setBackgroundColor(Color.RED);
//                        } else {
//                            checkedTextView.setBackgroundColor(Color.BLUE);
//                        }
                        timeW.setText(mSlot);
                    }
                });

            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, slotslist);

                slotGrid.setAdapter(adapter);


            }
        }

    }
}
