package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.UI;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.Cell;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.ColumnHeader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.RowHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TableViewModel {
    public static final int GIRL = 2;
    public static final int SAD = 1;

    private List<RowHeader> getSimpleRowHeaderList() {
        ArrayList<RowHeader> arrayList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            String valueOf = String.valueOf(i);
            arrayList.add(new RowHeader(valueOf, "row " + i));
        }
        return arrayList;
    }

    private List<ColumnHeader> getRandomColumnHeaderList() {
        ArrayList<ColumnHeader> arrayList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            String str = "column " + i;
            int nextInt = new Random().nextInt();
            if (nextInt % 4 == 0 || nextInt % 3 == 0 || nextInt == i) {
                str = "large column " + i;
            }
            arrayList.add(new ColumnHeader(String.valueOf(i), str));
        }
        return arrayList;
    }

    private List<List<Cell>> getCellListForSortingTest() {
        ArrayList<List<Cell>> arrayList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            ArrayList<Cell> arrayList2 = new ArrayList<>();
            for (int i2 = 0; i2 < 500; i2++) {
                arrayList2.add(new Cell(i2 + "-" + i, "cell " + i2 + " " + i));
            }
            arrayList.add(arrayList2);
        }
        return arrayList;
    }

    public List<List<Cell>> getCellList() {
        return getCellListForSortingTest();
    }

    public List<RowHeader> getRowHeaderList() {
        return getSimpleRowHeaderList();
    }

    public List<ColumnHeader> getColumnHeaderList() {
        return getRandomColumnHeaderList();
    }
}
