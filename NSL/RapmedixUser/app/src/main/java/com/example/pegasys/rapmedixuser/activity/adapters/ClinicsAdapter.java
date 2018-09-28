package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.Doctorworkingdatail;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/13/2017.
 */

public class ClinicsAdapter extends RecyclerView.Adapter<ClinicsAdapter.ViewHolder> {
    Context context;
    ArrayList<Doctorworkingdatail> list = new ArrayList<>();

    public ClinicsAdapter(Context context, ArrayList<Doctorworkingdatail> list) {
        this.context = context;
        this.list = list;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_desc_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Doctorworkingdatail doctorworkingdatail = list.get(position);
        holder.doctorAddress.setText(doctorworkingdatail.address);
        holder.doctorName.setText(doctorworkingdatail.hospitalName);
        holder.doctorPrice.setText(doctorworkingdatail.fee);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView doctorAddress;
        Button doctorBook, doctorDirection;
        TextView doctorEmail;
        TextView doctorMobile;
        TextView doctorName;
        TextView doctorPrice;
        TextView doctorTime;
        ImageView Navigation;

        public ViewHolder(View itemView) {
            super(itemView);

            doctorName = (TextView) itemView.findViewById(R.id.doctor_row_name);
            doctorAddress = (TextView) itemView.findViewById(R.id.doctor_row_desc);
            doctorMobile = (TextView) itemView.findViewById(R.id.mobile);
            doctorEmail = (TextView) itemView.findViewById(R.id.email);
            doctorTime = (TextView) itemView.findViewById(R.id.time);
            doctorPrice = (TextView) itemView.findViewById(R.id.doctor_row_price);
            doctorBook = (Button) itemView.findViewById(R.id.doctor_row_book);
            doctorDirection = (Button) itemView.findViewById(R.id.doctor_row_direction);
            Navigation = (ImageView) itemView.findViewById(R.id.iv_navifation);
            doctorBook.setOnClickListener(this);
            Navigation.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }
}
