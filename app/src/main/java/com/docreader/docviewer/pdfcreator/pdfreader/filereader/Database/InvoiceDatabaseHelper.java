package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Attachment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Client;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.InvoiceItem;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Product;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVTemplate.InvoiceHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.DocumentFiles;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.NotepadItemModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.ArrayList;

public class InvoiceDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "InvoiceMasterDB";
    Context context;

    public InvoiceDatabaseHelper(Context context2) {
        super(context2, DATABASE_NAME, null, 1);
        this.context = context2;
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE tblInvoiceItem(invoiceItem_id INTEGER PRIMARY KEY,invoiceItem_invoice_id INTEGER,invoiceItem_productId INTEGER,invoiceItem_name TEXT,invoiceItem_quantity INTEGER,invoiceItem_taxable INTEGER,invoiceItem_unitPrice TEXT,invoiceItem_vat TEXT,invoiceItem_discount TEXT,invoiceItem_discount_percenate TEXT,invoiceItem_priceExclVAT TEXT,invoiceItem_vatRate TEXT,invoiceItem_discountType TEXT,invoiceItem_detail TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE tblInvoice(invoiceId INTEGER PRIMARY KEY,invoiceClientId INTEGER,invoiceNumber TEXT,invoiceCreationDate TEXT,invoiceIssueDate TEXT,invoiceTerms TEXT,invoiceDueDate TEXT,invoicePoNumber TEXT,invoiceNote TEXT,invoiceBalanceDue TEXT,invoiceDiscount TEXT,invoiceDiscountType TEXT,invoiceDiscountPercentage TEXT,invoiceTax TEXT,invoiceTaxLabel TEXT,invoiceTaxType TEXT,invoiceTaxPercentage TEXT,invoiceTotalBill TEXT,invoicePayment TEXT,invoicePaymentDate TEXT,invoiceSignature TEXT,invoiceSignedOnDate TEXT,invoiceB_logoPath TEXT,invoiceB_name TEXT,invoiceB_ownerName TEXT,invoiceB_businessNumber TEXT,invoiceB_address1 TEXT,invoiceB_address2 TEXT,invoiceB_address3 TEXT,invoiceB_email TEXT,invoiceB_phone TEXT,invoiceB_mobile TEXT,invoiceB_website TEXT,invoiceB_paypalEmail TEXT,invoiceB_checkPayableTo TEXT,invoiceB_bankTransfer TEXT,invoiceB_otherPayment TEXT,invoiceB_taxType TEXT,invoiceB_taxLabel TEXT,invoiceB_Rate TEXT,invoiceB_invoicesTermsAndCondition TEXT,invoiceB_invoicesThankYouMessage TEXT,invoiceB_invoicesNote TEXT,invoiceB_estimatesNote TEXT,invoiceB_invoiceNumber TEXT,invoiceB_estimatesNumber TEXT,invoiceB_customizeInvoiceTitle TEXT,invoiceB_customizeEstimateTitle TEXT,invoiceB_customizeBusinessNumber TEXT,invoiceB_currency TEXT, TEXT,invoiceC_name TEXT,invoiceC_email TEXT,invoiceC_phone TEXT,invoiceC_mobile TEXT,invoiceC_fax TEXT,invoiceC_address1 TEXT,invoiceC_address2 TEXT,invoiceC_address3 TEXT,invoice_status TEXT,invoice_status_id INTEGER)");
        sQLiteDatabase.execSQL("CREATE TABLE tbleClient (clientId INTEGER PRIMARY KEY AUTOINCREMENT, clientName TEXT , clientEmail TEXT , clientMobile TEXT , clientFax TEXT , clientPhone TEXT , clientAddress1 TEXT , clientAddress2 TEXT , clientAddress3 TEXT , clientCreatedOn TEXT);");
        sQLiteDatabase.execSQL("CREATE TABLE tblProduct (productId INTEGER PRIMARY KEY AUTOINCREMENT, productTaxable TEXT , productName TEXT , productDescription TEXT , productCost TEXT , productCreatedOn TEXT);");
        sQLiteDatabase.execSQL("CREATE TABLE tblAttachment (attachmentId INTEGER PRIMARY KEY AUTOINCREMENT, attachmentInoviceId INTEGER , attachmentImagePath TEXT , attachmentDescription TEXT , attachmentAdditionalDetail TEXT , attachmentCreatedOn TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tblInvoiceItem");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tblInvoice");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbleClient");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tblProduct");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tblAttachment");
        onCreate(sQLiteDatabase);
    }

    public void dropAllTable() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL("DROP TABLE IF EXISTS tblInvoiceItem");
        writableDatabase.execSQL("DROP TABLE IF EXISTS tblInvoice");
        writableDatabase.execSQL("DROP TABLE IF EXISTS tbleClient");
        writableDatabase.execSQL("DROP TABLE IF EXISTS tblProduct");
        writableDatabase.execSQL("DROP TABLE IF EXISTS tblAttachment");
        onCreate(writableDatabase);
    }

    public void clearAllData() {
        deleteAllDataFromTblMsg();
    }

    private void deleteAllDataFromTblMsg() {
        getWritableDatabase().execSQL("delete from tblInvoiceItem");
    }

    public void clearChat(String str) {
        getWritableDatabase().delete("tblInvoiceItem", "invoiceItem_quantity = ?", new String[]{String.valueOf(str)});
    }

    @SuppressLint("Range")
    public ArrayList<DocumentFiles> getAllDocumentFiles() {
        ArrayList<DocumentFiles> arrayList = new ArrayList<>();
        try {
            Utility.logCatMsg("DataBase " + "SELECT  * FROM tblInvoice");
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tblInvoice", null);
            if (rawQuery.moveToFirst()) {
                do {
                    DocumentFiles documentFiles = new DocumentFiles();
                    documentFiles.setId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceId")));
                    documentFiles.setFileName(rawQuery.getString(rawQuery.getColumnIndex("invoiceClientId")));
                    documentFiles.setFileContent(rawQuery.getString(rawQuery.getColumnIndex("invoiceNumber")));
                    documentFiles.setCreatedAt(rawQuery.getString(rawQuery.getColumnIndex("invoiceCreationDate")));
                    documentFiles.setUpdatedAt(rawQuery.getString(rawQuery.getColumnIndex("invoiceIssueDate")));
                    documentFiles.setFileType(rawQuery.getInt(rawQuery.getColumnIndex("invoiceIssueDate")));
                    arrayList.add(documentFiles);
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return arrayList;
    }

    public int getUnSeenMsgCount(String str) {
        Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tblInvoiceItem WHERE invoiceItem_unitPrice = " + str + " And " + "invoiceItem_discount" + " = 0 ", null);
        int count = rawQuery.getCount();
        rawQuery.close();
        return count;
    }

    public void updateMsgSeen(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceItem_discount", "1");
        writableDatabase.update("tblInvoiceItem", contentValues, "invoiceItem_unitPrice = ?", new String[]{str});
    }

    public void updateFileContent(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceIssueDate", Utility.getCurrentDateTime());
        contentValues.put("invoiceNumber", str2);
        writableDatabase.update("tblInvoice", contentValues, "invoiceId = ?", new String[]{str});
    }

    public void renameFileName(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceClientId", str2);
        writableDatabase.update("tblInvoice", contentValues, "invoiceId = ?", new String[]{str});
    }

    public void deleteFile(String str) {
        getWritableDatabase().delete("tblInvoice", "invoiceId = ?", new String[]{String.valueOf(str)});
    }

    public ArrayList<NotepadItemModel> getAllNotes() {
        ArrayList<NotepadItemModel> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        @SuppressLint("Recycle") Cursor query = writableDatabase.query("tbleClient", new String[]{"clientId", "clientName", "clientEmail", "clientPhone", "clientMobile", "clientFax"}, null, null, null, null, "clientId DESC");
        if (query.moveToFirst()) {
            do {
                if (query.getInt(5) == 1) {
                    arrayList.add(0, new NotepadItemModel(query.getShort(0), query.getString(1), query.getString(2), query.getString(3), query.getInt(4), query.getInt(5)));
                } else {
                    arrayList.add(new NotepadItemModel(query.getShort(0), query.getString(1), query.getString(2), query.getString(3), query.getInt(4), query.getInt(5)));
                }
            } while (query.moveToNext());
        }
        writableDatabase.close();
        return arrayList;
    }

    public Cursor getNote(int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor query = writableDatabase.query("tbleClient", new String[]{"clientId", "clientName", "clientEmail", "clientPhone"}, "clientId = ?", new String[]{String.valueOf(i)}, null, null, null);
        query.moveToFirst();
        writableDatabase.close();
        return query;
    }

    @SuppressLint("Range")
    public ArrayList<Client> getAllClient() {
        ArrayList<Client> arrayList = new ArrayList<>();
        try {
            Utility.logCatMsg("DataBase " + "SELECT  * FROM tbleClient ORDER BY clientId desc");
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tbleClient ORDER BY clientId desc", null);
            if (rawQuery.moveToFirst()) {
                do {
                    Client client = new Client();
                    client.setClientId(rawQuery.getInt(rawQuery.getColumnIndex("clientId")));
                    client.setClientName(rawQuery.getString(rawQuery.getColumnIndex("clientName")));
                    client.setClientEmail(rawQuery.getString(rawQuery.getColumnIndex("clientEmail")));
                    client.setClientPhone(rawQuery.getString(rawQuery.getColumnIndex("clientPhone")));
                    client.setClientMobile(rawQuery.getString(rawQuery.getColumnIndex("clientMobile")));
                    client.setClientFax(rawQuery.getString(rawQuery.getColumnIndex("clientFax")));
                    client.setAddress1(rawQuery.getString(rawQuery.getColumnIndex("clientAddress1")));
                    client.setAddress2(rawQuery.getString(rawQuery.getColumnIndex("clientAddress2")));
                    client.setAddress3(rawQuery.getString(rawQuery.getColumnIndex("clientAddress3")));
                    updateClientOtherDetail(client);
                    arrayList.add(client);
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return arrayList;
    }

    private void updateClientOtherDetail(Client client) {
        int i;
        Exception e;
        int i2 = 0;
        float f = 0.0f;
        try {
            String str = "SELECT sum( invoiceTotalBill) , count( invoiceTotalBill ) FROM tblInvoice Where invoiceClientId = " + client.getClientId();
            Utility.logCatMsg("DataBase " + str);
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery(str, null);
            if (rawQuery.moveToFirst()) {
                i = 0;
                do {
                    try {
                        f = (float) rawQuery.getInt(0);
                        i = rawQuery.getInt(1);
                    } catch (Exception e2) {
                        e = e2;
                        Utility.logCatMsg("Error " + e.getMessage());
                        e.printStackTrace();
                        i2 = i;
                        client.setTotalBill(f);
                        client.setTotalInvoices(i2);
                    }
                } while (rawQuery.moveToNext());
                i2 = i;
            }
        } catch (Exception e3) {
            e = e3;
            i = 0;
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
            i2 = i;
            client.setTotalBill(f);
            client.setTotalInvoices(i2);
        }
        client.setTotalBill(f);
        client.setTotalInvoices(i2);
    }

    public void addNewClient(Client client) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("clientName", client.getClientName());
        contentValues.put("clientEmail", client.getClientEmail());
        contentValues.put("clientPhone", client.getClientPhone());
        contentValues.put("clientMobile", client.getClientMobile());
        contentValues.put("clientFax", client.getClientFax());
        contentValues.put("clientAddress1", client.getAddress1());
        contentValues.put("clientAddress2", client.getAddress2());
        contentValues.put("clientAddress3", client.getAddress3());
        contentValues.put("clientCreatedOn", System.currentTimeMillis() + "");
        writableDatabase.insert("tbleClient", null, contentValues);
        writableDatabase.close();
    }

    public void updateClient(Client client) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("clientName", client.getClientName());
        contentValues.put("clientEmail", client.getClientEmail());
        contentValues.put("clientPhone", client.getClientPhone());
        contentValues.put("clientMobile", client.getClientMobile());
        contentValues.put("clientFax", client.getClientFax());
        contentValues.put("clientAddress1", client.getAddress1());
        contentValues.put("clientAddress2", client.getAddress2());
        contentValues.put("clientAddress3", client.getAddress3());
        writableDatabase.update("tbleClient", contentValues, "clientId = ?", new String[]{String.valueOf(client.getClientId())});
        writableDatabase.close();
    }

    @SuppressLint("Range")
    public Client getClient(String str) {
        Client client = null;
        try {
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tbleClient WHERE clientId = " + str, null);
            if (!rawQuery.moveToFirst()) {
                return null;
            }
            while (true) {
                Client client2 = new Client();
                try {
                    client2.setClientId(rawQuery.getInt(rawQuery.getColumnIndex("clientId")));
                    client2.setClientName(rawQuery.getString(rawQuery.getColumnIndex("clientName")));
                    client2.setClientEmail(rawQuery.getString(rawQuery.getColumnIndex("clientEmail")));
                    client2.setClientPhone(rawQuery.getString(rawQuery.getColumnIndex("clientPhone")));
                    client2.setClientMobile(rawQuery.getString(rawQuery.getColumnIndex("clientMobile")));
                    client2.setClientFax(rawQuery.getString(rawQuery.getColumnIndex("clientFax")));
                    client2.setAddress1(rawQuery.getString(rawQuery.getColumnIndex("clientAddress1")));
                    client2.setAddress2(rawQuery.getString(rawQuery.getColumnIndex("clientAddress2")));
                    client2.setAddress3(rawQuery.getString(rawQuery.getColumnIndex("clientAddress3")));
                    if (!rawQuery.moveToNext()) {
                        return client2;
                    }
                } catch (Exception e) {
                    client = client2;
                    Utility.logCatMsg("Error " + e.getMessage());
                    e.printStackTrace();
                    return client;
                }
            }
        } catch (Exception e2) {
            Utility.logCatMsg("Error " + e2.getMessage());
            e2.printStackTrace();
            return client;
        }
    }

    public void removeClient(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete("tbleClient", "clientId = ?", new String[]{String.valueOf(str)});
        writableDatabase.close();
    }

    @SuppressLint("Range")
    public ArrayList<Product> getAllProductItems() {
        ArrayList<Product> arrayList = new ArrayList<>();
        try {
            Utility.logCatMsg("DataBase " + "SELECT  * FROM tblProduct ORDER BY productId desc");
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tblProduct ORDER BY productId desc", null);
            if (rawQuery.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setProductId(rawQuery.getInt(rawQuery.getColumnIndex("productId")));
                    product.setProductName(rawQuery.getString(rawQuery.getColumnIndex("productName")));
                    product.setProductDescription(rawQuery.getString(rawQuery.getColumnIndex("productDescription")));
                    product.setProductUnitCost(Float.parseFloat(rawQuery.getString(rawQuery.getColumnIndex("productCost"))));
                    product.setProductTaxable(Integer.parseInt(rawQuery.getString(rawQuery.getColumnIndex("productTaxable"))));
                    arrayList.add(product);
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return arrayList;
    }

    public void addNewProduct(Product product) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productName", product.getProductName());
        contentValues.put("productDescription", product.getProductDescription());
        contentValues.put("productCost", product.getProductUnitCost() + "");
        contentValues.put("productTaxable", product.getProductTaxable());
        writableDatabase.insert("tblProduct", null, contentValues);
        writableDatabase.close();
    }

    public void updateProduct(Product product) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productName", product.getProductName());
        contentValues.put("productDescription", product.getProductDescription());
        contentValues.put("productCost", product.getProductUnitCost() + "");
        contentValues.put("productTaxable", product.getProductTaxable());
        writableDatabase.update("tblProduct", contentValues, "productId = ?", new String[]{String.valueOf(product.getProductId())});
        writableDatabase.close();
    }

    @SuppressLint("Range")
    public Product getProduct(String str) {
        Product product = null;
        try {
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tblProduct WHERE productId = " + str, null);
            if (!rawQuery.moveToFirst()) {
                return null;
            }
            while (true) {
                Product product2 = new Product();
                try {
                    product2.setProductId(rawQuery.getInt(rawQuery.getColumnIndex("productId")));
                    product2.setProductName(rawQuery.getString(rawQuery.getColumnIndex("productName")));
                    product2.setProductDescription(rawQuery.getString(rawQuery.getColumnIndex("productDescription")));
                    product2.setProductUnitCost(Float.parseFloat(rawQuery.getString(rawQuery.getColumnIndex("productCost"))));
                    product2.setProductTaxable(Integer.parseInt(rawQuery.getString(rawQuery.getColumnIndex("productTaxable"))));
                    if (!rawQuery.moveToNext()) {
                        return product2;
                    }
                } catch (Exception e) {
                    product = product2;
                    Utility.logCatMsg("Error " + e.getMessage());
                    e.printStackTrace();
                    return product;
                }
            }
        } catch (Exception e2) {
            Utility.logCatMsg("Error " + e2.getMessage());
            e2.printStackTrace();
            return product;
        }
    }

    public void removeProductItem(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete("tblProduct", "productId = ?", new String[]{String.valueOf(str)});
        writableDatabase.close();
    }

    @SuppressLint("DefaultLocale")
    public int createNewInvoice(BusinessInfo businessInfo) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceClientId", "0");
        contentValues.put("invoiceNumber", "INV" + String.format("%04d", getLastInsertedInvoiceId()));
        contentValues.put("invoiceCreationDate", System.currentTimeMillis() + "");
        contentValues.put("invoiceIssueDate", System.currentTimeMillis() + "");
        contentValues.put("invoiceTerms", "");
        contentValues.put("invoiceDueDate", System.currentTimeMillis() + "");
        contentValues.put("invoicePoNumber", "");
        contentValues.put("invoiceNote", "");
        contentValues.put("invoiceBalanceDue", "0");
        contentValues.put("invoiceDiscount", "0");
        contentValues.put("invoiceDiscountType", InvoiceItem.FLAT_AMOUNT);
        contentValues.put("invoiceDiscountPercentage", "0");
        contentValues.put("invoiceTax", "0");
        contentValues.put("invoiceTaxLabel", "Tax");
        contentValues.put("invoiceTaxType", InvoiceHelper.TAX_TYPE_NONE);
        contentValues.put("invoiceTaxPercentage", "0");
        contentValues.put("invoiceTotalBill", "0");
        contentValues.put("invoicePayment", "0");
        contentValues.put("invoicePaymentDate", System.currentTimeMillis() + "");
        contentValues.put("invoiceSignature", "");
        contentValues.put("invoiceSignedOnDate", System.currentTimeMillis() + "");
        contentValues.put("invoiceB_logoPath", businessInfo.getLogoPath());
        contentValues.put("invoiceB_name", businessInfo.getName());
        contentValues.put("invoiceB_ownerName", businessInfo.getOwnerName());
        contentValues.put("invoiceB_businessNumber", businessInfo.getBusinessNumber());
        contentValues.put("invoiceB_address1", businessInfo.getAddress1());
        contentValues.put("invoiceB_address2", businessInfo.getAddress2());
        contentValues.put("invoiceB_address3", businessInfo.getAddress3());
        contentValues.put("invoiceB_email", businessInfo.getEmail());
        contentValues.put("invoiceB_phone", businessInfo.getPhone());
        contentValues.put("invoiceB_mobile", businessInfo.getMobile());
        contentValues.put("invoiceB_website", businessInfo.getWebsite());
        contentValues.put("invoiceB_paypalEmail", businessInfo.getPaypalEmail());
        contentValues.put("invoiceB_checkPayableTo", businessInfo.getCheckPayableTo());
        contentValues.put("invoiceB_bankTransfer", businessInfo.getBankTransfer());
        contentValues.put("invoiceB_otherPayment", businessInfo.getOtherPayment());
        contentValues.put("invoiceB_taxType", businessInfo.getTaxType());
        contentValues.put("invoiceB_taxLabel", businessInfo.getTaxLabel());
        contentValues.put("invoiceB_Rate", businessInfo.getRate());
        contentValues.put("invoiceB_invoicesNote", businessInfo.getInvoicesNote());
        contentValues.put("invoiceB_invoicesTermsAndCondition", businessInfo.getTermsAndCondition());
        contentValues.put("invoiceB_invoicesThankYouMessage", businessInfo.getThankYouMessage());
        contentValues.put("invoiceB_estimatesNote", businessInfo.getEstimatesNote());
        contentValues.put("invoiceB_invoiceNumber", businessInfo.getInvoiceNumber());
        contentValues.put("invoiceB_estimatesNumber", businessInfo.getEstimatesNumber());
        contentValues.put("invoiceB_customizeInvoiceTitle", businessInfo.getCustomizeInvoiceTitle());
        contentValues.put("invoiceB_customizeEstimateTitle", businessInfo.getCustomizeEstimateTitle());
        contentValues.put("invoiceB_customizeBusinessNumber", businessInfo.getCustomizeBusinessNumber());
        contentValues.put("invoiceB_currency", businessInfo.getCurrencyCode());
        contentValues.put("invoiceC_name", "");
        contentValues.put("invoiceC_email", "");
        contentValues.put("invoiceC_phone", "");
        contentValues.put("invoiceC_mobile", "");
        contentValues.put("invoiceC_fax", "");
        contentValues.put("invoiceC_address1", "");
        contentValues.put("invoiceC_address2", "");
        contentValues.put("invoiceC_address3", "");
        contentValues.put("invoice_status_id", "");
        contentValues.put("invoice_status", Invoice.INVOICE_STATUS_DRAFT);
        long insert = writableDatabase.insert("tblInvoice", null, contentValues);
        Utility.logCatMsg("New temp Invoice Created Successfully Invoice id is  " + insert);
        return (int) insert;
    }

    public int updateInvoice(Invoice invoice) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceClientId", invoice.getClientInfo().getClientId());
        contentValues.put("invoiceNumber", invoice.getInvoiceNumber());
        contentValues.put("invoiceCreationDate", invoice.getCreationDate());
        contentValues.put("invoiceTerms", invoice.getTerms());
        contentValues.put("invoiceDueDate", invoice.getDueDate());
        contentValues.put("invoicePoNumber", invoice.getPoNumber());
        contentValues.put("invoiceNote", invoice.getNote());
        contentValues.put("invoiceBalanceDue", invoice.getBalanceDue());
        contentValues.put("invoiceDiscount", invoice.getDiscount());
        contentValues.put("invoiceDiscountType", invoice.getDiscountType());
        contentValues.put("invoiceDiscountPercentage", invoice.getDiscountPercentage());
        contentValues.put("invoiceTax", invoice.getTax());
        contentValues.put("invoiceTaxLabel", invoice.getTaxLabel());
        contentValues.put("invoiceTaxType", invoice.getTaxType());
        contentValues.put("invoiceTaxPercentage", invoice.getTaxPercentage());
        contentValues.put("invoiceTotalBill", invoice.getTotalBill());
        contentValues.put("invoicePayment", invoice.getPayment());
        contentValues.put("invoicePaymentDate", invoice.getPaymentDate());
        contentValues.put("invoiceSignature", invoice.getSignature());
        contentValues.put("invoiceSignedOnDate", invoice.getSignedOnDate());
        contentValues.put("invoiceB_logoPath", invoice.getBusinessInfo().getLogoPath());
        contentValues.put("invoiceB_name", invoice.getBusinessInfo().getName());
        contentValues.put("invoiceB_ownerName", invoice.getBusinessInfo().getOwnerName());
        contentValues.put("invoiceB_businessNumber", invoice.getBusinessInfo().getBusinessNumber());
        contentValues.put("invoiceB_address1", invoice.getBusinessInfo().getAddress1());
        contentValues.put("invoiceB_address2", invoice.getBusinessInfo().getAddress2());
        contentValues.put("invoiceB_address3", invoice.getBusinessInfo().getAddress3());
        contentValues.put("invoiceB_email", invoice.getBusinessInfo().getEmail());
        contentValues.put("invoiceB_phone", invoice.getBusinessInfo().getPhone());
        contentValues.put("invoiceB_mobile", invoice.getBusinessInfo().getMobile());
        contentValues.put("invoiceB_website", invoice.getBusinessInfo().getWebsite());
        contentValues.put("invoiceB_paypalEmail", invoice.getBusinessInfo().getPaypalEmail());
        contentValues.put("invoiceB_checkPayableTo", invoice.getBusinessInfo().getCheckPayableTo());
        contentValues.put("invoiceB_bankTransfer", invoice.getBusinessInfo().getBankTransfer());
        contentValues.put("invoiceB_otherPayment", invoice.getBusinessInfo().getOtherPayment());
        contentValues.put("invoiceB_taxType", invoice.getBusinessInfo().getTaxType());
        contentValues.put("invoiceB_taxLabel", invoice.getBusinessInfo().getTaxLabel());
        contentValues.put("invoiceB_Rate", invoice.getBusinessInfo().getRate());
        contentValues.put("invoiceB_invoicesTermsAndCondition", invoice.getBusinessInfo().getTermsAndCondition());
        contentValues.put("invoiceB_invoicesThankYouMessage", invoice.getBusinessInfo().getThankYouMessage());
        contentValues.put("invoiceB_invoicesNote", invoice.getBusinessInfo().getInvoicesNote());
        contentValues.put("invoiceB_estimatesNote", invoice.getBusinessInfo().getEstimatesNote());
        contentValues.put("invoiceB_invoiceNumber", invoice.getBusinessInfo().getInvoiceNumber());
        contentValues.put("invoiceB_estimatesNumber", invoice.getBusinessInfo().getEstimatesNumber());
        contentValues.put("invoiceB_customizeInvoiceTitle", invoice.getBusinessInfo().getCustomizeInvoiceTitle());
        contentValues.put("invoiceB_customizeEstimateTitle", invoice.getBusinessInfo().getCustomizeEstimateTitle());
        contentValues.put("invoiceB_customizeBusinessNumber", invoice.getBusinessInfo().getCustomizeBusinessNumber());
        contentValues.put("invoiceB_currency", invoice.getBusinessInfo().getCurrencyCode());
        contentValues.put("invoiceC_name", invoice.getClientInfo().getClientName());
        contentValues.put("invoiceC_email", invoice.getClientInfo().getClientEmail());
        contentValues.put("invoiceC_phone", invoice.getClientInfo().getClientPhone());
        contentValues.put("invoiceC_mobile", invoice.getClientInfo().getClientMobile());
        contentValues.put("invoiceC_fax", invoice.getClientInfo().getClientFax());
        contentValues.put("invoiceC_address1", invoice.getClientInfo().getAddress1());
        contentValues.put("invoiceC_address2", invoice.getClientInfo().getAddress2());
        contentValues.put("invoiceC_address3", invoice.getClientInfo().getAddress3());
        contentValues.put("invoice_status_id", invoice.getInvoiceStatusId());
        contentValues.put("invoice_status", invoice.getInvoiceStatus());
        return writableDatabase.update("tblInvoice", contentValues, "invoiceId = ?", new String[]{invoice.getInvoiceId() + ""});
    }

    @SuppressLint("Range")
    public ArrayList<Invoice> getAllInvoices() {
        ArrayList<Invoice> arrayList = new ArrayList<>();
        try {
            Utility.logCatMsg("DataBase " + "SELECT  * FROM tblInvoice ORDER BY invoiceId desc");
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tblInvoice ORDER BY invoiceId desc", null);
            if (rawQuery.moveToFirst()) {
                do {
                    Invoice invoice = new Invoice();
                    Client client = new Client();
                    BusinessInfo businessInfo = new BusinessInfo();
                    invoice.setInvoiceId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceId")));
                    invoice.setInvoiceNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceNumber")));
                    invoice.setCreationDate(rawQuery.getString(rawQuery.getColumnIndex("invoiceCreationDate")));
                    invoice.setIssueDate(rawQuery.getString(rawQuery.getColumnIndex("invoiceIssueDate")));
                    invoice.setTerms(rawQuery.getString(rawQuery.getColumnIndex("invoiceTerms")));
                    invoice.setDueDate(rawQuery.getString(rawQuery.getColumnIndex("invoiceDueDate")));
                    invoice.setPoNumber(rawQuery.getString(rawQuery.getColumnIndex("invoicePoNumber")));
                    invoice.setNote(rawQuery.getString(rawQuery.getColumnIndex("invoiceNote")));
                    invoice.setBalanceDue(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceBalanceDue")));
                    invoice.setDiscount(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceDiscount")));
                    invoice.setDiscountType(rawQuery.getString(rawQuery.getColumnIndex("invoiceDiscountType")));
                    invoice.setDiscountPercentage(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceDiscountPercentage")));
                    invoice.setTax(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceTax")));
                    invoice.setTaxLabel(rawQuery.getString(rawQuery.getColumnIndex("invoiceTaxLabel")));
                    invoice.setTaxType(rawQuery.getString(rawQuery.getColumnIndex("invoiceTaxType")));
                    invoice.setTaxPercentage(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceTaxPercentage")));
                    invoice.setTotalBill(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceTotalBill")));
                    invoice.setPayment(rawQuery.getFloat(rawQuery.getColumnIndex("invoicePayment")));
                    invoice.setPaymentDate(rawQuery.getString(rawQuery.getColumnIndex("invoicePaymentDate")));
                    invoice.setSignature(rawQuery.getString(rawQuery.getColumnIndex("invoiceSignature")));
                    invoice.setSignedOnDate(rawQuery.getString(rawQuery.getColumnIndex("invoiceSignedOnDate")));
                    businessInfo.setLogoPath(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_logoPath")));
                    businessInfo.setName(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_name")));
                    businessInfo.setOwnerName(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_ownerName")));
                    businessInfo.setBusinessNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_businessNumber")));
                    businessInfo.setAddress1(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_address1")));
                    businessInfo.setAddress2(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_address2")));
                    businessInfo.setAddress3(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_address3")));
                    businessInfo.setEmail(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_email")));
                    businessInfo.setPhone(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_phone")));
                    businessInfo.setMobile(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_mobile")));
                    businessInfo.setWebsite(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_website")));
                    businessInfo.setPaypalEmail(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_paypalEmail")));
                    businessInfo.setCheckPayableTo(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_checkPayableTo")));
                    businessInfo.setBankTransfer(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_bankTransfer")));
                    businessInfo.setOtherPayment(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_otherPayment")));
                    businessInfo.setTaxType(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_taxType")));
                    businessInfo.setTaxLabel(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_taxLabel")));
                    businessInfo.setRate(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_Rate")));
                    businessInfo.setTermsAndCondition(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_invoicesTermsAndCondition")));
                    businessInfo.setThankYouMessage(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_invoicesThankYouMessage")));
                    businessInfo.setInvoicesNote(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_invoicesNote")));
                    businessInfo.setEstimatesNote(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_estimatesNote")));
                    businessInfo.setInvoiceNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_invoiceNumber")));
                    businessInfo.setEstimatesNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_estimatesNumber")));
                    businessInfo.setCustomizeInvoiceTitle(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_customizeInvoiceTitle")));
                    businessInfo.setCustomizeEstimateTitle(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_customizeEstimateTitle")));
                    businessInfo.setCustomizeBusinessNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_customizeBusinessNumber")));
                    businessInfo.setCurrencyCode(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_currency")));
                    businessInfo.setDateFormat(Singleton.getInstance().getBusinessInfo(this.context).getDateFormat());
                    invoice.setBusinessInfo(businessInfo);
                    client.setClientId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceClientId")));
                    client.setClientName(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_name")));
                    client.setClientEmail(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_email")));
                    client.setClientPhone(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_phone")));
                    client.setClientMobile(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_mobile")));
                    client.setClientFax(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_fax")));
                    client.setAddress1(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_address1")));
                    client.setAddress2(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_address2")));
                    client.setAddress3(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_address3")));
                    invoice.setInvoiceStatusId(rawQuery.getInt(rawQuery.getColumnIndex("invoice_status_id")));
                    invoice.setInvoiceStatus(rawQuery.getString(rawQuery.getColumnIndex("invoice_status")));
                    invoice.setClientInfo(client);
                    arrayList.add(invoice);
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return arrayList;
    }

    @SuppressLint("Range")
    public Invoice getSelectedInvoices(int i) {
        Invoice invoice = new Invoice();
        try {
            String str = "SELECT  * FROM tblInvoice Where invoiceId = " + i;
            Utility.logCatMsg("DataBase " + str);
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery(str, null);
            if (rawQuery.moveToFirst()) {
                do {
                    Client client = new Client();
                    BusinessInfo businessInfo = new BusinessInfo();
                    invoice.setInvoiceId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceId")));
                    invoice.setInvoiceNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceNumber")));
                    invoice.setCreationDate(rawQuery.getString(rawQuery.getColumnIndex("invoiceCreationDate")));
                    invoice.setIssueDate(rawQuery.getString(rawQuery.getColumnIndex("invoiceIssueDate")));
                    invoice.setTerms(rawQuery.getString(rawQuery.getColumnIndex("invoiceTerms")));
                    invoice.setDueDate(rawQuery.getString(rawQuery.getColumnIndex("invoiceDueDate")));
                    invoice.setPoNumber(rawQuery.getString(rawQuery.getColumnIndex("invoicePoNumber")));
                    invoice.setNote(rawQuery.getString(rawQuery.getColumnIndex("invoiceNote")));
                    invoice.setBalanceDue(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceBalanceDue")));
                    invoice.setDiscount(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceDiscount")));
                    invoice.setDiscountType(rawQuery.getString(rawQuery.getColumnIndex("invoiceDiscountType")));
                    invoice.setDiscountPercentage(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceDiscountPercentage")));
                    invoice.setTax(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceTax")));
                    invoice.setTaxLabel(rawQuery.getString(rawQuery.getColumnIndex("invoiceTaxLabel")));
                    invoice.setTaxType(rawQuery.getString(rawQuery.getColumnIndex("invoiceTaxType")));
                    invoice.setTaxPercentage(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceTaxPercentage")));
                    invoice.setTotalBill(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceTotalBill")));
                    invoice.setPayment(rawQuery.getFloat(rawQuery.getColumnIndex("invoicePayment")));
                    invoice.setPaymentDate(rawQuery.getString(rawQuery.getColumnIndex("invoicePaymentDate")));
                    invoice.setSignature(rawQuery.getString(rawQuery.getColumnIndex("invoiceSignature")));
                    invoice.setSignedOnDate(rawQuery.getString(rawQuery.getColumnIndex("invoiceSignedOnDate")));
                    businessInfo.setLogoPath(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_logoPath")));
                    businessInfo.setName(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_name")));
                    businessInfo.setOwnerName(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_ownerName")));
                    businessInfo.setBusinessNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_businessNumber")));
                    businessInfo.setAddress1(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_address1")));
                    businessInfo.setAddress2(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_address2")));
                    businessInfo.setAddress3(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_address3")));
                    businessInfo.setEmail(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_email")));
                    businessInfo.setPhone(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_phone")));
                    businessInfo.setMobile(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_mobile")));
                    businessInfo.setWebsite(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_website")));
                    businessInfo.setPaypalEmail(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_paypalEmail")));
                    businessInfo.setCheckPayableTo(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_checkPayableTo")));
                    businessInfo.setBankTransfer(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_bankTransfer")));
                    businessInfo.setOtherPayment(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_otherPayment")));
                    businessInfo.setTaxType(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_taxType")));
                    businessInfo.setTaxLabel(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_taxLabel")));
                    businessInfo.setRate(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_Rate")));
                    businessInfo.setTermsAndCondition(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_invoicesTermsAndCondition")));
                    businessInfo.setThankYouMessage(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_invoicesThankYouMessage")));
                    businessInfo.setInvoicesNote(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_invoicesNote")));
                    businessInfo.setEstimatesNote(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_estimatesNote")));
                    businessInfo.setInvoiceNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_invoiceNumber")));
                    businessInfo.setEstimatesNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_estimatesNumber")));
                    businessInfo.setCustomizeInvoiceTitle(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_customizeInvoiceTitle")));
                    businessInfo.setCustomizeEstimateTitle(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_customizeEstimateTitle")));
                    businessInfo.setCustomizeBusinessNumber(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_customizeBusinessNumber")));
                    businessInfo.setCurrencyCode(rawQuery.getString(rawQuery.getColumnIndex("invoiceB_currency")));
                    businessInfo.setDateFormat(Singleton.getInstance().getBusinessInfo(this.context).getDateFormat());
                    invoice.setBusinessInfo(businessInfo);
                    client.setClientId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceClientId")));
                    client.setClientName(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_name")));
                    client.setClientEmail(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_email")));
                    client.setClientPhone(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_phone")));
                    client.setClientMobile(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_mobile")));
                    client.setClientFax(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_fax")));
                    client.setAddress1(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_address1")));
                    client.setAddress2(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_address2")));
                    client.setAddress3(rawQuery.getString(rawQuery.getColumnIndex("invoiceC_address3")));
                    invoice.setInvoiceStatusId(rawQuery.getInt(rawQuery.getColumnIndex("invoice_status_id")));
                    invoice.setInvoiceStatus(rawQuery.getString(rawQuery.getColumnIndex("invoice_status")));
                    invoice.setClientInfo(client);
                    invoice.setAttachments(getAttachments(invoice.getInvoiceId() + ""));
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return invoice;
    }

    @SuppressLint("Range")
    public int getLastInsertedInvoiceId() {
        int i = 0;
        try {
            Utility.logCatMsg("DataBase " + "SELECT  invoiceId FROM tblInvoice ORDER BY invoiceId DESC LIMIT 1");
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  invoiceId FROM tblInvoice ORDER BY invoiceId DESC LIMIT 1", null);
            if (rawQuery.moveToFirst()) {
                do {
                    i = rawQuery.getInt(rawQuery.getColumnIndex("invoiceId"));
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return i + 1;
    }

    public float getTotalInvoiceDuesAmount() {
        float f = 0.0f;
        try {
            Utility.logCatMsg("DataBase " + "SELECT sum( invoiceBalanceDue) FROM tblInvoice");
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT sum( invoiceBalanceDue) FROM tblInvoice", null);
            if (rawQuery.moveToFirst()) {
                do {
                    f = (float) rawQuery.getInt(0);
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return f;
    }

    public void removeInvoice(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete("tblInvoice", "invoiceId = ?", new String[]{String.valueOf(str)});
        writableDatabase.close();
    }

    public void addNewInvoiceItem(InvoiceItem invoiceItem) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceItem_invoice_id", invoiceItem.getInvoiceId());
        contentValues.put("invoiceItem_productId", invoiceItem.getProductId());
        contentValues.put("invoiceItem_name", invoiceItem.getInvoiceName());
        contentValues.put("invoiceItem_quantity", invoiceItem.getQuantity());
        contentValues.put("invoiceItem_taxable", invoiceItem.getTaxable());
        contentValues.put("invoiceItem_unitPrice", invoiceItem.getUnitPrice());
        contentValues.put("invoiceItem_discount", invoiceItem.getDiscount());
        contentValues.put("invoiceItem_discount_percenate", invoiceItem.getDiscountPercentage());
        contentValues.put("invoiceItem_priceExclVAT", invoiceItem.getPriceExclVAT());
        contentValues.put("invoiceItem_vat", invoiceItem.getVat());
        contentValues.put("invoiceItem_vatRate", invoiceItem.getVatRate());
        contentValues.put("invoiceItem_discountType", invoiceItem.getDiscountType());
        contentValues.put("invoiceItem_detail", invoiceItem.getDetail());
        long insert = writableDatabase.insert("tblInvoiceItem", null, contentValues);
        Utility.logCatMsg("msg data inserted into database " + insert);
    }

    public void updateInvoiceItem(InvoiceItem invoiceItem) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceItem_invoice_id", invoiceItem.getInvoiceId());
        contentValues.put("invoiceItem_productId", invoiceItem.getProductId());
        contentValues.put("invoiceItem_name", invoiceItem.getInvoiceName());
        contentValues.put("invoiceItem_quantity", invoiceItem.getQuantity());
        contentValues.put("invoiceItem_taxable", invoiceItem.getTaxable());
        contentValues.put("invoiceItem_unitPrice", invoiceItem.getUnitPrice());
        contentValues.put("invoiceItem_discount", invoiceItem.getDiscount());
        contentValues.put("invoiceItem_discount_percenate", invoiceItem.getDiscountPercentage());
        contentValues.put("invoiceItem_priceExclVAT", invoiceItem.getPriceExclVAT());
        contentValues.put("invoiceItem_vat", invoiceItem.getVat());
        contentValues.put("invoiceItem_vatRate", invoiceItem.getVatRate());
        contentValues.put("invoiceItem_discountType", invoiceItem.getDiscountType());
        contentValues.put("invoiceItem_detail", invoiceItem.getDetail());
        long update = writableDatabase.update("tblInvoiceItem", contentValues, "invoiceItem_id = ?", new String[]{invoiceItem.getInvoiceItemId() + ""});
        Utility.logCatMsg("updated into database " + update);
    }

    public int deleteInvoiceItem(String str) {
        return getWritableDatabase().delete("tblInvoiceItem", "invoiceItem_id = ?", new String[]{String.valueOf(str)});
    }

    @SuppressLint("Range")
    public ArrayList<InvoiceItem> getAllOrderInvoicesItems(String str) {
        ArrayList<InvoiceItem> arrayList = new ArrayList<>();
        try {
            String str2 = "SELECT  * FROM tblInvoiceItem WHERE invoiceItem_invoice_id = " + str;
            Utility.logCatMsg("DataBase " + str2);
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery(str2, null);
            if (rawQuery.moveToFirst()) {
                do {
                    InvoiceItem invoiceItem = new InvoiceItem();
                    invoiceItem.setInvoiceItemId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_id")));
                    invoiceItem.setInvoiceId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_invoice_id")));
                    invoiceItem.setProductId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_productId")));
                    invoiceItem.setInvoiceName(rawQuery.getString(rawQuery.getColumnIndex("invoiceItem_name")));
                    invoiceItem.setQuantity(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_quantity")));
                    invoiceItem.setTaxable(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_taxable")));
                    invoiceItem.setUnitPrice(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_unitPrice")));
                    invoiceItem.setDiscount(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_discount")));
                    invoiceItem.setDiscountPercentage(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_discount_percenate")));
                    invoiceItem.setPriceExclVAT(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_priceExclVAT")));
                    invoiceItem.setVat(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_vat")));
                    invoiceItem.setVatRate(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_vatRate")));
                    invoiceItem.setDiscountType(rawQuery.getString(rawQuery.getColumnIndex("invoiceItem_discountType")));
                    invoiceItem.setDetail(rawQuery.getString(rawQuery.getColumnIndex("invoiceItem_detail")));
                    arrayList.add(invoiceItem);
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return arrayList;
    }

    @SuppressLint("Range")
    public InvoiceItem getOrderInvoicesItem(String str, String str2) {
        InvoiceItem invoiceItem = new InvoiceItem();
        try {
            String str3 = "SELECT  * FROM tblInvoiceItem WHERE invoiceItem_id = " + str + " And " + "invoiceItem_invoice_id" + " = " + str2;
            Utility.logCatMsg("DataBase " + str3);
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery(str3, null);
            if (rawQuery.moveToFirst()) {
                do {
                    invoiceItem.setInvoiceItemId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_id")));
                    invoiceItem.setInvoiceId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_invoice_id")));
                    invoiceItem.setProductId(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_productId")));
                    invoiceItem.setInvoiceName(rawQuery.getString(rawQuery.getColumnIndex("invoiceItem_name")));
                    invoiceItem.setQuantity(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_quantity")));
                    invoiceItem.setTaxable(rawQuery.getInt(rawQuery.getColumnIndex("invoiceItem_taxable")));
                    invoiceItem.setUnitPrice(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_unitPrice")));
                    invoiceItem.setDiscount(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_discount")));
                    invoiceItem.setDiscountPercentage(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_discount_percenate")));
                    invoiceItem.setPriceExclVAT(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_priceExclVAT")));
                    invoiceItem.setVat(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_vat")));
                    invoiceItem.setVatRate(rawQuery.getFloat(rawQuery.getColumnIndex("invoiceItem_vatRate")));
                    invoiceItem.setDiscountType(rawQuery.getString(rawQuery.getColumnIndex("invoiceItem_discountType")));
                    invoiceItem.setDetail(rawQuery.getString(rawQuery.getColumnIndex("invoiceItem_detail")));
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return invoiceItem;
    }

    public void addNewAttachment(Attachment attachment) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("attachmentInoviceId", attachment.getInvoiceId());
            contentValues.put("attachmentImagePath", attachment.getImagePath());
            contentValues.put("attachmentDescription", attachment.getDescription());
            contentValues.put("attachmentAdditionalDetail", attachment.getAdditionalDetails());
            contentValues.put("attachmentCreatedOn", System.currentTimeMillis() + "");
            writableDatabase.insert("tblAttachment", null, contentValues);
            writableDatabase.close();
        } catch (Exception e) {
            Utility.logCatMsg("Exception... ");
            e.printStackTrace();
        }
    }

    public void updateAttachment(Attachment attachment) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("attachmentInoviceId", attachment.getInvoiceId());
        contentValues.put("attachmentImagePath", attachment.getImagePath());
        contentValues.put("attachmentDescription", attachment.getDescription());
        contentValues.put("attachmentAdditionalDetail", attachment.getAdditionalDetails());
        writableDatabase.update("tblAttachment", contentValues, "attachmentId = ?", new String[]{String.valueOf(attachment.getAttachmentId())});
        writableDatabase.close();
    }

    @SuppressLint("Range")
    public ArrayList<Attachment> getAttachments(String str) {
        ArrayList<Attachment> arrayList = new ArrayList<>();
        try {
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tblAttachment WHERE attachmentInoviceId = " + str, null);
            if (rawQuery.moveToFirst()) {
                do {
                    Attachment attachment = new Attachment();
                    attachment.setAttachmentId(rawQuery.getInt(rawQuery.getColumnIndex("attachmentId")));
                    attachment.setInvoiceId(rawQuery.getInt(rawQuery.getColumnIndex("attachmentInoviceId")));
                    attachment.setImagePath(rawQuery.getString(rawQuery.getColumnIndex("attachmentImagePath")));
                    attachment.setDescription(rawQuery.getString(rawQuery.getColumnIndex("attachmentDescription")));
                    attachment.setAdditionalDetails(rawQuery.getString(rawQuery.getColumnIndex("attachmentAdditionalDetail")));
                    attachment.setCreatedOn(rawQuery.getString(rawQuery.getColumnIndex("attachmentCreatedOn")));
                    arrayList.add(attachment);
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return arrayList;
    }

    @SuppressLint("Range")
    public Attachment getAttachment(String str) {
        Attachment attachment = null;
        try {
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tblAttachment WHERE attachmentId = " + str, null);
            if (!rawQuery.moveToFirst()) {
                return null;
            }
            while (true) {
                Attachment attachment2 = new Attachment();
                try {
                    attachment2.setAttachmentId(rawQuery.getInt(rawQuery.getColumnIndex("attachmentId")));
                    attachment2.setInvoiceId(rawQuery.getInt(rawQuery.getColumnIndex("attachmentInoviceId")));
                    attachment2.setImagePath(rawQuery.getString(rawQuery.getColumnIndex("attachmentImagePath")));
                    attachment2.setDescription(rawQuery.getString(rawQuery.getColumnIndex("attachmentDescription")));
                    attachment2.setAdditionalDetails(rawQuery.getString(rawQuery.getColumnIndex("attachmentAdditionalDetail")));
                    attachment2.setCreatedOn(rawQuery.getString(rawQuery.getColumnIndex("attachmentCreatedOn")));
                    if (!rawQuery.moveToNext()) {
                        return attachment2;
                    }
                } catch (Exception e) {
                    attachment = attachment2;
                    Utility.logCatMsg("Error " + e.getMessage());
                    e.printStackTrace();
                    return attachment;
                }
            }
        } catch (Exception e2) {
            Utility.logCatMsg("Error " + e2.getMessage());
            e2.printStackTrace();
            return attachment;
        }
    }

    public void removeAttachment(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete("tblAttachment", "attachmentId = ?", new String[]{String.valueOf(str)});
        writableDatabase.close();
    }
}
