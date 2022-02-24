package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.ImageToPdfAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.AppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.ImageModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.CoroutinesTask;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.PhotoUtil;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.DynamicGridView.DynamicGridView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageToPDF extends BaseActivity implements View.OnClickListener {
    public boolean isDeleteSelectionMode = false;
    public static ArrayList<Object> mImagesList = new ArrayList<>();
    public ArrayList<Integer> selectedImage = new ArrayList<>();
    public ActionBar actionBar;
    public MenuItem action_add;
    public MenuItem action_delete;
    public MenuItem action_ok;
    public LinearLayout btnSelectLayout;
    public RelativeLayout chooseImageLayout;
    public RelativeLayout recyclerViewLayout;
    public DynamicGridView gridView;
    public ImageToPdfAdp imageToPdfAdapter;
    public TextView toolBarTitle;
    public SharedPrefs prefs;

    ActivityResultLauncher<Intent> mPdfUriResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        Intent data = activityResult.getData();
        if (data != null) {
            if (activityResult.getResultCode() == -1 && data.getData() != null) {
                convertToPDF(null, data.getData());
            }
        }
    });

    ActivityResultLauncher<Intent> mPhotoForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        Intent data = activityResult.getData();
        if (data != null && activityResult.getResultCode() == -1) {
            if (data.getClipData() != null) {
                int itemCount = data.getClipData().getItemCount();
                for (int i = 0; i < itemCount; i++) {
                    try {
                        ImageToPDF.mImagesList.add(new ImageModel("", data.getClipData().getItemAt(i).getUri()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) {
                ImageToPDF.mImagesList.add(new ImageModel("", data.getData()));
            }
            if (ImageToPDF.mImagesList.size() > 0) {
                recyclerViewLayout.setVisibility(View.VISIBLE);
                chooseImageLayout.setVisibility(View.GONE);
                imageToPdfAdapter = new ImageToPdfAdp(ImageToPDF.this, ImageToPDF.mImagesList, 3);
                gridView.setAdapter(imageToPdfAdapter);
            }
        }
    });

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult activityResult) {
            if (activityResult.getResultCode() == -1) {
                if (ImageToPDF.mImagesList.size() > 0) {
                    for (int i = 0; i < ImageToPDF.mImagesList.size(); i++) {
                        ((ImageModel) ImageToPDF.mImagesList.get(i)).setSelected(false);
                    }
                    recyclerViewLayout.setVisibility(View.VISIBLE);
                    chooseImageLayout.setVisibility(View.GONE);
                    imageToPdfAdapter = new ImageToPdfAdp(ImageToPDF.this, ImageToPDF.mImagesList, 3);
                    gridView.setAdapter(imageToPdfAdapter);
                }
                activityResult.getData();
            }
        }
    });

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_img_to_pdf);
        changeBackGroundColor(100);

        mImagesList.clear();

        prefs = new SharedPrefs(ImageToPDF.this);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(ImageToPDF.this, ll_banner);
                    break;
                case "a":
                    AppLovinAds.AppLovinBanner(ImageToPDF.this, ll_banner);
                    break;
            }
        }

        init();
        initGridView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imge_to_pdf, menu);
        action_add = menu.findItem(R.id.action_add);
        action_delete = menu.findItem(R.id.action_delete);
        action_ok = menu.findItem(R.id.action_ok);
        return true;
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolBarTitle = findViewById(R.id.toolBarTitle);
        btnSelectLayout = findViewById(R.id.btnSelectLayout);
        actionBar = getSupportActionBar();
        toolBarTitle.setText(getResources().getString(R.string.imageToPdfFile));
        chooseImageLayout = findViewById(R.id.chooseImageLayout);
        recyclerViewLayout = findViewById(R.id.recyclerViewLayout);
        chooseImageLayout.setOnClickListener(this);
        findViewById(R.id.btnConvertToPDF).setOnClickListener(this);
    }

    private void initGridView() {
        gridView = findViewById(R.id.dynamic_grid);
        imageToPdfAdapter = new ImageToPdfAdp(this, mImagesList, 3);
        gridView.setAdapter(imageToPdfAdapter);
        gridView.setOnItemClickListener((adapterView, view, i, j) -> {
            ImageModel imageModel = (ImageModel) adapterView.getAdapter().getItem(i);
            if (!gridView.isEditMode() && !isDeleteSelectionMode) {
                showOptionAlertDialog(imageModel);
            } else if (isDeleteSelectionMode) {
                imageModel.setSelected(!imageModel.isSelected());
                imageToPdfAdapter.notifyDataSetChanged();
            }
        });
    }

    public void showOptionAlertDialog(final ImageModel imageModel) {
        new AlertDialog.Builder(this).setItems(new String[]{getResources().getString(R.string.remove), getResources().getString(R.string.rearrange)}, (dialogInterface, i) -> {
            if (i == 0) {
                isDeleteSelectionMode = true;
                imageModel.setSelected(true);
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
            }
        }).create().show();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btnConvertToPDF) {
            if (id == R.id.chooseImageLayout) {
                openFolderViewActivity();
            }
        } else if (Build.VERSION.SDK_INT >= 20) {
            Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.setType("application/pdf");
            intent.putExtra("android.intent.extra.TITLE", "IMG_To_PDF_" + System.currentTimeMillis() + ".pdf");
            mPdfUriResult.launch(Intent.createChooser(intent, "Select Files Path"));
        } else {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_set_file_name, null);
            bottomSheetDialog.setContentView(inflate);

            final EditText editText = inflate.findViewById(R.id.editText);
            editText.setText(Utility.getDateTime());

            inflate.findViewById(R.id.setNameBtn).setOnClickListener(view1 -> {
                if (editText.getText().toString().length() > 0) {
                    String obj = editText.getText().toString();
                    convertToPDF(obj + ".pdf", null);
                    bottomSheetDialog.dismiss();
                    return;
                }

                Utility.Toast(ImageToPDF.this, getResources().getString(R.string.enterFileNameFirst));
            });

            inflate.findViewById(R.id.btnCancel).setOnClickListener(view13 -> bottomSheetDialog.dismiss());

            bottomSheetDialog.show();
        }
    }

    public void convertToPDF(final String str, Uri uri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.creatingNewFile));
        progressDialog.setCancelable(false);
        progressDialog.show();
        new CoroutinesTask(obj -> runOnUiThread(() -> {
            final String str1 = (String) obj;
            Utility.logCatMsg("file a path " + str1);
            progressDialog.dismiss();
            if (str1 != null) {
                Singleton.getInstance().setFileDeleted(true);
                new AlertDialog.Builder(ImageToPDF.this).setMessage(getResources().getString(R.string.pdfFileCreatedSuccessfully)).setPositiveButton(getResources().getString(R.string.openThisFile), (dialogInterface, i) -> {
                    if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
                        switch (prefs.getAds_name()) {
                            case "g":
                                if (Advertisement.adsdisplay) {
                                    Advertisement.FullScreenLoad(ImageToPDF.this, () -> {
                                        Advertisement.allcount60.start();
                                        IntentPDFView(str1);
                                        dialogInterface.dismiss();
                                    });
                                } else {
                                    IntentPDFView(str1);
                                    dialogInterface.dismiss();
                                }
                                break;
                            case "a":
                                if (Advertisement.adsdisplay) {
                                    AppLovinAds.AppLovinFullScreenShow(() -> {
                                        Advertisement.allcount60.start();
                                        IntentPDFView(str1);
                                        dialogInterface.dismiss();
                                    });
                                } else {
                                    IntentPDFView(str1);
                                    dialogInterface.dismiss();
                                }
                                break;

                        }
                    } else {
                        IntentPDFView(str1);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
            }
        })).convertToPDF(this, imageToPdfAdapter.getItems(), str, uri);
    }

    private void IntentPDFView(String str1) {
        Intent intent = new Intent(ImageToPDF.this, PDFViewWebViewBase.class);
        intent.putExtra("fileType", "3");
        intent.putExtra("name", str1);
        intent.putExtra("path", str1);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.action_add:
                openFolderViewActivity();
                break;
            case R.id.action_delete:
                selectedImage.clear();
                List<Object> items = imageToPdfAdapter.getItems();
                for (int i = 0; i < mImagesList.size(); i++) {
                    ImageModel imageModel = (ImageModel) mImagesList.get(i);
                    if (imageModel.isSelected()) {
                        items.remove(imageModel);
                    }
                }
                mImagesList.clear();
                mImagesList.addAll(items);
                imageToPdfAdapter.set(mImagesList);
                imageToPdfAdapter.notifyDataSetChanged();
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
                toolBarTitle.setText(getResources().getString(R.string.rearrangeImages));
            }
            changeBackGroundColor(3);
        } else {
            List<Object> items = imageToPdfAdapter.getItems();
            for (int i = 0; i < items.size(); i++) {
                ((ImageModel) items.get(i)).setSelected(z);
            }
            gridView.stopEditMode();
            backToOriginal();
        }
        imageToPdfAdapter.notifyDataSetChanged();
    }

    private void backToOriginal() {
        actionBar.setHomeAsUpIndicator(null);
        toolBarTitle.setText(getResources().getString(R.string.imageToPdfFile));
        action_add.setVisible(true);
        action_delete.setVisible(false);
        action_ok.setVisible(false);
        isDeleteSelectionMode = false;
        if (mImagesList.size() > 0) {
            btnSelectLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerViewLayout.setVisibility(View.GONE);
            chooseImageLayout.setVisibility(View.VISIBLE);
            btnSelectLayout.setVisibility(View.VISIBLE);
        }
        changeBackGroundColor(100);
    }

    @Override
    public void onDestroy() {
        Utility.deleteTempFolder(this);
        mImagesList.clear();
        super.onDestroy();
    }

    private void openFolderViewActivity() {
        if (new PhotoUtil(this, null).hasFolderColumnAvailable()) {
            Intent intent = new Intent(this, SelectImage.class);
            intent.putExtra("isChooseOnlyOneImage", false);
            mStartForResult.launch(intent);
            return;
        }
        Intent intent2 = new Intent();
        intent2.setType("image/*");
        intent2.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent2.setAction("android.intent.action.GET_CONTENT");
        mPhotoForResult.launch(Intent.createChooser(intent2, "Select Picture"));
    }
}