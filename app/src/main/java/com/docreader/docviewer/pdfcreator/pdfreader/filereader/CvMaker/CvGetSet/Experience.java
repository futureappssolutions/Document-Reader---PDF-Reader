package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

import android.os.Parcel;
import android.os.Parcelable;

public class Experience extends ResumeEvent {
    public static final Parcelable.Creator<Experience> CREATOR = new Parcelable.Creator<Experience>() {
        public Experience createFromParcel(Parcel parcel) {
            return new Experience(new ResumeEvent(parcel));
        }

        public Experience[] newArray(int i) {
            return new Experience[i];
        }
    };

    public Experience() {
    }

    public Experience(ResumeEvent resumeEvent) {
        super(resumeEvent);
    }

    public String getCompany() {
        return getTitle();
    }

    public void setCompany(String str) {
        setTitle(str);
    }

    public String getLocation() {
        return getDetail();
    }

    public void setLocation(String str) {
        setDetail(str);
    }

    public String getJobTitle() {
        return getSubtitle();
    }

    public void setJobTitle(String str) {
        setSubtitle(str);
    }
}
