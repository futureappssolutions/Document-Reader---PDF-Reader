package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

final class CropImageAnimation extends Animation implements Animation.AnimationListener {
    private final float[] mAnimMatrix = new float[9];
    private final float[] mAnimPoints = new float[8];
    private final float[] mEndBoundPoints = new float[8];
    private final float[] mEndImageMatrix = new float[9];
    private final float[] mStartBoundPoints = new float[8];
    private final float[] mStartImageMatrix = new float[9];
    private final RectF mEndCropWindowRect = new RectF();
    private final RectF mStartCropWindowRect = new RectF();
    private final RectF mAnimRect = new RectF();
    private final CropOverlayView mCropOverlayView;
    private final ImageView mImageView;

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }

    public CropImageAnimation(ImageView imageView, CropOverlayView cropOverlayView) {
        mImageView = imageView;
        mCropOverlayView = cropOverlayView;
        setDuration(300);
        setFillAfter(true);
        setInterpolator(new AccelerateDecelerateInterpolator());
        setAnimationListener(this);
    }

    public void setStartState(float[] fArr, Matrix matrix) {
        reset();
        System.arraycopy(fArr, 0, mStartBoundPoints, 0, 8);
        mStartCropWindowRect.set(mCropOverlayView.getCropWindowRect());
        matrix.getValues(mStartImageMatrix);
    }

    public void setEndState(float[] fArr, Matrix matrix) {
        System.arraycopy(fArr, 0, mEndBoundPoints, 0, 8);
        mEndCropWindowRect.set(mCropOverlayView.getCropWindowRect());
        matrix.getValues(mEndImageMatrix);
    }

    public void applyTransformation(float f, Transformation transformation) {
        float[] fArr;
        mAnimRect.left = mStartCropWindowRect.left + ((mEndCropWindowRect.left - mStartCropWindowRect.left) * f);
        mAnimRect.top = mStartCropWindowRect.top + ((mEndCropWindowRect.top - mStartCropWindowRect.top) * f);
        mAnimRect.right = mStartCropWindowRect.right + ((mEndCropWindowRect.right - mStartCropWindowRect.right) * f);
        mAnimRect.bottom = mStartCropWindowRect.bottom + ((mEndCropWindowRect.bottom - mStartCropWindowRect.bottom) * f);
        mCropOverlayView.setCropWindowRect(mAnimRect);
        int i = 0;
        int i2 = 0;
        while (true) {
            fArr = mAnimPoints;
            if (i2 >= fArr.length) {
                break;
            }
            float[] fArr2 = mStartBoundPoints;
            fArr[i2] = fArr2[i2] + ((mEndBoundPoints[i2] - fArr2[i2]) * f);
            i2++;
        }
        mCropOverlayView.setBounds(fArr, mImageView.getWidth(), mImageView.getHeight());
        while (true) {
            float[] fArr3 = mAnimMatrix;
            if (i < fArr3.length) {
                float[] fArr4 = mStartImageMatrix;
                fArr3[i] = fArr4[i] + ((mEndImageMatrix[i] - fArr4[i]) * f);
                i++;
            } else {
                Matrix imageMatrix = mImageView.getImageMatrix();
                imageMatrix.setValues(mAnimMatrix);
                mImageView.setImageMatrix(imageMatrix);
                mImageView.invalidate();
                mCropOverlayView.invalidate();
                return;
            }
        }
    }

    public void onAnimationEnd(Animation animation) {
        mImageView.clearAnimation();
    }
}
