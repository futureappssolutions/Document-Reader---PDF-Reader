package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.PDFView.PDFView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.PdfDocumentAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.io.File;
import java.util.Objects;

public class PDFViewWebViewBase extends WebViewBase {
    private boolean isExit = false;
    private boolean isFromAppActivity = false;
    private SharedPrefs mPrefs;
    private String filePath;
    private String pathForPrint;
    private String shareFilePath;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        mPrefs = new SharedPrefs(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            String stringExtra = getIntent().getStringExtra("path");
            filePath = stringExtra;
            shareFilePath = stringExtra;
            pathForPrint = getIntent().getStringExtra("path");
            String fileName = getIntent().getStringExtra("name");
            isFromAppActivity = getIntent().getBooleanExtra("fromAppActivity", false);
            changeBackGroundColor(Integer.parseInt(getIntent().getStringExtra("fileType")));
            toolBarTitle.setText(fileName);
        }
        try {
            filePath = Uri.encode("file://" + filePath, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        f801wv.loadUrl("file:///android_asset/pdfviewer/web/viewer.html?file=" + filePath);

        if (mPrefs.getPdfFileViewOption() == 1) {
            viewPdfFileByMethodOne();
        } else {
            viewPdfFileByMethodTwo();
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        if (Build.VERSION.SDK_INT < 21) {
            menu.findItem(R.id.ic_full_screen_view).setVisible(false);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            menu.findItem(R.id.ic_print).setVisible(true);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            menu.findItem(R.id.ic_pdf_view_option).setVisible(true);
        }
        return true;
    }

    @SuppressLint({"NonConstantResourceId", "ObsoleteSdkInt"})
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.ic_full_screen_view:
                hideSystemUI();
                break;
            case R.id.ic_option_one:
                mPrefs.setPdfFileViewOption(1);
                viewPdfFileByMethodOne();
                break;
            case R.id.ic_option_two:
                mPrefs.setPdfFileViewOption(2);
                viewPdfFileByMethodTwo();
                break;
            case R.id.ic_print:
                if (Build.VERSION.SDK_INT >= 19) {
                    PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                    try {
                        Utility.logCatMsg("File Path " + pathForPrint);
                        printManager.print("Document", new PdfDocumentAdp(this, pathForPrint), new PrintAttributes.Builder().build());
                        break;
                    } catch (Exception e) {
                        Utility.Toast(this, getResources().getString(R.string.printingFailed));
                        e.printStackTrace();
                        break;
                    }
                }
                break;
            case R.id.ic_share:
                Utility.shareFile(this, shareFilePath);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if (isFromAppActivity && !isExit) {
            isExit = true;
        }
        super.onBackPressed();
    }

    public void viewPdfFileByMethodTwo() {
        PDFView pDFView = findViewById(R.id.pdfView);
        pDFView.fromFile(new File(shareFilePath));
        pDFView.show();
        findViewById(R.id.pdfViewOptionTwo).setVisibility(View.VISIBLE);
        f801wv.setVisibility(View.GONE);
    }

    public void viewPdfFileByMethodOne() {
        f801wv.setVisibility(View.VISIBLE);
        findViewById(R.id.pdfViewOptionTwo).setVisibility(View.GONE);
    }
}
