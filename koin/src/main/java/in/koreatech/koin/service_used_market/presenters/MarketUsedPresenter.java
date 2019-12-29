package in.koreatech.koin.service_used_market.presenters;

import in.koreatech.koin.service_used_market.contracts.MarketUsedContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.interactors.MarketUsedInteractor;
import in.koreatech.koin.core.networks.responses.MarketPageResponse;


public class MarketUsedPresenter implements MarketUsedContract.Presenter {

    private final MarketUsedInteractor marketUsedInteractor;
    private final MarketUsedContract.View marketView;


    public MarketUsedPresenter(MarketUsedContract.View marketView, MarketUsedInteractor marketUsedInteractor) {
        this.marketUsedInteractor = marketUsedInteractor;
        this.marketView = marketView;
    }

    private final ApiCallback listApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            MarketPageResponse marketArrayList = (MarketPageResponse) object;
            marketView.onMarketDataReceived(marketArrayList);
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
            marketView.onGrantedDataReceived(item.grantEdit);
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
            Item item = (Item) object;
            marketView.onMarketDataReceived(item);
            marketView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketView.showMarketDataReceivedFail();
            marketView.hideLoading();
        }
    };
    @Override
    public void readMarket(int type, int limit) {
        marketView.showLoading();
        marketUsedInteractor.readMarketList(type, limit, listApiCallback);
    }

    @Override
    public void readGrantedDetail(int id) {
        marketView.showLoading();
        marketUsedInteractor.readGrantedDetail(id, grantedApiCallback);
    }

    @Override
    public void readDetailMarket(int id) {
        marketView.showLoading();
        marketUsedInteractor.readMarketDetail(id, detailApiCallback);
    }

}
