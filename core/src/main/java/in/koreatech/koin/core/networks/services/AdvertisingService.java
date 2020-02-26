package in.koreatech.koin.core.networks.services;

import com.google.gson.JsonObject;

import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.core.networks.entity.Land;
import in.koreatech.koin.core.networks.entity.LostItem;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static in.koreatech.koin.core.constants.URLConstant.ADVERTISING.ADVERTISING;
import static in.koreatech.koin.core.constants.URLConstant.ADVERTISING.GRANTCHECK;


/**
 * Created by hansol on 2020.1.1...
 * Edited by seongyun on 2020. 02. 27...
 */
public interface AdvertisingService {
    @GET(ADVERTISING)
    Observable<Advertising> getAdList();

    @GET(ADVERTISING + "/{id}")
    Observable<AdDetail> getAdDetailList(@Path("id") int id);

    @POST(ADVERTISING)
    Observable<AdDetail> postAdDetail(@Header("Authorization") String authHeader, @Body JsonObject adDetail);

    @POST(GRANTCHECK)
    Observable<AdDetail> postGrantCheck(@Header("Authorization") String authHeader, @Body JsonObject adDetail);

    @PUT(ADVERTISING + "/{id}")
    Observable<AdDetail> updateAdDetail(@Path("id") int articleId, @Header("Authorization") String authHeader, @Body JsonObject adDetail);

//Observable<Land> getLandDetail(@Path("id") int landId);
//
//    @GET(ADVERTISING+"/events/{articleId}/comment/{commentId}")
//    Observable<...> getAdDetailList();
//
//    @GET(ADVERTISING+"/{id}")
//    Observable<...> getAdDetailList();
}