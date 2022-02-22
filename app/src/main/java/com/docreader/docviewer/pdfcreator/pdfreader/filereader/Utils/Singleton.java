package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.content.Context;

import com.google.gson.Gson;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FavoriteFilesModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.Folder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.ImageModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Singleton {
    public static Singleton singleton = new Singleton();
    public ArrayList<Object> allDocumnentList = new ArrayList<>();
    public ArrayList<Object> allEbookFilesList = new ArrayList<>();
    public ArrayList<ImageModel> fileList = new ArrayList<>();
    public List<Folder> folders = new ArrayList<>();
    public BusinessInfo businessInfo;
    public boolean isCodeFileFolderView = false;
    public boolean isFileDeleted = false;
    public boolean isInvoiceDataUpdated = false;


    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

    public ArrayList<Object> getAllDocumentFileList() {
        if (allDocumnentList == null) {
            allDocumnentList = new ArrayList<>();
        }
        return allDocumnentList;
    }

    public ArrayList<Object> getAllEbookFileList() {
        if (allEbookFilesList == null) {
            allEbookFilesList = new ArrayList<>();
        }
        return allEbookFilesList;
    }

    public boolean isFileDeleted() {
        return isFileDeleted;
    }

    public void setFileDeleted(boolean z) {
        isFileDeleted = z;
    }

    public void setCodeFileFolderView(boolean z) {
        isCodeFileFolderView = z;
    }

    public void setFileList(ArrayList<ImageModel> arrayList) {
        fileList = arrayList;
    }

    public List<Folder> getFolderList(Collection<Folder> collection) {
        folders = new ArrayList<>(collection);
        return folders;
    }

    public void destroy() {
        if (allDocumnentList != null) {
            allDocumnentList.clear();
        }
        allDocumnentList = null;
        singleton = null;
    }

    public FavoriteFilesModel getFavoriteFilesModel(Context context) {
        String favoriteFilesData = new SharedPrefs(context).getFavoriteFilesData();
        Gson gson = new Gson();
        FavoriteFilesModel favoriteFilesModel;
        if (favoriteFilesData.isEmpty()) {
            favoriteFilesModel = FavoriteFilesModel.initFavoriteFileList();
        } else {
            favoriteFilesModel = gson.fromJson(favoriteFilesData, FavoriteFilesModel.class);
        }
        return favoriteFilesModel;
    }

    public boolean isInvoiceDataUpdated() {
        return isInvoiceDataUpdated;
    }

    public void setInvoiceDataUpdated(boolean z) {
        isInvoiceDataUpdated = z;
    }

    public BusinessInfo getBusinessInfo(Context context) {
        if (businessInfo == null) {
            String invoiceBusinessInfoData = new SharedPrefs(context).getInvoiceBusinessInfoData();
            Gson gson = new Gson();
            if (invoiceBusinessInfoData.isEmpty()) {
                businessInfo = new BusinessInfo();
            } else {
                businessInfo = gson.fromJson(invoiceBusinessInfoData, BusinessInfo.class);
            }
        }
        return businessInfo;
    }

    public void setAndSaveBusinessInfo(Context context, BusinessInfo businessInfo2) {
        new SharedPrefs(context).setInvoiceBusinessInfoData(new Gson().toJson(businessInfo2));
        businessInfo = businessInfo2;
    }
}
