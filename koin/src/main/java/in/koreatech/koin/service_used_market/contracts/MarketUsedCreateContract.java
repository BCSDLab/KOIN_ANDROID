package in.koreatech.koin.service_used_market.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.entity.MarketItem;


import java.io.File;


public interface MarketUsedCreateContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showMarketCreatedSuccess(Item item);

        void showMarketCreatefFail();

        void showImageUploadSuccess(String url);

        void showImageUploadFail();

    }

    interface Presenter extends BasePresenter{
        void uploadThumbnailImage(File file);

        void createMarketItem(MarketItem marketItem);

    }
}
