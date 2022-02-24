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
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.AppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvSkillAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Skill;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.RecyclerHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CustomIORatingBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.Objects;

public class ScreenSkill extends AppCompatActivity {
    public int customRatting = 0;
    public SharedPrefs prefs;
    public Resume resume;
    public LinearLayout llAddSkills;
    public CvSkillAdp recyclerAdapter;
    public RecyclerView recyclerView;
    public RecyclerHelper touchHelper;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_user_skill);

        init();

        touchHelper = new RecyclerHelper(resume.skill, recyclerAdapter);

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

    public void init() {
        prefs = new SharedPrefs(this);
        recyclerView = findViewById(R.id.recyclerView);
        llAddSkills = findViewById(R.id.llAddSkills);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.highlightskill));

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(ScreenSkill.this, ll_banner);
                    break;
                case "a":
                    AppLovinAds.AppLovinBanner(ScreenSkill.this, ll_banner);
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
        recyclerAdapter = new CvSkillAdp(resume.skill);
        recyclerView.setAdapter(recyclerAdapter);

        llAddSkills.setOnClickListener(v -> addNewSkillDialog());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressLint({"NotifyDataSetChanged", "ResourceType"})
    private void addNewSkillDialog() {
        customRatting = 0;
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ScreenSkill.this);
        View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_add_skill_hobby_language, null);
        bottomSheetDialog.setContentView(inflate);

        ((TextView) inflate.findViewById(R.id.txtHeading)).setText(Html.fromHtml("Add Skill"));
        final EditText editText = inflate.findViewById(R.id.skillNameEt);
        final TextView textView = inflate.findViewById(R.id.rattingNumber);

        ((CustomIORatingBar) inflate.findViewById(R.id.customIORatingBar)).setRatingChangeListener(i -> {
            customRatting = i;
            textView.setText(i + "/5");
        });

        inflate.findViewById(R.id.addBtn).setOnClickListener(view -> {
            if (editText.getText().toString().length() == 0) {
                Utility.Toast(this, getResources().getString(R.string.enterYourSkillFirst));
            } else if (customRatting == 0) {
                Utility.Toast(this, getResources().getString(R.string.rateYourSkillFirst));
            } else {
                resume.skill.add(0, new Skill(editText.getText().toString(), customRatting));
                recyclerAdapter.notifyDataSetChanged();
                prefs.setCVData(new Gson().toJson(resume));
                bottomSheetDialog.dismiss();
            }
        });

        inflate.findViewById(R.id.btnCancel).setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }
}
