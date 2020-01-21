package in.koreatech.koin.ui.circle.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleInteractor;

public class CircleDetailPresenter implements BasePresenter {

    private final CircleDetailContract.View cirlcleView;

    private final CircleInteractor circleInteractor;

    public CircleDetailPresenter(CircleDetailContract.View cirlcleView, CircleInteractor circleInteractor) {
        this.cirlcleView = cirlcleView;
        this.circleInteractor = circleInteractor;
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            cirlcleView.onCircleDataReceived((Circle) object);
            cirlcleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            cirlcleView.showMessage("동아리 정보를 받아오지 못했습니다.");
            cirlcleView.hideLoading();
        }
    };

    public void getCirlceInfo(int id) {
        cirlcleView.showLoading();
        circleInteractor.readCircle(id, apiCallback);
    }

}