package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.os.AsyncTask;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.MergeFilesListener;

import java.io.FileOutputStream;

public class MergePdf extends AsyncTask<String, Void, Void> {
    private String mFilename;
    private String mFinPath;
    private Boolean mIsPDFMerged;
    private final boolean mIsPasswordProtected;
    private final String mMasterPwd;
    private final MergeFilesListener mMergeFilesListener;
    private final String mPassword;

    public MergePdf(String str, String str2, boolean z, String str3, MergeFilesListener mergeFilesListener, String str4) {
        mFilename = str;
        mFinPath = str2;
        mMergeFilesListener = mergeFilesListener;
        mIsPasswordProtected = z;
        mPassword = str3;
        mMasterPwd = str4;
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
            mFilename += ".pdf";
            mFinPath += mFilename;
            PdfCopy pdfCopy = new PdfCopy(document, new FileOutputStream(mFinPath));
            if (mIsPasswordProtected) {
                pdfCopy.setEncryption(mPassword.getBytes(), mMasterPwd.getBytes(), 2068, 2);
            }
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
        mMergeFilesListener.resetValues(mIsPDFMerged, mFinPath, mFilename);
    }
}
