package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Code.CodeView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Code.Language;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Code.Theme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Objects;

public class TextViewer extends BaseActivity implements CodeView.OnHighlightListener {
    public boolean isFromAppActivity = false;
    public int themePos = 0;
    public String file_text = "";
    public CodeView mCodeView;
    public ProgressDialog mProgressDialog;

    public void onFontSizeChanged(int i) {
    }

    public void onLanguageDetected(Language language, int i) {
    }

    public void onLineClicked(int i, String str) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_code_view);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        changeBackGroundColor(6);

        mCodeView = findViewById(R.id.code_view);

        SharedPrefs prefs = new SharedPrefs(TextViewer.this);
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAds.showBannerAds(TextViewer.this, ll_banner);


        String stringExtra = getIntent().getStringExtra("path");
        ((TextView) findViewById(R.id.toolBarTitle)).setText(getIntent().getStringExtra("name"));
        isFromAppActivity = getIntent().getBooleanExtra("fromAppActivity", false);
        if (stringExtra == null) {
            finish();
            return;
        }
        File file = new File(Uri.parse(stringExtra).getPath());
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                    sb.append(10);
                } else {
                    bufferedReader.close();
                    file_text = sb.toString();
                    mCodeView.setOnHighlightListener(this).setOnHighlightListener(this).setTheme(Theme.ANDROIDSTUDIO).setCode(file_text).setLanguage(Language.AUTO).setWrapLine(true).setFontSize(14.0f).setZoomEnabled(true).setShowLineNumber(true).setStartLineNumber(1).apply();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.change_theme_action) {
            int i = themePos + 1;
            themePos = i;
            setHighlightTheme(i);
            return true;
        } else if (itemId == R.id.show_line_number_action) {
            mCodeView.toggleLineNumber();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onStartCodeHighlight() {
        mProgressDialog = ProgressDialog.show(this, null, getResources().getString(R.string.loadingFiles), true);
    }

    public void onFinishCodeHighlight() {
        ProgressDialog progressDialog = mProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void setHighlightTheme(int i) {
        mCodeView.setTheme(Theme.ALL.get(i)).apply();
    }
}
