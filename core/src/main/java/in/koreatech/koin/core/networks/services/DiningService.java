package in.koreatech.koin.core.networks.services;

import java.util.ArrayList;

import in.koreatech.koin.core.networks.entity.Dining;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static in.koreatech.koin.core.constants.URLConstant.DINING;

/**
 * Created by hyerim on 2018. 6. 21....
 */
public interface DiningService {
    //Get Specific Article API
    @GET(DINING)
    Observable<ArrayList<Dining>> getDiningMenu(@Query("date") String date);
}
