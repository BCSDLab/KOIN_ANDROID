package in.koreatech.koin.ui.store.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.StoreInteractor;
import in.koreatech.koin.data.network.response.StoresResponse;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class StorePresenter implements BasePresenter {

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
            storeView.showMessage(throwable.getMessage());
            storeView.hideLoading();
        }
    };

    public void getStoreList() {
        storeView.showLoading();
        storeInteractor.readStoreList(apiCallback);
    }

}