package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.customviews.TouchImageView;
import com.example.pegasys.rapmedixuser.activity.imageloadingutils.ImageLoader;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ImageViewActivity extends AppCompatActivity {
    String s_img;
    private ProgressDialog dialog;
    private ImageView iDownload;
    private String image_url;
    private String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent imageView_intent = getIntent();
        if (imageView_intent != null) {
            s_img = imageView_intent.getStringExtra("Img");
        }
        dialog = new ProgressDialog(this);
        dialog.show();


        ImageView backButton = toolbar.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView pic = (TouchImageView) findViewById(R.id.imageview);

        iDownload = findViewById(R.id.img_downloads);
        iDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new imageDownload().execute();
            }
        });


        int loader = R.drawable.no_image;
        image_url = Constants.BASE_URL
                + "/uploads/health_records/" +
                s_img;

        Uri uri = Uri.parse(image_url);

//        pic.setImageURI(uri);
        ImageLoader imgLoader = new ImageLoader(this);

        // whenever you want to load an image from url
        // call DisplayImage function
        // url - image url to load
        // loader - loader image, will be displayed before getting image
        // image - ImageView
        imgLoader.DisplayImage(image_url, loader, pic);
        dialog.dismiss();


    }

    private class imageDownload extends AsyncTask<String, String, String> {
        final ProgressDialog dialog = new ProgressDialog(ImageViewActivity.this);

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(image_url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                File rootfile = Environment.getExternalStorageDirectory().getAbsoluteFile();
                String fileName = "Downloadimagefile.png";

                Log.i("Local filename:", "" + fileName);
                File file = new File(rootfile, fileName);
                if (file.createNewFile()) {
                    file.createNewFile();
                }

                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();
                int totalSize = urlConnection.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
                }
                fileOutput.close();
                if (downloadedSize == totalSize) filepath = file.getPath();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                filepath = null;
                e.printStackTrace();
            }
            Log.i("filepath: ", "" + filepath);
            return filepath;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
            dialog.setCancelable(false);
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            super.onPostExecute(s);
        }
    }
}
