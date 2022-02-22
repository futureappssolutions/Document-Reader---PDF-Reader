package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.OnPhotosLoadListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.Folder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.ImageModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import kotlin.jvm.internal.Intrinsics;

public final class PhotoUtil {
    private Context context;
    private OnPhotosLoadListener onPhotosLoadListener;
    private HashMap<String, Folder> photosHashMap = new HashMap<>();

    public final OnPhotosLoadListener getOnPhotosLoadListener() {
        return this.onPhotosLoadListener;
    }

    public final void setOnPhotosLoadListener(OnPhotosLoadListener onPhotosLoadListener2) {
        this.onPhotosLoadListener = onPhotosLoadListener2;
    }

    public final HashMap<String, Folder> getPhotosHashMap() {
        return this.photosHashMap;
    }

    public final void setPhotosHashMap(HashMap<String, Folder> hashMap) {
        Intrinsics.checkNotNullParameter(hashMap, "<set-?>");
        this.photosHashMap = hashMap;
    }

    public final Context getContext() {
        return this.context;
    }

    public final void setContext(Context context2) {
        this.context = context2;
    }

    public PhotoUtil(Context context2, OnPhotosLoadListener onPhotosLoadListener2) {
        this.onPhotosLoadListener = onPhotosLoadListener2;
        this.context = context2;
    }

    public final void getAllPhotosFolderWise() {
        loadPhotos();
    }


    public final void loadPhotos() {
        Uri uri;
        Utility.logCatMsg("start...loading images");
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Images.Media.getContentUri("external");
            Intrinsics.checkNotNullExpressionValue(uri, "{\n            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)\n        }");
        } else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Intrinsics.checkNotNullExpressionValue(uri, "{\n            MediaStore.Images.Media.EXTERNAL_CONTENT_URI\n        }");
        }
        Context context = getContext();
        Intrinsics.checkNotNull(context);
        Cursor cursor = context.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        int columnIndexOrThrow = cursor.getColumnIndexOrThrow("_id");
        int columnIndexOrThrow2 = cursor.getColumnIndexOrThrow("_display_name");
        int columnIndexOrThrow3 = cursor.getColumnIndexOrThrow("bucket_display_name");
        String[] columnNames = cursor.getColumnNames();
        int columnIndexOrThrow4 = Arrays.asList(Arrays.copyOf(columnNames, columnNames.length)).contains("_data") ? cursor.getColumnIndexOrThrow("_data") : 100;
        while (cursor.moveToNext()) {
            long j = cursor.getLong(columnIndexOrThrow);
            String string = cursor.getString(columnIndexOrThrow2);
            String string2 = cursor.getString(columnIndexOrThrow3);
            if (string2 == null) {
                string2 = "040-021";
            }
            Uri withAppendedId = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, j);
            Intrinsics.checkNotNullExpressionValue(withAppendedId, "withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)");
            String str = "";
            if (columnIndexOrThrow4 != 100) {
                str = cursor.getString(columnIndexOrThrow4);
                Intrinsics.checkNotNullExpressionValue(str, "cursor.getString(dataColumn)");
            }
            ImageModel imageModel = new ImageModel();
            imageModel.setId(j);
            imageModel.setSelected(false);
            imageModel.setName(string);
            imageModel.setPath(str);
            imageModel.setUri(withAppendedId);
            Folder folder = new Folder(string2);
            folder.getImages().add(imageModel);
            setPhotoValueInHasMap(folder);
        }
        OnPhotosLoadListener onPhotosLoadListener = getOnPhotosLoadListener();
        Intrinsics.checkNotNull(onPhotosLoadListener);
        onPhotosLoadListener.onPhotoDataLoadedCompleted(getPhotosHashMap());
    }

    public final boolean hasFolderColumnAvailable() {
        Uri uri;
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Images.Media.getContentUri("external");
            Intrinsics.checkNotNullExpressionValue(uri, "{\n            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)\n        }");
        } else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Intrinsics.checkNotNullExpressionValue(uri, "{\n            MediaStore.Images.Media.EXTERNAL_CONTENT_URI\n        }");
        }
        Context context = this.context;
        Intrinsics.checkNotNull(context);
        Cursor cursor = context.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            String[] columnNames = cursor.getColumnNames();
            return Arrays.asList(Arrays.copyOf(columnNames, columnNames.length)).contains("bucket_display_name");
        }
        return false;
    }

    public final void setPhotoValueInHasMap(Folder folder) {
        ArrayList<ImageModel> images;
        Intrinsics.checkNotNullParameter(folder, "folder");
        if (this.photosHashMap.containsKey(folder.getFolderName())) {
            Folder folder2 = this.photosHashMap.get(folder.getFolderName());
            if (!(folder2 == null || (images = folder2.getImages()) == null)) {
                images.addAll(folder.getImages());
            }
            if (folder2 != null) {
                this.photosHashMap.put(folder.getFolderName(), folder2);
                return;
            }
            return;
        }
        this.photosHashMap.put(folder.getFolderName(), folder);
    }
}
