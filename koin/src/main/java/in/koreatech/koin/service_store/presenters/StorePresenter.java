package in.koreatech.koin.service_store.presenters;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.core.networks.interactors.StoreInteractor;
import in.koreatech.koin.core.networks.responses.StoresResponse;
import in.koreatech.koin.service_store.contracts.StoreContract;

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
        }

        @Override
        public void onFailure(Throwable throwable) {
            storeView.showMessage(throwable.getMessage());
        }
    };

    public void getStoreList() {
        storeInteractor.readStoreList(apiCallback);
    }

}