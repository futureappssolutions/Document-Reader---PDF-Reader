package com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zip {
    public static List<File> unzip(File file, File file2) {
        FileOutputStream fileOutputStream;
        ArrayList<File> arrayList = new ArrayList<>();
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
            while (true) {
                try {
                    ZipEntry nextEntry = zipInputStream.getNextEntry();
                    if (nextEntry != null) {
                        File file3 = new File(file2.getCanonicalPath(), nextEntry.getName());
                        if (file3.getCanonicalPath().startsWith(file2.getCanonicalPath())) {
                            if (Objects.requireNonNull(file3.getParentFile()).exists() || file3.getParentFile().mkdirs()) {
                                if (!nextEntry.isDirectory()) {
                                    arrayList.add(file3);
                                    fileOutputStream = new FileOutputStream(file3);
                                    byte[] bArr = new byte[1024];
                                    while (true) {
                                        int read = zipInputStream.read(bArr);
                                        if (read <= 0) {
                                            break;
                                        }
                                        fileOutputStream.write(bArr, 0, read);
                                    }
                                    fileOutputStream.close();
                                }
                            }
                        }
                    } else {
                        try {
                            break;
                        } catch (Exception e) {
                            Log.e("Fs", e.getMessage(), e);
                        }
                    }
                } catch (Exception e2) {
                    Log.e("Fs", e2.getMessage(), e2);
                } catch (Throwable th) {
                    try {
                        zipInputStream.close();
                    } catch (Exception e3) {
                        Log.e("Fs", e3.getMessage(), e3);
                    }
                    throw th;
                }
            }
            zipInputStream.close();
        } catch (IOException e4) {
            Log.e("DB", "Copy failed", e4);
        }
        return arrayList;
    }
}
