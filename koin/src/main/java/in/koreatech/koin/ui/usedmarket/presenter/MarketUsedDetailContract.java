package in.koreatech.koin.ui.usedmarket.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;


public interface MarketUsedDetailContract {
    interface View extends BaseView<Presenter> {
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

    interface Presenter extends BasePresenter {
        void readMarketDetail(int id);

        void createComment(int id, String content);

        void deleteComment(Comment comment, Item item);

        void editComment(Comment comment, Item item, String content);

        void deleteItem(int id);

        void checkGranted(int id);
    }
}
