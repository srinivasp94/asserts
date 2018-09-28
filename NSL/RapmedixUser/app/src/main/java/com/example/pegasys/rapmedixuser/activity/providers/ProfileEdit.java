/*
package com.example.pegasys.rapmedixuser.activity.providers;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.adapters.PlaceArrayAdapter;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.network.FileAsync;
import com.example.pegasys.rapmedixuser.activity.network.OnAsyncCompleteRequest;
import com.example.pegasys.rapmedixuser.activity.newactivities.ProfileActivity;
import com.example.pegasys.rapmedixuser.activity.utils.ConnectionDetector;
import com.example.pegasys.rapmedixuser.activity.utils.CustomAsync;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


*/
/**
 * Created by ADMIN on 2/4/2017.
 *//*


public class ProfileEdit extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    EditText Name, Email, City, State;
    TextView Mobile;
    AutoCompleteTextView Address;
    Button Submit;
    ImageView Profile;
    boolean isNet;
    String UserId = "", DbMobile = "", Url = "";
    View view;
    private Uri fileUri;
    String x = "", xx = "";

    private static final int FILE_SELECT_CODE = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "RapMedix_Pictures";

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(17.3660, 78.4760), new LatLng(17.3660, 78.4760));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Name = (EditText) findViewById(R.id.editName);
        Email = (EditText) findViewById(R.id.editEmail);
        Address = (AutoCompleteTextView) findViewById(R.id.editaddress);
        City = (EditText) findViewById(R.id.editCity);
        State = (EditText) findViewById(R.id.editState);

        Mobile = (TextView) findViewById(R.id.editMobile);

        Submit = (Button) findViewById(R.id.editSubmit);

        Profile = (ImageView) findViewById(R.id.prflImage);

        view = findViewById(R.id.editprofile);
//		View myLayout = findViewById(R.id.profileedit);
//
//		ImageView back = (ImageView) myLayout.findViewById(R.id.mainmenu);


//		back.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onBackPressed();
//			}
//		});

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {

            Name.setText(b.getString("Name"));
            Email.setText(b.getString("Email"));
            Address.setText(b.getString("Address"));
            City.setText(b.getString("City"));
            State.setText(b.getString("State"));

            Glide.with(ProfileEdit.this).
                    load(b.getString("Image")).
                    error(R.drawable.single).
                    //placeholder(R.drawable.single).
                            into(Profile);
        }

        ConnectionDetector cd = new ConnectionDetector(this);
        isNet = cd.isConnectingToInternet();

        DataBase_Helper db = new DataBase_Helper(this);
        UserId = db.getUserId("1");
        DbMobile = db.getUserMobileNumber("1");

        mGoogleApiClient = new GoogleApiClient.Builder(ProfileEdit.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(ProfileEdit.this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        Address.setThreshold(3);

        Address.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        Address.setAdapter(mPlaceArrayAdapter);


        Mobile.setText(DbMobile);

        Url = "https://www.rapmedix.com/user/userprofile_update_service";

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStoragePermissionGranted();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Name.getText().toString().equals("") || Address.getText().toString().equals("") ||
                        City.getText().toString().equals("")) {

                    Snackbar snackBar = Snackbar.make(view, "Fields Should not be Empty!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                    snackBar.setActionTextColor(Color.RED);
                    View sbView = snackBar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackBar.show();
                } else {

                    if (isNet) {

                        try {

                            JSONObject jo = new JSONObject();

                            jo.put("user_id", UserId);
                            jo.put("name", Name.getText().toString().trim());
                            jo.put("mobile", Mobile.getText().toString().trim());
                            jo.put("email", Email.getText().toString().trim());
                            jo.put("address", Address.getText().toString().trim());
                            jo.put("city", City.getText().toString().trim());
                            jo.put("state", State.getText().toString().trim());
                            jo.put("profile_pic", xx);

                            profileEdit(jo, Url);


                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    } else {

                        Snackbar snackBar = Snackbar.make(view, "No Internent Connection!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        finish();
                                        startActivity(getIntent());
                                    }
                                });
                        snackBar.setActionTextColor(Color.RED);
                        View sbView = snackBar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackBar.show();
                    }
                }
            }
        });
    }

    public void profileEdit(JSONObject jo, String url) {

        CustomAsync ca = new CustomAsync(ProfileEdit.this, jo, url, new OnAsyncCompleteRequest() {
            @Override
            public void asyncResponse(String result) {

                if (result.equals("") || result == null) {

                    Snackbar snackBar = Snackbar.make(view, "Please try again!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                    snackBar.setActionTextColor(Color.RED);
                    View sbView = snackBar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackBar.show();

                } else {

                    try {

                        JSONObject jo = new JSONObject(result);

                        String status = jo.getString("status");

                        if (status.equals("success")) {

                            Toast.makeText(ProfileEdit.this, "Profile Updated successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProfileEdit.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {

                            Toast.makeText(ProfileEdit.this, "" + status, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }
        });
        ca.execute();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.e("GoogleMaps", "Google Places API connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {

        mPlaceArrayAdapter.setGoogleApiClient(null);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(ProfileEdit.this, "parvez", Toast.LENGTH_SHORT).show();
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.e("Location", "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.e("Location", "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("Location", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            Address.setText(Html.fromHtml(place.getName() + ""));
            Address.setText(Html.fromHtml(place.getAddress() + ""));
            //Spanned Add2=(Html.fromHtml(place.getId() + ""));
            //Address.setText(Html.fromHtml(place.getLatLng()+ ""));
            //Address.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            //String Add4=(place.getWebsiteUri() + "");

			*/
