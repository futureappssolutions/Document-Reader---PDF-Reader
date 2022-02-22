package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.School;

import java.util.List;

public class CvSchoolsAdp extends CvResumeEventAdp<School> {
    public CvSchoolsAdp(List<School> list, CvResumeEventAdp.ResumeEventOnClickListener resumeEventOnClickListener) {
        super(list, resumeEventOnClickListener);
    }
}
