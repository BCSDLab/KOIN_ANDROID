package in.koreatech.koin.core.networks.services;

import java.util.ArrayList;

import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.core.networks.responses.StoresResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import static in.koreatech.koin.core.constants.URLConstant.SHOPS;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public interface ShopService {
    //Get Shop list API
    @GET(SHOPS)
    Observable<StoresResponse> getShopList();

    //Get Shop list API
    @GET(SHOPS + "/{id}")
    Observable<Store> getStore(@Path("id") int uid);

}
