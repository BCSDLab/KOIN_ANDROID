package in.koreatech.koin.service_circle.presenters;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Circle;
import in.koreatech.koin.core.networks.interactors.CircleInteractor;
import in.koreatech.koin.service_circle.contracts.CIrcleContract;
import in.koreatech.koin.service_circle.contracts.CIrcleDetailContract;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class CircleDetailPresenter implements BasePresenter {

    private final CIrcleDetailContract.View cirlcleView;

    private final CircleInteractor circleInteractor;

    public CircleDetailPresenter(CIrcleDetailContract.View cirlcleView, CircleInteractor circleInteractor) {
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