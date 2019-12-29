package in.koreatech.koin.service_used_market.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.MarketItem;

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

    interface Presenter extends BasePresenter{
        void editMarketContent(int id, MarketItem marketItem);

        void uploadThumbnailImage(File file);
    }
}

