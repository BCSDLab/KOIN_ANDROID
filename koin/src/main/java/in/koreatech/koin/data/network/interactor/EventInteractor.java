package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Event;

public interface EventInteractor {
    // 전체 홍보글 조회
    void readEventList(int page, final ApiCallback apiCallback);

    // 홍보글 작성
    void createEvent(Event event, final ApiCallback apiCallback);

    // 마감된 전체 홍보글 조회
    void readClosedEventList(int page, final ApiCallback apiCallback);

    // 특정 홍보글 수정 권한 확인
    void updateGrantCheck(int articleId, final ApiCallback apiCallback);

    // 점주의 가게 조회
    void readMyShopList(final ApiCallback apiCallback);

    // 진행 중인 전체 홍보글 조회
    void readPendingEventList(int page, final ApiCallback apiCallback);

    // TODO: 점주의 진행 중인 홍보 조회
    void readMyPendingEventList();

    // 진행 중인 이벤트 랜덤 조회
    void readPendingRandomEvent(final ApiCallback apiCallback);

    // 특정 홍보글 조회
    void readEventDetail(int id, final ApiCallback apiCallback);

    // 홍보글 수정
    void updateEvent(int articleId, Event ad, final ApiCallback apiCallback);

    // 홍보글 삭제
    void deleteEvent(int articleId, final ApiCallback apiCallback);

    // 댓글 작성
    void createEventComment(int articleId, Comment comment, final ApiCallback apiCallback);

    // 댓글 수정
    void updateEventComment(Comment comment, final ApiCallback apiCallback);

    // 댓글 삭제
    void deleteEventComment(int articleId, int commentId, final ApiCallback apiCallback);
}
