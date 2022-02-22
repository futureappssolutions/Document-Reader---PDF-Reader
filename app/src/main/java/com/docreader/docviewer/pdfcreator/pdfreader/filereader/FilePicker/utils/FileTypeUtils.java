package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils;

import android.webkit.MimeTypeMap;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FileTypeUtils {
    private static final Map<String, FileType> fileTypeExtensions = new HashMap<>();

    public enum FileType {
        DIRECTORY(R.drawable.icon_folder, R.string.type_directory),
        DOCUMENT(R.drawable.icon_allfiles, R.string.type_document);

        private final int description;
        private final String[] extensions;
        private final int icon;

        FileType(int i, int i2, String... strArr) {
            this.icon = i;
            this.description = i2;
            this.extensions = strArr;
        }

        public String[] getExtensions() {
            return this.extensions;
        }

        public int getIcon() {
            return this.icon;
        }

        public int getDescription() {
            return this.description;
        }
    }

    static {
        for (FileType fileType : FileType.values()) {
            for (String put : fileType.getExtensions()) {
                fileTypeExtensions.put(put, fileType);
            }
        }
    }

    public static FileType getFileType(File file) {
        if (file.isDirectory()) {
            return FileType.DIRECTORY;
        }
        FileType fileType = fileTypeExtensions.get(getExtension(file.getName()));
        if (fileType != null) {
            return fileType;
        }
        return FileType.DOCUMENT;
    }

    private static String getExtension(String str) {
        try {
            str = URLEncoder.encode(str, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ignored) {
        }
        return MimeTypeMap.getFileExtensionFromUrl(str).toLowerCase();
    }
}
