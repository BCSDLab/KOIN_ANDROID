package in.koreatech.koin.ui.land.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.BokdukRoom;

public interface LandContract {
    interface View extends BaseView<LandPresenter> {

        void onLandListDataReceived(ArrayList<BokdukRoom> landArrayList);
        void updateUserInterface();
        void showMessage(String message);
    }
}
