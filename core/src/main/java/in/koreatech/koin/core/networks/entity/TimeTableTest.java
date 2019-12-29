package in.koreatech.koin.core.networks.entity;

import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TimeTableTest {

    @SerializedName("semester")
    @Expose
    public String semester = "";

    @SerializedName("timetable")
    @Expose
    public ArrayList<TimeTableItem> timeTableItems;

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

        public String lectureClass = "";

        @SerializedName("professor")
        @Expose
        public String professor = "";

        @SerializedName("grades")
        @Expose
        public String grades = "";

        @SerializedName("is_edited")
        @Expose
        public boolean isEdited = false;

    }
}
