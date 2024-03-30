package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.PreSignedUrlApi
import okhttp3.RequestBody
import javax.inject.Inject

class PreSignedUrlRemoteDataSource @Inject constructor(
    private val preSignedUrlApi: PreSignedUrlApi
) {
    suspend fun putPreSignedUrl(url: String, file: RequestBody): Unit = preSignedUrlApi.uploadFile(url, file)
}