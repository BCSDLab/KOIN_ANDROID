package in.koreatech.koin.core.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;

/**
 * Created by hyerim on 2018. 6. 1....
 */
public interface SignupContract {
    interface View extends BaseView<Presenter> {
        void makeDialogTerms(String title, String textFile);

//        void checkTerms();

        void onClickSendVerificationButton();

//        void onClickToLoginButton();

        void onClickPersonalInfoTermsTextView();

        void onClickSignupTermsTextView();

//        void OnSignupTermsCheckBoxClicked();
//
//        void OnPersonalInfoTermsCheckBoxClicked();

        void showMessage(String message);

        void gotoEmail();

        void showProgress();

        void hideProgress();

    }

    interface Presenter extends BasePresenter {
        void signUp(String id, String password);
    }
}
