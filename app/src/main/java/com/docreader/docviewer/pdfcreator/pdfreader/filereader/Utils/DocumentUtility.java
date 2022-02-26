package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.OnDataLoaderListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public final class DocumentUtility {
    private Integer applicationType = 0;
    private Context context;
    private ArrayList<Object> documentList;
    private ArrayList<Object> ebookFilesList;
    private OnDataLoaderListener onDataLoaderListener;

    public final OnDataLoaderListener getOnDataLoaderListener() {
        return onDataLoaderListener;
    }

    public final void setOnDataLoaderListener(OnDataLoaderListener onDataLoaderListener2) {
        onDataLoaderListener = onDataLoaderListener2;
    }

    public final ArrayList<Object> getDocumentList() {
        return documentList;
    }

    public final void setDocumentList(ArrayList<Object> arrayList) {
        documentList = arrayList;
    }

    public final ArrayList<Object> getEbookFilesList() {
        return ebookFilesList;
    }

    public final void setEbookFilesList(ArrayList<Object> arrayList) {
        ebookFilesList = arrayList;
    }

    public final Context getContext() {
        return context;
    }

    public final void setContext(Context context2) {
        context = context2;
    }

    public final Integer getApplicationType() {
        return applicationType;
    }

    public final void setApplicationType(Integer num) {
        applicationType = num;
    }

    public DocumentUtility(Context context2, ArrayList<Object> arrayList, ArrayList<Object> arrayList2, OnDataLoaderListener onDataLoaderListener2) {
        context = context2;
        documentList = arrayList;
        ebookFilesList = arrayList2;
        onDataLoaderListener = onDataLoaderListener2;
    }

    public final void getAllDocumentFiles() {
        getLocalData();
    }

    public final boolean hasRealRemovableSdCard(Context context2) {
        return ContextCompat.getExternalFilesDirs(context2, (String) null).length >= 2;
    }

    public final String getExternalStoragePath(boolean z) {
        Context context2 = context;
        Object systemService = context2 == null ? null : context2.getSystemService(Context.STORAGE_SERVICE);
        if (systemService != null) {
            StorageManager storageManager = (StorageManager) systemService;
            try {
                Class<?> cls = Class.forName("android.os.storage.StorageVolume");
                Method method = storageManager.getClass().getMethod("getVolumeList");
                Method method2 = cls.getMethod("getPath");
                Method method3 = cls.getMethod("isRemovable");
                Object invoke = method.invoke(storageManager);
                assert invoke != null;
                int length = Array.getLength(invoke);
                if (length > 0) {
                    int i = 0;
                    while (true) {
                        int i2 = i + 1;
                        Object obj = Array.get(invoke, i);
                        Object invoke2 = method2.invoke(obj);
                        if (invoke2 != null) {
                            String str = (String) invoke2;
                            Object invoke3 = method3.invoke(obj);
                            if (invoke3 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type kotlin.Boolean");
                            } else if (z == (Boolean) invoke3) {
                                return str;
                            } else {
                                if (i2 >= length) {
                                    break;
                                }
                                i = i2;
                            }
                        } else {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
                        }
                    }
                }
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.os.storage.StorageManager");
    }

    @SuppressLint({"Range", "Recycle"})
    public final ArrayList<Object> getLocalData() {
        ContentResolver contentResolver;
        Cursor cursor = null;
        try {
            Uri contentUri = MediaStore.Files.getContentUri("external");
            StringBuilder sb = new StringBuilder();
            String[] strArr = {MainConstant.FILE_TYPE_DOC, MainConstant.FILE_TYPE_DOCX, MainConstant.FILE_TYPE_XLS, MainConstant.FILE_TYPE_XLSX, MainConstant.FILE_TYPE_PPT, MainConstant.FILE_TYPE_PPTX, MainConstant.FILE_TYPE_TXT, "pdf", MainConstant.FILE_TYPE_DOT, MainConstant.FILE_TYPE_DOTX, MainConstant.FILE_TYPE_DOTM, MainConstant.FILE_TYPE_RTF, MainConstant.FILE_TYPE_XLT, MainConstant.FILE_TYPE_XLTX, MainConstant.FILE_TYPE_XLSM, MainConstant.FILE_TYPE_XLTM, MainConstant.FILE_TYPE_POT, MainConstant.FILE_TYPE_PPTM, MainConstant.FILE_TYPE_POTX, MainConstant.FILE_TYPE_POTM, MainConstant.FILE_TYPE_CSV, MainConstant.FILE_TYPE_EPUB};
            int i = 0;
            while (true) {
                int i2 = i + 1;
                String str = strArr[i];
                if (Intrinsics.areEqual(str, strArr[21])) {
                    sb.append("_data");
                    sb.append(" LIKE '%.").append(str).append('\'');
                } else {
                    sb.append("_data");
                    sb.append(" LIKE '%.").append(str).append("' OR ");
                }
                if (i2 >= 22) {
                    break;
                }
                i = i2;
            }
            String sb2 = sb.toString();
            if (context != null) {
                contentResolver = context.getContentResolver();
            } else {
                contentResolver = null;
            }
            if (contentResolver != null) {
                cursor = contentResolver.query(contentUri, null, sb2, null, "date_modified");
            }
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String str2 = "";
                    if (cursor.getColumnIndex("_data") != -1) {
                        str2 = cursor.getString(cursor.getColumnIndex("_data"));
                    } else if (cursor.getColumnIndex("_display_name") != -1) {
                        str2 = UriUtils.getPathByUri(context, contentUri);
                        assert str2 != null;
                    }

                    String filename = str2.substring(str2.lastIndexOf("/") + 1);

                    long j = cursor.getLong(cursor.getColumnIndex("_size"));
                    long j2 = cursor.getLong(cursor.getColumnIndex("date_modified"));
                    String fileSize = UriUtils.getFileSize(j);
                    if (((double) j) > 0.0d) {
                        isSelectedFiles(str2);
                        FileModel fileModel = new FileModel();
                        fileModel.setName(filename);
                        fileModel.setPath(str2);
                        fileModel.setFavorite(false);
                        fileModel.setModifiedDate(j2);
                        fileModel.setFileType(applicationType);
                        fileModel.setSize(fileSize);
                        if (applicationType == 14) {
                            ebookFilesList.add(fileModel);
                        } else {
                            if (documentList != null) {
                                documentList.add(fileModel);
                            }
                        }
                    }
                }
            }

            if (onDataLoaderListener != null) {
                onDataLoaderListener.onComplete(documentList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable ignored) {
        }
        return null;
    }

    private void isSelectedFiles(String str) {
        Locale locale = Locale.getDefault();
        if (str != null) {
            String lowerCase = str.toLowerCase(locale);
            if (StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_DOC, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_DOCX, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_DOT, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_DOTX, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_DOTM, false)) {
                applicationType = 0;
            } else if (StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_TXT, false)) {
                applicationType = 4;
            } else if (StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_XLS, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_XLSX, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_XLT, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_XLTX, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_XLTM, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_XLSM, false)) {
                applicationType = 1;
            } else if (StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_PPT, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_PPTX, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_POT, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_PPTM, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_POTX, false) || StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_POTM, false)) {
                applicationType = 2;
            } else if (StringsKt.endsWith(lowerCase, "pdf", false)) {
                applicationType = 3;
            } else if (StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_CSV, false)) {
                applicationType = 10;
                return;
            } else if (StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_RTF, false)) {
                applicationType = 13;
                return;
            } else if (!StringsKt.endsWith(lowerCase, MainConstant.FILE_TYPE_EPUB, false)) {
                return;
            } else {
                applicationType = 14;
                return;
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }
}
