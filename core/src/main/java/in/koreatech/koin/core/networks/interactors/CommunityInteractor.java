package in.koreatech.koin.core.networks.interactors;

import com.google.android.gms.common.api.Api;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.entity.Comment;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public interface CommunityInteractor {
    //Boards
    void readBoardsList(final ApiCallback apiCallback);

    void readBoard(String uid, final ApiCallback apiCallback);

    //Article
    void readArticleList(int boardUid, int pageNum, final ApiCallback apiCallback);

    void readAnonymousArticleList(int pageNum, final ApiCallback apiCallback);

    void createArticle(Article article, final ApiCallback apiCallback);

    void createAnonymousArticle(String title, String content, String nickname, String password, final ApiCallback apiCallback);

    void readArticle(int articleUid, final ApiCallback apiCallback);

    void readAnonymousArticle(int articleUid, final ApiCallback apiCallback);

    void updateArticle(Article article, final ApiCallback apiCallback);

    void updateAnonymousArticle(int articleUid, String title, String content, String password, final ApiCallback apiCallback);

    void deleteArticle(int articleUid, final ApiCallback apiCallback);

    void deleteAnonymousArticle(int articleUid, String password, final ApiCallback apiCallback);

    //Comment
    void createComment(int articleUid, String content, final ApiCallback apiCallback);

    void readComment(int articleUid, int commentUid, final ApiCallback apiCallback);

    void updateComment(Comment comment, final ApiCallback apiCallback);

    void deleteComment(int articleUid, int commentUid, final ApiCallback apiCallback);

    void createAnonymousComment(int articleUid, String content, String nickname, String password, final ApiCallback apiCallback);

    void readAnonymousComment(int articleUid, int commentUid, final ApiCallback apiCallback);

    void updateAnonymousComment(Comment comment, final ApiCallback apiCallback);

    void deleteAnonymousComment(int articleUid, int commentUid, String password, final ApiCallback apiCallback);

    //GrantCheck
    void updateGrantCheck(int articleUid, final ApiCallback apiCallback);

    void updateAnonymousGrantCheck(int articleUid, String password, final ApiCallback apiCallback);

    void updateAnonymousCommentGrantCheck(int commentUid, String password, final ApiCallback apiCallback);
}
