package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView;

public class RtfState implements Cloneable {
    public int background;
    public boolean bold;
    public int dnup;
    public int font;
    public int fontSize;
    public boolean hidden;
    public boolean italic;
    public boolean strike;
    public boolean subscript;
    public boolean superscript;
    public int textColor;
    public boolean underline;

    public RtfState() {
        reset();
    }

    public Object clone() {
        RtfState rtfState = new RtfState();
        rtfState.bold = this.bold;
        rtfState.italic = this.italic;
        rtfState.underline = this.underline;
        rtfState.strike = this.strike;
        rtfState.hidden = this.hidden;
        rtfState.dnup = this.dnup;
        rtfState.subscript = this.subscript;
        rtfState.superscript = this.superscript;
        rtfState.fontSize = this.fontSize;
        rtfState.font = this.font;
        rtfState.textColor = this.textColor;
        rtfState.background = this.background;
        return rtfState;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RtfState)) {
            return false;
        }
        RtfState rtfState = (RtfState) obj;
        if (this.bold == rtfState.bold && this.italic == rtfState.italic && this.underline == rtfState.underline && this.strike == rtfState.strike && this.dnup == rtfState.dnup && this.subscript == rtfState.subscript && this.superscript == rtfState.superscript && this.hidden == rtfState.hidden && this.fontSize == rtfState.fontSize && this.font == rtfState.font && this.textColor == rtfState.textColor && this.background == rtfState.background) {
            return true;
        }
        return false;
    }

    public void reset() {
        this.bold = false;
        this.italic = false;
        this.underline = false;
        this.strike = false;
        this.hidden = false;
        this.dnup = 0;
        this.subscript = false;
        this.superscript = false;
        this.fontSize = 0;
        this.font = 0;
        this.textColor = 0;
        this.background = 0;
    }
}
