package in.koreatech.koin.ui.lostfound.presenter;


import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.LostItem;

public interface LostFoundCommentContract {

    interface View extends BaseView<LostFoundCommentContract.Presenter> {
        void showLoading();

        void hideLoading();

        void showSuccessLostItem(LostItem lostItem);

        void showSuccessUpdateComment(Comment comment);

        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {
        void createComment(int id, String comment);

        void updateComment(int id, int commentId, String comment);

        void deleteComment(int id, int commentId);

        void getLostItem(int id);
    }
}
