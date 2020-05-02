package in.koreatech.koin.ui.land.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.BokdukRoom;
import in.koreatech.koin.data.network.entity.Land;

public interface LandContract {
    interface View extends BaseView<LandPresenter> {

        void onLandListDataReceived(ArrayList<Land> landArrayList);

        void updateUserInterface();

        void showMessage(String message);
    }
}
