package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.MRICHEditor;

import android.os.Build;
import android.webkit.WebView;

public class RichEditorAction {
    private final WebView mWebView;

    public RichEditorAction(WebView webView) {
        this.mWebView = webView;
    }

    public void undo() {
        load("javascript:undo()");
    }

    public void redo() {
        load("javascript:redo()");
    }

    public void focus() {
        load("javascript:focus()");
    }

    public void disable() {
        load("javascript:disable()");
    }

    public void enable() {
        load("javascript:enable()");
    }

    public void bold() {
        load("javascript:bold()");
    }

    public void italic() {
        load("javascript:italic()");
    }

    public void underline() {
        load("javascript:underline()");
    }

    public void strikethrough() {
        load("javascript:strikethrough()");
    }

    public void superscript() {
        load("javascript:superscript()");
    }

    public void subscript() {
        load("javascript:subscript()");
    }

    public void backColor(String str) {
        load("javascript:backColor('" + str + "')");
    }

    public void foreColor(String str) {
        load("javascript:foreColor('" + str + "')");
    }

    public void fontName(String str) {
        load("javascript:fontName('" + str + "')");
    }

    public void fontSize(double d) {
        load("javascript:fontSize(" + d + ")");
    }

    public void justifyLeft() {
        load("javascript:justifyLeft()");
    }

    public void justifyRight() {
        load("javascript:justifyRight()");
    }

    public void justifyCenter() {
        load("javascript:justifyCenter()");
    }

    public void justifyFull() {
        load("javascript:justifyFull()");
    }

    public void insertOrderedList() {
        load("javascript:insertOrderedList()");
    }

    public void insertUnorderedList() {
        load("javascript:insertUnorderedList()");
    }

    public void indent() {
        load("javascript:indent()");
    }

    public void outdent() {
        load("javascript:outdent()");
    }

    public void formatPara() {
        load("javascript:formatPara()");
    }

    public void formatH1() {
        load("javascript:formatH1()");
    }

    public void formatH2() {
        load("javascript:formatH2()");
    }

    public void formatH3() {
        load("javascript:formatH3()");
    }

    public void formatH4() {
        load("javascript:formatH4()");
    }

    public void formatH5() {
        load("javascript:formatH5()");
    }

    public void formatH6() {
        load("javascript:formatH6()");
    }

    public void lineHeight(double d) {
        load("javascript:lineHeight(" + d + ")");
    }

    public void insertImageUrl(String str) {
        load("javascript:insertImageUrl('" + str + "')");
    }

    public void insertImageData(String str, String str2) {
        load("javascript:insertImageUrl('" + ("data:image/" + str.split("\\.")[1] + ";base64," + str2) + "')");
    }

    public void insertText(String str) {
        load("javascript:insertText('" + str + "')");
    }

    public void createLink(String str, String str2) {
        load("javascript:createLink('" + str + "','" + str2 + "')");
    }

    public void unlink() {
        load("javascript:unlink()");
    }

    public void codeView() {
        load("javascript:codeView()");
    }

    public void insertTable(int i, int i2) {
        load("javascript:insertTable('" + i + "x" + i2 + "')");
    }

    public void insertHorizontalRule() {
        load("javascript:insertHorizontalRule()");
    }

    public void formatBlockquote() {
        load("javascript:formatBlock('blockquote')");
    }

    public void formatBlockCode() {
        load("javascript:formatBlock('pre')");
    }

    public void insertHtml(String str) {
        load("javascript:pasteHTML('" + str + "')");
    }

    public void refreshHtml(RichEditorCallback richEditorCallback, RichEditorCallback.OnGetHtmlListener onGetHtmlListener) {
        richEditorCallback.setOnGetHtmlListener(onGetHtmlListener);
        load("javascript:refreshHTML()");
    }

    private void load(String str) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mWebView.evaluateJavascript(str, null);
        } else {
            this.mWebView.loadUrl(str);
        }
    }
}
