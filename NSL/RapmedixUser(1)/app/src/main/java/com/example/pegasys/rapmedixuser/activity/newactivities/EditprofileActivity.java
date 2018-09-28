package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditprofileActivity extends AppCompatActivity {

    EditText Name, Email, City, State;
    TextView Mobile;
    AutoCompleteTextView Address;
    Button Submit;
    ImageView Profile;
    private Uri fileUri;

    private static final int FILE_SELECT_CODE = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "RapMedix_Pictures";
    private String UserId, DbMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();


        Name = (EditText) findViewById(R.id.editName);
        Email = (EditText) findViewById(R.id.editEmail);
        Address = (AutoCompleteTextView) findViewById(R.id.editaddress);
        City = (EditText) findViewById(R.id.editCity);
        State = (EditText) findViewById(R.id.editState);
        Mobile = (TextView) findViewById(R.id.editMobile);
        Submit = (Button) findViewById(R.id.editSubmit);
        Profile = (ImageView) findViewById(R.id.prflImage);

        if (b != null) {
            Name.setText(b.getString("Name"));
            Email.setText(b.getString("Email"));
            Address.setText(b.getString("Address"));
            City.setText(b.getString("City"));
            State.setText(b.getString("State"));
            Picasso.with(this).load(b.getString("Image")).into(Profile);
        }
        DataBase_Helper db = new DataBase_Helper(this);
        UserId = db.getUserId("1");
        DbMobile = db.getUserMobileNumber("1");
        Mobile.setText(DbMobile);
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStoragePermissionGranted();
            }
        });

    }

    public void isStoragePermissionGranted() {


        if (ContextCompat.checkSelfPermission(EditprofileActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            /*if(ContextCompat.checkSelfPermission(LogReg.this,
                    Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_DENIED)*/
            SharedPreferences sp = getSharedPreferences("FixuPerms", MODE_PRIVATE);
            int s = sp.getInt("write_sdcard", 0);
            if (s == 2) {
                //Toast.makeText(LogReg.this,"You have denied the permission required to register for fixu",Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(EditprofileActivity.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.miss);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                TextView pop = (TextView) dialog.findViewById(R.id.mtextView);

                pop.setText("You have denied the permission required to use gallery.\n You can grant the permissions from fixu app permissions in settings.");  //  Please contact customer service

                Button popbtn = (Button) dialog.findViewById(R.id.mbutton);
                //Tf.setTvSemi(EditprofileActivity.this,new TextView[]{pop});
                //Tf.setBtFace(EditprofileActivity.this,new Button[]{popbtn});

                popbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + EditprofileActivity.this.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(i);


//                                                    Intent intent = new Intent(ForgetPassword.this, MainActivity.class);
//                                                    startActivity(intent);
                    }
                });
            } else if (s == 0) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(EditprofileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(EditprofileActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            11);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                //  captureImage();
                showFileChooser();
            }
        } else {
            // captureImage();
            showFileChooser();
        }
    }
    private void showFileChooser() {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(i, FILE_SELECT_CODE);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                //Log.v("onactiivtyresult","result code ok");
                if (fileUri.getPath() != null) {
                    uploadImage(fileUri.getPath());
                  /*  Toast.makeText(getApplicationContext(),
                            "Captured image successfully", Toast.LENGTH_LONG)
                            .show();*/

                    // pathlist.add(0, fileUri.getPath());
                    // cadpt.notifyDataSetChanged();
                } else {
                   /* Toast.makeText(getApplicationContext(),
                            "" + fileUri.getPath(), Toast.LENGTH_LONG)
                            .show();
*/
                    Toast.makeText(getApplicationContext(),
                            "Capturing image failed", Toast.LENGTH_LONG)
                            .show();
                }
                // previewCapturedImage();

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                //Log.v("onactiivtyresult","result code canceled");
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_LONG)
                        .show();
            } else {
                // failed to capture image
                //Log.v("onactiivtyresult","failed to capture image");
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_LONG)
                        .show();
            }
        }
        else if (requestCode == FILE_SELECT_CODE) {

            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                if (data != null) {

                    try {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            final String picturePath = cursor.getString(columnIndex);
                            cursor.close();
                            //	pathlist.add(0,picturePath);
                            //	cadpt.notifyDataSetChanged();
                            if (picturePath != null) {
                                File sourceFile = new File(picturePath);


                                if (!sourceFile.isFile()) {

                                    Toast.makeText(getApplicationContext(), "Source File not exist "
                                            , Toast.LENGTH_LONG).show();
                                } else {
                                    uploadImage(picturePath);
                                }
                            } else {
                                Toast.makeText(EditprofileActivity.this, "Sorry, Failed to upload image", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(EditprofileActivity.this, "Sorry, Failed to upload image", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Image Loading Failed", Toast.LENGTH_LONG)
                            .show();
                }
            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(),
                        "User cancelled file upload", Toast.LENGTH_LONG)
                        .show();
            } else {
                // failed to capture image

                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to load file", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            //Log.v("onactivityresult","request code error");
            Toast.makeText(getApplicationContext(), "Unable to capture the image..Please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage(String picturePath) {

    }


}
