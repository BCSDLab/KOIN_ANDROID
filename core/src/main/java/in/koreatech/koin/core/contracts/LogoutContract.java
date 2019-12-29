package in.koreatech.koin.core.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public interface LogoutContract {
    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showMessage(String message);
        void gotoLogIn();
    }

    interface Presenter extends BasePresenter {

    }
}
