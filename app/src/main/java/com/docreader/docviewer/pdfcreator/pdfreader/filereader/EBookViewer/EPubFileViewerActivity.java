package com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.itextpdf.text.Annotation;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.ShapeTypes;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class EPubFileViewerActivity extends BaseActivity {
    public static final String DRAG_SCROLL = "dragscroll";
    public static final String READEREXITEDNORMALLY = "readerexitednormally";
    public static final String SCREEN_PAGING = "screenpaging";
    public static final String TAG = "ReaderActivity";
    public final Object timerSync = new Object();
    public final Handler handler = new Handler();
    public Book book;
    public Throwable exception;
    public String filePath;
    public Point mScreenDim;
    public ProgressBar progressBar;
    public TimerTask scrollTask = null;
    public Timer timer;
    public WebView webView;
    public boolean isPagingDown;
    public boolean isPagingUp;
    public volatile int scrollDir;
    public int currentDimColor = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_ebup_viewer);

        getIntent();
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        mScreenDim = new Point();
        defaultDisplay.getSize(mScreenDim);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle((CharSequence) "");
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.toolBarTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            filePath = getIntent().getStringExtra("path");
            String stringExtra = getIntent().getStringExtra("name");
            Integer.parseInt(getIntent().getStringExtra("fileType"));
            changeBackGroundColor(100);
            textView.setText(stringExtra);
        }

        webView = (WebView) findViewById(R.id.page_view);
        webView.getSettings().setDefaultFontSize(18);
        webView.getSettings().setDefaultFixedFontSize(18);
        webView.getSettings().setAllowFileAccess(true);
        webView.setNetworkAvailable(false);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                handleLink(str);
                return true;
            }

            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                Uri url = webResourceRequest.getUrl();
                if (url.getScheme() == null || !url.getScheme().equals(Annotation.FILE)) {
                    return false;
                }
                handleLink(url.toString());
                return true;
            }

            public void onPageFinished(WebView webView, String str) {
                try {
                    restoreScrollOffsetDelayed(100);
                } catch (Throwable th) {
                    Log.e(EPubFileViewerActivity.TAG, th.getMessage(), th);
                }
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (filePath != null) {
            loadFile(new File(filePath));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_epub, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
        } else if (itemId != R.id.action_previous) {
            switch (itemId) {
                case R.id.action_mode_day:
                    applyColor(Color.argb(255, 255, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION), false);
                    break;
                case R.id.action_mode_night:
                    applyColor(Color.argb(255, 57, 52, 46), false);
                    break;
                case R.id.action_next:
                    nextPage();
                    break;
                default:
                    switch (itemId) {
                        case R.id.font_10:
                            selectFontSize(10);
                            break;
                        case R.id.font_12:
                            selectFontSize(12);
                            break;
                        case R.id.font_14:
                            selectFontSize(14);
                            break;
                        case R.id.font_16:
                            selectFontSize(16);
                            break;
                        case R.id.font_18:
                            selectFontSize(18);
                            break;
                        case R.id.font_20:
                            selectFontSize(20);
                            break;
                        case R.id.font_22:
                            selectFontSize(22);
                            break;
                        case R.id.font_24:
                            selectFontSize(24);
                            break;
                        case R.id.font_8:
                            selectFontSize(8);
                            break;
                    }
            }
        } else {
            prevPage();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void showMenu() {
        findViewById(R.id.slide_menu).setVisibility(View.VISIBLE);
    }

    public void hideMenu() {
        findViewById(R.id.slide_menu).setVisibility(View.GONE);
    }

    private void prevPage() {
        isPagingDown = false;
        if (webView.canScrollVertically(-1)) {
            webView.pageUp(false);
        } else {
            isPagingUp = true;
            showUri(book.getPreviousSection());
        }
        hideMenu();
    }

    private void nextPage() {
        isPagingUp = false;
        if (webView.canScrollVertically(1)) {
            webView.pageDown(false);
        } else {
            isPagingDown = true;
            if (book != null) {
                showUri(book.getNextSection());
            }
        }
        hideMenu();
    }

    public void saveScrollOffset() {
        webView.computeScroll();
        saveScrollOffset(webView.getScrollY());
    }

    private void saveScrollOffset(int i) {
        if (book != null) {
            book.setSectionOffset(i);
        }
    }

    public void restoreScrollOffsetDelayed(int i) {
        handler.postDelayed(this::restoreScrollOffset, (long) i);
    }

    public void restoreScrollOffset() {
        if (book != null) {
            int sectionOffset = book.getSectionOffset();
            webView.computeScroll();
            if (sectionOffset >= 0) {
                webView.scrollTo(0, sectionOffset);
            } else if (isPagingUp) {
                webView.pageDown(true);
            } else if (isPagingDown) {
                webView.pageUp(true);
            }
            isPagingUp = false;
            isPagingDown = false;
        }
    }

    private void loadFile(File file) {
        webView.loadData("Loading " + file.getPath(), "text/plain", "utf-8");
        new LoaderTask(this, file).execute();
    }

    private static class LoaderTask extends AsyncTask<Void, Integer, Book> {
        private final File file;
        private final WeakReference<EPubFileViewerActivity> ractref;

        LoaderTask(EPubFileViewerActivity ePubFileViewerActivity, File file2) {
            file = file2;
            ractref = new WeakReference<>(ePubFileViewerActivity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            EPubFileViewerActivity ePubFileViewerActivity = (EPubFileViewerActivity) ractref.get();
            if (ePubFileViewerActivity != null) {
                ePubFileViewerActivity.progressBar.setProgress(0);
                ePubFileViewerActivity.progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            EPubFileViewerActivity ePubFileViewerActivity = (EPubFileViewerActivity) ractref.get();
            if (ePubFileViewerActivity != null) {
                ePubFileViewerActivity.progressBar.setProgress(numArr[0].intValue());
            }
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            EPubFileViewerActivity ePubFileViewerActivity = (EPubFileViewerActivity) ractref.get();
            if (ePubFileViewerActivity != null) {
                ePubFileViewerActivity.progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public Book doInBackground(Void... voidArr) {
            EPubFileViewerActivity ePubFileViewerActivity = (EPubFileViewerActivity) ractref.get();
            if (ePubFileViewerActivity == null) {
                return null;
            }
            try {
                ePubFileViewerActivity.book = Book.getBookHandler(ePubFileViewerActivity, file.getPath());
                Utility.logCatMsg("File " + file.getName());
                if (ePubFileViewerActivity.book != null) {
                    ePubFileViewerActivity.book.load(file);
                    return ePubFileViewerActivity.book;
                }
            } catch (Throwable th) {
                ePubFileViewerActivity.exception = th;
            }
            return null;
        }

        @Override
        public void onPostExecute(Book book) {
            EPubFileViewerActivity ePubFileViewerActivity = (EPubFileViewerActivity) ractref.get();
            if (ePubFileViewerActivity != null) {
                String string = ePubFileViewerActivity.getString(R.string.someThingWentWrongPleaseTryAgain);
                try {
                    ePubFileViewerActivity.progressBar.setVisibility(View.GONE);
                    if (book == null) {
                        if (ePubFileViewerActivity.exception != null) {
                            ePubFileViewerActivity.webView.setOnTouchListener((View.OnTouchListener) null);
                            ePubFileViewerActivity.webView.setWebViewClient((WebViewClient) null);
                            ePubFileViewerActivity.webView.loadData(string + ePubFileViewerActivity.exception.getLocalizedMessage(), "text/plain", "utf-8");
                            throw ePubFileViewerActivity.exception;
                        }
                    }
                    if (book != null && ePubFileViewerActivity.book != null && ePubFileViewerActivity.book.hasDataDir()) {
                        int fontsize = ePubFileViewerActivity.book.getFontsize();
                        if (fontsize != -1) {
                            ePubFileViewerActivity.setFontSize(fontsize);
                        }
                        Uri currentSection = ePubFileViewerActivity.book.getCurrentSection();
                        if (currentSection != null) {
                            ePubFileViewerActivity.showUri(currentSection);
                            return;
                        }
                        Toast.makeText(ePubFileViewerActivity, string + " (no sections)", 1).show();
                    }
                } catch (Throwable th) {
                    Log.e(EPubFileViewerActivity.TAG, th.getMessage(), th);
                    Toast.makeText(ePubFileViewerActivity, string + th.getMessage(), 1).show();
                }
            }
        }
    }

    public void showUri(Uri uri) {
        if (uri != null) {
            webView.loadUrl(uri.toString());
        }
    }

    public void handleLink(String str) {
        if (str != null) {
            showUri(book.handleClickedLink(str));
        }
    }

    public void setFontSize(int i) {
        book.setFontsize(i);
        webView.getSettings().setDefaultFontSize(i);
        webView.getSettings().setDefaultFixedFontSize(i);
    }

    private void selectFontSize(int i) {
        int defaultFontSize = webView.getSettings().getDefaultFontSize();
        double d = (double) (((float) ((-webView.getScrollY()) * (defaultFontSize - i))) / getResources().getDisplayMetrics().density);
        Double.isNaN(d);
        setFontSize(i);
        webView.scrollBy(0, (int) (d / 2.7d));
    }

    private void applyColor(int i) {
        applyColor(i, false);
    }

    private void resetColor() {
        applyColor(Color.argb(255, ShapeTypes.PieWedge, ShapeTypes.PieWedge, ShapeTypes.PieWedge), true);
    }

    private void applyColor(int i, boolean z) {
        currentDimColor = i;
        try {
            getWindow().setBackgroundDrawable((Drawable) null);
            webView.setBackgroundColor(i);
            getWindow().setBackgroundDrawable(new ColorDrawable(i));
            if (!z) {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setJavaScriptEnabled(false);
            }
        } catch (Throwable th) {
            Log.e(TAG, th.getMessage(), th);
            Toast.makeText(this, th.getLocalizedMessage(), 1).show();
        }
    }

    @SuppressLint("ResourceType")
    private void setDimLevel(View view, int i) {
        Drawable drawable;
        try {
            view.setBackground((Drawable) null);
            if (Build.VERSION.SDK_INT >= 21) {
                drawable = getResources().getDrawable(17301508, (Resources.Theme) null).mutate();
            } else {
                drawable = getResources().getDrawable(17301508).mutate();
            }
            drawable.setColorFilter(i, PorterDuff.Mode.MULTIPLY);
            view.setBackground(drawable);
            if (view instanceof ImageButton) {
                ((ImageButton) view).getDrawable().mutate().setColorFilter(i, PorterDuff.Mode.MULTIPLY);
            }
        } catch (Throwable th) {
            Log.e(TAG, th.getMessage(), th);
        }
    }
}
