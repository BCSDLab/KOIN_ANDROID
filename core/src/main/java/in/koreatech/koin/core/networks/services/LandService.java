package in.koreatech.koin.core.networks.services;

import java.util.ArrayList;

import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.core.networks.entity.Land;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

import static in.koreatech.koin.core.constants.URLConstant.LAND;

public interface LandService {
    @GET(LAND + "/{id}")
    Observable<Land> getLandDetail(@Path("id") int landId);

    @GET(LAND)
    Observable<BokdukRoom> getLandList();
}
