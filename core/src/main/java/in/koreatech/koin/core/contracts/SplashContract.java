package in.koreatech.koin.core.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;

/**
 * Created by hyerim on 2018. 6. 1....
 */
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
