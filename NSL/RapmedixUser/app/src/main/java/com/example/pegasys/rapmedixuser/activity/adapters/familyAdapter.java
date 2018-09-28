package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    LinearLayout ll_mobile;

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
//        if (data.get(position).Mobile.equals("0"))
//        {
//            vh.mobilee_root.setVisibility(View.GONE);
//
//        }else {
//            vh.mobilee_root.setVisibility(View.VISIBLE);
//            vh.mobile.setText(data.get(position).Mobile);
//        }
        if (mFamilydatum.mobile.equals("0")) {
            ll_mobile.setVisibility(View.GONE);
        } else {
            ll_mobile.setVisibility(View.VISIBLE);
            holder.mobile.setText(mFamilydatum.mobile);
        }

        holder.relation.setText(mFamilydatum.relationShip);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, relation, mobile,age;
        CardView cardView;
        RelativeLayout relativeLayout;
        ImageView imageView_delete;


        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            relation = itemView.findViewById(R.id.relation);
            mobile = itemView.findViewById(R.id.mobile);
            cardView = itemView.findViewById(R.id.cv_root);
            relativeLayout = itemView.findViewById(R.id.layout_member);
            ll_mobile = itemView.findViewById(R.id.mobilee_root);
            imageView_delete = itemView.findViewById(R.id.iv_delete);
//            cardView.setOnClickListener(this);
            imageView_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }
}
