package in.koreatech.koin.core.networks.services;

import java.util.ArrayList;

import in.koreatech.koin.core.constants.URLConstant;
import in.koreatech.koin.core.networks.entity.Circle;
import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.core.networks.responses.StoresResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static in.koreatech.koin.core.constants.URLConstant.CIRCLE.CIRCLE;
import static in.koreatech.koin.core.constants.URLConstant.SHOPS;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public interface CircleService {
    //Get Circle list API
    @GET(CIRCLE)
    Observable<Circle> getCircleList(@Query("page") int page);

    //Get Circle list API
    @GET(CIRCLE + "/{id}")
    Observable<Circle> getCircle(@Path("id") int uid);

}
