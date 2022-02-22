package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.facebookMaster;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment.FrgInvoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment.FrgInvoiceClients;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment.FrgInvoiceProduct;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment.FrgInvoiceSettings;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment.FrgInvoiceTutorial;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class ActInvoiceMain extends AppCompatActivity {

    private TextView toolBarTitle;

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_main);
        setStatusBar();

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolBarTitle = findViewById(R.id.toolBarTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SharedPrefs mPrefs = new SharedPrefs(this);
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(mPrefs.getActive_Weekly().equals("true") || mPrefs.getActive_Monthly().equals("true") || mPrefs.getActive_Yearly().equals("true"))) {
            switch (mPrefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(ActInvoiceMain.this, ll_banner);
                    break;
                case "f":
                    facebookMaster.FbBanner(ActInvoiceMain.this, ll_banner);
                    break;
                case "both":
                    Advertisement.GoogleBannerBoth(ActInvoiceMain.this, ll_banner);
                    break;
            }
        }

        ((BottomNavigationView) findViewById(R.id.nav_view)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_clients:
                    toolBarTitle.setText("Clients");
                    loadFragment(new FrgInvoiceClients());
                    return true;
                case R.id.navigation_invoice:
                    toolBarTitle.setText("Invoices");
                    loadFragment(new FrgInvoice());
                    return true;
                case R.id.navigation_items:
                    toolBarTitle.setText("Product");
                    loadFragment(new FrgInvoiceProduct());
                    return true;
                case R.id.navigation_tutorial:
                    toolBarTitle.setText("Tutorial");
                    loadFragment(new FrgInvoiceTutorial());
                    return true;
                case R.id.navigation_setting:
                    toolBarTitle.setText("Settings");
                    loadFragment(new FrgInvoiceSettings());
                    return true;
                default:
                    return false;
            }
        });

        loadFragment(new FrgInvoiceTutorial());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame_container, fragment);
        beginTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        } else {
            window.clearFlags(0);
        }
    }
}