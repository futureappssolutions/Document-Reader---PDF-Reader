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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Product;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.Objects;

public class ActInvoiceProduct extends BaseActivity {
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public EditText productDescriptionEt;
    public EditText productNameEt;
    public EditText productPriceEt;
    public TextView productNameTL;
    public TextView productPriceEtTL;
    public int productId = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_product);
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


        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAppLovinAds.showBannerAds(ActInvoiceProduct.this, ll_banner);


        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);

        productNameEt = findViewById(R.id.productNameEt);
        productPriceEt = findViewById(R.id.productPriceEt);
        productDescriptionEt = findViewById(R.id.productDescriptionEt);
        productNameTL = findViewById(R.id.productNameTL);
        productPriceEtTL = findViewById(R.id.productPriceEtTL);

        productNameEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    productNameTL.setText(null);
                }
            }
        });

        productPriceEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    productPriceEtTL.setText(null);
                }
            }
        });

        if (getIntent() != null) {
            productId = Integer.parseInt(getIntent().getStringExtra(ScreenCVEdit.FIELD_ID));
            if (productId == 0) {
                textView.setText("Add New Product");
                return;
            }
            textView.setText("Edit Product");
            fillClientDetails();
        }
    }

    @SuppressLint("SetTextI18n")
    private void fillClientDetails() {
        Product product = invoiceDatabaseHelper.getProduct(productId + "");
        if (product != null) {
            productNameEt.setText(product.getProductName());
            productPriceEt.setText(product.getProductUnitCost() + "");
            productDescriptionEt.setText(product.getProductDescription());
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
            insertUpdateClient();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void insertUpdateClient() {
        Product product = new Product(productId, 0, productNameEt.getText().toString(), productDescriptionEt.getText().toString(), !productPriceEt.getText().toString().isEmpty() ? Float.parseFloat(productPriceEt.getText().toString()) : 0.0f);
        if (productId == 0) {
            invoiceDatabaseHelper.addNewProduct(product);
            Utility.Toast(this, "Product Added Successfully");
        } else {
            invoiceDatabaseHelper.updateProduct(product);
            Utility.Toast(this, "Product Updated Successfully");
        }
        Singleton.getInstance().setInvoiceDataUpdated(true);
        onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private boolean validInput() {
        productNameTL.setText(null);
        productPriceEtTL.setText(null);
        if (TextUtils.isEmpty(productNameEt.getText())) {
            productNameTL.setVisibility(View.VISIBLE);
            productNameTL.setText("Name cannot be empty");
            return false;
        } else if (!TextUtils.isEmpty(productPriceEt.getText())) {
            return true;
        } else {
            productPriceEtTL.setVisibility(View.VISIBLE);
            productPriceEtTL.setText("Price cannot be empty");
            return false;
        }
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
