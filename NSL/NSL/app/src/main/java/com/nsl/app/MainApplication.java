package com.nsl.app;

import android.app.Application;

/**
 * Created by Praveen on 12/7/2016.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//set Custom Typeface

        FontsOverride.setDefaultFont(this, "", "fonts/SEGOEWP-LIGHT.TTF");
    }
}
