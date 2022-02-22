package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet;

public class Product {
    String productDescription;
    int productId;
    String productName;
    int productTaxable;
    float productUnitCost;

    public Product() {
    }

    public Product(int i, int i2, String str, String str2, float f) {
        this.productId = i;
        this.productTaxable = i2;
        this.productName = str;
        this.productDescription = str2;
        this.productUnitCost = f;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int i) {
        this.productId = i;
    }

    public int getProductTaxable() {
        return this.productTaxable;
    }

    public void setProductTaxable(int i) {
        this.productTaxable = i;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String str) {
        this.productName = str;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public void setProductDescription(String str) {
        this.productDescription = str;
    }

    public float getProductUnitCost() {
        return this.productUnitCost;
    }

    public void setProductUnitCost(float f) {
        this.productUnitCost = f;
    }
}
