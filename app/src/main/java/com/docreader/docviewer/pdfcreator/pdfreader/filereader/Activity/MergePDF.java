package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.RearrangePdfFilesAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.MergeFilesListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.MergePdf;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.MergePdfMethodTwo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.DynamicGridView.DynamicGridView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MergePDF extends BaseActivity implements View.OnClickListener, MergeFilesListener {
    private final ArrayList<Object> mPDF_FilesList = new ArrayList<>();
    private final ArrayList<Integer> selectedImage = new ArrayList<>();
    private ArrayList<String> mSelectedImagesPathList = new ArrayList<>();
    private boolean isDeleteSelectionMode = false;
    private ActionBar actionBar;
    private MenuItem action_add;
    private MenuItem action_delete;
    private MenuItem action_ok;
    private LinearLayout btnSelectLayout;
    private DynamicGridView gridView;
    private SharedPrefs prefs;
    private ProgressDialog progressDialog;
    private RelativeLayout choosePdfFileLayout;
    private RearrangePdfFilesAdp rearrangePdfFilesAdapter;
    private RelativeLayout recyclerViewLayout;
    private TextView toolBarTitle;

    ActivityResultLauncher<Intent> mFileResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        Intent data = activityResult.getData();
        if (activityResult.getResultCode() != -1) {
            return;
        }
        if (data != null) {
            ArrayList<Uri> arrayList = new ArrayList<>();
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    arrayList.add(clipData.getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                arrayList.add(data.getData());
            }
            if (arrayList.size() > 0) {
                new AddFileToAdapter(arrayList).execute();
                return;
            }
            return;
        }
        Utility.logCatMsg("File uri not found {}");
    });

    ActivityResultLauncher<Intent> mPdfUriResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult activityResult) {
            Intent data = activityResult.getData();
            if (data != null)
                if (activityResult.getResultCode() == -1 && data.getData() != null) {
                    List<Object> items = rearrangePdfFilesAdapter.getItems();
                    String[] strArr = new String[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        strArr[i] = ((FileModel) items.get(i)).getPath();
                    }
                    new MergePdfMethodTwo(MergePDF.this, data.getData(), MergePDF.this).execute(strArr);
                }
        }
    });

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_merge_pdf);

        init();
        initGridView();
    }

    private void init() {
        prefs = new SharedPrefs(this);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        changeBackGroundColor(100);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAds.showBannerAds(MergePDF.this, ll_banner);

        toolBarTitle = findViewById(R.id.toolBarTitle);
        btnSelectLayout = findViewById(R.id.btnSelectLayout);
        actionBar = getSupportActionBar();
        toolBarTitle.setText(getResources().getString(R.string.mergePdfFiles));
        choosePdfFileLayout = findViewById(R.id.choosePdfFileLayout);
        recyclerViewLayout = findViewById(R.id.recyclerViewLayout);
        choosePdfFileLayout.setOnClickListener(this);

        findViewById(R.id.btnMergePDF).setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.mergingPdfFilePleaseWait));
        progressDialog.setCancelable(false);
    }

    private void initGridView() {
        gridView = findViewById(R.id.dynamic_grid);
        rearrangePdfFilesAdapter = new RearrangePdfFilesAdp(this, mSelectedImagesPathList, 1);
        gridView.setAdapter(rearrangePdfFilesAdapter);
        gridView.setOnItemClickListener((adapterView, view, i, j) -> {
            FileModel fileModel = (FileModel) adapterView.getAdapter().getItem(i);
            if (!gridView.isEditMode() && !isDeleteSelectionMode) {
                showOptionAlertDialog(fileModel);
            } else if (isDeleteSelectionMode) {
                fileModel.setSelected(!fileModel.isSelected());
                rearrangePdfFilesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imge_to_pdf, menu);
        action_add = menu.findItem(R.id.action_add);
        action_delete = menu.findItem(R.id.action_delete);
        action_ok = menu.findItem(R.id.action_ok);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.action_add:
                selectPdfFilesToMerge();
                break;
            case R.id.action_delete:
                selectedImage.clear();
                List<Object> items = rearrangePdfFilesAdapter.getItems();
                for (int i = 0; i < mPDF_FilesList.size(); i++) {
                    FileModel fileModel = (FileModel) mPDF_FilesList.get(i);
                    if (fileModel.isSelected()) {
                        items.remove(fileModel);
                    }
                }
                mPDF_FilesList.clear();
                mPDF_FilesList.addAll(items);
                rearrangePdfFilesAdapter.set(mPDF_FilesList);
                rearrangePdfFilesAdapter.notifyDataSetChanged();
                backToOriginal();
                break;
            case R.id.action_ok:
                action_ok.setVisible(false);
                gridView.stopEditMode();
                backToOriginal();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode() || isDeleteSelectionMode) {
            action_delete.setVisible(false);
            action_ok.setVisible(false);
            action_add.setVisible(true);
            enableDisableRearrange(false);
            return;
        }
        super.onBackPressed();
    }


    public void enableDisableRearrange(boolean z) {
        if (z) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
            btnSelectLayout.setVisibility(View.GONE);
            if (isDeleteSelectionMode) {
                toolBarTitle.setText(getResources().getString(R.string.selectToDelete));
            } else {
                toolBarTitle.setText(getResources().getString(R.string.rearrangeFile));
            }
            changeBackGroundColor(100);
        } else {
            List<Object> items = rearrangePdfFilesAdapter.getItems();
            for (int i = 0; i < items.size(); i++) {
                ((FileModel) items.get(i)).setSelected(z);
            }
            gridView.stopEditMode();
            backToOriginal();
        }
        rearrangePdfFilesAdapter.notifyDataSetChanged();
    }

    private void backToOriginal() {
        actionBar.setHomeAsUpIndicator(null);
        toolBarTitle.setText(getResources().getString(R.string.mergePdfFiles));
        action_add.setVisible(true);
        action_delete.setVisible(false);
        action_ok.setVisible(false);
        isDeleteSelectionMode = false;
        if (mPDF_FilesList.size() > 0) {
            btnSelectLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerViewLayout.setVisibility(View.GONE);
            choosePdfFileLayout.setVisibility(View.VISIBLE);
            btnSelectLayout.setVisibility(View.VISIBLE);
        }
        changeBackGroundColor(3);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btnMergePDF) {
            if (id == R.id.choosePdfFileLayout) {
                selectPdfFilesToMerge();
            }
        } else if (rearrangePdfFilesAdapter.getItems().size() <= 0) {
            Utility.Toast(this, getResources().getString(R.string.selectPdfFilesFirst));
        } else {
            Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.setType("application/pdf");
            intent.putExtra("android.intent.extra.TITLE", "MERGE_PDF_" + System.currentTimeMillis() + ".pdf");
            mPdfUriResult.launch(Intent.createChooser(intent, "Select Files Path"));
        }
    }

    public void createTextOrPDF_File(String str) {
        List<Object> items = rearrangePdfFilesAdapter.getItems();
        String[] strArr = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            strArr[i] = ((FileModel) items.get(i)).getPath();
        }
        new MergePdf(str, Utility.getDocumentDirPath(this).getPath(), false, null, this, getResources().getString(R.string.app_name)).execute(strArr);
    }

    public void resetValues(boolean z, final String str, final String str2) {
        Singleton.getInstance().setFileDeleted(true);
        progressDialog.dismiss();
        if (str != null) {
            AlertDialog.Builder title = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.fileSaved));
            title.setMessage(str2 + getResources().getString(R.string.fileHasBeenSavedTo) + getResources().getString(R.string.app_folder_name)).setPositiveButton(getResources().getString(R.string.openThisFile), (dialogInterface, i) -> {
                if (GoogleAds.adsdisplay) {
                    GoogleAds.showFullAds(MergePDF.this, () -> {
                        GoogleAds.allcount60.start();
                        CreateNewPdfFile.selectedFileSource = "";
                        IntentPDFView(str, str2);
                        dialogInterface.dismiss();
                    });
                } else {
                    CreateNewPdfFile.selectedFileSource = "";
                    IntentPDFView(str, str2);
                    dialogInterface.dismiss();
                }
            }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
        }
    }

    private void IntentPDFView(String str, String str2) {
        Intent intent = new Intent(MergePDF.this, PDFViewWebViewBase.class);
        intent.putExtra("fileType", "3");
        intent.putExtra("path", str);
        intent.putExtra("name", str2);
        startActivity(intent);
    }

    public void mergeStarted() {
        progressDialog.show();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 10) {
            ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("result");
            mSelectedImagesPathList = stringArrayListExtra;
            if (stringArrayListExtra != null) {
                for (int i3 = 0; i3 < mSelectedImagesPathList.size(); i3++) {
                    FileModel fileModel = new FileModel();
                    fileModel.setPath(mSelectedImagesPathList.get(i3));
                    fileModel.setName(new File(mSelectedImagesPathList.get(i3)).getName());
                    fileModel.setSelected(false);
                    mPDF_FilesList.add(fileModel);
                }
                recyclerViewLayout.setVisibility(View.VISIBLE);
                choosePdfFileLayout.setVisibility(View.GONE);
                rearrangePdfFilesAdapter = new RearrangePdfFilesAdp(this, mPDF_FilesList, 1);
                gridView.setAdapter(rearrangePdfFilesAdapter);
            }
        }
    }

    public void showOptionAlertDialog(final FileModel fileModel) {
        new AlertDialog.Builder(this).setItems(new String[]{getResources().getString(R.string.remove), getResources().getString(R.string.rearrange), getResources().getString(R.string.viewFile)}, (dialogInterface, i) -> {
            if (i == 0) {
                isDeleteSelectionMode = true;
                fileModel.setSelected(true);
                action_add.setVisible(false);
                action_ok.setVisible(false);
                action_delete.setVisible(true);
                enableDisableRearrange(true);
            } else if (i == 1) {
                gridView.startEditMode();
                action_add.setVisible(false);
                action_delete.setVisible(false);
                action_ok.setVisible(true);
                enableDisableRearrange(true);
            } else if (i == 2) {
                Intent intent = new Intent(MergePDF.this, PDFViewWebViewBase.class);
                intent.putExtra("path", fileModel.getPath());
                intent.putExtra("name", fileModel.getName());
                intent.putExtra("fileType", "3");
                startActivity(intent);
            }
        }).create().show();
    }

    private void selectPdfFilesToMerge() {
        if (prefs.isLoadAllFilesAtOnce()) {
            startActivityForResult(new Intent(this, SelectPdfFile.class), 10);
            return;
        }
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.setType("application/pdf");
        mFileResult.launch(Intent.createChooser(intent, "Select PDF"));
    }

    @SuppressLint("StaticFieldLeak")
    class AddFileToAdapter extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        ArrayList<Uri> uriList;

        @Override
        public void onPreExecute() {
            progressDialog.setMessage("Adding file please wait..");
            super.onPreExecute();
        }

        public AddFileToAdapter(ArrayList<Uri> arrayList) {
            progressDialog = new ProgressDialog(MergePDF.this);
            uriList = arrayList;
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            for (int i = 0; i < uriList.size(); i++) {
                FileModel access$300 = getFilePathForN(uriList.get(i));
                if (access$300 != null) {
                    access$300.setSelected(false);
                    mPDF_FilesList.add(access$300);
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Void voidR) {
            progressDialog.dismiss();
            recyclerViewLayout.setVisibility(View.VISIBLE);
            choosePdfFileLayout.setVisibility(View.GONE);
            rearrangePdfFilesAdapter = new RearrangePdfFilesAdp(MergePDF.this, mPDF_FilesList, 1);
            gridView.setAdapter(rearrangePdfFilesAdapter);
            super.onPostExecute(voidR);
        }
    }

    public FileModel getFilePathForN(Uri uri) {
        FileModel fileModel = new FileModel();
        fileModel.setSelected(false);
        Cursor query = getContentResolver().query(uri, null, null, null, null);
        if (query != null) {
            int columnIndex = query.getColumnIndex("_data");
            int columnIndex2 = query.getColumnIndex("_display_name");
            query.moveToFirst();
            if (query.getCount() > 0) {
                String string = query.getString(columnIndex2);
                if (string != null || columnIndex <= 0) {
                    Utility.logCatMsg("file Name " + string + "  :   dataPath " + "");
                    assert string != null;
                    File file = new File(Utility.getTempFolderDirectory(this), string);
                    String path = file.getPath();
                    query.close();
                    try {
                        InputStream openInputStream = getContentResolver().openInputStream(uri);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] bArr = new byte[Math.min(openInputStream.available(), 1048576)];
                        while (true) {
                            int read = openInputStream.read(bArr);
                            if (read != -1) {
                                fileOutputStream.write(bArr, 0, read);
                            } else {
                                openInputStream.close();
                                fileOutputStream.close();
                                fileModel.setPath(path);
                                fileModel.setName(string);
                                return fileModel;
                            }
                        }
                    } catch (Exception e) {
                        Utility.logCatMsg("Exception in getFilePath" + e.getMessage());
                    }
                } else {
                    String string2 = query.getString(columnIndex);
                    Utility.logCatMsg("file Name " + string + "  :   dataPath " + string2);
                    fileModel.setPath(string2);
                    fileModel.setName(string);
                    return fileModel;
                }
            }
        }
        return null;
    }
}
