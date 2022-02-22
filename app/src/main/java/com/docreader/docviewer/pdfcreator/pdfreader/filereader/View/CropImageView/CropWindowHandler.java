package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.graphics.RectF;

final class CropWindowHandler {
    private final RectF mEdges = new RectF();
    private final RectF mGetEdges = new RectF();
    private float mMaxCropResultHeight;
    private float mMaxCropResultWidth;
    private float mMaxCropWindowHeight;
    private float mMaxCropWindowWidth;
    private float mMinCropResultHeight;
    private float mMinCropResultWidth;
    private float mMinCropWindowHeight;
    private float mMinCropWindowWidth;
    private float mScaleFactorHeight = 1.0f;
    private float mScaleFactorWidth = 1.0f;

    private static boolean isInCenterTargetZone(float f, float f2, float f3, float f4, float f5, float f6) {
        return f > f3 && f < f5 && f2 > f4 && f2 < f6;
    }

    CropWindowHandler() {
    }

    public RectF getRect() {
        this.mGetEdges.set(this.mEdges);
        return this.mGetEdges;
    }

    public float getMinCropWidth() {
        return Math.max(this.mMinCropWindowWidth, this.mMinCropResultWidth / this.mScaleFactorWidth);
    }

    public float getMinCropHeight() {
        return Math.max(this.mMinCropWindowHeight, this.mMinCropResultHeight / this.mScaleFactorHeight);
    }

    public float getMaxCropWidth() {
        return Math.min(this.mMaxCropWindowWidth, this.mMaxCropResultWidth / this.mScaleFactorWidth);
    }

    public float getMaxCropHeight() {
        return Math.min(this.mMaxCropWindowHeight, this.mMaxCropResultHeight / this.mScaleFactorHeight);
    }

    public float getScaleFactorWidth() {
        return this.mScaleFactorWidth;
    }

    public float getScaleFactorHeight() {
        return this.mScaleFactorHeight;
    }

    public void setMinCropResultSize(int i, int i2) {
        this.mMinCropResultWidth = (float) i;
        this.mMinCropResultHeight = (float) i2;
    }

    public void setMaxCropResultSize(int i, int i2) {
        this.mMaxCropResultWidth = (float) i;
        this.mMaxCropResultHeight = (float) i2;
    }

    public void setCropWindowLimits(float f, float f2, float f3, float f4) {
        this.mMaxCropWindowWidth = f;
        this.mMaxCropWindowHeight = f2;
        this.mScaleFactorWidth = f3;
        this.mScaleFactorHeight = f4;
    }

    public void setInitialAttributeValues(CropImageOptions cropImageOptions) {
        this.mMinCropWindowWidth = (float) cropImageOptions.minCropWindowWidth;
        this.mMinCropWindowHeight = (float) cropImageOptions.minCropWindowHeight;
        this.mMinCropResultWidth = (float) cropImageOptions.minCropResultWidth;
        this.mMinCropResultHeight = (float) cropImageOptions.minCropResultHeight;
        this.mMaxCropResultWidth = (float) cropImageOptions.maxCropResultWidth;
        this.mMaxCropResultHeight = (float) cropImageOptions.maxCropResultHeight;
    }

    public void setRect(RectF rectF) {
        this.mEdges.set(rectF);
    }

    public boolean showGuidelines() {
        return this.mEdges.width() >= 100.0f && this.mEdges.height() >= 100.0f;
    }

    public CropWindowMoveHandler getMoveHandler(float f, float f2, float f3, CropImageView.CropShape cropShape) {
        CropWindowMoveHandler.Type type;
        if (cropShape == CropImageView.CropShape.OVAL) {
            type = getOvalPressedMoveType(f, f2);
        } else {
            type = getRectanglePressedMoveType(f, f2, f3);
        }
        if (type != null) {
            return new CropWindowMoveHandler(type, this, f, f2);
        }
        return null;
    }

