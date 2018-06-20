package com.nsl.app.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nsl.app.R;
import com.nsl.app.distributors.Model;
import com.nsl.app.pojo.ViewModalAdvBookingPojo;

import java.util.ArrayList;

public class ABSMultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojos;
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

    public class TypeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView mSubItemName;


        public TypeHeaderViewHolder(View itemView) {
            super(itemView);

           this.mSubItemName = (TextView) itemView
                    .findViewById(R.id.textViewTitle);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    public class TypeDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView itemname,tv_schemedetails,tv_rbs_amount,tv_orderdate,itemcount,tvQunatity,tvRate;
        LinearLayout heading;


        public TypeDataViewHolder(View itemView) {
            super(itemView);
            this.itemname             = (TextView)itemView.findViewById(R.id.tv_product);
            this.tv_schemedetails         = (TextView)itemView.findViewById(R.id.tv_schemedetails);
            this.tv_rbs_amount        = (TextView)itemView.findViewById(R.id.tv_amount);
            this.tv_orderdate         = (TextView) itemView.findViewById(R.id.tv_orderdate);
            this.itemcount            = (TextView) itemView.findViewById(R.id.tv_sr_no);
            this.tvQunatity            = (TextView) itemView.findViewById(R.id.tv_qunatity);
            this.tvRate            = (TextView) itemView.findViewById(R.id.tv_rate);


        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }



    public ABSMultiViewTypeAdapter(ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojos, Context context) {
        this.viewModalAdvBookingPojos = viewModalAdvBookingPojos;
        this.mContext = context;
        total_types = viewModalAdvBookingPojos.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_second, parent, false);
                return new TypeHeaderViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fnf_item, parent, false);
                return new TypeDataViewHolder(view);

        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (viewModalAdvBookingPojos.get(position).type) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        ViewModalAdvBookingPojo object = viewModalAdvBookingPojos.get(listPosition);

        if (object != null) {
            switch (object.type) {
                case 0:
                    ((TypeHeaderViewHolder) holder).mSubItemName.setText(object.orderDate);

                    break;
                case 1:

                        ((TypeDataViewHolder) holder).itemname.setText(object.customerName);
                        ((TypeDataViewHolder) holder).tv_rbs_amount.setText(object.aBS);
                        ((TypeDataViewHolder) holder).tvQunatity.setText(object.quantity);
                        ((TypeDataViewHolder) holder).tvRate.setText(object.rate);
                        ((TypeDataViewHolder) holder).tv_schemedetails.setText(object.slabId);
                        ((TypeDataViewHolder) holder).itemcount.setText(String.valueOf(listPosition));



                    break;

            }
        }

    }

    @Override
    public int getItemCount() {
        return viewModalAdvBookingPojos.size();
    }


}