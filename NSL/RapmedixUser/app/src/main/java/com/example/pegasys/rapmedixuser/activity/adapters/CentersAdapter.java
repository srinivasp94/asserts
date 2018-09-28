package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.Diagnosticscenter;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pegasys on 2/20/2018.
 */

public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Diagnosticscenter> diagnosticscentersList;
    private int price;

    public CentersAdapter(Context mContext, ArrayList<Diagnosticscenter> diagnosticscentersList) {
        this.mContext = mContext;
        this.diagnosticscentersList = diagnosticscentersList;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.centers_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Diagnosticscenter diagnosticscenter = diagnosticscentersList.get(i);

        viewHolder.txt_name.setText(diagnosticscenter.name);
        if (diagnosticscenter.profilePic != null && !diagnosticscenter.profilePic.equals("")) {
            Picasso.with(mContext).load(Constants.BASE_URL+ "/uploads/hospital_image/"+ diagnosticscenter.profilePic).into(viewHolder.imageView);
        }
        viewHolder.hospital.setText("" + diagnosticscenter.geolocation);
        double dist = diagnosticscenter.distance;
        price = (int)dist;
        viewHolder.distance.setText("" + price);
        viewHolder.txt_price.setText("Rs." + diagnosticscenter.bprice);

    }

    @Override
    public int getItemCount() {
        return diagnosticscentersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_name, specialization, hospital, distance, txt_price, txt_service;
        ImageView imageView;
        String doctorId;
//        LinearLayout llitem;
        CardView llitem;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.doctor_name);
            specialization = itemView.findViewById(R.id.specialization);
            hospital = itemView.findViewById(R.id.hospital);
            distance = itemView.findViewById(R.id.distance);
            txt_price = itemView.findViewById(R.id.tv_price);
            txt_service = itemView.findViewById(R.id.tv_centernames);
            llitem = itemView.findViewById(R.id.ll_item);
            imageView = itemView.findViewById(R.id.doctor_image);

            llitem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }
}
