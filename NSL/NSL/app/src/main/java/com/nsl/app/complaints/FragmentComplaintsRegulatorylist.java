package com.nsl.app.complaints;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class

FragmentComplaintsRegulatorylist extends Fragment {
    private JSONObject JSONObj;
    private JSONArray JSONArr = null;

    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    String statusval,id,company_id,division_id,crop_id,product_id,marketing_lot_number,complaint_type,farmer_name,contact_no,complaint_area_acres,soil_type;
    int i = 0, sum_osa = 0;

    private ListView listView, listViewproducts;
    public ItemfavitemAdapter adapter, adapter1;
    String customer_id, comp_id;
    String jsonData, userId,cmts,status1;
    int status;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> favouriteItem1 = new ArrayList<HashMap<String, String>>();
    String color;
    Button all_regulatory, all_products;
    String aging1 = null, aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name, tv_code, tv_total_osa_amt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_complaint_reg, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        db = new DatabaseHandler(getActivity());
        //DatabaseHandler db=new DatabaseHandler(getActivity());
        adapter = new ItemfavitemAdapter(getActivity(), favouriteItem);
        //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
        listView.setAdapter(adapter);
       // db.deleteComplaints();

       // new Async_getalloffline().execute();
       // FragmentComplaintsProductslis fp = new FragmentComplaintsProductslis(getApplicationContext());
        try {
           /* ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo != null) {  // connected to the internet
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    new Async_getallComplaints().execute();
                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan

                    new Async_getallComplaints().execute();
                }
            } else {

            }*/

            new Async_getalloffline().execute();

        } catch (Exception e) {
           // Log.d("Tag", e.toString());
        }



//        listViewproducts.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent detail = new Intent(getActivity(),AllregulatoryActivity.class);
                TextView comts = (TextView)view.findViewById(R.id.tv_name);
                TextView tv_place = (TextView) view.findViewById(R.id.tv_place);
                TextView tv_comptype = (TextView) view.findViewById(R.id.tv_complainttype);
                TextView tv_billno = (TextView) view.findViewById(R.id.tv_billno);
                TextView tv_retailer = (TextView) view.findViewById(R.id.tv_retailername);
                TextView tv_status_value = (TextView)view.findViewById(R.id.statusval);
              //  ImageView status_icon = (ImageView) view.findViewById(R.id.status_icon);

                String comments = comts.getText().toString();
                String place = tv_place.getText().toString();
                String comp_type=tv_comptype.getText().toString();
                String billno=tv_billno.getText().toString();
                String retailer=tv_retailer.getText().toString();
                String statusval=tv_status_value.getText().toString();

                detail.putExtra("comments",comments);
                detail.putExtra("place",place);
                detail.putExtra("comp_type",comp_type);
                detail.putExtra("billno",billno);
                detail.putExtra("retailer",retailer);
                detail.putExtra("status",statusval);
                detail.putExtra("remarks",favouriteItem.get(position).get("remarks"));
                Toast.makeText (getActivity(),"status "+statusval ,Toast.LENGTH_SHORT).show();
                startActivity(detail);
            }
        });
        //new Async_getalloffline().execute();
        return view;
    }




    private class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {

            final DatabaseHandler db = new DatabaseHandler(getActivity());
           try {
               Log.e("Reading: ", "Reading all Complaints from sqlite..");
                List<Complaints> complaints = db.getAllComplaintreg(userId);
               for (Complaints cm : complaints) {
                   String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                           cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                           + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                           + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                           + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                           ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                   Log.e("complaints: ", log);
               }
                for (Complaints fb : complaints) {


                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("sr", String.valueOf(fb.getID()));
                    map.put("name", fb.get_farmer_name());
                    map.put("area", fb.get_complaint_area_acres());
                    map.put("type", fb.get_complaint_type());
                    map.put("billno", fb.get_bill_number());
                    map.put("retailer", fb.get_retailer_name());
                    map.put("comments",fb.get_comments());
                    map.put("status", String.valueOf(fb.get_status()));
                    //    map.put("complaint_type",fb.get_complaint_type());
                    String comp_type = fb.get_complaint_type();
                    if (comp_type.equals("Regulatory")) {
                        favouriteItem.add(map);
                    } else if (comp_type.equals("Products")) {
                        favouriteItem1.add(map);
                    }

                    //  }

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
            adapter.updateResults(favouriteItem);
//            adapter1.updateResults(favouriteItem1);
            // tv_total_osa_amt.setText(String.valueOf(sum_osa));
        }
    }
    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> results) {
            this.context = context;
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
                // sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_complaints, parent, false);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
                holder.tv_comptype = (TextView) convertView.findViewById(R.id.tv_complainttype);
                holder.tv_billno = (TextView) convertView.findViewById(R.id.tv_billno);
                holder.tv_retailer = (TextView) convertView.findViewById(R.id.tv_retailername);
                holder.status_icon = (ImageView)convertView.findViewById(R.id.status_icon);
                holder.tv_status_value = (TextView)convertView.findViewById(R.id.statusval);

                // holder.tv_aging1          = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.tv_name.setText("Name : " + results.get(position).get("name"));
            holder.tv_place.setText("Complaint Area : " + results.get(position).get("area"));
            holder.tv_comptype.setText("Comments : " + results.get(position).get("comments"));
            holder.tv_billno.setText("Bill No : " + results.get(position).get("bill"));
            holder.tv_retailer.setText(results.get(position).get("retailer"));
            holder.tv_status_value.setText(results.get(position).get("status"));
            status1 = results.get(position).get("status");

             status = Integer.parseInt(status1);
            if(status==1){
                holder.status_icon.setImageResource(R.drawable.icon4);
            }
            else if(status==2){
                holder.status_icon.setImageResource(R.drawable.icon5);
            }
            else if(status==3){
                holder.status_icon.setImageResource(R.drawable.icon6);
            }
            cmts =results.get(position).get("comments");
            int len= cmts.length();
            if(len > 60){
                holder.tv_comptype.setText(cmts.substring(0,80)+"...");
            }
            else
            holder.tv_comptype.setText(cmts);
            holder.tv_name.setText("Comments:"+cmts);
            return convertView;
        }


        public class ViewHolder {
            public TextView tv_name, tv_place, tv_comptype, tv_billno, tv_retailer,tv_status_value;
            public ImageView img,status_icon;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }



    }

