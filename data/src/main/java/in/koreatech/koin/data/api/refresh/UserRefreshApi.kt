package `in`.koreatech.koin.data.api.refresh

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.user.RefreshTokenRequest
import `in`.koreatech.koin.data.response.AccessTokenResponse
import `in`.koreatech.koin.data.response.user.CheckNicknameResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserRefreshApi {

    @POST(URLConstant.USER.REFRESH)
    suspend fun refreshAccessToken(@Body refreshTokenRequest: RefreshTokenRequest): AccessTokenResponse
}