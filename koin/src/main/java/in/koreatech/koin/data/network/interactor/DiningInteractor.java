package in.koreatech.koin.data.network.interactor;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Dining;
import io.reactivex.Observable;

public interface DiningInteractor {
    //학식 메뉴 가져오기
    void readDiningList(String date, final ApiCallback apiCallback);

    Observable<ArrayList<Dining>> readDingingList(String date);
}
