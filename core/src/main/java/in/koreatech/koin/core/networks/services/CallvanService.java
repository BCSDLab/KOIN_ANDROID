package in.koreatech.koin.core.networks.services;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.koreatech.koin.core.networks.entity.CallvanRoom;
import in.koreatech.koin.core.networks.entity.Company;
import in.koreatech.koin.core.networks.responses.DefaultResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static in.koreatech.koin.core.constants.URLConstant.CALLVANS.COMPANIES;
import static in.koreatech.koin.core.constants.URLConstant.CALLVANS.PARTICIPANT;
import static in.koreatech.koin.core.constants.URLConstant.CALLVANS.ROOMS;

/**
 * Created by hyerim on 2018. 6. 17....
 */
public interface CallvanService {
    /**
     * Get Callvan Company List API
     * @param authHeader 사용자 token
     * @return api 결과를 담은 콜밴 회사 array list
     */
    @GET(COMPANIES)
    Observable<ArrayList<Company>> getCompanyList(@Header("Authorization") String authHeader);

    /**
     * Get Callvan Company API
     * @param uid 콜밴 회사 id
     * @param authHeader 사용자 token
     * @return 콜밴 회사 정보를 담은 company
     */
    @GET(COMPANIES + "/{id}")
    Observable<Company> getCompany(@Path("id") int uid, @Header("Authorization") String authHeader);

    /**
     * Get Callvan room List API
     * @param authHeader 사용자 token
     * @return api 결과를 담은 콜밴 방 array list
     */
    @GET(ROOMS)
    Observable<ArrayList<CallvanRoom>> getCallvanRoomsList(@Header("Authorization") String authHeader);

    /**
     * Get Callvan room API
     * @param uid 콜밴 방 id
     * @param authHeader 사용자 token
     * @return 콜밴 방 정보를 담은 CallvanRoom
     */
    @GET(ROOMS + "/{id}")
    Observable<CallvanRoom> getCallvanRoom(@Path("id") int uid, @Header("Authorization") String authHeader);

    /**
     * Create Callvan room API
     * @param authHeader 사용자 token
     * @param callvanRoom 생성할 콜밴 방 정보를 담은 파라미터
     * @return
     */
    @POST(ROOMS)
    Observable<CallvanRoom> postCallvanRoom(@Header("Authorization") String authHeader, @Body CallvanRoom callvanRoom);

    /**
     * Update Callvan Room API
     * @param authHeader 사용자 token
     * @param callvanRoom 업데이트 할 콜밴 방 정보를 담은 파라미터
     * @return
     */
    @PUT(ROOMS)
    Observable<CallvanRoom> putCallvanRoom(@Header("Authorization") String authHeader, @Body CallvanRoom callvanRoom);

    /**
     * Delete Specific Callvan ROOM API
     * @param uid 지워야할 콜밴 방 id
     * @param authHeader 사용자 토큰
     * @return api 성공여부 결과를 담은 defaultResponse
     */
    @DELETE(ROOMS + "/{id}")
    Observable<DefaultResponse> deleteCallvanRoom(@Path("id") int uid, @Header("Authorization") String authHeader);

    /**
     * join room API
     * @param authHeader 사용자 토큰
     * @param roomUid 사용자가 참여할 콜밴 방 id
     * @return api 성공여부 결과를 담은 defaultResponse
     */
    @POST(PARTICIPANT)
    Observable<DefaultResponse> postParticipant(@Header("Authorization") String authHeader, @Body JsonObject roomUid);

    /**
     * out room API
     * @param authHeader 사용자 토큰
     * @param roomUid 사용자가 퇴장할 콜밴 방 id
     * @return api 성공여부 결과를 담은 defaultResponse
     */
    @HTTP(method = "DELETE", path = PARTICIPANT, hasBody = true)
    Observable<CallvanRoom> deleteParticipant(@Header("Authorization") String authHeader, @Body JsonObject roomUid);


}
