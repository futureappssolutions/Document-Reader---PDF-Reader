package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.MergeFilesListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

public class MergePdfMethodTwo extends AsyncTask<String, Void, Void> {
    public final MergeFilesListener mMergeFilesListener;
    public Context context;
    public FileModel fileModel;
    public Boolean mIsPDFMerged;
    public Uri uri;

    public MergePdfMethodTwo(Context context2, Uri uri2, MergeFilesListener mergeFilesListener) {
        uri = uri2;
        context = context2;
        mMergeFilesListener = mergeFilesListener;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        mIsPDFMerged = false;
        mMergeFilesListener.mergeStarted();
    }

    @Override
    public Void doInBackground(String... strArr) {
        try {
            Document document = new Document();
            PdfCopy pdfCopy = new PdfCopy(document, context.getContentResolver().openOutputStream(uri));
            document.open();
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    PdfReader pdfReader = new PdfReader(strArr[i]);
                    int numberOfPages = pdfReader.getNumberOfPages();
                    for (int i2 = 1; i2 <= numberOfPages; i2++) {
                        pdfCopy.addPage(pdfCopy.getImportedPage(pdfReader, i2));
                    }
                    i++;
                } else {
                    mIsPDFMerged = true;
                    document.close();
                    fileModel = getFilePathForN();
                    return null;
                }
            }
        } catch (Exception e) {
            mIsPDFMerged = false;
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onPostExecute(Void voidR) {
        super.onPostExecute(voidR);
        if (fileModel != null) {
            mMergeFilesListener.resetValues(mIsPDFMerged, fileModel.getPath(), fileModel.getName());
        } else {
            mMergeFilesListener.resetValues(mIsPDFMerged, "", "");
        }
    }

    private FileModel getFilePathForN() {
        FileModel fileModel2 = new FileModel();
        fileModel2.setSelected(false);
        Cursor query = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        if (query != null) {
            int columnIndex = query.getColumnIndex("_data");
            int columnIndex2 = query.getColumnIndex("_display_name");
            query.moveToFirst();
            if (query.getCount() > 0) {
                String string = query.getString(columnIndex2);
                if (string != null || columnIndex <= 0) {
                    Utility.logCatMsg("file Name " + string + "  :   dataPath " + "");
                    File file = new File(Utility.getTempFolderDirectory(context), Objects.requireNonNull(string));
                    String path = file.getPath();
                    query.close();
                    try {
                        InputStream openInputStream = context.getContentResolver().openInputStream(uri);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] bArr = new byte[Math.min(openInputStream.available(), 1048576)];
                        while (true) {
                            int read = openInputStream.read(bArr);
                            if (read != -1) {
                                fileOutputStream.write(bArr, 0, read);
                            } else {
                                openInputStream.close();
                                fileOutputStream.close();
                                fileModel2.setPath(path);
                                fileModel2.setName(string);
                                return fileModel2;
                            }
                        }
                    } catch (Exception e) {
                        Utility.logCatMsg("Exception in getFilePath" + e.getMessage());
                    }
                } else {
                    String string2 = query.getString(columnIndex);
                    Utility.logCatMsg("file Name " + string + "  :   dataPath " + string2);
                    fileModel2.setPath(string2);
                    fileModel2.setName(string);
                    return fileModel2;
                }
            }
        }
        return null;
    }
}
