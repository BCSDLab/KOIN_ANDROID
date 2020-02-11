package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface MarketUsedEditContract {
    interface View extends BaseView<MarketUsedEditPresenter> {
        void showLoading();

        void hideLoading();

        void showUpdateSuccess();

        void showUpdateFail();

        void showImageUploadSuccess(String Url);

        void showImageUploadFail();

    }
}

