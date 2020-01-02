package in.koreatech.koin.data.network.service;

import com.google.gson.JsonObject;

import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.response.GrantCheckResponse;
import in.koreatech.koin.data.network.response.LostAndFoundPageResponse;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static in.koreatech.koin.core.constant.URLConstant.LOSTANDFOUND.GRANTCHECK;
import static in.koreatech.koin.core.constant.URLConstant.LOSTANDFOUND.LOSTITEMS;

public interface LostAndFoundService {
    @GET(LOSTITEMS)
    Observable<LostAndFoundPageResponse> getLostItemList(@Query("limit") int limit, @Query("page") int page);

    @POST(LOSTITEMS)
    Observable<LostItem> postLostItem(@Header("Authorization") String authHeader, @Body LostItem lostItem);

    @DELETE(LOSTITEMS + "/{id}")
    Observable<DefaultResponse> deleteLostItem(@Path("id") int id, @Header("Authorization") String authHeader);

    @GET(LOSTITEMS + "/{id}")
    Observable<LostItem> getLostItemDetail(@Path("id") int id, @Header("Authorization") String authHeader);

    @GET(LOSTITEMS + "/{id}")
    Observable<LostItem> getLostItemDetail(@Path("id") int id);

    @POST(GRANTCHECK)
    Observable<GrantCheckResponse> postLostItemGrantedCheck(@Header("Authorization") String authHeader, @Body JsonObject articleUid);

    @POST(LOSTITEMS + "/{lostItemId}" + "/comments")
    Observable<Comment> postLostItemComment(@Path("lostItemId") int lostItemId, @Header("Authorization") String authHeader, @Body JsonObject content);

    @DELETE(LOSTITEMS + "/{lostItemId}" + "/comments" + "/{commentId}")
    Observable<DefaultResponse> deleteLostItemComment(@Path("lostItemId") int lostItemId, @Path("commentId") int commentId, @Header("Authorization") String authHeader);

    @PUT(LOSTITEMS + "/{lostItemId}/comments/{commentId}")
    Observable<Comment> putLostItemComment(@Path("lostItemId") int lostItemId, @Path("commentId") int commentId, @Header("Authorization") String authHeader, @Body JsonObject content);

    @PUT(LOSTITEMS + "/{id}")
    Observable<LostItem> putLostAndFoundContent(@Path("id") int id, @Header("Authorization") String authHeader, @Body LostItem lostItem);

    @Multipart
    @POST(LOSTITEMS + "/image/thumbnail/upload")
    Observable<LostItem> postLostItemImage(@Header("Authorization") String authHeader, @Part MultipartBody.Part file);

}
