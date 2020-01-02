package in.koreatech.koin.data.network.service;

import com.google.gson.JsonObject;

import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.entity.MarketItem;
import io.reactivex.Observable;
import in.koreatech.koin.data.network.response.MarketPageResponse;
import okhttp3.MultipartBody;
import retrofit2.http.*;

import static in.koreatech.koin.core.constant.URLConstant.MARKET.ITEMS;


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
    Observable<Item> postGrantedCheck(@Header("Authorization") String authHeader, @Body JsonObject articleUid);

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