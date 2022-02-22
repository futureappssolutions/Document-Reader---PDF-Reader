package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate;

import android.content.Context;
import android.net.Uri;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Resume;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

public class ResumeTemplate2 {
    public String content;
    public Resume resume;
    
    public String getStyle() {
        return "<style>.msg { padding: 10px; background: #222; position: relative; }.msg h1 { color: #fff;  }.msg a { margin-left: 20px; background: #408814; color: white; padding: 4px 8px; text-decoration: none; }.msg a:hover { background: #266400; }/* //-- yui-grids style overrides -- */body { font-family: Georgia; color: #444; }#body { font-family: Montserrat; color: #000000; }#inner { padding: 10px 80px; margin: 80px auto; background: #f5f5f5; border: solid #666; border-width: 8px 0 2px 0; }.yui-gf { margin-bottom: 2em; padding-bottom: 2em; border-bottom: 1px solid #ccc; }/* //-- header, body, footer -- */#hd { margin: 2.5em 0 3em 0; padding-bottom: 1.5em; border-bottom: 1px solid #ccc }#hd h2 { text-transform: uppercase; letter-spacing: 2px; }#bd, #ft { margin-bottom: 2em; }/* //-- footer -- */#ft { padding: 1em 0 5em 0; font-size: 92%; border-top: 1px solid #ccc; text-align: center; }#ft p { margin-bottom: 0; text-align: center;   }/* //-- core typography and style -- */#hd h1 { font-size: 48px; text-transform: uppercase; letter-spacing: 3px; }h2 { font-size: 152% }h3, h4 { font-size: 122%; }h1, h2, h3, h4 { color: #333; }p { font-size: 100%; line-height: 18px; padding-right: 3em; }a { color: #990003 }a:hover { text-decoration: none; }strong { font-weight: bold; }li { line-height: 24px;  }p.enlarge { font-size: 144%; padding-right: 6.5em; line-height: 24px; }p.enlarge span { color: #000 }.contact-info { margin-top: 7px; }.first h2 { font-style: italic; }.last { border-bottom: 0 }/* //-- section styles -- */a#pdf { display: block; float: left; background: #666; color: white; padding: 6px 50px 6px 12px; margin-bottom: 6px; text-decoration: none;  }a#pdf:hover { background: #222; }.job { position: relative; margin-bottom: 1em; padding-bottom: 1em; border-bottom: 1px solid #ccc; }.job h4 { position: absolute; top: 0.35em; right: 0 }.job p { margin: 0.75em 0 3em 0; }.last { border: none; }.skills-list {  }.skills-list ul { margin: 0; }.skills-list li { margin: 3px 0; padding: 3px 0; }.skills-list li span { font-size: 152%; display: block; margin-bottom: -2px; padding: 0 }.talent { width: 32%; float: left }.talent h2 { margin-bottom: 6px; }#srt-ttab { margin-bottom: 100px; text-align: center;  }#srt-ttab img.last { margin-top: 20px }.yui-gf .yui-u{width:80.2%;}.yui-gf div.first{width:12.3%;}</style>";
    }

