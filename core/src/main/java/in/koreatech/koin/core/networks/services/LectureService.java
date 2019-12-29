package in.koreatech.koin.core.networks.services;

import java.util.ArrayList;

import in.koreatech.koin.core.networks.entity.Lecture;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static in.koreatech.koin.core.constants.URLConstant.LECTURE;

public interface LectureService {
    @GET(LECTURE)
    Observable<ArrayList<Lecture>> getLectureList(@Query("semester_date") String semester);

}
