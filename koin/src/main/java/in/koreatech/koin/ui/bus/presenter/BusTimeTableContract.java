package in.koreatech.koin.ui.bus.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface BusTimeTableContract {
    interface View extends BaseView<BusTimeTablePresenter> {

        void setBusTimeTableSpinner(int term);


    }
}
