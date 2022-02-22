package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActInvoiceNewClient;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVAdapter.IVClientListAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Client;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.CoroutinesTask;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;

public class FrgInvoiceClients extends Fragment implements View.OnClickListener {
    public List<Client> clientList;
    public IVClientListAdp adapter;
    public RecyclerView recyclerView;
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public ProgressBar progressBar;
    public View root;
    public int spanCount = 1;

    public static FrgInvoiceClients getInstance() {
        return new FrgInvoiceClients();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        invoiceDatabaseHelper = new InvoiceDatabaseHelper(getActivity());
        clientList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (root == null) {
            root = layoutInflater.inflate(R.layout.fragment_invoice_client, viewGroup, false);

            progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
            recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            getClient();

            root.findViewById(R.id.addNewClient).setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), ActInvoiceNewClient.class);
                intent.putExtra(ScreenCVEdit.FIELD_ID, "0");
                startActivity(intent);
            });
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Singleton.getInstance().isInvoiceDataUpdated()) {
            Singleton.getInstance().setInvoiceDataUpdated(false);
            getClient();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        return false;
    }

    private void getClient() {
        clientList.clear();
        progressBar.setVisibility(View.VISIBLE);
        new CoroutinesTask(obj -> requireActivity().runOnUiThread(() -> {
            ((Integer) obj).intValue();
            progressBar.setVisibility(View.GONE);
            adapter = new IVClientListAdp(getActivity(), clientList);
            recyclerView.setAdapter(adapter);
        })).loadClient(this);
    }

    public int loadClient() {
        clientList = invoiceDatabaseHelper.getAllClient();
        return clientList.size();
    }

    public void onClick(View view) {
        view.getId();
    }
}
