package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Lecture {
    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("grades")
    @Expose
    public String grades;

    @SerializedName("lecture_class")
    @Expose
    public String lectureClass;

    @SerializedName("regular_number")
    @Expose
    public String regularNumber;

    @SerializedName("department")
    @Expose
    public String department;

    @SerializedName("target")
    @Expose
    public String target;

    @SerializedName("professor")
    @Expose
    public String professor;

    @SerializedName("is_english")
    @Expose
    public String isEnglish;

    @SerializedName("design_score")
    @Expose
    public String designScore;

    @SerializedName("is_elearning")
    @Expose
    public String isElearning;

    @SerializedName("class_time")
    @Expose
    public ArrayList<Integer> classTime;

    public boolean isItemClicked = false;

    public boolean isAddButtonClicked = false;

}
