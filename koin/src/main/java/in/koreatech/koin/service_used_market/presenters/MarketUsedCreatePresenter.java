package in.koreatech.koin.service_used_market.presenters;

import com.google.firebase.perf.metrics.AddTrace;
import in.koreatech.koin.service_used_market.contracts.MarketUsedCreateContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.entity.MarketItem;
import in.koreatech.koin.core.networks.interactors.MarketUsedInteractor;

import java.io.File;

public class MarketUsedCreatePresenter implements MarketUsedCreateContract.Presenter {

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

    private final ApiCallback uploadImageApiCallback =  new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Item marketItem = (Item) object;
            if(marketItem.url != null)
                marketCreateContractView.showImageUploadSuccess(marketItem.url);
            marketCreateContractView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateContractView.showImageUploadFail();
            marketCreateContractView.hideLoading();
        }
    };

    @AddTrace(name = "MarketUsedCreatePresenter_uploadThumbnailImage")
    @Override
    public void uploadThumbnailImage(File file) {
        marketCreateContractView.showLoading();
        marketUsedInteractor.uploadThumbnailImage(file, uploadImageApiCallback);
    }

    @Override
    public void createMarketItem(MarketItem marketItem) {
        marketCreateContractView.showLoading();
        marketUsedInteractor.createMarketItem(marketItem, createMarketApiCallback);
    }
}
