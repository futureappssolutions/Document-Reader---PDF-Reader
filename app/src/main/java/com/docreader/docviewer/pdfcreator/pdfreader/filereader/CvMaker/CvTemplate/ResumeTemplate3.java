package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate;

import android.content.Context;
import android.net.Uri;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

public class ResumeTemplate3 {
    public Resume resume;
    public String content;
    public String ul1 = "";
    public String ul2 = "";
    public String ul3 = "";
    public String ul4 = "";
    
    public String getStyle() {
        return "      <style>@import 'https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.0/normalize.min.css';         @import 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css';         @import 'https://fonts.googleapis.com/css?family=Rubik:700';         @page {         size: 8.5in 11in;         margin: 0.75in 0.75in 0.75in 0.75in; }         :root {         font-size: 14px; }         * {         box-sizing: border-box; }         body {         width: 8.5in;         height: 11in;         margin: 0;         font-family: sans-serif;         line-height: 1.3;         font-weight: 400;         color: #444;         hyphens: auto; }         h1,         h2,         h3 {         font-family: \"Rubik\", sans-serif;         font-weight: 700;         letter-spacing: .5px; }         h4,         h5,         h6 {         letter-spacing: .3px;         font-size: 1rem;         font-weight: 500; }         h1,         h2,         h3,         h4,         h5,         h6 {         margin: 0 0 0.5rem 0; }         h1 {         font-size: 3rem; }         h2 {         font-size: 2rem; }         h3 {         font-size: 1.2rem; }         ul {         list-style: none;         padding: 0; }         a {         color: #008CFF; }         small {         font-size: 0.8rem; }         main {         display: flex;         flex-direction: row;         flex-wrap: nowrap;         justify-content: space-between;         align-items: stretch;         align-content: stretch; }         .block {         margin-bottom: 2rem; }         .block.boxed {         border-bottom: 1px dashed #e2e7e8; }         .block:last-child {         margin-bottom: 0; }         .block h2 {         text-transform: uppercase;         border-bottom: 3px solid #384347;         margin-bottom: 1rem; }         #content {         padding: 0 1rem 0 0;         width: 70%; }         #sidebar {         padding: 0 0 3.08rem 1rem;         width: 30%;         position: relative; }         #heading {         display: flex; }         #heading .info {         width: calc(100% - 120px);         padding-right: 1rem; }         #heading .image {         width: 120px; }         #heading .image img {         border-radius: .4rem;         margin-left: auto; }         #heading h1 {         text-transform: uppercase;         line-height: 1;         margin: 0; }         #heading span {         font-size: 1.5rem;         font-family: \"Rubik\", sans-serif;         font-weight: 700;         color: #008CFF; }         #heading i {         color: #008CFF; }         #heading .links {         display: table;         margin: 1rem 0 2rem;         width: 100%; }         #heading .links ul {         display: table-row; }         #heading .links ul li {         display: table-cell; }         #heading a {         color: #384347;         text-decoration: none; }         .job header ul {         margin: 0;         display: flex;         justify-content: space-between; }         .job header li {         flex-wrap: nowrap;         text-align: center; }         .job header li:first-child {         text-align: left; }         .job header li:last-child {         text-align: right; }         .job .details ul {         list-style: disc;         padding-left: 2rem; }         .job .place {         color: #008CFF;         font-weight: 500;         font-family: \"Rubik\", sans-serif; }         .job .place a {         text-decoration: none; }         .job h3 {         margin-bottom: 0.25rem; }         .job h4 {         margin-bottom: 0;         font-weight: 500; }         .job h4 + p,         .job h4 + ul {         margin-top: 0; }         .rate-item {         padding-bottom: 1rem;         margin-bottom: 1rem; }         .rate-item h5 {         margin-bottom: 0; }         .rate {         display: flex;         flex-wrap: nowrap;         justify-content: space-between;         margin-top: 0.5rem; }         .rate span {         width: 1.4rem;         height: .4rem;         border-radius: .4rem;         background: #e2e7e8; }         .rate span.fill {         background: #008CFF; }         #repo {         text-align: right;         position: absolute;         bottom: 0;         left: 1rem;         right: 0;         font-size: 0.8rem; }      </style>";
    }

