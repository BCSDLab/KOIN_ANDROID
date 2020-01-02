package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;

/**
 * Created by yunjae on 2019. 2. 20....
 */
public interface CircleInteractor {
    void readCircleList(int page,ApiCallback apiCallback);
    void readCircle(int id, ApiCallback apiCallback);
}
