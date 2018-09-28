package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.PrescriptList;
import com.example.pegasys.rapmedixuser.activity.pojo.Typos;

import java.util.ArrayList;

/**
 * Created by BhargavaSunkara on 27-Dec-17.
 */

public class HealthRecordAdapter extends RecyclerView.Adapter<HealthRecordAdapter.ViewHolder> {
    Context context;
    ArrayList<Typos> healthRecordList = new ArrayList<>();

    public HealthRecordAdapter(Context context, ArrayList<Typos> healthRecordList) {
        this.context = context;
        this.healthRecordList = healthRecordList;
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
                .inflate(R.layout.healthrecord_list_item, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(HealthRecordAdapter.ViewHolder holder, int position) {
        Typos list = healthRecordList.get(position);
        holder.record_title.setText(list.title);
        holder.doc_name.setText(list.docName);
        holder.date.setText(list.createdDate);
        holder.time.setText(list.time);

    }

    @Override
    public int getItemCount() {
        return healthRecordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView user_name, record_title, doc_name, specialization, date, time;
        private RelativeLayout layout_healthrecords;

        public ViewHolder(View itemView) {
            super(itemView);
            record_title = itemView.findViewById(R.id.record_title);
            doc_name = itemView.findViewById(R.id.doctor_name);
            date = itemView.findViewById(R.id.date_hr);
            time = itemView.findViewById(R.id.time_hr);
            layout_healthrecords= itemView.findViewById(R.id.rl_healthrecords);
            layout_healthrecords.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }
}
