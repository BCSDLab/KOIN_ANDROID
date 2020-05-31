package in.koreatech.koin.ui.circle.presenter;

import androidx.annotation.StringRes;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Circle;

public interface CircleContract {
    interface View extends BaseView<CirclePresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void showMessage(@StringRes int message);

        void onCircleListDataReceived(ArrayList<Circle> circleArrayList);

        void updateUserInterface();

        void goToCircleDetailActivity(int circleId, String circleName);
    }
}
