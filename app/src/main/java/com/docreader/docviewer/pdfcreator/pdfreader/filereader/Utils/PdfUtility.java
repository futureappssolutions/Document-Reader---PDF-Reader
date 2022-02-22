package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.ImageModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PdfUtility {
    List<?> items;
    int mBorderWidth = 0;
    int mMarginBottom = 38;
    int mMarginLeft = 50;
    int mMarginRight = 38;
    int mMarginTop = 50;
    String mPageNumStyle = "pg_num_style_page_x_of_n";
    String mPageSize = "A4";

    public String imagesToPdfFile(Context context, List<?> list, String str) {
        File file;
        Image image;
        this.items = list;
        if (Build.VERSION.SDK_INT >= 20) {
            file = new File(Utility.getTempFolderDirectory(context), str);
        } else {
            file = new File(Utility.getDocumentDirPath(context), str);
        }
        try {
            Rectangle rectangle = new Rectangle(PageSize.getRectangle(this.mPageSize));
            rectangle.setBackgroundColor(getBaseColor(-1));
            Document document = new Document(rectangle, (float) this.mMarginLeft, (float) this.mMarginRight, (float) this.mMarginTop, (float) this.mMarginBottom);
            document.setMargins((float) this.mMarginLeft, (float) this.mMarginRight, (float) this.mMarginTop, (float) this.mMarginBottom);
            Rectangle pageSize = document.getPageSize();
            PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            for (int i = 0; i < list.size(); i++) {
                ImageModel imageModel = (ImageModel) list.get(i);
                if (imageModel.getPath().isEmpty()) {
                    image = Image.getInstance(getBytes(context.getContentResolver().openInputStream(imageModel.getUri())));
                } else {
                    image = Image.getInstance(imageModel.getPath());
                }
                double d = (double) 30;
                Double.isNaN(d);
                double d2 = d * 0.09d;
                image.setCompressionLevel((int) d2);
                image.setBorder(15);
                image.setBorderWidth((float) this.mBorderWidth);
                Log.v("Stage 5", "Image compressed " + d2);
                Log.v("Stage 6", "Image path adding");
                image.scaleToFit(document.getPageSize().getWidth() - ((float) (this.mMarginLeft + this.mMarginRight)), document.getPageSize().getHeight() - ((float) (this.mMarginBottom + this.mMarginTop)));
                image.setAbsolutePosition((pageSize.getWidth() - image.getScaledWidth()) / 2.0f, (pageSize.getHeight() - image.getScaledHeight()) / 2.0f);
                Log.v("Stage 7", "Image Alignments");
                addPageNumber(pageSize, instance);
                document.add(image);
                document.newPage();
            }
            document.close();
            return file.getPath();
        } catch (DocumentException | IOException e) {
            Utility.logCatMsg("IOException " + e.getMessage());
            return null;
        }
    }

    public String imagesToPdfFile(Context context, List<?> list, Uri uri) {
        Image image;
        this.items = list;
        try {
            Rectangle rectangle = new Rectangle(PageSize.getRectangle(this.mPageSize));
            rectangle.setBackgroundColor(getBaseColor(-1));
            Document document = new Document(rectangle, (float) this.mMarginLeft, (float) this.mMarginRight, (float) this.mMarginTop, (float) this.mMarginBottom);
            document.setMargins((float) this.mMarginLeft, (float) this.mMarginRight, (float) this.mMarginTop, (float) this.mMarginBottom);
            Rectangle pageSize = document.getPageSize();
            PdfWriter instance = PdfWriter.getInstance(document, context.getContentResolver().openOutputStream(uri));
            document.open();
            for (int i = 0; i < list.size(); i++) {
                ImageModel imageModel = (ImageModel) list.get(i);
                if (imageModel.getPath().isEmpty()) {
                    image = Image.getInstance(getBytes(context.getContentResolver().openInputStream(imageModel.getUri())));
                } else {
                    image = Image.getInstance(imageModel.getPath());
                }
                double d = (double) 30;
                Double.isNaN(d);
                double d2 = d * 0.09d;
                image.setCompressionLevel((int) d2);
                image.setBorder(15);
                image.setBorderWidth((float) this.mBorderWidth);
                Log.v("Stage 5", "Image compressed " + d2);
                Log.v("Stage 6", "Image path adding");
                image.scaleToFit(document.getPageSize().getWidth() - ((float) (this.mMarginLeft + this.mMarginRight)), document.getPageSize().getHeight() - ((float) (this.mMarginBottom + this.mMarginTop)));
                image.setAbsolutePosition((pageSize.getWidth() - image.getScaledWidth()) / 2.0f, (pageSize.getHeight() - image.getScaledHeight()) / 2.0f);
                Log.v("Stage 7", "Image Alignments");
                addPageNumber(pageSize, instance);
                document.add(image);
                document.newPage();
            }
            document.close();
            return imagesToPdfFile(context, list, "temp.pdf");
        } catch (DocumentException | IOException e) {
            Utility.logCatMsg("IOException " + e.getMessage());
            return null;
        }
    }

    private BaseColor getBaseColor(int i) {
        return new BaseColor(Color.red(i), Color.green(i), Color.blue(i));
    }

    private void addPageNumber(Rectangle rectangle, PdfWriter pdfWriter) {
        if (this.mPageNumStyle != null) {
            ColumnText.showTextAligned(pdfWriter.getDirectContent(), 6, getPhrase(pdfWriter, this.mPageNumStyle, this.items.size()), (rectangle.getRight() + rectangle.getLeft()) / 2.0f, rectangle.getBottom() + 25.0f, 0.0f);
        }
    }

    private Phrase getPhrase(PdfWriter pdfWriter, String str, int i) {
        str.hashCode();
        if (str.equals("pg_num_style_page_x_of_n")) {
            return new Phrase(String.format("Page %d of %d", pdfWriter.getPageNumber(), i));
        } else if (!str.equals("pg_num_style_x_of_n")) {
            return new Phrase(String.format("%d", pdfWriter.getPageNumber()));
        } else {
            return new Phrase(String.format("%d of %d", pdfWriter.getPageNumber(), i));
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
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
}
