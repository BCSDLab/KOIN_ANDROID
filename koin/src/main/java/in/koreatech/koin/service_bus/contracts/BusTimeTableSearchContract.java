package in.koreatech.koin.service_bus.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.service_bus.presenters.BusTimeTableSearchPresenter;

public interface BusTimeTableSearchContract {

    interface View extends BaseView<BusTimeTableSearchPresenter> {
        void showLoading();

        void hideLoading();

        void updateShuttleBusTime(String time);

        void updateDaesungBusTime(String time);

        void updateFailDaesungBusDepartInfo();

        void updateFailShuttleBusDepartInfo();

        void showBusTimeInfo();
    }

}
