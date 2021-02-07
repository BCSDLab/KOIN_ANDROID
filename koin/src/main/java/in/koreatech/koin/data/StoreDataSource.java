package in.koreatech.koin.data;

import java.util.List;

import in.koreatech.koin.data.network.entity.Store;
import io.reactivex.Single;

public interface StoreDataSource {
    Single<List<Store>> getStoreList();

    Single<List<Store>> getStoreListByCategory(String... category);

    Single<List<Store>> getStoreListByName(String name);

    Single<List<Store>> getRandomStoreListByCategory(int count, int currentStoreID, String... category);

    Single<Store> getStoreDetail(int storeID);

    void refresh();
}
