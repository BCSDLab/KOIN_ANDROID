package in.koreatech.koin.service_land.presenter;

import android.util.Log;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.core.networks.interactors.BokdukInteractor;
import in.koreatech.koin.service_land.contracts.LandContract;

/**
 * 복덕방 presenter
 * Created by dongmin on 2019. 09. 02...
 */
public class LandPresenter implements BasePresenter {

    private final LandContract.View landView;
    private final BokdukInteractor bokdukInteractor;
    private ArrayList<BokdukRoom> landList;


    public LandPresenter(LandContract.View landView, BokdukInteractor bokdukInteractor) {
        this.landView = landView;
        this.bokdukInteractor = bokdukInteractor;
        landList = new ArrayList<>();
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        /**
         * 복덕방리스트를 Activity에 전달
         * @param object 복덕방
         */
        @Override
        public void onSuccess(Object object) {
            BokdukRoom lands = (BokdukRoom) object;
            landList.clear();
            landList.addAll(lands.lands);   //api로 받아온 복덕방리스트를 모두 추가
            landView.onLandListDataReceived(landList);
        }

        @Override
        public void onFailure(Throwable throwable) {
            landView.showMessage("원룸 리스트를 받아오지 못했습니다");
        }
    };

    public void getLandList() {
        landList.clear();
        bokdukInteractor.readBokdukList(apiCallback);

    }

}
