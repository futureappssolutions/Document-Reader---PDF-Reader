package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate;

import android.content.Context;
import android.net.Uri;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

public class ResumeTemplate6 {
    public String content;
    public Resume resume;
    
    public String getStyle() {
        return "<style>@import url(\"https://fonts.googleapis.com/css?family=Montserrat:400,500,700&display=swap\");* {  margin: 0;  padding: 0;  box-sizing: border-box;  list-style: none;  font-family: \"Montserrat\", sans-serif;}body {  background: #121B3B;  font-size: 14px;  line-height: 22px;  color: #555555;}.bold {  font-size: 20px;   padding: 10px;  text-transform: uppercase;   background: #F6463E;}.user_name {  font-weight: 700;  font-size: 40px;  text-transform: uppercase;  margin-top : 20px;  line-height: 1.0;  color: #F6463E;}.semi-bold {  font-weight: 500;  font-size: 16px;}.resume {  width: 800px;  height: auto;  display: flex;  margin: 50px auto;}.resume .resume_left {  width: 280px;  background: #fff;}.resume .resume_left .resume_profile {  width: 100%;  height: 280px;}.resume .resume_left .resume_profile img {  width: 100%;  height: 100%;}.resume .resume_left .resume_content {  padding: 0 25px;}.resume .title {  margin-bottom: 20px;}.resume .resume_left .bold {  color: #ffffff;  font-weight: 700;}.resume .resume_left .regular {  color: #4f4f4b;}.resume .resume_right .job_title {font-size: 20px;margin-top:20px;color: #ffffff;text-transform: uppercase;    letter-spacing: 5px;}.resume .resume_item {  padding: 25px 0;}.resume .resume_left .resume_item:last-child,.resume .resume_right .resume_item:last-child {  border-bottom: 0px;}.resume .resume_left ul li {  display: flex;  margin-bottom: 10px;  align-items: center;}.resume .resume_left ul li:last-child {  margin-bottom: 0;}.resume .resume_left ul li .icon {  width: 35px;  height: 35px;  background: #fff;  color: #0bb5f4;  border-radius: 50%;  margin-right: 15px;  font-size: 16px;  position: relative;}.resume .icon i,.resume .resume_right .resume_hobby ul li i {  position: absolute;  top: 50%;  left: 50%;  transform: translate(-50%, -50%);}.resume .resume_left ul li .data {  color: #4f4f4b;}.resume .resume_left .resume_skills ul li {  display: flex;  margin-bottom: 10px;  color: #4f4f4b;  justify-content: space-between;  align-items: center;}.resume .resume_left .resume_skills ul li .skill_name {  width: 50%;}.resume .resume_left .resume_skills ul li .skill_progress {  width: 48%;  margin: 0 5px;  height: 5px;  background: #d4ccca;  position: relative;}.resume .resume_left .resume_skills ul li .skill_per {  width: 15%;}.resume .resume_left .resume_skills ul li .skill_progress span {  position: absolute;  top: 0;  left: 0;  height: 100%;  background: #696969;}.resume .resume_left .resume_social .semi-bold {  color: #696969;  margin-bottom: 3px;  font-size: 14px;}.resume .resume_right {  width: 520px;color: #ffffff;   background: #121B3B;  padding: 25px;}.resume .resume_right .bold {  color: #121B3B;  font-weight: 700;}.resume .resume_right .resume_work ul,.resume .resume_right .resume_education ul {  padding-left: 40px;  overflow: hidden;}.resume .resume_right ul li {  position: relative;}.resume .resume_right ul li .date {  font-size: 16px;  font-weight: 500;  margin-bottom: 15px;}.resume .resume_right ul li .info {  margin-bottom: 20px;}.resume .resume_right ul li:last-child .info {  margin-bottom: 0;}.resume .resume_right .resume_work ul li:before,.resume .resume_right .resume_education ul li:before {  content: \"\";  position: absolute;  top: 5px;  left: -25px;  width: 6px;  height: 6px;}.resume .resume_right .resume_work ul li:after,.resume .resume_right .resume_education ul li:after {  content: \"\";  position: absolute;  top: 14px;  left: -21px;}.resume .resume_right .resume_hobby ul {  display: flex;  justify-content: space-between;}.resume .resume_right .resume_hobby ul li {  width: 80px;  height: 80px;  position: relative;  color: #0bb5f4;}.resume .resume_right .resume_hobby ul li i {  font-size: 30px;}.resume .resume_right .resume_hobby ul li:before {  content: \"\";  position: absolute;  top: 40px;  right: -52px;  width: 50px;  height: 2px;  background: #0bb5f4;}.resume .resume_right .resume_hobby ul li:last-child:before {  display: none;}</style>";
    }

