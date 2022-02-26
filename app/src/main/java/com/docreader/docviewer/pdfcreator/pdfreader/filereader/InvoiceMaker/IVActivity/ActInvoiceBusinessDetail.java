package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.ContentTypes;

import java.util.Objects;

public class ActInvoiceBusinessDetail extends BaseActivity {
    public InvoiceDatabaseHelper mInvoiceDatabaseHelper;
    public BusinessInfo businessInfo;
    public Invoice mInvoice;
    public Singleton singleton;
    public TextView businessNameTl;
    public TextView emailTl;
    public ImageView businessLogoIv;
    public ImageView clearImage;
    public EditText address1Et;
    public EditText address2Et;
    public EditText address3Et;
    public EditText businessNameEt;
    public EditText businessNumberEt;
    public EditText emailET;
    public EditText faxET;
    public EditText mobileEt;
    public EditText ownerNameEt;
    public EditText phoneEt;
    public EditText websiteEt;
    public int invoiceId = 0;
    public boolean isFromSetting = false;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult activityResult) {
            Intent data;
            if (activityResult.getResultCode() == -1 && (data = activityResult.getData()) != null) {
                Uri data2 = data.getData();
                businessInfo.setLogoPath(data2.toString());
                Glide.with(ActInvoiceBusinessDetail.this).load(data2).into(businessLogoIv);
                clearImage.setVisibility(View.VISIBLE);
            }
        }
    });

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_busniess_details);
        setStatusBar();
        singleton = Singleton.getInstance();
        mInvoiceDatabaseHelper = new InvoiceDatabaseHelper(this);

        SharedPrefs mPrefs = new SharedPrefs(ActInvoiceBusinessDetail.this);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAppLovinAds.showBannerAds(ActInvoiceBusinessDetail.this, ll_banner);


        if (getIntent() != null) {
            isFromSetting = getIntent().getBooleanExtra("fromSetting", false);
            if (isFromSetting) {
                businessInfo = singleton.getBusinessInfo(this);
            } else {
                invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
                mInvoice = mInvoiceDatabaseHelper.getSelectedInvoices(invoiceId);
                if (mInvoice != null) {
                    businessInfo = mInvoice.getBusinessInfo();
                } else {
                    businessInfo = new BusinessInfo();
                }
            }
        }

        init();
    }

    public void init() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        businessNameEt = findViewById(R.id.businessNameEt);
        ownerNameEt = findViewById(R.id.ownerNameEt);
        businessNumberEt = findViewById(R.id.businessNumberEt);
        mobileEt = findViewById(R.id.mobileEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailET = findViewById(R.id.emailET);
        faxET = findViewById(R.id.faxET);
        businessLogoIv = findViewById(R.id.businessLogoIv);
        clearImage = findViewById(R.id.clearImage);
        address1Et = findViewById(R.id.address1Et);
        websiteEt = findViewById(R.id.websiteEt);
        address2Et = findViewById(R.id.address2Et);
        address3Et = findViewById(R.id.address3Et);
        businessNameTl = findViewById(R.id.businessNameTl);
        emailTl = findViewById(R.id.emailTl);

        businessLogoIv.setOnClickListener(view -> pickImage());

        clearImage.setOnClickListener(view -> {
            businessLogoIv.setImageResource(R.drawable.icon_user_profile);
            businessInfo.setLogoPath("");
            clearImage.setVisibility(View.GONE);
        });

        businessNameEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    businessNameTl.setText(null);
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
                    emailTl.setVisibility(View.VISIBLE);
                    emailTl.setText("Email address is not valid");
                } else {
                    emailTl.setText(null);
                }
            }
        });

        fillBusinessInfoDetails();
    }

    private void fillBusinessInfoDetails() {
        if (businessInfo != null) {
            businessNameEt.setText(businessInfo.getName());
            ownerNameEt.setText(businessInfo.getOwnerName());
            businessNumberEt.setText(businessInfo.getBusinessNumber());
            mobileEt.setText(businessInfo.getMobile());
            phoneEt.setText(businessInfo.getPhone());
            emailET.setText(businessInfo.getEmail());
            websiteEt.setText(businessInfo.getWebsite());
            address1Et.setText(businessInfo.getAddress1());
            address2Et.setText(businessInfo.getAddress2());
            address3Et.setText(businessInfo.getAddress3());
            setBusinessLogo();
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
            businessInfo.setName(businessNameEt.getText().toString());
            businessInfo.setOwnerName(ownerNameEt.getText().toString());
            businessInfo.setBusinessNumber(businessNumberEt.getText().toString());
            businessInfo.setMobile(mobileEt.getText().toString());
            businessInfo.setPhone(phoneEt.getText().toString());
            businessInfo.setEmail(emailET.getText().toString());
            businessInfo.setWebsite(websiteEt.getText().toString());
            businessInfo.setAddress1(address1Et.getText().toString());
            businessInfo.setAddress2(address2Et.getText().toString());
            businessInfo.setAddress3(address3Et.getText().toString());
            if (isFromSetting) {
                singleton.setAndSaveBusinessInfo(this, businessInfo);
            } else {
                mInvoiceDatabaseHelper.updateInvoice(mInvoice);
            }
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private boolean validInput() {
        businessNameTl.setText(null);
        emailTl.setText(null);
        if (TextUtils.isEmpty(businessNameEt.getText())) {
            businessNameTl.setVisibility(View.VISIBLE);
            businessNameTl.setText("Name cannot be empty");
            return false;
        } else if (TextUtils.isEmpty(emailET.getText()) || emailET.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            return true;
        } else {
            emailTl.setVisibility(View.VISIBLE);
            emailTl.setText("Email address is not valid");
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void pickImage() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{ContentTypes.IMAGE_JPEG, ContentTypes.IMAGE_PNG});
        someActivityResultLauncher.launch(intent);
    }

    public boolean hasReadStoragePermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int checkSelfPermission = ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        int checkSelfPermission2 = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        return checkSelfPermission == 0 && checkSelfPermission2 == 0;
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 112 && iArr.length > 0) {
            boolean z = false;
            boolean z2 = iArr[0] == 0;
            if (iArr[1] == 0) {
                z = true;
            }
            if (!z2 || !z) {
                Utility.Toast(this, getResources().getString(R.string.permission_denied_message2));
            } else {
                pickImage();
            }
        }
    }

    private void setBusinessLogo() {
        if (businessInfo.getLogoPath() != null && !businessInfo.getLogoPath().isEmpty()) {
            Glide.with(this).load(Uri.parse(businessInfo.getLogoPath())).into(businessLogoIv);
            clearImage.setVisibility(View.VISIBLE);
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
