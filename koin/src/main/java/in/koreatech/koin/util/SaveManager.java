package in.koreatech.koin.util;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;

import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.entity.TimeTable.TimeTableItem;

public class SaveManager {
    public static final String TAG = "SaveManager";

    public static String saveTimeTable(TimeTable timeTable, String semester) {
        JsonObject obj1 = new JsonObject();
        JsonArray arr1 = new JsonArray();
        ArrayList<TimeTableItem> timeTableItems = timeTable.getTimeTableItems();
        for (TimeTableItem timeTableItem : timeTableItems) {
            JsonObject obj2 = new JsonObject();
            JsonArray timeArray = new JsonArray();
            obj2.addProperty("class_title", timeTableItem.getClassTitle());
            obj2.addProperty("id", timeTableItem.getId());
            obj2.addProperty("grades", timeTableItem.getGrades());
            obj2.addProperty("professor", timeTableItem.getProfessor());
            obj2.addProperty("class_place", timeTableItem.getClassPlace());
            obj2.addProperty("department", timeTableItem.getDepartment());
            obj2.addProperty("lecture_class", timeTableItem.getLectureClass());
            obj2.addProperty("regular_number", timeTableItem.getRegularNumber());
            obj2.addProperty("target", timeTableItem.getTarget());
            obj2.addProperty("design_score", timeTableItem.getDesignScore());
            obj2.addProperty("code", timeTableItem.getCode());
            ArrayList<Integer> timeArrayList = timeTableItem.getClassTime();
            getSortTime(timeArrayList);
            for (Integer time : timeArrayList) {
                timeArray.add(time);
            }
            obj2.add("class_time", timeArray);
            arr1.add(obj2);
        }
        obj1.add("timetable", arr1);
        obj1.addProperty("semester", semester);
        Log.d(TAG, "saveSticker: " + obj1.toString());
        return obj1.toString();
    }

    public static TimeTable loadTimeTable(String json) {
        TimeTable timeTable = new TimeTable();
        JsonParser parser = new JsonParser();
        ArrayList<Integer> timeArrayList = new ArrayList<>();
        if(FormValidatorUtil.validateStringIsEmpty(json)) return  timeTable;
        JsonObject obj1 = (JsonObject) parser.parse(json);
        JsonArray arr1 = obj1.getAsJsonArray("timetable");
        for (int i = 0; i < arr1.size(); i++) {
            JsonObject obj2 = (JsonObject) arr1.get(i);
            TimeTableItem timeTableItem = new TimeTableItem();
            timeTableItem.setClassTitle(obj2.get("class_title").getAsString());
            timeTableItem.setId(obj2.get("id").getAsInt());
            timeTableItem.setGrades(obj2.get("grades").getAsString());
            timeTableItem.setProfessor(obj2.get("class_place").getAsString());
            timeTableItem.setProfessor(obj2.get("professor").getAsString());
            timeTableItem.setCode(obj2.get("code").getAsString());
            timeTableItem.setDepartment(obj2.get("department").getAsString());
            timeTableItem.setLectureClass(obj2.get("lecture_class").getAsString());
            timeTableItem.setRegularNumber(obj2.get("regular_number").getAsString());
            timeTableItem.setTarget(obj2.get("target").getAsString());
            timeTableItem.setDesignScore(obj2.get("design_score").getAsString());
            JsonArray timeArray = obj2.getAsJsonArray("class_time");
            timeArrayList = new ArrayList<>();
            for (int j = 0; j < timeArray.size(); j++) {
                JsonElement time = timeArray.get(j);
                int timeInt = time.getAsInt();
                timeArrayList.add(timeInt);
            }
            getSortTime(timeArrayList);
            timeTableItem.setClassTime(timeArrayList);
            timeTable.getTimeTableItems().add(timeTableItem);
        }
        timeTable.setSemester(obj1.get("semester").getAsString());
        return timeTable;
    }

    public static JsonObject saveTimeTableAsJson(TimeTable timeTable, String semester) {
        JsonObject obj1 = new JsonObject();
        JsonArray arr1 = new JsonArray();
        ArrayList<TimeTableItem> timeTableItems = timeTable.getTimeTableItems();
        for (TimeTableItem timeTableItem : timeTableItems) {
            JsonObject obj2 = new JsonObject();
            JsonArray timeArray = new JsonArray();
            obj2.addProperty("class_title", timeTableItem.getClassTitle());
            obj2.addProperty("grades", timeTableItem.getGrades());
            obj2.addProperty("professor", timeTableItem.getProfessor());
            obj2.addProperty("class_place", timeTableItem.getClassPlace());
            obj2.addProperty("department", timeTableItem.getDepartment());
            obj2.addProperty("lecture_class", timeTableItem.getLectureClass());
            obj2.addProperty("regular_number", timeTableItem.getRegularNumber());
            obj2.addProperty("target", timeTableItem.getTarget());
            obj2.addProperty("design_score", timeTableItem.getDesignScore());
            obj2.addProperty("code", timeTableItem.getCode());
            ArrayList<Integer> timeArrayList = timeTableItem.getClassTime();
            getSortTime(timeArrayList);
            for (Integer time : timeArrayList) {
                timeArray.add(time);
            }
            obj2.add("class_time", timeArray);
            arr1.add(obj2);
        }
        obj1.add("timetable", arr1);
        obj1.addProperty("semester", semester);
        Log.d(TAG, "saveSticker: " + obj1.toString());
        return obj1;
    }

    public static JsonObject saveTimeTableItemAsJson(TimeTableItem timeTableItem, String semester) {
        JsonObject obj1 = new JsonObject();
        JsonArray arr1 = new JsonArray();
        JsonObject obj2 = new JsonObject();
        JsonObject timeObject = new JsonObject();
        JsonArray timeArray = new JsonArray();
        obj2.addProperty("class_title", timeTableItem.getClassTitle());
        obj2.addProperty("grades", timeTableItem.getGrades());
        obj2.addProperty("professor", timeTableItem.getProfessor());
        obj2.addProperty("class_place", timeTableItem.getClassPlace());
        obj2.addProperty("department", timeTableItem.getDepartment());
        obj2.addProperty("lecture_class", timeTableItem.getLectureClass());
        obj2.addProperty("regular_number", timeTableItem.getRegularNumber());
        obj2.addProperty("target", timeTableItem.getTarget());
        obj2.addProperty("design_score", timeTableItem.getDesignScore());
        obj2.addProperty("code", timeTableItem.getCode());
        ArrayList<Integer> timeArrayList = timeTableItem.getClassTime();
        getSortTime(timeArrayList);
        for (Integer time : timeArrayList) {
            timeArray.add(time);
        }
        obj2.add("class_time", timeArray);
        arr1.add(obj2);
        obj1.add("timetable", arr1);
        obj1.addProperty("semester", semester);
        Log.d(TAG, "saveTimeTableItemAsJson: " + obj1.toString());
        return obj1;
    }


    static private void getSortTime(ArrayList<Integer> times) {
        Collections.sort(times);
    }
}
