package in.koreatech.koin.core.networks.interactors;

import com.google.gson.JsonObject;

import in.koreatech.koin.core.networks.ApiCallback;

public interface TimeTableInteractor {

    void deleteSemesterTimeTable(final ApiCallback apiCallback, String semester);

    void deleteTimeTable(final ApiCallback apiCallback, int id);

    void readTimeTable(final ApiCallback apiCallback, String semester);

    void createTimeTable(final ApiCallback apiCallback, JsonObject jsonObject);

    void editTimeTable(final ApiCallback apiCallback, JsonObject jsonObject);


}
