package in.koreatech.koin.ui.usedmarket.presenter;


import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.util.FormValidatorUtil;

public class MarketUsedDetailCommentPresenter {

    private final MarketUsedInteractor marketUsedInteractor;
    private final MarketUsedCommentContract.View marketCreateCommentContractView;


    public MarketUsedDetailCommentPresenter(MarketUsedCommentContract.View marketCreateCommentContractView, MarketUsedInteractor marketUsedInteractor) {
        this.marketCreateCommentContractView = marketCreateCommentContractView;
        this.marketUsedInteractor = marketUsedInteractor;
        this.marketCreateCommentContractView.setPresenter(this);
    }


    private final ApiCallback detailApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof Item) {
                Item item = (Item) object;
                marketCreateCommentContractView.onMarketDataReceived(item);
            } else {
                marketCreateCommentContractView.showMarketDataReceivedFail();
            }
            marketCreateCommentContractView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateCommentContractView.showMarketDataReceivedFail();
            marketCreateCommentContractView.hideLoading();
        }
    };

    private final ApiCallback commentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketCreateCommentContractView.showMarketCommentUpdate();
            marketCreateCommentContractView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateCommentContractView.showMarketCommentUpdateFail();
            marketCreateCommentContractView.hideLoading();
        }
    };

    private final ApiCallback commentDeleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketCreateCommentContractView.showMarketCommentDelete();
            marketCreateCommentContractView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateCommentContractView.showMarketCommentDeleteFail();
            marketCreateCommentContractView.hideLoading();
        }
    };

    private final ApiCallback commentEditApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            marketCreateCommentContractView.showMarketCommentEdit();
            marketCreateCommentContractView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            marketCreateCommentContractView.showMarketCommentEditFail();
            marketCreateCommentContractView.hideLoading();
        }
    };

    public void readMarketDetail(int id) {
        marketCreateCommentContractView.showLoading();
        marketUsedInteractor.readMarketDetail(id, detailApiCallback);
    }

    public void createComment(int id, String content) {
        if (FormValidatorUtil.validateStringIsEmpty(content)) {
            marketCreateCommentContractView.showMessage(R.string.market_used_content_check);
            return;
        }
        marketCreateCommentContractView.showLoading();
        marketUsedInteractor.createCommentDetail(id, content, commentApiCallback);
    }

    public void deleteComment(Comment comment, Item item) {
        marketCreateCommentContractView.showLoading();
        marketUsedInteractor.deleteCommentDetail(item.getId(), comment.getCommentUid(), commentDeleteApiCallback);
    }

    public void editComment(Comment comment, Item item, String content) {
        if (FormValidatorUtil.validateStringIsEmpty(comment.getContent())) {
            marketCreateCommentContractView.showMessage(R.string.market_used_content_check);
            return;
        }
        marketCreateCommentContractView.showLoading();
        marketUsedInteractor.editCommentDetail(item.getId(), comment.getCommentUid(), content, commentEditApiCallback);
    }

}
