package com.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.widgets.utils.WDTypeFace;

public class WDTextView extends TextView {
    public WDTextView(Context context) {
        super(context);
    }

    public WDTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WDTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WDTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        WDTypeFace.setUbuntuRegular(this);
    }

}
