package in.koreatech.koin.core.networks.services;

import com.google.gson.JsonObject;

import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.entity.MarketItem;
import in.koreatech.koin.core.networks.responses.DefaultResponse;
import io.reactivex.Observable;
import in.koreatech.koin.core.networks.responses.MarketPageResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.*;

import java.io.File;
import java.util.jar.JarEntry;

import static in.koreatech.koin.core.constants.URLConstant.MARKET.ITEMS;


public interface MarketService {
    @GET(ITEMS)
    Observable<MarketPageResponse> getMarketList(@Query("type") int type, @Query("page") int page);

    @POST(ITEMS)
    Observable<Item> postMarketItem(@Header("Authorization") String authHeader, @Body MarketItem marketItem);

    @DELETE(ITEMS + "/{id}")
    Observable<Item> deleteMarketItem(@Path("id") int id, @Header("Authorization") String authHeader);

    @GET(ITEMS + "/{id}")
    Observable<Item> getMarketDetail(@Header("Authorization") String authHeader, @Path("id") int id);

    @GET(ITEMS + "/{id}")
    Observable<Item> getMarketDetail(@Path("id") int id);

    @POST(ITEMS + "/grant/check")
    Observable<DefaultResponse> postGrantedCheck(@Header("Authorization") String authHeader, @Body JsonObject articleUid);

    @POST(ITEMS + "/{itemId}" + "/comments")
    Observable<Comment> postComment(@Path("itemId") int id, @Header("Authorization") String authHeader, @Body JsonObject content);

    @DELETE(ITEMS + "/{itemId}" + "/comments" + "/{commentId}")
    Observable<Comment> deleteComment(@Path("itemId") int itemId, @Path("commentId") int commentId, @Header("Authorization") String authHeader);

    @PUT(ITEMS + "/{itemId}/comments/{commentId}")
    Observable<Comment> putEditComment(@Path("itemId") int itemId, @Path("commentId") int commentId, @Header("Authorization") String authHeader, @Body JsonObject content);

    @PUT(ITEMS + "/{id}")
    Observable<Item> putEditContent(@Path("id") int id, @Header("Authorization") String authHeader, @Body MarketItem marketItem);

    @Multipart
    @POST(ITEMS + "/image/thumbnail/upload")
    Observable<Item> postImage(@Header("Authorization") String authHeader, @Part MultipartBody.Part file);


}