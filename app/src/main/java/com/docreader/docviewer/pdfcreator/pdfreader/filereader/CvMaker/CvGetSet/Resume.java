package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Resume implements Parcelable {
    public static final Parcelable.Creator<Resume> CREATOR = new Parcelable.Creator<Resume>() {
        public Resume createFromParcel(Parcel parcel) {
            return new Resume(parcel);
        }

        public Resume[] newArray(int i) {
            return new Resume[i];
        }
    };
    public List<Experience> experience;
    public ArrayList<Hobby> hobbies;
    public ArrayList<Language> language;
    public OtherDetail otherDetail;
    public PersonalInfo personalInfo;
    public List<Project> projects;
    public List<School> schools;
    public Setting setting;
    public ArrayList<Skill> skill;
    public String test;
    public int version;

    @Override
    public int describeContents() {
        return 0;
    }

    private Resume() {
    }

    protected Resume(Parcel parcel) {
        personalInfo = parcel.readParcelable(PersonalInfo.class.getClassLoader());
        otherDetail = parcel.readParcelable(OtherDetail.class.getClassLoader());
        setting = parcel.readParcelable(Setting.class.getClassLoader());
        projects = parcel.createTypedArrayList(Project.CREATOR);
        schools = parcel.createTypedArrayList(School.CREATOR);
        skill = parcel.createTypedArrayList(Skill.CREATOR);
        language = parcel.createTypedArrayList(Language.CREATOR);
        experience = parcel.createTypedArrayList(Experience.CREATOR);
        hobbies = parcel.createTypedArrayList(Hobby.CREATOR);
        test = parcel.readString();
        version = parcel.readInt();
    }

    public static Resume createNewResume(Context context) {
        Resume resume = new Resume();
        resume.personalInfo = new PersonalInfo();
        resume.otherDetail = new OtherDetail();
        resume.setting = new Setting(context);
        resume.schools = new ArrayList<>();

        School school = new School();
        school.setSchoolName("Columbia University");
        school.setLocation("New York");
        school.setDegree("Bachlelor of science Computer information System");
        school.setDescription("some detail");
        school.setFromDate("2010");
        school.setToDate("2014");
        resume.schools.add(school);

        resume.skill = new ArrayList<>();
        resume.skill.add(new Skill("Project management", 5));
        resume.skill.add(new Skill("Creative design", 4));
        resume.skill.add(new Skill("Service focused", 5));

        resume.language = new ArrayList<>();
        resume.language.add(new Language("English", 5));
        resume.language.add(new Language("Chines", 5));
        resume.language.add(new Language("German", 3));

        resume.experience = new ArrayList<>();

        Experience experience2 = new Experience();
        experience2.setJobTitle("Web Developer");
        experience2.setCompany("Luna Web Design");
        experience2.setLocation("New York");
        experience2.setDescription("Cooperate with designers to create clean interfaces and simple, interaction and experiences. Develop project concepts and maintain optimal workflow.Complete detailed programing and developement tasks for front & back end.");
        experience2.setFromDate("09/2015");
        experience2.setToDate("05/217");
        resume.experience.add(experience2);

        resume.hobbies = new ArrayList<>();
        resume.hobbies.add(new Hobby("Writing"));
        resume.hobbies.add(new Hobby("Sketching"));
        resume.hobbies.add(new Hobby("Design"));

        resume.projects = new ArrayList<>();

        Project project = new Project();
        project.setName("7 Star Hospital Management");
        project.setDetail("");
        project.setDescription("Hospital Project Description....");
        project.setFromDate("May 2017");
        project.setToDate("July 2017");
        resume.projects.add(project);
        resume.test = "";
        resume.version = 1;
        return resume;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(personalInfo, i);
        parcel.writeParcelable(otherDetail, i);
        parcel.writeParcelable(setting, i);
        parcel.writeTypedList(projects);
        parcel.writeTypedList(schools);
        parcel.writeTypedList(skill);
        parcel.writeTypedList(hobbies);
        parcel.writeTypedList(language);
        parcel.writeTypedList(experience);
        parcel.writeString(test);
        parcel.writeInt(version);
    }
}
