package com.nsl.app.dailydairy;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.MainDailyDiaryActivity;
import com.nsl.app.R;
import com.nsl.app.SelectedCities;
import com.nsl.app.Utility;
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
import static com.nsl.app.DatabaseHandler.TABLE_DAILYDAIRY;

public class HistoryActivity extends AppCompatActivity {
    Spinner spin_date;
    ListView list;
    TextView tv_date;
    DatabaseHandler db;
    Button btn_ok,date;
    TextView empty;
    int i=0,sum_osa=0,flag=0;
    //ImageView time;
    String jsonData,id,title,user_id,note,time_slot,dairy_date,create_date,ffmid,ffmidsqlite;
    private ItemfavitemAdapter adapter;
    String checkuid,sel_date_id;
    String item="Select Date";
    SharedPreferences sharedpreferences;
    ProgressDialog progressDialog;
    public static final String mypreference = "mypref";
    private ArrayList<SelectedCities>  arlist_crops;
    String[] Maintime = {"9", "11", "2", "4"};
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    private static SQLiteDatabase sql, sdbr,sdbw;
    ArrayList<String> adapter_crops;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        date = (Button) findViewById(R.id.diary_date);
        sharedpreferences = HistoryActivity.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        // Toast.makeText(HistoryActivity.this,"user id is "+checkuid,Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), MainDailyDiaryActivity.class);
                startActivity(home);
                finish();
            }
        });

        try {
            if(Utility.isNetworkAvailable(HistoryActivity.this, Constants.isShowNetworkToast)){

                new Async_getAllDailyDairy().execute();
            } else {
                new AsyncDiaryDate().execute();
            }
        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }

        list = (ListView)findViewById(R.id.listView);
        spin_date = (Spinner) findViewById(R.id.spinner_date);

        tv_date = (TextView)findViewById(R.id.textViewdate);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        empty = (TextView)findViewById(R.id.empty);

        db = new DatabaseHandler(HistoryActivity.this);
        //  db.deleteDailyDiary();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int selmon = selectedmonth +1;
                        //sowing_date.setText(selectedyear + "-" + selmon + "-" + selectedday );
                        item = selectedyear + "-" + selmon + "-" + selectedday;
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-M-dd");
                        DateFormat outputFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
                        String inputDateStr=item;
                        Date date = null;
                        try {
                            date = inputFormat.parse(inputDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String outputDateStr = outputFormat.format(date);
                        Log.e("output",outputDateStr);
                        Log.e("selected",outputDateStr+" from calendar ");
                        favouriteItem.clear();
                        List<DailyDairy> dailydairy = db.getAlldateDailyDairy(checkuid,outputDateStr);

                        Log.e("list size", String.valueOf(dailydairy.size()));

                        if(dailydairy.size()==0){

                            empty.setVisibility(View.VISIBLE);
                        }
                        else {
                            empty.setVisibility(View.GONE);
                        }
                        for (DailyDairy fb : dailydairy) {
                            String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_comments() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate() + " : " + fb.get_ffmid();

                            Log.e("Dairy before : ", log);

                            Log.e("adding",fb.get_time());
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("id", String.valueOf(fb.getID()));
                            map.put("userid", String.valueOf(fb.get_userid()));
                            map.put("title", fb.get_title());
                            map.put("comments", fb.get_comments());
                            map.put("time", fb.get_time());
                            map.put("time", fb.get_date());


                            favouriteItem.add(map);
                        }




                        adapter = new ItemfavitemAdapter(HistoryActivity.this, favouriteItem);

                        list.setAdapter(adapter);
                        if (item.equalsIgnoreCase("Select Date")) {
                            tv_date.setText("Date");

                        } else {

                            String date_after = formateDateFromstring(Constants.DateFormat.COMMON_DATE_FORMAT, "dd MMM yyyy", item);
                            tv_date.setText(date_after);
                        }




                    };

                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select time");
                mDatePicker.show();
            }
        });
        Log.d("Reading: ", "Reading all daily..");

        List<DailyDairy> dailydairy = db.getAllDailyDairy(checkuid);

        Log.e("list size", String.valueOf(dailydairy.size()));

        if (dailydairy.size() == 0) {
            Log.e("no data found", "no data");
        } else {
            for (DailyDairy fb : dailydairy) {
                String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : "+ fb.get_comments() + " : "+ fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate()+ " : "   + fb.get_ffmid();

                Log.e("Dairy before : ", log);

                String datebefore = fb.get_date().toString();
                String date_after = formateDateFromstring(Constants.DateFormat.COMMON_DATE_FORMAT, "dd, MMM yyyy", datebefore);
                Log.e("dateafter",date_after);
//                String month = fb.get_date().toString().substring(6,7);
                //         Log.e("month is",month);

                //   String month_name =  new DateFormatSymbols().getMonths()[Integer.parseInt(month)-1];

                //  Log.e("month_name is",month_name);

                //  }

            }

        }

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
            Intent home = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

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
            progressDialog = new ProgressDialog(HistoryActivity.this);
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


                String selectQuery = "SELECT "+  KEY_DD_ID +","+ KEY_DD_DATE +","+ KEY_DD_TITLE + ","+  KEY_DD_COMMENTS+  " FROM " + TABLE_DAILYDAIRY + " group by(" + KEY_DD_DATE + ")";                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                // String selectQuery = "SELECT * FROM " + TABLE_DAILYDAIRY + " group by(" + KEY_DD_DATE + ")";                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                System.out.println(selectQuery);

                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();

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
                        System.out.println(cursor.getString(0)+cursor.getString(1)+cursor.getString(2)+cursor.getString(3));


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
            // adapter.updateResults(arrayList);
            spin_date.setAdapter(new ArrayAdapter<String>(HistoryActivity.this, R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_crops));
            // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.activity_list_item,android.R.id.text1,adapter_companies);
            //   dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //  spin_company.setAdapter(dataAdapter);

           /* spin_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_date_id = arlist_crops.get(position).getCityId();

                    item = parent.getItemAtPosition(position).toString();
                    Log.e("selected",item+" at "+ position);

                    favouriteItem.clear();
                    List<DailyDairy> dailydairy = db.getAlldateDailyDairy(checkuid,item);

                    Log.e("list size", String.valueOf(dailydairy.size()));

                    if(dailydairy.size()==0){

                        empty.setVisibility(View.VISIBLE);
                    }
                    else {
                        empty.setVisibility(View.GONE);
                    }
                        for (DailyDairy fb : dailydairy) {
                            String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_comments() + " : " + fb.get_time() + " : " + fb.get_date() + " : " + fb.get_createddate() + " : " + fb.get_ffmid();

                            Log.e("Dairy before : ", log);

                                Log.e("adding",fb.get_time());
                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("id", String.valueOf(fb.getID()));
                                map.put("userid", String.valueOf(fb.get_userid()));
                                map.put("title", fb.get_title());
                                map.put("comments", fb.get_comments());
                                map.put("time", fb.get_time());
                                map.put("time", fb.get_date());


                                favouriteItem.add(map);
                            }




                    adapter = new ItemfavitemAdapter(HistoryActivity.this, favouriteItem);

                    list.setAdapter(adapter);
                    if (item.equalsIgnoreCase("Select Date")) {
                        tv_date.setText("Date");

                    } else {

                        String date_after = formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", item);
                        tv_date.setText(date_after);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }
    }

    private class ItemfavitemAdapter extends BaseAdapter {
        Context context;
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String,String>>();
        public ItemfavitemAdapter(HistoryActivity historyActivity, ArrayList<HashMap<String, String>> favouriteItem) {
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
                convertView = inflater.inflate(R.layout.row_diary_history, parent, false);
                holder.tv_time_slot = (TextView) convertView.findViewById(R.id.tv_timeslot);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_comments = (TextView) convertView.findViewById(R.id.tv_comments);

                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_time_slot.setText(results.get(position).get("time"));
            holder.tv_title.setText(results.get(position).get("title"));
            holder.tv_comments.setText(results.get(position).get("comments"));
            //  holder.tv_place.setVisibility(View.GONE);
            return convertView;
        }
        public class ViewHolder
        {
            public TextView  tv_time_slot,tv_title,tv_comments;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }


    public class Async_getAllDailyDairy extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(HistoryActivity.this);
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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_DAILY_DAIRY+"&user_id="+checkuid)
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
                         db.deleteDailyDiary();
                        JSONArray companyarray = new JSONArray(jsonData);
                        System.out.println(companyarray.length());
                        Log.e("Length dailydairy", String.valueOf(companyarray.length()));

                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);

                            id = objinfo.getString("dailydairy_id");
                            title = objinfo.getString("title");
                            user_id = objinfo.getString("user_id");
                            note = objinfo.getString("comments");
                            time_slot = objinfo.getString("time_slot");
                            dairy_date = objinfo.getString("dairy_date");
                            create_date = objinfo.getString("created_date");
                            ffmid = objinfo.getString("dailydairy_id");

                            String selectQuery = "SELECT * FROM " + TABLE_DAILYDAIRY + " WHERE " +  KEY_DD_FFMID + " = '" + ffmid + "'";

                            sdbw = db.getWritableDatabase();
                            Cursor cc = sdbw.rawQuery(selectQuery, null);
                            cc.getCount();
                            // looping through all rows and adding to list
                            if(cc.getCount() == 0)
                            {
                                Log.d("Insert: ", "Inserting diary ..");
                                db.addDailyDairy(new DailyDairy(Integer.parseInt(id),title, Integer.parseInt(user_id), note, time_slot, dairy_date,create_date, ffmid));


                                Log.e("Inserted!!!!", "Inserted to sqlite");
                            }

                            else{

                                sdbw = db.getWritableDatabase();
                                String updatequerys = "UPDATE " + TABLE_DAILYDAIRY + " SET " + KEY_DD_TITLE + " = '" + title + "' , " +
                                        KEY_DD_COMMENTS + " = '"+ note +"' WHERE " + KEY_DD_FFMID + " = " + id +" AND "+KEY_DD_TIME_SLOT+" = '"+time_slot+"'";
                                Log.e("update dd",updatequerys);
                                sdbw.execSQL(updatequerys);

                                //  db.addDailyDairy(new DailyDairy(dtitle, Integer.parseInt(duser_id), dnote, dtime_slot, ddairy_date, null, Integer.parseInt(dffmid)));


                                Log.e("updated!!!!", "updated to sqlite");
                                // do some stuff....
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
            new AsyncDiaryDate().execute();
        }

    }
}



