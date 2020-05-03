package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.data.network.response.MarketPageResponse;


public class MarketUsedPresenter{

    private final MarketUsedInteractor marketUsedInteractor;
    private final MarketUsedContract.View marketView;


    public MarketUsedPresenter(MarketUsedContract.View marketView, MarketUsedInteractor marketUsedInteractor) {
        this.marketUsedInteractor = marketUsedInteractor;
        this.marketView = marketView;
        this.marketView.setPresenter(this);
    }

    private final ApiCallback listApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if(object instanceof MarketPageResponse) {
                MarketPageResponse marketPageResponse = (MarketPageResponse) object;
                marketView.onMarketDataReceived(marketPageResponse);
            } else {
                marketView.showMarketDataReceivedFail();
            }
            marketView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketView.showMarketDataReceivedFail();
            marketView.hideLoading();
        }
    };

    private final ApiCallback grantedApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Item item = (Item) object;
            if (item.isGrantEdit())
                marketView.onGrantedDataReceived(true);
            else
                marketView.onGrantedDataReceived(false);

            marketView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketView.onGrantedDataReceived(false);
            marketView.hideLoading();
        }
    };

    private final ApiCallback detailApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if(object instanceof Item) {
                Item item = (Item) object;
                marketView.onMarketDataReceived(item);
            } else {
                marketView.showMarketDataReceivedFail();
            }
            marketView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketView.showMarketDataReceivedFail();
            marketView.hideLoading();
        }
    };

    public void readMarket(int type, int limit) {
        marketView.showLoading();
        marketUsedInteractor.readMarketList(type, limit, listApiCallback);
    }

    public void readGrantedDetail(int id) {
        marketView.showLoading();
        marketUsedInteractor.readGrantedDetail(id, grantedApiCallback);
    }

    public void readDetailMarket(int id) {
        marketView.showLoading();
        marketUsedInteractor.readMarketDetail(id, detailApiCallback);
    }

}
