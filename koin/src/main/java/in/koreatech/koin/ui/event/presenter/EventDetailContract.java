package in.koreatech.koin.ui.event.presenter;

public interface EventDetailContract {
    interface View extends BaseView<AdDetailPresenter> {
        void showLoading();

        void hideLoading();

        void onAdDetailDataReceived(AdDetail adDetail);

        void onAdDetailDeleteCompleted(boolean inSuccess);

        void showMessage(String msg);

        void onAdDetailCommentReceived(Comment comment);

        void onAdDetailCommentDeleted(boolean isSuccess);

        void getAdDetailInfo(int shopId);
    }
}
