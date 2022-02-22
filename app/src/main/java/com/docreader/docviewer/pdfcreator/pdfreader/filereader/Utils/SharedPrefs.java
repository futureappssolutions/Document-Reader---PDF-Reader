package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    private final String Google_full = "Google_full";
    private final String Google_banner = "Google_banner";
    private final String Google_native = "Google_native";
    private final String Google_open = "Google_open";
    private final String Google_reward = "Google_reward";
    private final String Facebook_banner = "Facebook_banner";
    private final String Facebook_full = "Facebook_full";
    private final String Facebook_native = "Facebook_native";
    private final String Ads_time = "Ads_time";
    private final String Ads_name = "Ads_name";

    private final String remove_ads_weekly = "remove_ads_weekly";
    private final String remove_ads_monthly = "remove_ads_monthly";
    private final String remove_ads_yearly = "remove_ads_yearly";
    private final String base_key = "base_key";

    private final String active_Weekly = "active_Weekly";
    private final String active_Monthly = "active_Monthly";
    private final String active_Yearly = "active_Yearly";

    public SharedPrefs(Context context2) {
        SharedPreferences sharedPreferences = context2.getSharedPreferences("officeMaster", 0);
        this.prefs = sharedPreferences;
        this.editor = sharedPreferences.edit();
        this.context = context2;
    }

    public void setShowCodeFileInfoDialog(boolean z) {
        this.editor.putBoolean("codeFileInfoDialog", z);
        this.editor.commit();
    }

    public boolean isShowCodeFileInfoDialog() {
        return this.prefs.getBoolean("codeFileInfoDialog", false);
    }

    public void setCVData(String str) {
        this.editor.putString("SerializableObject", str);
        this.editor.commit();
    }

    public String getCVData() {
        return this.prefs.getString("SerializableObject", "");
    }

    public void setFavoriteFilesData(String str) {
        this.editor.putString("favoriteFilesData", str);
        this.editor.commit();
    }

    public String getFavoriteFilesData() {
        return this.prefs.getString("favoriteFilesData", "");
    }

    public void setProfilePic(String str) {
        this.editor.putString("profilePic", str);
        this.editor.commit();
    }

    public String getProfilePic() {
        return this.prefs.getString("profilePic", null);
    }

    public boolean isOpenPdfSaveDialog() {
        return this.prefs.getBoolean("PdfSaveDialog", true);
    }

    public void setOpenPdfSaveDialog(boolean z) {
        this.editor.putBoolean("PdfSaveDialog", z);
        this.editor.commit();
    }

    public void setAppLocalizationCode(String str) {
        this.editor.putString("appLocalizationCode", str);
        this.editor.commit();
    }

    public String getAppLocalizationCode() {
        return this.prefs.getString("appLocalizationCode", "en");
    }

    public void setAppLocalizationName(String str) {
        this.editor.putString("appLocalizationName", str);
        this.editor.commit();
    }

    public String getAppLocalizationName() {
        return this.prefs.getString("appLocalizationName", "English (Default)");
    }

    public void setListSorting(int i) {
        this.editor.putInt("sortedBy", i);
        this.editor.commit();
    }

    public int getListSorting() {
        return this.prefs.getInt("sortedBy", 0);
    }

    public void setPdfFileViewOption(int i) {
        this.editor.putInt("pdfFileViewOption", i);
        this.editor.commit();
    }

    public int getPdfFileViewOption() {
        return this.prefs.getInt("pdfFileViewOption", 1);
    }

    public boolean isMarqueeEffectEnable() {
        return this.prefs.getBoolean("marqueeEffect", true);
    }

    public void setMarqueeEffectEnable(boolean z) {
        this.editor.putBoolean("marqueeEffect", z);
        this.editor.commit();
    }

    public boolean isSetPasswordOnNotepad() {
        return this.prefs.getBoolean("isPasswordOnNotepad", false);
    }

    public void setPasswordOnNotepad(boolean z) {
        this.editor.putBoolean("isPasswordOnNotepad", z);
        this.editor.commit();
    }

    public boolean isLoadAllFilesAtOnce() {
        return this.prefs.getBoolean("isLoadFilesAtOnce", true);
    }

    public void setLoadAllFilesAtOnce(boolean z) {
        this.editor.putBoolean("isLoadFilesAtOnce", z);
        this.editor.commit();
    }

    public void setNotepadPassword(String str) {
        this.editor.putString("notepadPassword", str);
        this.editor.commit();
    }

    public String getNotepadPassword() {
        return this.prefs.getString("notepadPassword", null);
    }

    public void setSecurityQuestionAnswer(String str) {
        this.editor.putString("securityQuestionAnswer", str.trim());
        this.editor.commit();
    }

    public String getSecurityQuestionAnswer() {
        return this.prefs.getString("securityQuestionAnswer", null);
    }

    public void setSelectedSecurityQuestion(int i) {
        this.editor.putInt("selectedSecurityQuestion", i);
        this.editor.commit();
    }

    public int getSelectedSecurityQuestion() {
        return this.prefs.getInt("selectedSecurityQuestion", 1);
    }

    public void setInvoiceBusinessInfoData(String str) {
        this.editor.putString("invoiceBusinessInfo", str);
        this.editor.commit();
    }

    public String getInvoiceBusinessInfoData() {
        return this.prefs.getString("invoiceBusinessInfo", "");
    }

    public String getGoogle_full() {
        return prefs.getString(Google_full, "");
    }

    public void setGoogle_full(String value) {
        editor.putString(Google_full, value).apply();
    }

    public String getGoogle_banner() {
        return prefs.getString(Google_banner, "");
    }

    public void setGoogle_banner(String value) {
        editor.putString(Google_banner, value).apply();
    }

    public String getGoogle_native() {
        return prefs.getString(Google_native, "");
    }

    public void setGoogle_native(String value) {
        editor.putString(Google_native, value).apply();
    }

    public String getGoogle_open() {
        return prefs.getString(Google_open, "");
    }

    public void setGoogle_open(String value) {
        editor.putString(Google_open, value).apply();
    }

    public String getGoogle_reward() {
        return prefs.getString(Google_reward, "");
    }

    public void setGoogle_reward(String value) {
        editor.putString(Google_reward, value).apply();
    }

    public String getFacebook_banner() {
        return prefs.getString(Facebook_banner, "");
    }

    public void setFacebook_banner(String value) {
        editor.putString(Facebook_banner, value).apply();
    }

    public String getFacebook_full() {
        return prefs.getString(Facebook_full, "");
    }

    public void setFacebook_full(String value) {
        editor.putString(Facebook_full, value).apply();
    }

    public String getFacebook_native() {
        return prefs.getString(Facebook_native, "");
    }

    public void setFacebook_native(String value) {
        editor.putString(Facebook_native, value).apply();
    }

    public String getAds_time() {
        return prefs.getString(Ads_time, "");
    }

    public void setAds_time(String value) {
        editor.putString(Ads_time, value).apply();
    }

    public String getAds_name() {
        return prefs.getString(Ads_name, "");
    }

    public void setAds_name(String value) {
        editor.putString(Ads_name, value).apply();
    }

    public String getRemove_ads_weekly() {
        return prefs.getString(remove_ads_weekly, "");
    }

    public void setRemove_ads_weekly(String value) {
        editor.putString(remove_ads_weekly, value).apply();
    }

    public String getRemove_ads_monthly() {
        return prefs.getString(remove_ads_monthly, "");
    }

    public void setRemove_ads_monthly(String value) {
        editor.putString(remove_ads_monthly, value).apply();
    }

    public String getRemove_ads_yearly() {
        return prefs.getString(remove_ads_yearly, "");
    }

    public void setRemove_ads_yearly(String value) {
        editor.putString(remove_ads_yearly, value).apply();
    }

    public String getBase_key() {
        return prefs.getString(base_key, "");
    }

    public void setBase_key(String value) {
        editor.putString(base_key, value).apply();
    }

    public String getActive_Weekly() {
        return prefs.getString(active_Weekly, "");
    }

    public void setActive_Weekly(String value) {
        editor.putString(active_Weekly, value).apply();
    }

    public String getActive_Monthly() {
        return prefs.getString(active_Monthly, "");
    }

    public void setActive_Monthly(String value) {
        editor.putString(active_Monthly, value).apply();
    }

    public String getActive_Yearly() {
        return prefs.getString(active_Yearly, "");
    }

    public void setActive_Yearly(String value) {
        editor.putString(active_Yearly, value).apply();
    }
}
