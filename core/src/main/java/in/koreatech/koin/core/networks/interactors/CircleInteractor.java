package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;

/**
 * Created by yunjae on 2019. 2. 20....
 */
public interface CircleInteractor {
    void readCircleList(int page,ApiCallback apiCallback);
    void readCircle(int id, ApiCallback apiCallback);
}
