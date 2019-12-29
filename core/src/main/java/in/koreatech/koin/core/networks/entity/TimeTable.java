package in.koreatech.koin.core.networks.entity;

import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TimeTable {

    @SerializedName("semester")
    @Expose
    public String semester = "";

    @SerializedName("timetable")
    @Expose
    public ArrayList<TimeTableItem> timeTableItems;

    public TimeTable() {
        timeTableItems = new ArrayList<>();
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public ArrayList<TimeTableItem> getTimeTableItems() {
        return timeTableItems;
    }

    public void setTimeTableItems(ArrayList<TimeTableItem> timeTableItems) {
        this.timeTableItems = timeTableItems;
    }

    public void addTimeTableItem(TimeTableItem timeTableItem) {
        timeTableItems.add(timeTableItem);
    }


    public static class TimeTableItem implements Serializable {
        @SerializedName("class_time")
        @Expose
        public ArrayList<Integer> classTime;

        @SerializedName("id")
        @Expose
        public int id;

        @SerializedName("class_title")
        @Expose
        public String classTitle = "";

        @SerializedName("lecture_class")
        @Expose
        public String lectureClass = "";

        @SerializedName("professor")
        @Expose
        public String professor = "";

        @SerializedName("grades")
        @Expose
        public String grades = "";

        //추가
        @SerializedName("class_place")
        @Expose
        public String classPlace = "";

        @SerializedName("department")
        @Expose
        public String department = "";

        @SerializedName("regular_number")
        @Expose
        public String regularNumber = "";

        @SerializedName("target")
        @Expose
        public String target = "";


        @SerializedName("design_score")
        @Expose
        public String designScore = "";


        @SerializedName("code")
        @Expose
        public String code = "";


        public boolean isSavedAtServer;
        private transient ArrayList<TextView> stickerTextview;


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getClassPlace() {
            return classPlace;
        }

        public void setClassPlace(String classPlace) {
            this.classPlace = classPlace;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getRegularNumber() {
            return regularNumber;
        }

        public void setRegularNumber(String regularNumber) {
            this.regularNumber = regularNumber;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getDesignScore() {
            return designScore;
        }

        public void setDesignScore(String designScore) {
            this.designScore = designScore;
        }

        public TimeTableItem() {
            initArrayList();
            stickerTextview = new ArrayList<>();
            classTime = new ArrayList<>();
            isSavedAtServer = false;
        }

        public TimeTableItem(Lecture lecture) {
            initArrayList();
            this.classTitle = lecture.name;
            this.classTime = lecture.classTime;
            this.professor = lecture.professor;
            this.grades = lecture.grades;
            this.lectureClass = lecture.lectureClass;
            this.code = lecture.code;
            this.department = lecture.department;
            this.regularNumber = lecture.regularNumber;
            this.target = lecture.target;
            this.designScore = lecture.designScore;
        }

        public TimeTableItem(String classTitle, ArrayList<Integer> classTime, String professor) {
            initArrayList();
            isSavedAtServer = false;
            this.classTitle = classTitle;
            this.classTime = classTime;
            this.professor = professor;
        }

        public void initArrayList() {
            this.stickerTextview = new ArrayList<>();
            this.classTime = new ArrayList<>();
        }

        public ArrayList<TextView> getStickerTextview() {
            return stickerTextview;
        }

        public void setStickerTextview(ArrayList<TextView> stickerTextview) {
            this.stickerTextview = stickerTextview;
        }

        public void addStickerTextView(TextView stickerTextview) {
            this.stickerTextview.add(stickerTextview);
        }

        public ArrayList<Integer> getClassTime() {
            return classTime;
        }

        public void setClassTime(ArrayList<Integer> classTime) {
            this.classTime = classTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getClassTitle() {
            return classTitle;
        }

        public void setClassTitle(String classTitle) {
            this.classTitle = classTitle;
        }

        public String getLectureClass() {
            return lectureClass;
        }

        public void setLectureClass(String lectureClass) {
            this.lectureClass = lectureClass;
        }

        public String getProfessor() {
            return professor;
        }

        public void setProfessor(String professor) {
            this.professor = professor;
        }

        public String getGrades() {
            return grades;
        }

        public void setGrades(String grades) {
            this.grades = grades;
        }
    }
}
