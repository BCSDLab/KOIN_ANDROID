package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Event;

public interface EventInteractor {
    void readEventList(final ApiCallback apiCallback);

    void updateGrantCheck(int articleId, final ApiCallback apiCallback);

    void readEventDetail(int id, final ApiCallback apiCallback);

    void createEvent(Event ad, final ApiCallback apiCallback);

    void updateEvent(int articleId, Event ad, final ApiCallback apiCallback);

    void deleteEvent(int articleId, final ApiCallback apiCallback);

    // Event Comment
    void createEventComment(int articleId, Comment comment, final ApiCallback apiCallback);

    void updateEventComment(Comment comment, final ApiCallback apiCallback);

    void deleteEventComment(int articleId, int commentId, final ApiCallback apiCallback);
}
