package com.nsl.app.complaints;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Complaintsselectactivity extends AppCompatActivity {
    // JSON parser class


    private ListView listView;

    String jsonData, userId, companyinfo;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    int cnt=0, cnt1=0;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    CardView r, p;
    String checkuid;
    Button reg, prod, refresh;
    TextView countreg, countprod;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintsselectactivity);
        sharedpreferences = Complaintsselectactivity.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");

        reg = (Button) findViewById(R.id.btnreg);
        prod = (Button) findViewById(R.id.btnprod);

        countreg = (TextView) findViewById(R.id.regcount);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        // countreg.startAnimation(anim);


        countprod = (TextView) findViewById(R.id.prodcount);
        // countprod.startAnimation(anim);


        r = (CardView) findViewById(R.id.btnregcard);
        p = (CardView) findViewById(R.id.btnprodcard);

        db = new DatabaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Complaintsselectactivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Complaintsselectactivity.this, Complaintsselectactivity.class);
                startActivity(i);
                finish();
            }
        });

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(Complaintsselectactivity.this, ComplaintsregallActivity.class);
                startActivity(reg);
                finish();
            }
        });


        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(Complaintsselectactivity.this, ComplaintsprodallActivity.class);
                startActivity(reg);
                finish();
            }
        });

        Log.d("Reading: ", "Reading all complaints from sqlite..");
        List<Complaints> complaints = db.getAllComplaint(checkuid);

        for (Complaints cm : complaints) {
            String log = "Id: " + cm.getID() + "Userid: " + cm.get_user_id() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                    cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                    + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                    + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                    + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                    ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
            Log.e("no connection: ", log);
            if(cm.get_complaint_type().equalsIgnoreCase("regulatory")){
                cnt++;
                Log.e("reg = ", String.valueOf(cnt));
            }
            else if(cm.get_complaint_type().equalsIgnoreCase("product")){
                cnt1++;
                Log.e("prod = ", String.valueOf(cnt1));
            }

        }
     /*   String countQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + checkuid + "' AND LOWER(" + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + ") = 'regulatory'";
        Log.e("Query", countQuery);
        sdbw = db.getReadableDatabase();
        Cursor cursor = sdbw.rawQuery(countQuery, null);
        cnt = cursor.getCount();
        //   countreg.setText(cnt);
        cursor.close();*/
        countreg.setText(String.valueOf(cnt));
        Log.e("regulatory", String.valueOf(cnt));
        // countreg.setText(cnt);
      /*  String countQuery1 = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + checkuid + "' AND LOWER(" + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + ") = 'product'";
        Log.e("Query", countQuery1);
        sdbw = db.getReadableDatabase();
        Cursor cursor1 = sdbw.rawQuery(countQuery1, null);
        cnt1 = cursor1.getCount();
        cursor.close();*/
        countprod.setText(String.valueOf(cnt1));
        Log.e("Product", String.valueOf(cnt1));


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


}






