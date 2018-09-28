package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ImageView;
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
    private int nPrevSelGridItem = -1;
//    private TextView textView_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slots_popup);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView backButton = toolbar.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final Intent intent = getIntent();
        d_Id = intent.getStringExtra("doctorId");
        Hosp_Name = intent.getStringExtra("HOSP_NAME");
        Doc_name = intent.getStringExtra("doctorName");
        hosp_Id = intent.getStringExtra("HOSP_ID");
        main_Id = intent.getStringExtra("ID");
        sFee = intent.getStringExtra("Fee");

        layout = findViewById(R.id.snaklayout);
        tabLayout = findViewById(R.id.dates_tab);
        doctor_row_price = findViewById(R.id.doctor_row_price);
        doctor_name = findViewById(R.id.doctor_name);
        hosp_name = findViewById(R.id.hspital_name);
        datte = findViewById(R.id.date);
        timeW = findViewById(R.id.time);
        timeW.setText("00:00");
        doctor_row_book = findViewById(R.id.doctor_row_book);
        slotGrid = findViewById(R.id.slots_list);
//        textView_nodata = (TextView)findViewById(R.id.text_nodata);
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
            View relativeLayout = LayoutInflater.from(this).inflate(R.layout.slot_date_item, tabLayout, false);
            TextView tabTextView = relativeLayout.findViewById(R.id.month);
            TextView date = relativeLayout.findViewById(R.id.date);
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
                    finish();
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
                BookedSlots = (ArrayList<String>) respo.appointmentsdata;
                for (int i = 0; i < slotslist.size(); i++) {
                    NoOfSlots = modifiedDates.get(0).toString() + " " + slotslist.get(i).toString();
                }
                java.util.Calendar c = java.util.Calendar.getInstance();
                java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm a");

             /*   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        R.layout.timeslot_list, R.id.timeslot, slotslist);*/
                SelectSlotsAdapter adapter = new SelectSlotsAdapter(Slot_Selectionpage.this, slotslist, BookedSlots);

                slotGrid.setAdapter(adapter);
//                int nPrevSelGridItem = -1;

                slotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    View viewPrev;

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int posi, long l) {
                        String mSlot = slotslist.get(posi).toString();

                        try {
                            if (nPrevSelGridItem != -1) {
                                viewPrev = slotGrid.getChildAt(nPrevSelGridItem);
                                viewPrev.setBackgroundColor(Color.WHITE);
                            }
                            nPrevSelGridItem = posi;
                            if (nPrevSelGridItem == posi) {
                                //View viewPrev = (View) gridview.getChildAt(nPrevSelGridItem);
                                view.setBackgroundColor(Color.RED);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        timeW.setText(mSlot);
                        if (BookedSlots.contains(slotslist.get(posi))) {

                            Toast.makeText(Slot_Selectionpage.this,"This slot is Already booked",Toast.LENGTH_LONG).show();
                            timeW.setText("00:00");
                        }

                    }
                });

            } else {

                slotslist = new ArrayList<>();
             /*   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, slotslist);*/
                SelectSlotsAdapter adapter = new SelectSlotsAdapter(Slot_Selectionpage.this, slotslist, BookedSlots);

                slotGrid.setAdapter(adapter);


            }
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

    public class SelectSlotsAdapter extends BaseAdapter {

        Context con;
        ArrayList<String> DatesToDisplay;
        ArrayList<String> BookedSlots;
        LayoutInflater inflater;

        public SelectSlotsAdapter(Context con, ArrayList<String> DatesToDisplay, ArrayList<String> BookedSlots) {

            this.con = con;
            this.DatesToDisplay = DatesToDisplay;
            this.BookedSlots = BookedSlots;
            inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public int getCount() {
            return DatesToDisplay.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;


            if (convertView == null) {
                convertView = inflater.inflate(R.layout.timeslot_list, parent, false);
                vh = new ViewHolder();

                vh.listDates = convertView.findViewById(R.id.timeslot);

                convertView.setTag(vh);

            } else {
                vh = (ViewHolder) convertView.getTag();

            }
            vh.listDates.setText(DatesToDisplay.get(position));

            if (BookedSlots.contains(DatesToDisplay.get(position))) {

                vh.listDates.setPaintFlags(vh.listDates.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                vh.listDates.setFocusable(false);
            } else {

                vh.listDates.setPaintFlags(vh.listDates.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

               /* convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        slotTime = vh.listDates.getText().toString().trim();

                        Log.e("SlotTimeee", "123     "+ slotTime);

                        Intent intent = new Intent(SelectSlots.this, SelectFamilyMemebers.class);
                        intent.putExtra("slotDay", Today.getText().toString().substring(0,3));
                        intent.putExtra("slotDate", TodayDate.getText().toString());
                        intent.putExtra("slotTime", slotTime);
                        intent.putExtra("PriceFee", Price.getText().toString().trim());
                        intent.putExtra("DoctorId", DoctorId);
                        intent.putExtra("WorkingId", WorkingId);
                        intent.putExtra("DoctorName", DoctorName);
                        //intent.putExtra("HospitalName", HospiatlName);
                        startActivity(intent);
                    }
                });*/
            }

            return convertView;
        }

    }

    public class ViewHolder {

        TextView listDates;

    }


}
