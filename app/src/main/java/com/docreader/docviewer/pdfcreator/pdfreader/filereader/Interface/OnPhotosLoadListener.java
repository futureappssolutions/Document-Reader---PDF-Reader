package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.Folder;

import java.util.HashMap;

public interface OnPhotosLoadListener {
    void onPhotoDataLoadedCompleted(HashMap<String, Folder> hashMap);
}
