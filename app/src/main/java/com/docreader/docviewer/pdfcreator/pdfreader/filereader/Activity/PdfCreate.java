package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.DatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.DocumentFiles;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.MRICHEditor.ActionImageView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.MRICHEditor.ActionType;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.MRICHEditor.RichEditorAction;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.MRICHEditor.RichEditorCallback;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.ContentTypes;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PdfCreate extends BaseActivity implements View.OnClickListener {
    static String data = "";
    private final int PICK_PHOTO_FOR_AVATAR = 5;
    private int documentId = 0;
    private boolean backColorSelect = false;
    private boolean forColorSelect;
    private boolean isChangeTextBackColor = false;
    private RelativeLayout colorPallateLayout;
    private DatabaseHelper databaseHelper;
    private FrameLayout flAction;
    private String htmlContent = "<p>Write Here</p>";
    private PrintJob printJob;
    private SharedPrefs prefs;
    private ImageView iv_action__txt_for_color;
    private ImageView iv_action_txt_bg_color;
    private LinearLayout llActionBarContainer;
    private RichEditorAction mRichEditorAction;
    private RichEditorCallback mRichEditorCallback;
    private final List<Integer> mActionTypeIconList = Arrays.asList(R.drawable.pc_format_bold, R.drawable.pc_format_italic, R.drawable.pc_format_underline, R.drawable.pc_format_strikethrough, R.drawable.pc_format_subscript, R.drawable.pc_format_superscript, R.drawable.pc_format_normal, R.drawable.pc_format_h1, R.drawable.pc_format_h2, R.drawable.pc_format_h3, R.drawable.pc_format_h4, R.drawable.pc_format_h5, R.drawable.pc_format_h6, R.drawable.pc_format_indent_increase, R.drawable.pc_format_indent_decrease, R.drawable.pc_format_align_left, R.drawable.pc_format_align_center, R.drawable.pc_format_align_right, R.drawable.pc_format_align_justify, R.drawable.pc_format_list_numbered, R.drawable.pc_format_list_bulleted, R.drawable.pc_insert_line, R.drawable.pc_format_code, R.drawable.pc_format_quote);
    private final List<ActionType> mActionTypeList = Arrays.asList(ActionType.BOLD, ActionType.ITALIC, ActionType.UNDERLINE, ActionType.STRIKETHROUGH, ActionType.SUBSCRIPT, ActionType.SUPERSCRIPT, ActionType.NORMAL, ActionType.H1, ActionType.H2, ActionType.H3, ActionType.H4, ActionType.H5, ActionType.H6, ActionType.INDENT, ActionType.OUTDENT, ActionType.JUSTIFY_LEFT, ActionType.JUSTIFY_CENTER, ActionType.JUSTIFY_RIGHT, ActionType.JUSTIFY_FULL, ActionType.ORDERED, ActionType.UNORDERED, ActionType.LINE, ActionType.BLOCK_CODE, ActionType.BLOCK_QUOTE);
    private final RichEditorCallback.OnGetHtmlListener onGetHtmlListener = this::lambda$new$0$CreatePdfActivity;
    private final RichEditorCallback.OnGetHtmlListener onGetHtmlListenerForBackPress = this::lambda$new$1$CreatePdfActivity;

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_create_pdf);
        setStatusBar();
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        prefs = new SharedPrefs(PdfCreate.this);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAds.showBannerAds(PdfCreate.this, ll_banner);


        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.newPdfFile));
        if (getIntent() != null) {
            documentId = Integer.parseInt(getIntent().getStringExtra("sourceFileId"));
        }
        if (CreateNewPdfFile.selectedFileSource.length() > 0) {
            htmlContent = CreateNewPdfFile.selectedFileSource;
            Utility.logCatMsg("html Content " + htmlContent);
        }

        initView();
        initClickListener();

        int applyDimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40.0f, getResources().getDisplayMetrics());
        int applyDimension2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9.0f, getResources().getDisplayMetrics());
        int size = mActionTypeList.size();
        for (int i = 0; i < size; i++) {
            final ActionImageView actionImageView = new ActionImageView(this);
            actionImageView.setLayoutParams(new LinearLayout.LayoutParams(applyDimension, applyDimension));
            actionImageView.setPadding(applyDimension2, applyDimension2, applyDimension2, applyDimension2);
            actionImageView.setActionType(mActionTypeList.get(i));
            actionImageView.setTag(mActionTypeList.get(i));
            actionImageView.setRichEditorAction(mRichEditorAction);
            actionImageView.setImageResource(mActionTypeIconList.get(i));
            actionImageView.setOnClickListener(view -> actionImageView.command());
            llActionBarContainer.addView(actionImageView);
        }

        findViewById(R.id.iv_action_txt_size).setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PdfCreate.this);
            builder.setTitle("Choose Text Size");
            final String[] strArr = {"14", "18", "22", "26", "30", "34", "38", "id", "46", "50"};
            builder.setItems(strArr, (dialogInterface, i) -> mRichEditorAction.fontSize(Double.parseDouble(strArr[i])));
            builder.create().show();
        });

        findViewById(R.id.iv_action_line_height).setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PdfCreate.this);
            builder.setTitle("Choose Line Height");
            final String[] strArr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
            builder.setItems(strArr, (dialogInterface, i) -> mRichEditorAction.lineHeight(Double.parseDouble(strArr[i])));
            builder.create().show();
        });

        findViewById(R.id.iv_action_insert_image).setOnClickListener(view -> pickImage());

        findViewById(R.id.iv_action).setOnClickListener(view -> {
            if (flAction.getVisibility() == View.VISIBLE) {
                flAction.setVisibility(View.GONE);
                return;
            }
            flAction.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.iv_action_insert_link).setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PdfCreate.this);
            View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_insert_link, null);
            bottomSheetDialog.setContentView(inflate);

            final EditText editText = inflate.findViewById(R.id.displayTextET);
            final EditText editText2 = inflate.findViewById(R.id.enterLinkEt);

            inflate.findViewById(R.id.insertBtn).setOnClickListener(view1 -> {
                if (editText.getText().toString().trim().length() <= 0 || editText2.getText().toString().trim().length() <= 0) {
                    Utility.Toast(PdfCreate.this, getResources().getString(R.string.enterValueFirst));
                    return;
                }
                mRichEditorAction.createLink(editText.getText().toString().trim(), editText2.getText().toString().trim());
                bottomSheetDialog.dismiss();
            });

            inflate.findViewById(R.id.btnCancel).setOnClickListener(view12 -> bottomSheetDialog.dismiss());

            bottomSheetDialog.show();
        });

        findViewById(R.id.iv_action_table).setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PdfCreate.this);
            View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_table_dialog, null);
            bottomSheetDialog.setContentView(inflate);

            final EditText editText = inflate.findViewById(R.id.columnET);
            final EditText editText2 = inflate.findViewById(R.id.rowET);

            inflate.findViewById(R.id.insertBtn).setOnClickListener(view14 -> {
                if (editText.getText().toString().trim().length() <= 0 || editText2.getText().toString().trim().length() <= 0) {
                    Utility.Toast(PdfCreate.this, getResources().getString(R.string.enterValueFirst));
                    return;
                }
                mRichEditorAction.insertTable(Integer.parseInt(editText.getText().toString().trim()), Integer.parseInt(editText2.getText().toString().trim()));
                bottomSheetDialog.dismiss();
            });

            inflate.findViewById(R.id.btnCancel).setOnClickListener(view15 -> bottomSheetDialog.dismiss());

            bottomSheetDialog.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_pdf, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_undo) {
            mRichEditorAction.undo();
            return true;
        } else if (itemId == R.id.action_redo) {
            mRichEditorAction.redo();
            return true;
        } else if (itemId != R.id.action_preview) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            mRichEditorAction.refreshHtml(mRichEditorCallback, onGetHtmlListener);
            return true;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        WebView mWebView = findViewById(R.id.wv_container);
        flAction = findViewById(R.id.fl_action);
        llActionBarContainer = findViewById(R.id.ll_action_bar_container);
        iv_action_txt_bg_color = findViewById(R.id.iv_action_txt_bg_color);
        iv_action__txt_for_color = findViewById(R.id.iv_action__txt_for_color);
        colorPallateLayout = findViewById(R.id.colorPallateLayout);

        mWebView.setWebChromeClient(new CustomWebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mRichEditorCallback = new MRichEditorCallback();
        mWebView.addJavascriptInterface(mRichEditorCallback, "MRichEditor");
        mWebView.loadUrl("file:///android_asset/richEditor.html");
        mRichEditorAction = new RichEditorAction(mWebView);
        databaseHelper = new DatabaseHelper(this);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cancelLayout) {
            colorPallateLayout.setVisibility(View.GONE);
        } else if (id == R.id.iv_action__txt_for_color) {
            isChangeTextBackColor = false;
            colorPallateLayout.setVisibility(View.VISIBLE);
        } else if (id != R.id.iv_action_txt_bg_color) {
            switch (id) {
                case R.id.color1:
                    changeTextForAndBackColor("AntiqueWhite");
                    return;
                case R.id.color10:
                    changeTextForAndBackColor("Chocolate");
                    return;
                case R.id.color11:
                    changeTextForAndBackColor("Coral");
                    return;
                case R.id.color12:
                    changeTextForAndBackColor("CornflowerBlue");
                    return;
                case R.id.color13:
                    changeTextForAndBackColor("Crimson");
                    return;
                case R.id.color14:
                    changeTextForAndBackColor("DarkBlue");
                    return;
                case R.id.color15:
                    changeTextForAndBackColor("DarkGreen");
                    return;
                case R.id.color16:
                    changeTextForAndBackColor("DarkKhaki");
                    return;
                case R.id.color17:
                    changeTextForAndBackColor("DarkRed");
                    return;
                case R.id.color18:
                    changeTextForAndBackColor("DarkSlateGray");
                    return;
                case R.id.color19:
                    changeTextForAndBackColor("FireBrick");
                    return;
                case R.id.color2:
                    changeTextForAndBackColor("Aqua");
                    return;
                case R.id.color20:
                    changeTextForAndBackColor("Gold");
                    return;
                case R.id.color21:
                    changeTextForAndBackColor("HotPink");
                    return;
                case R.id.color22:
                    changeTextForAndBackColor("Indigo");
                    return;
                case R.id.color23:
                    changeTextForAndBackColor("LightBlue");
                    return;
                case R.id.color24:
                    changeTextForAndBackColor("LightCyan");
                    return;
                case R.id.color25:
                    changeTextForAndBackColor("LightGreen");
                    return;
                case R.id.color3:
                    changeTextForAndBackColor("Aquamarine");
                    return;
                case R.id.color4:
                    changeTextForAndBackColor("Black");
                    return;
                case R.id.color5:
                    changeTextForAndBackColor("Blue");
                    return;
                case R.id.color6:
                    changeTextForAndBackColor("BlueViolet");
                    return;
                case R.id.color7:
                    changeTextForAndBackColor("Brown");
                    return;
                case R.id.color8:
                    changeTextForAndBackColor("CadetBlue");
                    return;
                case R.id.color9:
                    changeTextForAndBackColor("Chartreuse");
                    return;
                case R.id.colorNone:
                    if (isChangeTextBackColor) {
                        mRichEditorAction.backColor("white");
                        backColorSelect = false;
                    } else {
                        mRichEditorAction.foreColor("black");
                        forColorSelect = false;
                    }
                    updateBackAndForColor();
                    colorPallateLayout.setVisibility(View.GONE);
                    return;
                default:
            }
        } else {
            isChangeTextBackColor = true;
            colorPallateLayout.setVisibility(View.VISIBLE);
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {
        private CustomWebChromeClient() {
        }

        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i == 100 && !TextUtils.isEmpty(htmlContent)) {
                mRichEditorAction.insertHtml(htmlContent);
            }
        }

        @Override
        public void onReceivedTitle(WebView webView, String str) {
            super.onReceivedTitle(webView, str);
        }
    }

    static class MRichEditorCallback extends RichEditorCallback {
        public void notifyFontStyleChange(ActionType actionType, String str) {
        }

        MRichEditorCallback() {
        }
    }

    public void lambda$new$0$CreatePdfActivity(String str) {
        if (TextUtils.isEmpty(str)) {
            Utility.Toast(this, getResources().getString(R.string.emptyData));
            return;
        }
        data = str;
        if (GoogleAds.adsdisplay) {
            GoogleAds.showFullAds(PdfCreate.this, () -> {
                GoogleAds.allcount60.start();
                startActivity(new Intent(this, PrintPreview.class));
            });
        } else {
            startActivity(new Intent(this, PrintPreview.class));
        }
    }

    public void lambda$new$1$CreatePdfActivity(final String str) {
        if (TextUtils.isEmpty(str)) {
            finish();
        } else if (documentId != 0) {
            databaseHelper.updateFileContent(documentId + "", str);
            finish();
        } else {
            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.saveAsSourceFile)).setMessage(getResources().getString(R.string.doYouWantToSaveMessage)).setCancelable(false).setPositiveButton(getResources().getString(R.string.save), (dialogInterface, i) -> {
                dialogInterface.dismiss();
                DocumentFiles documentFiles = new DocumentFiles();
                documentFiles.setFileName("Source_File_" + Utility.getCurrentDateTime());
                documentFiles.setCreatedAt(Utility.getCurrentDateTime());
                documentFiles.setUpdatedAt(Utility.getCurrentDateTime());
                documentFiles.setFileType(3);
                documentFiles.setFileContent(str);
                documentId = databaseHelper.insertIntoTblDocumentFile(documentFiles);
                Utility.Toast(PdfCreate.this, getResources().getString(R.string.fileSaved));
                finish();
            }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss()).setNeutralButton(getResources().getString(R.string.dontSave), (dialogInterface, i) -> {
                dialogInterface.dismiss();
                finish();
            }).create().show();
        }
    }

    @SuppressLint("WrongConstant")
    private void createWebPrintJob(WebView webView) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter;
        printAdapter = webView.createPrintDocumentAdapter("New PDF File.pdf");
        printJob = printManager.print(getString(R.string.app_name) + " Document", printAdapter, new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).setResolution(new PrintAttributes.Resolution(ScreenCVEdit.FIELD_ID, "print", 200, 200)).setColorMode(2).setMinMargins(PrintAttributes.Margins.NO_MARGINS).build());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (printJob != null && printJob.isCompleted()) {
            Utility.Toast(this, getResources().getString(R.string.pdfFileCreatedSuccessfully));
            Singleton.getInstance().setFileDeleted(true);
        }
    }

    public void updateBackAndForColor() {
        if (backColorSelect) {
            iv_action_txt_bg_color.setImageResource(R.drawable.pc_format_color_fill);
        } else {
            iv_action_txt_bg_color.setImageResource(R.drawable.pc_format_color_fill);
        }
        if (forColorSelect) {
            iv_action__txt_for_color.setImageResource(R.drawable.pc_format_color_text);
        } else {
            iv_action__txt_for_color.setImageResource(R.drawable.pc_format_color_text);
        }
    }

    public void pickImage() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{ContentTypes.IMAGE_JPEG, ContentTypes.IMAGE_PNG});
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Utility.logCatMsg("onActivityResult ");
        if (i == PICK_PHOTO_FOR_AVATAR && i2 == -1 && intent != null) {
            String[] strArr = {"_data"};
            Cursor query = getContentResolver().query(intent.getData(), strArr, null, null, null);
            if (query != null) {
                query.moveToFirst();
                @SuppressLint("Range") String string = query.getString(query.getColumnIndex(strArr[0]));
                File file = new File(string);
                Utility.logCatMsg("file name " + file.getName());
                mRichEditorAction.insertImageUrl("file://" + string);
                query.close();
            }
        }
    }

    private static String encodeFileToBase64Binary(String str) {
        return new String(Base64.encode(readFile2BytesByStream(new File(str)), 2));
    }

    public String getFileToByte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readFile2BytesByStream(File file) {
        Throwable th;
        BufferedInputStream bufferedInputStream = null;
        IOException e;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 524288);
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byte[] bArr = new byte[524288];
                while (true) {
                    int read = bufferedInputStream.read(bArr, 0, 524288);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    bufferedInputStream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                return byteArray;
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                try {
                    bufferedInputStream.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
                return null;
            }
        } catch (Throwable th3) {
            th = th3;
            try {
                bufferedInputStream.close();
            } catch (IOException e8) {
                e8.printStackTrace();
            }
            if (0 != 0) {
                try {
                    byteArrayOutputStream2.close();
                } catch (IOException e9) {
                    e9.printStackTrace();
                }
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return new byte[0];
    }

    @Override
    public void onBackPressed() {
        if (colorPallateLayout.getVisibility() == View.VISIBLE) {
            colorPallateLayout.setVisibility(View.GONE);
        } else {
            mRichEditorAction.refreshHtml(mRichEditorCallback, onGetHtmlListenerForBackPress);
        }
    }

    public void initClickListener() {
        findViewById(R.id.color1).setOnClickListener(this);
        findViewById(R.id.color2).setOnClickListener(this);
        findViewById(R.id.color3).setOnClickListener(this);
        findViewById(R.id.color4).setOnClickListener(this);
        findViewById(R.id.color5).setOnClickListener(this);
        findViewById(R.id.color6).setOnClickListener(this);
        findViewById(R.id.color7).setOnClickListener(this);
        findViewById(R.id.color8).setOnClickListener(this);
        findViewById(R.id.color9).setOnClickListener(this);
        findViewById(R.id.color10).setOnClickListener(this);
        findViewById(R.id.color11).setOnClickListener(this);
        findViewById(R.id.color12).setOnClickListener(this);
        findViewById(R.id.color13).setOnClickListener(this);
        findViewById(R.id.color14).setOnClickListener(this);
        findViewById(R.id.color15).setOnClickListener(this);
        findViewById(R.id.color16).setOnClickListener(this);
        findViewById(R.id.color17).setOnClickListener(this);
        findViewById(R.id.color18).setOnClickListener(this);
        findViewById(R.id.color19).setOnClickListener(this);
        findViewById(R.id.color20).setOnClickListener(this);
        findViewById(R.id.color21).setOnClickListener(this);
        findViewById(R.id.color22).setOnClickListener(this);
        findViewById(R.id.color23).setOnClickListener(this);
        findViewById(R.id.color24).setOnClickListener(this);
        findViewById(R.id.color25).setOnClickListener(this);
        findViewById(R.id.cancelLayout).setOnClickListener(this);
        findViewById(R.id.colorNone).setOnClickListener(this);
        iv_action_txt_bg_color.setOnClickListener(this);
        iv_action__txt_for_color.setOnClickListener(this);
    }

    public void changeTextForAndBackColor(String str) {
        if (isChangeTextBackColor) {
            mRichEditorAction.backColor(str);
            backColorSelect = true;
        } else {
            forColorSelect = true;
            mRichEditorAction.foreColor(str);
        }
        updateBackAndForColor();
        colorPallateLayout.setVisibility(View.GONE);
    }

    public String getHtmlContent(String str) {
        String LoadData = LoadData("bootstrap/bootstrap.css");
        String LoadData2 = LoadData("dist/summernote.css");
        return "<html><head> <style>" + LoadData + LoadData2 + "</style></head><body>" + str + "</body></html>";
    }

    public String LoadData(String str) {
        try {
            InputStream open = getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr);
        } catch (IOException unused) {
            return "";
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        } else {
            window.clearFlags(0);
        }
    }
}
