package in.koreatech.koin.ui.event.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Event;

public interface EventDetailContract {
    interface View extends BaseView<EventDetailPresenter> {
        void showLoading();

        void hideLoading();

        void onEventDataReceived(Event event);

        void onEventDeleteCompleted(boolean inSuccess);

        void showMessage(String msg);

        void onEventCommentReceived(Comment comment);

        void onEventCommentDeleted(boolean isSuccess);

        void getEventDetail(int shopId);
    }
}
