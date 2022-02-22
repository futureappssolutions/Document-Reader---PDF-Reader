package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.ModelDateFormat;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActInvoiceDateFormat extends BaseActivity {
    public RecyclerView recyclerView;
    public DateFormatListAdapter adapter;
    public List<ModelDateFormat> dateFormatList;
    public ProgressBar progressBar;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_invoice_date_formate);
        setStatusBar();
        init();
    }

    @SuppressLint("SetTextI18n")
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle((CharSequence) "");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.toolBarTitle)).setText("Choose Date Format");

        dateFormatList = new ArrayList<>();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new loadAllDateFormat().execute();
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

    private class loadAllDateFormat extends AsyncTask<Void, Void, Void> {
        private loadAllDateFormat() {
        }

        @Override
        public void onPreExecute() {
            dateFormatList.clear();
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            dateFormatList = new ModelDateFormat().getAllDateFormat();
            return null;
        }

        @Override
        public void onPostExecute(Void voidR) {
            progressBar.setVisibility(View.GONE);
            adapter = new DateFormatListAdapter(ActInvoiceDateFormat.this, dateFormatList);
            recyclerView.setAdapter(adapter);
            super.onPostExecute(voidR);
        }
    }

    static class DateFormatListAdapter extends RecyclerView.Adapter<DateFormatListAdapter.DateFormatViewHolder> {
        public List<ModelDateFormat> dateFormats;
        public Context mContext;

        private static class DateFormatViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout dataLayout;
            public TextView dateFormatTv;

            public DateFormatViewHolder(View view) {
                super(view);
                dateFormatTv = (TextView) view.findViewById(R.id.dateFormatTv);
                dataLayout = (LinearLayout) view.findViewById(R.id.dataLayout);
            }
        }

        public DateFormatListAdapter(Context context, List<ModelDateFormat> list) {
            mContext = context;
            dateFormats = list;
        }

        @NonNull
        @Override
        public DateFormatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new DateFormatViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.date_format_item, viewGroup, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(DateFormatViewHolder dateFormatViewHolder, int i) {
            final ModelDateFormat modelDateFormat = dateFormats.get(i);

            dateFormatViewHolder.dateFormatTv.setText(modelDateFormat.getDateFormat() + " (" + modelDateFormat.getDateFormatTime() + ")");

            dateFormatViewHolder.dataLayout.setOnClickListener(view -> {
                Singleton.getInstance().getBusinessInfo(mContext).setDateFormat(modelDateFormat.getDateFormat());
                Singleton.getInstance().setInvoiceDataUpdated(true);
                ((Activity) mContext).finish();
            });
        }

        @Override
        public int getItemCount() {
            return dateFormats.size();
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
