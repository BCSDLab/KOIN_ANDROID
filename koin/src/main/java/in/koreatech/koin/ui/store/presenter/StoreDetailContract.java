package in.koreatech.koin.ui.store.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.ui.store.presenter.StoreDetailPresenter;

public interface StoreDetailContract {
    interface View extends BaseView<StoreDetailPresenter> {
        void showLoading();

        void hideLoading();

        void onStoreDataReceived(Store store);

        void updateUserInterface();

        void showMessage(String message);

        void requestPermission();
    }
}
