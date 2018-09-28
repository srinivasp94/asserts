package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.specDoclist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pegasys on 12/11/2017.
 */

public class DoctorlistAdapter extends RecyclerView.Adapter<DoctorlistAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<specDoclist> list;

    public DoctorlistAdapter(Context mContext, ArrayList<specDoclist> list) {
        this.mContext = mContext;
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
                .inflate(R.layout.doctor_list_item_2, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        specDoclist doclist = list.get(position);

/*
        Picasso.with(mContext)
                .load(doclist.profilePic)
                .error(R.drawable.doctor_icon)
                .into(holder.imageView);
*/
        String img = doclist.profilePic;
        if (img.equals("")) {
            holder.imageView.setImageResource(R.drawable.rap_banner);
        } else {
            Picasso.with(mContext).load(doclist.profilePic).into(holder.imageView);
        }


        holder.textView.setText("Dr." + doclist.name);
//        holder.textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        holder.specialization.setText(doclist.specialisationName.toString().split(",")[0]);
        holder.exp.setText("( " + doclist.experience + "years Experience)");
        holder.degree.setText(doclist.degreeName.toString().split(",")[0]);
        holder.hospital.setText(doclist.hospitalName);
        holder.doctorId = doclist.id;

        holder.distance.setText(String.valueOf(Math.round(Double.parseDouble(doclist.distance))) + " km");



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView, specialization, exp, degree, hospital, distance;
        ImageView imageView;
        LinearLayout call_button;
        String doctorId;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.doctor_name);
            imageView = itemView.findViewById(R.id.doctor_image);
            call_button = itemView.findViewById(R.id.call_button);

            specialization = itemView.findViewById(R.id.specialization);
            exp = itemView.findViewById(R.id.exp);
            degree = itemView.findViewById(R.id.degree);
            hospital = itemView.findViewById(R.id.hospital);
            distance = itemView.findViewById(R.id.distance);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }

        }
    }
}
