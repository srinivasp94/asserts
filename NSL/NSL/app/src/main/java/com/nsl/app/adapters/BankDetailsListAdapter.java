package com.nsl.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsl.app.R;
import com.nsl.app.pojo.BankDetailsListPojo;

import java.util.List;

/**
 * Created by Jithu on 1/12/2016.
 */
public class BankDetailsListAdapter extends RecyclerView.Adapter<BankDetailsListAdapter.ViewHolder> {
    Context context;
    List<BankDetailsListPojo>  bankDetailsListPojo;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    public BankDetailsListAdapter(Context context,  List<BankDetailsListPojo>  bankDetailsListPojo) {
        this.context = context;
        this.bankDetailsListPojo = bankDetailsListPojo;
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bank_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.tvBankname.setText(bankDetailsListPojo.get(position).bankName);
        holder.tvAccountno.setText(bankDetailsListPojo.get(position).accountNo);
        holder.tvIfsc.setText(bankDetailsListPojo.get(position).ifscCode);



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
        public TextView tvBankname;
        public TextView tvAccountno;
        public TextView tvIfsc;



        public ViewHolder(final View rowView) {
            super(rowView);


            tvBankname = (TextView) itemView.findViewById(R.id.tv_bankname);
            tvAccountno = (TextView) itemView.findViewById(R.id.tv_accountno);
            tvIfsc = (TextView) itemView.findViewById(R.id.tv_ifsc);


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
