package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

final class BitmapCroppingWorkerTask extends AsyncTask<Void, Void, BitmapCroppingWorkerTask.Result> {
    private final int mAspectRatioX;
    private final int mAspectRatioY;
    private final Bitmap mBitmap;
    @SuppressLint("StaticFieldLeak")
    private final Context mContext;
    private final WeakReference<CropImageView> mCropImageViewReference;
    private final float[] mCropPoints;
    private final int mDegreesRotated;
    private final boolean mFixAspectRatio;
    private final boolean mFlipHorizontally;
    private final boolean mFlipVertically;
    private final int mOrgHeight;
    private final int mOrgWidth;
    private final int mReqHeight;
    private final CropImageView.RequestSizeOptions mReqSizeOptions;
    private final int mReqWidth;
    private final Bitmap.CompressFormat mSaveCompressFormat;
    private final int mSaveCompressQuality;
    private final Uri mSaveUri;
    private final Uri mUri;

    BitmapCroppingWorkerTask(CropImageView cropImageView, Bitmap bitmap, float[] fArr, int i, boolean z, int i2, int i3, int i4, int i5, boolean z2, boolean z3, CropImageView.RequestSizeOptions requestSizeOptions, Uri uri, Bitmap.CompressFormat compressFormat, int i6) {
        mCropImageViewReference = new WeakReference<>(cropImageView);
        mContext = cropImageView.getContext();
        mBitmap = bitmap;
        mCropPoints = fArr;
        mUri = null;
        mDegreesRotated = i;
        mFixAspectRatio = z;
        mAspectRatioX = i2;
        mAspectRatioY = i3;
        mReqWidth = i4;
        mReqHeight = i5;
        mFlipHorizontally = z2;
        mFlipVertically = z3;
        mReqSizeOptions = requestSizeOptions;
        mSaveUri = uri;
        mSaveCompressFormat = compressFormat;
        mSaveCompressQuality = i6;
        mOrgWidth = 0;
        mOrgHeight = 0;
    }

    BitmapCroppingWorkerTask(CropImageView cropImageView, Uri uri, float[] fArr, int i, int i2, int i3, boolean z, int i4, int i5, int i6, int i7, boolean z2, boolean z3, CropImageView.RequestSizeOptions requestSizeOptions, Uri uri2, Bitmap.CompressFormat compressFormat, int i8) {
        mCropImageViewReference = new WeakReference<>(cropImageView);
        mContext = cropImageView.getContext();
        mUri = uri;
        mCropPoints = fArr;
        mDegreesRotated = i;
        mFixAspectRatio = z;
        mAspectRatioX = i4;
        mAspectRatioY = i5;
        mOrgWidth = i2;
        mOrgHeight = i3;
        mReqWidth = i6;
        mReqHeight = i7;
        mFlipHorizontally = z2;
        mFlipVertically = z3;
        mReqSizeOptions = requestSizeOptions;
        mSaveUri = uri2;
        mSaveCompressFormat = compressFormat;
        mSaveCompressQuality = i8;
        mBitmap = null;
    }

    public Uri getUri() {
        return mUri;
    }


    public Result doInBackground(Void... voidArr) {
        BitmapUtils.BitmapSampled bitmapSampled;
        boolean z = true;
        try {
            if (isCancelled()) {
                return null;
            }
            Uri uri = mUri;
            if (uri != null) {
                bitmapSampled = BitmapUtils.cropBitmap(mContext, uri, mCropPoints, mDegreesRotated, mOrgWidth, mOrgHeight, mFixAspectRatio, mAspectRatioX, mAspectRatioY, mReqWidth, mReqHeight, mFlipHorizontally, mFlipVertically);
            } else {
                Bitmap bitmap = mBitmap;
                if (bitmap == null) {
                    return new Result((Bitmap) null, 1);
                }
                bitmapSampled = BitmapUtils.cropBitmapObjectHandleOOM(bitmap, mCropPoints, mDegreesRotated, mFixAspectRatio, mAspectRatioX, mAspectRatioY, mFlipHorizontally, mFlipVertically);
            }
            Bitmap resizeBitmap = BitmapUtils.resizeBitmap(bitmapSampled.bitmap, mReqWidth, mReqHeight, mReqSizeOptions);
            Uri uri2 = mSaveUri;
            if (uri2 == null) {
                return new Result(resizeBitmap, bitmapSampled.sampleSize);
            }
            BitmapUtils.writeBitmapToUri(mContext, resizeBitmap, uri2, mSaveCompressFormat, mSaveCompressQuality);
            resizeBitmap.recycle();
            return new Result(mSaveUri, bitmapSampled.sampleSize);
        } catch (Exception e) {
            if (mSaveUri == null) {
                z = false;
            }
            return new Result(e, z);
        }
    }


    public void onPostExecute(Result result) {
        CropImageView cropImageView;
        if (result != null) {
            boolean z = false;
            if (!isCancelled() && (cropImageView = mCropImageViewReference.get()) != null) {
                z = true;
                cropImageView.onImageCroppingAsyncComplete(result);
            }
            if (!z && result.bitmap != null) {
                result.bitmap.recycle();
            }
        }
    }

    static final class Result {
        public final Bitmap bitmap;
        final Exception error;
        final boolean isSave;
        final int sampleSize;
        public final Uri uri;

        Result(Bitmap bitmap2, int i) {
            bitmap = bitmap2;
            uri = null;
            error = null;
            isSave = false;
            sampleSize = i;
        }

        Result(Uri uri2, int i) {
            bitmap = null;
            uri = uri2;
            error = null;
            isSave = true;
            sampleSize = i;
        }

        Result(Exception exc, boolean z) {
            bitmap = null;
            uri = null;
            error = exc;
            isSave = z;
            sampleSize = 1;
        }
    }
}
