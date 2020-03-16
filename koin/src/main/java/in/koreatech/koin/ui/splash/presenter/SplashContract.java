package in.koreatech.koin.ui.splash.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface SplashContract {
    interface View extends BaseView<SplashPresenter> {
        void gotoMain();

        void gotoLogin();

        void gotoAppMarket(int type, String storeVersion);

        void showMessage(String message);
    }
}
