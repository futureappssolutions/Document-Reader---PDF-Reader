package com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtBook extends Book {
    private final List<String> f814l = new ArrayList<>();
    private final Map<String, String> toc = new LinkedHashMap<>();

    public TxtBook(Context context) {
        super(context);
    }

    @Override
    public void load() throws IOException {
        if (!getFile().exists() || !getFile().canRead()) {
            throw new FileNotFoundException(getFile() + " doesn't exist or not readable");
        }
        File outFile = getBookFile();

        if (!outFile.exists()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(getFile()))) {
                try (Writer out = new FileWriter(outFile)) {
                    StringBuilder para = new StringBuilder(4096);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.matches("^\\s*$")) {
                            para.append(System.lineSeparator());
                            para.append(System.lineSeparator());
                            out.write(para.toString());
                            para.delete(0, para.length());
                        } else {
                            para.append(line);
                            if (!line.matches(".*\\s+$")) {
                                para.append(" ");
                            }
                        }
                    }
                }
            }
        }

    }

    private File getBookFile() {
        return new File(getThisBookDir(), getFile().getName());
    }

    public Map<String, String> getToc() {
        return this.toc;
    }

    @Override
    public BookMetadata getMetaData() throws IOException {
        BookMetadata metadata = new BookMetadata();
        metadata.setFilename(getFile().getPath());

        try (BufferedReader reader = new BufferedReader(new FileReader(getFile()))) {
            int c = 0;
            String line;
            Pattern titlerx = Pattern.compile("^\\s*(?i:title)[:= \\t]+(.+)");
            Pattern authorrx = Pattern.compile("^\\s*(?i:author|by)[:= \\t]+(.{3,26})\\s*$");
            Pattern titleauthorrx =
                    Pattern.compile("^(?xi: " +
                            "\\s*(?:The \\s+ Project \\s+ Gutenberg \\s+ EBook \\s+ of \\s+)? " +
                            "(.+),? " +
                            "\\s+ (?:translated\\s+|written\\s+)? by \\s+ " +
                            "(.{3,26}) " +
                            " )$");

            boolean foundtitle = false;
            boolean foundauthor = false;
            String ptitle = null;
            String pauthor = null;

            String firstline = reader.readLine();

            line = firstline;

            if (line != null) {
                Matcher tam = titleauthorrx.matcher(line);
                if (tam.find()) {
                    ptitle = tam.group(1);
                    pauthor = tam.group(2);
                }

                do {

                    Matcher tm = titlerx.matcher(line);
                    if (!foundtitle && tm.find()) {
                        metadata.setTitle(tm.group(1));
                        foundtitle = true;
                    }
                    Matcher am = authorrx.matcher(line);
                    if (!foundauthor && am.find()) {
                        metadata.setAuthor(am.group(1));
                        foundauthor = true;
                    }
                    if (c++ > 50 || foundauthor && foundtitle) {
                        break;
                    }

                } while ((line = reader.readLine()) != null);

                if (!foundtitle && ptitle != null) {
                    metadata.setTitle(ptitle);
                    foundtitle = true;
                }
                if (!foundauthor && pauthor != null) {
                    metadata.setAuthor(pauthor);
                }

                if (!foundtitle) {
                    metadata.setTitle(getFile().getName() + " " + firstline);
                }
            }
        }
        return metadata;
    }

    public List<String> getSectionIds() {
        this.f814l.add("1");
        return this.f814l;
    }

    public Uri getUriForSectionID(String str) {
        return Uri.fromFile(getBookFile());
    }

    public Book.ReadPoint locateReadPoint(String str) {
        Book.ReadPoint readPoint = new Book.ReadPoint();
        readPoint.setId("1");
        readPoint.setPoint(Uri.parse(str));
        return readPoint;
    }
}
