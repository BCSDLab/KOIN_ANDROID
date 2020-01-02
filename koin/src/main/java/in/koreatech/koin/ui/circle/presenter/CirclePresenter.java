package in.koreatech.koin.ui.circle.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleInteractor;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class CirclePresenter implements BasePresenter {

    private ArrayList<Circle> mCirlceList;
    private final CIrcleContract.View cirlcleView;
    private int mCurrentPage = 1;

    private final CircleInteractor circleInteractor;

    public CirclePresenter(CIrcleContract.View cirlcleView, CircleInteractor circleInteractor) {
        this.cirlcleView = cirlcleView;
        this.circleInteractor = circleInteractor;
        mCirlceList = new ArrayList<>();
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Circle circles = (Circle) object;
            if (circles.totalPage != mCurrentPage) {
                mCirlceList.addAll(circles.circles);
                getCirlceList(++mCurrentPage);
                return;
            }
            mCirlceList.addAll(circles.circles);
            cirlcleView.onCircleListDataReceived(mCirlceList);
            mCurrentPage = 1;
            cirlcleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            cirlcleView.showMessage("동아리 리스트를 받아오지 못했습니다.");
            cirlcleView.hideLoading();
        }
    };

    public void getCirlceList(int page) {
        if (page == 1) {
            cirlcleView.showLoading();
            mCirlceList.clear();
        }
        circleInteractor.readCircleList(page, apiCallback);
    }

}