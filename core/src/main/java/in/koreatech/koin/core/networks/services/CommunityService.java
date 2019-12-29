package in.koreatech.koin.core.networks.services;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.responses.ArticlePageResponse;
import in.koreatech.koin.core.networks.entity.Boards;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.responses.DefaultResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ARTICLES;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.BOARDS;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.COMMENTS;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.GRANTCHECK;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.TEMPBOARD;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public interface
CommunityService {
    //Get Boards Lits API
    @GET(BOARDS)
    Observable<ArrayList<Boards>> getBoardsList();

    //Get Specific Board API
    @GET(BOARDS + "/{id}")
    Observable<Boards> getBoardInfo(@Header("Authorization") String authHeader, @Path("id") String id);

    @GET(BOARDS + "/{id}")
    Observable<Boards> getBoardInfo(@Path("id") String id);

    //Get Article List API
    @GET(ARTICLES)
    Observable<ArticlePageResponse> getArticleList(@Query("boardId") int boardId, @Query("page") int page);

    @GET(TEMPBOARD + "/articles")
    Observable<ArticlePageResponse> getAnonymousArticleList(@Query("page") int page);

    //Create Article API
    @POST(ARTICLES)
    Observable<Article> postArticle(@Header("Authorization") String authHeader, @Body JsonObject article);

    @GET(TEMPBOARD + "/articles/{id}")
    Observable<Article> getAnonymousArticle(@Path("id") int uid);

    //Get Specific Article API
    @GET(ARTICLES + "/{id}")
    Observable<Article> getArticle(@Path("id") String uid);

    //Get Specific Article API
    @GET(ARTICLES + "/{id}")
    Observable<Article> getArticle(@Header("Authorization") String authHeader, @Path("id") String uid);

    //Update Specific Article API
    @PUT(ARTICLES + "/{id}")
    Observable<Article> putArticle(@Path("id") String uid, @Header("Authorization") String authHeader, @Body JsonObject article);

    //Delete Specific Article API
    @DELETE(ARTICLES + "/{id}")
    Observable<DefaultResponse> deleteArticle(@Path("id") String uid, @Header("Authorization") String authHeader);

    @POST(GRANTCHECK)
    Observable<Article> postGrantCheck(@Header("Authorization") String authHeader, @Body JsonObject articleUid);

    //Create Comment API
    @POST(ARTICLES + "/{articleId}/" + COMMENTS)
    Observable<Comment> postComment(@Path("articleId") String uid, @Header("Authorization") String authHeader, @Body JsonObject content);

    @GET(ARTICLES + "/{articleId}/" + COMMENTS + "/{id}")
    Observable<Comment> getComment(@Path("articleId") String uid, @Path("id") String sub, @Header("Authorization") String authHeader);

    @PUT(ARTICLES + "/{articleId}/" + COMMENTS + "/{id}")
    Observable<Comment> putComment(@Path("articleId") String uid, @Path("id") String sub, @Header("Authorization") String authHeader, @Body JsonObject commentContent);

    @POST(TEMPBOARD + "/" + ARTICLES)
    Observable<Article> postAnonymousArticle(@Body JsonObject content);

    @PUT(TEMPBOARD + "/" + ARTICLES + "/{id}/")
    Observable<Article> putAnoymousArticle(@Path("id") String uid, @Body JsonObject commentContent);

    @DELETE(TEMPBOARD + "/" + ARTICLES + "/{id}/")
    Observable<DefaultResponse> deleteAnoymousArticle(@Path("id") String uid, @Header("password") String password);


    @DELETE(ARTICLES + "/{articleId}/" + COMMENTS + "/{id}")
    Observable<DefaultResponse> deleteComment(@Path("articleId") String uid, @Path("id") String sub, @Header("Authorization") String authHeader);

    @POST(TEMPBOARD + "/" + ARTICLES + "/{articleId}/" + COMMENTS)
    Observable<Comment> postAnonymousComment(@Path("articleId") String uid, @Body JsonObject content);

    @GET(TEMPBOARD + "/" + ARTICLES + "/{articleId}/" + COMMENTS + "/{id}")
    Observable<Comment> getAnonymousComment(@Path("articleId") String uid, @Path("id") String sub);

    @PUT(TEMPBOARD + "/" + ARTICLES + "/{articleId}/" + COMMENTS + "/{id}")
    Observable<Comment> putAnonymousComment(@Path("articleId") String uid, @Path("id") String sub, @Body JsonObject commentContent);

    @DELETE(TEMPBOARD + "/" + ARTICLES + "/{articleId}/" + COMMENTS + "/{id}")
    Observable<DefaultResponse> deleteAnonymousComment(@Path("articleId") String uid, @Path("id") String sub, @Header("password") String password);

    @POST(TEMPBOARD + "/" + ARTICLES + "/grant/check")
    Observable<Article> postAnonymousGrantCheck(@Body JsonObject content);

    @POST(TEMPBOARD + "/comments/grant/check")
    Observable<Article> postAnonymousCommentGrantCheck(@Body JsonObject commentContent);

//    /* 문의 사항 */
//    //Get Questions Article List API
//    @GET(BOARDS)
//    Observable<BoardPage> getQuestionsBoardList(@Query("tag") String tag, @Query("page") int pageNum, @Query("userUid") String uid);
//
//    //Update Questions Article Solved
//    @PUT(BOARDS + "/{uid}" + SOLVE)
//    Observable<Article> putQuestionsBoardSolved(@Header("x-access-token") String token, @Path("uid") String uid);
//
//    //Update Questions Article Unsolved
//    @PUT(BOARDS + "/{uid}" + UNSOLVE)
//    Observable<Article> putQuestionsBoardUnsolved(@Header("x-access-token") String token, @Path("uid") String uid);
}
