package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.content.Context;
import android.net.Uri;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.CvsTask;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.OnTaskComplete;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment.FrgInvoiceClients;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment.FrgInvoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment.FrgInvoiceProduct;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public final class CoroutinesTask {
    private Context context;
    private OnTaskComplete onTaskComplete;

    public final OnTaskComplete getOnTaskComplete() {
        return this.onTaskComplete;
    }

    public final void setOnTaskComplete(OnTaskComplete onTaskComplete2) {
        this.onTaskComplete = onTaskComplete2;
    }

    public final Context getContext() {
        return this.context;
    }

    public final void setContext(Context context2) {
        this.context = context2;
    }

    public CoroutinesTask(OnTaskComplete onTaskComplete2) {
        this.onTaskComplete = onTaskComplete2;
    }

    public final void convertToPDF(Context context2, List<?> list, String str, Uri uri) {
        String str1;
        if (str == null) {
            str1 = new PdfUtility().imagesToPdfFile(context2, list, uri);
        } else {
            str1 = new PdfUtility().imagesToPdfFile(context2, list, str);
        }
        OnTaskComplete onTaskComplete = getOnTaskComplete();
        Intrinsics.checkNotNull(onTaskComplete);
        onTaskComplete.onComplete(str1);
    }

    public final void loadInvoices(FrgInvoice fragmentInvoice) {
        int loadInvoice = fragmentInvoice.loadInvoice();
        OnTaskComplete onTaskComplete = getOnTaskComplete();
        onTaskComplete.onComplete(loadInvoice);
    }

    public final void loadClient(FrgInvoiceClients fragmentInvoiceClients) {
        int loadClient = fragmentInvoiceClients.loadClient();
        OnTaskComplete onTaskComplete = getOnTaskComplete();
        onTaskComplete.onComplete(loadClient);
    }

    public final void loadProduct(FrgInvoiceProduct fragmentInvoiceProduct) {
        int loadProduct = fragmentInvoiceProduct.loadProduct();
        OnTaskComplete onTaskComplete = getOnTaskComplete();
        onTaskComplete.onComplete(loadProduct);
    }

    public final void csvToPDF(Context context2, ArrayList<List<String>> arrayList, Uri uri, CvsTask cvsTask) {
        String csvToPDF = Utility.csvToPDF(context2, arrayList, uri, cvsTask);
        OnTaskComplete onTaskComplete = getOnTaskComplete();
        onTaskComplete.onComplete(csvToPDF);
    }
}
