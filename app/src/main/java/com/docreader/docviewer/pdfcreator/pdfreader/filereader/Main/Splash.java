package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

import java.util.Locale;

public class Splash extends BaseActivity implements OnSuccessListener<AppUpdateInfo> {

    public static final int REQUEST_CODE = 1234;
    public final int RC_APP_UPDATE = 100;
    public boolean mNeedsFlexibleUpdate;
    public boolean UpdateAvailable = false;
    public AppUpdateManager appUpdateManager;
    public RelativeLayout rl_splash;
    public SharedPrefs prefs;

    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (appUpdateManager != null) {
                    appUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            }
        }
    };

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        setContentView(R.layout.activity_splash);

        prefs = new SharedPrefs(this);

        rl_splash = findViewById(R.id.rl_splash);

        appUpdateManager = AppUpdateManagerFactory.create(Splash.this);
        mNeedsFlexibleUpdate = false;


        prefs.setRemove_ads_weekly("weekly_plan");
        prefs.setRemove_ads_monthly("monthly_plan");
        prefs.setRemove_ads_yearly("yearly_plan");


        try {
            GoogleAds.allcount60 = new android.os.CountDownTimer(8 * 1000L, 1000) {
                public void onTick(long millisUntilFinished) {
                    GoogleAds.adsdisplay = false;
                }

                public void onFinish() {
                    GoogleAds.adsdisplay = true;
                }
            };
            GoogleAds.allcount60.start();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        GoogleAds.preLoadAds(Splash.this);

        new Handler().postDelayed(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    if (prefs.getAppLocalizationCode().equals("en")) {
                        startActivity(new Intent(getBaseContext(), ActMain.class));
                    } else {
                        setLocale(prefs.getAppLocalizationCode());
                    }
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                    startActivityForResult(intent, 100);
                }
            } else {
                ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        }, 1700);
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            if (iArr.length > 0 && iArr[0] == PackageManager.PERMISSION_GRANTED) {
                if (prefs.getAppLocalizationCode().equals("en")) {
                    startActivity(new Intent(getBaseContext(), ActMain.class));
                } else {
                    setLocale(prefs.getAppLocalizationCode());
                }
                finish();
            } else {
                Utility.Toast(this, getResources().getString(R.string.permission_denied_message2));
            }
        }
    }


    @Override
    public void onSuccess(AppUpdateInfo appUpdateInfo) {
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
            startUpdate(appUpdateInfo);
        } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackBarForCompleteUpdate();
        } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdate(appUpdateInfo);
            } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                mNeedsFlexibleUpdate = true;
                showFlexibleUpdateNotification();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        appUpdateManager = AppUpdateManagerFactory.create(this);

        appUpdateManager.registerListener(installStateUpdatedListener);

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                UpdateAvailable = true;
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, Splash.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else {
                UpdateAvailable = false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    private void startUpdate(final AppUpdateInfo appUpdateInfo) {
        final Activity activity = this;
        new Thread(() -> {
            try {
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        activity,
                        REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(rl_splash, "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("INSTALL", view -> {
            if (appUpdateManager != null) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.app_color));
        snackbar.show();
    }

    private void showFlexibleUpdateNotification() {
        Snackbar snackbar = Snackbar.make(rl_splash, "An update is available and accessible in More.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (prefs.getAppLocalizationCode().equals("en")) {
                startActivity(new Intent(getBaseContext(), ActMain.class));
            } else {
                setLocale(prefs.getAppLocalizationCode());
            }
            finish();
        }


        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                UpdateAvailable = false;
            }
        }
    }

    public void setLocale(String str) {
        Locale locale = new Locale(str);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
        startActivity(new Intent(this, ActMain.class));
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.app_color ));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.app_color));
        } else {
            window.clearFlags(0);
        }
    }

}
