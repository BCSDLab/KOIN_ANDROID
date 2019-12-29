package in.koreatech.koin.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;

/**
 * Created by hyerim on 2018. 6. 1....
 */
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
