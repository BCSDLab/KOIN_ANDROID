package in.koreatech.koin.ui.userinfo.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;

public interface UserInfoContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onUserDataReceived();

        void onDeleteUserReceived();

        void checkRequiredInfo();

    }

    interface Presenter extends BasePresenter {

        void getUserData();

        void deleteUser(int roomUid);
    }

}
