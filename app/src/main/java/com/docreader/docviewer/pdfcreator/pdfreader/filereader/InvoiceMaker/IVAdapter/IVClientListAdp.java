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
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActInvoiceNewClient;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Client;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;

public class IVClientListAdp extends RecyclerView.Adapter<IVClientListAdp.MyViewHolder> implements Filterable {
    private final BusinessInfo businessInfo;
    private final List<Client> clientsList;
    private List<Client> fileFilteredList;
    private final Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView dataLayout;
        public TextView clientNameTv;
        public TextView totalBillTv;
        public TextView numberOfInvoicesTv;
        public ImageView optionMenu;

        public MyViewHolder(View view) {
            super(view);
            clientNameTv = (TextView) view.findViewById(R.id.clientNameTv);
            numberOfInvoicesTv = (TextView) view.findViewById(R.id.numberOfInvoicesTv);
            totalBillTv = (TextView) view.findViewById(R.id.totalBillTv);
            optionMenu = (ImageView) view.findViewById(R.id.optionMenu);
            dataLayout = (CardView) view.findViewById(R.id.dataLayout);
        }
    }

    public IVClientListAdp(Context context, List<Client> list) {
        mContext = context;
        clientsList = list;
        fileFilteredList = list;
        businessInfo = Singleton.getInstance().getBusinessInfo(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_client_list, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final Client client = fileFilteredList.get(i);

        myViewHolder.clientNameTv.setText(client.getClientName());
        myViewHolder.numberOfInvoicesTv.setText(client.getTotalInvoices() + " Invoices");
        myViewHolder.totalBillTv.setText(businessInfo.getCurrencySymbol() + " " + client.getTotalBill());

        myViewHolder.dataLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActInvoiceNewClient.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, client.getClientId() + "");
            mContext.startActivity(intent);
        });

        myViewHolder.dataLayout.setOnLongClickListener(view -> {
            openBottomSheet(client);
            return false;
        });

        myViewHolder.optionMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            popupMenu.getMenu().add("Edit");
            popupMenu.getMenu().add("Remove");
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getTitle().equals("Remove")) {
                    return true;
                }
                menuItem.getTitle().equals("Edit");
                return true;
            });
            popupMenu.show();
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
                    ArrayList<Client> arrayList = new ArrayList<>();
                    for (Client client : clientsList) {
                        if (client.getClientName().toUpperCase().contains(upperCase)) {
                            arrayList.add(client);
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
    public void openBottomSheet(final Client client) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_dialog, (ViewGroup) null);

        ((TextView) inflate.findViewById(R.id.titleTv)).setText(client.getClientName());
        ((TextView) inflate.findViewById(R.id.detailTv)).setText(client.getAddress1());

        inflate.findViewById(R.id.cancelTv).setOnClickListener(view -> bottomSheetDialog.dismiss());

        inflate.findViewById(R.id.deleteTv).setOnClickListener(view -> {
            clientsList.remove(client);
            fileFilteredList.remove(client);
            InvoiceDatabaseHelper invoiceDatabaseHelper = new InvoiceDatabaseHelper(mContext);
            invoiceDatabaseHelper.removeClient(client.getClientId() + "");
            notifyDataSetChanged();
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.editTv).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActInvoiceNewClient.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, client.getClientId() + "");
            mContext.startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.addNewTv).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActInvoiceNewClient.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, "0");
            mContext.startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(inflate);
        bottomSheetDialog.show();
    }
}
