package in.koreatech.koin.util;

import java.util.ArrayList;

import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.TimeTable.TimeTableItem;

import static in.koreatech.koin.util.DepartmentCode.*;


public class LectureFilterUtil {
    public static ArrayList<Lecture> getFilterUtil(DepartmentCode departmentCode, String keyword, ArrayList<Lecture> lectures) {
        ArrayList<Lecture> filterLectureArrayList = new ArrayList<>(lectures);
        switch (departmentCode.getType()) {
            case 0:
                return getKeywordFilter(keyword, lectures);
            case 1:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_1.getDepartMentString(), keyword, lectures);
                break;
            case 2:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_2.getDepartMentString(), keyword, lectures);
                break;
            case 3:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_3.getDepartMentString(), keyword, lectures);
                break;
            case 4:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_4.getDepartMentString(), keyword, lectures);
                break;
            case 5:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_5.getDepartMentString(), keyword, lectures);
                break;
            case 6:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_6.getDepartMentString(), keyword, lectures);
                break;
            case 7:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_7.getDepartMentString(), keyword, lectures);
                break;
            case 8:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_8.getDepartMentString(), keyword, lectures);
                break;
            case 9:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_9.getDepartMentString(), keyword, lectures);
                break;
            case 10:
                filterLectureArrayList = getFilter(DEPARTMENT_CODE_10.getDepartMentString(), keyword, lectures);
                break;

        }
        return filterLectureArrayList;
    }

    public static ArrayList<Lecture> getFilterUtil(DepartmentCode departmentCode, String keyword, ArrayList<Lecture> lectures, ArrayList<TimeTableItem> timeTableItems) {
        ArrayList<Lecture> filterLectureArrayList;
        filterLectureArrayList = getFilterUtil(departmentCode, keyword, lectures);
        for (Lecture lecture : filterLectureArrayList) {
            for (TimeTableItem timeTableItem : timeTableItems) {
                lecture.isAddButtonClicked = false;
                if (lecture.name.equals(timeTableItem.getClassTitle()) && lecture.professor.equals(timeTableItem.getProfessor()) && lecture.lectureClass.equals(timeTableItem.getLectureClass())) {
                    {
                        lecture.isAddButtonClicked = true;
                        break;

                    }
                }
            }
        }

        return filterLectureArrayList;

    }

    public static void getClikckedUtil(ArrayList<Lecture> lectures, ArrayList<TimeTableItem> timeTableItems) {
        for (Lecture lecture : lectures) {
            lecture.isAddButtonClicked = false;
            for (TimeTableItem timeTableItem : timeTableItems) {
                if (lecture.name.equals(timeTableItem.getClassTitle()) && lecture.professor.equals(timeTableItem.getProfessor()) && lecture.lectureClass.equals(timeTableItem.getLectureClass())) {
                    {
                        lecture.isAddButtonClicked = true;
                        break;

                    }
                }
            }
        }
    }

    public static boolean CheckClikckedUtil(Lecture lecture, TimeTableItem timeTableItem) {

        if (lecture.name.equals(timeTableItem.getClassTitle()) && lecture.professor.equals(timeTableItem.getProfessor()) && lecture.lectureClass.equals(timeTableItem.getLectureClass())) {
            return true;
        }
        return false;
    }


    private static ArrayList<Lecture> getFilter(String department, String
            keyword, ArrayList<Lecture> lectures) {
        ArrayList<Lecture> returnLectureArrayList = new ArrayList<>();

        for (Lecture lecture : lectures) {
            if (lecture.department.contains(department))
                returnLectureArrayList.add(lecture);
        }

        return getKeywordFilter(keyword, returnLectureArrayList);
    }

    private static ArrayList<Lecture> getKeywordFilter(String
                                                               keyword, ArrayList<Lecture> lectures) {
        ArrayList<Lecture> returnLectureArrayList = new ArrayList<>();
        if (keyword == null || keyword.equals(""))
            return lectures;

        for (Lecture lecture : lectures) {
            if (lecture.name.toUpperCase().contains(keyword.toUpperCase()))
                returnLectureArrayList.add(lecture);
        }
        return returnLectureArrayList;
    }
}
