package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Lecture {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("grades")
    @Expose
    private String grades;

    @SerializedName("lecture_class")
    @Expose
    private String lectureClass;

    @SerializedName("regular_number")
    @Expose
    private String regularNumber;

    @SerializedName("department")
    @Expose
    private String department;

    @SerializedName("target")
    @Expose
    private String target;

    @SerializedName("professor")
    @Expose
    private String professor;

    @SerializedName("is_english")
    @Expose
    private String isEnglish;

    @SerializedName("design_score")
    @Expose
    private String designScore;

    @SerializedName("is_elearning")
    @Expose
    private String isElearning;

    @SerializedName("class_time")
    @Expose
    private ArrayList<Integer> classTime;

    public boolean isItemClicked = false;

    public boolean isAddButtonClicked = false;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public String getLectureClass() {
        return lectureClass;
    }

    public void setLectureClass(String lectureClass) {
        this.lectureClass = lectureClass;
    }

    public String getRegularNumber() {
        return regularNumber;
    }

    public void setRegularNumber(String regularNumber) {
        this.regularNumber = regularNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getIsEnglish() {
        return isEnglish;
    }

    public void setIsEnglish(String isEnglish) {
        this.isEnglish = isEnglish;
    }

    public String getDesignScore() {
        return designScore;
    }

    public void setDesignScore(String designScore) {
        this.designScore = designScore;
    }

    public String getIsElearning() {
        return isElearning;
    }

    public void setIsElearning(String isElearning) {
        this.isElearning = isElearning;
    }

    public ArrayList<Integer> getClassTime() {
        return classTime;
    }

    public void setClassTime(ArrayList<Integer> classTime) {
        this.classTime = classTime;
    }

    public boolean isItemClicked() {
        return isItemClicked;
    }

    public void setItemClicked(boolean itemClicked) {
        isItemClicked = itemClicked;
    }

    public boolean isAddButtonClicked() {
        return isAddButtonClicked;
    }

    public void setAddButtonClicked(boolean addButtonClicked) {
        isAddButtonClicked = addButtonClicked;
    }
}
