package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;
import io.reactivex.disposables.Disposable;


public interface BusInteractor {
    //시내 버스 현황 가져오기
    Disposable readCityBusList(final ApiCallback apiCallback, String depart, String arrival);
    Disposable readDaesungBusList(final ApiCallback apiCallback, String depart, String arrival);
    Disposable readShuttleBusList(final ApiCallback apiCallback, String depart, String arrival);
}


