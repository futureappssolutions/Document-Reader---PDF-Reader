package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet;

import android.os.Parcel;
import android.os.Parcelable;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVTemplate.InvoiceHelper;

public class BusinessInfo implements Parcelable {
    public static final Parcelable.Creator<BusinessInfo> CREATOR = new Parcelable.Creator<BusinessInfo>() {
        public BusinessInfo createFromParcel(Parcel parcel) {
            return new BusinessInfo(parcel);
        }

        public BusinessInfo[] newArray(int i) {
            return new BusinessInfo[i];
        }
    };
    String Rate = "";
    String address1 = "";
    String address2 = "";
    String address3 = "";
    String bankTransfer = "";
    String businessNumber = "";
    String checkPayableTo = "";
    String currencyCode = "USD";
    String currencySymbol = "$";
    String customizeBusinessNumber = "";
    String customizeEstimateTitle = "";
    String customizeInvoiceTitle = "";
    String dateFormat = "yyy-MM-dd";
    String email = "";
    String estimatesNote = "";
    String estimatesNumber = "";
    String invoiceNumber = "";
    String invoicesNote = "";
    boolean isSetTax = false;
    String logoPath = "";
    String mobile = "";
    String name = "";
    String otherPayment = "";
    String ownerName = "";
    String paypalEmail = "";
    String phone = "";
    String taxLabel = "";
    String taxType = InvoiceHelper.TAX_TYPE_ON_THE_TOTAL;
    String termsAndCondition = "";
    String thankYouMessage = "";
    String website = "";

    @Override
    public int describeContents() {
        return 0;
    }

    public BusinessInfo() {
    }

    protected BusinessInfo(Parcel parcel) {
        this.logoPath = parcel.readString();
        this.name = parcel.readString();
        this.ownerName = parcel.readString();
        this.businessNumber = parcel.readString();
        this.address1 = parcel.readString();
        this.address2 = parcel.readString();
        this.address3 = parcel.readString();
        this.email = parcel.readString();
        this.phone = parcel.readString();
        this.mobile = parcel.readString();
        this.website = parcel.readString();
        this.paypalEmail = parcel.readString();
        this.checkPayableTo = parcel.readString();
        this.bankTransfer = parcel.readString();
        this.otherPayment = parcel.readString();
        this.taxType = parcel.readString();
        this.taxLabel = parcel.readString();
        this.Rate = parcel.readString();
        this.termsAndCondition = parcel.readString();
        this.thankYouMessage = parcel.readString();
        this.invoicesNote = parcel.readString();
        this.estimatesNote = parcel.readString();
        this.invoiceNumber = parcel.readString();
        this.estimatesNumber = parcel.readString();
        this.customizeInvoiceTitle = parcel.readString();
        this.customizeEstimateTitle = parcel.readString();
        this.customizeBusinessNumber = parcel.readString();
        this.currencyCode = parcel.readString();
        this.currencySymbol = parcel.readString();
        this.dateFormat = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.logoPath);
        parcel.writeString(this.name);
        parcel.writeString(this.ownerName);
        parcel.writeString(this.businessNumber);
        parcel.writeString(this.address1);
        parcel.writeString(this.address2);
        parcel.writeString(this.address3);
        parcel.writeString(this.email);
        parcel.writeString(this.phone);
        parcel.writeString(this.mobile);
        parcel.writeString(this.website);
        parcel.writeString(this.paypalEmail);
        parcel.writeString(this.checkPayableTo);
        parcel.writeString(this.bankTransfer);
        parcel.writeString(this.otherPayment);
        parcel.writeString(this.taxType);
        parcel.writeString(this.taxLabel);
        parcel.writeString(this.Rate);
        parcel.writeString(this.termsAndCondition);
        parcel.writeString(this.thankYouMessage);
        parcel.writeString(this.invoicesNote);
        parcel.writeString(this.estimatesNote);
        parcel.writeString(this.invoiceNumber);
        parcel.writeString(this.estimatesNumber);
        parcel.writeString(this.customizeInvoiceTitle);
        parcel.writeString(this.customizeEstimateTitle);
        parcel.writeString(this.customizeBusinessNumber);
        parcel.writeString(this.currencyCode);
        parcel.writeString(this.currencySymbol);
        parcel.writeString(this.dateFormat);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getLogoPath() {
        return this.logoPath;
    }

