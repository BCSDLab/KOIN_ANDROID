package in.koreatech.koin.service_bus.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.service_bus.presenters.BusTimeTablePresenter;

public interface BusTimeTableContract {
    interface View extends BaseView<BusTimeTablePresenter> {

        void setBusTimeTableSpinner(int term);


    }
}
