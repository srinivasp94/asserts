package com.nsl.app;


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


public class Payment_collection_detailsActivity extends AppCompatActivity {

    private JSONObject JSONObj;
    private JSONArray JSONArr=null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;
    int i=0,sum_osa=0,inside_bucket,outside_bucket;
    private ListView listView;
    ImageView ageimg;
    private ItemfavitemAdapter adapter;
    String customer_id,comp_id,comp_code,ageval;
    String jsonData,userId,company;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    Button history;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name,tv_code,Company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");
        db=new DatabaseHandler(this);
        Toolbar toolbar   = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent dtls = new Intent(Payment_collection_detailsActivity.this,PaymentActivity.class);

                dtls.putExtra("customer_id",customer_id);
                dtls.putExtra("company_code",comp_code);
                dtls.putExtra("company_id",comp_id);
                startActivity(dtls);*/
                finish();
            }
        });
        ageimg = (ImageView)findViewById(R.id.iv_ageimg);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_code = (TextView)findViewById(R.id.tv_code);
        Company = (TextView)findViewById(R.id.textView11);
        history = (Button)findViewById(R.id.btn_history);
        tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_code.setText(getIntent().getStringExtra("customer_code"));
        company=getIntent().getStringExtra("company_name");
        customer_id = getIntent().getStringExtra("customer_id");
        comp_id = getIntent().getStringExtra("company_id");
        comp_code = getIntent().getStringExtra("company_code");
        final String customer_name,customer_code,division;
        customer_name=tv_name.getText().toString();
        customer_code=tv_code.getText().toString();

        listView = (ListView) findViewById(R.id.listView1);
        final ArrayList<HashMap<String, String>> feedList= new ArrayList<HashMap<String, String>>();
        Company.setText(company);

       history.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent dtls = new Intent(Payment_collection_detailsActivity.this,Paymentcollection_All_Activity.class);
               dtls.putExtra("customer_name",tv_name.getText().toString());
               dtls.putExtra("customer_code",tv_code.getText().toString());
               dtls.putExtra("customer_id",customer_id);
               dtls.putExtra("company_code",comp_code);
               dtls.putExtra("company_id",comp_id);
               startActivity(dtls);

           }
       });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView divtext=(TextView)findViewById(R.id.tv_div);
                String div_value;
                div_value = divtext.getText().toString();

               //Toast.makeText(Payment_collection_detailsActivity.this,"you selcted"+ div_value + i + "division",Toast.LENGTH_SHORT).show();
                Common.Log.i(customer_id);
                Intent details = new Intent(Payment_collection_detailsActivity.this,PaymentCollectionPaymentTypeActivity.class);
                details.putExtra("customer_name",customer_name);
                details.putExtra("customer_code",customer_code);
                details.putExtra("customer_id",customer_id);
                details.putExtra("company_name" , company);
                details.putExtra("company_code",comp_code);
                details.putExtra("company_id",comp_id);
                details.putExtra("inside_bucket",String.valueOf(inside_bucket));
                details.putExtra("outside_bucket",String.valueOf(outside_bucket));
                details.putExtra("division_id",favouriteItem.get(i).get("did"));
                details.putExtra("division",div_value);
                details.putExtra("total_osa",favouriteItem.get(i).get("OSA"));
                startActivity(details);

            }
        });
        new Async_getalloffline().execute();

        adapter = new ItemfavitemAdapter(Payment_collection_detailsActivity.this, favouriteItem);
         listView.setAdapter(adapter);




    }

    private class Async_getalloffline extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Payment_collection_detailsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params)
        {


            try {
                List<Crops> cdcList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                //String selectQuery = "SELECT  " + KEY_SCHEMES_MASTER_ID + ","+ KEY_SCHEMES_SAP_CODE + " FROM " + TABLE_SCHEMES + "  where " + KEY_SCHEMES_COMPANY_ID + " = " + company_id + " and " + KEY_SCHEMES_DIVISION_ID + " = " + sel_division_id + " and " + KEY_SCHEMES_CROP_ID + " = " + sel_crop_id ;
                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();



            //    String selectQuery = "SELECT D." +KEY_TABLE_DIVISION_MASTER_ID +","+ KEY_TABLE_DIVISION_NAME + ","+ KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET + ","+ KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET +  " FROM " + TABLE_CUSTOMERS + " AS C LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD."+KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C."+ KEY_TABLE_CUSTOMER_MASTER_ID + " LEFT JOIN " + TABLE_DIVISION + " AS D ON D."+KEY_TABLE_DIVISION_MASTER_ID + " = " + " CD." + KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID + " WHERE C." + KEY_TABLE_CUSTOMER_MASTER_ID+ " = " + customer_id+ " group by(C." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
                String selectQuery = "SELECT D.division_id," +
                        " D.division_name," +
                        " CD.credit_limit," +
                        " CD.inside_bucket," +
                        " CD.outside_bucket FROM customer_details CD LEFT JOIN division D ON D.division_id = CD.division_id " +
                        " where customer_id =" + customer_id /*+ " AND CD.division_id IN (" + divisions + ")"*/;
                System.out.println(selectQuery);


                // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                         i=i+1;
                        /*Customers customers = new Customers();

                        customers.setCusMasterID(cursor.getString(0));
                        customers.setCusName(cursor.getString(1));
                        customers.setCuscode(cursor.getString(2));*/

                        System.out.println("******----" + cursor.getString(0)+ cursor.getString(1)+ cursor.getString(2) +cursor.getString(3));
                       /* Log.d("div id", cursor.getString(0));
                        Log.d("div name ", cursor.getString(1));
                        Log.d("inside bucket",cursor.getString(2));
                        Log.d("outside bucket",cursor.getString(3));
*/
                        inside_bucket = Integer.parseInt(cursor.getString(3));
                        outside_bucket = Integer.parseInt(cursor.getString(4));


                        int inside = Integer.parseInt(cursor.getString(3));
                        int outside = Integer.parseInt(cursor.getString(4));
                        int total_osa= inside+outside;
                        sum_osa = total_osa;
                        String aging = null;
                        if(outside>0) {
                            aging = "90";
                            ageval = "g";

                        }
                        else {
                            aging = "90";
                            ageval = "l";
                        }
                        if (sum_osa<=0){
                            sum_osa=0;
                        }
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("sr", String.valueOf(i));
                        map.put("did",cursor.getString(0));
                        map.put("division", cursor.getString(1));
                        map.put("OSA", String.valueOf(sum_osa));
                        map.put("ageval",ageval);
                        map.put("Aging", aging);
                        map.put("sum_osa", String.valueOf(sum_osa));
                        favouriteItem.add(map);

                       /* map = new HashMap<String, String>();
                        map.put("sr", "2");
                        map.put("division", "division2");
                        map.put("OSA", "amount");
                        map.put("Aging", "<90");
                        favouriteItem.add(map);*/



                    } while (cursor.moveToNext());

                }

                // do some stuff....
            }  catch (Exception e) {
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
            adapter = new ItemfavitemAdapter(Payment_collection_detailsActivity.this, favouriteItem);
            listView.setAdapter(adapter);
            //Company.setText(String.valueOf(company));
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

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String,String>>();


        public ItemfavitemAdapter(Payment_collection_detailsActivity pd, ArrayList<HashMap<String, String>> results) {
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
                convertView                 = inflater.inflate(R.layout.column_row_payment_collection_details, parent, false);
                holder.tv_sr                = (TextView)convertView.findViewById(R.id.tv_sr);
                holder.tv_division          = (TextView)convertView.findViewById(R.id.tv_div);
                holder.tv_osa               = (TextView)convertView.findViewById(R.id.tv_osa);
                holder.ageimg               = (ImageView)convertView.findViewById(R.id.iv_ageimg);
                holder.tv_aging             = (TextView)convertView.findViewById(R.id.tv_aging);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.tv_sr.setText(results.get(position).get("sr"));
            holder.tv_division.setText(results.get(position).get("division"));
            holder.tv_osa.setText(results.get(position).get("OSA"));

            String val = results.get(position).get("ageval");
            if(val!=null && val.equals("g"))
            {
                holder.ageimg.setBackgroundResource(R.drawable.greater);
            }
            else if(val!=null && val.equals("l")){
                holder.ageimg.setBackgroundResource(R.drawable.less);
            }

            holder.tv_aging.setText(results.get(position).get("Aging"));

            return convertView;
        }


        public class ViewHolder
        {
            public TextView  tv_sr,tv_division,tv_osa,tv_aging;
            public ImageView ageimg;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

//            this.results = results;
//            notifyDataSetChanged();
        }
    }




}



