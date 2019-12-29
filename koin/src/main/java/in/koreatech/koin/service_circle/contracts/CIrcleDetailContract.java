package in.koreatech.koin.service_circle.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Circle;
import in.koreatech.koin.service_circle.presenters.CircleDetailPresenter;
import in.koreatech.koin.service_circle.presenters.CirclePresenter;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public interface CIrcleDetailContract {
    interface View extends BaseView<CircleDetailPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onCircleDataReceived(Circle circle);

        void updateUserInterface();
    }
}
