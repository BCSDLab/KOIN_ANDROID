package in.koreatech.koin.util;

import java.util.ArrayList;
import java.util.HashSet;

import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.TimeTable.TimeTableItem;

public class TimeDuplicateCheckUtil {
    public static ArrayList<TimeTableItem> checkDuplicateSchedule(ArrayList<TimeTableItem> timeTable, Lecture selectedLecture) {
        ArrayList<TimeTableItem> duplicateTimeTableItems = new ArrayList<>();
        HashSet<Integer> timeHashSet = new HashSet<>();
        String selectedClassTitle = selectedLecture.getName();
        ArrayList<Integer> selectedClassTimeList = selectedLecture.getClassTime();

        for (TimeTableItem timeTableItem : timeTable) {
            boolean isTimeConatins = false;
            timeHashSet.addAll(timeTableItem.getClassTime());
            for (Integer time : selectedClassTimeList) {
                if (timeHashSet.contains(time)) {
                    isTimeConatins = true;
                    break;
                }
            }
            if (timeTableItem.getClassTitle().equals(selectedClassTitle) || isTimeConatins)
                duplicateTimeTableItems.add(timeTableItem);
            timeHashSet.clear();
        }

        return duplicateTimeTableItems;
    }

    public static String duplicateScheduleTostring(ArrayList<TimeTableItem> duplicateSchedule) {
        StringBuilder duplicateClassTitle = new StringBuilder();
        if (duplicateSchedule == null || duplicateSchedule.isEmpty())
            return "";
        else {
            for (TimeTableItem timeTableItem : duplicateSchedule) {
                if (timeTableItem.getClassTitle() != null && !timeTableItem.getClassTitle().isEmpty())
                    duplicateClassTitle.append(timeTableItem.getClassTitle()).append("\n");
            }
        }
        return duplicateClassTitle.toString();
    }
}
