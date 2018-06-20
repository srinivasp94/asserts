package com.nsl.app.feedback;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.app.Crops;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.app.DatabaseHandler.TABLE_CROPS;


public class FeedbackallActivity extends AppCompatActivity {

    private static final int FROM_DAILY_FEEDBACK_ALL_ACT = 3;
    private JSONObject JSONObj;
    private JSONArray JSONArr=null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    TextView empty;
    SQLiteDatabase sdbw,sdbr;
    int i=0,sum_osa=0,flag=0;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    String customer_id,comp_id,cropname;
    String jsonData,userId,id,user,farmer,place,contact_no,crop,hybrid,sowing_date,feedback_message,image,ffmid,ffmidsqlite;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color,fdbk;
    Button refresh;
    String aging1=null,aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name,tv_code,tv_total_osa_amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackall);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newfedback = new Intent(getApplicationContext(),MainFeedbackActivity.class);
                startActivityForResult(newfedback,FROM_DAILY_FEEDBACK_ALL_ACT);

            }
        });
        refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        db = new DatabaseHandler(FeedbackallActivity.this);
        //db.deleteDataByTableName(db.TABLE_FEEDBACK);

      /*  FragmentFeedback fragm = new FragmentFeedback();
        fragm.checkConnection();*/
        listView = (ListView) findViewById(R.id.listView);

        adapter = new ItemfavitemAdapter(FeedbackallActivity.this, favouriteItem);
        //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty));
        empty = (TextView)findViewById(R.id.empty);
       // db.deleteFeedback();
        try {


                Log.d("Reading: ", "Reading all Feedback from sqlite..");
                List<Feedback> feedback = db.getAllFeedbacks(userId);

                for (Feedback cm : feedback) {
                    String log = "Id: " + cm.getID() + ",userid: " + cm.get_user_id() + " ,farmer: " + cm.getFarmerName() + " ,place: " + cm.getplace() + ",contact_no:" + cm.getContactNo() + " ,crop: " + cm.getCrop() + ",hybrid:" +
                            cm.getHybrid() + ",sowing_date" + cm.getSowingDate() + ",feedback_message:" + cm.getfeedbackmessage() + ",image" + cm.getImage() + ",ffmid " + cm.get_ffmid();
                    Log.e("feedbackafterupdate: ", log);
                }
                for (Feedback fb : feedback) {
                    int crop_id = Integer.parseInt(fb.getCrop());
                    String selectQuerys = "SELECT  " + KEY_TABLE_CROPS_CROP_NAME  +" FROM " + TABLE_CROPS + " WHERE " + KEY_TABLE_CROPS_CROP_MASTER_ID + " = " + crop_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("crop query",selectQuerys);
                    Cursor cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Crops companies = new Crops();
                        cropname = cc.getString(0);
                        companies.setCropName(cropname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("crop",cropname);
                    }
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("sr", String.valueOf(fb.getID()));
                    map.put("name", fb.getFarmerName());
                    map.put("place", fb.getplace());
                    map.put("contact_number", fb.getContactNo());
                    map.put("crop",cropname);
                    map.put("hybrid", fb.getHybrid());
                    map.put("sowing_date", fb.getSowingDate());
                    map.put("feedback", fb.getfeedbackmessage());
                    map.put("image", fb.getImage());

                    favouriteItem.add(map);

                }
                adapter = new ItemfavitemAdapter(FeedbackallActivity.this, favouriteItem);
                //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
                listView.setAdapter(adapter);




        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent booking = new Intent(getApplicationContext(),Feedbackall.class);
                TextView tv_feedback          = (TextView)view.findViewById(R.id.tv_feedbackfull);
                TextView tv_name          = (TextView)view.findViewById(R.id.tv_name);
                TextView tv_place          = (TextView)view.findViewById(R.id.tv_place);
                String fb =  tv_feedback.getText().toString();
                String nm = tv_name.getText().toString();
                String plc = tv_place.getText().toString();
                booking.putExtra("feedback",fb);
                booking.putExtra("name",nm);
                booking.putExtra("place",plc);
                booking.putExtra("contact_no",favouriteItem.get(i).get("contact_number"));
                booking.putExtra("crop",favouriteItem.get(i).get("crop"));
                booking.putExtra("hybrid",favouriteItem.get(i).get("hybrid"));
                booking.putExtra("sowing_date",favouriteItem.get(i).get("sowing_date"));
                booking.putExtra("image",favouriteItem.get(i).get("image"));
                // booking.putExtra("company",       company);
                startActivity(booking);

            }
        });

        // new Async_getalloffline().execute();



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

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String,String>>();


        public ItemfavitemAdapter(FeedbackallActivity pd, ArrayList<HashMap<String, String>> results) {
            this.context = pd;
            this.results = results;

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
                convertView                 = inflater.inflate(R.layout.row_feedbacks, parent, false);
                holder.tv_name              = (TextView)convertView.findViewById(R.id.tv_name);
                holder.tv_place             = (TextView)convertView.findViewById(R.id.tv_place);
                holder.tv_feedback          = (TextView)convertView.findViewById(R.id.tv_feedback);
                holder.tv_fullfbk           = (TextView)convertView.findViewById(R.id.tv_feedbackfull);
                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.tv_name.setText(results.get(position).get("name"));
            holder.tv_place.setText(results.get(position).get("place"));
          //  holder.tv_place.setVisibility(View.GONE);
            holder.tv_feedback.setText(results.get(position).get("feedback"));
            fdbk = results.get(position).get("feedback");
            int len= fdbk.length();
            if(len > 60){
                holder.tv_feedback.setText(fdbk.substring(0,60)+"...");
            }
            else
                holder.tv_feedback.setText(fdbk);
            holder.tv_fullfbk.setText(fdbk);
            return convertView;
        }


        public class ViewHolder
        {
            public TextView  tv_name,tv_place,tv_feedback,tv_fullfbk;
            public ImageView img;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FROM_DAILY_FEEDBACK_ALL_ACT:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        this.recreate();
                        break;
                }

                break;
        }
    }
}



