package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.pojo.CategoriesList;
import com.example.pegasys.rapmedixuser.activity.pojo.DocspecList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by pegasys on 1/2/2018.
 */

public class DiagnosticsCategoriesAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoriesList> categoriesLists = new ArrayList<>();
    LayoutInflater inflater;

    public DiagnosticsCategoriesAdapter(Context context, ArrayList<CategoriesList> categoriesLists) {
        this.context = context;
        this.categoriesLists = categoriesLists;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return categoriesLists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
     Holder holder = null;
        if (view== null) {
            view= inflater.inflate(R.layout.docspeclist, null);
            holder = new Holder();
            holder.title = (TextView) view.findViewById(R.id.tv_docspectitle);
            holder.icon = (ImageView) view.findViewById(R.id.dsl_icon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        CategoriesList categoriesList= categoriesLists.get(i);
        holder.title.setText(categoriesList.diagnosticsName);
        Picasso.with(context).load("https://www.rapmedix.com/uploads/specialisation_image/"+categoriesList.diagnosticsImage).into(holder.icon);
//        holder.icon.setImageBitmap(categoriesList.specialisationImage);

        return view;
    }


    private class Holder {
        TextView title;
        ImageView icon;
    }
}
