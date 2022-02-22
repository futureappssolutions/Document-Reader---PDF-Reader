package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.itextpdf.text.Annotation;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.PackagingURIHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UriUtils {
    private static final String TAG = "UriUtils";

    public static String getPathByUri(Context context, Uri uri) {
        Uri uri2;
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri)) {
            int i = 0;
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + PackagingURIHelper.FORWARD_SLASH_STRING + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                if (documentId.startsWith("raw:")) {
                    return documentId.replaceFirst("raw:", "");
                }
                String[] strArr = {"content://downloads/public_downloads", "content://downloads/my_downloads", "content://downloads/all_downloads"};
                while (i < 3) {
                    try {
                        String dataColumn = getDataColumn(context, ContentUris.withAppendedId(Uri.parse(strArr[i]), Long.parseLong(documentId)), (String) null, (String[]) null);
                        if (dataColumn != null) {
                            return dataColumn;
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return getPathByCopyFile(context, uri);
            } else if (isMediaDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                String str = split2[0];
                if ("image".equals(str)) {
                    uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(str)) {
                    uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(str)) {
                    uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    uri2 = MediaStore.Files.getContentUri("external");
                }
                String dataColumn2 = getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
                return TextUtils.isEmpty(dataColumn2) ? getPathByCopyFile(context, uri) : dataColumn2;
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            String dataColumn3 = getDataColumn(context, uri, (String) null, (String[]) null);
            return TextUtils.isEmpty(dataColumn3) ? getPathByCopyFile(context, uri) : dataColumn3;
        } else if (Annotation.FILE.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static String getPathByCopyFile(Context context, Uri uri) {
        File generateFileName = generateFileName(getFileName(context, uri), Utility.getTempFolderDirectory(context));
        if (generateFileName == null) {
            return null;
        }
        String absolutePath = generateFileName.getAbsolutePath();
        saveFileFromUri(context, uri, absolutePath);
        return absolutePath;
    }

    private static File generateFileName(String str, File file) {
        String str2;
        if (str == null) {
            return null;
        }
        File file2 = new File(file, str);
        if (file2.exists()) {
            int lastIndexOf = str.lastIndexOf(46);
            int i = 0;
            if (lastIndexOf > 0) {
                String substring = str.substring(0, lastIndexOf);
                str2 = str.substring(lastIndexOf);
                str = substring;
            } else {
                str2 = "";
            }
            while (file2.exists()) {
                i++;
                file2 = new File(file, str + '(' + i + ')' + str2);
            }
        }
        try {
            if (!file2.createNewFile()) {
                return null;
            }
            return file2;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getFileName(Context context, Uri uri) {
        if (context.getContentResolver().getType(uri) == null) {
            return getName(uri.toString());
        }
        Cursor query = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        if (query == null) {
            return null;
        }
        int columnIndex = query.getColumnIndex("_display_name");
        query.moveToFirst();
        String string = query.getString(columnIndex);
        query.close();
        return string;
    }

    private static String getName(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(str.lastIndexOf(47) + 1);
    }

    private static void saveFileFromUri(Context context, Uri uri, String str) {
        Throwable th;
        BufferedOutputStream bufferedOutputStream;
        IOException e;
        InputStream inputStream = null;
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(str, false));
                try {
                    byte[] bArr = new byte[1024];
                    openInputStream.read(bArr);
                    do {
                        bufferedOutputStream.write(bArr);
                    } while (openInputStream.read(bArr) != -1);
                    if (openInputStream != null) {
                        try {
                            openInputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            return;
                        }
                    }
                    bufferedOutputStream.close();
                } catch (IOException e3) {
                    e = e3;
                    inputStream = openInputStream;
                    try {
                        e.printStackTrace();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (bufferedOutputStream != null) {
                            bufferedOutputStream.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                                throw th;
                            }
                        }
                        if (bufferedOutputStream != null) {
                            bufferedOutputStream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    inputStream = openInputStream;
                    if (inputStream != null) {
                    }
                    if (bufferedOutputStream != null) {
                    }
                    throw th;
                }
            } catch (IOException e5) {
                e = e5;
                bufferedOutputStream = null;
                inputStream = openInputStream;
                e.printStackTrace();
                if (inputStream != null) {
                }
                if (bufferedOutputStream != null) {
                }
            } catch (Throwable th4) {
                th = th4;
                bufferedOutputStream = null;
                inputStream = openInputStream;
                if (inputStream != null) {
                }
                if (bufferedOutputStream != null) {
                }
                throw th;
            }
        } catch (IOException e6) {
            e = e6;
            bufferedOutputStream = null;
            e.printStackTrace();
            if (inputStream != null) {
            }
            if (bufferedOutputStream != null) {
            }
        } catch (Throwable th5) {
            th = th5;
            bufferedOutputStream = null;
            if (inputStream != null) {
            }
            if (bufferedOutputStream != null) {
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Throwable th;
        IllegalArgumentException e;
        Cursor cursor;
        Cursor cursor2 = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                        if (cursor != null) {
                            cursor.close();
                        }
                        return string;
                    }
                } catch (IllegalArgumentException e2) {
                    e = e2;
                    try {
                        Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", e.getMessage()));
                    } catch (Throwable th2) {
                        th = th2;
                        cursor2 = cursor;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        throw th;
                    }
                }
            }
        } catch (IllegalArgumentException e3) {
            e = e3;
            cursor = null;
            Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", e.getMessage()));
        } catch (Throwable th3) {
            th = th3;
            if (cursor2 != null) {
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return str;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isGooglePlayPhotosUri(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }

    public static Uri getUriByPath(Context context, String str) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri contentUri = MediaStore.Files.getContentUri("external");
        Cursor query = contentResolver.query(contentUri, new String[]{"_id"}, "_data=? ", new String[]{str}, (String) null);
        if (query != null && query.moveToFirst()) {
            @SuppressLint("Range") int i = query.getInt(query.getColumnIndex("_id"));
            query.close();
            return Uri.withAppendedPath(contentUri, "" + i);
        } else if (!new File(str).exists()) {
            return null;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", str);
            return contentResolver.insert(contentUri, contentValues);
        }
    }

    public static String getFileSize(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double valueOf = Double.parseDouble(decimalFormat.format(j));
        if (valueOf < 1024.0d) {
            return valueOf + " B";
        }
        double d = (double) j;
        Double.isNaN(d);
        double valueOf2 = Double.parseDouble(decimalFormat.format(d / 1024.0d));
        if (valueOf2 < 1024.0d) {
            return valueOf2 + " KB";
        }
        Double.isNaN(d);
        double valueOf3 = Double.parseDouble(decimalFormat.format(d / 1048576.0d));
        if (valueOf3 < 1024.0d) {
            return valueOf3 + " MB";
        }
        Double.isNaN(d);
        double valueOf4 = Double.parseDouble(decimalFormat.format(d / 1.073741824E9d));
        if (valueOf4 >= 1024.0d) {
            return ">1TB";
        }
        return valueOf4 + " GB";
    }

    public static String getFormatFileDate(long j) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(j));
    }
}
