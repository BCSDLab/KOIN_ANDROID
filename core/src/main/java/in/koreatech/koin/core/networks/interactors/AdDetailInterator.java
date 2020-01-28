package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;

/**
 * Created by hansol on 2020.1.3...
 */
public interface AdDetailInterator {
    void readAdDetailList(int id, final ApiCallback apiCallback);
}
