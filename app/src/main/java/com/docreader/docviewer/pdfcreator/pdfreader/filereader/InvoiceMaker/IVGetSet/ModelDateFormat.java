package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ModelDateFormat {
    String dateFormat;
    String dateFormatTime;

    public ModelDateFormat() {
    }

    public ModelDateFormat(String str, String str2) {
        this.dateFormat = str;
        this.dateFormatTime = str2;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormat = str;
    }

    public String getDateFormatTime() {
        return this.dateFormatTime;
    }

    public void setDateFormatTime(String str) {
        this.dateFormatTime = str;
    }

    public ArrayList<ModelDateFormat> getAllDateFormat() {
        ArrayList<ModelDateFormat> arrayList = new ArrayList<>();
        arrayList.add(new ModelDateFormat("yyy-MM-dd", getCurrentDate("yyy-MM-dd")));
        arrayList.add(new ModelDateFormat("dd-MM-yyy", getCurrentDate("dd-MM-yyy")));
        arrayList.add(new ModelDateFormat("MM-dd-yyy", getCurrentDate("MM-dd-yyy")));
        arrayList.add(new ModelDateFormat("yyy/MM/dd", getCurrentDate("yyy/MM/dd")));
        arrayList.add(new ModelDateFormat("dd/MM/yyy", getCurrentDate("dd/MM/yyy")));
        arrayList.add(new ModelDateFormat("MM/dd/yyy", getCurrentDate("MM/dd/yyy")));
        arrayList.add(new ModelDateFormat("yyy.MM.dd", getCurrentDate("yyy.MM.dd")));
        arrayList.add(new ModelDateFormat("dd.MM.yyy", getCurrentDate("dd.MM.yyy")));
        arrayList.add(new ModelDateFormat("MM.dd.yyy", getCurrentDate("MM.dd.yyy")));
        arrayList.add(new ModelDateFormat("MMM d, yyy", getCurrentDate("MMM d, yyy")));
        arrayList.add(new ModelDateFormat("d MMM yyy", getCurrentDate("d MMM yyy")));
        return arrayList;
    }

    public String getCurrentDate(String str) {
        return new SimpleDateFormat(str, Locale.US).format(new Date());
    }
}
