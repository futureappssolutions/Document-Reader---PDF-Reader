package com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

public abstract class Book {
    private static final String BG_COLOR = "BG_COLOR";
    private static final String FONTSIZE = "fontsize";
    private static final String SECTION_ID = "sectionID";
    private static final String SECTION_ID_OFFSET = "sectionIDOffset";
    private static final String reservedChars = "[/\\\\:?\"'*|<>+\\[\\]()]";
    private final Context context;
    private int currentSectionIDPos = 0;
    private SharedPreferences data;
    private final File dataDir;
    private File file;
    private List<String> sectionIDs;
    private String subbook;
    private File thisBookDir;
    private String title;

    public static String getFileExtensionRX() {
        return ".*\\.(epub|txt|html?)";
    }


    public abstract BookMetadata getMetaData() throws IOException;


    public abstract List<String> getSectionIds();

    public abstract Map<String, String> getToc();


    public abstract Uri getUriForSectionID(String str);


    public abstract void load() throws IOException;


    public abstract ReadPoint locateReadPoint(String str);

    Book(Context context2) {
        this.dataDir = context2.getFilesDir();
        this.context = context2;
        this.sectionIDs = new ArrayList<>();
    }

    public void load(File file2) {
        this.file = file2;
        this.data = getStorage(this.context, file2);
        File bookDir = getBookDir(this.context, file2);
        this.thisBookDir = bookDir;
        bookDir.mkdirs();
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sectionIDs = getSectionIds();
        restoreCurrentSectionID();
    }

    public boolean hasDataDir() {
        return this.data != null;
    }

    public Uri getFirstSection() {
        clearSectionOffset();
        this.currentSectionIDPos = 0;
        saveCurrentSectionID();
        return getUriForSectionID(this.sectionIDs.get(this.currentSectionIDPos));
    }

    public Uri getCurrentSection() {
        try {
            restoreCurrentSectionID();
            if (this.currentSectionIDPos >= this.sectionIDs.size()) {
                this.currentSectionIDPos = 0;
                saveCurrentSectionID();
            }
            if (this.sectionIDs.size() == 0) {
                return null;
            }
            return getUriForSectionID(this.sectionIDs.get(this.currentSectionIDPos));
        } catch (Throwable th) {
            Log.e("Booky", th.getMessage(), th);
            return null;
        }
    }

    public void setFontsize(int i) {
        this.data.edit().putInt(FONTSIZE, i).apply();
    }

    public int getFontsize() {
        return this.data.getInt(FONTSIZE, -1);
    }

    public void clearFontsize() {
        this.data.edit().remove(FONTSIZE).apply();
    }

    public void setSectionOffset(int i) {
        this.data.edit().putInt(SECTION_ID_OFFSET, i).apply();
    }

    public int getSectionOffset() {
        SharedPreferences sharedPreferences = this.data;
        if (sharedPreferences == null) {
            return 0;
        }
        return sharedPreferences.getInt(SECTION_ID_OFFSET, -1);
    }

    private void clearSectionOffset() {
        this.data.edit().remove(SECTION_ID_OFFSET).apply();
    }

    public void setBackgroundColor(int i) {
        this.data.edit().putInt(BG_COLOR, i).apply();
    }

    public int getBackgroundColor() {
        return this.data.getInt(BG_COLOR, Integer.MAX_VALUE);
    }

    public void clearBackgroundColor() {
        this.data.edit().remove(BG_COLOR).apply();
    }

    public void setFlag(String str, boolean z) {
        this.data.edit().putBoolean(str, z).apply();
    }

    public boolean getFlag(String str, boolean z) {
        return this.data.getBoolean(str, z);
    }

    public Uri getNextSection() {
        try {
            if (this.currentSectionIDPos + 1 >= this.sectionIDs.size()) {
                return null;
            }
            clearSectionOffset();
            this.currentSectionIDPos++;
            saveCurrentSectionID();
            return getUriForSectionID(this.sectionIDs.get(this.currentSectionIDPos));
        } catch (Throwable th) {
            Log.e("Booky", th.getMessage(), th);
            return null;
        }
    }

    public Uri getPreviousSection() {
        try {
            if (this.currentSectionIDPos - 1 < 0) {
                return null;
            }
            clearSectionOffset();
            this.currentSectionIDPos--;
            saveCurrentSectionID();
            return getUriForSectionID(this.sectionIDs.get(this.currentSectionIDPos));
        } catch (Throwable th) {
            Log.e("Booky", th.getMessage(), th);
            return null;
        }
    }

