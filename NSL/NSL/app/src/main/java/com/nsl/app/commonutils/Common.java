package com.nsl.app.commonutils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsl.app.Constants;
import com.nsl.app.MyApplication;
import com.nsl.app.R;
import com.nsl.app.enums.PlannerEnum;
import com.nsl.app.pojo.VersionControlVo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit.mime.TypedFile;


public class Common {
    static final String LOG = Common.class.getSimpleName();
    public static final String INTERNET_UNABLEABLE = "Not connected to the internet. Please check your connection and try again.";
    public static final int TOAST_TIME = 2000;
    private static Activity previousActivity;




    public static void customToast(final Context context, final String msg, final int millisec)
    {
        Activity activity = (Activity) context;

        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(context, msg, millisec).show();
            }
        });

    }



    /*public static void showConfirmationDialog(Activity activity,String message)
    {

        final Dialog confirmationDialog = new Dialog(activity);

        confirmationDialog.setContentView(R.layout.confirmation_dialog);

        confirmationDialog.setCancelable(false);

        CustomTextView ctvConfirmationDialogMessage = (CustomTextView) confirmationDialog.findViewById(R.id.title);

        ctvConfirmationDialogMessage.setText(message);

        CustomButton customButton = (CustomButton) confirmationDialog.findViewById(R.id.ok);

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
            }
        });

        confirmationDialog.show();
    }*/



    public static boolean haveInternet(Context ctx) {
        try {
            NetworkInfo info = ((ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();

            if (info == null || !info.isConnected()) {
                return false;
            }
        } catch (Exception e) {
            android.util.Log.d("err", e.toString());
        }
        return true;
    }

    public static void getAlertDialog(final Activity context, String title,
                                      String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("Invite",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                context.finish();
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void showAlertDialog(final Activity context, String title,
                                       String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(Message)
                .setCancelable(false)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        intent.putExtra("EXIT", true);
                        if (context.getIntent().getBooleanExtra("EXIT", false)) {
                            context.finish();
                        }
                        android.os.Process.killProcess(android.os.Process.myPid());

                    }
                })
                .setPositiveButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void getAlertDialogWhyChooseUs(final Activity context, String title,
                                                 String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(Message)
                .setCancelable(false)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void hideKeyboard( Context context ) {

        try {
            InputMethodManager inputManager = ( InputMethodManager ) context.getSystemService( Context.INPUT_METHOD_SERVICE );

            View view = ( (Activity) context ).getCurrentFocus();
            Log.i("view: "+view);
            if ( view != null ) {
                inputManager.hideSoftInputFromWindow( view.getWindowToken(), 0);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
    public static void hideSoftKeyboard(FragmentActivity activity,View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    public static boolean checkSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
          return true;
        }
        return false;
    }


    public static void hideSoftKeyboardFromDialog(Dialog dialog,Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (dialog.getCurrentFocus() != null) {
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
        }
    }
    public static String retrieveContactRecord(Context activity, String phoneNo) {
        String contactId;
        String contactName = "";
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
            String[] projection = new String[]{ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_URI};
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = ContactsContract.PhoneLookup.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
            ContentResolver cr = activity.getContentResolver();
            if (cr != null) {
                Cursor resultCur = cr.query(uri, projection, selection, selectionArgs, sortOrder);
                if (resultCur != null) {
                    while (resultCur.moveToNext()) {
                        contactId = resultCur.getString(resultCur.getColumnIndex(ContactsContract.PhoneLookup._ID));
                        contactName = resultCur.getString(resultCur.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                        String photoUri = resultCur.getString(resultCur.getColumnIndexOrThrow(ContactsContract.PhoneLookup.PHOTO_URI));
//                        Log.e("Info","Contact Id : "+contactId);
//                        Log.e("Info","Contact Display Name : "+contactName);
//                        Log.d("SUBBU", photoUri);
                        byte[] data = resultCur.getBlob(resultCur.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI));
                        if (data != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                            Log.d("SUBBU", bitmap + "");
                        }
                        return contactName;

                    }
                    //resultCur.close();
                }
            }
        } catch (Exception sfg) {
            // Log.e("Error", "Error in loadContactRecord : "+sfg.toString());
        }
        return contactName;
    }//fn retrieveContactRecord

    public static long getContactIDFromNumber(String contactNumber, Context context) {
        String UriContactNumber = Uri.encode(contactNumber);
        long phoneContactID = new Random().nextInt();
        Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, UriContactNumber),
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, null, null, null);
        while (contactLookupCursor.moveToNext()) {
            phoneContactID = contactLookupCursor.getLong(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
        }
        contactLookupCursor.close();

        return phoneContactID;
    }

    /*date format YYYY-MM-DD to dd-mm-yyyy*/
    public static String dateformate(String dt) {
        String d = "";
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMMM-yyyy");
        try {
            Date date = formatter.parse(dt);
            d = formatter1.format(date);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return d;
    }

    /*date format  dd-mm-yyyy to YYYY-MM-DD */
    public static String dateformateDDMMYYYY(String dt) {
        String d = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dt);
            d = formatter1.format(date);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return d;
    }

    /*date format  dd-mm-yyyy to YYYY-MM-DD */
    public static String setDateFormateOnTxt(String dt) {
        String d = "";
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMM yyyy, EEE");
        try {
            Date date = formatter1.parse(dt);
            d = formatter2.format(date);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return d;
    }

    /*date format  dd-mm-yyyy to YYYY-MM-DD */
    public static String dateformateByTmeZone(String dt) {
        String d = "";
        TimeZone TZ1 = TimeZone.getDefault();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss a");
        try {
            Date date = formatter1.parse(dt);
            formatter2.setTimeZone(TimeZone.getTimeZone(TZ1.getDisplayName()));
            d = formatter2.format(date);

//            d=formatter1.format(date);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return d;
    }
    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String toDateStr(long milliseconds, String format)
    {
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date);
    }

    public static boolean isTime3AM(){
        int hours = new Time(System.currentTimeMillis()).getHours();
        Log.i("hours; "+hours);
        if (hours >= 3){

            return true;
        }
        return false;
    }

    public static void showGPSDisabledAlertToUser(final Activity context, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static String toTitleCase(String input) {
        input = input.toLowerCase();
        char c = input.charAt(0);
        String s = new String("" + c);
        String f = s.toUpperCase();
        return f + input.substring(1);
    }

    public static String getAddressString(Context context, double LATITUDE, double LONGITUDE) {

        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);


            if (addresses != null) {
                Address address = addresses.get(0);

                Log.i("address.toString() : " + address.toString());

                StringBuilder sb = new StringBuilder("");
                String Feature = address.getFeatureName();
                String ThroughFrare = address.getSubThoroughfare();
                String Sub_Admin = address.getSubAdminArea();
                String Locality = address.getLocality();
                String Admin = address.getAdminArea();
                String Country = address.getCountryName();

                /*if (Feature != null) {
                    sb.append(Feature);
                }
                if (ThroughFrare != null) {
                    sb.append(","+ThroughFrare);
                }
                if (Sub_Admin != null) {
                    sb.append(","+Sub_Admin);
                }*/
                if (Locality != null) {
                    Log.i("Locality : " + Locality);
                    sb.append(Locality);
                }
                if (Admin != null) {
                    Log.i("Admin : " + Admin);

                    sb.append(", " + Admin);
                }
                if (Country != null) {

                    Log.i("Country : " + Country);

                    sb.append(", " + Country);
                }
                strAdd = sb.toString();


                android.util.Log.w("My Current", "" + sb.toString());
            } else {
                android.util.Log.w("My Current", "No Address returned!");
            }


        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.w("My Current", "Canont get Address!");
        }

        return strAdd;

    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String dateformater(String dt) {
        String d = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd:MMMM:yyyy");
        try {
            Date date = formatter.parse(dt);
            d = formatter1.format(date);


        } catch (Exception e) {
            // TODO: handle exception
        }
        return d;
    }


    public static Object stringToObject(String str) {
        try {
            return new ObjectInputStream(new Base64InputStream(
                    new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                    | Base64.NO_WRAP)).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //eg String=17.12354,86.35466;  to double[] a={17.12354,86.35466};

    public static List<Double> stringToDubleArry(String latlngString) {
        List<Double> latlng = new ArrayList<>();

        String[] sourceArray = latlngString.split(",");

        for (int i = 0; i < sourceArray.length; i++) {
            String lat = sourceArray[i];
            latlng.add(Double.parseDouble(lat));
        }

        return latlng;
    }

    public static String[] spliteString(String stringForSplit) {
        String[] array = stringForSplit.split(",");

        return array;
    }

    public static String constructLatLng(Location latlng) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(latlng.getLatitude()));
        stringBuilder.append(',');
        stringBuilder.append(String.valueOf(latlng.getLongitude()));
        String latlngString = stringBuilder.toString();

        return latlngString;
    }

    /**
     * Encodes a sequence of LatLngs into an encoded path string.
     */
    public static String encode(final List<LatLng> path) {
        long lastLat = 0;
        long lastLng = 0;

        final StringBuffer result = new StringBuffer();

        for (final LatLng point : path) {
            long lat = Math.round(point.latitude * 1e5);
            long lng = Math.round(point.longitude * 1e5);

            long dLat = lat - lastLat;
            long dLng = lng - lastLng;

            encode(dLat, result);
            encode(dLng, result);

            lastLat = lat;
            lastLng = lng;
        }
        return result.toString();
    }

    private static void encode(long v, StringBuffer result) {
        v = v < 0 ? ~(v << 1) : v << 1;
        while (v >= 0x20) {
            result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
            v >>= 5;
        }
        result.append(Character.toChars((int) (v + 63)));
    }

    public static List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }

    /* public static String encodePolylinrByLntLatList( List<WayLocationsLog> list){
         List<LatLng> logs=new ArrayList<>();
         for (int i=0;i<list.size();i++){
             LatLng point=new LatLng(Double.parseDouble(list.get(i).getLatitude()),Double.parseDouble(list.get(i).getLongitude()));
             logs.add(point);
         }
         String encodedPolyline=Common.encode(logs);
         return encodedPolyline;
     }*/
    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(Color.parseColor("#ff6c2c"));
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
                finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    public static boolean isGooglePlayServicesAvailable(Activity context) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, context, 0).show();
            return false;
        }
    }

    /*public static final GeoDetails getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String completeAddress = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        GeoDetails geoDetails=null;
        StringBuilder sb = new StringBuilder("");
        String city = "";
        Address returnedAddress = null;
        String Country="";
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null&&addresses.size()>0) {
                 returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }
                completeAddress = strReturnedAddress.toString();


                String Locality = returnedAddress.getLocality();
                String Admin = returnedAddress.getAdminArea();
                Country = returnedAddress.getCountryName();

                String getSubAdminArea = returnedAddress.getSubAdminArea();
                String getSubLocality = returnedAddress.getSubLocality();
                String getFeatureName = returnedAddress.getFeatureName();
                Log.i("Locality : " + Locality+"getSubLocality : " + getSubLocality+"Admin : " + Admin+"getSubAdminArea : " + getSubAdminArea+"getFeatureName : " + getFeatureName);


                if (Locality != null) {
                    Log.i("Locality : " + Locality);
                    sb.append(Locality);
                }
                if (Admin != null) {
                    Log.i("Admin : " + Admin);
                    if (Locality != null) {
                        sb.append(", " );
                    }
                    sb.append(Admin);
                }
                if (Country != null) {

                    Log.i("Country : " + Country);

                    sb.append(", " + Country);
                }


                if (Locality == null) {

                    city = returnedAddress.getSubAdminArea();
                    if (city==null){
                        city= returnedAddress.getSubLocality();
                    }

                } else {
                    city = Locality;
                }
               geoDetails = new GeoDetails(sb.toString(), completeAddress, returnedAddress.getCountryName(),city);

                android.util.Log.w("My Current", "" + strReturnedAddress.toString());
            } else {
                android.util.Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.w("My Current", "Canont get Address!");
        }
        return new GeoDetails(sb.toString(), completeAddress, Country,city);
    }*/

    public static void customToast(Context context, String message) {
        customToast(context, message, Toast.LENGTH_LONG);
    }


    /*public static JSONObject getDataJsonObject(Object o) {
//        Object data = ((ApiResponseVo) o).getData();

        ApiResponseVo apiResponseVo = getSpecificDataObject(o, ApiResponseVo.class);

        *//*
        Object data = ((ApiResponseVo) o).data;
        String jsonString = new Gson().toJson(data);
        *//*

        String jsonString = new Gson().toJson(apiResponseVo.data);

        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }*/





    public static String getJsonStringFromHashMap(HashMap<String, String> hashMap) {
        return new Gson().toJson(hashMap);
    }

    public static SharedPreferences getDefaultSP(Context context) {

        return context.getSharedPreferences(Constants.MYPREFERENCE,Context.MODE_PRIVATE);

    }


    public static String getUserIdFromSP(Context context) {
        String userID = getDefaultSP(context).contains(Constants.USER_ID) ? getDefaultSP(context).getString(Constants.USER_ID, "") : "";

        return userID;

    }
    public static void saveCurrentLocationInSP(Context context,double latitude,double longitude){
        getDefaultSP(context).edit().putString("latitude", String.valueOf(latitude)).commit();
        getDefaultSP(context).edit().putString("longitude", String.valueOf(longitude)).commit();
    }
    public static Location getCurrentLocationFromSP(Context context){
        Location location=new Location("");
        location.setLatitude(Double.parseDouble(getDefaultSP(context).getString("latitude","0")));
        location.setLongitude(Double.parseDouble(getDefaultSP(context).getString("longitude","0")));

        return location;
    }

    public static String getDataFromSP(Context context,String key) {
        String userID = getDefaultSP(context).contains(key) ? getDefaultSP(context).getString(key, "0") : "0";

        return userID;

    }

    public static void saveDataInSP(Context context,String key,String value){
        getDefaultSP(context).edit().putString(key,value).commit();
    }

    public static boolean getBooleanDataFromSP(Context context,String key) {
        boolean userID = getDefaultSP(context).contains(key) ? getDefaultSP(context).getBoolean(key, false) : false;

        return userID;

    }

    public static void saveDataInSP(Context context,String key,boolean value){
        getDefaultSP(context).edit().putBoolean(key,value).commit();
    }

  /*  public static void saveUserIdInSP(Context context,int userID){
        getDefaultSP(context).edit().putInt(Constants.USER_ID,userID).commit();
    }

    public static void saveLoginStatusInSP(Context context,boolean status){
        getDefaultSP(context).edit().putBoolean(Constants.LOGIN_STATUS,status).commit();
    }

    public static boolean getLoginStatusFromSP(Context context){
        boolean loginStatus = getDefaultSP(context).contains(Constants.LOGIN_STATUS) ? getDefaultSP(context).getBoolean(Constants.LOGIN_STATUS, false) : false;
    return loginStatus;
    }*/

    public static String getDeviceResolution(Context context) {
        int density = context.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_MEDIUM:
                return "MDPI";
            case DisplayMetrics.DENSITY_HIGH:
                return "HDPI";
            case DisplayMetrics.DENSITY_LOW:
                return "LDPI";
            case DisplayMetrics.DENSITY_XHIGH:
                return "XHDPI";
            case DisplayMetrics.DENSITY_TV:
                return "TV";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "XXHDPI";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "XXXHDPI";
            default:
                return "Unknown";
        }
    }

    public static void showContentView(Activity activity, boolean showStatus) {

        int visibleStatus = showStatus ? View.VISIBLE : View.GONE;

        activity.findViewById(android.R.id.content).setVisibility(visibleStatus);


    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public static void dismissAlertDialog(AlertDialog alertDialog) {

        if (alertDialog != null) {
            alertDialog.dismiss();
        }

    }

    public static void dismissDialog(Dialog dialog) {

        if (dialog != null) {
            dialog.dismiss();
        }

    }

    public static void finishActivity(Activity activityInstance) {


        if (activityInstance != null) {
            activityInstance.finish();
        }

    }

    public static ProgressDialog showProgressDialog(Context context) {

        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Please Wait.....");


        if (!((Activity) context).isFinishing()) {
            progressDialog.show();
        }

        return progressDialog;


    }

    public static void refreshActivity(Activity activity) {

        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);

    }

    public static Gson getCustomGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    public static <T> T getSpecificDataObject(Object object, Class<T> classOfT) {

        String jsonString = new Gson().toJson(object);

        return new Gson().fromJson(jsonString, classOfT);

    }
    public static <T> T getSpecificDataObject(String jsonString, Class<T> classOfT) {

        //String jsonString = new Gson().toJson(object);

        return new Gson().fromJson(jsonString, classOfT);

    }

    public static void showScreenDensity() {

    }




    public static String getStringResourceText(int resourceId) {
        return MyApplication.getInstance().getResources().getString(resourceId);
    }
