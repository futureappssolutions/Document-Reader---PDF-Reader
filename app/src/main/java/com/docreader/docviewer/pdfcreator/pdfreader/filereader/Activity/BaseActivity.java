package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

public class BaseActivity extends AppCompatActivity {

    public boolean isServiceRunning(Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void rateUsClick() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

    public void changeBackGroundColor(int i) {
        int color = getResources().getColor(R.color.app_color);
        if (i == 0) {
            color = getResources().getColor(R.color.doc);
        } else if (i == 1) {
            color = getResources().getColor(R.color.xlx);
        } else if (i == 2) {
            color = getResources().getColor(R.color.ppt);
        } else if (i == 3) {
            color = getResources().getColor(R.color.pdf);
        } else if (i == 4) {
            color = getResources().getColor(R.color.txt);
        } else if (i == 5) {
            color = getResources().getColor(R.color.doc);
        } else if (i == 13) {
            color = getResources().getColor(R.color.rtf);
        } else if (i == 14) {
            color = getResources().getColor(R.color.ebook);
        } else if (i != 1010) {
            switch (i) {
                case 6:
                    color = getResources().getColor(R.color.code_dark);
                    break;
                case 7:
                    color = getResources().getColor(R.color.app_color4);
                    break;
                case 8:
                    color = getResources().getColor(R.color.folder_view);
                    break;
                case 9:
                    color = getResources().getColor(R.color.transparent);
                    break;
                default:
                    switch (i) {
                        case 100:
                            color = getResources().getColor(R.color.app_color);
                            break;
                        case 101:
                            color = getResources().getColor(R.color.notepad_color1);
                            break;
                        case 102:
                            color = getResources().getColor(R.color.notepad_color2);
                            break;
                        case 103:
                            color = getResources().getColor(R.color.notepad_color3);
                            break;
                        case 104:
                            color = getResources().getColor(R.color.notepad_color4);
                            break;
                        case 105:
                            color = getResources().getColor(R.color.notepad_color5);
                            break;
                        case 106:
                            color = getResources().getColor(R.color.notepad_color6);
                            break;
                        case 107:
                            color = getResources().getColor(R.color.notepad_color7);
                            break;
                        case 108:
                            color = getResources().getColor(R.color.notepad_color8);
                            break;
                        case 109:
                            color = getResources().getColor(R.color.notepad_color9);
                            break;
                    }
            }
        } else {
            color = getResources().getColor(R.color.notepad_color10);
        }
        LinearLayout linearLayout = findViewById(R.id.toolbarBackground);
        if (Build.VERSION.SDK_INT >= 21) {
            Utility.HideTitleBarBackground(this, false);
            linearLayout.setBackgroundColor(color);
            return;
        }
        linearLayout.setBackgroundColor(getResources().getColor(R.color.app_color));
        findViewById(R.id.status_bar_height).setVisibility(View.GONE);
    }

    public void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(3846);
        decorView.setOnSystemUiVisibilityChangeListener(i -> {
            if ((i & 4) == 0) {
                BaseActivity.this.showAndHide(true);
            } else {
                BaseActivity.this.showAndHide(false);
            }
        });
    }

    public void showAndHide(boolean z) {
        LinearLayout linearLayout = findViewById(R.id.toolbarBackground);
        if (linearLayout == null) {
            return;
        }
        if (z) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }
}
