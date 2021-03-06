package in.koreatech.koin.data.network.service;

import com.google.gson.JsonObject;


import in.koreatech.koin.constant.URLConstant;

import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.data.network.response.AuthResponse;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.response.UserInfoEditResponse;
import io.reactivex.Observable;
import retrofit2.http.*;

public interface UserService {
    @POST(URLConstant.USER.LOGIN)
    Observable<AuthResponse> getToken(@Body JsonObject body);

    @POST(URLConstant.USER.REGISTER)
    Observable<DefaultResponse> postRegister(@Body JsonObject body);

    @POST(URLConstant.USER.FINDPASSWORD)
    Observable<DefaultResponse> postPasswordReset(@Body JsonObject body);

    @GET(URLConstant.USER.ME)
    Observable<User> getUser(@Header("Authorization") String authHeader);

    @PUT(URLConstant.USER.ME)
    Observable<User> putUser(@Header("Authorization") String authHeader, @Body User user);

    @DELETE(URLConstant.USER.ME)
    Observable<DefaultResponse> deleteUser(@Header("Authorization") String authHeader);

    @GET(URLConstant.USER.CHECKNICKNAME + "/{nickname}")
    Observable<UserInfoEditResponse> checkNickName(@Path("nickname") String nickname);
}

