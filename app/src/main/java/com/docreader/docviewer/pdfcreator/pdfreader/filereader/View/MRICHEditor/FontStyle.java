package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.MRICHEditor;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.itextpdf.text.html.HtmlTags;

import java.util.List;

public class FontStyle {
    public List<AncestorsBean> ancestors;
    public boolean anchor;
    @SerializedName("font-backColor")
    public String fontBackColor;
    @SerializedName("font-block")
    public String fontBlock;
    @SerializedName("font-bold")
    public String fontBold;
    @SerializedName("font-family")
    public String fontFamily;
    @SerializedName("font-foreColor")
    public String fontForeColor;
    @SerializedName("font-italic")
    public String fontItalic;
    @SerializedName("font-size")
    public int fontSize;
    @SerializedName("font-strikethrough")
    public String fontStrikethrough;
    @SerializedName("font-subscript")
    public String fontSubscript;
    @SerializedName("font-superscript")
    public String fontSuperscript;
    @SerializedName("font-underline")
    public String fontUnderline;
    @SerializedName("line-height")
    public String lineHeight;
    @SerializedName("list-style")
    public String listStyle;
    @SerializedName("list-style-type")
    public String listStyleType;
    public RangeBean range;
    @SerializedName("text-align")
    public String textAlign;

    public static class AncestorsBean {
    }

    public String getFontFamily() {
        return this.fontFamily;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public ActionType getTextAlign() {
        if (TextUtils.isEmpty(this.textAlign)) {
            return null;
        }
        String str = this.textAlign;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1364013995:
                if (str.equals(HtmlTags.ALIGN_CENTER)) {
                    c = 0;
                    break;
                }
                break;
            case -1249482096:
                if (str.equals(HtmlTags.ALIGN_JUSTIFY)) {
                    c = 1;
                    break;
                }
                break;
            case 3317767:
                if (str.equals(HtmlTags.ALIGN_LEFT)) {
                    c = 2;
                    break;
                }
                break;
            case 108511772:
                if (str.equals(HtmlTags.ALIGN_RIGHT)) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return ActionType.JUSTIFY_CENTER;
            case 1:
                return ActionType.JUSTIFY_FULL;
            case 2:
                return ActionType.JUSTIFY_LEFT;
            case 3:
                return ActionType.JUSTIFY_RIGHT;
            default:
                return ActionType.JUSTIFY_FULL;
        }
    }

    public String getListStyleType() {
        return this.listStyleType;
    }

    public double getLineHeight() {
        if (TextUtils.isEmpty(this.lineHeight)) {
            return 0.0d;
        }
        try {
            return Double.parseDouble(this.lineHeight);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0d;
        }
    }

    public ActionType getFontBlock() {
        ActionType actionType = ActionType.NONE;
        if (TextUtils.isEmpty(this.fontBlock)) {
            return actionType;
        }
        if (HtmlTags.P.equals(this.fontBlock)) {
            return ActionType.NORMAL;
        }
        if (HtmlTags.H1.equals(this.fontBlock)) {
            return ActionType.H1;
        }
        if (HtmlTags.H2.equals(this.fontBlock)) {
            return ActionType.H2;
        }
        if (HtmlTags.H3.equals(this.fontBlock)) {
            return ActionType.H3;
        }
        if (HtmlTags.H4.equals(this.fontBlock)) {
            return ActionType.H4;
        }
        if (HtmlTags.H5.equals(this.fontBlock)) {
            return ActionType.H5;
        }
        return HtmlTags.H6.equals(this.fontBlock) ? ActionType.H6 : actionType;
    }

    public String getFontBackColor() {
        return this.fontBackColor;
    }

    public String getFontForeColor() {
        return this.fontForeColor;
    }

    public boolean isBold() {
        return HtmlTags.BOLD.equals(this.fontBold);
    }

    public boolean isItalic() {
        return HtmlTags.ITALIC.equals(this.fontItalic);
    }

    public boolean isUnderline() {
        return HtmlTags.UNDERLINE.equals(this.fontUnderline);
    }

    public boolean isSubscript() {
        return "subscript".equals(this.fontSubscript);
    }

    public boolean isSuperscript() {
        return "superscript".equals(this.fontSuperscript);
    }

    public boolean isStrikethrough() {
        return "strikethrough".equals(this.fontStrikethrough);
    }

    public ActionType getListStyle() {
        if (TextUtils.isEmpty(this.listStyle)) {
            return null;
        }
        if ("ordered".equals(this.listStyle)) {
            return ActionType.ORDERED;
        }
        if ("unordered".equals(this.listStyle)) {
            return ActionType.UNORDERED;
        }
        return ActionType.NONE;
    }

    public boolean isAnchor() {
        return this.anchor;
    }

    public RangeBean getRange() {
        return this.range;
    }

    public List<AncestorsBean> getAncestors() {
        return this.ancestors;
    }

    public static class RangeBean {
        private EcBean f1063ec;
        private int f1064eo;
        private ScBean f1065sc;
        private int f1066so;

        public static class EcBean {
        }

        public static class ScBean {
        }

        public ScBean getSc() {
            return this.f1065sc;
        }

        public void setSc(ScBean scBean) {
            this.f1065sc = scBean;
        }

        public int getSo() {
            return this.f1066so;
        }

        public void setSo(int i) {
            this.f1066so = i;
        }

        public EcBean getEc() {
            return this.f1063ec;
        }

        public void setEc(EcBean ecBean) {
            this.f1063ec = ecBean;
        }

        public int getEo() {
            return this.f1064eo;
        }

        public void setEo(int i) {
            this.f1064eo = i;
        }
    }
}
