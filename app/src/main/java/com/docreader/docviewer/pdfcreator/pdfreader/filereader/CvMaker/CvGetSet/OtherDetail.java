package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

import android.os.Parcel;
import android.os.Parcelable;

public class OtherDetail implements Parcelable {
    public static final Parcelable.Creator<OtherDetail> CREATOR = new Parcelable.Creator<OtherDetail>() {
        public OtherDetail createFromParcel(Parcel parcel) {
            return new OtherDetail(parcel);
        }

        public OtherDetail[] newArray(int i) {
            return new OtherDetail[i];
        }
    };
    public String achievements;
    public String certificate;
    public String faceboook;
    public String linkedin;
    public String reference;
    public String twitter;
    public String webSite;

    @Override
    public int describeContents() {
        return 0;
    }

    public OtherDetail() {
        this("Employee of the month, SQL", "https://www.christopher.com", "crazydesigner", "allDocumentReader", "christopher", "Anthony Patrickos Ollivarr Academy (123) 456-7890  hello@reallygreatsite.com", "Digital ");
    }

    public OtherDetail(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.achievements = str;
        this.webSite = str2;
        this.linkedin = str3;
        this.faceboook = str4;
        this.twitter = str5;
        this.reference = str6;
        this.certificate = str7;
    }

    protected OtherDetail(Parcel parcel) {
        this.achievements = parcel.readString();
        this.webSite = parcel.readString();
        this.linkedin = parcel.readString();
        this.faceboook = parcel.readString();
        this.twitter = parcel.readString();
        this.reference = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.achievements);
        parcel.writeString(this.webSite);
        parcel.writeString(this.linkedin);
        parcel.writeString(this.faceboook);
        parcel.writeString(this.twitter);
        parcel.writeString(this.reference);
    }

    public String getAchievements() {
        return this.achievements;
    }

    public void setAchievements(String str) {
        this.achievements = str;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String str) {
        this.reference = str;
    }

    public String getWebSite() {
        return this.webSite;
    }

    public void setWebSite(String str) {
        this.webSite = str;
    }

    public String getLinkedin() {
        return this.linkedin;
    }

    public void setLinkedin(String str) {
        this.linkedin = str;
    }

    public String getFaceboook() {
        return this.faceboook;
    }

    public void setFaceboook(String str) {
        this.faceboook = str;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public void setTwitter(String str) {
        this.twitter = str;
    }
}
