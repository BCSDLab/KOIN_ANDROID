package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;

public interface LandInteractor {
    void readLandDetail(int landId, final ApiCallback apiCallback);
}
