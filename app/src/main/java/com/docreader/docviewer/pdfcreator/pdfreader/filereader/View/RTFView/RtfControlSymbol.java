package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView;

import java.io.PrintStream;

public class RtfControlSymbol extends RtfElement {
    public int parameter = 0;
    public char symbol;

    public void dump(int i) {
        System.out.println("<div style='color:blue'>");
        indent(i);
        PrintStream printStream = System.out;
        printStream.println("SYMBOL " + symbol + " (" + parameter + ")");
        System.out.println("</div>");
    }
}
