package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.response.StoresResponse;
import io.reactivex.Single;

public interface StoreInteractor {
    Single<StoresResponse> readStoreList();

    void readStore(int id, ApiCallback apiCallback);

}
