package com.nsl.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsl.app.R;
import com.nsl.app.pojo.BankDetailsListPojo;
import com.nsl.app.pojo.SchemeDetailsListPojo;

import java.util.List;

/**
 * Created by Jithu on 1/12/2016.
 */
public class SchemeDetailsListAdapter extends RecyclerView.Adapter<SchemeDetailsListAdapter.ViewHolder> {
    Context context;
    List<SchemeDetailsListPojo>  bankDetailsListPojo;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    public SchemeDetailsListAdapter(Context context, List<SchemeDetailsListPojo>  bankDetailsListPojo) {
        this.context = context;
        this.bankDetailsListPojo = bankDetailsListPojo;
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scheme_products, parent, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.tvProductname.setText(bankDetailsListPojo.get(position).productName);
        holder.tvPrice.setText(bankDetailsListPojo.get(position).price);
        holder.tvRegion.setText(bankDetailsListPojo.get(position).region);
        holder.tvValidfrom.setText(bankDetailsListPojo.get(position).validfrom);
        holder.tvValidto.setText(bankDetailsListPojo.get(position).validto);



    }

    @Override
    public long getItemId(int i) {
        return bankDetailsListPojo.size();
    }

    @Override
    public int getItemCount() {
        return bankDetailsListPojo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvProductname;
        public TextView tvPrice;
        public TextView tvRegion;
        public TextView tvValidfrom;
        public TextView tvValidto;



        public ViewHolder(final View rowView) {
            super(rowView);


            tvProductname = (TextView) itemView.findViewById(R.id.tv_productname);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_productprice);
            tvRegion = (TextView) itemView.findViewById(R.id.tv_productregion);
            tvValidfrom = (TextView) itemView.findViewById(R.id.tv_productfrom);
            tvValidto = (TextView) itemView.findViewById(R.id.tv_productto);


            //retailer.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

}
