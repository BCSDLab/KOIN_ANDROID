package in.koreatech.koin.ui.land.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Land;

public interface LandDetailContract {
    interface View extends BaseView<LandDetailPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onLandDetailDataReceived(Land land);
    }
}
