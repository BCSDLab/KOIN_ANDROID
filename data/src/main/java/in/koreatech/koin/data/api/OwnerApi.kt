package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.owner.OwnerResponse
import retrofit2.Response
import retrofit2.http.GET

interface OwnerApi {
    @GET(URLConstant.OWNER)
    suspend fun getOwnerInfo(): Response<OwnerResponse>
}