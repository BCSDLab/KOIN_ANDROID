package in.koreatech.koin.ui.dining.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Dining;

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
