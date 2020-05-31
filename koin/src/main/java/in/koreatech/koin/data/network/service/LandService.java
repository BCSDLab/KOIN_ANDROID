package in.koreatech.koin.data.network.service;

import in.koreatech.koin.data.network.entity.Land;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static in.koreatech.koin.constant.URLConstant.LAND;

public interface LandService {
    @GET(LAND + "/{id}")
    Observable<Land> getLandDetail(@Path("id") int landId);

    @GET(LAND)
    Observable<Land> getLandList();
}
