package in.koreatech.koin.ui.forgotpassword.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface ForgotPasswordContract {
    interface View extends BaseView<ForgotPasswordPresenter> {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void onClickResetPasswordButton();

        void onClickToLoginButton();

        void goToEmail();
    }
}
