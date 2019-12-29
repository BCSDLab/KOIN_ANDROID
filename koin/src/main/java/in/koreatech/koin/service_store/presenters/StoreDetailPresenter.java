package in.koreatech.koin.service_store.presenters;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.core.networks.interactors.StoreInteractor;
import in.koreatech.koin.service_store.contracts.StoreDetailContract;

/**
 * Created by hyerim on 2018. 8. 16....
 */
public class StoreDetailPresenter implements BasePresenter {

    private final StoreDetailContract.View storeView;

    private final StoreInteractor storeInteractor;

    public StoreDetailPresenter(StoreDetailContract.View storeView, StoreInteractor storeInteractor) {
        this.storeView = storeView;
        this.storeInteractor = storeInteractor;
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
            storeView.showMessage("상점 정보를 불러오지 못했습니다.");
            storeView.hideLoading();
        }
    };




    public void getStore(int id) {
        storeView.showLoading();
        storeInteractor.readStore(id, apiCallback);
    }

}
