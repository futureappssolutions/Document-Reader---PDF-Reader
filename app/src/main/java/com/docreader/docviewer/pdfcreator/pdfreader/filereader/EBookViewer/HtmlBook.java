package com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.itextpdf.text.Annotation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlBook extends Book {
    private static final String ORDERCOUNT = "ordercount";
    private static final String TOC_CONTENT = "toc.content.";
    private static final String TOC_LABEL = "toc.label.";
    private final List<String> f813l = new ArrayList<>();
    private Map<String, String> toc;

    public HtmlBook(Context context) {
        super(context);
    }

    @Override
    public void load() throws IOException {
        if (!getFile().exists() || !getFile().canRead()) {
            throw new FileNotFoundException(getFile() + " doesn't exist or not readable");
        }

        toc = new LinkedHashMap<>();

        SharedPreferences bookdat = getSharedPreferences();
        if (bookdat.contains(ORDERCOUNT)) {
            int toccount = bookdat.getInt(ORDERCOUNT, 0);
            for (int i = 0; i < toccount; i++) {
                String label = bookdat.getString(TOC_LABEL + i, "");
                String point = bookdat.getString(TOC_CONTENT + i, "");
                toc.put(point, label);
                Log.d("EPUB", "TOC: " + label + ". File: " + point);
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(getFile()))) {
                int c = 0;
                String line;

                Pattern idlinkrx = Pattern.compile("<a\\s+[^>]*\\b(?i:name|id)=\"([^\"]+)\"[^>]*>(?:(.+?)</a>)?");
                Pattern hidlinkrx = Pattern.compile("<h[1-3]\\s+[^>]*\\bid=\"([^\"]+)\"[^>]*>(.+?)</h");

                SharedPreferences.Editor bookdatedit = bookdat.edit();

                while ((line = reader.readLine()) != null) {
                    String id = null;
                    String text = null;
                    Matcher t = idlinkrx.matcher(line);
                    if (t.find()) {
                        id = t.group(1);
                        text = t.group(2);
                    }
                    Matcher t2 = hidlinkrx.matcher(line);
                    if (t2.find()) {
                        id = t2.group(1);
                        text = t2.group(2);
                    }
                    if (id != null) {
                        if (text == null) text = id;
                        bookdatedit.putString(TOC_LABEL + c, text);
                        bookdatedit.putString(TOC_CONTENT + c, "#" + id);
                        toc.put("#" + id, text);
                        c++;
                    }
                }
                bookdatedit.putInt(ORDERCOUNT, c);
                bookdatedit.apply();
            }
        }
    }

    public Map<String, String> getToc() {
        return this.toc;
    }

    @Override
    public BookMetadata getMetaData() throws IOException {
        BookMetadata metadata = new BookMetadata();
        metadata.setFilename(getFile().getPath());
        try (Reader reader = new FileReader(getFile())) {
            char[] header = new char[8196];
            Pattern titlerx = Pattern.compile("(?is:<title.*?>\\s*(.+?)\\s*</title>)");

            boolean foundtitle = false;

            if (reader.read(header) > 0) {
                String line = new String(header);
                Matcher tm = titlerx.matcher(line);
                if (tm.find()) {
                    metadata.setTitle(tm.group(1));
                    foundtitle = true;
                }
            }

            if (!foundtitle) {
                metadata.setTitle(getFile().getName());
            }
        }
        return metadata;
    }

    public List<String> getSectionIds() {
        this.f813l.add("1");
        return this.f813l;
    }

    public Uri getUriForSectionID(String str) {
        return Uri.fromFile(getFile());
    }

    public Book.ReadPoint locateReadPoint(String str) {
        Book.ReadPoint readPoint = new Book.ReadPoint();
        readPoint.setId("1");
        Uri parse = Uri.parse(str);
        if (parse.isRelative()) {
            parse = new Uri.Builder().scheme(Annotation.FILE).path(getFile().getPath()).fragment(parse.getFragment()).build();
        }
        readPoint.setPoint(parse);
        return readPoint;
    }
}
