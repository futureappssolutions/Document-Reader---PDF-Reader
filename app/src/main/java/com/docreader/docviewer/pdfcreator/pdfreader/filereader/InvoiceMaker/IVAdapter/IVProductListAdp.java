package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActInvoiceProduct;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Product;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;

public class IVProductListAdp extends RecyclerView.Adapter<IVProductListAdp.MyViewHolder> implements Filterable {
    public List<Product> fileFilteredList;
    public List<Product> productsItems;
    public String currencySymbol;
    public Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView dataLayout;
        public ImageView optionMenu;
        public TextView priceTv;
        public TextView productNameTv;

        public MyViewHolder(View view) {
            super(view);
            productNameTv = (TextView) view.findViewById(R.id.productNameTv);
            priceTv = (TextView) view.findViewById(R.id.priceTv);
            optionMenu = (ImageView) view.findViewById(R.id.optionMenu);
            dataLayout = (CardView) view.findViewById(R.id.dataLayout);
        }
    }

    public IVProductListAdp(Context context, List<Product> list) {
        mContext = context;
        productsItems = list;
        fileFilteredList = list;
        currencySymbol = Singleton.getInstance().getBusinessInfo(context).getCurrencySymbol();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_list, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final Product product = fileFilteredList.get(i);

        myViewHolder.productNameTv.setText(product.getProductName());
        myViewHolder.priceTv.setText(currencySymbol + " " + product.getProductUnitCost());

        myViewHolder.dataLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActInvoiceProduct.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, product.getProductId() + "");
            mContext.startActivity(intent);
        });

        myViewHolder.dataLayout.setOnLongClickListener(view -> {
            openBottomSheet(product);
            return false;
        });

        myViewHolder.optionMenu.setOnClickListener(view -> {
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
                    ArrayList<Product> arrayList = new ArrayList<>();
                    for (Product product : productsItems) {
                        if (product.getProductName().toUpperCase().contains(upperCase)) {
                            arrayList.add(product);
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
    public void openBottomSheet(final Product product) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_dialog, (ViewGroup) null);

        ((TextView) inflate.findViewById(R.id.titleTv)).setText(product.getProductName());
        ((TextView) inflate.findViewById(R.id.detailTv)).setText(product.getProductDescription());

        inflate.findViewById(R.id.cancelTv).setOnClickListener(view -> bottomSheetDialog.dismiss());

        inflate.findViewById(R.id.deleteTv).setOnClickListener(view -> {
            productsItems.remove(product);
            fileFilteredList.remove(product);
            InvoiceDatabaseHelper invoiceDatabaseHelper = new InvoiceDatabaseHelper(mContext);
            invoiceDatabaseHelper.removeProductItem(product.getProductId() + "");
            notifyDataSetChanged();
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.editTv).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActInvoiceProduct.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, product.getProductId() + "");
            mContext.startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.addNewTv).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActInvoiceProduct.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, "0");
            mContext.startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(inflate);
        bottomSheetDialog.show();
    }
}
