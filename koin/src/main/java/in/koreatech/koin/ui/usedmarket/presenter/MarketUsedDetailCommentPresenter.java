package in.koreatech.koin.ui.usedmarket.presenter;


import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.util.FormValidatorUtil;

public class MarketUsedDetailCommentPresenter {

    private final MarketUsedInteractor marketUsedInteractor;
    private final MarketUsedCommentContract.View marketCreateCommentContracView;


    public MarketUsedDetailCommentPresenter(MarketUsedCommentContract.View mMarketCreateCommentContracView, MarketUsedInteractor marketUsedInteractor) {
        this.marketCreateCommentContracView = mMarketCreateCommentContracView;
        this.marketUsedInteractor = marketUsedInteractor;
    }


    private final ApiCallback detailApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketCreateCommentContracView.hideLoading();
            Item item = (Item) object;
            marketCreateCommentContracView.onMarketDataReceived(item);
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateCommentContracView.hideLoading();
            marketCreateCommentContracView.showMarketDataReceivedFail();
        }
    };

    private final ApiCallback commentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketCreateCommentContracView.showMarketCommentUpdate();
            marketCreateCommentContracView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateCommentContracView.showMarketCommentUpdateFail();
            marketCreateCommentContracView.hideLoading();
        }
    };

    private final ApiCallback commentDeleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketCreateCommentContracView.showMarketCommentDelete();
            marketCreateCommentContracView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateCommentContracView.showMarketCommentDeleteFail();
            marketCreateCommentContracView.hideLoading();
        }
    };

    private final ApiCallback commentEditApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketCreateCommentContracView.showMarketCommentEdit();
            marketCreateCommentContracView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateCommentContracView.showMarketCommentEditFail();
            marketCreateCommentContracView.hideLoading();
        }
    };

    public void readMarketDetail(int id) {
        marketCreateCommentContracView.showLoading();
        marketUsedInteractor.readMarketDetail(id, detailApiCallback);
    }

    public void createComment(int id, String content) {
        if (FormValidatorUtil.validateStringIsEmpty(content)) {
            marketCreateCommentContracView.showMessage(R.string.market_used_content_check);
            return;
        }
        marketCreateCommentContracView.showLoading();
        marketUsedInteractor.createCommentDetail(id, content, commentApiCallback);
    }

    public void deleteComment(Comment comment, Item item) {
        marketCreateCommentContracView.showLoading();
        marketUsedInteractor.deleteCommentDetail(item.getId(), comment.getCommentUid(), commentDeleteApiCallback);
    }

    public void editComment(Comment comment, Item item, String content) {
        if (FormValidatorUtil.validateStringIsEmpty(comment.getContent())) {
            marketCreateCommentContracView.showMessage(R.string.market_used_content_check);
            return;
        }
        marketCreateCommentContracView.showLoading();
        marketUsedInteractor.editCommentDetail(item.getId(), comment.getCommentUid(), content, commentEditApiCallback);
    }

}
