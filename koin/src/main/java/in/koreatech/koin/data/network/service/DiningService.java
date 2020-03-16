package in.koreatech.koin.data.network.service;

import java.util.ArrayList;

import in.koreatech.koin.data.network.entity.Dining;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static in.koreatech.koin.constant.URLConstant.DINING;

public interface DiningService {
    //Get Specific Article API
    @GET(DINING)
    Observable<ArrayList<Dining>> getDiningMenu(@Query("date") String date);
}
