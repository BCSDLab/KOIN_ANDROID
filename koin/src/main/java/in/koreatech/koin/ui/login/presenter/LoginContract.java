package in.koreatech.koin.ui.login.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void gotoMain();
    }

    interface Presenter extends BasePresenter {
        void login(String id, String password, Boolean isPasswordHash);
    }
}
