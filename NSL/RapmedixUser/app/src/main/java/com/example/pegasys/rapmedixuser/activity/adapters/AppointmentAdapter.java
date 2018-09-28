package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.AppointmentsList;
import com.example.pegasys.rapmedixuser.activity.pojo.Doctorworkingdatail;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/26/2017.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    Context context;
    ArrayList<AppointmentsList> alist = new ArrayList<>();

    public AppointmentAdapter(Context context, ArrayList<AppointmentsList> alist) {
        this.context = context;
        this.alist = alist;
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
                .inflate(R.layout.appointments_list_item, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AppointmentsList appointmentsList = alist.get(position);

        holder.doctor_name.setText(appointmentsList.name);
        holder.specialization.setText(appointmentsList.categoryName);
        holder.hos_name.setText(appointmentsList.hospitalName);
        holder.hos_place.setText(appointmentsList.location);
        holder.date.setText(appointmentsList.appointmentDate);
        holder.time.setText(appointmentsList.appointmentTime);
        if (appointmentsList.status.equals("3")) {
            holder.cancel.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return alist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView doctor_name, specialization, hos_name, hos_place, date, time;
        ImageView cancel;

        public ViewHolder(View itemView) {
            super(itemView);

            doctor_name = itemView.findViewById(R.id.doctor_name);
            specialization = itemView.findViewById(R.id.specialization);
            hos_name = itemView.findViewById(R.id.hos_name);
            hos_place = itemView.findViewById(R.id.hos_place);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            cancel = itemView.findViewById(R.id.cancel_ant);
            cancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }
}
