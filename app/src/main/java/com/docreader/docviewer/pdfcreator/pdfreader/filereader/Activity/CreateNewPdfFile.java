package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.CreatedDocumentFilesListAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.facebookMaster;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.DatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.DocumentFiles;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CreateNewPdfFile extends BaseActivity {
    public static String selectedFileSource = "";
    public int selectedFileSourceId = 0;
    public int spanCount = 1;
    public List<DocumentFiles> userCreatedFilesList = new ArrayList<>();
    public CreatedDocumentFilesListAdp adapter;
    public DocumentFiles clickedFileModel;
    public DatabaseHelper databaseHelper;
    public ProgressBar progressBar;
    public RecyclerView recyclerView;
    public TextView toolBarTitle;
    public Intent intent;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_create_new_pdf_file);
        changeBackGroundColor(100);

        databaseHelper = new DatabaseHelper(this);
        intent = new Intent(this, PdfCreate.class);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SharedPrefs prefs = new SharedPrefs(CreateNewPdfFile.this);
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(CreateNewPdfFile.this, ll_banner);
                    break;
                case "f":
                    facebookMaster.FbBanner(CreateNewPdfFile.this, ll_banner);
                    break;
                case "both":
                    Advertisement.GoogleBannerBoth(CreateNewPdfFile.this, ll_banner);
                    break;
            }
        }

        toolBarTitle = findViewById(R.id.toolBarTitle);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler_view);

        toolBarTitle.setText(getResources().getString(R.string.createdFile));
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        findViewById(R.id.createNewDocumentBtn).setOnClickListener(view -> {
            if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
                switch (prefs.getAds_name()) {
                    case "g":
                        if (Advertisement.adsdisplay) {
                            Advertisement.FullScreenLoad(CreateNewPdfFile.this, () -> {
                                Advertisement.allcount60.start();
                                CreateNewPdfFile.selectedFileSource = "";
                                selectedFileSourceId = 0;
                                intent.putExtra("sourceFileId", selectedFileSourceId + "");
                                startActivity(intent);
                            });
                        } else {
                            CreateNewPdfFile.selectedFileSource = "";
                            selectedFileSourceId = 0;
                            intent.putExtra("sourceFileId", selectedFileSourceId + "");
                            startActivity(intent);
                        }
                        break;
                    case "f":
                        if (Advertisement.adsdisplay) {
                            facebookMaster.FBFullScreenLoad(CreateNewPdfFile.this, () -> {
                                Advertisement.allcount60.start();
                                CreateNewPdfFile.selectedFileSource = "";
                                selectedFileSourceId = 0;
                                intent.putExtra("sourceFileId", selectedFileSourceId + "");
                                startActivity(intent);
                            });
                        } else {
                            CreateNewPdfFile.selectedFileSource = "";
                            selectedFileSourceId = 0;
                            intent.putExtra("sourceFileId", selectedFileSourceId + "");
                            startActivity(intent);
                        }
                        break;
                    case "both":
                        if (Advertisement.adsdisplay) {
                            Advertisement.FullScreenLoadBoth(CreateNewPdfFile.this, () -> {
                                Advertisement.allcount60.start();
                                CreateNewPdfFile.selectedFileSource = "";
                                selectedFileSourceId = 0;
                                intent.putExtra("sourceFileId", selectedFileSourceId + "");
                                startActivity(intent);
                            });
                        } else {
                            CreateNewPdfFile.selectedFileSource = "";
                            selectedFileSourceId = 0;
                            intent.putExtra("sourceFileId", selectedFileSourceId + "");
                            startActivity(intent);
                        }
                        break;
                }
            } else {
                CreateNewPdfFile.selectedFileSource = "";
                selectedFileSourceId = 0;
                intent.putExtra("sourceFileId", selectedFileSourceId + "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        new loadDataFromSQL().execute();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onOptionClicked(final DocumentFiles documentFiles, final int i) {
        new AlertDialog.Builder(this).setTitle(documentFiles.getFileName()).setItems(new String[]{getResources().getString(R.string.edit), getResources().getString(R.string.rename), getResources().getString(R.string.delete)}, (dialogInterface, i1) -> {
            if (i1 == 0) {
                clickedFileModel = documentFiles;
                CreateNewPdfFile.selectedFileSource = documentFiles.getFileContent();
                selectedFileSourceId = documentFiles.getId();
                intent.putExtra("sourceFileId", selectedFileSourceId + "");
                CreateNewPdfFile activityCreateNewPdfFile = CreateNewPdfFile.this;
                activityCreateNewPdfFile.startActivity(activityCreateNewPdfFile.intent);
            } else if (i1 == 1) {
                renameFileNameAlertDialog(documentFiles);
            } else if (i1 == 2) {
                deleteFile(documentFiles, i);
            }
        }).create().show();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteFile(final DocumentFiles documentFiles, final int i) {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.alert)).setMessage(getResources().getString(R.string.areYouSureToDeleteThisFile)).setPositiveButton(getResources().getString(R.string.delete), (dialogInterface, i1) -> {
            databaseHelper.deleteFile(documentFiles.getId() + "");
            userCreatedFilesList.remove(i);
            adapter.notifyDataSetChanged();
            dialogInterface.dismiss();
        }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i12) -> dialogInterface.dismiss()).create().show();
    }

    public void onItemClicked(DocumentFiles documentFiles, int i) {
        clickedFileModel = documentFiles;
        selectedFileSource = documentFiles.getFileContent();
        selectedFileSourceId = documentFiles.getId();
        Intent intent2 = intent;
        intent2.putExtra("sourceFileId", selectedFileSourceId + "");
        startActivity(intent);
    }

    public String getFileName(int i) {
        String string = getResources().getString(R.string.allFiles);
        if (i == 0) {
            return getResources().getString(R.string.wordFiles);
        }
        if (i == 1) {
            return getResources().getString(R.string.excelFiles);
        }
        if (i == 2) {
            return getResources().getString(R.string.powerPointFiles);
        }
        if (i == 3) {
            return getResources().getString(R.string.pdfFiles);
        }
        if (i == 4) {
            return getResources().getString(R.string.textFiles);
        }
        if (i == 6) {
            return getResources().getString(R.string.codeFile);
        }
        if (i == 100) {
            return getResources().getString(R.string.allFiles);
        }
        switch (i) {
            case 10:
                return getResources().getString(R.string.fileCSV);
            case 11:
                return getResources().getString(R.string.fileHTML);
            case 12:
                return getResources().getString(R.string.favoriteFile);
            case 13:
                return getResources().getString(R.string.rtfFile);
            case 14:
                return getResources().getString(R.string.eBook);
            default:
                return string;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.action_add) {
            selectedFileSource = "";
            selectedFileSourceId = 0;
            Intent intent2 = intent;
            intent2.putExtra("sourceFileId", selectedFileSourceId + "");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressLint({"ResourceType", "NotifyDataSetChanged"})
    public void renameFileNameAlertDialog(final DocumentFiles documentFiles) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_rename_file, null);
        bottomSheetDialog.setContentView(inflate);

        final EditText editText = inflate.findViewById(R.id.editText);
        editText.setText("");
        editText.append(documentFiles.getFileName());

        inflate.findViewById(R.id.setNameBtn).setOnClickListener(view -> {
            if (editText.getText().toString().length() > 0) {
                String obj = editText.getText().toString();
                documentFiles.setFileName(obj);
                databaseHelper.renameFileName(documentFiles.getId() + "", obj);
                adapter.notifyDataSetChanged();
                Utility.Toast(this, getResources().getString(R.string.fileRenamedSuccessfully));
                bottomSheetDialog.dismiss();
                return;
            }
            Utility.Toast(this, getResources().getString(R.string.enterFileNameFirst));
        });

        inflate.findViewById(R.id.btnCancel).setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    private class loadDataFromSQL extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            userCreatedFilesList.clear();
            userCreatedFilesList = databaseHelper.getAllDocumentFiles();
            Collections.reverse(userCreatedFilesList);
            return null;
        }

        @Override
        public void onPostExecute(Void voidR) {
            if (userCreatedFilesList.size() == 0) {
                findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
            }

            adapter = new CreatedDocumentFilesListAdp(CreateNewPdfFile.this, userCreatedFilesList, CreateNewPdfFile.this);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(voidR);
        }
    }
}
