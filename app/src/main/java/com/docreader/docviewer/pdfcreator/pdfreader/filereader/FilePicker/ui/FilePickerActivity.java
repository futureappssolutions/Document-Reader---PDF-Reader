package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.ui;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.FilesView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.PDFViewWebViewBase;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.RTFView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.TextViewer;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.facebookMaster;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.UI.CSVFileViewerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer.EPubFileViewerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter.FileFilter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter.PatternFilter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils.FileTypeUtils;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils.ImagePickerFileUtils;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.ContentTypes;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Pattern;

public class FilePickerActivity extends BaseActivity implements DirectoryFragment.FileClickListener {
    public static final String ARG_CLOSEABLE = "arg_closeable";
    public static final String ARG_CURRENT_FILE = "arg_current_path";
    public static final String ARG_FILTER = "arg_filter";
    public static final String ARG_START_FILE = "arg_start_path";
    public static final String ARG_TITLE = "arg_title";
    public static final String RESULT_FILE_PATH = "result_file_path";
    public static final String STATE_CURRENT_FILE = "state_current_path";
    public static final String STATE_START_FILE = "state_start_path";
    public static final int HANDLE_CLICK_DELAY = 150;
    public File clickedFile;
    public Boolean mCloseable = true;
    public File mCurrent;
    public FileFilter mFilter;
    public File mStart;
    public CharSequence mTitle;
    public Toolbar mToolbar;
    public Singleton singleton;

