package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;

public abstract class ResumeFragment extends Fragment {
    public static final String ARGUMENT_RESUME = "resume";

    public void setResume(Resume resume) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_RESUME, resume);
        setArguments(bundle);
    }

    public Resume getResume() {
        return getArguments().getParcelable(ARGUMENT_RESUME);
    }
}
