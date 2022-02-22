package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVTemplate;

import android.content.Context;
import android.net.Uri;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Attachment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Client;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.InvoiceItem;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.ArrayList;

public class InvoiceTemplate1 {
    private ArrayList<InvoiceItem> invoiceItems = new ArrayList<>();
    private InvoiceDatabaseHelper invoiceDatabaseHelper;
    private BusinessInfo businessInfo;
    private Client client;
    private Invoice mInvoice;

    public String getStyle() {
        return "<style>.clearfix:after {  content: \"\";  display: table;  clear: both;}.row::after {  content: \"\";  clear: both;  display: table;}a {  color: #5D6975;  text-decoration: underline;}body {  position: relative;  width: 21cm;    height: 29.7cm;   margin: 0 auto;   color: #001028;  background: #FFFFFF;   font-family: Arial, sans-serif;   font-size: 12px;   font-family: Arial;}header {  padding: 10px 0;  margin-bottom: 30px;}#logo {  margin-bottom: 5px;}#logo img { width: 180px; align: center;} h2 { font-size: 1.2rem; font-weight: normal; } h3 { font-size: 1.0rem; font-weight: normal;} h4 { font-size: 1.6rem; }h1 {  border-top: 1px solid  #5D6975;  border-bottom: 1px solid  #5D6975;  color: #5D6975;  font-size: 2.6em;  line-height: 1.4em;  font-weight: normal;  text-align: center;  margin: 0 0 20px 0;  background: url(dimension.png);}#project {float: left;}#project span {  color: #5D6975;  text-align: right;  width: 52px;  margin-right: 10px;  display: inline-block;  font-size: 0.8em;}.column_payment {  flex: 50%;  padding: 10px; }.column {  flex: 50%;  padding: 10px; }.column30 {  float: left;  width: 33.33%;  padding: 5px;}#company {  float: right;  text-align: right;}#invoice {  float: right;  text-align: right;}#project div,#company div {  white-space: nowrap;        }table {  width: 100%;  border-collapse: collapse;  border-spacing: 0;  margin-bottom: 20px;}table tr:nth-child(2n-1) td {  background: #F5F5F5;}table th,table td {  text-align: center;}table th {  padding: 5px 20px;  color: #5D6975;  border-bottom: 1px solid #C1CED9;  white-space: nowrap;          font-weight: normal;}table .service,table .desc {  text-align: left;}table td {  padding: 20px;  text-align: right;}table td.service,table td.desc {  vertical-align: top;}table td.unit,table td.qty,table td.total {  font-size: 1.2em;}table td.grand {  border-top: 1px solid #5D6975;;}#notices .notice {  color: #5D6975;  font-size: 1.2em;}footer {  color: #5D6975;  width: 100%;  height: 30px;  position: absolute;  bottom: 0;  border-top: 1px solid #C1CED9;  padding: 8px 0;  text-align: center;}\t</style>";
    }

    public String getContent(Context context, Invoice invoice) {
        mInvoice = invoice;
        invoiceDatabaseHelper = new InvoiceDatabaseHelper(context);
        businessInfo = mInvoice.getBusinessInfo();
        client = mInvoice.getClientInfo();
        return "<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Invoice</title><script src=\"https://kit.fontawesome.com/b99e675b6e.js\"></script>" + getStyle() + "</head><body><header class=\"clearfix\"><div style=\"display: flex;\">" + getCompanyLogo(context) + getInvoiceDetails() + "</div><div style=\"display: flex;\">" + getCompanyDetail() + getClientDetail() + "</div> </header>    <main>      <table>        <thead>          <tr>            <th>#</th>            <th class=\"desc\">DESCRIPTION</th>            <th>PRICE</th>            <th>QTY</th>            <th>DISCOUNT</th>            <th>TAX</th>            <th>TOTAL</th>          </tr>        </thead>        <tbody>" + getInvoiceItems() + "        </tbody>      </table><div style=\"display: flex;\">" + getPaymentAndOtherInfo() + getSignature() + "</div>" + getAttachment(context) + "    </main>  </body></html>";
    }

