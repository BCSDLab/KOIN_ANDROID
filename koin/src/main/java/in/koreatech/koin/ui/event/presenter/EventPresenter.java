package in.koreatech.koin.ui.event.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventView.showMessage("홍보 게시물을 받아오지 못했습니다");
        }
    };
    
    public void getEventList() {
        eventArrayList.clear();
        eventInteractor.readEventList(apiCallback);
    }

    public ArrayList<Event> displayProcessingEvent(boolean isChecked1, boolean isChecked2) {
        ArrayList<Event> subAdDate = new ArrayList<>();
        if (isChecked1 && isChecked2) { //전체
            subAdDate.addAll(eventArrayList);
        }
        if (!isChecked1 && !isChecked2) { //아무것도 없음
            return subAdDate;
        }

        Date date = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

        if (isChecked1 && !isChecked2) {
            for (int i = 0; i < eventArrayList.size(); i++) {
                try {
                    if ((date.compareTo(formatDate.parse(eventArrayList.get(i).getEndDate())) == 1)) {
                        subAdDate.add(eventArrayList.get(i));
                    }
                } catch (ParseException e) {
                    eventView.showMessage("에러");
                }
            }
        }
        if (!isChecked1 && isChecked2) {
            for (int i = 0; i < eventArrayList.size(); i++) {
                try {
                    if ((date.compareTo(formatDate.parse(eventArrayList.get(i).getEndDate())) == -1)
                            && (date.compareTo(formatDate.parse(eventArrayList.get(i).getStartDate())) == 1)) {
                        subAdDate.add(eventArrayList.get(i));
                    }
                } catch (ParseException e) {
                    eventView.showMessage("에러");
                }
            }
        }

        return subAdDate;
    }

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
}
