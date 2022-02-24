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
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvHobbyAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Hobby;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.RecyclerHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.Objects;

public class ScreenHobbies extends AppCompatActivity {
    public SharedPrefs prefs;
    public Resume resume;
    public CvHobbyAdp recyclerAdapter;
    public RecyclerView recyclerView;
    public LinearLayout llAddSkills;
    public RecyclerHelper touchHelper;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_user_skill);

        init();

        touchHelper = new RecyclerHelper(resume.hobbies, recyclerAdapter);

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

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(ScreenHobbies.this, ll_banner);
                    break;
                case "a":
                    AppLovinAds.AppLovinBanner(ScreenHobbies.this, ll_banner);
                    break;
            }
        }

        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.yourHobbies));
        ((TextView) findViewById(R.id.txtName)).setText("Add Hobbies");

        String cVData = prefs.getCVData();
        Gson gson = new Gson();
        if (cVData.isEmpty()) {
            resume = Resume.createNewResume(this);
        } else {
            resume = gson.fromJson(cVData, Resume.class);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new CvHobbyAdp(resume.hobbies);
        recyclerView.setAdapter(recyclerAdapter);

        llAddSkills.setOnClickListener(v -> addNewHobbyDialog());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressLint({"ResourceType", "NotifyDataSetChanged"})
    private void addNewHobbyDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ScreenHobbies.this);
        View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_add_skill_hobby_language, null);
        bottomSheetDialog.setContentView(inflate);

        ((TextView) inflate.findViewById(R.id.txtHeading)).setText(Html.fromHtml("Add Hobbies"));
        ((TextView) inflate.findViewById(R.id.title)).setText(getResources().getString(R.string.enterYourHobby));
        final EditText editText = inflate.findViewById(R.id.skillNameEt);
        inflate.findViewById(R.id.rattingLayout).setVisibility(View.GONE);

        inflate.findViewById(R.id.addBtn).setOnClickListener(view -> {
            if (editText.getText().toString().length() == 0) {
                ScreenHobbies activityHobbies = ScreenHobbies.this;
                Utility.Toast(activityHobbies, activityHobbies.getResources().getString(R.string.enterYourHobbyFirst));
                return;
            }
            resume.hobbies.add(0, new Hobby(editText.getText().toString()));
            recyclerAdapter.notifyDataSetChanged();
            prefs.setCVData(new Gson().toJson(resume));
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.btnCancel).setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }
}
