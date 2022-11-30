package in.koreatech.koin.data.network.service;

import in.koreatech.koin.data.response.bus.BusResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static in.koreatech.koin.constant.URLConstant.BUS;


public interface BusService {
    @GET(BUS)
    Observable<BusResponse> getBusList(@Query("bus_type") String busType, @Query("depart") String depart, @Query("arrival") String arrival);
}
