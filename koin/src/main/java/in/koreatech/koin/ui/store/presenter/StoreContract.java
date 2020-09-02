package in.koreatech.koin.ui.store.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.ui.store.presenter.StorePresenter;

public interface StoreContract {
    interface View extends BaseView<StorePresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onStoreListDataReceived(ArrayList<Store> storeArrayList);

        void updateUserInterface();

        void goToStoreDetailActivity(int storeUid, String storeName);
    }
}
