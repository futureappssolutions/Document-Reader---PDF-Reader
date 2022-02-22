package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet;

public class Client {
    String address1 = "";
    String address2 = "";
    String address3 = "";
    String clientEmail = "";
    String clientFax = "";
    int clientId = 0;
    String clientMobile = "";
    String clientName = "";
    String clientPhone = "";
    float totalBill = 0.0f;
    int totalInvoices = 0;

    public Client(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        this.clientId = i;
        this.clientName = str;
        this.clientEmail = str2;
        this.clientPhone = str3;
        this.clientMobile = str4;
        this.clientFax = str5;
        this.address1 = str6;
        this.address2 = str7;
        this.address3 = str8;
    }

    public Client() {
    }

    public int getClientId() {
        return this.clientId;
    }

    public void setClientId(int i) {
        this.clientId = i;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String str) {
        this.clientName = str;
    }

    public String getClientEmail() {
        return this.clientEmail;
    }

    public void setClientEmail(String str) {
        this.clientEmail = str;
    }

    public String getClientPhone() {
        return this.clientPhone;
    }

    public void setClientPhone(String str) {
        this.clientPhone = str;
    }

    public String getClientMobile() {
        return this.clientMobile;
    }

    public void setClientMobile(String str) {
        this.clientMobile = str;
    }

    public String getClientFax() {
        return this.clientFax;
    }

    public void setClientFax(String str) {
        this.clientFax = str;
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

    public int getTotalInvoices() {
        return this.totalInvoices;
    }

    public void setTotalInvoices(int i) {
        this.totalInvoices = i;
    }

    public float getTotalBill() {
        return this.totalBill;
    }

    public void setTotalBill(float f) {
        this.totalBill = f;
    }
}
