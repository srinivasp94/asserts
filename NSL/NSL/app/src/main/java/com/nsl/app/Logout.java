package com.nsl.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 10/19/2015.
 */
public class Logout extends DialogFragment {
    Context context;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

       // sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        return new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation")
                .setMessage("Do you want to Logout ?")
                .setIcon(R.drawable.ic_exit_to_app_black_24dp)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("userId", "");
                        editor.putInt(Constants.SharedPrefrancesKey.ROLE, 0);
                        editor.commit();
                        dialog.dismiss();
                        //getActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

    }
}