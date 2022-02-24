package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.app.Application;

import com.applovin.sdk.AppLovinSdk;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.AppOpenManager;
import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {

    public AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this, initializationStatus -> {
        });
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> {
            // AppLovin SDK is initialized, start loading ads
        });

        appOpenManager = new AppOpenManager(this);
    }
}
