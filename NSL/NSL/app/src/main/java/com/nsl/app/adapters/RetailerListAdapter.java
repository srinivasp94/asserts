package com.nsl.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsl.app.R;
import com.nsl.app.pojo.MappedRetailerPojo;
import com.nsl.app.pojo.RetailersNamePojo;

import java.util.List;

/**
 * Created by Jithu on 1/12/2016.
 */
public class RetailerListAdapter extends RecyclerView.Adapter<RetailerListAdapter.ViewHolder> {
    Context context;
    List<MappedRetailerPojo>  retailersNamePojos;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    public RetailerListAdapter(Context context, List<MappedRetailerPojo>  mappedRetailerPojos) {
        this.context = context;
        this.retailersNamePojos = mappedRetailerPojos;
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_retailer_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.tvDealerNameCotton.setText(retailersNamePojos.get(position).retailerName);
        holder.tvDealerTinNo.setText(retailersNamePojos.get(position).retailerTinNo);
        holder.stockPlaced.setText(retailersNamePojos.get(position).stockPlaced);
        holder.currentStock.setText(retailersNamePojos.get(position).currentStock);
        holder.pog.setText(retailersNamePojos.get(position).pog);
      /*  if (retailersNamePojos.get(position).stockMovementFirstListPojo != null){
           // holder.stockPlaced.setText(retailersNamePojos.get(position).stockMovementFirstListPojo.stockPlaced);
        holder.currentStock.setText(retailersNamePojos.get(position).stockMovementFirstListPojo.currentStock);
        holder.pog.setText(retailersNamePojos.get(position).stockMovementFirstListPojo.pog);
    }*/
       // holder.tvQuantity.setText(getStockPlacementPopupPojo.get(position).stockPlaced);
       // holder.tvDate.setText(AlertsTitleEnum.getPNTitle(getStockPlacementPopupPojo.get(position).pnCode).getPnTitle());
      //  holder.image1.setBackgroundResource(AlertsTitleEnum.getPNTitle(getStockPlacementPopupPojo.get(position).pnCode).getDrawableSelected());


    }

    @Override
    public long getItemId(int i) {
        return retailersNamePojos.size();
    }

    @Override
    public int getItemCount() {
        return retailersNamePojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvDealerNameCotton;
        public TextView tvDealerTinNo;
        public TextView stockPlaced;
        TextView currentStock;
        TextView pog;
        Button retailer;
        ImageView search;


        public ViewHolder(final View rowView) {
            super(rowView);


            tvDealerNameCotton = (TextView) itemView.findViewById(R.id.tv_dealer_name_cotton);
            tvDealerTinNo = (TextView) itemView.findViewById(R.id.tv_dealer_tin_no);
            stockPlaced = (TextView) itemView.findViewById(R.id.TextView_stockplaced);
            currentStock = (TextView) itemView.findViewById(R.id.TextView_current_stock);
            pog = (TextView) itemView.findViewById(R.id.TextView_pog);
            retailer = (Button) itemView.findViewById(R.id.button_r);
            search = (ImageView) itemView.findViewById(R.id.iv_search);

            stockPlaced.setOnClickListener(this);
            currentStock.setOnClickListener(this);
            pog.setOnClickListener(this);
            retailer.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

}
