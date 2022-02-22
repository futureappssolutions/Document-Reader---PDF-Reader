package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

import java.lang.ref.WeakReference;

final class BitmapLoadingWorkerTask extends AsyncTask<Void, Void, BitmapLoadingWorkerTask.Result> {
    @SuppressLint("StaticFieldLeak")
    private final Context mContext;
    private final WeakReference<CropImageView> mCropImageViewReference;
    private final int mHeight;
    private final Uri mUri;
    private final int mWidth;

    public BitmapLoadingWorkerTask(CropImageView cropImageView, Uri uri) {
        mUri = uri;
        mCropImageViewReference = new WeakReference<>(cropImageView);
        mContext = cropImageView.getContext();
        DisplayMetrics displayMetrics = cropImageView.getResources().getDisplayMetrics();
        double d = displayMetrics.density > 1.0f ? (double) (1.0f / displayMetrics.density) : 1.0d;
        double d2 = displayMetrics.widthPixels;
        Double.isNaN(d2);
        mWidth = (int) (d2 * d);
        double d3 = displayMetrics.heightPixels;
        Double.isNaN(d3);
        mHeight = (int) (d3 * d);
    }

    public Uri getUri() {
        return mUri;
    }

    public Result doInBackground(Void... voidArr) {
        try {
            if (isCancelled()) {
                return null;
            }
            BitmapUtils.BitmapSampled decodeSampledBitmap = BitmapUtils.decodeSampledBitmap(mContext, mUri, mWidth, mHeight);
            if (isCancelled()) {
                return null;
            }
            BitmapUtils.RotateBitmapResult rotateBitmapByExif = BitmapUtils.rotateBitmapByExif(decodeSampledBitmap.bitmap, mContext, mUri);
            return new Result(mUri, rotateBitmapByExif.bitmap, decodeSampledBitmap.sampleSize, rotateBitmapByExif.degrees);
        } catch (Exception e) {
            return new Result(mUri, e);
        }
    }

    public void onPostExecute(Result result) {
        CropImageView cropImageView;
        if (result != null) {
            boolean z = false;
            if (!isCancelled() && (cropImageView = mCropImageViewReference.get()) != null) {
                z = true;
                cropImageView.onSetImageUriAsyncComplete(result);
            }
            if (!z && result.bitmap != null) {
                result.bitmap.recycle();
            }
        }
    }

    public static final class Result {
        public final Bitmap bitmap;
        public final int degreesRotated;
        public final Exception error;
        public final int loadSampleSize;
        public final Uri uri;

        Result(Uri uri2, Bitmap bitmap2, int i, int i2) {
            uri = uri2;
            bitmap = bitmap2;
            loadSampleSize = i;
            degreesRotated = i2;
            error = null;
        }

        Result(Uri uri2, Exception exc) {
            uri = uri2;
            bitmap = null;
            loadSampleSize = 0;
            degreesRotated = 0;
            error = exc;
        }
    }
}