/*String Addd = Html.toHtml(Add2);

			Log.e("FinalLocationData", Addd);

			double lat = Double.valueOf(Addd.substring(23, 32));
			double lng = Double.valueOf(Addd.substring(33,43));



			Log.e("FinalLocationData", lat+"\n"+lng);*//*


			*/
/*Geocoder gcd = new Geocoder(ProfileEdit.this);
            try {
				List<Address> Addre = gcd.getFromLocationName(Address.getText().toString(), 5);

				Log.e("AddressData", "123    "+ Addre);

				if (Addre == null) {


				}

				else {

					android.location.Address loacation = Addre.get(0);
					double lat = Double.valueOf(loacation.getLatitude());
					double lng = Double.valueOf(loacation.getLongitude());

					Log.e("FinalLocationData", "123    "+ lat+"\n"+lng);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}*//*


            Log.e("FinalLocationData", "123    " + Address.getText().toString());

            if (attributions != null) {
                Address.setText(Html.fromHtml(attributions.toString()));
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                //Log.v("onactiivtyresult","result code ok");
                if (fileUri.getPath() != null) {
                    uploadImage(fileUri.getPath());
                  */
/*  Toast.makeText(getApplicationContext(),
                            "Captured image successfully", Toast.LENGTH_LONG)
                            .show();*//*


                    // pathlist.add(0, fileUri.getPath());
                    // cadpt.notifyDataSetChanged();
                } else {
                   */
/* Toast.makeText(getApplicationContext(),
                            "" + fileUri.getPath(), Toast.LENGTH_LONG)
                            .show();
*//*

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
        } else if (requestCode == FILE_SELECT_CODE) {

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
                            Log.i("profile", picturePath);
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
                                Toast.makeText(ProfileEdit.this, "Sorry, Failed to upload image", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ProfileEdit.this, "Sorry, Failed to upload image", Toast.LENGTH_LONG).show();
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

    public void isStoragePermissionGranted() {


        if (ContextCompat.checkSelfPermission(ProfileEdit.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            //noinspection StatementWithEmptyBody
            */
