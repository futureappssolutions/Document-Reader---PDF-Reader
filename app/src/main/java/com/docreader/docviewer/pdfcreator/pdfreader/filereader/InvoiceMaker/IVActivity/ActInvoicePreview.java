package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvTempletAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.ResumeTemp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVTemplate.InvoiceTemplate1;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.ArrayList;
import java.util.Objects;

public class ActInvoicePreview extends BaseActivity implements View.OnClickListener {
    public int invoiceId = 0;
    public boolean isPaidTemplate = false;
    public BusinessInfo businessInfo;
    public String currencySymbol = "";
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public CvTempletAdp mCvTempletAdapter;
    public PrintDocumentAdapter printAdapter;
    public ArrayList<ResumeTemp> list;
    public Invoice mInvoice;
    public SharedPrefs prefs;
    public PrintJob printJob;
    public RecyclerView rvFilterView;
    public Singleton singleton;
    public WebView webView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_preview);
        init();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void init() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        TextView textView = findViewById(R.id.toolBarTitle);

        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);

        singleton = Singleton.getInstance();
        businessInfo = singleton.getBusinessInfo(this);

        fillPreDefineData();

        prefs = new SharedPrefs(this);

        rvFilterView = findViewById(R.id.rvFilterView);
        webView = findViewById(R.id.webView);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setInitialScale(1);

        list = new ArrayList<>();
        list.add(new ResumeTemp(R.drawable.resume_3, true, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, true));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, true));

        if (getIntent() != null) {
            invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
            textView.setText("Preview Invoice");
            fillDetails();
        }

        webView.loadDataWithBaseURL(null, new InvoiceTemplate1().getContent(this, mInvoice), "text/html", "utf-8", null);

        mCvTempletAdapter = new CvTempletAdp(i -> {
            String content;
            for (int i2 = 0; i2 < list.size(); i2++) {
                list.get(i2).setSelected(false);
            }
            list.get(i).setSelected(true);
            mCvTempletAdapter.notifyDataSetChanged();

            if (i == 0) {
                InvoiceTemplate1 invoiceTemplate1 = new InvoiceTemplate1();
                content = invoiceTemplate1.getContent(this, mInvoice);
            } else if (i == 1) {
                InvoiceTemplate1 invoiceTemplate12 = new InvoiceTemplate1();
                content = invoiceTemplate12.getContent(this, mInvoice);
            } else if (i == 2) {
                InvoiceTemplate1 invoiceTemplate13 = new InvoiceTemplate1();
                content = invoiceTemplate13.getContent(this, mInvoice);
            } else if (i == 3) {
                InvoiceTemplate1 invoiceTemplate14 = new InvoiceTemplate1();
                content = invoiceTemplate14.getContent(this, mInvoice);
            } else if (i == 4) {
                InvoiceTemplate1 invoiceTemplate15 = new InvoiceTemplate1();
                content = invoiceTemplate15.getContent(this, mInvoice);
            } else if (i == 5) {
                InvoiceTemplate1 invoiceTemplate16 = new InvoiceTemplate1();
                content = invoiceTemplate16.getContent(this, mInvoice);
            } else {
                InvoiceTemplate1 invoiceTemplate17 = new InvoiceTemplate1();
                content = invoiceTemplate17.getContent(this, mInvoice);
            }
            webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        }, this, list);
        rvFilterView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvFilterView.setAdapter(mCvTempletAdapter);
    }

    private void fillPreDefineData() {
        currencySymbol = businessInfo.getCurrencySymbol();
    }

    private void fillDetails() {
        mInvoice = invoiceDatabaseHelper.getSelectedInvoices(invoiceId);
        mInvoice.getBusinessInfo().setCurrencyCode(businessInfo.getCurrencyCode());
        mInvoice.getBusinessInfo().setCurrencySymbol(businessInfo.getCurrencySymbol());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        menu.findItem(R.id.action_save).setTitle("Print");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.action_save) {
            createWebPrintJob(webView);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onBackPressed() {
        finish();
    }

    public void onClick(View view) {
        view.getId();
    }

    @SuppressLint("WrongConstant")
    public void createWebPrintJob(WebView webView2) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        if (Build.VERSION.SDK_INT >= 21) {
            printAdapter = webView2.createPrintDocumentAdapter("MyInvoice.pdf");
        } else {
            printAdapter = webView2.createPrintDocumentAdapter();
        }
        printJob = printManager.print(getString(R.string.app_name) + " Document", printAdapter, new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).setResolution(new PrintAttributes.Resolution(ScreenCVEdit.FIELD_ID, "print", 200, 200)).setColorMode(2).setMinMargins(PrintAttributes.Margins.NO_MARGINS).build());
    }

    public void onResume() {
        super.onResume();
        if (printJob != null && Build.VERSION.SDK_INT >= 19 && printJob.isCompleted()) {
            Utility.Toast(this, "Invoice Created Successfully..  can be find in PDF Files option in main screen");
            Singleton.getInstance().setFileDeleted(true);
        }
    }
}
