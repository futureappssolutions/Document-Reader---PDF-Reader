package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.Objects;

public class GoogleAppLovinAds {
    private static final String TAG = "AdsTags";
    public static InterstitialAd mInterstitialAd;
    public static NativeAd mNativeAd = null;
    public static AdView mAdView = null;
    public static MyCallback myCallback;

    public static MaxInterstitialAd maxInterstitialAd = null;
    public static MaxAd maxAd = null;
    @SuppressLint("StaticFieldLeak")
    public static MaxAdView maxAdView;
    public static MaxAd maxAdBanner;
    public static MaxNativeAdLoader nativeAdLoader;
    @SuppressLint("StaticFieldLeak")
    public static MaxNativeAdView mNativeAdView;
    public static MaxAd nativeAd;

    public static android.os.CountDownTimer allcount60;
    public static boolean adsdisplay = false;
    public static int retryAttempt;

    public static void preLoadAds(Activity activity) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        if (!(sharedPrefs.getActive_Weekly().equals("true") || sharedPrefs.getActive_Monthly().equals("true") || sharedPrefs.getActive_Yearly().equals("true"))) {
            if (sharedPrefs.getAds_name().equals("a")) {
                preLoadAppLovinFull(activity);
                preLoadAppLovinBanner(activity);
                preLoadApLovinNative(activity);
            } else {
                preLoadFull(activity);
                preLoadBanner(activity);
                preLoadNative(activity);
            }
        }
    }

    public static void preLoadFull(Activity activity) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, sharedPrefs.getGoogle_full(), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.d(TAG, "Google Full Load: onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, "Google Full Fail : " + loadAdError.getMessage());
                        mInterstitialAd = null;
                        preLoadAppLovinFull(activity);
                    }
                });
    }

    public static void preLoadAppLovinFull(Activity activity) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        maxInterstitialAd = new MaxInterstitialAd(sharedPrefs.getAppLovin_full(), activity);
        maxInterstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d(TAG, "Max Full onAdLoaded");
                maxAd = ad;
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {
                preLoadAppLovinFull(activity);
                interstitialCallBack();
                maxAd = null;
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                maxAd = null;
                Log.d(TAG, "Max Full onAdLoadFailed " + error.getMessage());
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.e("CheckLoadRoNo", "onAdDisplayFailed : " + error.getMessage());
            }
        });
        maxInterstitialAd.loadAd();
    }

    public static void preLoadBanner(Activity activity) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        AdView adView = new AdView(activity);
        adView.setAdUnitId(sharedPrefs.getGoogle_banner());
        AdSize adSize = getAdSize(activity);
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView = adView;
                Log.d(TAG, "Google Banner Load: onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mAdView = null;
                preLoadAppLovinBanner(activity);
                Log.d(TAG, "Google Banner Fail : " + loadAdError.getCode() + " : " + loadAdError.getMessage());

            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClosed() {
            }
        });
    }

    public static void preLoadAppLovinBanner(Activity activity) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        maxAdView = new MaxAdView(sharedPrefs.getAppLovin_banner(), activity);
        maxAdView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d(TAG, "Max Banner onAdLoaded");
                maxAdBanner = ad;
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
                Log.d(TAG, "Max Banner onAdLoadFailed : " + error.getMessage());
                maxAdBanner = null;
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });

        maxAdView.loadAd();
    }

    public static void preLoadNative(Activity activity) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        AdLoader.Builder builder = new AdLoader.Builder(activity, sharedPrefs.getGoogle_native()).forNativeAd(nativeAd -> {
            mNativeAd = nativeAd;
            Log.d(TAG, "Google Native Load: onAdLoaded");
        });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mNativeAd = null;
                @SuppressLint("DefaultLocale") String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(),
                        loadAdError.getMessage());
                Log.d(TAG, "Google Native Fail : " + error);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public static void preLoadApLovinNative(Activity activity) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.ad_applovin_view)
                .setTitleTextViewId(R.id.ad_advertiser)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_headline)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();
        MaxNativeAdView nativeAdView = new MaxNativeAdView(binder, activity);

        nativeAdLoader = new MaxNativeAdLoader(sharedPrefs.getAppLovin_native(), activity);
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(MaxNativeAdView mnativeAdView, MaxAd ad) {

                Log.d(TAG, "Max onNativeAdLoaded");
                // Cleanup any pre-existing native ad to prevent memory leaks.
                if (nativeAd != null) {
                    nativeAdLoader.destroy(nativeAd);
                }

                // Save ad for cleanup.
                nativeAd = ad;
                mNativeAdView = mnativeAdView;
                // Add ad view to view.

            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                Log.d(TAG, "Max onNativeAdLoadFailed : " + error.getMessage());
                nativeAd = null;
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {

            }
        });
        nativeAdLoader.loadAd(nativeAdView);
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

    public static void interstitialCallBack() {
        if (myCallback != null) {
            myCallback.callbackCall();
            myCallback = null;
        }
    }

    public static void showFullAds(Activity activity, MyCallback myCallback) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        GoogleAppLovinAds.myCallback = myCallback;
        if (!(sharedPrefs.getActive_Weekly().equals("true") || sharedPrefs.getActive_Monthly().equals("true") || sharedPrefs.getActive_Yearly().equals("true"))) {
            if (sharedPrefs.getAds_name().equals("a")) {
                if (maxAd != null) {
                    maxInterstitialAd.showAd();
                } else {
                    interstitialCallBack();
                }
            } else {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(activity);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.e(TAG, "The ad was dismissed.");
                            preLoadFull(activity);
                            interstitialCallBack();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            // Called when fullscreen content failed to show.
                            Log.e(TAG, "The ad failed to show.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                            Log.e(TAG, "The ad was shown.");
                        }
                    });
                } else {
                    if (maxAd != null) {
                        maxInterstitialAd.showAd();
                    } else {
                        interstitialCallBack();
                    }
                }
            }
        }
    }

    public static void showBannerAds(Activity activity, LinearLayout ll_banner) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        if (!(sharedPrefs.getActive_Weekly().equals("true") || sharedPrefs.getActive_Monthly().equals("true") || sharedPrefs.getActive_Yearly().equals("true"))) {
            if (sharedPrefs.getAds_name().equals("a")) {
                if (maxAdBanner != null) {
                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.banner_height);
                    maxAdView.setLayoutParams(new LinearLayout.LayoutParams(width, heightPx));
                    if (maxAdView.getParent() != null) {
                        ((ViewGroup) maxAdView.getParent()).removeView(maxAdView);
                    }
                    ll_banner.addView(maxAdView);
                    maxAdView = null;
                    maxAdBanner = null;
                    preLoadAppLovinBanner(activity);
                }
            } else {
                if (mAdView != null) {
                    if (mAdView.getParent() != null) {
                        ((ViewGroup) mAdView.getParent()).removeView(mAdView);
                    }
                    ll_banner.addView(mAdView);
                    mAdView = null;
                    preLoadBanner(activity);
                } else {
                    if (maxAdBanner != null) {
                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.banner_height);
                        maxAdView.setLayoutParams(new LinearLayout.LayoutParams(width, heightPx));
                        if (maxAdView.getParent() != null) {
                            ((ViewGroup) maxAdView.getParent()).removeView(maxAdView);
                        }
                        ll_banner.addView(maxAdView);
                        maxAdView = null;
                        maxAdBanner = null;
                        preLoadAppLovinBanner(activity);
                    }
                }
            }
        }
    }

    public static void showNativeAds(Activity activity, FrameLayout frameLayout) {
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        if (!(sharedPrefs.getActive_Weekly().equals("true") || sharedPrefs.getActive_Monthly().equals("true") || sharedPrefs.getActive_Yearly().equals("true"))) {
            if (sharedPrefs.getAds_name().equals("a")) {
                if (nativeAd != null) {
                    frameLayout.addView(mNativeAdView);
                    nativeAd = null;
                    preLoadApLovinNative(activity);
                }
            } else {
                if (mNativeAd != null) {
                    NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_new, null);
                    frameLayout.addView(adView);
                    populateUnifiedNativeAdView(mNativeAd, adView);
                    mNativeAd = null;
                    preLoadNative(activity);
                }
            }
        }
    }

    private static void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
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


