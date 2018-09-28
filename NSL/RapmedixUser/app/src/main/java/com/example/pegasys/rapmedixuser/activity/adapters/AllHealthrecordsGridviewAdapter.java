package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.healthrecords.AllHealthRecords;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pegasys on 2/8/2018.
 */

public class AllHealthrecordsGridviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AllHealthRecords> mAllHealthRecordses;
    private LayoutInflater layoutInflater;

    public AllHealthrecordsGridviewAdapter(Context context, ArrayList<AllHealthRecords> mAllHealthRecordses) {
        this.context = context;
        this.mAllHealthRecordses = mAllHealthRecordses;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mAllHealthRecordses.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.cardfor_healthrecords, viewGroup, false);
            holder = new Holder();
            holder.txt_record_name = view.findViewById(R.id.tv_healthecord_name);
            holder.txt_recordAge = view.findViewById(R.id.tv_healthecord_age);
            holder.iv_recordProfile = view.findViewById(R.id.iv_healthecord_proile);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        AllHealthRecords healthRecords = mAllHealthRecordses.get(position);
        Log.i("classname", healthRecords.toString());
        if (healthRecords.getProfile_pic() == null || healthRecords.getProfile_pic().equals("")) {
            holder.iv_recordProfile.setImageResource(R.drawable.doctor_icon);
        } else {
            Picasso.with(context).load(Constants.BASE_URL+ "/uploads/user_profile_images/"+healthRecords.getProfile_pic()).into(holder.iv_recordProfile);
        }
        holder.txt_record_name.setText(mAllHealthRecordses.get(position).getName());
        Log.i("name", healthRecords.getName());

        if (healthRecords.getStatus() == null) {
            holder.txt_recordAge.setText("");
        } else {
            String statusValue = healthRecords.getStatus();
            Log.i("statusValue", statusValue);
            if (healthRecords.getStatus().equals("1")) {
                holder.txt_recordAge.setText("Below 18");
            } else {
                holder.txt_recordAge.setText("18+");
            }
        }


        return view;
    }

    private class Holder {
        TextView txt_record_name, txt_recordAge;
        ImageView iv_recordProfile;
    }
}
