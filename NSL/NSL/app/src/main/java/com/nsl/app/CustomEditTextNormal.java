package com.nsl.app;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by admin on 2/15/2017.
 */

public class CustomEditTextNormal extends EditText {


    private Context context;
    private AttributeSet attrs;
    private int defStyle;

    public CustomEditTextNormal(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomEditTextNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }

    public CustomEditTextNormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context  = context;
        this.attrs    = attrs;
        this.defStyle = defStyle;
        init();
    }

    private void init() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/SEGOEWP.TTF");
        this.setTypeface(font);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SEGOEWP.TTF");
        super.setTypeface(tf, style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SEGOEWP.TTF");
        super.setTypeface(tf);
    }
}
