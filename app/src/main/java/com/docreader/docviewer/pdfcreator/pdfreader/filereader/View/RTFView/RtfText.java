package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView;

import java.io.PrintStream;

public class RtfText extends RtfElement {
    public String text;

    public void dump(int i) {
        System.out.println("<div style='color:red'>");
        indent(i);
        PrintStream printStream = System.out;
        printStream.println("TEXT " + this.text);
        System.out.println("</div>");
    }
}