    public String getContent(Context context, Resume resume2) {
        resume = resume2;
        content = "<html>   <head>      <meta charset=\"UTF-8\">   </head>   <body>" + getStyle() + getPersonalInfo() + "      </header>      <main>         <article id=\"content\">            <div class=\"block boxed\"><h2>" + resume2.setting.getAbout() + "</h2><p >" + resume2.personalInfo.getSummary() + "</p>            </div>" + getExperience() + getProject() + getEducation() + getReference() + "         </article>         <aside id=\"sidebar\">" + getSkill() + getLanguage() + getHobbies() + getSocialLink() + "         </aside>      </main>   </body></html>";
        return content;
    }
    
    public String getPersonalInfo() {
        int i;
        ul1 = "";
        ul2 = "";
        ul3 = "";
        ul4 = "";
        if (!resume.personalInfo.getAddressLine1().isEmpty()) {
            addValue(1, "<li>" + resume.personalInfo.getAddressLine1() + " </li>");
            i = 1;
        } else {
            i = 0;
        }
        if (!resume.personalInfo.getAddressLine1().isEmpty()) {
            i++;
            addValue(i, "<li>" + resume.personalInfo.getAddressLine2() + " </li>");
        }
        if (!resume.personalInfo.getPhone().isEmpty()) {
            i++;
            addValue(i, "<li>" + resume.personalInfo.getPhone() + " </li>");
        }
        if (!resume.personalInfo.getDob().isEmpty()) {
            i++;
            addValue(i, "<li>Date Of Birth : " + resume.personalInfo.getDob() + " </li>");
        }
        if (!resume.personalInfo.getMaritalStatus().isEmpty()) {
            i++;
            addValue(i, "<li>Marital Status : " + resume.personalInfo.getMaritalStatus() + " </li>");
        }
        if (!resume.personalInfo.getEmail().isEmpty()) {
            i++;
            addValue(i, "<li><a href=\"mailto:" + resume.personalInfo.getEmail() + "\">" + resume.personalInfo.getEmail() + "</a></li>");
        }
        if (!resume.otherDetail.getWebSite().isEmpty()) {
            addValue(i + 1, "<li><a href=\"https://" + resume.otherDetail.getWebSite() + "\">" + resume.otherDetail.getWebSite() + "</a> </li>");
        }
        return "      <header id=\"heading\">         <div class=\"info\"><h1>" + resume.personalInfo.getfName() + " " + resume.personalInfo.getlName() + "</h1>            <span>" + resume.personalInfo.getJobTitle() + "</span>            <div class=\"links\">               <ul>" + ul1 + "               </ul>               <ul>" + ul2 + "               </ul>               <ul>" + ul3 + "               </ul>               <ul>" + ul4 + "               </ul>            </div>         </div>";
    }

