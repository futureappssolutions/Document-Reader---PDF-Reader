package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.AllFilesAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.UI.CSVFileViewerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer.EPubFileViewerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FilesList extends BaseActivity {
    public List<Object> fileList = new ArrayList<>();
    public int fileType = 100;
    public int spanCount = 1;
    public boolean isFromConverterApp = false;
    public AllFilesAdp adapter;
    public FileModel clickedFileModel;
    public String filTypeName = "";
    public MenuItem menuItemDelete;
    public MenuItem menuItemSort;
    public SharedPrefs prefs;
    public ProgressBar progressBar;
    public RecyclerView recyclerView;
    public Singleton singleton;
    public TextView toolBarTitle;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_file_list);

        filTypeName = getResources().getString(R.string.allFiles);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolBarTitle = findViewById(R.id.toolBarTitle);

        singleton = Singleton.getInstance();
        prefs = new SharedPrefs(this);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAppLovinAds.showBannerAds(FilesList.this, ll_banner);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler_view);

        if (getIntent() != null) {
            fileType = Integer.parseInt(getIntent().getStringExtra("fileType"));
            isFromConverterApp = getIntent().getBooleanExtra("fromConverterApp", false);
            changeBackGroundColor(fileType);
            filTypeName = getFileName(fileType);
            toolBarTitle.setText(filTypeName);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getFilesData();
    }

    @Override
    public void onBackPressed() {
        if (adapter == null || !adapter.isLongPress) {
            finish();
            return;
        }
        adapter.isLongPress = false;
        changeToolBarControl();
        adapter.reSetAllSelection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        menuItemDelete = menu.findItem(R.id.action_delete);
        menuItemSort = menu.findItem(R.id.menu_sort);
        return true;
    }

    public void onOptionClicked(final FileModel fileModel, final int i1) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_menu_file_list);

        TextView readTv = bottomSheetDialog.findViewById(R.id.readTv);
        TextView renameTv = bottomSheetDialog.findViewById(R.id.renameTv);
        TextView deleteTv = bottomSheetDialog.findViewById(R.id.deleteTv);
        TextView shareTv = bottomSheetDialog.findViewById(R.id.shareTv);
        TextView infoTv = bottomSheetDialog.findViewById(R.id.infoTv);

        readTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                readFile(fileModel);
            }
        });

        renameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                renameFileNameAlertDialog(fileModel, i1);
            }
        });

        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                deleteFile(fileModel, i1);
            }
        });

        shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Utility.shareFile(FilesList.this, fileModel.getPath());
            }
        });

        infoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                fileInfo(fileModel);
            }
        });
        bottomSheetDialog.show();

