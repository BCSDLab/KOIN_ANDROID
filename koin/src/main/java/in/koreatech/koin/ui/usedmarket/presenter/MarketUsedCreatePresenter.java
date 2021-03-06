package in.koreatech.koin.ui.usedmarket.presenter;

import com.google.firebase.perf.metrics.AddTrace;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.entity.MarketItem;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;

import java.io.File;

public class MarketUsedCreatePresenter{

    private final MarketUsedInteractor marketUsedInteractor;
    private final MarketUsedCreateContract.View marketCreateContractView;


    public MarketUsedCreatePresenter(MarketUsedCreateContract.View marketCreateContractView, MarketUsedInteractor marketUsedInteractor) {
        this.marketUsedInteractor = marketUsedInteractor;
        this.marketCreateContractView = marketCreateContractView;
    }

    private final ApiCallback createMarketApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Item marketItem = (Item) object;
            marketCreateContractView.showMarketCreatedSuccess(marketItem);
            marketCreateContractView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateContractView.showMarketCreatefFail();
            marketCreateContractView.hideLoading();
        }
    };

    private final ApiCallback uploadImageApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Item marketItem = (Item) object;
            if (marketItem.getUrl() != null)
                marketCreateContractView.showImageUploadSuccess(marketItem.getUrl());
            marketCreateContractView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateContractView.showImageUploadFail();
            marketCreateContractView.hideLoading();
        }
    };

    @AddTrace(name = "MarketUsedCreatePresenter_uploadThumbnailImage")
    public void uploadThumbnailImage(File file) {
        marketCreateContractView.showLoading();
        marketUsedInteractor.uploadThumbnailImage(file, uploadImageApiCallback);
    }

    public void createMarketItem(MarketItem marketItem) {
        marketCreateContractView.showLoading();
        marketUsedInteractor.createMarketItem(marketItem, createMarketApiCallback);
    }
}
