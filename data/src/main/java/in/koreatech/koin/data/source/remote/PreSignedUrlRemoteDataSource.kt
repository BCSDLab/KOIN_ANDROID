package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.PreSignedUrlApi
import javax.inject.Inject
import okhttp3.RequestBody

class PreSignedUrlRemoteDataSource @Inject constructor(
    private val preSignedUrlApi: PreSignedUrlApi
) {
    suspend fun putPreSignedUrl(
        url: String,
        file: RequestBody
    ): Unit = preSignedUrlApi.uploadFile(url, file)
}
