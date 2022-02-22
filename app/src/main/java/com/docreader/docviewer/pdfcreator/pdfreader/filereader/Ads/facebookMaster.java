package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import java.util.ArrayList;
import java.util.List;

public class facebookMaster {
    public static InterstitialAd interstitialAd;
    public static NativeAdLayout nativeAdLayout;
    public static LinearLayout adView;
    public static NativeAd nativeAd;
    public static MyCallback myCallback;
    public static boolean isloading = false;

    public static void FBFullScreenCall(final Context context) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        interstitialAd = new InterstitialAd(context, sharedPrefs.getFacebook_full());
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                interstitialAd.loadAd();
                isloading = true;
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                isloading = false;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                isloading = false;
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        isloading = true;
    }

    public static void FBFullScreenLoad(Context context, final MyCallback _myCallback) {
        myCallback = _myCallback;
        try {
            if (interstitialAd != null) {
                if (interstitialAd.isAdLoaded()) {
                    interstitialAd.show();
                } else if (!isloading) {
                    interstitialAd.loadAd();
                    isloading = true;
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                } else if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            } else if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void FBFullScreenCallBoth(final Context context) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        interstitialAd = new InterstitialAd(context, sharedPrefs.getFacebook_full());
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                interstitialAd.loadAd();
                isloading = true;
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                isloading = false;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                isloading = false;
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        isloading = true;
    }

    public static void FBFullScreenLoadBoth(Context context, final MyCallback _myCallback) {
        myCallback = _myCallback;
        try {
            if (interstitialAd != null) {
                if (interstitialAd.isAdLoaded()) {
                    interstitialAd.show();
                } else if (!isloading) {
                    interstitialAd.loadAd();
                    isloading = true;
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                } else if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            } else if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void FbBanner(Context context, LinearLayout ll_banner) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        AdView adView = new AdView(context, sharedPrefs.getFacebook_banner(), AdSize.BANNER_HEIGHT_50);
        ll_banner.removeAllViews();
        ll_banner.addView(adView);

        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    public static void FBNative(final Activity context, final FrameLayout frameLayout) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        nativeAd = new NativeAd(context, sharedPrefs.getFacebook_native());

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                inflateAd(nativeAd, frameLayout, context);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
    }

    public static void inflateAd(NativeAd nativeAd, View v, Context context) {
        nativeAd.unregisterView();

        nativeAdLayout = v.findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(context);
        adView = (LinearLayout) inflater.inflate(R.layout.fb_native_main, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        LinearLayout adChoicesContainer = v.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }

    public interface MyCallback {
        void callbackCall();
    }
}