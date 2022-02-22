package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.MRICHEditor;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public abstract class RichEditorCallback {
    private final Gson gson = new Gson();
    private final List<ActionType> mFontBlockGroup = Arrays.asList(ActionType.NORMAL, ActionType.H1, ActionType.H2, ActionType.H3, ActionType.H4, ActionType.H5, ActionType.H6);
    private final List<ActionType> mListStyleGroup = Arrays.asList(ActionType.ORDERED, ActionType.UNORDERED);
    private final List<ActionType> mTextAlignGroup = Arrays.asList(ActionType.JUSTIFY_LEFT, ActionType.JUSTIFY_CENTER, ActionType.JUSTIFY_RIGHT, ActionType.JUSTIFY_FULL);
    private FontStyle mFontStyle = new FontStyle();
    private OnGetHtmlListener onGetHtmlListener;
    public String html;

    public interface OnGetHtmlListener {
        void getHtml(String str);
    }

    public abstract void notifyFontStyleChange(ActionType actionType, String str);

    @JavascriptInterface
    public void returnHtml(String str) {
        html = str;
        if (onGetHtmlListener != null) {
            onGetHtmlListener.getHtml(str);
        }
    }

    @JavascriptInterface
    public void updateCurrentStyle(String str) {
        FontStyle fontStyle = gson.fromJson(str, FontStyle.class);
        if (fontStyle != null) {
            updateStyle(fontStyle);
        }
    }

    private void updateStyle(FontStyle fontStyle) {
        if ((mFontStyle.getFontFamily() == null || !mFontStyle.getFontFamily().equals(fontStyle.getFontFamily())) && !TextUtils.isEmpty(fontStyle.getFontFamily())) {
            notifyFontStyleChange(ActionType.FAMILY, fontStyle.getFontFamily().split(",")[0].replace("\"", ""));
        }
        if ((mFontStyle.getFontForeColor() == null || !mFontStyle.getFontForeColor().equals(fontStyle.getFontForeColor())) && !TextUtils.isEmpty(fontStyle.getFontForeColor())) {
            notifyFontStyleChange(ActionType.FORE_COLOR, fontStyle.getFontForeColor());
        }
        if ((mFontStyle.getFontBackColor() == null || !mFontStyle.getFontBackColor().equals(fontStyle.getFontBackColor())) && !TextUtils.isEmpty(fontStyle.getFontBackColor())) {
            notifyFontStyleChange(ActionType.BACK_COLOR, fontStyle.getFontBackColor());
        }
        if (mFontStyle.getFontSize() != fontStyle.getFontSize()) {
            notifyFontStyleChange(ActionType.SIZE, String.valueOf(fontStyle.getFontSize()));
        }
        if (mFontStyle.getTextAlign() != fontStyle.getTextAlign()) {
            int size = mTextAlignGroup.size();
            for (int i = 0; i < size; i++) {
                ActionType actionType = mTextAlignGroup.get(i);
                notifyFontStyleChange(actionType, String.valueOf(actionType == fontStyle.getTextAlign()));
            }
        }
        if (mFontStyle.getLineHeight() != fontStyle.getLineHeight()) {
            notifyFontStyleChange(ActionType.LINE_HEIGHT, String.valueOf(fontStyle.getLineHeight()));
        }
        if (mFontStyle.isBold() != fontStyle.isBold()) {
            notifyFontStyleChange(ActionType.BOLD, String.valueOf(fontStyle.isBold()));
        }
        if (mFontStyle.isItalic() != fontStyle.isItalic()) {
            notifyFontStyleChange(ActionType.ITALIC, String.valueOf(fontStyle.isItalic()));
        }
        if (mFontStyle.isUnderline() != fontStyle.isUnderline()) {
            notifyFontStyleChange(ActionType.UNDERLINE, String.valueOf(fontStyle.isUnderline()));
        }
        if (mFontStyle.isSubscript() != fontStyle.isSubscript()) {
            notifyFontStyleChange(ActionType.SUBSCRIPT, String.valueOf(fontStyle.isSubscript()));
        }
        if (mFontStyle.isSuperscript() != fontStyle.isSuperscript()) {
            notifyFontStyleChange(ActionType.SUPERSCRIPT, String.valueOf(fontStyle.isSuperscript()));
        }
        if (mFontStyle.isStrikethrough() != fontStyle.isStrikethrough()) {
            notifyFontStyleChange(ActionType.STRIKETHROUGH, String.valueOf(fontStyle.isStrikethrough()));
        }
        if (mFontStyle.getFontBlock() != fontStyle.getFontBlock()) {
            int size2 = mFontBlockGroup.size();
            for (int i2 = 0; i2 < size2; i2++) {
                ActionType actionType2 = mFontBlockGroup.get(i2);
                notifyFontStyleChange(actionType2, String.valueOf(actionType2 == fontStyle.getFontBlock()));
            }
        }
        if (mFontStyle.getListStyle() != fontStyle.getListStyle()) {
            int size3 = mListStyleGroup.size();
            for (int i3 = 0; i3 < size3; i3++) {
                ActionType actionType3 = mListStyleGroup.get(i3);
                notifyFontStyleChange(actionType3, String.valueOf(actionType3 == fontStyle.getListStyle()));
            }
        }
        mFontStyle = fontStyle;
    }

    public OnGetHtmlListener getOnGetHtmlListener() {
        return onGetHtmlListener;
    }

    public void setOnGetHtmlListener(OnGetHtmlListener onGetHtmlListener2) {
        onGetHtmlListener = onGetHtmlListener2;
    }
}
