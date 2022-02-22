package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer.EPubFileViewerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AppActivity extends BaseActivity {
    String fileName = "";
    String filepath = "";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(5);

        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if ("android.intent.action.VIEW".equals(action)) {
                Uri data = intent.getData();
                if (data != null) {
                    filepath = getFilePathForN(data);
                } else {
                    filepath = null;
                }
            } else if ("android.intent.action.SEND".equals(action)) {
                Uri uri = intent.getParcelableExtra("android.intent.extra.STREAM");
                if (uri != null) {
                    filepath = getFilePathForN(uri);
                } else {
                    filepath = null;
                }
            }

            if (filepath != null) {
                int fileType = MainConstant.getFileType(filepath);
                Utility.logCatMsg("fileName " + fileName + " filePath " + filepath + " fileType " + fileType);
                if (!(fileType == -1 || fileType == 5)) {
                    Intent intent2 = new Intent(this, FilesView.class);
                    if (fileType == 3) {
                        intent2 = new Intent(this, PDFViewWebViewBase.class);
                    } else if (fileType == 6) {
                        intent2 = new Intent(this, TextViewer.class);
                    } else if (fileType == 14) {
                        intent2 = new Intent(this, EPubFileViewerActivity.class);
                    }
                    intent2.putExtra("path", filepath);
                    intent2.putExtra("name", fileName);
                    intent2.putExtra("fromConverterApp", false);
                    intent2.putExtra("fileType", fileType + "");
                    intent2.putExtra("fromAppActivity", true);
                    startActivity(intent2);
                }
                finish();
                return;
            }
            Utility.Toast(this, "Unable to open file from here, Go to Document Reader App and try to open from them");
            finish();
        }
    }

    public String getPath(Context context, Uri uri) {
        String[] strArr = {"_data"};
        Cursor query = context.getContentResolver().query(uri, strArr, null, null, null);
        String str = null;
        if (query != null) {
            if (query.moveToFirst()) {
                str = query.getString(query.getColumnIndexOrThrow(strArr[0]));
            }
            query.close();
        }
        return str == null ? "Not found" : str;
    }

    private String getFilePathForN(Uri uri) {
        Cursor query = getContentResolver().query(uri, null, null, null, null);
        if (query == null) {
            String path = uri.getPath();
            fileName = new File(path).getName();
            Utility.logCatMsg("cursor null");
            return path;
        }
        int columnIndex = query.getColumnIndex("_display_name");
        query.moveToFirst();
        if (query.getCount() <= 0) {
            return null;
        }
        fileName = query.getString(columnIndex);
        if (fileName == null) {
            int columnIndex2 = query.getColumnIndex("_data");
            if (columnIndex2 >= 0) {
                return query.getString(columnIndex2);
            }
            return null;
        }
        File file = new File(Utility.getTempFolderDirectory(this), fileName);
        String path2 = file.getPath();
        query.close();
        try {
            InputStream openInputStream = getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[Math.min(openInputStream.available(), 1048576)];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            openInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            Utility.logCatMsg("Exception" + e.getMessage());
        }
        return path2;
    }
}
