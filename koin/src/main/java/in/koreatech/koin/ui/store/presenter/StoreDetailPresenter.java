package in.koreatech.koin.ui.store.presenter;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.StoreInteractor;

public class StoreDetailPresenter {

    private final StoreDetailContract.View storeView;

    private final StoreInteractor storeInteractor;

    public StoreDetailPresenter(StoreDetailContract.View storeView, StoreInteractor storeInteractor) {
        this.storeView = storeView;
        this.storeInteractor = storeInteractor;
        this.storeView.setPresenter(this);
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Store store = (Store) object;
            storeView.onStoreDataReceived(store);
            storeView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            storeView.showMessage(R.string.store_get_data_fail);
            storeView.hideLoading();
        }
    };


    public void getStore(int id) {
        storeView.showLoading();
        storeInteractor.readStore(id, apiCallback);
    }

}
