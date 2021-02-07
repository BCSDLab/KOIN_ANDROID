package in.koreatech.koin.ui.store.presenter;

import androidx.annotation.StringRes;

import java.util.List;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Store;

public interface StoreDetailContract {
    interface View extends BaseView<StoreDetailPresenter> {
        void showLoading();

        void hideLoading();

        void onStoreDataReceived(Store store);

        void onRandomStoreListReceived(List<Store> store);

        void updateUserInterface();

        void showMessage(String message);

        void showMessage(@StringRes int message);

        void requestPermission();
    }
}
