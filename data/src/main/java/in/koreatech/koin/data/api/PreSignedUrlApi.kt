package `in`.koreatech.koin.data.api

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Url

interface PreSignedUrlApi {
    @PUT
    suspend fun uploadFile(
        @Url url: String,
        @Body file: RequestBody
    )
}