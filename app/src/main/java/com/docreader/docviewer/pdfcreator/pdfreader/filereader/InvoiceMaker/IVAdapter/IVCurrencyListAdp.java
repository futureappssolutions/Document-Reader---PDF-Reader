package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Currency;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;

public class IVCurrencyListAdp extends RecyclerView.Adapter<IVCurrencyListAdp.MyViewHolder> implements Filterable {
    public List<Currency> clientsList;
    public List<Currency> fileFilteredList;
    public Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView currencyCodeTv;
        public TextView currencyNameTv;
        public LinearLayout dataLayout;

        public MyViewHolder(View view) {
            super(view);
            currencyCodeTv = (TextView) view.findViewById(R.id.currencyCodeTv);
            currencyNameTv = (TextView) view.findViewById(R.id.currencyNameTv);
            dataLayout = (LinearLayout) view.findViewById(R.id.dataLayout);
        }
    }

    public IVCurrencyListAdp(Context context, List<Currency> list) {
        mContext = context;
        clientsList = list;
        fileFilteredList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_currency_list, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final Currency currency = fileFilteredList.get(i);

        myViewHolder.currencyCodeTv.setText(currency.getCode() + " (" + currency.getSymbol() + ")");
        myViewHolder.currencyNameTv.setText(currency.getCurrencyName());

        myViewHolder.dataLayout.setOnClickListener(view -> {
            BusinessInfo businessInfo = Singleton.getInstance().getBusinessInfo(mContext);
            businessInfo.setCurrencyCode(currency.getCode());
            businessInfo.setCurrencySymbol(currency.getSymbol());
            Singleton.getInstance().setInvoiceDataUpdated(true);
            ((Activity) mContext).finish();
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
                    fileFilteredList = clientsList;
                } else {
                    ArrayList<Currency> arrayList = new ArrayList<>();
                    for (Currency currency : clientsList) {
                        if (currency.getCurrencyName().toUpperCase().contains(upperCase) || currency.getCode().toUpperCase().contains(upperCase)) {
                            arrayList.add(currency);
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
}
