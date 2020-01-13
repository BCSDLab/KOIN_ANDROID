package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.MarketItem;

import java.io.File;

public interface MarketUsedEditContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showUpdateSuccess();

        void showUpdateFail();

        void showImageUploadSuccess(String Url);

        void showImageUploadFail();

    }

    interface Presenter extends BasePresenter {
        void editMarketContent(int id, MarketItem marketItem);

        void uploadThumbnailImage(File file);
    }
}

