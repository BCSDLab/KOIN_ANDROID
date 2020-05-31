package in.koreatech.koin.ui.usedmarket.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.entity.MarketItem;


import java.io.File;


public interface MarketUsedCreateContract {
    interface View extends BaseView<MarketUsedCreatePresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void showMessage(@StringRes int message);

        void showMarketCreatedSuccess(Item item);

        void showMarketCreateFail();

        void showImageUploadSuccess(String url);

        void showImageUploadFail();

    }
}
