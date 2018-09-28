package com.example.pegasys.rapmedixuser.activity.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.newactivities.CustomPhotoGalleryActivity;
import com.example.pegasys.rapmedixuser.activity.pojo.HealthrecordModel;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by ADMIN on 5/19/2017.
 */

public class HealthRecord_upload_new extends Fragment implements AdapterView.OnItemSelectedListener, RetrofitResponseListener {
    Spinner spinner;
    ImageView remove_image, plus_image, dummy;
    TextView document_title;
    Button upload;
    String UserId = "";
    String x = "", xx = "";
    String[] doc_type = {"Select Document Type", "Docotor Prescription", "Lab Reports", "Discharge Summeries", "Medical Bills",};
    private static final int FILE_SELECT_CODE = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "RapMedix_Pictures";
    Uri fileUri;
    ViewGroup insertPoint;
    Context context;
    ArrayList<String> Recordes_b64 = new ArrayList<>();
    StringBuilder recordsbase64 = new StringBuilder();
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CAMERA = 123;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    int PICK_IMAGE_MULTIPLE = 01;
    private Object obj;
    private Uri file;
    private Bitmap bitmap_Camera;
    private ThumbnailUtils thumbnail;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.upload_new_page, container, false);
        spinner = v.findViewById(R.id.spinner);
        document_title = v.findViewById(R.id.doc_title);
        upload = v.findViewById(R.id.upload);
        plus_image = v.findViewById(R.id.plus_image);
        dummy = v.findViewById(R.id.dummy);
        insertPoint = v.findViewById(R.id.main_layout_image);


        //document_title.setText(getString("doc_title"));

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, doc_type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

//        callCheckPermissionsGroup();


        plus_image.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                selectImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rciords = "";

                /*
                try {

                    for (String serverId : Recordes_b64) {
                        recordsbase64.append(",");
                        recordsbase64.append(serverId);
                    }
                    recordsbase64.deleteCharAt(0);*/
                //recordsbase64.append(Recordes_b64).append("\n");

                HealthrecordModel healthrecordModel = new HealthrecordModel();
                healthrecordModel.userId = new DataBase_Helper(getActivity()).getUserId("1");
                healthrecordModel.docType = " " + spinner.getSelectedItemPosition();
                healthrecordModel.title = document_title.getText().toString();

                for (String serverId : Recordes_b64) {
                    recordsbase64.append(",");
                    recordsbase64.append(serverId);
                }
                recordsbase64.deleteCharAt(0);
                healthrecordModel.document = recordsbase64.toString();

                try {
                    obj = Class.forName(HealthrecordModel.class.getName()).cast(healthrecordModel);
                    Log.d("obj", obj.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                new RetrofitRequester(HealthRecord_upload_new.this).callPostServices(obj, 1, "/user/upload_healthrecords_service", true);

//                    jsonObject.put("document", recordsbase64.toString());


                /*} catch (Exception e) {
                    Log.e("Exception", "" + e.toString());
                }
*/

//                dismiss();

            }

        });

        return v;

    }

    //---------------------call self permissions for storage
    private void callCheckPermissionsGroup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    + Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 22);
            } else {
                Toast.makeText(context, "Already Granted", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "No Permissions Needed", Toast.LENGTH_SHORT).show();
        }
    }
//
//    public void dismiss() {
//        getActivity().finish();
////        Intent i = new Intent(getActivity(), Health_Records.class);  //your class
////        startActivity(i);
//
//    }

    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");

                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_WRITE_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_WRITE_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        /*if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Srinu_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, file);
            startActivityForResult(intentCamera, 100);


        }*/
        if (requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                invitecameraintent();
            } else {

                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            selectImage();
        }
    }


    private void selectImage() {
        //Constants.iscamera = true;
        final CharSequence[] items = { "Choose from Library",
                "Cancel"};

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
                    callCamera();

                  /*  if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        } else {
                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    11);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }


                    } else {

                        *//*Intent intent = new Intent(
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
                        startActivityForResult(intents, 0);*//*
                        *//*captureImage();*//*

                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        file = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Srinu_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, file);
                        startActivityForResult(intentCamera, 100);

                    }*/

                } else if (items[item].equals("Choose from Library")) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


                        } else {
                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    22);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    } else {
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

    private void callCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA)

                    + ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    + ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }


        } else {
            invitecameraintent();
        }

    }
    private void invitecameraintent() {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Srinu_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intentCamera, 100);
    }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                *//*Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();*//*
            } else {
                // failed to capture image
                *//*Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();*//*
            }
        }*/
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                try {
                    bitmap_Camera = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), file);


                    LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View v = vi.inflate(R.layout.upload_item, null);

                    ImageView image = v.findViewById(R.id.dummy);
                    ImageView remove_image = v.findViewById(R.id.remove_button);
                    remove_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View namebar = (View) v.getParent().getParent();
                            insertPoint.removeView(namebar);
                        }
                    });
                    final CardView remove_layout = v.findViewById(R.id.image_uploaded);
                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(150, 150);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(140, 140);

                    v.setLayoutParams(params1);
                    image.setLayoutParams(params);
