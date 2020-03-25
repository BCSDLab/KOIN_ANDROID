package in.koreatech.koin.data.network.service;

import com.google.gson.JsonObject;

import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.response.DefaultResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static in.koreatech.koin.constant.URLConstant.EVENT.COMMENTS;
import static in.koreatech.koin.constant.URLConstant.EVENT.EVENT;
import static in.koreatech.koin.constant.URLConstant.EVENT.GRANTCHECK;
import static in.koreatech.koin.constant.URLConstant.EVENT.MYSHOP;
import static in.koreatech.koin.constant.URLConstant.EVENT.PENDING;

public interface EventService {
    // Event
    @GET(EVENT)
    Observable<Event> getEventList(@Query("page") int page);

    @GET(EVENT + "/{id}")
    Observable<Event> getEventDetail(@Path("id") int id);

    @POST(EVENT)
    Observable<Event> postEvent(@Header("Authorization") String authHeader, @Body JsonObject adDetail);

    @POST(GRANTCHECK)
    Observable<Event> postGrantCheck(@Header("Authorization") String authHeader, @Body JsonObject adDetail);

    @PUT(EVENT + "/{id}")
    Observable<Event> updateEvent(@Path("id") int articleId, @Header("Authorization") String authHeader, @Body JsonObject adDetail);

    @DELETE(EVENT + "/{id}")
    Observable<DefaultResponse> deleteEvent(@Path("id") int articleId, @Header("Authorization") String authHeader);

    @GET(MYSHOP)
    Observable<Event> getMyShopList(@Header("Authorization") String authHeader);

    @GET(EVENT + "/closed")
    Observable<Event> getClosedEventList(@Query("page") int page);

    @GET(PENDING)
    Observable<Event> getPendingEventList(@Query("page") int page);

    @GET(PENDING + "/my")
    Observable<Event> getMyPendingEventList(@Path("Authorization") String authHeader, @Query("page") int page);

    @GET(PENDING + "/random")
    Observable<Event> getRandomEventList();

    // Comment
    @POST(EVENT + "/{articleId}/" + COMMENTS)
    Observable<Comment> postEventComment(@Path("articleId") int articleId, @Header("Authorization") String authHeader, @Body JsonObject content);

    @PUT(EVENT + "/{articleId}/" + COMMENTS + "/{commentId}")
    Observable<Comment> updateEventComment(@Path("articleId") int articleId, @Path("commentId") int commentId, @Header("Authorization") String authHeader, @Body JsonObject content);

    @DELETE(EVENT + "/{articleId}/" + COMMENTS + "/{commentId}")
    Observable<DefaultResponse> deleteEventComment(@Path("articleId") int articleId, @Path("commentId") int commentId, @Header("Authorization") String authHeader);
}
