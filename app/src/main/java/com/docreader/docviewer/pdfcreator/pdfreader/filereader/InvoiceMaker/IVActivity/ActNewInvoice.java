package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVAdapter.IVItemsListAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Attachment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.InvoiceItem;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVTemplate.InvoiceHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.res.ResConstant;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ActNewInvoice extends BaseActivity implements View.OnClickListener {
    public ArrayList<InvoiceItem> invoiceItems = new ArrayList<>();
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public IVItemsListAdp IVItemsListAdapter;
    public RecyclerView invoiceItemRecyclerView;
    public RecyclerView attachmentRecyclerView;
    public BusinessInfo businessInfo;
    public Invoice mInvoice;
    public Singleton singleton;
    public LinearLayout llPreview;
    public EditText appreciationEt;
    public EditText noteEt;
    public EditText termsAndConditionEt;
    public TextView balanceDueTv;
    public TextView dateTv;
    public TextView discountTitleTv;
    public TextView discountTv;
    public TextView invoiceNumberTv;
    public TextView markPaidTv;
    public TextView paymentInstructionsTv;
    public TextView paymentsTv;
    public TextView signatureTv;
    public TextView subtotalTv;
    public TextView taxTitleTv;
    public TextView taxTv;
    public TextView taxTypeTv;
    public TextView toUserNameTv;
    public TextView totalTv;
    public double subtotalPrice = 0.0d;
    public double discount = 0.0d;
    public int invoiceId = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_invoice_new);
        setStatusBar();

        singleton = Singleton.getInstance();
        businessInfo = singleton.getBusinessInfo(this);
        init();
    }

    public void init() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView textView = findViewById(R.id.toolBarTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SharedPrefs mPrefs = new SharedPrefs(ActNewInvoice.this);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAds.showBannerAds(ActNewInvoice.this, ll_banner);



        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);

        invoiceNumberTv = findViewById(R.id.invoiceNumberTv);
        subtotalTv = findViewById(R.id.subtotalTv);
        toUserNameTv = findViewById(R.id.toUserNameTv);
        discountTitleTv = findViewById(R.id.discountTitleTv);
        discountTv = findViewById(R.id.discountTv);
        taxTitleTv = findViewById(R.id.taxTitleTv);
        taxTv = findViewById(R.id.taxTv);
        totalTv = findViewById(R.id.totalTv);
        paymentsTv = findViewById(R.id.paymentsTv);
        balanceDueTv = findViewById(R.id.balanceDueTv);
        paymentInstructionsTv = findViewById(R.id.paymentInstructionsTv);
        noteEt = findViewById(R.id.noteEt);
        appreciationEt = findViewById(R.id.appreciationEt);
        termsAndConditionEt = findViewById(R.id.termsAndConditionEt);
        signatureTv = findViewById(R.id.signatureTv);
        taxTypeTv = findViewById(R.id.taxTypeTv);
        markPaidTv = findViewById(R.id.markPaidTv);
        dateTv = findViewById(R.id.dateTv);
        llPreview = findViewById(R.id.llPreview);
        invoiceItemRecyclerView = findViewById(R.id.invoiceItemRecyclerView);
        attachmentRecyclerView = findViewById(R.id.attachmentRecyclerView);

        invoiceItemRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        invoiceItemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.addNewInvoiceItemLayout).setOnClickListener(this);

        if (getIntent() != null) {
            invoiceId = Integer.parseInt(getIntent().getStringExtra(ScreenCVEdit.FIELD_ID));
            textView.setText(Html.fromHtml("Invoice"));
            if (invoiceId == 0) {
                newInvoice();
            } else {
                fillInvoiceDetails();
            }
        }

        fillInvoiceItemRecyclerView();

        llPreview.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActInvoicePreview.class);
            intent.putExtra("invoiceId", invoiceId + "");
            startActivity(intent);
        });
    }

    private void fillInvoiceDetails() {
        mInvoice = invoiceDatabaseHelper.getSelectedInvoices(invoiceId);
        if (mInvoice != null) {
            if (!mInvoice.getClientInfo().getClientName().isEmpty()) {
                toUserNameTv.setText(mInvoice.getClientInfo().getClientName());
            } else {
                toUserNameTv.setText("");
            }

            ArrayList<String> arrayList = new ArrayList<>();
            if (!mInvoice.getBusinessInfo().getPaypalEmail().isEmpty()) {
                arrayList.add("PayPal");
            }
            if (!mInvoice.getBusinessInfo().getCheckPayableTo().isEmpty()) {
                arrayList.add("Check");
            }
            if (!mInvoice.getBusinessInfo().getBankTransfer().isEmpty()) {
                arrayList.add("Bank Transfer");
            }
            if (!mInvoice.getBusinessInfo().getOtherPayment().isEmpty()) {
                arrayList.add("Other Method");
            }
            if (arrayList.size() != 0) {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (i == 0) {
                        str = new StringBuilder("Payment : " + arrayList.get(i));
                    } else {
                        str.append(", ").append(arrayList.get(i));
                    }
                }
                paymentInstructionsTv.setText(str.toString());
            } else {
                paymentInstructionsTv.setText("");
            }

            if (mInvoice.getAttachments() != null) {
                attachmentRecyclerView.setAdapter(new AttachmentAdapter(mInvoice.getAttachments()));
            }

            noteEt.setText(mInvoice.getBusinessInfo().getInvoicesNote());
            appreciationEt.setText(mInvoice.getBusinessInfo().getThankYouMessage());
            termsAndConditionEt.setText(mInvoice.getBusinessInfo().getTermsAndCondition());

            noteEt.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    mInvoice.getBusinessInfo().setInvoicesNote(charSequence.toString());
                    invoiceDatabaseHelper.updateInvoice(mInvoice);
                }
            });

            appreciationEt.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    mInvoice.getBusinessInfo().setThankYouMessage(charSequence.toString());
                    invoiceDatabaseHelper.updateInvoice(mInvoice);
                }
            });

            termsAndConditionEt.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    mInvoice.getBusinessInfo().setTermsAndCondition(charSequence.toString());
                    invoiceDatabaseHelper.updateInvoice(mInvoice);
                }
            });

            if (mInvoice.getSignature().isEmpty() || mInvoice.getSignedOnDate().length() <= 0) {
                signatureTv.setText("");
                return;
            }

            String format = new SimpleDateFormat(mInvoice.getBusinessInfo().getDateFormat(), Locale.US).format(new Date(Long.parseLong(mInvoice.getSignedOnDate())));
            signatureTv.setText(Html.fromHtml("Signed On : " + format));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    public void fillInvoiceItemRecyclerView() {
        invoiceItems.clear();
        invoiceItems = invoiceDatabaseHelper.getAllOrderInvoicesItems(invoiceId + "");
        subtotalPrice = 0.0d;
        double d = 0.0d;
        double d2 = 0.0d;
        for (int i = 0; i < invoiceItems.size(); i++) {
            float quantity = (((float) invoiceItems.get(i).getQuantity()) * invoiceItems.get(i).getUnitPrice()) - invoiceItems.get(i).getDiscount();
            Double.isNaN(quantity);
            subtotalPrice = subtotalPrice + (double) quantity;
            if (mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM) && invoiceItems.get(i).getTaxable() == 1) {
                double vat = invoiceItems.get(i).getVat();
                Double.isNaN(vat);
                d += vat;
                double vatRate = invoiceItems.get(i).getVatRate();
                Double.isNaN(vatRate);
                d2 += vatRate;
            }
        }

        invoiceNumberTv.setText(mInvoice.getInvoiceNumber());
        subtotalTv.setText(businessInfo.getCurrencySymbol() + " " + Utility.fmt(subtotalPrice));
        discountTitleTv.setText("Discount");

        if ((mInvoice.getDiscountPercentage() == 0.0f && mInvoice.getDiscount() == 0.0f) || mInvoice.getDiscountType().equals(InvoiceItem.NO_DISCOUNT)) {
            discountTv.setText("");
            discount = 0.0d;
        } else if (mInvoice.getDiscountType().equals(InvoiceItem.PERCENTAGE)) {
            discountTitleTv.setText("Discount (" + new DecimalFormat("##.##").format(mInvoice.getDiscountPercentage()) + "%)");
            double discountPercentage = mInvoice.getDiscountPercentage() / 100.0f;
            Double.isNaN(discountPercentage);
            discount = subtotalPrice * discountPercentage;
            discountTv.setText("-" + businessInfo.getCurrencySymbol() + new DecimalFormat("##.##").format(discount));
        } else {
            discount = mInvoice.getDiscount();
            discountTv.setText("-" + businessInfo.getCurrencySymbol() + discount);
            discount = mInvoice.getDiscount();
        }


        double d6 = subtotalPrice - discount;
        String sb = "total... " + d6;
        Utility.logCatMsg(sb);
        taxTitleTv.setText(mInvoice.getTaxLabel());
        taxTv.setText("");

        if (mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_THE_TOTAL) || mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_DEDUCTED)) {
            if (mInvoice.getTaxPercentage() != 0.0f) {
                double taxPercentage = mInvoice.getTaxPercentage();
                taxTitleTv.setText(mInvoice.getTaxLabel() + " (" + taxPercentage + "%)");
                Double.isNaN(taxPercentage);
                double d8 = subtotalPrice * (taxPercentage / 100.0d);
                taxTv.setText(businessInfo.getCurrencySymbol() + new DecimalFormat("##.##").format(d8));
                if (mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_DEDUCTED)) {
                    taxTv.setText("-" + taxTv.getText());
                    d6 -= d8;
                } else {
                    Utility.logCatMsg("total... " + d6 + " taxPercentageValue " + d8);
                    d6 += d8;
                }
            }
        } else if (mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM)) {
            taxTitleTv.setText(mInvoice.getTaxLabel() + " (" + d + "%)");
            TextView textView = taxTv;
            String sb2 = businessInfo.getCurrencySymbol() + new DecimalFormat("##.##").format(d2);
            textView.setText(sb2);
            d6 += d2;
        }

        totalTv.setText(businessInfo.getCurrencySymbol() + new DecimalFormat("##.##").format(d6));
        mInvoice.setTotalBill((float) d6);
        paymentsTv.setText("-" + businessInfo.getCurrencySymbol() + new DecimalFormat("##.##").format(mInvoice.getPayment()));
        float payment = mInvoice.getPayment();
        Utility.logCatMsg("total " + d6 + "  mInvoice.getPayment() " + payment);
        Double.isNaN(payment);
        double d10 = d6 - (double) payment;
        Utility.logCatMsg("balanceDue " + d10);
        balanceDueTv.setText(businessInfo.getCurrencySymbol() + new DecimalFormat("##.##").format(d10));
        mInvoice.setBalanceDue((float) d10);
        invoiceDatabaseHelper.updateInvoice(mInvoice);
        discountTv.setHint(businessInfo.getCurrencySymbol() + "0.00");
        taxTv.setHint(businessInfo.getCurrencySymbol() + "0.00");
        totalTv.setHint(businessInfo.getCurrencySymbol() + "0.00");
        paymentsTv.setHint(businessInfo.getCurrencySymbol() + "0.00");
        balanceDueTv.setHint(businessInfo.getCurrencySymbol() + "0.00");
        taxTypeTv.setText("Tax Type : " + mInvoice.getTaxType());
        dateTv.setText(Utility.geDateTime(Long.parseLong(mInvoice.getCreationDate()), businessInfo.getDateFormat()));

        if (mInvoice.getInvoiceStatus().equals(Invoice.INVOICE_STATUS_ISSUED)) {
            balanceDueTv.setText(businessInfo.getCurrencySymbol() + new DecimalFormat("##.##").format(0));
            paymentsTv.setText("-" + businessInfo.getCurrencySymbol() + new DecimalFormat("##.##").format(d6));
            markPaidTv.setText("Mark Unpaid");
        } else {
            markPaidTv.setText("Mark Paid");
        }

        IVItemsListAdapter = new IVItemsListAdp(this, invoiceItems, invoiceId);
        invoiceItemRecyclerView.setAdapter(IVItemsListAdapter);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void newInvoice() {
        invoiceNumberTv.setText("INV" + String.format("%04d", invoiceDatabaseHelper.getLastInsertedInvoiceId()));
        invoiceId = invoiceDatabaseHelper.createNewInvoice(singleton.getBusinessInfo(this));
        fillInvoiceDetails();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Singleton.getInstance().isInvoiceDataUpdated()) {
            if (invoiceId != 0) {
                fillInvoiceDetails();
            }
            fillInvoiceItemRecyclerView();
            Singleton.getInstance().setInvoiceDataUpdated(false);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewInvoiceItemLayout:
                Intent intent = new Intent(this, ActInvoiceItem.class);
                intent.putExtra("invoiceItemId", "0");
                intent.putExtra("invoiceId", invoiceId + "");
                startActivity(intent);
                return;
            case R.id.addPhotoLayout:
                Intent intent2 = new Intent(this, ActInvoiceAttachment.class);
                intent2.putExtra("invoiceId", invoiceId + "");
                intent2.putExtra("attachmentId", "0");
                startActivity(intent2);
                return;
            case R.id.businessInfoTv:
                Intent intent3 = new Intent(this, ActInvoiceBusinessDetail.class);
                intent3.putExtra("fromSetting", false);
                intent3.putExtra("invoiceId", invoiceId + "");
                startActivity(intent3);
                return;
            case R.id.chooseUserCv:
                if (mInvoice.getClientInfo().getClientName().isEmpty()) {
                    Intent intent4 = new Intent(this, ActivitySelectClient.class);
                    intent4.putExtra("invoiceId", invoiceId + "");
                    startActivity(intent4);
                    return;
                }
                Intent intent5 = new Intent(this, ActInvoiceNewClient.class);
                intent5.putExtra("invoiceId", invoiceId + "");
                startActivity(intent5);
                return;
            case R.id.dateTv:
            case R.id.setInvoiceLayout:
                Intent intent6 = new Intent(this, ActInvoiceNumber.class);
                intent6.putExtra("invoiceId", invoiceId + "");
                startActivity(intent6);
                return;
            case R.id.discountLayout:
                Intent intent7 = new Intent(this, ActInvoiceDiscount.class);
                intent7.putExtra("invoiceId", invoiceId + "");
                startActivity(intent7);
                return;
            case R.id.markPaidTv:
                if (mInvoice.getInvoiceStatus().equals(Invoice.INVOICE_STATUS_ISSUED)) {
                    mInvoice.setInvoiceStatus(Invoice.INVOICE_STATUS_DRAFT);
                } else {
                    mInvoice.setInvoiceStatus(Invoice.INVOICE_STATUS_ISSUED);
                }
                invoiceDatabaseHelper.updateInvoice(mInvoice);
                fillInvoiceItemRecyclerView();
                return;
            case R.id.paymentInstructionsTv:
                Intent intent8 = new Intent(this, ActPaymentInstructions.class);
                intent8.putExtra("invoiceId", invoiceId + "");
                startActivity(intent8);
                return;
            case R.id.paymentsLayout:
                addPaymentDialog();
                return;
            case R.id.signatureTv:
                Intent intent10 = new Intent(this, ActInvoiceSignature.class);
                intent10.putExtra("invoiceId", invoiceId + "");
                startActivity(intent10);
                return;
            case R.id.taxLayout:
                Intent intent11 = new Intent(this, ActInvoiceTax.class);
                intent11.putExtra("invoiceId", invoiceId + "");
                startActivity(intent11);
                return;
            default:
        }
    }

    private class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.MyViewHolder> {
        public List<Attachment> attachmentList;

        public AttachmentAdapter(List<Attachment> list) {
            attachmentList = list;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView attachmentName;
            public CardView dataLayout;

            public MyViewHolder(View view) {
                super(view);
                attachmentName = view.findViewById(R.id.clientNameTv);
                dataLayout = view.findViewById(R.id.dataLayout);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_choose_user, viewGroup, false));
        }

        @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
            myViewHolder.attachmentName.setText("Photo # " + (i + 1));
            myViewHolder.attachmentName.setAllCaps(false);
            myViewHolder.attachmentName.setTypeface(null, Typeface.NORMAL);

            myViewHolder.dataLayout.setOnClickListener(view -> {
                Utility.logCatMsg("attachment Id " + attachmentList.get(i).getAttachmentId());
                Intent intent = new Intent(ActNewInvoice.this, ActInvoiceAttachment.class);
                intent.putExtra("invoiceId", invoiceId + "");
                intent.putExtra("attachmentId", attachmentList.get(i).getAttachmentId() + "");
                startActivity(intent);
            });

            myViewHolder.dataLayout.setOnLongClickListener(view -> {
                new AlertDialog.Builder(ActNewInvoice.this).setTitle("Delete.!").setMessage("Are you sure to delete attachment.?").setPositiveButton("Delete", (dialogInterface, i1) -> {
                    invoiceDatabaseHelper.removeAttachment(attachmentList.get(i1).getAttachmentId() + "");
                    attachmentList.remove(attachmentList.get(i1));
                    notifyDataSetChanged();
                    dialogInterface.dismiss();
                }).setNegativeButton(ResConstant.BUTTON_CANCEL, (dialogInterface, i1) -> dialogInterface.dismiss()).create().show();
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return attachmentList.size();
        }
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    private void addPaymentDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_set_payment, null);
        bottomSheetDialog.setContentView(inflate);

        final EditText editText = inflate.findViewById(R.id.paymentEt);
        Button button = inflate.findViewById(R.id.addPaymentBtn);
        TextView textView = inflate.findViewById(R.id.paymentDate);
        ((TextView) inflate.findViewById(R.id.totalPayment)).setText(totalTv.getText().toString());
        ((TextView) inflate.findViewById(R.id.dueBalanceTv)).setText(balanceDueTv.getText().toString());
        ((TextView) inflate.findViewById(R.id.paidBalanceTv)).setText(mInvoice.getPayment() + " " + businessInfo.getCurrencySymbol());

        if (!mInvoice.getPaymentDate().isEmpty()) {
            textView.setText(Utility.geDateTime(Long.parseLong(mInvoice.getPaymentDate()), businessInfo.getDateFormat()));
        } else {
            textView.setText("");
        }

        if (mInvoice.getPayment() == 0.0f) {
            editText.setText("0");
        } else {
            editText.setText(mInvoice.getPayment() + "");
        }

        button.setOnClickListener(view -> {
            if (editText.getText().toString().isEmpty()) {
                Utility.Toast(ActNewInvoice.this, "Please enter a valid value");
            } else {
                mInvoice.setPaymentDate(System.currentTimeMillis() + "");
                mInvoice.setPayment(Float.parseFloat(editText.getText().toString()));
                Utility.logCatMsg("payment set " + mInvoice.getPayment());
                invoiceDatabaseHelper.updateInvoice(mInvoice);
                fillInvoiceItemRecyclerView();
            }
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.cancelBtn).setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
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
