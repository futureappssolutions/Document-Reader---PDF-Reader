package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

public class Setting implements Parcelable {
    public static final Parcelable.Creator<Setting> CREATOR = new Parcelable.Creator<Setting>() {
        public Setting createFromParcel(Parcel parcel) {
            return new Setting(parcel);
        }

        public Setting[] newArray(int i) {
            return new Setting[i];
        }
    };
    private String about;
    private String achievements;
    private String contact;
    private String education;
    private String experience;
    private String hobby;
    private String language;
    private String project;
    private String reference;
    private String skill;
    private String socialLink;

    @Override
    public int describeContents() {
        return 0;
    }

    public Setting(Context context) {
        this(context.getResources().getString(R.string.language), context.getResources().getString(R.string.about), context.getResources().getString(R.string.workExperienc), context.getResources().getString(R.string.education), context.getResources().getString(R.string.highlightskill), context.getResources().getString(R.string.project), context.getResources().getString(R.string.reference), context.getResources().getString(R.string.achievements), context.getResources().getString(R.string.hobby), context.getResources().getString(R.string.contact), context.getResources().getString(R.string.socialLink));
    }

    public Setting(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11) {
        this.language = str;
        this.about = str2;
        this.experience = str3;
        this.education = str4;
        this.skill = str5;
        this.project = str6;
        this.reference = str7;
        this.achievements = str8;
        this.hobby = str9;
        this.contact = str10;
        this.socialLink = str11;
    }

    protected Setting(Parcel parcel) {
        this.language = parcel.readString();
        this.about = parcel.readString();
        this.experience = parcel.readString();
        this.education = parcel.readString();
        this.skill = parcel.readString();
        this.project = parcel.readString();
        this.reference = parcel.readString();
        this.achievements = parcel.readString();
        this.hobby = parcel.readString();
        this.contact = parcel.readString();
        this.socialLink = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.language);
        parcel.writeString(this.about);
        parcel.writeString(this.experience);
        parcel.writeString(this.education);
        parcel.writeString(this.skill);
        parcel.writeString(this.project);
        parcel.writeString(this.reference);
        parcel.writeString(this.achievements);
        parcel.writeString(this.hobby);
        parcel.writeString(this.contact);
        parcel.writeString(this.socialLink);
    }

    public String getAbout() {
        return this.about;
    }

    public void setAbout(String str) {
        this.about = str;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String str) {
        this.reference = str;
    }

    public String getExperience() {
        return this.experience;
    }

    public void setExperience(String str) {
        this.experience = str;
    }

    public String getEducation() {
        return this.education;
    }

    public void setEducation(String str) {
        this.education = str;
    }

    public String getSkill() {
        return this.skill;
    }

    public void setSkill(String str) {
        this.skill = str;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String str) {
        this.project = str;
    }

    public String getAchievements() {
        return this.achievements;
    }

    public void setAchievements(String str) {
        this.achievements = str;
    }

    public String getHobby() {
        return this.hobby;
    }

    public void setHobby(String str) {
        this.hobby = str;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String str) {
        this.contact = str;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public String getSocialLink() {
        return this.socialLink;
    }

    public void setSocialLink(String str) {
        this.socialLink = str;
    }
}
