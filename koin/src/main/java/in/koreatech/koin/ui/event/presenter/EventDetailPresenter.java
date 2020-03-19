package in.koreatech.koin.ui.event.presenter;

public class EventDetailPresenter {
    private AdDetailContract.View adDetailView;
    private AdDetailInterator adDetailInterator;

    public AdDetailPresenter(AdDetailContract.View adDetailView, AdDetailInterator adDetailInterator) {
        this.adDetailView = adDetailView;
        this.adDetailInterator = adDetailInterator;
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            adDetailView.onAdDetailDataReceived((AdDetail) object);
            adDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            adDetailView.showMessage("이벤트 세부 내용을 불러오지 못했습니다.");
            adDetailView.hideLoading();
        }
    };

    private final ApiCallback deleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            adDetailView.onAdDetailDeleteCompleted(true);
            adDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            adDetailView.showMessage(throwable.getMessage());
            adDetailView.hideLoading();
        }
    };

    // Comment ApiCallback
    private final ApiCallback commentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            adDetailView.onAdDetailCommentReceived((Comment) object);
            adDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            adDetailView.showMessage(throwable.getMessage());
            adDetailView.hideLoading();
        }
    };

    private final ApiCallback deleteCommentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            adDetailView.onAdDetailCommentDeleted(true);
            adDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            adDetailView.showMessage(throwable.getMessage());
            adDetailView.hideLoading();
        }
    };

    @Override
    public void getAdDetailInfo(int id) {
        adDetailView.showLoading();
        adDetailInterator.readAdDetailList(id, apiCallback);
    }

    public void deleteAdDetail(int articleId) {
        adDetailView.showLoading();
        adDetailInterator.deleteAdDetail(articleId, deleteApiCallback);
    }

    public void createComment(int articleId, Comment comment) {
        adDetailView.showLoading();
        adDetailInterator.createAdDetailComment(articleId, comment, commentApiCallback);
    }

    public void updateComment(Comment comment) {
        adDetailView.showLoading();
        adDetailInterator.updateAdDetailComment(comment, commentApiCallback);
    }

    public void deleteComment(Comment comment) {
        adDetailView.showLoading();
        adDetailInterator.deleteAdDetailComment(comment.articleUid, comment.commentUid, deleteApiCallback);
    }
}