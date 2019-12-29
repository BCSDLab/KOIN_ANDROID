package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;

public interface LandInteractor {
    void readLandDetail(int landId, final ApiCallback apiCallback);
}
