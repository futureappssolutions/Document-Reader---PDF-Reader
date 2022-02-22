package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.SearchableFilesListAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.facebookMaster;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.UI.CSVFileViewerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SearchFile extends BaseActivity {
    public SearchableFilesListAdp adapter;
    public FileModel clickedFileModel;
    public String filTypeName = "";
    public List<Object> fileList;
    public TextView toolBarTitle;
    public SharedPrefs prefs;
    public ProgressBar progressBar;
    public RecyclerView recyclerView;
    public SearchView searchView;
    public int fileType = 100;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_file_list);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolBarTitle = findViewById(R.id.toolBarTitle);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler_view);

        filTypeName = getResources().getString(R.string.allFiles);

        prefs = new SharedPrefs(this);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(SearchFile.this, ll_banner);
                    break;
                case "f":
                    facebookMaster.FbBanner(SearchFile.this, ll_banner);
                    break;
                case "both":
                    Advertisement.GoogleBannerBoth(SearchFile.this, ll_banner);
                    break;
            }
        }

        if (getIntent() != null) {
            int parseInt = Integer.parseInt(getIntent().getStringExtra("fileType"));
            fileType = parseInt;
            changeBackGroundColor(parseInt);
            filTypeName = getFileName(fileType);
            toolBarTitle.setText(getResources().getString(R.string.searchAllDocFile));
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getFilesData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getResources().getString(R.string.searchHere));
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
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
        return true;
    }

    public void onOptionClicked(final FileModel fileModel, final int i) {
        new AlertDialog.Builder(this).setTitle(fileModel.getName()).setItems(new String[]{getResources().getString(R.string.read), getResources().getString(R.string.rename), getResources().getString(R.string.delete), getResources().getString(R.string.share), getResources().getString(R.string.info)}, (dialogInterface, i1) -> {
            if (i1 == 0) {
                readFile(fileModel);
            } else if (i1 == 1) {
                renameFileNameAlertDialog(fileModel, i);
            } else if (i1 == 2) {
                deleteFile(fileModel, i);
            } else if (i1 == 3) {
                Utility.shareFile(SearchFile.this, fileModel.getPath());
            } else if (i1 == 4) {
                fileInfo(fileModel);
            }
        }).create().show();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void deleteFile(final FileModel fileModel, final int i) {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.alert)).setMessage(getResources().getString(R.string.areYouSureToDeleteThisFile)).setPositiveButton(getResources().getString(R.string.delete), (dialogInterface, i1) -> {
            File file = new File(fileModel.getPath());
            if (file.exists()) {
                file.delete();
                fileList.remove(i);
                adapter.notifyDataSetChanged();
                Singleton.getInstance().setFileDeleted(true);
            }
            dialogInterface.dismiss();
        }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i12) -> dialogInterface.dismiss()).create().show();
    }


    public void fileInfo(FileModel fileModel) {
        AlertDialog.Builder title = new AlertDialog.Builder(this).setTitle(fileModel.getName());
        title.setMessage(getResources().getString(R.string.pathColon) + fileModel.getPath() + "\n\n" + getResources().getString(R.string.nameColon) + fileModel.getName() + "\n\n" + getResources().getString(R.string.sizeColon) + fileModel.getSize() + "\n\n" + getResources().getString(R.string.modifyDateColon) + Utility.getFileDateTime(fileModel.getModifiedDate())).setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }


    public void readFile(FileModel fileModel) {
        Intent intent = new Intent(this, FilesView.class);
        if (fileModel.getFileType() == 3) {
            intent = new Intent(this, PDFViewWebViewBase.class);
        } else if (fileModel.getFileType() == 6) {
            intent = new Intent(this, TextViewer.class);
        } else if (fileModel.getFileType() == 6 || fileModel.getFileType() == 11) {
            intent = new Intent(this, TextViewer.class);
        } else if (fileModel.getFileType() == 10) {
            intent = new Intent(this, CSVFileViewerActivity.class);
        } else if (fileModel.getFileType() == 13) {
            intent = new Intent(this, RTFView.class);
        }
        intent.putExtra("path", fileModel.getPath());
        intent.putExtra("name", fileModel.getName());
        intent.putExtra("fileType", fileModel.getFileType() + "");
        startActivity(intent);
    }

    public void onItemClicked(FileModel fileModel, int i) {
        clickedFileModel = fileModel;
        readFile(fileModel);
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
        if (i == 13) {
            return getResources().getString(R.string.rtfFile);
        }
        if (i == 100) {
            return getResources().getString(R.string.allFiles);
        }
        if (i == 10) {
            return getResources().getString(R.string.fileCSV);
        }
        if (i != 11) {
            return string;
        }
        return getResources().getString(R.string.fileHTML);
    }

    private void getFilesData() {
        if (fileType == 100) {
            fileList = Singleton.getInstance().getAllDocumentFileList();
        } else {
            fileList = new ArrayList<>();
            ArrayList<Object> allDocumentFileList = Singleton.getInstance().getAllDocumentFileList();
            for (int i = 0; i < allDocumentFileList.size(); i++) {
                if ((allDocumentFileList.get(i) instanceof FileModel) && ((FileModel) allDocumentFileList.get(i)).getFileType() == fileType) {
                    fileList.add(allDocumentFileList.get(i));
                }
            }
        }
        if (fileList.size() == 0) {
            findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
        }
        shortList();
        SearchableFilesListAdp searchableFilesListAdapter = new SearchableFilesListAdp(this, fileList, this);
        adapter = searchableFilesListAdapter;
        recyclerView.setAdapter(searchableFilesListAdapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void renameFileNameAlertDialog(FileModel fileModel, int i) {
        String name = fileModel.getName();
        final String substring = name.substring(name.lastIndexOf("."));
        final File file = new File(fileModel.getPath());

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_rename_file, null);
        bottomSheetDialog.setContentView(inflate);

        final EditText editText = inflate.findViewById(R.id.editText);
        final String replaceFirst = file.getName().replaceFirst("[.][^.]+$", "");
        editText.setText("");
        editText.append(replaceFirst);

        inflate.findViewById(R.id.setNameBtn).setOnClickListener(view -> {
            if (editText.getText().toString().length() > 0) {
                String replace = fileModel.getPath().replace(replaceFirst, editText.getText().toString());
                if (file.renameTo(new File(replace))) {
                    fileModel.setName(editText.getText() + substring);
                    fileModel.setPath(replace);
                    adapter.notifyDataSetChanged();
                    Utility.Toast(SearchFile.this, getResources().getString(R.string.fileRenamedSuccessfully));
                } else {
                    Utility.logCatMsg("File rename failed");
                }
                bottomSheetDialog.dismiss();
                return;
            }
            Utility.Toast(SearchFile.this, getResources().getString(R.string.enterFileNameFirst));
        });

        inflate.findViewById(R.id.btnCancel).setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    public void shortList() {
        Collections.sort(fileList, (obj, obj2) -> {
            FileModel fileModel = (FileModel) obj;
            FileModel fileModel2 = (FileModel) obj2;
            if (prefs.getListSorting() == 0) {
                return fileModel.getName().compareToIgnoreCase(fileModel2.getName());
            }
            if (prefs.getListSorting() == 1) {
                return fileModel2.getName().compareToIgnoreCase(fileModel.getName());
            }
            if (prefs.getListSorting() != 2) {
                return fileModel.getSize().compareToIgnoreCase(fileModel2.getSize());
            }
            return new Date(fileModel2.getModifiedDate()).compareTo(new Date(fileModel.getModifiedDate()));
        });
    }
}
