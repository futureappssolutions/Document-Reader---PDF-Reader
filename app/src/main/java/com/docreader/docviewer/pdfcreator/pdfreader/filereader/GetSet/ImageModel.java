package com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public final class ImageModel implements Parcelable {

    public static final Parcelable.Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel source) {
            return new ImageModel(source);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    private long f143id;
    private boolean isSelected;
    private boolean isVideo;
    private String name;
    private String path;
    private Uri uri;

    public ImageModel() {
    }

    public ImageModel(long j, String str, String str2, boolean z) {
        this.f143id = j;
        this.name = str;
        this.path = str2;
        this.isVideo = z;
    }

    public ImageModel(String str, boolean z) {
        this.path = str;
        this.isSelected = z;
    }

    public ImageModel(String str, Uri uri2) {
        this.path = str;
        this.uri = uri2;
        this.isSelected = false;
    }

    public ImageModel(Uri uri2, boolean z) {
        this.uri = uri2;
        this.isSelected = z;
    }

    protected ImageModel(Parcel parcel) {
        this.f143id = parcel.readLong();
        this.name = parcel.readString();
        this.path = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public final long getId() {
        return this.f143id;
    }

    public final void setId(long j) {
        this.f143id = j;
    }

    public final Uri getUri() {
        return this.uri;
    }

    public final void setUri(Uri uri2) {
        this.uri = uri2;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String str) {
        this.name = str;
    }

    public final String getPath() {
        return this.path;
    }

    public final void setPath(String str) {
        this.path = str;
    }

    public final boolean isVideo() {
        return this.isVideo;
    }

    public final void setVideo(boolean z) {
        this.isVideo = z;
    }

    public final boolean isSelected() {
        return this.isSelected;
    }

    public final void setSelected(boolean z) {
        this.isSelected = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !Intrinsics.areEqual(getClass(), obj.getClass())) {
            return false;
        }
        return StringsKt.equals(((ImageModel) obj).path, this.path, true);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.f143id);
        parcel.writeString(this.name);
        parcel.writeString(this.path);
        parcel.writeString(String.valueOf(this.uri));
    }
}
