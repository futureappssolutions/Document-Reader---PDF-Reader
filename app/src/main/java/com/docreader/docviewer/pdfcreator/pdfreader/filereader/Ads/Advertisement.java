package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.Objects;

public class Advertisement {
    public static InterstitialAd mInterstitialAd;
    public static MyCallback myCallback;
    public static android.os.CountDownTimer allcount60;
    public static boolean adsdisplay = false;

    public static void GoogleFullScreenCall(Context context) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, sharedPrefs.getGoogle_full(), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mInterstitialAd = null;
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                        GoogleFullScreenCall(context);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    public static void FullScreenLoad(Activity activity, final MyCallback _myCallback) {
        myCallback = _myCallback;
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
            } else {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void GoogleFullScreenCallBoth(Context context) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, sharedPrefs.getGoogle_full(), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mInterstitialAd = null;
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                        GoogleFullScreenCallBoth(context);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    public static void FullScreenLoadBoth(Activity activity, final MyCallback _myCallback) {
        myCallback = _myCallback;
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
            } else {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                facebookMaster.FBFullScreenLoadBoth(activity, facebookMaster.myCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void GoogleBanner(Activity context, LinearLayout ll_banner) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(context);
        ll_banner.removeAllViews();
        ll_banner.addView(adView);
        AdSize adSize = getAdSize(context);
        adView.setAdSize(adSize);
        adView.setAdUnitId(sharedPrefs.getGoogle_banner());
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClosed() {
            }
        });
    }

    public static void GoogleBannerBoth(Context context, LinearLayout ll_banner) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(context);
        ll_banner.removeAllViews();
        ll_banner.addView(adView);
        adView.setAdSize(getAdSize((Activity) context));
        adView.setAdUnitId(sharedPrefs.getGoogle_banner());
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                facebookMaster.FbBanner(context, ll_banner);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClosed() {

            }
        });
    }

    public static AdSize getAdSize(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    @SuppressLint("InflateParams")
    public static void GoogleNative(Context context, FrameLayout frameLayout) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        AdLoader.Builder builder = new AdLoader.Builder(context, sharedPrefs.getGoogle_native()).forNativeAd(nativeAd -> {
            NativeAdView adView = (NativeAdView) LayoutInflater.from(context).inflate(R.layout.ad_unified_new, null);
            Google_viewNative(nativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });

        VideoOptions videoOptions = new VideoOptions.Builder().build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @SuppressLint("InflateParams")
    public static void GoogleNativeBoth(Context context, FrameLayout frameLayout, FrameLayout native_ad_container) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        AdLoader.Builder builder = new AdLoader.Builder(context, sharedPrefs.getGoogle_native()).forNativeAd(nativeAd -> {
            NativeAdView adView = (NativeAdView) LayoutInflater.from(context).inflate(R.layout.ad_unified_new, null);
            Google_viewNative(nativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });

        VideoOptions videoOptions = new VideoOptions.Builder().build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                facebookMaster.FBNative((Activity) context, native_ad_container);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void Google_viewNative(NativeAd nativeAd, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    imageView.setAdjustViewBounds(true);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });

        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }

    public interface MyCallback {
        void callbackCall();
    }
}