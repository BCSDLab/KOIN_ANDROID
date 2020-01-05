package in.koreatech.koin.core.networks.services;

import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.core.networks.entity.Land;
import in.koreatech.koin.core.networks.entity.LostItem;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static in.koreatech.koin.core.constants.URLConstant.ADVERTISING;

/**
 * Created by hansol on 2020.1.1...
 */
public interface AdvertisingService {
    @GET(ADVERTISING)
    Observable<Advertising> getAdList();

    @GET(ADVERTISING+ "/{id}")
    Observable<AdDetail> getAdDetailList(@Path("id") int id);


//Observable<Land> getLandDetail(@Path("id") int landId);
//
//    @GET(ADVERTISING+"/events/{articleId}/comment/{commentId}")
//    Observable<...> getAdDetailList();
//
//    @GET(ADVERTISING+"/{id}")
//    Observable<...> getAdDetailList();



}
