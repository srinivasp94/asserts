package com.nsl.app.complaints;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_BILL_NUMBER;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_COMMENTS;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_FARMER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_RETAILER_NAME;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_COMPLAINTS_STATUS;
import static com.nsl.app.DatabaseHandler.TABLE_COMPLAINT;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentComplaintsProductslis extends Fragment {
    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    int i = 0, sum_osa = 0;
    private static final String URL = "http://www.mehndiqueens.com/indianbeauty/api/mycookingtalent/get";
    private ListView listView, listViewproducts;
    public ItemfavitemAdapter1 adapter1;
    String customer_id, comp_id,cmts;
    String jsonData, userId;
    int status;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> favouriteItem1 = new ArrayList<HashMap<String, String>>();
    String color;
    String statusval;
    Button all_regulatory, all_products;
    String aging1 = null, aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name, tv_code, tv_total_osa_amt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_complaint_prod, container, false);
        listView = (ListView) view.findViewById(R.id.listViewproducts);
        listView.setAdapter(adapter1);
        adapter1 = new ItemfavitemAdapter1(getActivity(), favouriteItem);
        //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
        listView.setAdapter(adapter1);
        db = new DatabaseHandler(getActivity());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detail = new Intent(getActivity(),AllproductsActivity.class);
                TextView comts = (TextView)view.findViewById(R.id.tv_name);
                TextView tv_place = (TextView) view.findViewById(R.id.tv_place);
                TextView tv_comptype = (TextView) view.findViewById(R.id.tv_complainttype);
                TextView tv_billno = (TextView) view.findViewById(R.id.tv_billno);
                TextView tv_retailer = (TextView) view.findViewById(R.id.tv_retailername);
                TextView tv_status_value = (TextView)view.findViewById(R.id.statusval);
                //ImageView status_icon = (ImageView) view.findViewById(R.id.status_icon);

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
                Toast.makeText (getActivity(),"status "+statusval ,Toast.LENGTH_SHORT).show();
                startActivity(detail);
            }
        });





        String selectQuery  = " SELECT DISTINCT " +KEY_TABLE_COMPLAINTS_ID + ","+ KEY_TABLE_COMPLAINTS_FARMER_NAME + ","+KEY_TABLE_COMPLAINTS_COMMENTS+","+ KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE +","+KEY_TABLE_COMPLAINTS_BILL_NUMBER+
                ","+KEY_TABLE_COMPLAINTS_STATUS+","+KEY_TABLE_COMPLAINTS_RETAILER_NAME+","+KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES+" FROM " + TABLE_COMPLAINT + " GROUP BY " + KEY_TABLE_COMPLAINTS_ID +" ORDER BY "+KEY_TABLE_COMPLAINTS_ID+ " DESC";
        Log.e("selectQuery",selectQuery);
        sdbw = db.getWritableDatabase();



        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                //int osa           = Integer.parseInt(cursor.getString(2));
               // sum_osa = sum_osa+osa;
             //   Log.e("Values",  cursor.getString(0)+" : "+cursor.getString(1)+" : OSA"+String.valueOf(osa) +" : customer id"+cursor.getString(3));
                HashMap<String, String> map = new HashMap<String, String>();
                // adding each child node to HashMap key => value
                map.put("sr", cursor.getString(0));
                map.put("name",  cursor.getString(1));
                map.put("area",  cursor.getString(7));
                map.put("type",  cursor.getString(3));
                map.put("billno",  cursor.getString(4));
                map.put("retailer",  cursor.getString(6));
                map.put("comments", cursor.getString(2));
                map.put("status", cursor.getString(5));

                String type = cursor.getString(3);
                if(type.equalsIgnoreCase("product")){
                    favouriteItem.add(map);
                }

                statusval = cursor.getString(5);
                Log.e("status value",statusval);
                String orders = String.valueOf(favouriteItem.size());
                Log.e("size products",orders);
            } while (cursor.moveToNext());

        }

        else Log.d("LOG", "returned null!");

        adapter1.updateResults(favouriteItem);

       // db.deleteComplaints1();

        return view;
    }


    class ItemfavitemAdapter1 extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter1(Context context, ArrayList<HashMap<String, String>> results) {
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
            holder.tv_name.setText("Name : "                + results.get(position).get("retailer"));
            holder.tv_place.setText("Complaint Area : "     + results.get(position).get("area"));
            holder.tv_comptype.setText(results.get(position).get("comments"));
            holder.tv_billno.setText("Bill No : " + results.get(position).get("bill"));
            holder.tv_retailer.setText(results.get(position).get("name"));
            holder.tv_status_value.setText(results.get(position).get("status"));
            cmts =results.get(position).get("comments");
            int len= cmts.length();

           status = Integer.parseInt(results.get(position).get("status"));
            Log.e("status is " , String.valueOf(status));
            if(status==1){
                holder.status_icon.setImageResource(R.drawable.icon4);
            }
            else if(status==2){
               holder.status_icon.setImageResource(R.drawable.icon5);
            }
            else if(status==3){
                holder.status_icon.setImageResource(R.drawable.icon6);
            }
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
