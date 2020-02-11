package in.koreatech.koin.ui.signup.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface SignupContract {
    interface View extends BaseView<SignupPresenter> {
        void makeDialogTerms(String title, String textFile);

        void showMessage(String message);

        void gotoEmail();

        void showProgress();

        void hideProgress();

    }
}
