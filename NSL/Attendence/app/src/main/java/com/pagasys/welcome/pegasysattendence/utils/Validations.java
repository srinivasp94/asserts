package com.pagasys.welcome.pegasysattendence.utils;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by pegasys on 10/5/2017.
 */

public class Validations {
    Context context;
    String textdata;

    //requred
    public Validations() {
    }

    public Validations(Context context) {
        this.context = context;
        this.textdata = textdata;

    }

    public boolean Is_Valid_Person_Name(EditText edt) throws NumberFormatException {

        textdata = edt.getText().toString();
        if (textdata.length() <= 0) {
            edt.setError("Accept Alphabets Only.");
            textdata = null;
            return false;
        } else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
            edt.setError("Accept Alphabets Only.");
            textdata = null;
            return false;
        } else {
            textdata = edt.getText().toString();

        }
        return true;

    }

    public void Is_valid_phoneNumber(EditText edtxt) throws Exception {
        textdata = edtxt.getText().toString();
        if (textdata.length() <= 0 && textdata.length() > 11) {
            edtxt.setError("Accept Phone Number Only");
            textdata = null;
        } else if (textdata.matches("[0-9]+")) {
            edtxt.setError("Accept Phone Number Only");
            textdata = null;
        } else {
            textdata = edtxt.getText().toString();
        }
    }

    public boolean Is_valid_password(EditText edtxt) {
        textdata = edtxt.getText().toString();
        if (textdata.length() <= 0 ) {
            edtxt.setError("Field Should not empty");
            textdata = null;
            return false;
        } else {
            textdata = edtxt.getText().toString();

        }
        return true;
    }

}
