package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;

/**
 * Created by hansol on 2020.1.1...
 */
public interface AdvertisingInteractor {
    void readAdList(final ApiCallback apiCallback);

    void updateGrantCheck(int articleId, final ApiCallback apiCallback);
}
