package com.fitpass.libfitpass.fontcomponent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;



@SuppressLint("AppCompatCustomView")
public class FontAwesome extends TextView {
    public FontAwesome(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public FontAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public FontAwesome(Context context) {
        super(context);
        init();
    }
    private void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fitpass_font.ttf");
        setTypeface(tf);
    }
}