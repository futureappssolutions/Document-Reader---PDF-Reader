package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActNewInvoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.res.ResConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class IVListAdp extends RecyclerView.Adapter<IVListAdp.MyViewHolder> implements Filterable {
    public List<Invoice> fileFilteredList;
    public List<Invoice> productsItems;
    public BusinessInfo businessInfo;
    public Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView clientNameTv;
        public CardView dataLayout;
        public TextView dueDateTv;
        public TextView invoiceNumberTv;
        public TextView priceTv;

        public MyViewHolder(View view) {
            super(view);
            clientNameTv = (TextView) view.findViewById(R.id.clientNameTv);
            invoiceNumberTv = (TextView) view.findViewById(R.id.invoiceNumberTv);
            priceTv = (TextView) view.findViewById(R.id.priceTv);
            dueDateTv = (TextView) view.findViewById(R.id.dueDateTv);
            dataLayout = view.findViewById(R.id.dataLayout);
        }
    }

    public IVListAdp(Context context, List<Invoice> list) {
        mContext = context;
        productsItems = list;
        fileFilteredList = list;
        businessInfo = Singleton.getInstance().getBusinessInfo(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice_row, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Invoice invoice = fileFilteredList.get(i);

        if (invoice.getClientInfo() != null) {
            if (invoice.getClientInfo().getClientName().isEmpty()) {
                myViewHolder.clientNameTv.setText("No Client");
            } else {
                myViewHolder.clientNameTv.setText(invoice.getClientInfo().getClientName());
            }
        }

        myViewHolder.invoiceNumberTv.setText(invoice.getInvoiceNumber());
        myViewHolder.priceTv.setText(businessInfo.getCurrencySymbol() + " " + invoice.getBalanceDue());
        myViewHolder.dueDateTv.setText("Due " + Utility.geDateTime(Long.parseLong(invoice.getDueDate()), businessInfo.getDateFormat()));

        myViewHolder.dataLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActNewInvoice.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, invoice.getInvoiceId() + "");
            mContext.startActivity(intent);
        });

        myViewHolder.dataLayout.setOnLongClickListener(view -> {
            openBottomSheet(invoice);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return fileFilteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                String upperCase = charSequence.toString().toUpperCase();
                if (upperCase.isEmpty()) {
                    fileFilteredList = productsItems;
                } else {
                    ArrayList<Invoice> arrayList = new ArrayList<>();
                    for (Invoice invoice : productsItems) {
                        if (invoice.getClientInfo().getClientName().toUpperCase().contains(upperCase)) {
                            arrayList.add(invoice);
                        }
                    }
                    fileFilteredList = arrayList;
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = fileFilteredList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                fileFilteredList = (ArrayList) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @SuppressLint("NotifyDataSetChanged")
    public void openBottomSheet(final Invoice invoice) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_dialog, (ViewGroup) null);

        ((TextView) inflate.findViewById(R.id.titleTv)).setText(invoice.getClientInfo().getClientName());
        ((TextView) inflate.findViewById(R.id.detailTv)).setText(invoice.getInvoiceNumber());

        inflate.findViewById(R.id.cancelTv).setOnClickListener(view -> bottomSheetDialog.dismiss());

        inflate.findViewById(R.id.deleteTv).setOnClickListener(view -> {
            new AlertDialog.Builder(mContext).setMessage("Are you sure to delete this invoice ?").setPositiveButton(ResConstant.BUTTON_CANCEL, (dialogInterface, i) -> dialogInterface.dismiss()).setNegativeButton("Delete", (dialogInterface, i) -> {
                productsItems.remove(invoice);
                fileFilteredList.remove(invoice);
                InvoiceDatabaseHelper invoiceDatabaseHelper = new InvoiceDatabaseHelper(mContext);
                invoiceDatabaseHelper.removeInvoice(invoice.getInvoiceId() + "");
                notifyDataSetChanged();
                dialogInterface.dismiss();
            }).create().show();
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.editTv).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActNewInvoice.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, invoice.getInvoiceId() + "");
            mContext.startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.addNewTv).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActNewInvoice.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, "0");
            mContext.startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(inflate);
        bottomSheetDialog.show();
    }
}
