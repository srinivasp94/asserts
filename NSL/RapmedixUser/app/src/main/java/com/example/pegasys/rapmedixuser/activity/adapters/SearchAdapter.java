package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.Searchmodel;

import java.util.ArrayList;

/**
 * Created by pegasys on 2/28/2018.
 */

public class SearchAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Searchmodel> modelList = new ArrayList<>();
    LayoutInflater inflater;

    public SearchAdapter(Context context, ArrayList<Searchmodel> modelList) {
        this.context = context;
        this.modelList = modelList;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.auto_list, null);
            holder = new Holder();
            holder.title = view.findViewById(R.id.text1);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        Searchmodel model = modelList.get(i);

        holder.title.setText(model.getName());
        return view;
    }

   /* @Override
    public Filter getFilter() {
       Filter filter = new Filter() {
           @Override
           protected FilterResults performFiltering(CharSequence charSequence) {
               FilterResults results =new FilterResults();
               if (charSequence != null) {
                   ArrayList<Searchmodel> list =
               }
               return ;
           }

           @Override
           protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

           }
       }
    }*/

    private class Holder {
        TextView title;
    }
}
