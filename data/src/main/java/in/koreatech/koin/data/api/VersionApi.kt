package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.VersionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface VersionApi {
    @GET("version/{type}")
    suspend fun getVersion(
        @Path("type") type: String
    ): VersionResponse
}
