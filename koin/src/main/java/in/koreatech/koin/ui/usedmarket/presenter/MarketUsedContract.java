package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.response.MarketPageResponse;


public interface MarketUsedContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void onMarketDataReceived(MarketPageResponse marketPageResponses);

        void showMarketDataReceivedFail();

        void onGrantedDataReceived(boolean granted);

        void onMarketDataReceived(Item item);
    }

    interface Presenter extends BasePresenter {
        void readMarket(int type, int page);

        void readGrantedDetail(int id);

        void readDetailMarket(int id);
    }
}
