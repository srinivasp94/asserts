package com.nsl.app;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPaymentCollections extends Fragment {
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

        View view = inflater.inflate(R.layout.fragment_paymentcollections, container, false);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
                .coordinatorLayout);



        return view;
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
