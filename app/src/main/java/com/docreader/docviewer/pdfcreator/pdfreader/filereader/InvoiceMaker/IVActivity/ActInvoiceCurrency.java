package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVAdapter.IVCurrencyListAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActInvoiceCurrency extends BaseActivity {
    public RecyclerView recyclerView;
    public IVCurrencyListAdp adapter;
    public List<Currency> currencyList;
    public ProgressBar progressBar;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_invoice_currency);
        setStatusBar();

        init();
    }

    @SuppressLint("SetTextI18n")
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle((CharSequence) "");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.toolBarTitle)).setText("Choose Currency");

        currencyList = new ArrayList<>();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new loadAllCurries().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.searchHere));
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

    private class loadAllCurries extends AsyncTask<Void, Void, Void> {
        private loadAllCurries() {
        }

        @Override
        public void onPreExecute() {
            currencyList.clear();
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            currencyList = new Currency().getAllCurrency();
            return null;
        }


        public void onPostExecute(Void voidR) {
            progressBar.setVisibility(View.GONE);
            adapter = new IVCurrencyListAdp(ActInvoiceCurrency.this, currencyList);
            recyclerView.setAdapter(adapter);
            super.onPostExecute(voidR);
        }
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
