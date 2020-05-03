package in.koreatech.koin.ui.usedmarket.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;


public interface MarketUsedCommentContract {
    interface View extends BaseView<MarketUsedDetailCommentPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void showMessage(@StringRes int message);

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
