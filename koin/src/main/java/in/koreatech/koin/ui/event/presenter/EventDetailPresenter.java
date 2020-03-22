package in.koreatech.koin.ui.event.presenter;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.interactor.EventInteractor;

public class EventDetailPresenter {
    private EventDetailContract.View eventDetailView;
    private EventInteractor eventInteractor;

    public EventDetailPresenter(EventDetailContract.View eventDetailView, EventInteractor eventInteractor) {
        this.eventDetailView = eventDetailView;
        this.eventInteractor = eventInteractor;
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            eventDetailView.onEventDataReceived((Event) object);
            eventDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventDetailView.showMessage("이벤트 세부 내용을 불러오지 못했습니다.");
            eventDetailView.hideLoading();
        }
    };

    private final ApiCallback deleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            eventDetailView.onEventDeleteCompleted(true);
            eventDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventDetailView.showMessage(throwable.getMessage());
            eventDetailView.hideLoading();
        }
    };

    // Comment ApiCallback
    private final ApiCallback commentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            eventDetailView.onEventCommentReceived((Comment) object);
            eventDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventDetailView.showMessage(throwable.getMessage());
            eventDetailView.hideLoading();
        }
    };

    private final ApiCallback deleteCommentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            eventDetailView.onEventCommentDeleted(true);
            eventDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            eventDetailView.showMessage(throwable.getMessage());
            eventDetailView.hideLoading();
        }
    };

    public void getEventDetail(int id) {
        eventDetailView.showLoading();
        eventInteractor.readEventList(id, apiCallback);
    }

    public void deleteEvent(int articleId) {
        eventDetailView.showLoading();
        eventInteractor.deleteEvent(articleId, deleteApiCallback);
    }

    public void createComment(int articleId, Comment comment) {
        eventDetailView.showLoading();
        eventInteractor.createEventComment(articleId, comment, commentApiCallback);
    }

    public void updateComment(Comment comment) {
        eventDetailView.showLoading();
        eventInteractor.updateEventComment(comment, commentApiCallback);
    }

    public void deleteComment(Comment comment) {
        eventDetailView.showLoading();
        eventInteractor.deleteEventComment(comment.getArticleUid(), comment.getCommentUid(), deleteApiCallback);
    }
}