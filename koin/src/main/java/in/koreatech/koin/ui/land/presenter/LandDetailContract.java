package in.koreatech.koin.ui.land.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Land;

public interface LandDetailContract {
    interface View extends BaseView<LandDetailPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(@StringRes int message);

        void onLandDetailDataReceived(Land land);
    }
}
