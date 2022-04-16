package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView.RtfHtml;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView.RtfParseException;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView.RtfReader;

import java.io.File;
import java.util.Objects;

public class RTFView extends BaseActivity {
    public boolean isExit = false;
    public boolean isFromAppActivity = false;
    public Boolean fromConverterApp = false;
    public PrintDocumentAdapter printAdapter;
    public String fileName;
    public String filePath;
    public PrintJob printJob;
    public WebView webview;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_rtf_file_view);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        TextView textView = findViewById(R.id.toolBarTitle);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAds.showBannerAds(RTFView.this, ll_banner);


        if (getIntent() != null) {
            filePath = getIntent().getStringExtra("path");
            fileName = getIntent().getStringExtra("name");
            isFromAppActivity = getIntent().getBooleanExtra("fromAppActivity", false);
            fromConverterApp = getIntent().getBooleanExtra("fromConverterApp", false);
            changeBackGroundColor(Integer.parseInt(getIntent().getStringExtra("fileType")));
            textView.setText(fileName);
        }

        webview = findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setAllowFileAccess(true);

        new loadTextFromRtfFile().execute();

        Utility.Toast(this, getResources().getString(R.string.rtfFileTakeFewSecondToLoadPleaseWait));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fromConverterApp) {
            getMenuInflater().inflate(R.menu.convert, menu);
        } else {
            getMenuInflater().inflate(R.menu.share, menu);
            if (Build.VERSION.SDK_INT < 21) {
                menu.findItem(R.id.ic_full_screen_view).setVisible(false);
            }
            if (Build.VERSION.SDK_INT >= 19) {
                menu.findItem(R.id.ic_print).setVisible(true);
            }
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.action_convert:
                if (Build.VERSION.SDK_INT < 19) {
                    Utility.Toast(this, getResources().getString(R.string.sorryThisFeatureIsStartFromKitKatDevices));
                    break;
                } else {
                    createWebPrintJob(webview);
                    break;
                }
            case R.id.ic_full_screen_view:
                hideSystemUI();
                break;
            case R.id.ic_print:
                if (Build.VERSION.SDK_INT >= 19) {
                    createWebPrintJob(webview);
                    break;
                }
                break;
            case R.id.ic_share:
                Utility.shareFile(this, filePath);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        public WebViewClient() {
        }

        @Override
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return true;
        }

        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (isFromAppActivity && !isExit) {
            isExit = true;
        }
        super.onBackPressed();
    }

    @SuppressLint("WrongConstant")
    private void createWebPrintJob(WebView webView) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        if (Build.VERSION.SDK_INT >= 21) {
            printAdapter = webView.createPrintDocumentAdapter("New_RTF_File.pdf");
        } else {
            printAdapter = webView.createPrintDocumentAdapter();
        }
        printJob = printManager.print(getString(R.string.app_name) + " Document", printAdapter, new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).setResolution(new PrintAttributes.Resolution(ScreenCVEdit.FIELD_ID, "print", 200, 200)).setColorMode(2).setMinMargins(PrintAttributes.Margins.NO_MARGINS).build());
        Singleton.getInstance().setFileDeleted(true);
    }

    @SuppressLint("StaticFieldLeak")
    class loadTextFromRtfFile extends AsyncTask<Void, Void, Void> {
        String html;

        loadTextFromRtfFile() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            File file = new File(filePath);
            RtfReader rtfReader = new RtfReader();
            RtfHtml rtfHtml = new RtfHtml();
            try {
                rtfReader.parse(file);
                html = rtfHtml.format(rtfReader.root, true);
                System.out.println();
                return null;
            } catch (RtfParseException e) {
                Utility.logCatMsg("RtfParseException " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            webview.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
        }
    }
}
