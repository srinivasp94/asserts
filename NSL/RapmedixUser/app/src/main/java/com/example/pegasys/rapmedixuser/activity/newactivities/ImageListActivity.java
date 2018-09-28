package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.imageloadingutils.ImageLoader;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {
    private List<String> imgurls = new ArrayList<>();
    private GridView imgslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView backButton = toolbar.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        imgslist = findViewById(R.id.image_list);
        Intent inten = getIntent();
        if (inten != null) {
            String s = inten.getStringExtra("Imageurls");
            imgurls = Arrays.asList(s.split(","));
        }

        imageAdapter imageAdapter = new imageAdapter(ImageListActivity.this, imgurls);
        imgslist.setAdapter(imageAdapter);
        imgslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent(ImageListActivity.this,ImageViewActivity.class);
                intent.putExtra("Img",imgurls.get(i));
                startActivity(intent);
            }
        });
    }

    public class imageAdapter extends BaseAdapter {
        private Context context;
        private List<String> imgList = new ArrayList<>();
        LayoutInflater layoutInflater;


        public imageAdapter(Context context, List<String> imgList) {
            this.context = context;
            this.imgList = imgList;
            layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imgList.size();
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
//            if (view == null) {
            view = layoutInflater.inflate(R.layout.activity_switch_image_example, viewGroup, false);
//            TouchImageView img = (TouchImageView) view.findViewById(R.id.img);
            ImageView imageView = view.findViewById(R.id.img);
/*
            int loader = R.drawable.no_image;
            String image_url = Constants.BASE_URL
                    + "/uploads/health_records/" +
                    imgList.get(i);

            ImageLoader imgLoader = new ImageLoader(getApplicationContext());

            // whenever you want to load an image from url
            // call DisplayImage function
            // url - image url to load
            // loader - loader image, will be displayed before getting image
            // image - ImageView
            imgLoader.DisplayImage(image_url, loader, imageView);*/



               Picasso.with(context).load(Constants.BASE_URL
                       + "/uploads/health_records/"+
               imgList.get(i)).error(R.drawable.no_image).into(imageView);

//            }
            return view;
        }
    }

}
