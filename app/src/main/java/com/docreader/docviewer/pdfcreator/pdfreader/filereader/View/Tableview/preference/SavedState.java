package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.preference;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public SavedState createFromParcel(Parcel parcel) {
            return new SavedState(parcel);
        }

        public SavedState[] newArray(int i) {
            return new SavedState[i];
        }
    };
    public Preferences preferences;

    public SavedState(Parcelable parcelable) {
        super(parcelable);
    }

    private SavedState(Parcel parcel) {
        super(parcel);
        this.preferences = (Preferences) parcel.readParcelable(Preferences.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.preferences, i);
    }
}
