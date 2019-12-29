package in.koreatech.koin.service_lostfound.presenters;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.LostItem;
import in.koreatech.koin.core.networks.interactors.LostAndFoundInteractor;
import in.koreatech.koin.core.networks.interactors.LostAndFoundRestInteractor;
import in.koreatech.koin.core.networks.responses.DefaultResponse;
import in.koreatech.koin.service_lostfound.Contracts.LostFoundCommentContract;

/**
 * @author yunjaeaNa
 * @since 2019-09-08
 *
 * 댓글 Presenter
 */
public class LostFoundCommentPresenter implements LostFoundCommentContract.Presenter {
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
            lostFoundCommentView.showMessage("생성되었습니다.");
            getLostItem(id);
            lostFoundCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundCommentView.showMessage("네트워크를 확인해주세요");
            lostFoundCommentView.hideLoading();
        }
    };

    final ApiCallback updateCommentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            lostFoundCommentView.showMessage("수정되었습니다.");
            getLostItem(id);
            lostFoundCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundCommentView.showMessage("네트워크를 확인해주세요");
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
                    lostFoundCommentView.showMessage("삭제되었습니다.");
                } else
                    lostFoundCommentView.showMessage("삭제에 실패했습니다.");
            }
            lostFoundCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundCommentView.showMessage("네트워크를 확인해주세요");
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
                lostFoundCommentView.showMessage("네트워크를 확인해주세요");
            }
            lostFoundCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundCommentView.showMessage("네트워크를 확인해주세요");
            lostFoundCommentView.hideLoading();
        }
    };

    @Override
    public void createComment(int id, String comment) {
        lostFoundCommentView.showLoading();
        this.id = id;
        lostAndFoundInteractor.createCommentDetail(id, comment, createCommentApiCallback);
    }

    @Override
    public void updateComment(int id, int commentId, String comment) {
        lostFoundCommentView.showLoading();
        this.id = id;
        lostAndFoundInteractor.editCommentDetail(id, commentId, comment, updateCommentApiCallback);
    }

    @Override
    public void deleteComment(int id, int commentId) {
        lostFoundCommentView.showLoading();
        this.id = id;
        lostAndFoundInteractor.deleteCommentDetail(id, commentId, deleteCommentApiCallback);
    }

    @Override
    public void getLostItem(int id) {
        lostFoundCommentView.showLoading();
        this.id = id;
        lostAndFoundInteractor.readLostAndFoundDetail(id, getLostItemApiCallback);
    }
}
