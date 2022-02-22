package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

public class FrgInvoiceTutorial extends Fragment {
    public View root;

    public static FrgInvoiceTutorial getInstance() {
        return new FrgInvoiceTutorial();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.root == null) {
            root = layoutInflater.inflate(R.layout.fragment_invoice_tutorial, viewGroup, false);

            loadInvoiceSampleImages();
        }
        return this.root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        return false;
    }


    public void loadInvoiceSampleImages() {
        Glide.with(requireActivity()).load("https://i.pinimg.com/originals/ad/fe/fe/adfefe9dd6fe4d8a567cbe5b8bbf5d94.png").into((ZoomageView) this.root.findViewById(R.id.invoiceSample1));
    }
}
