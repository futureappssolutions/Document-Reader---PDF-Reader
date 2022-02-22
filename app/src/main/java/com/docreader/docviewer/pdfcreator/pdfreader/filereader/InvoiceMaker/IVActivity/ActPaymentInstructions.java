package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.util.Objects;

public class ActPaymentInstructions extends BaseActivity {
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public EditText bankTransferEt;
    public EditText otherEt;
    public EditText payPalEmailEt;
    public EditText payableChecksEt;
    public TextView payPalEmailTl;
    public ImageView businessLogoIv;
    public BusinessInfo businessInfo;
    public Invoice mInvoice;
    public Singleton singleton;
    public int invoiceId = 0;
    public boolean isFromInvoiceActivity = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_payment_details);
        singleton = Singleton.getInstance();
        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);
        setStatusBar();
        init();
    }

    @SuppressLint("SetTextI18n")
    public void init() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView textView = findViewById(R.id.toolBarTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        payPalEmailEt = findViewById(R.id.payPalEmailEt);
        businessLogoIv = findViewById(R.id.businessLogoIv);
        payableChecksEt = findViewById(R.id.payableChecksEt);
        bankTransferEt = findViewById(R.id.bankTransferEt);
        otherEt = findViewById(R.id.otherEt);
        payPalEmailTl = findViewById(R.id.payPalEmailTl);

        payPalEmailEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (TextUtils.isEmpty(payPalEmailEt.getText())) {
                    payPalEmailTl.setText(null);
                } else if (!payPalEmailEt.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    payPalEmailTl.setVisibility(View.VISIBLE);
                    payPalEmailTl.setText("Email address is not valid");
                } else {
                    payPalEmailTl.setText(null);
                }
            }
        });

        if (getIntent() != null) {
            if (getIntent().hasExtra("invoiceId")) {
                invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
                isFromInvoiceActivity = true;
                textView.setText("Payment Instructions");
            } else {
                textView.setText("Business Detail");
            }
            fillBusinessInfoDetails();
        }
    }

    private void fillBusinessInfoDetails() {
        if (isFromInvoiceActivity) {
            mInvoice = invoiceDatabaseHelper.getSelectedInvoices(invoiceId);
            businessInfo = mInvoice.getBusinessInfo();
        } else {
            businessInfo = singleton.getBusinessInfo(this);
        }
        if (businessInfo != null) {
            payPalEmailEt.setText(businessInfo.getPaypalEmail());
            payableChecksEt.setText(businessInfo.getCheckPayableTo());
            bankTransferEt.setText(businessInfo.getBankTransfer());
            otherEt.setText(businessInfo.getOtherPayment());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        menu.findItem(R.id.action_save).setIcon(R.drawable.ic_arrow_white_true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.action_save && validInput()) {
            businessInfo.setPaypalEmail(payPalEmailEt.getText().toString());
            businessInfo.setCheckPayableTo(payableChecksEt.getText().toString());
            businessInfo.setBankTransfer(bankTransferEt.getText().toString());
            businessInfo.setOtherPayment(otherEt.getText().toString());
            if (isFromInvoiceActivity) {
                Invoice invoice = mInvoice;
                if (invoice != null) {
                    invoice.setBusinessInfo(businessInfo);
                    invoiceDatabaseHelper.updateInvoice(mInvoice);
                    Singleton.getInstance().setInvoiceDataUpdated(true);
                }
            } else {
                singleton.setAndSaveBusinessInfo(this, businessInfo);
            }
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressLint("SetTextI18n")
    private boolean validInput() {
        payPalEmailTl.setText(null);
        if (TextUtils.isEmpty(payPalEmailEt.getText()) || payPalEmailEt.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            return true;
        }
        payPalEmailTl.setVisibility(View.VISIBLE);
        payPalEmailTl.setText("Email address is not valid");
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
