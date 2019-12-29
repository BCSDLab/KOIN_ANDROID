package in.koreatech.koin.service_land.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.service_land.presenter.LandPresenter;

public interface LandContract {
    interface View extends BaseView<LandPresenter> {

        void onLandListDataReceived(ArrayList<BokdukRoom> landArrayList);
        void updateUserInterface();
        void showMessage(String message);
    }
}