    private String getAttachment(Context context) {
        String str;
        ArrayList<Attachment> attachments = mInvoice.getAttachments();
        if (attachments.size() <= 0) {
            return "";
        }
        StringBuilder str2 = new StringBuilder();
        for (int i = 0; i < attachments.size(); i++) {
            String str3 = "<b>" + attachments.get(i).getDescription() + "</b>";
            String additionalDetails = attachments.get(i).getAdditionalDetails();
            if (!str3.isEmpty() || !additionalDetails.isEmpty()) {
                str = "<p>" + str3 + " " + additionalDetails + "</p>";
            } else {
                str = "";
            }
            str2.append("<div class=\"column\"><img  style=\"width: 200px;\" src=\"data:image/png;base64, ").append(Utility.convertToBase64String(context, Uri.parse(attachments.get(i).getImagePath()))).append("\"  />").append(str).append("</div>");
        }
        return "<h2>ATTACHMENT</h2><div >" + str2 + "</div>";
    }

    private String getInvoiceDetails() {
        String str;
        String str2;
        String str3 = "";
        if (!mInvoice.getInvoiceNumber().isEmpty()) {
            str = "<h2 style=\"font-size: 2.0em;\">" + mInvoice.getInvoiceNumber() + "</h2>";
        } else {
            str = str3;
        }
        if (!mInvoice.getCreationDate().isEmpty()) {
            str2 = "<span>Invoice Date : </span><b>" + Utility.geDateTime(Long.parseLong(mInvoice.getCreationDate()), businessInfo.getDateFormat()) + "</b></br>";
        } else {
            str2 = str3;
        }
        if (!mInvoice.getDueDate().isEmpty()) {
            str3 = "<span>Due Date : </span><b>" + Utility.geDateTime(Long.parseLong(mInvoice.getDueDate()), businessInfo.getDateFormat()) + "</b></br>";
        }
        return "  <div class=\"column30\" style=\"text-align: right;\"  > <h3>" + str + str2 + str3 + "</h3> </div>";
    }

    private String getSignature() {
        if (mInvoice.getSignature().isEmpty()) {
            return "";
        }
        return "  <div class=\"column\" style=\"text-align: right;\"><img  style=\"width: 260px;\" src=\"data:image/png;base64, " + mInvoice.getSignature() + "\"  /> <p><b>Authorise Sign</b><br>Sign Date : " + Utility.geDateTime(Long.parseLong(mInvoice.getSignedOnDate()), businessInfo.getDateFormat()) + "</p> </div>";
    }

