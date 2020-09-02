package in.koreatech.koin.data.network.interactor;

import com.google.gson.JsonObject;

import in.koreatech.koin.core.network.ApiCallback;

public interface TimeTableInteractor {

    void deleteSemesterTimeTable(final ApiCallback apiCallback, String semester);

    void deleteTimeTable(final ApiCallback apiCallback, int id);

    void readTimeTable(final ApiCallback apiCallback, String semester);

    void createTimeTable(final ApiCallback apiCallback, JsonObject jsonObject);

    void editTimeTable(final ApiCallback apiCallback, JsonObject jsonObject);

    void readSemesters(final ApiCallback apiCallback);

}
