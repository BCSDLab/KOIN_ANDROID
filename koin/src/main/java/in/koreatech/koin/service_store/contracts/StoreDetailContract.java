package in.koreatech.koin.service_store.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.service_store.presenters.StoreDetailPresenter;

/**
 * Created by hyerim on 2018. 8. 16....
 */
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
