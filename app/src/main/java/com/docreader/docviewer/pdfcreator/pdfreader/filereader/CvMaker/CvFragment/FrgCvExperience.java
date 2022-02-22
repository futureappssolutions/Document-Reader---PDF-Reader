package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment;

import android.content.Intent;
import android.view.View;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvExperienceAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvResumeEventAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Experience;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeEventFragment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeFragment;

public class FrgCvExperience extends ResumeEventFragment<Experience> {
    public static ResumeFragment newInstance(Resume resume) {
        FrgCvExperience fragmentCvExperience = new FrgCvExperience();
        fragmentCvExperience.setResume(resume);
        return fragmentCvExperience;
    }

    public void delete(int i) {
        getResume().experience.remove(i);
    }

    public void onClick(int i) {
        Intent experienceIntent = ScreenCVEdit.getExperienceIntent(getContext());
        ScreenCVEdit.setData(experienceIntent, i, getResume().experience.get(i));
        startActivityForResult(experienceIntent, 2);
    }
    
    public void addClicked() {
        startActivityForResult(ScreenCVEdit.getExperienceIntent(getContext()), 1);
    }
    
    public CvResumeEventAdp<Experience> getAdapter(View view) {
        return new CvExperienceAdp(getResume().experience, this).setEmptyView(view);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            getResume().experience.add(new Experience(ScreenCVEdit.getEvent(intent)));
            notifyDataChanged();
        }
        if (i == 2 && i2 == -1) {
            getResume().experience.get(intent.getIntExtra(ScreenCVEdit.FIELD_ID, -1)).cloneThis(ScreenCVEdit.getEvent(intent));
            notifyDataChanged();
        }
        super.onActivityResult(i, i2, intent);
    }
}
