package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;


public interface MarketUsedDetailContract {
    interface View extends BaseView<MarketUsedDetailPresenter> {
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

        void showMarketItemDelete();

        void showMarketItemDeleteFail();

        void showGrantCheck(boolean isGranted);
    }
}
