package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.AppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;

public final class ScreenResumeHome extends BaseActivity {
    public SharedPrefs prefs;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_resume_home);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.professionalResumeMaker));
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        prefs = new SharedPrefs(ScreenResumeHome.this);

        FrameLayout fl_native = findViewById(R.id.fl_native);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleNative(ScreenResumeHome.this, fl_native);
                    break;
                case "a":
                    AppLovinAds.AppLovinNative(ScreenResumeHome.this, fl_native);
                    break;

            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    public final void onButtonClick(View view) {
        String str;
        switch (view.getId()) {
            case R.id.action_education:
                str = "3";
                break;
            case R.id.action_experience:
                str = "4";
                break;
            case R.id.action_hobby:
                if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
                    switch (prefs.getAds_name()) {
                        case "g":
                            if (Advertisement.adsdisplay) {
                                Advertisement.FullScreenLoad(ScreenResumeHome.this, () -> {
                                    Advertisement.allcount60.start();
                                    IntentHobby();
                                });
                            } else {
                                IntentHobby();
                            }
                            break;
                        case "a":
                            if (Advertisement.adsdisplay) {
                                AppLovinAds.AppLovinFullScreenShow(() -> {
                                    Advertisement.allcount60.start();
                                    IntentHobby();
                                });
                            } else {
                                IntentHobby();
                            }
                            break;
                    }
                } else {
                    IntentHobby();
                }
                return;
            case R.id.action_language:
                if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
                    switch (prefs.getAds_name()) {
                        case "g":
                            if (Advertisement.adsdisplay) {
                                Advertisement.FullScreenLoad(ScreenResumeHome.this, () -> {
                                    Advertisement.allcount60.start();
                                    IntentLanguage();
                                });
                            } else {
                                IntentLanguage();
                            }
                            break;
                        case "a":
                            if (Advertisement.adsdisplay) {
                                AppLovinAds.AppLovinFullScreenShow(() -> {
                                    Advertisement.allcount60.start();
                                    IntentLanguage();
                                });
                            } else {
                                IntentLanguage();
                            }
                            break;
                    }
                } else {
                    IntentLanguage();
                }
                return;
            case R.id.action_personal_info:
                str = "0";
                break;
            case R.id.action_preview:
                str = "5";
                break;
            case R.id.action_projects:
                str = "2";
                break;
            case R.id.otherDetail:
                str = "6";
                break;
            case R.id.userSkill:
                if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
                    switch (prefs.getAds_name()) {
                        case "g":
                            if (Advertisement.adsdisplay) {
                                Advertisement.FullScreenLoad(ScreenResumeHome.this, () -> {
                                    Advertisement.allcount60.start();
                                    IntentSkill();
                                });
                            } else {
                                IntentSkill();
                            }
                            break;
                        case "a":
                            if (Advertisement.adsdisplay) {
                                AppLovinAds.AppLovinFullScreenShow(() -> {
                                    Advertisement.allcount60.start();
                                    IntentSkill();
                                });
                            } else {
                                IntentSkill();
                            }
                            break;
                    }
                } else {
                    IntentSkill();
                }
                return;
            case R.id.settingDetail:
                if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
                    switch (prefs.getAds_name()) {
                        case "g":
                            if (Advertisement.adsdisplay) {
                                Advertisement.FullScreenLoad(ScreenResumeHome.this, () -> {
                                    Advertisement.allcount60.start();
                                    IntentResume("7");
                                });
                            } else {
                                IntentResume("7");
                            }
                            break;
                        case "a":
                            if (Advertisement.adsdisplay) {
                                AppLovinAds.AppLovinFullScreenShow(() -> {
                                    Advertisement.allcount60.start();
                                    IntentResume("7");
                                });
                            } else {
                                IntentResume("7");
                            }
                            break;
                    }
                } else {
                    IntentResume("7");
                }
                return;
            default:
                str = "1";
                break;
        }

        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    if (Advertisement.adsdisplay) {
                        Advertisement.FullScreenLoad(ScreenResumeHome.this, () -> {
                            Advertisement.allcount60.start();
                            IntentResume(str);
                        });
                    } else {
                        IntentResume(str);
                    }
                    break;
                case "a":
                    if (Advertisement.adsdisplay) {
                        AppLovinAds.AppLovinFullScreenShow(() -> {
                            Advertisement.allcount60.start();
                            IntentResume(str);
                        });
                    } else {
                        IntentResume(str);
                    }
                    break;
            }
        } else {
            IntentResume(str);
        }
    }

    private void IntentHobby() {
        startActivity(new Intent(this, ScreenHobbies.class));
    }

    private void IntentLanguage() {
        startActivity(new Intent(this, ScreenCVLanguage.class));
    }

    private void IntentSkill() {
        startActivity(new Intent(this, ScreenSkill.class));
    }

    private void IntentResume(String str) {
        Intent intent = new Intent(this, ScreenResume.class);
        intent.putExtra("value", str);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
