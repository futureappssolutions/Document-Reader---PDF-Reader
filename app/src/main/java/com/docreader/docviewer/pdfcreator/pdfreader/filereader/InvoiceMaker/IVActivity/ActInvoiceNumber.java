package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.core.motion.utils.TypedValues;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.Calendar;
import java.util.Objects;

public class ActInvoiceNumber extends BaseActivity implements View.OnClickListener {
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public BusinessInfo businessInfo;
    public LinearLayout discountLayout;
    public Invoice mInvoice;
    public Singleton singleton;
    public String creationDate = "";
    public String currencySymbol = "";
    public String dateFormat = "";
    public String dueDate = "";
    public String terms = "";
    public TextView dueDateTv;
    public TextView labelInvoiceErrorTv;
    public TextView termsTv;
    public TextView creationDateTv;
    public EditText invoiceNumberEt;
    public EditText poNumberEt;
    public int invoiceId = 0;
    public boolean isMakeChanges = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_invoice_number);
        init();
    }


    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle((CharSequence) "");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);
        singleton = Singleton.getInstance();
        businessInfo = singleton.getBusinessInfo(this);

        TextView textView = (TextView) findViewById(R.id.toolBarTitle);
        discountLayout = (LinearLayout) findViewById(R.id.discountLayout);
        poNumberEt = (EditText) findViewById(R.id.poNumberEt);
        labelInvoiceErrorTv = (TextView) findViewById(R.id.labelInvoiceErrorTv);
        invoiceNumberEt = (EditText) findViewById(R.id.invoiceNumberEt);
        creationDateTv = (TextView) findViewById(R.id.dateTv);
        termsTv = (TextView) findViewById(R.id.termsTv);
        dueDateTv = (TextView) findViewById(R.id.dueDateTv);

        invoiceNumberEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().isEmpty()) {
                    findViewById(R.id.labelInvoiceErrorTv).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.labelInvoiceErrorTv).setVisibility(View.GONE);
                }
            }
        });

        fillPreDefineData();

        if (getIntent() != null) {
            invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
            textView.setText(Html.fromHtml("Invoice Info"));
            fillDetails();
        }
    }

    private void fillPreDefineData() {
        currencySymbol = businessInfo.getCurrencySymbol();
        dateFormat = businessInfo.getDateFormat();
    }

    private void fillDetails() {
        mInvoice = invoiceDatabaseHelper.getSelectedInvoices(invoiceId);
        if (mInvoice != null) {
            creationDate = mInvoice.getCreationDate();
            dueDate = mInvoice.getDueDate();

            dueDateTv.setText(Utility.geDateTime(Long.parseLong(dueDate), dateFormat));
            creationDateTv.setText(Utility.geDateTime(Long.parseLong(creationDate), dateFormat));
            invoiceNumberEt.setText(mInvoice.getInvoiceNumber());
            poNumberEt.setText(mInvoice.getPoNumber());

            terms = mInvoice.getTerms();
            termsTv.setText(terms);
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
        } else if (itemId == R.id.action_save) {
            if (!invoiceNumberEt.getText().toString().trim().isEmpty()) {
                updatedInvoice();
            } else {
                Utility.Toast(this, "Invoice Number is required");
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void updatedInvoice() {
        mInvoice.setInvoiceNumber(invoiceNumberEt.getText().toString());
        mInvoice.setPoNumber(poNumberEt.getText().toString());
        mInvoice.setDueDate(dueDate);
        mInvoice.setTerms(terms);
        mInvoice.setCreationDate(creationDate);
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
        int id = view.getId();
        if (id == R.id.dateLayout) {
            Calendar instance = Calendar.getInstance();
            new DatePickerDialog(this, (datePicker, i, i2, i3) -> {
                Calendar instance1 = Calendar.getInstance();
                instance1.set(5, i3);
                instance1.set(2, i2);
                instance1.set(1, i);
                creationDate = instance1.getTimeInMillis() + "";
                creationDateTv.setText(Utility.geDateTime(Long.parseLong(creationDate), dateFormat));
            }, instance.get(1), instance.get(2), instance.get(5)).show();
        } else if (id == R.id.dueDateLayout) {
            Calendar instance2 = Calendar.getInstance();
            new DatePickerDialog(this, (datePicker, i, i2, i3) -> {
                Calendar instance = Calendar.getInstance();
                instance.set(5, i3);
                instance.set(2, i2);
                instance.set(1, i);
                dueDate = instance.getTimeInMillis() + "";
                dueDateTv.setText(Utility.geDateTime(Long.parseLong(dueDate), dateFormat));
                terms = "Custom";
                termsTv.setText(terms);
            }, instance2.get(1), instance2.get(2), instance2.get(5)).show();
        } else if (id == R.id.termsLayout) {
            final String[] strArr = {"None", "Due on receipt", "Next Day", "2 Days", "3 Days", "4 Days", "5 Days", "6 Days", "7 Days", "8 Days", "9 Days", "10 Days", "14 Days", "21 Days", "30 Days", "45 Days", "60 Days", "90 Days", TypedValues.Custom.NAME};
            new AlertDialog.Builder(this).setCancelable(true).setItems(strArr, (dialogInterface, i) -> {
                terms = strArr[i];
                termsTv.setText(terms);
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

