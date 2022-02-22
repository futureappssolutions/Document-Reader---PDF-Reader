package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet;

public class Attachment {
    String additionalDetails = "";
    int attachmentId;
    String createdOn = "";
    String description = "";
    String imagePath = "";
    int invoiceId;

    public int getAttachmentId() {
        return this.attachmentId;
    }

    public void setAttachmentId(int i) {
        this.attachmentId = i;
    }

    public int getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(int i) {
        this.invoiceId = i;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String str) {
        this.imagePath = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getAdditionalDetails() {
        return this.additionalDetails;
    }

    public void setAdditionalDetails(String str) {
        this.additionalDetails = str;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String str) {
        this.createdOn = str;
    }
}
