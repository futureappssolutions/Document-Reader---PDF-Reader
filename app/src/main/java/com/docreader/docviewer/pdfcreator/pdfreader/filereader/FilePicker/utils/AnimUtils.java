package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;

public class AnimUtils {
    private static Interpolator fastOutSlowIn;

    @SuppressLint("ResourceType")
    public static Interpolator getFastOutSlowInInterpolator(Context context) {
        if (fastOutSlowIn == null) {
            fastOutSlowIn = AnimationUtils.loadInterpolator(context, 17563661);
        }
        return fastOutSlowIn;
    }

    public static void marqueeAfterDelay(int i, TextView textView) {
        new Handler().postDelayed(() -> textView.setSelected(true), i);
    }
}
