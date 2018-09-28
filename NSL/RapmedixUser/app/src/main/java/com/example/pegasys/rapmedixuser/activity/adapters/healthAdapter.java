package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.checkupList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/14/2017.
 */

public class healthAdapter extends RecyclerView.Adapter<healthAdapter.ViewHolder> {
    Context context;
    ArrayList<checkupList> list = new ArrayList<>();

    public healthAdapter(Context context, ArrayList<checkupList> list) {
        this.context = context;
        this.list = list;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public healthAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_checkup_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(healthAdapter.ViewHolder holder, int position) {
        checkupList checkupList =  list.get(position);
        holder.textView.setText(checkupList.planName);
        holder.description.setText(checkupList.description);
        Picasso.with(context).load("https://www.rapmedix.com/uploads/healthcheckups_categoryimages/"+ checkupList.planImage).into(holder.imagee);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView, description;
        ImageView imagee;
        LinearLayout  root;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            imagee = (ImageView) itemView.findViewById(R.id.imagee);
            root = (LinearLayout)itemView.findViewById(R.id.ll_hc);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }
}
