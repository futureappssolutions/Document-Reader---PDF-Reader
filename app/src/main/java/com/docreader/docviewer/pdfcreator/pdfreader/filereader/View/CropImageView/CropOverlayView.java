package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.Arrays;

public class CropOverlayView extends View {
    private boolean initializedCropWindow;
    private boolean mFixAspectRatio;
    private boolean mMultiTouchEnabled;
    private float mBorderCornerLength;
    private float mBorderCornerOffset;
    private float mSnapRadius;
    private float mTargetAspectRatio;
    private float mTouchRadius;
    private float mInitialCropWindowPaddingRatio;
    private int mAspectRatioX;
    private int mAspectRatioY;
    private int mViewHeight;
    private int mViewWidth;
    private final float[] mBoundsPoints;
    private final Path mPath;
    private final RectF mDrawRect;
    private final RectF mCalcBounds;
    private final Rect mInitialCropWindowRect;
    public final CropWindowHandler mCropWindowHandler;
    private Paint mBackgroundPaint;
    private Paint mBorderCornerPaint;
    private Paint mBorderPaint;
    private Paint mGuidelinePaint;
    private CropImageView.CropShape mCropShape;
    private CropWindowChangeListener mCropWindowChangeListener;
    private CropImageView.Guidelines mGuidelines;
    private CropWindowMoveHandler mMoveHandler;
    private Integer mOriginalLayerType;
    private ScaleGestureDetector mScaleDetector;

    public interface CropWindowChangeListener {
        void onCropWindowChanged(boolean z);
    }

    public CropOverlayView(Context context) {
        this(context, null);
    }