    public String getContent(Context context, Resume resume2) {
        resume = resume2;
        content = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Resume/CV Design</title><script src=\"https://kit.fontawesome.com/b99e675b6e.js\"></script>" + getStyle() + "</head><body><div class=\"resume\"><div class=\"resume_left\">" + getProfilePic(context) + "<div class=\"resume_content\">" + getPersonalInfo() + getLanguage() + getSkill() + getSocialLink() + "</div></div>  <div class=\"resume_right\">" + getUserNameAndJobTitle() + getExperience() + getProject() + getEducation() + getHobbies() + getReference() + "  </div></div></body></html>";
        return content;
    }

    public String getPersonalInfo() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7 = "";
        if (!resume.personalInfo.getAddressLine1().isEmpty()) {
            str = "<li> <div class=\"data\">" + resume.personalInfo.getAddressLine1() + " </div> </li>";
        } else {
            str = str7;
        }
        if (!resume.personalInfo.getAddressLine1().isEmpty()) {
            str2 = "<li> <div class=\"data\">" + resume.personalInfo.getAddressLine2() + " </div> </li>";
        } else {
            str2 = str7;
        }
        if (!resume.personalInfo.getPhone().isEmpty()) {
            str3 = "<li> <div class=\"data\">" + resume.personalInfo.getPhone() + " </div> </li>";
        } else {
            str3 = str7;
        }
        if (!resume.personalInfo.getDob().isEmpty()) {
            str4 = "<li> <div class=\"data\">Date Of Birth : " + resume.personalInfo.getDob() + " </div> </li>";
        } else {
            str4 = str7;
        }
        if (!resume.personalInfo.getMaritalStatus().isEmpty()) {
            str5 = "<li> <div class=\"data\">Marital Status : " + resume.personalInfo.getMaritalStatus() + " </div> </li>";
        } else {
            str5 = str7;
        }
        if (!resume.personalInfo.getEmail().isEmpty()) {
            str6 = "<li> <div class=\"data\"><a href=\"mailto:" + resume.personalInfo.getEmail() + "\">" + resume.personalInfo.getEmail() + "</a> </div> </li>";
        } else {
            str6 = str7;
        }
        if (!resume.otherDetail.getWebSite().isEmpty()) {
            str7 = "<li> <div class=\"data\"><a href=\"https://" + resume.otherDetail.getWebSite() + "\">" + resume.otherDetail.getWebSite() + "</a> </div> </li>";
        }
        return "<div class=\"resume_item resume_info\"> <div class=\"title\"><p class=\"bold\">" + resume.setting.getAbout() + "</p></div><p>" + resume.personalInfo.getSummary() + "</p> <br/> <ul>" + str4 + str5 + str3 + str6 + str + str2 + str7 + " </ul></div>";
    }
    
    public String getProfilePic(Context context) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        if (sharedPrefs.getProfilePic() == null) {
            return "";
        }
        String convertToBase64String = Utility.convertToBase64String(context, Uri.parse(sharedPrefs.getProfilePic()));
        return " <div class=\"resume_profile\"><img src=\"data:image/png;base64, " + convertToBase64String + "\" alt=\"profile_pic\" /> </div>";
    }

    public String getLanguage() {
        String str = "";
        if (resume.skill.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.language.size(); i++) {
            str = str + "<li><div class=\"skill_name\">" + resume.language.get(i).getName() + " </div> <div class=\"skill_progress\">  <span style=\"width: " + (resume.language.get(i).getRatting() * 20) + "%;\"></span> </div></li>";
        }
        return "<div class=\"resume_item resume_skills\">         <div class=\"title\">           <p class=\"bold\">" + resume.setting.getLanguage() + "</p>         </div>         <ul>" + str + "         </ul>       </div>";
    }
    
    public String getSkill() {
        String str = "";
        if (resume.skill.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.skill.size(); i++) {
            str = str + "<li><div class=\"skill_name\">" + resume.skill.get(i).getName() + " </div> <div class=\"skill_progress\">  <span style=\"width: " + (resume.skill.get(i).getRatting() * 20) + "%;\"></span> </div></li>";
        }
        return "<div class=\"resume_item resume_skills\">         <div class=\"title\">           <p class=\"bold\">" + resume.setting.getSkill() + "</p>         </div>         <ul>" + str + "         </ul>       </div>";
    }

    public String getSocialLink() {
        String str;
        String str2;
        String str3 = "";
        if (resume.otherDetail.getFaceboook().isEmpty() && resume.otherDetail.getLinkedin().isEmpty() && resume.otherDetail.getTwitter().isEmpty()) {
            return str3;
        }
        if (!resume.otherDetail.getFaceboook().isEmpty()) {
            str = "<li><div class=\"data\"><p class=\"semi-bold\">Facebook</p><p><a href=\"https://www.facebook.com/" + resume.otherDetail.getFaceboook() + "\">" + resume.otherDetail.getFaceboook() + "</a></p></div></li>";
        } else {
            str = str3;
        }
        if (!resume.otherDetail.getTwitter().isEmpty()) {
            str2 = "<li><div class=\"data\"><p class=\"semi-bold\">Twitter</p><p><a href=\"https://twitter.com/" + resume.otherDetail.getTwitter() + "\">" + resume.otherDetail.getTwitter() + "</a></p></div></li>";
        } else {
            str2 = str3;
        }
        if (!resume.otherDetail.getLinkedin().isEmpty()) {
            str3 = "<li><div class=\"data\"><p class=\"semi-bold\">Linkedin</p><p><a href=\"https://www.linkedin.com/in/" + resume.otherDetail.getLinkedin() + "\">" + resume.otherDetail.getLinkedin() + "</a></p></div></li>";
        }
        return "<div class=\"resume_item resume_social\">         <div class=\"title\">           <p class=\"bold\">" + resume.setting.getSocialLink() + "</p>         </div>         <ul>" + str + str2 + str3 + "         </ul>       </div>";
    }

    public String getExperience() {
        String str = "";
        if (resume.experience.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.experience.size(); i++) {
            str = str + "<li><div class=\"date\">" + resume.experience.get(i).getFromDate() + " - " + resume.experience.get(i).getToDate() + "</div> <div class=\"info\"><p class=\"semi-bold\">" + resume.experience.get(i).getJobTitle() + " at " + resume.experience.get(i).getCompany() + " , " + resume.experience.get(i).getLocation() + "</p> <p>" + resume.experience.get(i).getDescription() + "</p></div></li>";
        }
        return "<div class=\"resume_item resume_work\"><div class=\"title\"><p class=\"bold\">" + resume.setting.getExperience() + "</p></div><ul>" + str + "</ul></div>";
    }
    
    public String getEducation() {
        String str = "";
        if (resume.schools.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.schools.size(); i++) {
            str = str + "<li><div class=\"date\">" + resume.schools.get(i).getFromDate() + " - " + resume.schools.get(i).getToDate() + "</div> <div class=\"info\"><p class=\"semi-bold\">" + resume.schools.get(i).getDegree() + " <br> " + resume.schools.get(i).getSchoolName() + " , " + resume.schools.get(i).getLocation() + "</p> <p>" + resume.schools.get(i).getDescription() + "</p></div></li>";
        }
        return "<div class=\"resume_item resume_work\"><div class=\"title\"><p class=\"bold\">" + resume.setting.getEducation() + "</p></div><ul>" + str + "</ul></div>";
    }
    
    public String getProject() {
        String str = "";
        if (resume.projects.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.projects.size(); i++) {
            str = str + "<li><div class=\"date\">" + resume.projects.get(i).getFromDate() + " - " + resume.projects.get(i).getToDate() + "</div> <div class=\"info\"><p class=\"semi-bold\">" + resume.projects.get(i).getName() + "</p> <p>" + resume.projects.get(i).getDescription() + "</p></div></li>";
        }
        return "<div class=\"resume_item resume_work\"><div class=\"title\"><p class=\"bold\">" + resume.setting.getProject() + "</p></div><ul>" + str + "</ul></div>";
    }
    
    public String getHobbies() {
        String str = "";
        if (resume.hobbies.size() <= 0) {
            return str;
        }
        String str2 = str;
        for (int i = 0; i < resume.hobbies.size(); i++) {
            if (i % 2 == 0) {
                str = str + "  <li>" + resume.hobbies.get(i).getName() + "</li>";
            } else {
                str2 = str2 + "  <li>" + resume.hobbies.get(i).getName() + "</li>";
            }
        }
        return " <div class=\"resume_item resume_education\"> <div class=\"title\"> <p class=\"bold\">" + resume.setting.getHobby() + "</p> </div> <ul> <li><div style=\"float:left; width:50%; \" class=\"info\"><ul style=\"Display:in-line-block;\">" + str + " </ul></div><div style=\"float:right; width:50%;\" class=\"info\"> <ul style=\"Display:in-line-block;\">" + str2 + "</ul></div> </li></ul></div>";
    }

    public String getReference() {
        if (resume.otherDetail.getReference().isEmpty()) {
            return "";
        }
        return " <div class=\"resume_item resume_education\"><div class=\"title\"><p class=\"bold\">" + resume.setting.getReference() + "</p></div><ul> <li> <div class=\"info\"> <p>" + resume.otherDetail.getReference() + "</p>  </div></li></ul></div>";
    }

    public String getUserNameAndJobTitle() {
        return " <div class=\"title\"><p class=\"user_name\">" + resume.personalInfo.getfName() + " " + resume.personalInfo.getlName() + "</p><p class=\"job_title\">" + resume.personalInfo.getJobTitle() + "</p></div>";
    }
}
