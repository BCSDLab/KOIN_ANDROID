package in.koreatech.koin.ui.usedmarket.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;

public interface MarketUsedEditContract {
    interface View extends BaseView<MarketUsedEditPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void showMessage(@StringRes int message);

        void showUpdateSuccess();

        void showUpdateFail();

        void showImageUploadSuccess(String Url);

        void showImageUploadFail();

    }
}

