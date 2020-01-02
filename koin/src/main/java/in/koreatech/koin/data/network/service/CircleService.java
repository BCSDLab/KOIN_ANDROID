package in.koreatech.koin.data.network.service;

import in.koreatech.koin.data.network.entity.Circle;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static in.koreatech.koin.core.constant.URLConstant.CIRCLE.CIRCLE;

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
