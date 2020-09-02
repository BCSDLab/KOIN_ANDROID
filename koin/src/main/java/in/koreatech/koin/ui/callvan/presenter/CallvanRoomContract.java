package in.koreatech.koin.ui.callvan.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.CallvanRoom;

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
