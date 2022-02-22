package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView.CropImage;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.CropImageView.CropImageView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.PhotoUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class OCRText extends BaseActivity implements View.OnClickListener {
    public BottomSheetBehavior<View> bottomSheetBehavior;
    public RelativeLayout chooseImageLayout;
    public ImageView arrowUpDown;
    public ImageView resultImageView;
    public TextView ocrTextView;
    public SharedPrefs prefs;
    public Bitmap cropedImageBitmap = null;
    public Uri selectedImageUri = null;

    ActivityResultLauncher<Intent> mPdfUriResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult activityResult) {
            Intent data = activityResult.getData();
            if (data != null) {
                if (activityResult.getResultCode() == -1 && data.getData() != null) {
                    onFileCreated(Utility.writeStringAsPDF(OCRText.this, ocrTextView.getText().toString(), data.getData()));
                }
            }
        }
    });

    ActivityResultLauncher<Intent> mPhotoForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult activityResult) {
            Intent data = activityResult.getData();
            if (data != null) {
                if (activityResult.getResultCode() == -1) {
                    selectedImageUri = data.getData();
                    resultImageView.setImageURI(selectedImageUri);
                    CropImage.activity(selectedImageUri).start(OCRText.this);
                }
            }
        }
    });

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult activityResult) {
            if (activityResult.getData() != null && activityResult.getResultCode() == -1) {
                String stringExtra = activityResult.getData().getStringExtra("result");
                selectedImageUri = Uri.parse(stringExtra);
                resultImageView.setImageURI(selectedImageUri);
                CropImage.activity(selectedImageUri).start(OCRText.this);
            }
        }
    });

    ActivityResultLauncher<Intent> mTxtUriResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult activityResult) {
            Intent data = activityResult.getData();
            if (data != null)
                if (activityResult.getResultCode() == -1 && data.getData() != null) {
                    onFileCreated(Utility.writeStringAsFile(OCRText.this, ocrTextView.getText().toString(), data.getData()));
                }
        }
    });

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ocr_text);
        changeBackGroundColor(100);

        prefs = new SharedPrefs(this);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ocrTextView = findViewById(R.id.ocrTextView);
        resultImageView = findViewById(R.id.resultImageView);
        chooseImageLayout = findViewById(R.id.chooseImageLayout);
        arrowUpDown = findViewById(R.id.arrowUpDown);
        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.imagToText));

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == 1) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == 3) {
                    arrowUpDown.setImageDrawable(ContextCompat.getDrawable(OCRText.this, R.drawable.ic_arrow_down));
                } else {
                    arrowUpDown.setImageDrawable(ContextCompat.getDrawable(OCRText.this, R.drawable.ic_arrow_up));
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        findViewById(R.id.share).setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.copyIv).setOnClickListener(this);
        findViewById(R.id.moreIv).setOnClickListener(this);
        findViewById(R.id.orcResultTitle).setOnClickListener(this);
        findViewById(R.id.fab).setOnClickListener(this);
        arrowUpDown.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ocr, menu);
        return true;
    }

    private void inspect(Uri uri) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            options.inScreenDensity = DisplayMetrics.DENSITY_LOW;
            bitmap = BitmapFactory.decodeStream(is, null, options);
            inspectFromBitmap(bitmap);
        } catch (FileNotFoundException e) {
            Log.w("TAG", "Failed to find the file: " + uri, e);
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.w("TAG", "Failed to close InputStream", e);
                }
            }
        }
    }

    private void inspectFromBitmap(Bitmap bitmap) {
        TextRecognizer build = new TextRecognizer.Builder(this).build();
        try {
            if (!build.isOperational()) {
                new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.textRecognizerCouldNotBeSetUpOnYourDevice)).show();
                ocrTextView.setText("");
                return;
            }

            SparseArray<TextBlock> detect = build.detect(new Frame.Builder().setBitmap(bitmap).build());
            ArrayList<TextBlock> arrayList = new ArrayList<>();
            for (int i = 0; i < detect.size(); i++) {
                arrayList.add(detect.valueAt(i));
            }

            Collections.sort(arrayList, (textBlock, textBlock2) -> {
                int i = textBlock.getBoundingBox().top - textBlock2.getBoundingBox().top;
                return i != 0 ? i : textBlock.getBoundingBox().left - textBlock2.getBoundingBox().left;
            });

            StringBuilder sb = new StringBuilder();
            for (TextBlock textBlock : arrayList) {
                if (!(textBlock == null)) {
                    sb.append(textBlock.getValue());
                    sb.append("\n");
                }
            }
            ocrTextView.setText(sb);
            if (sb.toString().trim().length() > 0) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                Utility.Toast(this, getResources().getString(R.string.noTextRecognizeOnThisImagePleaseTryAgain));
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
            build.release();
        } finally {
            build.release();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            if (i == 203) {
                CropImage.ActivityResult activityResult = CropImage.getActivityResult(intent);
                selectedImageUri = null;
                cropedImageBitmap = null;
                if (activityResult != null) {
                    handleCropResult(activityResult);
                }
            } else if (i == 10) {
                Uri fromFile = Uri.fromFile(new File(intent.getStringExtra("result")));
                selectedImageUri = fromFile;
                resultImageView.setImageURI(fromFile);
                CropImage.activity(selectedImageUri).start(this);
            }
        } else if (i == 203) {
            Uri uri = selectedImageUri;
            if (uri != null) {
                resultImageView.setImageURI(uri);
                inspect(selectedImageUri);
                chooseImageLayout.setVisibility(View.GONE);
                return;
            }
            Bitmap bitmap = cropedImageBitmap;
            if (bitmap != null) {
                resultImageView.setImageBitmap(bitmap);
                inspectFromBitmap(cropedImageBitmap);
                chooseImageLayout.setVisibility(View.GONE);
            }
        }
    }

    private void handleCropResult(CropImageView.CropResult cropResult) {
        if (cropResult.getError() == null) {
            if (cropResult.getUri() != null) {
                Uri uri = cropResult.getUri();
                selectedImageUri = uri;
                resultImageView.setImageURI(uri);
                inspect(selectedImageUri);
            } else {
                Bitmap bitmap = cropResult.getBitmap();
                cropedImageBitmap = bitmap;
                resultImageView.setImageBitmap(bitmap);
                inspectFromBitmap(cropedImageBitmap);
            }
            chooseImageLayout.setVisibility(View.GONE);
            return;
        }
        Utility.logCatMsg("Failed to crop image " + cropResult.getError());
        Utility.Toast(this, getResources().getString(R.string.imageCropFaild) + cropResult.getError().getMessage());
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrowUpDown:
            case R.id.orcResultTitle:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                return;
            case R.id.fab:
                chooseImage();
                return;
            case R.id.copyIv:
                copyTextToClipBoard();
                return;
            case R.id.edit:
                Intent intent = new Intent(this, OCREditText.class);
                intent.putExtra("ocrText", ocrTextView.getText().toString());
                startActivity(intent);
                return;
            case R.id.moreIv:
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.inflate(R.menu.menu_ocr_save);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.action_open_as_pdf:
                            Utility.openStringOrPDFAsFile(this, ocrTextView.getText().toString(), 1);
                            break;
                        case R.id.action_open_as_text:
                            Utility.openStringOrPDFAsFile(this, ocrTextView.getText().toString(), 0);
                            break;
                        case R.id.action_save_as_pdf:
                            Intent intent1 = new Intent("android.intent.action.CREATE_DOCUMENT");
                            intent1.addCategory("android.intent.category.OPENABLE");
                            intent1.setType("application/pdf");
                            intent1.putExtra("android.intent.extra.TITLE", "Txt_To_PDF_" + System.currentTimeMillis() + ".pdf");
                            mPdfUriResult.launch(Intent.createChooser(intent1, "Select Files Path"));
                            break;
                        case R.id.action_save_as_text:
                            Intent intent2 = new Intent("android.intent.action.CREATE_DOCUMENT");
                            intent2.addCategory("android.intent.category.OPENABLE");
                            intent2.setType("text/plain");
                            intent2.putExtra("android.intent.extra.TITLE", "Txt_" + System.currentTimeMillis() + ".txt");
                            mTxtUriResult.launch(Intent.createChooser(intent2, "Select Files Path"));
                            break;
                    }
                    return false;
                });
                popupMenu.show();
                return;
            case R.id.share:
                shareOCRText();
                return;
            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.action_add) {
            chooseImage();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void copyTextToClipBoard() {
        ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("label", ocrTextView.getText().toString()));
        Utility.Toast(this, getResources().getString(R.string.copyTextToClipBoard));
    }

    private void shareOCRText() {
        try {
            String charSequence = ocrTextView.getText().toString();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", charSequence);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.shareVia)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFileNameAlertDialog(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.fileName));
        if (i == 0) {
            builder.setMessage(getResources().getString(R.string.setTextFileName));
        } else {
            builder.setMessage(getResources().getString(R.string.setPdfFileName));
        }
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(10, 0, 10, 0);
        editText.setLayoutParams(layoutParams);
        builder.setView(editText);
        builder.setPositiveButton(getResources().getString(R.string.setName), (dialogInterface, i1) -> {
            if (editText.getText().toString().length() > 0) {
                if (i1 == 0) {
                    createTextOrPDF_File(editText.getText().toString(), ".txt", i1);
                } else {
                    createTextOrPDF_File(editText.getText().toString(), ".pdf", i1);
                }
                dialogInterface.dismiss();
                return;
            }
            Utility.Toast(OCRText.this, getResources().getString(R.string.enterFileNameFirst));
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i12) -> dialogInterface.cancel());
        builder.show();
    }

    public void onFileCreated(final String str) {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.fileSaved)).setPositiveButton(getResources().getString(R.string.openThisFile), (dialogInterface, i) -> {
            Intent intent;
            if (str.endsWith(".txt")) {
                intent = new Intent(OCRText.this, FilesView.class);
                intent.putExtra("fileType", "4");
                intent.putExtra("name", "Txt File");
            } else {
                intent = new Intent(OCRText.this, PDFViewWebViewBase.class);
                intent.putExtra("fileType", "3");
                intent.putExtra("name", "PDF File");
            }
            intent.putExtra("path", str);
            startActivity(intent);
            dialogInterface.dismiss();
        }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }

    public void createTextOrPDF_File(String str, String str2, final int i) {
        final String str3;
        final String str4 = str + str2;
        if (i == 0) {
            str3 = Utility.writeStringAsFile(this, ocrTextView.getText().toString(), str4);
        } else {
            str3 = Utility.writeStringAsPDF(this, "", ocrTextView.getText().toString(), str4);
        }
        if (str3 != null) {
            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.fileSaved)).setMessage(str4 + getResources().getString(R.string.fileHasBeenSavedTo) + getResources().getString(R.string.app_folder_name)).setPositiveButton(getResources().getString(R.string.openThisFile), (dialogInterface, i1) -> {
                Intent intent;
                if (i1 == 0) {
                    intent = new Intent(OCRText.this, FilesView.class);
                    intent.putExtra("fileType", "4");
                } else {
                    intent = new Intent(OCRText.this, PDFViewWebViewBase.class);
                    intent.putExtra("fileType", "3");
                }
                intent.putExtra("path", str3);
                intent.putExtra("name", str4);
                startActivity(intent);
                dialogInterface.dismiss();
            }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i12) -> dialogInterface.dismiss()).create().show();
        }
    }

    private void chooseImage() {
        if (new PhotoUtil(this, null).hasFolderColumnAvailable()) {
            Intent intent = new Intent(this, SelectImage.class);
            intent.putExtra("isChooseOnlyOneImage", true);
            mStartForResult.launch(intent);
            return;
        }
        Intent intent2 = new Intent();
        intent2.setType("image/*");
        intent2.setAction("android.intent.action.GET_CONTENT");
        mPhotoForResult.launch(Intent.createChooser(intent2, "Select Picture"));
    }
}
