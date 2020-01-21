package in.koreatech.koin.ui.usedmarket.presenter;


import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;

public class MarketUsedDetailCommentPresenter{

    private final MarketUsedInteractor marketUsedInteractor;
    private final MarketUsedCommentContract.View mMarketCreateCommentContracView;

    public MarketUsedDetailCommentPresenter(MarketUsedCommentContract.View mMarketCreateCommentContracView, MarketUsedInteractor marketUsedInteractor) {
        this.mMarketCreateCommentContracView = mMarketCreateCommentContracView;
        this.marketUsedInteractor = marketUsedInteractor;
    }


    private final ApiCallback detailApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mMarketCreateCommentContracView.hideLoading();
            Item item = (Item) object;
            mMarketCreateCommentContracView.onMarketDataReceived(item);
        }

        @Override
        public void onFailure(Throwable throwable) {
            mMarketCreateCommentContracView.hideLoading();
            mMarketCreateCommentContracView.showMarketDataReceivedFail();
        }
    };

    private final ApiCallback commentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mMarketCreateCommentContracView.showMarketCommentUpdate();
            mMarketCreateCommentContracView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mMarketCreateCommentContracView.showMarketCommentUpdateFail();
            mMarketCreateCommentContracView.hideLoading();
        }
    };

    private final ApiCallback commentDeleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mMarketCreateCommentContracView.showMarketCommentDelete();
            mMarketCreateCommentContracView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mMarketCreateCommentContracView.showMarketCommentDeleteFail();
            mMarketCreateCommentContracView.hideLoading();
        }
    };

    private final ApiCallback commentEditApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mMarketCreateCommentContracView.showMarketCommentEdit();
            mMarketCreateCommentContracView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mMarketCreateCommentContracView.showMarketCommentEditFail();
            mMarketCreateCommentContracView.hideLoading();
        }
    };

    public void readMarketDetail(int id) {
        mMarketCreateCommentContracView.showLoading();
        marketUsedInteractor.readMarketDetail(id, detailApiCallback);
    }

    public void createComment(int id, String content) {
        mMarketCreateCommentContracView.showLoading();
        marketUsedInteractor.createCommentDetail(id, content, commentApiCallback);
    }

    public void deleteComment(Comment comment, Item item) {
        mMarketCreateCommentContracView.showLoading();
        marketUsedInteractor.deleteCommentDetail(item.id, comment.commentUid, commentDeleteApiCallback);
    }

    public void editComment(Comment comment, Item item, String content) {
        mMarketCreateCommentContracView.showLoading();
        marketUsedInteractor.editCommentDetail(item.id, comment.commentUid, content, commentEditApiCallback);
    }

}
