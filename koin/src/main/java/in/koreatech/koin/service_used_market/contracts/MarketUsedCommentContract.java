package in.koreatech.koin.service_used_market.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.entity.Item;


public interface MarketUsedCommentContract {
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
    }

    interface Presenter extends BasePresenter{
       void readMarketDetail(int id);

       void createComment(int id, String content);

        void deleteComment(Comment comment, Item item);

        void editComment(Comment comment, Item item, String content);
    }
}
