package in.koreatech.koin.ui.bus.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface BusMainContract {

    interface View extends BaseView<BusMainPresenter> {
        void showLoading();

        void hideLoading();

        void updateShuttleBusTime(long current, long next);

        void updateCityBusTime(long current, long next);

        void updateDaesungBusTime(long current, long next);

        void updateShuttleBusDepartInfo(String current, String next);

        void updateCityBusDepartInfo(long current, long next);

        void updateDaesungBusDepartInfo(String current, String next);

        void updateFailDaesungBusDepartInfo();

        void updateFailShuttleBusDepartInfo();

        void updateFailCityBusDepartInfo();

        void updateShuttleBusInfo(int term);

    }

}
