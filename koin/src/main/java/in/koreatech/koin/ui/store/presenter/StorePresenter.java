package in.koreatech.koin.ui.store.presenter;

import java.util.ArrayList;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.StoreInteractor;
import in.koreatech.koin.data.network.response.StoresResponse;

public class StorePresenter {

    private final StoreContract.View storeView;

    private final StoreInteractor storeInteractor;

    public StorePresenter(StoreContract.View storeView, StoreInteractor storeInteractor) {
        this.storeView = storeView;
        this.storeInteractor = storeInteractor;
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<Store> arrayList = ((StoresResponse) object).storeArrayList;
            storeView.onStoreListDataReceived(arrayList);
            storeView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            storeView.showMessage(R.string.store_get_data_fail);
            storeView.hideLoading();
        }
    };

    public void getStoreList() {
        storeView.showLoading();
        storeInteractor.readStoreList(apiCallback);
    }

}