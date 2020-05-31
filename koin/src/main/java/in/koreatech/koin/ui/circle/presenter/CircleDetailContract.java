package in.koreatech.koin.ui.circle.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Circle;

public interface CircleDetailContract {
    interface View extends BaseView<CircleDetailPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void showMessage(@StringRes int message);

        void onCircleDataReceived(Circle circle);

        void updateUserInterface();
    }
}
