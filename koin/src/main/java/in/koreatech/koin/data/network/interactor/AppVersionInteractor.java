package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;

public interface AppVersionInteractor {
    void readAppVersion(String code, final ApiCallback apiCallback);

}
