package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView;

public abstract class RtfElement {
    
    public abstract void dump(int i);

    
    public void indent(int i) {
        for (int i2 = 0; i2 < i; i2++) {
            System.out.println("&nbsp;");
        }
    }
}
