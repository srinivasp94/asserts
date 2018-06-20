package com.nsl.app.distributors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;
import com.nsl.app.commonutils.Common;

import java.util.ArrayList;


public class DIstributorsListActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView mRecyclerView;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    private String jsonData, team, company_id, company, checkuid;
    ArrayList<Model> favouriteItem = new ArrayList<Model>();
    private int user_pos;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private Spinner spin_distribute;
    ArrayList<String> optionsList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributors_list);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        team = sharedpreferences.getString("team", "");
        checkuid = sharedpreferences.getString("userId", "");
        Log.e("userid is : ", checkuid);
        Log.d("team", team);
        db = new DatabaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        System.out.println("&&&&&" + getIntent().getStringExtra("company_id"));
        //  System.out.println("&&&&&" + db.getAllUser_Company_Customer().get(0).getuccCustomerId());

        company_id = getIntent().getStringExtra("company_id");
        company = getIntent().getStringExtra("company_name");
        spin_distribute = (Spinner) findViewById(R.id.spin_distribute);




        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(favouriteItem, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);


        new Async_getalloffline().execute();
    }

    private class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DIstributorsListActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {
            favouriteItem.clear();
            String temp;
            try {
                String[] splitedArr = team.split(",");

                for (int i = 0; i < splitedArr.length; i++) {
                    if(splitedArr[i].equals(checkuid)){
                        temp = splitedArr[0];
                        splitedArr[0]=splitedArr[i];
                        splitedArr[i]=temp;
                    }
                    System.out.println("arr"+splitedArr[i]);
                }
                for (int i = 0; i < splitedArr.length; i++) {
                    System.out.println("arroutside" + splitedArr[i]);
                }
                for (int i = 0; i < splitedArr.length; i++) {
                    // arr[i] = Integer.parseInt(splitedArr[i]);
                    sdbw = db.getWritableDatabase();

                   // System.out.println(Arrays.toString(arr));



                    String selectQuery = "SELECT " + db.KEY_TABLE_USERS_FIRST_NAME + "," + db.KEY_TABLE_USERS_SAP_ID + " FROM " + db.TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_MASTER_ID + " = " + splitedArr[i];

                    Log.e("MO selectQuery", selectQuery);
                    Cursor cursor = sdbw.rawQuery(selectQuery, null);

                    if (cursor.moveToFirst()) {


                            favouriteItem.add(new Model(Model.MO_TYPE, "",  "","","","",cursor.getString(0),  cursor.getString(1)));


                    }




                        String selectQuery1 = "SELECT DISTINCT (CR.customer_id)," +
                                "CR.customer_name, " +
                                "CR.customer_code, " +
                                "C.company_id, " +
                                "C.company_code" +
                                " FROM user_company_customer AS CDC JOIN customers AS CR ON CDC.customer_id = CR.customer_id " +
                                "JOIN companies AS C ON C.company_id = CDC.company_id WHERE user_id =" + splitedArr[i];





                        Log.e("Distributor selectQuery", selectQuery1);
                        Cursor cursor1 = sdbw.rawQuery(selectQuery1, null);

                        if (cursor1.moveToFirst()) {
                            do {

                                favouriteItem.add(new Model(Model.RM_TYPE, cursor1.getString(0),  cursor1.getString(1),cursor1.getString(2),  cursor1.getString(3),cursor1.getString(4), "",""));


                            } while (cursor1.moveToNext());
                        }
                    }

                // do some stuff....
            } catch (Exception e) {
                e.printStackTrace();
            }
            Common.Log.i(favouriteItem.toString());

            return jsonData;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


            MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(favouriteItem, DIstributorsListActivity.this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DIstributorsListActivity.this, OrientationHelper.VERTICAL, false);

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(linearLayoutManager);
           // mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new MultiViewTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent booking = new Intent(getApplicationContext(), Distributor_details.class);

                    //   booking.putExtra("company_name",  company);
                    booking.putExtra("customer_code", favouriteItem.get(position).customer_code);
                    booking.putExtra("customer_id",   favouriteItem.get(position).customer_id);
                    booking.putExtra("customer_name", favouriteItem.get(position).customer_name);
                    booking.putExtra("company_id",    favouriteItem.get(position).company_id);
                   // booking.putExtra("OSA",           favouriteItem.get(position).get("osa"));
                    booking.putExtra("company",       favouriteItem.get(position).company_code);
                    startActivity(booking);
                    finish();
                }
            });

        }
    }
}

