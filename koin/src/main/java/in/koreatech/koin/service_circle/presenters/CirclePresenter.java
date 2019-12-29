package in.koreatech.koin.service_circle.presenters;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Circle;
import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.core.networks.interactors.CircleInteractor;
import in.koreatech.koin.core.networks.interactors.StoreInteractor;
import in.koreatech.koin.core.networks.responses.StoresResponse;
import in.koreatech.koin.service_circle.contracts.CIrcleContract;
import in.koreatech.koin.service_store.contracts.StoreContract;

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