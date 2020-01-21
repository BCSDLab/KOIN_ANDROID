package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;


public interface MarketUsedCommentContract {
    interface View extends BaseView<MarketUsedDetailCommentPresenter> {
        void showLoading();

        void hideLoading();

        void onMarketDataReceived(Item item);

        void showMarketDataReceivedFail();

        void showMarketCommentUpdate();

        void showMarketCommentUpdateFail();

        void showMarketCommentDelete();

        void showMarketCommentDeleteFail();

        void showMarketCommentEdit();

        void showMarketCommentEditFail();
    }
}
