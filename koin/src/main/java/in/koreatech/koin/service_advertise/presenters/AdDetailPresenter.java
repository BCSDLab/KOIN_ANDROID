package in.koreatech.koin.service_advertise.presenters;

import android.bluetooth.le.AdvertiseData;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.interactors.AdDetailInterator;
import in.koreatech.koin.service_advertise.contracts.AdDetailContract;

/**
 * Created by hansol on 2020.1.3...
 */
public class AdDetailPresenter implements AdDetailContract.Presenter {
    private AdDetailContract.View adDetailView;
    private AdDetailInterator adDetailInterator;

    public AdDetailPresenter(AdDetailContract.View adDetailView, AdDetailInterator adDetailInterator) {
        this.adDetailView = adDetailView;
        this.adDetailInterator = adDetailInterator;
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            adDetailView.onAdDetailDataReceived((AdDetail) object);
            adDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            adDetailView.showMessage("이벤트 세부 내용을 불러오지 못했습니다.");
            adDetailView.hideLoading();
        }
    };

    private final ApiCallback deleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            adDetailView.onAdDetailDeleteCompleted(true);
            adDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            adDetailView.showMessage(throwable.getMessage());
            adDetailView.hideLoading();
        }
    };

    @Override
    public void getAdDetailInfo(int id) {
        adDetailView.showLoading();
        adDetailInterator.readAdDetailList(id, apiCallback);
    }

    public void deleteAdDetail(int articleId) {
        adDetailView.showLoading();
        adDetailInterator.deleteAdDetail(articleId, deleteApiCallback);
    }
}
