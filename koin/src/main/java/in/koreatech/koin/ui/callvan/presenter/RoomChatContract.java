package in.koreatech.koin.ui.callvan.presenter;

import in.koreatech.koin.core.contract.BaseView;

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
