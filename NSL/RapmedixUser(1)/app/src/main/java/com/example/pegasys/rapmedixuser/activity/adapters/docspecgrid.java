package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.DocspecList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/11/2017.
 */

/*public class docspecgrid {
}*/
public class docspecgrid extends BaseAdapter {

    private Context context;
    ArrayList<DocspecList> list;
    LayoutInflater inflater;

    public docspecgrid(Context context, ArrayList<DocspecList> list) {
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
        DocspecList docspecList = list.get(position);
        holder.title.setText(docspecList.specialisationName);
        Picasso.with(context).load(docspecList.specialisationImage).into(holder.icon);
//        holder.icon.setImageBitmap(docspecList.specialisationImage);

        return convertView;
    }


    private class Holder {
        TextView title;
        ImageView icon;
    }
}