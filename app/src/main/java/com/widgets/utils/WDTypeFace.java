package com.widgets.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.widgets.WDTextView;

public class WDTypeFace {

    public static void setUbuntuRegular(WDTextView view) {
        Context context = view.getContext();
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-R.ttf");
        view.setTypeface(tf, Typeface.NORMAL);
    }

}
