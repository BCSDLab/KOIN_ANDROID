package in.koreatech.koin.ui.circle.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Circle;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public interface CircleDetailContract {
    interface View extends BaseView<CircleDetailPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onCircleDataReceived(Circle circle);

        void updateUserInterface();
    }
}
