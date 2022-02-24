package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.FolderViewAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.PickImageVideoAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.AppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.OnFolderItemClickListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.OnPhotosLoadListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.onRecyclerViewItemClick;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.Folder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.ImageModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.PhotoUtil;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.PackagingURIHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.res.ResConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectImage extends BaseActivity implements onRecyclerViewItemClick, OnFolderItemClickListener, OnPhotosLoadListener {
    private Map<String, Folder> folderMap = null;
    private ArrayList<ImageModel> mRecyclerViewItems;
    private final ArrayList<String> mSelectedFilesPathList = new ArrayList<>();
    private List<Folder> foldersList;
    private ActionBar actionBar;
    private FolderViewAdp adapterFolderView;
    private PickImageVideoAdp adapterPickImageVideo;
    private LinearLayout hideButtonLayout;
    private TextView hideCounterTv;
    private TextView toolBarTitle;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewFolderView;
    private MenuItem nav_select;
    private ProgressBar progressBar;
    private int imageSelectionCounter = 0;
    private boolean isChooseOnlyOneImage = false;
    boolean isFileTaskInProgress = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_select_image);
        changeBackGroundColor(0);

        init();

        SharedPrefs prefs = new SharedPrefs(SelectImage.this);
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(SelectImage.this, ll_banner);
                    break;
                case "a":
                    AppLovinAds.AppLovinBanner(SelectImage.this, ll_banner);
                    break;
            }
        }

        initFolderViewRecyclerView();
        initImageVideoRecyclerView();

        if (getIntent() != null) {
            isChooseOnlyOneImage = getIntent().getBooleanExtra("isChooseOnlyOneImage", false);
        }

        new PhotoUtil(this, this).getAllPhotosFolderWise();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_folder_view, menu);
        nav_select = menu.findItem(R.id.nav_select_images);
        return true;
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.nav_select_images) {
            if (mSelectedFilesPathList.size() > 0) {
                setResult(-1, new Intent());
                finish();
            } else {
                Utility.Toast(this, getResources().getString(R.string.selectPhotoFirst));
            }
        } else if (itemId == R.id.nav_select_all) {
            imageSelectionCounter = 0;
            mSelectedFilesPathList.clear();
            ImageToPDF.mImagesList.clear();
            for (int i = 0; i < mRecyclerViewItems.size(); i++) {
                mRecyclerViewItems.get(i).setSelected(true);
                mSelectedFilesPathList.add(mRecyclerViewItems.get(i).getPath());
                ImageToPDF.mImagesList.add(mRecyclerViewItems.get(i));
                imageSelectionCounter++;
            }
            adapterPickImageVideo.notifyDataSetChanged();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
            toolBarTitle.setText(imageSelectionCounter + getResources().getString(R.string.items));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressLint("SetTextI18n")
    public void onItemClick(Object obj) {
        ImageModel imageModel = (ImageModel) obj;
        if (isChooseOnlyOneImage) {
            Intent intent = new Intent();
            intent.putExtra("result", imageModel.getUri().toString());
            setResult(-1, intent);
            finish();
            return;
        }
        if (imageModel.isSelected()) {
            imageSelectionCounter++;
            mSelectedFilesPathList.add(imageModel.getPath());
            ImageToPDF.mImagesList.add(imageModel);
        } else {
            imageSelectionCounter--;
            mSelectedFilesPathList.remove(imageModel.getPath());
            ImageToPDF.mImagesList.remove(imageModel);
        }
        if (imageSelectionCounter < 0) {
            imageSelectionCounter = 0;
        }
        if (imageSelectionCounter == 0) {
            enableDisableLayout(hideButtonLayout, false);
            toolBarTitle.setText("Pick File");
            nav_select.setVisible(false);
            actionBar.setHomeAsUpIndicator(null);
        } else {
            nav_select.setVisible(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
            toolBarTitle.setText(imageSelectionCounter + getResources().getString(R.string.items));
            enableDisableLayout(hideButtonLayout, true);
        }
        hideCounterTv.setText(getResources().getString(R.string.hide) + imageSelectionCounter + getResources().getString(R.string.bracketRight));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onFolderItemClick(Object obj) {
        Folder folder = (Folder) obj;
        mRecyclerViewItems = folder.getImages();
        Singleton.getInstance().setFileList(mRecyclerViewItems);
        Collections.reverse(mRecyclerViewItems);
        PickImageVideoAdp pickImageVideoAdapter = new PickImageVideoAdp(this, mRecyclerViewItems);
        adapterPickImageVideo = pickImageVideoAdapter;
        mRecyclerView.setAdapter(pickImageVideoAdapter);
        adapterPickImageVideo.setOnRecyclerViewItemClick(this);
        adapterPickImageVideo.notifyDataSetChanged();
        toolBarTitle.setText(folder.getFolderName());
        mRecyclerViewFolderView.setVisibility(View.GONE);
    }

    public void onPhotoDataLoadedCompleted(final HashMap<String, Folder> hashMap) {
        runOnUiThread(() -> {
            try {
                folderMap = hashMap;
                if (folderMap != null) {
                    foldersList = Singleton.getInstance().getFolderList(folderMap.values());
                }
                if (foldersList.size() > 0) {
                    Collections.reverse(foldersList);
                    adapterFolderView = new FolderViewAdp(this, foldersList);
                    mRecyclerViewFolderView.setAdapter(adapterFolderView);
                    progressBar.setVisibility(View.GONE);
                    adapterFolderView.setOnFolderViewItemClick(SelectImage.this);
                } else {
                    findViewById(R.id.progrssBar).setVisibility(View.GONE);
                    Utility.Toast(this, getResources().getString(R.string.noFilesFound));
                }
                isFileTaskInProgress = false;
            } catch (Exception e) {
                Utility.logCatMsg("Exception " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void loadAllFiles(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isFile() && isSelectedFiles(file2)) {
                    ImageModel imageModel = new ImageModel();
                    imageModel.setPath(file2.getPath());
                    imageModel.setName(file2.getName());
                    imageModel.setSelected(false);
                    imageModel.setVideo(isSelectedFileIsVideo(file2.getName()));
                    try {
                        String[] split = file2.getPath().split(PackagingURIHelper.FORWARD_SLASH_STRING);
                        if (split.length > 0) {
                            String str = split[split.length - 2];
                            Map<String, Folder> map = folderMap;
                            if (map != null) {
                                Folder folder = map.get(str);
                                if (folder == null) {
                                    folder = new Folder(str);
                                    folderMap.put(str, folder);
                                }
                                folder.getImages().add(imageModel);
                            }
                        }
                    } catch (Exception e) {
                        Utility.logCatMsg("Exception " + e.getMessage());
                    }
                } else if (file2.isDirectory()) {
                    String[] split2 = file2.getPath().split(PackagingURIHelper.FORWARD_SLASH_STRING);
                    String str2 = split2[split2.length - 1];
                    if (!str2.startsWith(".") && !str2.equals("app") && !str2.equals("Wallpapers") && !str2.equals("cache")) {
                        loadAllFiles(file2.getAbsoluteFile());
                    }
                }
            }
        }
    }

    private void enableDisableLayout(View view, boolean z) {
        view.setEnabled(z);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                enableDisableLayout(viewGroup.getChildAt(i), z);
            }
        }
    }

    private boolean isSelectedFileIsVideo(String str) {
        String lowerCase = str.toLowerCase();
        return lowerCase.endsWith(".3gp") || lowerCase.endsWith(".mp4");
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView textView = findViewById(R.id.toolBarTitle);
        toolBarTitle = textView;
        textView.setText("Gallery");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        progressBar = findViewById(R.id.progrssBar);
    }

    private void initFolderViewRecyclerView() {
        GridLayoutManager gridLayoutManager;
        mRecyclerViewFolderView = findViewById(R.id.folder_view_recycler_view);
        mRecyclerViewFolderView.setVisibility(View.VISIBLE);
        findViewById(R.id.adLayout).setVisibility(View.GONE);
        if (getResources().getConfiguration().orientation == 1) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 3);
        }
        mRecyclerViewFolderView.setLayoutManager(gridLayoutManager);
    }

    private void initImageVideoRecyclerView() {
        GridLayoutManager gridLayoutManager;
        mRecyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progrssBar);
        hideCounterTv = findViewById(R.id.hideCounterTv);
        hideButtonLayout = findViewById(R.id.hideButtonLayout);
        if (getResources().getConfiguration().orientation == 1) {
            gridLayoutManager = new GridLayoutManager(this, 3);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 5);
        }
        mRecyclerView.setLayoutManager(gridLayoutManager);
        hideButtonLayout.setOnClickListener(view -> {
            if (mSelectedFilesPathList.size() > 0) {
                AlertDialog.Builder icon = new AlertDialog.Builder(SelectImage.this).setTitle(getResources().getString(R.string.alert)).setCancelable(false);
                icon.setMessage("Are you sure to move " + imageSelectionCounter + " files to calculator hider app?").setPositiveButton("Move/Hide Files", (dialogInterface, i) -> {
                }).setNeutralButton(ResConstant.BUTTON_CANCEL, (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
                return;
            }
            Utility.Toast(SelectImage.this, "Select photo or video first");
        });
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBackPressed() {
        if (imageSelectionCounter != 0) {
            for (int i = 0; i < mRecyclerViewItems.size(); i++) {
                if (mRecyclerViewItems.get(i).isSelected()) {
                    mRecyclerViewItems.get(i).setSelected(false);
                }
            }
            adapterPickImageVideo.reSetCounter();
            adapterPickImageVideo.notifyDataSetChanged();
            imageSelectionCounter = 0;
            toolBarTitle.setText("Pick File");
            nav_select.setVisible(false);
            actionBar.setHomeAsUpIndicator(null);
        } else if (mRecyclerViewFolderView.getVisibility() == View.GONE) {
            toolBarTitle.setText("Gallery");
            mRecyclerViewFolderView.setVisibility(View.VISIBLE);
        } else {
            if (isChooseOnlyOneImage) {
                setResult(0, new Intent());
            }
            finish();
        }
    }

    private boolean isSelectedFiles(File file) {
        String lowerCase = file.getPath().toLowerCase();
        return lowerCase.endsWith(".png") || lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg");
    }
}
