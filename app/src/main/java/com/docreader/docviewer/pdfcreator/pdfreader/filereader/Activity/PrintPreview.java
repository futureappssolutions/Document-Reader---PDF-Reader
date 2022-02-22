package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class PrintPreview extends AppCompatActivity {
    private PrintJob printJob;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_print_preview);
        setStatusBar();

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final WebView webView = findViewById(R.id.webView);
        webView.setInitialScale(100);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                if (i < 100) {
                    findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
                }
                if (i == 100) {
                    findViewById(R.id.progress_bar).setVisibility(View.GONE);
                }
            }
        });
        if (getIntent() != null) {
            webView.loadDataWithBaseURL(null, getHtmlContent(PdfCreate.data), "text/html", "utf-8", null);
        }
        findViewById(R.id.btnSaveAsPDF).setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 19) {
                createWebPrintJob(webView);
            }
        });
    }


    @SuppressLint("WrongConstant")
    public void createWebPrintJob(WebView webView) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter;
        if (Build.VERSION.SDK_INT >= 21) {
            printAdapter = webView.createPrintDocumentAdapter("New PDF File.pdf");
        } else {
            printAdapter = webView.createPrintDocumentAdapter();
        }
        printJob = printManager.print(getString(R.string.app_name) + " Document", printAdapter, new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).setColorMode(2).setMinMargins(PrintAttributes.Margins.NO_MARGINS).build());
    }

    public String getHtmlContent(String str) {
        String LoadData = LoadData("bootstrap/bootstrap.css");
        String LoadData2 = LoadData("dist/summernote.css");
        return "<html><head> <style>" + LoadData + LoadData2 + "</style></head><body>" + str + "</body></html>";
    }

    @Override
    public void onResume() {
        super.onResume();
        if (printJob != null && Build.VERSION.SDK_INT >= 19 && printJob.isCompleted()) {
            Utility.Toast(this, getResources().getString(R.string.pdfFileCreatedSuccessfully));
            Singleton.getInstance().setFileDeleted(true);
        }
    }

    public String LoadData(String str) {
        try {
            InputStream open = getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr);
        } catch (IOException unused) {
            return "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        } else {
            window.clearFlags(0);
        }
    }

}
