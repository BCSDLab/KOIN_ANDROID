package in.koreatech.koin.ui.userinfo.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface UserInfoContract {
    interface View extends BaseView<UserInfoPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onUserDataReceived();

        void onDeleteUserReceived();

        void checkRequiredInfo();

    }
}
