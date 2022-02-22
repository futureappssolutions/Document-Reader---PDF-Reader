package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.facebookMaster;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvLanguageAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Language;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.RecyclerHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CustomIORatingBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.Objects;

public class ScreenCVLanguage extends AppCompatActivity {
    public int customRatting = 0;
    public SharedPrefs prefs;
    public Resume resume;
    public CvLanguageAdp recyclerAdapter;
    public RecyclerView recyclerView;
    public LinearLayout llAddSkills;
    public RecyclerHelper touchHelper;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_user_skill);

        init();

        touchHelper = new RecyclerHelper(resume.language, recyclerAdapter);

        touchHelper.setRecyclerItemDragEnabled(true).setOnDragItemListener((i, i2) -> {
        });

        touchHelper.setRecyclerItemSwipeEnabled(true).setOnSwipeItemListener(() -> {
        });

        new ItemTouchHelper(touchHelper).attachToRecyclerView(recyclerView);

        touchHelper.getList();
    }

    @Override
    public void onBackPressed() {
        prefs.setCVData(new Gson().toJson(resume));
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    public void init() {
        prefs = new SharedPrefs(this);
        recyclerView = findViewById(R.id.recyclerView);
        llAddSkills = findViewById(R.id.llAddSkills);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.knownlLangugages));
        ((TextView) findViewById(R.id.txtName)).setText("Add Language");

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(ScreenCVLanguage.this, ll_banner);
                    break;
                case "f":
                    facebookMaster.FbBanner(ScreenCVLanguage.this, ll_banner);
                    break;
                case "both":
                    Advertisement.GoogleBannerBoth(ScreenCVLanguage.this, ll_banner);
                    break;
            }
        }

        String cVData = prefs.getCVData();
        Gson gson = new Gson();
        if (cVData.isEmpty()) {
            resume = Resume.createNewResume(this);
        } else {
            resume = gson.fromJson(cVData, Resume.class);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new CvLanguageAdp(resume.language);
        recyclerView.setAdapter(recyclerAdapter);

        llAddSkills.setOnClickListener(v -> addNewLanguageDialog());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressLint({"ResourceType", "NotifyDataSetChanged", "SetTextI18n"})
    private void addNewLanguageDialog() {
        customRatting = 0;
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ScreenCVLanguage.this);
        View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_add_skill_hobby_language, null);
        bottomSheetDialog.setContentView(inflate);

        ((TextView) inflate.findViewById(R.id.txtHeading)).setText(Html.fromHtml("Add Language"));
        ((TextView) inflate.findViewById(R.id.title)).setText(getResources().getString(R.string.enterYourLanguage));
        ((TextView) inflate.findViewById(R.id.subtitle)).setText(getResources().getString(R.string.rateYourLanguagesOutOfFive));
        final EditText editText = inflate.findViewById(R.id.skillNameEt);
        final TextView textView = inflate.findViewById(R.id.rattingNumber);

        ((CustomIORatingBar) inflate.findViewById(R.id.customIORatingBar)).setRatingChangeListener(i -> {
            customRatting = i;
            textView.setText(i + "/5");
        });

        inflate.findViewById(R.id.addBtn).setOnClickListener(view -> {
            if (editText.getText().toString().length() == 0) {
                ScreenCVLanguage activityCVLanguage = ScreenCVLanguage.this;
                Utility.Toast(activityCVLanguage, activityCVLanguage.getResources().getString(R.string.enterYourLanguageFirst));
            } else if (customRatting == 0) {
                ScreenCVLanguage activityCVLanguage2 = ScreenCVLanguage.this;
                Utility.Toast(activityCVLanguage2, activityCVLanguage2.getResources().getString(R.string.rateYourLanguageFirst));
            } else {
                resume.language.add(0, new Language(editText.getText().toString(), customRatting));
                recyclerAdapter.notifyDataSetChanged();
                prefs.setCVData(new Gson().toJson(resume));
                bottomSheetDialog.dismiss();
            }
        });

        inflate.findViewById(R.id.btnCancel).setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }
}
