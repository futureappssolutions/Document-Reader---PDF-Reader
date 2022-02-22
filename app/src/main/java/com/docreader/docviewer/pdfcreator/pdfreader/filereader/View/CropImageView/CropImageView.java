package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.exifinterface.media.ExifInterface;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class CropImageView extends FrameLayout {
    private boolean mSaveBitmapToInstanceState;
    private boolean mAutoZoomEnabled;
    private boolean mShowCropOverlay;
    private boolean mShowProgressBar;
    private boolean mSizeChanged;
    private boolean mFlipHorizontally;
    private boolean mFlipVertically;
    private final float[] mScaleImagePoints = new float[8];
    private final float[] mImagePoints = new float[8];
    private float mZoom;
    private float mZoomOffsetX;
    private float mZoomOffsetY;
    private int mDegreesRotated;
    private int mImageResource;
    private int mInitialDegreesRotated;
    private int mLayoutHeight;
    private int mLayoutWidth;
    private int mLoadedSampleSize;
    private int mMaxZoom;
    private int mRestoreDegreesRotated;
    private final CropOverlayView mCropOverlayView;
    private final Matrix mImageInverseMatrix = new Matrix();
    private final Matrix mImageMatrix = new Matrix();
    private final ImageView mImageView;
    private final ProgressBar mProgressBar;
    private CropImageAnimation mAnimation;
    private WeakReference<BitmapCroppingWorkerTask> mBitmapCroppingWorkerTask;
    private WeakReference<BitmapLoadingWorkerTask> mBitmapLoadingWorkerTask;
    private RectF mRestoreCropWindowRect;
    private Uri mSaveInstanceStateBitmapUri;
    private Uri mLoadedImageUri;
    private ScaleType mScaleType;
    private Bitmap mBitmap;
    public OnCropImageCompleteListener mOnCropImageCompleteListener;
    public OnSetCropOverlayReleasedListener mOnCropOverlayReleasedListener;
    public OnSetCropOverlayMovedListener mOnSetCropOverlayMovedListener;
    public OnSetCropWindowChangeListener mOnSetCropWindowChangeListener;
    public OnSetImageUriCompleteListener mOnSetImageUriCompleteListener;

    public enum CropShape {
        RECTANGLE,
        OVAL
    }

    public enum Guidelines {
        OFF,
        ON_TOUCH,
        ON
    }

    public interface OnCropImageCompleteListener {
        void onCropImageComplete(CropImageView cropImageView, CropResult cropResult);
    }

    public interface OnSetCropOverlayMovedListener {
        void onCropOverlayMoved(Rect rect);
    }

    public interface OnSetCropOverlayReleasedListener {
        void onCropOverlayReleased(Rect rect);
    }

    public interface OnSetCropWindowChangeListener {
        void onCropWindowChanged();
    }

    public interface OnSetImageUriCompleteListener {
        void onSetImageUriComplete(CropImageView cropImageView, Uri uri, Exception exc);
    }

    public enum RequestSizeOptions {
        NONE,
        SAMPLING,
        RESIZE_INSIDE,
        RESIZE_FIT,
        RESIZE_EXACT
    }

    public enum ScaleType {
        FIT_CENTER,
        CENTER,
        CENTER_CROP,
        CENTER_INSIDE
    }

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        CropImageOptions options = null;
        Intent intent = context instanceof Activity ? ((Activity) context).getIntent() : null;
        if (intent != null) {
            options = intent.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_OPTIONS);
        }

        if (options == null) {

            options = new CropImageOptions();

            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CropImageView, 0, 0);
                try {
                    options.fixAspectRatio = ta.getBoolean(R.styleable.CropImageView_cropFixAspectRatio, options.fixAspectRatio);
                    options.aspectRatioX = ta.getInteger(R.styleable.CropImageView_cropAspectRatioX, options.aspectRatioX);
                    options.aspectRatioY = ta.getInteger(R.styleable.CropImageView_cropAspectRatioY, options.aspectRatioY);
                    options.scaleType = ScaleType.values()[ta.getInt(R.styleable.CropImageView_cropScaleType, options.scaleType.ordinal())];
                    options.autoZoomEnabled = ta.getBoolean(R.styleable.CropImageView_cropAutoZoomEnabled, options.autoZoomEnabled);
                    options.maxZoom = ta.getInteger(R.styleable.CropImageView_cropMaxZoom, options.maxZoom);
                    options.cropShape = CropShape.values()[ta.getInt(R.styleable.CropImageView_cropShape, options.cropShape.ordinal())];
                    options.guidelines = Guidelines.values()[ta.getInt(R.styleable.CropImageView_cropGuidelines, options.guidelines.ordinal())];
                    options.snapRadius = ta.getDimension(R.styleable.CropImageView_cropSnapRadius, options.snapRadius);
                    options.touchRadius = ta.getDimension(R.styleable.CropImageView_cropTouchRadius, options.touchRadius);
                    options.initialCropWindowPaddingRatio = ta.getFloat(R.styleable.CropImageView_cropInitialCropWindowPaddingRatio, options.initialCropWindowPaddingRatio);
                    options.borderLineThickness = ta.getDimension(R.styleable.CropImageView_cropBorderLineThickness, options.borderLineThickness);
                    options.borderLineColor = ta.getInteger(R.styleable.CropImageView_cropBorderLineColor, options.borderLineColor);
                    options.borderCornerThickness = ta.getDimension(R.styleable.CropImageView_cropBorderCornerThickness, options.borderCornerThickness);
                    options.borderCornerOffset = ta.getDimension(R.styleable.CropImageView_cropBorderCornerOffset, options.borderCornerOffset);
                    options.borderCornerLength = ta.getDimension(R.styleable.CropImageView_cropBorderCornerLength, options.borderCornerLength);
                    options.borderCornerColor = ta.getInteger(R.styleable.CropImageView_cropBorderCornerColor, options.borderCornerColor);
                    options.guidelinesThickness = ta.getDimension(R.styleable.CropImageView_cropGuidelinesThickness, options.guidelinesThickness);
                    options.guidelinesColor = ta.getInteger(R.styleable.CropImageView_cropGuidelinesColor, options.guidelinesColor);
                    options.backgroundColor = ta.getInteger(R.styleable.CropImageView_cropBackgroundColor, options.backgroundColor);
                    options.showCropOverlay = ta.getBoolean(R.styleable.CropImageView_cropShowCropOverlay, mShowCropOverlay);
                    options.showProgressBar = ta.getBoolean(R.styleable.CropImageView_cropShowProgressBar, mShowProgressBar);
                    options.borderCornerThickness = ta.getDimension(R.styleable.CropImageView_cropBorderCornerThickness, options.borderCornerThickness);
                    options.minCropWindowWidth = (int) ta.getDimension(R.styleable.CropImageView_cropMinCropWindowWidth, options.minCropWindowWidth);
                    options.minCropWindowHeight = (int) ta.getDimension(R.styleable.CropImageView_cropMinCropWindowHeight, options.minCropWindowHeight);
                    options.minCropResultWidth = (int) ta.getFloat(R.styleable.CropImageView_cropMinCropResultWidthPX, options.minCropResultWidth);
                    options.minCropResultHeight = (int) ta.getFloat(R.styleable.CropImageView_cropMinCropResultHeightPX, options.minCropResultHeight);
                    options.maxCropResultWidth = (int) ta.getFloat(R.styleable.CropImageView_cropMaxCropResultWidthPX, options.maxCropResultWidth);
                    options.maxCropResultHeight = (int) ta.getFloat(R.styleable.CropImageView_cropMaxCropResultHeightPX, options.maxCropResultHeight);
                } finally {
                    ta.recycle();
                }
            }
        }

        options.validate();

        mScaleType = options.scaleType;
        mAutoZoomEnabled = options.autoZoomEnabled;
        mMaxZoom = options.maxZoom;
        mShowCropOverlay = options.showCropOverlay;
        mShowProgressBar = options.showProgressBar;

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.crop_image_view, this, true);

        mImageView = v.findViewById(R.id.ImageView_image);
        mImageView.setScaleType(ImageView.ScaleType.MATRIX);

        mCropOverlayView = v.findViewById(R.id.CropOverlayView);
        mCropOverlayView.setCropWindowChangeListener(inProgress -> handleCropWindowChanged(inProgress, true));
        mCropOverlayView.setInitialAttributeValues(options);

        mProgressBar = v.findViewById(R.id.CropProgressBar);
        setProgressBarVisibility();
    }

    public ScaleType getScaleType() {
        return mScaleType;
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType != mScaleType) {
            mScaleType = scaleType;
            mZoom = 1.0f;
            mZoomOffsetY = 0.0f;
            mZoomOffsetX = 0.0f;
            mCropOverlayView.resetCropOverlayView();
            requestLayout();
        }
    }

    public CropShape getCropShape() {
        return mCropOverlayView.getCropShape();
    }

    public void setCropShape(CropShape cropShape) {
        mCropOverlayView.setCropShape(cropShape);
    }

    public boolean isAutoZoomEnabled() {
        return mAutoZoomEnabled;
    }

    public void setAutoZoomEnabled(boolean z) {
        if (mAutoZoomEnabled != z) {
            mAutoZoomEnabled = z;
            handleCropWindowChanged(false, false);
            mCropOverlayView.invalidate();
        }
    }

    public void setMultiTouchEnabled(boolean z) {
        if (mCropOverlayView.setMultiTouchEnabled(z)) {
            handleCropWindowChanged(false, false);
            mCropOverlayView.invalidate();
        }
    }

    public int getMaxZoom() {
        return mMaxZoom;
    }

    public void setMaxZoom(int i) {
        if (mMaxZoom != i && i > 0) {
            mMaxZoom = i;
            handleCropWindowChanged(false, false);
            mCropOverlayView.invalidate();
        }
    }

    public void setMinCropResultSize(int i, int i2) {
        mCropOverlayView.setMinCropResultSize(i, i2);
    }

    public void setMaxCropResultSize(int i, int i2) {
        mCropOverlayView.setMaxCropResultSize(i, i2);
    }

    public int getRotatedDegrees() {
        return mDegreesRotated;
    }

    public void setRotatedDegrees(int i) {
        int i2 = mDegreesRotated;
        if (i2 != i) {
            rotateImage(i - i2);
        }
    }

    public boolean isFixAspectRatio() {
        return mCropOverlayView.isFixAspectRatio();
    }

    public void setFixedAspectRatio(boolean z) {
        mCropOverlayView.setFixedAspectRatio(z);
    }

    public boolean isFlippedHorizontally() {
        return mFlipHorizontally;
    }

    public void setFlippedHorizontally(boolean z) {
        if (mFlipHorizontally != z) {
            mFlipHorizontally = z;
            applyImageMatrix((float) getWidth(), (float) getHeight(), true, false);
        }
    }

    public boolean isFlippedVertically() {
        return mFlipVertically;
    }

    public void setFlippedVertically(boolean z) {
        if (mFlipVertically != z) {
            mFlipVertically = z;
            applyImageMatrix((float) getWidth(), (float) getHeight(), true, false);
        }
    }

    public Guidelines getGuidelines() {
        return mCropOverlayView.getGuidelines();
    }

    public void setGuidelines(Guidelines guidelines) {
        mCropOverlayView.setGuidelines(guidelines);
    }

    public Pair<Integer, Integer> getAspectRatio() {
        return new Pair<>(mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY());
    }

    public void setAspectRatio(int i, int i2) {
        mCropOverlayView.setAspectRatioX(i);
        mCropOverlayView.setAspectRatioY(i2);
        setFixedAspectRatio(true);
    }

    public void clearAspectRatio() {
        mCropOverlayView.setAspectRatioX(1);
        mCropOverlayView.setAspectRatioY(1);
        setFixedAspectRatio(false);
    }

    public void setSnapRadius(float f) {
        if (f >= 0.0f) {
            mCropOverlayView.setSnapRadius(f);
        }
    }

    public boolean isShowProgressBar() {
        return mShowProgressBar;
    }

    public void setShowProgressBar(boolean z) {
        if (mShowProgressBar != z) {
            mShowProgressBar = z;
            setProgressBarVisibility();
        }
    }

    public boolean isShowCropOverlay() {
        return mShowCropOverlay;
    }

    public void setShowCropOverlay(boolean z) {
        if (mShowCropOverlay != z) {
            mShowCropOverlay = z;
            setCropOverlayVisibility();
        }
    }

    public boolean isSaveBitmapToInstanceState() {
        return mSaveBitmapToInstanceState;
    }

    public void setSaveBitmapToInstanceState(boolean z) {
        mSaveBitmapToInstanceState = z;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public Uri getImageUri() {
        return mLoadedImageUri;
    }

    public Rect getWholeImageRect() {
        int i = mLoadedSampleSize;
        Bitmap bitmap = mBitmap;
        if (bitmap == null) {
            return null;
        }
        return new Rect(0, 0, bitmap.getWidth() * i, bitmap.getHeight() * i);
    }

    public Rect getCropRect() {
        int i = mLoadedSampleSize;
        Bitmap bitmap = mBitmap;
        if (bitmap == null) {
            return null;
        }
        return BitmapUtils.getRectFromPoints(getCropPoints(), bitmap.getWidth() * i, i * bitmap.getHeight(), mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY());
    }

    public RectF getCropWindowRect() {
        CropOverlayView cropOverlayView = mCropOverlayView;
        if (cropOverlayView == null) {
            return null;
        }
        return cropOverlayView.getCropWindowRect();
    }

    public float[] getCropPoints() {
        RectF cropWindowRect = mCropOverlayView.getCropWindowRect();
        float[] fArr = {cropWindowRect.left, cropWindowRect.top, cropWindowRect.right, cropWindowRect.top, cropWindowRect.right, cropWindowRect.bottom, cropWindowRect.left, cropWindowRect.bottom};
        mImageMatrix.invert(mImageInverseMatrix);
        mImageInverseMatrix.mapPoints(fArr);
        for (int i = 0; i < 8; i++) {
            fArr[i] = fArr[i] * ((float) mLoadedSampleSize);
        }
        return fArr;
    }

    public void setCropRect(Rect rect) {
        mCropOverlayView.setInitialCropWindowRect(rect);
    }

    public void resetCropRect() {
        mZoom = 1.0f;
        mZoomOffsetX = 0.0f;
        mZoomOffsetY = 0.0f;
        mDegreesRotated = mInitialDegreesRotated;
        mFlipHorizontally = false;
        mFlipVertically = false;
        applyImageMatrix((float) getWidth(), (float) getHeight(), false, false);
        mCropOverlayView.resetCropWindowRect();
    }

    public Bitmap getCroppedImage() {
        return getCroppedImage(0, 0, RequestSizeOptions.NONE);
    }

    public Bitmap getCroppedImage(int i, int i2) {
        return getCroppedImage(i, i2, RequestSizeOptions.RESIZE_INSIDE);
    }

    public Bitmap getCroppedImage(int i, int i2, RequestSizeOptions requestSizeOptions) {
        Bitmap bitmap;
        if (mBitmap == null) {
            return null;
        }
        mImageView.clearAnimation();
        int i3 = 0;
        int i4 = requestSizeOptions != RequestSizeOptions.NONE ? i : 0;
        if (requestSizeOptions != RequestSizeOptions.NONE) {
            i3 = i2;
        }
        if (mLoadedImageUri == null || (mLoadedSampleSize <= 1 && requestSizeOptions != RequestSizeOptions.SAMPLING)) {
            bitmap = BitmapUtils.cropBitmapObjectHandleOOM(mBitmap, getCropPoints(), mDegreesRotated, mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY(), mFlipHorizontally, mFlipVertically).bitmap;
        } else {
            bitmap = BitmapUtils.cropBitmap(getContext(), mLoadedImageUri, getCropPoints(), mDegreesRotated, mBitmap.getWidth() * mLoadedSampleSize, mBitmap.getHeight() * mLoadedSampleSize, mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY(), i4, i3, mFlipHorizontally, mFlipVertically).bitmap;
        }
        return BitmapUtils.resizeBitmap(bitmap, i4, i3, requestSizeOptions);
    }

    public void getCroppedImageAsync() {
        getCroppedImageAsync(0, 0, RequestSizeOptions.NONE);
    }

    public void getCroppedImageAsync(int i, int i2) {
        getCroppedImageAsync(i, i2, RequestSizeOptions.RESIZE_INSIDE);
    }

    public void getCroppedImageAsync(int i, int i2, RequestSizeOptions requestSizeOptions) {
        if (mOnCropImageCompleteListener != null) {
            startCropWorkerTask(i, i2, requestSizeOptions, null, null, 0);
            return;
        }
        throw new IllegalArgumentException("mOnCropImageCompleteListener is not set");
    }

    public void saveCroppedImageAsync(Uri uri) {
        saveCroppedImageAsync(uri, Bitmap.CompressFormat.JPEG, 90, 0, 0, RequestSizeOptions.NONE);
    }

    public void saveCroppedImageAsync(Uri uri, Bitmap.CompressFormat compressFormat, int i) {
        saveCroppedImageAsync(uri, compressFormat, i, 0, 0, RequestSizeOptions.NONE);
    }

    public void saveCroppedImageAsync(Uri uri, Bitmap.CompressFormat compressFormat, int i, int i2, int i3) {
        saveCroppedImageAsync(uri, compressFormat, i, i2, i3, RequestSizeOptions.RESIZE_INSIDE);
    }

    public void saveCroppedImageAsync(Uri uri, Bitmap.CompressFormat compressFormat, int i, int i2, int i3, RequestSizeOptions requestSizeOptions) {
        if (mOnCropImageCompleteListener != null) {
            startCropWorkerTask(i2, i3, requestSizeOptions, uri, compressFormat, i);
            return;
        }
        throw new IllegalArgumentException("mOnCropImageCompleteListener is not set");
    }

    public void setOnSetCropOverlayReleasedListener(OnSetCropOverlayReleasedListener onSetCropOverlayReleasedListener) {
        mOnCropOverlayReleasedListener = onSetCropOverlayReleasedListener;
    }

    public void setOnSetCropOverlayMovedListener(OnSetCropOverlayMovedListener onSetCropOverlayMovedListener) {
        mOnSetCropOverlayMovedListener = onSetCropOverlayMovedListener;
    }

    public void setOnCropWindowChangedListener(OnSetCropWindowChangeListener onSetCropWindowChangeListener) {
        mOnSetCropWindowChangeListener = onSetCropWindowChangeListener;
    }

    public void setOnSetImageUriCompleteListener(OnSetImageUriCompleteListener onSetImageUriCompleteListener) {
        mOnSetImageUriCompleteListener = onSetImageUriCompleteListener;
    }

    public void setOnCropImageCompleteListener(OnCropImageCompleteListener onCropImageCompleteListener) {
        mOnCropImageCompleteListener = onCropImageCompleteListener;
    }

    public void setImageBitmap(Bitmap bitmap) {
        mCropOverlayView.setInitialCropWindowRect(null);
        setBitmap(bitmap, 0, null, 1, 0);
    }

    public void setImageBitmap(Bitmap bitmap, ExifInterface exifInterface) {
        int i;
        Bitmap bitmap2;
        if (bitmap == null || exifInterface == null) {
            bitmap2 = bitmap;
            i = 0;
        } else {
            BitmapUtils.RotateBitmapResult rotateBitmapByExif = BitmapUtils.rotateBitmapByExif(bitmap, exifInterface);
            Bitmap bitmap3 = rotateBitmapByExif.bitmap;
            int i2 = rotateBitmapByExif.degrees;
            mInitialDegreesRotated = rotateBitmapByExif.degrees;
            bitmap2 = bitmap3;
            i = i2;
        }
        mCropOverlayView.setInitialCropWindowRect(null);
        setBitmap(bitmap2, 0, null, 1, i);
    }

    public void setImageResource(int i) {
        if (i != 0) {
            mCropOverlayView.setInitialCropWindowRect(null);
            setBitmap(BitmapFactory.decodeResource(getResources(), i), i, null, 1, 0);
        }
    }

    public void setImageUriAsync(Uri uri) {
        if (uri != null) {
            WeakReference<BitmapLoadingWorkerTask> weakReference = mBitmapLoadingWorkerTask;
            BitmapLoadingWorkerTask bitmapLoadingWorkerTask = weakReference != null ? weakReference.get() : null;
            if (bitmapLoadingWorkerTask != null) {
                bitmapLoadingWorkerTask.cancel(true);
            }
            clearImageInt();
            mRestoreCropWindowRect = null;
            mRestoreDegreesRotated = 0;
            mCropOverlayView.setInitialCropWindowRect(null);
            WeakReference<BitmapLoadingWorkerTask> weakReference2 = new WeakReference<>(new BitmapLoadingWorkerTask(this, uri));
            mBitmapLoadingWorkerTask = weakReference2;
            weakReference2.get().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            setProgressBarVisibility();
        }
    }

    public void clearImage() {
        clearImageInt();
        mCropOverlayView.setInitialCropWindowRect(null);
    }

    public void rotateImage(int i) {
        int i2;
        if (mBitmap != null) {
            if (i < 0) {
                i2 = (i % 360) + 360;
            } else {
                i2 = i % 360;
            }
            boolean z = !mCropOverlayView.isFixAspectRatio() && ((i2 > 45 && i2 < 135) || (i2 > 215 && i2 < 305));
            BitmapUtils.RECT.set(mCropOverlayView.getCropWindowRect());
            RectF rectF = BitmapUtils.RECT;
            float height = (z ? rectF.height() : rectF.width()) / 2.0f;
            RectF rectF2 = BitmapUtils.RECT;
            float width = (z ? rectF2.width() : rectF2.height()) / 2.0f;
            if (z) {
                boolean z2 = mFlipHorizontally;
                mFlipHorizontally = mFlipVertically;
                mFlipVertically = z2;
            }
            mImageMatrix.invert(mImageInverseMatrix);
            BitmapUtils.POINTS[0] = BitmapUtils.RECT.centerX();
            BitmapUtils.POINTS[1] = BitmapUtils.RECT.centerY();
            BitmapUtils.POINTS[2] = 0.0f;
            BitmapUtils.POINTS[3] = 0.0f;
            BitmapUtils.POINTS[4] = 1.0f;
            BitmapUtils.POINTS[5] = 0.0f;
            mImageInverseMatrix.mapPoints(BitmapUtils.POINTS);
            mDegreesRotated = (mDegreesRotated + i2) % 360;
            applyImageMatrix((float) getWidth(), (float) getHeight(), true, false);
            mImageMatrix.mapPoints(BitmapUtils.POINTS2, BitmapUtils.POINTS);
            double d = mZoom;
            double sqrt = Math.sqrt(Math.pow(BitmapUtils.POINTS2[4] - BitmapUtils.POINTS2[2], 2.0d) + Math.pow(BitmapUtils.POINTS2[5] - BitmapUtils.POINTS2[3], 2.0d));
            Double.isNaN(d);
            float f = (float) (d / sqrt);
            mZoom = f;
            mZoom = Math.max(f, 1.0f);
            applyImageMatrix((float) getWidth(), (float) getHeight(), true, false);
            mImageMatrix.mapPoints(BitmapUtils.POINTS2, BitmapUtils.POINTS);
            double sqrt2 = Math.sqrt(Math.pow(BitmapUtils.POINTS2[4] - BitmapUtils.POINTS2[2], 2.0d) + Math.pow(BitmapUtils.POINTS2[5] - BitmapUtils.POINTS2[3], 2.0d));
            double d2 = height;
            Double.isNaN(d2);
            float f2 = (float) (d2 * sqrt2);
            double d3 = width;
            Double.isNaN(d3);
            float f3 = (float) (d3 * sqrt2);
            BitmapUtils.RECT.set(BitmapUtils.POINTS2[0] - f2, BitmapUtils.POINTS2[1] - f3, BitmapUtils.POINTS2[0] + f2, BitmapUtils.POINTS2[1] + f3);
            mCropOverlayView.resetCropOverlayView();
            mCropOverlayView.setCropWindowRect(BitmapUtils.RECT);
            applyImageMatrix((float) getWidth(), (float) getHeight(), true, false);
            handleCropWindowChanged(false, false);
            mCropOverlayView.fixCurrentCropWindowRect();
        }
    }

    public void flipImageHorizontally() {
        mFlipHorizontally = !mFlipHorizontally;
        applyImageMatrix((float) getWidth(), (float) getHeight(), true, false);
    }

    public void flipImageVertically() {
        mFlipVertically = !mFlipVertically;
        applyImageMatrix((float) getWidth(), (float) getHeight(), true, false);
    }


    public void onSetImageUriAsyncComplete(BitmapLoadingWorkerTask.Result result) {
        mBitmapLoadingWorkerTask = null;
        setProgressBarVisibility();
        if (result.error == null) {
            mInitialDegreesRotated = result.degreesRotated;
            setBitmap(result.bitmap, 0, result.uri, result.loadSampleSize, result.degreesRotated);
        }
        OnSetImageUriCompleteListener onSetImageUriCompleteListener = mOnSetImageUriCompleteListener;
        if (onSetImageUriCompleteListener != null) {
            onSetImageUriCompleteListener.onSetImageUriComplete(this, result.uri, result.error);
        }
    }


    public void onImageCroppingAsyncComplete(BitmapCroppingWorkerTask.Result result) {
        mBitmapCroppingWorkerTask = null;
        setProgressBarVisibility();
        OnCropImageCompleteListener onCropImageCompleteListener = mOnCropImageCompleteListener;
        if (onCropImageCompleteListener != null) {
            onCropImageCompleteListener.onCropImageComplete(this, new CropResult(mBitmap, mLoadedImageUri, result.bitmap, result.uri, result.error, getCropPoints(), getCropRect(), getWholeImageRect(), getRotatedDegrees(), result.sampleSize));
        }
    }

    private void setBitmap(Bitmap bitmap, int i, Uri uri, int i2, int i3) {
        Bitmap bitmap2 = mBitmap;
        if (bitmap2 == null || !bitmap2.equals(bitmap)) {
            mImageView.clearAnimation();
            clearImageInt();
            mBitmap = bitmap;
            mImageView.setImageBitmap(bitmap);
            mLoadedImageUri = uri;
            mImageResource = i;
            mLoadedSampleSize = i2;
            mDegreesRotated = i3;
            applyImageMatrix((float) getWidth(), (float) getHeight(), true, false);
            CropOverlayView cropOverlayView = mCropOverlayView;
            if (cropOverlayView != null) {
                cropOverlayView.resetCropOverlayView();
                setCropOverlayVisibility();
            }
        }
    }

    private void clearImageInt() {
        Bitmap bitmap = mBitmap;
        if (bitmap != null && (mImageResource > 0 || mLoadedImageUri != null)) {
            bitmap.recycle();
        }
        mBitmap = null;
        mImageResource = 0;
        mLoadedImageUri = null;
        mLoadedSampleSize = 1;
        mDegreesRotated = 0;
        mZoom = 1.0f;
        mZoomOffsetX = 0.0f;
        mZoomOffsetY = 0.0f;
        mImageMatrix.reset();
        mSaveInstanceStateBitmapUri = null;
        mImageView.setImageBitmap(null);
        setCropOverlayVisibility();
    }

    public void startCropWorkerTask(int i, int i2, RequestSizeOptions requestSizeOptions, Uri uri, Bitmap.CompressFormat compressFormat, int i3) {
        if (mBitmap != null) {
            mImageView.clearAnimation();
            BitmapCroppingWorkerTask bitmapCroppingWorkerTask = mBitmapCroppingWorkerTask != null ? mBitmapCroppingWorkerTask.get() : null;
            if (bitmapCroppingWorkerTask != null) {
                bitmapCroppingWorkerTask.cancel(true);
            }
            int i4 = requestSizeOptions != RequestSizeOptions.NONE ? i : 0;
            int i5 = requestSizeOptions != RequestSizeOptions.NONE ? i2 : 0;
            int width = mBitmap.getWidth() * mLoadedSampleSize;
            int height = mBitmap.getHeight();
            int i6 = mLoadedSampleSize;
            int i7 = height * i6;
            if (mLoadedImageUri == null || (i6 <= 1 && requestSizeOptions != RequestSizeOptions.SAMPLING)) {
                BitmapCroppingWorkerTask bitmapCroppingWorkerTask3 = new BitmapCroppingWorkerTask(this, mBitmap, getCropPoints(), mDegreesRotated, mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY(), i4, i5, mFlipHorizontally, mFlipVertically, requestSizeOptions, uri, compressFormat, i3);
                mBitmapCroppingWorkerTask = new WeakReference<>(bitmapCroppingWorkerTask3);
            } else {
                BitmapCroppingWorkerTask bitmapCroppingWorkerTask5 = new BitmapCroppingWorkerTask(this, mLoadedImageUri, getCropPoints(), mDegreesRotated, width, i7, mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY(), i4, i5, mFlipHorizontally, mFlipVertically, requestSizeOptions, uri, compressFormat, i3);
                mBitmapCroppingWorkerTask = new WeakReference<>(bitmapCroppingWorkerTask5);
            }
            mBitmapCroppingWorkerTask.get().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            setProgressBarVisibility();
        }
    }

    public Parcelable onSaveInstanceState() {
        BitmapLoadingWorkerTask bitmapLoadingWorkerTask;
        if (mLoadedImageUri == null && mBitmap == null && mImageResource < 1) {
            return super.onSaveInstanceState();
        }
        Bundle bundle = new Bundle();
        Uri uri = mLoadedImageUri;
        if (mSaveBitmapToInstanceState && uri == null && mImageResource < 1) {
            uri = BitmapUtils.writeTempStateStoreBitmap(getContext(), mBitmap, mSaveInstanceStateBitmapUri);
            mSaveInstanceStateBitmapUri = uri;
        }
        if (!(uri == null || mBitmap == null)) {
            String uuid = UUID.randomUUID().toString();
            BitmapUtils.mStateBitmap = new Pair<>(uuid, new WeakReference(mBitmap));
            bundle.putString("LOADED_IMAGE_STATE_BITMAP_KEY", uuid);
        }
        WeakReference<BitmapLoadingWorkerTask> weakReference = mBitmapLoadingWorkerTask;
        if (!(weakReference == null || (bitmapLoadingWorkerTask = weakReference.get()) == null)) {
            bundle.putParcelable("LOADING_IMAGE_URI", bitmapLoadingWorkerTask.getUri());
        }
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putParcelable("LOADED_IMAGE_URI", uri);
        bundle.putInt("LOADED_IMAGE_RESOURCE", mImageResource);
        bundle.putInt("LOADED_SAMPLE_SIZE", mLoadedSampleSize);
        bundle.putInt("DEGREES_ROTATED", mDegreesRotated);
        bundle.putParcelable("INITIAL_CROP_RECT", mCropOverlayView.getInitialCropWindowRect());
        BitmapUtils.RECT.set(mCropOverlayView.getCropWindowRect());
        mImageMatrix.invert(mImageInverseMatrix);
        mImageInverseMatrix.mapRect(BitmapUtils.RECT);
        bundle.putParcelable("CROP_WINDOW_RECT", BitmapUtils.RECT);
        bundle.putString("CROP_SHAPE", mCropOverlayView.getCropShape().name());
        bundle.putBoolean("CROP_AUTO_ZOOM_ENABLED", mAutoZoomEnabled);
        bundle.putInt("CROP_MAX_ZOOM", mMaxZoom);
        bundle.putBoolean("CROP_FLIP_HORIZONTALLY", mFlipHorizontally);
        bundle.putBoolean("CROP_FLIP_VERTICALLY", mFlipVertically);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            if (mBitmapLoadingWorkerTask == null && mLoadedImageUri == null && mBitmap == null && mImageResource == 0) {
                Uri uri = bundle.getParcelable("LOADED_IMAGE_URI");
                if (uri != null) {
                    String string = bundle.getString("LOADED_IMAGE_STATE_BITMAP_KEY");
                    if (string != null) {
                        Bitmap bitmap = (BitmapUtils.mStateBitmap == null || !BitmapUtils.mStateBitmap.first.equals(string)) ? null : (Bitmap) ((WeakReference) BitmapUtils.mStateBitmap.second).get();
                        BitmapUtils.mStateBitmap = null;
                        if (bitmap != null && !bitmap.isRecycled()) {
                            setBitmap(bitmap, 0, uri, bundle.getInt("LOADED_SAMPLE_SIZE"), 0);
                        }
                    }
                    if (mLoadedImageUri == null) {
                        setImageUriAsync(uri);
                    }
                } else {
                    int i = bundle.getInt("LOADED_IMAGE_RESOURCE");
                    if (i > 0) {
                        setImageResource(i);
                    } else {
                        Uri uri2 = bundle.getParcelable("LOADING_IMAGE_URI");
                        if (uri2 != null) {
                            setImageUriAsync(uri2);
                        }
                    }
                }
                int i2 = bundle.getInt("DEGREES_ROTATED");
                mRestoreDegreesRotated = i2;
                mDegreesRotated = i2;
                Rect rect = bundle.getParcelable("INITIAL_CROP_RECT");
                if (rect != null && (rect.width() > 0 || rect.height() > 0)) {
                    mCropOverlayView.setInitialCropWindowRect(rect);
                }
                RectF rectF = bundle.getParcelable("CROP_WINDOW_RECT");
                if (rectF != null && (rectF.width() > 0.0f || rectF.height() > 0.0f)) {
                    mRestoreCropWindowRect = rectF;
                }
                mCropOverlayView.setCropShape(CropShape.valueOf(bundle.getString("CROP_SHAPE")));
                mAutoZoomEnabled = bundle.getBoolean("CROP_AUTO_ZOOM_ENABLED");
                mMaxZoom = bundle.getInt("CROP_MAX_ZOOM");
                mFlipHorizontally = bundle.getBoolean("CROP_FLIP_HORIZONTALLY");
                mFlipVertically = bundle.getBoolean("CROP_FLIP_VERTICALLY");
            }
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }


    public void onMeasure(int i, int i2) {
        double d;
        double d2;
        int i3;
        int i4;
        super.onMeasure(i, i2);
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        Bitmap bitmap = mBitmap;
        if (bitmap != null) {
            if (size2 == 0) {
                size2 = bitmap.getHeight();
            }
            if (size < mBitmap.getWidth()) {
                double d3 = size;
                double width = mBitmap.getWidth();
                Double.isNaN(d3);
                Double.isNaN(width);
                d = d3 / width;
            } else {
                d = Double.POSITIVE_INFINITY;
            }
            if (size2 < mBitmap.getHeight()) {
                double d4 = size2;
                double height = mBitmap.getHeight();
                Double.isNaN(d4);
                Double.isNaN(height);
                d2 = d4 / height;
            } else {
                d2 = Double.POSITIVE_INFINITY;
            }
            if (d == Double.POSITIVE_INFINITY && d2 == Double.POSITIVE_INFINITY) {
                i4 = mBitmap.getWidth();
                i3 = mBitmap.getHeight();
            } else if (d <= d2) {
                double height2 = mBitmap.getHeight();
                Double.isNaN(height2);
                i3 = (int) (height2 * d);
                i4 = size;
            } else {
                double width2 = mBitmap.getWidth();
                Double.isNaN(width2);
                i4 = (int) (width2 * d2);
                i3 = size2;
            }
            int onMeasureSpec = getOnMeasureSpec(mode, size, i4);
            int onMeasureSpec2 = getOnMeasureSpec(mode2, size2, i3);
            mLayoutWidth = onMeasureSpec;
            mLayoutHeight = onMeasureSpec2;
            setMeasuredDimension(onMeasureSpec, onMeasureSpec2);
            return;
        }
        setMeasuredDimension(size, size2);
    }


    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (mLayoutWidth <= 0 || mLayoutHeight <= 0) {
            updateImageBounds(true);
            return;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = mLayoutWidth;
        layoutParams.height = mLayoutHeight;
        setLayoutParams(layoutParams);
        if (mBitmap != null) {
            float f = (float) (i3 - i);
            float f2 = (float) (i4 - i2);
            applyImageMatrix(f, f2, true, false);
            if (mRestoreCropWindowRect != null) {
                int i5 = mRestoreDegreesRotated;
                if (i5 != mInitialDegreesRotated) {
                    mDegreesRotated = i5;
                    applyImageMatrix(f, f2, true, false);
                }
                mImageMatrix.mapRect(mRestoreCropWindowRect);
                mCropOverlayView.setCropWindowRect(mRestoreCropWindowRect);
                handleCropWindowChanged(false, false);
                mCropOverlayView.fixCurrentCropWindowRect();
                mRestoreCropWindowRect = null;
            } else if (mSizeChanged) {
                mSizeChanged = false;
                handleCropWindowChanged(false, false);
            }
        } else {
            updateImageBounds(true);
        }
    }


    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        mSizeChanged = i3 > 0 && i4 > 0;
    }

    private void handleCropWindowChanged(boolean inProgress, boolean animate) {
        int width = getWidth();
        int height = getHeight();
        if (mBitmap != null && width > 0 && height > 0) {

            RectF cropRect = mCropOverlayView.getCropWindowRect();
            if (inProgress) {
                if (cropRect.left < 0 || cropRect.top < 0 || cropRect.right > width || cropRect.bottom > height) {
                    applyImageMatrix(width, height, false, false);
                }
            } else if (mAutoZoomEnabled || mZoom > 1) {
                float newZoom = 0;
                // keep the cropping window covered area to 50%-65% of zoomed sub-area
                if (mZoom < mMaxZoom && cropRect.width() < width * 0.5f && cropRect.height() < height * 0.5f) {
                    newZoom = Math.min(mMaxZoom, Math.min(width / (cropRect.width() / mZoom / 0.64f), height / (cropRect.height() / mZoom / 0.64f)));
                }
                if (mZoom > 1 && (cropRect.width() > width * 0.65f || cropRect.height() > height * 0.65f)) {
                    newZoom = Math.max(1, Math.min(width / (cropRect.width() / mZoom / 0.51f), height / (cropRect.height() / mZoom / 0.51f)));
                }
                if (!mAutoZoomEnabled) {
                    newZoom = 1;
                }

                if (newZoom > 0 && newZoom != mZoom) {
                    if (animate) {
                        if (mAnimation == null) {
                            // lazy create animation single instance
                            mAnimation = new CropImageAnimation(mImageView, mCropOverlayView);
                        }
                        // set the state for animation to start from
                        mAnimation.setStartState(mImagePoints, mImageMatrix);
                    }

                    updateCropRectByZoomChange(newZoom / mZoom);
                    mZoom = newZoom;

                    applyImageMatrix(width, height, true, animate);
                }
            }
        }
    }

    private void updateCropRectByZoomChange(float zoomChange) {
        RectF cropRect = mCropOverlayView.getCropWindowRect();
        float xCenterOffset = getWidth() / 2 - cropRect.centerX();
        float yCenterOffset = getHeight() / 2 - cropRect.centerY();
        cropRect.offset(xCenterOffset - xCenterOffset * zoomChange, yCenterOffset - yCenterOffset * zoomChange);
        cropRect.inset((cropRect.width() - cropRect.width() * zoomChange) / 2f, (cropRect.height() - cropRect.height() * zoomChange) / 2f);
        mCropOverlayView.setCropWindowRect(cropRect);
    }

    private void applyImageMatrix(float f, float f2, boolean z, boolean z2) {
        float f3;
        if (mBitmap != null) {
            float f4 = 0.0f;
            if (f > 0.0f && f2 > 0.0f) {
                mImageMatrix.invert(mImageInverseMatrix);
                RectF cropWindowRect = mCropOverlayView.getCropWindowRect();
                mImageInverseMatrix.mapRect(cropWindowRect);
                mImageMatrix.reset();
                mImageMatrix.postTranslate((f - ((float) mBitmap.getWidth())) / 2.0f, (f2 - ((float) mBitmap.getHeight())) / 2.0f);
                mapImagePointsByImageMatrix();
                int i = mDegreesRotated;
                if (i > 0) {
                    mImageMatrix.postRotate((float) i, BitmapUtils.getRectCenterX(mImagePoints), BitmapUtils.getRectCenterY(mImagePoints));
                    mapImagePointsByImageMatrix();
                }
                float min = Math.min(f / BitmapUtils.getRectWidth(mImagePoints), f2 / BitmapUtils.getRectHeight(mImagePoints));
                if (mScaleType == ScaleType.FIT_CENTER || ((mScaleType == ScaleType.CENTER_INSIDE && min < 1.0f) || (min > 1.0f && mAutoZoomEnabled))) {
                    mImageMatrix.postScale(min, min, BitmapUtils.getRectCenterX(mImagePoints), BitmapUtils.getRectCenterY(mImagePoints));
                    mapImagePointsByImageMatrix();
                }
                float f5 = mFlipHorizontally ? -mZoom : mZoom;
                float f6 = mFlipVertically ? -mZoom : mZoom;
                mImageMatrix.postScale(f5, f6, BitmapUtils.getRectCenterX(mImagePoints), BitmapUtils.getRectCenterY(mImagePoints));
                mapImagePointsByImageMatrix();
                mImageMatrix.mapRect(cropWindowRect);
                if (z) {
                    if (f > BitmapUtils.getRectWidth(mImagePoints)) {
                        f3 = 0.0f;
                    } else {
                        f3 = Math.max(Math.min((f / 2.0f) - cropWindowRect.centerX(), -BitmapUtils.getRectLeft(mImagePoints)), ((float) getWidth()) - BitmapUtils.getRectRight(mImagePoints)) / f5;
                    }
                    mZoomOffsetX = f3;
                    if (f2 <= BitmapUtils.getRectHeight(mImagePoints)) {
                        f4 = Math.max(Math.min((f2 / 2.0f) - cropWindowRect.centerY(), -BitmapUtils.getRectTop(mImagePoints)), ((float) getHeight()) - BitmapUtils.getRectBottom(mImagePoints)) / f6;
                    }
                    mZoomOffsetY = f4;
                } else {
                    mZoomOffsetX = Math.min(Math.max(mZoomOffsetX * f5, -cropWindowRect.left), (-cropWindowRect.right) + f) / f5;
                    mZoomOffsetY = Math.min(Math.max(mZoomOffsetY * f6, -cropWindowRect.top), (-cropWindowRect.bottom) + f2) / f6;
                }
                mImageMatrix.postTranslate(mZoomOffsetX * f5, mZoomOffsetY * f6);
                cropWindowRect.offset(mZoomOffsetX * f5, mZoomOffsetY * f6);
                mCropOverlayView.setCropWindowRect(cropWindowRect);
                mapImagePointsByImageMatrix();
                mCropOverlayView.invalidate();
                if (z2) {
                    mAnimation.setEndState(mImagePoints, mImageMatrix);
                    mImageView.startAnimation(mAnimation);
                } else {
                    mImageView.setImageMatrix(mImageMatrix);
                }
                updateImageBounds(false);
            }
        }
    }

    private void mapImagePointsByImageMatrix() {
        mImagePoints[0] = 0;
        mImagePoints[1] = 0;
        mImagePoints[2] = mBitmap.getWidth();
        mImagePoints[3] = 0;
        mImagePoints[4] = mBitmap.getWidth();
        mImagePoints[5] = mBitmap.getHeight();
        mImagePoints[6] = 0;
        mImagePoints[7] = mBitmap.getHeight();
        mImageMatrix.mapPoints(mImagePoints);
        mScaleImagePoints[0] = 0;
        mScaleImagePoints[1] = 0;
        mScaleImagePoints[2] = 100;
        mScaleImagePoints[3] = 0;
        mScaleImagePoints[4] = 100;
        mScaleImagePoints[5] = 100;
        mScaleImagePoints[6] = 0;
        mScaleImagePoints[7] = 100;
        mImageMatrix.mapPoints(mScaleImagePoints);
    }

    private static int getOnMeasureSpec(int i, int i2, int i3) {
        if (i == 1073741824) {
            return i2;
        }
        return i == Integer.MIN_VALUE ? Math.min(i3, i2) : i3;
    }

    private void setCropOverlayVisibility() {
        CropOverlayView cropOverlayView = mCropOverlayView;
        if (cropOverlayView != null) {
            cropOverlayView.setVisibility((!mShowCropOverlay || mBitmap == null) ? INVISIBLE : VISIBLE);
        }
    }

    private void setProgressBarVisibility() {
        int i = 0;
        boolean z = mShowProgressBar && ((mBitmap == null && mBitmapLoadingWorkerTask != null) || mBitmapCroppingWorkerTask != null);
        if (!z) {
            i = 4;
        }
        mProgressBar.setVisibility(i);
    }

    private void updateImageBounds(boolean z) {
        if (mBitmap != null && !z) {
            mCropOverlayView.setCropWindowLimits((float) getWidth(), (float) getHeight(), (((float) mLoadedSampleSize) * 100.0f) / BitmapUtils.getRectWidth(mScaleImagePoints), (((float) mLoadedSampleSize) * 100.0f) / BitmapUtils.getRectHeight(mScaleImagePoints));
        }
        mCropOverlayView.setBounds(z ? null : mImagePoints, getWidth(), getHeight());
    }

    public static class CropResult {
        private final Bitmap mBitmap;
        private final float[] mCropPoints;
        private final Rect mCropRect;
        private final Exception mError;
        private final Bitmap mOriginalBitmap;
        private final Uri mOriginalUri;
        private final int mRotation;
        private final int mSampleSize;
        private final Uri mUri;
        private final Rect mWholeImageRect;

        CropResult(Bitmap bitmap, Uri uri, Bitmap bitmap2, Uri uri2, Exception exc, float[] fArr, Rect rect, Rect rect2, int i, int i2) {
            mOriginalBitmap = bitmap;
            mOriginalUri = uri;
            mBitmap = bitmap2;
            mUri = uri2;
            mError = exc;
            mCropPoints = fArr;
            mCropRect = rect;
            mWholeImageRect = rect2;
            mRotation = i;
            mSampleSize = i2;
        }

        public Bitmap getOriginalBitmap() {
            return mOriginalBitmap;
        }

        public Uri getOriginalUri() {
            return mOriginalUri;
        }

        public boolean isSuccessful() {
            return mError == null;
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }

        public Uri getUri() {
            return mUri;
        }

        public Exception getError() {
            return mError;
        }

        public float[] getCropPoints() {
            return mCropPoints;
        }

        public Rect getCropRect() {
            return mCropRect;
        }

        public Rect getWholeImageRect() {
            return mWholeImageRect;
        }

        public int getRotation() {
            return mRotation;
        }

        public int getSampleSize() {
            return mSampleSize;
        }
    }
}
