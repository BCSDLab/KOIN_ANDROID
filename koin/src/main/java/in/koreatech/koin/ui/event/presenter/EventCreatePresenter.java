package in.koreatech.koin.ui.event.presenter;

import java.io.File;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.entity.Image;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.EventInteractor;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.data.network.interactor.StoreInteractor;
import in.koreatech.koin.data.network.interactor.StoreRestInteractor;

public class EventCreatePresenter {
    private EventCreateContract.View eventCreateView;
    private EventInteractor eventInteractor;
    private MarketUsedInteractor marketUsedInteractor;

    private String uploadImageId;

    public EventCreatePresenter(EventCreateContract.View eventCreateView, EventInteractor eventInterator) {
        this.eventCreateView = eventCreateView;
        this.eventInteractor = eventInterator;
        this.marketUsedInteractor = new MarketUsedRestInteractor();
    }

    private final ApiCallback eventApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            eventCreateView.onEventDataReceived((Event) object);
            eventCreateView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventCreateView.showMessage(throwable.getMessage());
            eventCreateView.hideLoading();
        }
    };

    private final ApiCallback uploadImageApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Image img = (Image) object;

            if (img != null) {
                eventCreateView.showUploadImage(img.getUrls().get(0), uploadImageId);
            } else {
                eventCreateView.showFailUploadImage(uploadImageId);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventCreateView.showFailUploadImage(uploadImageId);
        }
    };

    private final ApiCallback myShopApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Event event = (Event) object;
            eventCreateView.onMyShopListReceived(event.getMyShopList());
            eventCreateView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventCreateView.showMessage(throwable.getMessage());
            eventCreateView.hideLoading();
        }
    };

    private final ApiCallback shopNameApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Store store = (Store) object;
            eventCreateView.onShopNameReceived(store.getName());
            eventCreateView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventCreateView.showMessage(throwable.getMessage());
            eventCreateView.hideLoading();
        }
    };

    public void createEvent(Event adDetail) {
        eventCreateView.showLoading();
        eventInteractor.createEvent(adDetail, eventApiCallback);
    }

    public void uploadImage(File file, String uid) {
        uploadImageId = uid;
        if(file == null) {
            eventCreateView.showFailUploadImage(uid);
            return;
        }

        marketUsedInteractor.uploadImage(file, uploadImageApiCallback);
    }

    public void updateEvent(int articleId, Event event) {
        eventCreateView.showLoading();
        eventInteractor.updateEvent(articleId, event, eventApiCallback);
    }

    public void getMyShopList() {
        eventCreateView.showLoading();
        eventInteractor.readMyShopList(myShopApiCallback);
    }

    public void getShopName(int shopId) {
        eventCreateView.showLoading();
        StoreInteractor storeInteractor = new StoreRestInteractor();
        storeInteractor.readStore(shopId, shopNameApiCallback);
    }
}
