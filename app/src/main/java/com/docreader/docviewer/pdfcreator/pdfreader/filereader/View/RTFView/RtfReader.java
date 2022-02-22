package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView;

import com.itextpdf.text.html.HtmlTags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RtfReader {
    private RtfGroup group;
    private int len;
    private int pos;
    public RtfGroup root = null;
    private String rtf;
    private char tchar;

    public void getChar() {
        if (pos < rtf.length()) {
            String str = rtf;
            int i = pos;
            pos = i + 1;
            tchar = str.charAt(i);
        }
    }

    public int hexdec(String str) {
        return Integer.parseInt(str, 16);
    }

    public boolean isDigit() {
        char c = tchar;
        return c >= '0' && c <= '9';
    }

    public boolean isLetter() {
        char c = tchar;
        if (c < 'A' || c > 'Z') {
            return c >= 'a' && c <= 'z';
        }
        return true;
    }

    public void parseStartGroup() {
        RtfGroup rtfGroup = new RtfGroup();
        RtfGroup rtfGroup2 = group;
        if (rtfGroup2 != null) {
            rtfGroup.parent = rtfGroup2;
        }
        if (root == null) {
            group = rtfGroup;
            root = rtfGroup;
            return;
        }
        group.children.add(rtfGroup);
        group = rtfGroup;
    }

    public void parseEndGroup() {
        group = group.parent;
    }

    public void parseControlWord() {
        boolean z;
        int i;
        getChar();
        String str = "";
        while (isLetter()) {
            str = str + tchar;
            getChar();
        }
        if (tchar == '-') {
            getChar();
            i = -1;
            z = true;
        } else {
            i = -1;
            z = false;
        }
        while (isDigit()) {
            if (i == -1) {
                i = 0;
            }
            i = (i * 10) + Integer.parseInt(tchar + "");
            getChar();
        }
        if (i == -1) {
            i = 1;
        }
        if (z) {
            i = -i;
        }
        if (str.equals(HtmlTags.U)) {
            if (tchar == ' ') {
                getChar();
            }
            if (tchar == '\\' && rtf.charAt(pos) == '\'') {
                pos += 3;
            }
            if (z) {
                i += 65536;
            }
        } else if (tchar != ' ') {
            pos--;
        }
        RtfControlWord rtfControlWord = new RtfControlWord();
        rtfControlWord.word = str;
        rtfControlWord.parameter = i;
        group.children.add(rtfControlWord);
    }

    public void parseControlSymbol() {
        int i;
        getChar();
        char c = tchar;
        if (c == '\'') {
            getChar();
            getChar();
            i = hexdec((tchar + "") + (tchar + ""));
        } else {
            i = 0;
        }
        RtfControlSymbol rtfControlSymbol = new RtfControlSymbol();
        rtfControlSymbol.symbol = c;
        rtfControlSymbol.parameter = i;
        group.children.add(rtfControlSymbol);
    }

    public void parseControl() {
        getChar();
        pos--;
        if (isLetter()) {
            parseControlWord();
        } else {
            parseControlSymbol();
        }
    }

    protected void parseText() throws RtfParseException {
        String text = "";
        boolean terminate = false;
        do {
            terminate = false;
            if (tchar == '\\') {
                getChar();
                switch (tchar) {
                    case '\\':
                    case '{':
                    case '}':
                        break;
                    default:
                        pos -= 2;
                        terminate = true;
                        break;
                }
            } else if (tchar == '{' || tchar == '}') {
                pos--;
                terminate = true;
            }

            if (!terminate) {
                text += tchar;
                getChar();
            }
        } while (!terminate && pos < len);

        RtfText rtfText = new RtfText();
        rtfText.text = text;

        if (group == null) {
            throw new RtfParseException("Invalid RTF file.");
        }

        group.children.add(rtfText);
    }

    public void parse(File rtfFile) throws RtfParseException {
        try {
            try (FileInputStream fis = new FileInputStream(rtfFile)) {
                parse(fis);
            }
        } catch (IOException e) {
            throw new RtfParseException(e.getMessage());
        }
    }

    public void parse(InputStream inputStream) throws RtfParseException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                    sb.append(10);
                } else {
                    parse(sb.toString());
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void parse(String str) throws RtfParseException {
        rtf = str;
        pos = 0;
        len = str.length();
        group = null;
        root = null;
        while (pos < len) {
            getChar();
            char c = tchar;
            if (!(c == 10 || c == 13)) {
                if (c == '\\') {
                    parseControl();
                } else if (c == '{') {
                    parseStartGroup();
                } else if (c != '}') {
                    parseText();
                } else {
                    parseEndGroup();
                }
            }
        }
    }
}
