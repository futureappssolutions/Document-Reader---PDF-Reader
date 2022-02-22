package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.facebookMaster;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Client;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivitySelectClient extends AppCompatActivity {
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public RecyclerView recyclerView;
    public ClientListAdapter adapter;
    public List<Client> clientList;
    public ProgressBar progressBar;
    public Invoice invoice;
    public int invoiceId = 0;
    public int spanCount = 1;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_select_client);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SharedPrefs mPrefs = new SharedPrefs(ActivitySelectClient.this);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(mPrefs.getActive_Weekly().equals("true") || mPrefs.getActive_Monthly().equals("true") || mPrefs.getActive_Yearly().equals("true"))) {
            switch (mPrefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(ActivitySelectClient.this, ll_banner);
                    break;
                case "f":
                    facebookMaster.FbBanner(ActivitySelectClient.this, ll_banner);
                    break;
                case "both":
                    Advertisement.GoogleBannerBoth(ActivitySelectClient.this, ll_banner);
                    break;
            }
        }

        invoiceDatabaseHelper = new InvoiceDatabaseHelper(this);
        clientList = new ArrayList<>();

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new loadClient().execute();

        findViewById(R.id.addNewClient).setOnClickListener(view -> {
            Intent intent = new Intent(ActivitySelectClient.this, ActInvoiceNewClient.class);
            intent.putExtra(ScreenCVEdit.FIELD_ID, "0");
            startActivity(intent);
        });

        if (getIntent() != null) {
            invoiceId = Integer.parseInt(getIntent().getStringExtra("invoiceId"));
            invoice = invoiceDatabaseHelper.getSelectedInvoices(invoiceId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Singleton.getInstance().isInvoiceDataUpdated()) {
            Singleton.getInstance().setInvoiceDataUpdated(false);
            new loadClient().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.searchHere));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                if (searchView.isIconified()) {
                    return false;
                }
                searchView.setIconified(true);
                return false;
            }

            public boolean onQueryTextChange(String str) {
                if (adapter == null) {
                    return false;
                }
                adapter.getFilter().filter(str);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return false;
        }
        onBackPressed();
        return false;
    }

    private class loadClient extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            clientList.clear();
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            clientList = invoiceDatabaseHelper.getAllClient();
            return null;
        }

        @Override
        public void onPostExecute(Void voidR) {
            progressBar.setVisibility(View.GONE);
            adapter = new ClientListAdapter(ActivitySelectClient.this, clientList);
            recyclerView.setAdapter(adapter);
            super.onPostExecute(voidR);
        }
    }

    public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.MyViewHolder> implements Filterable {
        public List<Client> clientsList;
        public List<Client> fileFilteredList;
        public Context mContext;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView clientNameTv;
            public CardView dataLayout;

            public MyViewHolder(View view) {
                super(view);
                clientNameTv = view.findViewById(R.id.clientNameTv);
                dataLayout = view.findViewById(R.id.dataLayout);
            }
        }

        public ClientListAdapter(Context context, List<Client> list) {
            mContext = context;
            clientsList = list;
            fileFilteredList = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_choose_user, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            final Client client = fileFilteredList.get(i);
            myViewHolder.clientNameTv.setText(client.getClientName());

            myViewHolder.dataLayout.setOnClickListener(view -> {
                invoice.setClientInfo(client);
                invoiceDatabaseHelper.updateInvoice(invoice);
                Singleton.getInstance().setInvoiceDataUpdated(true);
                finish();
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
    }
}
