package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.UI.CSVFileViewerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.UriInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;

import java.util.Objects;

public class AllFilesConverterOption extends BaseActivity implements View.OnClickListener {
    public SharedPrefs prefs;

    ActivityResultLauncher<Intent> mFileResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        Intent intent;
        Intent data = activityResult.getData();
        if (activityResult.getResultCode() != -1) {
            return;
        }
        if (data == null || data.getData() == null) {
            Utility.logCatMsg("File uri not found {}");
            return;
        }
        UriInfo uriInfo = Utility.getUriInfo(AllFilesConverterOption.this, data.getData());
        if (uriInfo != null) {
            int fileType = MainConstant.getFileType(uriInfo.getFilePath());
            if (fileType != 5) {
                new Intent(AllFilesConverterOption.this, FilesView.class);
                if (fileType == 10) {
                    intent = new Intent(AllFilesConverterOption.this, CSVFileViewerActivity.class);
                } else if (fileType == 13) {
                    intent = new Intent(AllFilesConverterOption.this, RTFView.class);
                } else {
                    Utility.Toast(AllFilesConverterOption.this, "Please choose CSV or RTF file only");
                    return;
                }
                intent.putExtra("path", uriInfo.getFilePath());
                intent.putExtra("name", uriInfo.getFileName());
                intent.putExtra("fromConverterApp", true);
                intent.putExtra("fileType", fileType + "");
                startActivity(intent);
                return;
            }
            Utility.Toast(AllFilesConverterOption.this, "This file is not supported to open");
        }
    });

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_files_converter);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        changeBackGroundColor(100);

        prefs = new SharedPrefs(this);

        FrameLayout fl_native = findViewById(R.id.fl_native);
        GoogleAds.showNativeAds(AllFilesConverterOption.this,fl_native);


        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.allFilesConverter));
        findViewById(R.id.csvToPDF).setOnClickListener(this);
        findViewById(R.id.rtfToPDF).setOnClickListener(this);
        findViewById(R.id.imageToPdfLayout).setOnClickListener(this);
        findViewById(R.id.mergePDFLayout).setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startActivity(int i) {
        if (prefs.isLoadAllFilesAtOnce()) {
            if (GoogleAds.adsdisplay) {
                GoogleAds.showFullAds(AllFilesConverterOption.this, () -> {
                    GoogleAds.allcount60.start();
                    IntentFileList(i);
                });
            } else {
                IntentFileList(i);
            }

            return;
        }
        Intent intent2 = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent2.addCategory("android.intent.category.OPENABLE");
        if (i == 10) {
            intent2.setType("text/csv");
        } else if (i == 13) {
            intent2.setType("text/rtf");
        }
        mFileResult.launch(Intent.createChooser(intent2, "Select Files"));
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rtfToPDF) {
            startActivity(13);
        } else if (id == R.id.csvToPDF) {
            startActivity(10);
        } else if (id == R.id.imageToPdfLayout) {
            if (GoogleAds.adsdisplay) {
                GoogleAds.showFullAds(AllFilesConverterOption.this, () -> {
                    GoogleAds.allcount60.start();
                    IntentImagePDF();
                });
            } else {
                IntentImagePDF();
            }
        } else if (id == R.id.mergePDFLayout) {
            if (GoogleAds.adsdisplay) {
                GoogleAds.showFullAds(AllFilesConverterOption.this, () -> {
                    GoogleAds.allcount60.start();
                    IntentMergePDF();
                });
            } else {
                IntentMergePDF();
            }
        }
    }

    private void IntentFileList(int i) {
        Intent intent = new Intent(this, FilesList.class);
        intent.putExtra("fileType", i + "");
        intent.putExtra("fromConverterApp", true);
        startActivity(intent);
    }

    private void IntentMergePDF() {
        startActivity(new Intent(this, MergePDF.class));
    }

    private void IntentImagePDF() {
        startActivity(new Intent(this, ImageToPDF.class));
    }
}
