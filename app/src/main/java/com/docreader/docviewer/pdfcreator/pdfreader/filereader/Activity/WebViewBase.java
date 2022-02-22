package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.facebookMaster;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;

public class WebViewBase extends BaseActivity {
    protected WebView f801wv;

    @SuppressLint({"WrongConstant", "SetJavaScriptEnabled"})
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        getWindow().setFlags(16777216, 16777216);

        SharedPrefs prefs = new SharedPrefs(WebViewBase.this);
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(WebViewBase.this, ll_banner);
                    break;
                case "f":
                    facebookMaster.FbBanner(WebViewBase.this, ll_banner);
                    break;
                case "both":
                    Advertisement.GoogleBannerBoth(WebViewBase.this, ll_banner);
                    break;
            }
        }

        f801wv = findViewById(R.id.browser);
        f801wv.setWebViewClient(new MyClient());
        f801wv.setScrollBarStyle(33554432);
        f801wv.setScrollbarFadingEnabled(true);
        f801wv.setLayerType(2, null);
        f801wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
            }
        });

        WebSettings settings = f801wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(2);
        settings.setDomStorageEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4 || !f801wv.canGoBack()) {
            return super.onKeyDown(i, keyEvent);
        }
        f801wv.goBack();
        return false;
    }

    @Override
    public void onResume() {
        f801wv.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        f801wv.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        f801wv.removeAllViews();
        f801wv.destroy();
        super.onDestroy();
    }

    static class MyClient extends WebViewClient {
        MyClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return super.shouldOverrideUrlLoading(webView, str);
        }

        @Override
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
        }

        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
        }
    }
}
