package in.koreatech.koin.ui.circle.presenter;

import java.util.ArrayList;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleInteractor;

public class CirclePresenter {

    private ArrayList<Circle> circleList;
    private final CircleContract.View circleView;
    private int currentPage = 1;

    private final CircleInteractor circleInteractor;

    public CirclePresenter(CircleContract.View circleView, CircleInteractor circleInteractor) {
        this.circleView = circleView;
        this.circleInteractor = circleInteractor;
        circleList = new ArrayList<>();
        circleView.setPresenter(this);
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Circle circles = (Circle) object;
            if (circles.getTotalPage() != currentPage) {
                circleList.addAll(circles.getCircles());
                getCircleList(++currentPage);
                return;
            }
            circleList.addAll(circles.getCircles());
            circleView.onCircleListDataReceived(circleList);
            currentPage = 1;
            circleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            circleView.showMessage(R.string.circle_get_list_fail);
            circleView.hideLoading();
        }
    };

    public void getCircleList(int page) {
        if (page == 1) {
            circleView.showLoading();
            circleList.clear();
        }
        circleInteractor.readCircleList(page, apiCallback);
    }

}