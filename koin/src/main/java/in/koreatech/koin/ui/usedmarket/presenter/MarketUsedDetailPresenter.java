package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;

public class MarketUsedDetailPresenter{

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

    private final ApiCallback grantCheckApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            DefaultResponse defaultResponse = (DefaultResponse) object;
            if (defaultResponse.isGrantEdit())
                marketDetailView.showGrantCheck(true);
            else
                marketDetailView.showGrantCheck(false);
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketDetailView.showGrantCheck(false);
            marketDetailView.hideLoading();
        }
    };

    public void readMarketDetail(int id) {
        marketDetailView.showLoading();
        marketUsedInteractor.readMarketDetail(id, detailApiCallback);
    }

    public void createComment(int id, String content) {
        marketDetailView.showLoading();
        marketUsedInteractor.createCommentDetail(id, content, commentApiCallback);
    }

    public void deleteComment(Comment comment, Item item) {
        marketDetailView.showLoading();
        marketUsedInteractor.deleteCommentDetail(item.id, comment.commentUid, commentDeleteApiCallback);
    }

    public void editComment(Comment comment, Item item, String content) {
        marketDetailView.showLoading();
        marketUsedInteractor.editCommentDetail(item.id, comment.commentUid, content, commentEditApiCallback);
    }

    public void deleteItem(int id) {
        marketDetailView.showLoading();
        marketUsedInteractor.deleteMarketItem(id, itemDeleteApiCallback);
    }

    public void checkGranted(int id) {
        marketDetailView.showLoading();
        marketUsedInteractor.readGrantedDetail(id, grantCheckApiCallback);
    }
}
