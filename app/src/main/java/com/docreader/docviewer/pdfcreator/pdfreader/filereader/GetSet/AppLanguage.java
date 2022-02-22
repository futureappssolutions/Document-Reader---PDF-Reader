package com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;

import java.util.ArrayList;

public final class AppLanguage {
    private String code;
    private String flag;
    private boolean isSelected;
    private String localLanguageName;
    private String name;

    public AppLanguage(String str, String str2, String str3, boolean z, String str4) {
        this.name = str;
        this.localLanguageName = str2;
        this.code = str3;
        this.isSelected = z;
        this.flag = str4;
    }

    public AppLanguage() {
    }

    public static ArrayList<AppLanguage> getAppSupportedLanguages() {
        return getAppSupportedLanguagess();
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String str) {
        this.name = str;
    }

    public final String getCode() {
        return this.code;
    }

    public final void setCode(String str) {
        this.code = str;
    }

    public final String getLocalLanguageName() {
        return this.localLanguageName;
    }

    public final void setLocalLanguageName(String str) {
        this.localLanguageName = str;
    }

    public final String getFlag() {
        return this.flag;
    }

    public final void setFlag(String str) {
        this.flag = str;
    }

    public final boolean isSelected() {
        return this.isSelected;
    }

    public final void setSelected(boolean z) {
        this.isSelected = z;
    }

    public static ArrayList<AppLanguage> getAppSupportedLanguagess() {
        ArrayList<AppLanguage> arrayList = new ArrayList<>();
        arrayList.add(new AppLanguage("English", "Default", "en", false, "uk.png"));
        arrayList.add(new AppLanguage("Arabic", "العربية", "ar", false, "uae.png"));
        arrayList.add(new AppLanguage("Spanish", "Español", "es", false, "spain.png"));
        arrayList.add(new AppLanguage("Russian", "Русский", "ru", false, "russia.png"));
        arrayList.add(new AppLanguage("Chinese", "中文", "zh", false, "china.png"));
        arrayList.add(new AppLanguage("India", "हिंदी", "hi", false, "india.png"));
        arrayList.add(new AppLanguage("French", "français", "fr", false, "abc.png"));
        arrayList.add(new AppLanguage("Bengali", "বাংলা", "bn", false, "bangladesh.png"));
        arrayList.add(new AppLanguage("Indonesian", "Indonesia", ScreenCVEdit.FIELD_ID, false, "indonesia.png"));
        arrayList.add(new AppLanguage("Japanese", "日本語", "ja", false, "japan.png"));
        arrayList.add(new AppLanguage("Malay", "Malay", "ms", false, "malaysia.png"));
        arrayList.add(new AppLanguage("Portuguese", "português", "pt", false, "portugal.png"));
        arrayList.add(new AppLanguage("Turkish", "Türkçe ", "tr", false, "turkey.png"));
        arrayList.add(new AppLanguage("Urdu", "اُردُو ", "ur", false, "pakistan.png"));
        arrayList.add(new AppLanguage("German", "German", "de", false, "germany.png"));
        arrayList.add(new AppLanguage("Korean", "韓國語 ", "ko", false, "korean.png"));
        return arrayList;
    }
}
