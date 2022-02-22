package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class CropImageOptions implements Parcelable {
    public static final Parcelable.Creator<CropImageOptions> CREATOR = new Parcelable.Creator<CropImageOptions>() {
        public CropImageOptions createFromParcel(Parcel parcel) {
            return new CropImageOptions(parcel);
        }

        public CropImageOptions[] newArray(int i) {
            return new CropImageOptions[i];
        }
    };
    public int activityMenuIconColor;
    public CharSequence activityTitle;
    public boolean allowCounterRotation;
    public boolean allowFlipping;
    public boolean allowRotation;
    public int aspectRatioX;
    public int aspectRatioY;
    public boolean autoZoomEnabled;
    public int backgroundColor;
    public int borderCornerColor;
    public float borderCornerLength;
    public float borderCornerOffset;
    public float borderCornerThickness;
    public int borderLineColor;
    public float borderLineThickness;
    public int cropMenuCropButtonIcon;
    public CharSequence cropMenuCropButtonTitle;
    public CropImageView.CropShape cropShape;
    public boolean fixAspectRatio;
    public boolean flipHorizontally;
    public boolean flipVertically;
    public CropImageView.Guidelines guidelines;
    public int guidelinesColor;
    public float guidelinesThickness;
    public float initialCropWindowPaddingRatio;
    public Rect initialCropWindowRectangle;
    public int initialRotation;
    public int maxCropResultHeight;
    public int maxCropResultWidth;
    public int maxZoom;
    public int minCropResultHeight;
    public int minCropResultWidth;
    public int minCropWindowHeight;
    public int minCropWindowWidth;
    public boolean multiTouchEnabled;
    public boolean noOutputImage;
    public Bitmap.CompressFormat outputCompressFormat;
    public int outputCompressQuality;
    public int outputRequestHeight;
    public CropImageView.RequestSizeOptions outputRequestSizeOptions;
    public int outputRequestWidth;
    public Uri outputUri;
    public int rotationDegrees;
    public CropImageView.ScaleType scaleType;
    public boolean showCropOverlay;
    public boolean showProgressBar;
    public float snapRadius;
    public float touchRadius;

    public int describeContents() {
        return 0;
    }

    @SuppressLint("WrongConstant")
    public CropImageOptions() {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        cropShape = CropImageView.CropShape.RECTANGLE;
        snapRadius = TypedValue.applyDimension(1, 3.0f, displayMetrics);
        touchRadius = TypedValue.applyDimension(1, 24.0f, displayMetrics);
        guidelines = CropImageView.Guidelines.ON_TOUCH;
        scaleType = CropImageView.ScaleType.FIT_CENTER;
        showCropOverlay = true;
        showProgressBar = true;
        autoZoomEnabled = true;
        multiTouchEnabled = false;
        maxZoom = 4;
        initialCropWindowPaddingRatio = 0.1f;
        fixAspectRatio = false;
        aspectRatioX = 1;
        aspectRatioY = 1;
        borderLineThickness = TypedValue.applyDimension(1, 3.0f, displayMetrics);
        borderLineColor = Color.argb(170, 255, 255, 255);
        borderCornerThickness = TypedValue.applyDimension(1, 2.0f, displayMetrics);
        borderCornerOffset = TypedValue.applyDimension(1, 5.0f, displayMetrics);
        borderCornerLength = TypedValue.applyDimension(1, 14.0f, displayMetrics);
        borderCornerColor = -1;
        guidelinesThickness = TypedValue.applyDimension(1, 1.0f, displayMetrics);
        guidelinesColor = Color.argb(170, 255, 255, 255);
        backgroundColor = Color.argb(119, 0, 0, 0);
        minCropWindowWidth = (int) TypedValue.applyDimension(1, 42.0f, displayMetrics);
        minCropWindowHeight = (int) TypedValue.applyDimension(1, 42.0f, displayMetrics);
        minCropResultWidth = 40;
        minCropResultHeight = 40;
        maxCropResultWidth = 99999;
        maxCropResultHeight = 99999;
        activityTitle = "";
        activityMenuIconColor = 0;
        outputUri = Uri.EMPTY;
        outputCompressFormat = Bitmap.CompressFormat.JPEG;
        outputCompressQuality = 90;
        outputRequestWidth = 0;
        outputRequestHeight = 0;
        outputRequestSizeOptions = CropImageView.RequestSizeOptions.NONE;
        noOutputImage = false;
        initialCropWindowRectangle = null;
        initialRotation = -1;
        allowRotation = true;
        allowFlipping = true;
        allowCounterRotation = false;
        rotationDegrees = 90;
        flipHorizontally = false;
        flipVertically = false;
        cropMenuCropButtonTitle = null;
        cropMenuCropButtonIcon = 0;
    }

    protected CropImageOptions(Parcel parcel) {
        cropShape = CropImageView.CropShape.values()[parcel.readInt()];
        snapRadius = parcel.readFloat();
        touchRadius = parcel.readFloat();
        guidelines = CropImageView.Guidelines.values()[parcel.readInt()];
        scaleType = CropImageView.ScaleType.values()[parcel.readInt()];
        showCropOverlay = parcel.readByte() != 0;
        showProgressBar = parcel.readByte() != 0;
        autoZoomEnabled = parcel.readByte() != 0;
        multiTouchEnabled = parcel.readByte() != 0;
        maxZoom = parcel.readInt();
        initialCropWindowPaddingRatio = parcel.readFloat();
        fixAspectRatio = parcel.readByte() != 0;
        aspectRatioX = parcel.readInt();
        aspectRatioY = parcel.readInt();
        borderLineThickness = parcel.readFloat();
        borderLineColor = parcel.readInt();
        borderCornerThickness = parcel.readFloat();
        borderCornerOffset = parcel.readFloat();
        borderCornerLength = parcel.readFloat();
        borderCornerColor = parcel.readInt();
        guidelinesThickness = parcel.readFloat();
        guidelinesColor = parcel.readInt();
        backgroundColor = parcel.readInt();
        minCropWindowWidth = parcel.readInt();
        minCropWindowHeight = parcel.readInt();
        minCropResultWidth = parcel.readInt();
        minCropResultHeight = parcel.readInt();
        maxCropResultWidth = parcel.readInt();
        maxCropResultHeight = parcel.readInt();
        activityTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        activityMenuIconColor = parcel.readInt();
        outputUri = parcel.readParcelable(Uri.class.getClassLoader());
        outputCompressFormat = Bitmap.CompressFormat.valueOf(parcel.readString());
        outputCompressQuality = parcel.readInt();
        outputRequestWidth = parcel.readInt();
        outputRequestHeight = parcel.readInt();
        outputRequestSizeOptions = CropImageView.RequestSizeOptions.values()[parcel.readInt()];
        noOutputImage = parcel.readByte() != 0;
        initialCropWindowRectangle = parcel.readParcelable(Rect.class.getClassLoader());
        initialRotation = parcel.readInt();
        allowRotation = parcel.readByte() != 0;
        allowFlipping = parcel.readByte() != 0;
        allowCounterRotation = parcel.readByte() != 0;
        rotationDegrees = parcel.readInt();
        flipHorizontally = parcel.readByte() != 0;
        flipVertically = parcel.readByte() != 0;
        cropMenuCropButtonTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        cropMenuCropButtonIcon = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(cropShape.ordinal());
        parcel.writeFloat(snapRadius);
        parcel.writeFloat(touchRadius);
        parcel.writeInt(guidelines.ordinal());
        parcel.writeInt(scaleType.ordinal());
        parcel.writeByte(showCropOverlay ? (byte) 1 : 0);
        parcel.writeByte(showProgressBar ? (byte) 1 : 0);
        parcel.writeByte(autoZoomEnabled ? (byte) 1 : 0);
        parcel.writeByte(multiTouchEnabled ? (byte) 1 : 0);
        parcel.writeInt(maxZoom);
        parcel.writeFloat(initialCropWindowPaddingRatio);
        parcel.writeByte(fixAspectRatio ? (byte) 1 : 0);
        parcel.writeInt(aspectRatioX);
        parcel.writeInt(aspectRatioY);
        parcel.writeFloat(borderLineThickness);
        parcel.writeInt(borderLineColor);
        parcel.writeFloat(borderCornerThickness);
        parcel.writeFloat(borderCornerOffset);
        parcel.writeFloat(borderCornerLength);
        parcel.writeInt(borderCornerColor);
        parcel.writeFloat(guidelinesThickness);
        parcel.writeInt(guidelinesColor);
        parcel.writeInt(backgroundColor);
        parcel.writeInt(minCropWindowWidth);
        parcel.writeInt(minCropWindowHeight);
        parcel.writeInt(minCropResultWidth);
        parcel.writeInt(minCropResultHeight);
        parcel.writeInt(maxCropResultWidth);
        parcel.writeInt(maxCropResultHeight);
        TextUtils.writeToParcel(activityTitle, parcel, i);
        parcel.writeInt(activityMenuIconColor);
        parcel.writeParcelable(outputUri, i);
        parcel.writeString(outputCompressFormat.name());
        parcel.writeInt(outputCompressQuality);
        parcel.writeInt(outputRequestWidth);
        parcel.writeInt(outputRequestHeight);
        parcel.writeInt(outputRequestSizeOptions.ordinal());
        parcel.writeInt(noOutputImage ? 1 : 0);
        parcel.writeParcelable(initialCropWindowRectangle, i);
        parcel.writeInt(initialRotation);
        parcel.writeByte(allowRotation ? (byte) 1 : 0);
        parcel.writeByte(allowFlipping ? (byte) 1 : 0);
        parcel.writeByte(allowCounterRotation ? (byte) 1 : 0);
        parcel.writeInt(rotationDegrees);
        parcel.writeByte(flipHorizontally ? (byte) 1 : 0);
        parcel.writeByte(flipVertically ? (byte) 1 : 0);
        TextUtils.writeToParcel(cropMenuCropButtonTitle, parcel, i);
        parcel.writeInt(cropMenuCropButtonIcon);
    }

    public void validate() {
        if (maxZoom < 0) {
            throw new IllegalArgumentException("Cannot set max zoom to a number < 1");
        } else if (touchRadius >= 0.0f) {
            float f = initialCropWindowPaddingRatio;
            if (f < 0.0f || ((double) f) >= 0.5d) {
                throw new IllegalArgumentException("Cannot set initial crop window padding value to a number < 0 or >= 0.5");
            } else if (aspectRatioX <= 0) {
                throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
            } else if (aspectRatioY <= 0) {
                throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
            } else if (borderLineThickness < 0.0f) {
                throw new IllegalArgumentException("Cannot set line thickness value to a number less than 0.");
            } else if (borderCornerThickness < 0.0f) {
                throw new IllegalArgumentException("Cannot set corner thickness value to a number less than 0.");
            } else if (guidelinesThickness < 0.0f) {
                throw new IllegalArgumentException("Cannot set guidelines thickness value to a number less than 0.");
            } else if (minCropWindowHeight >= 0) {
                int i = minCropResultWidth;
                if (i >= 0) {
                    int i2 = minCropResultHeight;
                    if (i2 < 0) {
                        throw new IllegalArgumentException("Cannot set min crop result height value to a number < 0 ");
                    } else if (maxCropResultWidth < i) {
                        throw new IllegalArgumentException("Cannot set max crop result width to smaller value than min crop result width");
                    } else if (maxCropResultHeight < i2) {
                        throw new IllegalArgumentException("Cannot set max crop result height to smaller value than min crop result height");
                    } else if (outputRequestWidth < 0) {
                        throw new IllegalArgumentException("Cannot set request width value to a number < 0 ");
                    } else if (outputRequestHeight >= 0) {
                        int i3 = rotationDegrees;
                        if (i3 < 0 || i3 > 360) {
                            throw new IllegalArgumentException("Cannot set rotation degrees value to a number < 0 or > 360");
                        }
                    } else {
                        throw new IllegalArgumentException("Cannot set request height value to a number < 0 ");
                    }
                } else {
                    throw new IllegalArgumentException("Cannot set min crop result width value to a number < 0 ");
                }
            } else {
                throw new IllegalArgumentException("Cannot set min crop window height value to a number < 0 ");
            }
        } else {
            throw new IllegalArgumentException("Cannot set touch radius value to a number <= 0 ");
        }
    }
}
