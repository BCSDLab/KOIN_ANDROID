package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;

public interface DiningInteractor {
    //학식 메뉴 가져오기
    void readDiningList(String date, final ApiCallback apiCallback);
}
