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
public class CustomTextViewBold extends TextView {
    public CustomTextViewBold(Context context) {
        super(context);
        init();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), Constants.CUSTOM_FONT_PATH_BOLD);

        setTypeface(externalFont);
//        setAllCaps(true);

    }


}
