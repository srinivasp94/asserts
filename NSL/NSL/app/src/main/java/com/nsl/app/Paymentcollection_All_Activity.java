package com.nsl.app;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.nsl.app.commonutils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Paymentcollection_All_Activity extends AppCompatActivity {

    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    TextView empty;
    SQLiteDatabase sdbw, sdbr;
    int i = 0, sum_osa = 0, flag = 0;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    String comp_id,cust_name,cust_code,cust_id;
    String jsonData, userId, id, payment_type, company_id, division_id, customer_id, total_amount, payment_mode, bank_name, rtgs_or_neft_no, payment_datetime, date_on_cheque_no, cheque_no_dd_no, created_datetime, updated_datetime, ffmid, ffmidsqlite;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color, fdbk,bankname;
    Button refresh;
    String aging1 = null, aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tvc_name, tvc_code, tv_total_osa_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentcollection_all);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        tvc_name = (TextView)findViewById(R.id.tv_name);
        tvc_code = (TextView)findViewById(R.id.tv_code);
        userId = sharedpreferences.getString("userId", "");
        cust_name = getIntent().getStringExtra("customer_name");
        cust_code = getIntent().getStringExtra("customer_code");
        comp_id = getIntent().getStringExtra("company_id");
        cust_id = getIntent().getStringExtra("customer_id");
        tvc_name.setText(cust_name);
        tvc_code.setText(cust_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), Payment_collection_detailsActivity.class);
                home.putExtra("customer_name",cust_name);
                home.putExtra("customer_code",cust_code);
                home.putExtra("company_id",comp_id);
                home.putExtra("customer_id",cust_id);
                startActivity(home);
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
        refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ItemfavitemAdapter(Paymentcollection_All_Activity.this, favouriteItem);
        //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
        listView.setAdapter(adapter);
        db = new DatabaseHandler(Paymentcollection_All_Activity.this);
        empty = (TextView)findViewById(R.id.empty);
        // db.deletePayment();



                Log.e("Reading: ", "calling history..");

                List<Payment_collection> paymentCollections = db.getAllPaymentCollectionhistory(userId,cust_id);
               // List<Payment_collection> paymentCollections = db.getAllPaymentCollections(userId);
               if(paymentCollections.size()==0){
                   empty.setVisibility(View.VISIBLE);
               }

                else {
                   empty.setVisibility(View.GONE);
               }
                for (Payment_collection pc : paymentCollections) {
                    String log = "Id: " + pc.getID() + " ,pay_type: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() +
                            " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() +
                            " ,amt : " + pc.get_total_amount() + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() +
                            "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() +
                            ",cheque number:" + pc.get_cheque_no_dd_no() + ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() +
                            ",updated datetime:" + pc.get_updated_datetime() + ",ffmid:" + pc.get_ffmid();

                    Log.e("payment collection: ", log);
                }
                int srno = 1;
                for (Payment_collection fb : paymentCollections) {

                    String bankid  = fb.get_bank_name();

                    Log.e("bank id",bankid);
/*
                    String selectQuerys = "SELECT  " + KEY_BANKDETAIL_BANK_NAME  +" FROM " + TABLE_CUSTOMER_BANKDETAILS + " WHERE " + KEY_BANKDETAIL_MASTER_ID + " = '" + bankid+"'";
                    sdbw = db.getWritableDatabase();
                    Log.e("bank query",selectQuerys);
                    Cursor cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Customer_BankDetails companies = new Customer_BankDetails();
                        bankname = cc.getString(0);
                        companies.set_bank_name(bankname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("bank name",bankname);
                    }*/
                    Log.e("serial number", String.valueOf(srno));
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("srno", String.valueOf(srno));
                    map.put("sr", String.valueOf(fb.getID()));
                    map.put("name", fb.get_bank_name());
                    map.put("amount", fb.get_total_amount());
                    map.put("mode", fb.get_payment_mode());
                    map.put("type", fb.get_payment_type());
                    map.put("company_id", String.valueOf(fb.get_company_id()));
                    map.put("division_id", String.valueOf(fb.get_division_id()));
                    map.put("customer_id", String.valueOf(fb.get_customer_id()));
                    map.put("total_amount",fb.get_total_amount());
                    map.put("payment_mode",fb.get_payment_mode());
                    map.put("rtgs",fb.get_rtgs_or_neft_no());
                    map.put("pay_date",fb.get_payment_datetime());
                    map.put("dateoncheque",fb.get_date_on_cheque_no());
                    map.put("chequeno",fb.get_cheque_no_dd_no());
                    map.put("status", String.valueOf(fb.get_status()));
                    map.put("createddate",fb.get_created_datetime());
                    map.put("updateddate",fb.get_updated_datetime());
                    map.put("ffmid", fb.get_ffmid());

                    favouriteItem.add(map);
                   srno=srno+1;

                }
                adapter = new ItemfavitemAdapter(Paymentcollection_All_Activity.this, favouriteItem);
                //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
                listView.setAdapter(adapter);







        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(Paymentcollection_All_Activity.this, "selected " + i, Toast.LENGTH_LONG).show();

                Intent booking = new Intent(Paymentcollection_All_Activity.this, PaymentAll.class);


                booking.putExtra("name", favouriteItem.get(i).get("name"));
                booking.putExtra("amount", favouriteItem.get(i).get("amount"));
                booking.putExtra("mode", favouriteItem.get(i).get("mode"));
                booking.putExtra("type", favouriteItem.get(i).get("type"));
                booking.putExtra("company_id",favouriteItem.get(i).get("company_id"));
                booking.putExtra("division_id",favouriteItem.get(i).get("division_id"));
                booking.putExtra("customer_id",favouriteItem.get(i).get("customer_id"));
                booking.putExtra("total_amount",favouriteItem.get(i).get("total_amount"));
                booking.putExtra("payment_mode",favouriteItem.get(i).get("payment_mode"));
                booking.putExtra("rtgs",   favouriteItem.get(i).get("rtgs"));
                booking.putExtra("pay_date", favouriteItem.get(i).get("pay_date"));
                booking.putExtra("dateoncheque",favouriteItem.get(i).get("dateoncheque"));
                booking.putExtra("chequeno",favouriteItem.get(i).get("chequeno"));
                booking.putExtra("status",favouriteItem.get(i).get("status"));
                booking.putExtra("createddate",favouriteItem.get(i).get("createddate"));
                booking.putExtra("updateddate",favouriteItem.get(i).get("updateddate"));
                // booking.putExtra("ffmid",       company);
               // startActivity(booking);

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
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();

        String date;
        public ItemfavitemAdapter(Paymentcollection_All_Activity pd, ArrayList<HashMap<String, String>> results) {
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
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_payment_history, parent, false);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
                holder.tv_comptype = (TextView) convertView.findViewById(R.id.tv_complainttype);
                holder.tv_billno = (TextView) convertView.findViewById(R.id.tv_billno);
                holder.tv_retailer = (TextView) convertView.findViewById(R.id.tv_retailername);
                holder.tvsr = (TextView) convertView.findViewById(R.id.tv_sr);
                holder.tvrtgschq = (TextView) convertView.findViewById(R.id.tv_rtgs_chq);
                holder.tvpaydate = (TextView) convertView.findViewById(R.id.tv_paydate);
                holder.tvamt = (TextView) convertView.findViewById(R.id.tv_amount);
                holder.tvrtgschqno = (TextView) convertView.findViewById(R.id.tv_rtgsno);
                holder.tvbankname = (TextView) convertView.findViewById(R.id.tv_bankname);
                holder.tv_paymemt_type = (TextView) convertView.findViewById(R.id.tv_paymemt_type);
                holder.on = (TextView) convertView.findViewById(R.id.tv_osa);
                // holder.tv_aging1          = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
          /*  holder.tv_name.setText( results.get(position).get("name"));

            holder.tv_comptype.setText("Payment Type : " + results.get(position).get("type"));
            holder.tv_billno.setText("Payment Mode : " + results.get(position).get("mode"));*/
            holder.tvsr.setText("  "+results.get(position).get("srno")+".");
            holder.tvbankname.setText(results.get(position).get("name"));
            holder.tvamt.setText(results.get(position).get("amount"));
            if (results.get(position).get("mode").equalsIgnoreCase("Online")) {
                holder.tvrtgschq.setText("RTGS/NEFT");
                holder.tvrtgschqno.setText(results.get(position).get("rtgs"));
            } else if (results.get(position).get("mode").equalsIgnoreCase("Cheque/dd")) {
                holder.tvrtgschq.setText("CHEQUE/DD");
                holder.tvrtgschqno.setText(results.get(position).get("chequeno"));
            }else {
                holder.tvrtgschq.setVisibility(View.INVISIBLE);
                holder.on.setVisibility(View.INVISIBLE);
            }
            if (results.get(position).get("type").equalsIgnoreCase("1")) {
                holder.tv_paymemt_type.setText("ABS");

            }else {
                holder.tv_paymemt_type.setText("Normal");
            }

            date = results.get(position).get("pay_date");

            if (date.contains("00:00:00")) {
                date = date.substring(0, date.length() - 8);

            }
            holder.tvpaydate.setText(Common.setDateFormateOnTxt(date));


            return convertView;
        }


        public class ViewHolder {
            public TextView  tv_name, tv_place, tv_comptype, tv_billno, tv_retailer,tvsr,tvrtgschq,tvpaydate,tvrtgschqno,tvbankname,tvamt;
            public ImageView img;
            public TextView tv_paymemt_type;
            public TextView on;
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }



}




