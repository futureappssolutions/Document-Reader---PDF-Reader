package com.docreader.docviewer.pdfcreator.pdfreader.filereader.PDFView;

import android.content.Context;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.PackagingURIHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import kotlin.io.ByteStreamsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public final class FileUtils {
    public static final FileUtils INSTANCE = new FileUtils();

    private FileUtils() {
    }

    public final File fileFromAsset(Context context, String str) throws IOException {
        File file = new File(context.getCacheDir(), Intrinsics.stringPlus(str, "-pdfview.pdf"));
        if (StringsKt.contains(str, PackagingURIHelper.FORWARD_SLASH_STRING, false)) {
            file.getParentFile().mkdirs();
        }
        InputStream open = context.getAssets().open(str);
        ByteStreamsKt.copyTo(open, new FileOutputStream(file), 0);
        return file;
    }
}
