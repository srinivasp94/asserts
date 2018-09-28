package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.Diagnosticscenter;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.TestReviewsResponse;

import java.util.ArrayList;

/**
 * Created by pegasys on 2/20/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TestReviewsResponse> responseArrayList ;

    public ReviewsAdapter(Context mContext, ArrayList<TestReviewsResponse> responseArrayList) {
        this.mContext = mContext;
        this.responseArrayList = responseArrayList;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.testrreviews_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TestReviewsResponse reviewsResponse = responseArrayList.get(i);
        viewHolder.txt_itemName.setText(reviewsResponse.subserviceName);
        viewHolder.txt_price.setText(reviewsResponse.basicprice);
    }

    @Override
    public int getItemCount() {
        return responseArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_itemName,txt_price;
        LinearLayout btn_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_itemName = itemView.findViewById(R.id.tv_docspectitle);
            txt_price= itemView.findViewById(R.id.btn_add_to_cart);
            btn_delete = itemView.findViewById(R.id.ll_delete);
            btn_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view,getPosition());
            }

        }
    }
}
