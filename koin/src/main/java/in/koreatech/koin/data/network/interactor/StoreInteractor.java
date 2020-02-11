package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;

public interface StoreInteractor {
    void readStoreList(ApiCallback apiCallback);

    void readStore(int id, ApiCallback apiCallback);

}
