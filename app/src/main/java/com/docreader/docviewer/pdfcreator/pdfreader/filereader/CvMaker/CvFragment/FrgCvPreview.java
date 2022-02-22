package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvTempletAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.ResumeTemp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeFragment;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeTemplate1;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeTemplate2;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeTemplate3;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeTemplate4;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeTemplate5;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeTemplate6;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.ResumeTemplate7;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.ArrayList;

public class FrgCvPreview extends ResumeFragment {
    public boolean isPaidTemplate = false;
    public ArrayList<ResumeTemp> list;
    public RecyclerView rvFilterView;
    public CvTempletAdp mCvTempletAdapter;
    public PrintDocumentAdapter printAdapter;
    public SharedPrefs prefs;
    public PrintJob printJob;
    public Resume resume;
    public WebView webView;

    public static ResumeFragment newInstance(Resume resume2) {
        FrgCvPreview fragmentCvPreview = new FrgCvPreview();
        fragmentCvPreview.setResume(resume2);
        return fragmentCvPreview;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        setStatusBar();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_preview, viewGroup, false);
        setStatusBar();

        prefs = new SharedPrefs(requireActivity());
        resume = getResume();

        rvFilterView = inflate.findViewById(R.id.rvFilterView);
        webView = inflate.findViewById(R.id.webView);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setInitialScale(1);

        list = new ArrayList<>();
        list.add(new ResumeTemp(R.drawable.resume_3, true, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));
        list.add(new ResumeTemp(R.drawable.resume_3, false, false));

        webView.loadDataWithBaseURL(null, new ResumeTemplate7().getContent(getActivity(), resume), "text/html", "utf-8", null);

        mCvTempletAdapter = new CvTempletAdp(i -> {
            String content;
            for (int i2 = 0; i2 < list.size(); i2++) {
                list.get(i2).setSelected(false);
            }
            list.get(i).setSelected(true);
            mCvTempletAdapter.notifyDataSetChanged();
            isPaidTemplate = false;
            if (i == 0) {
                content = new ResumeTemplate1().getContent(getActivity(), resume);
            } else if (i == 1) {
                content = new ResumeTemplate2().getContent(getActivity(), resume);
            } else if (i == 2) {
                content = new ResumeTemplate3().getContent(getActivity(), resume);
            } else if (i == 3) {
                content = new ResumeTemplate4().getContent(getActivity(), resume);
            } else if (i == 4) {
                content = new ResumeTemplate5().getContent(getActivity(), resume);
            } else if (i == 5) {
                content = new ResumeTemplate6().getContent(getActivity(), resume);
            } else {
                content = new ResumeTemplate7().getContent(getActivity(), resume);
            }
            webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        }, getActivity(), list);

        rvFilterView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvFilterView.setAdapter(mCvTempletAdapter);
        return inflate;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.print, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_print) {
            if (prefs.isOpenPdfSaveDialog()) {
                howToSaveInfoDialog();
            } else {
                createWebPrintJob(webView);
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }



    private void howToSaveInfoDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_save_cv, null);
        bottomSheetDialog.setContentView(inflate);

        inflate.findViewById(R.id.gotItBtn).setOnClickListener(view -> {
            createWebPrintJob(webView);
            bottomSheetDialog.dismiss();
        });

//        inflate.findViewById(R.id.btnCancel).setOnClickListener(view -> {
//            createWebPrintJob(webView);
//            bottomSheetDialog.dismiss();
//        });

        ((CheckBox) inflate.findViewById(R.id.neverShowAgainCB)).setOnCheckedChangeListener((compoundButton, z) -> {
            createWebPrintJob(webView);
            prefs.setOpenPdfSaveDialog(z);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
    @SuppressLint({"ObsoleteSdkInt", "WrongConstant"})
    public void createWebPrintJob(WebView webView2) {
        PrintManager printManager = (PrintManager) requireActivity().getSystemService(Context.PRINT_SERVICE);
        if (Build.VERSION.SDK_INT >= 21) {
            printAdapter = webView2.createPrintDocumentAdapter("myCV.pdf");
        } else {
            printAdapter = webView2.createPrintDocumentAdapter();
        }
        printJob = printManager.print(getString(R.string.app_name) + " Document", printAdapter, new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).setResolution(new PrintAttributes.Resolution(ScreenCVEdit.FIELD_ID, "print", 200, 200)).setColorMode(2).setMinMargins(PrintAttributes.Margins.NO_MARGINS).build());
    }

    public void onResume() {
        super.onResume();
        if (printJob != null && printJob.isCompleted()) {
            Utility.Toast(getActivity(), getResources().getString(R.string.resumeCreateSuccessfullyAndCanBeFoundMessage));
            Singleton.getInstance().setFileDeleted(true);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBar() {
        Window window = getActivity().getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.white));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.white));
        } else {
            window.clearFlags(0);
        }
    }
}
