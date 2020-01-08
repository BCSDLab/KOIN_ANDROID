package in.koreatech.koin.data.network.service;

import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.response.StoresResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static in.koreatech.koin.core.constant.URLConstant.SHOPS;

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
