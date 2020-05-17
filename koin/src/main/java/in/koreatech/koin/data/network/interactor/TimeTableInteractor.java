package in.koreatech.koin.data.network.interactor;

import com.google.gson.JsonObject;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.TimeTable;

public interface TimeTableInteractor {

    void deleteSemesterTimeTable(final ApiCallback apiCallback, String semester);

    void deleteTimeTable(final ApiCallback apiCallback, int id);

    void readTimeTable(final ApiCallback apiCallback, String semester);

    void createTimeTable(final ApiCallback apiCallback, JsonObject jsonObject);

    void editTimeTable(final ApiCallback apiCallback, JsonObject jsonObject);

    void readSemesters(final ApiCallback apiCallback);

    void readTimeTableFromLocal(String semester, final ApiCallback apiCallback);

    void editTimeTableItemAtLocal(TimeTable.TimeTableItem timeTableItem, String semester, final ApiCallback apiCallback);

    void deleteTimeTableItemAtLocal(String semester, int id, final ApiCallback apiCallback);
}