//        new AlertDialog.Builder(this).setTitle(fileModel.getName()).setItems(new String[]{getResources().getString(R.string.read), getResources().getString(R.string.rename), getResources().getString(R.string.delete), getResources().getString(R.string.share), getResources().getString(R.string.info)}, (dialogInterface, i) -> {
//            if (i == 0) {
//                readFile(fileModel);
//            } else if (i == 1) {
//                renameFileNameAlertDialog(fileModel, i1);
//            } else if (i == 2) {
//                deleteFile(fileModel, i1);
//            } else if (i == 3) {
//                Utility.shareFile(FilesList.this, fileModel.getPath());
//            } else if (i == 4) {
//                fileInfo(fileModel);
//            }
//        }).create().show();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteFile(final FileModel fileModel, final int i) {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.alert)).setMessage(getResources().getString(R.string.areYouSureToDeleteThisFile)).setPositiveButton(getResources().getString(R.string.delete), (dialogInterface, i1) -> {
            File file = new File(fileModel.getPath());
            if (file.exists()) {
                file.delete();
                Singleton.getInstance().getAllDocumentFileList().remove(fileList.get(i));
                adapter.notifyDataSetChanged();
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
        if (fileModel.getFileType() == 15) {
            extractCompressFile(fileModel);
            return;
        }
        if (fileModel.getFileType() == 3) {
            intent = Build.VERSION.SDK_INT >= 21 ? new Intent(this, PDFViewWebViewBase.class) : new Intent(this, PDFViewWebViewBase.class);
        } else if (fileModel.getFileType() == 6 || fileModel.getFileType() == 11) {
            intent = new Intent(this, TextViewer.class);
        } else if (fileModel.getFileType() == 10) {
            intent = new Intent(this, CSVFileViewerActivity.class);
        } else if (fileModel.getFileType() == 13) {
            intent = new Intent(this, RTFView.class);
        } else if (fileModel.getFileType() == 14) {
            intent = new Intent(this, EPubFileViewerActivity.class);
        }
        intent.putExtra("path", fileModel.getPath());
        intent.putExtra(AppMeasurementSdk.ConditionalUserProperty.NAME, fileModel.getName());
        intent.putExtra("fromConverterApp", this.isFromConverterApp);
        intent.putExtra("fileType", fileModel.getFileType() + "");
        Utility.logCatMsg("path " + fileModel.getPath());
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
            case 15:
                return getResources().getString(R.string.zipFiles);
            default:
                return string;
        }
    }

    private void getFilesData() {
        int i = fileType;
        if (i == 100) {
            fileList = Singleton.getInstance().getAllDocumentFileList();
        } else {
            int i2 = 0;
            if (i == 12) {
                fileList = new ArrayList<>();
                ArrayList<Object> allDocumentFileList = Singleton.getInstance().getAllDocumentFileList();
                while (i2 < allDocumentFileList.size()) {
                    if ((allDocumentFileList.get(i2) instanceof FileModel) && ((FileModel) allDocumentFileList.get(i2)).isFavorite()) {
                        fileList.add(allDocumentFileList.get(i2));
                    }
                    i2++;
                }
            } else if (i == 14) {
                fileList = Singleton.getInstance().getAllEbookFileList();
            } else {
                fileList = new ArrayList<>();
                ArrayList<Object> allDocumentFileList2 = Singleton.getInstance().getAllDocumentFileList();
                while (i2 < allDocumentFileList2.size()) {
                    if ((allDocumentFileList2.get(i2) instanceof FileModel) && ((FileModel) allDocumentFileList2.get(i2)).getFileType() == fileType) {
                        fileList.add(allDocumentFileList2.get(i2));
                    }
                    i2++;
                }
            }
        }
        shortList();
    }

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.action_delete) {
            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.alert)).setMessage(getResources().getString(R.string.areYouSureToDeleteThisFile)).setPositiveButton(getResources().getString(R.string.delete), (dialogInterface, i) -> {
                int i2 = 0;
                while (fileList.size() > i2) {
                    if (fileList.get(i2) instanceof FileModel) {
                        if (((FileModel) fileList.get(i2)).isSelected()) {
                            boolean deleteDir = Utility.deleteDir(new File(((FileModel) fileList.get(i2)).getPath()));
                            Utility.logCatMsg("isDelete " + deleteDir);
                            Singleton.getInstance().getAllDocumentFileList().remove(fileList.get(i2));
                        } else {
                            i2++;
                        }
                    }
                }
                adapter.isLongPress = false;
                adapter.notifyDataSetChanged();
                dialogInterface.dismiss();
                changeToolBarControl();
                FilesList activityFilesList = FilesList.this;
                Utility.Toast(activityFilesList, activityFilesList.getResources().getString(R.string.deletedSuccessfully));
            }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
        } else if (itemId != R.id.action_search) {
            switch (itemId) {
                case R.id.sort_by_ascending:
                    prefs.setListSorting(0);
                    shortList();
                    break;
                case R.id.sort_by_date:
                    prefs.setListSorting(2);
                    shortList();
                    break;
                case R.id.sort_by_descending:
                    prefs.setListSorting(1);
                    shortList();
                    break;
                case R.id.sort_by_size:
                    prefs.setListSorting(3);
                    shortList();
                    break;
            }
        } else {
            Intent intent = new Intent(this, SearchFile.class);
            intent.putExtra("fileType", fileType + "");
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressLint("ResourceType")
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
                    Utility.Toast(FilesList.this, getResources().getString(R.string.fileRenamedSuccessfully));
                } else {
                    Utility.logCatMsg("File rename failed");
                }
                bottomSheetDialog.dismiss();
                return;
            }

            Utility.Toast(FilesList.this, getResources().getString(R.string.enterFileNameFirst));
        });

        inflate.findViewById(R.id.btnCancel).setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    public void shortList() {
        if (fileList.size() == 0) {
            findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
            return;
        }
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
        AllFilesAdp allFilesAdapter = new AllFilesAdp(this, fileList, this);
        adapter = allFilesAdapter;
        recyclerView.setAdapter(allFilesAdapter);
        progressBar.setVisibility(View.GONE);
    }

    public void extractCompressFile(FileModel fileModel) {
        Utility.logCatMsg("extractCompressFile");
    }

    public void changeToolBarControl() {
        AllFilesAdp allFilesAdapter = adapter;
        if (allFilesAdapter == null) {
            return;
        }
        if (allFilesAdapter.isLongPress) {
            menuItemDelete.setVisible(true);
            menuItemSort.setVisible(false);
            Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(ContextCompat.getDrawable(FilesList.this, R.drawable.ic_close));
            return;
        }
        menuItemDelete.setVisible(false);
        menuItemSort.setVisible(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(null);
    }
}
