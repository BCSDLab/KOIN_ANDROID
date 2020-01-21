package in.koreatech.koin.ui.splash.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;

public interface SplashContract {
    interface View extends BaseView<Presenter> {
        void gotoMain();

        void gotoLogin();

        void gotoAppMarket(int type, String storeVersion);

        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {
        void callActivity();

        void checkUpdate(String currentVersion);

        void checkToken();

        void updateToken();
    }

}
