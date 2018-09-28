package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.Doctorworkingdatail;
import com.example.pegasys.rapmedixuser.activity.pojo.PrescriptList;

import java.util.ArrayList;

/**
 * Created by BhargavaSunkara on 27-Dec-17.
 */

public class PrescriptAdapter extends RecyclerView.Adapter<PrescriptAdapter.ViewHolder> {
    Context context;
    ArrayList<PrescriptList> prescriptLists = new ArrayList<>();

    public PrescriptAdapter(Context context, ArrayList<PrescriptList> prescriptLists) {
        this.context = context;
        this.prescriptLists = prescriptLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.healthrecord_list_item, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(PrescriptAdapter.ViewHolder holder, int position) {
        PrescriptList list = prescriptLists.get(position);
        holder.record_title.setText(list.getTitle());
        holder.doc_name.setText(list.getDoc_type());
        holder.date.setText(list.getCreatedat());
    }

    @Override
    public int getItemCount() {
        return prescriptLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, record_title, doc_name, specialization, date, time;

        public ViewHolder(View itemView) {
            super(itemView);
            record_title = (TextView) itemView.findViewById(R.id.record_title);
            doc_name = (TextView) itemView.findViewById(R.id.doctor_name);
            date = (TextView) itemView.findViewById(R.id.date_hr);
            time = (TextView) itemView.findViewById(R.id.time_hr);
        }
    }
}
