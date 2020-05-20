package in.koreatech.koin.ui.circle.presenter;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleInteractor;

public class CircleDetailPresenter {

    private final CircleDetailContract.View circleView;

    private final CircleInteractor circleInteractor;

    public CircleDetailPresenter(CircleDetailContract.View circleView, CircleInteractor circleInteractor) {
        this.circleView = circleView;
        this.circleInteractor = circleInteractor;
        circleView.setPresenter(this);
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            circleView.onCircleDataReceived((Circle) object);
            circleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            circleView.showMessage(R.string.circle_get_fail);
            circleView.hideLoading();
        }
    };

    public void getCircleInfo(int id) {
        circleView.showLoading();
        circleInteractor.readCircle(id, apiCallback);
    }

}