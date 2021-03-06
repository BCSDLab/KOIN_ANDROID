package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.entity.MarketItem;


import java.io.File;


public interface MarketUsedCreateContract {
    interface View extends BaseView<MarketUsedCreatePresenter> {
        void showLoading();

        void hideLoading();

        void showMarketCreatedSuccess(Item item);

        void showMarketCreatefFail();

        void showImageUploadSuccess(String url);

        void showImageUploadFail();

    }
}
