package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActInvoiceItem;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.InvoiceItem;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.res.ResConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.List;

public class IVItemsListAdp extends RecyclerView.Adapter<IVItemsListAdp.MyViewHolder> {
    public List<InvoiceItem> productsItems;
    public BusinessInfo businessInfo;
    public Context mContext;
    public int invoiceId;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout dataLayout;
        public LinearLayout discountLayout;
        public TextView discountTitleTv;
        public TextView discountTv;
        public TextView invoiceNameTv;
        public TextView priceTv;

        public MyViewHolder(View view) {
            super(view);
            invoiceNameTv = view.findViewById(R.id.invoiceNameTv);
            priceTv = view.findViewById(R.id.priceTv);
            dataLayout = view.findViewById(R.id.dataLayout);
            discountLayout = view.findViewById(R.id.discountLayout);
            discountTitleTv = view.findViewById(R.id.discountTitleTv);
            discountTv = view.findViewById(R.id.discountTv);
        }
    }

    public IVItemsListAdp(Context context, List<InvoiceItem> list, int i) {
        mContext = context;
        productsItems = list;
        businessInfo = Singleton.getInstance().getBusinessInfo(context);
        invoiceId = i;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice_list, viewGroup, false));
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        final InvoiceItem invoiceItem = productsItems.get(i);

        myViewHolder.invoiceNameTv.setText(invoiceItem.getInvoiceName());
        myViewHolder.priceTv.setText(invoiceItem.getQuantity() + " x " + businessInfo.getCurrencySymbol() + invoiceItem.getUnitPrice() + "\n" + businessInfo.getCurrencySymbol() + (((float) invoiceItem.getQuantity()) * invoiceItem.getUnitPrice()));
        myViewHolder.discountLayout.setVisibility(View.GONE);

        if (invoiceItem.getDiscount() != 0.0f) {
            myViewHolder.discountLayout.setVisibility(View.VISIBLE);
            myViewHolder.discountTitleTv.setText("Discount");
            myViewHolder.discountTv.setText("-" + businessInfo.getCurrencySymbol() + invoiceItem.getDiscount());
            if (invoiceItem.getDiscountType().equals(InvoiceItem.PERCENTAGE)) {
                myViewHolder.discountTitleTv.setText("Discount (% " + invoiceItem.getDiscountPercentage() + ")");
            }
        }

        myViewHolder.dataLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActInvoiceItem.class);
            intent.putExtra("invoiceItemId", invoiceItem.getInvoiceItemId() + "");
            intent.putExtra("invoiceId", invoiceId + "");
            mContext.startActivity(intent);
        });

        myViewHolder.dataLayout.setOnLongClickListener(view -> {
            if (!invoiceItem.getInvoiceName().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure to to delete '" + invoiceItem.getInvoiceName() + "' ?").setPositiveButton(ResConstant.BUTTON_CANCEL, (dialogInterface, i1) -> dialogInterface.dismiss()).setNegativeButton("Delete", (dialogInterface, i1) -> {
                    InvoiceDatabaseHelper invoiceDatabaseHelper = new InvoiceDatabaseHelper(mContext);
                    invoiceDatabaseHelper.deleteInvoiceItem(invoiceItem.getInvoiceItemId() + "");
                    Utility.logCatMsg("delete time Position " + i1);
                    productsItems.remove(i1);
                    notifyDataSetChanged();
                    dialogInterface.dismiss();
                }).setCancelable(false).create().show();
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return productsItems.size();
    }
}
