package in.koreatech.koin.ui.store.presenter;

import android.util.Log;

import java.io.EOFException;
import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.EventRestInteractor;
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
            storeView.showMessage(throwable.getMessage());
            storeView.hideLoading();
        }
    };

    private final ApiCallback randomEventApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Event event = (Event) object;
            storeView.onRandomEventDataReceived(event);
            storeView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            if(!(throwable instanceof EOFException)){
                storeView.showMessage(throwable.getMessage());
            }
            storeView.hideLoading();
        }
    };

    public void getStoreList() {
        storeView.showLoading();
        storeInteractor.readStoreList(apiCallback);
    }

    public void getRandomEvent() {
        storeView.showLoading();
        EventRestInteractor eventRestInteractor = new EventRestInteractor();
        eventRestInteractor.readPendingRandomEvent(randomEventApiCallback);
    }

}