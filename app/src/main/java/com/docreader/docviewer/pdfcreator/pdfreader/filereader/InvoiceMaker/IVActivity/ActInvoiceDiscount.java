package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.InvoiceItem;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.text.DecimalFormat;
import java.util.Objects;

public class ActInvoiceDiscount extends BaseActivity implements View.OnClickListener {
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public String discountType = InvoiceItem.PERCENTAGE;
    public String currencySymbol = "";
    public EditText discountAmountEt;
    public TextView discountTypeTv;
    public LinearLayout discountLayout;
    public BusinessInfo businessInfo;
    public Invoice mInvoice;
    public Singleton singleton;
    public int invoiceId = 0;
    public boolean isMakeChanges = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_discount);
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

        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);
        singleton = Singleton.getInstance();
        businessInfo = singleton.getBusinessInfo(this);

        discountLayout = findViewById(R.id.discountLayout);
        discountAmountEt = findViewById(R.id.discountAmountEt);
        discountTypeTv = findViewById(R.id.discountTypeTv);

        findViewById(R.id.discountTypeLayout).setOnClickListener(this);

        fillPreDefineData();

        if (getIntent() != null) {
            invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
            textView.setText("Discount");
            fillDetails();
        }
    }

    private void fillPreDefineData() {
        currencySymbol = businessInfo.getCurrencySymbol();
    }

    @SuppressLint("SetTextI18n")
    private void fillDetails() {
        mInvoice = invoiceDatabaseHelper.getSelectedInvoices(invoiceId);
        if (mInvoice != null) {
            if (mInvoice.getDiscountPercentage() == 0.0f && mInvoice.getDiscount() == 0.0f) {
                if (mInvoice.getDiscountType().equals(InvoiceItem.NO_DISCOUNT)) {
                    discountLayout.setVisibility(View.GONE);
                    discountTypeTv.setText("No Discount");
                }
                discountAmountEt.setText("");
            } else if (mInvoice.getDiscountType().equals(InvoiceItem.PERCENTAGE)) {
                discountType = InvoiceItem.PERCENTAGE;
                discountTypeTv.setText("Percentage");
                discountAmountEt.setText(new DecimalFormat("##.##").format(mInvoice.getDiscountPercentage()) + "");
                discountAmountEt.setHint("0%");
            } else {
                discountType = InvoiceItem.FLAT_AMOUNT;
                discountTypeTv.setText("Flat amount");
                discountAmountEt.setText(new DecimalFormat("##.##").format(mInvoice.getDiscount()) + "");
                discountAmountEt.setHint(currencySymbol + "0");
            }
            discountAmountEt.setSelection(discountAmountEt.getText().length());
        }

        discountAmountEt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                isMakeChanges = true;
            }
        });
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
        mInvoice.setDiscountType(discountType);
        float parseFloat = !discountAmountEt.getText().toString().isEmpty() ? Float.parseFloat(discountAmountEt.getText().toString()) : 0.0f;
        if (discountType.equals(InvoiceItem.NO_DISCOUNT)) {
            mInvoice.setDiscountPercentage(0.0f);
            mInvoice.setDiscount(0.0f);
        } else if (discountType.equals(InvoiceItem.PERCENTAGE)) {
            mInvoice.setDiscountPercentage(parseFloat);
            mInvoice.setDiscount(0.0f);
        } else {
            mInvoice.setDiscountPercentage(0.0f);
            mInvoice.setDiscount(parseFloat);
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

    @SuppressLint("SetTextI18n")
    public void onClick(View view) {
        if (view.getId() == R.id.discountTypeLayout) {
            new AlertDialog.Builder(this).setCancelable(true).setItems(new String[]{"No Discount", "Percentage", "Flat Amount"}, (dialogInterface, i) -> {
                if (i == 0) {
                    discountType = InvoiceItem.NO_DISCOUNT;
                    discountTypeTv.setText("No Discount");
                    discountLayout.setVisibility(View.GONE);
                } else if (i == 1) {
                    discountType = InvoiceItem.PERCENTAGE;
                    discountTypeTv.setText("Percentage");
                    discountAmountEt.setHint("0%");
                    discountLayout.setVisibility(View.VISIBLE);
                } else {
                    discountType = InvoiceItem.FLAT_AMOUNT;
                    discountTypeTv.setText("Flat amount");
                    discountAmountEt.setHint(currencySymbol + "0");
                    discountLayout.setVisibility(View.VISIBLE);
                }
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
