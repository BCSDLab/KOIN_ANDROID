package in.koreatech.koin.data;

import in.koreatech.koin.data.remote.StoreRemoteDataSource;

public class Injection {
    public static StoreRepository provideStoreRepository() {
        return StoreRepository.getInstance(StoreRemoteDataSource.getInstance());
    }
}
