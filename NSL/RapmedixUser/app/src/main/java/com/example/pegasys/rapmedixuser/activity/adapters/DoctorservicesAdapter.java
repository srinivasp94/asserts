package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.DocspecList;
import com.example.pegasys.rapmedixuser.activity.pojo.DoctorSelectedService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pegasys on 1/24/2018.
 */

public class DoctorservicesAdapter extends BaseAdapter {

    private Context context;
    ArrayList<DoctorSelectedService> serviceArrayList;
    LayoutInflater inflater;

    public DoctorservicesAdapter(Context context, ArrayList<DoctorSelectedService> serviceArrayList) {
        this.context = context;
        this.serviceArrayList = serviceArrayList;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return serviceArrayList.size();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.docspeclist, null);
            holder = new Holder();
            holder.title = convertView.findViewById(R.id.tv_docspectitle);
            holder.icon = convertView.findViewById(R.id.dsl_icon);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        DoctorSelectedService mdoctorSelectedService = serviceArrayList.get(i);
        holder.title.setText(mdoctorSelectedService.serviceName);
        Picasso.with(context).load("https://www.rapmedix.com/uploads/hospitalservice_image/" + mdoctorSelectedService.serviceImage).error(R.drawable.doctor_black).into(holder.icon);
//        holder.icon.setImageBitmap(docspecList.specialisationImage);

        return convertView;
    }


    private class Holder {
        TextView title;
        ImageView icon;
    }
}
