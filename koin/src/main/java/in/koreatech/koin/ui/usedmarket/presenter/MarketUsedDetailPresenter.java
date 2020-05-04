package in.koreatech.koin.ui.usedmarket.presenter;

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
        this.marketDetailView.setPresenter(this);
    }

    private final ApiCallback detailApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if(object instanceof Item) {
                Item item = (Item) object;
                marketDetailView.onMarketDataReceived(item);
            }
            marketDetailView.showMarketDataReceivedFail();
            marketDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketDetailView.showMarketDataReceivedFail();
            marketDetailView.hideLoading();
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
            Item item = (Item) object;
            if (item.isGrantEdit())
                marketDetailView.showGrantCheck(true);
            else
                marketDetailView.showGrantCheck(false);

            marketDetailView.hideLoading();
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
        marketUsedInteractor.deleteCommentDetail(item.getId(), comment.getCommentUid(), commentDeleteApiCallback);
    }

    public void editComment(Comment comment, Item item, String content) {
        marketDetailView.showLoading();
        marketUsedInteractor.editCommentDetail(item.getId(), comment.getCommentUid(), content, commentEditApiCallback);
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
