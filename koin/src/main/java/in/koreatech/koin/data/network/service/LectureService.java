package in.koreatech.koin.data.network.service;

import java.util.ArrayList;

import in.koreatech.koin.data.network.entity.Lecture;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static in.koreatech.koin.constant.URLConstant.LECTURE;

public interface LectureService {
    @GET(LECTURE)
    Observable<ArrayList<Lecture>> getLectureList(@Query("semester_date") String semester);

}
