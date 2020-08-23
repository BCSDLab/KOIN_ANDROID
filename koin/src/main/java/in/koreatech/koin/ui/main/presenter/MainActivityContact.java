package in.koreatech.koin.ui.main.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface MainActivityContact {
    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();

        void updateShuttleBusTime(int current);
        void updateCityBusTime(int current);
        void updateDaesungBusTime(int current);
        void updateShuttleBusDepartInfo(String current);
        void updateCityBusDepartInfo(int current);
        void updateDaesungBusDepartInfo(String current);
        void updateFailDaesungBusDepartInfo();
        void updateFailShuttleBusDepartInfo();
        void updateFailCityBusDepartInfo();
        void updateShuttleBusInfo(int term);
    }

    interface Presenter {
        void getTermInfo();
        void getCityBus(int depart, int arrival);
        void getDaesungBus(int depart, int arrival);
        void getShuttleBus(int depart, int arrival, int term);
    }
}
