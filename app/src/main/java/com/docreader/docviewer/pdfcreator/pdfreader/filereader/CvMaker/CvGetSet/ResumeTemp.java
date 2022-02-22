package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

public class ResumeTemp {
    public boolean isPremium;
    public boolean isSelected;
    public int temp;

    public ResumeTemp(int i, boolean z, boolean z2) {
        this.temp = i;
        this.isSelected = z;
        this.isPremium = z2;
    }

    public int getTemp() {
        return this.temp;
    }

    public void setTemp(int i) {
        this.temp = i;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
