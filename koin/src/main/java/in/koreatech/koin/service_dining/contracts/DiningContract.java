package in.koreatech.koin.service_dining.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Dining;
import in.koreatech.koin.service_dining.presenters.DiningPresenter;


/**
 * Created by hyerim on 2018. 6. 21....
 */
public interface DiningContract {
    interface View extends BaseView<DiningPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void showNetworkError();

        void onDiningListDataReceived(ArrayList<Dining> diningArrayList);

        void updateUserInterface();

        void showUserInterface();
    }
}
