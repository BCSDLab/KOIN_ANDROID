package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;

/**
 * Created by hyerim on 2018. 6. 21....
 */
public interface DiningInteractor {
    //학식 메뉴 가져오기
    void readDiningList(String date, final ApiCallback apiCallback);
}