    public String getContent(Context context, Resume resume2) {
        resume = resume2;
        content = "<head><title>Jonathan Doe | Web Designer, Director | name@yourdomain.com</title><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /><meta name=\"keywords\" content=\"\" /><meta name=\"description\" content=\"\" /><link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet'><link rel=\"stylesheet\" type=\"text/css\" href=\"http://yui.yahooapis.com/2.7.0/build/reset-fonts-grids/reset-fonts-grids.css\" media=\"all\" /> " + getStyle() + "</head><body><div id=\"doc2\" class=\"yui-t7\"><div id=\"inner\">" + getPersonalInfo() + "<div id=\"bd\"><div id=\"yui-main\"><div class=\"yui-b\"><div class=\"yui-gf\"><div class=\"yui-u first\"><h2>" + resume2.setting.getAbout() + "</h2></div><div class=\"yui-u\"><p >" + resume2.personalInfo.getSummary() + "</p></div></div><!--// .yui-gf -->" + getSkill() + getProject() + getExperience() + getEducation() + getLanguage() + getSocialLink() + getHobbies() + "</div><!--// .yui-b --></div><!--// yui-main --></div><!--// bd -->" + getReference() + "</div><!-- // inner --></div><!--// doc --></body></html>";
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
            str = "<h3>" + resume.personalInfo.getAddressLine1() + " </h3>";
        } else {
            str = str7;
        }
        if (!resume.personalInfo.getAddressLine1().isEmpty()) {
            str2 = "<h3>" + resume.personalInfo.getAddressLine2() + " </h3>";
        } else {
            str2 = str7;
        }
        if (!resume.personalInfo.getPhone().isEmpty()) {
            str3 = "<h3>" + resume.personalInfo.getPhone() + " </h3>";
        } else {
            str3 = str7;
        }
        if (!resume.personalInfo.getDob().isEmpty()) {
            str4 = "<h3>Date Of Birth : " + resume.personalInfo.getDob() + " </h3>";
        } else {
            str4 = str7;
        }
        if (!resume.personalInfo.getMaritalStatus().isEmpty()) {
            str5 = "<h3>Marital Status : " + resume.personalInfo.getMaritalStatus() + " </h3>";
        } else {
            str5 = str7;
        }
        if (!resume.personalInfo.getEmail().isEmpty()) {
            str6 = "<h3><a href=\"mailto:" + resume.personalInfo.getEmail() + "\">" + resume.personalInfo.getEmail() + "</a></h3>";
        } else {
            str6 = str7;
        }
        if (!resume.otherDetail.getWebSite().isEmpty()) {
            str7 = "<h3><a href=\"https://" + resume.otherDetail.getWebSite() + "\">" + resume.otherDetail.getWebSite() + "</a> </h3>";
        }
        return "<div id=\"hd\"><div class=\"yui-gc\"><div class=\"yui-u first\"><h1>" + resume.personalInfo.getfName() + " " + resume.personalInfo.getlName() + "</h1><h2>" + resume.personalInfo.getJobTitle() + "</h2></div><div class=\"yui-u\"><div class=\"contact-info\">" + str3 + str6 + str4 + str5 + str + str2 + str7 + "</div><!--// .contact-info --></div></div><!--// .yui-gc --></div><!--// hd -->";
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
        if (resume.language.size() <= 0) {
            return str;
        }
        String str2 = str;
        String str3 = str2;
        char c = 0;
        for (int i = 0; i < resume.language.size(); i++) {
            if (c == 0) {
                str = str + "<li>" + resume.language.get(i).getName() + "</li>";
                c = 1;
            } else if (c == 1) {
                str2 = str2 + "<li>" + resume.language.get(i).getName() + "</li>";
                c = 2;
            } else {
                str3 = str3 + "<li>" + resume.language.get(i).getName() + "</li>";
                c = 0;
            }
        }
        return "<div class=\"yui-gf\"><div class=\"yui-u first\"><h2>" + resume.setting.getLanguage() + "</h2></div><div class=\"yui-u\"><ul class=\"talent\">" + str + "</ul><ul class=\"talent\">" + str2 + "</ul><ul class=\"talent\">" + str3 + "</ul></div></div>";
    }
    
    public String getSkill() {
        String str = "";
        if (resume.skill.size() <= 0) {
            return str;
        }
        String str2 = str;
        String str3 = str2;
        char c = 0;
        for (int i = 0; i < resume.skill.size(); i++) {
            if (c == 0) {
                str = str + "<li>" + resume.skill.get(i).getName() + "</li>";
                c = 1;
            } else if (c == 1) {
                str2 = str2 + "<li>" + resume.skill.get(i).getName() + "</li>";
                c = 2;
            } else {
                str3 = str3 + "<li>" + resume.skill.get(i).getName() + "</li>";
                c = 0;
            }
        }
        return "<div class=\"yui-gf\"><div class=\"yui-u first\"><h2>" + resume.setting.getSkill() + "</h2></div><div class=\"yui-u\"><ul class=\"talent\">" + str + "</ul><ul class=\"talent\">" + str2 + "</ul><ul class=\"talent\">" + str3 + "</ul></div></div>";
    }

    public String getSocialLink() {
        String str;
        String str2;
        String str3 = "";
        if (resume.otherDetail.getFaceboook().isEmpty() && resume.otherDetail.getLinkedin().isEmpty() && resume.otherDetail.getTwitter().isEmpty()) {
            return str3;
        }
        if (!resume.otherDetail.getFaceboook().isEmpty()) {
            str = "<div class=\"talent\"><h2>Facebook</h2><p><a href=\"https://www.facebook.com/" + resume.otherDetail.getFaceboook() + "\">" + resume.otherDetail.getFaceboook() + "</a></p></div>";
        } else {
            str = str3;
        }
        if (!resume.otherDetail.getTwitter().isEmpty()) {
            str2 = "<div class=\"talent\"><h2>Twitter</h2><p><a href=\"https://twitter.com/" + resume.otherDetail.getTwitter() + "\">" + resume.otherDetail.getTwitter() + "</a></p></div>";
        } else {
            str2 = str3;
        }
        if (!resume.otherDetail.getLinkedin().isEmpty()) {
            str3 = "<div class=\"talent\"><h2>Linkedin</h2><p><a href=\"https://www.linkedin.com/in/" + resume.otherDetail.getLinkedin() + "\">" + resume.otherDetail.getLinkedin() + "</a></p></div>";
        }
        return "<div class=\"yui-gf\"><div class=\"yui-u first\"><h2>" + resume.setting.getSocialLink() + "</h2></div><div class=\"yui-u\">" + str + str2 + str3 + "</div></div>";
    }
    
    public String getExperience() {
        String str = "";
        if (resume.experience.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.experience.size(); i++) {
            str = str + "<div class=\"job\"><h2>" + resume.experience.get(i).getCompany() + " , " + resume.experience.get(i).getLocation() + "</h2><h3>" + resume.experience.get(i).getJobTitle() + "</h3><h4>" + resume.experience.get(i).getFromDate() + " - " + resume.experience.get(i).getToDate() + "</h4><p>" + resume.experience.get(i).getDescription() + "</p></div>";
        }
        return "<div class=\"yui-gf\"><div class=\"yui-u first\"><h2>" + resume.setting.getExperience() + "</h2></div><!--// .yui-u --><div class=\"yui-u\">" + str + "</div><!--// .yui-u --></div>";
    }

    public String getEducation() {
        String str = "";
        if (resume.schools.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.schools.size(); i++) {
            str = str + "<div class=\"job\"><h2>" + resume.schools.get(i).getDegree() + "</h2><h3>" + resume.schools.get(i).getSchoolName() + " , " + resume.schools.get(i).getLocation() + " </h3 > <h4>" + resume.schools.get(i).getFromDate() + " - " + resume.schools.get(i).getToDate() + "</h4><p>" + resume.schools.get(i).getDescription() + "</p></div>";
        }
        return "<div class=\"yui-gf\"><div class=\"yui-u first\"><h2>" + resume.setting.getEducation() + "</h2></div><!--// .yui-u --><div class=\"yui-u\">" + str + "</div><!--// .yui-u --></div>";
    }

    public String getProject() {
        String str = "";
        if (resume.projects.size() <= 0) {
            return str;
        }
        for (int i = 0; i < resume.projects.size(); i++) {
            str = str + "<div class=\"job\"><h2>" + resume.projects.get(i).getName() + "</h2><h4>" + resume.projects.get(i).getFromDate() + " - " + resume.projects.get(i).getToDate() + "</h4><p>" + resume.projects.get(i).getDescription() + "</p></div>";
        }
        return "<div class=\"yui-gf\"><div class=\"yui-u first\"><h2>" + resume.setting.getProject() + "</h2></div><!--// .yui-u --><div class=\"yui-u\">" + str + "</div><!--// .yui-u --></div><!--// .yui-gf -->";
    }
    
    public String getHobbies() {
        String str = "";
        if (resume.hobbies.size() <= 0) {
            return str;
        }
        String str2 = str;
        String str3 = str2;
        char c = 0;
        for (int i = 0; i < resume.hobbies.size(); i++) {
            if (c == 0) {
                str = str + "  <li>" + resume.hobbies.get(i).getName() + "</li>";
                c = 1;
            } else if (c == 1) {
                str2 = str2 + "  <li>" + resume.hobbies.get(i).getName() + "</li>";
                c = 2;
            } else if (c == 2) {
                str3 = str3 + "  <li>" + resume.hobbies.get(i).getName() + "</li>";
                c = 0;
            }
        }
        return "<div class=\"yui-gf last\"><div class=\"yui-u first\"><h2>" + resume.setting.getHobby() + "</h2></div><div class=\"yui-u\"><ul class=\"talent\">" + str + "</ul><ul class=\"talent\">" + str2 + "</ul><ul class=\"talent\">" + str3 + "</ul></div></div>";
    }

    public String getReference() {
        if (resume.otherDetail.getReference().isEmpty()) {
            return "";
        }
        return "<div id=\"ft\"><p>" + resume.setting.getReference() + " &mdash; " + resume.otherDetail.getReference() + "</p></div>";
    }
}
