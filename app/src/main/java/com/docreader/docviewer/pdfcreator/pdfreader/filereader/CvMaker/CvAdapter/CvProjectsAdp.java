package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter;

import android.view.View;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Project;

import java.util.List;

public class CvProjectsAdp extends CvResumeEventAdp<Project> {
    public CvProjectsAdp(List<Project> list, CvResumeEventAdp.ResumeEventOnClickListener resumeEventOnClickListener) {
        super(list, resumeEventOnClickListener);
    }

    public void updateViewHolder(CvResumeEventAdapterViewHolder cvResumeEventAdapterViewHolder) {
        cvResumeEventAdapterViewHolder.subtitle.setVisibility(View.GONE);
    }
}
