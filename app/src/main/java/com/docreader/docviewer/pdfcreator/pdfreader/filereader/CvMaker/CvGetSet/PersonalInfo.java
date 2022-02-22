package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonalInfo implements Parcelable {
    public static final Parcelable.Creator<PersonalInfo> CREATOR = new Parcelable.Creator<PersonalInfo>() {
        public PersonalInfo createFromParcel(Parcel parcel) {
            return new PersonalInfo(parcel);
        }

        public PersonalInfo[] newArray(int i) {
            return new PersonalInfo[i];
        }
    };
    private String addressLine1;
    private String addressLine2;
    private String dob;
    private String email;
    private String fName;
    private String jobTitle;
    private String lName;
    private String maritalStatus;
    private String phone;
    private String summary;

    @Override
    public int describeContents() {
        return 0;
    }

    public PersonalInfo() {
        this("Christopher", "Watson", "24-Aug-1993", "Single", "Senior Web Developer specialiing in front end development.Experienced with all stages of the development cycle for dynamic web projects.Well-versed in numerous programing languages including HTML, PHP, OOP, JavaScript, CSS, MySQL. Strong background  in project managment and customer relations.", "Web Developer", "176 Great portland street, london W5W abc", "16 Great portland street, london W5W", "+44 (0)20 1236 4589", "christopher5@gmail.com");
    }

    public PersonalInfo(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        this.fName = str;
        this.lName = str2;
        this.dob = str3;
        this.maritalStatus = str4;
        this.summary = str5;
        this.jobTitle = str6;
        this.addressLine1 = str7;
        this.addressLine2 = str8;
        this.phone = str9;
        this.email = str10;
    }

    protected PersonalInfo(Parcel parcel) {
        this.fName = parcel.readString();
        this.lName = parcel.readString();
        this.dob = parcel.readString();
        this.maritalStatus = parcel.readString();
        this.summary = parcel.readString();
        this.jobTitle = parcel.readString();
        this.addressLine1 = parcel.readString();
        this.addressLine2 = parcel.readString();
        this.phone = parcel.readString();
        this.email = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.fName);
        parcel.writeString(this.lName);
        parcel.writeString(this.dob);
        parcel.writeString(this.maritalStatus);
        parcel.writeString(this.summary);
        parcel.writeString(this.jobTitle);
        parcel.writeString(this.addressLine1);
        parcel.writeString(this.addressLine2);
        parcel.writeString(this.phone);
        parcel.writeString(this.email);
    }

    public String getfName() {
        return this.fName;
    }

    public void setfName(String str) {
        this.fName = str;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String str) {
        this.jobTitle = str;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public void setAddressLine1(String str) {
        this.addressLine1 = str;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public void setAddressLine2(String str) {
        this.addressLine2 = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getlName() {
        return this.lName;
    }

    public void setlName(String str) {
        this.lName = str;
    }

    public String getDob() {
        return this.dob;
    }

    public void setDob(String str) {
        this.dob = str;
    }

    public String getMaritalStatus() {
        return this.maritalStatus;
    }

    public void setMaritalStatus(String str) {
        this.maritalStatus = str;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String str) {
        this.summary = str;
    }
}
