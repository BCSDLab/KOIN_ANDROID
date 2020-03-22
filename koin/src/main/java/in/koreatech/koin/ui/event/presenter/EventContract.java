package in.koreatech.koin.ui.event.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Event;

public interface EventContract {
    interface View extends BaseView<EventPresenter> {
        void onEventListDataReceived(ArrayList<Event> eventArrayList);

        void onGrantCheckReceived(Event event);

        void showMessage(String message);

        void showLoading();

        void hideLoading();

        ArrayList<Event> displayProcessingEvent(boolean isChecked1, boolean isChecked2);
    }
}
