package com.nsl.app;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;



import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String HOME_URL  = "http://www.mehndiqueens.com/indianbeauty/api/user/images";
    ViewPager mViewPager;


   //private SliderLayout imageSlider;
   SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final PieChart pieChart = (PieChart) view.findViewById(R.id.pie_chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(5f, 0));
        entries.add(new Entry(5f, 1));
        entries.add(new Entry(5f, 2));
        entries.add(new Entry(5f,3));


        PieDataSet dataset = new PieDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Planned");
        labels.add("Completed");
        labels.add("Pendings");
        labels.add("Postponed");


        PieData data = new PieData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        pieChart.setDescription("Daily Sales Report");
        pieChart.setData(data);
        pieChart.setRotationEnabled(false);
        pieChart.animateX(2000);
        pieChart.setCenterText(" D S R ");

        BarChart barchart = (BarChart) view.findViewById(R.id.bar_chart);

        BarData bardata = new BarData(getXAxisValues(), getDataSet());
        barchart.setData(bardata);
        barchart.setDescription("7Days Orders");
        barchart.animateXY(1000, 1000);
        YAxis yAxis = barchart.getAxisLeft();
        yAxis.setAxisMaxValue(100f);
        yAxis.setAxisMinValue(0f);
        barchart.invalidate();


        LineChart lineChart = (LineChart) view.findViewById(R.id.chart);


        ArrayList<Entry> line_entries = new ArrayList<>();
        line_entries.add(new Entry(4f, 0));
        line_entries.add(new Entry(8f, 1));
        line_entries.add(new Entry(6f, 2));
        line_entries.add(new Entry(2f, 3));
        line_entries.add(new Entry(18f, 4));
        line_entries.add(new Entry(9f, 5));

        LineDataSet line_dataset = new LineDataSet(line_entries, "7Days Travelled");

        ArrayList<String> line_labels = new ArrayList<String>();
        line_labels.add("10th Nov");
        line_labels.add("11th Nov");
        line_labels.add("12th Nov");
        line_labels.add("13th Nov");
        line_labels.add("14th Nov");
        line_labels.add("15th Nov");

        LineData line_data = new LineData(line_labels, line_dataset);
        // dataset.setColor(color);
        line_dataset.setCircleColor(R.color.colorPrimaryDark);
        line_dataset.setLineWidth(1f);
        line_dataset.setCircleSize(4f);
        line_dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        //dataset.setDrawCubic(true);
        // dataset.setDrawFilled(true);

        lineChart.setData(line_data);
        lineChart.animateY(1000);
        return view;
    }
    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(35.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(30.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(20.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(25.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(15.000f, 5); // Jun
        valueSet1.add(v1e6);
        BarEntry v1e7 = new BarEntry(10.000f, 6); // Jun
        valueSet1.add(v1e7);

       /* ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);*/

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Orders");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        /*BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);*/

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        //dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("10th Nov");
        xAxis.add("11th Nov");
        xAxis.add("12th Nov");
        xAxis.add("13th Nov");
        xAxis.add("14th Nov");
        xAxis.add("15th Nov");
        xAxis.add("16th Nov");
        return xAxis;
    }
   /* public class AsyncHome extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait");
            dialog.show();
        }

        protected String doInBackground(Void... arg0)
        {   ImagesArray.clear();
            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormEncodingBuilder()
                        .add("categoryId", "12")
                        .build();
                *//*Request request = new Request.Builder()
                        .url(Singup_URL)
                        .build();*//*
                Request request = new Request.Builder()
                        .url(HOME_URL)
                        .post(formBody)
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                    jsonData  = responses.body().string();
                    System.out.println("!!!!!!!1"+jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return jsonData;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            JSONArray array;

            if (jsonData != null && jsonData.startsWith("["))
            {
                try
                {
                    array = new JSONArray(jsonData);
                    //System.out.println("result"+result);
                    if (array != null && array.length() > 0) {

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject obj = array.optJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            // adding each child node to HashMap key => value
                            map.put("Image",           obj.getString("image"));
                            ImagesArray.add(map);
                        }
            }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getActivity(),"No data found",Toast.LENGTH_LONG).show();


            }

            adapter.notifyDataSetChanged();


        }

    }
     class ZoomAdapter extends PagerAdapter {
        LayoutInflater inflator;
        ImageView imgDisplay;
        Context context;
        //  ArrayList<ZoomimageModel> slideShowImages;
        //  ArrayList<String> slideShowImages;
        private ArrayList<HashMap<String, String>> slideShowImages;
        String plotimages;



        public ZoomAdapter(FragmentActivity context, ArrayList<HashMap<String, String>> slideShowImages) {
            this.context = context;
            this.slideShowImages = slideShowImages;

        }
        @Override
        public int getCount() {
            return slideShowImages.size();
        }
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        // @Override
    *//*  public boolean isViewFromObject(View view, Object object) {
          return view == ((ImageView) object);
      }*//*

        //    @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflator.inflate(R.layout.pager_item, container,
                    false);
            imgDisplay = (ImageView) viewLayout.findViewById(R.id.imageView);

            *//* for(int i=0;i<ImagesArray.size();i++)
            NUM_PAGES =ImagesArray.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);*//*
            Glide.with(context).load(slideShowImages.get(position).get("Image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgDisplay);
            //Glide.with(context).load(plotimages).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgDisplay);


            container.addView(viewLayout);
            return viewLayout;
        }



        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // ((ViewPager) container).removeView((ImageView) object);
            (container).removeView((LinearLayout) object);
            // container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }



    }*/

}
