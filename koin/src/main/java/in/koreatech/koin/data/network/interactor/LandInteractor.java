package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;

public interface LandInteractor {
    void readLandList(final ApiCallback apiCallback);
    void readLandDetail(int landId, final ApiCallback apiCallback);
}