//                    image.setImageBitmap(thumbnail);


                    image.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap_Camera, bitmap_Camera.getWidth(), bitmap_Camera.getHeight()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("exception", e.toString());
                }
            }

        }

        switch (requestCode) {
            case 1:
                Bitmap bitmap = null;
                if (resultCode == RESULT_OK) {
                    if (data != null) {


                        try {


                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = getActivity().getContentResolver().query(
                                    selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();
                            // Bitmap thumbnail =
                            // (BitmapFactory.decodeFile(picturePath));

                            Bitmap thumbnail = decodeSampledBitmapFromResource(
                                    picturePath, 500, 500);


                            LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            View v = vi.inflate(R.layout.upload_item, null);

// fill in an
                            ImageView image = v.findViewById(R.id.dummy);
                            ImageView remove_image = v.findViewById(R.id.remove_button);
                            remove_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    View namebar = (View) v.getParent().getParent();
                                    insertPoint.removeView(namebar);
                                }
                            });
                            final CardView remove_layout = v.findViewById(R.id.image_uploaded);
                            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(150, 150);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(140, 140);

                            v.setLayoutParams(params1);
                            image.setLayoutParams(params);
                            image.setImageBitmap(thumbnail);

                            insertPoint.addView(v);


                            //Log.e("filae_b64", "inSamplein storage"+getEncoded64ImageStringFromBitmap(thumbnail));

                            Recordes_b64.add(getEncoded64ImageStringFromBitmap(thumbnail));


                            //recordsbase64.append(Recordes_b64).append(",");
                            //recordsbase64.append(getEncoded64ImageStringFromBitmap(thumbnail).toString());
                            //recordsbase64.append("&");

                            Log.e("rcords_size", Recordes_b64.toString() + "" + Recordes_b64.size());
                            //	dummy.setImageBitmap(thumbnail);
                            /*Bitmap thumbnail_r = imageOreintationValidator(
                                    thumbnail, picturePath);*/
                            //dummy.setImageBitmap(thumbnail_r);
                            //dummy

                        } catch (Exception e) {
                            Log.e("hjhgj", "" + Recordes_b64.size());

                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case 0:
                if (resultCode == RESULT_OK) {

                    previewCapturedImage();

                }

                break;
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_MULTIPLE) {
                ArrayList<String> imagesPathList = new ArrayList<String>();
                String[] imagesPath = data.getStringExtra("data").split("\\|");
                try {
                    //	insertPoint.removeAllViews();
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < imagesPath.length; i++) {
                    imagesPathList.add(imagesPath[i]);
                    Bitmap yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);


                    Bitmap thumbnail = decodeSampledBitmapFromResource(
                            imagesPath[i], 500, 500);


                    LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.upload_item, null);

                    final ImageView image = v.findViewById(R.id.dummy);
                    ImageView remove_image = v.findViewById(R.id.remove_button);
                    final CardView remove_layout = v.findViewById(R.id.image_uploaded);


                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(150, 150);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(140, 140);
                    v.setLayoutParams(params1);
                    image.setLayoutParams(params);
                    image.setImageBitmap(thumbnail);


                    insertPoint.addView(v);


                    Log.e("filae_b64", "inSamplein storage" + getEncoded64ImageStringFromBitmap(thumbnail));

                    Recordes_b64.add(getEncoded64ImageStringFromBitmap(thumbnail));
                    remove_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //remove_layout.setVisibility(View.INVISIBLE);
                            //remove.removeView(image);

                            View namebar = (View) v.getParent().getParent();
                            insertPoint.removeView(namebar);

                        }
                    });

                }
            }
        }

    }


    public static Bitmap decodeSampledBitmapFromResource(String pathToFile,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        Log.e("inSampleSize", "inSampleSize______________in storage"
                + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;
    }

    private void previewCapturedImage() {
        try {
            // hide video preview


            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500,
                    false);

            // rotated
            Bitmap thumbnail_r = imageOreintationValidator(resizedBitmap,
                    fileUri.getPath());


            LayoutInflater vi2 = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View v2 = vi2.inflate(R.layout.upload_item, null);
            //dummy.setBackground(null);
            String[] stockArr = Recordes_b64.toArray(new String[Recordes_b64.size()]);
            //Recordes_b64.add(base64Convertions(fileUri.getPath()));
            Recordes_b64.add(base64Convertions(encodeToBase64(thumbnail_r, Bitmap.CompressFormat.PNG, 100)));

            //recordsbase64.append(fileUri.getPath()).append(",");
            Log.e("rcords_size", "" + Recordes_b64.size());


            ImageView image2 = v2.findViewById(R.id.dummy);
            ImageView remove_image = v2.findViewById(R.id.remove_button);
            remove_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View namebar = (View) v.getParent().getParent();
                    insertPoint.removeView(namebar);
                }
            });
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(150, 150);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(140, 140);
            v2.setLayoutParams(params1);
            image2.setLayoutParams(params);
            insertPoint.addView(v2);

            image2.setImageBitmap(thumbnail_r);
            //dummy = true;
            Toast.makeText(getActivity(), "done", Toast.LENGTH_LONG)
                    .show();
        } catch (NullPointerException e) {
            Log.e("mmmmmmmm", "inSamplein storage" + e.toString());
            e.printStackTrace();
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            source.recycle();
            Date d = new Date();
            CharSequence s = DateFormat
                    .format("MM-dd-yy-hh-mm-ss", d.getTime());
            String fullPath = Environment.getExternalStorageDirectory()
                    + "/RYB_pic/" + s.toString() + ".jpg";
            if ((fullPath != null) && (new File(fullPath).exists())) {
                new File(fullPath).delete();
            }
            bitmap = null;
            err.printStackTrace();
        }
        return bitmap;
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;


        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public static HealthRecord_upload_new newInstance(String text) {

        HealthRecord_upload_new f = new HealthRecord_upload_new();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);


        return f;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(parent.getContext(), "Selected: " +doc_type[position], Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String base64Convertions(String fileName) {
        String encodedString = "";

        try {
            InputStream inputStream = new FileInputStream(fileName);//You can get an inputStream using any IO API
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        return encodedString;
    }


    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            Simpleresponse simpleresponse = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
            Gson gson = new Gson();
            if (simpleresponse.status.equals("success")) {
                Toast.makeText(getActivity(), "" + simpleresponse.status, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "" + simpleresponse.status, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
