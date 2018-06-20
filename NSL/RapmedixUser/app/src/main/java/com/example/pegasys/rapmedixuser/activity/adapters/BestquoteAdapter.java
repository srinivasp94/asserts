package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.bestquoteList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/18/2017.
 */

public class BestquoteAdapter extends BaseAdapter {

    private Context context;
    ArrayList<bestquoteList> list;
    LayoutInflater inflater;

    public BestquoteAdapter(Context context, ArrayList<bestquoteList> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list;
    }

    @Override
    public long getItemId(int i) {
        return list.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.docspeclist, null);
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_docspectitle);
            holder.icon = (ImageView) convertView.findViewById(R.id.dsl_icon);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        bestquoteList Visitlist = list.get(position);
        holder.title.setText(Visitlist.bestquoteName);
        Picasso.with(context).load("https://www.rapmedix.com/uploads/bestquote_image/" + Visitlist.bestquoteImage).into(holder.icon);
        return convertView;
    }


    private class Holder {
        TextView title;
        ImageView icon;
    }
}
