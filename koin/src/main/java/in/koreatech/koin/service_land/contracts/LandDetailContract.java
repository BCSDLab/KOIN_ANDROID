package in.koreatech.koin.service_land.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Land;
import in.koreatech.koin.service_land.presenter.LandDetailPresenter;

public interface LandDetailContract {
    interface View extends BaseView<LandDetailPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onLandDetailDataReceived(Land land);
    }
}
