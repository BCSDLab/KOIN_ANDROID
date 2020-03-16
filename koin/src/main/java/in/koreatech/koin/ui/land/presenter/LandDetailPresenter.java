package in.koreatech.koin.ui.land.presenter;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Land;
import in.koreatech.koin.data.network.interactor.LandInteractor;

/**
 * 복덕방 presenter
 */
public class LandDetailPresenter {
    private LandInteractor landInteractor;
    private LandDetailContract.View landDetailView;

    public LandDetailPresenter(LandDetailContract.View landDetailView, LandInteractor landInteractor) {
        this.landInteractor = landInteractor;
        this.landDetailView = landDetailView;
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        /**
         * 특정 원룸의 상세정보를 landDetailActivity 로 전달
         * @param object 특정 원룸의 상세 정보
         */
        @Override
        public void onSuccess(Object object) {
            landDetailView.onLandDetailDataReceived((Land) object);
            landDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            landDetailView.showMessage("원룸 정보를 받아오지 못했습니다.");
            landDetailView.hideLoading();
        }
    };

    public void getLandDetailInfo(int landId) {
        landDetailView.showLoading();
        landInteractor.readLandDetail(landId, apiCallback);
    }
}