package com.nsl.app;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomGridtAdapterTenImg extends BaseAdapter {
    private final int length = 9;
    AlertDialog dialog;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    Context context;
    ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();

    public CustomGridtAdapterTenImg(Context context, ArrayList<HashMap<String, String>> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_grid_row, parent, false);

            holder.pic = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       // Log.d("TAG", "Image: " + results.get(position).get("Catimage"));
       // Glide.with(context).load(results.get(position).get("hallImage")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);


        return convertView;
    }

    private static class ViewHolder {

        public ImageView pic;
    }

    public void updateResults(ArrayList<HashMap<String, String>> results) {
        this.results = results;
        notifyDataSetChanged();
    }
}