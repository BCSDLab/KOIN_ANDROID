package in.koreatech.koin.ui.lostfound.presenter;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.interactor.LostAndFoundInteractor;
import in.koreatech.koin.data.network.interactor.LostAndFoundRestInteractor;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.util.FormValidatorUtil;

/**
 * 댓글 Presenter
 */
public class LostFoundCommentPresenter {
    private LostFoundCommentContract.View lostFoundCommentView;
    private LostAndFoundInteractor lostAndFoundInteractor;
    private int id;

    public LostFoundCommentPresenter(LostFoundCommentContract.View lostFoundCommentView) {
        this.lostFoundCommentView = lostFoundCommentView;
        this.lostAndFoundInteractor = new LostAndFoundRestInteractor();
        this.lostFoundCommentView.setPresenter(this);
    }

    final ApiCallback createCommentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            lostFoundCommentView.showMessage(R.string.lost_and_found_created);
            getLostItem(id);
            lostFoundCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundCommentView.showMessage(R.string.error_network);
            lostFoundCommentView.hideLoading();
        }
    };

    final ApiCallback updateCommentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            lostFoundCommentView.showMessage(R.string.lost_and_found_edited);
            getLostItem(id);
            lostFoundCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundCommentView.showMessage(R.string.error_network);
            lostFoundCommentView.hideLoading();
        }
    };

    final ApiCallback deleteCommentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof DefaultResponse) {
                DefaultResponse defaultResponse = (DefaultResponse) object;
                if (defaultResponse.success) {
                    getLostItem(id);
                    lostFoundCommentView.showMessage(R.string.lost_and_found_deleted);
                } else
                    lostFoundCommentView.showMessage(R.string.lost_and_found_delete_fail);
            }
            lostFoundCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundCommentView.showMessage(R.string.error_network);
            lostFoundCommentView.hideLoading();
        }
    };

    final ApiCallback getLostItemApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof LostItem) {
                LostItem lostItem = (LostItem) object;
                lostFoundCommentView.showSuccessLostItem(lostItem);
            } else {
                lostFoundCommentView.showMessage(R.string.error_network);
            }
            lostFoundCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundCommentView.showMessage(R.string.error_network);
            lostFoundCommentView.hideLoading();
        }
    };

    public void createComment(int id, String comment) {
        if (FormValidatorUtil.validateStringIsEmpty(comment)) {
            lostFoundCommentView.showMessage(R.string.lost_and_found_content_check);
            return;
        }
        lostFoundCommentView.showLoading();
        this.id = id;
        lostAndFoundInteractor.createCommentDetail(id, comment, createCommentApiCallback);
    }

    public void updateComment(int id, int commentId, String comment) {
        if (FormValidatorUtil.validateStringIsEmpty(comment)) {
            lostFoundCommentView.showMessage(R.string.lost_and_found_content_check);
            return;
        }
        lostFoundCommentView.showLoading();
        this.id = id;
        lostAndFoundInteractor.editCommentDetail(id, commentId, comment, updateCommentApiCallback);
    }

    public void deleteComment(int id, int commentId) {
        lostFoundCommentView.showLoading();
        this.id = id;
        lostAndFoundInteractor.deleteCommentDetail(id, commentId, deleteCommentApiCallback);
    }

    public void getLostItem(int id) {
        lostFoundCommentView.showLoading();
        this.id = id;
        lostAndFoundInteractor.readLostAndFoundDetail(id, getLostItemApiCallback);
    }
}
