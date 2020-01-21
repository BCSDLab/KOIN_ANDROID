package in.koreatech.koin.ui.signup.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;

public interface SignupContract {
    interface View extends BaseView<Presenter> {
        void makeDialogTerms(String title, String textFile);

        void showMessage(String message);

        void gotoEmail();

        void showProgress();

        void hideProgress();

    }

    interface Presenter extends BasePresenter {
        void signUp(String id, String password);
    }
}
