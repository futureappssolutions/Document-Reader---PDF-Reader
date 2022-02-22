package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

import android.os.Parcel;
import android.os.Parcelable;

public class Project extends ResumeEvent {
    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
        public Project createFromParcel(Parcel parcel) {
            return new Project(new ResumeEvent(parcel));
        }

        public Project[] newArray(int i) {
            return new Project[i];
        }
    };

    public Project() {
    }

    public Project(ResumeEvent resumeEvent) {
        super(resumeEvent);
    }

    public String getName() {
        return getTitle();
    }

    public void setName(String str) {
        setTitle(str);
    }
}
