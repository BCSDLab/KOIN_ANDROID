package in.koreatech.koin.ui.login.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface LoginContract {
    interface View extends BaseView<LoginPresenter> {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void gotoMain();
    }
}