/*
    public static float getDimensionResourceValue(int resourceId) {
        return WAYALERTS.getInstance().getResources().getDimension(resourceId);
    }

    public static String getOnlyPhoneNumber(String countryCode, String phoneNumberWithCountryCode) {
        return countryCode == null ? phoneNumberWithCountryCode : phoneNumberWithCountryCode.replaceFirst(countryCode.replace("+", ""), "");
    }

    public static void saveUserIdIntoSP(int userid) {

        getDefaultSP(WAYALERTS.getInstance().getApplicationContext()).edit().putInt(Constants.SharedPreferencesKeys.USERID, userid).commit();

    }

    public static void saveLoginStatusIntoSP(boolean status) {

        getDefaultSP(WAYALERTS.getInstance().getApplicationContext()).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS, status).commit();

    }
*/

    public static void setCircleImageBackgroundFromUrl(Context context, ImageView profileImgView, String profilePicUrl) {

        if (profilePicUrl != null && !TextUtils.isEmpty(profilePicUrl)) {

            Picasso.with(context).load(profilePicUrl).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(profileImgView);

        } else {
            Log.i("Unable to set Bg Image.");
        }


    }

   /* public static String getLocaleFromSP(Context context)
    {
//        return Common.getDefaultSP(context).getString(Constants.SharedPreferencesKeys.APP_LOCALE, Language.ENGLISH.getLocaleCode());

        Log.i("? - Locale.getDefault().getLanguage() : "+Locale.getDefault().getLanguage());

        return Common.getDefaultSP(context).getString(Constants.SharedPreferencesKeys.APP_LOCALE, Locale.getDefault().getLanguage());
    }

    public static void setLocaleToSP(Context context, String localeCode)
    {
        Common.getDefaultSP(context).edit().putString(Constants.SharedPreferencesKeys.APP_LOCALE,localeCode).commit();

    }*/
