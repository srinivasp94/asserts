package com.example.pegasys.rapmedixuser.activity.utils;

import android.widget.EditText;

import java.util.regex.Pattern;

public class Validation {

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "[0-9]{10,13}";
    private static final String PIN_REGEX = "[0-9]{6}";
    private static final String TEXT_REGEX = "[A-Za-z]";

    // Error Messages
    private static final String REQUIRED_MSG = "This Field is Required";
    private static final String EMAIL_MSG = "Invalid Email";
    private static final String PHONE_MSG = "invalid Mobile Number";
    private static final String PIN_MSG = "invalid PIN Number";
    private static final String PASS_MSG = "Password Does not Match";


    // email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    //text validation
    public static boolean isTextEntered(EditText editText, boolean required) {
        return isValid(editText, TEXT_REGEX, REQUIRED_MSG, required);
    }

    // phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    public static boolean isPincode(EditText editText, boolean required) {
        return isValid(editText, PIN_REGEX, PIN_MSG, required);
    }


    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {
        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasText(editText)) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }
        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    public static boolean isequal(EditText pass, EditText confirmpass) {
        String text = pass.getText().toString().trim();
        String text1 = confirmpass.getText().toString().trim();
        pass.setError(null);
        confirmpass.setError(null);
        if (!text.equals(text1)) {
            confirmpass.setError(PASS_MSG);
            return false;
        }
        return true;

    }
}