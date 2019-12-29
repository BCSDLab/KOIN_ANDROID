package in.koreatech.koin.service_bus.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.service_bus.presenters.BusMainPresenter;

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
