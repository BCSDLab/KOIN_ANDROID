package in.koreatech.koin.service_callvan.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.CallvanRoom;
import in.koreatech.koin.service_callvan.presenters.CallvanRoomPresenter;

/**
 * Created by hyerim on 2018. 6. 18....
 */
public interface CallvanRoomContract {
    interface View extends BaseView<CallvanRoomPresenter> {
        void showMessage(String message);

        void onCallvanRoomListDataReceived(ArrayList<CallvanRoom> roomArrayList);
        void onCallvanRoomDataReceived(CallvanRoom room);
        void onCallvanRoomDecreasePeopleReceived(boolean isSuccess);
        void onCallvanRoomIncreasePeopleReceived(int roomUid);

        ArrayList<CallvanRoom> sortRoomArrayList(ArrayList<CallvanRoom> sortArrayList);

        void updateUserInterface(ArrayList<CallvanRoom> renderingList, int scrollPosition);

    }
}
