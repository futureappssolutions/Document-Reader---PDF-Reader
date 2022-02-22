package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.res.ResConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

public class ActInvoiceSignature extends AppCompatActivity implements View.OnClickListener {
    public int invoiceId = 0;
    public CardView clearCv;
    public CardView deleteCv;
    public CardView resignCv;
    public CardView saveCv;
    public CardView undoCv;
    public Invoice mInvoice;
    public InvoiceDatabaseHelper mInvoiceDatabaseHelper;
    public SignaturePad mSignaturePad;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_invoice_signature);

        mInvoiceDatabaseHelper = new InvoiceDatabaseHelper(this);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        undoCv = (CardView) findViewById(R.id.undoCv);
        clearCv = (CardView) findViewById(R.id.clearCv);
        deleteCv = (CardView) findViewById(R.id.deleteCv);
        resignCv = (CardView) findViewById(R.id.resignCv);
        saveCv = (CardView) findViewById(R.id.saveCv);

        if (getIntent() != null) {
            invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
            mInvoice = mInvoiceDatabaseHelper.getSelectedInvoices(invoiceId);
            if (!mInvoice.getSignature().isEmpty()) {
                byte[] decode = Base64.decode(mInvoice.getSignature(), 0);
                mSignaturePad.setSignatureBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));
                deleteCv.setVisibility(View.VISIBLE);
                resignCv.setVisibility(View.VISIBLE);
                saveCv.setVisibility(View.GONE);
                clearCv.setVisibility(View.GONE);
            } else {
                clearCv.setVisibility(View.VISIBLE);
                saveCv.setVisibility(View.VISIBLE);
                deleteCv.setVisibility(View.GONE);
                resignCv.setVisibility(View.GONE);
            }
            undoCv.setVisibility(View.GONE);
        }

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            public void onClear() {
            }

            public void onSigned() {
            }

            public void onStartSigning() {
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelCv /*2131362050*/:
                onBackPressed();
                return;
            case R.id.clearCv /*2131362107*/:
                mSignaturePad.clear();
                return;
            case R.id.deleteCv /*2131362229*/:
                new AlertDialog.Builder(this).setTitle((CharSequence) "Delete.!").setMessage((CharSequence) "Are you sure to delete signature?").setPositiveButton((CharSequence) ResConstant.BUTTON_CANCEL, (DialogInterface.OnClickListener) (dialogInterface, i) -> dialogInterface.dismiss()).setNegativeButton((CharSequence) "Delete", (DialogInterface.OnClickListener) (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mSignaturePad.clear();
                    mInvoice.setSignature("");
                    mInvoice.setSignedOnDate("");
                    mInvoiceDatabaseHelper.updateInvoice(mInvoice);
                    Singleton.getInstance().setInvoiceDataUpdated(true);
                    onBackPressed();
                }).create().show();
                return;
            case R.id.resignCv /*2131362951*/:
                undoCv.setVisibility(View.VISIBLE);
                clearCv.setVisibility(View.VISIBLE);
                saveCv.setVisibility(View.VISIBLE);
                resignCv.setVisibility(View.GONE);
                deleteCv.setVisibility(View.GONE);
                mSignaturePad.clear();
                return;
            case R.id.saveCv /*2131362975*/:
                if (!mSignaturePad.isEmpty()) {
                    mInvoice.setSignature(Utility.getFileToByte(mSignaturePad.getSignatureBitmap()));
                    Invoice invoice = mInvoice;
                    invoice.setSignedOnDate(System.currentTimeMillis() + "");
                    mInvoiceDatabaseHelper.updateInvoice(mInvoice);
                    Singleton.getInstance().setInvoiceDataUpdated(true);
                    onBackPressed();
                    return;
                }
                Utility.Toast(this, "First sign and then try again");
                return;
            case R.id.undoCv /*2131363195*/:
                if (!mInvoice.getSignature().isEmpty()) {
                    byte[] decode = Base64.decode(mInvoice.getSignature(), 0);
                    mSignaturePad.setSignatureBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));
                }
                deleteCv.setVisibility(View.VISIBLE);
                resignCv.setVisibility(View.VISIBLE);
                undoCv.setVisibility(View.GONE);
                clearCv.setVisibility(View.GONE);
                saveCv.setVisibility(View.GONE);
                return;
            default:
        }
    }
}
