package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Main;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.LanguageAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.OnLanguageChooseItemClick;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.AppLanguage;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Language extends BaseActivity implements OnLanguageChooseItemClick {
    public int scrollToPosition = 0;
    public LanguageAdp adapter;
    public SharedPrefs prefs;
    public ProgressBar progressBar;
    public RecyclerView recyclerView;
    public SearchView searchView;
    public List<AppLanguage> supportedLanguages;
    public TextView toolBarTitle;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_language);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText(getResources().getString(R.string.changeAppLanguage));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        changeBackGroundColor(100);

        prefs = new SharedPrefs(this);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        loadSupportedLanguages();
    }

    public void onBackPressed() {
        super.onBackPressed();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                if (searchView.isIconified()) {
                    return false;
                }
                searchView.setIconified(true);
                return false;
            }

            public boolean onQueryTextChange(String str) {
                if (adapter == null) {
                    return false;
                }
                adapter.getFilter().filter(str);
                return false;
            }
        });
        return true;
    }

    private void loadSupportedLanguages() {
        supportedLanguages = AppLanguage.getAppSupportedLanguages();
        int i = 0;
        while (true) {
            if (i >= supportedLanguages.size()) {
                break;
            } else if (supportedLanguages.get(i).getCode().equals(prefs.getAppLocalizationCode())) {
                supportedLanguages.get(i).setSelected(true);
                scrollToPosition = i;
                break;
            } else {
                i++;
            }
        }
        adapter = new LanguageAdp(this, supportedLanguages);
        adapter.setOnRecyclerViewItemClick(this);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(scrollToPosition);
        progressBar.setVisibility(View.GONE);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressLint("WrongConstant")
    public void onItemClick(AppLanguage appLanguage, int i) {
        prefs.setAppLocalizationCode(appLanguage.getCode());
        SharedPrefs sharedPrefs = prefs;
        sharedPrefs.setAppLocalizationName(appLanguage.getName() + " (" + appLanguage.getLocalLanguageName() + ")");
        Locale locale = new Locale(appLanguage.getCode());
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
        Intent intent = new Intent(getApplicationContext(), Splash.class);
        intent.addFlags(67108864);
        startActivity(intent);
        if (Build.VERSION.SDK_INT >= 16) {
            finishAffinity();
        } else {
            ActivityCompat.finishAffinity(this);
        }
    }
}
