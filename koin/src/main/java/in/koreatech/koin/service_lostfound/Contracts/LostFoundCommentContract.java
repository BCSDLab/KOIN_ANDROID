package in.koreatech.koin.service_lostfound.Contracts;


import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.entity.LostAndFound;
import in.koreatech.koin.core.networks.entity.LostItem;

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
