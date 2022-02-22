package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Main;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.ActivityPremium;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.BuildConfig;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;

import java.util.Objects;

public class Setting extends BaseActivity implements View.OnClickListener {
    private SharedPrefs prefs;
    private SwitchCompat switchMarqueeEffect;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.setting));
        changeBackGroundColor(100);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        prefs = new SharedPrefs(this);
        switchMarqueeEffect = findViewById(R.id.switchMarqueeEffect);
        switchMarqueeEffect.setChecked(prefs.isMarqueeEffectEnable());

        findViewById(R.id.rlPrePlan).setOnClickListener(this);
        findViewById(R.id.rlLanguage).setOnClickListener(this);
        findViewById(R.id.rlFileEffect).setOnClickListener(this);
        findViewById(R.id.rlRateUs).setOnClickListener(this);
        findViewById(R.id.rlShare).setOnClickListener(this);
        findViewById(R.id.rlPrivacy).setOnClickListener(this);
        findViewById(R.id.rlTerm).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlFileEffect:
                prefs.setMarqueeEffectEnable(!prefs.isMarqueeEffectEnable());
                switchMarqueeEffect.setChecked(prefs.isMarqueeEffectEnable());
                return;
            case R.id.rlLanguage:
                startActivity(new Intent(this, Language.class));
                return;
            case R.id.rlPrePlan:
                startActivity(new Intent(this, ActivityPremium.class));
                return;
            case R.id.rlRateUs:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                return;
            case R.id.rlShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return;
            case R.id.rlPrivacy:
                return;
            case R.id.rlTerm:
                return;
            default:
        }
    }
}
