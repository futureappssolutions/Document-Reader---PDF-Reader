package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeFragment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.TextChangeListener;

public class FrgCvOtherDetail extends ResumeFragment {

    public static ResumeFragment newInstance(Resume resume) {
        FrgCvOtherDetail fragmentCvOtherDetail = new FrgCvOtherDetail();
        fragmentCvOtherDetail.setResume(resume);
        return fragmentCvOtherDetail;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_other_detail, viewGroup, false);

        final Resume resume = getResume();

        EditText editText = inflate.findViewById(R.id.achievementsEt);
        editText.setHint(resume.otherDetail.getAchievements());
        editText.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                resume.otherDetail.setAchievements(charSequence.toString());
            }
        });

        EditText editText2 = inflate.findViewById(R.id.websiteEt);
        editText2.setHint(resume.otherDetail.getWebSite());
        editText2.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                resume.otherDetail.setWebSite(charSequence.toString());
            }
        });

        EditText editText3 = inflate.findViewById(R.id.linkdinEt);
        editText3.setHint(resume.otherDetail.getLinkedin());
        editText3.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                resume.otherDetail.setLinkedin(charSequence.toString());
            }
        });

        EditText editText4 = inflate.findViewById(R.id.facebookET);
        editText4.setHint(resume.otherDetail.getFaceboook());
        editText4.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                resume.otherDetail.setFaceboook(charSequence.toString());
            }
        });

        EditText editText5 = inflate.findViewById(R.id.twitterEt);
        editText5.setHint(resume.otherDetail.getTwitter());
        editText5.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                resume.otherDetail.setTwitter(charSequence.toString());
            }
        });

        EditText editText6 = inflate.findViewById(R.id.referenceEt);
        editText6.setHint(resume.otherDetail.getReference());
        editText6.addTextChangedListener(new TextChangeListener() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                resume.otherDetail.setReference(charSequence.toString());
            }
        });
        return inflate;
    }
}
