package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView;

import java.io.PrintStream;

public class RtfControlWord extends RtfElement {
    public int parameter;
    public String word;

    public void dump(int i) {
        System.out.println("<div style='color:green'>");
        indent(i);
        PrintStream printStream = System.out;
        printStream.println("WORD " + word + " (" + parameter + ")");
        System.out.println("</div>");
    }
}
