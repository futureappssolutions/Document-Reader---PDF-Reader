package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.PDFFileSelectionAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectPdfFile extends BaseActivity {
    private final ArrayList<String> mSelectedFilesPathList = new ArrayList<>();
    private int imageSelectionCounter = 0;
    private List<Object> fileList;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView toolBarTitle;
    private ActionBar actionBar;
    private MenuItem action_ok;
    private PDFFileSelectionAdp adapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_file_list);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        toolBarTitle = findViewById(R.id.toolBarTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler_view);
        findViewById(R.id.guideTextView).setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            changeBackGroundColor(3);
            toolBarTitle.setText(getResources().getString(R.string.selectPdfFileToMerge));
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getFilesData();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBackPressed() {
        if (imageSelectionCounter != 0) {
            for (int i = 0; i < fileList.size(); i++) {
                if ((fileList.get(i) instanceof FileModel) && ((FileModel) fileList.get(i)).isSelected()) {
                    ((FileModel) fileList.get(i)).setSelected(false);
                    mSelectedFilesPathList.remove(((FileModel) fileList.get(i)).getPath());
                }
            }
            adapter.notifyDataSetChanged();
            imageSelectionCounter = 0;
            toolBarTitle.setText(getResources().getString(R.string.selectPdfFileToMerge));
            action_ok.setVisible(false);
            actionBar.setHomeAsUpIndicator(null);
            return;
        }

        setResult(0, new Intent());
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_pdf_files, menu);
        action_ok = menu.findItem(R.id.action_ok);
        return true;
    }

    @SuppressLint("SetTextI18n")
    public void onItemClicked(FileModel fileModel, int i) {
        if (fileModel.isSelected()) {
            imageSelectionCounter++;
            mSelectedFilesPathList.add(fileModel.getPath());
        } else {
            imageSelectionCounter--;
            mSelectedFilesPathList.remove(fileModel.getPath());
        }
        if (imageSelectionCounter < 0) {
            imageSelectionCounter = 0;
        }
        if (imageSelectionCounter == 0) {
            toolBarTitle.setText(getResources().getString(R.string.selectPdfFileToMerge));
            action_ok.setVisible(false);
            actionBar.setHomeAsUpIndicator(null);
            return;
        }
        action_ok.setVisible(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        toolBarTitle.setText(imageSelectionCounter + getResources().getString(R.string.items));
    }

    private void getFilesData() {
        fileList = new ArrayList<>();
        ArrayList<Object> allDocumentFileList = Singleton.getInstance().getAllDocumentFileList();
        for (int i = 0; i < allDocumentFileList.size(); i++) {
            if (allDocumentFileList.get(i) instanceof FileModel) {
                ((FileModel) allDocumentFileList.get(i)).setSelected(false);
                if (((FileModel) allDocumentFileList.get(i)).getFileType() == 3) {
                    fileList.add(allDocumentFileList.get(i));
                }
            }
        }
        if (fileList.size() == 0) {
            findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
        }
        PDFFileSelectionAdp pDF_FileSelectionAdapter = new PDFFileSelectionAdp(this, fileList, this);
        adapter = pDF_FileSelectionAdapter;
        recyclerView.setAdapter(pDF_FileSelectionAdapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.action_ok) {
            if (mSelectedFilesPathList.size() > 0) {
                Intent intent = new Intent();
                intent.putExtra("result", mSelectedFilesPathList);
                setResult(-1, intent);
                finish();
            } else {
                Utility.Toast(this, getResources().getString(R.string.selectFileFirst));
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
