package in.koreatech.koin.ui.usedmarket.presenter;

import com.google.firebase.perf.metrics.AddTrace;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.entity.MarketItem;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.util.FilterUtil;
import in.koreatech.koin.util.FormValidatorUtil;

import java.io.File;

public class MarketUsedEditPresenter {
    private final MarketUsedInteractor marketUsedInteractor;
    private final MarketUsedEditContract.View marketEditView;


    public MarketUsedEditPresenter(MarketUsedEditContract.View marketEditView, MarketUsedInteractor marketUsedInteractor) {
        this.marketEditView = marketEditView;
        this.marketUsedInteractor = marketUsedInteractor;
        this.marketEditView.setPresenter(this);
    }

    private final ApiCallback contentEditApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketEditView.showUpdateSuccess();
            marketEditView.hideLoading();
        }

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
            if (marketItem.getUrl() != null)
                marketEditView.showImageUploadSuccess(marketItem.getUrl());
            marketEditView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketEditView.showImageUploadFail();
            marketEditView.hideLoading();
        }
    };

    public void editMarketContent(int id, MarketItem marketItem) {
        if (marketItem.getIsPhoneOpen() == 1 && !FilterUtil.isPhoneValidate(marketItem.getPhone())) {
            marketEditView.showMessage(R.string.market_used_phone_check);
            return;
        } else if (FormValidatorUtil.validateStringIsEmpty(marketItem.getTitle())) {
            marketEditView.showMessage(R.string.market_used_title_check);
            return;
        } else if (FormValidatorUtil.validateHTMLStringIsEmpty(marketItem.getContent())) {
            marketEditView.showMessage(R.string.market_used_content_check);
            return;
        }
        marketEditView.showLoading();
        marketUsedInteractor.editCotentEdit(id, marketItem, contentEditApiCallback);
    }

    public void uploadThumbnailImage(File file) {
        marketEditView.showLoading();
        marketUsedInteractor.uploadThumbnailImage(file, uploadImageApiCallback);
    }
}
