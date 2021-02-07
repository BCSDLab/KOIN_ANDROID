package in.koreatech.koin.data.remote;

import androidx.annotation.Nullable;

import java.util.List;

import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.StoreDataSource;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.service.ShopService;
import io.reactivex.Single;

public class StoreRemoteDataSource implements StoreDataSource {
    @Nullable
    private static StoreRemoteDataSource INSTANCE;

    public static StoreDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StoreRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Single<List<Store>> getStoreList() {
        return RetrofitManager.getInstance()
                .getRetrofit()
                .create(ShopService.class)
                .getShopList()
                .map(storesResponse -> storesResponse.storeArrayList);

    }

    @Override
    public Single<List<Store>> getStoreListByCategory(String... category) {
        return Single.never();
    }

    @Override
    public Single<List<Store>> getStoreListByName(String name) {
        return Single.never();
    }

    @Override
    public Single<List<Store>> getRandomStoreListByCategory(int count, int currentStoreID, String... category) {
        return Single.never();
    }

    @Override
    public Single<Store> getStoreDetail(int storeID) {
        return RetrofitManager.getInstance()
                .getRetrofit()
                .create(ShopService.class)
                .getStore(storeID);
    }

    @Override
    public void refresh() {
    }
}
