package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.DynamicGridView;

import android.view.View;

import java.util.List;

public class DynamicGridUtils {
    public static void reorder(List list, int i, int i2) {
        list.add(i2, list.remove(i));
    }

    public static void swap(List list, int i, int i2) {
        Object obj = list.get(i);
        list.set(i, list.get(i2));
        list.set(i2, obj);
    }

    public static float getViewX(View view) {
        return (float) Math.abs((view.getRight() - view.getLeft()) / 2);
    }

    public static float getViewY(View view) {
        return (float) Math.abs((view.getBottom() - view.getTop()) / 2);
    }
}
