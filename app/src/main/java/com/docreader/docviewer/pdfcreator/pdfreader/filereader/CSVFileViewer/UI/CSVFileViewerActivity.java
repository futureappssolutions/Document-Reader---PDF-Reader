package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.UI;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.facebookMaster;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.PDFViewWebViewBase;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.Cell;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.ColumnHeader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.RowHeader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.CoroutinesTask;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.TableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.Filter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.pagination.Pagination;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CSVFileViewerActivity extends BaseActivity {
    public List<List<Cell>> cellDataList = new ArrayList<>();
    public List<ColumnHeader> columnHeaderList = new ArrayList<>();
    public List<RowHeader> rowHeaderList = new ArrayList<>();
    public ArrayList<List<String>> csv_data = new ArrayList<>();
    public Boolean fromConverterApp = false;
    public Pagination mPagination;
    public Filter mTableFilter;
    public TableView mTableView;
    public boolean mPaginationEnabled = false;

    ActivityResultLauncher<Intent> mPdfUriResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        Intent data = activityResult.getData();
        if (activityResult.getResultCode() == -1 && Objects.requireNonNull(data).getData() != null) {
            convertCSV_To_PDF(data.getData());
        }
    });

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_csv_file_viewer);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mTableView = findViewById(R.id.tableview);
        TextView textView = findViewById(R.id.toolBarTitle);

        SharedPrefs prefs = new SharedPrefs(CSVFileViewerActivity.this);
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(CSVFileViewerActivity.this, ll_banner);
                    break;
                case "f":
                    facebookMaster.FbBanner(CSVFileViewerActivity.this, ll_banner);
                    break;
                case "both":
                    Advertisement.GoogleBannerBoth(CSVFileViewerActivity.this, ll_banner);
                    break;
            }
        }

        if (getIntent() != null) {
            String stringExtra = getIntent().getStringExtra("path");
            String stringExtra2 = getIntent().getStringExtra("name");
            fromConverterApp = getIntent().getBooleanExtra("fromConverterApp", false);
            Integer.parseInt(getIntent().getStringExtra("fileType"));
            changeBackGroundColor(100);
            textView.setText(stringExtra2);
            new LoadCVSDataFromPath(stringExtra).execute();
        }

        mTableFilter = new Filter(mTableView);
        if (mPaginationEnabled) {
            mPagination = new Pagination(mTableView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fromConverterApp) {
            getMenuInflater().inflate(R.menu.convert, menu);
        } else {
            getMenuInflater().inflate(R.menu.csv_menu, menu);
            if (Build.VERSION.SDK_INT >= 19) {
                MenuItem findItem = menu.findItem(R.id.action_print);
                MenuItem findItem2 = menu.findItem(R.id.action_convert_to_pdf);
                findItem.setVisible(true);
                findItem2.setVisible(true);
            }
            final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextSubmit(String str) {
                    if (searchView.isIconified()) {
                        return false;
                    }
                    searchView.setIconified(true);
                    return false;
                }

                public boolean onQueryTextChange(String str) {
                    filterTable(str);
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                finish();
                break;
            case R.id.action_convert:
                lunchCreatePdfFileIntent();
                break;
            case R.id.action_convert_to_pdf:
                lunchCreatePdfFileIntent();
                break;
            case R.id.action_print:
                if (Build.VERSION.SDK_INT >= 19) {
                    convertCSV_To_PDF(null);
                    break;
                }
                break;
            case R.id.action_scroll_to_bottom:
                if (rowHeaderList.size() > 0) {
                    mTableView.scrollToRowPosition(rowHeaderList.size() - 1);
                    break;
                }
                break;
            case R.id.action_scroll_to_top:
                mTableView.scrollToRowPosition(0);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void lunchCreatePdfFileIntent() {
        if (Build.VERSION.SDK_INT >= 19) {
            Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.setType("application/pdf");
            intent.putExtra("android.intent.extra.TITLE", "CSV_To_PDF_" + System.currentTimeMillis() + ".pdf");
            mPdfUriResult.launch(Intent.createChooser(intent, "Select Files Path"));
        }
    }

    public void convertCSV_To_PDF(Uri uri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.preparingPdfPagesPleaseWait));
        progressDialog.setProgressStyle(1);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new CoroutinesTask(obj -> runOnUiThread(() -> {
            progressDialog.dismiss();
            if (obj != null) {
                openPDFDialog(CSVFileViewerActivity.this, obj.toString());
            }
        })).csvToPDF(this, csv_data, uri, i -> runOnUiThread(() -> {
            if (i != 100) {
                progressDialog.setProgress(i);
            } else {
                progressDialog.setProgress(99);
            }
        }));
    }

    public void loadCVSDate(String str) {
        try {
            List read = new CSVReader(this).read(new FileInputStream(str));
            if (read.size() > 0) {
                for (int i = 0; i < read.size(); i++) {
                    String[] strArr = (String[]) read.get(i);
                    ArrayList<Cell> arrayList = new ArrayList<>();
                    ArrayList<String> arrayList2 = new ArrayList<>();
                    if (strArr.length > 1) {
                        StringBuilder str2 = new StringBuilder();
                        for (int i2 = 0; i2 < strArr.length; i2++) {
                            if (i == 0) {
                                columnHeaderList.add(new ColumnHeader(String.valueOf(i), strArr[i2]));
                            } else {
                                arrayList.add(new Cell(i2 + "-" + i, strArr[i2]));
                            }
                            arrayList2.add(strArr[i2] + "");
                            str2.append(" -  ").append(strArr[i2]);
                        }
                        rowHeaderList.add(new RowHeader(String.valueOf(i), String.valueOf(i)));
                    }
                    cellDataList.add(arrayList);
                    csv_data.add(arrayList2);
                }
                Utility.logCatMsg("size " + read.size());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void filterTable(String str) {
        mTableFilter.set(str);
    }

    public void filterTableForMood(String str) {
        mTableFilter.set(2, str);
    }

    public void filterTableForGender(String str) {
        mTableFilter.set(4, str);
    }

    public void nextTablePage() {
        mPagination.nextPage();
    }

    public void previousTablePage() {
        mPagination.previousPage();
    }

    public void goToTablePage(int i) {
        mPagination.goToPage(i);
    }

    public void setTableItemsPerPage(int i) {
        mPagination.setItemsPerPage(i);
    }

    @SuppressLint("StaticFieldLeak")
    class LoadCVSDataFromPath extends AsyncTask<Void, Void, Void> {
        String file_path;
        ProgressDialog progressDialog;

        public LoadCVSDataFromPath(String str) {
            file_path = str;
        }

        @Override
        public void onPreExecute() {
            progressDialog = new ProgressDialog(CSVFileViewerActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.loadingFiles));
            progressDialog.setProgressStyle(1);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);
            progressDialog.setButton(-1, getResources().getString(R.string.cancel), (dialogInterface, i) -> {
                dialogInterface.dismiss();
                LoadCVSDataFromPath.this.cancel(true);
            });
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            loadCVSDate(file_path);
            return null;
        }

        @Override
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            TableViewAdapter tableViewAdapter = new TableViewAdapter();
            mTableView.setAdapter(tableViewAdapter);
            mTableView.setTableViewListener(new TableViewListener(mTableView));
            tableViewAdapter.setAllItems(columnHeaderList, rowHeaderList, cellDataList);
        }
    }

    @SuppressLint("ResourceType")
    public void openPDFDialog(final CSVFileViewerActivity cSVFileViewerActivity, final String str) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = cSVFileViewerActivity.getLayoutInflater().inflate(R.layout.bottomsheet_open_pdf_file, null);
        bottomSheetDialog.setContentView(inflate);

        ((TextView) inflate.findViewById(R.id.description)).setText(cSVFileViewerActivity.getResources().getString(R.string.filePath) + " : " + str);

        inflate.findViewById(R.id.ViewFile).setOnClickListener(view -> {
            Intent intent = new Intent(cSVFileViewerActivity, PDFViewWebViewBase.class);
            intent.putExtra("path", str);
            intent.putExtra("name", "CSV Converted");
            intent.putExtra("fileType", "3");
            cSVFileViewerActivity.startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.noThanks).setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}