/*if (ContextCompat.checkSelfPermission(ProfileEdit.this,
                    Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_DENIED) {

            }*//*

            SharedPreferences sp = getSharedPreferences("FixuPerms", MODE_PRIVATE);
            int s = sp.getInt("write_sdcard", 0);
            if (s == 2) {
                //Toast.makeText(LogReg.this,"You have denied the permission required to register for fixu",Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(ProfileEdit.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.miss);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                TextView pop = (TextView) dialog.findViewById(R.id.mtextView);

                pop.setText("You have denied the permission required to use gallery.\n You can grant the permissions from fixu app permissions in settings.");  //  Please contact customer service

                Button popbtn = (Button) dialog.findViewById(R.id.mbutton);
                //Tf.setTvSemi(ProfileEdit.this,new TextView[]{pop});
                //Tf.setBtFace(ProfileEdit.this,new Button[]{popbtn});

                popbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + ProfileEdit.this.getPackageName()));
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileEdit.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(ProfileEdit.this,
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

    private void uploadImage(String path) {
        //DbHelper sh = new DbHelper(ProfileEdit.this);
        //String vid = sh.getUserID(1);
        // String m=getString(R.string.ser)+C.up_pimg;
        String m = "https://www.rapmedix.com/user/update_user_profile";

        //Log.e("sending data",vid+"\n "+m+"\n "+picturePath);
        FileAsync fa = (FileAsync) new FileAsync(ProfileEdit.this, m, path, UserId, new OnAsyncCompleteRequest() {

            @Override
            public void asyncResponse(String response) {
                // TODO Auto-generated method stub
                //	Log.e("resp",response);
                if (response == null || TextUtils.isEmpty(response)) {
                    Toast.makeText(ProfileEdit.this, "Unable to Register.Please Try Again", Toast.LENGTH_LONG).show();
                    // d.s("Unable to Register.Please Try Again");
                } else {
                    try {
                        final JSONObject j = new JSONObject(response);

                        if (j.getString("status").equals("success")) {
                            Toast.makeText(ProfileEdit.this, "Success", Toast.LENGTH_LONG).show();

                            xx = j.getString("profile_pic");
                            x = "https://www.rapmedix.com/uploads/user_profile_images/" + j.getString("profile_pic");

                            //Log.e("ImagePath", x+"    123");
                            Glide.with(ProfileEdit.this).load(x)
                                    .bitmapTransform(new CropCircleTransformation(ProfileEdit.this))
                                    .placeholder(R.drawable.single)
//                                    .crossFade()
//                                    .thumbnail(0.1f)
                                    .error(R.drawable.single)
                                    //.into(Pic);

                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            // load second image here

                                            //Log.e("ImagePath", x+"    123");

                                            Glide.with(ProfileEdit.this).load(x)
                                                    .error(R.drawable.single)
                                                    .into(Profile);
                                            return false;
                                        }
                                    }).into(Profile);

                            Profile.setAlpha((float) 0.5);

                            new PicSaver(ProfileEdit.this, j).execute();


                            //  bmFile = BitmapFactory.decodeFile(picturePath);

                            //  p_img.setImageBitmap(bmFile);
                        } else {
                            //  d.s("Sorry, Failed to upload image");
                            Toast.makeText(ProfileEdit.this, "Sorry, Failed to upload image", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        });
        fa.execute();
    }


    private class PicSaver extends AsyncTask<Void, Void, Void> {


        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //super.onPostExecute(result);

            //  caller.asyncResponse((result));

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //super.onPreExecute();

        }

        Context con;
        JSONObject job;

        OnAsyncCompleteRequest caller;


        public PicSaver(Context con, JSONObject jo) {
            // TODO Auto-generated constructor stub
            this.con = con;
            job = jo;

            //   caller = oas;
            //this.otp = otp;
        }


        @Override
        protected Void doInBackground(Void... params) {

            try {


                //DbHelper dh = new DbHelper(con);
                //dh.updateUserPImg(job.getString("profile_pic"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
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


}
*/
