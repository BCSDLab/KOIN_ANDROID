package `in`.koreatech.koin.data.api.refresh

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.user.RefreshTokenRequest
import `in`.koreatech.koin.data.response.TokenRsponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRefreshApi {

    @POST(URLConstant.USER.REFRESH)
    suspend fun refreshAccessToken(@Body refreshTokenRequest: RefreshTokenRequest): TokenRsponse
}