    private CropWindowMoveHandler.Type getRectanglePressedMoveType(float f, float f2, float f3) {
        if (isInCornerTargetZone(f, f2, this.mEdges.left, this.mEdges.top, f3)) {
            return CropWindowMoveHandler.Type.TOP_LEFT;
        }
        if (isInCornerTargetZone(f, f2, this.mEdges.right, this.mEdges.top, f3)) {
            return CropWindowMoveHandler.Type.TOP_RIGHT;
        }
        if (isInCornerTargetZone(f, f2, this.mEdges.left, this.mEdges.bottom, f3)) {
            return CropWindowMoveHandler.Type.BOTTOM_LEFT;
        }
        if (isInCornerTargetZone(f, f2, this.mEdges.right, this.mEdges.bottom, f3)) {
            return CropWindowMoveHandler.Type.BOTTOM_RIGHT;
        }
        if (isInCenterTargetZone(f, f2, this.mEdges.left, this.mEdges.top, this.mEdges.right, this.mEdges.bottom) && focusCenter()) {
            return CropWindowMoveHandler.Type.CENTER;
        }
        if (isInHorizontalTargetZone(f, f2, this.mEdges.left, this.mEdges.right, this.mEdges.top, f3)) {
            return CropWindowMoveHandler.Type.TOP;
        }
        if (isInHorizontalTargetZone(f, f2, this.mEdges.left, this.mEdges.right, this.mEdges.bottom, f3)) {
            return CropWindowMoveHandler.Type.BOTTOM;
        }
        if (isInVerticalTargetZone(f, f2, this.mEdges.left, this.mEdges.top, this.mEdges.bottom, f3)) {
            return CropWindowMoveHandler.Type.LEFT;
        }
        if (isInVerticalTargetZone(f, f2, this.mEdges.right, this.mEdges.top, this.mEdges.bottom, f3)) {
            return CropWindowMoveHandler.Type.RIGHT;
        }
        if (!isInCenterTargetZone(f, f2, this.mEdges.left, this.mEdges.top, this.mEdges.right, this.mEdges.bottom) || focusCenter()) {
            return null;
        }
        return CropWindowMoveHandler.Type.CENTER;
    }

    private CropWindowMoveHandler.Type getOvalPressedMoveType(float f, float f2) {
        float width = this.mEdges.width() / 6.0f;
        float f3 = this.mEdges.left + width;
        float f4 = this.mEdges.left + (width * 5.0f);
        float height = this.mEdges.height() / 6.0f;
        float f5 = this.mEdges.top + height;
        float f6 = this.mEdges.top + (height * 5.0f);
        if (f < f3) {
            if (f2 < f5) {
                return CropWindowMoveHandler.Type.TOP_LEFT;
            }
            if (f2 < f6) {
                return CropWindowMoveHandler.Type.LEFT;
            }
            return CropWindowMoveHandler.Type.BOTTOM_LEFT;
        } else if (f < f4) {
            if (f2 < f5) {
                return CropWindowMoveHandler.Type.TOP;
            }
            if (f2 < f6) {
                return CropWindowMoveHandler.Type.CENTER;
            }
            return CropWindowMoveHandler.Type.BOTTOM;
        } else if (f2 < f5) {
            return CropWindowMoveHandler.Type.TOP_RIGHT;
        } else {
            if (f2 < f6) {
                return CropWindowMoveHandler.Type.RIGHT;
            }
            return CropWindowMoveHandler.Type.BOTTOM_RIGHT;
        }
    }

    private static boolean isInCornerTargetZone(float f, float f2, float f3, float f4, float f5) {
        return Math.abs(f - f3) <= f5 && Math.abs(f2 - f4) <= f5;
    }

    private static boolean isInHorizontalTargetZone(float f, float f2, float f3, float f4, float f5, float f6) {
        return f > f3 && f < f4 && Math.abs(f2 - f5) <= f6;
    }

    private static boolean isInVerticalTargetZone(float f, float f2, float f3, float f4, float f5, float f6) {
        return Math.abs(f - f3) <= f6 && f2 > f4 && f2 < f5;
    }

    private boolean focusCenter() {
        return !showGuidelines();
    }
}
