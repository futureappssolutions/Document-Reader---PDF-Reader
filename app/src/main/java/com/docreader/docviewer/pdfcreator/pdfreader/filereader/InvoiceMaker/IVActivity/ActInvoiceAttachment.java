package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.facebookMaster;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Attachment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.ContentTypes;

import java.util.Objects;

public class ActInvoiceAttachment extends AppCompatActivity implements View.OnClickListener {
    public String[] PERMISSIONS_LIST = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public EditText additionalDetailEt;
    public EditText descriptionEt;
    public ImageView imageView;
    public Attachment attachment;
    public int attachmentId = 0;
    public int invoiceId = 0;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult activityResult) {
            Intent data;
            if (activityResult.getResultCode() == -1 && (data = activityResult.getData()) != null) {
                Uri data2 = data.getData();
                Glide.with(ActInvoiceAttachment.this).load(data2).into(imageView);
                attachment.setImagePath(data2.toString());
            }
        }
    });

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_attachment);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        attachment = new Attachment();
        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);

        imageView = findViewById(R.id.imageView);
        descriptionEt = findViewById(R.id.descriptionEt);
        additionalDetailEt = findViewById(R.id.additionalDetailEt);

        SharedPrefs prefs = new SharedPrefs(ActInvoiceAttachment.this);

        FrameLayout fl_native = findViewById(R.id.fl_native);
        FrameLayout native_ad_container = findViewById(R.id.native_ad_container);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleNative(ActInvoiceAttachment.this, fl_native);
                    break;
                case "f":
                    facebookMaster.FBNative(ActInvoiceAttachment.this, native_ad_container);
                    break;
                case "both":
                    Advertisement.GoogleNativeBoth(ActInvoiceAttachment.this, fl_native, native_ad_container);
                    break;
            }
        }

        if (getIntent() != null) {
            invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
            attachmentId = Integer.parseInt(getIntent().getStringExtra("attachmentId"));
            if (attachmentId != 0) {
                attachment = invoiceDatabaseHelper.getAttachment(attachmentId + "");
            } else {
                attachment = new Attachment();
                attachment.setInvoiceId(invoiceId);
            }
            fillAttachmentDetail();
        }
    }

    private void fillAttachmentDetail() {
        if (!attachment.getImagePath().isEmpty()) {
            Glide.with(this).load(attachment.getImagePath()).into(imageView);
        }
        descriptionEt.setText(attachment.getDescription());
        additionalDetailEt.setText(attachment.getAdditionalDetails());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client_save, menu);
        if (attachmentId != 0) {
            menu.findItem(R.id.action_delete).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageView) {
            if (hasReadStoragePermission()) {
                pickImage();
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, 112);
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.action_delete:
                invoiceDatabaseHelper.removeAttachment(attachmentId + "");
                Singleton.getInstance().setInvoiceDataUpdated(true);
                finish();
                break;
            case R.id.action_save:
                if (attachment.getImagePath().length() <= 10) {
                    Utility.Toast(this, "Please select image first");
                } else {
                    insertUpdateAttachment();
                }
                break;
            case R.id.clearImage:
                imageView.setImageResource(R.drawable.icon_user_profile);
                attachment.setImagePath("");
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void insertUpdateAttachment() {
        attachment.setDescription(descriptionEt.getText().toString());
        attachment.setAdditionalDetails(additionalDetailEt.getText().toString());
        if (attachmentId == 0) {
            invoiceDatabaseHelper.addNewAttachment(attachment);
        } else {
            invoiceDatabaseHelper.updateAttachment(attachment);
        }
        Singleton.getInstance().setInvoiceDataUpdated(true);
        onBackPressed();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
