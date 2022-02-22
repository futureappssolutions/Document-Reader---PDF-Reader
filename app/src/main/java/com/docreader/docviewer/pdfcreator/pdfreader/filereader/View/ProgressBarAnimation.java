package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class ProgressBarAnimation extends Animation {
    private final float mFrom;
    private final ProgressBar mProgressBar;
    private final float mTo;

    public ProgressBarAnimation(ProgressBar progressBar, float f, float f2) {
        this.mProgressBar = progressBar;
        this.mFrom = f;
        this.mTo = f2;
    }


    public void applyTransformation(float f, Transformation transformation) {
        super.applyTransformation(f, transformation);
        float f2 = this.mFrom;
        this.mProgressBar.setProgress((int) (f2 + ((this.mTo - f2) * f)));
    }
}
