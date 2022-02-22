package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

import android.os.Parcel;
import android.os.Parcelable;

public class ResumeEvent implements Parcelable {
    public static final Parcelable.Creator<ResumeEvent> CREATOR = new Parcelable.Creator<ResumeEvent>() {
        public ResumeEvent createFromParcel(Parcel parcel) {
            return new ResumeEvent(parcel);
        }

        public ResumeEvent[] newArray(int i) {
            return new ResumeEvent[i];
        }
    };
    private String description;
    private String detail;
    private String fromDate;
    private String subtitle;
    private String title;
    private String toDate;

    @Override
    public int describeContents() {
        return 0;
    }

    public ResumeEvent() {
    }

    public ResumeEvent(String str, String str2, String str3, String str4, String str5, String str6) {
        this.title = str;
        this.detail = str2;
        this.subtitle = str3;
        this.description = str4;
        this.fromDate = str5;
        this.toDate = str6;
    }

    protected ResumeEvent(Parcel parcel) {
        this.title = parcel.readString();
        this.detail = parcel.readString();
        this.subtitle = parcel.readString();
        this.description = parcel.readString();
        this.fromDate = parcel.readString();
        this.toDate = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.detail);
        parcel.writeString(this.subtitle);
        parcel.writeString(this.description);
        parcel.writeSerializable(this.fromDate);
        parcel.writeSerializable(this.toDate);
    }

    public ResumeEvent(ResumeEvent resumeEvent) {
        cloneThis(resumeEvent);
    }

    public void cloneThis(ResumeEvent resumeEvent) {
        this.title = resumeEvent.title;
        this.detail = resumeEvent.detail;
        this.subtitle = resumeEvent.subtitle;
        this.description = resumeEvent.description;
        this.fromDate = resumeEvent.fromDate;
        this.toDate = resumeEvent.toDate;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String str) {
        this.detail = str;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String str) {
        this.subtitle = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(String str) {
        this.fromDate = str;
    }

    public String getToDate() {
        return this.toDate;
    }

    public void setToDate(String str) {
        this.toDate = str;
    }
}
