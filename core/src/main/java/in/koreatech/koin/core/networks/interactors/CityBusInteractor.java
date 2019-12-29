package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;

/**
 * Created by hyerim on 2018. 8. 13....
 */
public interface CityBusInteractor {
    //시내 버스 현황 가져오기
    void readCityBusList(final ApiCallback apiCallback, String depart, String arrival);
}


