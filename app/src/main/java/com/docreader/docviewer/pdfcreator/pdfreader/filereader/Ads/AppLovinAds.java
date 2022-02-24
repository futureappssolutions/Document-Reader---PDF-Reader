package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;

public class AppLovinAds {
    public static MaxInterstitialAd interstitialAd;
    public static MaxNativeAdLoader nativeAdLoader;
    public static MaxAd maxAd;
    public static MaxAd nativeAd;
    public static MyCallback myCallback;
    public static int retryAttempt;

    public static void AppLovinFullScreenCall(Activity context) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        interstitialAd = new MaxInterstitialAd(sharedPrefs.getAppLovin_full(), context);
        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                maxAd = ad;
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {
                AppLovinFullScreenCall(context);
                interstitialCallBack();
                maxAd = null;
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                maxAd = null;
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
            }
        });

        interstitialAd.loadAd();
    }

    public static void AppLovinFullScreenShow(MyCallback _myCallback) {
        myCallback = _myCallback;
        if (maxAd != null) {
            interstitialAd.showAd();
        } else {
            interstitialCallBack();
        }
    }

    public static void interstitialCallBack() {
        if (myCallback != null) {
            myCallback.callbackCall();
            myCallback = null;
        }
    }

    public static void AppLovinBanner(Activity activity, LinearLayout ll_banner) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        MaxAdView maxAdView = new MaxAdView(sharedPrefs.getAppLovin_banner(), activity);
        maxAdView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {

            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.banner_height);
        maxAdView.setLayoutParams(new LinearLayout.LayoutParams(width, heightPx));
        ll_banner.removeAllViews();
        ll_banner.addView(maxAdView);
        maxAdView.loadAd();
    }

    public static void AppLovinNative(Context context, FrameLayout frameLayout) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);

        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.ad_applovin_view)
                .setTitleTextViewId(R.id.ad_advertiser)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_headline)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();
        MaxNativeAdView nativeAdView = new MaxNativeAdView(binder, context);

        nativeAdLoader = new MaxNativeAdLoader(sharedPrefs.getAppLovin_native(), context);
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                if (nativeAd != null) {
                    nativeAdLoader.destroy(nativeAd);
                }

                nativeAd = maxAd;

                frameLayout.removeAllViews();
                frameLayout.addView(maxNativeAdView);
                super.onNativeAdLoaded(maxNativeAdView, maxAd);
            }

            @Override
            public void onNativeAdLoadFailed(String s, MaxError maxError) {
                nativeAd = null;
                super.onNativeAdLoadFailed(s, maxError);
            }

            @Override
            public void onNativeAdClicked(MaxAd maxAd) {
                super.onNativeAdClicked(maxAd);
            }
        });
        nativeAdLoader.loadAd(nativeAdView);
    }

    public interface MyCallback {
        void callbackCall();
    }
}

