package in.koreatech.koin.ui.event.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.entity.Store;

public interface EventContract {
    interface View extends BaseView<EventPresenter> {
        void onEventListDataReceived(ArrayList<Event> eventArrayList);

        void onGrantCheckReceived(Event event);

        void onMyShopListReceived(ArrayList<Event> shopArrayList);

        void showMessage(String message);

        void showLoading();

        void hideLoading();
    }
}
