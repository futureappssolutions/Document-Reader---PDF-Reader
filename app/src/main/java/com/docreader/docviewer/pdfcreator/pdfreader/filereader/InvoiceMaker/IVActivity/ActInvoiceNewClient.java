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
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Client;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.Objects;

public class ActInvoiceNewClient extends BaseActivity {
    public EditText address1Et;
    public EditText address2Et;
    public EditText address3Et;
    public EditText clientNameEt;
    public EditText emailET;
    public EditText faxET;
    public EditText mobileEt;
    public EditText phoneEt;
    public TextView address1Tl;
    public TextView clientNameTl;
    public TextView emailTl;
    public Invoice mInvoice;
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public int clientId = 0;
    public int invoiceId = 0;
    public boolean isFromInvoiceActivity = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_new_cleint);
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
        GoogleAppLovinAds.showBannerAds(ActInvoiceNewClient.this, ll_banner);

        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);
        clientNameEt = findViewById(R.id.clientNameEt);
        mobileEt = findViewById(R.id.mobileEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailET = findViewById(R.id.emailET);
        faxET = findViewById(R.id.faxET);
        address1Et = findViewById(R.id.address1Et);
        address2Et = findViewById(R.id.address2Et);
        address3Et = findViewById(R.id.address3Et);
        clientNameTl = findViewById(R.id.clientNameTl);
        address1Tl = findViewById(R.id.address1Tl);
        emailTl = findViewById(R.id.emailTl);

        clientNameEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    clientNameTl.setText(null);
                }
            }
        });

        address1Et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    address1Tl.setText(null);
                }
            }
        });

        emailET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (TextUtils.isEmpty(emailET.getText())) {
                    emailTl.setText(null);
                } else if (!emailET.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    emailTl.setText("Email address is not valid");
                } else {
                    emailTl.setText(null);
                }
            }
        });

        if (getIntent() != null) {
            if (getIntent().hasExtra("invoiceId")) {
                invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
                isFromInvoiceActivity = true;
                textView.setText("Client");
            } else {
                int parseInt = Integer.parseInt(getIntent().getStringExtra(ScreenCVEdit.FIELD_ID));
                clientId = parseInt;
                if (parseInt == 0) {
                    textView.setText("Add New Client");
                } else {
                    textView.setText("Edit Client");
                }
            }
            fillClientDetails();
        }
    }

    private void fillClientDetails() {
        Client client;
        if (isFromInvoiceActivity) {
            mInvoice = invoiceDatabaseHelper.getSelectedInvoices(invoiceId);
            client = mInvoice.getClientInfo();
            clientId = client.getClientId();
        } else {
            client = invoiceDatabaseHelper.getClient(clientId + "");
        }
        if (client != null) {
            clientNameEt.setText(client.getClientName());
            mobileEt.setText(client.getClientMobile());
            phoneEt.setText(client.getClientPhone());
            emailET.setText(client.getClientEmail());
            faxET.setText(client.getClientFax());
            address1Et.setText(client.getAddress1());
            address2Et.setText(client.getAddress2());
            address3Et.setText(client.getAddress3());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client_save, menu);
        if (isFromInvoiceActivity) {
            menu.findItem(R.id.action_delete).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.action_delete) {
            Client client = new Client();
            client.setClientId(clientId);
            mInvoice.setClientInfo(client);
            invoiceDatabaseHelper.updateInvoice(mInvoice);
            Singleton.getInstance().setInvoiceDataUpdated(true);
            finish();
        } else if (itemId == R.id.action_save && validInput()) {
            insertUpdateClient();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void insertUpdateClient() {
        Client client = new Client(clientId, clientNameEt.getText().toString(), emailET.getText().toString(), phoneEt.getText().toString(), mobileEt.getText().toString(), faxET.getText().toString(), address1Et.getText().toString(), address2Et.getText().toString(), address3Et.getText().toString());
        if (isFromInvoiceActivity) {
            if (mInvoice != null) {
                mInvoice.setClientInfo(client);
                invoiceDatabaseHelper.updateInvoice(mInvoice);
            }
        } else if (clientId == 0) {
            invoiceDatabaseHelper.addNewClient(client);
            Utility.Toast(this, "Client Added Successfully");
        } else {
            invoiceDatabaseHelper.updateClient(client);
            Utility.Toast(this, "Client Updated Successfully");
        }
        Singleton.getInstance().setInvoiceDataUpdated(true);
        onBackPressed();
    }

    private boolean validInput() {
        clientNameTl.setText(null);
        address1Tl.setText(null);
        emailTl.setText(null);
        if (TextUtils.isEmpty(clientNameEt.getText())) {
            clientNameTl.setVisibility(View.VISIBLE);
            clientNameTl.setText("Name cannot be empty");
            return false;
        } else if (!TextUtils.isEmpty(emailET.getText()) && !emailET.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            emailTl.setVisibility(View.VISIBLE);
            emailTl.setText("Email address is not valid");
            return false;
        } else if (!TextUtils.isEmpty(address1Et.getText())) {
            return true;
        } else {
            address1Tl.setVisibility(View.VISIBLE);
            address1Tl.setText("Address cannot be empty");
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
