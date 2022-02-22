package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet;

import java.util.ArrayList;

public class Invoice {
    public static String INVOICE_STATUS_CREDITED = "Credited";
    public static String INVOICE_STATUS_DELETE = "Delete";
    public static String INVOICE_STATUS_DRAFT = "Draft";
    public static String INVOICE_STATUS_ISSUED = "Issued";
    ArrayList<Attachment> attachments;
    float balanceDue;
    BusinessInfo businessInfo;
    Client clientInfo;
    String creationDate;
    float discount;
    float discountPercentage;
    String discountType;
    String dueDate;
    int invoiceId;
    ArrayList<InvoiceItem> invoiceItemArrayList;
    String invoiceNumber;
    String invoiceStatus;
    int invoiceStatusId;
    String issueDate;
    String note;
    float payment;
    String paymentDate;
    String poNumber;
    String signature;
    String signedOnDate;
    float subTotal;
    float tax;
    String taxLabel;
    float taxPercentage;
    String taxType;
    String terms;
    float totalBill;

    public int getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(int i) {
        this.invoiceId = i;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public void setInvoiceNumber(String str) {
        this.invoiceNumber = str;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(String str) {
        this.creationDate = str;
    }

    public String getIssueDate() {
        return this.issueDate;
    }

    public void setIssueDate(String str) {
        this.issueDate = str;
    }

    public String getTerms() {
        return this.terms;
    }

    public void setTerms(String str) {
        this.terms = str;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(String str) {
        this.dueDate = str;
    }

    public String getPoNumber() {
        return this.poNumber;
    }

    public void setPoNumber(String str) {
        this.poNumber = str;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String str) {
        this.note = str;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String str) {
        this.signature = str;
    }

    public String getSignedOnDate() {
        return this.signedOnDate;
    }

    public void setSignedOnDate(String str) {
        this.signedOnDate = str;
    }

    public BusinessInfo getBusinessInfo() {
        return this.businessInfo;
    }

    public void setBusinessInfo(BusinessInfo businessInfo2) {
        this.businessInfo = businessInfo2;
    }

    public Client getClientInfo() {
        return this.clientInfo;
    }

    public void setClientInfo(Client client) {
        this.clientInfo = client;
    }

    public ArrayList<InvoiceItem> getInvoiceItemArrayList() {
        return this.invoiceItemArrayList;
    }

    public void setInvoiceItemArrayList(ArrayList<InvoiceItem> arrayList) {
        this.invoiceItemArrayList = arrayList;
    }

    public int getInvoiceStatusId() {
        return this.invoiceStatusId;
    }

    public void setInvoiceStatusId(int i) {
        this.invoiceStatusId = i;
    }

    public String getInvoiceStatus() {
        return this.invoiceStatus;
    }

    public void setInvoiceStatus(String str) {
        this.invoiceStatus = str;
    }

    public ArrayList<Attachment> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(ArrayList<Attachment> arrayList) {
        this.attachments = arrayList;
    }

    public float getSubTotal() {
        return this.subTotal;
    }

    public void setSubTotal(float f) {
        this.subTotal = f;
    }

    public float getTax() {
        return this.tax;
    }

    public void setTax(float f) {
        this.tax = f;
    }

    public float getTotalBill() {
        return this.totalBill;
    }

    public void setTotalBill(float f) {
        this.totalBill = f;
    }

    public float getBalanceDue() {
        return this.balanceDue;
    }

    public void setBalanceDue(float f) {
        this.balanceDue = f;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public void setDiscountType(String str) {
        this.discountType = str;
    }

    public String getTaxType() {
        return this.taxType;
    }

    public void setTaxType(String str) {
        this.taxType = str;
    }

    public float getDiscount() {
        return this.discount;
    }

    public void setDiscount(float f) {
        this.discount = f;
    }

    public float getDiscountPercentage() {
        return this.discountPercentage;
    }

    public void setDiscountPercentage(float f) {
        this.discountPercentage = f;
    }

    public float getTaxPercentage() {
        return this.taxPercentage;
    }

    public void setTaxPercentage(float f) {
        this.taxPercentage = f;
    }

    public float getPayment() {
        return this.payment;
    }

    public void setPayment(float f) {
        this.payment = f;
    }

    public String getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(String str) {
        this.paymentDate = str;
    }

    public String getTaxLabel() {
        return this.taxLabel;
    }

    public void setTaxLabel(String str) {
        this.taxLabel = str;
    }
}
