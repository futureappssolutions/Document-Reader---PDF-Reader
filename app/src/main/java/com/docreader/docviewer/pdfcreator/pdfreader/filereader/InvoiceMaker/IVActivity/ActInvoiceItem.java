package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVTemplate.InvoiceHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.InvoiceItem;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Product;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActInvoiceItem extends BaseActivity implements View.OnClickListener {
    public EditText additionalDetailEt;
    public EditText discountAmountEt;
    public EditText quantityEt;
    public EditText taxPercentageEt;
    public EditText unitCostEt;
    public String currencySymbol = "";
    public String discountType = InvoiceItem.PERCENTAGE;
    public String taxType = InvoiceHelper.TAX_TYPE_NONE;
    public TextView discountTypeTv;
    public TextView totalTv;
    public TextView invoiceNameTL;
    public SwitchCompat taxableSc;
    public LinearLayout textRateLayout;
    public InvoiceItem invoiceItem;
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public AutoCompleteTextView invoiceNameAutoCompleteTextView;
    public Invoice mInvoice;
    public BusinessInfo businessInfo;
    public Singleton singleton;
    public List<Product> savedProductList = new ArrayList<>();
    public ArrayList<String> suggestionProduct = new ArrayList<>();
    public float discountPercentage = 0.0f;
    public float discountValue = 0.0f;
    public float total = 0.0f;
    public int invoiceId = 0;
    public int invoiceItemId = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_item);
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



        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAppLovinAds.showBannerAds(ActInvoiceItem.this, ll_banner);


        invoiceNameTL = findViewById(R.id.invoiceNameTL);
        invoiceNameAutoCompleteTextView = findViewById(R.id.invoiceNameEt);
        unitCostEt = findViewById(R.id.unitCostEt);
        quantityEt = findViewById(R.id.quantityEt);
        discountAmountEt = findViewById(R.id.discountAmountEt);
        additionalDetailEt = findViewById(R.id.additionalDetailEt);
        taxPercentageEt = findViewById(R.id.taxPercentageEt);
        taxableSc = findViewById(R.id.taxableSc);
        textRateLayout = findViewById(R.id.textRateLayout);
        discountTypeTv = findViewById(R.id.discountTypeTv);
        totalTv = findViewById(R.id.totalTv);

        findViewById(R.id.discountTypeLayout).setOnClickListener(this);

        fillPreDefineData();

        taxableSc.setOnCheckedChangeListener((compoundButton, z) -> {
            if (!z || !taxType.equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM)) {
                textRateLayout.setVisibility(View.GONE);
            } else {
                textRateLayout.setVisibility(View.VISIBLE);
            }
        });

        if (getIntent() != null) {
            invoiceItemId = Integer.parseInt(getIntent().getStringExtra("invoiceItemId"));
            invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
            textView.setText("Item");
            mInvoice = invoiceDatabaseHelper.getSelectedInvoices(invoiceId);
            if (mInvoice != null) {
                taxType = mInvoice.getTaxType();
            }
            if (invoiceItemId == 0) {
                invoiceItem = new InvoiceItem();
            } else {
                fillInvoiceItemDetails();
            }
        }

        new loadProducts().execute();
    }

    private void fillPreDefineData() {
        currencySymbol = businessInfo.getCurrencySymbol();
        unitCostEt.setHint(currencySymbol + "0.00");

        quantityEt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                calculateTotal();
            }
        });

        unitCostEt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                calculateTotal();
            }
        });

        discountAmountEt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                calculateTotal();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void calculateTotal() {
        discountValue = 0.0f;
        discountPercentage = 0.0f;
        if (unitCostEt.getText().toString().isEmpty()) {
            total = 0.0f;
        } else {
            int i = 1;
            if (!quantityEt.getText().toString().isEmpty()) {
                i = Integer.parseInt(quantityEt.getText().toString());
            }
            float f = (float) i;
            total = Float.parseFloat(unitCostEt.getText().toString()) * f;
            if (!discountAmountEt.getText().toString().isEmpty()) {
                if (discountType.equals(InvoiceItem.PERCENTAGE)) {
                    discountPercentage = Float.parseFloat(discountAmountEt.getText().toString());
                    discountValue = Float.parseFloat(unitCostEt.getText().toString()) * (Float.parseFloat(discountAmountEt.getText().toString()) / 100.0f);
                    float f2 = discountValue * f;
                    discountValue = f2;
                    total -= f2;
                } else {
                    discountValue = Float.parseFloat(discountAmountEt.getText().toString());
                    total -= Float.parseFloat(discountAmountEt.getText().toString());
                }
            }
        }
        totalTv.setText(currencySymbol + new DecimalFormat("##.##").format(total));
    }

    @SuppressLint("SetTextI18n")
    private void fillInvoiceItemDetails() {
        InvoiceItem orderInvoicesItem = invoiceDatabaseHelper.getOrderInvoicesItem(invoiceItemId + "", invoiceId + "");
        if (orderInvoicesItem != null) {
            invoiceNameAutoCompleteTextView.setText(orderInvoicesItem.getInvoiceName());
            unitCostEt.setText(orderInvoicesItem.getUnitPrice() + "");
            quantityEt.setText(orderInvoicesItem.getQuantity() + "");
            additionalDetailEt.setText(orderInvoicesItem.getDetail() + "");
            if (orderInvoicesItem.getDiscountType().equals(InvoiceItem.PERCENTAGE)) {
                discountType = InvoiceItem.PERCENTAGE;
                discountTypeTv.setText("Percentage");
                discountAmountEt.setText(orderInvoicesItem.getDiscountPercentage() + "");
                discountAmountEt.setHint("0%");
            } else {
                discountType = InvoiceItem.FLAT_AMOUNT;
                discountTypeTv.setText("Flat amount");
                discountAmountEt.setText(orderInvoicesItem.getDiscount() + "");
                discountAmountEt.setHint("0");
            }
            if (orderInvoicesItem.getTaxable() == 0) {
                taxableSc.setChecked(false);
            } else {
                taxableSc.setChecked(true);
                if (taxType.equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM)) {
                    textRateLayout.setVisibility(View.VISIBLE);
                }
            }
            taxPercentageEt.setText(orderInvoicesItem.getVat() + "");
            calculateTotal();
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
            insertUpdateInvoiceItem();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void insertUpdateInvoiceItem() {
        float f;
        float f2;
        boolean isChecked = taxableSc.isChecked();
        int parseInt = !quantityEt.getText().toString().isEmpty() ? Integer.parseInt(quantityEt.getText().toString()) : 1;
        if (!taxPercentageEt.getText().toString().isEmpty()) {
            float parseFloat = Float.parseFloat(taxPercentageEt.getText().toString());
            float parseFloat2 = ((((float) parseInt) * Float.parseFloat(unitCostEt.getText().toString())) * parseFloat) / 100.0f;
            Utility.logCatMsg("vat rate " + parseFloat2);
            f2 = parseFloat;
            f = parseFloat2;
        } else {
            f2 = 0.0f;
            f = 0.0f;
        }
        InvoiceItem invoiceItem2 = new InvoiceItem(invoiceId, 0, parseInt, isChecked ? 1 : 0, Float.parseFloat(unitCostEt.getText().toString()), discountValue, discountPercentage, 0.0f, f2, f, invoiceNameAutoCompleteTextView.getText().toString(), discountType, additionalDetailEt.getText().toString());
        int i = invoiceItemId;
        if (i == 0) {
            invoiceDatabaseHelper.addNewInvoiceItem(invoiceItem2);
            Utility.Toast(this, "Invoice Item Successfully");
        } else {
            invoiceItem2.setInvoiceItemId(i);
            invoiceDatabaseHelper.updateInvoiceItem(invoiceItem2);
            Utility.Toast(this, "Invoice Item Updated Successfully");
        }
        Singleton.getInstance().setInvoiceDataUpdated(true);
        onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private boolean validInput() {
        invoiceNameTL.setText(null);
        if (TextUtils.isEmpty(invoiceNameAutoCompleteTextView.getText())) {
            invoiceNameTL.setVisibility(View.VISIBLE);
            invoiceNameTL.setText("Name cannot be empty");
            return false;
        } else if (!TextUtils.isEmpty(unitCostEt.getText())) {
            return true;
        } else {
            Utility.Toast(this, "Unit price cannot be empty");
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    public void onClick(View view) {
        if (view.getId() == R.id.discountTypeLayout) {
            new AlertDialog.Builder(this).setItems(new String[]{"Percentage", "Flat Amount"}, (dialogInterface, i) -> {
                if (i == 0) {
                    discountType = InvoiceItem.PERCENTAGE;
                    discountTypeTv.setText("Percentage");
                    discountAmountEt.setHint("0%");
                } else {
                    discountType = InvoiceItem.FLAT_AMOUNT;
                    discountTypeTv.setText("Flat amount");
                    discountAmountEt.setHint("0");
                }
                calculateTotal();
            }).create().show();
        }
    }

    class loadProducts extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            savedProductList.clear();
            suggestionProduct.clear();
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            savedProductList = invoiceDatabaseHelper.getAllProductItems();
            for (int i = 0; i < savedProductList.size(); i++) {
                suggestionProduct.add(savedProductList.get(i).getProductName());
            }
            return null;
        }

        @Override
        public void onPostExecute(Void voidR) {
            @SuppressLint("ResourceType") final ArrayAdapter arrayAdapter = new ArrayAdapter(ActInvoiceItem.this, 17367043, suggestionProduct);
            invoiceNameAutoCompleteTextView.setThreshold(2);
            invoiceNameAutoCompleteTextView.setAdapter(arrayAdapter);
            invoiceNameAutoCompleteTextView.setOnItemClickListener((adapterView, view, i, j) -> setPrice((String) arrayAdapter.getItem(i)));
            super.onPostExecute(voidR);
        }

        public void setPrice(String str) {
            for (int i = 0; i < savedProductList.size(); i++) {
                if (savedProductList.get(i).getProductName().equals(str)) {
                    unitCostEt.setText(savedProductList.get(i).getProductUnitCost() + "");
                    additionalDetailEt.setText(savedProductList.get(i).getProductDescription() + "");
                    return;
                }
            }
        }
    }
}
