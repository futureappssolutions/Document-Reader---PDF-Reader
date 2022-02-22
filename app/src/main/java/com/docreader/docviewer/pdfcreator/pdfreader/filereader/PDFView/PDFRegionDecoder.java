package com.docreader.docviewer.pdfcreator.pdfreader.filereader.PDFView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.EventConstant;

import java.io.File;
import java.io.IOException;

import kotlin.jvm.internal.Intrinsics;


public final class PDFRegionDecoder implements ImageRegionDecoder {
    private int pageHeight;
    private int pageWidth;
    private final int backgroundColorPdf;
    private final float scale;
    private final File file;
    private final PDFView view;
    private PdfRenderer renderer;
    private ParcelFileDescriptor descriptor;

    public PDFRegionDecoder(PDFView pDFView, File file2, float f, int i) {
        view = pDFView;
        file = file2;
        scale = f;
        backgroundColorPdf = i;
    }

    public PDFRegionDecoder(PDFView pDFView, File file2, float f, int i, int i2) {
        this(pDFView, file2, f, (i2 & 8) != 0 ? -1 : i);
    }

    public Point init(Context context, Uri uri) throws Exception {
        descriptor = ParcelFileDescriptor.open(file, EventConstant.FILE_CREATE_FOLDER_ID);
        if (descriptor != null) {
            renderer = new PdfRenderer(descriptor);
            PdfRenderer.Page openPage = renderer.openPage(0);
            pageWidth = (int) (((float) openPage.getWidth()) * scale);
            pageHeight = (int) (((float) openPage.getHeight()) * scale);

            if (renderer != null) {
                if (renderer.getPageCount() == 1) {
                    view.setMinimumScaleType(1);
                }
                openPage.close();
                int i = pageWidth;
                int i2 = pageHeight;
                if (renderer != null) {
                    return new Point(i, i2 * renderer.getPageCount());
                }
                Intrinsics.throwUninitializedPropertyAccessException("renderer");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("renderer");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("descriptor");
        throw null;
    }

    @SuppressLint("WrongConstant")
    public Bitmap decodeRegion(Rect rect, int i) {
        Rect rect2 = rect;
        int i2 = i;
        double d = rect2.top;
        double d2 = pageHeight;
        Double.isNaN(d);
        Double.isNaN(d2);
        int floor = (int) Math.floor(d / d2);
        double d3 = rect2.bottom;
        double d4 = pageHeight;
        Double.isNaN(d3);
        Double.isNaN(d4);
        int ceil = ((int) Math.ceil(d3 / d4)) - 1;
        Bitmap createBitmap = Bitmap.createBitmap(rect.width() / i2, rect.height() / i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(backgroundColorPdf);
        canvas.drawBitmap(createBitmap, 0.0f, 0.0f, null);
        if (floor <= ceil) {
            int i3 = 0;
            int i4 = floor;
            while (true) {
                int i5 = i3 + 1;
                int i6 = i4 + 1;
                if (renderer != null) {
                    synchronized (renderer) {
                        if (renderer != null) {
                            PdfRenderer.Page openPage = renderer.openPage(i4);
                            Matrix matrix = new Matrix();
                            float f = scale;
                            float f2 = (float) i2;
                            matrix.setScale(f / f2, f / f2);
                            int i7 = rect2.top;
                            int i8 = pageHeight;
                            matrix.postTranslate((float) ((-rect2.left) / i2), (-((float) ((i7 - (i8 * floor)) / i2))) + ((((float) i8) / f2) * ((float) i3)));
                            openPage.render(createBitmap, null, matrix, 1);
                            openPage.close();
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("renderer");
                            throw null;
                        }
                    }
                    if (i4 == ceil) {
                        break;
                    }
                    rect2 = rect;
                    i3 = i5;
                    i4 = i6;
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("renderer");
                    throw null;
                }
            }
        }
        Intrinsics.checkNotNullExpressionValue(createBitmap, "bitmap");
        return createBitmap;
    }

    public boolean isReady() {
        return pageWidth > 0 && pageHeight > 0;
    }

    public void recycle() {
        PdfRenderer pdfRenderer = renderer;
        if (pdfRenderer != null) {
            pdfRenderer.close();
            ParcelFileDescriptor parcelFileDescriptor = descriptor;
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pageWidth = 0;
                pageHeight = 0;
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("descriptor");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("renderer");
        throw null;
    }
}