    public CropOverlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mCropWindowHandler = new CropWindowHandler();
        mDrawRect = new RectF();
        mPath = new Path();
        mBoundsPoints = new float[8];
        mCalcBounds = new RectF();
        mTargetAspectRatio = ((float) mAspectRatioX) / ((float) mAspectRatioY);
        mInitialCropWindowRect = new Rect();
    }

    public void setCropWindowChangeListener(CropWindowChangeListener cropWindowChangeListener) {
        mCropWindowChangeListener = cropWindowChangeListener;
    }

    public RectF getCropWindowRect() {
        return mCropWindowHandler.getRect();
    }

    public void setCropWindowRect(RectF rectF) {
        mCropWindowHandler.setRect(rectF);
    }

    public void fixCurrentCropWindowRect() {
        RectF cropWindowRect = getCropWindowRect();
        fixCropWindowRectByRules(cropWindowRect);
        mCropWindowHandler.setRect(cropWindowRect);
    }

    public void setBounds(float[] fArr, int i, int i2) {
        if (fArr == null || !Arrays.equals(mBoundsPoints, fArr)) {
            if (fArr == null) {
                Arrays.fill(mBoundsPoints, 0.0f);
            } else {
                System.arraycopy(fArr, 0, mBoundsPoints, 0, fArr.length);
            }
            mViewWidth = i;
            mViewHeight = i2;
            RectF rect = mCropWindowHandler.getRect();
            if (rect.width() == 0.0f || rect.height() == 0.0f) {
                initCropWindow();
            }
        }
    }

    public void resetCropOverlayView() {
        if (initializedCropWindow) {
            setCropWindowRect(BitmapUtils.EMPTY_RECT_F);
            initCropWindow();
            invalidate();
        }
    }

    public CropImageView.CropShape getCropShape() {
        return mCropShape;
    }

    public void setCropShape(CropImageView.CropShape cropShape) {
        if (mCropShape != cropShape) {
            mCropShape = cropShape;
            if (Build.VERSION.SDK_INT <= 17) {
                if (mCropShape == CropImageView.CropShape.OVAL) {
                    int valueOf = getLayerType();
                    mOriginalLayerType = valueOf;
                    if (valueOf != 1) {
                        setLayerType(LAYER_TYPE_SOFTWARE, null);
                    } else {
                        mOriginalLayerType = null;
                    }
                } else {
                    Integer num = mOriginalLayerType;
                    if (num != null) {
                        setLayerType(num, null);
                        mOriginalLayerType = null;
                    }
                }
            }
            invalidate();
        }
    }

    public CropImageView.Guidelines getGuidelines() {
        return mGuidelines;
    }

    public void setGuidelines(CropImageView.Guidelines guidelines) {
        if (mGuidelines != guidelines) {
            mGuidelines = guidelines;
            if (initializedCropWindow) {
                invalidate();
            }
        }
    }

    public boolean isFixAspectRatio() {
        return mFixAspectRatio;
    }

    public void setFixedAspectRatio(boolean z) {
        if (mFixAspectRatio != z) {
            mFixAspectRatio = z;
            if (initializedCropWindow) {
                initCropWindow();
                invalidate();
            }
        }
    }

    public int getAspectRatioX() {
        return mAspectRatioX;
    }

    public void setAspectRatioX(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
        } else if (mAspectRatioX != i) {
            mAspectRatioX = i;
            mTargetAspectRatio = ((float) i) / ((float) mAspectRatioY);
            if (initializedCropWindow) {
                initCropWindow();
                invalidate();
            }
        }
    }

    public int getAspectRatioY() {
        return mAspectRatioY;
    }

    public void setAspectRatioY(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
        } else if (mAspectRatioY != i) {
            mAspectRatioY = i;
            mTargetAspectRatio = ((float) mAspectRatioX) / ((float) i);
            if (initializedCropWindow) {
                initCropWindow();
                invalidate();
            }
        }
    }

    public void setSnapRadius(float f) {
        mSnapRadius = f;
    }

    public boolean setMultiTouchEnabled(boolean z) {
        if (mMultiTouchEnabled == z) {
            return false;
        }
        mMultiTouchEnabled = z;
        if (!z || mScaleDetector != null) {
            return true;
        }
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        return true;
    }

    public void setMinCropResultSize(int i, int i2) {
        mCropWindowHandler.setMinCropResultSize(i, i2);
    }

    public void setMaxCropResultSize(int i, int i2) {
        mCropWindowHandler.setMaxCropResultSize(i, i2);
    }

    public void setCropWindowLimits(float f, float f2, float f3, float f4) {
        mCropWindowHandler.setCropWindowLimits(f, f2, f3, f4);
    }

    public Rect getInitialCropWindowRect() {
        return mInitialCropWindowRect;
    }

    public void setInitialCropWindowRect(Rect rect) {
        if (rect == null) {
            rect = BitmapUtils.EMPTY_RECT;
        }
        mInitialCropWindowRect.set(rect);
        if (initializedCropWindow) {
            initCropWindow();
            invalidate();
            callOnCropWindowChanged(false);
        }
    }

    public void resetCropWindowRect() {
        if (initializedCropWindow) {
            initCropWindow();
            invalidate();
            callOnCropWindowChanged(false);
        }
    }

    public void setInitialAttributeValues(CropImageOptions cropImageOptions) {
        mCropWindowHandler.setInitialAttributeValues(cropImageOptions);
        setCropShape(cropImageOptions.cropShape);
        setSnapRadius(cropImageOptions.snapRadius);
        setGuidelines(cropImageOptions.guidelines);
        setFixedAspectRatio(cropImageOptions.fixAspectRatio);
        setAspectRatioX(cropImageOptions.aspectRatioX);
        setAspectRatioY(cropImageOptions.aspectRatioY);
        setMultiTouchEnabled(cropImageOptions.multiTouchEnabled);
        mTouchRadius = cropImageOptions.touchRadius;
        mInitialCropWindowPaddingRatio = cropImageOptions.initialCropWindowPaddingRatio;
        mBorderPaint = getNewPaintOrNull(cropImageOptions.borderLineThickness, cropImageOptions.borderLineColor);
        mBorderCornerOffset = cropImageOptions.borderCornerOffset;
        mBorderCornerLength = cropImageOptions.borderCornerLength;
        mBorderCornerPaint = getNewPaintOrNull(cropImageOptions.borderCornerThickness, cropImageOptions.borderCornerColor);
        mGuidelinePaint = getNewPaintOrNull(cropImageOptions.guidelinesThickness, cropImageOptions.guidelinesColor);
        mBackgroundPaint = getNewPaint(cropImageOptions.backgroundColor);
    }

    private void initCropWindow() {
        float max = Math.max(BitmapUtils.getRectLeft(mBoundsPoints), 0.0f);
        float max2 = Math.max(BitmapUtils.getRectTop(mBoundsPoints), 0.0f);
        float min = Math.min(BitmapUtils.getRectRight(mBoundsPoints), (float) getWidth());
        float min2 = Math.min(BitmapUtils.getRectBottom(mBoundsPoints), (float) getHeight());
        if (min > max && min2 > max2) {
            RectF rectF = new RectF();
            initializedCropWindow = true;
            float f = mInitialCropWindowPaddingRatio;
            float f2 = min - max;
            float f3 = f * f2;
            float f4 = min2 - max2;
            float f5 = f * f4;
            if (mInitialCropWindowRect.width() > 0 && mInitialCropWindowRect.height() > 0) {
                rectF.left = (((float) mInitialCropWindowRect.left) / mCropWindowHandler.getScaleFactorWidth()) + max;
                rectF.top = (((float) mInitialCropWindowRect.top) / mCropWindowHandler.getScaleFactorHeight()) + max2;
                rectF.right = rectF.left + (((float) mInitialCropWindowRect.width()) / mCropWindowHandler.getScaleFactorWidth());
                rectF.bottom = rectF.top + (((float) mInitialCropWindowRect.height()) / mCropWindowHandler.getScaleFactorHeight());
                rectF.left = Math.max(max, rectF.left);
                rectF.top = Math.max(max2, rectF.top);
                rectF.right = Math.min(min, rectF.right);
                rectF.bottom = Math.min(min2, rectF.bottom);
            } else if (!mFixAspectRatio || min <= max || min2 <= max2) {
                rectF.left = max + f3;
                rectF.top = max2 + f5;
                rectF.right = min - f3;
                rectF.bottom = min2 - f5;
            } else if (f2 / f4 > mTargetAspectRatio) {
                rectF.top = max2 + f5;
                rectF.bottom = min2 - f5;
                float width = ((float) getWidth()) / 2.0f;
                mTargetAspectRatio = ((float) mAspectRatioX) / ((float) mAspectRatioY);
                float max3 = Math.max(mCropWindowHandler.getMinCropWidth(), rectF.height() * mTargetAspectRatio) / 2.0f;
                rectF.left = width - max3;
                rectF.right = width + max3;
            } else {
                rectF.left = max + f3;
                rectF.right = min - f3;
                float height = ((float) getHeight()) / 2.0f;
                float max4 = Math.max(mCropWindowHandler.getMinCropHeight(), rectF.width() / mTargetAspectRatio) / 2.0f;
                rectF.top = height - max4;
                rectF.bottom = height + max4;
            }
            fixCropWindowRectByRules(rectF);
            mCropWindowHandler.setRect(rectF);
        }
    }

    private void fixCropWindowRectByRules(RectF rectF) {
        if (rectF.width() < mCropWindowHandler.getMinCropWidth()) {
            float minCropWidth = (mCropWindowHandler.getMinCropWidth() - rectF.width()) / 2.0f;
            rectF.left -= minCropWidth;
            rectF.right += minCropWidth;
        }
        if (rectF.height() < mCropWindowHandler.getMinCropHeight()) {
            float minCropHeight = (mCropWindowHandler.getMinCropHeight() - rectF.height()) / 2.0f;
            rectF.top -= minCropHeight;
            rectF.bottom += minCropHeight;
        }
        if (rectF.width() > mCropWindowHandler.getMaxCropWidth()) {
            float width = (rectF.width() - mCropWindowHandler.getMaxCropWidth()) / 2.0f;
            rectF.left += width;
            rectF.right -= width;
        }
        if (rectF.height() > mCropWindowHandler.getMaxCropHeight()) {
            float height = (rectF.height() - mCropWindowHandler.getMaxCropHeight()) / 2.0f;
            rectF.top += height;
            rectF.bottom -= height;
        }
        calculateBounds(rectF);
        if (mCalcBounds.width() > 0.0f && mCalcBounds.height() > 0.0f) {
            float max = Math.max(mCalcBounds.left, 0.0f);
            float max2 = Math.max(mCalcBounds.top, 0.0f);
            float min = Math.min(mCalcBounds.right, (float) getWidth());
            float min2 = Math.min(mCalcBounds.bottom, (float) getHeight());
            if (rectF.left < max) {
                rectF.left = max;
            }
            if (rectF.top < max2) {
                rectF.top = max2;
            }
            if (rectF.right > min) {
                rectF.right = min;
            }
            if (rectF.bottom > min2) {
                rectF.bottom = min2;
            }
        }
        if (mFixAspectRatio && ((double) Math.abs(rectF.width() - (rectF.height() * mTargetAspectRatio))) > 0.1d) {
            if (rectF.width() > rectF.height() * mTargetAspectRatio) {
                float abs = Math.abs((rectF.height() * mTargetAspectRatio) - rectF.width()) / 2.0f;
                rectF.left += abs;
                rectF.right -= abs;
                return;
            }
            float abs2 = Math.abs((rectF.width() / mTargetAspectRatio) - rectF.height()) / 2.0f;
            rectF.top += abs2;
            rectF.bottom -= abs2;
        }
    }


    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        if (mCropWindowHandler.showGuidelines()) {
            if (mGuidelines == CropImageView.Guidelines.ON) {
                drawGuidelines(canvas);
            } else if (mGuidelines == CropImageView.Guidelines.ON_TOUCH && mMoveHandler != null) {
                drawGuidelines(canvas);
            }
        }
        drawBorders(canvas);
        drawCorners(canvas);
    }

    private void drawBackground(Canvas canvas) {
        RectF rect = mCropWindowHandler.getRect();
        float max = Math.max(BitmapUtils.getRectLeft(mBoundsPoints), 0.0f);
        float max2 = Math.max(BitmapUtils.getRectTop(mBoundsPoints), 0.0f);
        float min = Math.min(BitmapUtils.getRectRight(mBoundsPoints), (float) getWidth());
        float min2 = Math.min(BitmapUtils.getRectBottom(mBoundsPoints), (float) getHeight());
        if (mCropShape != CropImageView.CropShape.RECTANGLE) {
            mPath.reset();
            if (Build.VERSION.SDK_INT > 17 || mCropShape != CropImageView.CropShape.OVAL) {
                mDrawRect.set(rect.left, rect.top, rect.right, rect.bottom);
            } else {
                mDrawRect.set(rect.left + 2.0f, rect.top + 2.0f, rect.right - 2.0f, rect.bottom - 2.0f);
            }
            mPath.addOval(mDrawRect, Path.Direction.CW);
            canvas.save();
            if (Build.VERSION.SDK_INT >= 26) {
                canvas.clipOutPath(mPath);
            } else {
                canvas.clipPath(mPath, Region.Op.XOR);
            }
            canvas.drawRect(max, max2, min, min2, mBackgroundPaint);
            canvas.restore();
        } else if (!isNonStraightAngleRotated() || Build.VERSION.SDK_INT <= 17) {
            canvas.drawRect(max, max2, min, rect.top, mBackgroundPaint);
            canvas.drawRect(max, rect.bottom, min, min2, mBackgroundPaint);
            canvas.drawRect(max, rect.top, rect.left, rect.bottom, mBackgroundPaint);
            canvas.drawRect(rect.right, rect.top, min, rect.bottom, mBackgroundPaint);
        } else {
            mPath.reset();
            float[] fArr = mBoundsPoints;
            mPath.moveTo(fArr[0], fArr[1]);
            float[] fArr2 = mBoundsPoints;
            mPath.lineTo(fArr2[2], fArr2[3]);
            float[] fArr3 = mBoundsPoints;
            mPath.lineTo(fArr3[4], fArr3[5]);
            float[] fArr4 = mBoundsPoints;
            mPath.lineTo(fArr4[6], fArr4[7]);
            mPath.close();
            canvas.save();
            if (Build.VERSION.SDK_INT >= 26) {
                canvas.clipOutPath(mPath);
            } else {
                canvas.clipPath(mPath, Region.Op.INTERSECT);
            }
            canvas.clipRect(rect, Region.Op.XOR);
            canvas.drawRect(max, max2, min, min2, mBackgroundPaint);
            canvas.restore();
        }
    }

    private void drawGuidelines(Canvas canvas) {
        if (mGuidelinePaint != null) {
            float strokeWidth = mBorderPaint != null ? mBorderPaint.getStrokeWidth() : 0.0f;
            RectF rect = mCropWindowHandler.getRect();
            rect.inset(strokeWidth, strokeWidth);
            float width = rect.width() / 3.0f;
            float height = rect.height() / 3.0f;
            if (mCropShape == CropImageView.CropShape.OVAL) {
                float width2 = (rect.width() / 2.0f) - strokeWidth;
                float height2 = (rect.height() / 2.0f) - strokeWidth;
                float f = rect.left + width;
                float f2 = rect.right - width;
                double sin = Math.sin(Math.acos((width2 - width) / width2));
                Double.isNaN(height2);
                float f3 = (float) ((double) height2 * sin);
                canvas.drawLine(f, (rect.top + height2) - f3, f, (rect.bottom - height2) + f3, mGuidelinePaint);
                canvas.drawLine(f2, (rect.top + height2) - f3, f2, (rect.bottom - height2) + f3, mGuidelinePaint);
                float f4 = rect.top + height;
                float f5 = rect.bottom - height;
                double cos = Math.cos(Math.asin((height2 - height) / height2));
                Double.isNaN(width2);
                float f6 = (float) ((double) width2 * cos);
                canvas.drawLine((rect.left + width2) - f6, f4, (rect.right - width2) + f6, f4, mGuidelinePaint);
                canvas.drawLine((rect.left + width2) - f6, f5, (rect.right - width2) + f6, f5, mGuidelinePaint);
                return;
            }
            float f7 = rect.left + width;
            float f8 = rect.right - width;
            canvas.drawLine(f7, rect.top, f7, rect.bottom, mGuidelinePaint);
            canvas.drawLine(f8, rect.top, f8, rect.bottom, mGuidelinePaint);
            float f9 = rect.top + height;
            float f10 = rect.bottom - height;
            canvas.drawLine(rect.left, f9, rect.right, f9, mGuidelinePaint);
            canvas.drawLine(rect.left, f10, rect.right, f10, mGuidelinePaint);
        }
    }

    private void drawBorders(Canvas canvas) {
        Paint paint = mBorderPaint;
        if (paint != null) {
            float strokeWidth = paint.getStrokeWidth();
            RectF rect = mCropWindowHandler.getRect();
            float f = strokeWidth / 2.0f;
            rect.inset(f, f);
            if (mCropShape == CropImageView.CropShape.RECTANGLE) {
                canvas.drawRect(rect, mBorderPaint);
            } else {
                canvas.drawOval(rect, mBorderPaint);
            }
        }
    }

    private void drawCorners(Canvas canvas) {
        if (mBorderCornerPaint != null) {
            Paint paint = mBorderPaint;
            float f = 0.0f;
            float strokeWidth = paint != null ? paint.getStrokeWidth() : 0.0f;
            float strokeWidth2 = mBorderCornerPaint.getStrokeWidth();
            float f2 = strokeWidth2 / 2.0f;
            if (mCropShape == CropImageView.CropShape.RECTANGLE) {
                f = mBorderCornerOffset;
            }
            float f3 = f + f2;
            RectF rect = mCropWindowHandler.getRect();
            rect.inset(f3, f3);
            float f4 = (strokeWidth2 - strokeWidth) / 2.0f;
            float f5 = f2 + f4;
            canvas.drawLine(rect.left - f4, rect.top - f5, rect.left - f4, rect.top + mBorderCornerLength, mBorderCornerPaint);
            canvas.drawLine(rect.left - f5, rect.top - f4, rect.left + mBorderCornerLength, rect.top - f4, mBorderCornerPaint);
            canvas.drawLine(rect.right + f4, rect.top - f5, rect.right + f4, rect.top + mBorderCornerLength, mBorderCornerPaint);
            canvas.drawLine(rect.right + f5, rect.top - f4, rect.right - mBorderCornerLength, rect.top - f4, mBorderCornerPaint);
            canvas.drawLine(rect.left - f4, rect.bottom + f5, rect.left - f4, rect.bottom - mBorderCornerLength, mBorderCornerPaint);
            canvas.drawLine(rect.left - f5, rect.bottom + f4, rect.left + mBorderCornerLength, rect.bottom + f4, mBorderCornerPaint);
            canvas.drawLine(rect.right + f4, rect.bottom + f5, rect.right + f4, rect.bottom - mBorderCornerLength, mBorderCornerPaint);
            canvas.drawLine(rect.right + f5, rect.bottom + f4, rect.right - mBorderCornerLength, rect.bottom + f4, mBorderCornerPaint);
        }
    }

    private static Paint getNewPaint(int i) {
        Paint paint = new Paint();
        paint.setColor(i);
        return paint;
    }

    private static Paint getNewPaintOrNull(float f, int i) {
        if (f <= 0.0f) {
            return null;
        }
        Paint paint = new Paint();
        paint.setColor(i);
        paint.setStrokeWidth(f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        return paint;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        if (mMultiTouchEnabled) {
            mScaleDetector.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    onActionMove(motionEvent.getX(), motionEvent.getY());
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                } else if (action != 3) {
                    return false;
                }
            }
            getParent().requestDisallowInterceptTouchEvent(false);
            onActionUp();
            return true;
        }
        onActionDown(motionEvent.getX(), motionEvent.getY());
        return true;
    }

    private void onActionDown(float f, float f2) {
        CropWindowMoveHandler moveHandler = mCropWindowHandler.getMoveHandler(f, f2, mTouchRadius, mCropShape);
        mMoveHandler = moveHandler;
        if (moveHandler != null) {
            invalidate();
        }
    }

    private void onActionUp() {
        if (mMoveHandler != null) {
            mMoveHandler = null;
            callOnCropWindowChanged(false);
            invalidate();
        }
    }

    private void onActionMove(float f, float f2) {
        if (mMoveHandler != null) {
            float f3 = mSnapRadius;
            RectF rect = mCropWindowHandler.getRect();
            mMoveHandler.move(rect, f, f2, mCalcBounds, mViewWidth, mViewHeight, calculateBounds(rect) ? 0.0f : f3, mFixAspectRatio, mTargetAspectRatio);
            mCropWindowHandler.setRect(rect);
            callOnCropWindowChanged(true);
            invalidate();
        }
    }

    private boolean calculateBounds(RectF rectF) {
        float rectLeft = BitmapUtils.getRectLeft(mBoundsPoints);
        float rectTop = BitmapUtils.getRectTop(mBoundsPoints);
        float rectRight = BitmapUtils.getRectRight(mBoundsPoints);
        float rectBottom = BitmapUtils.getRectBottom(mBoundsPoints);
        if (!isNonStraightAngleRotated()) {
            mCalcBounds.set(rectLeft, rectTop, rectRight, rectBottom);
            return false;
        }
        float[] fArr = mBoundsPoints;
        float f = fArr[0];
        float f2 = fArr[1];
        float f3 = fArr[4];
        float f4 = fArr[5];
        float f5 = fArr[6];
        float f6 = fArr[7];
        if (fArr[7] < fArr[1]) {
            if (fArr[1] < fArr[3]) {
                f = fArr[6];
                f2 = fArr[7];
                f3 = fArr[2];
                f4 = fArr[3];
                f5 = fArr[4];
                f6 = fArr[5];
            } else {
                f = fArr[4];
                f2 = fArr[5];
                f3 = fArr[0];
                f4 = fArr[1];
                f5 = fArr[2];
                f6 = fArr[3];
            }
        } else if (fArr[1] > fArr[3]) {
            f = fArr[2];
            f2 = fArr[3];
            f3 = fArr[6];
            f4 = fArr[7];
            f5 = fArr[0];
            f6 = fArr[1];
        }
        float f7 = (f6 - f2) / (f5 - f);
        float f8 = -1.0f / f7;
        float f9 = f2 - (f7 * f);
        float f10 = f2 - (f * f8);
        float f11 = f4 - (f7 * f3);
        float f12 = f4 - (f3 * f8);
        float centerY = (rectF.centerY() - rectF.top) / (rectF.centerX() - rectF.left);
        float f13 = -centerY;
        float f14 = rectF.top - (rectF.left * centerY);
        float f15 = rectF.top - (rectF.right * f13);
        float f16 = f7 - centerY;
        float f17 = (f14 - f9) / f16;
        if (f17 >= rectF.right) {
            f17 = rectLeft;
        }
        float max = Math.max(rectLeft, f17);
        float f18 = (f14 - f10) / (f8 - centerY);
        if (f18 >= rectF.right) {
            f18 = max;
        }
        float max2 = Math.max(max, f18);
        float f19 = f8 - f13;
        float f20 = (f15 - f12) / f19;
        if (f20 >= rectF.right) {
            f20 = max2;
        }
        float max3 = Math.max(max2, f20);
        float f21 = (f15 - f10) / f19;
        if (f21 <= rectF.left) {
            f21 = rectRight;
        }
        float min = Math.min(rectRight, f21);
        float f22 = (f15 - f11) / (f7 - f13);
        if (f22 <= rectF.left) {
            f22 = min;
        }
        float min2 = Math.min(min, f22);
        float f23 = (f14 - f11) / f16;
        if (f23 <= rectF.left) {
            f23 = min2;
        }
        float min3 = Math.min(min2, f23);
        float max4 = Math.max(rectTop, Math.max((f7 * max3) + f9, (f8 * min3) + f10));
        float min4 = Math.min(rectBottom, Math.min((f8 * max3) + f12, (f7 * min3) + f11));
        mCalcBounds.left = max3;
        mCalcBounds.top = max4;
        mCalcBounds.right = min3;
        mCalcBounds.bottom = min4;
        return true;
    }

    private boolean isNonStraightAngleRotated() {
        float[] fArr = mBoundsPoints;
        return fArr[0] != fArr[6] && fArr[1] != fArr[7];
    }

    private void callOnCropWindowChanged(boolean z) {
        try {
            CropWindowChangeListener cropWindowChangeListener = mCropWindowChangeListener;
            if (cropWindowChangeListener != null) {
                cropWindowChangeListener.onCropWindowChanged(z);
            }
        } catch (Exception e) {
            Log.e("AIC", "Exception in crop window changed", e);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            RectF rect = mCropWindowHandler.getRect();
            float focusX = scaleGestureDetector.getFocusX();
            float focusY = scaleGestureDetector.getFocusY();
            float currentSpanY = scaleGestureDetector.getCurrentSpanY() / 2.0f;
            float currentSpanX = scaleGestureDetector.getCurrentSpanX() / 2.0f;
            float f = focusY - currentSpanY;
            float f2 = focusX - currentSpanX;
            float f3 = focusX + currentSpanX;
            float f4 = focusY + currentSpanY;
            if (f2 >= f3 || f > f4 || f2 < 0.0f || f3 > mCropWindowHandler.getMaxCropWidth() || f < 0.0f || f4 > mCropWindowHandler.getMaxCropHeight()) {
                return true;
            }
            rect.set(f2, f, f3, f4);
            mCropWindowHandler.setRect(rect);
            invalidate();
            return true;
        }
    }
}
