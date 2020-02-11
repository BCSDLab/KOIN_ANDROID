package in.koreatech.koin.ui.bus.presenter;

import in.koreatech.koin.core.contract.BaseView;

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
