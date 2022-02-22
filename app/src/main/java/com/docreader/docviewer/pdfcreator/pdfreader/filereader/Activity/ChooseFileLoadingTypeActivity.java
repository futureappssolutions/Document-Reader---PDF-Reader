package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.Objects;

public class ChooseFileLoadingTypeActivity extends BaseActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_choose_file_loading_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle((CharSequence) "");
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.setting));
        changeBackGroundColor(100);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final SharedPrefs sharedPrefs = new SharedPrefs(this);
        RadioButton radioButton = (RadioButton) findViewById(R.id.loadAllFileAtOnce);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.loadAllFileOneByOne);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        if (sharedPrefs.isLoadAllFilesAtOnce()) {
            radioButton.setChecked(true);
        } else {
            radioButton2.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            sharedPrefs.setLoadAllFilesAtOnce(i == R.id.loadAllFileAtOnce);
            Singleton.getInstance().setFileDeleted(true);
            Utility.Toast(ChooseFileLoadingTypeActivity.this, "Changes has been saved");
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
