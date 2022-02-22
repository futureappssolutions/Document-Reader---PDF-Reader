package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet;

import java.io.Serializable;

public class InvoiceItem implements Serializable {
    public static String FLAT_AMOUNT = "flatAmount";
    public static String NO_DISCOUNT = "noDiscount";
    public static String PERCENTAGE = "percentage";
    String detail = "";
    float discount = 0.0f;
    float discountPercentage = 0.0f;
    String discountType = "";
    int invoiceId = 0;
    int invoiceItemId = 0;
    String invoiceName = "";
    float priceExclVAT = 0.0f;
    int productId = 0;
    int quantity = 0;
    int taxable = 0;
    float unitPrice = 0.0f;
    float vat = 0.0f;
    float vatRate = 0.0f;

    public InvoiceItem() {
    }

    public InvoiceItem(int i, int i2, int i3, int i4, float f, float f2, float f3, float f4, float f5, float f6, String str, String str2, String str3) {
        this.invoiceId = i;
        this.productId = i2;
        this.quantity = i3;
        this.taxable = i4;
        this.unitPrice = f;
        this.discount = f2;
        this.discountPercentage = f3;
        this.priceExclVAT = f4;
        this.vat = f5;
        this.vatRate = f6;
        this.invoiceName = str;
        this.discountType = str2;
        this.detail = str3;
    }

    public String getInvoiceName() {
        return this.invoiceName;
    }

    public void setInvoiceName(String str) {
        this.invoiceName = str;
    }

    public int getInvoiceItemId() {
        return this.invoiceItemId;
    }

    public void setInvoiceItemId(int i) {
        this.invoiceItemId = i;
    }

    public int getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(int i) {
        this.invoiceId = i;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int i) {
        this.productId = i;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }

    public int getTaxable() {
        return this.taxable;
    }

    public void setTaxable(int i) {
        this.taxable = i;
    }

    public float getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(float f) {
        this.unitPrice = f;
    }

    public float getDiscount() {
        return this.discount;
    }

    public void setDiscount(float f) {
        this.discount = f;
    }

    public float getPriceExclVAT() {
        return this.priceExclVAT;
    }

    public void setPriceExclVAT(float f) {
        this.priceExclVAT = f;
    }

    public float getVat() {
        return this.vat;
    }

    public void setVat(float f) {
        this.vat = f;
    }

    public float getVatRate() {
        return this.vatRate;
    }

    public void setVatRate(float f) {
        this.vatRate = f;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public void setDiscountType(String str) {
        this.discountType = str;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String str) {
        this.detail = str;
    }

    public float getDiscountPercentage() {
        return this.discountPercentage;
    }

    public void setDiscountPercentage(float f) {
        this.discountPercentage = f;
    }
}
