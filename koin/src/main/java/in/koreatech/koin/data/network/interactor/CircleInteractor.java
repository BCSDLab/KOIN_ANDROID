package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;


public interface CircleInteractor {
    void readCircleList(int page, ApiCallback apiCallback);

    void readCircle(int id, ApiCallback apiCallback);
}
