package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment;

import android.content.Intent;
import android.view.View;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvResumeEventAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvSchoolsAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.School;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeEventFragment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeFragment;

public class FrgCvEducation extends ResumeEventFragment<School> {

    public static ResumeFragment newInstance(Resume resume) {
        FrgCvEducation fragmentCvEducation = new FrgCvEducation();
        fragmentCvEducation.setResume(resume);
        return fragmentCvEducation;
    }

    public void delete(int i) {
        getResume().schools.remove(i);
    }

    public void onClick(int i) {
        Intent schoolIntent = ScreenCVEdit.getSchoolIntent(getContext());
        ScreenCVEdit.setData(schoolIntent, i, getResume().schools.get(i));
        startActivityForResult(schoolIntent, 2);
    }

    public void addClicked() {
        startActivityForResult(ScreenCVEdit.getSchoolIntent(getContext()), 1);
    }

    public CvResumeEventAdp<School> getAdapter(View view) {
        return new CvSchoolsAdp(getResume().schools, this).setEmptyView(view);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            getResume().schools.add(new School(ScreenCVEdit.getEvent(intent)));
            notifyDataChanged();
        }
        if (i == 2 && i2 == -1) {
            getResume().schools.get(intent.getIntExtra(ScreenCVEdit.FIELD_ID, -1)).cloneThis(ScreenCVEdit.getEvent(intent));
            notifyDataChanged();
        }
        super.onActivityResult(i, i2, intent);
    }
}
