package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActInvoiceBusinessDetail;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActInvoiceCurrency;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActInvoiceDateFormat;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActPaymentInstructions;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.ModelDateFormat;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVTemplate.InvoiceHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

public class FrgInvoiceSettings extends Fragment implements View.OnClickListener {
    public EditText appreciationEt;
    public EditText termsAndConditionEt;
    public TextView currencyCodeTv;
    public TextView currencyPreviewTv;
    public TextView dateFormatPreviewTv;
    public TextView dateFormatTv;
    public TextView textTypeTv;
    public LinearLayout textTypeLayout;
    public LinearLayout chooseTextTypeLayout;
    public SwitchCompat taxableSc;
    public BusinessInfo businessInfo;
    public Singleton singleton;

    public static FrgInvoiceSettings getInstance() {
        return new FrgInvoiceSettings();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = Singleton.getInstance();
        businessInfo = singleton.getBusinessInfo(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_settings, container, false);

        chooseTextTypeLayout = (LinearLayout) view.findViewById(R.id.chooseTextTypeLayout);
        textTypeLayout = (LinearLayout) view.findViewById(R.id.textTypeLayout);
        textTypeTv = (TextView) view.findViewById(R.id.textTypeTv);
        currencyPreviewTv = (TextView) view.findViewById(R.id.currencyPreviewTv);
        currencyCodeTv = (TextView) view.findViewById(R.id.currencyCodeTv);
        dateFormatPreviewTv = (TextView) view.findViewById(R.id.dateFormatPreviewTv);
        dateFormatTv = (TextView) view.findViewById(R.id.dateFormatTv);
        appreciationEt = (EditText) view.findViewById(R.id.appreciationEt);
        termsAndConditionEt = (EditText) view.findViewById(R.id.termsAndConditionEt);
        taxableSc = (SwitchCompat) view.findViewById(R.id.taxableSc);

        view.findViewById(R.id.businessDetailLayout).setOnClickListener(this);
        view.findViewById(R.id.paymentInstructionsLayout).setOnClickListener(this);
        view.findViewById(R.id.taxLayout).setOnClickListener(this);
        view.findViewById(R.id.chooseCurrencyLayout).setOnClickListener(this);
        view.findViewById(R.id.chooseDateFormatLayout).setOnClickListener(this);

        chooseTextTypeLayout.setOnClickListener(this);
        taxableSc.setChecked(businessInfo.isSetTax());
        textTypeTv.setText(businessInfo.getTaxType());
        if (!businessInfo.isSetTax()) {
            textTypeLayout.setVisibility(View.GONE);
        }

        currencyPreviewTv.setText(businessInfo.getCurrencySymbol() + "12,385.48");
        currencyCodeTv.setText(businessInfo.getCurrencyCode());
        appreciationEt.setText(businessInfo.getThankYouMessage());
        termsAndConditionEt.setText(businessInfo.getTermsAndCondition());
        dateFormatPreviewTv.setText(new ModelDateFormat().getCurrentDate(businessInfo.getDateFormat()));
        dateFormatTv.setText(businessInfo.getDateFormat());

        appreciationEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                businessInfo.setThankYouMessage(charSequence.toString());
            }
        });

        termsAndConditionEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                businessInfo.setTermsAndCondition(charSequence.toString());
            }
        });

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.businessDetailLayout /*2131362036*/:
                Intent intent = new Intent(getActivity(), ActInvoiceBusinessDetail.class);
                intent.putExtra("fromSetting", true);
                startActivity(intent);
                return;
            case R.id.chooseCurrencyLayout /*2131362094*/:
                startActivity(new Intent(getActivity(), ActInvoiceCurrency.class));
                return;
            case R.id.chooseDateFormatLayout /*2131362095*/:
                startActivity(new Intent(getActivity(), ActInvoiceDateFormat.class));
                return;
            case R.id.chooseTextTypeLayout /*2131362101*/:
                invoiceTaxDropDown();
                return;
            case R.id.paymentInstructionsLayout /*2131362871*/:
                startActivity(new Intent(getActivity(), ActPaymentInstructions.class));
                return;
            case R.id.taxLayout /*2131363108*/:
                businessInfo.setSetTax(!businessInfo.isSetTax());
                taxableSc.setChecked(businessInfo.isSetTax());
                singleton.setAndSaveBusinessInfo(getActivity(), businessInfo);
                if (businessInfo.isSetTax()) {
                    textTypeLayout.setVisibility(View.VISIBLE);
                } else {
                    textTypeLayout.setVisibility(View.GONE);
                }
                return;
            default:
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if (singleton.isInvoiceDataUpdated()) {
            singleton.setInvoiceDataUpdated(false);
            currencyPreviewTv.setText(businessInfo.getCurrencySymbol() + "12,385.48");
            currencyCodeTv.setText(businessInfo.getCurrencyCode());
            dateFormatPreviewTv.setText(new ModelDateFormat().getCurrentDate(businessInfo.getDateFormat()));
            dateFormatTv.setText(businessInfo.getDateFormat());
        }
    }

    public void invoiceTaxDropDown() {
        PopupMenu popupMenu = new PopupMenu(getContext(), chooseTextTypeLayout);
        popupMenu.getMenu().add(InvoiceHelper.TAX_TYPE_NONE);
        popupMenu.getMenu().add(InvoiceHelper.TAX_TYPE_ON_THE_TOTAL);
        popupMenu.getMenu().add(InvoiceHelper.TAX_TYPE_DEDUCTED);
        popupMenu.getMenu().add(InvoiceHelper.TAX_TYPE_ON_PER_ITEM);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle().equals(InvoiceHelper.TAX_TYPE_ON_THE_TOTAL)) {
                businessInfo.setTaxType(InvoiceHelper.TAX_TYPE_ON_THE_TOTAL);
            } else if (menuItem.getTitle().equals(InvoiceHelper.TAX_TYPE_DEDUCTED)) {
                businessInfo.setTaxType(InvoiceHelper.TAX_TYPE_DEDUCTED);
            } else if (menuItem.getTitle().equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM)) {
                businessInfo.setTaxType(InvoiceHelper.TAX_TYPE_ON_PER_ITEM);
            }
            textTypeTv.setText(businessInfo.getTaxType());
            return true;
        });
        popupMenu.show();
    }
}