package in.koreatech.koin.data.network.service;

import com.google.gson.JsonObject;

import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.response.DefaultResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static in.koreatech.koin.constant.URLConstant.EVENT.COMMENTS;
import static in.koreatech.koin.constant.URLConstant.EVENT.EVENT;
import static in.koreatech.koin.constant.URLConstant.EVENT.GRANTCHECK;

public interface EventService {
    // Event
    @GET(EVENT)
    Observable<Event> getAdList();

    @GET(EVENT + "/{id}")
    Observable<Event> getEventList(@Path("id") int id);
    
    // Event
    @POST(EVENT)
    Observable<Event> postEvent(@Header("Authorization") String authHeader, @Body JsonObject adDetail);

    @POST(GRANTCHECK)
    Observable<Event> postGrantCheck(@Header("Authorization") String authHeader, @Body JsonObject adDetail);

    @PUT(EVENT + "/{id}")
    Observable<Event> updateEvent(@Path("id") int articleId, @Header("Authorization") String authHeader, @Body JsonObject adDetail);

    @DELETE(EVENT + "/{id}")
    Observable<DefaultResponse> deleteEvent(@Path("id") int articleId, @Header("Authorization") String authHeader);

    // Event Comment
    @POST(EVENT + "/{articleId}/" + COMMENTS)
    Observable<Comment> postEventComment(@Path("articleId") int articleId, @Header("Authorization") String authHeader, @Body JsonObject content);

    @PUT(EVENT + "/{articleId}/" + COMMENTS + "/{commentId}")
    Observable<Comment> updateEventComment(@Path("articleId") int articleId, @Path("commentId") int commentId, @Header("Authorization") String authHeader, @Body JsonObject content);

    @DELETE(EVENT + "/{articleId}/" + COMMENTS + "/{commentId}")
    Observable<DefaultResponse> deleteEventComment(@Path("articleId") int articleId, @Path("commentId") int commentId, @Header("Authorization") String authHeader);
}
