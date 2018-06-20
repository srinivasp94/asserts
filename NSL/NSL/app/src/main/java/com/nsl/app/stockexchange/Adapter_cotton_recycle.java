package com.nsl.app.stockexchange;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.nsl.app.R;

/**
 * Created by Jithu on 1/12/2016.
 */
public class Adapter_cotton_recycle extends RecyclerView.Adapter<Adapter_cotton_recycle.ViewHolder> {
    Context context;
    String [] getAlertsPojo;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    public Adapter_cotton_recycle(Context context, String []  objects) {
        this.context = context;
        this.getAlertsPojo = objects;
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_cottton_new, parent, false);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(i);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nameOfDealer.setText(getAlertsPojo[position]);



//        holder.schoolName.setText(Common.getDate(Long.parseLong(getAlertsPojo.get(position).receivedTime),"yyyy-MM-dd 'at' HH:mm:ss a"));
//        holder.routeName.setText(AlertsTitleEnum.getPNTitle(getAlertsPojo.get(position).pnCode).getPnTitle());


    }

    @Override
    public long getItemId(int i) {
        return getAlertsPojo.length ;
    }

    @Override
    public int getItemCount() {
        return getAlertsPojo.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView stockPlaced;
        TextView nameOfDealer;
        TextView currentStock;
        TextView pog;
        Button retailer;
        ImageView search;

        public ViewHolder(final View rowView) {
            super(rowView);

            stockPlaced=(TextView)rowView.findViewById(R.id.TextView_stockplaced);
            currentStock = (TextView) rowView.findViewById(R.id.TextView_current_stock);
            pog = (TextView) rowView.findViewById(R.id.TextView_pog);
            retailer=(Button)rowView.findViewById(R.id.button_r);
            search=(ImageView)rowView.findViewById(R.id.iv_search);
            nameOfDealer = (TextView) rowView.findViewById(R.id.tv_dealer_name_cotton);

            nameOfDealer.setOnClickListener(this);
            stockPlaced.setOnClickListener(this);
            currentStock.setOnClickListener(this);
            pog.setOnClickListener(this);
            retailer.setOnClickListener(this);
            search.setOnClickListener(this);

            rowView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

}
