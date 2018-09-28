package com.example.pegasys.rapmedixuser.activity.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.Simpleresponse;
import com.example.pegasys.rapmedixuser.activity.pojo.pharmacyModel;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pegasys on 3/21/2018.
 */

public class Pharmacy extends Fragment implements View.OnClickListener, RetrofitResponseListener {
    private RelativeLayout l_upload;
    private ImageView img_preview;
    private Button b_next;
    private EditText et_description, et_coupons;
    private TextView tv_coupons, tv_add;
    View view;
    private static final int PERMISSION_REQUEST = 111;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private Dialog dialog;
    private Uri file;
    private Bitmap bitmap;
    private ThumbnailUtils thumbnail;
    private String basestr;
    private Object obj;

    public Pharmacy() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pharmacy_layout, container, false);
        initmethods();
        return view;
    }

    private void initmethods() {
        setreferences();
        setClicklistners();
        checkpermissions();

    }


    private void setreferences() {
        l_upload = view.findViewById(R.id.rl_upload);
        img_preview = view.findViewById(R.id.iv_preview);
        b_next = view.findViewById(R.id.btn_next);
        et_description = view.findViewById(R.id.edt_description);
        et_coupons = view.findViewById(R.id.edt_coupons);
        tv_coupons= view.findViewById(R.id.txt_coupons);
        tv_add = view.findViewById(R.id.txt_add);
    }

    private void setClicklistners() {
        l_upload.setOnClickListener(this);
        b_next.setOnClickListener(this);
        tv_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_upload:
                callalert();
                break;
            case R.id.btn_next:
                updateToServer();
                break;
            case R.id.txt_add:
                if (tv_coupons.getVisibility() == View.VISIBLE) {
                    tv_coupons.setVisibility(View.GONE);
                    et_coupons.setVisibility(View.VISIBLE);
                } else {
                    et_coupons.setVisibility(View.GONE);
                    tv_coupons.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void updateToServer() {
        String uId = new DataBase_Helper(getActivity()).getUserId("1");
        String uName = new DataBase_Helper(getActivity()).getUserName("1");
        String description;
        if (et_description.getText().toString().equals("")) {
            description = "No Description";
        } else {
            description = et_description.getText().toString();
        }

        pharmacyModel model = new pharmacyModel();
        model.userId = uId;
        model.patientName = uName;
        model.description = description;
        if (basestr != null) {
            model.document = basestr;
        } else {
            Toast.makeText(getActivity(), "No Image Attached", Toast.LENGTH_SHORT).show();
        }
        try {
            obj = Class.forName(pharmacyModel.class.getName()).cast(model);
            Log.d("obj", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/save_pharmacy_prescription_service", true);
    }

    private void callalert() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo !");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Take Photo")) {
                    openCamera();

                } else if (items[i].equals("Choose from Library")) {
                    openGalley();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();

    }


    private void openCamera() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Srinu_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intentCamera, REQUEST_CAMERA);
    }


    private void openGalley() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_GALLERY);
    }

    private boolean checkpermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    + ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    + ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                    ) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, PERMISSION_REQUEST);

                return false;

            } else {
                Toast.makeText(getActivity(), "Already Permissions Granted", Toast.LENGTH_SHORT).show();
                return true;
            }

        } else {
            Toast.makeText(getActivity(), "Below Marshmallow", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                try {
                    /*final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);*/
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), file);
                    basestr = encodeImage(bitmap);
                    img_preview.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();
                if (photoUri != null) {
                    try {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(photoUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        Log.i("pic", picturePath);
                        cursor.close();
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                        basestr = encodeImage(bitmap);
                        img_preview.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
         /*   if (requestCode == REQUEST_GALLERY) {
        }*/
        else {


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String encodeImage(Bitmap selectedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        Log.i("picbase", encImage);
        return encImage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Toast.makeText(getActivity(), "Permissions Denied", Toast.LENGTH_SHORT).show();
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(getActivity(), "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    Simpleresponse simpleresponse = Common.getSpecificDataObject(objectResponse, Simpleresponse.class);
                    Gson gson = new Gson();
                    Toast.makeText(getActivity(), "" + simpleresponse.status, Toast.LENGTH_SHORT).show();

            }
        }

    }
}
