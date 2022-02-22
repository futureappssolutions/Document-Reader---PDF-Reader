package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.preference;

import android.os.Parcel;
import android.os.Parcelable;

public class Preferences implements Parcelable {
    public static final Parcelable.Creator<Preferences> CREATOR = new Parcelable.Creator<Preferences>() {
        public Preferences createFromParcel(Parcel parcel) {
            return new Preferences(parcel);
        }

        public Preferences[] newArray(int i) {
            return new Preferences[i];
        }
    };
    public int columnPosition;
    public int columnPositionOffset;
    public int rowPosition;
    public int rowPositionOffset;
    public int selectedColumnPosition;
    public int selectedRowPosition;

    public int describeContents() {
        return 0;
    }

    public Preferences() {
    }

    protected Preferences(Parcel parcel) {
        this.rowPosition = parcel.readInt();
        this.rowPositionOffset = parcel.readInt();
        this.columnPosition = parcel.readInt();
        this.columnPositionOffset = parcel.readInt();
        this.selectedRowPosition = parcel.readInt();
        this.selectedColumnPosition = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.rowPosition);
        parcel.writeInt(this.rowPositionOffset);
        parcel.writeInt(this.columnPosition);
        parcel.writeInt(this.columnPositionOffset);
        parcel.writeInt(this.selectedRowPosition);
        parcel.writeInt(this.selectedColumnPosition);
    }
}
