package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment;

import android.content.Intent;
import android.view.View;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvProjectsAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvResumeEventAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Project;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeEventFragment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeFragment;

public class FrgCvProjects extends ResumeEventFragment<Project> {
    public static ResumeFragment newInstance(Resume resume) {
        FrgCvProjects fragmentCvProjects = new FrgCvProjects();
        fragmentCvProjects.setResume(resume);
        return fragmentCvProjects;
    }


    public void delete(int i) {
        getResume().projects.remove(i);
    }

    public void onClick(int i) {
        Intent projectIntent = ScreenCVEdit.getProjectIntent(getContext());
        ScreenCVEdit.setData(projectIntent, i, getResume().projects.get(i));
        startActivityForResult(projectIntent, 2);
    }


    public void addClicked() {
        startActivityForResult(ScreenCVEdit.getProjectIntent(getContext()), 1);
    }


    public CvResumeEventAdp<Project> getAdapter(View view) {
        return new CvProjectsAdp(getResume().projects, this).setEmptyView(view);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            getResume().projects.add(new Project(ScreenCVEdit.getEvent(intent)));
            notifyDataChanged();
        }
        if (i == 2 && i2 == -1) {
            getResume().projects.get(intent.getIntExtra(ScreenCVEdit.FIELD_ID, -1)).cloneThis(ScreenCVEdit.getEvent(intent));
            notifyDataChanged();
        }
        super.onActivityResult(i, i2, intent);
    }
}
