package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.exifinterface.media.ExifInterface;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

final class BitmapUtils {
    static final Rect EMPTY_RECT = new Rect();
    static final RectF EMPTY_RECT_F = new RectF();
    static final float[] POINTS = new float[6];
    static final float[] POINTS2 = new float[6];
    static final RectF RECT = new RectF();
    static int mMaxTextureSize;
    static Pair<String, WeakReference<Bitmap>> mStateBitmap;

    BitmapUtils() {
    }

    static RotateBitmapResult rotateBitmapByExif(Bitmap bitmap, Context context, Uri uri) {
        ExifInterface ei = null;
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            if (is != null) {
                ei = new ExifInterface(is);
                is.close();
            }
        } catch (Exception ignored) {
        }
        return ei != null ? rotateBitmapByExif(bitmap, ei) : new RotateBitmapResult(bitmap, 0);
    }

    static RotateBitmapResult rotateBitmapByExif(Bitmap bitmap, ExifInterface exif) {
        int degrees;
        int orientation =
                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degrees = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degrees = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degrees = 270;
                break;
            default:
                degrees = 0;
                break;
        }
        return new RotateBitmapResult(bitmap, degrees);
    }

    static BitmapSampled decodeSampledBitmap(Context context, Uri uri, int reqWidth, int reqHeight) {

        try {
            ContentResolver resolver = context.getContentResolver();

            // First decode with inJustDecodeBounds=true to check dimensions
            BitmapFactory.Options options = decodeImageForOption(resolver, uri);

            if (options.outWidth == -1 && options.outHeight == -1)
                throw new RuntimeException("File is not a picture");

            // Calculate inSampleSize
            options.inSampleSize =
                    Math.max(
                            calculateInSampleSizeByReqestedSize(
                                    options.outWidth, options.outHeight, reqWidth, reqHeight),
                            calculateInSampleSizeByMaxTextureSize(options.outWidth, options.outHeight));

            // Decode bitmap with inSampleSize set
            Bitmap bitmap = decodeImage(resolver, uri, options);

            return new BitmapSampled(bitmap, options.inSampleSize);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load sampled bitmap: " + uri + "\r\n" + e.getMessage(), e);
        }
    }

    static BitmapSampled cropBitmapObjectHandleOOM(
            Bitmap bitmap,
            float[] points,
            int degreesRotated,
            boolean fixAspectRatio,
            int aspectRatioX,
            int aspectRatioY,
            boolean flipHorizontally,
            boolean flipVertically) {
        int scale = 1;
        while (true) {
            try {
                Bitmap cropBitmap =
                        cropBitmapObjectWithScale(
                                bitmap,
                                points,
                                degreesRotated,
                                fixAspectRatio,
                                aspectRatioX,
                                aspectRatioY,
                                1 / (float) scale,
                                flipHorizontally,
                                flipVertically);
                return new BitmapSampled(cropBitmap, scale);
            } catch (OutOfMemoryError e) {
                scale *= 2;
                if (scale > 8) {
                    throw e;
                }
            }
        }
    }

    private static Bitmap cropBitmapObjectWithScale(
            Bitmap bitmap,
            float[] points,
            int degreesRotated,
            boolean fixAspectRatio,
            int aspectRatioX,
            int aspectRatioY,
            float scale,
            boolean flipHorizontally,
            boolean flipVertically) {

        // get the rectangle in original image that contains the required cropped area (larger for non
        // rectangular crop)
        Rect rect =
                getRectFromPoints(
                        points,
                        bitmap.getWidth(),
                        bitmap.getHeight(),
                        fixAspectRatio,
                        aspectRatioX,
                        aspectRatioY);

        // crop and rotate the cropped image in one operation
        Matrix matrix = new Matrix();
        matrix.setRotate(degreesRotated, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        matrix.postScale(flipHorizontally ? -scale : scale, flipVertically ? -scale : scale);
        Bitmap result =
                Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height(), matrix, true);

        if (result == bitmap) {
            // corner case when all bitmap is selected, no worth optimizing for it
            result = bitmap.copy(bitmap.getConfig(), false);
        }

        // rotating by 0, 90, 180 or 270 degrees doesn't require extra cropping
        if (degreesRotated % 90 != 0) {

            // extra crop because non rectangular crop cannot be done directly on the image without
            // rotating first
            result =
                    cropForRotatedImage(
                            result, points, rect, degreesRotated, fixAspectRatio, aspectRatioX, aspectRatioY);
        }

        return result;
    }

    static BitmapSampled cropBitmap(
            Context context,
            Uri loadedImageUri,
            float[] points,
            int degreesRotated,
            int orgWidth,
            int orgHeight,
            boolean fixAspectRatio,
            int aspectRatioX,
            int aspectRatioY,
            int reqWidth,
            int reqHeight,
            boolean flipHorizontally,
            boolean flipVertically) {
        int sampleMulti = 1;
        while (true) {
            try {
                // if successful, just return the resulting bitmap
                return cropBitmap(
                        context,
                        loadedImageUri,
                        points,
                        degreesRotated,
                        orgWidth,
                        orgHeight,
                        fixAspectRatio,
                        aspectRatioX,
                        aspectRatioY,
                        reqWidth,
                        reqHeight,
                        flipHorizontally,
                        flipVertically,
                        sampleMulti);
            } catch (OutOfMemoryError e) {
                // if OOM try to increase the sampling to lower the memory usage
                sampleMulti *= 2;
                if (sampleMulti > 16) {
                    throw new RuntimeException(
                            "Failed to handle OOM by sampling ("
                                    + sampleMulti
                                    + "): "
                                    + loadedImageUri
                                    + "\r\n"
                                    + e.getMessage(),
                            e);
                }
            }
        }
    }

    static float getRectLeft(float[] fArr) {
        return Math.min(Math.min(Math.min(fArr[0], fArr[2]), fArr[4]), fArr[6]);
    }

    static float getRectTop(float[] fArr) {
        return Math.min(Math.min(Math.min(fArr[1], fArr[3]), fArr[5]), fArr[7]);
    }

    static float getRectRight(float[] fArr) {
        return Math.max(Math.max(Math.max(fArr[0], fArr[2]), fArr[4]), fArr[6]);
    }

    static float getRectBottom(float[] fArr) {
        return Math.max(Math.max(Math.max(fArr[1], fArr[3]), fArr[5]), fArr[7]);
    }

    static float getRectWidth(float[] fArr) {
        return getRectRight(fArr) - getRectLeft(fArr);
    }

    static float getRectHeight(float[] fArr) {
        return getRectBottom(fArr) - getRectTop(fArr);
    }

    static float getRectCenterX(float[] fArr) {
        return (getRectRight(fArr) + getRectLeft(fArr)) / 2.0f;
    }

    static float getRectCenterY(float[] fArr) {
        return (getRectBottom(fArr) + getRectTop(fArr)) / 2.0f;
    }

    static Rect getRectFromPoints(
            float[] points,
            int imageWidth,
            int imageHeight,
            boolean fixAspectRatio,
            int aspectRatioX,
            int aspectRatioY) {
        int left = Math.round(Math.max(0, getRectLeft(points)));
        int top = Math.round(Math.max(0, getRectTop(points)));
        int right = Math.round(Math.min(imageWidth, getRectRight(points)));
        int bottom = Math.round(Math.min(imageHeight, getRectBottom(points)));

        Rect rect = new Rect(left, top, right, bottom);
        if (fixAspectRatio) {
            fixRectForAspectRatio(rect, aspectRatioX, aspectRatioY);
        }

        return rect;
    }

    private static void fixRectForAspectRatio(Rect rect, int aspectRatioX, int aspectRatioY) {
        if (aspectRatioX == aspectRatioY && rect.width() != rect.height()) {
            if (rect.height() > rect.width()) {
                rect.bottom -= rect.height() - rect.width();
            } else {
                rect.right -= rect.width() - rect.height();
            }
        }
    }

    static Uri writeTempStateStoreBitmap(Context context, Bitmap bitmap, Uri uri) {
        try {
            boolean needSave = true;
            if (uri == null) {
                uri =
                        Uri.fromFile(
                                File.createTempFile("aic_state_store_temp", ".jpg", context.getCacheDir()));
            } else if (new File(uri.getPath()).exists()) {
                needSave = false;
            }
            if (needSave) {
                writeBitmapToUri(context, bitmap, uri, Bitmap.CompressFormat.JPEG, 95);
            }
            return uri;
        } catch (Exception e) {
            Log.w("AIC", "Failed to write bitmap to temp file for image-cropper save instance state", e);
            return null;
        }
    }

    static void writeBitmapToUri(
            Context context,
            Bitmap bitmap,
            Uri uri,
            Bitmap.CompressFormat compressFormat,
            int compressQuality)
            throws FileNotFoundException {
        OutputStream outputStream = null;
        try {
            outputStream = context.getContentResolver().openOutputStream(uri);
            bitmap.compress(compressFormat, compressQuality, outputStream);
        } finally {
            closeSafe(outputStream);
        }
    }

    static Bitmap resizeBitmap(
            Bitmap bitmap, int reqWidth, int reqHeight, CropImageView.RequestSizeOptions options) {
        try {
            if (reqWidth > 0
                    && reqHeight > 0
                    && (options == CropImageView.RequestSizeOptions.RESIZE_FIT
                    || options == CropImageView.RequestSizeOptions.RESIZE_INSIDE
                    || options == CropImageView.RequestSizeOptions.RESIZE_EXACT)) {

                Bitmap resized = null;
                if (options == CropImageView.RequestSizeOptions.RESIZE_EXACT) {
                    resized = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
                } else {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    float scale = Math.max(width / (float) reqWidth, height / (float) reqHeight);
                    if (scale > 1 || options == CropImageView.RequestSizeOptions.RESIZE_FIT) {
                        resized =
                                Bitmap.createScaledBitmap(
                                        bitmap, (int) (width / scale), (int) (height / scale), false);
                    }
                }
                if (resized != null) {
                    if (resized != bitmap) {
                        bitmap.recycle();
                    }
                    return resized;
                }
            }
        } catch (Exception e) {
            Log.w("AIC", "Failed to resize cropped image, return bitmap before resize", e);
        }
        return bitmap;
    }

    private static BitmapSampled cropBitmap(
            Context context,
            Uri loadedImageUri,
            float[] points,
            int degreesRotated,
            int orgWidth,
            int orgHeight,
            boolean fixAspectRatio,
            int aspectRatioX,
            int aspectRatioY,
            int reqWidth,
            int reqHeight,
            boolean flipHorizontally,
            boolean flipVertically,
            int sampleMulti) {

        // get the rectangle in original image that contains the required cropped area (larger for non
        // rectangular crop)
        Rect rect =
                getRectFromPoints(points, orgWidth, orgHeight, fixAspectRatio, aspectRatioX, aspectRatioY);

        int width = reqWidth > 0 ? reqWidth : rect.width();
        int height = reqHeight > 0 ? reqHeight : rect.height();

        Bitmap result = null;
        int sampleSize = 1;
        try {
            // decode only the required image from URI, optionally sub-sampling if reqWidth/reqHeight is
            // given.
            BitmapSampled bitmapSampled =
                    decodeSampledBitmapRegion(context, loadedImageUri, rect, width, height, sampleMulti);
            result = bitmapSampled.bitmap;
            sampleSize = bitmapSampled.sampleSize;
        } catch (Exception ignored) {
        }

        if (result != null) {
            try {
                // rotate the decoded region by the required amount
                result = rotateAndFlipBitmapInt(result, degreesRotated, flipHorizontally, flipVertically);

                // rotating by 0, 90, 180 or 270 degrees doesn't require extra cropping
                if (degreesRotated % 90 != 0) {

                    // extra crop because non rectangular crop cannot be done directly on the image without
                    // rotating first
                    result =
                            cropForRotatedImage(
                                    result, points, rect, degreesRotated, fixAspectRatio, aspectRatioX, aspectRatioY);
                }
            } catch (OutOfMemoryError e) {
                if (result != null) {
                    result.recycle();
                }
                throw e;
            }
            return new BitmapSampled(result, sampleSize);
        } else {
            // failed to decode region, may be skia issue, try full decode and then crop
            return cropBitmap(
                    context,
                    loadedImageUri,
                    points,
                    degreesRotated,
                    fixAspectRatio,
                    aspectRatioX,
                    aspectRatioY,
                    sampleMulti,
                    rect,
                    width,
                    height,
                    flipHorizontally,
                    flipVertically);
        }
    }

    private static BitmapSampled cropBitmap(
            Context context,
            Uri loadedImageUri,
            float[] points,
            int degreesRotated,
            boolean fixAspectRatio,
            int aspectRatioX,
            int aspectRatioY,
            int sampleMulti,
            Rect rect,
            int width,
            int height,
            boolean flipHorizontally,
            boolean flipVertically) {
        Bitmap result = null;
        int sampleSize;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize =
                    sampleSize =
                            sampleMulti
                                    * calculateInSampleSizeByReqestedSize(rect.width(), rect.height(), width, height);

            Bitmap fullBitmap = decodeImage(context.getContentResolver(), loadedImageUri, options);
            if (fullBitmap != null) {
                try {
                    // adjust crop points by the sampling because the image is smaller
                    float[] points2 = new float[points.length];
                    System.arraycopy(points, 0, points2, 0, points.length);
                    for (int i = 0; i < points2.length; i++) {
                        points2[i] = points2[i] / options.inSampleSize;
                    }

                    result =
                            cropBitmapObjectWithScale(
                                    fullBitmap,
                                    points2,
                                    degreesRotated,
                                    fixAspectRatio,
                                    aspectRatioX,
                                    aspectRatioY,
                                    1,
                                    flipHorizontally,
                                    flipVertically);
                } finally {
                    if (result != fullBitmap) {
                        fullBitmap.recycle();
                    }
                }
            }
        } catch (OutOfMemoryError e) {
            if (result != null) {
                result.recycle();
            }
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load sampled bitmap: " + loadedImageUri + "\r\n" + e.getMessage(), e);
        }
        return new BitmapSampled(result, sampleSize);
    }

    private static BitmapFactory.Options decodeImageForOption(ContentResolver resolver, Uri uri)
            throws FileNotFoundException {
        InputStream stream = null;
        try {
            stream = resolver.openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(stream, EMPTY_RECT, options);
            options.inJustDecodeBounds = false;
            return options;
        } finally {
            closeSafe(stream);
        }
    }

    private static Bitmap decodeImage(
            ContentResolver resolver, Uri uri, BitmapFactory.Options options)
            throws FileNotFoundException {
        do {
            InputStream stream = null;
            try {
                stream = resolver.openInputStream(uri);
                return BitmapFactory.decodeStream(stream, EMPTY_RECT, options);
            } catch (OutOfMemoryError e) {
                options.inSampleSize *= 2;
            } finally {
                closeSafe(stream);
            }
        } while (options.inSampleSize <= 512);
        throw new RuntimeException("Failed to decode image: " + uri);
    }

    private static BitmapSampled decodeSampledBitmapRegion(
            Context context, Uri uri, Rect rect, int reqWidth, int reqHeight, int sampleMulti) {
        InputStream stream = null;
        BitmapRegionDecoder decoder = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize =
                    sampleMulti
                            * calculateInSampleSizeByReqestedSize(
                            rect.width(), rect.height(), reqWidth, reqHeight);

            stream = context.getContentResolver().openInputStream(uri);
            decoder = BitmapRegionDecoder.newInstance(stream, false);
            do {
                try {
                    return new BitmapSampled(decoder.decodeRegion(rect, options), options.inSampleSize);
                } catch (OutOfMemoryError e) {
                    options.inSampleSize *= 2;
                }
            } while (options.inSampleSize <= 512);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load sampled bitmap: " + uri + "\r\n" + e.getMessage(), e);
        } finally {
            closeSafe(stream);
            if (decoder != null) {
                decoder.recycle();
            }
        }
        return new BitmapSampled(null, 1);
    }

    private static Bitmap cropForRotatedImage(
            Bitmap bitmap,
            float[] points,
            Rect rect,
            int degreesRotated,
            boolean fixAspectRatio,
            int aspectRatioX,
            int aspectRatioY) {
        if (degreesRotated % 90 != 0) {

            int adjLeft = 0, adjTop = 0, width = 0, height = 0;
            double rads = Math.toRadians(degreesRotated);
            int compareTo =
                    degreesRotated < 90 || (degreesRotated > 180 && degreesRotated < 270)
                            ? rect.left
                            : rect.right;
            for (int i = 0; i < points.length; i += 2) {
                if (points[i] >= compareTo - 1 && points[i] <= compareTo + 1) {
                    adjLeft = (int) Math.abs(Math.sin(rads) * (rect.bottom - points[i + 1]));
                    adjTop = (int) Math.abs(Math.cos(rads) * (points[i + 1] - rect.top));
                    width = (int) Math.abs((points[i + 1] - rect.top) / Math.sin(rads));
                    height = (int) Math.abs((rect.bottom - points[i + 1]) / Math.cos(rads));
                    break;
                }
            }

            rect.set(adjLeft, adjTop, adjLeft + width, adjTop + height);
            if (fixAspectRatio) {
                fixRectForAspectRatio(rect, aspectRatioX, aspectRatioY);
            }

            Bitmap bitmapTmp = bitmap;
            bitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
            if (bitmapTmp != bitmap) {
                bitmapTmp.recycle();
            }
        }
        return bitmap;
    }

    private static int calculateInSampleSizeByReqestedSize(
            int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            while ((height / 2 / inSampleSize) > reqHeight && (width / 2 / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static int calculateInSampleSizeByMaxTextureSize(int width, int height) {
        int inSampleSize = 1;
        if (mMaxTextureSize == 0) {
            mMaxTextureSize = getMaxTextureSize();
        }
        if (mMaxTextureSize > 0) {
            while ((height / inSampleSize) > mMaxTextureSize
                    || (width / inSampleSize) > mMaxTextureSize) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateAndFlipBitmapInt(
            Bitmap bitmap, int degrees, boolean flipHorizontally, boolean flipVertically) {
        if (degrees > 0 || flipHorizontally || flipVertically) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            matrix.postScale(flipHorizontally ? -1 : 1, flipVertically ? -1 : 1);
            Bitmap newBitmap =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            if (newBitmap != bitmap) {
                bitmap.recycle();
            }
            return newBitmap;
        } else {
            return bitmap;
        }
    }

    private static int getMaxTextureSize() {
        final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

        try {
            EGL10 egl = (EGL10) EGLContext.getEGL();
            EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

            int[] version = new int[2];
            egl.eglInitialize(display, version);

            int[] totalConfigurations = new int[1];
            egl.eglGetConfigs(display, null, 0, totalConfigurations);

            EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
            egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

            int[] textureSize = new int[1];
            int maximumTextureSize = 0;

            for (int i = 0; i < totalConfigurations[0]; i++) {
                egl.eglGetConfigAttrib(
                        display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

                if (maximumTextureSize < textureSize[0]) {
                    maximumTextureSize = textureSize[0];
                }
            }

            egl.eglTerminate(display);

            return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
        } catch (Exception e) {
            return IMAGE_MAX_BITMAP_DIMENSION;
        }
    }

    private static void closeSafe(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    static final class BitmapSampled {
        public final Bitmap bitmap;
        final int sampleSize;

        BitmapSampled(Bitmap bitmap, int sampleSize) {
            this.bitmap = bitmap;
            this.sampleSize = sampleSize;
        }
    }

    static final class RotateBitmapResult {

        public final Bitmap bitmap;

        final int degrees;

        RotateBitmapResult(Bitmap bitmap, int degrees) {
            this.bitmap = bitmap;
            this.degrees = degrees;
        }
    }
}
