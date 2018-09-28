package com.example.pegasys.rapmedixuser.activity.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.newactivities.CustomPhotoGalleryActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * Created by pegasys on 12/26/2017.
 */

public class uploadnewFrag extends Fragment {
    Spinner spinner;
    ImageView remove_image, plus_image, dummy;
    TextView document_title;
    Button upload;
    ViewGroup insertPoint;

    String[] doc_type = {"Select Document Type", "Docotor Prescription", "Lab Reports", "Discharge Summeries", "Medical Bills",};
    private static final int FILE_SELECT_CODE = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "RapMedix_Pictures";
    Uri fileUri;
    ArrayList<String> Recordes_b64 = new ArrayList<>();
    StringBuilder recordsbase64 = new StringBuilder();
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CAMERA = 123;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    int PICK_IMAGE_MULTIPLE = 01;

    public uploadnewFrag() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_new_page, container, false);

        spinner = view.findViewById(R.id.spinner);
        document_title = view.findViewById(R.id.doc_title);
        upload = view.findViewById(R.id.upload);
        plus_image = view.findViewById(R.id.plus_image);
        dummy = view.findViewById(R.id.dummy);
        insertPoint = view.findViewById(R.id.main_layout_image);

        ArrayAdapter dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, doc_type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        plus_image.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                selectImage();
            }
        });

        return view;
    }
    private void selectImage() {
        //Constants.iscamera = true;
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        TextView title = new TextView(getActivity());
        title.setText("Add Photo!");
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setCustomTitle(title);

        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                //checkPermission();
                if (items[item].equals("Take Photo"))

                {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        {

                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        } else
                        {
                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    11);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }


                    }

                    else
                    {

                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);


                        File photo = new
                                File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photo));



                        //imageUri = Uri.fromFile(photo);

                        //startActivityForResult(intent,TAKE_PICTURE);

                        Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                        intents.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);


                        // start the image capture Intent
                        //dummy.setImageBitmap();
                        startActivityForResult(intents, 0);

                    }
                }
                else if (items[item].equals("Choose from Library")) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        {


                        } else
                        {
                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    22);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }else {
                        Intent intent = new Intent(getActivity(), CustomPhotoGalleryActivity.class);
                        startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

}
