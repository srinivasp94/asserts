package com.nsl.app;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_USERS;

import static com.nsl.app.advancebooking.NewAdvancebokingDivisionsActivity.team;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanerTypeActivity extends AppCompatActivity {
    private static final int FROM_PLANNER_TYPE_ACTIVITY = 444;
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr = null;


    ProgressDialog progressDialog;

    private GridView gridView;
    private EditText et_date;


    String jsonData;
    String userId;
    String companyinfo;
    String chkevm;
    String startDate;
    int role;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_date;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    String[] Name = {"FD-meeting plan", "Office- admin/ actions plan"};//
    String datefromcalander, eventdate;
    SimpleDateFormat sdf;
    private TextView txt_select_user;
    private Spinner spin_user;
    private ArrayList<String> adapterusers;
    ArrayList<Users> userses;

    String sel_userId;
    String sel_userName="NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannertype);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        team = sharedpreferences.getString("team", "");

        db = new DatabaseHandler(this);
        sdf = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        Date date = new Date();
        startDate = sdf.format(date);
        datefromcalander = getIntent().getStringExtra("date");
        sel_userName = getIntent().getStringExtra("selecteduser");
        sel_userId = getIntent().getStringExtra("selecteduserid");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        et_date = (EditText) findViewById(R.id.et_date);
        txt_select_user = (TextView) findViewById(R.id.txt_select_user);
        spin_user = (Spinner) findViewById(R.id.spin_user);

        gridView = (GridView) findViewById(R.id.gridView);

        Log.e("role===", String.valueOf(role));



        if (datefromcalander == null) {
            et_date.setText(startDate);
        } else {
            et_date.setText(datefromcalander);
        }
        //   Log.e("dateis",datefromcalander);

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 2; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", Name[i]);

            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"txt"};

        // Ids of views in listview_layout
        int[] to = {R.id.tv_type};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), aList, R.layout.row_planner_selection, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                if (i == 0) {
                    Intent schedule = new Intent(getApplicationContext(), PlanScheduleFormActivity.class);
                    schedule.putExtra("type", "1");
                    schedule.putExtra("startDate", et_date.getText().toString());
                    if (role != Constants.Roles.ROLE_7) {
                        schedule.putExtra("sel_userId", sel_userId);
                        schedule.putExtra("sel_userName", sel_userName);
                        schedule.putExtra("isDirectCustomers", getIntent().getBooleanExtra("isDirectCustomers",false));
                        Log.d("sending username", sel_userName);
                    }
                    startActivityForResult(schedule, FROM_PLANNER_TYPE_ACTIVITY);
                    //  finish();
                } else {

                    Intent event = new Intent(getApplicationContext(), PlanEventFormActivity.class);
                    event.putExtra("type", "2");
                    event.putExtra("startDate", et_date.getText().toString());
                    if (role != Constants.Roles.ROLE_7) {
                        event.putExtra("sel_userId", sel_userId);
                        event.putExtra("sel_userName", sel_userName);
                        event.putExtra("isDirectCustomers", getIntent().getBooleanExtra("isDirectCustomers",false));
                        Log.e("sending username", sel_userName + sel_userId);
                    }
                    startActivityForResult(event,FROM_PLANNER_TYPE_ACTIVITY);
                    // finish();
                }
            }
        });
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(PlanerTypeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        int sel_month = selectedmonth + 1;
                        String sday = String.valueOf(selectedday);
                        String smonth = null;
                        if (sel_month < 10)
                            smonth = "0" + sel_month;
                        else
                            smonth = String.valueOf(sel_month);

                        if (selectedday < 10)
                            sday = "0" + selectedday;
                        else
                            sday = String.valueOf(selectedday);

                        startDate = selectedyear + "-" + smonth + "-" + sday;
                        et_date.setText(startDate);

                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


    }

    private void loadUsers() {

        userses = new ArrayList<Users>();
        adapterusers = new ArrayList<String>();
        userses.clear();
        Users user = new Users();
        user.setID(0);
        user.setUser_first_name("Self");
        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
        userses.add(user);
        adapterusers.add("Self");
        try {

            String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  user_id in (" + userId + "," + team + ")";
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                   /* Users users = new Users();

                    users.setUserMasterID(cursor.getString(1));
                    users.setUser_first_name(cursor.getString(0));*/

                    Users userDEtail = new Users();
                    userDEtail.setID(Integer.parseInt(cursor.getString(1)));
                    userDEtail.setUser_first_name(cursor.getString(0));
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    userses.add(userDEtail);
                    adapterusers.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("selected user intent", sel_userName);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menu.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult", "OnresultAxtivity1");
        switch (requestCode) {

            case FROM_PLANNER_TYPE_ACTIVITY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        setResult(Activity.RESULT_OK);
                        finish();
                        Log.d("onActivityResult", "FROM_PLANNER_TYPE_ACTIVITY");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // finish();

                        break;

                    default:
                        break;
                }
                break;


        }
    }
}