    private String getPaymentAndOtherInfo() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        if (!businessInfo.getPaypalEmail().isEmpty()) {
            str = "<span>Paypal : </span><b>" + businessInfo.getPaypalEmail() + "</b></br>";
        } else {
            str = "";
        }
        if (!businessInfo.getCheckPayableTo().isEmpty()) {
            str2 = "<span>Payable Check : </span><b>" + businessInfo.getCheckPayableTo() + "</b></br>";
        } else {
            str2 = "";
        }
        if (!businessInfo.getBankTransfer().isEmpty()) {
            str3 = "<span>Bank Transfer : </span><b>" + businessInfo.getBankTransfer() + "</b></br>";
        } else {
            str3 = "";
        }
        if (!businessInfo.getOtherPayment().isEmpty()) {
            str4 = "<span>Other Method : </span><b>" + businessInfo.getOtherPayment() + "</b></br>";
        } else {
            str4 = "";
        }
        if (!businessInfo.getThankYouMessage().isEmpty()) {
            str5 = "<div class=\"column_payment\" ><h3>" + businessInfo.getThankYouMessage() + "</h3></div>";
        } else {
            str5 = "";
        }
        if (!businessInfo.getTermsAndCondition().isEmpty()) {
            str6 = "<div class=\"column_payment\" ><h2>Terms & Condition </h2><h3>" + businessInfo.getTermsAndCondition() + "</h3></div>";
        } else {
            str6 = "";
        }
        if (!businessInfo.getInvoicesNote().isEmpty()) {
            str7 = "<div class=\"column_payment\" ><h3>Note : " + businessInfo.getInvoicesNote() + "</h3></div>";
        } else {
            str7 = "";
        }
        if (str.isEmpty() && str2.isEmpty() && str3.isEmpty() && str4.isEmpty()) {
            return "";
        }
        return "  <div class=\"column\" >" + str5 + "<div class=\"column_payment\" ><h2>Payment Info</h2><h3>" + str + str2 + str3 + str4 + "</h3></div>" + str6 + str7 + "</div>";
    }

    private String getInvoiceItems() {
        String str;
        double d;
        String str2;
        String str3 = null;
        String str4;
        String str5 = null;
        String str6;
        double d2;
        String str7;
        String str8;
        double d3;
        String str9;
        this.invoiceItems.clear();
        InvoiceDatabaseHelper invoiceDatabaseHelper = this.invoiceDatabaseHelper;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mInvoice.getInvoiceId());
        String str10 = "";
        sb.append(str10);
        this.invoiceItems = invoiceDatabaseHelper.getAllOrderInvoicesItems(sb.toString());
        int i = 0;
        String str11 = str10;
        double d4 = 0.0d;
        double d5 = 0.0d;
        double d6 = 0.0d;
        while (i < this.invoiceItems.size()) {
            InvoiceItem invoiceItem = this.invoiceItems.get(i);
            double quantity = (double) ((((float) this.invoiceItems.get(i).getQuantity()) * this.invoiceItems.get(i).getUnitPrice()) - this.invoiceItems.get(i).getDiscount());
            Double.isNaN(quantity);
            double d7 = d4 + quantity;
            if (!this.mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM) || this.invoiceItems.get(i).getTaxable() != 1) {
                str6 = str10;
            } else {
                str6 = str10;
                double vat = (double) this.invoiceItems.get(i).getVat();
                Double.isNaN(vat);
                d5 += vat;
                double vatRate = (double) this.invoiceItems.get(i).getVatRate();
                Double.isNaN(vatRate);
                d6 += vatRate;
            }
            if (invoiceItem.getDiscount() != 0.0f) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("<td class=\"qty\">- ");
                str7 = "<td class=\"qty\"></td>";
                sb2.append(this.businessInfo.getCurrencySymbol());
                d2 = d7;
                sb2.append(Utility.fmt((double) invoiceItem.getDiscount()));
                sb2.append("</td>");
                str8 = sb2.toString();
                if (invoiceItem.getDiscountType().equals(InvoiceItem.PERCENTAGE)) {
                    str8 = "<td class=\"qty\">- " + this.businessInfo.getCurrencySymbol() + Utility.fmt((double) invoiceItem.getDiscount()) + "<br/><small>" + Utility.fmt((double) invoiceItem.getDiscountPercentage()) + "%</small></td>";
                }
            } else {
                str7 = "<td class=\"qty\"></td>";
                d2 = d7;
                str8 = str7;
            }
            if (!this.mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM) || invoiceItem.getTaxable() != 1) {
                d3 = d5;
                str9 = str7;
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("<td class=\"qty\">");
                sb3.append(this.businessInfo.getCurrencySymbol());
                d3 = d5;
                sb3.append(Utility.fmt((double) invoiceItem.getVatRate()));
                sb3.append("<br/>");
                sb3.append(Utility.fmt((double) invoiceItem.getVat()));
                sb3.append("%</td>");
                str9 = sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str11);
            sb4.append("<tr><td>");
            int i2 = i + 1;
            sb4.append(i2);
            sb4.append("</td><td class=\"desc\">");
            sb4.append(this.invoiceItems.get(i).getInvoiceName());
            sb4.append("<br/>");
            sb4.append(this.invoiceItems.get(i).getDetail());
            sb4.append("</td><td class=\"unit\">");
            sb4.append(this.businessInfo.getCurrencySymbol());
            sb4.append(Utility.fmt((double) invoiceItem.getUnitPrice()));
            sb4.append("</td><td class=\"qty\">");
            sb4.append(this.invoiceItems.get(i).getQuantity());
            sb4.append("</td>");
            sb4.append(str8);
            sb4.append(str9);
            sb4.append("<td class=\"total\">");
            sb4.append(this.businessInfo.getCurrencySymbol());
            sb4.append(Utility.fmt((double) ((invoiceItem.getUnitPrice() * ((float) invoiceItem.getQuantity())) - invoiceItem.getDiscount())));
            sb4.append("</td></tr>");
            str11 = sb4.toString();
            i = i2;
            str10 = str6;
            d4 = d2;
            d5 = d3;
        }
        if ((this.mInvoice.getDiscountPercentage() == 0.0f && this.mInvoice.getDiscount() == 0.0f) || this.mInvoice.getDiscountType().equals(InvoiceItem.NO_DISCOUNT)) {
            str = "</td></tr>";
            str2 = str10;
        } else if (this.mInvoice.getDiscountType().equals(InvoiceItem.PERCENTAGE)) {
            double discountPercentage = (double) (this.mInvoice.getDiscountPercentage() / 100.0f);
            Double.isNaN(discountPercentage);
            d = discountPercentage * d4;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("<tr><td colspan=\"6\">DISCOUNT (");
            sb5.append(Utility.fmt((double) this.mInvoice.getDiscountPercentage()));
            sb5.append("%)</td><td class=\"total\">- ");
            sb5.append(this.businessInfo.getCurrencySymbol());
            sb5.append(Utility.fmt(d));
            str = "</td></tr>";
            sb5.append(str);
            str2 = sb5.toString();
            double d8 = d4 - d;
            if (this.mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_THE_TOTAL)) {
                str3 = str11;
                if (!this.mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_DEDUCTED)) {
                    if (this.mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM)) {
                        d8 += d6;
                        str4 = "<tr><td colspan=\"6\">" + this.mInvoice.getTaxLabel().toUpperCase() + " (" + Utility.fmt(d5) + "%)</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d6) + str;
                        double d9 = 0.0d;
                        if (d6 <= 0.0d) {
                            str4 = str10;
                        }
                        if (this.mInvoice.getPayment() > 0.0f) {
                            str5 = "<tr><td colspan=\"6\">PAYMENTS</td><td class=\"total\">-" + this.businessInfo.getCurrencySymbol() + Utility.fmt((double) this.mInvoice.getPayment()) + str;
                        } else {
                            str5 = str10;
                        }
                        double payment = (double) this.mInvoice.getPayment();
                        Double.isNaN(payment);
                        double d10 = d8 - payment;
                        if (this.mInvoice.getInvoiceStatus().equals(Invoice.INVOICE_STATUS_ISSUED)) {
                            str5 = "<tr><td colspan=\"6\">PAYMENTS</td><td class=\"total\">-" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str;
                        } else {
                            d9 = d10;
                        }
                        return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d9) + str;
                    }
                    str4 = str10;
                    double d92 = 0.0d;
                    if (d6 <= 0.0d) {
                    }
                    if (this.mInvoice.getPayment() > 0.0f) {
                    }
                    double payment2 = (double) this.mInvoice.getPayment();
                    Double.isNaN(payment2);
                    double d102 = d8 - payment2;
                    if (this.mInvoice.getInvoiceStatus().equals(Invoice.INVOICE_STATUS_ISSUED)) {
                    }
                    return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d92) + str;
                }
            } else {
                str3 = str11;
            }
            if (this.mInvoice.getTaxPercentage() != 0.0f) {
                double taxPercentage = (double) this.mInvoice.getTaxPercentage();
                Double.isNaN(taxPercentage);
                d6 = (taxPercentage / 100.0d) * d4;
                String str12 = "<tr><td colspan=\"6\">" + this.mInvoice.getTaxLabel().toUpperCase() + " (" + Utility.fmt(taxPercentage) + "%)</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d6) + str;
                if (this.mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_DEDUCTED)) {
                    str4 = "<tr><td colspan=\"6\">" + this.mInvoice.getTaxLabel().toUpperCase() + " (" + Utility.fmt(taxPercentage) + "%)</td><td class=\"total\">- " + this.businessInfo.getCurrencySymbol() + Utility.fmt(d6) + str;
                    d8 -= d6;
                } else {
                    d8 += d6;
                    str4 = str12;
                }
                double d922 = 0.0d;
                if (d6 <= 0.0d) {
                }
                if (this.mInvoice.getPayment() > 0.0f) {
                }
                double payment22 = (double) this.mInvoice.getPayment();
                Double.isNaN(payment22);
                double d1022 = d8 - payment22;
                if (this.mInvoice.getInvoiceStatus().equals(Invoice.INVOICE_STATUS_ISSUED)) {
                }
                return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d922) + str;
            }
            str4 = str10;
            double d9222 = 0.0d;
            if (d6 <= 0.0d) {
            }
            if (this.mInvoice.getPayment() > 0.0f) {
            }
            double payment222 = (double) this.mInvoice.getPayment();
            Double.isNaN(payment222);
            double d10222 = d8 - payment222;
            if (this.mInvoice.getInvoiceStatus().equals(Invoice.INVOICE_STATUS_ISSUED)) {
            }
            return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d9222) + str;
        } else {
            str = "</td></tr>";
            str2 = "<tr><td colspan=\"6\">DISCOUNT</td><td class=\"total\"> -" + this.businessInfo.getCurrencySymbol() + Utility.fmt((double) this.mInvoice.getDiscount()) + str;
        }
        d = 0.0d;
        double d82 = d4 - d;
        if (this.mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_THE_TOTAL)) {
        }
        if (this.mInvoice.getTaxPercentage() != 0.0f) {
        }
        str4 = str10;
        double d92222 = 0.0d;
        if (d6 <= 0.0d) {
        }
        if (this.mInvoice.getPayment() > 0.0f) {
        }
        double payment2222 = (double) this.mInvoice.getPayment();
        Double.isNaN(payment2222);
        double d102222 = d82 - payment2222;
        if (this.mInvoice.getInvoiceStatus().equals(Invoice.INVOICE_STATUS_ISSUED)) {
        }
        return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d82) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + this.businessInfo.getCurrencySymbol() + Utility.fmt(d92222) + str;
    }


    /*private String getInvoiceItems() {
        String str;
        double d;
        String str2;
        String str3 = null;
        String str4;
        String str5 = null;
        String str6;
        double d2;
        String str7;
        String str8;
        double d3;
        String str9;
        invoiceItems.clear();
        String str10 = "";
        String sb = mInvoice.getInvoiceId() + str10;
        invoiceItems = invoiceDatabaseHelper.getAllOrderInvoicesItems(sb);
        int i = 0;
        StringBuilder str11 = new StringBuilder(str10);
        double d4 = 0.0d;
        double d5 = 0.0d;
        double d6 = 0.0d;
        while (i < invoiceItems.size()) {
            InvoiceItem invoiceItem = invoiceItems.get(i);
            double quantity = (double) ((((float) invoiceItems.get(i).getQuantity()) * invoiceItems.get(i).getUnitPrice()) - invoiceItems.get(i).getDiscount());
            Double.isNaN(quantity);
            double d7 = d4 + quantity;
            if (!mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM) || invoiceItems.get(i).getTaxable() != 1) {
                str6 = str10;
            } else {
                str6 = str10;
                double vat = (double) invoiceItems.get(i).getVat();
                Double.isNaN(vat);
                d5 += vat;
                double vatRate = (double) invoiceItems.get(i).getVatRate();
                Double.isNaN(vatRate);
                d6 += vatRate;
            }
            if (invoiceItem.getDiscount() != 0.0f) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("<td class=\"qty\">- ");
                str7 = "<td class=\"qty\"></td>";
                sb2.append(businessInfo.getCurrencySymbol());
                d2 = d7;
                sb2.append(Utility.fmt((double) invoiceItem.getDiscount()));
                sb2.append("</td>");
                str8 = sb2.toString();
                if (invoiceItem.getDiscountType().equals(InvoiceItem.PERCENTAGE)) {
                    str8 = "<td class=\"qty\">- " + businessInfo.getCurrencySymbol() + Utility.fmt((double) invoiceItem.getDiscount()) + "<br/><small>" + Utility.fmt((double) invoiceItem.getDiscountPercentage()) + "%</small></td>";
                }
            } else {
                str7 = "<td class=\"qty\"></td>";
                d2 = d7;
                str8 = str7;
            }
            if (!mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM) || invoiceItem.getTaxable() != 1) {
                d3 = d5;
                str9 = str7;
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("<td class=\"qty\">");
                sb3.append(businessInfo.getCurrencySymbol());
                d3 = d5;
                sb3.append(Utility.fmt((double) invoiceItem.getVatRate()));
                sb3.append("<br/>");
                sb3.append(Utility.fmt((double) invoiceItem.getVat()));
                sb3.append("%</td>");
                str9 = sb3.toString();
            }
            int i2 = i + 1;
            i = i2;
            str10 = str6;
            d4 = d2;
            d5 = d3;
            str11.append("<tr><td>").append(i2).append("</td><td class=\"desc\">").append(invoiceItems.get(i).getInvoiceName()).append("<br/>").append(invoiceItems.get(i).getDetail()).append("</td><td class=\"unit\">").append(businessInfo.getCurrencySymbol()).append(Utility.fmt((double) invoiceItem.getUnitPrice())).append("</td><td class=\"qty\">").append(invoiceItems.get(i).getQuantity()).append("</td>").append(str8).append(str9).append("<td class=\"total\">").append(businessInfo.getCurrencySymbol()).append(Utility.fmt((double) ((invoiceItem.getUnitPrice() * ((float) invoiceItem.getQuantity())) - invoiceItem.getDiscount()))).append("</td></tr>");
        }
        if ((mInvoice.getDiscountPercentage() == 0.0f && mInvoice.getDiscount() == 0.0f) || mInvoice.getDiscountType().equals(InvoiceItem.NO_DISCOUNT)) {
            str = "</td></tr>";
            str2 = str10;
        } else if (mInvoice.getDiscountType().equals(InvoiceItem.PERCENTAGE)) {
            double discountPercentage = (double) (mInvoice.getDiscountPercentage() / 100.0f);
            Double.isNaN(discountPercentage);
            d = discountPercentage * d4;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("<tr><td colspan=\"6\">DISCOUNT (");
            sb5.append(Utility.fmt((double) mInvoice.getDiscountPercentage()));
            sb5.append("%)</td><td class=\"total\">- ");
            sb5.append(businessInfo.getCurrencySymbol());
            sb5.append(Utility.fmt(d));
            str = "</td></tr>";
            sb5.append(str);
            str2 = sb5.toString();
            double d8 = d4 - d;
            if (mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_THE_TOTAL)) {
                str3 = str11.toString();
                if (!mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_DEDUCTED)) {
                    if (mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_ON_PER_ITEM)) {
                        d8 += d6;
                        str4 = "<tr><td colspan=\"6\">" + mInvoice.getTaxLabel().toUpperCase() + " (" + Utility.fmt(d5) + "%)</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d6) + str;
                        double d9 = 0.0d;
                        if (d6 <= 0.0d) {
                            str4 = str10;
                        }
                        if (mInvoice.getPayment() > 0.0f) {
                            str5 = "<tr><td colspan=\"6\">PAYMENTS</td><td class=\"total\">-" + businessInfo.getCurrencySymbol() + Utility.fmt((double) mInvoice.getPayment()) + str;
                        } else {
                            str5 = str10;
                        }
                        double payment = (double) mInvoice.getPayment();
                        Double.isNaN(payment);
                        double d10 = d8 - payment;
                        if (mInvoice.getInvoiceStatus().equals(Invoice.INVOICE_STATUS_ISSUED)) {
                            str5 = "<tr><td colspan=\"6\">PAYMENTS</td><td class=\"total\">-" + businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str;
                        } else {
                            d9 = d10;
                        }
                        return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d9) + str;
                    }
                    str4 = str10;
                    double d92 = 0.0d;
                    double payment2 = (double) mInvoice.getPayment();
                    Double.isNaN(payment2);
                    return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d92) + str;
                }
            } else {
                str3 = str11.toString();
            }
            if (mInvoice.getTaxPercentage() != 0.0f) {
                double taxPercentage = (double) mInvoice.getTaxPercentage();
                Double.isNaN(taxPercentage);
                d6 = (taxPercentage / 100.0d) * d4;
                String str12 = "<tr><td colspan=\"6\">" + mInvoice.getTaxLabel().toUpperCase() + " (" + Utility.fmt(taxPercentage) + "%)</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d6) + str;
                if (mInvoice.getTaxType().equals(InvoiceHelper.TAX_TYPE_DEDUCTED)) {
                    str4 = "<tr><td colspan=\"6\">" + mInvoice.getTaxLabel().toUpperCase() + " (" + Utility.fmt(taxPercentage) + "%)</td><td class=\"total\">- " + businessInfo.getCurrencySymbol() + Utility.fmt(d6) + str;
                    d8 -= d6;
                } else {
                    d8 += d6;
                    str4 = str12;
                }
                double d922 = 0.0d;
                double payment22 = (double) mInvoice.getPayment();
                Double.isNaN(payment22);
                return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d922) + str;
            }
            str4 = str10;
            double d9222 = 0.0d;
            double payment222 = (double) mInvoice.getPayment();
            Double.isNaN(payment222);
            return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d8) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d9222) + str;
        } else {
            str = "</td></tr>";
            str2 = "<tr><td colspan=\"6\">DISCOUNT</td><td class=\"total\"> -" + businessInfo.getCurrencySymbol() + Utility.fmt((double) mInvoice.getDiscount()) + str;
        }
        d = 0.0d;
        double d82 = d4 - d;
        str4 = str10;
        double d92222 = 0.0d;
        double payment2222 = (double) mInvoice.getPayment();
        Double.isNaN(payment2222);
        return str3 + "<tr><td colspan=\"6\">SUBTOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d4) + str + str2 + str4 + "<tr><td colspan=\"6\">TOTAL</td><td class=\"total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d82) + str + str5 + "<tr><td colspan=\"6\" class=\"grand total\">BALANCE DUE</td><td class=\"grand total\">" + businessInfo.getCurrencySymbol() + Utility.fmt(d92222) + str;
    }*/

    private String getClientDetail() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8 = "";
        if (!client.getClientName().isEmpty()) {
            str = "<b>" + client.getClientName() + "</b></br>";
        } else {
            str = str8;
        }
        if (!client.getAddress1().isEmpty()) {
            str2 = client.getAddress1() + "</br>";
        } else {
            str2 = str8;
        }
        if (!client.getAddress2().isEmpty()) {
            str3 = client.getAddress2() + "</br>";
        } else {
            str3 = str8;
        }
        if (!client.getAddress3().isEmpty()) {
            str4 = client.getAddress3() + "</br>";
        } else {
            str4 = str8;
        }
        if (!client.getClientEmail().isEmpty()) {
            str5 = "<a href=\"mailto:" + client.getClientEmail() + "\">" + client.getClientEmail() + "</a></br>";
        } else {
            str5 = str8;
        }
        if (!client.getClientMobile().isEmpty()) {
            str6 = "<div><span>Mobile</span>" + client.getClientMobile() + "</br>";
        } else {
            str6 = str8;
        }
        if (!client.getClientPhone().isEmpty()) {
            str7 = client.getClientPhone() + "</br>";
        } else {
            str7 = str8;
        }
        if (!client.getClientFax().isEmpty()) {
            str8 = client.getClientFax() + "</br>";
        }
        return " <div id=\"column\" class=\"clearfix\" style=\"text-align: right;\"><h3>" + str + str2 + str3 + str4 + str5 + str6 + str7 + str8 + " </h3></div>";
    }

    private String getCompanyDetail() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9 = "";
        if (!businessInfo.getName().isEmpty()) {
            str = businessInfo.getOwnerName() + "</br>";
        } else {
            str = str9;
        }
        if (!businessInfo.getName().isEmpty()) {
            str2 = "<b>" + businessInfo.getName() + "</b></br>";
        } else {
            str2 = str9;
        }
        if (!businessInfo.getAddress1().isEmpty()) {
            str3 = businessInfo.getAddress1() + "</br>";
        } else {
            str3 = str9;
        }
        if (!businessInfo.getAddress2().isEmpty()) {
            str4 = businessInfo.getAddress2() + "</br>";
        } else {
            str4 = str9;
        }
        if (!businessInfo.getAddress3().isEmpty()) {
            str5 = businessInfo.getAddress3() + "</br>";
        } else {
            str5 = str9;
        }
        if (!businessInfo.getPhone().isEmpty()) {
            str6 = businessInfo.getPhone() + "</br>";
        } else {
            str6 = str9;
        }
        if (!businessInfo.getMobile().isEmpty()) {
            str7 = businessInfo.getMobile() + "</br>";
        } else {
            str7 = str9;
        }
        if (!businessInfo.getEmail().isEmpty()) {
            str8 = "<strong><a href=\"mailto:" + businessInfo.getEmail() + "\">" + businessInfo.getEmail() + "</a></strong></br>";
        } else {
            str8 = str9;
        }
        if (!businessInfo.getWebsite().isEmpty()) {
            str9 = businessInfo.getWebsite() + "</br>";
        }
        return "<div id=\"column\"><h3>" + str2 + str + str3 + str4 + str5 + str6 + str7 + str8 + str9 + " </h3> </div>";
    }


    public String getCompanyLogo(Context context) {
        if (mInvoice.getBusinessInfo().getLogoPath() == null || mInvoice.getBusinessInfo().getLogoPath().isEmpty()) {
            return "";
        }
        String convertToBase64String = Utility.convertToBase64String(context, Uri.parse(mInvoice.getBusinessInfo().getLogoPath()));
        return " <div class=\"column\" ><div id=\"logo\"><img src=\"data:image/png;base64, " + convertToBase64String + "\" alt=\"company_logo\" /></div></div>";
    }
}
