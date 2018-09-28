package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.Doctorworkingdatail;
import com.example.pegasys.rapmedixuser.activity.pojo.Familydatum;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/21/2017.
 */

public class familyAdapter extends RecyclerView.Adapter<familyAdapter.ViewHolder> {

    Context context;
    ArrayList<Familydatum> list = new ArrayList<>();

    public familyAdapter(Context context, ArrayList<Familydatum> list) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.family_members_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Familydatum mFamilydatum = list.get(position);
        holder.name.setText(mFamilydatum.name);
        holder.age.setText(mFamilydatum.dateofbirth);
        holder.mobile.setText(mFamilydatum.mobile);
        holder.relation.setText(mFamilydatum.relationShip);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, relation, mobile,age;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);
            relation = (TextView) itemView.findViewById(R.id.relation);
            mobile = (TextView) itemView.findViewById(R.id.mobile);
            cardView = (CardView)itemView.findViewById(R.id.cv_root);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }
}
