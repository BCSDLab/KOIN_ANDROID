package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;


public interface CityBusInteractor {
    //시내 버스 현황 가져오기
    void readCityBusList(final ApiCallback apiCallback, String depart, String arrival);
}


