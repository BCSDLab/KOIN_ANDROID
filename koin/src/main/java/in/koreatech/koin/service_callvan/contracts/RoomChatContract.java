package in.koreatech.koin.service_callvan.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.service_callvan.presenters.CallvanRoomChatPresenter;

/**
 * Created by hyerim on 2018. 7. 1....
 */
public interface RoomChatContract {
    interface View extends BaseView<CallvanRoomChatPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onClickSendButton();

        void updateRoomChatView();

        void updateUserInterface();
    }
}
