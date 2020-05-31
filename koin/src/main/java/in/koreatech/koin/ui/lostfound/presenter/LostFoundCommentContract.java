package in.koreatech.koin.ui.lostfound.presenter;


import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.LostItem;

public interface LostFoundCommentContract {

    interface View extends BaseView<LostFoundCommentPresenter> {
        void showLoading();

        void hideLoading();

        void showSuccessLostItem(LostItem lostItem);

        void showSuccessUpdateComment(Comment comment);

        void showMessage(@StringRes int message);
    }
}
