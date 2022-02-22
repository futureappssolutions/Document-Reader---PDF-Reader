package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.app.Application;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.AppOpenManager;
import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {

    public AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this, initializationStatus -> {
        });

        appOpenManager = new AppOpenManager(this);
    }
}
