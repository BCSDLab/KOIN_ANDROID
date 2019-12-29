package in.koreatech.koin.service_used_market.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.responses.MarketPageResponse;


public interface MarketUsedContract {
    interface View extends BaseView<Presenter>{
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
