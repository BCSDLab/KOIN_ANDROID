package in.koreatech.koin.service_used_market.presenters;

import in.koreatech.koin.service_used_market.contracts.MarketUsedDetailContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.interactors.MarketUsedInteractor;

public class MarketUsedDetailPresenter implements MarketUsedDetailContract.Presenter {

    private final MarketUsedInteractor marketUsedInteractor;
    private final MarketUsedDetailContract.View marketDetailView;

    public MarketUsedDetailPresenter(MarketUsedDetailContract.View marketDetailView, MarketUsedInteractor marketUsedInteractor) {
        this.marketUsedInteractor = marketUsedInteractor;
        this.marketDetailView = marketDetailView;
    }

    private final ApiCallback detailApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketDetailView.hideLoading();
            Item item = (Item) object;
            marketDetailView.onMarketDataReceived(item);
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketDetailView.hideLoading();
            marketDetailView.showMarketDataReceivedFail();
        }
    };

    private final ApiCallback commentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketDetailView.showMarketCommentUpdate();
            marketDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketDetailView.showMarketCommentUpdateFail();
            marketDetailView.hideLoading();
        }
    };

    private final ApiCallback commentDeleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketDetailView.showMarketCommentDelete();
            marketDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketDetailView.showMarketCommentDeleteFail();
            marketDetailView.hideLoading();
        }
    };

    private final ApiCallback commentEditApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketDetailView.showMarketCommentEdit();
            marketDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketDetailView.showMarketCommentEditFail();
            marketDetailView.hideLoading();
        }
    };

    private final ApiCallback itemDeleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketDetailView.showMarketItemDelete();
            marketDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketDetailView.showMarketItemDeleteFail();
            marketDetailView.hideLoading();
        }
    };

    @Override
    public void readMarketDetail(int id)
    {   marketDetailView.showLoading();
        marketUsedInteractor.readMarketDetail(id, detailApiCallback);
    }

    @Override
    public void createComment(int id, String content) {
        marketDetailView.showLoading();
        marketUsedInteractor.createCommentDetail(id, content, commentApiCallback);
    }

    @Override
    public void deleteComment(Comment comment, Item item) {
        marketDetailView.showLoading();
        marketUsedInteractor.deleteCommentDetail(item.id, comment.commentUid, commentDeleteApiCallback);
    }

    @Override
    public void editComment(Comment comment, Item item, String content) {
        marketDetailView.showLoading();
        marketUsedInteractor.editCommentDetail(item.id, comment.commentUid, content, commentEditApiCallback);
    }

    @Override
    public void deleteItem(int id){
        marketDetailView.showLoading();
        marketUsedInteractor.deleteMarketItem(id,itemDeleteApiCallback);
    }
}
