package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVTemplate.InvoiceHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.text.DecimalFormat;
import java.util.Objects;

public class ActInvoiceTax extends BaseActivity implements View.OnClickListener {
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public String taxType = InvoiceHelper.TAX_TYPE_NONE;
    public String currencySymbol = "";
    public TextView rateErrorTv;
    public TextView taxTitleTv;
    public TextView taxTypeTv;
    public EditText taxLabelEt;
    public EditText taxPercentageEt;
    public LinearLayout taxLayout;
    public Invoice mInvoice;
    public Singleton singleton;
    public BusinessInfo businessInfo;
    public int invoiceId = 0;
    public boolean isMakeChanges = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_tax);
        init();
    }

    @SuppressLint("SetTextI18n")
    public void init() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);
        singleton = Singleton.getInstance();
        businessInfo = singleton.getBusinessInfo(this);

        TextView textView = findViewById(R.id.toolBarTitle);
        taxLayout = findViewById(R.id.taxLayout);
        taxPercentageEt = findViewById(R.id.taxPercentageEt);
        taxLabelEt = findViewById(R.id.taxLabelEt);
        taxTitleTv = findViewById(R.id.taxTitleTv);
        taxTypeTv = findViewById(R.id.taxTypeTv);
        rateErrorTv = findViewById(R.id.rateErrorTv);

        findViewById(R.id.discountTypeLayout).setOnClickListener(this);

        fillPreDefineData();

        if (getIntent() != null) {
            invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
            textView.setText("Tax");
            fillDetails();
        }
    }

    private void fillPreDefineData() {
        currencySymbol = businessInfo.getCurrencySymbol();
    }

    private void fillDetails() {
        String str;
        mInvoice = invoiceDatabaseHelper.getSelectedInvoices(invoiceId);
        if (mInvoice != null) {
            taxType = mInvoice.getTaxType();
            if (taxType.equals(InvoiceHelper.TAX_TYPE_NONE)) {
                taxLayout.setVisibility(View.GONE);
            } else if (taxType.equals(InvoiceHelper.TAX_TYPE_ON_THE_TOTAL) || taxType.equals(InvoiceHelper.TAX_TYPE_DEDUCTED)) {
                str = new DecimalFormat("##.##").format(mInvoice.getTaxPercentage());
                taxPercentageEt.setText(str);
                taxTypeTv.setText(taxType);
            } else {
                taxLayout.setVisibility(View.GONE);
            }
            str = "";
            taxPercentageEt.setText(str);
            taxTypeTv.setText(taxType);
        }

        assert mInvoice != null;
        taxLabelEt.setText(mInvoice.getTaxLabel());

        taxLabelEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().isEmpty()) {
                    findViewById(R.id.labelErrorTv).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.labelErrorTv).setVisibility(View.GONE);
                }
            }
        });

        taxPercentageEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @SuppressLint("SetTextI18n")
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().isEmpty()) {
                    rateErrorTv.setText("Rate is required");
                    rateErrorTv.setVisibility(View.VISIBLE);
                } else if (charSequence.toString().trim().equals("0")) {
                    rateErrorTv.setText("Rate must be valid number");
                    rateErrorTv.setVisibility(View.VISIBLE);
                } else {
                    rateErrorTv.setVisibility(View.GONE);
                }
            }
        });

        taxLabelEt.setSelection(taxLabelEt.getText().length());
        taxPercentageEt.setSelection(taxPercentageEt.getText().length());
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
        } else if (itemId == R.id.action_save) {
            updatedInvoice();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void updatedInvoice() {
        mInvoice.setTaxPercentage(!taxPercentageEt.getText().toString().isEmpty() ? Float.parseFloat(taxPercentageEt.getText().toString()) : 0.0f);
        mInvoice.setTaxType(taxType);
        if (taxLabelEt.getText().toString().isEmpty()) {
            mInvoice.setTaxLabel("Tax");
        } else {
            mInvoice.setTaxLabel(taxLabelEt.getText().toString());
        }
        invoiceDatabaseHelper.updateInvoice(mInvoice);
        Singleton.getInstance().setInvoiceDataUpdated(true);
        isMakeChanges = false;
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isMakeChanges) {
            saveChangesAlertDialog();
            return;
        }
        finish();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.discountTypeLayout) {
            new AlertDialog.Builder(this).setCancelable(true).setItems(new String[]{InvoiceHelper.TAX_TYPE_NONE, InvoiceHelper.TAX_TYPE_ON_THE_TOTAL, InvoiceHelper.TAX_TYPE_DEDUCTED, InvoiceHelper.TAX_TYPE_ON_PER_ITEM}, (dialogInterface, i) -> {
                if (i == 0) {
                    taxType = InvoiceHelper.TAX_TYPE_NONE;
                    taxLayout.setVisibility(View.GONE);
                } else if (i == 1) {
                    taxType = InvoiceHelper.TAX_TYPE_ON_THE_TOTAL;
                    taxLayout.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    taxType = InvoiceHelper.TAX_TYPE_DEDUCTED;
                    taxLayout.setVisibility(View.VISIBLE);
                } else {
                    taxType = InvoiceHelper.TAX_TYPE_ON_PER_ITEM;
                    taxLayout.setVisibility(View.GONE);
                }
                taxTypeTv.setText(taxType);
                isMakeChanges = true;
            }).create().show();
        }
    }

    public void saveChangesAlertDialog() {
        new AlertDialog.Builder(this).setTitle("Save Changes!").setMessage("Do you want to save these change?").setPositiveButton("Save", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            updatedInvoice();
        }).setNegativeButton("Discard", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            isMakeChanges = false;
            onBackPressed();
        }).create().show();
    }
}
