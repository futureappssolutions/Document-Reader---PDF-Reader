package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.FilesView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.PDFViewWebViewBase;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.CvsTask;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.UriInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Utility {

    public static void logCatMsg(String str) {
    }

    public static void Toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    public static boolean isServiceRunning(Context context, Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void HideTitleBarBackground(Activity activity, boolean z) {
        Window window = activity.getWindow();
        window.getAttributes();
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(0);
        View decorView = window.getDecorView();
        if (z) {
            if (Build.VERSION.SDK_INT >= 23) {
                decorView.setSystemUiVisibility(9472);
                return;
            }
            window.setStatusBarColor(-16777216);
        }
        decorView.setSystemUiVisibility(1280);
    }

    public static String getFileDateTime(Long l) {
        return new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US).format(new Date(l));
    }

    public static String geDateTime(Long l) {
        return new SimpleDateFormat(" hh:mm, dd MMM", Locale.US).format(new Date(l));
    }

    public static String geDateTime(Long l, String str) {
        return new SimpleDateFormat(str, Locale.US).format(new Date(l));
    }

    public static String getLastModifyDate(Long l) {
        return new SimpleDateFormat("MMM dd | hh:mm a", Locale.US).format(new Date(l));
    }

    public static String getDateTime() {
        @SuppressLint("SimpleDateFormat") String format = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        return "PDF_" + format;
    }

    public static String getFileSize(File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if (file.isFile()) {
            double length = (double) file.length();
            if (length > 1048576.0d) {
                StringBuilder sb = new StringBuilder();
                Double.isNaN(length);
                sb.append(decimalFormat.format(length / 1048576.0d));
                sb.append(" MB");
                return sb.toString();
            } else if (length > 1024.0d) {
                StringBuilder sb2 = new StringBuilder();
                Double.isNaN(length);
                sb2.append(decimalFormat.format(length / 1024.0d));
                sb2.append(" KB");
                return sb2.toString();
            } else {
                return decimalFormat.format(length) + " B";
            }
        } else {
            throw new IllegalArgumentException("Expected a file");
        }
    }

    public static String getCurrentDateTime() {
        return System.currentTimeMillis() + "";
    }

    public static String writeStringAsFile(Context context, String str, Uri uri) {
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(openFileDescriptor.getFileDescriptor());
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();
            openFileDescriptor.close();
            File file = new File(getTempFolderDirectory(context), "temp.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(str);
            fileWriter.close();
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String writeStringAsFile(Context context, String str, String str2) {
        File file = new File(getDocumentDirPath(context), str2);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(str);
            fileWriter.close();
            return file.getPath();
        } catch (IOException e) {
            logCatMsg("IOException " + e.getMessage());
            return null;
        }
    }

    public static String writeStringAsPDF(Context context, String str, Uri uri) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, context.getContentResolver().openOutputStream(uri));
            document.open();
            document.add(new Paragraph(str));
            document.close();
            try {
                File file = new File(getTempFolderDirectory(context), "temp.pdf");
                Document document2 = new Document();
                PdfWriter.getInstance(document2, new FileOutputStream(file));
                document2.open();
                document2.add(new Paragraph(str));
                document2.close();
                return file.getPath();
            } catch (DocumentException | IOException e) {
                logCatMsg("IOException " + e.getMessage());
                return null;
            }
        } catch (DocumentException | IOException e2) {
            logCatMsg("IOException " + e2.getMessage());
            return null;
        }
    }

    public static String writeStringAsPDF(Context context, String str, String str2, String str3) {
        File file = new File(getDocumentDirPath(context), str3);
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            if (str.length() > 0) {
                document.add(new Paragraph(str));
                document.add(new Paragraph(""));
            }
            document.add(new Paragraph(str2));
            document.close();
            return file.getPath();
        } catch (DocumentException | IOException e) {
            logCatMsg("IOException " + e.getMessage());
            return null;
        }
    }

    public static void deleteTempFolder(Context context) {
        File tempFolderDirectory = getTempFolderDirectory(context);
        if (tempFolderDirectory.exists()) {
            deleteDir(tempFolderDirectory);
        }
    }

    public static void openStringOrPDFAsFile(Context context, String str, int i) {
        Intent intent;
        File file = new File(getTempFolderDirectory(context), i != 0 ? "temp.pdf" : "temp.txt");
        if (i == 0) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(str);
                fileWriter.close();
                intent = new Intent(context, FilesView.class);
                intent.putExtra("fileType", "4");
                intent.putExtra("name", "Text File");
            } catch (IOException e) {
                logCatMsg("IOException " + e.getMessage());
                return;
            }
        } else {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }
            document.open();
            try {
                document.add(new Paragraph(str));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            document.close();
            intent = new Intent(context, PDFViewWebViewBase.class);
            intent.putExtra("fileType", "3");
            intent.putExtra("name", "PDF File");
        }
        intent.putExtra("path", file.getPath());
        context.startActivity(intent);
    }

    public static File getTempFolderDirectory(Context context) {
        File file = new File(context.getFilesDir() + "/temp");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static boolean deleteDir(File file) {
        if (file.isDirectory()) {
            String[] list = file.list();
            for (String file2 : Objects.requireNonNull(list)) {
                if (!deleteDir(new File(file, file2))) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static void vibratePhone(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, -1));
        } else {
            vibrator.vibrate(50);
        }
    }

    public static String getFileToByte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertToBase64String(Context context, Uri uri) {
        try {
            return Base64.encodeToString(getBytes(context.getContentResolver().openInputStream(uri)), 0);
        } catch (Exception e) {
            e.printStackTrace();
            logCatMsg("Exception " + e.toString());
            return "";
        }
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static String fmt(double d) {
        return new DecimalFormat("###.##").format(d);
    }

    public static File getDocumentDirPath(Context context) {
        File file = new File(context.getFilesDir() + context.getResources().getString(R.string.app_folder_name));
        if (Build.VERSION.SDK_INT < 20) {
            file = new File(Environment.getExternalStorageDirectory() + context.getResources().getString(R.string.app_folder_name));
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static UriInfo getUriInfo(Context context, Uri uri) {
        UriInfo uriInfo = new UriInfo();
        Cursor query = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        if (query != null) {
            int columnIndex = query.getColumnIndex("_data");
            int columnIndex2 = query.getColumnIndex("_display_name");
            query.moveToFirst();
            if (query.getCount() > 0) {
                String string = query.getString(columnIndex2);
                if (string != null || columnIndex <= 0) {
                    logCatMsg("file Name " + string + "  :   dataPath " + "");
                    File file = new File(getTempFolderDirectory(context), Objects.requireNonNull(string));
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
                                uriInfo.setFileName(string);
                                uriInfo.setFilePath(path);
                                return uriInfo;
                            }
                        }
                    } catch (Exception e) {
                        logCatMsg("Exception in getFilePath" + e.getMessage());
                    }
                } else {
                    String string2 = query.getString(columnIndex);
                    logCatMsg("file Name " + string + "  :   filePath " + string2);
                    uriInfo.setFileName(string);
                    uriInfo.setFilePath(string2);
                    return uriInfo;
                }
            }
        }
        return null;
    }

    public static String csvToPDF(Context context, ArrayList<List<String>> arrayList, Uri uri, CvsTask cvsTask) {
        try {
            PdfPTable pdfPTable = new PdfPTable(arrayList.get(1).size());
            pdfPTable.setWidthPercentage(100.0f);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                List list = arrayList.get(i);
                for (int i2 = 0; i2 < list.size(); i2++) {
                    PdfPCell pdfPCell = new PdfPCell(new Phrase((String) list.get(i2)));
                    pdfPCell.setPadding(5.0f);
                    pdfPCell.setHorizontalAlignment(1);
                    pdfPCell.setVerticalAlignment(5);
                    pdfPTable.addCell(pdfPCell);
                }
                i++;
                double d = (double) (i * 100);
                double d2 = (double) size;
                Double.isNaN(d);
                Double.isNaN(d2);
                cvsTask.onProgress((int) (d / d2));
            }
            if (uri != null) {
                Document document = new Document();
                PdfWriter.getInstance(document, context.getContentResolver().openOutputStream(uri));
                document.open();
                document.add(pdfPTable);
                document.close();
            }
            File file = new File(getTempFolderDirectory(context), "temp.pdf");
            Document document2 = new Document();
            PdfWriter.getInstance(document2, new FileOutputStream(file));
            document2.open();
            document2.add(pdfPTable);
            document2.close();
            return file.getPath();
        } catch (DocumentException | IOException e) {
            logCatMsg("IOException " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("WrongConstant")
    public static void shareFile(Context context, String str) {
        File file = new File(str);
        Context applicationContext = context.getApplicationContext();
        applicationContext.getClass();
        Uri uriForFile = FileProvider.getUriForFile(applicationContext, context.getApplicationContext().getPackageName() + ".provider", file);
        if (uriForFile != null) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.addFlags(1);
            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.shareUsing)));
        }
    }
}
