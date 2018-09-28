package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.ImageResponse;
import com.example.pegasys.rapmedixuser.activity.MyApplication;
import com.example.pegasys.rapmedixuser.activity.customviews.RoundedImageView;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.EditProfile;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.example.pegasys.rapmedixuser.activity.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditprofileActivity extends AppCompatActivity implements RetrofitResponseListener {

    EditText Name, Email, City, State;
    TextView Mobile;
    AutoCompleteTextView Address;
    Button Submit;
    RoundedImageView Profile;
    LinearLayout layout;
    private Uri fileUri;
    String mImageFileName;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    private static final int FILE_SELECT_CODE = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "RapMedix_Pictures";
    private String UserId, DbMobile;

    private Object obj;
    String x = "", xx = "";
    private String result = "NO IMAGE";
    private boolean check = true;
    private String response;
    //    private ApiInterface apiService;
    private ProgressDialog progressDialog;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);


        ImageView backButton = toolbar.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(EditprofileActivity.this,ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
//        checkSelfPermission()


        Name = findViewById(R.id.editName);
        Email = findViewById(R.id.editEmail);
        Address = findViewById(R.id.editaddress);
        City = findViewById(R.id.editCity);
        State = findViewById(R.id.editState);
        Mobile = findViewById(R.id.editMobile);
        Submit = findViewById(R.id.editSubmit);
        Profile = findViewById(R.id.prflImage);
        layout = findViewById(R.id.imageLayout);

        if (b != null) {
            Name.setText(b.getString("Name"));
            Email.setText(b.getString("Email"));
            Address.setText(b.getString("Address"));
            City.setText(b.getString("City"));
            State.setText(b.getString("State"));

            Picasso.with(this).load(b.getString("Image")).into(Profile);
        }
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfile profile = new EditProfile();
                profile.userId = new DataBase_Helper(EditprofileActivity.this).getUserId("1");
                profile.name = Name.getText().toString().trim();
                profile.mobile = Mobile.getText().toString().trim();
                profile.email = Email.getText().toString().trim();
                profile.address = Address.getText().toString().trim();
                profile.city = City.getText().toString().trim();
                profile.state = State.getText().toString().trim();
                profile.profilePic = mImageFileName;

                // profile.profilePic = body;
                try {
                    obj = Class.forName(EditProfile.class.getName()).cast(profile);
                    Log.d("obj", obj.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                new RetrofitRequester(EditprofileActivity.this).callPostServices(obj, 1, "/user/update_user_profile", true);


            }
        });
        DataBase_Helper db = new DataBase_Helper(this);
        UserId = db.getUserId("1");
        DbMobile = db.getUserMobileNumber("1");
        Mobile.setText(DbMobile);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isStoragePermissionGranted();
                selectImage1();
            }
        });


    }

    private void selectImage1() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditprofileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditprofileActivity.this);
              /*  if (items[item].equals("Take Photo")) {
//                    userChoosenTask = "Take Photo";
//                    if (result)
//                        cameraIntent();

                    if (ContextCompat.checkSelfPermission(EditprofileActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(EditprofileActivity.this,
                                Manifest.permission.CAMERA)) {

                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        } else {
                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(EditprofileActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    11);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }


                    } else {

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
                } else*/
              if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
/*
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();*/
  /*                  else */
                    if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();

                } else {
                    Toast.makeText(EditprofileActivity.this, "User Denied The Permissions", Toast.LENGTH_SHORT).show();
                    //code for deny
                }
                break;

            case 11:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                } else {
                    Toast.makeText(EditprofileActivity.this, "User Denied The Permissions", Toast.LENGTH_SHORT).show();
                }
                break;

            /*case Utility.MY_CAMERA_REQUEST_CODE:
                if (userChoosenTask.equals("Take Photo"))
                    cameraIntent();

                break;*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == 0)
                onCaptureImageResult(data);
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] byteArray = bytes.toByteArray();
                result = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fileUri = data.getData();
                file = new File(fileUri.getPath());

                File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;

                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                sendImageToServer(saveBitmapToFile(destination));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Profile.setImageBitmap(bm);
    }


    //===================================================================================================================

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri, File file) {
// https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
// use the FileUtils to get the actual file by uri
        File file1 = new File(fileUri.getPath());

// create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getMimeType(getApplicationContext(), fileUri)),
                        file
                );

// MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

//Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
//If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
//If scheme is a File
//This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    private void sendImageToServer(File file) {
        Toast.makeText(getApplicationContext(), "Image upload started", Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("uploading");
        progressDialog.create();
        progressDialog.show();
        Uri.fromFile(file);
        MultipartBody.Part part = prepareFilePart("profile_pic", Uri.fromFile(file), file);
        Call<ImageResponse> abc = MyApplication.getInstance().getAPIInterface().uploadFile("/user/userprofile_update_service", part);

        abc.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                ImageResponse imageResponse = response.body();
//                Toast.makeText(getApplicationContext(), "" + imageResponse, Toast.LENGTH_SHORT).show();
                if (imageResponse.status.equals("success")) {
                    mImageFileName = imageResponse.profilePic.trim();
                    Toast.makeText(getApplicationContext(), "" + imageResponse.status + "..Profile picture Updated", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), " " + imageResponse.status, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = null;

        if (data != null) {
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] byteArray = bytes.toByteArray();
                result = Base64.encodeToString(byteArray, Base64.DEFAULT);
                fileUri = data.getData();
                file = new File(fileUri.getPath());

                File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;

                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                sendImageToServer(destination);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        try {
            Profile.setImageBitmap(bitmap);
        /*} catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
       /* Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        byte[] byteArray = bytes.toByteArray();
        result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendImageToServer(destination);
        Profile.setImageBitmap(thumbnail);
*/
        // Capture image from camera

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }
    private void captureImage() {
        //  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //android.provider.MediaStore.ACTION_IMAGE_CAPTURE

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory(),
                // .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                //   + IMAGE_DIRECTORY_NAME + " directory");
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


    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(EditprofileActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            Simpleresponse simpleresponse = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
            Gson gson = new Gson();
            if (simpleresponse.status.equals("success")) {
                Toast.makeText(EditprofileActivity.this,"" +simpleresponse.status +"..Profile Updated", Toast.LENGTH_LONG).show();



            } else {
                Toast.makeText(EditprofileActivity.this, simpleresponse.status, Toast.LENGTH_SHORT).show();
            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    Intent intent = new Intent(EditprofileActivity.this,ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditprofileActivity.this,ProfileActivity.class);
        startActivity(intent);
        finish();
    }


}
