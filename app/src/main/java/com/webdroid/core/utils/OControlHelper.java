package com.webdroid.core.utils;

import android.graphics.Typeface;

public class OControlHelper {
    public static final String TAG = OControlHelper.class.getSimpleName();

    public static Typeface boldFont() {
        return Typeface.create("sans-serif-condensed", 0);
    }

    public static Typeface lightFont() {
        return Typeface.create("sans-serif-light", 0);
    }
}