package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter.FileFilter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImagePickerFileUtils {
    public static List<File> getFileList(File file, FileFilter fileFilter) {
        Utility.logCatMsg("get file list " + fileFilter.accept(file.getAbsoluteFile()));
        fileFilter.getClass();
        File[] listFiles = file.listFiles(fileFilter::accept);
        if (listFiles == null) {
            return new ArrayList<>();
        }
        List<File> asList = Arrays.asList(listFiles);
        Collections.sort(asList, new FileComparator());
        return asList;
    }

    public static File getParentOrNull(File file) {
        if (file.getParent() == null) {
            return null;
        }
        return file.getParentFile();
    }

    public static boolean isParent(File file, File file2) {
        if (file2.exists() && file2.isDirectory()) {
            while (file != null) {
                if (file.equals(file2)) {
                    return true;
                }
                file = file.getParentFile();
            }
        }
        return false;
    }

    private boolean isDocumentFile(String str) {
        String lowerCase = str.toLowerCase();
        if (lowerCase.endsWith(MainConstant.FILE_TYPE_DOC) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOCX) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOT) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLS) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLT) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSM) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTM) || lowerCase.endsWith("pdf") || lowerCase.endsWith(MainConstant.FILE_TYPE_TXT)) {
            return true;
        }
        return false;
    }
}
