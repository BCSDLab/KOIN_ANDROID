package in.koreatech.koin.service_store.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.service_store.presenters.StorePresenter;

/**
 * Created by hyerim on 2018. 8. 12....
 */
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
