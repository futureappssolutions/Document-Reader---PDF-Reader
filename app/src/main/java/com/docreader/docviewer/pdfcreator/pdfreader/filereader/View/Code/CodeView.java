package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Code;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeView extends WebView {
    private String code;
    private String escapeCode;
    private Language language;
    private OnHighlightListener onHighlightListener;
    private ScaleGestureDetector pinchDetector;
    private Theme theme;
    private float fontSize;
    private int highlightLineNumber;
    private int startLineNumber;
    private int lineCount;
    private boolean showLineNumber;
    private boolean wrapLine;
    private boolean zoomEnabled;

    public interface OnHighlightListener {
        void onFinishCodeHighlight();

        void onFontSizeChanged(int i);

        void onLanguageDetected(Language language, int i);

        void onLineClicked(int i, String str);

        void onStartCodeHighlight();
    }

    public CodeView(Context context) {
        this(context, null);
    }

    public CodeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CodeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        code = "";
        fontSize = 16.0f;
        wrapLine = false;
        zoomEnabled = false;
        showLineNumber = false;
        startLineNumber = 1;
        lineCount = 0;
        highlightLineNumber = -1;
        init(context, attributeSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isZoomEnabled()) {
            pinchDetector.onTouchEvent(motionEvent);
        }
        return super.onTouchEvent(motionEvent);
    }

    @SuppressLint({"ResourceType", "SetJavaScriptEnabled"})
    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CodeView, 0, 0);
        setWrapLine(obtainStyledAttributes.getBoolean(4, false));
        setFontSize((float) obtainStyledAttributes.getInt(0, 14));
        setZoomEnabled(obtainStyledAttributes.getBoolean(5, false));
        setShowLineNumber(obtainStyledAttributes.getBoolean(2, false));
        setStartLineNumber(obtainStyledAttributes.getInt(3, 1));
        highlightLineNumber = obtainStyledAttributes.getInt(1, -1);
        obtainStyledAttributes.recycle();
        pinchDetector = new ScaleGestureDetector(context, new PinchListener());
        setWebChromeClient(new WebChromeClient());
        getSettings().setJavaScriptEnabled(true);
        getSettings().setCacheMode(2);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public CodeView setOnHighlightListener(OnHighlightListener onHighlightListener2) {
        if (onHighlightListener2 == null) {
            removeJavascriptInterface("android");
        } else if (onHighlightListener != onHighlightListener2) {
            onHighlightListener = onHighlightListener2;
            addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void onStartCodeHighlight() {
                    if (onHighlightListener != null) {
                        onHighlightListener.onStartCodeHighlight();
                    }
                }

                @JavascriptInterface
                public void onFinishCodeHighlight() {
                    if (onHighlightListener != null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                fillLineNumbers();
                                showHideLineNumber(isShowLineNumber());
                                highlightLineNumber(getHighlightLineNumber());
                            }
                        });
                        onHighlightListener.onFinishCodeHighlight();
                    }
                }

                @JavascriptInterface
                public void onLanguageDetected(String str, int i) {
                    if (onHighlightListener != null && str != null) {
                        onHighlightListener.onLanguageDetected(Language.getLanguageByName(str), i);
                    }
                }

                @JavascriptInterface
                public void onLineClicked(int i, String str) {
                    if (onHighlightListener != null) {
                        onHighlightListener.onLineClicked(i, str);
                    }
                }
            }, "android");
        }
        return this;
    }

    public float getFontSize() {
        return fontSize;
    }

    public CodeView setFontSize(float f) {
        if (f < 8.0f) {
            f = 8.0f;
        }
        fontSize = f;
        OnHighlightListener onHighlightListener2 = onHighlightListener;
        if (onHighlightListener2 != null) {
            onHighlightListener2.onFontSizeChanged((int) f);
        }
        return this;
    }

    public String getCode() {
        return code;
    }

    public CodeView setCode(String str) {
        if (str == null) {
            str = "";
        }
        code = str;
        escapeCode = Html.escapeHtml(str);
        return this;
    }

    public Theme getTheme() {
        return theme;
    }

    public CodeView setTheme(Theme theme2) {
        theme = theme2;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public CodeView setLanguage(Language language2) {
        language = language2;
        return this;
    }

    public boolean isWrapLine() {
        return wrapLine;
    }

    public CodeView setWrapLine(boolean z) {
        wrapLine = z;
        return this;
    }

    public boolean isZoomEnabled() {
        return zoomEnabled;
    }

    public CodeView setZoomEnabled(boolean z) {
        zoomEnabled = z;
        return this;
    }

    public boolean isShowLineNumber() {
        return showLineNumber;
    }

    public CodeView setShowLineNumber(boolean z) {
        showLineNumber = z;
        return this;
    }

    public int getStartLineNumber() {
        return startLineNumber;
    }

    public CodeView setStartLineNumber(int i) {
        if (i < 0) {
            i = 1;
        }
        startLineNumber = i;
        return this;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void toggleLineNumber() {
        boolean z = !showLineNumber;
        showLineNumber = z;
        showHideLineNumber(z);
    }

    public void apply() {
        loadDataWithBaseURL("", toHtml(), "text/html", "UTF-8", "");
    }

    private String toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("<link rel='stylesheet' href='");
        sb.append(getTheme().getPath());
        sb.append("' />\n");
        sb.append("<style>\n");
        sb.append("body {");
        sb.append("font-size:");
        sb.append(String.format("%dpx;", (int) getFontSize()));
        sb.append("margin: 0px; line-height: 1.2;");
        sb.append("}\n");
        sb.append(".hljs {");
        sb.append("}\n");
        sb.append("pre {");
        sb.append("margin: 0px; position: relative;");
        sb.append("}\n");
        if (isWrapLine()) {
            sb.append("td.line {");
            sb.append("word-wrap: break-word; white-space: pre-wrap; word-break: break-all;");
            sb.append("}\n");
        }
        sb.append("table, td, tr {");
        sb.append("margin: 0px; padding: 0px;");
        sb.append("}\n");
        sb.append("code > span { display: none; }");
        sb.append("td.ln { text-align: right; padding-right: 2px; }");
        sb.append("td.line:hover span {background: #661d76; color: #fff;}");
        sb.append("td.line:hover {background: #661d76; color: #fff; border-radius: 2px;}");
        sb.append("td.destacado {background: #ffda11; color: #000; border-radius: 2px;}");
        sb.append("td.destacado span {background: #ffda11; color: #000;}");
        sb.append("</style>");
        sb.append("<script src='file:///android_asset/highlightjs/highlight.js'></script>");
        sb.append("<script>hljs.initHighlightingOnLoad();</script>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<pre><code class='");
        sb.append(language.getLanguageName());
        sb.append("'>");
        sb.append(insertLineNumber(escapeCode));
        sb.append("</code></pre>\n");
        return sb.toString();
    }

    private void executeJavaScript(String str) {
        if (Build.VERSION.SDK_INT >= 19) {
            evaluateJavascript("javascript:" + str, null);
            return;
        }
        loadUrl("javascript:" + str);
    }
    
    public void changeFontSize(int i) {
        executeJavaScript("document.body.style.fontSize = '" + i + "px'");
    }
    
    public void fillLineNumbers() {
        executeJavaScript("var i; var x = document.querySelectorAll('td.ln'); for(i = 0; i < x.length; i++) {x[i].innerHTML = x[i].getAttribute('line');}");
    }

    public void showHideLineNumber(boolean z) {
        Locale locale = Locale.ENGLISH;
        Object[] objArr = new Object[1];
        objArr[0] = z ? "''" : "'none'";
        executeJavaScript(String.format(locale, "var i; var x = document.querySelectorAll('td.ln'); for(i = 0; i < x.length; i++) {x[i].style.display = %s;}", objArr));
    }

    public int getHighlightLineNumber() {
        return highlightLineNumber;
    }

    public void highlightLineNumber(int i) {
        highlightLineNumber = i;
        executeJavaScript(String.format(Locale.ENGLISH, "var x = document.querySelectorAll('.destacado'); if(x && x.length == 1) x[0].classList.remove('destacado');"));
        if (i >= 0) {
            executeJavaScript(String.format(Locale.ENGLISH, "var x = document.querySelectorAll(\"td.line[line='%d']\"); if(x && x.length == 1) x[0].classList.add('destacado');", i));
        }
    }

    private String insertLineNumber(String str) {
        Matcher matcher = Pattern.compile("(.*?)&#10;").matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        int startLineNumber2 = getStartLineNumber();
        lineCount = 0;
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, String.format(Locale.ENGLISH, "<tr><td line='%d' class='hljs-number ln'></td><td line='%d' onclick='android.onLineClicked(%d, textContent);' class='line'>$1 </td></tr>&#10;", startLineNumber2, startLineNumber2, startLineNumber2));
            startLineNumber2++;
            lineCount++;
        }
        return "<table>\n" + stringBuffer.toString().trim() + "</table>\n";
    }

    private class PinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float fontSize;
        private int oldFontSize;

        private PinchListener() {
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            float fontSize2 = getFontSize();
            fontSize = fontSize2;
            oldFontSize = (int) fontSize2;
            return super.onScaleBegin(scaleGestureDetector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            fontSize = fontSize;
            super.onScaleEnd(scaleGestureDetector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float fontSize2 = getFontSize() * scaleGestureDetector.getScaleFactor();
            fontSize = fontSize2;
            if (fontSize2 >= 8.0f) {
                changeFontSize((int) fontSize2);
                if (!(onHighlightListener == null || oldFontSize == ((int) fontSize))) {
                    onHighlightListener.onFontSizeChanged((int) fontSize);
                }
                oldFontSize = (int) fontSize;
                return false;
            }
            fontSize = 8.0f;
            return false;
        }
    }
}
