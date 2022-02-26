package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment.FrgCvEducation;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment.FrgCvExperience;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment.FrgCvOtherDetail;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment.FrgCvPersonalInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment.FrgCvPreview;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment.FrgCvProjects;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment.FrgCvSetting;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeFragment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.google.gson.Gson;

import java.util.Objects;

public class ScreenResume extends BaseActivity {
    public SharedPrefs mPrefs;
    public Resume resume;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_resume);

        mPrefs = new SharedPrefs(this);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAppLovinAds.showBannerAds(ScreenResume.this, ll_banner);


        Gson gson = new Gson();
        String cVData = mPrefs.getCVData();
        if (cVData.isEmpty()) {
            resume = Resume.createNewResume(this);
        } else {
            resume = gson.fromJson(cVData, Resume.class);
        }

        if (getIntent() != null) {
            setFragment(getIntent().getStringExtra("value"));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPrefs.setCVData(new Gson().toJson(resume));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void setFragment(String str) {
        String str2;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (str.equals("1")) {
                    c = 1;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    c = 2;
                    break;
                }
                break;
            case 51:
                if (str.equals("3")) {
                    c = 3;
                    break;
                }
                break;
            case 52:
                if (str.equals("4")) {
                    c = 4;
                    break;
                }
                break;
            case 53:
                if (str.equals("5")) {
                    c = 5;
                    break;
                }
                break;
            case 54:
                if (str.equals("6")) {
                    c = 6;
                    break;
                }
                break;
            case 55:
                if (str.equals("7")) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                openFragment(FrgCvPersonalInfo.newInstance(resume));
                str2 = getResources().getString(R.string.personalInfo);
                break;
            case 1:
            case 6:
                openFragment(FrgCvOtherDetail.newInstance(resume));
                str2 = getResources().getString(R.string.otherDetail);
                break;
            case 2:
                openFragment(FrgCvProjects.newInstance(resume));
                str2 = getResources().getString(R.string.project);
                break;
            case 3:
                openFragment(FrgCvEducation.newInstance(resume));
                str2 = getResources().getString(R.string.education);
                break;
            case 4:
                openFragment(FrgCvExperience.newInstance(resume));
                str2 = getResources().getString(R.string.experience);
                break;
            case 5:
                openFragment(FrgCvPreview.newInstance(resume));
                str2 = getResources().getString(R.string.preview);
                break;
            case 7:
                openFragment(FrgCvSetting.newInstance(resume));
                str2 = getResources().getString(R.string.setting);
                break;
            default:
                str2 = "";
                break;
        }
        ((TextView) findViewById(R.id.toolBarTitle)).setText(str2);
    }

    private void openFragment(ResumeFragment resumeFragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.content_frame, resumeFragment);
        beginTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