    public void setLogoPath(String str) {
        this.logoPath = str;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String str) {
        this.ownerName = str;
    }

    public String getBusinessNumber() {
        return this.businessNumber;
    }

    public void setBusinessNumber(String str) {
        this.businessNumber = str;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(String str) {
        this.address1 = str;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String str) {
        this.address2 = str;
    }

    public String getAddress3() {
        return this.address3;
    }

    public void setAddress3(String str) {
        this.address3 = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String str) {
        this.mobile = str;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String str) {
        this.website = str;
    }

    public String getPaypalEmail() {
        return this.paypalEmail;
    }

    public void setPaypalEmail(String str) {
        this.paypalEmail = str;
    }

    public String getCheckPayableTo() {
        return this.checkPayableTo;
    }

    public void setCheckPayableTo(String str) {
        this.checkPayableTo = str;
    }

    public String getBankTransfer() {
        return this.bankTransfer;
    }

    public void setBankTransfer(String str) {
        this.bankTransfer = str;
    }

    public String getOtherPayment() {
        return this.otherPayment;
    }

    public void setOtherPayment(String str) {
        this.otherPayment = str;
    }

    public String getTaxType() {
        return this.taxType;
    }

    public void setTaxType(String str) {
        this.taxType = str;
    }

    public String getTaxLabel() {
        return this.taxLabel;
    }

    public void setTaxLabel(String str) {
        this.taxLabel = str;
    }

    public String getRate() {
        return this.Rate;
    }

    public void setRate(String str) {
        this.Rate = str;
    }

    public String getInvoicesNote() {
        return this.invoicesNote;
    }

    public void setInvoicesNote(String str) {
        this.invoicesNote = str;
    }

    public String getEstimatesNote() {
        return this.estimatesNote;
    }

    public void setEstimatesNote(String str) {
        this.estimatesNote = str;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public void setInvoiceNumber(String str) {
        this.invoiceNumber = str;
    }

    public String getEstimatesNumber() {
        return this.estimatesNumber;
    }

    public void setEstimatesNumber(String str) {
        this.estimatesNumber = str;
    }

    public String getCustomizeInvoiceTitle() {
        return this.customizeInvoiceTitle;
    }

    public void setCustomizeInvoiceTitle(String str) {
        this.customizeInvoiceTitle = str;
    }

    public String getCustomizeEstimateTitle() {
        return this.customizeEstimateTitle;
    }

    public void setCustomizeEstimateTitle(String str) {
        this.customizeEstimateTitle = str;
    }

    public String getCustomizeBusinessNumber() {
        return this.customizeBusinessNumber;
    }

    public void setCustomizeBusinessNumber(String str) {
        this.customizeBusinessNumber = str;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String str) {
        this.currencyCode = str;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormat = str;
    }

    public boolean isSetTax() {
        return this.isSetTax;
    }

    public void setSetTax(boolean z) {
        this.isSetTax = z;
    }

    public String getCurrencySymbol() {
        return this.currencySymbol;
    }

    public void setCurrencySymbol(String str) {
        this.currencySymbol = str;
    }

    public String getTermsAndCondition() {
        return this.termsAndCondition;
    }

    public void setTermsAndCondition(String str) {
        this.termsAndCondition = str;
    }

    public String getThankYouMessage() {
        return this.thankYouMessage;
    }

    public void setThankYouMessage(String str) {
        this.thankYouMessage = str;
    }
}
