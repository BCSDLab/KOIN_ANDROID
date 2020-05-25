package in.koreatech.koin.ui.land.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Land;
import in.koreatech.koin.data.network.interactor.LandInteractor;

/**
 * 복덕방 presenter
 */
public class LandPresenter {

    private final LandContract.View landView;
    private final LandInteractor landInteractor;
    private ArrayList<Land> landList;


    public LandPresenter(LandContract.View landView, LandInteractor landInteractor) {
        this.landView = landView;
        this.landInteractor = landInteractor;
        landList = new ArrayList<>();
        this.landView.setPresenter(this);
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        /**
         * 복덕방리스트를 Activity에 전달
         * @param object 복덕방
         */
        @Override
        public void onSuccess(Object object) {
            Land lands = (Land) object;
            landList.clear();
            landList.addAll(lands.getLands());   //api로 받아온 복덕방리스트를 모두 추가
            landView.onLandListDataReceived(landList);
            landView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            landView.showMessage("원룸 리스트를 받아오지 못했습니다");
            landView.hideLoading();
        }
    };

    public void getLandList() {
        landView.showLoading();
        landInteractor.readLandList(apiCallback);
    }

}
