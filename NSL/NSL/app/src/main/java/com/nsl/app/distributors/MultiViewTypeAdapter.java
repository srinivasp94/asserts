package com.nsl.app.distributors;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nsl.app.R;

import java.util.ArrayList;

public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Model> dataSet;
    Context mContext;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class RMTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView txtcustomer,txtcode,txtcompany;


        public RMTypeViewHolder(View itemView) {
            super(itemView);

            this.txtcustomer = (TextView) itemView.findViewById(R.id.tv_customer);
            this.txtcode = (TextView) itemView.findViewById(R.id.tv_customer_code);
            this.txtcompany = (TextView) itemView.findViewById(R.id.tv_osa);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    public class MOTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView tv_mo_name,tv_mo_id;
        LinearLayout heading;


        public MOTypeViewHolder(View itemView) {
            super(itemView);

            this.tv_mo_name = (TextView) itemView.findViewById(R.id.tv_mo_name);
            this.tv_mo_id = (TextView) itemView.findViewById(R.id.tv_mo_id);
            this.heading = (LinearLayout)itemView.findViewById(R.id.lay_heading);


        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }



    public MultiViewTypeAdapter(ArrayList<Model> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.RM_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customers, parent, false);
                return new RMTypeViewHolder(view);
            case Model.MO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customers1, parent, false);
                return new MOTypeViewHolder(view);

        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return Model.RM_TYPE;
            case 1:
                return Model.MO_TYPE;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Model object = dataSet.get(listPosition);

        if (object != null) {
            switch (object.type) {
                case Model.RM_TYPE:
                    ((RMTypeViewHolder) holder).txtcustomer.setText(object.customer_name);
                    ((RMTypeViewHolder) holder).txtcode.setText(object.customer_code);
                    ((RMTypeViewHolder) holder).txtcompany.setText(object.company_code);

                    break;
                case Model.MO_TYPE:
                    if(listPosition==0){
                        ((MOTypeViewHolder) holder).heading.setVisibility(View.GONE);

                    }
                    else{
                        ((MOTypeViewHolder) holder).heading.setVisibility(View.VISIBLE);

                        ((MOTypeViewHolder) holder).tv_mo_name.setText(object.mo_name);
                        ((MOTypeViewHolder) holder).tv_mo_id.setText(object.sap_id);
                    }


                    break;

            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}