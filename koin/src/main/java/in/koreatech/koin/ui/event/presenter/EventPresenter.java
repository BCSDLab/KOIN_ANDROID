package in.koreatech.koin.ui.event.presenter;

import java.util.ArrayList;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.interactor.EventInteractor;

public class EventPresenter {
    private final EventContract.View eventView;
    private final EventInteractor eventInteractor;
    private ArrayList<Event> eventArrayList;

    public EventPresenter(EventContract.View eventView, EventInteractor eventInteractor) {
        this.eventView = eventView;
        this.eventInteractor = eventInteractor;
        eventArrayList = new ArrayList<>();
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Event event = (Event) object;
            eventArrayList.clear();
            eventArrayList.addAll(event.getEventArrayList());
            eventView.onEventListDataReceived(eventArrayList);
            eventView.hideLoading();
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
        eventArrayList.clear();
    }

    public void getPendingEventList(int pageNum) {
        eventView.showLoading();
        eventInteractor.readPendingEventList(pageNum, apiCallback);
    }

    public void getClosedEventList(int pageNum) {
        eventView.showLoading();
        eventInteractor.readClosedEventList(pageNum, apiCallback);
    }
}
