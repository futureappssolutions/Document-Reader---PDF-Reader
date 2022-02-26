package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.InvoiceDatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActNewInvoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVAdapter.IVListAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.BusinessInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.Invoice;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.CoroutinesTask;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;

public class FrgInvoice extends Fragment implements View.OnClickListener {
    public List<Invoice> invoicesItems;
    public RecyclerView recyclerView;
    public IVListAdp adapter;
    public InvoiceDatabaseHelper invoiceDatabaseHelper;
    public TextView totalDuesAmountTv;
    public BusinessInfo businessInfo;
    public ProgressBar progressBar;
    public SharedPrefs prefs;
    public Singleton singleton;
    public View root;
    public int spanCount = 1;
    public float totalDuesAmount = 0.0f;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        prefs = new SharedPrefs(requireActivity());
        singleton = Singleton.getInstance();
        invoiceDatabaseHelper = new InvoiceDatabaseHelper(getActivity());
        invoicesItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (root == null) {
            root = layoutInflater.inflate(R.layout.fragment_invoice, viewGroup, false);

            progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
            recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            totalDuesAmountTv = (TextView) root.findViewById(R.id.totalDuesAmountTv);

            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            root.findViewById(R.id.createNewInvoice).setOnClickListener(view -> {
                if (GoogleAppLovinAds.adsdisplay) {
                    GoogleAppLovinAds.showFullAds(requireActivity(), () -> {
                        GoogleAppLovinAds.allcount60.start();
                        IntentInvoice();
                    });
                } else {
                    IntentInvoice();
                }
            });
        }
        return root;
    }

    private void IntentInvoice() {
        Intent intent = new Intent(getActivity(), ActNewInvoice.class);
        intent.putExtra(ScreenCVEdit.FIELD_ID, "0");
        requireActivity().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Singleton.getInstance().setInvoiceDataUpdated(false);
        getInvoice();
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

    public void onClick(View view) {
        view.getId();
    }

    @SuppressLint("SetTextI18n")
    private void getInvoice() {
        invoicesItems.clear();
        progressBar.setVisibility(View.VISIBLE);
        new CoroutinesTask(obj -> requireActivity().runOnUiThread(() -> {
            ((Integer) obj).intValue();
            progressBar.setVisibility(View.GONE);
            adapter = new IVListAdp(getActivity(), invoicesItems);
            recyclerView.setAdapter(adapter);
            totalDuesAmountTv.setText(businessInfo.getCurrencySymbol() + " " + totalDuesAmount);
        })).loadInvoices(this);
    }

    public int loadInvoice() {
        businessInfo = singleton.getBusinessInfo(getActivity());
        totalDuesAmount = invoiceDatabaseHelper.getTotalInvoiceDuesAmount();
        invoicesItems = invoiceDatabaseHelper.getAllInvoices();
        return invoicesItems.size();
    }
}
