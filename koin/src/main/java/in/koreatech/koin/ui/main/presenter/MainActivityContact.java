package in.koreatech.koin.ui.main.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.ui.bus.presenter.BusMainPresenter;

public interface MainActivityContact {
    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();

        void updateBusPager();
        void updateDining();
    }

    interface Presenter {
        void getBusRemaningTime(long nowTime, int semester, int departure, int arrival);
        void getDiningList(String date);
    }
}
