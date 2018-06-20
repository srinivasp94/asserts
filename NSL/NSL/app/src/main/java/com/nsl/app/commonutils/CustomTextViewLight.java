package com.nsl.app.commonutils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nsl.app.Constants;


/**
 * Created by Venkateswarlu SKP on 30-08-2016.
 */
public class CustomTextViewLight extends TextView {
    public CustomTextViewLight(Context context) {
        super(context);
        init();
    }

    public CustomTextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTextViewLight(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), Constants.CUSTOM_FONT_PATH_LIGHT);

        setTypeface(externalFont);
//        setAllCaps(true);

    }


}
