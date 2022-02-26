package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Main;

import static com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAppLovinAds.retryAttempt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.CreateNewPdfFile;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.FilesList;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.FilesView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.ImageToPDF;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.OCRText;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.PDFViewWebViewBase;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.RTFView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.SearchFile;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.TextViewer;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.AllFilesConverterOption;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.ChooseFileLoadingTypeActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.ActivityPremium;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.BuildConfig;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.UI.CSVFileViewerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenResumeHome;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer.EPubFileViewerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.MaterialFilePicker;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.ui.FilePickerActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVActivity.ActInvoiceMain;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad.ActNotepadList;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad.ActPassword;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.DocumentUtility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Security;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ActMain extends BaseActivity implements View.OnClickListener {
    private ArrayList<Object> allFilesList;
    private ArrayList<Object> ebookFilesList;
    private TextView fileLoadingMessageTv;
    private SwipeRefreshLayout swipeContainer;
    private DrawerLayout drawer;
    private SharedPrefs prefs;
    private Singleton singleton;
    private String fileName = "";
    private RewardedAd rewardedAd;
    private MaxRewardedAd rewardedAdLovin;
    public boolean isLoading = false;
    private boolean isFilesLoading = false;
    public int fileType = 0, AdsType = 0;

    PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, list) -> {
    };

    ActivityResultLauncher<Intent> mFileResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        Intent data = activityResult.getData();
        if (activityResult.getResultCode() != -1) {
            return;
        }
        if (data == null || data.getData() == null) {
            return;
        }
        String str = getFilePathForN(data.getData());
        if (str != null) {
            int fileType = MainConstant.getFileType(str);
            if (fileType != 5) {
                Intent intent = new Intent(ActMain.this, FilesView.class);
                if (fileType == 3) {
                    intent = new Intent(ActMain.this, PDFViewWebViewBase.class);
                } else if (fileType == 10) {
                    intent = new Intent(ActMain.this, CSVFileViewerActivity.class);
                } else if (fileType == 13) {
                    intent = new Intent(ActMain.this, RTFView.class);
                } else if (fileType == 14) {
                    intent = new Intent(ActMain.this, EPubFileViewerActivity.class);
                } else if (fileType == 6) {
                    intent = new Intent(ActMain.this, TextViewer.class);
                }
                intent.putExtra("path", str);
                intent.putExtra("name", fileName);
                intent.putExtra("fromConverterApp", false);
                intent.putExtra("fileType", fileType + "");
                startActivity(intent);
                return;
            }
            Utility.Toast(ActMain.this, "This file is not supported to open");
        }
    });

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        prefs = new SharedPrefs(this);


        checkActiveSubs();


        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        FrameLayout fl_native = findViewById(R.id.fl_native);

        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            loadRewardedAd();
            loadRewardedAdAppLovin();
        }

        GoogleAppLovinAds.showBannerAds(ActMain.this, ll_banner);
        GoogleAppLovinAds.showNativeAds(ActMain.this,fl_native);



        String stringExtra;
        if (!(getIntent() == null || (stringExtra = getIntent().getStringExtra("activity")) == null || !stringExtra.equals("Resume"))) {
            startActivity(new Intent(this, ScreenResumeHome.class));
        }

        singleton = Singleton.getInstance();
        singleton.setFileDeleted(false);
        allFilesList = Singleton.getInstance().getAllDocumentFileList();
        ebookFilesList = Singleton.getInstance().getAllEbookFileList();

        swipeContainer = findViewById(R.id.swipeContainer);
        fileLoadingMessageTv = findViewById(R.id.fileLoadingMessageTv);
        drawer = findViewById(R.id.drawer_layout);

        LinearLayout llSearch = findViewById(R.id.llSearch);
        llSearch.setOnClickListener(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        disableNavigationViewScrollbars(navigationView);
        View headerView = navigationView.getHeaderView(0);
        headerView.findViewById(R.id.llSettings).setOnClickListener(this);
        headerView.findViewById(R.id.llDashboard).setOnClickListener(this);
        headerView.findViewById(R.id.llPremium).setOnClickListener(this);
        headerView.findViewById(R.id.llFavorite).setOnClickListener(this);
        headerView.findViewById(R.id.llImageToPdf).setOnClickListener(this);
        headerView.findViewById(R.id.llShareApp).setOnClickListener(this);
        headerView.findViewById(R.id.llRateApp).setOnClickListener(this);

        findViewById(R.id.codeFiles).setOnClickListener(this);
        findViewById(R.id.allFilesList).setOnClickListener(this);
        findViewById(R.id.pdfFilesList).setOnClickListener(this);
        findViewById(R.id.excelFilesList).setOnClickListener(this);
        findViewById(R.id.docFilesList).setOnClickListener(this);
        findViewById(R.id.pptFilesList).setOnClickListener(this);
        findViewById(R.id.txtFilesList).setOnClickListener(this);
        findViewById(R.id.csvFilesList).setOnClickListener(this);
//        findViewById(R.id.ebookFilesList).setOnClickListener(this);
        findViewById(R.id.rtfFilesList).setOnClickListener(this);
        findViewById(R.id.favoriteFileList).setOnClickListener(this);
        findViewById(R.id.navigationDrwer).setOnClickListener(this);
        findViewById(R.id.folderVise).setOnClickListener(this);
        findViewById(R.id.imagToText).setOnClickListener(this);
        findViewById(R.id.createNewPdfLayout).setOnClickListener(this);
        findViewById(R.id.createNewCVLayout).setOnClickListener(this);
        findViewById(R.id.viewMoreConverter).setOnClickListener(this);
        findViewById(R.id.notepadLayout).setOnClickListener(this);
        findViewById(R.id.invoiceMaker).setOnClickListener(this);

        ImageView imgPremium = findViewById(R.id.imgPremium);
        imgPremium.setOnClickListener(v -> startActivity(new Intent(ActMain.this, ActivityPremium.class)));

        ImageView imgCVPremium = findViewById(R.id.imgCVPremium);
        ImageView imgCPPremium = findViewById(R.id.imgCPPremium);
        ImageView imgIVPremium = findViewById(R.id.imgIVPremium);
        if (prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true")) {
            imgCVPremium.setVisibility(View.GONE);
            imgCPPremium.setVisibility(View.GONE);
            imgIVPremium.setVisibility(View.GONE);
        } else {
            imgCVPremium.setVisibility(View.VISIBLE);
            imgCPPremium.setVisibility(View.VISIBLE);
            imgIVPremium.setVisibility(View.VISIBLE);
        }

        if (!isFilesLoading) {
            loadAllFilesAtOnce();
        }

        swipeContainer.setOnRefreshListener(() -> {
            if (!isFilesLoading) {
                loadAllFilesAtOnce();
            }
        });

        Utility.deleteTempFolder(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Log.e("Permission", "Granted");
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                startActivity(intent);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1 && iArr.length > 0) {
            boolean z = false;
            if (iArr[0] == 0) {
                z = true;
            }
            if (!z) {
                Utility.Toast(this, getResources().getString(R.string.permission_denied_message2));
            } else {
                Utility.Toast(this, getResources().getString(R.string.permissionGranted));
            }
        }
    }

    private void checkActiveSubs() {
        BillingClient billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();


        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, (billingResult1, list) -> {
                        Purchase.PurchasesResult purchaseResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                        if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK && !Objects.requireNonNull(purchaseResult.getPurchasesList()).isEmpty()) {

                            ArrayList<String> skusW = new ArrayList<>();
                            skusW.add(prefs.getRemove_ads_weekly());

                            ArrayList<String> skusM = new ArrayList<>();
                            skusM.add(prefs.getRemove_ads_monthly());

                            ArrayList<String> skusY = new ArrayList<>();
                            skusY.add(prefs.getRemove_ads_yearly());

                            if (purchaseResult.getPurchasesList().size() == 0) {
                                Toast.makeText(ActMain.this, purchaseResult.getPurchasesList().size(), Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < purchaseResult.getPurchasesList().size(); i++) {
                                    if (purchaseResult.getPurchasesList().get(i).getSkus().equals(skusW)) {
                                        if (!verifyValidSignature(purchaseResult.getPurchasesList().get(i).getOriginalJson(), purchaseResult.getPurchasesList().get(i).getSignature())) {
                                            prefs.setActive_Weekly("");
                                        } else {
                                            prefs.setActive_Weekly("true");
                                        }
                                    } else if (purchaseResult.getPurchasesList().get(i).getSkus().equals(skusM)) {
                                        if (!verifyValidSignature(purchaseResult.getPurchasesList().get(i).getOriginalJson(), purchaseResult.getPurchasesList().get(i).getSignature())) {
                                            prefs.setActive_Monthly("");
                                        } else {
                                            prefs.setActive_Monthly("true");
                                        }
                                    } else if (purchaseResult.getPurchasesList().get(i).getSkus().equals(skusY)) {
                                        if (!verifyValidSignature(purchaseResult.getPurchasesList().get(i).getOriginalJson(), purchaseResult.getPurchasesList().get(i).getSignature())) {
                                            prefs.setActive_Yearly("");
                                        } else {
                                            prefs.setActive_Yearly("true");
                                        }
                                    } else {
                                        prefs.setActive_Weekly("");
                                        prefs.setActive_Monthly("");
                                        prefs.setActive_Yearly("");
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            String base64Key = prefs.getBase_key();
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    public void loadAllFilesAtOnce() {
        if (prefs.isLoadAllFilesAtOnce()) {
            swipeContainer.setEnabled(true);
            fileLoadingMessageTv.setText(getResources().getString(R.string.takeLongTimeToLoadFileMessage));
            allFilesList.clear();
            ebookFilesList.clear();
            isFilesLoading = true;
            swipeContainer.setRefreshing(true);
            new DocumentUtility(this, allFilesList, ebookFilesList, list -> runOnUiThread(() -> {
                swipeContainer.setRefreshing(false);
                if (allFilesList != null) {
                    isFilesLoading = false;
                    swipeContainer.setRefreshing(false);
                }
            })).getAllDocumentFiles();
            return;
        }
        fileLoadingMessageTv.setText(getResources().getString(R.string.allFilesNotOpeningChangeFileLoadingSetting));
        swipeContainer.setEnabled(false);
    }

    private void startActivity(int i) {
        if (prefs.isLoadAllFilesAtOnce()) {
            if (GoogleAppLovinAds.adsdisplay) {
                GoogleAppLovinAds.showFullAds(ActMain.this, () -> {
                    GoogleAppLovinAds.allcount60.start();
                    IntentFileList(i);
                });
            } else {
                IntentFileList(i);
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            Intent intent2 = new Intent("android.intent.action.OPEN_DOCUMENT");
            intent2.addCategory("android.intent.category.OPENABLE");
            if (i == 100 || i == 6) {
                intent2.setType("*/*");
                intent2.putExtra("android.intent.extra.MIME_TYPES", new String[]{"application/*", "text/*"});
                intent2.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
            } else {
                intent2.setType("*/*");
                intent2.putExtra("android.intent.extra.MIME_TYPES", new String[]{"application/*", "text/*"});
                intent2.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
            }
            mFileResult.launch(Intent.createChooser(intent2, "Select Files"));
        } else {
            if (GoogleAppLovinAds.adsdisplay) {
                GoogleAppLovinAds.showFullAds(ActMain.this, () -> {
                    GoogleAppLovinAds.allcount60.start();
                    IntentFileList(i);
                });
            } else {
                IntentFileList(i);
            }
        }
    }

    private void IntentFileList(int i) {
        Intent intent = new Intent(this, FilesList.class);
        intent.putExtra("fileType", i + "");
        startActivity(intent);
    }

    public boolean isDocumentFile(String str) {
        String lowerCase = str.toLowerCase();
        if (lowerCase.endsWith(MainConstant.FILE_TYPE_DOC) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOCX) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOT) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTM)) {
            fileType = 0;
            return true;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_XLS) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLT) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSM)) {
            fileType = 1;
            return true;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_PPT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTM)) {
            fileType = 2;
            return true;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_PDF)) {
            fileType = 3;
            return true;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_TXT)) {
            fileType = 4;
            return true;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_CSV)) {
            fileType = 10;
            return true;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_RTF)) {
            fileType = 13;
            return true;
        } else if (!lowerCase.endsWith(MainConstant.FILE_TYPE_EPUB)) {
            return false;
        } else {
            fileType = 14;
            return true;
        }
    }

    public void onResume() {
        if (singleton.isFileDeleted()) {
            loadAllFilesAtOnce();
            singleton.setFileDeleted(false);
        }
        super.onResume();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        if (!isFilesLoading) {
            switch (view.getId()) {
                case R.id.allFilesList:
                    startActivity(100);
                    return;
                case R.id.docFilesList:
                    startActivity(0);
                    return;
                case R.id.excelFilesList:
                    startActivity(1);
                    return;
                case R.id.pptFilesList:
                    startActivity(2);
                    return;
                case R.id.pdfFilesList:
                    startActivity(3);
                    return;
                case R.id.txtFilesList:
                    startActivity(4);
                    return;
                case R.id.csvFilesList:
                    startActivity(10);
                    return;
                case R.id.favoriteFileList:
                    startActivity(12);
                    return;
                case R.id.rtfFilesList:
                    startActivity(13);
                    return;
//                case R.id.ebookFilesList:
//                    startActivity(14);
//                    return;
                case R.id.llSettings:
                    startActivity(new Intent(this, Setting.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    drawer.closeDrawers();
                    return;
                case R.id.llDashboard:
                    drawer.closeDrawers();
                    return;
                case R.id.llPremium:
                    startActivity(new Intent(this, ActivityPremium.class));
                    drawer.closeDrawers();
                    return;
                case R.id.llFavorite:
                    startActivity(12);
                    drawer.closeDrawers();
                    return;
                case R.id.llImageToPdf:
                    startActivity(new Intent(this, ImageToPDF.class));
                    drawer.closeDrawers();
                    return;
                case R.id.llShareApp:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    drawer.closeDrawers();
                    return;
                case R.id.llRateApp:
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                    drawer.closeDrawers();
                    return;
                case R.id.chooseFileLoadingOptionLayout:
                    startActivity(new Intent(this, ChooseFileLoadingTypeActivity.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    return;
                case R.id.llSearch:
                    openSearchActivity();
                    return;
                case R.id.codeFiles:
                    if (!prefs.isShowCodeFileInfoDialog()) {
                        codeFileInfoDialog();
                        return;
                    } else {
                        openCodeFilePicker();
                        return;
                    }
                case R.id.createNewCVLayout:
                    AdsType = 1;
                    if (prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true")) {
                        startActivity(new Intent(this, ScreenResumeHome.class));
                    } else {
                        ShowAdDialog();
                    }
                    return;
                case R.id.createNewPdfLayout:
                    AdsType = 2;
                    if (prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true")) {
                        startActivity(new Intent(this, CreateNewPdfFile.class));
                    } else {
                        ShowAdDialog();
                    }
                    return;
                case R.id.folderVise:
                    openFilePicker();
                    return;
                case R.id.imagToText:
                    startActivity(new Intent(this, OCRText.class));
                    return;
                case R.id.invoiceMaker:
                    AdsType = 3;
                    if (prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true")) {
                        startActivity(new Intent(this, ActInvoiceMain.class));
                    } else {
                        ShowAdDialog();
                    }
                    return;
                case R.id.navigationDrwer:
                    drawer.openDrawer(GravityCompat.START);
                    return;
                case R.id.notepadLayout:
                    if (!prefs.isSetPasswordOnNotepad() || prefs.getNotepadPassword() == null) {
                        startActivity(new Intent(this, ActNotepadList.class));
                        return;
                    }
                    Intent intent = new Intent(this, ActPassword.class);
                    intent.putExtra("flag", ActPassword.PASSWORD);
                    startActivity(intent);
                    return;
                case R.id.viewMoreConverter:
                    startActivity(new Intent(this, AllFilesConverterOption.class));
                    return;
                default:
            }
        } else {
            Utility.Toast(this, getResources().getString(R.string.pleaseWaitAsecFilesAreLoading));
        }
    }

    public void openSearchActivity() {
        Intent intent = new Intent(this, SearchFile.class);
        intent.putExtra("fileType", "100");
        startActivity(intent);
    }

    private void loadRewardedAdAppLovin() {
        rewardedAdLovin = MaxRewardedAd.getInstance(prefs.getAppLovin_reward(), ActMain.this);
        rewardedAdLovin.setListener(new MaxRewardedAdListener() {
            @Override
            public void onRewardedVideoStarted(MaxAd ad) {

            }

            @Override
            public void onRewardedVideoCompleted(MaxAd ad) {

            }

            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {
                if (AdsType == 1) {
                    startActivity(new Intent(ActMain.this, ScreenResumeHome.class));
                } else if (AdsType == 2) {
                    startActivity(new Intent(ActMain.this, CreateNewPdfFile.class));
                } else if (AdsType == 3) {
                    startActivity(new Intent(ActMain.this, ActInvoiceMain.class));
                }
            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                retryAttempt = 0;
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {
                rewardedAdLovin.loadAd();
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

                new Handler().postDelayed(() -> rewardedAdLovin.loadAd(), delayMillis);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                rewardedAdLovin.loadAd();
            }
        });

        rewardedAdLovin.loadAd();
    }


    private void loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true;
            AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
            RewardedAd.load(this, prefs.getGoogle_reward(), adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    rewardedAd = null;
                    isLoading = false;
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd1) {
                    rewardedAd = rewardedAd1;
                    isLoading = false;

                    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdShowedFullScreenContent() {
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                            rewardedAd = null;
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            rewardedAd = null;
                            loadRewardedAd();
                        }
                    });
                }
            });
        }
    }

    private void showRewardedVideo() {
        if (rewardedAd == null) {
            showRewardedVideoAppLovin();
        } else {
            rewardedAd.show(ActMain.this, rewardItem -> {
                if (AdsType == 1) {
                    startActivity(new Intent(this, ScreenResumeHome.class));
                } else if (AdsType == 2) {
                    startActivity(new Intent(this, CreateNewPdfFile.class));
                } else if (AdsType == 3) {
                    startActivity(new Intent(this, ActInvoiceMain.class));
                }
            });
        }
    }

    private void showRewardedVideoAppLovin() {
        if (rewardedAdLovin.isReady()) {
            rewardedAdLovin.showAd();
        }
    }

    private void ShowAdDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActMain.this);
        View inflate = LayoutInflater.from(ActMain.this).inflate(R.layout.bottomsheet_upgrade_ads, null);
        bottomSheetDialog.setContentView(inflate);

        inflate.findViewById(R.id.startUpgradeButton).setOnClickListener(v -> {
            startActivity(new Intent(ActMain.this, ActivityPremium.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.startWatchAds).setOnClickListener(v -> {
            switch (prefs.getAds_name()) {
                case "g":
                    showRewardedVideo();
                    break;
                case "a":
                    showRewardedVideoAppLovin();
                    break;
            }
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.imgClose).setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }


    @SuppressLint("ResourceType")
    private void openFilePicker() {
        final File externalStorageDirectory = Environment.getExternalStorageDirectory();
        singleton.setCodeFileFolderView(false);
        if (hasRealRemovableSdCard(this)) {
            final String externalStoragePath = getExternalStoragePath(this, true);
            if (externalStoragePath != null) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActMain.this);
                View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_storage_option, null);
                bottomSheetDialog.setContentView(inflate);

                inflate.findViewById(R.id.btnCancel).setOnClickListener(view -> bottomSheetDialog.dismiss());

                inflate.findViewById(R.id.chooseFromPhoneLayout).setOnClickListener(view -> {
                    openFilePicker(getResources().getString(R.string.phoneStorage), externalStorageDirectory.getAbsolutePath(), null);
                    bottomSheetDialog.dismiss();
                });

                inflate.findViewById(R.id.chooseFromSDCardLayout).setOnClickListener(view -> {
                    openFilePicker(getResources().getString(R.string.sdCard), externalStoragePath, null);
                    bottomSheetDialog.dismiss();
                });

                bottomSheetDialog.show();
                return;
            }
            openFilePicker(getResources().getString(R.string.phoneStorage), externalStorageDirectory.getAbsolutePath(), null);
            return;
        }
        openFilePicker(getResources().getString(R.string.phoneStorage), externalStorageDirectory.getAbsolutePath(), null);
    }


    @SuppressLint("ResourceType")
    public void openCodeFilePicker() {
        singleton.setCodeFileFolderView(true);
        final File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (hasRealRemovableSdCard(this)) {
            final String externalStoragePath = getExternalStoragePath(this, true);
            if (externalStoragePath != null) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActMain.this);
                View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_storage_option, null);
                bottomSheetDialog.setContentView(inflate);

                inflate.findViewById(R.id.btnCancel).setOnClickListener(view -> bottomSheetDialog.dismiss());

                inflate.findViewById(R.id.chooseFromPhoneLayout).setOnClickListener(view -> {
                    openFilePicker(getResources().getString(R.string.codeFileFolderView), externalStorageDirectory.getAbsolutePath(), MainConstant.getCodeFilePattern());
                    bottomSheetDialog.dismiss();
                });

                inflate.findViewById(R.id.chooseFromSDCardLayout).setOnClickListener(view -> {
                    openFilePicker(getResources().getString(R.string.codeFileFolderView), externalStoragePath, MainConstant.getCodeFilePattern());
                    bottomSheetDialog.dismiss();
                });

                bottomSheetDialog.show();
                return;
            }
            openFilePicker(getResources().getString(R.string.codeFileFolderView), externalStorageDirectory.getAbsolutePath(), MainConstant.getCodeFilePattern());
            return;
        }
        openFilePicker(getResources().getString(R.string.codeFileFolderView), externalStorageDirectory.getAbsolutePath(), MainConstant.getCodeFilePattern());
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 5 && i2 == -1) {
            String stringExtra = intent.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            String stringExtra2 = intent.getStringExtra("name");
            String stringExtra3 = intent.getStringExtra("fileType");
            if (stringExtra != null) {
                int parseInt = Integer.parseInt(stringExtra3);
                Intent intent2 = new Intent(this, FilesView.class);
                if (parseInt == 3) {
                    intent2 = new Intent(this, PDFViewWebViewBase.class);
                } else if (parseInt == 6 || parseInt == 11) {
                    intent2 = new Intent(this, TextViewer.class);
                } else if (parseInt == 10) {
                    intent2 = new Intent(this, CSVFileViewerActivity.class);
                } else if (parseInt == 13) {
                    intent2 = new Intent(this, RTFView.class);
                } else if (parseInt == 14) {
                    intent2 = new Intent(this, EPubFileViewerActivity.class);
                }
                intent2.putExtra("path", stringExtra);
                intent2.putExtra("name", stringExtra2);
                intent2.putExtra("fromConverterApp", false);
                intent2.putExtra("fileType", parseInt + "");
                startActivity(intent2);
            }
        }
    }

    public void onDestroy() {
        singleton.destroy();
        Utility.deleteTempFolder(this);
        super.onDestroy();
    }

    public void onBackPressed() {
        finish();
    }

    private void codeFileInfoDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActMain.this);
        View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_code_file_info, null);
        bottomSheetDialog.setContentView(inflate);

        ((TextView) inflate.findViewById(R.id.description)).setText(Html.fromHtml(getResources().getString(R.string.withCodeFileFolderViewYouCanSeeMessage)));

        ((CheckBox) inflate.findViewById(R.id.neverShowAgainCB)).setOnCheckedChangeListener((compoundButton, z) -> prefs.setShowCodeFileInfoDialog(z));

        inflate.findViewById(R.id.gotItBtn).setOnClickListener(view -> {
            openCodeFilePicker();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        NavigationMenuView navigationMenuView;
        if (navigationView != null && (navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0)) != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
    }

    public boolean hasRealRemovableSdCard(Context context) {
        return ContextCompat.getExternalFilesDirs(context, null).length >= 2;
    }

    public String getExternalStoragePath(Context context, boolean z) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?> cls = Class.forName("android.os.storage.StorageVolume");
            Method method = storageManager.getClass().getMethod("getVolumeList");
            Method method2 = cls.getMethod("getPath");
            Method method3 = cls.getMethod("isRemovable");
            Object invoke = method.invoke(storageManager);
            assert invoke != null;
            int length = Array.getLength(invoke);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(invoke, i);
                String str = (String) method2.invoke(obj, new Object[0]);
                if (z == (Boolean) method3.invoke(obj, new Object[0])) {
                    return str;
                }
            }
            return null;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void openFilePicker(String str, String str2, Pattern pattern) {
        MaterialFilePicker materialFilePicker = new MaterialFilePicker();
        if (pattern != null) {
            materialFilePicker.withFilter(MainConstant.getCodeFilePattern());
        }
        materialFilePicker.withActivity(this).withCloseMenu(true).withRootPath(str2).withHiddenFiles(false).withFilterDirectories(false).withTitle(str).withRequestCode(5).start();
    }

    public String getFilePathForN(Uri uri) {
        Cursor query = getContentResolver().query(uri, null, null, null, null);
        if (query != null) {
            int columnIndex = query.getColumnIndex("_data");
            int columnIndex2 = query.getColumnIndex("_display_name");
            query.moveToFirst();
            if (query.getCount() > 0) {
                fileName = query.getString(columnIndex2);
                if (fileName != null || columnIndex <= 0) {
                    File file = new File(Utility.getTempFolderDirectory(this), fileName);
                    String path = file.getPath();
                    query.close();
                    try {
                        InputStream openInputStream = getContentResolver().openInputStream(uri);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] bArr = new byte[Math.min(openInputStream.available(), 1048576)];
                        while (true) {
                            int read = openInputStream.read(bArr);
                            if (read != -1) {
                                fileOutputStream.write(bArr, 0, read);
                            } else {
                                openInputStream.close();
                                fileOutputStream.close();
                                return path;
                            }
                        }
                    } catch (Exception ignored) {

                    }
                } else {
                    return query.getString(columnIndex);
                }
            }
        }
        return null;
    }
}
