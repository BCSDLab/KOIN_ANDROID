package in.koreatech.koin.core.networks.services;
import com.google.gson.JsonObject;

import java.util.ArrayList;


import in.koreatech.koin.core.networks.entity.Semester;
import in.koreatech.koin.core.networks.entity.TimeTable;
import in.koreatech.koin.core.networks.entity.TimeTable.TimeTableItem;
import in.koreatech.koin.core.networks.entity.TimeTableTest;
import in.koreatech.koin.core.networks.responses.DefaultResponse;
import in.koreatech.koin.core.networks.responses.TimeTablePostItemResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import retrofit2.http.Query;


import static in.koreatech.koin.core.constants.URLConstant.SEMESTERS;
import static in.koreatech.koin.core.constants.URLConstant.TIMETABLE;
import static in.koreatech.koin.core.constants.URLConstant.TIMETABLES;

public interface TimeTableService {


    @DELETE(TIMETABLE)
    Observable<DefaultResponse> deleteTimeTable(@Header("Authorization") String authHeader, @Query("id") int id);

    @DELETE(TIMETABLES)
    Observable<DefaultResponse> deleteTimeTables(@Header("Authorization") String authHeader, @Query("semester") String semester);

    @GET(TIMETABLES)
    Observable<TimeTable> getTimeTablesList(@Header("Authorization") String authHeader, @Query("semester") String semester);

    @POST(TIMETABLES)
    Observable<TimeTable> postTimeTables(@Header("Authorization") String authHeader, @Body JsonObject TimeTableUid);

    @PUT(TIMETABLES)
    Observable<ArrayList<TimeTable>> putEditTimeTables( @Header("Authorization") String authHeader, @Body JsonObject TimeTableUid);

    @GET(SEMESTERS)
    Observable<ArrayList<Semester>> getSemesters();

}