    public String getProfilePic(Context context) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        if (sharedPrefs.getProfilePic() == null) {
            return "";
        }
        sharedPrefs.getProfilePic();
        String convertToBase64String = Utility.convertToBase64String(context, Uri.parse(sharedPrefs.getProfilePic()));
        return "         <div class=\"image\"><img src=\"data:image/png;base64, " + convertToBase64String + "alt=\"profile_pic\"  width=\"150px;\" height=\"150px;\"/>         </div>";
    }

    public String getLanguage() {
        String str = "";
        if (resume.language.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.language.size(); i++) {
            str = str + "<h5>" + resume.language.get(i).getName() + "</h5><br/>";
        }
        return "            <div class=\"block\"><h2>" + resume.setting.getLanguage() + "</h2>               <div class=\"block boxed rate-item\">" + str + "               </div>            </div>";
    }
    
    public String getSkill() {
        String str = "";
        if (resume.skill.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.skill.size(); i++) {
            str = str + "<h5>" + resume.skill.get(i).getName() + "</h5><br/>";
        }
        return "            <div class=\"block\"><h2>" + resume.setting.getSkill() + "</h2>               <div class=\"block boxed rate-item\">" + str + "               </div>            </div>";
    }
    
    public String getSocialLink() {
        String str;
        String str2;
        String str3 = "";
        if (resume.otherDetail.getFaceboook().isEmpty() && resume.otherDetail.getLinkedin().isEmpty() && resume.otherDetail.getTwitter().isEmpty()) {
            return str3;
        }
        if (!resume.otherDetail.getFaceboook().isEmpty()) {
            str = "<h5>Facebook<br/><small><a href=\"https://www.facebook.com/" + resume.otherDetail.getFaceboook() + "\">" + resume.otherDetail.getFaceboook() + "</a></small></h5>";
        } else {
            str = str3;
        }
        if (!resume.otherDetail.getTwitter().isEmpty()) {
            str2 = "<h5>Twitter<br/><small><a href=\"https://twitter.com/" + resume.otherDetail.getTwitter() + "\">" + resume.otherDetail.getTwitter() + "</a></small></h5>";
        } else {
            str2 = str3;
        }
        if (!resume.otherDetail.getLinkedin().isEmpty()) {
            str3 = "<h5>Linkedin<br/><small><a href=\"https://www.linkedin.com/in/" + resume.otherDetail.getLinkedin() + "\">" + resume.otherDetail.getLinkedin() + "</a></small></h5>";
        }
        return "            <div class=\"block\"><h2>" + resume.setting.getSocialLink() + "</h2>               <div class=\"block boxed rate-item\">" + str + str2 + str3 + "               </div>            </div>";
    }

    public String getExperience() {
        String str = "";
        if (resume.experience.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.experience.size(); i++) {
            str = str + "  <div class=\"block boxed job\">               <header><h3>" + resume.experience.get(i).getJobTitle() + "</h3>                  <span class=\"place\"></span>                  <ul>                     <li><i class=\"fa fa-building\" aria-hidden=\"true\"></i> at " + resume.experience.get(i).getCompany() + "                     </li>                     <li><i class=\"fa fa-map-marker\"></i> " + resume.experience.get(i).getLocation() + "                     </li>                     <li><i class=\"fa fa-calendar\"></i> " + resume.experience.get(i).getFromDate() + " - " + resume.experience.get(i).getToDate() + "                     </li>                  </ul>               </header>               <div class=\"details\"><p>" + resume.experience.get(i).getDescription() + "</p>               </div>            </div>";
        }
        return "<h2>" + resume.setting.getExperience() + "</h2>" + str;
    }

    public String getEducation() {
        String str = "";
        if (resume.schools.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.schools.size(); i++) {
            str = str + "  <div class=\"block boxed job\">               <header><h3>" + resume.schools.get(i).getDegree() + "</h3>                  <span class=\"place\"></span>                  <ul>                     <li><i class=\"fa fa-graduation-cap\" aria-hidden=\"true\"></i> at " + resume.schools.get(i).getSchoolName() + "                     </li>                     <li><i class=\"fa fa-map-marker\"></i> " + resume.schools.get(i).getLocation() + "                     </li>                     <li><i class=\"fa fa-calendar\"></i> " + resume.schools.get(i).getFromDate() + " - " + resume.schools.get(i).getToDate() + "                     </li>                  </ul>               </header>               <div class=\"details\"><p>" + resume.schools.get(i).getDescription() + "</p>               </div>            </div>";
        }
        return "<h2>" + resume.setting.getEducation() + "</h2>" + str;
    }
    
    public String getProject() {
        String str = "";
        if (resume.projects.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.projects.size(); i++) {
            str = str + "  <div class=\"block boxed job\">               <header><h3>" + resume.projects.get(i).getName() + "</h3>                  <span class=\"place\"></span>                  <ul>                     <li>                     <li><i class=\"fa fa-calendar\"></i> " + resume.projects.get(i).getFromDate() + " - " + resume.projects.get(i).getToDate() + "                     </li>                  </ul>               </header>               <div class=\"details\"><p>" + resume.projects.get(i).getDescription() + "</p>               </div>            </div>";
        }
        return "<h2>" + resume.setting.getProject() + "</h2>" + str;
    }
    
    public String getHobbies() {
        String str = "";
        if (resume.hobbies.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.hobbies.size(); i++) {
            str = str + "<h5>" + resume.hobbies.get(i).getName() + "</h5><br/>";
        }
        return "            <div class=\"block\"><h2>" + resume.setting.getHobby() + "</h2>               <div class=\"block boxed rate-item\">" + str + "               </div>            </div>";
    }
    
    public String getReference() {
        if (resume.otherDetail.getReference().isEmpty()) {
            return "";
        }
        return "<h2>" + resume.setting.getReference() + "</h2><p>" + resume.otherDetail.getReference() + "</p>";
    }

    public void addValue(int i, String str) {
        if (i == 1 || i == 2) {
            ul1 += str;
        } else if (i == 3 || i == 4) {
            ul2 += str;
        } else if (i == 5 || i == 6) {
            ul3 += str;
        } else {
            ul4 += str;
        }
    }
}
