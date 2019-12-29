package in.koreatech.koin.core.networks.services;

import in.koreatech.koin.core.networks.responses.BusResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static in.koreatech.koin.core.constants.URLConstant.BUS;

/**
 * Created by hyerim on 2018. 8. 13....
 */
public interface CityBusService {
    @GET(BUS)
    Observable<BusResponse> getBusList(@Query("depart") String depart, @Query("arrival") String arrival);
}
