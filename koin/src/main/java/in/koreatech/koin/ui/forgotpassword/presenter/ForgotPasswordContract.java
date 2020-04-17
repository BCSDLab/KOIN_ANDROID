package in.koreatech.koin.ui.forgotpassword.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;

public interface ForgotPasswordContract {
    interface View extends BaseView<ForgotPasswordPresenter> {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void showMessage(@StringRes int message);

        void onClickResetPasswordButton();

        void onClickToLoginButton();

        void goToEmail();
    }
}
