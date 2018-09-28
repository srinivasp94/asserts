package com.example.pegasys.rapmedixuser.activity.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;


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
        Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");

        setTypeface(externalFont);
//        setAllCaps(true);

    }


}
