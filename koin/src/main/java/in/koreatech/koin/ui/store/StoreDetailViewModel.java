package in.koreatech.koin.ui.store;

import java.util.List;

import in.koreatech.koin.data.network.entity.Store;

public class StoreDetailViewModel {
    private final Store store;
    private final List<Store> randomStoreList;

    public StoreDetailViewModel(Store store, List<Store> randomStoreList) {
        this.store = store;
        this.randomStoreList = randomStoreList;
    }

    public Store getStore() {
        return store;
    }

    public List<Store> getRandomStoreList() {
        return randomStoreList;
    }
}
