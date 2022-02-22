package com.docreader.docviewer.pdfcreator.pdfreader.filereader.PDFView;

import android.content.Context;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.io.IOException;

public final class PDFView extends SubsamplingScaleImageView {
    private float mScale;
    private File mfile;

    public PDFView(Context context) {
        this(context, null, 2);
    }

    public PDFView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mScale = 8.0f;
        setMinimumTileDpi(120);
        setMinimumScaleType(4);
    }

    public PDFView(Context context, AttributeSet attributeSet, int i) {
        this(context, (i & 2) != 0 ? null : attributeSet);
    }

    public final PDFView fromAsset(String str) {
        FileUtils fileUtils = FileUtils.INSTANCE;
        Context context = getContext();
        try {
            mfile = fileUtils.fileFromAsset(context, str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public final PDFView fromFile(File file) {
        mfile = file;
        return this;
    }

    public final PDFView fromFile(String str) {
        mfile = new File(str);
        return this;
    }

    public final PDFView scale(float f) {
        mScale = f;
        return this;
    }

    public final void show() {
        File file = mfile;
        ImageSource uri = ImageSource.uri(file.getPath());
        setRegionDecoderFactory(() -> {
            File file1 = mfile;
            return new PDFRegionDecoder(PDFView.this, file1, mScale, 0, 8);
        });
        setImage(uri);
    }


    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycle();
    }
}
