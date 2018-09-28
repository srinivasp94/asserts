package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.LabList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rk on 03-Jan-18.
 */

public class LabsAdapter extends RecyclerView.Adapter<LabsAdapter.ViewHolder> {
    Context context;
    ArrayList<LabList> mlabList = new ArrayList<>();

    public LabsAdapter(Context context, ArrayList<LabList> mlabList) {
        this.context = context;
        this.mlabList = mlabList;
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
                .inflate(R.layout.labsitem, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LabsAdapter.ViewHolder holder, int position) {
        LabList labList = mlabList.get(position);
        String img = labList.profilePic;
        if (img.equals("")) {
            holder.imageView.setImageResource(R.drawable.rap_banner);
        } else {
            Picasso.with(context).load("https://www.rapmedix.com/uploads/hospital_image/" + labList.profilePic).into(holder.imageView);
        }


        holder.textView.setText("Dr." + labList.hospitalName);
//        holder.textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        holder.specialization.setText(labList.categoryNames);

        holder.hospital.setText(labList.hospitalName);
        holder.doctorId = labList.id;
        holder.distance.setText(String.valueOf(Math.round(Double.parseDouble(labList.distance))) + " km");


    }

    @Override
    public int getItemCount() {
        return mlabList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView, specialization, exp, degree, hospital, distance;
        ImageView imageView;
        LinearLayout call_button, diagnostics_labs;
        String doctorId;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.doctor_name);
            imageView = itemView.findViewById(R.id.doctor_image);
            call_button = itemView.findViewById(R.id.call_button);
            diagnostics_labs = itemView.findViewById(R.id.linearlayout_diagnostics_labs);

            specialization = itemView.findViewById(R.id.specialization);
            exp = itemView.findViewById(R.id.exp);
            degree = itemView.findViewById(R.id.degree);
            hospital = itemView.findViewById(R.id.hospital);
            distance = itemView.findViewById(R.id.distance);

            diagnostics_labs.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                if (mListener != null) {
                    mListener.onItemClick(view, getPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
