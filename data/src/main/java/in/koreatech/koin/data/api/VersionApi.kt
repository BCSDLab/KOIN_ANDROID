package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.VersionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.invoke.MethodHandles.constant

interface VersionApi {
    @GET(URLConstant.VERSION + "/{type}")
    suspend fun getVersion(@Path("type") type: String): VersionResponse
}