/*

    public static void restartApp(Activity activity)
    {

        Intent launchIntentForPackage = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
        launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchIntentForPackage.putExtra(Constants.IntentKeys.SHOW_CONTENT_VIEW,false);
        activity.startActivity(launchIntentForPackage);

    }

    public static void sendUnReadNotificationCountBrodCast(Context context, int unReadNotificationsCount) {

        Intent intent = new Intent();

        intent.setAction(PushNotificationListenerActivity.UN_READ_NOTIFICATION_COUNT_CHANGE);

        intent.putExtra(Constants.KEY_1,unReadNotificationsCount);

        context.sendBroadcast(intent);

    }
*/

    /*public static Dialog getAppThemeCustomDialog(Activity activity, int contentViewLayoutId, float leftRightMargin)
    {

        final Dialog dialog = new Dialog(activity);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(contentViewLayoutId);

        DisplayMetrics metrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Log.i("? - Display width in px is " + metrics.widthPixels);

//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setLayout(metrics.widthPixels - (int)leftRightMargin, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setGravity(Gravity.CENTER);

        return dialog;

    }*/




    public static String booleanToIntString(boolean b)
    {

        return String.valueOf(b ? 1 : 0);

    }

    public static boolean intToBoolean(int b)
    {
        boolean value = b == 1 ? true : false;

        return value;

    }


    public static class Log {
        public static void i(String string) {

            android.util.Log.i("SKP", string);


        }
    }

    public static void setLanguage(Context context, String language) {
        Locale locale1 = new Locale(language);
        Locale.setDefault(locale1);
        Configuration config1 = new Configuration();
        config1.locale = locale1;
        context.getResources().updateConfiguration(config1,
                context.getResources().getDisplayMetrics());

    }

    public static void hideView(Context context, final View view, int id) {
        Animation animation = AnimationUtils.loadAnimation(context, id);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
        });

        view.startAnimation(animation);
    }


    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /*public static void copy(String string, Context context) {
        if (string.trim().length() > 0) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboardMgr = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardMgr.setText(string.trim());
            } else {
                // this api requires SDK version 11 and above, so suppress warning for now
                android.content.ClipboardManager clipboardMgr = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied..", string.trim());
                clipboardMgr.setPrimaryClip(clip);
            }
            customToast(context, Common.getStringResourceText(R.string.copied), TOAST_TIME);
        }
    }*/

    public void paste(TextView txtNotes, Context context) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.getText() != null) {
                // txtNotes.getText().insert(txtNotes.getSelectionStart(), clipboard.getText());

            }
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            if (item.getText() != null) {
                // txtNotes.getText().insert(txtNotes.getSelectionStart(), item.getText());
            }
        }
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isValidMobileNumber(String phoneNumber) {

        if (phoneNumber.matches("123456789") || phoneNumber.matches("1234567890")
                || phoneNumber.matches("0123456789") || phoneNumber.matches("0000000000")
                || phoneNumber.matches("1111111111") || phoneNumber.matches("2222222222")
                || phoneNumber.matches("3333333333") || phoneNumber.matches("4444444444")
                || phoneNumber.matches("5555555555") || phoneNumber.matches("6666666666")
                || phoneNumber.matches("7777777777") || phoneNumber.matches("8888888888")
                || phoneNumber.matches("9999999999") || phoneNumber.matches("0000000000")) {
            return false;
        }
        return true;
    }

    public static Bitmap getBitmapFromByteArray(byte[] bytes) {

        return bytes == null ? null : BitmapFactory.decodeStream(new ByteArrayInputStream(bytes));
    }

    public static void bitmapToDrawable(Bitmap bitmap, TextView textView) {
        BitmapDrawable d = new BitmapDrawable(bitmap);
        d.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        textView.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);


    }


    public static Activity getPreviousActivity() {
        return previousActivity;
    }

    public static void setPreviousActivity(Activity activity) {
        previousActivity = activity;
    }


    /*public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(Constants.ENCRYPTION_KEY.getBytes(), "AES");
    }*/

    public static byte[] encryptMsg(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
/* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

    /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }


    public static String getTwoDecimalRoundValueString(double value) {
//        return new DecimalFormat("#.##").format(value);
        return new DecimalFormat("0.00").format(value);
    }

    public static void removeZeroPrefix(final Context context, final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // android.util.Log.d("beforeTextChanged","No need to enter1 "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // android.util.Log.d("beforeTextChanged","No need to enter 2 "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // android.util.Log.d("beforeTextChanged","No need to enter 3 "+s.toString());
                if (s.toString().startsWith("0")) {
                    //      android.util.Log.d("beforeTextChanged","No need to enter 4 "+s.toString());
                    editText.setText("");
                    Common.customToast(context, "No need to enter 0");
                }
            }
        });

    }

    public static void restrictSpaceInPasswordField(final Context context, final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                android.util.Log.d("beforeTextChanged","No need to enter1 "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                android.util.Log.d("beforeTextChanged","No need to enter 2 "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                android.util.Log.d("beforeTextChanged","Space not allowed. "+s.toString());
                if (s.toString().contains(" ")) {
                    //      android.util.Log.d("beforeTextChanged","No need to enter 4 "+s.toString());
                    editText.setText(s.toString().replaceAll("\\s",""));
                    editText.setSelection(s.toString().replaceAll("\\s","").length());
                    Common.customToast(context, "Space not allowed.");
                }
            }
        });

    }


    public static String ignoreZeroPrefix(final EditText editText) {
        String text = editText.getText().toString().trim();
        if (NumberUtils.isDigits(text)) {
            if (text.startsWith("0")){
                return text.substring(1);
            }else {
                return text;
            }


        }else {
             return text;
        }

    }



    public static Bitmap getBitmapFromView(View view)
    {

        Log.i("? - view.getWidth() : " + view.getWidth());
        Log.i("? - view.getHeight() : " + view.getHeight());

        Log.i("? - view.getMeasuredWidth() : " + view.getMeasuredWidth());
        Log.i("? - view.getMeasuredHeight() : " + view.getMeasuredHeight());

        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.draw(c);

        return b;


    }





    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }



    public static void setupUI(final Activity activity, View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(activity, innerView);
            }
        }
    }





    public static TypedFile getTypedFile(File file) {
        Log.i("file : " + file);
        return (file != null) ? new TypedFile("multipart/form-data", file) : null;
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    /*public static int getColorFromResource(int resourceId) {
        return WAYALERTS.getInstance().getResources().getColor(resourceId);
    }*/


    public static void setViewsEnableStatus(boolean status, View... views)
    {


        Log.i("views.length : "+views.length);

        for (View view : views)
        {

            Log.i("view : "+view);
            Log.i("view.toString() : "+view.toString());
            view.setFocusableInTouchMode(status);
            view.setFocusable(status);
            view.setEnabled(status);


        }


    }

    public static void setViewsEnableStatuss(boolean status, View... views)
    {


        Log.i("views.length : "+views.length);

        for (View view : views)
        {

            Log.i("view : "+view);
            Log.i("view.toString() : "+view.toString());
//            view.setFocusableInTouchMode(status);
//            view.setFocusable(status);
            view.setEnabled(status);


        }


    }





    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
       /* int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
*/


        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
        return resizedBitmap;
    }


    /*public static void setCustomTabView(Context context, int selectedTabPosition, int tabsCount,List<TabsViews> tabsViewses){
        for (int i=0;i<tabsCount;i++) {

            TabsData tabsData = TabsData.getData(i);
            View view = tabsViewses.get(i).getView();
            TextView textView =  tabsViewses.get(i).getTextView();
            ImageView imageView =  tabsViewses.get(i).getImageView();

            textView.setText(tabsData.getReferenceName());

            if (selectedTabPosition==tabsData.getReferenceId()) {
                textView.setTextColor(context.getResources().getColor(R.color.white));
                imageView.setImageDrawable(context.getResources().getDrawable(tabsData.getVisitTypes()));

            }else{
                textView.setTextColor(context.getResources().getColor(R.color.blue));
                imageView.setImageDrawable(context.getResources().getDrawable(tabsData.getDrawableDeSelected()));
            }
            //  tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.arw_down, 0, 0);
            //   tabLayout.getTabAt(i).setCustomView(view);


        }

    }*/


