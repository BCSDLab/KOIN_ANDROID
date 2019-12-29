package in.koreatech.koin.service_circle.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Circle;
import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.service_circle.presenters.CirclePresenter;
import in.koreatech.koin.service_store.presenters.StorePresenter;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public interface CIrcleContract {
    interface View extends BaseView<CirclePresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onCircleListDataReceived(ArrayList<Circle> circleArrayList);

        void updateUserInterface();

        void goToCircleDetailActivity(int circleId, String circleName);
    }
}
