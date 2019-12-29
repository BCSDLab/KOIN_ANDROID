package in.koreatech.koin.service_used_market.presenters;

import com.google.firebase.perf.metrics.AddTrace;

import in.koreatech.koin.service_used_market.contracts.MarketUsedEditContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.entity.MarketItem;
import in.koreatech.koin.core.networks.interactors.MarketUsedInteractor;

import java.io.File;

public class MarketUsedEditPresenter implements MarketUsedEditContract.Presenter {
    private final MarketUsedInteractor marketUsedInteractor;
    private final MarketUsedEditContract.View marketEditView;


    public MarketUsedEditPresenter(MarketUsedEditContract.View marketEditView, MarketUsedInteractor marketUsedInteractor) {
        this.marketEditView = marketEditView;
        this.marketUsedInteractor = marketUsedInteractor;
    }

    private final ApiCallback contentEditApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketEditView.showUpdateSuccess();
            marketEditView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketEditView.showUpdateFail();
            marketEditView.hideLoading();
        }
    };
    @AddTrace(name = "MarketUsedDetailPresenter_uploadThumbnailImage")
    private final ApiCallback uploadImageApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Item marketItem = (Item) object;
            if (marketItem.url != null)
                marketEditView.showImageUploadSuccess(marketItem.url);
            marketEditView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketEditView.showImageUploadFail();
            marketEditView.hideLoading();
        }
    };

    @Override
    public void editMarketContent(int id, MarketItem marketItem) {
        marketEditView.showLoading();
        marketUsedInteractor.editCotentEdit(id, marketItem, contentEditApiCallback);
    }

    @Override
    public void uploadThumbnailImage(File file) {
        marketEditView.showLoading();
        marketUsedInteractor.uploadImage(file, uploadImageApiCallback);
    }
}