    private void gotoSectionID(String str) {
        try {
            int indexOf = this.sectionIDs.indexOf(str);
            if (indexOf > -1 && indexOf < this.sectionIDs.size()) {
                this.currentSectionIDPos = indexOf;
                saveCurrentSectionID();
                getUriForSectionID(this.sectionIDs.get(this.currentSectionIDPos));
            }
        } catch (Throwable th) {
            Log.e("Booky", th.getMessage(), th);
        }
    }

    public Uri handleClickedLink(String str) {
        ReadPoint locateReadPoint = locateReadPoint(str);
        if (locateReadPoint == null) {
            return null;
        }
        gotoSectionID(locateReadPoint.getId());
        clearSectionOffset();
        return locateReadPoint.getPoint();
    }

    private void saveCurrentSectionID() {
        Log.d("Book", "saving section " + this.currentSectionIDPos);
        this.data.edit().putInt(SECTION_ID, this.currentSectionIDPos).apply();
    }

    private void restoreCurrentSectionID() {
        this.currentSectionIDPos = this.data.getInt(SECTION_ID, this.currentSectionIDPos);
        Log.d("Book", "Loaded section " + this.currentSectionIDPos);
    }

    private static String makeOldFName(File file2) {
        return file2.getPath().replaceAll("[/\\\\]", "_");
    }

    private static String makeFName(File file2) {
        String replaceAll = file2.getPath().replaceAll(reservedChars, "_");
        if (replaceAll.getBytes().length <= 60) {
            return replaceAll;
        }
        int i = 30;
        if (replaceAll.length() <= 30) {
            i = replaceAll.length() - 1;
        }
        return replaceAll.substring(0, i) + replaceAll.substring(replaceAll.length() - (i / 2)) + crc32(replaceAll);
    }

    private static long crc32(String str) {
        byte[] bytes = str.getBytes();
        CRC32 crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    private static String getProperFName(Context context2, File file2) {
        if (hasOldBookDir(context2, file2)) {
            String makeOldFName = makeOldFName(file2);
            Log.d("Book", "using old fname " + makeOldFName);
            return makeOldFName;
        }
        String makeFName = makeFName(file2);
        Log.d("Book", "using new fname " + makeFName);
        return makeFName;
    }

    private static boolean hasOldBookDir(Context context2, File file2) {
        return new File(context2.getFilesDir(), "book" + makeOldFName(file2)).exists();
    }

    private static File getBookDir(Context context2, File file2) {
        String properFName = getProperFName(context2, file2);
        return new File(context2.getFilesDir(), "book" + properFName);
    }

    private static SharedPreferences getStorage(Context context2, File file2) {
        return context2.getSharedPreferences(getProperFName(context2, file2), 0);
    }

    
    public File getThisBookDir() {
        return this.thisBookDir;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public File getFile() {
        return this.file;
    }

    private void setFile(File file2) {
        this.file = file2;
    }

    public File getDataDir() {
        return this.dataDir;
    }

    public Context getContext() {
        return this.context;
    }

    
    public SharedPreferences getSharedPreferences() {
        return this.data;
    }

    public static Book getBookHandler(Context context2, String str) throws IOException {
        if (str.toLowerCase().endsWith(".epub")) {
            return new EpubBook(context2);
        }
        if (str.toLowerCase().endsWith(".txt")) {
            return new TxtBook(context2);
        }
        if (str.toLowerCase().endsWith(".html") || str.toLowerCase().endsWith(".htm")) {
            return new HtmlBook(context2);
        }
        return null;
    }

    public static BookMetadata getBookMetaData(Context context2, String str) throws IOException {
        Book bookHandler = getBookHandler(context2, str);
        if (bookHandler == null) {
            return null;
        }
        bookHandler.setFile(new File(str));
        return bookHandler.getMetaData();
    }

    protected class ReadPoint {
        private String f812id;
        private Uri point;

        protected ReadPoint() {
        }

        public String getId() {
            return this.f812id;
        }
        
        public void setId(String str) {
            this.f812id = str;
        }

        public Uri getPoint() {
            return this.point;
        }

        public void setPoint(Uri uri) {
            this.point = uri;
        }
    }
}
