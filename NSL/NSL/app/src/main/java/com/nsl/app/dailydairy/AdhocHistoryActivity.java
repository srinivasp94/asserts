package com.nsl.app.dailydairy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.MainDailyDiaryActivity;
import com.nsl.app.R;
import com.nsl.app.SelectedCities;
import com.nsl.app.Users;
import com.nsl.app.Utility;
import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_DD_COMMENTS;
import static com.nsl.app.DatabaseHandler.KEY_DD_DATE;
import static com.nsl.app.DatabaseHandler.KEY_DD_FFMID;
import static com.nsl.app.DatabaseHandler.KEY_DD_ID;
import static com.nsl.app.DatabaseHandler.KEY_DD_TIME_SLOT;
import static com.nsl.app.DatabaseHandler.KEY_DD_TITLE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_DAILYDAIRY;
import static com.nsl.app.DatabaseHandler.TABLE_USERS;

public class AdhocHistoryActivity extends AppCompatActivity {
    private static final int FROM_ADHOC_HISTORY = 2;
    Spinner spin_date;
    ListView list;
    TextView tv_date;
    DatabaseHandler db;
    Button btn_ok, date;
    TextView empty;
    int i = 0, sum_osa = 0, flag = 0;
    //ImageView time;
    String jsonData, id, title, user_id, note, time_slot, dairy_date, create_date, ffmid, ffmidsqlite;
    private ItemfavitemAdapter adapter;
    String checkuid, sel_date_id;
    String item="Date";
    SharedPreferences sharedpreferences;
    ProgressDialog progressDialog;
    public static final String mypreference = "mypref";
    private ArrayList<SelectedCities> arlist_crops;
    String[] Maintime = {"9", "11", "2", "4"};
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    private static SQLiteDatabase sql, sdbr, sdbw;
    ArrayList<String> adapter_crops;
    private FloatingActionButton fab;
    private Spinner spin_user;
    private ArrayList<SelectedCities> organisations;
    ArrayList<String> adaptercity;
    private String team;
    private int role;
    private String userName;
    private String customerId;
    private LinearLayout llSppiner;
    private View view;
    String outputDateStr;
    String completedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhoc_history);

        db = new DatabaseHandler(AdhocHistoryActivity.this);

        date = (Button) findViewById(R.id.diary_date);
        list = (ListView) findViewById(R.id.listView);
        spin_user = (Spinner) findViewById(R.id.spin_user);

        tv_date = (TextView) findViewById(R.id.textViewdate);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        empty = (TextView) findViewById(R.id.empty);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        llSppiner = (LinearLayout) findViewById(R.id.id_ll_sppiner);
        view = (View) findViewById(R.id.view);

        sharedpreferences = AdhocHistoryActivity.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

        // Toast.makeText(HistoryActivity.this,"user id is "+checkuid,Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        if (role == Constants.Roles.ROLE_5 || role == Constants.Roles.ROLE_6) {
            llSppiner.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            callapi();

        } else {
            llSppiner.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
        }
        try {
            if (Utility.isNetworkAvailable(AdhocHistoryActivity.this, Constants.isShowNetworkToast)) {

                new Async_getAllDailyDairy().execute();
            } else {
                 // new AsyncDiaryDate().execute();
            }
        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdhocHistoryActivity.this, AdhocActivity.class);
                startActivityForResult(intent, FROM_ADHOC_HISTORY);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(AdhocHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {


                        int selmon = selectedmonth + 1;
                        //sowing_date.setText(selectedyear + "-" + selmon + "-" + selectedday );
                        item = selectedyear + "-" + selmon + "-" + selectedday;
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-M-dd");
                        DateFormat outputFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
                        String inputDateStr = item;
                        Date date = null;
                        try {
                            date = inputFormat.parse(inputDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        outputDateStr = outputFormat.format(date);
                        Log.e("output", outputDateStr);
                        Log.e("selected", outputDateStr + " from calendar ");
                        if ((role == Constants.Roles.ROLE_5 || role == Constants.Roles.ROLE_6) && spin_user.getSelectedItemPosition() == 0) {
                            Toast.makeText(getApplicationContext(), "Please select user", Toast.LENGTH_SHORT).show();
                        } else {
                            onDateSeletion();
                        }


                    }

                    ;

                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select time");
                mDatePicker.show();
            }
        });
        Log.d("Reading: ", "Reading all daily..");


    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();

    }

    private void onDateSeletion() {
        List<DailyDairy> dailydairy = db.getAdhocDailyDairy(checkuid,outputDateStr);
        favouriteItem.clear();
        Log.e("list size", String.valueOf(dailydairy.size()));

        if (dailydairy.size() == 0) {

            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }
        for (DailyDairy fb : dailydairy) {
            String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_comments() + " : " + " : " + fb.get_date() + " : " + fb.get_createddate() + " : " + fb.get_ffmid();

            Log.e("Dairy before : ", log);

//                            Log.e("adding",fb.get_time());
            HashMap<String, String> map = new HashMap<String, String>();

            map.put("id", String.valueOf(fb.getID()));
            map.put("userid", String.valueOf(fb.get_userid()));
            map.put("title", fb.get_title());
            map.put("comments", fb.get_comments());
            map.put("time", fb.get_tentative_time());
            map.put("ffmid", fb.get_ffmid());
            map.put("status", String.valueOf(fb.get_status()));


            favouriteItem.add(map);
        }


        adapter = new ItemfavitemAdapter(AdhocHistoryActivity.this, favouriteItem);

        list.setAdapter(adapter);
        if (item.equalsIgnoreCase("Date")) {
            tv_date.setText("Date");

        } else {

            String date_after = formateDateFromstring(Constants.DateFormat.COMMON_DATE_FORMAT, "dd MMM yyyy", item);
            tv_date.setText(date_after);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdhocHistoryActivity.this, CompleteAdhocTaskActivity.class);
                intent.putExtra("data", favouriteItem.get(position));
                startActivityForResult(intent, FROM_ADHOC_HISTORY);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("TAG", "ParseException - dateFormat");
        }

        return outputDate;

    }


    public class AsyncDiaryDate extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdhocHistoryActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_crops = new ArrayList<SelectedCities>();
            adapter_crops = new ArrayList<String>();
            arlist_crops.clear();

            SelectedCities crops = new SelectedCities();
            crops.setCityId("0");
            crops.setCityName("Select Date");
            //    System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_crops.add(crops);
            adapter_crops.add("Select Date");


            try {

                List<DailyDairy> cropList = new ArrayList<>();


                String selectQuery = "SELECT " + KEY_DD_ID + "," + KEY_DD_DATE + "," + KEY_DD_TITLE + "," + KEY_DD_COMMENTS + " FROM " + TABLE_DAILYDAIRY + " group by(" + KEY_DD_DATE + ")";                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                // String selectQuery = "SELECT * FROM " + TABLE_DAILYDAIRY + " group by(" + KEY_DD_DATE + ")";                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                System.out.println(selectQuery);


                sql = db.getWritableDatabase();
                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        DailyDairy Crop = new DailyDairy();

                        crops.setCityId(cursor.getString(0));

                        crops.setCityName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_crops.add(cities2);
                        adapter_crops.add(cursor.getString(1));
                        System.out.println(cursor.getString(0) + cursor.getString(1) + cursor.getString(2) + cursor.getString(3));


                    } while (cursor.moveToNext());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing())
                progressDialog.dismiss();

        }
    }

    private class ItemfavitemAdapter extends BaseAdapter {
        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();

        public ItemfavitemAdapter(AdhocHistoryActivity historyActivity, ArrayList<HashMap<String, String>> favouriteItem) {
            this.context = historyActivity;
            this.results = favouriteItem;
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_adhoc_history, parent, false);
                holder.tv_time_slot = (TextView) convertView.findViewById(R.id.tv_timeslot);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_comments = (TextView) convertView.findViewById(R.id.tv_comments);
                holder.image = (ImageView) convertView.findViewById(R.id.image);

                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Log.d("status: ","status"+results.get(position).get("status"));

           if (results.get(position).get("status")=="2"){
               holder.image.setImageResource(R.drawable.add);

           }
           else{
               holder.image.setImageResource(R.drawable.cal);

           }
            holder.tv_title.setText(results.get(position).get("title"));
            holder.tv_comments.setText(results.get(position).get("time"));
            //  holder.tv_place.setVisibility(View.GONE);
            return convertView;
        }

        public class ViewHolder {
            public TextView tv_time_slot, tv_title, tv_comments;
            public ImageView image;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }


    public class Async_getAllDailyDairy extends AsyncTask<Void, Void, String> {

        private String tentative_time;
        private int type;
        private int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(AdhocHistoryActivity.this);
            progressDialog.setMessage("Please wait ");
            progressDialog.setCancelable(false);
//              progressDialog.show();
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        //.url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_DAILY_DAIRY+"&user_id="+checkuid)
                        .url(Common.getCompleteURL(role, Constants.URL_NSL_MAIN + Constants.URL_MASTER_DAILY_DAIRY, checkuid, team))
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (jsonData != null) {
                    try {
                        db.deleteDataByTableName(db.TABLE_DAILYDAIRY);
                        JSONArray companyarray = new JSONArray(jsonData);
                        System.out.println(companyarray.length());
                        Log.e("Length dailydairy", String.valueOf(companyarray.length()));

                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);
                            if (!objinfo.has("error")) {

                                id = objinfo.getString("dailydairy_id");
                                title = objinfo.getString("title");
                                user_id = objinfo.getString("user_id");
                                note = objinfo.getString("comments");
                                time_slot = objinfo.getString("time_slot");
                                dairy_date = objinfo.getString("dairy_date");
                                create_date = objinfo.getString("created_date");
                                ffmid = objinfo.getString("dailydairy_id");
                                tentative_time = objinfo.getString("tentative_time");
                                type = objinfo.getInt("type");
                                status = objinfo.getInt("status");

                                String selectQuery = "SELECT * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_FFMID + " = '" + ffmid + "'";

                                sdbw = db.getWritableDatabase();
                                Cursor cc = sdbw.rawQuery(selectQuery, null);
                                cc.getCount();
                                // looping through all rows and adding to list
                                if (cc.getCount() == 0) {
                                    Log.d("Insert: ", "Inserting diary ..");
                                    db.addDailyDairy(new DailyDairy(title, Integer.parseInt(user_id), note, time_slot, dairy_date, create_date, ffmid, type, tentative_time, status));

                                    Log.e("Inserted!!!!", "Inserted to sqlite");
                                } else {

                                    sdbw = db.getWritableDatabase();
                                    String updatequerys = "UPDATE " + TABLE_DAILYDAIRY + " SET " + KEY_DD_TITLE + " = '" + title + "' , " +
                                            KEY_DD_COMMENTS + " = '" + note + "' WHERE " + KEY_DD_FFMID + " = " + id + " AND " + KEY_DD_TIME_SLOT + " = '" + time_slot + "'";
                                    Log.e("update dd", updatequerys);
                                    sdbw.execSQL(updatequerys);

                                    //  db.addDailyDairy(new DailyDairy(dtitle, Integer.parseInt(duser_id), dnote, dtime_slot, ddairy_date, null, Integer.parseInt(dffmid)));


                                    Log.e("updated!!!!", "updated to sqlite");
                                    // do some stuff....
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    //  db.deleteFeedback();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
              //new AsyncDiaryDate().execute();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FROM_ADHOC_HISTORY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //this.recreate();
                        onDateSeletion();
                        break;
                }

                break;
        }
    }


    private void callapi() {
        organisations = new ArrayList<SelectedCities>();
        adaptercity = new ArrayList<String>();
        organisations.clear();
        SelectedCities citiez = new SelectedCities();
        citiez.setCityId("0");
        citiez.setCityName("Select User");
        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
        organisations.add(citiez);
        adaptercity.add("Select User");
        try {

            String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  user_id in (" + team + ")";
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();
            System.out.println("selectQuery " + selectQuery);
            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    Users users = new Users();

                    users.setUserMasterID(cursor.getString(1));
                    users.setUser_first_name(cursor.getString(0));

                    SelectedCities cities2 = new SelectedCities();
                    cities2.setCityId(cursor.getString(1));
                    cities2.setCityName(cursor.getString(0));
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    organisations.add(cities2);
                    adaptercity.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        }

        spin_user.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));
        spin_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customerId = organisations.get(position).getCityId();
                userName = organisations.get(position).getCityName();
                checkuid = customerId;
                Log.d("checkuid", checkuid + " : " + customerId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}



