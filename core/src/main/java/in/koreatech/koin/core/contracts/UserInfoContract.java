package in.koreatech.koin.core.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;

/**
 * Created by hyerim on 2018. 7. 2....
 */
public interface UserInfoContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onUserDataReceived();

        void onDeleteUserReceived();

        void checkRequiredInfo();

        void onClickDeleteUserButton();
    }

    interface Presenter extends BasePresenter {

        void getUserData();

        void deleteUser(int roomUid);
    }

}
