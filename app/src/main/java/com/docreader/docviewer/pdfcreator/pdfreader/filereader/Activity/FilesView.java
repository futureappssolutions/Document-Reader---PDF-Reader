package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Main.ActMain;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.IOfficeToPicture;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.EventConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.macro.DialogListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.res.ResKit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.ss.sheetbar.SheetBar;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.IControl;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.IMainFrame;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.MainControl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilesView extends BaseActivity implements IMainFrame {
    private LinearLayout appFrame;
    private final Object f799bg = -7829368;
    private SheetBar bottomBar;
    private MainControl control;
    private String filePath;
    private String tempFilePath;
    private Toast toast;
    private int applicationType = -1;
    private boolean isDispose;
    private boolean isFromAppActivity = false;
    private boolean isThumbnail;
    private boolean writeLog = true;

    public void changePage() {
    }

    public void changeZoom() {
    }

    public void completeLayout() {
    }

    public void error(int i) {
    }

    public void fullScreen(boolean z) {
    }

    public Activity getActivity() {
        return this;
    }

    public DialogListener getDialogListener() {
        return null;
    }

    public byte getPageListViewMovingPosition() {
        return 0;
    }

    public String getTXTDefaultEncode() {
        return "GBK";
    }

    public int getTopBarHeight() {
        return 0;
    }

    public byte getWordDefaultView() {
        return 0;
    }

    public boolean isChangePage() {
        return true;
    }

    public boolean isDrawPageNumber() {
        return true;
    }

    public boolean isIgnoreOriginalSize() {
        return false;
    }

    public boolean isPopUpErrorDlg() {
        return true;
    }

    public boolean isShowFindDlg() {
        return true;
    }

    public boolean isShowPasswordDlg() {
        return true;
    }

    public boolean isShowProgressBar() {
        return true;
    }

    public boolean isShowTXTEncodeDlg() {
        return true;
    }

    public boolean isShowZoomingMsg() {
        return true;
    }

    public boolean isTouchZoom() {
        return true;
    }

    public boolean isZoomAfterLayoutForWord() {
        return true;
    }

    public void onCurrentPageChange() {
    }

    public boolean onEventMethod(View view, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, byte b) {
        return false;
    }

    public void onPagesCountChange() {
    }

    public void setFindBackForwardState(boolean z) {
    }

    public void setIgnoreOriginalSize(boolean z) {
    }

    public void updateViewImages(List<Integer> list) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_file_view);

        Toolbar toolbar = findViewById(R.id.toolBar);
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        control = new MainControl(this);
        appFrame = findViewById(R.id.appFrame);

        SharedPrefs prefs = new SharedPrefs(FilesView.this);
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAds.showBannerAds(FilesView.this, ll_banner);

        if (getIntent() != null) {
            filePath = getIntent().getStringExtra("path");
            String fileName = getIntent().getStringExtra("name");
            isFromAppActivity = getIntent().getBooleanExtra("fromAppActivity", false);
            changeBackGroundColor(Integer.parseInt(getIntent().getStringExtra("fileType")));
            toolBarTitle.setText(fileName);
        }

        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        createView();
        control.openFile(filePath);
        control.setOffictToPicture(new IOfficeToPicture() {
            private Bitmap bitmap;

            public void dispose() {
            }

            public byte getModeType() {
                return 1;
            }

            public boolean isZoom() {
                return false;
            }

            public void setModeType(byte b) {
            }

            public Bitmap getBitmap(int i, int i2) {
                if (i == 0 || i2 == 0) {
                    return null;
                }
                if (!(bitmap != null && bitmap.getWidth() == i && bitmap.getHeight() == i2)) {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    bitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
                }
                return bitmap;
            }

            public void callBack(Bitmap bitmap2) {
                saveBitmapToFile(bitmap2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        if (Build.VERSION.SDK_INT >= 21) {
            return true;
        }
        menu.findItem(R.id.ic_full_screen_view).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.ic_full_screen_view) {
            hideSystemUI();
        } else if (itemId == R.id.ic_share) {
            Utility.shareFile(this, filePath);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void saveBitmapToFile(Bitmap bitmap) {
        if (bitmap != null) {
            if (tempFilePath == null) {
                if ("mounted".equals(Environment.getExternalStorageState())) {
                    tempFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
                File file = new File(tempFilePath + File.separatorChar + "tempPic");
                if (!file.exists()) {
                    file.mkdir();
                }
                tempFilePath = file.getAbsolutePath();
            }
            File file2 = new File(tempFilePath + File.separatorChar + "export_image.jpg");
            try {
                if (file2.exists()) {
                    file2.delete();
                }
                file2.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        Object actionValue = control.getActionValue(EventConstant.PG_SLIDESHOW, null);
        if (actionValue == null || !(Boolean) actionValue) {
            if (control.getReader() != null) {
                control.getReader().abortReader();
            }
            if (control == null || !control.isAutoTest()) {
                if (isFromAppActivity) {
                    startActivity(new Intent(this, ActMain.class));
                }
                super.onBackPressed();
                return;
            }
            System.exit(0);
            return;
        }
        fullScreen(false);
        control.actionEvent(EventConstant.PG_SLIDESHOW_END, null);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void onDestroy() {
        dispose();
        super.onDestroy();
    }

    public void showProgressBar(boolean z) {
        setProgressBarIndeterminateVisibility(z);
    }

    private void createView() {
        String lowerCase = filePath.toLowerCase();
        if (lowerCase.endsWith(MainConstant.FILE_TYPE_DOC) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOCX) || lowerCase.endsWith(MainConstant.FILE_TYPE_TXT) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOT) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTM)) {
            applicationType = 0;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_XLS) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLT) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSM)) {
            applicationType = 1;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_PPT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTM)) {
            applicationType = 2;
        } else if (lowerCase.endsWith("pdf")) {
            applicationType = 3;
        } else {
            applicationType = 0;
        }
    }

    public void fileShare() {
        ArrayList<Uri> arrayList = new ArrayList<>();
        arrayList.add(Uri.fromFile(new File(filePath)));
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.putExtra("android.intent.extra.STREAM", arrayList);
        intent.setType("application/octet-stream");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.sys_share_title)));
    }

    @Override
    public Dialog onCreateDialog(int i) {
        return control.getDialog(this, i);
    }

    public void updateToolsbarStatus() {
        if (appFrame != null && !isDispose) {
            int childCount = appFrame.getChildCount();
            for (int i = 0; i < childCount; i++) {
                appFrame.getChildAt(i);
            }
        }
    }

    public IControl getControl() {
        return control;
    }

    public int getApplicationType() {
        return applicationType;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean doActionEvent(int i, Object obj) {
        if (i == 0) {
            onBackPressed();
        } else if (i != 15) {
            if (i == 20) {
                updateToolsbarStatus();
            } else if (i == 25) {
                setTitle((String) obj);
            } else if (i != 268435464) {
                if (i == 536870913) {
                    fileShare();
                } else if (i == 788529152) {
                    String trim = ((String) obj).trim();
                    if (trim.length() <= 0 || !control.getFind().find(trim)) {
                        setFindBackForwardState(false);
                        toast.setText(getLocalString("DIALOG_FIND_NOT_FOUND"));
                        toast.show();
                    } else {
                        setFindBackForwardState(true);
                    }
                } else if (i != 1073741828) {
                    switch (i) {
                        case EventConstant.APP_DRAW_ID:
                            control.getSysKit().getCalloutManager().setDrawingMode(1);
                            appFrame.post(() -> control.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null));
                            break;
                        case EventConstant.APP_BACK_ID:
                            control.getSysKit().getCalloutManager().setDrawingMode(0);
                            break;
                        case EventConstant.APP_PEN_ID:
                            if (!(Boolean) obj) {
                                control.getSysKit().getCalloutManager().setDrawingMode(0);
                                break;
                            } else {
                                control.getSysKit().getCalloutManager().setDrawingMode(1);
                                appFrame.post(() -> control.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null));
                                break;
                            }
                        case EventConstant.APP_ERASER_ID:
                            try {
                                if (!(Boolean) obj) {
                                    control.getSysKit().getCalloutManager().setDrawingMode(0);
                                    break;
                                } else {
                                    control.getSysKit().getCalloutManager().setDrawingMode(2);
                                    break;
                                }
                            } catch (Exception e) {
                                control.getSysKit().getErrorKit().writerLog(e);
                                break;
                            }
                        default:
                            return false;
                    }
                } else {
                    bottomBar.setFocusSheetButton((Integer) obj);
                }
            }
        }
        return true;
    }

    public void openFileFinish() {
        View view = new View(getApplicationContext());
        view.setBackgroundColor(-7829368);
        appFrame.addView(view, new LinearLayout.LayoutParams(-1, 1));
        appFrame.addView(control.getView(), new LinearLayout.LayoutParams(-1, -1));
    }

    public int getBottomBarHeight() {
        SheetBar sheetBar = bottomBar;
        if (sheetBar != null) {
            return sheetBar.getSheetbarHeight();
        }
        return 0;
    }

    public String getAppName() {
        return getString(R.string.sys_name);
    }

    public void destroyEngine() {
        super.onBackPressed();
    }

    public String getLocalString(String str) {
        return ResKit.instance().getLocalString(str);
    }

    public void setWriteLog(boolean z) {
        writeLog = z;
    }

    public boolean isWriteLog() {
        return writeLog;
    }

    public void setThumbnail(boolean z) {
        isThumbnail = z;
    }

    public Object getViewBackground() {
        return f799bg;
    }

    public boolean isThumbnail() {
        return isThumbnail;
    }

    public File getTemporaryDirectory() {
        File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir != null) {
            return externalFilesDir;
        }
        return getFilesDir();
    }

    public void dispose() {
        isDispose = true;
        if (control != null) {
            control.dispose();
            control = null;
        }
        bottomBar = null;
        if (appFrame != null) {
            int childCount = appFrame.getChildCount();
            for (int i = 0; i < childCount; i++) {
                appFrame.getChildAt(i);
            }
            appFrame = null;
        }
    }
}
