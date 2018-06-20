package com.nsl.app;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.app.feedback.Feedback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSchemes extends Fragment {
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String HOME_URL = "http://www.mehndiqueens.com/indianbeauty/api/user/images";
    ViewPager mViewPager;
    ProgressDialog progressDialog;
    DatabaseHandler db = new DatabaseHandler(getActivity());

    private ListView listView;
    private ItemfavitemAdapter adapter;
    String customer_id, comp_id;
    String jsonData, userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    //private SliderLayout imageSlider;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_schemes, container, false);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
                .coordinatorLayout);

        listView = (ListView) view.findViewById(R.id.listView);
        final ArrayList<HashMap<String, String>> feedList = new ArrayList<HashMap<String, String>>();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*TextView divtext=(TextView)findViewById(R.id.tv_div);
                String div_value;
                div_value= divtext.getText().toString();*/

            }
        });
        adapter = new ItemfavitemAdapter(getActivity(), favouriteItem);
        listView.setAdapter(adapter);
        new Async_getalloffline().execute();

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


            try {
                List<Feedback> feedback = db.getAllFeedbacks(userId);

                for (Feedback fb : feedback) {
                    String log = "Id: " + fb.getID() + " ,Name: " + fb.getFarmerName() + " ,fb place " + fb.getplace() + " ,div sapid: " + fb.getfeedbackmessage();
                    // Writing Contacts to log
                    Log.e("DIVISIONS: ", log);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("sr", String.valueOf(fb.getID()));
                    map.put("name", fb.getFarmerName());
                    map.put("place", fb.getplace());
                    map.put("feedback", fb.getfeedbackmessage());

                    favouriteItem.add(map);

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
            // tv_total_osa_amt.setText(String.valueOf(sum_osa));
        }
    }


   /* public class GetFavourites extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {
            favouriteItem.clear();
            try {
                List<Feedback> feedback = db.getAllFeedbacks();

                for (Feedback fb : feedback) {
                    String log = "Id: " + fb.getID() + " ,Name: " + fb.getFarmerName() + " ,fb place " + fb.getplace() + " ,div sapid: " + fb.getfeedbackmessage();
                    // Writing Contacts to log
                    Log.e("DIVISIONS: ", log);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("sr", String.valueOf(fb.getID()));
                    map.put("name", fb.getFarmerName());
                    map.put("place", fb.getplace());
                    map.put("feedback", fb.getfeedbackmessage());

                    favouriteItem.add(map);

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

        }
    }*/

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
            ItemfavitemAdapter.ViewHolder holder = new ItemfavitemAdapter.ViewHolder();
            if (convertView == null) {


                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_feedbacks, parent, false);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
                holder.tv_feedback = (TextView) convertView.findViewById(R.id.tv_feedback);
                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            } else {
                holder = (ItemfavitemAdapter.ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.tv_name.setText("Name : " + results.get(position).get("name"));
            holder.tv_place.setText("Place : " + results.get(position).get("place"));
            holder.tv_feedback.setText("Feedback : " + results.get(position).get("feedback"));


            return convertView;
        }


        public class ViewHolder {
            public TextView tv_name, tv_place, tv_feedback;
            public ImageView img;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }


    }
}
