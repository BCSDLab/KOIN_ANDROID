package in.koreatech.koin.ui.event.presenter;

import android.util.Log;

import java.util.ArrayList;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.EventInteractor;

public class EventPresenter {
    private final EventContract.View eventView;
    private final EventInteractor eventInteractor;

    public EventPresenter(EventContract.View eventView, EventInteractor eventInteractor) {
        this.eventView = eventView;
        this.eventInteractor = eventInteractor;
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Event event = (Event) object;
            ArrayList<Event> eventArrayList = new ArrayList<>(event.getEventArrayList());
            eventView.onEventListDataReceived(eventArrayList);
            eventView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventView.showMessage(throwable.getMessage());
            eventView.hideLoading();
        }
    };

    // 점주 당 홍보 게시물은 1개이므로 1개의 홍보 게시물을 추출하여 전달
    private final ApiCallback myPendingApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Event event = (Event) object;
            ArrayList<Event> eventArrayList = new ArrayList<>(event.getEventArrayList());
            eventView.onMyPendingEventReceived(eventArrayList.get(0));
            eventView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventView.showMessage(throwable.getMessage());
            eventView.hideLoading();
        }
    };

    private final ApiCallback myShopApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Event event = (Event) object;
            eventView.onMyShopListReceived(event.getMyShopList());
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventView.showMessage(throwable.getMessage());
            eventView.hideLoading();
        }
    };

    private final ApiCallback grantCheckApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            eventView.onGrantCheckReceived((Event) object);
            eventView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventView.showMessage(throwable.getMessage());
            eventView.hideLoading();
        }
    };

    public void getEventGrantCheck(int articleUid) {
        eventView.showLoading();
        eventInteractor.updateGrantCheck(articleUid, grantCheckApiCallback);
    }
    
    public void getEventList(int pageNum) {
        eventView.showLoading();
        eventInteractor.readEventList(pageNum, apiCallback);
    }

    public void getMyShops() {
        eventView.showLoading();
        eventInteractor.readMyShopList(myShopApiCallback);
    }

    public void getPendingEventList(int pageNum) {
        eventView.showLoading();
        eventInteractor.readPendingEventList(pageNum, apiCallback);
    }

    public void getClosedEventList(int pageNum) {
        eventView.showLoading();
        eventInteractor.readClosedEventList(pageNum, apiCallback);
    }

    public void getMyPendingEvent() {
        eventView.showLoading();
        eventInteractor.readMyPendingEvent(myPendingApiCallback);
    }
}
