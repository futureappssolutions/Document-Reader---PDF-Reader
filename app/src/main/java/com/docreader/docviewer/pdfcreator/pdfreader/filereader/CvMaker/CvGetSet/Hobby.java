package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

import android.os.Parcel;
import android.os.Parcelable;

public class Hobby implements Parcelable {
    public static final Parcelable.Creator<Hobby> CREATOR = new Parcelable.Creator<Hobby>() {
        public Hobby createFromParcel(Parcel parcel) {
            return new Hobby(parcel);
        }

        public Hobby[] newArray(int i) {
            return new Hobby[i];
        }
    };
    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    public Hobby() {
        this("Reading");
    }

    public Hobby(String str) {
        this.name = str;
    }

    protected Hobby(Parcel parcel) {
        this.name = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