    public FilePickerActivity() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        mStart = externalStorageDirectory;
        mCurrent = externalStorageDirectory;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_file_picker);

        SharedPrefs prefs = new SharedPrefs(FilePickerActivity.this);
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(FilePickerActivity.this, ll_banner);
                    break;
                case "f":
                    facebookMaster.FbBanner(FilePickerActivity.this, ll_banner);
                    break;
                case "both":
                    Advertisement.GoogleBannerBoth(FilePickerActivity.this, ll_banner);
                    break;
            }
        }

        initArguments(bundle);
        initViews();
        initToolbar();
        if (bundle == null) {
            initBackStackState();
        }
    }

    private void initArguments(Bundle bundle) {
        if (getIntent().hasExtra(ARG_FILTER)) {
            Serializable serializableExtra = getIntent().getSerializableExtra(ARG_FILTER);
            if (serializableExtra instanceof Pattern) {
                mFilter = new PatternFilter((Pattern) serializableExtra, false);
            } else {
                mFilter = (FileFilter) serializableExtra;
            }
        }
        if (bundle != null) {
            mStart = (File) bundle.getSerializable(STATE_START_FILE);
            mCurrent = (File) bundle.getSerializable(STATE_CURRENT_FILE);
            updateTitle();
        } else {
            if (getIntent().hasExtra(ARG_START_FILE)) {
                File file = (File) getIntent().getSerializableExtra(ARG_START_FILE);
                mStart = file;
                mCurrent = file;
            }
            if (getIntent().hasExtra(ARG_CURRENT_FILE)) {
                File file2 = (File) getIntent().getSerializableExtra(ARG_CURRENT_FILE);
                if (ImagePickerFileUtils.isParent(file2, mStart)) {
                    mCurrent = file2;
                }
            }
        }
        if (getIntent().hasExtra(ARG_TITLE)) {
            mTitle = getIntent().getCharSequenceExtra(ARG_TITLE);
        }
        if (getIntent().hasExtra(ARG_CLOSEABLE)) {
            mCloseable = getIntent().getBooleanExtra(ARG_CLOSEABLE, true);
        }
    }

    private void initToolbar() {
        Field field;
        setSupportActionBar(mToolbar);
        changeBackGroundColor(100);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        try {
            if (TextUtils.isEmpty(mTitle)) {
                field = mToolbar.getClass().getDeclaredField("mTitleTextView");
            } else {
                field = mToolbar.getClass().getDeclaredField("mSubtitleTextView");
            }
            field.setAccessible(true);
            ((TextView) Objects.requireNonNull(field.get(mToolbar))).setEllipsize(TextUtils.TruncateAt.START);
        } catch (Exception ignored) {
        }
        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }
        updateTitle();
    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolBar);
        singleton = Singleton.getInstance();
        ((TextView) findViewById(R.id.toolBarTitle)).setText("");
    }

    private void initBackStackState() {
        ArrayList<File> arrayList = new ArrayList<>();
        for (File file = mCurrent; file != null; file = ImagePickerFileUtils.getParentOrNull(file)) {
            arrayList.add(file);
            if (file.equals(mStart)) {
                break;
            }
        }
        Collections.reverse(arrayList);
        for (File addFragmentToBackStack : arrayList) {
            addFragmentToBackStack(addFragmentToBackStack);
        }
    }

    private void updateTitle() {
        if (getSupportActionBar() != null) {
            String absolutePath = mCurrent.getAbsolutePath();
            if (TextUtils.isEmpty(mTitle)) {
                getSupportActionBar().setTitle(absolutePath);
            } else {
                getSupportActionBar().setSubtitle(absolutePath);
            }
        }
    }

    private void addFragmentToBackStack(File file) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, DirectoryFragment.getInstance(file, mFilter)).addToBackStack(null).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_close).setVisible(mCloseable);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        } else if (menuItem.getItemId() == R.id.action_close) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            mCurrent = ImagePickerFileUtils.getParentOrNull(mCurrent);
            updateTitle();
            return;
        }
        setResult(0);
        finish();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable(STATE_CURRENT_FILE, mCurrent);
        bundle.putSerializable(STATE_START_FILE, mStart);
    }

    public void onFileClicked(File file) {
        handleFileClicked(file);
        final Handler handler = new Handler();
        new Runnable() {
            public void run() {
                handler.postDelayed(this, 150);
            }
        };
    }

    public void onCurrentFileClicked() {
        handleFileClicked(mCurrent);
    }

    private void handleFileClicked(File file) {
        if (!isFinishing()) {
            if (file.isDirectory()) {
                mCurrent = file;
                if (file.getAbsolutePath().equals("/storage/emulated")) {
                    mCurrent = Environment.getExternalStorageDirectory();
                }
                addFragmentToBackStack(mCurrent);
                updateTitle();
                return;
            }
            clickedFile = file;
            setResultAndFinish(file);
        }
    }

    @SuppressLint("WrongConstant")
    private void setResultAndFinish(File file) {
        Intent intent;
        int fileType = MainConstant.getFileType(file.getPath());
        if (fileType == 3) {
            intent = new Intent(this, PDFViewWebViewBase.class);
        } else if (fileType == 6 || fileType == 11) {
            intent = new Intent(this, TextViewer.class);
        } else if (fileType == 10) {
            intent = new Intent(this, CSVFileViewerActivity.class);
        } else if (fileType == 13) {
            intent = new Intent(this, RTFView.class);
        } else if (fileType == 14) {
            intent = new Intent(this, EPubFileViewerActivity.class);
        } else if (fileType == 1) {
            intent = new Intent(this, FilesView.class);
        } else {
            intent = (fileType == 2 || fileType == 4 || fileType == 0) ? new Intent(this, FilesView.class) : null;
        }
        if (intent != null) {
            intent.putExtra("path", file.getPath());
            intent.putExtra("name", file.getName());
            intent.putExtra("fromConverterApp", false);
            intent.putExtra("fileType", fileType + "");
            startActivity(intent);
            return;
        }
        FileTypeUtils.FileType fileType2 = FileTypeUtils.getFileType(file);
        Uri uriForFile = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        if (fileType2.getDescription() == R.string.type_image) {
            Intent intent2 = new Intent();
            intent2.setAction("android.intent.action.VIEW");
            intent2.addFlags(1);
            intent2.setDataAndType(uriForFile, ContentTypes.IMAGE_PNG);
            startActivity(intent2);
        } else if (fileType2.getDescription() == R.string.type_word) {
            Intent intent3 = new Intent();
            intent3.setAction("android.intent.action.VIEW");
            intent3.addFlags(1);
            intent3.setDataAndType(uriForFile, "application/vnd.ms-excel");
            try {
                startActivity(intent3);
            } catch (ActivityNotFoundException unused) {
                Utility.Toast(this, "No Application available to view");
            }
        } else if (fileType2.getIcon() == R.drawable.icon_zip) {
            Intent intent4 = new Intent("android.intent.action.VIEW");
            intent4.setDataAndType(uriForFile, "application/zip");
            try {
                startActivity(intent4);
            } catch (ActivityNotFoundException unused2) {
                Utility.Toast(this, "No Application available to view");
            }
        } else if (fileType2.getIcon() == R.drawable.icon_music) {
            Intent intent5 = new Intent();
            intent5.setAction("android.intent.action.VIEW");
            intent5.addFlags(1);
            intent5.setDataAndType(uriForFile, "audio/*");
            startActivity(intent5);
        } else if (fileType2.getIcon() == R.drawable.ic_video) {
            Intent intent6 = new Intent();
            intent6.setAction("android.intent.action.VIEW");
            intent6.addFlags(1);
            intent6.setDataAndType(uriForFile, "video/*");
            startActivity(intent6);
        } else if (fileType2.getIcon() == R.drawable.icon_android) {
            Intent intent7 = new Intent();
            intent7.setAction("android.intent.action.VIEW");
            intent7.addFlags(1);
            intent7.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            try {
                startActivity(intent7);
            } catch (ActivityNotFoundException unused3) {
                Utility.Toast(this, "No Application available to view");
            }
        } else {
            Utility.Toast(this, "No Application available to view");
        }
    }
}