/*
    public static List<TabsViews> setCustomTabView(Context context, int selectedTabPosition, int tabsCount, TabLayout tabLayout){
        List<TabsViews> list=new ArrayList<TabsViews>();
        for (int i=0;i<tabsCount;i++) {
            TabsViews tabsViews=new TabsViews();
            TabsData tabsData = TabsData.getData(i);
            View view = (View) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            TextView textView = (TextView) view.findViewById(R.id.tab_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.tab_image);

            textView.setText(tabsData.getReferenceName());

            if (selectedTabPosition==tabsData.getReferenceId()) {
                textView.setTextColor(context.getResources().getColor(R.color.white));
                imageView.setImageDrawable(context.getResources().getDrawable(tabsData.getVisitTypes()));

            }else{
                textView.setTextColor(context.getResources().getColor(R.color.blue));
                imageView.setImageDrawable(context.getResources().getDrawable(tabsData.getDrawableDeSelected()));
            }
            //  tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.arw_down, 0, 0);
            tabLayout.getTabAt(i).setCustomView(view);

            tabsViews.setView(view);
            tabsViews.setTextView(textView);
            tabsViews.setImageView(imageView);

            list.add(tabsData.getReferenceId(),tabsViews);
        }

        return list;
    }
*/

/*
    public static void myCurrentLocationCamera(Location location, GoogleMap googleMap) {
        android.util.Log.d("lastknownLocation",location.toString());
        if (location != null&&googleMap!=null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            android.util.Log.d("lastknownLocation",location.toString()+" "+googleMap.getCameraPosition());

        }
    }
*/

    /*public static void myCurrentLocationCamera(LatLng latLng, GoogleMap googleMap) {
        android.util.Log.d("lastknownLocation",latLng.toString());
        if (latLng != null&&googleMap!=null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            android.util.Log.d("lastknownLocation",latLng.toString()+" "+googleMap.getCameraPosition());

        }
    }*/
    public static Fragment getActiveFragment(FragmentActivity fragmentActivity) {
        if (fragmentActivity.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = fragmentActivity.getSupportFragmentManager().getBackStackEntryAt(fragmentActivity.getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return fragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
    }



    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public  static String getCompleteURL(int role ,String baseURLwith ,String userId,String team){
        char bb = baseURLwith.charAt(baseURLwith.length() - 1);
        String append="";
        if (!String.valueOf(bb).equalsIgnoreCase("?")){
            append="&";
        }

        String url;

        if (role== Constants.Roles.ROLE_7){
            url=baseURLwith+append+"user_id="+userId;
        }else{
            url=baseURLwith+append+"team="+team;
        }

        android.util.Log.d("URL: ",url);
        return url;
    }

    public static String getCompleteURLEVM(String baseURLwith, String userId, String team) {
        StringBuilder sb=new StringBuilder();

        sb.append(baseURLwith).append("&user_id="+userId);
        String[] teamArray = team.split(",");

        if (teamArray.length > 1) {
            sb.append("&team=" + team);

        }


        android.util.Log.d("URL: ", sb.toString());
        return sb.toString();
    }


    public static String getPlannerTitleById(String ids){
        if (ids.equalsIgnoreCase(null) || ids.equalsIgnoreCase("")){

            return "0";
        }
        String[] purpose_visitid = ids.split(",");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < purpose_visitid.length; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(PlannerEnum.getPNTitle(Integer.parseInt(purpose_visitid[i])).getTitle());
        }
        return stringBuilder.toString();
    }


    public static String getURLToUpdateTableByTBName(String tableName){


        return null;
    }

    public static Dialog showpopup(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_progress);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return dialog;



    }

    public static void saveDBFile(Context context){
        File f=new File("/data/data/com.nslbeejtantra.app/databases/NSL");
        FileInputStream fis=null;
        FileOutputStream fos=null;

        try
        {
            fis=new FileInputStream(f);
            fos=new FileOutputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "NSL"
                    + File.separator + "NSL");
            while(true)
            {
                int i=fis.read();
                if(i!=-1)
                {fos.write(i);}
                else
                {break;}
            }
            fos.flush();
            Toast.makeText(context, "DB dump OK", Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, "DB dump ERROR", Toast.LENGTH_LONG).show();
        }
        finally
        {
            try
            {
                fos.close();
                fis.close();
            }
            catch(IOException ioe)
            {}
        }
    }

    public static boolean isListContaintsValue(List<VersionControlVo> versionControlVoList,String value) {
        for (VersionControlVo versionControlVo: versionControlVoList){
            if (versionControlVo.tableName.equals(value)){
                return true;
            }
        }



        return false;
    }

    public static String getDeviceInfo(){

        String deviceInfo = "SERIAL: " + Build.SERIAL + "," +
                "MODEL: " + Build.MODEL + "," +
                "ID: " + Build.ID + "," +
                "Manufacture: " + Build.MANUFACTURER + "," +
                "Brand: " + Build.BRAND + "," +
            //    "Type: " + Build.TYPE + "," +
            //    "User: " + Build.USER + "," +
           //     "BASE: " + Build.VERSION_CODES.BASE + "," +
            //    "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "," +
                "SDK:  " + Build.VERSION.SDK + "," +
              //  "BOARD: " + Build.BOARD + "," +
               // "BRAND: " + Build.BRAND + "," +
               // "HOST: " + Build.HOST + "," +
                "Version Code: " + Build.VERSION.RELEASE;

        return deviceInfo;
    }

    public static String getPackageName(Context context){

        final PackageManager pm = context.getPackageManager();
//get a list of installed apps.

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        StringBuilder commaSepValueBuilder = new StringBuilder();

        for (ApplicationInfo packageInfo : packages) {

            android.util.Log.d("Installed package :" , packageInfo.packageName);
            // Log.d( "Source dir : " , packageInfo.sourceDir);
            // Log.d( "Launch Activity :" , String.valueOf(pm.getLaunchIntentForPackage(packageInfo.packageName)));
            if ( commaSepValueBuilder.toString().length()>0){
                commaSepValueBuilder.append(",");
            }
            commaSepValueBuilder.append(packageInfo.packageName);

        }
        return commaSepValueBuilder.toString();
    }


    public static Handler disableClickEvent(final View v,boolean needEnable) {
        Handler handler=null;
        v.setEnabled(false);
        v.setFocusable(false);
        v.setClickable(false);
        if (needEnable) {
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    v.setEnabled(true);
                    v.setFocusable(true);
                    v.setClickable(true);
                }
            }, 8000);
        }

        return handler;
    }
    public static Handler disableClickEvent(final View v) {
        return  disableClickEvent(v,false);
    }

    public static void disableClickEvent(final View v,Handler handler) {
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
        else
            disableClickEvent(v);

    }

}

