package com.nsl.app.distributors;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.app.Crops;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.orderindent.NewSalesOrderChooseActivity;
import com.nsl.app.stockmovement.NewStockMovementChooseActivity;
import com.nsl.app.PaymentActivity;
import com.nsl.app.R;
import com.nsl.app.adapters.BankDetailsListAdapter;
import com.nsl.app.advancebooking.NewAdvancebokingChooseActivity;
import com.nsl.app.pojo.BankDetailsListPojo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_CODE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.app.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;


public class Distributor_details extends AppCompatActivity implements View.OnClickListener {

    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    int i = 0, sum_osa = 0;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    private String customer_id, comp_id;
    private String jsonData, userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    private String color;
    private String aging1 = null, aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    SharedPreferences.Editor editor;
    private TextView tv_name, tv_code, comapnyname;
    private StringBuilder builder = new StringBuilder();
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab_one, fab_advancebooking, fab_stock_movement, fab_payment_collection, fab_orderindent, fab_bankdetails;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private String customer_name, customer_code, division, divisions,company;
    int inside, outside, total_osa;
    public Dialog dialog;
    private RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_details);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        userId = sharedpreferences.getString("userId", "");
        db = new DatabaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent dist = new Intent(Distributor_details.this,DIstributorsListActivity.class);
                startActivity(dist);
                finish();
            }
        });

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_code = (TextView) findViewById(R.id.tv_code);
        comapnyname = (TextView) findViewById(R.id.textView11);

        tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_code.setText("(" + getIntent().getStringExtra("customer_code") + ")");
        company = getIntent().getStringExtra("company");
       // String osa = getIntent().getStringExtra("OSA");
        customer_id = getIntent().getStringExtra("customer_id");
        comp_id = getIntent().getStringExtra("company_id");

        customer_name = tv_name.getText().toString();
        customer_code = tv_code.getText().toString();

        listView = (ListView) findViewById(R.id.listView1);
        final ArrayList<HashMap<String, String>> feedList = new ArrayList<HashMap<String, String>>();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        fab_one = (FloatingActionButton) findViewById(R.id.fab_one);
        fab_bankdetails = (FloatingActionButton)findViewById(R.id.fab_banks_details);
        fab_advancebooking = (FloatingActionButton) findViewById(R.id.fab_advancebooking);
        fab_orderindent = (FloatingActionButton) findViewById(R.id.fab_orderindent);
        fab_payment_collection = (FloatingActionButton) findViewById(R.id.fab_payment_collection);
        fab_stock_movement = (FloatingActionButton) findViewById(R.id.fab_stock_movements);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clock);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlock);

        fab_bankdetails.setVisibility(View.GONE);
        fab.setOnClickListener(this);
        fab_bankdetails.setOnClickListener(this);
        fab_advancebooking.setOnClickListener(this);
        fab_orderindent.setOnClickListener(this);
        fab_payment_collection.setOnClickListener(this);
        fab_stock_movement.setOnClickListener(this);
        fab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // creating custom Floating Action button
                CustomDialog();
              //  Popup();
            }
        });

        fetchdivisionid();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView divtext = (TextView) findViewById(R.id.tv_div);
                String div_value;
                div_value = divtext.getText().toString();


            }
        });
        adapter = new ItemfavitemAdapter(Distributor_details.this, favouriteItem);
        listView.setAdapter(adapter);
        new Async_getalloffline().execute();

       /* List<BankDetailsListPojo> detailsListPojos = db.getBankDetails(customer_id);
        setListOnAdapter(detailsListPojos,recycler_view);*/


    }

 /*   private void Popup() {
        dialog = new Dialog(Distributor_details.this);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.popup);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.CENTER | Gravity.CENTER;
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.show();
    }*/

    private void fetchdivisionid() {

        /*String selectQuery  = "SELECT division_id" +
                " FROM user_company_division WHERE user_id = "+userId +" AND company_id = "+comp_id;*/
        String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_DIVISION_CODE + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " and " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + comp_id + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
        sdbw = db.getWritableDatabase();
        Log.e("Distributor selectQuery", selectQuery);
        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        System.out.println("cursor count "+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {

                builder.append(cursor.getString(0));
                if (!cursor.isLast())
                    builder.append(",");
                divisions = builder.toString();


                {


                }


            } while (cursor.moveToNext());
        }
    }

    public void CustomDialog() {

        dialog = new Dialog(Distributor_details.this);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.dialogbox);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        // initialize the item of the dialog box, whose id is demo1
        View demodialog = (View) dialog.findViewById(R.id.cross);
        View bankdetails = (View) dialog.findViewById(R.id.fab_banks_details);
        View advbooking = (View) dialog.findViewById(R.id.fab_advancebooking);
        View orderindent = (View) dialog.findViewById(R.id.fab_orderindent);
        View stockmovement = (View) dialog.findViewById(R.id.fab_stock_movements);
        View paymentcollection = (View) dialog.findViewById(R.id.fab_payment_collection);
        // it call when click on the item whose id is demo1.
        demodialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                dialog.dismiss();
            }
        });
      /*  bankdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("customer_id", customer_id);
                editor.putString("customer_name", customer_name);
                editor.commit();
                Intent bank = new Intent(getApplicationContext(), MainBankDetailsActivity.class);
                bank.putExtra("selection", "bank");
                startActivity(bank);
                dialog.dismiss();
                finish();
            }
        });*/
        advbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("customer_id", customer_id);
                editor.putString("customer_name", customer_name);
                editor.commit();
                Intent advancebooking = new Intent(getApplicationContext(), NewAdvancebokingChooseActivity.class);
                advancebooking.putExtra("selection", "distributor");
                startActivity(advancebooking);
                dialog.dismiss();
                finish();
            }
        });
        orderindent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("customer_id", customer_id);
                editor.putString("customer_name", customer_name);
                editor.commit();
                Intent orderindent = new Intent(getApplicationContext(), NewSalesOrderChooseActivity.class);
                orderindent.putExtra("selection", "distributor");
                startActivity(orderindent);
                dialog.dismiss();
                finish();
            }
        });
        stockmovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("customer_id", customer_id);
                editor.putString("customer_name", customer_name);
                editor.commit();
                Intent advancebooking = new Intent(getApplicationContext(), NewStockMovementChooseActivity.class);
                advancebooking.putExtra("selection", "distributor");
                startActivity(advancebooking);
                dialog.dismiss();
                finish();
            }
        });
        paymentcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payment = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivity(payment);
                dialog.dismiss();
                finish();
            }
        });


        // it show the dialog box
        dialog.show();

    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab_bankdetails.startAnimation(fab_close);
            fab_advancebooking.startAnimation(fab_close);
            fab_orderindent.startAnimation(fab_close);
            fab_payment_collection.startAnimation(fab_close);
            fab_stock_movement.startAnimation(fab_close);
            fab_bankdetails.setClickable(false);
            fab_advancebooking.setClickable(false);
            fab_orderindent.setClickable(false);
            fab_payment_collection.setClickable(false);
            fab_stock_movement.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab_bankdetails.startAnimation(fab_open);
            fab_advancebooking.startAnimation(fab_open);
            fab_orderindent.startAnimation(fab_open);
            fab_payment_collection.startAnimation(fab_open);
            fab_stock_movement.startAnimation(fab_open);
            fab_bankdetails.setClickable(true);
            fab_advancebooking.setClickable(true);
            fab_orderindent.setClickable(true);
            fab_payment_collection.setClickable(true);
            fab_stock_movement.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab_banks_details:

                editor.putString("customer_id", customer_id);
                editor.putString("customer_name", customer_name);
                editor.commit();
                Intent advancebooking = new Intent(getApplicationContext(), NewAdvancebokingChooseActivity.class);
                advancebooking.putExtra("selection", "distributor");
               // startActivity(advancebooking);
                break;
            case R.id.fab_advancebooking:

                editor.putString("customer_id", customer_id);
                editor.putString("customer_name", customer_name);
                editor.commit();
                 advancebooking = new Intent(getApplicationContext(), NewAdvancebokingChooseActivity.class);
                advancebooking.putExtra("selection", "distributor");
                startActivity(advancebooking);
                break;
            case R.id.fab_orderindent:

                editor.putString("customer_id", customer_id);
                editor.putString("customer_name", customer_name);
                editor.commit();
                Intent orderindent = new Intent(getApplicationContext(), NewSalesOrderChooseActivity.class);
                orderindent.putExtra("selection", "distributor");
                startActivity(orderindent);
                break;
            case R.id.fab_stock_movements:

                Log.d("Raj", "stock_movements");
                break;
            case R.id.fab_payment_collection:

                Intent payment = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivity(payment);
                break;
        }
    }

    private class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Distributor_details.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {


            try {
                List<Crops> cdcList = new ArrayList<>();

               /* String selectQuery = "SELECT CD." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + ","
                        + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET + "," + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET
                        + " FROM " + TABLE_CUSTOMERS + " AS C LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID
                        + " LEFT JOIN " + TABLE_DIVISION + " AS D ON D." + KEY_TABLE_DIVISION_MASTER_ID + " = " + " CD." + KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID
                        + " WHERE C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + customer_id + " group by(C." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";*/

                String selectQuery = "SELECT D.division_id," +
                        " D.division_name," +
                        " CD.credit_limit," +
                        " CD.inside_bucket," +
                        " CD.outside_bucket FROM customer_details CD LEFT JOIN division D ON D.division_id = CD.division_id " +
                        " where customer_id =" + customer_id + " AND CD.division_id IN (" + divisions + ")";
                System.out.println("??????" + selectQuery);

                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        i = i + 1;


                       /* System.out.println("******----" + cursor.getString(0) + cursor.getString(1) + cursor.getString(2) + cursor.getString(3));
                        Log.d("div id", cursor.getString(0));
                        Log.d("div name ", cursor.getString(1));
                        Log.d("inside bucket", cursor.getString(2));
                        Log.d("outside bucket", cursor.getString(3));*/
                        /*if (cursor.getString(3) != null && cursor.getString(4) != null) {
                            inside = Integer.parseInt(cursor.getString(3));
                            outside = Integer.parseInt(cursor.getString(4));
                            total_osa = inside + outside;
                            sum_osa = total_osa;
                        }*/
                        String inside_value = cursor.getString(3);
                        String outside_value = cursor.getString(4);
                        int inside = (inside_value!=null && !inside_value.equals("null")) ? Integer.parseInt(inside_value):0;
                        int outside = (outside_value!=null && !outside_value.equals("null")) ? Integer.parseInt(outside_value):0;

                        total_osa = inside + outside;
                        sum_osa = total_osa;

                        if (outside > 0) {
                            aging1 = ">";
                            aging2 = "90";
                            //color= "#ff0000";

                        } else  {
                            aging1 = "<";
                            aging2 = "90";
                            // color="#008000";
                        }

                        String creditLimit1 = cursor.getString(2);
                        int creditLimit = Integer.parseInt(creditLimit1) - (sum_osa);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("sr", String.valueOf(i));
                        map.put("did", cursor.getString(0));
                        map.put("division", cursor.getString(1));
                        map.put("OSA", String.valueOf(sum_osa <=0 ? 0:sum_osa));
                        map.put("Aging1", aging1);
                        map.put("Aging2", aging2);
                        map.put("sum_osa", String.valueOf(sum_osa <=0 ? 0:sum_osa));
                        map.put("credit_limit",String.valueOf(creditLimit));
                        favouriteItem.add(map);

                    } while (cursor.moveToNext());
                    //
                }

                // do some stuff....
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
            if (favouriteItem.size() == 0) {
               /*tv_alert.setVisibility(View.VISIBLE);
               lv_content.setVisibility(View.GONE);*/

                listView.setEmptyView(findViewById(R.id.tv_alert));
            } else {
               /* tv_alert.setVisibility(View.GONE);
                lv_content.setVisibility(View.VISIBLE);*/
                adapter.updateResults(favouriteItem);

            }
            comapnyname.setText(company);
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
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(Distributor_details pd, ArrayList<HashMap<String, String>> results) {
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
                convertView = inflater.inflate(R.layout.row_distributor_details, parent, false);
                holder.tv_sr = (TextView) convertView.findViewById(R.id.tv_sr);
                holder.tv_division = (TextView) convertView.findViewById(R.id.tv_div);
                holder.tv_osa = (TextView) convertView.findViewById(R.id.tv_osa);
                holder.tv_credit_limit = (TextView) convertView.findViewById(R.id.tv_credit_limit);
                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                if (aging1 != null && aging1.equals("<")) {
                    holder.img.setImageResource(R.drawable.less);
                } else if (aging1 != null && aging1.equals(">")) {
                    holder.img.setImageResource(R.drawable.greater);
                }
                holder.tv_aging2 = (TextView) convertView.findViewById(R.id.tv_aging2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            //holder.tv_sr.setText(results.get(position).get("sr"));
            holder.tv_division.setText(results.get(position).get("division"));
            holder.tv_osa.setText(results.get(position).get("OSA"));
            //holder.tv_aging1.setText(results.get(position).get("Aging1"));
            holder.tv_aging2.setText(results.get(position).get("Aging2"));
            holder.tv_credit_limit.setText(results.get(position).get("credit_limit"));

            return convertView;
        }


        public class ViewHolder {
            public TextView tv_sr, tv_division, tv_osa, tv_aging2,tv_credit_limit;
            public ImageView img;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }


    public void setListOnAdapter(final List<BankDetailsListPojo>  bankDetailsListPojo, RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        BankDetailsListAdapter bankDetailsListAdapter = null;

        if (bankDetailsListPojo.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
           // empty.setVisibility(View.GONE);
            bankDetailsListAdapter = new BankDetailsListAdapter(getApplicationContext(), bankDetailsListPojo);
            recyclerView.setAdapter(bankDetailsListAdapter);
            bankDetailsListAdapter.setOnItemClickListener(new BankDetailsListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {


                }
            });
           // stockPlacementPopupListAdapter.notifyDataSetChanged();

        } else if (bankDetailsListPojo == null || bankDetailsListPojo.size() == 0) {

           // empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }
}



