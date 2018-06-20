package com.nsl.app.dailydairy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.MainDailyDiaryActivity;
import com.nsl.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DailyDairyActivity extends AppCompatActivity {
    public static final int FROM_DAILY_DAIRY_FORM = 1;
    Button b2, b3, b4;
    private ListView listView;
    int global_position = 0;
    String[] Name = {"9AM - 11AM", "11AM - 1PM","2PM - 4PM", "4PM - 6PM", "Adhoc Task"};
    DatabaseHandler db;
    String checkuid;
    ImageView status;
    private ItemfavitemAdapter adapter;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private static SQLiteDatabase sdbw;
    private String team;
    private int role;
    private String userdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_dairy);
        db = new DatabaseHandler(DailyDairyActivity.this);
        sdbw = db.getWritableDatabase();
        sharedpreferences = DailyDairyActivity.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

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
        listView = (ListView)findViewById(R.id.listView);

        // Each row in the list stores country name, currency and flag

        ArrayList<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<5;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt",  Name[i]);
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "txt" };

        // Ids of views in listview_layout
        int[] to = { R.id.tv_type};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
       // SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), aList, R.layout.row_dailydiary, from, to);
        adapter = new ItemfavitemAdapter(DailyDairyActivity.this, aList);
        listView.setAdapter(adapter);

       userdate = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT).format(new Date());

      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              String selectedFromList = String.valueOf(listView.getItemAtPosition(position));

              Log.e("time", selectedFromList+":"+ position);

              if(position == 0){
                  Intent i = new Intent(DailyDairyActivity.this, DailyDairyForm.class);
                  i.putExtra("time","9AM - 11AM");
                  startActivityForResult(i,FROM_DAILY_DAIRY_FORM);
              }
              if(position == 1){
                  Intent i = new Intent(DailyDairyActivity.this, DailyDairyForm.class);
                  i.putExtra("time","11AM - 1PM");
                  startActivityForResult(i,FROM_DAILY_DAIRY_FORM);
              }
              if(position == 2){
                  Intent i = new Intent(DailyDairyActivity.this, DailyDairyForm.class);
                  i.putExtra("time","2PM - 4PM");
                  startActivityForResult(i,FROM_DAILY_DAIRY_FORM);
              }
              if(position == 3){
                  Intent i = new Intent(DailyDairyActivity.this, DailyDairyForm.class);
                  i.putExtra("time","4PM - 6PM");
                  startActivityForResult(i,FROM_DAILY_DAIRY_FORM);
              }
              if(position == 4){
                  Intent i = new Intent(DailyDairyActivity.this, AdhocHistoryActivity.class);
                  startActivityForResult(i,FROM_DAILY_DAIRY_FORM);
              }
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
            Intent home = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FROM_DAILY_DAIRY_FORM:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        this.recreate();
                        break;
                }

                break;
        }
    }

    class ItemfavitemAdapter extends BaseAdapter {

        private final List<DailyDairy> dailydairy;
        Context context;
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String,String>>();


        public ItemfavitemAdapter(DailyDairyActivity pd, List<HashMap<String, String>> results) {
            this.context = pd;
            this.results = (ArrayList<HashMap<String, String>>) results;
            String userdate = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT).format(new Date());
            dailydairy = db.getAlldateDailyDairy(checkuid, userdate);

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if(convertView == null)
            {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater     = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView                 = inflater.inflate(R.layout.row_dailydiary, parent, false);
                holder.img              = (ImageView) convertView.findViewById(R.id.imageViewsatus);
                holder.timeslot          = (TextView) convertView.findViewById(R.id.tv_type);
                holder.tv_adhoc_completed          = (TextView) convertView.findViewById(R.id.tv_adhoc_completed);
                holder.tv_adhoc_pending          = (TextView) convertView.findViewById(R.id.tv_adhoc_pending);
                holder.ll_count          = (LinearLayout) convertView.findViewById(R.id.ll_count);
                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }

            if (position==4){
                holder.img.setVisibility(View.GONE);
                holder.ll_count.setVisibility(View.VISIBLE);
                int pendingCount = db.getAdhocCompletedPendingCount(team, userdate, 1);
                int completedCount = db.getAdhocCompletedPendingCount(team, userdate, 2);
                holder.tv_adhoc_completed.setText(""+completedCount);
                holder.tv_adhoc_pending.setText(""+pendingCount);

            }else {
                holder.img.setVisibility(View.VISIBLE);
                holder.ll_count.setVisibility(View.GONE);
                if (dailydairy.size() > 0) {
                    boolean isDataAvaiable = false;
                    for (int i = 0; i < dailydairy.size(); i++) {

                        Log.e("dailydairy ", dailydairy.get(i).get_time() + "dailydairy " + results.get(position).get("txt"));
                        if (dailydairy.get(i).get_time().equals(results.get(position).get("txt"))) {
                            isDataAvaiable = true;
                            break;
                        }

                    }

                    if (isDataAvaiable) {
                        holder.img.setImageResource(R.drawable.icon4);
                    } else {
                        holder.img.setImageResource(R.drawable.icon5);
                    }
                } else {
                    holder.img.setImageResource(R.drawable.icon5);
                }

            }
           holder.timeslot.setText(results.get(position).get("txt"));

            return convertView;
        }


        public class ViewHolder
        {
           TextView timeslot;
           TextView tv_adhoc_completed;
           TextView tv_adhoc_pending;
            public ImageView img;
            LinearLayout ll_count;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }
}
