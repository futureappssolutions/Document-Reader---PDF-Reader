package com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FavoriteFilesModel implements Parcelable {
    public ArrayList<String> favoriteFilesList;

    public static final Parcelable.Creator<FavoriteFilesModel> CREATOR = new Parcelable.Creator<FavoriteFilesModel>() {
        public FavoriteFilesModel createFromParcel(Parcel parcel) {
            return new FavoriteFilesModel(parcel);
        }

        public FavoriteFilesModel[] newArray(int i) {
            return new FavoriteFilesModel[i];
        }
    };

    private FavoriteFilesModel() {
    }

    protected FavoriteFilesModel(Parcel parcel) {
        this.favoriteFilesList = parcel.createStringArrayList();
    }

    public static FavoriteFilesModel initFavoriteFileList() {
        FavoriteFilesModel favoriteFilesModel = new FavoriteFilesModel();
        favoriteFilesModel.favoriteFilesList = new ArrayList<>();
        return favoriteFilesModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.favoriteFilesList);
    }
}
