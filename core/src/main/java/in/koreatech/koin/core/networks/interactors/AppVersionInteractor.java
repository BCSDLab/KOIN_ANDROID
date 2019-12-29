package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;

public interface AppVersionInteractor {
    void readAppVersion(String code, final ApiCallback apiCallback);

}
