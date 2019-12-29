package in.koreatech.koin.core.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;

/**
 * Created by hyerim on 2018. 5. 31....
 */
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
