package in.koreatech.koin.ui.bus.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface BusMainContract {

    interface View extends BaseView<BusMainPresenter> {
        void showLoading();

        void hideLoading();

        void updateShuttleBusTime(int current, int next);

        void updateCityBusTime(int current, int next);

        void updateDaesungBusTime(int current, int next);

        void updateShuttleBusDepartInfo(String current,String next);

        void updateCityBusDepartInfo(int current, int next);

        void updateDaesungBusDepartInfo(String current, String next);

        void updateFailDaesungBusDepartInfo();

        void updateFailShuttleBusDepartInfo();

        void updateFailCityBusDepartInfo();

    }

}
