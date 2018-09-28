package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.PrescriptionModelList;

import java.util.ArrayList;

/**
 * Created by pegasys on 1/2/2018.
 */

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.Viewholder> {
    Context context;
    ArrayList<PrescriptionModelList> prescriptLists = new ArrayList<>();

    public PrescriptionAdapter(Context context, ArrayList<PrescriptionModelList> prescriptLists) {
        this.context = context;
        this.prescriptLists = prescriptLists;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescriptitem, parent, false);

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(PrescriptionAdapter.Viewholder holder, int position) {
        PrescriptionModelList modelList = prescriptLists.get(position);
        holder.pTitle.setText(modelList.title);
        holder.pDoctor.setText(modelList.doctorName);
        holder.pPatient.setText(modelList.patientName);
        holder.pDate.setText(modelList.createdDate);

    }

    @Override
    public int getItemCount() {
        return prescriptLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pTitle, pDoctor, pPatient, pDate, pPreview;

        public Viewholder(View itemView) {
            super(itemView);

            pTitle = itemView.findViewById(R.id.p_title);
            pDoctor = itemView.findViewById(R.id.p_doc_name);
            pPatient = itemView.findViewById(R.id.p_Ptent_name);
            pDate = itemView.findViewById(R.id.p_date);
            pPreview = itemView.findViewById(R.id.tv_view_images);

            pPreview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }

        }
    }

}
