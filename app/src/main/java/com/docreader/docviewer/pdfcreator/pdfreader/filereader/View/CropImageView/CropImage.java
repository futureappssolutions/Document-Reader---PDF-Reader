package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CropImage {
    public static final int CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE = 2011;
    public static final String CROP_IMAGE_EXTRA_BUNDLE = "CROP_IMAGE_EXTRA_BUNDLE";
    public static final String CROP_IMAGE_EXTRA_OPTIONS = "CROP_IMAGE_EXTRA_OPTIONS";
    public static final String CROP_IMAGE_EXTRA_RESULT = "CROP_IMAGE_EXTRA_RESULT";
    public static final String CROP_IMAGE_EXTRA_SOURCE = "CROP_IMAGE_EXTRA_SOURCE";

    private CropImage() {
    }

    public static Bitmap toOvalBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawOval(new RectF(0.0f, 0.0f, (float) width, (float) height), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        bitmap.recycle();
        return createBitmap;
    }

    public static void startPickImageActivity(Activity activity) {
        activity.startActivityForResult(getPickImageChooserIntent(activity), 200);
    }

    public static void startPickImageActivity(Context context, Fragment fragment) {
        fragment.startActivityForResult(getPickImageChooserIntent(context), 200);
    }

    public static Intent getPickImageChooserIntent(Context context) {
        return getPickImageChooserIntent(context, context.getString(R.string.pick_image_intent_chooser_title), false, true);
    }

    public static Intent getPickImageChooserIntent(Context context, CharSequence charSequence, boolean z, boolean z2) {
        Intent intent;
        ArrayList<Intent> arrayList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        if (!isExplicitCameraPermissionRequired(context) && z2) {
            arrayList.addAll(getCameraIntents(context, packageManager));
        }
        List<Intent> galleryIntents = getGalleryIntents(packageManager, "android.intent.action.GET_CONTENT", z);
        if (galleryIntents.size() == 0) {
            galleryIntents = getGalleryIntents(packageManager, "android.intent.action.PICK", z);
        }
        arrayList.addAll(galleryIntents);
        if (arrayList.isEmpty()) {
            intent = new Intent();
        } else {
            intent = arrayList.get(arrayList.size() - 1);
            arrayList.remove(arrayList.size() - 1);
        }
        Intent createChooser = Intent.createChooser(intent, charSequence);
        createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", arrayList.toArray(new Parcelable[arrayList.size()]));
        return createChooser;
    }

    public static Intent getCameraIntent(Context context, Uri uri) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (uri == null) {
            uri = getCaptureImageOutputUri(context);
        }
        intent.putExtra("output", uri);
        return intent;
    }

    public static List<Intent> getCameraIntents(Context context, PackageManager packageManager) {
        ArrayList<Intent> arrayList = new ArrayList<>();
        Uri captureImageOutputUri = getCaptureImageOutputUri(context);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        for (ResolveInfo next : packageManager.queryIntentActivities(intent, 0)) {
            Intent intent2 = new Intent(intent);
            intent2.setComponent(new ComponentName(next.activityInfo.packageName, next.activityInfo.name));
            intent2.setPackage(next.activityInfo.packageName);
            if (captureImageOutputUri != null) {
                intent2.putExtra("output", captureImageOutputUri);
            }
            arrayList.add(intent2);
        }
        return arrayList;
    }

    public static List<Intent> getGalleryIntents(PackageManager packageManager, String str, boolean z) {
        Intent intent;
        ArrayList<Intent> arrayList = new ArrayList<>();
        if (str == "android.intent.action.GET_CONTENT") {
            intent = new Intent(str);
        } else {
            intent = new Intent(str, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        for (ResolveInfo next : packageManager.queryIntentActivities(intent, 0)) {
            Intent intent2 = new Intent(intent);
            intent2.setComponent(new ComponentName(next.activityInfo.packageName, next.activityInfo.name));
            intent2.setPackage(next.activityInfo.packageName);
            arrayList.add(intent2);
        }
        if (!z) {
            Iterator<Intent> it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Intent intent3 = it.next();
                if (intent3.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                    arrayList.remove(intent3);
                    break;
                }
            }
        }
        return arrayList;
    }

    public static boolean isExplicitCameraPermissionRequired(Context context) {
        return Build.VERSION.SDK_INT >= 23 && hasPermissionInManifest(context, "android.permission.CAMERA") && context.checkSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasPermissionInManifest(Context context, String str) {
        try {
            @SuppressLint("WrongConstant") String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr != null && strArr.length > 0) {
                for (String equalsIgnoreCase : strArr) {
                    if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    public static Uri getCaptureImageOutputUri(Context context) {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null) {
            return Uri.fromFile(new File(externalCacheDir.getPath(), "pickImageResult.jpeg"));
        }
        return null;
    }

    public static Uri getPickImageResultUri(Context context, Intent intent) {
        String action;
        boolean z = true;
        if (!(intent == null || intent.getData() == null || ((action = intent.getAction()) != null && action.equals("android.media.action.IMAGE_CAPTURE")))) {
            z = false;
        }
        return (z || intent.getData() == null) ? getCaptureImageOutputUri(context) : intent.getData();
    }

    public static boolean isReadExternalStoragePermissionsRequired(Context context, Uri uri) {
        return Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED && isUriRequiresPermissions(context, uri);
    }

    public static boolean isUriRequiresPermissions(Context context, Uri uri) {
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            if (openInputStream == null) {
                return false;
            }
            openInputStream.close();
            return false;
        } catch (Exception unused) {
            return true;
        }
    }

    public static ActivityBuilder activity() {
        return new ActivityBuilder(null);
    }

    public static ActivityBuilder activity(Uri uri) {
        return new ActivityBuilder(uri);
    }

    public static ActivityResult getActivityResult(Intent intent) {
        if (intent != null) {
            return intent.getParcelableExtra(CROP_IMAGE_EXTRA_RESULT);
        }
        return null;
    }

    public static final class ActivityBuilder {
        private final CropImageOptions mOptions;
        private final Uri mSource;

        private ActivityBuilder(Uri uri) {
            this.mSource = uri;
            this.mOptions = new CropImageOptions();
        }

        public Intent getIntent(Context context) {
            return getIntent(context, CropImageActivity.class);
        }

        public Intent getIntent(Context context, Class<?> cls) {
            this.mOptions.validate();
            Intent intent = new Intent();
            intent.setClass(context, cls);
            Bundle bundle = new Bundle();
            bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, this.mSource);
            bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, this.mOptions);
            intent.putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, bundle);
            return intent;
        }

        public void start(Activity activity) {
            this.mOptions.validate();
            activity.startActivityForResult(getIntent(activity), 203);
        }

        public void start(Activity activity, Class<?> cls) {
            this.mOptions.validate();
            activity.startActivityForResult(getIntent(activity, cls), 203);
        }

        public void start(Context context, Fragment fragment) {
            fragment.startActivityForResult(getIntent(context), 203);
        }

        public void start(Context context, android.app.Fragment fragment) {
            fragment.startActivityForResult(getIntent(context), 203);
        }

        public void start(Context context, Fragment fragment, Class<?> cls) {
            fragment.startActivityForResult(getIntent(context, cls), 203);
        }

        public void start(Context context, android.app.Fragment fragment, Class<?> cls) {
            fragment.startActivityForResult(getIntent(context, cls), 203);
        }

        public ActivityBuilder setCropShape(CropImageView.CropShape cropShape) {
            this.mOptions.cropShape = cropShape;
            return this;
        }

        public ActivityBuilder setSnapRadius(float f) {
            this.mOptions.snapRadius = f;
            return this;
        }

        public ActivityBuilder setTouchRadius(float f) {
            this.mOptions.touchRadius = f;
            return this;
        }

        public ActivityBuilder setGuidelines(CropImageView.Guidelines guidelines) {
            this.mOptions.guidelines = guidelines;
            return this;
        }

        public ActivityBuilder setScaleType(CropImageView.ScaleType scaleType) {
            this.mOptions.scaleType = scaleType;
            return this;
        }

        public ActivityBuilder setShowCropOverlay(boolean z) {
            this.mOptions.showCropOverlay = z;
            return this;
        }

        public ActivityBuilder setAutoZoomEnabled(boolean z) {
            this.mOptions.autoZoomEnabled = z;
            return this;
        }

        public ActivityBuilder setMultiTouchEnabled(boolean z) {
            this.mOptions.multiTouchEnabled = z;
            return this;
        }

        public ActivityBuilder setMaxZoom(int i) {
            this.mOptions.maxZoom = i;
            return this;
        }

        public ActivityBuilder setInitialCropWindowPaddingRatio(float f) {
            this.mOptions.initialCropWindowPaddingRatio = f;
            return this;
        }

        public ActivityBuilder setFixAspectRatio(boolean z) {
            this.mOptions.fixAspectRatio = z;
            return this;
        }

        public ActivityBuilder setAspectRatio(int i, int i2) {
            this.mOptions.aspectRatioX = i;
            this.mOptions.aspectRatioY = i2;
            this.mOptions.fixAspectRatio = true;
            return this;
        }

        public ActivityBuilder setBorderLineThickness(float f) {
            this.mOptions.borderLineThickness = f;
            return this;
        }

        public ActivityBuilder setBorderLineColor(int i) {
            this.mOptions.borderLineColor = i;
            return this;
        }

        public ActivityBuilder setBorderCornerThickness(float f) {
            this.mOptions.borderCornerThickness = f;
            return this;
        }

        public ActivityBuilder setBorderCornerOffset(float f) {
            this.mOptions.borderCornerOffset = f;
            return this;
        }

        public ActivityBuilder setBorderCornerLength(float f) {
            this.mOptions.borderCornerLength = f;
            return this;
        }

        public ActivityBuilder setBorderCornerColor(int i) {
            this.mOptions.borderCornerColor = i;
            return this;
        }

        public ActivityBuilder setGuidelinesThickness(float f) {
            this.mOptions.guidelinesThickness = f;
            return this;
        }

        public ActivityBuilder setGuidelinesColor(int i) {
            this.mOptions.guidelinesColor = i;
            return this;
        }

        public ActivityBuilder setBackgroundColor(int i) {
            this.mOptions.backgroundColor = i;
            return this;
        }

        public ActivityBuilder setMinCropWindowSize(int i, int i2) {
            this.mOptions.minCropWindowWidth = i;
            this.mOptions.minCropWindowHeight = i2;
            return this;
        }

        public ActivityBuilder setMinCropResultSize(int i, int i2) {
            this.mOptions.minCropResultWidth = i;
            this.mOptions.minCropResultHeight = i2;
            return this;
        }

        public ActivityBuilder setMaxCropResultSize(int i, int i2) {
            this.mOptions.maxCropResultWidth = i;
            this.mOptions.maxCropResultHeight = i2;
            return this;
        }

        public ActivityBuilder setActivityTitle(CharSequence charSequence) {
            this.mOptions.activityTitle = charSequence;
            return this;
        }

        public ActivityBuilder setActivityMenuIconColor(int i) {
            this.mOptions.activityMenuIconColor = i;
            return this;
        }

        public ActivityBuilder setOutputUri(Uri uri) {
            this.mOptions.outputUri = uri;
            return this;
        }

        public ActivityBuilder setOutputCompressFormat(Bitmap.CompressFormat compressFormat) {
            this.mOptions.outputCompressFormat = compressFormat;
            return this;
        }

        public ActivityBuilder setOutputCompressQuality(int i) {
            this.mOptions.outputCompressQuality = i;
            return this;
        }

        public ActivityBuilder setRequestedSize(int i, int i2) {
            return setRequestedSize(i, i2, CropImageView.RequestSizeOptions.RESIZE_INSIDE);
        }

        public ActivityBuilder setRequestedSize(int i, int i2, CropImageView.RequestSizeOptions requestSizeOptions) {
            this.mOptions.outputRequestWidth = i;
            this.mOptions.outputRequestHeight = i2;
            this.mOptions.outputRequestSizeOptions = requestSizeOptions;
            return this;
        }

        public ActivityBuilder setNoOutputImage(boolean z) {
            this.mOptions.noOutputImage = z;
            return this;
        }

        public ActivityBuilder setInitialCropWindowRectangle(Rect rect) {
            this.mOptions.initialCropWindowRectangle = rect;
            return this;
        }

        public ActivityBuilder setInitialRotation(int i) {
            this.mOptions.initialRotation = (i + 360) % 360;
            return this;
        }

        public ActivityBuilder setAllowRotation(boolean z) {
            this.mOptions.allowRotation = z;
            return this;
        }

        public ActivityBuilder setAllowFlipping(boolean z) {
            this.mOptions.allowFlipping = z;
            return this;
        }

        public ActivityBuilder setAllowCounterRotation(boolean z) {
            this.mOptions.allowCounterRotation = z;
            return this;
        }

        public ActivityBuilder setRotationDegrees(int i) {
            this.mOptions.rotationDegrees = (i + 360) % 360;
            return this;
        }

        public ActivityBuilder setFlipHorizontally(boolean z) {
            this.mOptions.flipHorizontally = z;
            return this;
        }

        public ActivityBuilder setFlipVertically(boolean z) {
            this.mOptions.flipVertically = z;
            return this;
        }

        public ActivityBuilder setCropMenuCropButtonTitle(CharSequence charSequence) {
            this.mOptions.cropMenuCropButtonTitle = charSequence;
            return this;
        }

        public ActivityBuilder setCropMenuCropButtonIcon(int i) {
            this.mOptions.cropMenuCropButtonIcon = i;
            return this;
        }
    }

    public static final class ActivityResult extends CropImageView.CropResult implements Parcelable {
        public static final Parcelable.Creator<ActivityResult> CREATOR = new Parcelable.Creator<ActivityResult>() {
            public ActivityResult createFromParcel(Parcel parcel) {
                return new ActivityResult(parcel);
            }

            public ActivityResult[] newArray(int i) {
                return new ActivityResult[i];
            }
        };

        public int describeContents() {
            return 0;
        }

        public ActivityResult(Uri uri, Uri uri2, Exception exc, float[] fArr, Rect rect, int i, Rect rect2, int i2) {
            super(null, uri, null, uri2, exc, fArr, rect, rect2, i, i2);
        }

        protected ActivityResult(Parcel parcel) {
            super(null, parcel.readParcelable(Uri.class.getClassLoader()), null, parcel.readParcelable(Uri.class.getClassLoader()), (Exception) parcel.readSerializable(), parcel.createFloatArray(), parcel.readParcelable(Rect.class.getClassLoader()), parcel.readParcelable(Rect.class.getClassLoader()), parcel.readInt(), parcel.readInt());
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(getOriginalUri(), i);
            parcel.writeParcelable(getUri(), i);
            parcel.writeSerializable(getError());
            parcel.writeFloatArray(getCropPoints());
            parcel.writeParcelable(getCropRect(), i);
            parcel.writeParcelable(getWholeImageRect(), i);
            parcel.writeInt(getRotation());
            parcel.writeInt(getSampleSize());
        }
    }
}
