package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.response.MarketPageResponse;


public interface MarketUsedContract {
    interface View extends BaseView<MarketUsedPresenter> {
        void showLoading();

        void hideLoading();

        void onMarketDataReceived(MarketPageResponse marketPageResponses);

        void showMarketDataReceivedFail();

        void onGrantedDataReceived(boolean granted);

        void onMarketDataReceived(Item item);
    }
}
