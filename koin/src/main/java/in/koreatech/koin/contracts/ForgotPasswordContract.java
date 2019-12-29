package in.koreatech.koin.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;

/**
 * Created by hyerim on 2018. 7. 8....
 */
public interface ForgotPasswordContract {
    interface View extends BaseView<Presenter> {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void onClickResetPasswordButton();

        void onClickToLoginButton();

        void goToEmail();
    }

    interface Presenter extends BasePresenter {
        void findPassword(String id);
    }
}
