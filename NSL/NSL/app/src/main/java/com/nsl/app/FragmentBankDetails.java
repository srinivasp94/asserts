package com.nsl.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nsl.app.commonutils.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



/**
 * Created by admin on 2/22/2017.
 */
public class FragmentBankDetails extends Fragment {
    DatabaseHandler db;
    Button submit,cancel;
    ProgressDialog progressDialog;
    private static SQLiteDatabase sql, sdbr,sdbw;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    EditText account_name, bank_name, acc_no, branch, ifsc;
    String jsonData,sqliteid,ffmid,checkuid;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private String customer_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());

       /* FragmentFeedback fb = new FragmentFeedback();
        fb.checkConnection();*/
        View view = inflater.inflate(R.layout.fragment_bank_details, container, false);
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid   = sharedpreferences.getString("userId", "");
        customer_id   = sharedpreferences.getString("customer_id", "");
        Log.e("cust_id=",checkuid);
        Toast.makeText(getActivity(),"id is "+checkuid,Toast.LENGTH_SHORT).show();
        account_name = (EditText) view.findViewById(R.id.etRetailerName);
        bank_name = (EditText) view.findViewById(R.id.ettinno);
        acc_no = (EditText) view.findViewById(R.id.etMobile);
        branch = (EditText) view.findViewById(R.id.etemail);
        ifsc = (EditText) view.findViewById(R.id.etphone);
        //address = (EditText) view.findViewById(R.id.etaddress);
        submit = (Button) view.findViewById(R.id.btnSubmit);
        cancel = (Button)view.findViewById(R.id.btncancel);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });



        return view;
    }

    private void validate() {
        String fid = "1";
        String retailer = account_name.getText().toString();
        String mobileno = bank_name.getText().toString();
        String phn = acc_no.getText().toString();
        String addr = branch.getText().toString();
        String eml = ifsc.getText().toString().trim();
        // String tin = tinno.getText().toString();

        if (TextUtils.isEmpty(retailer) || retailer.length() > 0 && retailer.startsWith(" ")) {
            account_name.setError("Please enter Account holder name");
            account_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mobileno) || mobileno.length() > 0 && mobileno.startsWith(" ")) {
            bank_name.setError("Please enter bank name");
            bank_name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phn) || phn.length() > 0 && phn.startsWith(" ")) {
            acc_no.setError("Please enter account number");
            acc_no.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(addr) || addr.length() > 0 && addr.startsWith(" ")) {
            branch.setError("Please enter Branch");
            branch.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(eml) || eml.length() > 0 && eml.startsWith(" ")) {
            ifsc.setError("Please enter IFSC");
            ifsc.requestFocus();
            return;
        }

        else {
            //  Toast.makeText(getActivity(), "Inserting...", Toast.LENGTH_SHORT).show();
            int fdid = Integer.parseInt(fid);
            //String img="xyz.jpg";
            String createdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            db.addCustomersBankDetails(new Customer_Bank_Details(String.valueOf(fdid),customer_id, phn, retailer, mobileno,  eml,addr,"1", checkuid,null,createdate,null));
            Log.e("retailer name ", retailer + ":" + phn + ":" + retailer);
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put("Id", fid);
            map.put("customer_id", customer_id);
            map.put("account_no", phn);
            map.put("account_name", retailer);
            map.put("bank_name", mobileno);
            map.put("branch_name", addr);
            map.put("ifsc_code", eml);
            map.put("status", "1");
            map.put("created_by", checkuid);
            map.put("updated_by", null);
            map.put("created_date", createdate);

            favouriteItem.add(map);
            // do some stuff....

        }
        if(Utility.isNetworkAvailable(getActivity(),Constants.isShowNetworkToast)){
            insertToService();
        }

    }

    private void insertToService() {
        db = new DatabaseHandler(getActivity());
        Log.d("Reading: ", "Reading all retailers..");


        List<Customer_BankDetails> feedback = db.getAllNullCustomerbankdetails(checkuid);

        Log.e("list size", String.valueOf(feedback.size()));

        if (feedback.size() == 0) {
            Log.e("no data found", "no data");
        } else {
            for (Customer_BankDetails cus : feedback) {

                String log = "Id: " + cus.getID() + " \n master ID" + cus.get_cus_bak_dtls_masterid() + " , \n customer_id: " + cus.get_customer_id() + "\n  account_no " + cus.get_account_no() + " \n account_name : " + cus.get_account_name() + " \n bank_name : " + cus.get_bank_name() + "\n branch_name " + cus.get_branch_name() + ":" + cus.get_ifsc_code() + " \n  status " + cus.get_status()+"ffmid "+cus.get_ffmid();
                // Writing Contacts to log
                Log.e("details before insert: ", log);
                if (Common.haveInternet(getContext())) {
                    new Async_InsertBankDetails().execute((String.valueOf(cus.getID())), cus.get_customer_id(), cus.get_account_no(),
                            cus.get_account_name(), cus.get_bank_name(), cus.get_branch_name(), cus.get_ifsc_code(), cus.get_status(),
                            cus.get_created_by(), cus.get_updated_by(), cus.get_created_date());
                }else {
                    getActivity().finish();
                }


            }
           /* Intent viewall = new Intent(getActivity(), PaymentActivity.class);
            startActivity(viewall);*/


        }
    }



    public class Async_InsertBankDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
                        .add("mobile_id",params[0])
                        .add("customer_id",params[1])
                        .add("account_no", params[2])
                        .add("account_name", params[3])
                        .add("bank_name", params[4])
                        .add("branch_name", params[5])
                        .add("ifsc_code",params[6])
                        .add("status", params[7])
                        .add("created_date",params[10])
                        .build();

                Response responses = null;

                System.out.println("---- bank data -----" +params[0]+ params[1]+params[2]+params[3]+params[4]+params[5]+params[6]+params[7]+params[8]+params[9]+params[10]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_BANK)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertBank" + jsonData);

                } catch (IOException e) {
                    e.printStackTrace();
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


            if (jsonData != null)
            {   JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    System.out.println("!!!!!!!1 postexecuteInsertbank" + jsonData);

                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        //  Toast.makeText(getActivity(), "Feed back inserted sucessfully", Toast.LENGTH_SHORT).show();
                        sqliteid = jsonobject.getString("sqlite");
                        ffmid = jsonobject.getString("ffm_id");

                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", sqliteid);
                        Log.e("ffmid", ffmid);
                        sdbw = db.getWritableDatabase();
                        // updateFeedback(Feedback feedback);
                        String updatequery = "UPDATE " + db.TABLE_CUSTOMER_BANKDETAILS + " SET " + db.KEY_BANKDETAIL_FFMID + " = '" + ffmid + "' WHERE " + db.KEY_BANKDETAIL_ID + " = '" + sqliteid+"'";
                        sdbw.execSQL(updatequery);
                        System.out.println(updatequery);
                        List<Customer_Bank_Details> feedback = db.getAllCustomerbankdetails(checkuid);

                        for (Customer_Bank_Details cus : feedback) {
                            String log = "Id: " + cus.getID() + " \n master ID" + cus.get_cus_bak_dtls_masterid() + " , \n customer_id: " + cus.get_customer_id() + "\n  account_no " + cus.get_account_no() + " \n account_name : " + cus.get_account_name() + " \n bank_name : " + cus.get_bank_name() + "\n branch_name " + cus.get_branch_name() + ":" + cus.get_ifsc_code() + " \n  status " + cus.get_status()+ " \n  ffmid " + cus.get_ffmid();
                            // Writing Contacts to log
                            Log.e("after BankDetails: ", log);
                        }

//                        Intent viewall = new Intent(getActivity(),PaymentActivity.class);
//                        startActivity(viewall);
                        getActivity().finish();

                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

        }
    }
}

