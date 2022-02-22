package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Experience;

import java.util.List;

public class CvExperienceAdp extends CvResumeEventAdp<Experience> {
    public CvExperienceAdp(List<Experience> list, CvResumeEventAdp.ResumeEventOnClickListener resumeEventOnClickListener) {
        super(list, resumeEventOnClickListener);
    }
}
